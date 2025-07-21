/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ViewModel;

/**
 *
 * @author dashcodes
 */

import Model.EmployeeDetails;
import Repository.DataSource;
import View.ViewSettings;
import View.ViewTimesheet;
import com.toedter.calendar.JDateChooser;
import java.awt.GridLayout;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Controller for managing Timesheet-related operations.
 * Handles database interactions for Timesheet entries.
 */
public class TimesheetController {
    private final DataSource dataSource;
    private final DBQueries dbQueries;
    
    public TimesheetController() {
        this.dataSource = DataSource.getInstance(); 
        this.dbQueries = new DBQueries();
    }
    
    /**
     * Displays a dialog for adding a new timesheet entry.
     * @param parentFrame The parent JFrame for the dialog.
     * @param eid The Employee ID for whom the timesheet entry is being added.
     * @param tableModel The DefaultTableModel to refresh after adding the entry.
     */
    public void showAddDialog(JFrame parentFrame, int eid, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parentFrame, "Add Timesheet Entry", true);
        dialog.setLayout(new GridLayout(0, 2));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);

        int nextAttendanceId = generateNextAttID();
        JTextField attendanceIdField = new JTextField(String.valueOf(nextAttendanceId));
        attendanceIdField.setEditable(false);

        JTextField eidField = new JTextField(String.valueOf(eid));
        eidField.setEditable(true);

        JDateChooser dateChooser = new JDateChooser();
        JFormattedTextField timeField = null;
        try {
            MaskFormatter timeFormatter = new MaskFormatter("##:##:##");
            timeField = new JFormattedTextField(timeFormatter);
            timeField.setColumns(8);
        } catch (ParseException e) {
            e.printStackTrace();
            timeField = new JFormattedTextField(); // Fallback
        }
        
        String[] statusOptions = {"Present", "Absent", "Late", "On Leave"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);

        dialog.add(new JLabel("Attendance ID:"));
        dialog.add(attendanceIdField);
        dialog.add(new JLabel("EID:"));
        dialog.add(eidField);
        dialog.add(new JLabel("Date (MM/DD/YYYY):"));
        dialog.add(dateChooser);
        dialog.add(new JLabel("Time (HH:MM:SS):"));
        dialog.add(timeField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusCombo);

        JButton addButton = new JButton("Add");
        JFormattedTextField finalTimeField = timeField;
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Date logDate = dateChooser.getDate();
                    String logTimeString = finalTimeField.getText();
                    String attStatus = (String) statusCombo.getSelectedItem();

                    if (logDate == null || logTimeString.trim().isEmpty() || attStatus == null) {
                        showMessage("Please fill in all fields.", "Input Error", true);
                        return;
                    }

                    // Parse time string to java.util.Date (for Time object conversion)
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                    Date logTime = timeFormat.parse(logTimeString);

                    boolean success = addTimesheetEntry(eid, logDate, logTime, attStatus);

                    if (success) {
                        showMessage("Timesheet entry added successfully!", "Success", false);
                        loadTimesheetData(tableModel); // Refresh the table
                        dialog.dispose();
                    } else {
                        showMessage("Failed to add timesheet entry.", "Error", true);
                    }
                } catch (ParseException ex) {
                    showMessage("Invalid time format. Please use HH:MM:SS.", "Input Error", true);
                } catch (Exception ex) {
                    showMessage("Error adding timesheet entry: " + ex.getMessage(), "Error", true);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel);
        dialog.setVisible(true);
    }

    /**
     * Loads Timesheet data into the provided table model
     * @param tableModel The table model to populate
     * @return true if data was loaded successfully, false otherwise
     */
    public boolean loadTimesheetData(DefaultTableModel tableModel) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(dbQueries.getAllTimesheets)) { 
            
            tableModel.setRowCount(0);
            int rowCount = 0;
            
            while (rs.next()) {
                String attendanceId = rs.getString("Attendance_ID");
                int eid = rs.getInt("EID");
                Date logDate = rs.getDate("LogDate");
                Time logTime = rs.getTime("LogTime");
                String attStatus = rs.getString("AttStatus");
                
                tableModel.addRow(new Object[]{
                    attendanceId, 
                    eid, 
                    logDate, 
                    logTime, 
                    attStatus
                });
                rowCount++;
            }
            return rowCount > 0;
            
        } catch (SQLException e) {
            System.err.println("Error loading timesheet data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Adds a new timesheet entry to the database.
     * @param eid Employee ID.
     * @param logDate Date of the log.
     * @param logTime Time of the log.
     * @param attStatus Attendance status (e.g., "Present", "Absent").
     * @return true if the entry was added successfully, false otherwise.
     */
    public boolean addTimesheetEntry(int eid, java.util.Date logDate, java.util.Date logTime, String attStatus) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.addTimesheetEntry)) {
            ps.setInt(1, eid);
            ps.setDate(2, new java.sql.Date(logDate.getTime())); 
            ps.setTime(3, new java.sql.Time(logTime.getTime())); 
            ps.setString(4, attStatus);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding timesheet entry: " + e.getMessage());
            return false;
        }
    }

    private int generateNextAttID() {
        int nextId = 1;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.getQuery("GET_MAX_ATTENDANCE_ID"));
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nextId = rs.getInt("maxId") + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error generating next Attendance ID: " + e.getMessage());
        }
        return nextId;
    }
    
    /**
     * Adds a new timesheet entry to the database from an Object array.
     * This method handles parsing the date and time strings into Date objects.
     * @param timeLog An Object array containing:
     *                [0] - AttendanceID (String, currently unused for adding)
     *                [1] - EID (String, will be parsed to int)
     *                [2] - LogDate (String, will be parsed to java.util.Date)
     *                [3] - LogTime (String, will be parsed to java.util.Date)
     *                [4] - Status (String)
     * @return true if the entry was added successfully, false otherwise.
     */
    public boolean addTimeLog(Object[] timeLog) {
        try {
            // Extract and convert data from the Object array
            // Assuming the order: AttendanceID, EID, LogDate, LogTime, Status
            // AttendanceID is not used for adding, as it's usually auto-generated or handled by the DB.
            int eid = Integer.parseInt(timeLog[1].toString());
            
            // Parse LogDate String to java.util.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy"); // Adjust format if needed
            java.util.Date logDate = dateFormat.parse(timeLog[2].toString());
            
            // Parse LogTime String to java.util.Date
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // Adjust format if needed
            java.util.Date logTime = timeFormat.parse(timeLog[3].toString());
            
            String attStatus = timeLog[4].toString();

            // Call the existing addTimesheetEntry method
            return addTimesheetEntry(eid, logDate, logTime, attStatus);
        } catch (ParseException | NumberFormatException e) {
            System.err.println("Error parsing timesheet data: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Deletes a timesheet entry from the database based on its Attendance_ID.
     * @param attendanceID The Attendance_ID of the timesheet entry to delete.
     * @return true if the entry was deleted successfully, false otherwise.
     */
    public boolean deleteTimeLog(String attendanceID) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.deleteTimesheet)) {
            ps.setString(1, attendanceID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting timesheet entry: " + e.getMessage());
            return false;
        }
    }
    
    public void showDeleteConfirmation(String attendanceId, DefaultTableModel tableModel) {
        int confirmResult = JOptionPane.showConfirmDialog(
            null,
            "Are you sure you want to delete the timesheet entry with ID: " + attendanceId + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmResult == JOptionPane.YES_OPTION) {
            // User confirmed, now proceed with deletion
            performDeletion(attendanceId, tableModel);
        } else {
            // User cancelled, do nothing or show a message
            System.out.println("Deletion cancelled by user.");
        }
    }
    
    private void performDeletion(String attendanceId, DefaultTableModel tableModel) {
        boolean deleted = deleteTimeLog(attendanceId);

        if (deleted) {
            ViewSettings.showNotification(null, "Timesheet entry deleted successfully.", ViewSettings.NotificationType.SUCCESS);
            loadTimesheetData(tableModel);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete timesheet entry. Please check logs for details.", "Deletion Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    /**
     * Updates an existing timesheet entry in the database from an Object array.
     * This method handles parsing the date and time strings into Date objects.
     * @param timeLog An Object array containing:
     *                [0] - AttendanceID (String)
     *                [1] - EID (String, will be parsed to int)
     *                [2] - LogDate (String, will be parsed to java.util.Date)
     *                [3] - LogTime (String, will be parsed to java.util.Date)
     *                [4] - Status (String)
     * @return true if the entry was updated successfully, false otherwise.
     */
    public boolean updateTimeLog(Object[] timeLog) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.updateTimesheet)) {
            
            // Extract and convert data from the Object array
            String attendanceID = timeLog[0].toString();
            int eid = Integer.parseInt(timeLog[1].toString());
            
            // Parse LogDate String to java.util.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format if needed
            java.util.Date logDate = dateFormat.parse(timeLog[2].toString());
            
            // Parse LogTime String to java.util.Date
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); // Adjust format if needed
            java.util.Date logTime = timeFormat.parse(timeLog[3].toString());
            
            String attStatus = timeLog[4].toString();

            ps.setInt(1, eid);
            ps.setDate(2, new java.sql.Date(logDate.getTime()));
            ps.setTime(3, new java.sql.Time(logTime.getTime()));
            ps.setString(4, attStatus);
            ps.setString(5, attendanceID); // WHERE clause for Attendance_ID
            
            return ps.executeUpdate() > 0;
        } catch (ParseException | NumberFormatException | SQLException e) {
            System.err.println("Error updating timesheet entry: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Adds an empty row to the provided DefaultTableModel.
     * This is typically used to allow users to input new data directly into the table.
     * @param tableModel The DefaultTableModel to which the empty row will be added.
     */
    public void addEmptyRowToTable(DefaultTableModel tableModel) {
        tableModel.addRow(new Object[]{"", "", "", "", ""}); // Add an empty row
    }
    
    /**
     * Saves new timesheet entries from the provided DefaultTableModel to the database.
     * It identifies new rows by checking if the AttendanceID column (column 0) is empty.
     * @param tableModel The DefaultTableModel containing the timesheet data.
     * @return true if all new entries were saved successfully, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean saveNewTimeLogFromTable(DefaultTableModel tableModel) throws SQLException {
        boolean allSuccess = true;
        // Iterate from the last row to the first to avoid issues if rows are removed during iteration
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
            Object attendanceIdObj = tableModel.getValueAt(i, 0);
            
            // Check if it's a new row (AttendanceID is empty or null)
            if (attendanceIdObj == null || attendanceIdObj.toString().trim().isEmpty()) {
                try {
                    // Extract data for the new row
                    // Assuming table columns are: AttendanceID, EID, Employee Name, LogDate, LogTime, Status
                    // Note: Employee Name (column 2) is not needed for addTimeLog
                    Object[] newTimeLogData = {
                        "", // Placeholder for AttendanceID, will be generated by DB
                        tableModel.getValueAt(i, 1), // EID
                        tableModel.getValueAt(i, 3), // LogDate
                        tableModel.getValueAt(i, 4), // LogTime
                        tableModel.getValueAt(i, 5)  // Status
                    };

                    // Call the addTimeLog method to save the new entry
                    boolean success = addTimeLog(newTimeLogData);
                    if (!success) {
                        allSuccess = false;
                        showMessage("Failed to save new time log for row " + (i + 1), "Save Error", true);
                    }
                } catch (Exception e) {
                    allSuccess = false;
                    showMessage("Error processing new time log for row " + (i + 1) + ": " + e.getMessage(), "Save Error", true);
                }
            }
        }
        return allSuccess;
    }
    
    /**
     * Loads timesheet data for a specific employee into the provided table model.
     * @param tableModel The table model to populate.
     * @param eid The EID of the employee.
     * @return true if data was loaded successfully, false otherwise.
     */
    /**
     * Displays a message dialog to the user.
     * @param message The message to display.
     * @param title The title of the dialog.
     * @param isError True if it's an error message (uses ERROR_MESSAGE icon), false for information.
     */
    public void showMessage(String message, String title, boolean isError) {
        JOptionPane.showMessageDialog(null, message, title, isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a dialog for updating timesheet data.
     * @param parentFrame The parent JFrame for the dialog.
     * @param attendanceID The Attendance_ID of the timesheet entry to update.
     * @param tableModel The DefaultTableModel containing the timesheet data.
     */
    public void showUpdateDialog(JFrame parentFrame, String attendanceID, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parentFrame, "Update Timesheet Entry", true);
        dialog.setLayout(new GridLayout(0, 2));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(parentFrame);

        // Find the row to update based on attendanceID
        int rowIndex = -1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(attendanceID)) {
                rowIndex = i;
                break;
            }
        }

        if (rowIndex == -1) {
            showMessage("Timesheet entry not found for update.", "Error", true);
            dialog.dispose();
            return;
        }

        // Get current values
        String currentEid = tableModel.getValueAt(rowIndex, 1).toString();
        String currentLogDate = tableModel.getValueAt(rowIndex, 2).toString(); // LogDate is column 2
        String currentLogTime = tableModel.getValueAt(rowIndex, 3).toString(); // LogTime is column 3
        String currentAttStatus = tableModel.getValueAt(rowIndex, 4).toString(); // Status is column 4

        JTextField eidField = new JTextField(currentEid);
        eidField.setEditable(true);
        JTextField logDateField = new JTextField(currentLogDate);
        JTextField logTimeField = new JTextField(currentLogTime);
        
        JTextField attStatusField = new JTextField(currentAttStatus);

        dialog.add(new JLabel("EID:"));
        dialog.add(eidField);
        dialog.add(new JLabel("Log Date (YYYY-MM-DD):"));
        dialog.add(logDateField);
        dialog.add(new JLabel("Log Time (HH:MM:SS):"));
        dialog.add(logTimeField);
        dialog.add(new JLabel("Status:"));
        dialog.add(attStatusField);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> {
            try {
                Object[] updatedTimeLog = {
                    attendanceID,
                    eidField.getText(),
                    logDateField.getText(),
                    logTimeField.getText(),
                    attStatusField.getText()
                };

                boolean success = updateTimeLog(updatedTimeLog);
                if (success) {
                    showMessage("Timesheet entry updated successfully.", "Success", false);
                    // Reload data into the table after update
                    loadTimesheetData(tableModel);
                    dialog.dispose();
                } else {
                    showMessage("Failed to update timesheet entry.", "Error", true);
                }
            } catch (Exception ex) {
                showMessage("Error updating timesheet: " + ex.getMessage(), "Error", true);
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(buttonPanel);
        dialog.setVisible(true);
    }

    public boolean loadEmployeeTimesheetData(DefaultTableModel tableModel, int eid) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.getTimesheetByEid)) {
            ps.setInt(1, eid);
            try (ResultSet rs = ps.executeQuery()) {
                tableModel.setRowCount(0); // Clear existing data
                while (rs.next()) {
                    Date logDate = rs.getDate("LogDate");
                    Time logTime = rs.getTime("LogTime");
                    String attStatus = rs.getString("AttStatus");
                    tableModel.addRow(new Object[]{logDate, logTime, attStatus});
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error loading employee timesheet data: " + e.getMessage());
            return false;
        }
    }

    public Object[] getTimesheetEntryByEid(String eid) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.getTimesheetByEid)) {
            ps.setString(1, eid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Assuming the query returns LogDate, LogTime, AttStatus
                    Date logDate = rs.getDate("LogDate");
                    Time logTime = rs.getTime("LogTime");
                    String attStatus = rs.getString("AttStatus");
                    return new Object[]{eid, logDate, logTime, attStatus};
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving timesheet entry by EID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Displays a dialog for sorting timesheet data.
     * @param parentFrame The parent JFrame for the dialog.
     * @param tableModel The DefaultTableModel to sort.
     */
    public void showSortDialog(JFrame parentFrame, DefaultTableModel tableModel) {
        JDialog dialog = new JDialog(parentFrame, "Sort Timesheet Data", true);
        dialog.setLayout(new GridLayout(0, 1));
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(parentFrame);

        JComboBox<String> columnChooser = new JComboBox<>(new String[]{
            "Attendance_ID", "EID", "LogDate", "LogTime", "AttStatus"});
        JRadioButton rbAscending = new JRadioButton("Ascending");
        JRadioButton rbDescending = new JRadioButton("Descending");
        ButtonGroup sortOrderGroup = new ButtonGroup();
        sortOrderGroup.add(rbAscending);
        sortOrderGroup.add(rbDescending);
        rbAscending.setSelected(true); // Default sort order

        JPanel panel = new JPanel();
        panel.add(new JLabel("Sort by:"));
        panel.add(columnChooser);
        panel.add(rbAscending);
        panel.add(rbDescending);

        JButton btnSort = new JButton("Sort");
        btnSort.addActionListener(e -> {
            String column = (String) columnChooser.getSelectedItem();
            boolean ascending = rbAscending.isSelected();
            sortTableModel(tableModel, column, ascending);
            dialog.dispose();
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSort);
        buttonPanel.add(btnCancel);

        dialog.add(panel);
        dialog.add(buttonPanel);
        dialog.setVisible(true);
    }

    private void sortTableModel(DefaultTableModel tableModel, String columnName, boolean ascending) {
        int columnIndex = -1;
        switch (columnName) {
            case "Attendance_ID": columnIndex = 0; break;
            case "EID": columnIndex = 1; break;
            case "LogDate": columnIndex = 2; break; 
            case "LogTime": columnIndex = 3; break; 
            case "AttStatus": columnIndex = 4; break; 
        }

        // Adjust column index if "Employee Name" is added to the table model
        if (tableModel.getColumnName(2).equals("Employee Name")) {
            if (columnName.equals("LogDate")) columnIndex = 3;
            else if (columnName.equals("LogTime")) columnIndex = 4;
            else if (columnName.equals("AttStatus")) columnIndex = 5;
        }

        if (columnIndex == -1) return;

        final int idx = columnIndex;

        List<Object[]> rows = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < row.length; j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            rows.add(row);
        }

        rows.sort((r1, r2) -> {
            Object val1 = r1[idx];
            Object val2 = r2[idx];

            if (val1 == null && val2 == null) return 0;
            if (val1 == null) return ascending ? -1 : 1;
            if (val2 == null) return ascending ? 1 : -1;

            if (columnName.equals("LogDate")) {
                try {
                    java.util.Date date1 = (java.util.Date) val1;
                    java.util.Date date2 = (java.util.Date) val2;
                    return ascending ? date1.compareTo(date2) : date2.compareTo(date1);
                } catch (ClassCastException e) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy"); 
                        Date date1 = sdf.parse(val1.toString());
                        Date date2 = sdf.parse(val2.toString());
                        return ascending ? date1.compareTo(date2) : date2.compareTo(date1);
                    } catch (ParseException ex) {
                        return ascending ? val1.toString().compareTo(val2.toString()) : val2.toString().compareTo(val1.toString());
                    }
                }
            } else if (columnName.equals("LogTime")) {
                try {
                    Time time1 = (Time) val1;
                    Time time2 = (Time) val2;
                    return ascending ? time1.compareTo(time2) : time2.compareTo(time1);
                } catch (ClassCastException e) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
                        Date time1 = sdf.parse(val1.toString());
                        Date time2 = sdf.parse(val2.toString());
                        return ascending ? time1.compareTo(time2) : time2.compareTo(time1);
                    } catch (ParseException ex) {
                        return ascending ? val1.toString().compareTo(val2.toString()) : val2.toString().compareTo(val1.toString());
                    }
                }
            }  else if (columnName.equals("Attendance_ID")){
                try {
                    Integer aid1 = Integer.parseInt(val1.toString());
                    Integer aid2 = Integer.parseInt(val2.toString());
                    return ascending ? aid1.compareTo(aid2) : aid2.compareTo(aid1);
                } catch (NumberFormatException e) {
                    return ascending ? val1.toString().compareTo(val2.toString()) : val2.toString().compareTo(val1.toString());
                }
            } else if (columnName.equals("EID")) {
                try {
                    Integer eid1 = Integer.parseInt(val1.toString());
                    Integer eid2 = Integer.parseInt(val2.toString());
                    return ascending ? eid1.compareTo(eid2) : eid2.compareTo(eid1);
                } catch (NumberFormatException e) {
                    return ascending ? val1.toString().compareTo(val2.toString()) : val2.toString().compareTo(val1.toString());
                }
            }
            else {
                return ascending ? val1.toString().compareTo(val2.toString()) : val2.toString().compareTo(val1.toString());
            }
        });
        tableModel.setRowCount(0);
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }
    
    public Time recordTime(int eid, String recordType) throws SQLException {
        // Check if a record of the same type already exists for today
        if (hasExistingRecord(eid, recordType)) {
            return null; // Or throw an exception indicating a duplicate record
        }

        // Proceed to insert the new time record
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.addTimesheetEntry)) {
            
            long now = System.currentTimeMillis();
            Date today = new Date(now);
            Time currentTime = new Time(now);

            ps.setInt(1, eid);
            ps.setDate(2, new java.sql.Date(today.getTime()));
            ps.setTime(3, currentTime);
            ps.setString(4, recordType); // "Time In" or "Time Out"

            int rowsAffected = ps.executeUpdate();
            return (rowsAffected > 0) ? currentTime : null;
        }
    }

    public boolean hasExistingRecord(int eid, String recordType) throws SQLException {
        return false;
    }
    
    public Time getTimeRecord(int eid, String recordType) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.getTimesheetRecord)) {
            
            long now = System.currentTimeMillis();
            Date today = new Date(now);

            ps.setInt(1, eid);
            ps.setDate(2, new java.sql.Date(today.getTime()));
            ps.setString(3, recordType);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getTime("LogTime");
                }
            }
        }
        return null;
    }
}