/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ViewModel;

/**
 * @author Jafph and dashcodes
 */

import Model.Allowances;
import Model.Deductions;
import Model.Incentives;
import Model.EmployeeDetails;
import Repository.DataSource; 
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles all salary-related calculations including gross pay, deductions, incentives, and net pay.
 * */

public class SalaryCalculator {
    // Constants for deduction rates (kept for reference, though calculations are now mostly DB-side)
    private static final float SSS_RATE = 0.045f;
    private static final float PHILHEALTH_RATE = 0.035f;
    private static final float PAGIBIG_RATE = 0.02f;
    private static final float TAX_RATE = 0.15f;
    private static final float OVERTIME_RATE = 1.25f;
    private static final int WORKING_DAYS = 26; 
    
    private final Map<String, Float> deductions = new HashMap<>();
    private final Map<String, Float> incentives = new HashMap<>(); // Will only populate with fetched incentives
    private float grossPay;
    private float overTimePay;
    private float holidayPay; // Will be 0 if not explicitly fetched from DBQueries.getPayslipData
    private float totalAllowances;
    private float totalDeductions;
    private float totalIncentives;
    private float netPay;
    private float riceSubsidy;
    private float phoneAllowance;
    private float clothingAllowance;
    private float performanceBonus;
    private float basicSalary;
    private float sssContribution;
    private float philHealthContribution;
    private float pagIbigContribution;
    private float withholdingTax;
    
    private DBQueries dbQueries;

    public SalaryCalculator() {
        this.dbQueries = new DBQueries(); 
        resetCalculations();
    }
    
    /**
     * Formats a float value as currency with the peso sign (₱)
     * @param value The value to format
     * @return Formatted currency string
     */
    private String formatCurrency(float value) {
        return String.format("₱%.2f", value);
    }
    
    public String getFormattedDeduction(String key) {
        Float rawValue = deductions.get(key);  
        return rawValue != null ? formatCurrency(rawValue) : "₱0.00";
    }

