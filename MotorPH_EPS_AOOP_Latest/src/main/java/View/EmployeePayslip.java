/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

/**
 *
 * @author dashcodes
 */

import Repository.DataSource;
import Model.Allowances;
import Model.Deductions;
import Model.Incentives;
import Model.DashboardData;
import Model.EmployeeDetails;
import ViewModel.DBQueries;
import ViewModel.RoleAuthenticator;
import ViewModel.SalaryCalculator;
import ViewModel.UserSession;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EmployeePayslip extends javax.swing.JFrame {
    // Core Components
    private String loggedInUsername;
    
    // Business Logic Components
    private EmployeeDetails employeeDetails;
    private Allowances allowances;
    private Deductions deductions;
    private Incentives incentives;
    private DashboardData dashboardData;
    private SalaryCalculator salaryCalculator;
    private RoleAuthenticator roleAuthenticator;
    
    // Number formatter for consistent currency display
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getInstance(Locale.US);
    
    static {
        CURRENCY_FORMATTER.setMinimumFractionDigits(2);
        CURRENCY_FORMATTER.setMaximumFractionDigits(2);
    }
    
    public EmployeePayslip() {
    initComponents();
    try {
        Connection conn = DataSource.getInstance().getConnection();
    }   catch (SQLException ex) {
            Logger.getLogger(EmployeePayslip.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        // Get user from session
        EmployeeDetails user = UserSession.getInstance().getLoggedInUser();
        if (user != null) {
            // Update UI with user details
            // Use the correct component names as in your original constructor
            this.fullName.setText(user.getFirstName() + " " + user.getLastName());
            this.designationWithEid.setText(user.getDesignation() + " -- " + user.getEid());
        } else {
            // Handle case when user is not in session
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            // Redirect to login
            new LoginPage().setVisible(true);
            this.dispose();
            }
        }
    
     /**
     * Constructor that accepts EID to load user-specific data
     * @param eid The employee ID of the user
     */
    public EmployeePayslip (String eid) {
        initComponents();
        
        // Load employee data and initialize dashboard by EID
        initializeDashboardByEid(eid);
        
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Initialize role authenticator
        this.roleAuthenticator = new RoleAuthenticator();
        
        // Stores the EID
        this.loggedInUsername = eid; // Using loggedInUsername to store EID to minimize changes
        
        
    }

    /**
     * Constructor that accepts basic user data
     * @param firstName Employee's first name
     * @param lastName Employee's last name
     * @param designation Employee's job title
     * @param eid Employee ID
     * @param connection Database connection
     */
    public EmployeePayslip(
        String firstName,
        String lastName,
        String designation,
        int eid) 
    {
        initComponents();
        
        // Update UI with user data
        String fullName = firstName + " " + lastName;
        String designationWithEid = designation + " -- " + eid;
        
        // Set these values to UI components
        this.fullName.setText(fullName);
        this.designationWithEid.setText(designationWithEid);
    }
    
    /**
     * Constructor with comprehensive employee data
     * @param firstName Employee's first name
     * @param lastName Employee's last name
     * @param designation Employee's job title
     * @param eid Employee ID
     * @param basicSalary Employee's basic pay
     * @param overTimePay Employee's overtime pay
     * @param holidayPay Employee's holiday pay
     * @param performanceBonus Employee's performance bonus
     * @param riceAllowance Employee's rice allowance
     * @param phoneAllowance Employee's phone allowance
     * @param clothingAllowance Employee's clothing allowance
     * @param pagIbigContribution Employee's PAG-IBIG contribution
     * @param philHealthContribution Employee's PhilHealth contribution
     * @param sssContribution Employee's SSS contribution
     * @param withholdingTax Employee's withholding tax
     * @param grossPay Employee's gross pay
     * @param totalAllowances Employee's total allowances
     * @param totalIncentives Employee's total incentives
     * @param totalDeductions Employee's total deductions
     * @param netPay Employee's net pay
     * @param connection Database connection
     */
    public EmployeePayslip(
        // Basic Information
        String firstName,
        String lastName,
        String designation,
        int eid,
        
        // Gross Pay Values
        float basicSalary,
        float overTimePay,
        float holidayPay,
        float performanceBonus,
        
        // Allowances Values
        float riceAllowance,
        float phoneAllowance,
        float clothingAllowance,
        
        // Deductions Values
        float pagIbigContribution,
        float philHealthContribution,
        float sssContribution,
        float withholdingTax,
        
        // Payroll Summary
        float grossPay,
        float totalAllowances,
        float totalIncentives,
        float totalDeductions,
        float netPay) 
    {
        initComponents();
        
       
        
        // Update UI with user data
        String fullNameLbl = firstName + " " + lastName;
        String designationWithEidLbl = designation + " -- " + eid;
        
        // Set basic employee information
        this.fullName.setText(fullNameLbl);
        this.designationWithEid.setText(designationWithEidLbl);
        
        // Update all UI elements with formatted values
        updateFinancialUI(// Gross Pay values
            basicSalary, 
            overTimePay, 
            holidayPay, 
            performanceBonus,
            
            // Allowances Values
            riceAllowance, 
            phoneAllowance, 
            clothingAllowance,
            
            // Deductions Values
            sssContribution, 
            philHealthContribution, 
            pagIbigContribution, 
            withholdingTax,
            
            // Payroll Summary Values
            grossPay, 
            totalAllowances,
            totalIncentives,
            totalDeductions,
            netPay);
    }
    
    
    /**
     * Update the financial UI components with formatted values
     * 
     * @param basicSalary Employee's basic pay
     * @param overTimePay Employee's overtime pay
     * @param holidayPay Employee's holiday pay
     * @param performanceBonus Employee's performance bonus
     * @param riceAllowance Employee's rice allowance
     * @param phoneAllowance Employee's phone allowance
     * @param clothingAllowance Employee's clothing allowance
     * @param sssContribution Employee's SSS contribution
     * @param philHealthContribution Employee's PhilHealth contribution
     * @param pagIbigContribution Employee's Pag-IBIG contribution
     * @param withholdingTax Employee's withholding tax
     * @param grossPay Employee's gross pay
     * @param totalAllowances Employee's total allowances
     * @param totalDeductions Employee's total deductions
     * @param netPay Employee's net pay
     */
    private void updateFinancialUI(
            float basicSalary, 
            float overTimePay, 
            float holidayPay, 
            float performanceBonus, 
            float riceAllowance, 
            float phoneAllowance, 
            float clothingAllowance, 
            float sssContribution, 
            float philHealthContribution, 
            float pagIbigContribution, 
            float withholdingTax, 
            float grossPay, 
            float overTime, 
            float totalAllowances, 
            float totalIncentives, 
            float totalDeductions) {

        /// Assigned jLabels + formatted values
        
        // Gross Pay Section
        this.grossSalary1.setText(formatCurrency(salaryCalculator.getGrossPay()));
        this.overTimeHours.setText(String.valueOf(overTime));
        
        
        // Allowances Section
        this.riceAllowance.setText(formatCurrency(riceAllowance));
        this.phoneAllowance.setText(formatCurrency(phoneAllowance));
        this.clothingAllowance.setText(formatCurrency(clothingAllowance));
        
        // Deductions Section
        this.sssContribution.setText(formatCurrency(salaryCalculator.getDeduction("SSS")));
        this.philHealthContribution.setText(formatCurrency(salaryCalculator.getDeduction("PhilHealth")));
        this.pagIbigContribution.setText(formatCurrency(salaryCalculator.getDeduction("PagIBIG")));
        this.withholdingTax.setText(formatCurrency(salaryCalculator.getDeduction("WithholdingTax")));
        
        // Payroll Summary
        this.grossSalary.setText(formatCurrency(salaryCalculator.getGrossPay()));
        this.totalAllowances.setText(formatCurrency(totalAllowances));
        this.totalIncentives1.setText(formatCurrency(totalIncentives));
        this.totalDeductions.setText(formatCurrency(totalDeductions));
        this.netPayAmount.setText(formatCurrency(salaryCalculator.getNetPay()));
    }
    
     /**
     * Format a floating-point value as currency with "₱" prefix
     * 
     * @param value The value to format
     * @return Formatted currency string
     */
    private String formatCurrency(float value) {
        return "₱" + CURRENCY_FORMATTER.format(value);
    }
    
    /**
     * Initialize the dashboard with user data by EID
     * 
     * @param eid The employee ID of the user
     */
    private void initializeDashboardByEid(String eid) {
        try {
            // Load employee data
            employeeDetails = new Repository.EmployeeRepository().getEmployeeByEid(eid);
            
            if (employeeDetails != null) {
                
                // Initializes salary calculator
                salaryCalculator = new SalaryCalculator();
                
                // Period Dates for Calculation
                String dateFrom = "12/01/2023"; // Placeholder start date
                String dateTo = "12/31/2023";   // Placeholder end date
                salaryCalculator.calculateSalaryFromDB(employeeDetails, dateFrom, dateTo);
                
                System.out.println("Employee loaded: " + employeeDetails.getFirstName() + " " + 
                    employeeDetails.getLastName() + ", " + employeeDetails.getDesignation());
                System.out.println("Employee ID: " + employeeDetails.getEid());
                
                // Initializes allowances object
                allowances = new Allowances(employeeDetails.getEid());
                allowances.setRiceAllowance(employeeDetails.getRiceSubsidy());
                allowances.setPhoneAllowance(employeeDetails.getPhoneAllowance());
                allowances.setClothingAllowance(employeeDetails.getClothingAllowance());
                
                // Initializes deductions object
                deductions = new Deductions(employeeDetails.getEid());
            	deductions.setSssContribution(salaryCalculator.getDeduction("SSS"));
            	deductions.setPhilHealthContribution(salaryCalculator.getDeduction("PhilHealth"));
            	deductions.setPagIbigContribution(salaryCalculator.getDeduction("PagIBIG"));
            	deductions.setWithholdingTax(salaryCalculator.getDeduction("Withholding Tax"));
            	deductions.setTotalDeductions(salaryCalculator.getTotalDeductions());
                
                // Initializes incentives object 
                incentives = new Incentives(employeeDetails.getEid());
                incentives.setOverTimePay(salaryCalculator.getOverTimePay()); 
            	incentives.setHolidayPay(salaryCalculator.getHolidayPay());
            	incentives.setPerformanceBonus(salaryCalculator.getIncentive("Performance Bonus"));
                incentives.setTotalIncentives(salaryCalculator.getTotalIncentives());
                
                // Initializes dashboard components
                dashboardData = new DashboardData();
                dashboardData.processEmployeeData(employeeDetails);
                dashboardData.processAllowanceData(employeeDetails);
                dashboardData.processDeductionData(employeeDetails, salaryCalculator);
                dashboardData.processSalaryData(employeeDetails, salaryCalculator);
                
                // Update UI with employee data
                updateUI();
            }
            else {
                JOptionPane.showMessageDialog(this, 
                    "Employee data not found for EID: " + eid, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeePayslip.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error initializing dashboard: " + ex.getMessage(), 
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Initialize the dashboard with user data
     * 
     * @param username The username of the logged-in user
     */
    
    private void initializeDashboard(String username) {
        try {
            // Load employee data
            employeeDetails = new Repository.EmployeeRepository().getEmployeeByUsername(username);
            
            if (employeeDetails != null) {
                
                // Initializes salary calculator
                salaryCalculator = new SalaryCalculator();
                
                // Period Dates for Calculation
                String dateFrom = "2023-12-01"; // Placeholder start date
                String dateTo = "2023-12-31";   // Placeholder end date
                salaryCalculator.calculateSalaryFromDB(employeeDetails, dateFrom, dateTo);
                
                System.out.println("Employee loaded: " + employeeDetails.getFirstName() + " " + 
                    employeeDetails.getLastName() + ", " + employeeDetails.getDesignation());
                System.out.println("Employee ID: " + employeeDetails.getEid());
                
                // Initializes allowances object
                allowances = new Allowances(employeeDetails.getEid());
                allowances.setRiceAllowance(employeeDetails.getRiceSubsidy());
                allowances.setPhoneAllowance(employeeDetails.getPhoneAllowance());
                allowances.setClothingAllowance(employeeDetails.getClothingAllowance());
                
                // Initializes deductions object
                deductions = new Deductions(employeeDetails.getEid());
            	deductions.setSssContribution(salaryCalculator.getDeduction("SSS"));
            	deductions.setPhilHealthContribution(salaryCalculator.getDeduction("PhilHealth"));
            	deductions.setPagIbigContribution(salaryCalculator.getDeduction("PagIBIG"));
            	deductions.setWithholdingTax(salaryCalculator.getDeduction("Withholding Tax"));
            	deductions.setTotalDeductions(salaryCalculator.getTotalDeductions());
                
                // Initializes incentives object 
                incentives = new Incentives(employeeDetails.getEid());
                incentives.setOverTimePay(salaryCalculator.getOverTimePay()); 
            	incentives.setHolidayPay(salaryCalculator.getHolidayPay());
            	incentives.setPerformanceBonus(salaryCalculator.getIncentive("Performance Bonus"));
                incentives.setTotalIncentives(salaryCalculator.getTotalIncentives());
                
                // Initializes dashboard components
                dashboardData = new DashboardData();
                dashboardData.processEmployeeData(employeeDetails);
                dashboardData.processAllowanceData(employeeDetails);
                dashboardData.processDeductionData(employeeDetails, salaryCalculator);
                dashboardData.processSalaryData(employeeDetails, salaryCalculator);
                
                // Update UI with employee data
                updateUI();
            }
            else {
                JOptionPane.showMessageDialog(this, 
                    "Employee data not found for username: " + username, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeePayslip.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error initializing dashboard: " + ex.getMessage(), 
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    

    
    /**
     * Updates the interface with employee details from the business logic components
     */
    private void updateUI() {
        try {
            // Update basic information
            fullName.setText(employeeDetails.getFirstName() + " " + employeeDetails.getLastName());
            designationWithEid.setText(employeeDetails.getDesignation() + " -- " + employeeDetails.getEid());

            // Calculate values (ensure these are float variables, not JLabels)
            float basicSalary = employeeDetails.getBasicSalary();
            float overTimePay = salaryCalculator.getIncentive("overtimepay"); 
            float holidayPay = salaryCalculator.getIncentive("holidaypay");  
            float performanceBonus = salaryCalculator.getIncentive("performancebonus"); 

            float riceAllowance = employeeDetails.getRiceSubsidy();  
            float phoneAllowance = allowances.getPhoneAllowance();
            float clothingAllowance = allowances.getClothingAllowance();

            float sssContribution = salaryCalculator.getDeduction("sss");
            float philHealthContribution = salaryCalculator.getDeduction("philhealth");
            float pagIbigContribution = salaryCalculator.getDeduction("PagIBIG");
            float withholdingTax = salaryCalculator.getDeduction("tax");

            float grossPay = salaryCalculator.getGrossPay(); 
            float totalAllowances = salaryCalculator.getTotalAllowances();
            float totalIncentives = salaryCalculator.getTotalIncentives();
            float totalDeductions = salaryCalculator.getTotalDeductions();
            float netPay = salaryCalculator.getNetPay();

            // Pass float variables to updateFinancialUI
            updateFinancialUI(
                basicSalary,
                overTimePay,
                holidayPay,
                performanceBonus,
                riceAllowance,
                phoneAllowance,
                clothingAllowance,
                sssContribution, 
                philHealthContribution,
                pagIbigContribution,
                withholdingTax,
                grossPay, 
                totalAllowances,
                totalIncentives,
                totalDeductions,  
                netPay            
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error updating UI: " + ex.getMessage(), 
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        NavigationPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        dashboardPanel = new javax.swing.JPanel();
        dashboardPane = new javax.swing.JLabel();
        profilePanel = new javax.swing.JPanel();
        payPeriodLabel = new javax.swing.JLabel();
        payrollCycle = new javax.swing.JLabel();
        homeButton = new javax.swing.JButton();
        fullName = new javax.swing.JLabel();
        designationWithEid = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        riceAllowance = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        phoneAllowance = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        clothingAllowance = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        sssContribution = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        philHealthContribution = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        pagIbigContribution = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        withholdingTax = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        totalAllowances = new javax.swing.JLabel();
        totalDeductions = new javax.swing.JLabel();
        netPayAmount = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        grossSalary = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        totalIncentives1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        overTimeHours = new javax.swing.JLabel();
        numberOfDays = new javax.swing.JLabel();
        grossSalary1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        NavigationPanel.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogo small.png"))); // NOI18N

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(37, 61, 144));
        dashboardPane.setText("Employee Pay Slip");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        payPeriodLabel.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        payPeriodLabel.setForeground(new java.awt.Color(255, 255, 255));
        payPeriodLabel.setText("March 2025");

        payrollCycle.setFont(new java.awt.Font("Cambria", 0, 10)); // NOI18N
        payrollCycle.setForeground(new java.awt.Color(255, 255, 255));
        payrollCycle.setText("Payroll Cycle");

        homeButton.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        homeButton.setForeground(new java.awt.Color(37, 61, 144));
        homeButton.setText("Home");
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });

        fullName.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        fullName.setForeground(new java.awt.Color(255, 255, 255));
        fullName.setText("fullName");

        designationWithEid.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        designationWithEid.setForeground(new java.awt.Color(255, 255, 255));
        designationWithEid.setText("designationWithEid");

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addComponent(fullName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 699, Short.MAX_VALUE)
                        .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(payPeriodLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(profilePanelLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(payrollCycle)
                                .addGap(18, 18, 18)))
                        .addGap(36, 36, 36))
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addComponent(designationWithEid)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(homeButton)
                        .addGap(46, 46, 46))))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fullName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(designationWithEid)
                .addGap(20, 20, 20))
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(payPeriodLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(payrollCycle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(homeButton)
                .addGap(0, 16, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(37, 61, 144));
        jLabel10.setText("Benefits");

        jLabel14.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(37, 61, 144));
        jLabel14.setText("Deductions");

        jLabel23.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(143, 143, 143));
        jLabel23.setText("Rice Subsidy:");

        riceAllowance.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        riceAllowance.setForeground(new java.awt.Color(143, 143, 143));
        riceAllowance.setText("0.00");

        jLabel24.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel24.setText("Amount");

        jLabel25.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(143, 143, 143));
        jLabel25.setText("Phone Allowance:");

        phoneAllowance.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        phoneAllowance.setForeground(new java.awt.Color(143, 143, 143));
        phoneAllowance.setText("0.00");

        jLabel26.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(143, 143, 143));
        jLabel26.setText("Clothing Allowance:");

        clothingAllowance.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        clothingAllowance.setForeground(new java.awt.Color(143, 143, 143));
        clothingAllowance.setText("0.00");

        jLabel28.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(143, 143, 143));
        jLabel28.setText("SSS Contribution:");

        sssContribution.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sssContribution.setForeground(new java.awt.Color(143, 143, 143));
        sssContribution.setText("0.00");

        jLabel29.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(143, 143, 143));
        jLabel29.setText("Philhealth:");

        philHealthContribution.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        philHealthContribution.setForeground(new java.awt.Color(143, 143, 143));
        philHealthContribution.setText("0.00");

        jLabel30.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(143, 143, 143));
        jLabel30.setText("Pag-Ibig:");

        pagIbigContribution.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        pagIbigContribution.setForeground(new java.awt.Color(143, 143, 143));
        pagIbigContribution.setText("0.00");

        jLabel31.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(143, 143, 143));
        jLabel31.setText("Withholding Tax:");

        withholdingTax.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        withholdingTax.setForeground(new java.awt.Color(143, 143, 143));
        withholdingTax.setText("0.00");

        jLabel38.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel38.setText("Amount");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel31)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel14)
                    .addComponent(jLabel26)
                    .addComponent(jLabel25)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(jLabel38)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sssContribution)
                            .addComponent(pagIbigContribution)
                            .addComponent(withholdingTax)
                            .addComponent(philHealthContribution))))
                .addGap(46, 46, 46))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(riceAllowance, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clothingAllowance, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(phoneAllowance, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(55, 55, 55))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel24))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(riceAllowance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneAllowance)
                    .addComponent(jLabel25))
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(clothingAllowance))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sssContribution)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(philHealthContribution)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pagIbigContribution)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(withholdingTax)
                    .addComponent(jLabel31))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(37, 61, 144));
        jLabel11.setText("Summary");

        jLabel18.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(143, 143, 143));
        jLabel18.setText("Amount");

        jLabel20.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(143, 143, 143));
        jLabel20.setText("Benefits:");

        jLabel21.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(143, 143, 143));
        jLabel21.setText("Deductions:");

        jLabel22.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel22.setText("Net Pay:");

        totalAllowances.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalAllowances.setForeground(new java.awt.Color(143, 143, 143));
        totalAllowances.setText("0.00");

        totalDeductions.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalDeductions.setForeground(new java.awt.Color(143, 143, 143));
        totalDeductions.setText("0.00");

        netPayAmount.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        netPayAmount.setText("0.00");

        jLabel37.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(143, 143, 143));
        jLabel37.setText("Gross Pay:");

        grossSalary.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        grossSalary.setForeground(new java.awt.Color(143, 143, 143));
        grossSalary.setText("0.00");

        jLabel39.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(143, 143, 143));
        jLabel39.setText("Incentives:");

        totalIncentives1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalIncentives1.setForeground(new java.awt.Color(143, 143, 143));
        totalIncentives1.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel21)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20)
                                .addComponent(jLabel37)
                                .addComponent(jLabel11)
                                .addComponent(jLabel39))
                            .addGap(5, 5, 5)))
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netPayAmount)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(grossSalary)
                                .addComponent(totalAllowances)
                                .addComponent(totalDeductions)
                                .addComponent(totalIncentives1)))))
                .addGap(67, 67, 67))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(grossSalary))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(totalAllowances))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalIncentives1)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(totalDeductions))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(netPayAmount))
                .addGap(97, 97, 97))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(37, 61, 144));
        jLabel13.setText("Earnings");

        jLabel32.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel32.setText("Amount");

        jLabel33.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(143, 143, 143));
        jLabel33.setText("Monthly Rate:");

        jLabel35.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(143, 143, 143));
        jLabel35.setText("Days Worked:");

        jLabel36.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(143, 143, 143));
        jLabel36.setText("Over Time:");

        overTimeHours.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        overTimeHours.setForeground(new java.awt.Color(143, 143, 143));
        overTimeHours.setText("0.00");

        numberOfDays.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        numberOfDays.setForeground(new java.awt.Color(143, 143, 143));
        numberOfDays.setText("26");

        grossSalary1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        grossSalary1.setForeground(new java.awt.Color(143, 143, 143));
        grossSalary1.setText("0.00");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                        .addComponent(jLabel32)
                        .addGap(82, 82, 82))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(numberOfDays)
                        .addGap(114, 114, 114))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel33))
                        .addGap(97, 97, 97)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(overTimeHours)
                            .addComponent(grossSalary1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel32))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(grossSalary1))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(numberOfDays))
                .addGap(34, 34, 34)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(overTimeHours))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addComponent(dashboardPane)
                        .addContainerGap(901, Short.MAX_VALUE))
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(profilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17))))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPane)
                .addGap(18, 18, 18)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(68, 68, 68))
        );

        javax.swing.GroupLayout NavigationPanelLayout = new javax.swing.GroupLayout(NavigationPanel);
        NavigationPanel.setLayout(NavigationPanelLayout);
        NavigationPanelLayout.setHorizontalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboardPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavigationPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addGap(443, 443, 443))
        );
        NavigationPanelLayout.setVerticalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavigationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavigationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(NavigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void homeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeButtonActionPerformed
        try {
        // Get the logged-in user from UserSession
        EmployeeDetails user = UserSession.getInstance().getLoggedInUser();
        
        if (user == null) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String designation = user.getDesignation();
        
        // Check if the designation contains "Rank and File"
        if (designation.contains("Rank and File")) {
            // Navigate to EmployeeDashboard using the stored user data
            new EmployeeDashboard(
                user.getFirstName(),
                user.getLastName(),
                user.getDesignation(),
                user.getEid(),
                user.getBasicSalary(),
                user.getTotalAllowances(),
                user.getTotalIncentives(), 
                user.getTotalDeductions(),
                user.getNetPay(),
                UserSession.getInstance().getUsername()
            ).setVisible(true);
        } else {
            // Navigate to AdminDashboard
            new AdminDashboard(
                UserSession.getInstance().getUsername()
            ).setVisible(true);
        }
        
        this.dispose(); // Close the current window
    } catch (Exception ex) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this,
            "Error: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_homeButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PaySlip.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PaySlip().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JLabel clothingAllowance;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JLabel fullName;
    private javax.swing.JLabel grossSalary;
    private javax.swing.JLabel grossSalary1;
    private javax.swing.JButton homeButton;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel netPayAmount;
    private javax.swing.JLabel numberOfDays;
    private javax.swing.JLabel overTimeHours;
    private javax.swing.JLabel pagIbigContribution;
    private javax.swing.JLabel payPeriodLabel;
    private javax.swing.JLabel payrollCycle;
    private javax.swing.JLabel philHealthContribution;
    private javax.swing.JLabel phoneAllowance;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JLabel riceAllowance;
    private javax.swing.JLabel sssContribution;
    private javax.swing.JLabel totalAllowances;
    private javax.swing.JLabel totalDeductions;
    private javax.swing.JLabel totalIncentives1;
    private javax.swing.JLabel withholdingTax;
    // End of variables declaration//GEN-END:variables
}
