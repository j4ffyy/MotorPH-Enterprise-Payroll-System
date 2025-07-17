/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package ViewModel;


/**
 *
 * @author dashcodes 
 */

import javax.swing.text.AttributeSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RestrictedInputTest {

    private RestrictedInput instance;

    @Before
    public void setUp() {
        instance = new RestrictedInput(true, true, true, true, true, true, true, true, 50); // Allow all only for testing
    }

    @Test
    public void testInsertString_ValidInput() throws Exception {
        System.out.println("insertString - Valid Input");
        int offs = 0;
        String str = "Welcome to MotorPH";
        AttributeSet a = null;
        instance.insertString(offs, str, a);
        assertEquals("Welcome to MotorPH", instance.getText(0, instance.getLength()));
    }

    @Test
    public void testInsertString_InvalidInput() throws Exception {
        System.out.println("insertString - Invalid Input");
        int offs = 0;
        String str = "Welcome /to MotorPH"; // Invalid character '/' 
        AttributeSet a = null;

        // Debug output (optional - remove in final version)
        System.out.println("Inserting: '" + str + "'");

        instance.insertString(offs, str, a);

        String result = instance.getText(0, instance.getLength());
        System.out.println("Expected: 'Welcome to MotorPH'");
        System.out.println("Actual: '" + result + "'");

        assertEquals("Welcome to MotorPH", instance.getText(0, instance.getLength()));
    }

    @Test
    public void testInsertString_ExceedMaxLength() throws Exception {
        System.out.println("insertString - Exceed Max Length");
        instance.setMaxLength(10);
        int offs = 0;
        String str = "Welcome to MotorPH"; // More than 10 characters
        AttributeSet a = null;
        instance.insertString(offs, str, a);
        assertEquals("Welcome to", instance.getText(0, instance.getLength())); 
    }

    @Test
    public void testReplace_ValidInput() throws Exception {
        System.out.println("replace - Valid Input");
        instance.insertString(0, "Welcome", null);
        int offset = 0;
        int length = 7;
        String str = "Hello";
        AttributeSet attrs = null;
        instance.replace(offset, length, str, attrs);
        assertEquals("Hello", instance.getText(0, instance.getLength()));
    }

   @Test
    public void testReplace_InvalidInput() throws Exception {
        System.out.println("replace - Invalid Input");
        instance.insertString(0, "Welcome to MotorPH", null);

        String str = "InvalidInput";
        System.out.println("Replacing with: '" + str + "'");

        instance.replace(0, 18, str, null);

        String result = instance.getText(0, instance.getLength());
        System.out.println("Expected: 'Welcome to MotorPH'");
        System.out.println("Actual: '" + result + "'");

        assertEquals("Welcome to MotorPH", result);
    }

    @Test
    public void testNumbersOnly() {
        System.out.println("numbersOnly");
        int maxLength = 5;
        RestrictedInput result = RestrictedInput.numbersOnly(maxLength);
        assertNotNull(result);
        assertFalse(result.allowLetters);
        assertFalse(result.allowSpaces);
        assertFalse(result.allowHyphens);
        assertFalse(result.allowApostrophes);
        assertTrue(result.allowNumbers);
        assertEquals(maxLength, result.maxLength);
    }

    @Test
    public void testSetAllowLetters() {
        System.out.println("setAllowLetters");
        instance.setAllowLetters(false);
        assertFalse(instance.allowLetters);
    }

    @Test
    public void testSetAllowSpaces() {
        System.out.println("setAllowSpaces");
        instance.setAllowSpaces(false);
        assertFalse(instance.allowSpaces);
    }

    @Test
    public void testSetAllowHyphens() {
        System.out.println("setAllowHyphens");
        instance.setAllowHyphens(false);
        assertFalse(instance.allowHyphens);
    }

    @Test
    public void testSetAllowApostrophes() {
        System.out.println("setAllowApostrophes");
        instance.setAllowApostrophes(false);
        assertFalse(instance.allowApostrophes);
    }

    @Test
    public void testSetAllowNumbers() {
        System.out.println("setAllowNumbers");
        instance.setAllowNumbers(false);
        assertFalse(instance.allowNumbers);
    }

    @Test
    public void testSetMaxLength() {
        System.out.println("setMaxLength");
        int maxLength = 30;
        instance.setMaxLength(maxLength);
        assertEquals(maxLength, instance.maxLength);
    }
}
