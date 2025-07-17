/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

/**
 *
 * @author dashcodes
 */

import Model.DashboardData;
import Model.EmployeeDetails;
import ViewModel.AdminRole;
import ViewModel.DBQueries;
import ViewModel.LeaveController;
import ViewModel.RoleAuthenticator;
import Repository.DataSource;
import ViewModel.UserSession;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class LeaveDashboard extends javax.swing.JFrame {
    private LeaveController leaveController;
    private RoleAuthenticator roleAuthenticator;
    private String loggedInUsername;
    
    // Business Logic Components
    private EmployeeDetails employeeDetails;
    private DashboardData dashboardData;
    private AdminRole adminRole;
    
    private String firstName;
    private String lastName;
    private int eid;
    private String designation;
    private String username;
    
    
    public LeaveDashboard(String username) throws SQLException {
        initComponents();
        
        // Initialize AdminRole and tableModel
        leaveController = new LeaveController();
        this.loggedInUsername = username;
        
        // Load employee details using EmployeeRepository
        this.employeeDetails = new Repository.EmployeeRepository().getEmployeeByUsername(username);
        
        if (this.employeeDetails != null) {
            this.leaveController.setLoggedInUser(username, this.employeeDetails.getEid());
        } else {
            // Handle case where employee details are not found
            JOptionPane.showMessageDialog(this, "Employee details not found for username: " + username, "Error", JOptionPane.ERROR_MESSAGE);
            // Optionally, dispose of the window or redirect to login
            this.dispose();
            return;
        }
        
        // Loads the Leave table
        loadLeaveData();
        
        // Update UI
        updateUI();
    }
    
    public LeaveDashboard(){
        initComponents();
    }

    public LeaveDashboard(String firstName, String lastName, String designation, int eid, String userName) throws SQLException {
        initComponents();  
        
        this.loggedInUsername = userName;
        this.eid = eid;
        this.lastName = lastName;
        this.firstName = firstName;
        this.designation = designation;

        // Setup employeeDetails using EmployeeRepository
        this.employeeDetails = new Repository.EmployeeRepository().getEmployeeById(eid);
    
        // Now that employeeDetails is initialized, update the UI
        updateUI();
        
        this.leaveController = new LeaveController();
        loadLeaveData();
    }

    
    private void loadLeaveData() {
        // Populates leave table
        leavesTable.setModel(leaveController.getAllLeavesTableModel());
    }
    
    // Update the interface with specific details
    private void updateUI() {
        // Update basic information
        fullName.setText(employeeDetails.getFirstName() + " " + employeeDetails.getLastName());
        designationWithEid.setText(employeeDetails.getDesignation() + " -- " + employeeDetails.getEid());
    }
    
    private void navigateBasedOnRole(String username) {
     try {
         if (username == null || username.isEmpty()) {
             JOptionPane.showMessageDialog(this,
                 "User session not found. Please log in again.",
                 "Session Error",
                 JOptionPane.ERROR_MESSAGE);
             return;
         }

         // Initialize roleAuthenticator if it's null
         if (this.roleAuthenticator == null) {
             this.roleAuthenticator = new RoleAuthenticator();
         }

         // Instead of trying to authenticate again, just get the user details directly
         String designation = getUserDesignationFromDB(username);

         if (designation == null) {
             JOptionPane.showMessageDialog(this,
                 "User designation not found",
                 "Error",
                 JOptionPane.ERROR_MESSAGE);
             return;
         }

         if (designation.contains("Rank and File")) {
             new EmployeeDashboard(username).setVisible(true);
         } else {
             new AdminDashboard(username).setVisible(true);
         }

         this.dispose();
     } catch (HeadlessException ex) {
         Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
         JOptionPane.showMessageDialog(this,
             "Navigation error: " + ex.getMessage(),
             "Error",
             JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to get user designation directly from the database
        private String getUserDesignationFromDB(String username) {
        String designation = null;
        try (Connection conn = DataSource.getInstance().getConnection()) {
            DBQueries queries = new DBQueries();
            PreparedStatement pstmt = conn.prepareStatement(queries.getUserDesignation);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                designation = rs.getString("designation");
            }

            rs.close();
            pstmt.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return designation;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        leavesTable = new javax.swing.JTable();
        updateButton = new javax.swing.JButton();
        sortButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        viewSettingsButton.setFont(new java.awt.Font("Cambria", 1, 18)); // NOI18N
        viewSettingsButton.setForeground(new java.awt.Color(51, 51, 51));
        viewSettingsButton.setText("Settings");
        viewSettingsButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewSettingsButtonMouseClicked(evt);
            }
        });

        dashboardPanel.setBackground(new java.awt.Color(245, 245, 245));

        dashboardPane.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        dashboardPane.setForeground(new java.awt.Color(37, 61, 144));
        dashboardPane.setText("Leave Dashboard");

        profilePanel.setBackground(new java.awt.Color(0, 35, 102));

        viewProfileButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        viewProfileButton.setText("View Profile");
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

        javax.swing.GroupLayout profilePanelLayout = new javax.swing.GroupLayout(profilePanel);
        profilePanel.setLayout(profilePanelLayout);
        profilePanelLayout.setHorizontalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fullName)
                    .addComponent(designationWithEid))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 472, Short.MAX_VALUE)
                .addComponent(viewProfileButton)
                .addGap(81, 81, 81))
        );
        profilePanelLayout.setVerticalGroup(
            profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profilePanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(profilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(viewProfileButton)
                        .addGap(39, 39, 39))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profilePanelLayout.createSequentialGroup()
                        .addComponent(fullName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(designationWithEid)
                        .addGap(28, 28, 28))))
        );

        jLabel5.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 35, 102));
        jLabel5.setText("Options");

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
                .addGap(310, 310, 310)
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
                .addContainerGap(250, Short.MAX_VALUE))
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

        refreshButton.setBackground(new java.awt.Color(0, 35, 102));
        refreshButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        refreshButton.setForeground(new java.awt.Color(255, 255, 255));
        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        leavesTable.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        leavesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "EID", "Leave ID", "Date Filed", "Date From", "Date To", "Reason", "Status"
            }
        ));
        jScrollPane1.setViewportView(leavesTable);

        updateButton.setBackground(new java.awt.Color(0, 35, 102));
        updateButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        updateButton.setForeground(new java.awt.Color(255, 255, 255));
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        sortButton.setBackground(new java.awt.Color(0, 35, 102));
        sortButton.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        sortButton.setForeground(new java.awt.Color(255, 255, 255));
        sortButton.setText("Sort");
        sortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(profilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dashboardPanelLayout.createSequentialGroup()
                        .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dashboardPane)
                            .addComponent(jLabel5)
                            .addGroup(dashboardPanelLayout.createSequentialGroup()
                                .addComponent(refreshButton)
                                .addGap(18, 18, 18)
                                .addComponent(updateButton)
                                .addGap(18, 18, 18)
                                .addComponent(sortButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 929, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(dashboardPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(profilePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addGroup(dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshButton)
                    .addComponent(updateButton)
                    .addComponent(sortButton))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogo small.png"))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(37, 61, 144));
        jLabel17.setText("----");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(dashboardLabel)
                        .addGap(75, 75, 75)
                        .addComponent(payrollLabel)
                        .addGap(68, 68, 68)
                        .addComponent(viewSettingsButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(311, 311, 311))))
            .addComponent(dashboardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dashboardLabel)
                            .addComponent(payrollLabel)
                            .addComponent(viewSettingsButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dashboardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            JOptionPane.showMessageDialog(this,
                "Error opening PayrollDashboard: " + ex.getMessage(),
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

    private void viewProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewProfileButtonActionPerformed
        try {
            if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "User session not found. Please log in again.",
                    "Session Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Opens EmployeeDashboard and passes the username
            new ViewProfile (this.loggedInUsername).setVisible(true);
            this.dispose();
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this,
                "Error opening EditProfile: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);} catch (SQLException ex) {
            Logger.getLogger(LeaveDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewProfileButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        loadLeaveData();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
    int selectedRow = leavesTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, 
            "Please select a leave application to update.", 
            "Selection Required", 
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String leaveId = leavesTable.getValueAt(selectedRow, 0).toString();
    String currentStatus = leavesTable.getValueAt(selectedRow, 6).toString();
    
    // Creates a status selection dialog
    String[] statusOptions = {"Approved", "Pending", "Rejected"};
    String newStatus = (String) JOptionPane.showInputDialog(
        this,
        "Select new status for leave " + leaveId,
        "Update Leave Status",
        JOptionPane.QUESTION_MESSAGE,
        null,
        statusOptions,
        currentStatus);
    
    // If user cancels or closes the dialog, newStatus will be null
    if (newStatus != null && !newStatus.equals(currentStatus)) {
        System.out.println("Attempting to update Leave ID: " + leaveId + " to Status: " + newStatus);
        boolean success = leaveController.updateLeaveStatus(leaveId, newStatus);
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Leave status updated successfully.", 
                "Status Updated", 
                JOptionPane.INFORMATION_MESSAGE);
            // Refresh the table to show the updated status
            loadLeaveData();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to update leave status.", 
                "Update Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    }//GEN-LAST:event_updateButtonActionPerformed

    private void sortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortButtonActionPerformed
         // Creates a column selection dialog
    String[] columnOptions = {"EID", "Leave_ID", "Reason", "Status"};
    String selectedColumn = (String) JOptionPane.showInputDialog(
        this,
        "Select column to sort by:",
        "Sort Leaves",
        JOptionPane.QUESTION_MESSAGE,
        null,
        columnOptions,
        "EID");
    
    // If user cancels or closes the dialog, selectedColumn will be null
    if (selectedColumn != null) {
        // Creates a sorting order dialog
        String[] orderOptions = {"Ascending", "Descending"};
        String selectedOrder = (String) JOptionPane.showInputDialog(
            this,
            "Select sorting order:",
            "Sort Order",
            JOptionPane.QUESTION_MESSAGE,
            null,
            orderOptions,
            "Ascending");
        
        // If user cancels or closes the dialog, selectedOrder will be null
        if (selectedOrder != null) {
            boolean ascending = selectedOrder.equals("Ascending");
            leaveController.sortLeavesTable(leavesTable, selectedColumn, ascending);
        }
    }

    }//GEN-LAST:event_sortButtonActionPerformed

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
            java.util.logging.Logger.getLogger(LeaveDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LeaveDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LeaveDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LeaveDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LeaveDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JLabel dashboardPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JLabel designationWithEid;
    private javax.swing.JLabel fullName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable leavesTable;
    private javax.swing.JLabel payrollLabel;
    private javax.swing.JPanel profilePanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton sortButton;
    private javax.swing.JButton updateButton;
    private javax.swing.JButton viewProfileButton;
    private javax.swing.JLabel viewSettingsButton;
    // End of variables declaration//GEN-END:variables
}
