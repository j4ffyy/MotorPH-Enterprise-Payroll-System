/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Employee Reports Business Logic Class
 * Contains getters and setters for employee report data
 * 
 * @author dashcodes
 */


public class EmployeeReports {
        private int eid;
        private String lastName;
        private String firstName;
        private Date birthday;
        private String address;
        private String phoneNumber;
        private String username;
        private String status;
        private String designation;
        private String immediateSupervisor;

        // Government IDs
        private String sssNumber;
        private String philhealthNumber;
        private String tinNumber;
        private String pagibigNumber;

        // Payroll Components
        private BigDecimal basicSalary;
        private BigDecimal riceSubsidy;
        private BigDecimal phoneAllowance;
        private BigDecimal clothingAllowance;
        private BigDecimal halfMonthRate;
        private BigDecimal hourlyRate;
        
        // Constructors
    public EmployeeReports() {
    }
    
    public EmployeeReports(int eid, String lastName, String firstName, 
                          Date birthday, String address, String phoneNumber, 
                          String username, String status, String designation, 
                          String immediateSupervisor) {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.status = status;
        this.designation = designation;
        this.immediateSupervisor = immediateSupervisor;
    }

    public EmployeeReports(int eid, 
            String lastName, 
            String firstName, 
            String designation, 
            String sss, 
            String philHealth, 
            String tin, 
            String pagIbig, 
            float basicSalary, 
            float hourlyRate, 
            float riceSubsidy, 
            float phoneAllowance, 
            float clothingAllowance) {
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.designation = designation;
        this.sssNumber = sss;
        this.philhealthNumber = philHealth;
        this.tinNumber = tin;
        this.pagibigNumber = pagIbig;
            BigDecimal basicSalary1 = this.basicSalary;
            BigDecimal hourlyRate1 = this.hourlyRate;
            BigDecimal riceSubsidy1 = this.riceSubsidy;
            BigDecimal phoneAllowance1 = this.phoneAllowance;
            BigDecimal clothingAllowance1 = this.clothingAllowance;
    }
    
    // Getters

    public int getEid() {
        return eid;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

    public String getDesignation() {
        return designation;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getPagibigNumber() {
        return pagibigNumber;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public BigDecimal getRiceSubsidy() {
        return riceSubsidy;
    }

    public BigDecimal getPhoneAllowance() {
        return phoneAllowance;
    }

    public BigDecimal getClothingAllowance() {
        return clothingAllowance;
    }

    public BigDecimal getHalfMonthRate() {
        return halfMonthRate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }
    
    // Setters

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }

    public void setSssNumber(String sssNumber) {
        this.sssNumber = sssNumber;
    }

    public void setPhilhealthNumber(String philhealthNumber) {
        this.philhealthNumber = philhealthNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public void setPagibigNumber(String pagibigNumber) {
        this.pagibigNumber = pagibigNumber;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setRiceSubsidy(BigDecimal riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public void setPhoneAllowance(BigDecimal phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(BigDecimal clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public void setHalfMonthRate(BigDecimal halfMonthRate) {
        this.halfMonthRate = halfMonthRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
    
     // Utility Methods
    public BigDecimal getTotalAllowances() {
        BigDecimal total = BigDecimal.ZERO;
        if (riceSubsidy != null) total = total.add(riceSubsidy);
        if (phoneAllowance != null) total = total.add(phoneAllowance);
        if (clothingAllowance != null) total = total.add(clothingAllowance);
        return total;
    }
    
    @Override
    public String toString() {
        return "EmployeeReports{" +
                "eid=" + eid +
                ", fullName='" + getLastName() + "," + getFirstName() + '\'' +
                ", designation='" + designation + '\'' +
                ", status='" + status + '\'' +
                ", basicSalary=" + basicSalary +
                '}';
    }
    
}
