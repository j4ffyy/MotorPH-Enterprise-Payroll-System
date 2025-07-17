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
    public class AllowancesTest {
    
    public AllowancesTest() {
    }
    
    @Before
    public void setUp() {
    }

    /**
     * Test of getEid method, of class Allowances.
     */
    @Test
    public void testGetEid() {
    System.out.println("getEid");
    Allowances instance = new Allowances(10001); 
    int expResult = 10001;
    int result = instance.getEid();
    assertEquals(expResult, result);
}

    /**
     * Test of getBasicSalary method, of class Allowances.
     */
    @Test
    public void testGetBasicSalary() {
    System.out.println("getBasicSalary");
    // Use constructor that sets basicSalary
    Allowances instance = new Allowances(10001, 90009.00f, 1500.00f, 2000.00f, 1000.00f, 45000.00f, 535.71f);
    float expResult = 90009.00f;
    float result = instance.getBasicSalary();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of getRiceAllowance method, of class Allowances.
     */
    @Test
    public void testGetRiceAllowance() {
    System.out.println("getRiceAllowance");
    Allowances instance = new Allowances(10001, 1500.00f, 2000.00f, 1000.00f);
    float expResult = 1500.00f;
    float result = instance.getRiceAllowance();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of getPhoneAllowance method, of class Allowances.
     */
    @Test
    public void testGetPhoneAllowance() {
    System.out.println("getPhoneAllowance");
    Allowances instance = new Allowances(10001, 1500.00f, 2000.00f, 1000.00f);
    float expResult = 2000.00f;
    float result = instance.getPhoneAllowance();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of getClothingAllowance method, of class Allowances.
     */
    @Test
    public void testGetClothingAllowance() {
    System.out.println("getClothingAllowance");
    Allowances instance = new Allowances(10001, 1500.00f, 2000.00f, 1000.00f);
    float expResult = 1000.00f;
    float result = instance.getClothingAllowance();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of getHalfMonthRate method, of class Allowances.
     */
   @Test
    public void testGetHalfMonthRate() {
    System.out.println("getHalfMonthRate");
    Allowances instance = new Allowances(10001, 90009.00f, 1500.00f, 2000.00f, 1000.00f, 45000.00f, 535.71f);
    float expResult = 45000.00f;
    float result = instance.getHalfMonthRate();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of getHourlyRate method, of class Allowances.
     */
    @Test
    public void testGetHourlyRate() {
    System.out.println("getHourlyRate");
    Allowances instance = new Allowances(10001, 90009.00f, 1500.00f, 2000.00f, 
                                       1000.00f, 45000.00f, 535.71f);
    float expResult = 535.71f;
    float result = instance.getHourlyRate();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of getTotalAllowance method, of class Allowances.
     */
    @Test
    public void testGetTotalAllowance() {
    System.out.println("getTotalAllowance");
    Allowances instance = new Allowances(10001, 1500.00f, 2000.00f, 1000.00f);
    float expResult = 1500.00f + 2000.00f + 1000.00f; // 4500.00f
    float result = instance.getTotalAllowance();
    assertEquals(expResult, result, 0);
}

    /**
     * Test of setEid method, of class Allowances.
     */
   @Test
    public void testSetEid() {
    System.out.println("setEid");
    int eid = 10001;
    Allowances instance = new Allowances();
    instance.setEid(eid);
    assertEquals(eid, instance.getEid());
}

    /**
     * Test of setBasicSalary method, of class Allowances.
     */
   @Test
    public void testSetBasicSalary() {
    System.out.println("setBasicSalary");
    float basicSalary = 50000.0f;
    Allowances instance = new Allowances();
    instance.setBasicSalary(basicSalary);
    assertEquals(basicSalary, instance.getBasicSalary(), 0);
}

    /**
     * Test of setRiceAllowance method, of class Allowances.
     */
    @Test
    public void testSetRiceAllowance() {
    System.out.println("setRiceAllowance");
    float riceAllowance = 2000.00f;
    Allowances instance = new Allowances();
    instance.setRiceAllowance(riceAllowance);
    assertEquals(riceAllowance, instance.getRiceAllowance(), 0);
}

    /**
     * Test of setPhoneAllowance method, of class Allowances.
     */
    @Test
    public void testSetPhoneAllowance() {
    System.out.println("setPhoneAllowance");
    float phoneAllowance = 2500.00f;
    Allowances instance = new Allowances();
    instance.setPhoneAllowance(phoneAllowance);
    assertEquals(phoneAllowance, instance.getPhoneAllowance(), 0);
}

    /**
     * Test of setClothingAllowance method, of class Allowances.
     */
    @Test
    public void testSetClothingAllowance() {
    System.out.println("setClothingAllowance");
    float clothingAllowance = 1500.00f;
    Allowances instance = new Allowances();
    instance.setClothingAllowance(clothingAllowance);
    assertEquals(clothingAllowance, instance.getClothingAllowance(), 0);
}

    /**
     * Test of setHalfMonthRate method, of class Allowances.
     */
    @Test
    public void testSetHalfMonthRate() {
    System.out.println("setHalfMonthRate");
    float halfMonthRate = 30000.00f;
    Allowances instance = new Allowances();
    instance.setHalfMonthRate(halfMonthRate);
    assertEquals(halfMonthRate, instance.getHalfMonthRate(), 0);
}

    /**
     * Test of setHourlyRate method, of class Allowances.
     */
    @Test
    public void testSetHourlyRate() {
    System.out.println("setHourlyRate");
    float hourlyRate = 600.00f;
    Allowances instance = new Allowances();
    instance.setHourlyRate(hourlyRate);
    assertEquals(hourlyRate, instance.getHourlyRate(), 0);
}

    /**
     * Test of setTotalAllowance method, of class Allowances.
     */
   @Test
    public void testSetTotalAllowance() {
    System.out.println("setTotalAllowance");
    float totalAllowance = 5000.00f;
    Allowances instance = new Allowances();
    instance.setTotalAllowance(totalAllowance);
    assertEquals(totalAllowance, instance.getTotalAllowance(), 0);
}
    
}
