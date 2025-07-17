package Model;

import java.math.BigDecimal;

public class PayslipData {
    private Integer employeeId;
    private String employeeName;
    private String position;
    private String department; // No direct mapping, will be empty or null
    private BigDecimal monthlyRate;
    private BigDecimal dailyRate;
    private Long daysWorked;
    private Integer overtimeHours;
    private BigDecimal overtimePay;
    private BigDecimal grossIncome;
    private BigDecimal riceSubsidy;
    private BigDecimal phoneAllowance;
    private BigDecimal clothingAllowance;
    private BigDecimal totalBenefits;
    private BigDecimal sssContribution;
    private BigDecimal philhealthContribution;
    private BigDecimal pagibigContribution;
    private BigDecimal withholdingTax;
    private BigDecimal totalDeductions;
    private BigDecimal takeHomePay;

    // Getters and Setters

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Long getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Long daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Integer getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Integer overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(BigDecimal grossIncome) {
        this.grossIncome = grossIncome;
    }

    public BigDecimal getRiceSubsidy() {
        return riceSubsidy;
    }

    public void setRiceSubsidy(BigDecimal riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public BigDecimal getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(BigDecimal phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public BigDecimal getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(BigDecimal clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public BigDecimal getTotalBenefits() {
        return totalBenefits;
    }

    public void setTotalBenefits(BigDecimal totalBenefits) {
        this.totalBenefits = totalBenefits;
    }

    public BigDecimal getSssContribution() {
        return sssContribution;
    }

    public void setSssContribution(BigDecimal sssContribution) {
        this.sssContribution = sssContribution;
    }

    public BigDecimal getPhilhealthContribution() {
        return philhealthContribution;
    }

    public void setPhilhealthContribution(BigDecimal philhealthContribution) {
        this.philhealthContribution = philhealthContribution;
    }

    public BigDecimal getPagibigContribution() {
        return pagibigContribution;
    }

    public void setPagibigContribution(BigDecimal pagibigContribution) {
        this.pagibigContribution = pagibigContribution;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public BigDecimal getTakeHomePay() {
        return takeHomePay;
    }

    public void setTakeHomePay(BigDecimal takeHomePay) {
        this.takeHomePay = takeHomePay;
    }
}
