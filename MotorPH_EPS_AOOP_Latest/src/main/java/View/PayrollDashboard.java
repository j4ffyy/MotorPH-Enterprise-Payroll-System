/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;


/**
 *
 * @author dashcodes and jaf 
 */
import Repository.DataSource;
import Model.Allowances;
import View.PaySlip;
import Model.Deductions;
import Model.Incentives;
import Model.DashboardData;
import Model.EmployeeDetails;
import ViewModel.DBQueries;
import ViewModel.RoleAuthenticator;
import ViewModel.SalaryCalculator;
import ViewModel.UserSession;
import ViewModel.Reports.EmployeeReportGenerator;
import ViewModel.Reports.PayrollReportsGenerator;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import Model.PayslipData;
import Repository.EmployeeRepository;
import Repository.PayslipRepository;
import java.util.List;


    public class PayrollDashboard extends javax.swing.JFrame {
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
    private DBQueries dbQueries;
    private PaySlip paySlip;
    
    // Number formatter for consistent currency display
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getInstance(Locale.US);

    static {
        // Set the logging level for JasperReports to SEVERE to suppress warnings
        Logger.getLogger("net.sf.jasperreports").setLevel(Level.SEVERE);
    }
    
    static {
        CURRENCY_FORMATTER.setMinimumFractionDigits(2);
        CURRENCY_FORMATTER.setMaximumFractionDigits(2);
    }
    
    public PayrollDashboard() {
        initComponents();
        this.dbQueries = new DBQueries();
    }

     /**
     * Constructor that accepts username to load user-specific data
     * @param username The username of the logged-in user
     */
    public PayrollDashboard(String username) {
        initComponents();
        
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Initialize role authenticator
        this.roleAuthenticator = new RoleAuthenticator();
        

        this.dbQueries = new DBQueries();
        initializeDashboard(username);
  
        // Stores the username
        this.loggedInUsername = username;
        
        
    }
     
    /**
     * Constructor that accepts basic user data
     * @param firstName Employee's first name
     * @param lastName Employee's last name
     * @param designation Employee's job title
     * @param eid Employee ID
     * @param connection Database connection
     */
    public PayrollDashboard(
        String firstName,
        String lastName,
        String designation,
        int eid) 
    {
        initComponents();
        
        this.dbQueries = new DBQueries();
        
        // Update UI with user data
        String fullName = firstName + " " + lastName;
        String designationWithEid = designation + " -- " + eid;
        
        // Set these values to UI components
        this.firstName.setText(fullName);
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
    public PayrollDashboard(
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
        
        
        this.dbQueries = new DBQueries();
        
        // Update UI with user data
        String fullNameLbl = firstName + " " + lastName;
        String designationWithEidLbl = designation + " -- " + eid;
        
        // Set basic employee information
        this.firstName.setText(fullNameLbl);
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
        float totalAllowances, 
        float totalIncentives,
        float totalDeductions,  // Added parameter
        float netPay) {

            /// Assigned jLabels + formatted values

            // Gross Pay Section
            this.basicSalary.setText(formatCurrency(salaryCalculator.getGrossPay()));
            this.overTimePayAmount1.setText(formatCurrency(salaryCalculator.getOverTimePay()));
            this.holidayPayAmount1.setText(formatCurrency(salaryCalculator.getHolidayPay()));
            this.performancePayAmount1.setText(formatCurrency(salaryCalculator.getPerformanceBonus()));

            // Allowances Section
            this.riceAllowance.setText(formatCurrency(salaryCalculator.getRiceSubsidy()));
            this.phoneAllowance.setText(formatCurrency(salaryCalculator.getPhoneAllowance()));
            this.clothingAllowance.setText(formatCurrency(salaryCalculator.getClothingAllowance()));

            // Deductions Section
            this.sssLabel.setText(formatCurrency(salaryCalculator.getSssContribution()));
            this.philHealthLabel.setText(formatCurrency(salaryCalculator.getPhilHealthContribution()));
            this.pagIbigLabel.setText(formatCurrency(salaryCalculator.getPagIbigContribution()));
            this.taxLabel.setText(formatCurrency(salaryCalculator.getWithholdingTax()));

            // Payroll Summary
            this.grossPay.setText(formatCurrency(salaryCalculator.getGrossPay()));
            this.totalAllowances.setText(formatCurrency(totalAllowances));
            this.totalIncentives1.setText(formatCurrency(totalIncentives));
            this.totalDeductions.setText(formatCurrency(totalDeductions));
            this.netPayAmount.setText(formatCurrency(netPay));
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
                // Check user's designation and restrict print button access
                if (employeeDetails.getDesignation() != null && employeeDetails.getDesignation().contains("Rank and File")) {
                    printButton.setVisible(false);
                    printButton.setEnabled(false);
                } else {
                    printButton.setVisible(true);
                    printButton.setEnabled(true);
                }
                
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
            	deductions.setSssContribution(employeeDetails.getSssContribution());
            	deductions.setPhilHealthContribution(employeeDetails.getPhilhealthContribution());
            	deductions.setPagIbigContribution(employeeDetails.getPagibigContribution());
            	deductions.setWithholdingTax(employeeDetails.getWithholdingTax());
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
                
                
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Employee data not found for username: " + username, 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    
                }
                // Update UI with employee data
                updateUI();
            } catch (HeadlessException | SQLException ex) {
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
            firstName.setText(employeeDetails.getFirstName() + " " + employeeDetails.getLastName());
            designationWithEid.setText(employeeDetails.getDesignation() + " -- " + employeeDetails.getEid());

            // Calculate values (ensure these are float variables, not JLabels)
            float basicSalary = employeeDetails.getBasicSalary();
            float overTimePay = salaryCalculator.getIncentive("overtimepay"); 
            float holidayPay = salaryCalculator.getIncentive("holidaypay");  
            float performanceBonus = salaryCalculator.getIncentive("performancebonus"); 

            float riceAllowance = allowances.getRiceAllowance();
            float phoneAllowance = allowances.getPhoneAllowance();
            float clothingAllowance = allowances.getClothingAllowance();

            float sssContribution = salaryCalculator.getSssContribution();
            float philHealthContribution = salaryCalculator.getPhilHealthContribution();
            float pagIbigContribution = salaryCalculator.getPagIbigContribution();
            float withholdingTax = salaryCalculator.getWithholdingTax();

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
    private void showPayslipDialog() throws ParseException {
        // Create input panel
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Employee ID:"));
        JTextField eidField = new JTextField(String.valueOf(employeeDetails.getEid()));
        eidField.setEditable(false);
        panel.add(eidField);

        panel.add(new JLabel("Period Start (MM-dd-yyyy:"));
        JTextField startField = new JTextField();
        panel.add(startField);

        panel.add(new JLabel("Period End (MM-dd-yyyy):"));
        JTextField endField = new JTextField();
        panel.add(endField);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, panel, "Generate Payslip", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Date startDate = parseDate(startField.getText());
            Date endDate = parseDate(endField.getText());
            // Generate report using classpath resource
            try {                        
                PayslipRepository payslipRepo = new PayslipRepository();
                List<PayslipData> payslipData = payslipRepo.getEmployeePayslipData(employeeDetails.getEid(), startDate, endDate);
                JRDataSource dataSource = new JRBeanCollectionDataSource(payslipData);

                generateReport("EmpPayslipMotorPH.jrxml", 
                        Map.of(
                                "EID", employeeDetails.getEid(),
                                "Period_Start", startDate,
                                "Period_End", endDate
                        ),
                        dataSource
                        );
                } catch (SQLException ex) {
                    Logger.getLogger(PayrollDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error fetching payslip data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
   
    private void generateReport(String reportName, Map<String, Object> params, JRDataSource dataSource) {
        try {
            // Show target file in Terminal
            System.out.println("Attempting to load report: /resources/JasperReports/MotorphReports/" + reportName); 
            
            // Load JRXML from classpath
            InputStream reportStream = getClass().getResourceAsStream("/JasperReports/MotorphReports/" + reportName);
            if (reportStream == null) {
                throw new FileNotFoundException("Report file not found in resources: " + reportName);
            }

            // Compile and fill report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Load the logo image and add it to parameters
            InputStream logoStream = getClass().getResourceAsStream("/MotorPHLogobig.png");
            if (logoStream == null) {
                throw new FileNotFoundException("Logo image not found in resources: MotorPHLogobig.png");
            }
            params.put("LOGO_IMAGE", logoStream);

            JasperPrint print = JasperFillManager.fillReport(
                jasperReport, 
                params, 
                dataSource
            );

            // Save to desktop
            String desktopPath = System.getProperty("user.home") + "/Desktop";
            java.io.File desktopDir = new java.io.File(desktopPath);
            if (!desktopDir.exists()) {
                desktopDir.mkdirs();
            }
            String fileName = "Report_" + System.currentTimeMillis() + ".pdf";
            JasperExportManager.exportReportToPdfFile(print, desktopPath + "/" + fileName);

            JOptionPane.showMessageDialog(this, "Report saved to:\n" + desktopPath + "/" + fileName);
        } catch (JRException | FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Report generation failed: " + e.getMessage());
        }
    }
    
    private Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.parse(dateStr);
    }
    
    private void showEmployeeReportDialog() throws SQLException {
    // Step 1: Get EID
    String eidStr = JOptionPane.showInputDialog(this, "Enter Employee ID:");
    try {
        int targetEid = Integer.parseInt(eidStr);
        
        // Validate employee exists
            EmployeeDetails targetEmployee = new Repository.EmployeeRepository().getEmployeeByEid(String.valueOf(targetEid));
            if (targetEmployee == null) {
                JOptionPane.showMessageDialog(this, "Employee not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Step 2: Show details and get dates
            JPanel panel = new JPanel(new GridLayout(4, 2));
            panel.add(new JLabel("Employee ID:"));
            panel.add(new JLabel(String.valueOf(targetEid)));
            panel.add(new JLabel("Name:"));
            panel.add(new JLabel(targetEmployee.getFirstName() + " " + targetEmployee.getLastName()));
            panel.add(new JLabel("Period Start (yyyy/MM/dd):"));
            JTextField startField = new JTextField();
            panel.add(startField);
            panel.add(new JLabel("Period End (yyyy/MM/dd):"));
            JTextField endField = new JTextField();
            panel.add(endField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Generate Employee Report", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    Date startDate = parseDate(startField.getText());
                    Date endDate = parseDate(endField.getText());

                    // Generate report
                    Map<String, Object> params = new HashMap<>();
                    params.put("EID", targetEid);
                    params.put("Period_Start", startDate);
                    params.put("Period_End", endDate);

                    DBQueries dbQueries = new DBQueries();
                    PayslipRepository payslipRepo = new PayslipRepository();

                    List<PayslipData> payslipData = payslipRepo.getEmployeePayslipData(targetEid, startDate, endDate);
                    JRDataSource dataSource = new JRBeanCollectionDataSource(payslipData);

                    generateReport("EmpPayslipMotorPH.jrxml", params, dataSource);
                    JOptionPane.showMessageDialog(this, "Employee Report generated successfully!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid EID format", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        NavigationPanel = new javax.swing.JPanel();
        dashboardLabel = new javax.swing.JLabel();
        payrollLabel = new javax.swing.JLabel();
        settingsLabel = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        dashboardPanel = new javax.swing.JPanel();
        dashboardPane = new javax.swing.JLabel();
        profilePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        editProfileButton = new javax.swing.JButton();
        firstName = new javax.swing.JLabel();
        designationWithEid = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        homeButton = new javax.swing.JButton();
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
        sssLabel = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        philHealthLabel = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        pagIbigLabel = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        taxLabel = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
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
        grossPay = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        totalIncentives1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        basicSalary = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        overTimePayAmount1 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        holidayPayAmount1 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        performancePayAmount1 = new javax.swing.JLabel();
        payrollCycle = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        printButton = new javax.swing.JButton();
        viewPayslipButton = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(326, 326, 326))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        NavigationPanel.setBackground(new java.awt.Color(255, 255, 255));

        dashboardLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        dashboardLabel.setForeground(new java.awt.Color(51, 51, 51));
        dashboardLabel.setText("Dashboard");
        dashboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardLabelMouseClicked(evt);
            }
        });

        payrollLabel.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        payrollLabel.setForeground(new java.awt.Color(51, 51, 51));
        payrollLabel.setText("Payroll");

        settingsLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        settingsLabel.setForeground(new java.awt.Color(51, 51, 51));
        settingsLabel.setText("Settings");
        settingsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                settingsLabelMouseClicked(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogo small.png"))); // NOI18N

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(37, 61, 144));
        dashboardPane.setText("Payroll");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blankephoto.png"))); // NOI18N

        editProfileButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        editProfileButton.setForeground(java.awt.Color.gray);
        editProfileButton.setText("View Profile");
        editProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editProfileButtonActionPerformed(evt);
            }
        });

        firstName.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        firstName.setForeground(new java.awt.Color(255, 255, 255));
        firstName.setText("fullName");

        designationWithEid.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        designationWithEid.setForeground(new java.awt.Color(255, 255, 255));
        designationWithEid.setText("designationWithEid");

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1)
                .addGap(55, 55, 55)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(firstName)
                    .addComponent(designationWithEid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editProfileButton)
                .addGap(83, 83, 83))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(firstName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designationWithEid))
                    .addGroup(profilePanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(editProfileButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        firstName.getAccessibleContext().setAccessibleDescription("");

        jLabel5.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 35, 102));
        jLabel5.setText("Quick Actions");

        homeButton.setBackground(new java.awt.Color(0, 35, 102));
        homeButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        homeButton.setForeground(new java.awt.Color(255, 255, 255));
        homeButton.setText("Home");
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(37, 61, 144));
        jLabel10.setText("Allowances");

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

        sssLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sssLabel.setForeground(new java.awt.Color(143, 143, 143));
        sssLabel.setText("0.00");

        jLabel29.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(143, 143, 143));
        jLabel29.setText("Philhealth:");

        philHealthLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        philHealthLabel.setForeground(new java.awt.Color(143, 143, 143));
        philHealthLabel.setText("0.00");

        jLabel30.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(143, 143, 143));
        jLabel30.setText("Pag-Ibig:");

        pagIbigLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        pagIbigLabel.setForeground(new java.awt.Color(143, 143, 143));
        pagIbigLabel.setText("0.00");

        jLabel31.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(143, 143, 143));
        jLabel31.setText("Withholding Tax:");

        taxLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        taxLabel.setForeground(new java.awt.Color(143, 143, 143));
        taxLabel.setText("0.00");

        jLabel27.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel27.setText("Amount");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26)
                            .addComponent(jLabel14)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(taxLabel)
                                .addComponent(riceAllowance)
                                .addComponent(clothingAllowance)
                                .addComponent(phoneAllowance)
                                .addComponent(sssLabel)
                                .addComponent(philHealthLabel)
                                .addComponent(pagIbigLabel))
                            .addComponent(jLabel27))
                        .addGap(69, 69, 69))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel26)
                        .addGap(21, 21, 21)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel27))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel28)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(riceAllowance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(phoneAllowance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clothingAllowance)
                        .addGap(54, 54, 54)
                        .addComponent(sssLabel)
                        .addGap(13, 13, 13)
                        .addComponent(philHealthLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addComponent(pagIbigLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(taxLabel)
                        .addContainerGap())))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(37, 61, 144));
        jLabel11.setText("Summary");

        jLabel18.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel18.setText("Amount");

        jLabel20.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(143, 143, 143));
        jLabel20.setText("Allowances:");

        jLabel21.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(143, 143, 143));
        jLabel21.setText("Deductions:");

        jLabel22.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel22.setText("Net Pay:");

        totalAllowances.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalAllowances.setForeground(new java.awt.Color(143, 143, 143));
        totalAllowances.setText("0.00");

        totalDeductions.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalDeductions.setForeground(new java.awt.Color(143, 143, 143));
        totalDeductions.setText("0.00");

        netPayAmount.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        netPayAmount.setText("0.00");

        jLabel37.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(143, 143, 143));
        jLabel37.setText("Gross Pay:");

        grossPay.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        grossPay.setForeground(new java.awt.Color(143, 143, 143));
        grossPay.setText("0.00");

        jLabel38.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(143, 143, 143));
        jLabel38.setText("Incentives:");

        totalIncentives1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        totalIncentives1.setForeground(new java.awt.Color(143, 143, 143));
        totalIncentives1.setText("0.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel20)
                    .addComponent(jLabel38)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel11)
                        .addComponent(jLabel37))
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netPayAmount)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(totalAllowances)
                                .addComponent(grossPay)
                                .addComponent(totalIncentives1)
                                .addComponent(totalDeductions)))
                        .addGap(56, 56, 56))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(grossPay)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalAllowances)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalIncentives1)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(totalDeductions))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(netPayAmount))
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(37, 61, 144));
        jLabel13.setText("Gross Pay");

        jLabel32.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel32.setText("Amount");

        jLabel33.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(143, 143, 143));
        jLabel33.setText("Basic Pay:");

        basicSalary.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        basicSalary.setForeground(new java.awt.Color(143, 143, 143));
        basicSalary.setText("0.00");

        jLabel34.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(143, 143, 143));
        jLabel34.setText("Over time Pay:");

        overTimePayAmount1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        overTimePayAmount1.setForeground(new java.awt.Color(143, 143, 143));
        overTimePayAmount1.setText("0.00");

        jLabel35.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(143, 143, 143));
        jLabel35.setText("Holiday Pay:");

        holidayPayAmount1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        holidayPayAmount1.setForeground(new java.awt.Color(143, 143, 143));
        holidayPayAmount1.setText("0.00");

        jLabel36.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(143, 143, 143));
        jLabel36.setText("Performance Bonus:");

        performancePayAmount1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        performancePayAmount1.setForeground(new java.awt.Color(143, 143, 143));
        performancePayAmount1.setText("0.00");

        payrollCycle.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        payrollCycle.setForeground(new java.awt.Color(143, 143, 143));
        payrollCycle.setText("Payroll Cycle");

        jLabel15.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(37, 61, 144));
        jLabel15.setText("Incentives");

        jLabel39.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel39.setText("Amount");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(payrollCycle)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(performancePayAmount1))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(overTimePayAmount1))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(holidayPayAmount1)))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel32))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(157, 157, 157)
                        .addComponent(basicSalary)
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39)))
                .addGap(52, 52, 52))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(basicSalary)
                    .addComponent(jLabel33))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel39))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(overTimePayAmount1)
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(holidayPayAmount1)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(performancePayAmount1))
                .addGap(17, 17, 17)
                .addComponent(payrollCycle)
                .addGap(18, 18, 18))
        );

        printButton.setBackground(new java.awt.Color(0, 35, 102));
        printButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        printButton.setForeground(new java.awt.Color(255, 255, 255));
        printButton.setText("Print Report");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        viewPayslipButton.setBackground(new java.awt.Color(0, 35, 102));
        viewPayslipButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewPayslipButton.setForeground(new java.awt.Color(255, 255, 255));
        viewPayslipButton.setText("View Payslip");
        viewPayslipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPayslipButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dashboardPane)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                                        .addComponent(homeButton)
                                        .addGap(624, 624, 624)
                                        .addComponent(viewPayslipButton)
                                        .addGap(29, 29, 29)
                                        .addComponent(printButton))
                                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addGap(85, 85, 85))
            .addComponent(profilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(dashboardPane)
                .addGap(18, 18, 18)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(11, 11, 11)
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(homeButton)
                            .addComponent(printButton)
                            .addComponent(viewPayslipButton))
                        .addGap(43, 43, 43)
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel19.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(37, 61, 144));
        jLabel19.setText("----");

        javax.swing.GroupLayout NavigationPanelLayout = new javax.swing.GroupLayout(NavigationPanel);
        NavigationPanel.setLayout(NavigationPanelLayout);
        NavigationPanelLayout.setHorizontalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel17)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(186, 186, 186)
                        .addComponent(dashboardLabel)
                        .addGap(75, 75, 75)
                        .addComponent(payrollLabel)
                        .addGap(76, 76, 76)
                        .addComponent(settingsLabel))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(365, 365, 365)
                        .addComponent(jLabel19)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(dashboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        NavigationPanelLayout.setVerticalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavigationPanelLayout.createSequentialGroup()
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel17))
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dashboardLabel)
                            .addComponent(payrollLabel)
                            .addComponent(settingsLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(NavigationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(NavigationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
            // For other designations, show report options
            String[] options = {"Employee Payslip"};
            int choice = JOptionPane.showOptionDialog(this, 
                "Which report would you like to generate?", 
                "Generate Report", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);

            switch (choice) {
                case 0: {
                    try {
                        //Existing employee payslip report logic
                        showEmployeeReportDialog();
                    } catch (SQLException ex) {
                        Logger.getLogger(PayrollDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    break;
            }

    }//GEN-LAST:event_printButtonActionPerformed

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
            JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
            Logger.getLogger(PayrollDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_settingsLabelMouseClicked

    private void editProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editProfileButtonActionPerformed
        try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opens PaySlip and passes the username
        new ViewProfile (this.loggedInUsername).setVisible(true);
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);} catch (SQLException ex) {
            Logger.getLogger(PayrollDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_editProfileButtonActionPerformed

    private void dashboardLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseClicked
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
    }//GEN-LAST:event_dashboardLabelMouseClicked

    private void viewPayslipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPayslipButtonActionPerformed
      Connection conn;
          try {
              conn = DataSource.getInstance().getConnection();
           
            if (employeeDetails == null) {
                  JOptionPane.showMessageDialog(this,
                      "Employee data not loaded. Cannot generate report.",
                      "Error",
                      JOptionPane.ERROR_MESSAGE);
                  return;
            }

                String loggedInUserDesignation = employeeDetails.getDesignation();
                String loggedInUserEid = String.valueOf(employeeDetails.getEid());

                // If logged-in user is 'Rank and File', directly open EmployeePayslip
                if (loggedInUserDesignation != null && loggedInUserDesignation.contains("Rank and File")) {
                    new EmployeePayslip(loggedInUserEid).setVisible(true);
                    this.dispose();
            } else {
                new PaySlip (loggedInUserEid, this.loggedInUsername, conn).setVisible(true);
                this.dispose();
                }
            } catch (SQLException ex) {
                  Logger.getLogger(PayrollDashboard.class.getName()).log(Level.SEVERE, null, ex);
          }                
    }//GEN-LAST:event_viewPayslipButtonActionPerformed

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
            java.util.logging.Logger.getLogger(PayrollDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PayrollDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JLabel basicSalary;
    private javax.swing.JLabel clothingAllowance;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JButton editProfileButton;
    private javax.swing.JLabel firstName;
    private javax.swing.JLabel grossPay;
    private javax.swing.JLabel holidayPayAmount1;
    private javax.swing.JButton homeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel netPayAmount;
    private javax.swing.JLabel overTimePayAmount1;
    private javax.swing.JLabel pagIbigLabel;
    private javax.swing.JLabel payrollCycle;
    private javax.swing.JLabel payrollLabel;
    private javax.swing.JLabel performancePayAmount1;
    private javax.swing.JLabel philHealthLabel;
    private javax.swing.JLabel phoneAllowance;
    private javax.swing.JButton printButton;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JLabel riceAllowance;
    private javax.swing.JLabel settingsLabel;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JLabel taxLabel;
    private javax.swing.JLabel totalAllowances;
    private javax.swing.JLabel totalDeductions;
    private javax.swing.JLabel totalIncentives1;
    private javax.swing.JButton viewPayslipButton;
    // End of variables declaration//GEN-END:variables

}
