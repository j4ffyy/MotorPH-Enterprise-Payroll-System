/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ViewModel;

/**
 *
 * @author dashcodes
 */

import Model.EmployeeDetails; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List; 
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AdminRoleTest {
    
    private AdminRole adminRole;
    private Connection connection;
    private DBQueries dbQueries; 
    
    @Before
    public void setUp() throws SQLException {
        // Initialize database connection
        String url = "jdbc:mysql://localhost:3306/payrollsystem_db";
        String username = "root"; 
        String password = "admin"; 
        connection = DriverManager.getConnection(url, username, password);
        
        
        adminRole = new AdminRole();
        dbQueries = new DBQueries(); 
    }
    
    @Test
    public void testGenerateNextEID() {
        System.out.println("generateNextEID");
        String nextEid = adminRole.generateNextEID();
        assertNotNull("Generated EID should not be null", nextEid);
        assertTrue("Generated EID should be a number", nextEid.matches("\\d+"));
    }

    @Test
    public void testGetDesignationIdByName() {
        System.out.println("getDesignationIdByName");
        String designationName = "Chief Operating Officer"; 
        try {
            int designationId = adminRole.getDesignationIdByName(designationName);
            assertTrue("Designation ID should be greater than 0", designationId > 0);
        } catch (SQLException e) {
            fail("getDesignationIdByName threw SQLException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("getDesignationIdByName threw IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test
    public void testGetSupervisorIdByName() {
        System.out.println("getSupervisorIdByName");
        String supervisorName = "Lim, Antonio"; 
        try {
            int supervisorId = adminRole.getSupervisorIdByName(supervisorName);
            assertTrue("Supervisor ID should be greater than 0", supervisorId > 0);
        } catch (SQLException e) {
            fail("getSupervisorIdByName threw SQLException: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            fail("getSupervisorIdByName threw IllegalArgumentException: " + e.getMessage());
        }
    }

    @Test
    public void testDbQueriesConstants() {
        System.out.println("testDbQueriesConstants");
        
        assertNotNull("userLogin query should not be null", dbQueries.userLogin);
        assertNotNull("usernameExists query should not be null", dbQueries.usernameExists);
        assertNotNull("getUserDetailsByUsernamePassword query should not be null", dbQueries.getUserDetailsByUsernamePassword);
        assertNotNull("getEmployeeReportData query should not be null", dbQueries.getEmployeeReportData);
        assertNotNull("generateNextEid query should not be null", dbQueries.generateNextEid);
        
        assertNotNull("insertEmployee query should not be null", dbQueries.insertEmployee);
        assertNotNull("updateEmployee query should not be null", dbQueries.updateEmployee);
        assertNotNull("deleteEmployee query should not be null", dbQueries.deleteEmployee);
        assertNotNull("getDesignationIdByName query should not be null", dbQueries.getDesignationIdByName);
        assertNotNull("getSupervisorIdByName query should not be null", dbQueries.getSupervisorIdByName);

    }
    
    // Cleanup method
    @org.junit.After
    public void tearDown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}