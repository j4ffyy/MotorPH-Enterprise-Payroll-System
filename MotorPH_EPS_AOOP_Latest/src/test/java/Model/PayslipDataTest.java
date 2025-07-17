/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Model;

/*
* @author dashcodes and CJ
*/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

public class PayslipDataTest {
    
    private PayslipData payslipData;
    
    @BeforeEach
    void setUp() {
        payslipData = new PayslipData();
    }
    
    @Test
    @DisplayName("Test Employee ID getter and setter")
    void testEmployeeId() {
        // Use a test eid but can be like the ones we have
        Integer employeeId = 10004;
        payslipData.setEmployeeId(employeeId);
        assertEquals(employeeId, payslipData.getEmployeeId());
        
        // Test setting null value
        payslipData.setEmployeeId(null);
        assertNull(payslipData.getEmployeeId());
        
        // Test setting zero value
        payslipData.setEmployeeId(0);
        assertEquals(0, payslipData.getEmployeeId());
    }
    
    @Test
    @DisplayName("Test Employee Name getter and setter")
    void testEmployeeName() {
        // Test setting and getting valid employee name
        String employeeName = "Alex Martinez";
        payslipData.setEmployeeName(employeeName);
        assertEquals(employeeName, payslipData.getEmployeeName());
        
        // Test setting null value
        payslipData.setEmployeeName(null);
        assertNull(payslipData.getEmployeeName());
        
        // Test setting empty string
        payslipData.setEmployeeName("");
        assertEquals("", payslipData.getEmployeeName());
    }
    
    @Test
    @DisplayName("Test Position getter and setter")
    void testPosition() {
        // Test setting and getting valid position
        String position = "Software Engineer";
        payslipData.setPosition(position);
        assertEquals(position, payslipData.getPosition());
        
        // Test setting null value
        payslipData.setPosition(null);
        assertNull(payslipData.getPosition());
    }
    
    @Test
    @DisplayName("Test Department getter and setter")
    void testDepartment() {
        // Test setting and getting valid department
        String department = "IT Department";
        payslipData.setDepartment(department);
        assertEquals(department, payslipData.getDepartment());
        
        // Test setting null value
        payslipData.setDepartment(null);
        assertNull(payslipData.getDepartment());
        
        // Test setting empty string
        payslipData.setDepartment("");
        assertEquals("", payslipData.getDepartment());
    }
    
    @Test
    @DisplayName("Test Monthly Rate getter and setter")
    void testMonthlyRate() {
        // Test setting and getting valid monthly rate
        BigDecimal monthlyRate = new BigDecimal("50000.00");
        payslipData.setMonthlyRate(monthlyRate);
        assertEquals(monthlyRate, payslipData.getMonthlyRate());
        
        // Test setting null value
        payslipData.setMonthlyRate(null);
        assertNull(payslipData.getMonthlyRate());
        
        // Test setting zero value
        payslipData.setMonthlyRate(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, payslipData.getMonthlyRate());
    }
    
    @Test
    @DisplayName("Test Daily Rate getter and setter")
    void testDailyRate() {
        // Test setting and getting valid daily rate
        BigDecimal dailyRate = new BigDecimal("2500.00");
        payslipData.setDailyRate(dailyRate);
        assertEquals(dailyRate, payslipData.getDailyRate());
        
        // Test setting null value
        payslipData.setDailyRate(null);
        assertNull(payslipData.getDailyRate());
    }
    
    @Test
    @DisplayName("Test Days Worked getter and setter")
    void testDaysWorked() {
        // Test setting and getting valid days worked
        Long daysWorked = 22L;
        payslipData.setDaysWorked(daysWorked);
        assertEquals(daysWorked, payslipData.getDaysWorked());
        
        // Test setting null value
        payslipData.setDaysWorked(null);
        assertNull(payslipData.getDaysWorked());
        
        // Test setting zero value
        payslipData.setDaysWorked(0L);
        assertEquals(0L, payslipData.getDaysWorked());
    }
    
