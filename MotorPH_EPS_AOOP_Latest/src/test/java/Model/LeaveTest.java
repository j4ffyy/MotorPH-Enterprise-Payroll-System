package Model;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LeaveTest {
    
    public LeaveTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getEid method, of class Leave.
     */
    @Test
    public void testGetEid() {
        System.out.println("getEid");
        int expResult = 10001;
        Leave instance = new Leave(10001, "L1001", new Date(), new Date(), new Date(), "Vacation", "Approved");
        int result = instance.getEid();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLeaveId method, of class Leave.
     */
    @Test
    public void testGetLeaveId() {
        System.out.println("getLeaveId");
        String expResult = "L1001";
        Leave instance = new Leave(10001, "L1001", new Date(), new Date(), new Date(), "Vacation", "Approved");
        String result = instance.getLeaveId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDateFiled method, of class Leave.
     */
    @Test
    public void testGetDateFiled() {
        System.out.println("getDateFiled");
        Date expResult = new Date();
        Leave instance = new Leave(10001, "L1001", expResult, new Date(), new Date(), "Vacation", "Approved");
        Date result = instance.getDateFiled();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDateFrom method, of class Leave.
     */
    @Test
    public void testGetDateFrom() {
        System.out.println("getDateFrom");
        Date expResult = new Date();
        Leave instance = new Leave(10001, "L1001", new Date(), expResult, new Date(), "Vacation", "Approved");
        Date result = instance.getDateFrom();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDateTo method, of class Leave.
     */
    @Test
    public void testGetDateTo() {
        System.out.println("getDateTo");
        Date expResult = new Date();
        Leave instance = new Leave(10001, "L1001", new Date(), new Date(), expResult, "Vacation", "Approved");
        Date result = instance.getDateTo();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReasonForLeave method, of class Leave.
     */
    @Test
    public void testGetReasonForLeave() {
        System.out.println("getReasonForLeave");
        String expResult = "Vacation";
        Leave instance = new Leave(10001, "L1001", new Date(), new Date(), new Date(), expResult, "Approved");
        String result = instance.getReasonForLeave();
        assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class Leave.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        String expResult = "Approved";
        Leave instance = new Leave(10001, "L1001", new Date(), new Date(), new Date(), "Vacation", expResult);
        String result = instance.getLeaveStatus();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNotes method, of class Leave.
     */
    @Test
    public void testGetNotes() {
        System.out.println("getNotes");
        String expResult = "Some notes";
        Leave instance = new Leave(10001, "L1001", new Date(), new Date(), new Date(), "Vacation", "Approved");
        instance.setNotes(expResult);
        String result = instance.getNotes();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEid method, of class Leave.
     */
    @Test
    public void testSetEid() {
        System.out.println("setEid");
        int eid = 10001;
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setEid(eid);
        assertEquals(eid, instance.getEid());
    }

    /**
     * Test of setLeaveId method, of class Leave.
     */
    @Test
    public void testSetLeaveId() {
        System.out.println("setLeaveId");
        String leaveId = "L1001";
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setLeaveId(leaveId);
        assertEquals(leaveId, instance.getLeaveId());
    }

    /**
     * Test of setDateFiled method, of class Leave.
     */
    @Test
    public void testSetDateFiled() {
        System.out.println("setDateFiled");
        Date dateFiled = new Date();
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setDateFiled(dateFiled);
        assertEquals(dateFiled, instance.getDateFiled());
    }

    /**
     * Test of setDateFrom method, of class Leave.
     */
    @Test
    public void testSetDateFrom() {
        System.out.println("setDateFrom");
        Date dateFrom = new Date();
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setDateFrom(dateFrom);
        assertEquals(dateFrom, instance.getDateFrom());
    }

    /**
     * Test of setDateTo method, of class Leave.
     */
    @Test
    public void testSetDateTo() {
        System.out.println("setDateTo");
        Date dateTo = new Date();
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setDateTo(dateTo);
        assertEquals(dateTo, instance.getDateTo());
    }

    /**
     * Test of setReasonForLeave method, of class Leave.
     */
    @Test
    public void testSetReasonForLeave() {
        System.out.println("setReasonForLeave");
        String reasonForLeave = "Vacation";
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setReasonForLeave(reasonForLeave);
        assertEquals(reasonForLeave, instance.getReasonForLeave());
    }

    /**
     * Test of setStatus method, of class Leave.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        String status = "Approved";
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setLeaveStatus(status);
        assertEquals(status, instance.getLeaveStatus());
    }

    /**
     * Test of setNotes method, of class Leave.
     */
    @Test
    public void testSetNotes() {
        System.out.println("setNotes");
        String notes = "Some notes";
        Leave instance = new Leave(0, "", new Date(), new Date(), new Date(), "", "");
        instance.setNotes(notes);
        assertEquals(notes, instance.getNotes());
    }
}