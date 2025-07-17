package Model;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkTrackerTest {
    
    public WorkTrackerTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of addWorkTime method, of class WorkTracker.
     */
    @Test
    public void testAddWorkTime() {
        System.out.println("addWorkTime");
        LocalDate date = LocalDate.of(2025, 7, 1);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        WorkTracker instance = new WorkTracker();
        instance.addWorkTime(date, startTime, endTime);
        // Test passes if no exception is thrown
    }

    /**
     * Test of calculateMinutesWorked method, of class WorkTracker.
     */
    @Test
    public void testCalculateMinutesWorked() {
        System.out.println("calculateMinutesWorked");
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        WorkTracker instance = new WorkTracker();
        long expResult = 8 * 60; // 480 minutes
        long result = instance.calculateMinutesWorked(startTime, endTime);
        assertEquals(expResult, result);
    }

    /**
     * Test of calculateMonthlyWorkHours method, of class WorkTracker.
     */
    @Test
    public void testCalculateMonthlyWorkHours() {
        System.out.println("calculateMonthlyWorkHours");
        int month = 7;
        int year = 2025;
        WorkTracker instance = new WorkTracker();
        
        // Adding three work days with 8 hours each (after lunch deduction)
        LocalDate date1 = LocalDate.of(2025, 7, 1);
        instance.addWorkTime(date1, LocalTime.of(9, 0), LocalTime.of(17, 0)); // 8h
        LocalDate date2 = LocalDate.of(2025, 7, 2);
        instance.addWorkTime(date2, LocalTime.of(8, 0), LocalTime.of(16, 0)); // 8h
        LocalDate date3 = LocalDate.of(2025, 7, 3);
        instance.addWorkTime(date3, LocalTime.of(10, 0), LocalTime.of(18, 0)); // 8h
        
        instance.calculateMonthlyWorkHours(month, year);
        
        // Each day: 8h * 60 = 480 minutes, minus 60 for lunch = 420
        // Total minutes: 3 * 420 = 1260
        // Undertime per day: 480 - 420 = 60, total undertime 3*60=180
        assertEquals(1260, instance.calculateMinutesWorked);
        assertEquals(0, instance.calculateOvertimeMinutes);
        assertEquals(180, instance.calculateUndertimeMinutes);
    }
}