    @Test
    @DisplayName("Test Overtime Hours getter and setter")
    void testOvertimeHours() {
        // Test setting and getting valid overtime hours
        Integer overtimeHours = 10;
        payslipData.setOvertimeHours(overtimeHours);
        assertEquals(overtimeHours, payslipData.getOvertimeHours());
        
        // Test setting null value
        payslipData.setOvertimeHours(null);
        assertNull(payslipData.getOvertimeHours());
        
        // Test setting zero value
        payslipData.setOvertimeHours(0);
        assertEquals(0, payslipData.getOvertimeHours());
    }
    
    @Test
    @DisplayName("Test Overtime Pay getter and setter")
    void testOvertimePay() {
        // Test setting and getting valid overtime pay
        BigDecimal overtimePay = new BigDecimal("5000.00");
        payslipData.setOvertimePay(overtimePay);
        assertEquals(overtimePay, payslipData.getOvertimePay());
        
        // Test setting null value
        payslipData.setOvertimePay(null);
        assertNull(payslipData.getOvertimePay());
    }
    
    @Test
    @DisplayName("Test Gross Income getter and setter")
    void testGrossIncome() {
        // Test setting and getting valid gross income
        BigDecimal grossIncome = new BigDecimal("55000.00");
        payslipData.setGrossIncome(grossIncome);
        assertEquals(grossIncome, payslipData.getGrossIncome());
        
        // Test setting null value
        payslipData.setGrossIncome(null);
        assertNull(payslipData.getGrossIncome());
    }
    
    @Test
    @DisplayName("Test Rice Subsidy getter and setter")
    void testRiceSubsidy() {
        // Test setting and getting valid rice subsidy
        BigDecimal riceSubsidy = new BigDecimal("1500.00");
        payslipData.setRiceSubsidy(riceSubsidy);
        assertEquals(riceSubsidy, payslipData.getRiceSubsidy());
        
        // Test setting null value
        payslipData.setRiceSubsidy(null);
        assertNull(payslipData.getRiceSubsidy());
    }
    
    @Test
    @DisplayName("Test Phone Allowance getter and setter")
    void testPhoneAllowance() {
        // Test setting and getting valid phone allowance
        BigDecimal phoneAllowance = new BigDecimal("800.00");
        payslipData.setPhoneAllowance(phoneAllowance);
        assertEquals(phoneAllowance, payslipData.getPhoneAllowance());
        
        // Test setting null value
        payslipData.setPhoneAllowance(null);
        assertNull(payslipData.getPhoneAllowance());
    }
    
    @Test
    @DisplayName("Test Clothing Allowance getter and setter")
    void testClothingAllowance() {
        // Test setting and getting valid clothing allowance
        BigDecimal clothingAllowance = new BigDecimal("1200.00");
        payslipData.setClothingAllowance(clothingAllowance);
        assertEquals(clothingAllowance, payslipData.getClothingAllowance());
        
        // Test setting null value
        payslipData.setClothingAllowance(null);
        assertNull(payslipData.getClothingAllowance());
    }
    
    @Test
    @DisplayName("Test Total Benefits getter and setter")
    void testTotalBenefits() {
        // Test setting and getting valid total benefits
        BigDecimal totalBenefits = new BigDecimal("3500.00");
        payslipData.setTotalBenefits(totalBenefits);
        assertEquals(totalBenefits, payslipData.getTotalBenefits());
        
        // Test setting null value
        payslipData.setTotalBenefits(null);
        assertNull(payslipData.getTotalBenefits());
    }
    
    @Test
    @DisplayName("Test SSS Contribution getter and setter")
    void testSssContribution() {
        // Test setting and getting valid SSS contribution
        BigDecimal sssContribution = new BigDecimal("2200.00");
        payslipData.setSssContribution(sssContribution);
        assertEquals(sssContribution, payslipData.getSssContribution());
        
        // Test setting null value
        payslipData.setSssContribution(null);
        assertNull(payslipData.getSssContribution());
    }
    
