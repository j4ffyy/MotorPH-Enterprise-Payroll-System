package ViewModel;

import Model.EmployeeDetails;
import Repository.DataSource;
import Repository.DataAccessObjects.EmployeeDataAccess;
import com.toedter.calendar.JDateChooser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import ViewModel.RestrictedInput;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class AdminRole {
    
    private DBQueries dbQueries;
    private EmployeeDataAccess employeeDataAccess;

    public AdminRole() {
        this.dbQueries = new DBQueries();
        this.employeeDataAccess = new EmployeeDataAccess();
    }
    
    /**
     * Retrieves the Designation ID by its name.
     * This method might be moved to EmployeeDataAccess or a specific lookup service later.
     * @param designationName The name of the designation.
     * @return The Designation ID.
     * @throws SQLException If the designation is not found or a database error occurs.
     */
    public int getDesignationIdByName(Connection conn, String designationName) throws SQLException {
        return getDesignationIdByName(designationName);
    }

    /**
     * Retrieves the Designation ID by its name.
     * This method might be moved to EmployeeDataAccess or a specific lookup service later.
     * @param designationName The name of the designation.
     * @return The Designation ID.
     * @throws SQLException If the designation is not found or a database error occurs.
     */
    public int getDesignationIdByName(String designationName) throws SQLException {
        if (designationName == null || designationName.isEmpty()) {
            throw new IllegalArgumentException("Designation name cannot be null or empty.");
        }
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(dbQueries.getDesignationIdByName)) {
            ps.setString(1, designationName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Designation_ID");
                }
            }
        }
        throw new SQLException("Designation not found: " + designationName);
    }

    /**
     * Retrieves the Supervisor ID by name.
     * This method might be moved to EmployeeDataAccess or a specific lookup service later.
     * @param supervisorName The name of the supervisor.
     * @return The Supervisor ID.
     * @throws SQLException If the supervisor is not found or a database error occurs.
     */
    public int getSupervisorIdByName(Connection conn, String supervisorName) throws SQLException {
        return getSupervisorIdByName(supervisorName);
    }

    /**
     * Retrieves the Supervisor ID by name.
     * This method might be moved to EmployeeDataAccess or a specific lookup service later.
     * @param supervisorName The name of the supervisor.
     * @return The Supervisor ID.
     * @throws SQLException If the supervisor is not found or a database error occurs.
     */
    public int getSupervisorIdByName(String supervisorName) throws SQLException {
        if (supervisorName == null || supervisorName.isEmpty()) {
            throw new IllegalArgumentException("Supervisor name cannot be null or empty.");
        }
        try (Connection conn = DataSource.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(dbQueries.getSupervisorIdByName)) {
            ps.setString(1, supervisorName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Supervisor_ID");
                }
            }
        }
        throw new SQLException("Supervisor not found: " + supervisorName);
    }
    
    public String generateNextEID() {
        try (Connection connection = DataSource.getInstance().getConnection();
                
                Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(dbQueries.generateNextEid)) {
            
            if (rs.next()) {
                int maxEid = rs.getInt("nextEid");
                return String.valueOf(maxEid);
            }
            return "10001";
            
        } catch (SQLException e) {
            System.err.println("Error generating next EID: " + e.getMessage());
            return "10001";
        }
    }
    
    /**
     * Generates the next payslip number (PN).
     * Format: MMDDYYY-EID-NNN
     * @param eid The employee ID.
     * @return The generated payslip number.
     */
    public String generateNextPN(String eid) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String datePart = today.format(formatter);
       
        String sequencePart = "001"; 
        
        return datePart + "-" + eid + "-" + sequencePart;
    }

    public void showAddDialog(JFrame parent, DefaultTableModel tableModel) {
        EmployeeDetails employee = new EmployeeDetails();
        
        JDialog addDialog = new JDialog(parent, "Add New Employee", true);
        addDialog.setSize(new Dimension(600, 700));
        addDialog.setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        final JTextField eidField = new JTextField();
        try {
            eidField.setText(generateNextEID());
        } catch (Exception e) {
            eidField.setText("Error");
        }
        eidField.setEditable(false);
        eidField.setFocusable(false);

        final JTextField lastNameField = new JTextField(20);
        final JTextField firstNameField = new JTextField(20);
        
        // Switch to JDateChooser from JTextField
        final JDateChooser birthDayChooser = new JDateChooser();
        birthDayChooser.setDateFormatString("MM/dd/yyyy"); 
        birthDayChooser.setPreferredSize(new Dimension(200, 25));
        birthDayChooser.setToolTipText("Select birth date");
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -100);
        birthDayChooser.setMinSelectableDate(minDate.getTime());
        birthDayChooser.setMaxSelectableDate(new Date());
        
        final JTextField addressField = new JTextField(20);
        final JTextField phoneField = new JTextField(15);
        phoneField.setDocument(new PhoneNumberDocument());
        setPlaceholder(phoneField, "XXX-XXX-XXX");

        final JTextField sssField = new JTextField(employee.getSss());
        sssField.setDocument(new GovernmentIDDocument(10));
        setPlaceholder(sssField, "XX-XXXXXXX-X");

        final JTextField philHealthField = new JTextField(employee.getPhilHealth());
        philHealthField.setDocument(new GovernmentIDDocument(12));
        setPlaceholder(philHealthField, "XXXXXXXXXXXX");

        final JTextField tinField = new JTextField(employee.getTin());
        tinField.setDocument(new GovernmentIDDocument(9));
        setPlaceholder(tinField, "XXX-XXX-XXX-XXX");

        final JTextField pagIbigField = new JTextField(employee.getPagIbig());
        pagIbigField.setDocument(new GovernmentIDDocument(12));
        setPlaceholder(pagIbigField, "XXXX-XXXX-XXXX");

        final String[] statusOptions = {"Probationary", "Regular"};
        final JComboBox<String> statusCombo = new JComboBox<>(statusOptions);

        final JComboBox<String> designationCombo = new JComboBox<>(designationOptions);
        final JTextField salaryField = new JTextField(15);
        salaryField.setDocument(new SalaryDocument());
        addSalaryFormatting(salaryField);

        addFormField(formPanel, "EID:", eidField);
        addFormField(formPanel, "Last Name:", lastNameField);
        addFormField(formPanel, "First Name:", firstNameField);
        addFormField(formPanel, "Birthday:", birthDayChooser);
        addFormField(formPanel, "Address:", addressField);
        addFormField(formPanel, "Phone Number:", phoneField);
        addFormField(formPanel, "SSS #:", sssField);
        addFormField(formPanel, "PhilHealth #:", philHealthField);
        addFormField(formPanel, "TIN #:", tinField);
        addFormField(formPanel, "Pag-ibig #:", pagIbigField);
        addFormField(formPanel, "Status:", statusCombo);
        addFormField(formPanel, "Designation:", designationCombo);
        addFormField(formPanel, "Basic Salary:", salaryField);

        JButton addButton = new JButton("Add Employee");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        addDialog.setLayout(new BorderLayout());
        addDialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> { addEmployeeAction(addDialog, tableModel, 
                eidField, 
                lastNameField, 
                firstNameField, 
                birthDayChooser, 
                addressField, 
                phoneField, 
                sssField, 
                philHealthField, 
                tinField, 
                pagIbigField, 
                statusCombo, designationCombo, salaryField); });
        cancelButton.addActionListener(e -> addDialog.dispose());
        
        addDialog.setVisible(true);
    }

    private void addEmployeeAction(
            JDialog addDialog, 
            DefaultTableModel tableModel, 
            JTextField eidField, 
            JTextField lastNameField, 
            JTextField firstNameField, 
            JDateChooser birthDayChooser, 
            JTextField addressField, 
            JTextField phoneField, 
            JTextField sssField, 
            JTextField philHealthField, 
            JTextField tinField, 
            JTextField pagIbigField, 
            JComboBox<String> statusCombo, 
            JComboBox<String> designationCombo, 
            JTextField salaryField) {
        try {
            formatSalaryField(salaryField);

            String eidText = eidField.getText().trim();
            int eid;
            try {
                eid = Integer.parseInt(eidText);
            } catch (NumberFormatException e) {
                showMessage("Invalid Employee ID format. Please ensure the EID is a valid number.", "Validation Error", true);
                return;
            }
            String lastName = lastNameField.getText().trim();
            String firstName = firstNameField.getText().trim();
            
            // Validation for birth date
            Date selectedBirthDate = birthDayChooser.getDate();
            if (selectedBirthDate == null) {
                showMessage("Please select a birth date.", "Validation Error", true);
                return;
            }

            // Check if date is reasonable
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -120);
            Date minReasonableDate = cal.getTime();
            if (selectedBirthDate.before(minReasonableDate)) {
                showMessage("Please select a valid birth date.", "Validation Error", true);
                return;
            }

            // Format the date properly for database
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String birthdayStr = sdf.format(selectedBirthDate);
                        
            String address = addressField.getText().trim();
            String phoneNumber = phoneField.getText().trim();
            String sssNum = sssField.getText().trim();
            String philhealthNum = philHealthField.getText().trim();
            String tinNum = tinField.getText().trim();
            String pagibigNum = pagIbigField.getText().trim();
            String status = statusCombo.getSelectedItem().toString();
            String designationName = designationCombo.getSelectedItem().toString();
            double basicSalary = Double.parseDouble(salaryField.getText().trim().replaceAll("[^0-9.]", ""));

            // Validate data
            if (lastName.isEmpty() || firstName.isEmpty() || birthdayStr.isEmpty() ||
                address.isEmpty() || phoneNumber.isEmpty() || sssNum.isEmpty() ||
                philhealthNum.isEmpty() || tinNum.isEmpty() || pagibigNum.isEmpty()) {
                showMessage("All fields are required.", "Validation Error", true);
                return;
            }

            try (Connection conn = DataSource.getInstance().getConnection()) {
                java.sql.Date birthday = dbQueries.convertStringToSqlDate(birthdayStr);
                if (birthday == null) {
                    showMessage("Invalid Birthday format. Please use MM/DD/YYYY.", "Validation Error", true);
                    return;
                }

                int designationId = getDesignationIdByName(conn, designationName);
                // Assuming a default supervisor ID or fetching it based on some logic
                // For now, let's use a placeholder or fetch from a UI component if available
                int supervisorId = 1; // Placeholder: You might need to add a supervisor selection to the UI

                EmployeeDetails newEmployee = new EmployeeDetails(
                    eid, lastName, firstName, birthday, null, null, designationName, address, phoneNumber,
                    sssNum, philhealthNum, tinNum, pagibigNum, status, null
                );
                newEmployee.setBasicSalary((float) basicSalary); // Set basic salary

                if (employeeDataAccess.addEmployee(newEmployee, designationId, supervisorId)) {
                    showMessage("Employee added successfully!", "Success", false);
                    employeeDataAccess.getAllEmployees(); // Refresh table in AdminDashboard
                    addDialog.dispose();
                } else {
                    showMessage("Failed to add employee.", "Error", true);
                }
            } catch (SQLException ex) {
                showMessage("Error: " + ex.getMessage(), "Error", true);
            }
        } catch (NumberFormatException ex) {
            showMessage("Error: " + ex.getMessage(), "Error", true);
        }
    }

    private void addFormField(JPanel panel, String label, Component field) {
        JPanel fieldPanel = new JPanel(new GridLayout(1, 2));
        fieldPanel.add(new JLabel(label));
        fieldPanel.add(field);
        panel.add(fieldPanel);
    }

    private void setPlaceholder(JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                } else {
                    textField.setForeground(Color.BLACK);
                }
            }
        });
    }

    private void showMessage(String message, String title, boolean isError) {
        int messageType = isError ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE;
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    private void formatSalaryField(JTextField salaryField) {
        String text = salaryField.getText().trim().replaceAll("[^0-9.]", "");
        if (text.isEmpty()) {
            salaryField.setText("20000.00");
            return;
        }
        try {
            double salary = Double.parseDouble(text);
            salary = Math.min(salary, 999999.99);
            salaryField.setText(String.format("%.2f", salary));
        } catch (NumberFormatException ex) {
            salaryField.setText("20000.00");
        }
    }

    private void addSalaryFormatting(JTextField salaryField) {
        salaryField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatSalaryField(salaryField);
            }
        });
        salaryField.setText("20,000.00");
    }

    // Helper Document classes (copied from OldAdminRole)
    private class PhoneNumberDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null || !str.matches("\\d*")) return;
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            String plainText = newText.replaceAll("-", "");
            if (plainText.length() > 9) return;
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < plainText.length(); i++) {
                if (i == 3 || i == 6) formatted.append('-');
                formatted.append(plainText.charAt(i));
            }
            remove(0, getLength());
            super.insertString(0, formatted.toString(), a);
        }
    }

    private class SalaryDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null || !str.matches("[0-9.]*")) return;
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            try {
                String plainText = newText.replaceAll(",", "");
                String[] parts = plainText.split("\\.");
                if (parts[0].length() > 6) return;
                if (plainText.indexOf('.') != plainText.lastIndexOf('.')) return;
                if (parts.length > 1 && parts[1].length() > 2) return;
                super.insertString(offs, str, a);
            } catch (NumberFormatException e) {
                return;
            }
        }
    }

    private class GovernmentIDDocument extends PlainDocument {
        private final int maxDigits;
        private final int[] hyphenPositions;

        public GovernmentIDDocument(int maxDigits) {
            this.maxDigits = maxDigits;
            if (maxDigits == 10) { // SSS: XX-XXXXXXX-X
                hyphenPositions = new int[]{2, 10};
            } else if (maxDigits == 9) { // TIN: XXX-XXX-XXX
                hyphenPositions = new int[]{3, 7};
            } else {
                hyphenPositions = new int[0];
            }
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null || !str.matches("\\d*")) return;
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            String plainText = newText.replaceAll("-", "");
            if (plainText.length() > maxDigits) return;
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < plainText.length(); i++) {
                for (int pos : hyphenPositions) {
                    if (i == pos) formatted.append('-');
                }
                formatted.append(plainText.charAt(i));
            }
            remove(0, getLength());
            super.insertString(0, formatted.toString(), a);
        }
    }

    private class DateDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null || !str.matches("\\d+")) return; //This only allow digits
            
            String currentText = getText(0, getLength());
            String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
            
            String digitsOnly = newText.replaceAll("/", ""); // Removes existing separators
            
            if (digitsOnly.length() > 8) return; // Max 8 digits for MMDDYYYY
            
            StringBuilder formatted = new StringBuilder();
            for (int i = 0; i < digitsOnly.length(); i++) {
                
                if (i == 2 || i == 4) {
                    formatted.append('/');
                }
                formatted.append(digitsOnly.charAt(i));
            }
            remove(0, getLength());
            super.insertString(0, formatted.toString(), a);
        }
    }

    private final String[] designationOptions = {
        "IT Operations and Systems",
        "HR Manager",
        "HR Team Leader",
        "HR Rank and File",
        "Accounting Head",
        "Payroll Manager",
        "Payroll Team Leader",
        "Payroll Rank and File",
        "Account Manager",
        "Account Team Leader",
        "Account Rank and File",
        "Sales & Marketing",
        "Supply Chain and Logistics",
        "Customer Service and Relations"
    };

    public void deleteEmployeeWithConfirmation(java.awt.Component parent, String eid, DefaultTableModel tableModel) {
        int confirmation = JOptionPane.showConfirmDialog(
            parent,
            "Are you sure you want to delete employee with EID: " + eid + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                if (employeeDataAccess.deleteEmployee(Integer.parseInt(eid))) {
                    showMessage("Employee deleted successfully.", "Success", false);
                    loadEmployeeData(tableModel); // Refresh table
                } else {
                    showMessage("Failed to delete employee. Employee not found or error occurred.", "Error", true);
                }
            } catch (SQLException | NumberFormatException ex) {
                showMessage("Database error: " + ex.getMessage(), "Error", true);
            }
        }
    }

    public boolean reactivateEmployee(int eid) throws SQLException {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.reactivateEmployee)) {
            ps.setInt(1, eid);
            return ps.executeUpdate() > 0;
        }
    }

    public void showSearchDialog(JFrame parent, DefaultTableModel tableModel) {
        JDialog searchDialog = new JDialog(parent, "Search Employees", true);
        searchDialog.setSize(new Dimension(400, 200));
        searchDialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        final JComboBox<String> columnComboBox = new JComboBox<>(new String[]{"EID", "Last_Name", "Designation_Name"});
        final JTextField searchField = new JTextField(20);

        panel.add(new JLabel("Search by:"));
        panel.add(columnComboBox);
        panel.add(new JLabel("Search term:"));
        panel.add(searchField);

        JButton searchButton = new JButton("Search");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        searchDialog.add(panel, BorderLayout.CENTER);
        searchDialog.add(buttonPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            String column = (String) columnComboBox.getSelectedItem();
            String searchTerm = searchField.getText().trim();

            if (searchTerm.isEmpty()) {
                showMessage("Please enter a search term.", "Error", true);
                return;
            }

            try {
                Integer eid = null;
                String lastName = null;
                String designation = null;

                if (column.equals("EID")) {
                    eid = Integer.parseInt(searchTerm);
                } else if (column.equals("Last_Name")) {
                    lastName = searchTerm;
                } else if (column.equals("Designation_Name")) {
                    designation = searchTerm;
                }

                List<EmployeeDetails> searchResults = employeeDataAccess.searchEmployees(eid, lastName, designation);
                
                tableModel.setRowCount(0); // Clear existing data
                for (EmployeeDetails emp : searchResults) {
                    tableModel.addRow(new Object[]{
                        emp.getEid(),
                        emp.getLastName() + ", " + emp.getFirstName(),
                        dbQueries.formatSqlDateToString(new java.sql.Date(emp.getBirthday().getTime())),
                        emp.getAddress(),
                        emp.getPhoneNumber(),
                        emp.getSss(),
                        emp.getPhilHealth(),
                        emp.getTin(),
                        emp.getPagIbig(),
                        emp.getStatus(),
                        emp.getDesignation(),
                        emp.getBasicSalary()
                    });
                }

                if (searchResults.isEmpty()) {
                    showMessage("No matching records found.", "No Results", false);
                }
                tableModel.fireTableDataChanged(); // Notify table of data change
                searchDialog.dispose();
            } catch (SQLException | NumberFormatException ex) {
                showMessage("Error during search: " + ex.getMessage(), "Error", true);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> searchDialog.dispose());

        searchDialog.setVisible(true);
    }
	
    public boolean sortEmployees(String columnName, boolean ascending, DefaultTableModel tableModel) {
        String query = dbQueries.createSortQuery(columnName, ascending);

        try (Connection conn = DataSource.getInstance().getConnection();
                Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Clear existing rows
            tableModel.setRowCount(0);
            tableModel.fireTableDataChanged();

            // Add data from ResultSet to the table model
            while (rs.next()) {
                java.util.Vector<Object> row = new java.util.Vector<>();
                row.add(rs.getString("EID"));

                // Combine lastName and firstName
                String lastName = rs.getString("Last_Name");
                String firstName = rs.getString("First_Name");
                row.add(lastName + ", " + firstName);

                row.add(rs.getString("Birthday"));
                row.add(rs.getString("Address"));
                row.add(rs.getString("Phone_Number"));
                row.add(rs.getString("SSS_Num"));
                row.add(rs.getString("Philhealth_Num"));
                row.add(rs.getString("TIN_Num"));
                row.add(rs.getString("Pagibig_Num"));
                row.add(rs.getString("Status"));
                row.add(rs.getString("Designation_Name"));

                // Fix for numeric value with commas - get as string and handle conversion
                try {
                    // First try getting as double directly
                    double salary = rs.getDouble("Basic_Salary");
                    row.add(salary);
                } catch (SQLException ex) {
                    // If that fails, get as string and clean it
                    String salaryStr = rs.getString("Basic_Salary");
                    if (salaryStr != null) {
                        // Remove commas and convert to double
                        salaryStr = salaryStr.replace(",", "");
                        try {
                            double salary = Double.parseDouble(salaryStr);
                            row.add(salary);
                        } catch (NumberFormatException e) {
                            // If all else fails, just add the string value
                            row.add(salaryStr);
                        }
                    } else {
                        row.add(0.0); // Default value if null
                    }
                }

                tableModel.addRow(row);
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Error sorting employees: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
	
	public void showSortDialog(JFrame parent, DefaultTableModel tableModel) {
        JDialog sortDialog = new JDialog(parent, "Sort Employees", true);
        sortDialog.setSize(new Dimension(400, 200));
        sortDialog.setLocationRelativeTo(parent);

        // Create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components
        JComboBox<String> columnComboBox = new JComboBox<>(new String[]{
            "Status", "Designation_Name", "EID", "Last_Name"
        });
        JRadioButton ascendingRadio = new JRadioButton("Ascending", true);
        JRadioButton descendingRadio = new JRadioButton("Descending", false);

        // Group radio buttons
        ButtonGroup sortOrderGroup = new ButtonGroup();
        sortOrderGroup.add(ascendingRadio);
        sortOrderGroup.add(descendingRadio);

        // Add labels and components to panel
        panel.add(new JLabel("Sort by:"));
        panel.add(columnComboBox);
        panel.add(new JLabel("Sort order:"));
        panel.add(ascendingRadio);
        panel.add(descendingRadio);

        // Add buttons
        JButton sortButton = new JButton("Sort");
        JButton cancelButton = new JButton("Cancel");

        // Add buttons to a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sortButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        sortDialog.add(panel, "Center");
        sortDialog.add(buttonPanel, "South");

        // Add action listeners to buttons
        sortButton.addActionListener(e -> {
            String column = (String) columnComboBox.getSelectedItem();
            boolean ascending = ascendingRadio.isSelected();

            String dbColumnName = column.replace(" ", "_");
            if (sortEmployees(dbColumnName, ascending, tableModel)) {
                sortDialog.dispose();
            } else {
                showMessage("Sort failed.", "Error", true);
            }
        });

        cancelButton.addActionListener(e -> sortDialog.dispose());

        // Show dialog
        sortDialog.setVisible(true);
    }

    public List<EmployeeDetails> searchInactiveEmployees(Integer eid, String lastName, String designation) throws SQLException {
        List<EmployeeDetails> employees = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.searchInactiveEmployees)) {
            ps.setObject(1, eid);
            ps.setObject(2, eid);
            ps.setString(3, lastName != null ? "%" + lastName + "%" : null);
            ps.setString(4, lastName != null ? "%" + lastName + "%" : null);
            ps.setString(5, designation != null ? "%" + designation + "%" : null);
            ps.setString(6, designation != null ? "%" + designation + "%" : null);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EmployeeDetails employee = new EmployeeDetails();
                    // Populate employee details from result set
                    employee.setEid(rs.getInt("EID"));
                    employee.setLastName(rs.getString("Last_Name"));
                    employee.setFirstName(rs.getString("First_Name"));
                    employee.setBirthday(rs.getDate("Birthday"));
                    employee.setUserName(rs.getString("Username"));
                    employee.setPassword(rs.getString("Password"));
                    employee.setDesignation(rs.getString("Designation_Name"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setPhoneNumber(rs.getString("Phone_Number"));
                    employee.setSss(rs.getString("SSS_Num"));
                    employee.setPhilHealth(rs.getString("Philhealth_Num"));
                    employee.setTin(rs.getString("TIN_Num"));
                    employee.setPagIbig(rs.getString("Pagibig_Num"));
                    employee.setStatus(rs.getString("Status"));
                    employee.setImmediateSupervisor(rs.getString("Supervisor_Name"));
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

    public void showUpdateDialog(JFrame parent, String eid, DefaultTableModel tableModel) {
        EmployeeDetails employee = getEmployeeById(eid);
        
        if (employee == null) {
            showMessage("Could not retrieve employee data.", "Error", true);
            return;
        }

        JDialog updateDialog = new JDialog(parent, "Update Employee", true);
        updateDialog.setSize(new Dimension(600, 500));
        updateDialog.setLocationRelativeTo(parent);

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Add fields with pre-filled values
        JTextField eidField = new JTextField(String.valueOf(employee.getEid()), 10);

        // Make it read-only since it's auto-generated
        eidField.setEditable(false);
        eidField.setFocusable(false);

        // For LastName field - allow letters and hyphens
        JTextField lastNameField = new JTextField(employee.getLastName(), 20);
        lastNameField.setDocument(new RestrictedInput(
            true,   // allow letters
            true,  // no spaces
            true,   // allow hyphens
            false,  // no apostrophes
            false,  // no numbers
            true,   // allow uppercase
            false,   // Allow @ sign
            false,  // Allow dot sign 
            40      // max length
        ));

        JTextField firstNameField = new JTextField(employee.getFirstName(), 20);
        firstNameField.setEditable(false);
        firstNameField.setFocusable(false);

        JTextField birthDayField = new JTextField(20);
        birthDayField.setEditable(false);
        birthDayField.setFocusable(false);
        // If birthday is empty or null, set a place value
        if (employee.getBirthday() == null) {
        birthDayField.setText("Not Available");
        } else {
        birthDayField.setText(dbQueries.formatSqlDateToString(employee.getBirthday()));
        }   

        JTextField addressField = new JTextField(employee.getAddress(), 20);

        JTextField phoneField = new JTextField(15);
        phoneField.setDocument(RestrictedInput.numbersOnly(10)); // 10 digits for phone number

        // Retrieve and format the phone number
        if (employee.getPhoneNumber() == null || employee.getPhoneNumber().trim().isEmpty()) {
        setPlaceholder(phoneField, "XXX-XXX-XXX");
        } else {
        String rawPhone = employee.getPhoneNumber().replaceAll("[^0-9]", ""); // Remove non-digits
        try {
            // Clear existing content
            phoneField.getDocument().remove(0, phoneField.getDocument().getLength());
            // Simulate user input to trigger formatting
            for (char c : rawPhone.toCharArray()) {
                phoneField.getDocument().insertString(
                    phoneField.getDocument().getLength(), 
                    String.valueOf(c), 
                    null
                );
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            // Fallback: Set raw value if formatting fails
            phoneField.setText(rawPhone);
        }
    }

        String[] statusOptions = {"Probationary", "Regular"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        statusCombo.setSelectedItem(employee.getStatus()); // Use correct index for status

        System.out.println("Employee designation from object: " + employee.getDesignation());
        JComboBox<String> designationCombo = new JComboBox<>(designationOptions);
        designationCombo.setSelectedItem(employee.getDesignation()); 

        // Format salary as a string
        JTextField salaryField = new JTextField();
        salaryField.setDocument(new SalaryDocument());
        addSalaryFormatting(salaryField);
        salaryField.setText(String.format("%.2f", employee.getBasicSalary()));
        
        
        // Add all fields to form panel with labels
        addFormField(formPanel, "EID:", eidField);
        addFormField(formPanel, "Last Name:", lastNameField);
        addFormField(formPanel, "First Name:", firstNameField);
        addFormField(formPanel, "Birthday (MM/DD/YYYY):", birthDayField);
        addFormField(formPanel, "Address:", addressField);
        addFormField(formPanel, "Phone Number:", phoneField);
        addFormField(formPanel, "Status:", statusCombo);
        addFormField(formPanel, "Designation:", designationCombo);
        addFormField(formPanel, "Basic Salary:", salaryField);

        // Add buttons
        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");

        // Add buttons to a button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);

        // Add form panel and button panel to dialog
        updateDialog.setLayout(new BorderLayout());
        updateDialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        updateButton.addActionListener(e -> {
            try {
                String currentEid = eidField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String firstName = firstNameField.getText().trim();
                String birthdayStr = birthDayField.getText().trim();
                String address = addressField.getText().trim();
                String phoneNumber = phoneField.getText().trim();
                String status = statusCombo.getSelectedItem().toString();
                String designationName = designationCombo.getSelectedItem().toString();
               

                double basicSalary;
                try {
                    basicSalary = Double.parseDouble(salaryField.getText().trim());
                } catch (NumberFormatException ex) {
                    showMessage("Invalid salary format. Please enter a number.", "Error", true);
                    return;
                }

                if (updateEmployee(currentEid, lastName, firstName, birthdayStr, address, phoneNumber, status, designationName, basicSalary)) {
                    showMessage("Employee updated successfully.", "Success", false);
                    loadEmployeeData(tableModel);
                    updateDialog.dispose();
                } else {
                    showMessage("Failed to update employee.", "Error", true);
                }
            } catch (Exception ex) {
                showMessage("Error: " + ex.getMessage(), "Error", true);
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> updateDialog.dispose());

        updateDialog.setVisible(true);
    }

    public void showReactivateEmployeeDialog(JFrame parent, DefaultTableModel mainTableModel) {
        JDialog reactivateDialog = new JDialog(parent, "Reactivate Employee", true);
        reactivateDialog.setSize(new Dimension(700, 500));
        reactivateDialog.setLocationRelativeTo(parent);
        reactivateDialog.setLayout(new BorderLayout());

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search for Inactive Employee"));

        JComboBox<String> searchByComboBox = new JComboBox<>(new String[]{"EID", "Last Name"});
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchByComboBox);
        searchPanel.add(new JLabel("Enter value:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // --- Results Table ---
        DefaultTableModel inactiveTableModel = new DefaultTableModel(new String[]{"EID", "Full Name", "Designation", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        JTable inactiveEmployeesTable = new JTable(inactiveTableModel);
        inactiveEmployeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton reactivateButton = new JButton("Reactivate Selected Employee");
        JButton cancelButton = new JButton("Cancel");
        reactivateButton.setEnabled(false); // Disabled until a row is selected
        buttonPanel.add(reactivateButton);
        buttonPanel.add(cancelButton);

        // --- Add components to dialog ---
        reactivateDialog.add(searchPanel, BorderLayout.NORTH);
        reactivateDialog.add(new JScrollPane(inactiveEmployeesTable), BorderLayout.CENTER);
        reactivateDialog.add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---

        // Enable reactivate button when a row is selected
        inactiveEmployeesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && inactiveEmployeesTable.getSelectedRow() != -1) {
                reactivateButton.setEnabled(true);
            } else {
                reactivateButton.setEnabled(false);
            }
        });

        // Search button action
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (searchTerm.isEmpty()) {
                showMessage("Please enter a search term.", "Search Error", true);
                return;
            }

            String searchBy = (String) searchByComboBox.getSelectedItem();
            Integer eid = null;
            String lastName = null;

            if ("EID".equals(searchBy)) {
                try {
                    eid = Integer.parseInt(searchTerm);
                } catch (NumberFormatException ex) {
                    showMessage("Invalid EID format. Please enter a number.", "Search Error", true);
                    return;
                }
            } else {
                lastName = searchTerm;
            }

            try {
                List<EmployeeDetails> results = searchInactiveEmployees(eid, lastName, null);
                inactiveTableModel.setRowCount(0); // Clear previous results

                if (results.isEmpty()) {
                    showMessage("No inactive employees found matching the criteria.", "Search Result", false);
                } else {
                    for (EmployeeDetails emp : results) {
                        inactiveTableModel.addRow(new Object[]{
                            emp.getEid(),
                            emp.getLastName() + ", " + emp.getFirstName(),
                            emp.getDesignation(),
                            emp.getStatus()
                        });
                    }
                }
            } catch (SQLException ex) {
                showMessage("Database error during search: " + ex.getMessage(), "Error", true);
                ex.printStackTrace();
            }
        });

        // Reactivate button action
        reactivateButton.addActionListener(e -> {
            int selectedRow = inactiveEmployeesTable.getSelectedRow();
            if (selectedRow == -1) {
                showMessage("Please select an employee to reactivate.", "Error", true);
                return;
            }

            int eidToReactivate = (int) inactiveTableModel.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(
                reactivateDialog,
                "Are you sure you want to reactivate employee with EID: " + eidToReactivate + "?",
                "Confirm Reactivation",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                try {
                    if (reactivateEmployee(eidToReactivate)) {
                        showMessage("Employee " + eidToReactivate + " has been reactivated successfully.", "Success", false);
                        loadEmployeeData(mainTableModel); // Refresh the main dashboard table
                        reactivateDialog.dispose();
                    } else {
                        showMessage("Failed to reactivate employee.", "Error", true);
                    }
                } catch (SQLException ex) {
                    showMessage("Database error during reactivation: " + ex.getMessage(), "Error", true);
                    ex.printStackTrace();
                }
            }
        });

    // Cancel button action
    cancelButton.addActionListener(e -> reactivateDialog.dispose());

    reactivateDialog.setVisible(true);
}

    public EmployeeDetails getEmployeeById(String eid) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(dbQueries.getEmployeeDetailsByEid)) {

            stmt.setString(1, eid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    EmployeeDetails employee = new EmployeeDetails();
                    employee.setEid(rs.getInt("EID"));
                    employee.setLastName(rs.getString("Last_Name"));
                    employee.setFirstName(rs.getString("First_Name"));
                    employee.setBirthday(rs.getDate("Birthday"));
                    employee.setAddress(rs.getString("Address"));
                    employee.setPhoneNumber(rs.getString("Phone_Number"));
                    employee.setSss(rs.getString("SSS_Num"));
                    employee.setPhilHealth(rs.getString("Philhealth_Num"));
                    employee.setTin(rs.getString("TIN_Num"));
                    employee.setPagIbig(rs.getString("Pagibig_Num"));
                    employee.setStatus(rs.getString("Status"));
                    employee.setDesignation(rs.getString("Designation_Name"));
                    employee.setBasicSalary(rs.getFloat("Basic_Salary"));
                    // Assuming Username, Password, Supervisor_Name are not needed for this specific dialog
                    // If they are, ensure they are fetched and set as well.

                    return employee;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateEmployee(String eid, 
            String lastName, 
            String firstName, 
            String birthdayStr, 
            String address, 
            String phoneNumber, 
            String status, 
            String designationName, 
            double basicSalary) {
        try (Connection conn = DataSource.getInstance().getConnection()) {
            // Update employee and payroll components
            String updateEmployeeSql = dbQueries.updateEmployee;
            try (PreparedStatement stmt = conn.prepareStatement(updateEmployeeSql)) {
                stmt.setString(1, lastName);
                stmt.setString(2, firstName);
                stmt.setDate(3, dbQueries.convertStringToSqlDate(birthdayStr));
                stmt.setString(4, address);
                stmt.setString(5, phoneNumber);
                stmt.setString(6, status);
                
                int designationId = getDesignationIdByName(conn, designationName);
                stmt.setInt(7, designationId);
                stmt.setDouble(8, basicSalary);
                stmt.setInt(9, Integer.parseInt(eid));

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) return false; // No employee record updated
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminRole.class.getName()).log(Level.SEVERE, null, ex);
        }  return true;
    } 
        
    

    public void loadEmployeeData(DefaultTableModel tableModel) {
        try (Connection conn = DataSource.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(dbQueries.getAllEmployees)) {

            // Clear existing rows
            tableModel.setRowCount(0);
            tableModel.fireTableDataChanged();

            // Populate the table model with data
            while (rs.next()) {
                Object[] row = new Object[13];
                row[0] = rs.getString("EID");
                String lastName = rs.getString("Last_Name");
                String firstName = rs.getString("First_Name");
                row[1] = lastName + ", " + firstName;
                row[2] = rs.getString("Birthday");
                row[3] = rs.getString("Address");
                row[4] = rs.getString("Phone_Number");
                row[5] = rs.getString("SSS_Num");
                row[6] = rs.getString("Philhealth_Num");
                row[7] = rs.getString("TIN_Num");
                row[8] = rs.getString("Pagibig_Num");
                row[9] = rs.getString("Status");
                row[10] = rs.getString("Designation_Name");
                row[11] = rs.getString("Basic_Salary");
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
        }
    }
}