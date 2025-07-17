/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Model;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jafph
 */
public class DayOfMonthTest {
    
    public DayOfMonthTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getFirstDayOfMonth method, of class DayOfMonth.
     */
    @Test
    public void testGetFirstDayOfMonth() {
        System.out.println("getFirstDayOfMonth");
        int month = 1;  // January
        int year = 2024; // Leap year
        LocalDate expResult = LocalDate.of(2024, 1, 1);
        LocalDate result = DayOfMonth.getFirstDayOfMonth(month, year);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLastDayOfMonth method, of class DayOfMonth.
     */
    @Test
    public void testGetLastDayOfMonth() {
        System.out.println("getLastDayOfMonth");
        int month = 2;  // February
        int year = 2024; // Leap year
        LocalDate expResult = LocalDate.of(2024, 2, 29);
        LocalDate result = DayOfMonth.getLastDayOfMonth(month, year);
        assertEquals(expResult, result);
    }
    
}
