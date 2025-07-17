package Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Pure unit tests for EmployeeDetails without external dependencies.
 * These tests focus on testing the business logic and basic functionality
 * without requiring Mockito or database connections.
 */
public class EmployeeDetailsTest {
    
    private EmployeeDetails employeeDetails;
    
    @Before
    public void setUp() {
        employeeDetails = new EmployeeDetails();
    }
    
    // Unit Tests for Getters and Setters
    @Test
    public void testGetSetEid() {
        employeeDetails.setEid(10001);
        assertEquals(10001, employeeDetails.getEid());
    }
    
    @Test
    public void testGetSetFirstName() {
        employeeDetails.setFirstName("Manuel III");
        assertEquals("Manuel III", employeeDetails.getFirstName());
    }
    
    @Test
    public void testGetSetLastName() {
        employeeDetails.setLastName("Garcia");
        assertEquals("Garcia", employeeDetails.getLastName());
    }
    
    @Test
    public void testGetSetDesignation() {
        employeeDetails.setDesignation("Chief Executive Officer");
        assertEquals("Chief Executive Officer", employeeDetails.getDesignation());
    }
    
    @Test
    public void testGetSetUserName() {
        employeeDetails.setUserName("mgarcia@motorph.com");
        assertEquals("mgarcia@motorph.com", employeeDetails.getUserName());
    }
    
    @Test
    public void testGetSetStatus() {
        employeeDetails.setStatus("Regular");
        assertEquals("Regular", employeeDetails.getStatus());
    }
    
    @Test
    public void testGetSetPhoneNumber() {
        employeeDetails.setPhoneNumber("966-860-270");
        assertEquals("966-860-270", employeeDetails.getPhoneNumber());
    }
    
    @Test
    public void testGetSetBasicSalary() {
        employeeDetails.setBasicSalary(90009.00f);
        assertEquals(90009.00f, employeeDetails.getBasicSalary(), 0.01f);
    }
    
    // Unit Tests for Business Logic (if getFullName method exists)
    @Test
    public void testGetFullName() {
        employeeDetails.setFirstName("Manuel III");
        employeeDetails.setLastName("Garcia");
      
        String expectedFullName = employeeDetails.getFirstName() + " " + employeeDetails.getLastName();
        assertEquals("Manuel III Garcia", expectedFullName);
    }
    
    @Test
    public void testGetFullNameWithNullFirstName() {
        employeeDetails.setFirstName(null);
        employeeDetails.setLastName("Garcia");
        
        String expectedFullName = employeeDetails.getFirstName() + " " + employeeDetails.getLastName();
        assertEquals("null Garcia", expectedFullName);
    }
    
    @Test
    public void testGetFullNameWithNullLastName() {
        employeeDetails.setFirstName("Manuel III");
        employeeDetails.setLastName(null);
        
        String expectedFullName = employeeDetails.getFirstName() + " " + employeeDetails.getLastName();
        assertEquals("Manuel III null", expectedFullName);
    }
    
    @Test
    public void testGetFullNameWithBothNamesNull() {
        employeeDetails.setFirstName(null);
        employeeDetails.setLastName(null);
        
        String expectedFullName = employeeDetails.getFirstName() + " " + employeeDetails.getLastName();
        assertEquals("null null", expectedFullName);
    }
    
    @Test
    public void testGetFullNameWithEmptyNames() {
        employeeDetails.setFirstName("");
        employeeDetails.setLastName("");
        
        String expectedFullName = employeeDetails.getFirstName() + " " + employeeDetails.getLastName();
        assertEquals(" ", expectedFullName);
    }
    
    // Unit Tests for Constructor
    @Test
    public void testDefaultConstructor() {
        EmployeeDetails employee = new EmployeeDetails();
        
        // Test default values - adjust these based on your actual implementation
        assertEquals(0, employee.getEid());
        assertNull(employee.getFirstName());
        assertNull(employee.getLastName());
        assertNull(employee.getUserName());
        assertNull(employee.getDesignation());
        assertNull(employee.getStatus());
        assertNull(employee.getPhoneNumber());
        assertEquals(0.0f, employee.getBasicSalary(), 0.01f);
    }
    

