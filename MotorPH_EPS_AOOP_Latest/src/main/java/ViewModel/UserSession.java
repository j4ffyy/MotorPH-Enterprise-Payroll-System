/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ViewModel;

/**
 *
 * @author dashcodes
 */

import Model.EmployeeDetails;

public class UserSession {
    private static UserSession instance;
    private EmployeeDetails loggedInUser;
    private String username;
    
    private UserSession() {
        // Private constructor for singleton
    }
    
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setLoggedInUser(EmployeeDetails user) {
        this.loggedInUser = user;
    }
    
    public EmployeeDetails getLoggedInUser() {
        return loggedInUser;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void clearSession() {
        loggedInUser = null;
        username = null;
    }
}