/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Model;

/**
 *
 * @author dashcodes
 */

import java.sql.Date;
import ViewModel.DBQueries;
import java.text.SimpleDateFormat;

public class EmployeeDetails {
    // Employee basic details
    private int eid;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String designation; 
    private String address;
    private String phoneNumber;
    private Date birthday; 
    private String status;
    private String immediateSupervisor; 
    private boolean isActive;
    
    // Government IDs
    private String sss;
    private String philHealth;
    private String tin;
    private String pagIbig;
    
    // Financial details
    private float basicSalary;
    private float riceSubsidy; 
    private float phoneAllowance; 
    private float clothingAllowance; 
    private float grossSemiMonthlyRate; 
    private float hourlyRate; 
    
    // Financial fields (Calculated/Derived)
    private float totalAllowances;
    private float totalDeductions;
    private float totalIncentives;
    private float grossPay;
    private float netPay;
    
    // Gross Pay Section (Variables)
    private float overTimePay;
    private float overTime;
    private float holidayPay;
    private float performanceBonus;
    
    // Earnings Section (Contributions/Deductions)
    private float sssContribution;
    private float philhealthContribution;
    private float pagibigContribution;
    private float withholdingTax;
    
    private DBQueries dbQueries;
    
    

    public EmployeeDetails(int eid, 
            String lastName, 
            String firstName, 
            Date birthday, 
            String userName, 
            String password, 
            String designation, 
            String address, 
            String phoneNumber, 
            String sss, 
            String philHealth, 
            String tin, 
            String pagIbig, 
            String status, 
            String immediateSupervisor, 
            float basicSalary, 
            float riceSubsidy, 
            float phoneAllowance, 
            float clothingAllowance,
            float overTime) {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.overTime = overTime;
    }

    public EmployeeDetails(int eid, 
            String lastName, 
            String firstName, 
            Date birthday, 
            String userName, 
            String password, 
            String designation, 
            String address, 
            String phoneNumber, 
            String sss, 
            String philHealth, 
            String tin, 
            String pagIbig, 
            String status, 
            String immediateSupervisor) {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
    }
    