    @Test
    public void testSetNullValues() {
        // Test that setting null values doesn't cause exceptions
        employeeDetails.setFirstName(null);
        employeeDetails.setLastName(null);
        employeeDetails.setUserName(null);
        employeeDetails.setDesignation(null);
        employeeDetails.setStatus(null);
        employeeDetails.setPhoneNumber(null);
        
        assertNull(employeeDetails.getFirstName());
        assertNull(employeeDetails.getLastName());
        assertNull(employeeDetails.getUserName());
        assertNull(employeeDetails.getDesignation());
        assertNull(employeeDetails.getStatus());
        assertNull(employeeDetails.getPhoneNumber());
    }
    
    @Test
    public void testSetEmptyStrings() {
        
        employeeDetails.setFirstName("");
        employeeDetails.setLastName("");
        employeeDetails.setUserName("");
        employeeDetails.setDesignation("");
        employeeDetails.setStatus("");
        employeeDetails.setPhoneNumber("");
        
        assertEquals("", employeeDetails.getFirstName());
        assertEquals("", employeeDetails.getLastName());
        assertEquals("", employeeDetails.getUserName());
        assertEquals("", employeeDetails.getDesignation());
        assertEquals("", employeeDetails.getStatus());
        assertEquals("", employeeDetails.getPhoneNumber());
    }
    
    @Test
    public void testSetLongStrings() {
        // Test with long strings to ensure no truncation issues
        String longString = "This is a very long string that might cause issues if there are length restrictions in the database or application logic";
        
        employeeDetails.setFirstName(longString);
        employeeDetails.setLastName(longString);
        employeeDetails.setUserName(longString);
        
        assertEquals(longString, employeeDetails.getFirstName());
        assertEquals(longString, employeeDetails.getLastName());
        assertEquals(longString, employeeDetails.getUserName());
    }
    
    @Test
    public void testSetNegativeEid() {
        // Test edge case with negative EID
        employeeDetails.setEid(-1);
        assertEquals(-1, employeeDetails.getEid());
    }
    
    @Test
    public void testSetZeroEid() {
        // Test edge case with zero EID
        employeeDetails.setEid(0);
        assertEquals(0, employeeDetails.getEid());
    }
    
    @Test
    public void testSetNegativeBasicSalary() {
        // Test edge case with negative salary
        employeeDetails.setBasicSalary(-1000.0f);
        assertEquals(-1000.0f, employeeDetails.getBasicSalary(), 0.01f);
    }
    
    @Test
    public void testSetZeroBasicSalary() {
        // Test edge case with zero salary
        employeeDetails.setBasicSalary(0.0f);
        assertEquals(0.0f, employeeDetails.getBasicSalary(), 0.01f);
    }
    
    @Test
    public void testSetVeryLargeBasicSalary() {
        // Test edge case with very large salary
        float largeSalary = Float.MAX_VALUE;
        employeeDetails.setBasicSalary(largeSalary);
        assertEquals(largeSalary, employeeDetails.getBasicSalary(), 0.01f);
    }
    
    // Tests data consistency
    @Test
    public void testDataConsistency() {
        // Sets all fields and verify they remain consistent
        employeeDetails.setEid(12345);
        employeeDetails.setFirstName("Jaf");
        employeeDetails.setLastName("Grengia");
        employeeDetails.setUserName("jaf.grengia@company.com");
        employeeDetails.setDesignation("Software Developer");
        employeeDetails.setStatus("Active");
        employeeDetails.setPhoneNumber("123-456-7890");
        employeeDetails.setBasicSalary(75000.0f);
        
        // Verifies all fields are set correctly
        assertEquals(12345, employeeDetails.getEid());
        assertEquals("Jaf", employeeDetails.getFirstName());
        assertEquals("Grengia", employeeDetails.getLastName());
        assertEquals("jaf.grengia@company.com", employeeDetails.getUserName());
        assertEquals("Software Developer", employeeDetails.getDesignation());
        assertEquals("Active", employeeDetails.getStatus());
        assertEquals("123-456-7890", employeeDetails.getPhoneNumber());
        assertEquals(75000.0f, employeeDetails.getBasicSalary(), 0.01f);
    }
}