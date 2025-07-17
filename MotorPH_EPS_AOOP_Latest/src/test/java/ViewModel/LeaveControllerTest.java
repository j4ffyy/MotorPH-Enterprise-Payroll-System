package ViewModel;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;
import Repository.TestUnitFactory.TestDateChooser;
import Repository.TestUnitFactory.TestComboBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class LeaveControllerTest {

    private LeaveController leaveController;

    @Before
    public void setUp() {
        leaveController = new LeaveController();
    }

    // --- Test Cases for setLoggedInUser ---
    @Test
    public void testSetLoggedInUser() throws NoSuchFieldException, IllegalAccessException {
        String expectedUsername = "testUser";
        int expectedEid = 12345;

        leaveController.setLoggedInUser(expectedUsername, expectedEid);

        // Use reflection to access private fields for verification
        java.lang.reflect.Field usernameField = LeaveController.class.getDeclaredField("loggedInUsername");
        usernameField.setAccessible(true);
        String actualUsername = (String) usernameField.get(leaveController);

        java.lang.reflect.Field eidField = LeaveController.class.getDeclaredField("loggedInEid");
        eidField.setAccessible(true);
        int actualEid = (int) eidField.get(leaveController);

        assertEquals("Logged-in username should match", expectedUsername, actualUsername);
        assertEquals("Logged-in EID should match", expectedEid, actualEid);
    }

    // --- Test Cases for getCurrentDate ---
    @Test
    public void testGetCurrentDate() {
        String currentDateString = leaveController.getCurrentDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String expectedDateString = dateFormat.format(new Date()); // Format current system date

        assertEquals("Current date string should match MM-dd-yyyy format", expectedDateString, currentDateString);
    }

    // --- Test Cases for validateLeaveForm ---
    @Test
    public void testValidateLeaveForm_AllValid() {
        TestDateChooser dateFrom = new TestDateChooser();
        dateFrom.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        TestDateChooser dateTo = new TestDateChooser();
        dateTo.setDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        TestComboBox<String> reasonCombo = new TestComboBox<>(new String[]{"< Select one >", "Sick Leave", "Vacation Leave"});
        reasonCombo.setSelectedIndex(1); // Select a valid reason

        // Use reflection to access the private validateLeaveForm method
        try {
            java.lang.reflect.Method method = LeaveController.class.getDeclaredMethod("validateLeaveForm", JDateChooser.class, JDateChooser.class, JComboBox.class);
            method.setAccessible(true);
            boolean isValid = (boolean) method.invoke(leaveController, dateFrom, dateTo, reasonCombo);
            assertTrue("Form should be valid when all fields are filled", isValid);
        } catch (Exception e) {
            fail("Exception thrown when validating form: " + e.getMessage());
        }
    }

    @Test
    public void testValidateLeaveForm_DateFromNull() {
        TestDateChooser dateFrom = new TestDateChooser();
        dateFrom.setDate(null); // Date From is null

        TestDateChooser dateTo = new TestDateChooser();
        dateTo.setDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        TestComboBox<String> reasonCombo = new TestComboBox<>(new String[]{"< Select one >", "Sick Leave", "Vacation Leave"});
        reasonCombo.setSelectedIndex(1);

        try {
            java.lang.reflect.Method method = LeaveController.class.getDeclaredMethod("validateLeaveForm", JDateChooser.class, JDateChooser.class, JComboBox.class);
            method.setAccessible(true);
            boolean isValid = (boolean) method.invoke(leaveController, dateFrom, dateTo, reasonCombo);
            assertFalse("Form should be invalid when Date From is null", isValid);
        } catch (Exception e) {
            fail("Exception thrown when validating form: " + e.getMessage());
        }
    }

    @Test
    public void testValidateLeaveForm_ReasonNotSelected() {
        TestDateChooser dateFrom = new TestDateChooser();
        dateFrom.setDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        TestDateChooser dateTo = new TestDateChooser();
        dateTo.setDate(Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        TestComboBox<String> reasonCombo = new TestComboBox<>(new String[]{"< Select one >", "Sick Leave", "Vacation Leave"});
        reasonCombo.setSelectedIndex(0); // "< Select one >" selected

        try {
            java.lang.reflect.Method method = LeaveController.class.getDeclaredMethod("validateLeaveForm", JDateChooser.class, JDateChooser.class, JComboBox.class);
            method.setAccessible(true);
            boolean isValid = (boolean) method.invoke(leaveController, dateFrom, dateTo, reasonCombo);
            assertFalse("Form should be invalid when no reason is selected", isValid);
        } catch (Exception e) {
            fail("Exception thrown when validating form: " + e.getMessage());
        }
    }

    // --- Test Cases for clearLeaveForm ---
    @Test
    public void testClearLeaveForm() {
        TestDateChooser dateFrom = new TestDateChooser();
        dateFrom.setDate(new Date());

        TestDateChooser dateTo = new TestDateChooser();
        dateTo.setDate(new Date());

        TestComboBox<String> reasonCombo = new TestComboBox<>(new String[]{"< Select one >", "Sick Leave"});
        reasonCombo.setSelectedIndex(1);

        leaveController.clearLeaveForm(dateFrom, dateTo, reasonCombo);

        assertNull("Date From should be null after clearing", dateFrom.getDate());
        assertNull("Date To should be null after clearing", dateTo.getDate());
        assertEquals("Reason combo box should be reset to index 0", 0, reasonCombo.getSelectedIndex());
    }

    // --- Test Cases for sortLeavesTable ---
    @Test
    public void testSortLeavesTable_Ascending() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{
                {"L003", 1003, "Charlie", new Date(), new Date(), new Date(), "Vacation", "Approved"},
                {"L001", 1001, "Alice", new Date(), new Date(), new Date(), "Sick", "Pending"},
                {"L002", 1002, "Bob", new Date(), new Date(), new Date(), "Personal", "Rejected"}
            },
            new Object[]{"Leave ID", "EID", "Employee Name", "Date Filed", "Date From", "Date To", "Reason", "Status"}
        );
        JTable table = new JTable(model);

        leaveController.sortLeavesTable(table, "Employee Name", true);

        assertEquals("Alice", table.getValueAt(0, 2));
        assertEquals("Bob", table.getValueAt(1, 2));
        assertEquals("Charlie", table.getValueAt(2, 2));
    }

    @Test
    public void testSortLeavesTable_Descending() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{
                {"L003", 1003, "Charlie", new Date(), new Date(), new Date(), "Vacation", "Approved"},
                {"L001", 1001, "Alice", new Date(), new Date(), new Date(), "Sick", "Pending"},
                {"L002", 1002, "Bob", new Date(), new Date(), new Date(), "Personal", "Rejected"}
            },
            new Object[]{"Leave ID", "EID", "Employee Name", "Date Filed", "Date From", "Date To", "Reason", "Status"}
        );
        JTable table = new JTable(model);

        leaveController.sortLeavesTable(table, "Employee Name", false);

        assertEquals("Charlie", table.getValueAt(0, 2));
        assertEquals("Bob", table.getValueAt(1, 2));
        assertEquals("Alice", table.getValueAt(2, 2));
    }

    @Test
    public void testSortLeavesTable_ColumnNotFound() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{
                {"L001", 1001, "Alice", new Date(), new Date(), new Date(), "Sick", "Pending"}
            },
            new Object[]{"Leave ID", "EID", "Employee Name", "Date Filed", "Date From", "Date To", "Reason", "Status"}
        );
        JTable table = new JTable(model);

        // We can't easily test JOptionPane.showMessageDialog directly in a unit test
        // without mocking, so we just ensure no exception is thrown.
        // The message dialog is a side effect and is difficult to assert on without mocks.
        leaveController.sortLeavesTable(table, "NonExistentColumn", true);
        // Assert that the table model is unchanged or handles it gracefully
        assertEquals(1, table.getRowCount()); // Still has one row
    }

    @Test
    public void testSortLeavesTable_SortByEID() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[][]{
                {"L003", 1003, "Charlie", new Date(), new Date(), new Date(), "Vacation", "Approved"},
                {"L001", 1001, "Alice", new Date(), new Date(), new Date(), "Sick", "Pending"},
                {"L002", 1002, "Bob", new Date(), new Date(), new Date(), "Personal", "Rejected"}
            },
            new Object[]{"Leave ID", "EID", "Employee Name", "Date Filed", "Date From", "Date To", "Reason", "Status"}
        );
        JTable table = new JTable(model);

        leaveController.sortLeavesTable(table, "EID", true); // Sort by EID ascending

        assertEquals(1001, table.getValueAt(0, 1));
        assertEquals(1002, table.getValueAt(1, 1));
        assertEquals(1003, table.getValueAt(2, 1));
    }
}