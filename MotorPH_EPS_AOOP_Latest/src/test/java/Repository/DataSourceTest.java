package Repository;

import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;

public class DataSourceTest {

    @Test
    public void testGetConnection() {
        try {
            Connection connection = InMemoryDB.getInstance().getConnection();
            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should be open", connection.isClosed());
            connection.close();
        } catch (Exception e) {
            fail("Failed to get or close connection: " + e.getMessage());
        }
    }
}