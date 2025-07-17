/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

/**
 *
 * @author dashcodes
 */

import Model.EmployeeDetails;
import Model.DashboardData;
import Repository.DataSource;
import ViewModel.DBQueries;
import ViewModel.RoleAuthenticator;
import ViewModel.UserSession;
import java.awt.HeadlessException;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewProfile extends javax.swing.JFrame {

        // Core Components
    private String loggedInUsername;
    private RoleAuthenticator roleAuthenticator;
    
    // Business Logic Components
    private EmployeeDetails employeeDetails;
    private DashboardData dashboardData;
    private DBQueries dbQueries;
    private Date birthDay; 
    

    public ViewProfile(String username) throws SQLException {
        initComponents();
        this.loggedInUsername = username;
        
        // Disables Fullscreen and always launch at the center of the screen
        setExtendedState(JFrame.NORMAL);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Load employee data and initialize dashboard
        initializeDashboard(username);
    }
    /**
     * Constructor that accepts basic user data
     * @param firstName Employee's first name
     * @param lastName Employee's last name
     * @param designation Employee's job title
     * @param eid Employee ID
     * @param phoneNumber Employee's Phone Number
     * @param birthday Employee's Birthday
     * @param status Employee's employment status
     * @param sss Employee's SSS #
     * @param philHealth Employee's PhilHealth #
     * @param tin Employee's TIN #
     * @param pagIbig Employee's PAG-IBIG #
     * @param connection Database connection
     */
    public ViewProfile(
            
         // Basic Details
            String firstName, 
            String lastName, 
            String designation, 
            int eid,
            String phoneNumber,
            Date birthday,
            String status,
            
    
         // Government IDs
            String sss,
            String philHealth,
            String tin,
            String pagIbig) 
    {
        initComponents();
        
        // Update UI with user data
        String fullName = firstName + " " + lastName;
        String designationWithEid = designation + " -- " + eid;
        
        // Set values to UI components
        
        /// Displays Employee Basic Details
        this.fullName.setText(fullName);
        this.designationWithEid.setText(designationWithEid);
        
        
        /// Displays Government IDs in the Benefits Section
        this.sssField.setText(sss);
        this.philHealthField.setText(philHealth);
        this.tinField.setText(tin);
        this.pagIbigField.setText(pagIbig);
        
        /// Displayed Info in the Details Section
        this.positionField.setText(designation);
        this.eidField.setText(String.valueOf(eid));
        this.statusField.setText(status);   
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
                
                System.out.println("Employee loaded: " + employeeDetails.getFirstName() + " " + 
                    employeeDetails.getLastName() + ", " + employeeDetails.getDesignation());
                System.out.println("Employee ID: " + employeeDetails.getEid());
                
                // Initializes dashboard components
                dashboardData = new DashboardData();
                dashboardData.processEmployeeData(employeeDetails);
   
                // Update UI with employee data
                updateUI();
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
            Logger.getLogger(ViewProfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Updates the interface with employee details from the business logic components
     */
    private void updateUI() {
        try {
            
            // Updates basic information
            /// Displays Employee Basic Details
            fullName.setText(employeeDetails.getFirstName() + " " + employeeDetails.getLastName());
            designationWithEid.setText(employeeDetails.getDesignation() + " -- " + employeeDetails.getEid());
            
            
            /// Updates Details in the Contact Information Section
            this.phoneNumberField.setText(employeeDetails.getPhoneNumber());
            this.emailAddressField.setText(employeeDetails.getUserName());
            this.addressField.setText(employeeDetails.getAddress());
            this.birthdayField.setText(new SimpleDateFormat ("yyyy/MM/dd").format(employeeDetails.getBirthday()));
        
            /// Updates Government IDs in the Benefits Section
            this.sssField.setText(employeeDetails.getSss());
            this.philHealthField.setText(employeeDetails.getPhilHealth());
            this.tinField.setText(employeeDetails.getTin());
            this.pagIbigField.setText(employeeDetails.getPagIbig());
        
        /// Displayed Info in the Details Section
            this.positionField.setText(employeeDetails.getDesignation());
            this.eidField.setText(String.valueOf(employeeDetails.getEid()));
            this.statusField.setText(employeeDetails.getStatus()); 
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error updating UI: " + ex.getMessage(), 
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * default constructor
     */
    public ViewProfile() {    
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
        homeButton = new javax.swing.JButton();
        fullName = new javax.swing.JLabel();
        designationWithEid = new javax.swing.JLabel();
        viewSettingsBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        sssLabel = new javax.swing.JLabel();
        philHealthLabel = new javax.swing.JLabel();
        pagibigLabel = new javax.swing.JLabel();
        tinLabel = new javax.swing.JLabel();
        sssField = new javax.swing.JLabel();
        philHealthField = new javax.swing.JLabel();
        pagIbigField = new javax.swing.JLabel();
        tinField = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        positionField = new javax.swing.JLabel();
        eidField = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        statusField = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        userNameLabel1 = new javax.swing.JLabel();
        phoneNumberField = new javax.swing.JLabel();
        emailAddressField = new javax.swing.JLabel();
        addressField = new javax.swing.JLabel();
        birthdayField = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        NavigationPanel.setBackground(new java.awt.Color(255, 255, 255));
        NavigationPanel.setToolTipText("");

        dashboardLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        dashboardLabel.setForeground(new java.awt.Color(51, 51, 51));
        dashboardLabel.setText("Dashboard");
        dashboardLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardLabelMouseClicked(evt);
            }
        });

        payrollLabel.setFont(new java.awt.Font("Cambria", 0, 18)); // NOI18N
        payrollLabel.setForeground(new java.awt.Color(51, 51, 51));
        payrollLabel.setText("Payroll");
        payrollLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payrollLabelMouseClicked(evt);
            }
        });

        settingsLabel.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        settingsLabel.setForeground(new java.awt.Color(0, 35, 102));
        settingsLabel.setText("Settings");

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(0, 35, 102));
        dashboardPane.setText("View Profile");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/blankephoto.png"))); // NOI18N

        homeButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
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

        viewSettingsBtn.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewSettingsBtn.setForeground(new java.awt.Color(0, 35, 102));
        viewSettingsBtn.setText("Settings");
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
                .addGap(45, 45, 45)
                .addComponent(jLabel1)
                .addGap(62, 62, 62)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullName)
                    .addComponent(designationWithEid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 558, Short.MAX_VALUE)
                .addComponent(homeButton)
                .addGap(18, 18, 18)
                .addComponent(viewSettingsBtn)
                .addGap(130, 130, 130))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(fullName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designationWithEid)
                        .addGap(77, 77, 77))))
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(homeButton)
                    .addComponent(viewSettingsBtn))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(37, 61, 144));
        jLabel10.setText("Benefits");

        sssLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sssLabel.setForeground(new java.awt.Color(143, 143, 143));
        sssLabel.setText("SSS Number:");

        philHealthLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        philHealthLabel.setForeground(new java.awt.Color(143, 143, 143));
        philHealthLabel.setText("PhilHealth Number:");

        pagibigLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        pagibigLabel.setForeground(new java.awt.Color(143, 143, 143));
        pagibigLabel.setText("PAG-IBIG Number:");

        tinLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tinLabel.setForeground(new java.awt.Color(143, 143, 143));
        tinLabel.setText("TIN Number:");

        sssField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sssField.setForeground(new java.awt.Color(143, 143, 143));
        sssField.setText("#");

        philHealthField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        philHealthField.setForeground(new java.awt.Color(143, 143, 143));
        philHealthField.setText("#");

        pagIbigField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        pagIbigField.setForeground(new java.awt.Color(143, 143, 143));
        pagIbigField.setText("#");

        tinField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tinField.setForeground(new java.awt.Color(143, 143, 143));
        tinField.setText("#");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sssLabel)
                    .addComponent(tinLabel)
                    .addComponent(pagibigLabel)
                    .addComponent(philHealthLabel)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sssField)
                    .addComponent(philHealthField)
                    .addComponent(pagIbigField)
                    .addComponent(tinField))
                .addGap(77, 77, 77))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel10)
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sssLabel)
                    .addComponent(sssField))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(philHealthLabel)
                    .addComponent(philHealthField))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pagibigLabel)
                    .addComponent(pagIbigField))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tinLabel)
                    .addComponent(tinField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(37, 61, 144));
        jLabel11.setText("MotorPH Details");

        jLabel18.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(143, 143, 143));
        jLabel18.setText("Job Title:");

        jLabel21.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(143, 143, 143));
        jLabel21.setText("Employee ID:");

        positionField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        positionField.setForeground(new java.awt.Color(143, 143, 143));
        positionField.setText("position");

        eidField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        eidField.setForeground(new java.awt.Color(143, 143, 143));
        eidField.setText("eid");

        statusLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        statusLabel.setForeground(new java.awt.Color(143, 143, 143));
        statusLabel.setText("Employment Status:");

        statusField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        statusField.setForeground(new java.awt.Color(143, 143, 143));
        statusField.setText("status");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel18)
                            .addComponent(statusLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(eidField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(positionField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statusField, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(168, 168, 168))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel11)
                .addGap(34, 34, 34)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(positionField))
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(eidField))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(statusField))
                .addContainerGap(102, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(37, 61, 144));
        jLabel13.setText("Contact Information");

        jLabel28.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(143, 143, 143));
        jLabel28.setText("Phone Number:");

        userNameLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        userNameLabel.setForeground(new java.awt.Color(143, 143, 143));
        userNameLabel.setText("Email:");

        jLabel29.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(143, 143, 143));
        jLabel29.setText("Complete Address:");

        userNameLabel1.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        userNameLabel1.setForeground(new java.awt.Color(143, 143, 143));
        userNameLabel1.setText("Birthday:");

        phoneNumberField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        phoneNumberField.setForeground(new java.awt.Color(143, 143, 143));
        phoneNumberField.setText("#");

        emailAddressField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        emailAddressField.setForeground(new java.awt.Color(143, 143, 143));
        emailAddressField.setText("#");

        addressField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        addressField.setForeground(new java.awt.Color(143, 143, 143));
        addressField.setText("FULL ADDRESS");

        birthdayField.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        birthdayField.setForeground(new java.awt.Color(143, 143, 143));
        birthdayField.setText("#");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addressField, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(userNameLabel1)
                        .addGap(119, 119, 119)
                        .addComponent(birthdayField))
                    .addComponent(jLabel29)
                    .addComponent(jLabel13)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(userNameLabel))
                        .addGap(85, 85, 85)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(phoneNumberField)
                            .addComponent(emailAddressField))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel13)
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(phoneNumberField))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userNameLabel)
                    .addComponent(emailAddressField))
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addGap(17, 17, 17)
                .addComponent(addressField)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userNameLabel1)
                    .addComponent(birthdayField))
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
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dashboardPane)
                    .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(80, Short.MAX_VALUE))
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPane)
                .addGap(18, 18, 18)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogo small.png"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(37, 61, 144));
        jLabel17.setText("----");

        javax.swing.GroupLayout NavigationPanelLayout = new javax.swing.GroupLayout(NavigationPanel);
        NavigationPanel.setLayout(NavigationPanelLayout);
        NavigationPanelLayout.setHorizontalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel2)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(NavigationPanelLayout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(dashboardLabel)
                        .addGap(75, 75, 75)
                        .addComponent(payrollLabel)
                        .addGap(78, 78, 78)
                        .addComponent(settingsLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NavigationPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(454, 454, 454))))
            .addComponent(dashboardPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        NavigationPanelLayout.setVerticalGroup(
            NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(NavigationPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(NavigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dashboardLabel)
                    .addComponent(payrollLabel)
                    .addComponent(settingsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setToolTipText("");

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
                .addGap(361, 361, 361))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
    } catch (HeadlessException ex) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this,
            "Error: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_homeButtonActionPerformed

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

    private void payrollLabelMouseClicked(java.awt.event.MouseEvent evt) {                                          
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
    }

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
            java.util.logging.Logger.getLogger(ViewProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewProfile.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewProfile().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel NavigationPanel;
    private javax.swing.JLabel addressField;
    private javax.swing.JLabel birthdayField;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JLabel eidField;
    private javax.swing.JLabel emailAddressField;
    private javax.swing.JLabel fullName;
    private javax.swing.JButton homeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel pagIbigField;
    private javax.swing.JLabel pagibigLabel;
    private javax.swing.JLabel payrollLabel;
    private javax.swing.JLabel philHealthField;
    private javax.swing.JLabel philHealthLabel;
    private javax.swing.JLabel phoneNumberField;
    private javax.swing.JLabel positionField;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JLabel settingsLabel;
    private javax.swing.JLabel sssField;
    private javax.swing.JLabel sssLabel;
    private javax.swing.JLabel statusField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel tinField;
    private javax.swing.JLabel tinLabel;
    private javax.swing.JLabel userNameLabel;
    private javax.swing.JLabel userNameLabel1;
    private javax.swing.JButton viewSettingsBtn;
    // End of variables declaration//GEN-END:variables
}
