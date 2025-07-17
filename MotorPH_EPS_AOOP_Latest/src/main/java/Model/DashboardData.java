/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Model;

/**
 *
 * @author dashcodes
 * SalaryCalculator.java will be transferred to EmployeeDetails.java
 * 
 */

import java.util.HashMap;
import java.util.Map;

public class DashboardData {
    private Map<String, String> displayData = new HashMap<>();
        
    /**
     * Processes employee details to generate dashboard display data
     * @param employee The employee details to process
     */
    public void processEmployeeData(EmployeeDetails employee) {
        if (employee != null) {
            // Store employee data in displayData map
            displayData.put("firstName", employee.getFirstName() != null ? employee.getFirstName() : "N/A");
            displayData.put("lastName", employee.getLastName() != null ? employee.getLastName() : "N/A");
            displayData.put("designation", employee.getDesignation() != null ? employee.getDesignation() : "N/A");
            displayData.put("address", employee.getAddress() != null ? employee.getAddress() : "N/A");
            displayData.put("phoneNumber", employee.getPhoneNumber() != null ? employee.getPhoneNumber() : "N/A");
            displayData.put("status", employee.getStatus() != null ? employee.getStatus() : "N/A");
            displayData.put("sssNumber", employee.getSss() != null ? employee.getSss() : "N/A");
            displayData.put("philHealthNumber", employee.getPhilHealth() != null ? employee.getPhilHealth() : "N/A");
            displayData.put("tinNumber", employee.getTin() != null ? employee.getTin() : "N/A");
            displayData.put("pagIbigNumber", employee.getPagIbig() != null ? employee.getPagIbig() : "N/A");

            // Financial Details
            displayData.put("basicSalary", formatCurrency(employee.getBasicSalary()));
            displayData.put("riceSubsidy", formatCurrency(employee.getRiceSubsidy()));
            displayData.put("phoneAllowance", formatCurrency(employee.getPhoneAllowance()));
            displayData.put("clothingAllowance", formatCurrency(employee.getClothingAllowance()));
            displayData.put("grossSemiMonthlyRate", formatCurrency(employee.getGrossSemiMonthlyRate()));
            displayData.put("hourlyRate", formatCurrency(employee.getHourlyRate()));

            // Incentives
            displayData.put("overTimePay", formatCurrency(employee.getOverTimePay()));
            displayData.put("performanceBonus", formatCurrency(employee.getPerformanceBonus()));
            displayData.put("holidayPay", formatCurrency(employee.getHolidayPay()));

            // Calculated Totals
            displayData.put("totalAllowances", formatCurrency(employee.getTotalAllowances()));
            displayData.put("totalIncentives", formatCurrency(employee.getTotalIncentives()));

            // Gross Pay and Net Pay (now retrieved directly from EmployeeDetails)
            displayData.put("grossPay", formatCurrency(employee.getGrossPay())); // Get grossPay directly from EmployeeDetails
            displayData.put("netPay", formatCurrency(employee.getNetPay())); // Get netPay directly from EmployeeDetails
            displayData.put("totalDeductions", formatCurrency(employee.getTotalDeductions())); // Also display total deductions

            displayData.put("overTime", String.valueOf(employee.getOverTime())); // This is just the hours
        }
    }
    
    /**
     * Processes allowance data to generate dashboard display data.
     * @param employee The EmployeeDetails object containing allowance data.
     */
    public void processAllowanceData(EmployeeDetails employee) {
        if (employee != null) {
            displayData.put("riceSubsidy", formatCurrency(employee.getRiceSubsidy()));
            displayData.put("phoneAllowance", formatCurrency(employee.getPhoneAllowance()));
            displayData.put("clothingAllowance", formatCurrency(employee.getClothingAllowance()));
            displayData.put("totalAllowances", formatCurrency(employee.getTotalAllowances()));
        }
    }

    /**
     * Processes deduction data to generate dashboard display data.
     * @param employee The EmployeeDetails object containing deduction data.
     * @param salaryCalculator The SalaryCalculator object to retrieve calculated deductions.
     */
    public void processDeductionData(EmployeeDetails employee, ViewModel.SalaryCalculator salaryCalculator) {
        if (employee != null && salaryCalculator != null) {
            displayData.put("sssContribution", formatCurrency(salaryCalculator.getDeduction("SSS")));
            displayData.put("philHealthContribution", formatCurrency(salaryCalculator.getDeduction("PhilHealth")));
            displayData.put("pagIbigContribution", formatCurrency(salaryCalculator.getDeduction("Pag-IBIG")));
            displayData.put("withholdingTax", formatCurrency(salaryCalculator.getDeduction("Withholding Tax")));
            displayData.put("totalDeductions", formatCurrency(salaryCalculator.getTotalDeductions()));
        }
    }

    /**
     * Processes salary data to generate dashboard display data.
     * @param employee The EmployeeDetails object containing basic salary and other financial data.
     * @param salaryCalculator The SalaryCalculator object to retrieve calculated gross and net pay.
     */
    public void processSalaryData(EmployeeDetails employee, ViewModel.SalaryCalculator salaryCalculator) {
        if (employee != null && salaryCalculator != null) {
            displayData.put("basicSalary", formatCurrency(employee.getBasicSalary()));
            displayData.put("grossPay", formatCurrency(salaryCalculator.getGrossPay()));
            displayData.put("netPay", formatCurrency(salaryCalculator.getNetPay()));
            displayData.put("overTimePay", formatCurrency(salaryCalculator.getOverTimePay()));
            displayData.put("holidayPay", formatCurrency(salaryCalculator.getHolidayPay()));
            displayData.put("performanceBonus", formatCurrency(salaryCalculator.getIncentive("Performance Bonus")));
            displayData.put("totalIncentives", formatCurrency(salaryCalculator.getTotalIncentives()));
        }
    }
    
    /**
     * Gets a display value by key
     * @param key The key for the display value
     * @return The formatted display value
     */
    public String getDisplayValue(String key) {
        return displayData.getOrDefault(key, "N/A");
    }
    
    /**
     * Formats a float value as currency with the peso sign (₱)
     * @param value The value to format
     * @return Formatted currency string
     */
    private String formatCurrency(float value) {
        if (value <= 0) {
            return "N/A";
        }
        return String.format("₱%.2f", value);
    }
    
    /**
     * Safely parses a string to float, returning 0 if parsing fails
     */
    private float parseFloatSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Clears all display data
     */
    public void clearDisplayData() {
        displayData.clear();
    }
    
    /**
     * Gets all display data as an unmodifiable map
     * @return Map of all display data
     */
    public Map<String, String> getAllDisplayData() {
        return java.util.Collections.unmodifiableMap(displayData);
    }
}