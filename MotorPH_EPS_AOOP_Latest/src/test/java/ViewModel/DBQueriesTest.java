/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ViewModel;

/**
 *
 * @author dashcodes
 */

import java.sql.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DBQueriesTest {
    
    private DBQueries dbQueries;
    
    @Before
    public void setUp() {
        dbQueries = new DBQueries();
    }

    @Test
    public void testCreateSortQuery_Ascending() {
        String columnName = "lastName";
        String expected = "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, e.Address, e.Phone_Number, g.SSS_Num, g.Philhealth_Num, g.TIN_Num, g.Pagibig_Num, d.Designation_Name, e.Status, epc.Basic_Salary FROM employee e JOIN designations d ON e.Designation_ID = d.Designation_ID LEFT JOIN employee_government_ids g ON e.EID = g.EID JOIN employee_payroll_components epc ON e.EID = epc.EID ORDER BY lastName ASC";
        String result = dbQueries.createSortQuery(columnName, true);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateSortQuery_Descending() {
        String columnName = "firstName";
        String expected = "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, e.Address, e.Phone_Number, g.SSS_Num, g.Philhealth_Num, g.TIN_Num, g.Pagibig_Num, d.Designation_Name, e.Status, epc.Basic_Salary FROM employee e JOIN designations d ON e.Designation_ID = d.Designation_ID LEFT JOIN employee_government_ids g ON e.EID = g.EID JOIN employee_payroll_components epc ON e.EID = epc.EID ORDER BY firstName DESC";
        String result = dbQueries.createSortQuery(columnName, false);
        assertEquals(expected, result);
    }

    @Test
    public void testConvertStringToSqlDate_ValidFormat1() {
        String dateString = "12/31/2023";
        Date result = dbQueries.convertStringToSqlDate(dateString);
        assertNotNull(result);
        assertEquals("2023-12-31", result.toString());
    }

    @Test
    public void testConvertStringToSqlDate_ValidFormat2() {
        String dateString = "01/15/2023";
        Date result = dbQueries.convertStringToSqlDate(dateString);
        assertNotNull(result);
        assertEquals("2023-01-15", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertStringToSqlDate_InvalidFormat() {
        String dateString = "31/12/2023";
        dbQueries.convertStringToSqlDate(dateString);
    }

    @Test
    public void testGetQuery_KnownKey() {
        String result = dbQueries.getQuery("GET_ALL_LEAVES");
        assertEquals(dbQueries.getAllLeaves, result);
    }

    @Test
    public void testGetQuery_UnknownKey() {
        // Test that getQuery returns null or empty string for unknown keys
        // instead of expecting an exception
        String result = dbQueries.getQuery("UNKNOWN_QUERY");
        assertTrue("Expected null or empty string for unknown query key", 
                   result == null || result.isEmpty());
    }
}