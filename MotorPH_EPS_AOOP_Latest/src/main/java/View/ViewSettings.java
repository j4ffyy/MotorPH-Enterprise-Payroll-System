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
import ViewModel.DBQueries;
import ViewModel.RoleAuthenticator;
import ViewModel.TimeManager;
import ViewModel.UserSession;
import Repository.DataSource;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

public class ViewSettings extends JFrame {

    // Custom JPanel for the loading animation
    private static class LoadingCirclePanel extends JPanel {
        private int angle = 0;
        private Timer timer;

        public LoadingCirclePanel() {
            setOpaque(false); // Make the panel transparent
            setPreferredSize(new java.awt.Dimension(100, 100)); // Set preferred size for the circle
            timer = new Timer(15, new ActionListener() { // Adjust delay for speed
                @Override
                public void actionPerformed(ActionEvent e) {
                    angle = (angle + 5) % 360; // Increment angle for animation
                    repaint();
                }
            });
        }

        public void startAnimation() {
            timer.start();
        }

        public void stopAnimation() {
            timer.stop();
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int size = Math.min(width, height) - 10; // Leave some padding
            int x = (width - size) / 2;
            int y = (height - size) / 2;

            g2d.setColor(new Color(0, 35, 102)); // MotorPH Navy Blue
            g2d.setStroke(new BasicStroke(5)); // Line thickness

            // Draw the animated arc
            g2d.drawArc(x, y, size, size, angle, 270); // Draws 3/4 of a circle
            
            g2d.dispose();
        }
    }

