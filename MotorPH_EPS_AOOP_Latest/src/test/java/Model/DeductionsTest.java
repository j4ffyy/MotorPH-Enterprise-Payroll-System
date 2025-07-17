/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package Model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jafph
 */
public class DeductionsTest {
    
    public DeductionsTest() {
    }
    private Deductions instance;

    @Before
    public void setUp() {
    // Deduction Objects with sample data
    instance = new Deductions(
        10001,          // eid
        90009.00f,      // basicSalary
        1800.18f,       // pagIbigContribution
        2250.23f,       // philHealthContribution
        4050.41f,       // sssContribution
        17835.60f,      // withholdingTax
        25936.42f           // totalDeductions (calculated later)
    );
}

    @Test
    public void testGetEid() {
    System.out.println("getEid");
    int expResult = 10001;  
    int result = instance.getEid();
    assertEquals(expResult, result);
}

    /**
     * Test of getBasicSalary method, of class Deductions.
     */
    @Test
    public void testGetBasicSalary() {
        System.out.println("getBasicSalary");
        float expResult = 90009.00F;
        float result = instance.getBasicSalary();
        assertEquals(expResult, result, 0.0f);
    }

    /**
     * Test of getPagIbigContribution method, of class Deductions.
     */
    @Test
    public void testGetPagIbigContribution() {
        System.out.println("getPagIbigContribution");
        float expResult = 1800.18F;
        float result = instance.getPagIbigContribution();
        assertEquals(expResult, result, 0.0f);
    }

    /**
     * Test of getPhilHealthContribution method, of class Deductions.
     */
    @Test
    public void testGetPhilHealthContribution() {
        System.out.println("getPhilHealthContribution");
        float expResult = 2250.23F;
        float result = instance.getPhilHealthContribution();
        assertEquals(expResult, result, 0);
    }

    /**
     * Test of getSssContribution method, of class Deductions.
     */
    @Test
    public void testGetSssContribution() {
        System.out.println("getSssContribution");
        
        float expResult = 4050.41F;
        float result = instance.getSssContribution();
        assertEquals(expResult, result, 0);
    }

    /**
     * Test of getWithholdingTax method, of class Deductions.
     */
    @Test
    public void testGetWithholdingTax() {
        System.out.println("getWithholdingTax");
        float expResult = 17835.60F;
        float result = instance.getWithholdingTax();
        assertEquals(expResult, result, 0);
    }

    /**
     * Test of getTotalDeductions method, of class Deductions.
     */
    @Test
    public void testGetTotalDeductions() {
        System.out.println("getTotalDeductions");
        
        float expTotal =
                instance.getPagIbigContribution()+
                instance.getPhilHealthContribution()+
                instance.getSssContribution() +
                instance.getWithholdingTax();
       
        assertEquals(expTotal, instance.getTotalDeductions(), 0.0f);
        
        
    }

    /**
     * Test of setEid method, of class Deductions.
     */
    @Test
    public void testSetEid() {
        System.out.println("setEid");
        int eid = 10001;
        instance.setEid(eid);
        assertEquals(eid, instance.getEid());
    }

    /**
     * Test of setBasicSalary method, of class Deductions.
     */
    @Test
    public void testSetBasicSalary() {
        System.out.println("setBasicSalary");
        float basicSalary = 90000.00F;
        instance.setBasicSalary(basicSalary);
        assertEquals(basicSalary, instance.getBasicSalary(), 0.0f);
    }

    /**
     * Test of setPagIbigContribution method, of class Deductions.
     */
    @Test
    public void testSetPagIbigContribution() {
        System.out.println("setPagIbigContribution");
        float pagIbigContribution = 1800.18F;
        instance.setPagIbigContribution(pagIbigContribution);
        assertEquals(pagIbigContribution, instance.getPagIbigContribution(), 0.0f);
        
        
    }

    /**
     * Test of setPhilHealthContribution method, of class Deductions.
     */
    @Test
    public void testSetPhilHealthContribution() {
        System.out.println("setPhilHealthContribution");
        float philHealthContribution = 1800.18F;
        instance.setPhilHealthContribution(philHealthContribution);
        assertEquals(philHealthContribution, instance.getPhilHealthContribution(), 0.0f); 
    }

    /**
     * Test of setSssContribution method, of class Deductions.
     */
    @Test
    public void testSetSssContribution() {
        System.out.println("setSssContribution");
        float sssContribution = 4050.41f;
        instance.setSssContribution(sssContribution);
        assertEquals(sssContribution, instance.getSssContribution(), 0.0f);    
    }

    /**
     * Test of setWithholdingTax method, of class Deductions.
     */
    @Test
    public void testSetWithholdingTax() {
        System.out.println("setWithholdingTax");
        float withholdingTax = 17835.60F;
        instance.setWithholdingTax(withholdingTax);
        assertEquals(withholdingTax, instance.getWithholdingTax(), 0.0f);
    }

    /**
     * Test of setTotalDeductions method, of class Deductions.
     */
    @Test
    public void testSetTotalDeductions() {
        System.out.println("setTotalDeductions");
        float totalDeductions = 0.0F;
        instance.setTotalDeductions(totalDeductions);      
    }
    
}
