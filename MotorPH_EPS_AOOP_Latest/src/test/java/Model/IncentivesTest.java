package Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IncentivesTest {
    
    public IncentivesTest() {
    }
    
    @Before
    public void setUp() {
    }

    @Test
    public void testGetEid() {
        System.out.println("getEid");
        Incentives instance = new Incentives(10001, 1071.42f, 2f, 5000f, 8571.36f, 14642.78f);
        int expResult = 10001;
        int result = instance.getEid();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetOverTimePay() {
        System.out.println("getOverTimePay");
        Incentives instance = new Incentives(10001, 1071.42f, 2f, 5000f, 8571.36f, 14642.78f);
        float expResult = 1071.42f;
        float result = instance.getOverTimePay();
        assertEquals(expResult, result, 0.001f);
    }

    @Test
    public void testGetOverTime() {
        System.out.println("getOverTime");
        Incentives instance = new Incentives(10001, 1071.42f, 2f, 5000f, 8571.36f, 14642.78f);
        float expResult = 2f;
        float result = instance.getOverTime();
        assertEquals(expResult, result, 0.001f);
    }

    @Test
    public void testGetPerformanceBonus() {
        System.out.println("getPerformanceBonus");
        Incentives instance = new Incentives(10001, 1071.42f, 2f, 5000f, 8571.36f, 14642.78f);
        float expResult = 5000f;
        float result = instance.getPerformanceBonus();
        assertEquals(expResult, result, 0.001f);
    }

    @Test
    public void testGetHolidayPay() {
        System.out.println("getHolidayPay");
        Incentives instance = new Incentives(10001, 1071.42f, 2f, 5000f, 8571.36f, 14642.78f);
        float expResult = 8571.36f;
        float result = instance.getHolidayPay();
        assertEquals(expResult, result, 0.001f);
    }

    @Test
    public void testGetTotalIncentives() {
        System.out.println("getTotalIncentives");
        Incentives instance = new Incentives(10001, 1071.42f, 2f, 5000f, 8571.36f, 14642.78f);
        float expResult = 14642.78f;
        float result = instance.getTotalIncentives();
        assertEquals(expResult, result, 0.001f);
    }

    @Test
    public void testSetEid() {
        System.out.println("setEid");
        int eid = 10001;
        Incentives instance = new Incentives();
        instance.setEid(eid);
        assertEquals(eid, instance.getEid());
    }

    @Test
    public void testSetOverTimePay() {
        System.out.println("setOverTimePay");
        float overTimePay = 1071.42f;
        Incentives instance = new Incentives();
        instance.setOverTimePay(overTimePay);
        assertEquals(overTimePay, instance.getOverTimePay(), 0.001f);
    }

    @Test
    public void testSetOverTime() {
        System.out.println("setOverTime");
        float overTime = 2f;
        Incentives instance = new Incentives();
        instance.setOverTime(overTime);
        assertEquals(overTime, instance.getOverTime(), 0.001f);
    }

    @Test
    public void testSetPerformanceBonus() {
        System.out.println("setPerformanceBonus");
        float performanceBonus = 5000f;
        Incentives instance = new Incentives();
        instance.setPerformanceBonus(performanceBonus);
        assertEquals(performanceBonus, instance.getPerformanceBonus(), 0.001f);
    }

    @Test
    public void testSetHolidayPay() {
        System.out.println("setHolidayPay");
        float holidayPay = 8571.36f;
        Incentives instance = new Incentives();
        instance.setHolidayPay(holidayPay);
        assertEquals(holidayPay, instance.getHolidayPay(), 0.001f);
    }

    @Test
    public void testSetTotalIncentives() {
        System.out.println("setTotalIncentives");
        float totalIncentives = 14642.78f;
        Incentives instance = new Incentives();
        instance.setTotalIncentives(totalIncentives);
        assertEquals(totalIncentives, instance.getTotalIncentives(), 0.001f);
    }
}