    @Test
    @DisplayName("Test PhilHealth Contribution getter and setter")
    void testPhilhealthContribution() {
        // Test setting and getting valid PhilHealth contribution
        BigDecimal philhealthContribution = new BigDecimal("1100.00");
        payslipData.setPhilhealthContribution(philhealthContribution);
        assertEquals(philhealthContribution, payslipData.getPhilhealthContribution());
        
        // Test setting null value
        payslipData.setPhilhealthContribution(null);
        assertNull(payslipData.getPhilhealthContribution());
    }
    
    @Test
    @DisplayName("Test Pag-IBIG Contribution getter and setter")
    void testPagibigContribution() {
        // Test setting and getting valid Pag-IBIG contribution
        BigDecimal pagibigContribution = new BigDecimal("100.00");
        payslipData.setPagibigContribution(pagibigContribution);
        assertEquals(pagibigContribution, payslipData.getPagibigContribution());
        
        // Test setting null value
        payslipData.setPagibigContribution(null);
        assertNull(payslipData.getPagibigContribution());
    }
    
    @Test
    @DisplayName("Test Withholding Tax getter and setter")
    void testWithholdingTax() {
        // Test setting and getting valid withholding tax
        BigDecimal withholdingTax = new BigDecimal("5500.00");
        payslipData.setWithholdingTax(withholdingTax);
        assertEquals(withholdingTax, payslipData.getWithholdingTax());
        
        // Test setting null value
        payslipData.setWithholdingTax(null);
        assertNull(payslipData.getWithholdingTax());
    }
    
    @Test
    @DisplayName("Test Total Deductions getter and setter")
    void testTotalDeductions() {
        // Test setting and getting valid total deductions
        BigDecimal totalDeductions = new BigDecimal("8900.00");
        payslipData.setTotalDeductions(totalDeductions);
        assertEquals(totalDeductions, payslipData.getTotalDeductions());
        
        // Test setting null value
        payslipData.setTotalDeductions(null);
        assertNull(payslipData.getTotalDeductions());
    }
    
    @Test
    @DisplayName("Test Take Home Pay getter and setter")
    void testTakeHomePay() {
        // Test setting and getting valid take home pay
        BigDecimal takeHomePay = new BigDecimal("49600.00");
        payslipData.setTakeHomePay(takeHomePay);
        assertEquals(takeHomePay, payslipData.getTakeHomePay());
        
        // Test setting null value
        payslipData.setTakeHomePay(null);
        assertNull(payslipData.getTakeHomePay());
    }
    
