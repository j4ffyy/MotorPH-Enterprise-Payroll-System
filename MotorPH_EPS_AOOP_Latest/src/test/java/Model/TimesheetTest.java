package Model;

import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TimesheetTest {

    private Timesheet timesheet;
    private final int TIMESHEET_ID = 1;
    private final int EID = 10001;
    private final Date DATE = new Date();
    private final String TIME_IN = "08:00 AM";
    private final String TIME_OUT = "05:00 PM";

    @BeforeEach
    public void setUp() {
        timesheet = new Timesheet(TIMESHEET_ID, EID, DATE, TIME_IN, TIME_OUT);
    }

    @Test
    public void testGetTimesheetID() {
        System.out.println("getTimesheetID");
        assertEquals(TIMESHEET_ID, timesheet.getTimesheetID());
    }

    @Test
    public void testSetTimesheetID() {
        System.out.println("setTimesheetID");
        int newTimesheetID = 2;
        timesheet.setTimesheetID(newTimesheetID);
        assertEquals(newTimesheetID, timesheet.getTimesheetID());
    }

    @Test
    public void testGetEid() {
        System.out.println("getEid");
        assertEquals(EID, timesheet.getEid());
    }

    @Test
    public void testSetEid() {
        System.out.println("setEid");
        int newEid = 10002;
        timesheet.setEid(newEid);
        assertEquals(newEid, timesheet.getEid());
    }

    @Test
    public void testGetDate() {
        System.out.println("getDate");
        assertEquals(DATE, timesheet.getDate());
    }

    @Test
    public void testSetDate() {
        System.out.println("setDate");
        Date newDate = new Date();
        timesheet.setDate(newDate);
        assertEquals(newDate, timesheet.getDate());
    }

    @Test
    public void testGetTimeIn() {
        System.out.println("getTimeIn");
        assertEquals(TIME_IN, timesheet.getTimeIn());
    }

    @Test
    public void testSetTimeIn() {
        System.out.println("setTimeIn");
        String newTimeIn = "09:00 AM";
        timesheet.setTimeIn(newTimeIn);
        assertEquals(newTimeIn, timesheet.getTimeIn());
    }

    @Test
    public void testGetTimeOut() {
        System.out.println("getTimeOut");
        assertEquals(TIME_OUT, timesheet.getTimeOut());
    }

    @Test
    public void testSetTimeOut() {
        System.out.println("setTimeOut");
        String newTimeOut = "06:00 PM";
        timesheet.setTimeOut(newTimeOut);
        assertEquals(newTimeOut, timesheet.getTimeOut());
    }
}