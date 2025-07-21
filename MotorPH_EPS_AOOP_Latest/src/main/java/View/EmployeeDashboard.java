/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

/**
 *
 * @author dashcodes
 */

import Model.Allowances;
import Model.Deductions;
import Model.Incentives;
import Model.DashboardData;
import Repository.DataSource;
import Model.EmployeeDetails;
import ViewModel.RoleAuthenticator;
import ViewModel.SalaryCalculator;
import ViewModel.TimeManager;
import ViewModel.TimesheetController;
import ViewModel.UserSession;
import java.awt.Color;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EmployeeDashboard extends javax.swing.JFrame {
                
        // Core components
        private TimeManager timeManager;
        private RoleAuthenticator roleAuthenticator;
        private TimesheetController timesheetController;
        private String loggedInUsername;
        private String username;

        // Business logic components
        private EmployeeDetails employeeDetails;
        private DashboardData dashboardData;
        private SalaryCalculator salaryCalculator;
        
    // Number formatter for consistent currency display
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getInstance(Locale.US);
    
    static {
        CURRENCY_FORMATTER.setMinimumFractionDigits(2);
        CURRENCY_FORMATTER.setMaximumFractionDigits(2);
    }

        
        //Default constructor
    public EmployeeDashboard(String username) {
        initComponents();
    
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        setupProgressBarColors();

        // Store in UserSession
        UserSession.getInstance().setUsername(username);

        // Also keep local instance variables if needed
        this.loggedInUsername = username;

        // Initialize time manager for real-time date and time display
        timeManager = new TimeManager(dateTodayLabel, timeNowLabel);
        timeManager.startClock();

        // Initialize TimesheetController
        timesheetController = new TimesheetController();
    }

     /**
     * Constructor that accepts username to load user-specific data
     * @param firstName
     * @param lastName
     * @param designation
     * @param eid
     * @param basicSalary
     * @param totalAllowances
     * @param totalDeductions
     * @param netPay
     * @param username The username of the logged-in user
     */
    public EmployeeDashboard(
            String firstName, 
            String lastName, 
            String designation, 
            int eid, 
            float basicSalary, 
            float totalAllowances,
            float totalIncentives,
            float totalDeductions, 
            float netPay,
            String username) {
        initComponents();
        
        // Initialize time manager for real-time date and time display
        timeManager = new TimeManager(dateTodayLabel, timeNowLabel);
        timeManager.startClock();
        
        setupProgressBarColors();

        // Initialize TimesheetController
        timesheetController = new TimesheetController();
        
        // Stores the username
        this.loggedInUsername = username;
        UserSession.getInstance().setUsername(username);
        
        // Load employee data and initialize dashboard
        initializeDashboard(username);
        
        // Initialize TimesheetController
        timesheetController = new TimesheetController();
        
        // Update time-in/time-out buttons and labels
        updateButtonAndLabelStates();
    }
     
    /**
     * Constructor that accepts basic user data
     * @param firstName Employee's first name
     * @param lastName Employee's last name
     * @param designation Employee's job title
     * @param eid Employee ID
     */
    public EmployeeDashboard(
        String firstName,
        String lastName,
        String designation,
        int eid) 
    {
        initComponents();
        
        setupProgressBarColors();
        
        // Update UI with user data
        String fullNameLbl = firstName + " " + lastName;
        String designationWithEidLbl = designation + " -- " + eid;
        
        // Set basic employee information
        this.fullName.setText(fullNameLbl);
        this.designationWithEid.setText(designationWithEidLbl);
        
        // Store the username
        this.loggedInUsername = username;
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
     * @param overTime
     */
    public EmployeeDashboard(
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
        float netPay, 
        float overTime) 
    {
        initComponents();
        
        setupProgressBarColors();
        
        // Update UI with user data
        String fullNameLbl = firstName + " " + lastName;
        String designationWithEidLbl = designation + " -- " + eid;
        
        // Set basic employee information
        this.fullName.setText(fullNameLbl);
        this.designationWithEid.setText(designationWithEidLbl);
        
        // Update all UI elements with formatted values
        updateFinancialUI(
                
            // Overview values
            basicSalary,
            overTime,
            totalAllowances,
            totalIncentives,
            totalDeductions, 
            netPay
        );
    }


    public EmployeeDashboard(
            String firstName, 
            String lastName, 
            String designation, 
            int eid, 
            float basicSalary, 
            float totalAllowances, 
            float totalDeductions, 
            float netPay) {
        
        initComponents();
        
        setupProgressBarColors();
        
    }
    
    

    public EmployeeDashboard(
            String firstName,
            String lastName,
            String designation,
            int eid,
            float basicSalary,
            float totalAllowances,
            float totalDeductions,
            float netPay,
            String userName) {
        
        initComponents();
        
        setupProgressBarColors();
        
        this.loggedInUsername = userName;
        
        // Update UI with user data
        String fullNameLbl = firstName + " " + lastName;
        String designationWithEidLbl = designation + " -- " + eid;
        
        // Set basic employee information
        this.fullName.setText(fullNameLbl);
        this.designationWithEid.setText(designationWithEidLbl);
        
    }
    
    public EmployeeDashboard(){
        
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
     * @param pagIbigContribution Employee's PAG-IBIG contribution
     * @param withholdingTax Employee's withholding tax
     * @param grossPay Employee's gross pay
     * @param totalAllowances Employee's total allowances
     * @param totalDeductions Employee's total deductions
     * @param netPay Employee's net pay
     */
    private void updateFinancialUI(
            float basicSalary, float overTime, float totalAllowances, float totalIncentives, float totalDeductions, float netPay) 
    
    {
        /// Assigned jLabels + formatted values
        
        // Payslip Quick Overview
        this.basicSalaryLbl.setText(formatCurrency(salaryCalculator.getGrossPay()));
        this.overtimeHours.setText(String.valueOf(overTime));
        this.totalAllowancesLabel.setText(formatCurrency(totalAllowances));
        this.totalIncentivesLabel.setText(formatCurrency(totalIncentives));
        this.totalDeductionsLabel.setText(formatCurrency(totalDeductions));
        this.netPayLabel.setText(formatCurrency(netPay));
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
     * Initialize the dashboard with user data
     * 
     * @param username The username of the logged-in user
     */
    private void initializeDashboard(String username) {
        try {
            // Load employee data
            employeeDetails = new Repository.EmployeeRepository().getEmployeeByUsername(username);
            
            if (employeeDetails != null) {
                
                // Initializes salary calculator and performs all calculations
                salaryCalculator = new SalaryCalculator();
                salaryCalculator.calculateSalaryFromDB(employeeDetails, "01/01/2025", "01/31/2025");
                
                System.out.println("Employee loaded: " + employeeDetails.getFirstName() + " " + 
                    employeeDetails.getLastName() + ", " + employeeDetails.getDesignation());
                System.out.println("Employee ID: " + employeeDetails.getEid());
                
                
                dashboardData = new DashboardData();
                dashboardData.processEmployeeData(employeeDetails);
                
                // Update UI with employee data
                updateUI();

                // Update time-in/time-out buttons and labels
                updateButtonAndLabelStates();
                
                setupProgressBarColors();
            }
            else {
                JOptionPane.showMessageDialog(this, 
                    "Employee data not found for username: " + username, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error initializing dashboard: " + ex.getMessage(), 
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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
    
    private void setupProgressBarColors() {
        annualProgress.setBackground(new Color(5,150,105));
        annualProgress.setForeground(new Color(0,35,102));
        
        sickProgress.setBackground(new Color(94,158,217));
        sickProgress.setForeground(new Color(0,35,102));
        
        annualProgress.setBackground(new Color(5,150,105));
        annualProgress.setForeground(new Color(0,35,102));
    }
    
    
    private void handleTimeAction(String recordType, javax.swing.JButton button, javax.swing.JLabel label) {
        if (employeeDetails == null) {
            JOptionPane.showMessageDialog(this, "Employee data not loaded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

            if (!employeeDetails.getDesignation().contains("Rank and File")) {
                JOptionPane.showMessageDialog(this, "This feature is available only for Rank and File employees.", "Access Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

                try {
                    Time recordedTime = timesheetController.recordTime(employeeDetails.getEid(), recordType);
                    if (recordedTime != null) {
                        label.setText(recordedTime.toString());
                        button.setEnabled(false);

                        if (recordType.equals("Time In")) {
                            timeOutButton.setEnabled(true);
                        }

                        ViewSettings.showNotification(this, recordType + " recorded successfully.", ViewSettings.NotificationType.SUCCESS);
                    } else {
                        JOptionPane.showMessageDialog(this, "You have already recorded your " + recordType.toLowerCase() + " for today.", "Already Recorded", JOptionPane.WARNING_MESSAGE);
                    }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDashboard.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "An error occurred while recording time.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
     }

    private void updateButtonAndLabelStates() {
        // Reset buttons and labels to default state for new session
        timeInButton.setEnabled(true);
        timeOutButton.setEnabled(false);
        timeInLabel.setText("You have not clocked in yet.");
        timeOutLabel.setText("You have not clocked out yet.");

        if (employeeDetails == null) {
            return; // Employee data not yet loaded
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
            
            // Uses DashboardData to display data in the UI
            updateFinancialUI(
                employeeDetails.getBasicSalary(),
                employeeDetails.getOverTime(),
                salaryCalculator.getTotalAllowances(), 
                salaryCalculator.getTotalIncentives(),
                salaryCalculator.getTotalDeductions(),
                salaryCalculator.getNetPay()
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
        dashboardLabel = new javax.swing.JLabel();
        payrollLabel = new javax.swing.JLabel();
        settingsLabel = new javax.swing.JLabel();
        dashboardPanel = new javax.swing.JPanel();
        dashboardPane = new javax.swing.JLabel();
        profilePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fullName = new javax.swing.JLabel();
        designationWithEid = new javax.swing.JLabel();
        viewProfileButton = new javax.swing.JButton();
        leaveButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        viewPayslipButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        timeInLog = new javax.swing.JLabel();
        dateToday1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        logOutTime = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        hoursWorked = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        overtimeHours = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        totalHoursWorked = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        basicPayTitle = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        deductionsAmount = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        basicSalaryLbl = new javax.swing.JLabel();
        totalAllowancesLabel = new javax.swing.JLabel();
        totalDeductionsLabel = new javax.swing.JLabel();
        netPayLabel = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        totalIncentivesLabel = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        annualProgress = new javax.swing.JProgressBar();
        compensationProgress = new javax.swing.JProgressBar();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        sickProgress = new javax.swing.JProgressBar();
        annualLeaveRemaining = new javax.swing.JLabel();
        sickLeaveRemaining = new javax.swing.JLabel();
        compensationLeaveRemaining = new javax.swing.JLabel();
        dateTodayLabel = new javax.swing.JLabel();
        timeNowLabel = new javax.swing.JLabel();
        timeOutButton = new javax.swing.JButton();
        timeInButton = new javax.swing.JButton();
        timeInLabel = new javax.swing.JLabel();
        timeOutLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MotorPH Employee Dashboard");

        NavigationPanel.setBackground(new java.awt.Color(255, 255, 255));

        dashboardLabel.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        dashboardLabel.setForeground(new java.awt.Color(0, 35, 102));
        dashboardLabel.setText("Dashboard");
        dashboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dashboardLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dashboardLabelMouseExited(evt);
            }
        });

        payrollLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        payrollLabel.setForeground(new java.awt.Color(51, 51, 51));
        payrollLabel.setText("Payroll");
        payrollLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payrollLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                payrollLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                payrollLabelMouseExited(evt);
            }
        });

        settingsLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        settingsLabel.setForeground(new java.awt.Color(51, 51, 51));
        settingsLabel.setText("Settings");
        settingsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                settingsLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                settingsLabelMouseExited(evt);
            }
        });

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(37, 61, 144));
        dashboardPane.setText("Dashboard");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blankephoto.png"))); // NOI18N

        fullName.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        fullName.setForeground(new java.awt.Color(255, 255, 255));
        fullName.setText("fullName");

        designationWithEid.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        designationWithEid.setForeground(new java.awt.Color(255, 255, 255));
        designationWithEid.setText("designationWithEid");

        viewProfileButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewProfileButton.setForeground(new java.awt.Color(0, 35, 102));
        viewProfileButton.setText("View Profile");
        viewProfileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewProfileButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewProfileButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewProfileButtonMouseExited(evt);
            }
        });

        leaveButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        leaveButton.setForeground(new java.awt.Color(0, 35, 102));
        leaveButton.setText("Apply Leave");
        leaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                leaveButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                leaveButtonMouseExited(evt);
            }
        });
        leaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(72, 72, 72)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullName)
                    .addComponent(designationWithEid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
                .addComponent(leaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewProfileButton)
                .addGap(106, 106, 106))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(profilePanelLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(fullName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(designationWithEid)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(viewProfileButton)
                            .addComponent(leaveButton))))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 35, 102));
        jLabel5.setText("Quick Actions");

        viewPayslipButton.setBackground(new java.awt.Color(94, 158, 217));
        viewPayslipButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewPayslipButton.setForeground(new java.awt.Color(255, 255, 255));
        viewPayslipButton.setText("View Payslip");
        viewPayslipButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewPayslipButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewPayslipButtonMouseExited(evt);
            }
        });
        viewPayslipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPayslipButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(37, 61, 144));
        jLabel10.setText("Attendance");

        timeInLog.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        timeInLog.setForeground(new java.awt.Color(143, 143, 143));
        timeInLog.setText("08:00 AM");

        dateToday1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        dateToday1.setForeground(new java.awt.Color(143, 143, 143));
        dateToday1.setText("Log In Time:");

        jLabel3.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(143, 143, 143));
        jLabel3.setText("Log Out Time:");

        logOutTime.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        logOutTime.setForeground(new java.awt.Color(143, 143, 143));
        logOutTime.setText("0.00");

        jLabel4.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(143, 143, 143));
        jLabel4.setText("Hours Worked:");

        hoursWorked.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        hoursWorked.setForeground(new java.awt.Color(143, 143, 143));
        hoursWorked.setText("0.00");

        jLabel14.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(143, 143, 143));
        jLabel14.setText("Over Time:");

        overtimeHours.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        overtimeHours.setForeground(new java.awt.Color(143, 143, 143));
        overtimeHours.setText("0.00");

        jLabel23.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(143, 143, 143));
        jLabel23.setText("Total Hours:");

        totalHoursWorked.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        totalHoursWorked.setForeground(new java.awt.Color(143, 143, 143));
        totalHoursWorked.setText("0.00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(dateToday1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logOutTime)
                            .addComponent(timeInLog)
                            .addComponent(hoursWorked)
                            .addComponent(overtimeHours)
                            .addComponent(totalHoursWorked))
                        .addGap(84, 84, 84))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel14)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel10)
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateToday1)
                    .addComponent(timeInLog))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(logOutTime))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(hoursWorked))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(overtimeHours))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(totalHoursWorked))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(37, 61, 144));
        jLabel11.setText("Pay slip Quickview");

        jLabel18.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel18.setText("Amount");

        basicPayTitle.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        basicPayTitle.setForeground(new java.awt.Color(143, 143, 143));
        basicPayTitle.setText("Basic Pay:");

        jLabel20.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(143, 143, 143));
        jLabel20.setText("Allowances:");

        deductionsAmount.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        deductionsAmount.setForeground(new java.awt.Color(143, 143, 143));
        deductionsAmount.setText("Deductions:");

        jLabel22.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(143, 143, 143));
        jLabel22.setText("Net Pay:");

        basicSalaryLbl.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        basicSalaryLbl.setForeground(new java.awt.Color(143, 143, 143));
        basicSalaryLbl.setText("0.00");

        totalAllowancesLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalAllowancesLabel.setForeground(new java.awt.Color(143, 143, 143));
        totalAllowancesLabel.setText("0.00");

        totalDeductionsLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalDeductionsLabel.setForeground(new java.awt.Color(143, 143, 143));
        totalDeductionsLabel.setText("0.00");

        netPayLabel.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        netPayLabel.setForeground(new java.awt.Color(143, 143, 143));
        netPayLabel.setText("0.00");

        jLabel21.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(143, 143, 143));
        jLabel21.setText("Incentives:");

        totalIncentivesLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalIncentivesLabel.setForeground(new java.awt.Color(143, 143, 143));
        totalIncentivesLabel.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(103, 254, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(basicPayTitle)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(totalAllowancesLabel)
                                            .addComponent(basicSalaryLbl)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(netPayLabel)
                                                    .addComponent(totalDeductionsLabel)
                                                    .addComponent(totalIncentivesLabel))))))
                                .addGap(92, 92, 92))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(deductionsAmount))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addGap(1, 1, 1)
                .addComponent(jLabel18)
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(basicPayTitle)
                    .addComponent(basicSalaryLbl))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(totalAllowancesLabel))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(totalIncentivesLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deductionsAmount)
                    .addComponent(totalDeductionsLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(netPayLabel))
                .addGap(82, 82, 82))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(37, 61, 144));
        jLabel13.setText("Leave Credits");

        annualProgress.setBackground(new java.awt.Color(242, 242, 242));
        annualProgress.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        annualProgress.setString("100%");
        annualProgress.setStringPainted(true);

        compensationProgress.setBackground(new java.awt.Color(242, 242, 242));
        compensationProgress.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        compensationProgress.setString("100%");
        compensationProgress.setStringPainted(true);

        jLabel12.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(10, 4, 4));
        jLabel12.setText("Annual Leave");

        jLabel15.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(10, 4, 4));
        jLabel15.setText("Sick Leave");

        jLabel16.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(10, 4, 4));
        jLabel16.setText("Compensation Leave");

        sickProgress.setBackground(new java.awt.Color(242, 242, 242));
        sickProgress.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sickProgress.setString("80%");
        sickProgress.setStringPainted(true);

        annualLeaveRemaining.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        annualLeaveRemaining.setForeground(new java.awt.Color(10, 4, 4));
        annualLeaveRemaining.setText("10");

        sickLeaveRemaining.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sickLeaveRemaining.setForeground(new java.awt.Color(10, 4, 4));
        sickLeaveRemaining.setText("8");

        compensationLeaveRemaining.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        compensationLeaveRemaining.setForeground(new java.awt.Color(10, 4, 4));
        compensationLeaveRemaining.setText("10");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sickProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sickLeaveRemaining))
                            .addComponent(compensationProgress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(compensationLeaveRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(annualLeaveRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(annualProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel13)
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(annualLeaveRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(annualProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sickLeaveRemaining, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sickProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(compensationLeaveRemaining, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(compensationProgress, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );

        dateTodayLabel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        dateTodayLabel.setForeground(new java.awt.Color(0, 35, 102));
        dateTodayLabel.setText("Date Today");

        timeNowLabel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        timeNowLabel.setForeground(new java.awt.Color(0, 35, 102));
        timeNowLabel.setText("Time Now");

        timeOutButton.setBackground(new java.awt.Color(94, 158, 217));
        timeOutButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        timeOutButton.setForeground(new java.awt.Color(255, 255, 255));
        timeOutButton.setText("Time Out");
        timeOutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                timeOutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                timeOutButtonMouseExited(evt);
            }
        });
        timeOutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeOutButtonActionPerformed(evt);
            }
        });

        timeInButton.setBackground(new java.awt.Color(94, 158, 217));
        timeInButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        timeInButton.setForeground(new java.awt.Color(255, 255, 255));
        timeInButton.setText("Time In");
        timeInButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                timeInButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                timeInButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                timeInButtonMouseExited(evt);
            }
        });
        timeInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeInButtonActionPerformed(evt);
            }
        });

        timeInLabel.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        timeInLabel.setForeground(new java.awt.Color(0, 35, 102));
        timeInLabel.setText("You have not clocked in yet.");

        timeOutLabel.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        timeOutLabel.setForeground(new java.awt.Color(0, 35, 102));
        timeOutLabel.setText("You have not clocked out yet.");

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5))))
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(viewPayslipButton)))
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(timeOutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(timeInButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(timeInLabel)
                            .addComponent(timeOutLabel))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addComponent(dashboardPane)
                        .addGap(653, 653, 653)
                        .addComponent(dateTodayLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(timeNowLabel)
                        .addGap(66, 66, 66))
                    .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dashboardPane)
                    .addComponent(dateTodayLabel)
                    .addComponent(timeNowLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(11, 11, 11)
                        .addComponent(viewPayslipButton))
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeInButton)
                            .addComponent(timeInLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeOutButton)
                            .addComponent(timeOutLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogo small.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Office Hours: 8:30am–5:30pm, Monday through Saturday");

        jLabel7.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Office Address: 7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City");

        jLabel8.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073 ");

        jLabel9.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Email: corporate@motorph.com");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(322, 322, 322)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addComponent(jLabel9)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout NavigationPanelLayout = new javax.swing.GroupLayout(NavigationPanel);
        NavigationPanel.setLayout(NavigationPanelLayout);
        NavigationPanelLayout.setHorizontalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel2)
                .addGap(180, 180, 180)
                .addComponent(dashboardLabel)
                .addGap(75, 75, 75)
                .addComponent(payrollLabel)
                .addGap(78, 78, 78)
                .addComponent(settingsLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        NavigationPanelLayout.setVerticalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dashboardLabel)
                            .addComponent(payrollLabel)
                            .addComponent(settingsLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavigationPanelLayout.createSequentialGroup()
                        .addContainerGap(10, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavigationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void viewPayslipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPayslipButtonActionPerformed
            if (employeeDetails == null) {
                JOptionPane.showMessageDialog(this, 
                    "Employee data not loaded. Please log in again.", 
                    "Session Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String loggedInUserDesignation = employeeDetails.getDesignation();
            String loggedInUserEid = String.valueOf(employeeDetails.getEid());

            if (loggedInUserDesignation != null && loggedInUserDesignation.contains("Rank and File")) {
                new EmployeePayslip(loggedInUserEid).setVisible(true);
            
            this.dispose();
        }
    }//GEN-LAST:event_viewPayslipButtonActionPerformed

    private void payrollLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseClicked
         try {
            if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
            }

            // Opens payrolldashboard and passes the username
            new PayrollDashboard (this.loggedInUsername).setVisible(true);
            this.dispose();
        } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_payrollLabelMouseClicked

    private void viewProfileButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProfileButtonMouseClicked
        try {
            if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
            // Opens Employee and passes the username
            new ViewProfile (this.loggedInUsername).setVisible(true);
            this.dispose();
        } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_viewProfileButtonMouseClicked

    private void settingsLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLabelMouseClicked
    try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opens ViewSettings and passes the username
        new ViewSettings (this.loggedInUsername).setVisible(true);
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);} catch (SQLException ex) {
                Logger.getLogger(EmployeeDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
    }//GEN-LAST:event_settingsLabelMouseClicked

    private void leaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaveButtonActionPerformed
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
                    // Navigate to Leave Application
                    new LeaveApplication (
                        user.getFirstName(),
                        user.getLastName(),
                        user.getDesignation(),
                        user.getEid(),
                        user.getUserName()
                        ).setVisible(true);
                } else {
                    // FIXED: Pass all necessary user details to LeaveDashboard
                    new LeaveDashboard(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getDesignation(),
                        user.getEid(),
                        user.getUserName()  // Include username if needed
                        ).setVisible(true);
                }
                this.dispose(); // Close the current window
            } catch (HeadlessException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }   catch (SQLException ex) {
                Logger.getLogger(EmployeeDashboard.class.getName());
        }
    }//GEN-LAST:event_leaveButtonActionPerformed

    private void timeOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeOutButtonActionPerformed
        handleTimeAction("Time Out", timeOutButton, timeOutLabel);
    }//GEN-LAST:event_timeOutButtonActionPerformed

    private void timeInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeInButtonActionPerformed
        handleTimeAction("Time In", timeInButton, timeInLabel); 
    }//GEN-LAST:event_timeInButtonActionPerformed

    private void payrollLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseEntered
        payrollLabel.setForeground(new Color(94,158,217));
    }//GEN-LAST:event_payrollLabelMouseEntered

    private void payrollLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseExited
        payrollLabel.setForeground(new Color(51,51,51));
    }//GEN-LAST:event_payrollLabelMouseExited

    private void settingsLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLabelMouseEntered
        settingsLabel.setForeground(new Color(94,158,217));
    }//GEN-LAST:event_settingsLabelMouseEntered

    private void settingsLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_settingsLabelMouseExited
        settingsLabel.setForeground(new Color(51,51,51));
    }//GEN-LAST:event_settingsLabelMouseExited

    private void dashboardLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseEntered
        dashboardLabel.setForeground(new Color(94,158,217));
    }//GEN-LAST:event_dashboardLabelMouseEntered

    private void dashboardLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseExited
        dashboardLabel.setForeground(new Color(51,51,51));
    }//GEN-LAST:event_dashboardLabelMouseExited

    private void leaveButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveButtonMouseEntered
        leaveButton.setBackground(new Color(0,35,102));
        leaveButton.setForeground(Color.white);
    }//GEN-LAST:event_leaveButtonMouseEntered

    private void leaveButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveButtonMouseExited
        leaveButton.setBackground(Color.white);
        leaveButton.setForeground(new Color(0,35,102));
    }//GEN-LAST:event_leaveButtonMouseExited

    private void viewProfileButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProfileButtonMouseEntered
        viewProfileButton.setBackground(new Color(0,35,102));
        viewProfileButton.setForeground(Color.white);
    }//GEN-LAST:event_viewProfileButtonMouseEntered

    private void timeInButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeInButtonMouseEntered
        timeInButton.setBackground(new Color(5,150,105));
    }//GEN-LAST:event_timeInButtonMouseEntered

    private void timeInButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeInButtonMouseExited
        timeInButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_timeInButtonMouseExited

    private void timeOutButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeOutButtonMouseEntered
        timeOutButton.setBackground(new Color(255,107,107));
    }//GEN-LAST:event_timeOutButtonMouseEntered

    private void timeOutButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeOutButtonMouseExited
        timeOutButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_timeOutButtonMouseExited

    private void viewPayslipButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPayslipButtonMouseEntered
        viewPayslipButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_viewPayslipButtonMouseEntered

    private void viewPayslipButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewPayslipButtonMouseExited
        viewPayslipButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_viewPayslipButtonMouseExited

    private void timeInButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeInButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_timeInButtonMouseClicked

    private void viewProfileButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProfileButtonMouseExited
        viewProfileButton.setBackground(Color.white);
        viewProfileButton.setForeground(new Color(0,35,102));
    }//GEN-LAST:event_viewProfileButtonMouseExited
    

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
            @Override
            public void run() {
                new EmployeeDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JLabel annualLeaveRemaining;
    private javax.swing.JProgressBar annualProgress;
    private javax.swing.JLabel basicPayTitle;
    private javax.swing.JLabel basicSalaryLbl;
    private javax.swing.JLabel compensationLeaveRemaining;
    private javax.swing.JProgressBar compensationProgress;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel dateToday1;
    private javax.swing.JLabel dateTodayLabel;
    private javax.swing.JLabel deductionsAmount;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JLabel fullName;
    private javax.swing.JLabel hoursWorked;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JButton leaveButton;
    private javax.swing.JLabel logOutTime;
    private javax.swing.JLabel netPayLabel;
    private javax.swing.JLabel overtimeHours;
    private javax.swing.JLabel payrollLabel;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JLabel settingsLabel;
    private javax.swing.JLabel sickLeaveRemaining;
    private javax.swing.JProgressBar sickProgress;
    private javax.swing.JButton timeInButton;
    private javax.swing.JLabel timeInLabel;
    private javax.swing.JLabel timeInLog;
    private javax.swing.JLabel timeNowLabel;
    private javax.swing.JButton timeOutButton;
    private javax.swing.JLabel timeOutLabel;
    private javax.swing.JLabel totalAllowancesLabel;
    private javax.swing.JLabel totalDeductionsLabel;
    private javax.swing.JLabel totalHoursWorked;
    private javax.swing.JLabel totalIncentivesLabel;
    private javax.swing.JButton viewPayslipButton;
    private javax.swing.JButton viewProfileButton;
    // End of variables declaration//GEN-END:variables
}
