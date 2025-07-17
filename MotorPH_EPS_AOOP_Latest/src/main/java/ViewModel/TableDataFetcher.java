package ViewModel;

import Model.EmployeeDetails;
import Repository.DataAccessObjects.EmployeeDataAccess;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class TableDataFetcher {
    private final DefaultTableModel tableModel;
    private final EmployeeDataAccess employeeDataAccess;

    public TableDataFetcher(DefaultTableModel tableModel, EmployeeDataAccess employeeDataAccess) {
        this.tableModel = tableModel;
        this.employeeDataAccess = employeeDataAccess;
    }

    /**
     * Refreshes the JTable with the latest employee data from the database.
     */
    public void refreshTable() {
        tableModel.setRowCount(0); // Clear existing data
        try {
            List<EmployeeDetails> employees = employeeDataAccess.getAllEmployees();
            for (EmployeeDetails employee : employees) {
                tableModel.addRow(new Object[]{
                    employee.getEid(),
                    employee.getFullName(),
                    employee.formatDate(employee.getBirthday(), "MM/dd/yyyy"),
                    employee.getAddress(),
                    employee.getPhoneNumber(),
                    employee.getSss(),
                    employee.getPhilHealth(),
                    employee.getTin(),
                    employee.getPagIbig(),
                    employee.getStatus(),
                    employee.getDesignation(),
                    employee.getBasicSalary()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error refreshing employee table: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public DefaultTableModel getUpdatedEmployeeTableModel() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{
            "EID", "Full Name", "Birthday", "Address", "Phone Number",
            "SSS", "PhilHealth", "TIN", "Pag-IBIG", "Status",
            "Designation", "Basic Salary"
        });

        try {
            List<EmployeeDetails> employees = employeeDataAccess.getAllEmployees();
            for (EmployeeDetails employee : employees) {
                model.addRow(new Object[]{
                    employee.getEid(),
                    employee.getFullName(),
                    employee.formatDate(employee.getBirthday(), "MM/dd/yyyy"),
                    employee.getAddress(),
                    employee.getPhoneNumber(),
                    employee.getSss(),
                    employee.getPhilHealth(),
                    employee.getTin(),
                    employee.getPagIbig(),
                    employee.getStatus(),
                    employee.getDesignation(),
                    employee.getBasicSalary()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error fetching updated employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return model;
    }
}