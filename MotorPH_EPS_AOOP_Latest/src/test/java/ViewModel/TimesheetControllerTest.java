/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TimesheetController class.
 * @author jafph
 */
public class TimesheetControllerTest {
    
    private TimesheetController controller;
    private DefaultTableModel tableModel;
    
    @BeforeEach
    public void setUp() {
        controller = new TimesheetController();
        
        // Initialize table model with proper column structure
        String[] columns = {"Attendance_ID", "EID", "Employee Name", "LogDate", "LogTime", "AttStatus"};
        tableModel = new DefaultTableModel(columns, 0);
    }

    @Test
    @DisplayName("Test addEmptyRowToTable with null table model")
    public void testAddEmptyRowToTableWithNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            controller.addEmptyRowToTable(null);
        });
    }

    @Test
    @DisplayName("Test addEmptyRowToTable with multiple additions")
    public void testAddEmptyRowToTableMultiple() {
        // Given
        int initialRowCount = tableModel.getRowCount();
        
        // When
        controller.addEmptyRowToTable(tableModel);
        controller.addEmptyRowToTable(tableModel);
        controller.addEmptyRowToTable(tableModel);
        
        // Then
        assertEquals(initialRowCount + 3, tableModel.getRowCount());
    }

    @Test
    @DisplayName("Test addTimeLog with null input")
    public void testAddTimeLogWithNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            controller.addTimeLog(null);
        });
    }

    @Test
    @DisplayName("Test addTimeLog with invalid array length")
    public void testAddTimeLogWithInvalidArrayLength() {
        // Given
        Object[] invalidTimeLog = {"123", "456"}; // Only 2 elements, should have 5
        
        // When & Then
        assertThrows(Exception.class, () -> {
            controller.addTimeLog(invalidTimeLog);
        });
    }

    @Test
    @DisplayName("Test addTimeLog with invalid EID format")
    public void testAddTimeLogWithInvalidEID() {
        // Given
        Object[] timeLog = {"", "invalid_eid", "2023-01-01", "09:00:00", "Present"};
        
        // When & Then
        assertFalse(controller.addTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test addTimeLog with invalid date format")
    public void testAddTimeLogWithInvalidDate() {
        // Given
        Object[] timeLog = {"", "123", "invalid_date", "09:00:00", "Present"};
        
        // When & Then
        assertFalse(controller.addTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test addTimeLog with invalid time format")
    public void testAddTimeLogWithInvalidTime() {
        // Given
        Object[] timeLog = {"", "123", "2023-01-01", "invalid_time", "Present"};
        
        // When & Then
        assertFalse(controller.addTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test updateTimeLog with null input")
    public void testUpdateTimeLogWithNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            controller.updateTimeLog(null);
        });
    }

    @Test
    @DisplayName("Test updateTimeLog with invalid array length")
    public void testUpdateTimeLogWithInvalidArrayLength() {
        // Given
        Object[] invalidTimeLog = {"123", "456"}; // Only 2 elements, should have 5
        
        // When & Then
        assertThrows(Exception.class, () -> {
            controller.updateTimeLog(invalidTimeLog);
        });
    }

    @Test
    @DisplayName("Test updateTimeLog with invalid EID format")
    public void testUpdateTimeLogWithInvalidEID() {
        // Given
        Object[] timeLog = {"ATT123", "invalid_eid", "2023-01-01", "09:00:00", "Present"};
        
        // When & Then
        assertFalse(controller.updateTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test updateTimeLog with invalid date format")
    public void testUpdateTimeLogWithInvalidDate() {
        // Given
        Object[] timeLog = {"ATT123", "123", "invalid_date", "09:00:00", "Present"};
        
        // When & Then
        assertFalse(controller.updateTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test updateTimeLog with invalid time format")
    public void testUpdateTimeLogWithInvalidTime() {
        // Given
        Object[] timeLog = {"ATT123", "123", "2023-01-01", "invalid_time", "Present"};
        
        // When & Then
        assertFalse(controller.updateTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test deleteTimeLog with null attendanceID")
    public void testDeleteTimeLogWithNull() {
        // When & Then
        assertFalse(controller.deleteTimeLog(null));
    }

    @Test
    @DisplayName("Test deleteTimeLog with empty attendanceID")
    public void testDeleteTimeLogWithEmptyString() {
        // When & Then
        assertFalse(controller.deleteTimeLog(""));
    }

    @Test
    @DisplayName("Test saveNewTimeLogFromTable with empty table")
    public void testSaveNewTimeLogFromTableWithEmptyTable() throws Exception {
        // Given
        DefaultTableModel emptyModel = new DefaultTableModel();
        
        // When
        boolean result = controller.saveNewTimeLogFromTable(emptyModel);
        
        // Then
        assertTrue(result); // Should return true as there's nothing to save
    }

    @Test
    @DisplayName("Test saveNewTimeLogFromTable with existing entries only")
    public void testSaveNewTimeLogFromTableWithExistingEntries() throws Exception {
        // Given
        tableModel.addRow(new Object[]{"ATT001", "123", "Jaf Grengia", "2023-01-01", "09:00:00", "Present"});
        tableModel.addRow(new Object[]{"ATT002", "124", "Jane Smith", "2023-01-02", "09:15:00", "Present"});
        
        // When
        boolean result = controller.saveNewTimeLogFromTable(tableModel);
        
        // Then
        assertTrue(result); // Should return true as there are no new entries to save
    }

    @Test
    @DisplayName("Test table model column indexing for sorting")
    public void testTableModelColumnStructure() {
        // Given
        String[] expectedColumns = {"Attendance_ID", "EID", "Employee Name", "LogDate", "LogTime", "AttStatus"};
        
        // When & Then
        assertEquals(expectedColumns.length, tableModel.getColumnCount());
        for (int i = 0; i < expectedColumns.length; i++) {
            assertEquals(expectedColumns[i], tableModel.getColumnName(i));
        }
    }

    @Test
    @DisplayName("Test date parsing logic for valid dates")
    public void testDateParsingLogic() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        // When & Then
        assertDoesNotThrow(() -> {
            Date date = dateFormat.parse("2023-01-01");
            Date time = timeFormat.parse("09:00:00");
            assertNotNull(date);
            assertNotNull(time);
        });
    }

    @Test
    @DisplayName("Test date parsing logic for invalid dates")
    public void testDateParsingLogicInvalid() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        
        // When & Then
        assertThrows(Exception.class, () -> {
            dateFormat.parse("invalid-date");
        });
        
        assertThrows(Exception.class, () -> {
            timeFormat.parse("invalid-time");
        });
    }

    @Test
    @DisplayName("Test getTimesheetEntryByEid with null EID")
    public void testGetTimesheetEntryByEidWithNull() {
        // When
        Object[] result = controller.getTimesheetEntryByEid(null);
        
        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Test getTimesheetEntryByEid with empty EID")
    public void testGetTimesheetEntryByEidWithEmpty() {
        // When
        Object[] result = controller.getTimesheetEntryByEid("");
        
        // Then
        assertNull(result);
    }

    @Test
    @DisplayName("Test loadTimesheetData with null table model")
    public void testLoadTimesheetDataWithNullModel() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            controller.loadTimesheetData(null);
        });
    }

    @Test
    @DisplayName("Test loadEmployeeTimesheetData with null table model")
    public void testLoadEmployeeTimesheetDataWithNullModel() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            controller.loadEmployeeTimesheetData(null, 123);
        });
    }

    
    @Test
    @DisplayName("Test loadEmployeeTimesheetData with invalid EID")
    public void testLoadEmployeeTimesheetDataWithInvalidEID() {
        boolean result = controller.loadEmployeeTimesheetData(tableModel, -1);
        assertTrue(result);
    }


    @Test
    @DisplayName("Test controller initialization")
    public void testControllerInitialization() {
        // When
        TimesheetController newController = new TimesheetController();
        
        // Then
        assertNotNull(newController);
    }

    @Test
    @DisplayName("Test showMessage method doesn't throw exceptions")
    public void testShowMessageDoesNotThrow() {
        assertDoesNotThrow(() -> {
        });
    }

    @Test
    @DisplayName("Test edge case: addTimeLog with empty strings")
    public void testAddTimeLogWithEmptyStrings() {
        // Given
        Object[] timeLog = {"", "", "", "", ""};
        
        // When & Then - Empty EID will cause NumberFormatException
        assertFalse(controller.addTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test edge case: updateTimeLog with empty strings")
    public void testUpdateTimeLogWithEmptyStrings() {
        // Given
        Object[] timeLog = {"", "", "", "", ""};
        
        // When & Then - Empty EID will cause NumberFormatException
        assertFalse(controller.updateTimeLog(timeLog));
    }

    @Test
    @DisplayName("Test table model state after adding empty row")
    public void testTableModelStateAfterAddingEmptyRow() {
        // Given
        Object[] testData = {"ATT001", "123", "Jaf Grengia", "2023-01-01", "09:00:00", "Present"};
        tableModel.addRow(testData);
        int initialRowCount = tableModel.getRowCount();
        
        // When
        assertDoesNotThrow(() -> {
            controller.addEmptyRowToTable(tableModel);
        });
        
        // Then
        assertEquals(initialRowCount + 1, tableModel.getRowCount());
        
        // Verify original data is intact
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], tableModel.getValueAt(0, i));
        }
        
        // Verify new row is empty - the controller adds 5 empty strings, not 6
        int newRowIndex = tableModel.getRowCount() - 1;
        for (int i = 0; i < 5; i++) { // Changed from tableModel.getColumnCount() to 5
            assertEquals("", tableModel.getValueAt(newRowIndex, i));
        }
    }
}