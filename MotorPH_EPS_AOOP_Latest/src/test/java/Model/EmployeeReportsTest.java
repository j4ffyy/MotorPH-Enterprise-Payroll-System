package Model;

import java.math.BigDecimal;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmployeeReportsTest {
    
    private EmployeeReports instance;
    
    @Before
    public void setUp() {
        instance = new EmployeeReports();
    }

    @Test
    public void testGetEid() {
        assertEquals(0, instance.getEid());
    }

    @Test
    public void testGetLastName() {
        assertNull(instance.getLastName());
    }

    @Test
    public void testGetFirstName() {
        assertNull(instance.getFirstName());
    }

    @Test
    public void testGetBirthday() {
        assertNull(instance.getBirthday());
    }

    @Test
    public void testGetAddress() {
        assertNull(instance.getAddress());
    }

    @Test
    public void testGetPhoneNumber() {
        assertNull(instance.getPhoneNumber());
    }

    @Test
    public void testGetUsername() {
        assertNull(instance.getUsername());
    }

    @Test
    public void testGetStatus() {
        assertNull(instance.getStatus());
    }

    @Test
    public void testGetDesignation() {
        assertNull(instance.getDesignation());
    }

    @Test
    public void testGetImmediateSupervisor() {
        assertNull(instance.getImmediateSupervisor());
    }

    @Test
    public void testGetSssNumber() {
        assertNull(instance.getSssNumber());
    }

    @Test
    public void testGetPhilhealthNumber() {
        assertNull(instance.getPhilhealthNumber());
    }

    @Test
    public void testGetTinNumber() {
        assertNull(instance.getTinNumber());
    }

    @Test
    public void testGetPagibigNumber() {
        assertNull(instance.getPagibigNumber());
    }

    @Test
    public void testGetBasicSalary() {
        assertNull(instance.getBasicSalary());
    }

    @Test
    public void testGetRiceSubsidy() {
        assertNull(instance.getRiceSubsidy());
    }

    @Test
    public void testGetPhoneAllowance() {
        assertNull(instance.getPhoneAllowance());
    }

    @Test
    public void testGetClothingAllowance() {
        assertNull(instance.getClothingAllowance());
    }

    @Test
    public void testGetHalfMonthRate() {
        assertNull(instance.getHalfMonthRate());
    }

    @Test
    public void testGetHourlyRate() {
        assertNull(instance.getHourlyRate());
    }

    @Test
    public void testSetEid() {
        instance.setEid(10001);
        assertEquals(10001, instance.getEid());
    }

    @Test
    public void testSetLastName() {
        instance.setLastName("Garcia");
        assertEquals("Garcia", instance.getLastName());
    }

    @Test
    public void testSetFirstName() {
        instance.setFirstName("Manuel III");
        assertEquals("Manuel III", instance.getFirstName());
    }

    @Test
    public void testSetBirthday() {
        Date date = new Date();
        instance.setBirthday(date);
        assertEquals(date, instance.getBirthday());
    }

    @Test
    public void testSetAddress() {
        instance.setAddress("Valero Carpark");
        assertEquals("Valero Carpark", instance.getAddress());
    }

    @Test
    public void testSetPhoneNumber() {
        instance.setPhoneNumber("966-860-270");
        assertEquals("966-860-270", instance.getPhoneNumber());
    }

    @Test
    public void testSetUsername() {
        instance.setUsername("mgarcia");
        assertEquals("mgarcia", instance.getUsername());
    }

    @Test
    public void testSetStatus() {
        instance.setStatus("Regular");
        assertEquals("Regular", instance.getStatus());
    }

    @Test
    public void testSetDesignation() {
        instance.setDesignation("CEO");
        assertEquals("CEO", instance.getDesignation());
    }

    @Test
    public void testSetImmediateSupervisor() {
        instance.setImmediateSupervisor("Supervisor");
        assertEquals("Supervisor", instance.getImmediateSupervisor());
    }

    @Test
    public void testSetSssNumber() {
        instance.setSssNumber("44-4506057-3");
        assertEquals("44-4506057-3", instance.getSssNumber());
    }

    @Test
    public void testSetPhilhealthNumber() {
        instance.setPhilhealthNumber("820126853951");
        assertEquals("820126853951", instance.getPhilhealthNumber());
    }

    @Test
    public void testSetTinNumber() {
        instance.setTinNumber("442-605-657-000");
        assertEquals("442-605-657-000", instance.getTinNumber());
    }

    @Test
    public void testSetPagibigNumber() {
        instance.setPagibigNumber("691295330870");
        assertEquals("691295330870", instance.getPagibigNumber());
    }

    @Test
    public void testSetBasicSalary() {
        BigDecimal salary = new BigDecimal("90009.00");
        instance.setBasicSalary(salary);
        assertEquals(salary, instance.getBasicSalary());
    }

    @Test
    public void testSetRiceSubsidy() {
        BigDecimal subsidy = new BigDecimal("1500.00");
        instance.setRiceSubsidy(subsidy);
        assertEquals(subsidy, instance.getRiceSubsidy());
    }

    @Test
    public void testSetPhoneAllowance() {
        BigDecimal allowance = new BigDecimal("2000.00");
        instance.setPhoneAllowance(allowance);
        assertEquals(allowance, instance.getPhoneAllowance());
    }

    @Test
    public void testSetClothingAllowance() {
        BigDecimal allowance = new BigDecimal("1000.00");
        instance.setClothingAllowance(allowance);
        assertEquals(allowance, instance.getClothingAllowance());
    }

    @Test
    public void testSetHalfMonthRate() {
        BigDecimal rate = new BigDecimal("45000.00");
        instance.setHalfMonthRate(rate);
        assertEquals(rate, instance.getHalfMonthRate());
    }

    @Test
    public void testSetHourlyRate() {
        BigDecimal rate = new BigDecimal("535.71");
        instance.setHourlyRate(rate);
        assertEquals(rate, instance.getHourlyRate());
    }

    @Test
    public void testGetTotalAllowances() {
        instance.setRiceSubsidy(new BigDecimal("1500.00"));
        instance.setPhoneAllowance(new BigDecimal("2000.00"));
        instance.setClothingAllowance(new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("4500.00"), instance.getTotalAllowances());
    }

    @Test
    public void testToString() {
        EmployeeReports emp = new EmployeeReports(
            10001, "Garcia", "Manuel III", new Date(), 
            "Valero Carpark", "966-860-270", "mgarcia", 
            "Regular", "CEO", "Supervisor"
        );
        String expected = "EmployeeReports{eid=10001, fullName='Garcia,Manuel III', designation='CEO', status='Regular', basicSalary=null}";
        assertEquals(expected, emp.toString());
    }
}