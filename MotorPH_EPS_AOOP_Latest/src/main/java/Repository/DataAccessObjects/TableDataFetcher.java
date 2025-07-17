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
import Repository.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel; 
import ViewModel.DBQueries; 
import java.util.List;
import javax.swing.JOptionPane;

public class TableDataFetcher {

     private DBQueries dbQueries;
     private DefaultTableModel tableModel;

    public TableDataFetcher() {
        this.dbQueries = new DBQueries();
    }

    /**
     * Executes an SQL query and returns the results as a DefaultTableModel.
     * This method dynamically determines column names and data types from the ResultSetMetaData.
     *
     * @param sqlQuery The SQL query string to execute.
     * @return A DefaultTableModel containing the query results, or an empty model if an error occurs.
     */
    public DefaultTableModel fetchTableModel(String sqlQuery) {
        DefaultTableModel model = new DefaultTableModel();

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Get column names
            for (int i = 1; i <= columnCount; i++) {
                // Use getColumnLabel for display name, or getColumnName for database column name
                model.addColumn(metaData.getColumnLabel(i));
            }

            // Get row data
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching table data: " + e.getMessage());
        }
        return model;
    }
    
    /**
    * Refreshes the JTable with the latest employee data from the database.
    */
   public void refreshTable() {
       try (Connection conn = DataSource.getInstance().getConnection()) {
           // Get the SQL query from DBQueries
           dbQueries.getQuery("GET_ALL_EMPLOYEES");

           // Execute the query and get ResultSet
           PreparedStatement pstmt = conn.prepareStatement("GET_ALL_EMPLOYEES");
           ResultSet rs = pstmt.executeQuery();

           // Clear existing table data
           tableModel.setRowCount(0);

           // Populate table with fresh data
           while (rs.next()) {
               Object[] rowData = {
                   rs.getInt("eid"),
                   rs.getString("full_name"),
                   rs.getDate("birthday"),
                   rs.getString("address"),
                   rs.getString("phone_number"),
                   rs.getString("sss"),
                   rs.getString("philhealth"),
                   rs.getString("tin"),
                   rs.getString("pagibig"),
                   rs.getString("status"),
                   rs.getString("designation"),
                   rs.getDouble("basic_salary")
               };
               tableModel.addRow(rowData);
           }

           // Refresh the table display
           tableModel.fireTableDataChanged();
           
             // Close resources
           rs.close();
           pstmt.close();

       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(null, 
               "Error refreshing employee table: " + ex.getMessage(), 
               "Database Error", 
               JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace();
       }
   }

    /**
     * Overloaded method to fetch a table model using a query from DBQueries.
     *
     * @param dbQueries An instance of DBQueries containing the SQL strings.
     * @param queryName The key for the desired SQL query from DBQueries (e.g., "GET_ALL_LEAVES").
     * @return A DefaultTableModel containing the query results.
     */
    public DefaultTableModel fetchTableModel(DBQueries dbQueries, String queryName) {
        String sqlQuery = dbQueries.getQuery(queryName); //
        return fetchTableModel(sqlQuery);
    }

    /**
     * Fetches the table model for all employee leaves.
     * @param dbQueries An instance of DBQueries.
     * @return A DefaultTableModel containing all leave records.
     */
    public DefaultTableModel getLeavesTableModel(DBQueries dbQueries) {
        return fetchTableModel(dbQueries, "GET_ALL_LEAVES"); //
    }

    /**
     * Fetches the table model for payslip data.
     * Note: The "GET_PAYSLIP_DATA" query in DBQueries.java likely requires parameters
     * (e.g., Employee ID, date range). This method is a generic call. If your payslip query requires specific parameters
     * to be set on the PreparedStatement, you would need a more specialized method here that accepts those parameters
     * and sets them before execution.
     * @param dbQueries An instance of DBQueries.
     * @return A DefaultTableModel containing payslip data.
     */
    public DefaultTableModel getPayslipTableModel(DBQueries dbQueries) {
        return fetchTableModel(dbQueries, "GET_PAYSLIP_DATA"); //
    }

    /**
     * Fetches the table model for employee report data (often used for dashboards).
     * @param dbQueries An instance of DBQueries.
     * @return A DefaultTableModel containing employee report data.
     */
    public DefaultTableModel getEmployeeDashboardTableModel(DBQueries dbQueries) {
        return fetchTableModel(dbQueries, "GET_EMPLOYEE_REPORT_DATA"); //
    }
}