package Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import ViewModel.SalaryCalculator;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DashboardData class
 * Tests all methods using real objects instead of mocks to avoid Mockito issues
 * 
 * @author dashcodes
 */
public class DashboardDataTest {
    
    private DashboardData dashboardData;
    private EmployeeDetails testEmployee;
    private SalaryCalculator testSalaryCalculator;
    
    @BeforeEach
    public void setUp() {
        dashboardData = new DashboardData();
        testEmployee = createTestEmployee();
        testSalaryCalculator = new SalaryCalculator(); // No database connection
    }
    
    /**
     * Creates a test employee with sample data
     */
    private EmployeeDetails createTestEmployee() {
        EmployeeDetails employee = new EmployeeDetails();
        
        // Set basic information
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDesignation("Software Engineer");
        employee.setAddress("123 Main St");
        employee.setPhoneNumber("09123456789");
        employee.setStatus("Active");
        employee.setSss("12-3456789-0");
        employee.setPhilHealth("12-345678901-2");
        employee.setTin("123-456-789-000");
        employee.setPagIbig("1234-5678-9012");
        
        // Set financial data
        employee.setBasicSalary(50000.0f);
        employee.setRiceSubsidy(2000.0f);
        employee.setPhoneAllowance(1500.0f);
        employee.setClothingAllowance(1000.0f);
        employee.setGrossSemiMonthlyRate(25000.0f);
        employee.setHourlyRate(312.5f);
        
        // Set incentives
        employee.setOverTimePay(5000.0f);
        employee.setPerformanceBonus(3000.0f);
        employee.setHolidayPay(2000.0f);
        
        // Set calculated totals
        employee.setTotalAllowances(4500.0f);
        employee.setTotalIncentives(10000.0f);
        employee.setGrossPay(64500.0f);
        employee.setNetPay(55000.0f);
        employee.setTotalDeductions(9500.0f);
        employee.setOverTime(16.0f);
        
        return employee;
    }
    
    /**
     * Creates a test employee with null values
     */
    private EmployeeDetails createEmployeeWithNullValues() {
        EmployeeDetails employee = new EmployeeDetails();
        // Most fields will be null by default
        employee.setBasicSalary(0.0f);
        employee.setRiceSubsidy(-100.0f); // Negative value
        return employee;
    }
    
    /**
     * Creates a test SalaryCalculator with sample deduction data
     */
    private SalaryCalculator createTestSalaryCalculator() {
        SalaryCalculator calculator = new SalaryCalculator();
        
        // We'll use reflection or create a method to set test data
        // Since SalaryCalculator doesn't have public setters, we'll work with what we have
        return calculator;
    }
    
    @Test
    @DisplayName("Test processEmployeeData with valid employee")
    public void testProcessEmployeeDataWithValidEmployee() {
        // Act
        dashboardData.processEmployeeData(testEmployee);
        
        // Assert - Verify personal information
        assertEquals("John", dashboardData.getDisplayValue("firstName"));
        assertEquals("Doe", dashboardData.getDisplayValue("lastName"));
        assertEquals("Software Engineer", dashboardData.getDisplayValue("designation"));
        assertEquals("123 Main St", dashboardData.getDisplayValue("address"));
        assertEquals("09123456789", dashboardData.getDisplayValue("phoneNumber"));
        assertEquals("Active", dashboardData.getDisplayValue("status"));
        assertEquals("12-3456789-0", dashboardData.getDisplayValue("sssNumber"));
        assertEquals("12-345678901-2", dashboardData.getDisplayValue("philHealthNumber"));
        assertEquals("123-456-789-000", dashboardData.getDisplayValue("tinNumber"));
        assertEquals("1234-5678-9012", dashboardData.getDisplayValue("pagIbigNumber"));
        
        // Assert - Verify financial data formatting
        assertEquals("₱50000.00", dashboardData.getDisplayValue("basicSalary"));
        assertEquals("₱2000.00", dashboardData.getDisplayValue("riceSubsidy"));
        assertEquals("₱1500.00", dashboardData.getDisplayValue("phoneAllowance"));
        assertEquals("₱1000.00", dashboardData.getDisplayValue("clothingAllowance"));
        assertEquals("₱25000.00", dashboardData.getDisplayValue("grossSemiMonthlyRate"));
        assertEquals("₱312.50", dashboardData.getDisplayValue("hourlyRate"));
        
        // Assert - Verify incentives
        assertEquals("₱5000.00", dashboardData.getDisplayValue("overTimePay"));
        assertEquals("₱3000.00", dashboardData.getDisplayValue("performanceBonus"));
        assertEquals("₱2000.00", dashboardData.getDisplayValue("holidayPay"));
        
        // Assert - Verify calculated totals
        assertEquals("₱4500.00", dashboardData.getDisplayValue("totalAllowances"));
        assertEquals("₱10000.00", dashboardData.getDisplayValue("totalIncentives"));
        assertEquals("₱64500.00", dashboardData.getDisplayValue("grossPay"));
        assertEquals("₱55000.00", dashboardData.getDisplayValue("netPay"));
        assertEquals("₱9500.00", dashboardData.getDisplayValue("totalDeductions"));
        assertEquals("16.0", dashboardData.getDisplayValue("overTime"));
    }
    