    /**
     * Calculates the gross pay for an employee by fetching pre-calculated payroll data
     * from the database using the provided EID and date range. This method now serves
     * as the primary entry point to load all salary components.
     * @param employee The EmployeeDetails object containing the employee's EID.
     * @param dateFrom The start date for the payroll period (MM/dd/yyyy).
     * @param dateTo The end date for the payroll period (MM/dd/yyyy).
     */
    public void calculateSalaryFromDB(EmployeeDetails employee, String dateFrom, String dateTo) {
    resetCalculations(); 
    
    try (Connection conn = DataSource.getInstance().getConnection()) {
        if (dbQueries == null) {
            dbQueries = new DBQueries();
        }

        // Convert date strings to java.sql.Date
        java.sql.Date sqlDateFrom = dbQueries.convertStringToSqlDate(dateFrom);
        java.sql.Date sqlDateTo = dbQueries.convertStringToSqlDate(dateTo);

        try (PreparedStatement ps = conn.prepareStatement(dbQueries.getPayslipData)) {
            // Set parameters for the query (3 parameters in total)
            ps.setDate(1, sqlDateFrom);   // days_worked start
            ps.setDate(2, sqlDateTo);     // days_worked end
            ps.setInt(3, employee.getEid()); // EID filter

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Populate fields from ResultSet
                    this.grossPay = rs.getFloat("Monthly_Rate");
                    this.basicSalary = rs.getFloat("Monthly_Rate");
                    this.riceSubsidy = rs.getFloat("Rice_Subsidy");
                    this.phoneAllowance = rs.getFloat("Phone_Allowance");
                    this.clothingAllowance = rs.getFloat("Clothing_Allowance");
                    this.overTimePay = rs.getFloat("Overtime_Pay");
		    this.holidayPay = rs.getFloat("Holiday_Pay");
            	    this.performanceBonus = rs.getFloat("Performance_Bonus");
                     
                    
                    // Calculate totals
                    this.totalAllowances = this.riceSubsidy + this.phoneAllowance + this.clothingAllowance;
                    this.totalIncentives = this.overTimePay + this.holidayPay + this.performanceBonus; 
                    
                    // Populate deductions map
                    deductions.put("SSS", rs.getFloat("SSS_Contribution"));
                    deductions.put("PhilHealth", rs.getFloat("Philhealth_Contribution"));
                    deductions.put("PagIBIG", rs.getFloat("Pagibig_Contribution"));
                    deductions.put("WithholdingTax", rs.getFloat("Withholding_Tax"));
                    
                    this.totalDeductions = deductions.get("SSS") + deductions.get("PhilHealth") + deductions.get("PagIBIG") + deductions.get("WithholdingTax");
                    
                    this.netPay = this.grossPay + 
                                  this.totalAllowances - 
                                  this.totalDeductions;
                    }
                }
            }
        } catch (SQLException e) {
            resetCalculations();
        }
    }
   
    
    /**
     * Calculates the net pay based on gross pay and total deductions.
     * Assumes grossPay and totalDeductions are already calculated (or fetched from DB).
     * This method is called internally after fetching data from the DB.
     */
    public void calculateNetPay() {
        this.netPay = this.grossPay + this.totalIncentives - this.totalDeductions;
    }
    
    
    /**
     * Retrieves a specific deduction amount by its key.
     * @param key The name of the deduction (e.g., "SSS", PhilHealth, Pag-Ibig, "Withholding Tax").
     * @return The deduction amount, or 0 if not found.
     */
    public float getDeduction(String key) {
        return deductions.getOrDefault(key, 0f);
    }
    
    /**
     * Retrieves a specific incentive amount by its key.
     * @param key The name of the incentive (e.g., "Overtime Pay", "Holiday Pay").
     * @return The incentive amount, or 0 if not found.
     */
    public float getIncentive(String key) {
        return incentives.getOrDefault(key, 0f);
    }
    
    /**
     * Returns an unmodifiable map of all calculated deductions.
     * @return A map where keys are deduction names and values are their amounts.
     */
    public Map<String, Float> getAllDeductions() {
        return Map.copyOf(deductions);
    }
    
    /**
     * Returns an unmodifiable map of all calculated incentives.
     * @return A map where keys are incentive names and values are their amounts.
     */
    public Map<String, Float> getAllIncentives() {
        return Map.copyOf(incentives);
    }
    
    /**
     * Returns the calculated gross pay.
     * @return The gross pay.
     */
    public float getGrossPay() {
        return grossPay;
    }
    
    /**
     * Returns the total calculated allowances.
     * @return The total allowances.
     */
    public float getTotalAllowances() {
        return totalAllowances;
    }
    
    /**
     * Returns the total calculated incentives.
     * @return The total incentives.
     */
    public float getTotalIncentives() {
        return totalIncentives;
    }
    
    /**
     * Returns the calculated overtime pay.
     * @return The overtime pay.
     */
    public float getOverTimePay() {
        return overTimePay;
    }
    
    /**
     * Returns the calculated holiday pay. Note: This may be 0 if not fetched from the DB.
     * @return The holiday pay.
     */
    public float getHolidayPay() {
        return holidayPay;
    }
    
    /**
     * Returns the total calculated deductions.
     * @return The total deductions.
     */
    public float getTotalDeductions() {
        return totalDeductions;
    }
    
    /**
     * Returns the calculated net pay (take-home pay).
     * @return The net pay.
     */
    public float getNetPay() {
        return netPay;
    }

    /**
     * Returns the calculated rice subsidy.
     * @return The rice subsidy amount.
     */
    public float getRiceSubsidy() {
        return riceSubsidy;
    }

    /**
     * Returns the calculated phone allowance.
     * @return The phone allowance amount.
     */
    public float getPhoneAllowance() {
        return phoneAllowance;
    }

    /**
     * Returns the calculated clothing allowance.
     * @return The clothing allowance amount.
     */
    public float getClothingAllowance() {
        return clothingAllowance;
    }
    
    /**
     * Returns the calculated performance bonus.
     * @return The performance bonus amount.
     */
    public float getPerformanceBonus() {
        return performanceBonus;
    }
    
        /**
     * Returns the basic salary.
     * @return The basic salary amount.
     */
    public float getBasicSalary() {
        return basicSalary;
    }
    
    /**
     * Returns the SSS Contribution
     * @return the SSS Contribution Amount
     */
    public float getSssContribution(){
        return deductions.getOrDefault("SSS", 0f);
    }
    
    /**
     * Returns the PhilHealth Contribution
     * @return the PhilHealth Contribution Amount
     */
    public float getPhilHealthContribution(){
        return deductions.getOrDefault("PhilHealth", 0f);
    }
    
    /**
     * Returns the PagIbig Contribution
     * @return the PagIbig Contribution Amount
     */
    public float getPagIbigContribution(){
        return deductions.getOrDefault("PagIBIG", 0f);
    }
    
    /**
     * Returns the Withholding Tax Contribution
     * @return the Withholding Tax Contribution Amount
     */
    public float getWithholdingTax(){
        return deductions.getOrDefault("WithholdingTax", 0f);
    }
    
    
    /**
     * Resets all calculation values to their initial state.
     */
    public void resetCalculations() {
        this.grossPay = 0;
        this.overTimePay = 0;
        this.holidayPay = 0;
        this.performanceBonus = 0;
        this.totalAllowances = 0;
        this.totalDeductions = 0;
        this.totalIncentives = 0;
        this.netPay = 0;
        deductions.clear();
        incentives.clear();
    }
    
    /**
     * Safely parses a string to float. Handles null, empty, and currency symbols.
     * @param value The string to parse.
     * @return The parsed float value, or 0 if parsing fails.
     */
    private float parseFloatSafely(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        String cleanValue = value.replace("₱", "").replace(",", "").trim();
        try {
            return Float.parseFloat(cleanValue);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing float value: " + cleanValue + " - " + e.getMessage());
            return 0;
        }
    }
}