    public static JDialog showLoadingAnimation(JFrame owner, int durationSeconds) {
        JDialog loadingDialog = new JDialog(owner, false); // Non-modal dialog
        loadingDialog.setUndecorated(true);
        loadingDialog.setBackground(new Color(0, 0, 0, 0)); // Fully transparent background

        LoadingCirclePanel loadingPanel = new LoadingCirclePanel();
        loadingDialog.add(loadingPanel);
        loadingDialog.pack();

        // Center the dialog on the owner frame
        loadingDialog.setLocationRelativeTo(owner);
        
        loadingPanel.startAnimation();
        loadingDialog.setVisible(true);

        // Timer to stop animation and dispose dialog after duration
        Timer disposeTimer = new Timer(durationSeconds * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadingPanel.stopAnimation();
                loadingDialog.dispose();
            }
        });
        disposeTimer.setRepeats(false); // Run only once
        disposeTimer.start();
        
        return loadingDialog;
    }

    private TimeManager timeManager;
    private String loggedInUsername;
    private RoleAuthenticator roleAuthenticator;
    
    
     public enum NotificationType {
        SUCCESS,
        ERROR,
        INFO
    }
    
    
    private static Icon scaleIcon(Icon icon, int size) {
        if (icon instanceof ImageIcon) {
            Image img = ((ImageIcon) icon).getImage();
            Image scaledImg = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        }
        return icon;
    }

    private static Icon createSuccessIcon() {
        int size = 24;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Enable anti-aliasing for smooth lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a green circle
        g2d.setColor(new Color(34, 139, 34)); // ForestGreen
        g2d.fillOval(0, 0, size, size);

        // Draw the white checkmark
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3)); // Set line thickness
        g2d.drawLine(6, 12, 11, 17);
        g2d.drawLine(11, 17, 18, 8);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public static void showNotification(final JFrame owner, final String message, final NotificationType type) {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog(owner);
            dialog.setUndecorated(true);
            dialog.setAlwaysOnTop(true);
            dialog.setFocusableWindowState(false);

            // Determine background color based on the owner frame
            Color backgroundColor;
            if (owner instanceof ChangePassword || owner instanceof LoginPage || owner instanceof ForgotPassword) {
                backgroundColor = new Color(255, 255, 255, 230); // White with 230 opacity
            } else {
                backgroundColor = new Color(0, 35, 102, 230); // Signature Navy Blue with 230 opacity
            }
            dialog.setBackground(backgroundColor);

            JPanel panel = new JPanel(new BorderLayout(10, 0)); 
            panel.setOpaque(false);

            JLabel label = new JLabel(message);
            label.setOpaque(false);
            label.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); 
            label.setIconTextGap(10); 

            // Set text color based on background color
            if (backgroundColor.equals(new Color(0, 35, 102, 230))) { // If navy blue background
                label.setForeground(Color.WHITE);
            } else { // If white background
                label.setForeground(new Color(0, 35, 102)); // Navy blue text
            } 

            Icon icon;
            int iconSize = 24;

            switch (type) {
                case SUCCESS:
                    icon = createSuccessIcon();
                    break;
                case ERROR:
                    icon = scaleIcon(UIManager.getIcon("OptionPane.errorIcon"), iconSize);
                    break;
                case INFO:
                default:
                    icon = scaleIcon(UIManager.getIcon("OptionPane.informationIcon"), iconSize);
                    break;
            }
            label.setIcon(icon);

            panel.add(label, BorderLayout.CENTER);
            dialog.setContentPane(panel);

            dialog.pack();

            // --- Slide-in Animation Logic ---
            final int targetX = owner.getX() + owner.getWidth() - dialog.getWidth() - 27; 
            final int startX = targetX + 40; 
            final int fixedY = owner.getY() + 50; 

            dialog.setLocation(startX, fixedY); // Set initial off-screen position
            dialog.setVisible(true);

            final int SLIDE_DURATION_MS = 200; // 0.2 seconds for slide-in
            final int SLIDE_STEP_MS = 10;      // Update position every 10ms
            final int slideSteps = SLIDE_DURATION_MS / SLIDE_STEP_MS;
            final int xStep = (startX - targetX) / slideSteps;

            Timer slideTimer = new Timer(SLIDE_STEP_MS, new ActionListener() {
                private int currentStep = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentStep < slideSteps) {
                        dialog.setLocation(startX - (currentStep * xStep), fixedY);
                        currentStep++;
                    } else {
                        dialog.setLocation(targetX, fixedY); // Ensure it ends at the exact target
                        ((Timer) e.getSource()).stop(); // Stop slide timer
                        // Start the display and fade-out timers after slide-in completes
                        startDisplayAndFadeOutTimers(dialog);
                    }
                }
            });
            slideTimer.start();
        });
    }

    private static void startDisplayAndFadeOutTimers(final JDialog dialog) {
        final int DISPLAY_DURATION_MS = 1000; // 1 second before fade starts
        final int FADE_DURATION_MS = 3000;  // 3 seconds for fade out
        final int FADE_STEP_MS = 50;        // Update opacity every 50ms
        final float INITIAL_OPACITY = dialog.getOpacity();
        final float OPACITY_DECREMENT = INITIAL_OPACITY / (FADE_DURATION_MS / FADE_STEP_MS);

        Timer displayTimer = new Timer(DISPLAY_DURATION_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This timer fires once after DISPLAY_DURATION_MS
                // Now start the fade-out timer
                Timer fadeTimer = new Timer(FADE_STEP_MS, new ActionListener() {
                    float currentOpacity = INITIAL_OPACITY;

                    @Override
                    public void actionPerformed(ActionEvent e2) {
                        currentOpacity -= OPACITY_DECREMENT;
                        if (currentOpacity <= 0) {
                            dialog.setOpacity(0);
                            dialog.dispose();
                            ((Timer) e2.getSource()).stop(); // Stop fade timer
                        } else {
                            dialog.setOpacity(currentOpacity);
                        }
                    }
                });
                fadeTimer.setRepeats(true);
                fadeTimer.start();

                ((Timer) e.getSource()).stop(); // Stop display timer
            }
        });
        displayTimer.setRepeats(false); // Only fire once
        displayTimer.start();
    }
    
    
    public ViewSettings(String username) throws SQLException {
    initComponents();
        
        
    // Stores the username
        this.loggedInUsername = username;
        
        // Initialize time manager for real-time date and time display
        timeManager = new TimeManager(dateTodayLabel, timeNowLabel);
        timeManager.startClock();
    }
    
    public ViewSettings() {
        initComponents();

    // Initialize time manager for real-time date and time display
        timeManager = new TimeManager(dateTodayLabel, timeNowLabel);
        timeManager.startClock();        
    }
    
    
    @Override
    public void dispose() {
        // Stop the time manager when the form is closed
        if (timeManager != null) {
            timeManager.stopClock();
        }
        super.dispose();
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
        jLabel17 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        leaveButton = new javax.swing.JButton();
        changePasswordButton = new javax.swing.JButton();
        logoutButton = new javax.swing.JButton();
        dateTodayLabel = new javax.swing.JLabel();
        timeNowLabel = new javax.swing.JLabel();
        homeButton = new javax.swing.JButton();
        viewTimesheetButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("MotorPH Settings");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MotorPHLogobig.png"))); // NOI18N
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, -1, -1));

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 28)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(37, 61, 144));
        jLabel1.setText("Settings");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, -1));

        leaveButton.setBackground(new java.awt.Color(94, 158, 217));
        leaveButton.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        leaveButton.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel1.add(leaveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 250, 140, -1));

        changePasswordButton.setBackground(new java.awt.Color(94, 158, 217));
        changePasswordButton.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        changePasswordButton.setForeground(new java.awt.Color(255, 255, 255));
        changePasswordButton.setText("Change Password");
        changePasswordButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                changePasswordButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                changePasswordButtonMouseExited(evt);
            }
        });
        changePasswordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changePasswordButtonActionPerformed(evt);
            }
        });
        jPanel1.add(changePasswordButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 200, 140, -1));

        logoutButton.setBackground(new java.awt.Color(255, 107, 107));
        logoutButton.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        logoutButton.setForeground(new java.awt.Color(255, 255, 255));
        logoutButton.setText("Log out");
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButtonMouseExited(evt);
            }
        });
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        jPanel1.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 250, 80, -1));

        dateTodayLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        dateTodayLabel.setForeground(new java.awt.Color(37, 61, 144));
        dateTodayLabel.setText("Date");
        jPanel1.add(dateTodayLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        timeNowLabel.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        timeNowLabel.setForeground(new java.awt.Color(37, 61, 144));
        timeNowLabel.setText("Time");
        jPanel1.add(timeNowLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, -1, -1));

        homeButton.setBackground(new java.awt.Color(94, 158, 217));
        homeButton.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        homeButton.setForeground(new java.awt.Color(255, 255, 255));
        homeButton.setText("Home");
        homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homeButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homeButtonMouseExited(evt);
            }
        });
        homeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(homeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 80, -1));

        viewTimesheetButton1.setBackground(new java.awt.Color(94, 158, 217));
        viewTimesheetButton1.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        viewTimesheetButton1.setForeground(new java.awt.Color(255, 255, 255));
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
        jPanel1.add(viewTimesheetButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, 140, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
        Logger.getLogger(ViewSettings.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this,
            "Error: " + ex.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }   catch (SQLException ex) {
            Logger.getLogger(ViewSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_leaveButtonActionPerformed

    private void changePasswordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changePasswordButtonActionPerformed
        try {
        if (this.loggedInUsername == null || this.loggedInUsername.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "User session not found. Please log in again.", 
                "Session Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Opens changePassword and pass the username
        new ChangePassword(this.loggedInUsername).setVisible(true);
        this.dispose();
    } catch (HeadlessException ex) {
        JOptionPane.showMessageDialog(this, 
            "Error opening Change Password: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_changePasswordButtonActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        UserSession.getInstance().clearSession(); 
        // Close all open frames except the LoginPage we're about to open
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            if (frame.isVisible() && !(frame instanceof LoginPage)) {
                frame.dispose();
            }
        }
        // Open the login page
        new LoginPage().setVisible(true);
    }//GEN-LAST:event_logoutButtonActionPerformed

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
                user.getTotalDeductions(),
                user.getNetPay(),
                user.getUserName()
            ).setVisible(true);
        } else {
            // FIXED: Pass all necessary user details to ViewTimesheet
            new ViewTimesheet(
                user.getFirstName(),
                user.getLastName(),
                user.getDesignation(),
                user.getEid(),
                user.getUserName()  // Include username if needed
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

    private void logoutButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseEntered
        logoutButton.setBackground(new Color(220,38,38));
    }//GEN-LAST:event_logoutButtonMouseEntered

    private void logoutButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutButtonMouseExited
        logoutButton.setBackground(new Color(255,107,107));
    }//GEN-LAST:event_logoutButtonMouseExited

    private void homeButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeButtonMouseEntered
        homeButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_homeButtonMouseEntered

    private void homeButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeButtonMouseExited
        homeButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_homeButtonMouseExited

    private void changePasswordButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePasswordButtonMouseEntered
        changePasswordButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_changePasswordButtonMouseEntered

    private void changePasswordButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changePasswordButtonMouseExited
        changePasswordButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_changePasswordButtonMouseExited

    private void leaveButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveButtonMouseEntered
        leaveButton.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_leaveButtonMouseEntered

    private void leaveButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_leaveButtonMouseExited
        leaveButton.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_leaveButtonMouseExited

    private void viewTimesheetButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewTimesheetButton1MouseEntered
        viewTimesheetButton1.setBackground(new Color(0,35,102));
    }//GEN-LAST:event_viewTimesheetButton1MouseEntered

    private void viewTimesheetButton1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewTimesheetButton1MouseExited
        viewTimesheetButton1.setBackground(new Color(94,158,217));
    }//GEN-LAST:event_viewTimesheetButton1MouseExited

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
            java.util.logging.Logger.getLogger(ViewSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewSettings().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton changePasswordButton;
    private javax.swing.JLabel dateTodayLabel;
    private javax.swing.JButton homeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton leaveButton;
    private javax.swing.JButton logoutButton;
    private javax.swing.JLabel timeNowLabel;
    private javax.swing.JButton viewTimesheetButton1;
    // End of variables declaration//GEN-END:variables
}
