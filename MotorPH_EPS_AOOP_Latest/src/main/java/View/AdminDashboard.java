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
import Repository.DataAccessObjects.EmployeeDataAccess;
import Model.DashboardData;
import Model.EmployeeDetails;
import Repository.DataAccessObjects.TableDataFetcher;
import ViewModel.RoleAuthenticator;
import ViewModel.AdminRole;
import ViewModel.DBQueries.ReportColumns;
import ViewModel.TimeManager;
import ViewModel.UserSession;
import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDashboard extends javax.swing.JFrame {
    // Core Components
    private RoleAuthenticator roleAuthenticator; 
    private Connection connection;
    private final String loggedInUsername;
    
    // Business Logic Components
    private EmployeeDetails employeeDetails;
    private DashboardData dashboardData;
    private EmployeeDataAccess employeeData;
            
    // Controller Components
    private final AdminRole adminRole;
    private final DefaultTableModel tableModel;
    private TableDataFetcher tableDataFetcher;
    private DefaultTableModel employeeTableModel;
    
    private TimeManager timeManager;
    
    
    /**
     * Primary constructor for AdminDashboard.
     * This constructor should be used after a successful login, passing the established connection and username.
     * @param username The username of the logged-in user.
     * @param connection The active database connection.
     */
    public AdminDashboard(String username) {
        initComponents();
        
        // Initialize core components
        this.loggedInUsername = username;
        initializeDashboard(username);
        this.employeeTableModel = (DefaultTableModel) employeeTable.getModel();
        
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Initialize AdminRole and tableModel
        adminRole = new AdminRole(); 
        tableModel = (DefaultTableModel) employeeTable.getModel();
        
        // Set column names for the employee table
        tableModel.setColumnIdentifiers(ReportColumns.getAllColumnNames());
        
        // Initialize and integrate controller
        employeeData = new EmployeeDataAccess();
        
         // Initialize time manager for real-time date and time display
        timeManager = new TimeManager(dateTodayLabel, timeNowLabel);
        timeManager.startClock();
        
        
        // Add ListSelectionListener to enable/disable buttons based on table selection
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) {
                boolean isRowSelected = employeeTable.getSelectedRow() != -1;
                employeePayslip.setEnabled(isRowSelected);
                employeeProfile.setEnabled(isRowSelected);
            }
        });
        
        // Initially disable buttons if no row is selected
        employeePayslip.setEnabled(false);
        employeeProfile.setEnabled(false);
        
        try {
            // Load employee data for the logged-in user and initialize dashboard UI
            if (username != null && !username.trim().isEmpty()) {
                initializeDashboard(username);
            }
            
            // Load all employee data into the table for admin view using controller
            adminRole.loadEmployeeData(tableModel);
            
        } catch (Exception ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                "Error during dashboard initialization: "+ ex.getMessage(),
                "Initialization Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
   
    
    /**
     * Secondary constructor for specific user data display (e.g., for testing or specific views).
     * This constructor will establish its own connection if not already provided.
     * @param firstName Employee's first name.
     * @param lastName Employee's last name.
     * @param designation Employee's designation.
     * @param eid Employee ID.
     * @param basicPay Employee's basic pay.
     * @param totalAllowances Employee's total allowances.
     * @param totalDeductions Employee's total deductions.
     * @param netPay Employee's net pay.
     * @param username The username of the logged-in user.
     */
    public AdminDashboard(
        String firstName,
        String lastName,
        String designation,
        int eid,
        float basicPay,
        float totalAllowances,
        float totalDeductions,
        float netPay,
        String username) throws SQLException {
        
        initComponents();
        
        // Initialize core components
        this.loggedInUsername = username;
        
        // Setup initial UI configuration
        setupInitialUI();
        
        // Initialize AdminRole and tableModel
        adminRole = new AdminRole();
        tableModel = (DefaultTableModel) employeeTable.getModel();
        this.employeeTableModel = (DefaultTableModel) employeeTable.getModel();
        
        // Set column names
        tableModel.setColumnIdentifiers(ReportColumns.getAllColumnNames());
        
        //Initialize employeeDataAccess here
        employeeData = new EmployeeDataAccess();
        
        
        // Setup table selection listener
        setupTableSelectionListener();
        
        // Update UI with user data specific to this constructor
        String fullNameText = firstName + " " + lastName;
        String designationWithEidText = designation + " -- " + eid;
        this.fullName.setText(fullNameText);
        this.designationWithEid.setText(designationWithEidText);
        
         // Load employee data using EmpDAO
        adminRole.loadEmployeeData(tableModel);
    }
    
    /**
     * Simplified constructor for basic initialization with employee details.
     * This constructor will establish its own connection if not already provided.
     * @param firstName Employee's first name.
     * @param lastName Employee's last name.
     * @param designation Employee's designation.
     * @param eid Employee ID.
     */
    public AdminDashboard(String firstName, String lastName, String designation, int eid) throws SQLException {
        initComponents();
        
        // Initialize core components
        this.loggedInUsername = null;
        
        // Setup initial UI configuration
        setupInitialUI();
        
        // Initialize AdminRole and tableModel
        adminRole = new AdminRole();
        tableModel = (DefaultTableModel) employeeTable.getModel();
        this.employeeTableModel = (DefaultTableModel) employeeTable.getModel();
        
        // Set column names
        tableModel.setColumnIdentifiers(ReportColumns.getAllColumnNames());
        
         //Initialize employeeDataAccess here
        employeeData = new EmployeeDataAccess();
        
        
        
        // Setup table selection listener
        setupTableSelectionListener();
        
        // Update UI with user data specific to this constructor
        String fullNameText = firstName + " " + lastName;
        String designationWithEidText = designation + " -- " + eid;
        this.fullName.setText(fullNameText);
        this.designationWithEid.setText(designationWithEidText);
        
         // Load employee data using EmpDAO
        adminRole.loadEmployeeData(tableModel);
    }
    
    /**
     * Default constructor for AdminDashboard.
     * This constructor should primarily be used for design-time or initial setup where no user is logged in yet.
     * It will attempt to establish a new database connection.
     */
    public AdminDashboard() throws SQLException {
        initComponents();
        
        // Initialize core components
        this.loggedInUsername = null;
        
        // Setup initial UI configuration
        setupInitialUI();
        
        // Initialize AdminRole and tableModel
        adminRole = new AdminRole();
        tableModel = (DefaultTableModel) employeeTable.getModel();
        this.employeeTableModel = (DefaultTableModel) employeeTable.getModel();
        
        // Set column names
        tableModel.setColumnIdentifiers(ReportColumns.getAllColumnNames());
        
         //Initialize employeeDataAccess here
        employeeData = new EmployeeDataAccess();
        
        
        
        // Setup table selection listener
        setupTableSelectionListener();
        
        // Load employee data using EmpDAO
        adminRole.loadEmployeeData(tableModel);
    }
    
    /**
     * Gets a valid database connection, creating one if necessary.
     * @return A valid database connection.
     */
    private Connection getValidConnection() {
        try {
            return DataSource.getInstance().getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                "Failed to establish database connection: " + ex.getMessage(),
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Sets up initial UI configuration.
     */
    private void setupInitialUI() {
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Initially disable buttons if no row is selected
        employeePayslip.setEnabled(false);
        employeeProfile.setEnabled(false);
    }
    
    /**
     * Sets up the table selection listener to enable/disable buttons.
     */
    private void setupTableSelectionListener() {
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                boolean isRowSelected = employeeTable.getSelectedRow() != -1;
                employeePayslip.setEnabled(isRowSelected);
                employeeProfile.setEnabled(isRowSelected);
            }
        });
    }
    
    /**
     * Initializes the dashboard with data for the logged-in user.
     * This method loads employee details and updates the UI.
     * @param username The username of the logged-in user.
     */
    private void initializeDashboard(String username) {
        try {
            // Load employee data for the specific user
            employeeDetails = new Repository.EmployeeRepository().getEmployeeByUsername(username);
            
            if (employeeDetails != null) {
                // Initialize dashboard components with employee data
                dashboardData = new DashboardData();
                dashboardData.processEmployeeData(employeeDetails);
                dashboardData.processAllowanceData(employeeDetails);
                
                System.out.println("Employee loaded: " + employeeDetails.getFirstName() + " " + 
                    employeeDetails.getLastName() + ", " + employeeDetails.getDesignation());
                System.out.println("Employee ID: " + employeeDetails.getEid());
                
                // Update UI with employee data
                updateUI();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Employee data not found for username: " + username, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                "Error initializing dashboard for user " + username + ": " + ex.getMessage(), 
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Updates the UI elements with the current employee's details.
     */
    private void updateUI() {
        if (employeeDetails != null) {
            fullName.setText(employeeDetails.getFirstName() + " " + employeeDetails.getLastName());
            designationWithEid.setText(employeeDetails.getDesignation() + " -- " + employeeDetails.getEid());
        } else {
            // Handle case where employeeDetails might be null
            fullName.setText("N/A");
            designationWithEid.setText("N/A");
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
        viewSettingsButton = new javax.swing.JLabel();
        dashboardPanel = new javax.swing.JPanel();
        dashboardPane = new javax.swing.JLabel();
        profilePanel = new javax.swing.JPanel();
        viewProfileButton = new javax.swing.JButton();
        fullName = new javax.swing.JLabel();
        designationWithEid = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        viewTimesheetButton1 = new javax.swing.JButton();
        leaveButton = new javax.swing.JButton();
        viewSettingsBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        employeeTable = new javax.swing.JTable();
        updateButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        sortButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        employeePayslip = new javax.swing.JButton();
        employeeProfile = new javax.swing.JButton();
        reactivateEmployeeButton = new javax.swing.JButton();
        dateTodayLabel = new javax.swing.JLabel();
        timeNowLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MotorPH Admin Dashboard");
        setMinimumSize(new java.awt.Dimension(1024, 720));

        NavigationPanel.setBackground(new java.awt.Color(255, 255, 255));
        NavigationPanel.setToolTipText("");

        dashboardLabel.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        dashboardLabel.setForeground(new java.awt.Color(0, 35, 102));
        dashboardLabel.setText("Dashboard");
        dashboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardLabelMouseClicked(evt);
            }
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

        viewSettingsButton.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        viewSettingsButton.setForeground(new java.awt.Color(51, 51, 51));
        viewSettingsButton.setText("Settings");
        viewSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewSettingsButtonMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewSettingsButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewSettingsButtonMouseExited(evt);
            }
        });

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(37, 61, 144));
        dashboardPane.setText("Dashboard");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        viewProfileButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewProfileButton.setForeground(new java.awt.Color(0, 35, 102));
        viewProfileButton.setText("View Profile");
        viewProfileButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewProfileButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewProfileButtonMouseExited(evt);
            }
        });
        viewProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewProfileButtonActionPerformed(evt);
            }
        });

        fullName.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        fullName.setForeground(new java.awt.Color(255, 255, 255));
        fullName.setText("fullName");

        designationWithEid.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        designationWithEid.setForeground(new java.awt.Color(255, 255, 255));
        designationWithEid.setText("designationWithEid");

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blankephoto.png"))); // NOI18N

        viewTimesheetButton1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewTimesheetButton1.setForeground(new java.awt.Color(0, 35, 102));
        viewTimesheetButton1.setText("Timesheet");
        viewTimesheetButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewTimesheetButton1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewTimesheetButton1MouseExited(evt);
            }
        });
        viewTimesheetButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTimesheetButton1ActionPerformed(evt);
            }
        });

        leaveButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        leaveButton.setForeground(new java.awt.Color(0, 35, 102));
        leaveButton.setText("Time-Off App");
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

        viewSettingsBtn.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewSettingsBtn.setForeground(new java.awt.Color(0, 35, 102));
        viewSettingsBtn.setText("Settings");
        viewSettingsBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                viewSettingsBtnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                viewSettingsBtnMouseExited(evt);
            }
        });
        viewSettingsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSettingsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(designationWithEid)
                    .addComponent(fullName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(leaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewTimesheetButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewProfileButton)
                .addGap(18, 18, 18)
                .addComponent(viewSettingsBtn)
                .addGap(58, 58, 58))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 5, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(fullName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designationWithEid)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(leaveButton)
                            .addComponent(viewTimesheetButton1)
                            .addComponent(viewProfileButton)
                            .addComponent(viewSettingsBtn))
                        .addGap(39, 39, 39))))
        );

        jLabel5.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 35, 102));
        jLabel5.setText("Quick Actions");

        addButton.setBackground(new java.awt.Color(94, 158, 217));
        addButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        addButton.setForeground(new java.awt.Color(255, 255, 255));
        addButton.setText("Add");
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButtonMouseExited(evt);
            }
        });
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        employeeTable.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        employeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "EID", "Full Name", "Birthday", "Address", "Phone_Number", "SSS_Num", "PhilHealth_Num", "TIN_Num", "Pag-Ibig_Num", "Status", "Designation", "Basic Salary"
            }
        ));
        employeeTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(employeeTable);

        updateButton.setBackground(new java.awt.Color(94, 158, 217));
        updateButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        updateButton.setForeground(new java.awt.Color(255, 255, 255));
        updateButton.setText("Update");
        updateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                updateButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButtonMouseExited(evt);
            }
        });
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        searchButton.setBackground(new java.awt.Color(94, 158, 217));
        searchButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setText("Search");
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButtonMouseExited(evt);
            }
        });
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        deleteButton.setBackground(new java.awt.Color(255, 107, 107));
        deleteButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("Delete");
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButtonMouseExited(evt);
            }
        });
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        sortButton.setBackground(new java.awt.Color(94, 158, 217));
        sortButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sortButton.setForeground(new java.awt.Color(255, 255, 255));
        sortButton.setText("Sort");
        sortButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sortButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sortButtonMouseExited(evt);
            }
        });
        sortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortButtonActionPerformed(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));
        jPanel10.setForeground(new java.awt.Color(255, 255, 255));
        jPanel10.setToolTipText("");

        jLabel55.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Office Hours: 8:30am–5:30pm, Monday through Saturday");

        jLabel56.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Office Address: 7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City");

        jLabel57.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073 ");

        jLabel58.setFont(new java.awt.Font("Candara", 0, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Email: corporate@motorph.com");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(286, 286, 286)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel55))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel57)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addGap(61, 61, 61)
                                        .addComponent(jLabel58)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        refreshButton.setBackground(new java.awt.Color(94, 158, 217));
        refreshButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        refreshButton.setForeground(new java.awt.Color(255, 255, 255));
        refreshButton.setText("Refresh");
        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshButtonMouseExited(evt);
            }
        });
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        employeePayslip.setBackground(new java.awt.Color(94, 158, 217));
        employeePayslip.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        employeePayslip.setForeground(new java.awt.Color(255, 255, 255));
        employeePayslip.setText("Employee Payslip");
        employeePayslip.setToolTipText("");
        employeePayslip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                employeePayslipMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                employeePayslipMouseExited(evt);
            }
        });
        employeePayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeePayslipActionPerformed(evt);
            }
        });

        employeeProfile.setBackground(new java.awt.Color(94, 158, 217));
        employeeProfile.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        employeeProfile.setForeground(new java.awt.Color(255, 255, 255));
        employeeProfile.setText("Employee Profile");
        employeeProfile.setToolTipText("");
        employeeProfile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                employeeProfileMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                employeeProfileMouseExited(evt);
            }
        });
        employeeProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeProfileActionPerformed(evt);
            }
        });

        reactivateEmployeeButton.setBackground(new java.awt.Color(94, 158, 217));
        reactivateEmployeeButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        reactivateEmployeeButton.setForeground(new java.awt.Color(255, 255, 255));
        reactivateEmployeeButton.setText("Former Employees");
        reactivateEmployeeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reactivateEmployeeButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reactivateEmployeeButtonMouseExited(evt);
            }
        });
        reactivateEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reactivateEmployeeButtonActionPerformed(evt);
            }
        });

        dateTodayLabel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        dateTodayLabel.setForeground(new java.awt.Color(0, 35, 102));
        dateTodayLabel.setText("Date Today");

        timeNowLabel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        timeNowLabel.setForeground(new java.awt.Color(0, 35, 102));
        timeNowLabel.setText("Time Now");

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(dashboardPane)
                                .addGap(584, 584, 584)
                                .addComponent(dateTodayLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(timeNowLabel)
                                .addGap(91, 91, 91))
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(addButton)
                                .addGap(18, 18, 18)
                                .addComponent(updateButton)
                                .addGap(18, 18, 18)
                                .addComponent(deleteButton)
                                .addGap(18, 18, 18)
                                .addComponent(searchButton)
                                .addGap(18, 18, 18)
                                .addComponent(sortButton)
                                .addGap(18, 18, 18)
                                .addComponent(refreshButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                                .addComponent(employeeProfile)
                                .addGap(18, 18, 18)
                                .addComponent(employeePayslip)
                                .addGap(18, 18, 18)
                                .addComponent(reactivateEmployeeButton)
                                .addGap(21, 21, 21))
                            .addComponent(jLabel5))))
                .addContainerGap())
            .addComponent(profilePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dateTodayLabel)
                        .addComponent(timeNowLabel))
                    .addComponent(dashboardPane))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(14, 14, 14)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(updateButton)
                    .addComponent(searchButton)
                    .addComponent(deleteButton)
                    .addComponent(sortButton)
                    .addComponent(refreshButton)
                    .addComponent(employeePayslip)
                    .addComponent(employeeProfile)
                    .addComponent(reactivateEmployeeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogo small.png"))); // NOI18N

        javax.swing.GroupLayout NavigationPanelLayout = new javax.swing.GroupLayout(NavigationPanel);
        NavigationPanel.setLayout(NavigationPanelLayout);
        NavigationPanelLayout.setHorizontalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel2)
                .addGap(163, 163, 163)
                .addComponent(dashboardLabel)
                .addGap(75, 75, 75)
                .addComponent(payrollLabel)
                .addGap(68, 68, 68)
                .addComponent(viewSettingsButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(dashboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        NavigationPanelLayout.setVerticalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dashboardLabel)
                            .addComponent(payrollLabel)
                            .addComponent(viewSettingsButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void dashboardLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dashboardLabelMouseClicked

    private void payrollLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseClicked
       try {
            if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "User session not found. Please log in again.", 
                    "Session Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Opens PayrollDashboard and pass the username
            new PayrollDashboard(this.loggedInUsername).setVisible(true);
            this.dispose();
        } catch (HeadlessException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, 
                "Error opening Payroll Dashboard: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_payrollLabelMouseClicked

    private void viewSettingsButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSettingsButtonMouseClicked
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
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewSettingsButtonMouseClicked

    private void viewSettingsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSettingsBtnActionPerformed
        try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opens ViewSettings and passes the username and connection
        new ViewSettings (this.loggedInUsername).setVisible(true);
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewSettingsBtnActionPerformed

    private void sortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortButtonActionPerformed
        adminRole.showSortDialog(this, tableModel);
    }//GEN-LAST:event_sortButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        adminRole.loadEmployeeData(tableModel);
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        adminRole.showAddDialog(this, tableModel);
        adminRole.loadEmployeeData(tableModel);
    }//GEN-LAST:event_addButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
        employeeData.showMessage("Please select an employee to update.", "Selection Required", false);
        return;
        }
        String eid = tableModel.getValueAt(selectedRow, 0).toString();
        adminRole.showUpdateDialog(this, eid, tableModel);
        adminRole.loadEmployeeData(tableModel);
    }//GEN-LAST:event_updateButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow == -1) {
                employeeData.showMessage("Please select an employee to delete.", "Selection Required", false);
                return;
            }

            // Get the EID from the selected row (uncommented and fixed)
            String eid = tableModel.getValueAt(selectedRow, 0).toString();

            // Call the delete method with the correct parameters (parent component and eid)
            adminRole.deleteEmployeeWithConfirmation(this, eid, tableModel);

            // Refresh the table after deletion
            // tableDataFetcher.refreshTable(); // This line is no longer needed as refresh is handled in AdminRole
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        adminRole.showSearchDialog(this, tableModel);
        // The table model is updated within showSearchDialog, so we just need to ensure the table refreshes.
        tableModel.fireTableDataChanged();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void viewProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewProfileButtonActionPerformed
        try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
            try {
                // Opens ViewProfileand passes the username
                new ViewProfile (this.loggedInUsername).setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening EditProfile: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);}
    }//GEN-LAST:event_viewProfileButtonActionPerformed

    private void employeePayslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeePayslipActionPerformed
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            employeeData.showMessage("Please select an employee to view.", "Selection Required", false);
            return;
        }
        String eid = tableModel.getValueAt(selectedRow, 0).toString();
        new EmployeePayslip(eid).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_employeePayslipActionPerformed

    private void employeeProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeProfileActionPerformed
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow == -1) {
            employeeData.showMessage("Please select an employee to view.", "Selection Required", false);
            return;
        }
        String eid = tableModel.getValueAt(selectedRow, 0).toString();

        // Get username from UserSession
        String username = UserSession.getInstance().getUsername();

        new EmployeeProfile(username, eid).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_employeeProfileActionPerformed

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
                    user.getUserName()
                ).setVisible(true);
            }
            this.dispose(); // Close the current window
        } catch (HeadlessException ex) {
            Logger.getLogger(ViewSettings.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }   catch (SQLException ex) {
            Logger.getLogger(ViewSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_leaveButtonActionPerformed

    private void viewTimesheetButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTimesheetButton1ActionPerformed
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
                // Navigate to EmployeeDashboard
                JOptionPane.showMessageDialog(this,
                    "You are not authorized to access this page.",
                    "Unauthorized access.",
                    JOptionPane.ERROR_MESSAGE);
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
                user.getUserName()
            ).setVisible(true);
        } else {
            // Navigate to ViewTimesheet
            new ViewTimesheet(
                UserSession.getInstance().getUsername()
            ).setVisible(true);
        }
            this.dispose(); // Close the current window
        } catch (HeadlessException ex) {
            Logger.getLogger(ViewSettings.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_viewTimesheetButton1ActionPerformed

    private void reactivateEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reactivateEmployeeButtonActionPerformed
      // Check if adminRole was initialized successfully before using it
        if (adminRole != null) {
            // 'this' refers to the AdminDashboard JFrame (the parent window)
            // 'employeeTableModel' is the model for your main employee table, which the dialog will refresh
            adminRole.showReactivateEmployeeDialog(this, employeeTableModel);
        } else {
            JOptionPane.showMessageDialog(
                this, 
                "Cannot perform action because the database connection is not available.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_reactivateEmployeeButtonActionPerformed

    private void leaveButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveButtonMouseEntered
        leaveButton.setBackground(new Color(0,35,102));
        leaveButton.setForeground(Color.white);
    }//GEN-LAST:event_leaveButtonMouseEntered

    private void leaveButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveButtonMouseExited
        leaveButton.setBackground(Color.white);
        leaveButton.setForeground(new Color(0,35,102));
    }//GEN-LAST:event_leaveButtonMouseExited

    private void addButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addButtonMouseEntered
        addButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_addButtonMouseEntered

    private void addButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addButtonMouseExited
        addButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_addButtonMouseExited

    private void updateButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateButtonMouseEntered
        updateButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_updateButtonMouseEntered

    private void updateButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateButtonMouseExited
        updateButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_updateButtonMouseExited

    private void deleteButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteButtonMouseEntered
        deleteButton.setBackground(new Color(220,38,38));
    }//GEN-LAST:event_deleteButtonMouseEntered

    private void deleteButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteButtonMouseExited
        deleteButton.setBackground(new Color(255,107,107));
    }//GEN-LAST:event_deleteButtonMouseExited

    private void searchButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchButtonMouseEntered
        searchButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_searchButtonMouseEntered

    private void searchButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchButtonMouseExited
        searchButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_searchButtonMouseExited

    private void sortButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sortButtonMouseEntered
        sortButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_sortButtonMouseEntered

    private void sortButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sortButtonMouseExited
        sortButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_sortButtonMouseExited

    private void refreshButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshButtonMouseEntered
        refreshButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_refreshButtonMouseEntered

    private void refreshButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshButtonMouseExited
        refreshButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_refreshButtonMouseExited

    private void dashboardLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseEntered
        
    }//GEN-LAST:event_dashboardLabelMouseEntered

    private void dashboardLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardLabelMouseExited
        
    }//GEN-LAST:event_dashboardLabelMouseExited

    private void payrollLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseEntered
        payrollLabel.setForeground(new Color(94,158,217));
    }//GEN-LAST:event_payrollLabelMouseEntered

    private void payrollLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollLabelMouseExited
        payrollLabel.setForeground(new Color(51,51,51));
    }//GEN-LAST:event_payrollLabelMouseExited

    private void viewSettingsButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSettingsButtonMouseEntered
       viewSettingsButton.setForeground(new Color(94,158,217));
    }//GEN-LAST:event_viewSettingsButtonMouseEntered

    private void viewSettingsButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSettingsButtonMouseExited
        viewSettingsButton.setForeground(new Color(51,51,51));
    }//GEN-LAST:event_viewSettingsButtonMouseExited

    private void viewTimesheetButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewTimesheetButton1MouseEntered
        viewTimesheetButton1.setBackground(new Color(0,35,102));
        viewTimesheetButton1.setForeground(Color.white);
    }//GEN-LAST:event_viewTimesheetButton1MouseEntered

    private void viewTimesheetButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewTimesheetButton1MouseExited
        viewTimesheetButton1.setBackground(Color.white);
        viewTimesheetButton1.setForeground(new Color(0,35,102));
    }//GEN-LAST:event_viewTimesheetButton1MouseExited

    private void viewProfileButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProfileButtonMouseEntered
        viewProfileButton.setBackground(new Color(0,35,102));
        viewProfileButton.setForeground(Color.white);
    }//GEN-LAST:event_viewProfileButtonMouseEntered

    private void viewProfileButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewProfileButtonMouseExited
        viewProfileButton.setBackground(Color.white);
        viewProfileButton.setForeground(new Color(0,35,102));
    }//GEN-LAST:event_viewProfileButtonMouseExited

    private void viewSettingsBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSettingsBtnMouseEntered
        viewSettingsBtn.setBackground(new Color(0,35,102));
        viewSettingsBtn.setForeground(Color.white);
    }//GEN-LAST:event_viewSettingsBtnMouseEntered

    private void viewSettingsBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewSettingsBtnMouseExited
        viewSettingsBtn.setBackground(Color.white);
        viewSettingsBtn.setForeground(new Color(0,35,102));
    }//GEN-LAST:event_viewSettingsBtnMouseExited

    private void employeeProfileMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeProfileMouseEntered
        employeeProfile.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_employeeProfileMouseEntered

    private void employeeProfileMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeProfileMouseExited
        employeeProfile.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_employeeProfileMouseExited

    private void employeePayslipMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeePayslipMouseEntered
        employeePayslip.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_employeePayslipMouseEntered

    private void employeePayslipMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeePayslipMouseExited
        employeePayslip.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_employeePayslipMouseExited

    private void reactivateEmployeeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reactivateEmployeeButtonMouseEntered
        reactivateEmployeeButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_reactivateEmployeeButtonMouseEntered

    private void reactivateEmployeeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reactivateEmployeeButtonMouseExited
        reactivateEmployeeButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_reactivateEmployeeButtonMouseExited

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
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AdminDashboard().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    // Optionally, show a message to the user
                    JOptionPane.showMessageDialog(null, "A critical database error occurred. The application will now close.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JButton addButton;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel dateTodayLabel;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JButton employeePayslip;
    private javax.swing.JButton employeeProfile;
    private javax.swing.JTable employeeTable;
    private javax.swing.JLabel fullName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton leaveButton;
    private javax.swing.JLabel payrollLabel;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JButton reactivateEmployeeButton;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JButton sortButton;
    private javax.swing.JLabel timeNowLabel;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton viewProfileButton;
    private javax.swing.JButton viewSettingsBtn;
    private javax.swing.JLabel viewSettingsButton;
    private javax.swing.JButton viewTimesheetButton1;
    // End of variables declaration//GEN-END:variables

}
