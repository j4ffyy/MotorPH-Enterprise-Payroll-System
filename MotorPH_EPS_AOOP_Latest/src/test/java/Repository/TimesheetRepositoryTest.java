package Repository;

import org.junit.Test;
import static org.junit.Assert.*;

public class TimesheetRepositoryTest {

    @Test
    public void testGetAllTimesheets() {
        TimesheetRepository repository = new TimesheetRepository();
        try {
            assertNotNull("List of timesheets should not be null", repository.getAllTimesheets());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}