/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ViewModel;

/**
 *
 * @author dashcodes
 */

import static org.junit.Assert.*;
import Model.EmployeeDetails;
import org.junit.Before;
import org.junit.Test;
import ViewModel.UserSession;
import java.util.Date;

public class UserSessionTest {
    
    private UserSession userSession;
    private EmployeeDetails testEmployee;
    
    @Before
    public void setUp() {
        userSession = UserSession.getInstance();
        userSession.clearSession();
        
        // Create a test employee from database 
        testEmployee = new EmployeeDetails(
            10001, // EID 
            "Garcia", // Last Name
            "Manuel III", // First Name
            new java.sql.Date(new Date().getTime()), // Birthday
            "mgarcia@motorph.com", // Username
            "123abc", // Password
            "Regular", // Designation
            "Valero Carpark Building Valero Street 1227, Makati City", // Address
            "966-360-270", // Phone Number
            "123456789", // SSS
            "987654321", // PhilHealth
            "456789123", // TIN
            "321654987", // Pag-IBIG
            "Active", // Status
            "Supervisor Name"); // Immediate Supervisor
    }
    
    @Test
    public void testSingletonInstance() {
        UserSession instance1 = UserSession.getInstance();
        UserSession instance2 = UserSession.getInstance();
        
        assertSame("UserSession should return the same instance", instance1, instance2);
        assertNotNull("UserSession instance should not be null", instance1);
    }
    
    @Test
    public void testSetAndGetLoggedInUser() {
        userSession.setLoggedInUser(testEmployee);
        
        EmployeeDetails retrievedUser = userSession.getLoggedInUser();
        
        assertNotNull("Retrieved user should not be null", retrievedUser);
        assertEquals("EID should match", testEmployee.getEid(), retrievedUser.getEid());
        assertEquals("First name should match", testEmployee.getFirstName(), retrievedUser.getFirstName());
        assertEquals("Last name should match", testEmployee.getLastName(), retrievedUser.getLastName());
        assertEquals("Username should match", testEmployee.getUserName(), retrievedUser.getUserName());
    }
    
    @Test
    public void testSetAndGetUsername() {
        // Test setting and getting username
        String testUsername = "mgarcia@motorph.com";
        userSession.setUsername(testUsername);
        
        String retrievedUsername = userSession.getUsername();
        
        assertEquals("Username should match", testUsername, retrievedUsername);
    }
    
    @Test
    public void testClearSession() {
        // Set up session data
        userSession.setLoggedInUser(testEmployee);
        userSession.setUsername("mgarcia@motorph.com");
        
        assertNotNull("User should be set before clearing", userSession.getLoggedInUser());
        assertNotNull("Username should be set before clearing", userSession.getUsername());
        
        // Clear session
        userSession.clearSession();
        
        assertNull("User should be null after clearing session", userSession.getLoggedInUser());
        assertNull("Username should be null after clearing session", userSession.getUsername());
    }
    
    @Test
    public void testSessionPersistenceAcrossInstances() {
        UserSession session1 = UserSession.getInstance();
        session1.setLoggedInUser(testEmployee);
        session1.setUsername("mgarcia@motorphil.com");
        
        UserSession session2 = UserSession.getInstance();
        
        assertNotNull("User should persist across instances", session2.getLoggedInUser());
        assertEquals("Username should persist across instances", "mgarcia@motorphil.com", session2.getUsername());
        assertEquals("EID should match across instances", testEmployee.getEid(), session2.getLoggedInUser().getEid());
    }
    
    @Test
    public void testInitialSessionState() {
        UserSession freshSession = UserSession.getInstance();
        freshSession.clearSession(); 
        
        assertNull("Initial logged in user should be null", freshSession.getLoggedInUser());
        assertNull("Initial username should be null", freshSession.getUsername());
    }
    
    @Test
    public void testSetNullValues() {
        // Test setting null values 
        userSession.setLoggedInUser(null);
        userSession.setUsername(null);
        
        assertNull("User should be null when set to null", userSession.getLoggedInUser());
        assertNull("Username should be null when set to null", userSession.getUsername());
    }
    
    @Test
    public void testMultipleUserSessions() {
        // Test switching between different users
        EmployeeDetails user1 = new EmployeeDetails(
            10002, "Lim", "Antonio", new java.sql.Date(new Date().getTime()),
            "alim@motorph.com", "123abc", "Regular",
            "Test Address", "123-456-7890",
            "111111111", "222222222", "333333333", "444444444",
            "Active", "Test Supervisor"
        );
        
        // Set first user
        userSession.setLoggedInUser(testEmployee);
        userSession.setUsername(testEmployee.getUserName());
        assertEquals("Should have first user", testEmployee.getEid(), userSession.getLoggedInUser().getEid());
        
        // Switch to second user
        userSession.setLoggedInUser(user1);
        userSession.setUsername(user1.getUserName());
        assertEquals("Should have second user", user1.getEid(), userSession.getLoggedInUser().getEid());
        assertEquals("Username should be updated", user1.getUserName(), userSession.getUsername());
    }
}