    @Test
    @DisplayName("Test processEmployeeData with null employee")
    public void testProcessEmployeeDataWithNullEmployee() {
        // Act
        dashboardData.processEmployeeData(null);
        
        // Assert - Should not crash, but no data should be processed
        assertEquals("N/A", dashboardData.getDisplayValue("firstName"));
        assertEquals("N/A", dashboardData.getDisplayValue("lastName"));
        assertEquals("N/A", dashboardData.getDisplayValue("basicSalary"));
    }
    
    @Test
    @DisplayName("Test processEmployeeData with null and negative values")
    public void testProcessEmployeeDataWithNullAndNegativeValues() {
        // Arrange
        EmployeeDetails employeeWithNulls = createEmployeeWithNullValues();
        
        // Act
        dashboardData.processEmployeeData(employeeWithNulls);
        
        // Assert
        assertEquals("N/A", dashboardData.getDisplayValue("firstName"));
        assertEquals("N/A", dashboardData.getDisplayValue("lastName"));
        assertEquals("N/A", dashboardData.getDisplayValue("basicSalary")); // 0 value
        assertEquals("N/A", dashboardData.getDisplayValue("riceSubsidy")); // Negative value
    }
    
    @Test
    @DisplayName("Test processAllowanceData with valid employee")
    public void testProcessAllowanceDataWithValidEmployee() {
        // Act
        dashboardData.processAllowanceData(testEmployee);
        
        // Assert
        assertEquals("₱2000.00", dashboardData.getDisplayValue("riceSubsidy"));
        assertEquals("₱1500.00", dashboardData.getDisplayValue("phoneAllowance"));
        assertEquals("₱1000.00", dashboardData.getDisplayValue("clothingAllowance"));
        assertEquals("₱4500.00", dashboardData.getDisplayValue("totalAllowances"));
    }
    
    @Test
    @DisplayName("Test processAllowanceData with null employee")
    public void testProcessAllowanceDataWithNullEmployee() {
        // Act
        dashboardData.processAllowanceData(null);
        
        // Assert - Should not crash
        assertEquals("N/A", dashboardData.getDisplayValue("riceSubsidy"));
        assertEquals("N/A", dashboardData.getDisplayValue("phoneAllowance"));
        assertEquals("N/A", dashboardData.getDisplayValue("clothingAllowance"));
        assertEquals("N/A", dashboardData.getDisplayValue("totalAllowances"));
    }
    
    @Test
    @DisplayName("Test processDeductionData with valid data")
    public void testProcessDeductionDataWithValidData() {
        // Note: SalaryCalculator will have default values (0) since no DB connection
        // Act
        dashboardData.processDeductionData(testEmployee, testSalaryCalculator);
        
        // Assert - Since SalaryCalculator has no DB, deductions will be N/A
        assertEquals("N/A", dashboardData.getDisplayValue("sssContribution"));
        assertEquals("N/A", dashboardData.getDisplayValue("philHealthContribution"));
        assertEquals("N/A", dashboardData.getDisplayValue("pagIbigContribution"));
        assertEquals("N/A", dashboardData.getDisplayValue("withholdingTax"));
        assertEquals("N/A", dashboardData.getDisplayValue("totalDeductions"));
    }
    
