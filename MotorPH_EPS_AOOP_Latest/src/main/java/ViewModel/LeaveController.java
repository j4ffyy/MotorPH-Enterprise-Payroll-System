/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ViewModel;


/**
 *
 * @author dashcodes
 */

import Model.Leave;
import Repository.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import javax.swing.table.TableRowSorter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

    /**
     * Controller class for managing leave-related operations in the MVVM architecture.
     * Handles business logic between the View (UI) and Model (data) for leave management.
     */
    public class LeaveController {
        private static final String PENDING_STATUS = "Pending";
        private final DBQueries dbQueries;
        
        private String loggedInUsername;
        private int loggedInEid;

        /**
         * Default constructor initializes database connection and query handler.
         * The connection is no longer stored as an instance variable to avoid
         * issues with connection management (e.g., stale connections, concurrency).
         */
        public LeaveController() {
            this.dbQueries = new DBQueries();
        }
    
    /**
     * Sets the currently logged-in user details.
     * @param username The username of the logged-in employee
     * @param eid The employee ID of the logged-in employee
     */
    public void setLoggedInUser(String username, int eid) {
        this.loggedInUsername = username;
        this.loggedInEid = eid;
    }

    /**
     * Applies for a new leave by inserting the leave details into the database.
     * @param leave The Leave object containing leave details.
     * @return true if the leave application was successful, false otherwise.
     */
    public boolean addLeave(JDateChooser dateFrom, JDateChooser dateTo, JComboBox<String> reasonCombo) {
        // --- Data Extraction and Validation (UI-level validation should ideally precede this call) ---
        java.util.Date utilDateFrom = dateFrom.getDate();
        java.util.Date utilDateTo = dateTo.getDate();

        // Basic validation - though the UI code already has this
        if (utilDateFrom == null || utilDateTo == null || reasonCombo.getSelectedIndex() == 0) {
            System.err.println("Validation failed: Dates or Reason not selected.");
            return false;
        }

        // Convert java.util.Date to java.sql.Date for database compatibility
        Date sqlDateFrom = new Date(utilDateFrom.getTime());
        Date sqlDateTo = new Date(utilDateTo.getTime());
        Date dateFiled = new Date(System.currentTimeMillis()); // Date when the application is filed

        String reason = (String) reasonCombo.getSelectedItem();
        String status = "Pending"; // Initial status for a new leave application

        // --- Retrieve Employee ID (Crucial for the APPLY_LEAVE query) ---
        // You MUST replace '0' with the actual EID of the logged-in employee.
        // This EID would typically come from a user session, a global EmployeeDetails object,
        // or passed to the LeaveController's constructor.
        int employeeId = this.loggedInEid; // Use the logged-in employee's ID

        // Generate Leave_ID
        String leaveId = generateNextLeaveId();

        // --- Database Interaction ---
        String applyLeaveQuery = dbQueries.getQuery("APPLY_LEAVE"); 

        try (Connection connection = DataSource.getInstance().getConnection();
                PreparedStatement pstmt = connection.prepareStatement(applyLeaveQuery)) {
            // Set the parameters for the prepared statement based on the 'applyLeave' query
            pstmt.setString(1, leaveId);     // Leave_ID
            pstmt.setInt(2, employeeId);     // EID
            pstmt.setDate(3, dateFiled);     // DateFiled
            pstmt.setDate(4, sqlDateFrom);   // DateFrom
            pstmt.setDate(5, sqlDateTo);     // DateTo
            pstmt.setString(6, reason);      // Reason
            pstmt.setString(7, status);      // Status

            int rowsAffected = pstmt.executeUpdate(); // Execute the INSERT statement
            return rowsAffected > 0; // Return true if the insert was successful
        } catch (SQLException e) {
            System.err.println("Error submitting leave application: " + e.getMessage());
            e.printStackTrace(); // Log the full stack trace for debugging
            return false; // Return false if an error occurred
        }
    }

    private String generateNextLeaveId() {
        String maxLeaveId = null;
        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(dbQueries.getMaxLeaveId);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                maxLeaveId = rs.getString("maxId");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching max Leave_ID: " + e.getMessage());
            e.printStackTrace();
        }

        if (maxLeaveId == null || maxLeaveId.isEmpty()) {
            return "L1001"; // Default starting ID
        } else {
            // Extract the numeric part, increment, and format back
            int numericPart = Integer.parseInt(maxLeaveId.substring(1));
            numericPart++;
            return "L" + String.format("%04d", numericPart);
        }
    }

    /**
     * Updates the status of a leave application.
     * @param leaveId The ID of the leave to update.
     * @param status The new status (e.g., "Approved", "Rejected").
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateLeaveStatus(String leaveId, String status) {
        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(dbQueries.updateLeaveStatus)) {
            ps.setString(1, status);
            ps.setString(2, leaveId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleDatabaseError(e);
            return false;
        }
    }
    
    /**
     * Loads all leave applications from the database into a table model.
     * This method is typically for admin view.
     * @param tableModel The DefaultTableModel to populate with leave data.
     */
    public void loadAllLeavesIntoTable(DefaultTableModel tableModel) {
        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(dbQueries.getAllLeaves);
             ResultSet rs = stmt.executeQuery()) {
            
            tableModel.setRowCount(0); // Clear existing data
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"),
                    rs.getInt("EID"),
                    rs.getString("First_Name") + " " + rs.getString("Last_Name"), 
                    rs.getDate("Date_Filed"),
                    rs.getDate("Date_From"),
                    rs.getDate("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }
    
    
    
    /**
     * Loads leave applications for a specific employee into a table model.
     * This method is typically for employee's personal view.
     * @param tableModel The DefaultTableModel to populate.
     * @param eid The EID of the employee whose leaves are to be loaded.
     */
    public void loadLeavesByEidIntoTable(DefaultTableModel tableModel, int eid) {
        try (Connection connection = DataSource.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(dbQueries.getLeaveByEid)) { 
            stmt.setInt(1, eid);
            ResultSet rs = stmt.executeQuery();
            
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"), 
                    rs.getInt("EID"),         
                    rs.getDate("Date_Filed"), 
                    rs.getDate("Date_From"),  
                    rs.getDate("Date_To"),    
                    rs.getString("Reason_For_Leave"), 
                    rs.getString("Leave_Status")    
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }
    
    /**
     * Retrieves all leave applications from the database and returns them as a DefaultTableModel.
     * This method is suitable for populating a JTable directly.
     * @return A DefaultTableModel containing all leave data.
     */
    public DefaultTableModel getAllLeavesTableModel() {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };
        // Define columns for the table model
        tableModel.setColumnIdentifiers(new Object[]{"Leave ID", "EID", "Employee Name", "Date Filed", "Date From", "Date To", "Reason", "Status"});

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getAllLeaves);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"),
                    rs.getInt("EID"),
                    rs.getString("First_Name") + " " + rs.getString("Last_Name"),
                    rs.getDate("Date_Filed"),
                    rs.getDate("Date_From"),
                    rs.getDate("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
        return tableModel;
    }

    /**
     * Retrieves leave applications for a specific employee and returns them as a DefaultTableModel.
     * This method is suitable for populating a JTable directly.
     * @param eid The EID of the employee whose leaves are to be loaded.
     * @return A DefaultTableModel containing the employee's leave data.
     */
    public DefaultTableModel getEmployeeLeavesTableModel(int eid) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };
        // Define columns for the table model (ensure these match your database schema and desired display)
        tableModel.setColumnIdentifiers(new Object[]{"Leave ID", "EID", "Date Filed", "Date From", "Date To", "Reason", "Status"});
        
        searchLeavesByEid(eid, tableModel); // Use the existing method to populate the model
        return tableModel;
    }

    public Object[] getLeaveById(String leaveId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getQuery("GET_LEAVE_BY_ID"))) {
            stmt.setString(1, leaveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Object[]{
                    rs.getString("Leave_ID"),
                    rs.getInt("EID"),
                    rs.getDate("Date_Filed"),
                    rs.getDate("Date_From"),
                    rs.getDate("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                };
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
        return null;
    }

    public void searchLeavesByEid(int eid, DefaultTableModel tableModel) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getLeaveByEid)) {
            stmt.setInt(1, eid);
            ResultSet rs = stmt.executeQuery();
            
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("Leave_ID"), 
                    rs.getInt("EID"),         
                    rs.getDate("Date_Filed"), 
                    rs.getDate("Date_From"),  
                    rs.getDate("Date_To"),    
                    rs.getString("Reason_For_Leave"), 
                    rs.getString("Leave_Status")    
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }
    
    // Get current date formatted as string
    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        return dateFormat.format(new java.util.Date());
    }
    
    public boolean deleteLeave(String leaveId) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.deleteLeave)) {
            ps.setString(1, leaveId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            handleDatabaseError(e);
            return false;
        }
    }
    
    private boolean validateLeaveForm(JDateChooser dateFrom, JDateChooser dateTo, JComboBox<String> reasonCombo) {
        return dateFrom.getDate() != null && 
               dateTo.getDate() != null && 
               reasonCombo.getSelectedIndex() != 0;
    }
    
    private void handleDatabaseError(SQLException e) {
     System.err.println("Database Error: " + e.getMessage());
     e.printStackTrace(); 
     JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }   

    /**
     * Clears the leave application form fields.
     * @param dateFrom The JDateChooser for the start date.
     * @param dateTo The JDateChooser for the end date.
     * @param reasonCombo The JComboBox for the reason for leave.
     */
    public void clearLeaveForm(JDateChooser dateFrom, JDateChooser dateTo, JComboBox<String> reasonCombo) {
        dateFrom.setDate(null);
        dateTo.setDate(null);
        reasonCombo.setSelectedIndex(0); // Assuming 0 is the "< Select one >" or default option
    }   

    /**
     * Sorts the JTable based on the selected column and order.
     * @param table The JTable to be sorted.
     * @param column The name of the column to sort by. The column name should match the table header.
     * @param ascending True for ascending order, false for descending.
     */
    public void sortLeavesTable(JTable table, String column, boolean ascending) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        int columnIndex = -1;

        // Find the column index by name
        for (int i = 0; i < model.getColumnCount(); i++) {
            // Normalize column name for comparison (remove spaces and underscores, make case-insensitive)
            String normalizedColumnName = model.getColumnName(i).replace(" ", "").replace("_", "").toLowerCase();
            String normalizedSearchColumn = column.replace(" ", "").replace("_", "").toLowerCase();

            if (normalizedColumnName.equals(normalizedSearchColumn)) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex != -1) {
            sortKeys.add(new RowSorter.SortKey(columnIndex, ascending ? SortOrder.ASCENDING : SortOrder.DESCENDING));
            sorter.setSortKeys(sortKeys);
            sorter.sort();
        } else {
            JOptionPane.showMessageDialog(null, "Column not found for sorting: " + column, "Sorting Error", JOptionPane.WARNING_MESSAGE);
        }
    }   
}