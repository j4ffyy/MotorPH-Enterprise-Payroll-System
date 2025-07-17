package Repository;

import Model.EmployeeDetails;
import ViewModel.DBQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository implements IEmployeeDetailsRepository {

    private final DBQueries dbQueries = new DBQueries();

    @Override
    public void addEmployee(EmployeeDetails employee) throws SQLException {
        String query = dbQueries.addEmployee;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, employee.getEid());
            pst.setString(2, employee.getLastName());
            pst.setString(3, employee.getFirstName());
            pst.setDate(4, new java.sql.Date(employee.getBirthday().getTime()));
            pst.setString(5, employee.getUserName());
            pst.setString(6, employee.getPassword());
            pst.setInt(7, new ViewModel.AdminRole().getDesignationIdByName(employee.getDesignation()));
            pst.setString(8, employee.getAddress());
            pst.setString(9, employee.getPhoneNumber());
            pst.setString(10, employee.getStatus());
            pst.setInt(11, new ViewModel.AdminRole().getSupervisorIdByName(employee.getImmediateSupervisor()));
            pst.executeUpdate();
        }
    }

    @Override
    public void updateEmployee(EmployeeDetails employee) throws SQLException {
        String query = dbQueries.updateEmployee;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, employee.getLastName());
            pst.setString(2, employee.getFirstName());
            pst.setDate(3, new java.sql.Date(employee.getBirthday().getTime()));
            pst.setString(4, employee.getAddress());
            pst.setString(5, employee.getPhoneNumber());
            pst.setString(6, employee.getStatus());
            pst.setInt(7, new ViewModel.AdminRole().getDesignationIdByName(employee.getDesignation()));
            pst.setFloat(8, employee.getBasicSalary());
            pst.setInt(9, employee.getEid());
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteEmployee(int employeeId) throws SQLException {
        String query = dbQueries.deleteEmployee;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, employeeId);
            pst.executeUpdate();
        }
    }

    @Override
    public EmployeeDetails getEmployeeById(int employeeId) throws SQLException {
        String query = dbQueries.getEmployeeDetailsByEid;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, employeeId);
            try (ResultSet rs = pst.executeQuery()) {
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
                        rs.getString("Supervisor_Name"),
                        rs.getFloat("Basic_Salary"),
                        rs.getFloat("Rice_Subsidy"),
                        rs.getFloat("Phone_Allowance"),
                        rs.getFloat("Clothing_Allowance"),
                        rs.getFloat("Over_Time")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<EmployeeDetails> getAllEmployees() throws SQLException {
        String query = dbQueries.getAllEmployees;
        List<EmployeeDetails> employees = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                employees.add(new EmployeeDetails(
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
                ));
            }
        }
        return employees;
    }

    public EmployeeDetails getEmployeeByUsername(String username) throws SQLException {
        String query = dbQueries.getUserDetailsByUsername;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, username);
            try (ResultSet rs = pst.executeQuery()) {
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
                        rs.getString("Supervisor_Name"),
                        rs.getFloat("Basic_Salary"),
                        rs.getFloat("Rice_Subsidy"),
                        rs.getFloat("Phone_Allowance"),
                        rs.getFloat("Clothing_Allowance"),
                        rs.getFloat("Over_Time")
                    );
                }
            }
        }
        return null;
    }

    public EmployeeDetails getEmployeeByEid(String eid) throws SQLException {
        String query = dbQueries.getEmployeeDetailsByEid;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, eid);
            try (ResultSet rs = pst.executeQuery()) {
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
                        rs.getString("Supervisor_Name"),
                        rs.getFloat("Basic_Salary"),
                        rs.getFloat("Rice_Subsidy"),
                        rs.getFloat("Phone_Allowance"),
                        rs.getFloat("Clothing_Allowance"),
                        rs.getFloat("Over_Time")
                    );
                }
            }
        }
        return null;
    }
}