    @Test
    @DisplayName("Test processDeductionData with null parameters")
    public void testProcessDeductionDataWithNullParameters() {
        // Act
        dashboardData.processDeductionData(null, null);
        dashboardData.processDeductionData(testEmployee, null);
        dashboardData.processDeductionData(null, testSalaryCalculator);
        
        // Assert - Should not crash
        assertEquals("N/A", dashboardData.getDisplayValue("sssContribution"));
        assertEquals("N/A", dashboardData.getDisplayValue("philHealthContribution"));
        assertEquals("N/A", dashboardData.getDisplayValue("pagIbigContribution"));
        assertEquals("N/A", dashboardData.getDisplayValue("withholdingTax"));
        assertEquals("N/A", dashboardData.getDisplayValue("totalDeductions"));
    }
    
    @Test
    @DisplayName("Test processSalaryData with valid data")
    public void testProcessSalaryDataWithValidData() {
        // Act
        dashboardData.processSalaryData(testEmployee, testSalaryCalculator);
        
        // Assert - Since SalaryCalculator has no DB, some values will be N/A
        assertEquals("₱50000.00", dashboardData.getDisplayValue("basicSalary"));
        assertEquals("N/A", dashboardData.getDisplayValue("grossPay")); // Default from SalaryCalculator
        assertEquals("N/A", dashboardData.getDisplayValue("netPay")); // Default from SalaryCalculator
        assertEquals("N/A", dashboardData.getDisplayValue("overTimePay")); // Default from SalaryCalculator
        assertEquals("N/A", dashboardData.getDisplayValue("holidayPay")); // Default from SalaryCalculator
        assertEquals("N/A", dashboardData.getDisplayValue("performanceBonus")); // Default from SalaryCalculator
        assertEquals("N/A", dashboardData.getDisplayValue("totalIncentives")); // Default from SalaryCalculator
    }
    
    @Test
    @DisplayName("Test processSalaryData with null parameters")
    public void testProcessSalaryDataWithNullParameters() {
        // Act
        dashboardData.processSalaryData(null, null);
        dashboardData.processSalaryData(testEmployee, null);
        dashboardData.processSalaryData(null, testSalaryCalculator);
        
        // Assert - Should not crash
        assertEquals("N/A", dashboardData.getDisplayValue("basicSalary"));
        assertEquals("N/A", dashboardData.getDisplayValue("grossPay"));
        assertEquals("N/A", dashboardData.getDisplayValue("netPay"));
    }
    
    @Test
    @DisplayName("Test getDisplayValue with non-existent key")
    public void testGetDisplayValueWithNonExistentKey() {
        // Act & Assert
        assertEquals("N/A", dashboardData.getDisplayValue("nonExistentKey"));
        assertEquals("N/A", dashboardData.getDisplayValue(""));
        assertEquals("N/A", dashboardData.getDisplayValue("invalidKey123"));
    }
    
    @Test
    @DisplayName("Test clearDisplayData functionality")
    public void testClearDisplayData() {
        // Arrange - Add some data first
        dashboardData.processEmployeeData(testEmployee);
        
        // Verify data exists
        assertEquals("John", dashboardData.getDisplayValue("firstName"));
        assertEquals("₱50000.00", dashboardData.getDisplayValue("basicSalary"));
        
        // Act
        dashboardData.clearDisplayData();
        
        // Assert
        assertEquals("N/A", dashboardData.getDisplayValue("firstName"));
        assertEquals("N/A", dashboardData.getDisplayValue("basicSalary"));
        assertEquals("N/A", dashboardData.getDisplayValue("riceSubsidy"));
    }
    