    public EmployeeDetails(int eid, 
            String lastName, 
            String firstName, 
            Date birthday, 
            String userName, 
            String password, 
            String designation, 
            String address, 
            String phoneNumber, 
            String status, 
            String immediateSupervisor) {
        this(eid, lastName, firstName, birthday, userName,
            password, designation, address, phoneNumber, status);
    }

    
    public EmployeeDetails(
            int eid, 
            String lastName, 
            String firstName, 
            Date birthday, 
            String userName, 
            String password, 
            String designation, 
            String address, 
            String phoneNumber, 
            String status) {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
    }
    
    
    public EmployeeDetails(){
        
    }
    
    
    // Getters/Accessors
    public int getEid() {
        return eid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDesignation() {
        return designation;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getBirthday() {
        return birthday;
    }
    
    public String getStatus() {
        return status;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public String getSss() {
        return sss;
    }

    public String getPhilHealth() {
        return philHealth;
    }

    public String getTin() {
        return tin;
    }

    public String getPagIbig() {
        return pagIbig;
    }

    public float getBasicSalary() {
        return basicSalary;
    }

    public float getRiceSubsidy() {
        return riceSubsidy;
    }

    public float getPhoneAllowance() {
        return phoneAllowance;
    }

    public float getClothingAllowance() {
        return clothingAllowance;
    }

    public float getGrossSemiMonthlyRate() {
        return grossSemiMonthlyRate;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public float getTotalAllowances() {
        return totalAllowances;
    }

    public float getTotalDeductions() {
        return totalDeductions;
    }

    public float getTotalIncentives() {
        return totalIncentives;
    }

    public float getGrossPay() {
        return grossPay;
    }

    public float getNetPay() {
        return netPay;
    }

    public float getOverTimePay() {
        return overTimePay;
    }

    public float getOverTime() {
        return overTime;
    }

    public float getHolidayPay() {
        return holidayPay;
    }

    public float getPerformanceBonus() {
        return performanceBonus;
    }

    public float getSssContribution() {
        return sssContribution;
    }

    public float getPhilhealthContribution() {
        return philhealthContribution;
    }

    public float getPagibigContribution() {
        return pagibigContribution;
    }

    public float getWithholdingTax() {
        return withholdingTax;
    }
    
    /**
     * Gets the full name of the employee
     * @return 
     * @return  
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public DBQueries getDbQueries() {
        return dbQueries;
    }

    // Setters/Mutators
    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    /**
     *
     * @param formattedBirthday
     */
    public void setBirthday(String formattedBirthday) {
       DBQueries dbQueries = new DBQueries();
       try{
           this.birthday = dbQueries.convertStringToSqlDate(formattedBirthday);
      }catch (IllegalArgumentException e){
          System.err.println("Invalid date format: " + e.getMessage());
      }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }

    public void setPhilHealth(String philHealth) {
        this.philHealth = philHealth;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public void setPagIbig(String pagIbig) {
        this.pagIbig = pagIbig;
    }

    public void setBasicSalary(float basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setRiceSubsidy(float riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public void setPhoneAllowance(float phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(float clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public void setGrossSemiMonthlyRate(float grossSemiMonthlyRate) {
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
    }

    public void setHourlyRate(float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setTotalAllowances(float totalAllowances) {
        this.totalAllowances = totalAllowances;
    }

    public void setTotalDeductions(float totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public void setTotalIncentives(float totalIncentives) {
        this.totalIncentives = totalIncentives;
    }

    public void setGrossPay(float grossPay) {
        this.grossPay = grossPay;
    }

    public void setNetPay(float netPay) {
        this.netPay = netPay;
    }

    public void setOverTimePay(float overTimePay) {
        this.overTimePay = overTimePay;
    }

    public void setOverTime(float overTime) {
        this.overTime = overTime;
    }

    public void setHolidayPay(float holidayPay) {
        this.holidayPay = holidayPay;
    }

    public void setPerformanceBonus(float performanceBonus) {
        this.performanceBonus = performanceBonus;
    }

    public void setSssContribution(float sssContribution) {
        this.sssContribution = sssContribution;
    }

    public void setPhilhealthContribution(float philhealthContribution) {
        this.philhealthContribution = philhealthContribution;
    }

    public void setPagibigContribution(float pagibigContribution) {
        this.pagibigContribution = pagibigContribution;
    }

    public void setWithholdingTax(float withholdingTax) {
        this.withholdingTax = withholdingTax;
    }
    
    
    
    /**
     * Calculates the total deductions for the employee.
     * This method assumes that SSS, PhilHealth, PagIBIG, and Withholding Tax
     * contributions have already been loaded or calculated and set in this object.
     * @return The sum of all deductions.
     */
    public float calculateTotalDeductions() {
        // Assuming these values are already set in the object
        this.totalDeductions = this.sssContribution + this.philhealthContribution +
                               this.pagibigContribution + this.withholdingTax;
        return this.totalDeductions;
    }

    /**
     * Formats a date to string using the specified pattern
     * @param date
     */
    public String formatDate(Date date, String pattern) {
        if (date == null) return "N/A";
        return new SimpleDateFormat(pattern).format(date);
    }
    
    
    
    /**
     * Constructor matching the parameters shown in the provided image.
     *
     * @param eid The employee ID.
     * @param lastName The employee's last name.
     * @param firstName The employee's first name.
     * @param birthday The employee's birthday.
     * @param userName The employee's username for login.
     * @param password The employee's password for login.
     * @param designation The employee's job designation.
     * @param address The employee's address.
     * @param phoneNumber The employee's phone number.
     * @param sss The employee's SSS number.
     * @param philHealth The employee's PhilHealth number.
     * @param tin The employee's TIN number.
     * @param pagIbig The employee's Pag-IBIG number.
     * @param status The employee's employment status.
     * @param immediateSupervisor The name of the employee's immediate supervisor.
     * @param dbQueries An instance of DBQueries (NOT RECOMMENDED for a Model class).
     */
    public EmployeeDetails(Date birthday, int eid,
            String lastName,
            String firstName,
            Date Birthday,
            String userName,
            String password,
            String designation,
            String address,
            String phoneNumber,
            String sss,
            String philHealth,
            String tin,
            String pagIbig,
            String status,
            String immediateSupervisor)
    {
        this(
            eid, 
            lastName,
            firstName,
            birthday,
            userName,
            password,
            designation,
            address,
            phoneNumber,
            sss,
            philHealth,
            tin,
            pagIbig,
            status,
            immediateSupervisor,
            Birthday);
    }

    /**
     * Constructor matching the parameters shown in the provided image.
     *
     * @param eid The employee ID.
     * @param lastName The employee's last name.
     * @param firstName The employee's first name.
     * @param birthday The employee's birthday.
     * @param userName The employee's username for login.
     * @param password The employee's password for login.
     * @param designation The employee's job designation.
     * @param address The employee's address.
     * @param phoneNumber The employee's phone number.
     * @param sss The employee's SSS number.
     * @param philHealth The employee's PhilHealth number.
     * @param tin The employee's TIN number.
     * @param pagIbig The employee's Pag-IBIG number.
     * @param status The employee's employment status.
     * @param immediateSupervisor The name of the employee's immediate supervisor.
     * @param dbQueries An instance of DBQueries (NOT RECOMMENDED for a Model class).
     */
    public EmployeeDetails(
            int eid,
            String lastName,
            String firstName,
            Date birthday,
            String userName,
            String password,
            String designation,
            String address,
            String phoneNumber,
            String sss,
            String philHealth,
            String tin,
            String pagIbig,
            String status,
            String immediateSupervisor,
            Date Birthday) {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
    }
    
    public EmployeeDetails(int eid, 
            String firstName, 
            String lastName, 
            String userName, 
            String password, 
            String designation, 
            String address, 
            String phoneNumber, 
            Date birthday, 
            String status, 
            String immediateSupervisor, 
            String sss, 
            String philHealth, 
            String tin, 
            String pagIbig, 
            float basicSalary, 
            float riceSubsidy, 
            float phoneAllowance, 
            float clothingAllowance, 
            float grossSemiMonthlyRate, 
            float hourlyRate, 
            float totalAllowances, 
            float totalDeductions, 
            float totalIncentives, 
            float grossPay, 
            float netPay, 
            float overTimePay, 
            float overTime, 
            float holidayPay, 
            float performanceBonus, 
            float sssContribution, 
            float philhealthContribution, 
            float pagibigContribution, 
            float withholdingTax) {
        this.eid = eid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.designation = designation;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.status = status;
        this.immediateSupervisor = immediateSupervisor;
        this.sss = sss;
        this.philHealth = philHealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
        this.totalAllowances = totalAllowances;
        this.totalDeductions = totalDeductions;
        this.totalIncentives = totalIncentives;
        this.grossPay = grossPay;
        this.netPay = netPay;
        this.overTimePay = overTimePay;
        this.overTime = overTime;
        this.holidayPay = holidayPay;
        this.performanceBonus = performanceBonus;
        this.sssContribution = sssContribution;
        this.philhealthContribution = philhealthContribution;
        this.pagibigContribution = pagibigContribution;
        this.withholdingTax = withholdingTax;
    }           
}