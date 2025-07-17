/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ViewModel;



/**
 *
 * @author dashcodes
 */

import java.awt.GraphicsEnvironment;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OneTimePinTest {
    
    public OneTimePinTest() {
    }
    
    @Before
    public void setUp() {
        // Only generate OTP if not in headless mode
        if (!GraphicsEnvironment.isHeadless()) {
            OneTimePin.generateOTP();
        }
    }
    
    /**
     * Test of generateOTP method, of class OneTimePin.
     */
    @Test
    public void testGenerateOTP() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Skipping GUI test in headless environment");
            return;
        }
        
        System.out.println("generateOTP");
        String result = OneTimePin.generateOTP();
        
        // Check that the result is a 6-digit number
        assertNotNull(result);
        assertEquals(6, result.length());
        assertTrue(result.matches("\\d{6}")); // Ensure it contains only digits
    }
    /**
     * Test of verifyOTP method, of class OneTimePin.
     */
    @Test
    public void testVerifyOTP() {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Skipping GUI test in headless environment");
            return;
        }
        System.out.println("verifyOTP");
        
        // Generate an OTP
        String generatedOTP = OneTimePin.generateOTP();
        
    }
}