    @Test
    @DisplayName("Test complete payslip data scenario")
    void testCompletePayslipData() {
        // Test a complete payslip data scenario
        Integer employeeId = 1001;
        String employeeName = "Maria Santos";
        String position = "Senior Developer";
        String department = "IT";
        BigDecimal monthlyRate = new BigDecimal("60000.00");
        BigDecimal dailyRate = new BigDecimal("2727.27");
        Long daysWorked = 22L;
        Integer overtimeHours = 8;
        BigDecimal overtimePay = new BigDecimal("4000.00");
        BigDecimal grossIncome = new BigDecimal("64000.00");
        BigDecimal riceSubsidy = new BigDecimal("1500.00");
        BigDecimal phoneAllowance = new BigDecimal("1000.00");
        BigDecimal clothingAllowance = new BigDecimal("1500.00");
        BigDecimal totalBenefits = new BigDecimal("4000.00");
        BigDecimal sssContribution = new BigDecimal("2640.00");
        BigDecimal philhealthContribution = new BigDecimal("1320.00");
        BigDecimal pagibigContribution = new BigDecimal("100.00");
        BigDecimal withholdingTax = new BigDecimal("6600.00");
        BigDecimal totalDeductions = new BigDecimal("10660.00");
        BigDecimal takeHomePay = new BigDecimal("57340.00");
        
        // Set all values
        payslipData.setEmployeeId(employeeId);
        payslipData.setEmployeeName(employeeName);
        payslipData.setPosition(position);
        payslipData.setDepartment(department);
        payslipData.setMonthlyRate(monthlyRate);
        payslipData.setDailyRate(dailyRate);
        payslipData.setDaysWorked(daysWorked);
        payslipData.setOvertimeHours(overtimeHours);
        payslipData.setOvertimePay(overtimePay);
        payslipData.setGrossIncome(grossIncome);
        payslipData.setRiceSubsidy(riceSubsidy);
        payslipData.setPhoneAllowance(phoneAllowance);
        payslipData.setClothingAllowance(clothingAllowance);
        payslipData.setTotalBenefits(totalBenefits);
        payslipData.setSssContribution(sssContribution);
        payslipData.setPhilhealthContribution(philhealthContribution);
        payslipData.setPagibigContribution(pagibigContribution);
        payslipData.setWithholdingTax(withholdingTax);
        payslipData.setTotalDeductions(totalDeductions);
        payslipData.setTakeHomePay(takeHomePay);
        
        // Assert all values
        assertEquals(employeeId, payslipData.getEmployeeId());
        assertEquals(employeeName, payslipData.getEmployeeName());
        assertEquals(position, payslipData.getPosition());
        assertEquals(department, payslipData.getDepartment());
        assertEquals(monthlyRate, payslipData.getMonthlyRate());
        assertEquals(dailyRate, payslipData.getDailyRate());
        assertEquals(daysWorked, payslipData.getDaysWorked());
        assertEquals(overtimeHours, payslipData.getOvertimeHours());
        assertEquals(overtimePay, payslipData.getOvertimePay());
        assertEquals(grossIncome, payslipData.getGrossIncome());
        assertEquals(riceSubsidy, payslipData.getRiceSubsidy());
        assertEquals(phoneAllowance, payslipData.getPhoneAllowance());
        assertEquals(clothingAllowance, payslipData.getClothingAllowance());
        assertEquals(totalBenefits, payslipData.getTotalBenefits());
        assertEquals(sssContribution, payslipData.getSssContribution());
        assertEquals(philhealthContribution, payslipData.getPhilhealthContribution());
        assertEquals(pagibigContribution, payslipData.getPagibigContribution());
        assertEquals(withholdingTax, payslipData.getWithholdingTax());
        assertEquals(totalDeductions, payslipData.getTotalDeductions());
        assertEquals(takeHomePay, payslipData.getTakeHomePay());
    }
    
    @Test
    @DisplayName("Test initial state of PayslipData object")
    void testInitialState() {
        // Test that all fields are null initially
        PayslipData newPayslipData = new PayslipData();
        assertNull(newPayslipData.getEmployeeId());
        assertNull(newPayslipData.getEmployeeName());
        assertNull(newPayslipData.getPosition());
        assertNull(newPayslipData.getDepartment());
        assertNull(newPayslipData.getMonthlyRate());
        assertNull(newPayslipData.getDailyRate());
        assertNull(newPayslipData.getDaysWorked());
        assertNull(newPayslipData.getOvertimeHours());
        assertNull(newPayslipData.getOvertimePay());
        assertNull(newPayslipData.getGrossIncome());
        assertNull(newPayslipData.getRiceSubsidy());
        assertNull(newPayslipData.getPhoneAllowance());
        assertNull(newPayslipData.getClothingAllowance());
        assertNull(newPayslipData.getTotalBenefits());
        assertNull(newPayslipData.getSssContribution());
        assertNull(newPayslipData.getPhilhealthContribution());
        assertNull(newPayslipData.getPagibigContribution());
        assertNull(newPayslipData.getWithholdingTax());
        assertNull(newPayslipData.getTotalDeductions());
        assertNull(newPayslipData.getTakeHomePay());
    }
}