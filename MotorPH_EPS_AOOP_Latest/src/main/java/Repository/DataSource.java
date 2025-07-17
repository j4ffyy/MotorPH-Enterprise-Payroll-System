/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Repository;

/**
 *
 * @author dashcodes
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataSource {
    private static final String URL = "jdbc:mysql://localhost:3306/payrollsystem_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";
    
    private static DataSource instance;
    Connection connection;
    
    public DataSource() {} // Private constructor
    
    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }
    
    public static void setConnection(Connection connection){
        connection = connection;
    }
    public Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            throw new SQLException("Connection failed: " + e.getMessage());
        }
    }
    
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}