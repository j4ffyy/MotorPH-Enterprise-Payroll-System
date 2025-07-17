package Repository;

import org.junit.Test;
import static org.junit.Assert.*;

public class LeaveRepositoryTest {

    @Test
    public void testGetAllLeaves() {
        LeaveRepository repository = new LeaveRepository();
        try {
            assertNotNull("List of leaves should not be null", repository.getAllLeaves());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}