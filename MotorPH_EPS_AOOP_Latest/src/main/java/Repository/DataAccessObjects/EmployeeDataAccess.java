/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Repository.DataAccessObjects;

/**
 *
 * @author dashcodes
 */

import Model.EmployeeDetails;
import ViewModel.DBQueries;
import Repository.DataSource;
import ViewModel.AdminRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EmployeeDataAccess {

    private DBQueries dbQueries;

    public EmployeeDataAccess() {
        this.dbQueries = new DBQueries();
    }

    /**
     * Retrieves a list of all employees with their comprehensive details.
     * @param connection The database connection to use for the query.
     * @return A list of EmployeeDetails objects.
     * @throws SQLException If a database access error occurs.
     */
    public List<EmployeeDetails> getAllEmployees() throws SQLException {
        List<EmployeeDetails> employees = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(dbQueries.getEmployeeReportData);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                // Populate EmployeeDetails object (same logic as in searchEmployees)
                int employeeEid = resultSet.getInt("EID");
                String employeeLastName = resultSet.getString("Last_Name");
                String employeeFirstName = resultSet.getString("First_Name");
                Date employeeBirthday = resultSet.getDate("Birthday");
                String employeeUsername = resultSet.getString("Username");
                String employeePassword = resultSet.getString("Password");
                String employeeDesignation = resultSet.getString("Designation_Name");
                String employeeAddress = resultSet.getString("Address");
                String employeePhoneNumber = resultSet.getString("Phone_Number");
                String employeeSssNum = resultSet.getString("SSS_Num");
                String employeePhilhealthNum = resultSet.getString("Philhealth_Num");
                String employeeTinNum = resultSet.getString("TIN_Num");
                String employeePagibigNum = resultSet.getString("Pagibig_Num");
                String employeeStatus = resultSet.getString("Status");
                String employeeSupervisorName = resultSet.getString("Supervisor_Name");
                float employeeBasicSalary = resultSet.getFloat("Basic_Salary");

                EmployeeDetails employee = new EmployeeDetails(
                    employeeEid, employeeLastName, employeeFirstName, (java.sql.Date) employeeBirthday,
                    employeeUsername, employeePassword, employeeDesignation, employeeAddress,
                    employeePhoneNumber, employeeSssNum, employeePhilhealthNum,
                    employeeTinNum, employeePagibigNum, employeeStatus, employeeSupervisorName
                );
                employee.setBasicSalary(employeeBasicSalary);
                employees.add(employee);
            }
        }
        return employees;
    }
    
    
     public String formatDateForDatabase(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        return outputFormat.format(date);
    }
    
    /**
     * Adds a new employee and their government IDs to the database.
     * This method performs two inserts within a single transaction.
     * * @param connection The database connection.
     * @param employeeDetails The EmployeeDetails object containing the new employee's information.
     * @return true if the employee was successfully added, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean addEmployee(EmployeeDetails employeeDetails, int designationId, int supervisorId) throws SQLException {
        // Use SimpleDateFormat to format the Date object into a String
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
         String birthdayString = formatDateForDatabase(employeeDetails.getBirthday());
        // Insert into employee table
        try (Connection conn = DataSource.getInstance().getConnection();PreparedStatement stmtEmployee = conn.prepareStatement(dbQueries.insertEmployee)) {
            stmtEmployee.setInt(1, employeeDetails.getEid());
            stmtEmployee.setString(2, employeeDetails.getLastName());
            stmtEmployee.setString(3, employeeDetails.getFirstName());
            stmtEmployee.setString(4, birthdayString); // Use the formatted string
            stmtEmployee.setString(5, employeeDetails.getAddress());
            stmtEmployee.setString(6, employeeDetails.getPhoneNumber());
            stmtEmployee.setString(7, employeeDetails.getUserName());
            stmtEmployee.setString(8, employeeDetails.getPassword());
            stmtEmployee.setString(9, employeeDetails.getStatus());
            stmtEmployee.setInt(10, designationId);
            stmtEmployee.setInt(11, supervisorId); 
            stmtEmployee.executeUpdate();
        }
        // Insert into employee_government_ids table
        try (Connection conn = DataSource.getInstance().getConnection();PreparedStatement stmtGovIds = conn.prepareStatement(dbQueries.addGovernmentIds)) {
            stmtGovIds.setInt(1, employeeDetails.getEid());
            stmtGovIds.setString(2, employeeDetails.getSss());
            stmtGovIds.setString(3, employeeDetails.getPhilHealth());
            stmtGovIds.setString(4, employeeDetails.getTin());
            stmtGovIds.setString(5, employeeDetails.getPagIbig());
            stmtGovIds.executeUpdate();
        }
        return true;
    }

    /**
     * Searches for employees based on EID, Last Name, and/or Designation.
     * Null parameters will be ignored in the search criteria.
     * @param connection The database connection to use for the query.
     * @param eid The Employee ID to search for (can be null to ignore).
     * @param lastName The last name to search for (can be null or empty to ignore).
     * @param designation The designation name to search for (can be null or empty to ignore).
     * @return A list of EmployeeDetails objects matching the criteria.
     * @throws SQLException If a database access error occurs.
     */
    public List<EmployeeDetails> searchEmployees(Integer eid, String lastName, String designation) throws SQLException {
        List<EmployeeDetails> employees = new ArrayList<>();
        
        // Ensure that DBQueries has the public final String searchEmployees definition
        String sqlQuery = dbQueries.searchEmployees; 

        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            int paramIndex = 1;

            // Parameter 1 and 2: EID
            if (eid == null) {
                preparedStatement.setNull(paramIndex++, java.sql.Types.INTEGER);
                preparedStatement.setNull(paramIndex++, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(paramIndex++, eid);
                preparedStatement.setInt(paramIndex++, eid);
            }

            // Parameter 3 and 4: Last Name
            if (lastName == null || lastName.trim().isEmpty()) {
                preparedStatement.setNull(paramIndex++, java.sql.Types.VARCHAR);
                preparedStatement.setNull(paramIndex++, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(paramIndex++, "%" + lastName.trim() + "%");
                preparedStatement.setString(paramIndex++, "%" + lastName.trim() + "%");
            }

            // Parameter 5 and 6: Designation Name
            if (designation == null || designation.trim().isEmpty()) {
                preparedStatement.setNull(paramIndex++, java.sql.Types.VARCHAR);
                preparedStatement.setNull(paramIndex++, java.sql.Types.VARCHAR);
            } else {
                preparedStatement.setString(paramIndex++, "%" + designation.trim() + "%");
                preparedStatement.setString(paramIndex++, "%" + designation.trim() + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int employeeEid = resultSet.getInt("EID");
                    String employeeLastName = resultSet.getString("Last_Name");
                    String employeeFirstName = resultSet.getString("First_Name");
                    Date employeeBirthday = resultSet.getDate("Birthday");
                    String employeeUsername = resultSet.getString("Username");
                    String employeePassword = resultSet.getString("Password");
                    String employeeDesignation = resultSet.getString("Designation_Name");
                    String employeeAddress = resultSet.getString("Address");
                    String employeePhoneNumber = resultSet.getString("Phone_Number");
                    String employeeSssNum = resultSet.getString("SSS_Num");
                    String employeePhilhealthNum = resultSet.getString("Philhealth_Num");
                    String employeeTinNum = resultSet.getString("TIN_Num");
                    String employeePagibigNum = resultSet.getString("Pagibig_Num");
                    String employeeStatus = resultSet.getString("Status");
                    String employeeSupervisorName = resultSet.getString("Supervisor_Name");

                    EmployeeDetails employee = new EmployeeDetails(
                        employeeEid, employeeLastName, employeeFirstName, (java.sql.Date) employeeBirthday,
                        employeeUsername, employeePassword, employeeDesignation, employeeAddress,
                        employeePhoneNumber, employeeSssNum, employeePhilhealthNum,
                        employeeTinNum, employeePagibigNum, employeeStatus, employeeSupervisorName
                    );
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    /**
     * Sorts employees by a given column in ascending or descending order.
     * @param connection The database connection to use for the query.
     * @param columnName The name of the column to sort by.
     * @param ascending True for ascending order, false for descending.
     * @return A list of EmployeeDetails objects, sorted.
     * @throws SQLException If a database access error occurs.
     */
    public List<EmployeeDetails> sortEmployees(String columnName, boolean ascending) throws SQLException {
        List<EmployeeDetails> employees = new ArrayList<>();
        String orderBy = ascending ? "ASC" : "DESC";
        // Ensure dbQueries.getEmployeeReportData includes an ORDER BY clause that can be dynamically added
        // For simplicity, let's assume a base query and append ORDER BY.
        // You might need a new SQL string in DBQueries or construct it here.
        String sqlQuery = dbQueries.getEmployeeReportData + " ORDER BY " + columnName + " " + orderBy; // Example dynamic sort

        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Populate EmployeeDetails object (same logic as in searchEmployees)
                    int employeeEid = resultSet.getInt("EID");
                    String employeeLastName = resultSet.getString("Last_Name");
                    String employeeFirstName = resultSet.getString("First_Name");
                    Date employeeBirthday = resultSet.getDate("Birthday");
                    String employeeUsername = resultSet.getString("Username");
                    String employeePassword = resultSet.getString("Password");
                    String employeeDesignation = resultSet.getString("Designation_Name");
                    String employeeAddress = resultSet.getString("Address");
                    String employeePhoneNumber = resultSet.getString("Phone_Number");
                    String employeeSssNum = resultSet.getString("SSS_Num");
                    String employeePhilhealthNum = resultSet.getString("Philhealth_Num");
                    String employeeTinNum = resultSet.getString("TIN_Num");
                    String employeePagibigNum = resultSet.getString("Pagibig_Num");
                    String employeeStatus = resultSet.getString("Status");
                    String employeeSupervisorName = resultSet.getString("Supervisor_Name");

                    EmployeeDetails employee = new EmployeeDetails(
                        employeeEid, employeeLastName, employeeFirstName, (java.sql.Date) employeeBirthday,
                        employeeUsername, employeePassword, employeeDesignation, employeeAddress,
                        employeePhoneNumber, employeeSssNum, employeePhilhealthNum,
                        employeeTinNum, employeePagibigNum, employeeStatus, employeeSupervisorName
                    );
                    employees.add(employee);
                }
            }
        }
        return employees;
    }
    
    public EmployeeDetails getEmployeeByEid(int eid) throws SQLException {
        String query = dbQueries.getEmployeeDetailsByEid;
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, eid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EmployeeDetails(
                        rs.getInt("EID"),
                        rs.getString("Last_Name"),
                        rs.getString("First_Name"),
                        rs.getDate("Birthday"),
                        rs.getString("Username"),
                        rs.getString("Password"),
                        rs.getString("Designation_Name"),
                        rs.getString("Address"),
                        rs.getString("Phone_Number"),
                        rs.getString("SSS_Num"),
                        rs.getString("Philhealth_Num"),
                        rs.getString("TIN_Num"),
                        rs.getString("Pagibig_Num"),
                        rs.getString("Status"),
                        rs.getString("Supervisor_Name")
                    );
                }
            }
        }
        return null;
    }

    public void showUpdateDialog(java.awt.Component parent, String eid) {
        try {
            EmployeeDetails empDetails = getEmployeeByEid(Integer.parseInt(eid));
            if (empDetails == null) {
                showMessage("Employee not found.", "Error", true);
                return;
            }

            // Create a panel with fields for updating
            JPanel panel = new JPanel(new java.awt.GridLayout(0, 2, 5, 5));
            JTextField lastNameField = new JTextField(empDetails.getLastName());
            JTextField firstNameField = new JTextField(empDetails.getFirstName());
            JTextField addressField = new JTextField(empDetails.getAddress());
            JTextField phoneField = new JTextField(empDetails.getPhoneNumber());
            JTextField statusField = new JTextField(empDetails.getStatus());
            JTextField designationField = new JTextField(empDetails.getDesignation());
            
            panel.add(new JLabel("Last Name:"));
            panel.add(lastNameField);
            panel.add(new JLabel("First Name:"));
            panel.add(firstNameField);
            panel.add(new JLabel("Address:"));
            panel.add(addressField);
            panel.add(new JLabel("Phone Number:"));
            panel.add(phoneField);
            panel.add(new JLabel("Status:"));
            panel.add(statusField);
            panel.add(new JLabel("Designation:"));
            panel.add(designationField);
            panel.add(new JLabel("SSS Number:"));
            

            int result = JOptionPane.showConfirmDialog(parent, panel, "Update Employee",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Create a new EmployeeDetails object with updated information
                EmployeeDetails updatedDetails = new EmployeeDetails(
                    Integer.parseInt(eid),
                    lastNameField.getText(),
                    firstNameField.getText(),
                    empDetails.getBirthday(),
                    empDetails.getUserName(),
                    empDetails.getPassword(),
                    designationField.getText(),
                    addressField.getText(),
                    phoneField.getText(),
                    statusField.getText(),
                    empDetails.getImmediateSupervisor()
                   
                    
                );

                // Update the employee in the database
                AdminRole adminRole = new AdminRole();
                int designationId = adminRole.getDesignationIdByName(designationField.getText());
                int supervisorId = adminRole.getSupervisorIdByName(empDetails.getImmediateSupervisor());
                updateEmployee(updatedDetails, designationId, supervisorId);
                showMessage("Employee updated successfully.", "Success", false);
            }
        } catch (SQLException | NumberFormatException ex) {
            showMessage("Database error: " + ex.getMessage(), "Error", true);
        }
    }

    public List<EmployeeDetails> updateEmployee(
            EmployeeDetails empDetails, 
            int designationId, 
            int supervisorId) 
            throws SQLException {
    String updateEmployeeQuery = dbQueries.updateEmployee;
    String updateGovIdsQuery = dbQueries.updateGovernmentIds;

    try (Connection conn = DataSource.getInstance().getConnection();
        PreparedStatement psEmployee = conn.prepareStatement(updateEmployeeQuery)) {
        psEmployee.setString(1, empDetails.getLastName());
        psEmployee.setString(2, empDetails.getFirstName());
        psEmployee.setDate(3, new java.sql.Date(empDetails.getBirthday().getTime()));
        psEmployee.setString(4, empDetails.getUserName());
        psEmployee.setString(5, empDetails.getPassword());
        psEmployee.setInt(6, designationId);
        psEmployee.setString(7, empDetails.getAddress());
        psEmployee.setString(8, empDetails.getPhoneNumber());
        psEmployee.setString(9, empDetails.getStatus());
        psEmployee.setInt(10, supervisorId);
        psEmployee.setInt(11, empDetails.getEid());
        psEmployee.executeUpdate();
        
    }
    // After updating, return the full, refreshed list of employees
    return getAllEmployees();
}
    
    /**
    * Displays a message dialog to the user
    * @param message The message to display
    * @param title The title of the dialog
    * @param isError Whether this is an error message (true) or info message (false)
    */
   public void showMessage(String message, String title, boolean isError) {
       int messageType = isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
       JOptionPane.showMessageDialog(null, message, title, messageType);
   }

    public void deleteEmployeeWithConfirmation(java.awt.Component parent, String eid) {
        int confirmation = JOptionPane.showConfirmDialog(
            parent,
            "Are you sure you want to delete employee with EID: " + eid + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                if (deleteEmployee(Integer.parseInt(eid))) {
                    showMessage("Employee deleted successfully.", "Success", false);
                } else {
                    showMessage("Failed to delete employee. Employee not found or error occurred.", "Error", true);
                }
            } catch (SQLException | NumberFormatException ex) {
                showMessage("Database error: " + ex.getMessage(), "Error", true);
            }
        }
    }

    public boolean deleteEmployee(int eid) throws SQLException {
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(dbQueries.deleteEmployee)) {
            ps.setInt(1, eid);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Return true if the employee was soft-deleted
        }
    }
}