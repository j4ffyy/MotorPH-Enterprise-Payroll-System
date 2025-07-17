/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ViewModel;

/**
 *
 * @author dashcodes
 */

import Repository.DataSource;
import Model.EmployeeDetails;
import View.EmployeeDashboard;
import View.AdminDashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoleAuthenticator {
    private DBQueries dbQueries = new DBQueries();
    private static EmployeeDetails loggedInUser;
    
    public RoleAuthenticator() {
    }
    
    public static void setLoggedInUser(EmployeeDetails user) {
        loggedInUser = user;
    }
    
    public static EmployeeDetails getLoggedInUser() {
        return loggedInUser;
    }
    
    /**
     * Authenticates user credentials against the database.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return true if authentication is successful, false otherwise.
     */
    public boolean authenticateUser(String username, String password) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.userLogin)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // User authenticated, now fetch full details
                    loggedInUser = getUserDetails(username, password);
                    return loggedInUser != null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Authenticates user credentials and retrieves full employee details.
     * This method combines authentication and detail retrieval for convenience in the UI layer.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return An EmployeeDetails object if authentication is successful, null otherwise.
     */
    public EmployeeDetails authenticateAndGetUserDetails(String username, String password) {
        try (Connection conn = DataSource.getInstance().getConnection()) {
            // First, authenticate the user
            try (PreparedStatement ps = conn.prepareStatement(dbQueries.userLogin)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // User authenticated, now fetch full details
                        loggedInUser = getUserDetails(username, password);
                        return loggedInUser;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication and details retrieval: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a username already exists in the database.
     * @param username The username to check.
     * @return true if the username exists, false otherwise.
     */
    public boolean usernameExists(String username) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.usernameExists)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error checking username existence: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Retrieves full employee details after successful authentication.
     * @param connection The active database connection.
     * @param username The username of the logged-in employee.
     * @param password The password of the logged-in employee.
     * @return An EmployeeDetails object populated with data, or null if not found.
     */
    private EmployeeDetails getUserDetails(String username, String password) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.getUserDetailsByUsernamePassword)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmployeeDetails user = new EmployeeDetails();
                    user.setEid(rs.getInt("EID"));
                    user.setLastName(rs.getString("Last_Name"));
                    user.setFirstName(rs.getString("First_Name"));
                    user.setBirthday(rs.getDate("Birthday"));
                    user.setUserName(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setDesignation(rs.getString("Designation_Name"));
                    user.setAddress(rs.getString("Address"));
                    user.setPhoneNumber(rs.getString("Phone_Number"));
                    user.setSss(rs.getString("SSS_Num"));
                    user.setPhilHealth(rs.getString("Philhealth_Num"));
                    user.setTin(rs.getString("TIN_Num"));
                    user.setPagIbig(rs.getString("Pagibig_Num"));
                    user.setStatus(rs.getString("Status"));
                    user.setImmediateSupervisor(rs.getString("Supervisor_Name"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user details: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Fetches the designation name for a given Employee ID.
     * @param eid The EID of the employee.
     * @return The designation name, or null if not found.
     */
    public String getUserDesignation(int eid) {
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(dbQueries.getUserDesignation)) {
            ps.setInt(1, eid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Designation_Name");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user designation: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Updates the password for a given username.
     * @param username The username for which to update the password.
     * @param newPassword The new password to set.
     * @return true if the password was successfully updated, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        // First, verify the user exists using the usernameExists query
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(dbQueries.getQuery("USERNAME_EXISTS"))) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // User does not exist
                    System.out.println("No user found with username: " + username);
                    return false;
                }
            }
        }

        // If user exists, proceed with password update using the new updateUserPassword query
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(dbQueries.getQuery("UPDATE_USERS_PASSWORD"))) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected + " for username: " + username);

            return rowsAffected > 0;
        }
    }

    public boolean authenticateAndRedirect(String designation) {
        try {
            if (designation != null) {
                if (designation.contains("Rank and File")) {
                    EmployeeDashboard employeeDashboard = new EmployeeDashboard();
                    employeeDashboard.setVisible(true);
                } else {
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                }
                return true;
            }
             return false;
        } catch (Exception e) {
            Logger.getLogger(RoleAuthenticator.class.getName()).log(Level.SEVERE, "Error during authentication", e);
            return false;
        }
    }
    
    public static String getCurrentDateFormatted() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return today.format(formatter);
    }
    
    public static String getCurrentTimeFormatted() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return now.format(formatter);
    }
}