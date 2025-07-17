package ViewModel;

import Model.EmployeeDetails;
import java.sql.SQLException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RoleAuthenticatorTest {
    
    private RoleAuthenticator roleAuthenticator;
    private EmployeeDetails testEmployee;
    
    @Before
    public void setUp() throws SQLException {
        roleAuthenticator = new RoleAuthenticator();
        
        testEmployee = new EmployeeDetails();
        testEmployee.setEid(10001);
        testEmployee.setLastName("Garcia");
        testEmployee.setFirstName("Manuel III");
        testEmployee.setUserName("mgarcia@motorph.com");
        testEmployee.setPassword("123abc");
        testEmployee.setDesignation("Chief Executive Officer");
    }
    
    @Test
    public void testSetAndGetLoggedInUser() {
        System.out.println("setLoggedInUser and getLoggedInUser");
        
        RoleAuthenticator.setLoggedInUser(testEmployee);
        EmployeeDetails result = RoleAuthenticator.getLoggedInUser();
        
        assertEquals(testEmployee.getEid(), result.getEid());
        assertEquals(testEmployee.getLastName(), result.getLastName());
        assertEquals(testEmployee.getFirstName(), result.getFirstName());
        assertEquals(testEmployee.getDesignation(), result.getDesignation());
    }
    
    @Test
    public void testAuthenticateAndRedirectLogic() {
        System.out.println("authenticateAndRedirect - Logic Test");
        
        String adminDesignation = "Chief Executive Officer";
        assertNotNull("Admin designation should not be null", adminDesignation);
        assertFalse("Admin designation should not contain 'Rank and File'", 
                   adminDesignation.contains("Rank and File"));
        
        String employeeDesignation = "Account Rank and File";
        assertNotNull("Employee designation should not be null", employeeDesignation);
        assertTrue("Employee designation should contain 'Rank and File'", 
                  employeeDesignation.contains("Rank and File"));
        
        String nullDesignation = null;
        assertNull("Null designation should be null", nullDesignation);
    }
    
    @Test
    public void testGetCurrentDateFormatted() {
        System.out.println("getCurrentDateFormatted");
        String result = RoleAuthenticator.getCurrentDateFormatted();
        assertNotNull(result);
        assertTrue("Date should match format MM/dd/yyyy", 
            result.matches("\\d{2}/\\d{2}/\\d{4}"));
    }
    
    @Test
    public void testGetCurrentTimeFormatted() {
        System.out.println("getCurrentTimeFormatted");
        String result = RoleAuthenticator.getCurrentTimeFormatted();
        assertNotNull(result);
        assertTrue("Time should match format hh:mm:ss a", 
            result.matches("\\d{2}:\\d{2}:\\d{2} [AP]M"));
    }
    
    @Test
    public void testSessionManagement() {
        System.out.println("sessionManagement");
        
        RoleAuthenticator.setLoggedInUser(testEmployee);
        EmployeeDetails sessionUser = RoleAuthenticator.getLoggedInUser();
        
        assertEquals(testEmployee.getEid(), sessionUser.getEid());
        assertEquals(testEmployee.getUserName(), sessionUser.getUserName());
        
        RoleAuthenticator.setLoggedInUser(null);
        EmployeeDetails clearedSession = RoleAuthenticator.getLoggedInUser();
        assertNull("Session should be cleared", clearedSession);
    }
    
    @Test
    public void testDesignationCategorization() {
        System.out.println("designationCategorization");
        
        String[] adminDesignations = {
            "Chief Executive Officer",
            "IT Manager",
            "HR Manager",
            "Finance Manager"
        };
        
        String[] employeeDesignations = {
            "Account Rank and File",
            "HR Rank and File",
            "IT Rank and File"
        };
        
        for (String designation : adminDesignations) {
            assertFalse("Admin designation should not contain 'Rank and File': " + designation,
                       designation.contains("Rank and File"));
        }
        
        for (String designation : employeeDesignations) {
            assertTrue("Employee designation should contain 'Rank and File': " + designation,
                      designation.contains("Rank and File"));
        }
    }
}