package Repository;

import Model.EmployeeDetails;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmployeeRepositoryTest {

    @Test
    public void testGetEmployeeByUsername() {
        EmployeeRepository repository = new EmployeeRepository();
        // Assuming 'mgarcia@motorph.com' is a valid username in your test database
        // This test will pass if the method doesn't throw an exception and returns a non-null object
        // A more robust test would involve mocking the database or setting up known test data
        try {
            EmployeeDetails employee = repository.getEmployeeByUsername("mgarcia@motorph.com");
            assertNotNull("Employee should not be null for a valid username", employee);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetEmployeeByEid() {
        EmployeeRepository repository = new EmployeeRepository();
        // Assuming '10001' is a valid EID in your test database
        try {
            EmployeeDetails employee = repository.getEmployeeByEid("10001");
            assertNotNull("Employee should not be null for a valid EID", employee);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}