    @Test
    @DisplayName("Test getAllDisplayData returns unmodifiable map")
    public void testGetAllDisplayDataReturnsUnmodifiableMap() {
        // Arrange
        dashboardData.processEmployeeData(testEmployee);
        
        // Act
        Map<String, String> allData = dashboardData.getAllDisplayData();
        
        // Assert
        assertNotNull(allData);
        assertTrue(allData.containsKey("firstName"));
        assertEquals("John", allData.get("firstName"));
        assertTrue(allData.containsKey("basicSalary"));
        assertEquals("₱50000.00", allData.get("basicSalary"));
        
        // Verify it's unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            allData.put("testKey", "testValue");
        });
    }
    
    @Test
    @DisplayName("Test currency formatting edge cases")
    public void testCurrencyFormattingEdgeCases() {
        // Arrange
        EmployeeDetails edgeCaseEmployee = new EmployeeDetails();
        edgeCaseEmployee.setBasicSalary(0.0f);
        edgeCaseEmployee.setRiceSubsidy(-500.0f);
        edgeCaseEmployee.setPhoneAllowance(0.01f);
        edgeCaseEmployee.setClothingAllowance(999.99f);
        edgeCaseEmployee.setGrossPay(1000000.50f);
        
        // Act
        dashboardData.processEmployeeData(edgeCaseEmployee);
        
        // Assert
        assertEquals("N/A", dashboardData.getDisplayValue("basicSalary")); // 0 value
        assertEquals("N/A", dashboardData.getDisplayValue("riceSubsidy")); // Negative value
        assertEquals("₱0.01", dashboardData.getDisplayValue("phoneAllowance")); // Small positive value
        assertEquals("₱999.99", dashboardData.getDisplayValue("clothingAllowance")); // Decimal value
        assertEquals("₱1000000.50", dashboardData.getDisplayValue("grossPay")); // Large value
    }
    
    @Test
    @DisplayName("Test multiple processing methods don't interfere")
    public void testMultipleProcessingMethodsIntegration() {
        // Arrange
        EmployeeDetails employee2 = new EmployeeDetails();
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setBasicSalary(45000.0f);
        employee2.setRiceSubsidy(1800.0f);
        employee2.setPhoneAllowance(1200.0f);
        employee2.setClothingAllowance(800.0f);
        employee2.setTotalAllowances(3800.0f);
        
        // Act - Process different types of data
        dashboardData.processEmployeeData(employee2);
        dashboardData.processAllowanceData(employee2);
        dashboardData.processDeductionData(employee2, testSalaryCalculator);
        dashboardData.processSalaryData(employee2, testSalaryCalculator);
        
        // Assert - All data should be accessible and not interfere
        assertEquals("Jane", dashboardData.getDisplayValue("firstName"));
        assertEquals("Smith", dashboardData.getDisplayValue("lastName"));
        assertEquals("₱45000.00", dashboardData.getDisplayValue("basicSalary"));
        assertEquals("₱1800.00", dashboardData.getDisplayValue("riceSubsidy"));
        assertEquals("₱1200.00", dashboardData.getDisplayValue("phoneAllowance"));
        assertEquals("₱800.00", dashboardData.getDisplayValue("clothingAllowance"));
        assertEquals("₱3800.00", dashboardData.getDisplayValue("totalAllowances"));
        
        // Verify map contains expected entries
        Map<String, String> allData = dashboardData.getAllDisplayData();
        assertTrue(allData.size() > 10); // Should have many entries
        assertTrue(allData.containsKey("firstName"));
        assertTrue(allData.containsKey("basicSalary"));
        assertTrue(allData.containsKey("riceSubsidy"));
    }
    
    @Test
    @DisplayName("Test data persistence after multiple operations")
    public void testDataPersistenceAfterMultipleOperations() {
        // Arrange & Act
        dashboardData.processEmployeeData(testEmployee);
        String firstNameAfterEmployee = dashboardData.getDisplayValue("firstName");
        
        dashboardData.processAllowanceData(testEmployee);
        String firstNameAfterAllowance = dashboardData.getDisplayValue("firstName");
        String riceSubsidyAfterAllowance = dashboardData.getDisplayValue("riceSubsidy");
        
        dashboardData.processDeductionData(testEmployee, testSalaryCalculator);
        String firstNameAfterDeduction = dashboardData.getDisplayValue("firstName");
        String riceSubsidyAfterDeduction = dashboardData.getDisplayValue("riceSubsidy");
        
        // Assert - Data should persist across different processing methods
        assertEquals("John", firstNameAfterEmployee);
        assertEquals("John", firstNameAfterAllowance);
        assertEquals("John", firstNameAfterDeduction);
        assertEquals("₱2000.00", riceSubsidyAfterAllowance);
        assertEquals("₱2000.00", riceSubsidyAfterDeduction);
    }
}