/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ViewModel;

/**
 *
 * @author dashcodes
 */

import Model.PayslipData;
import Repository.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class contains all SQL queries for the payroll system application.
 * Updated to match the current database schema.
 */
public class DBQueries {

    // Login and authentication queries
    public final String userLogin = "SELECT * FROM employee WHERE Username = ? AND Password = ?";
    public final String usernameExists = "SELECT COUNT(*) FROM employee WHERE Username = ?";
    public final String getUserDetailsByUsernamePassword = "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, "
            + "e.Username, e.Password, d.Designation_Name, e.Address, e.Phone_Number, g.SSS_Num, g.Philhealth_Num, " +
        "g.TIN_Num, g.Pagibig_Num, e.Status, s.Supervisor_Name" +
        " FROM employee e " +
        "LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID " +
        "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
        "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID " +
        "WHERE e.Username = ? AND e.Password = ? AND e.is_active = 1";
    
    public final String getUserDetailsByUsername = "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, "
            + "e.Username, e.Password, d.Designation_Name, e.Address, e.Phone_Number, g.SSS_Num, g.Philhealth_Num, " +
        "g.TIN_Num, g.Pagibig_Num, e.Status, s.Supervisor_Name, pc.Basic_Salary, pc.Rice_Subsidy, pc.Phone_Allowance, pc.Clothing_Allowance, pv.Over_Time" +
        " FROM employee e " +
        "LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID " +
        "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
        "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID " +
        "LEFT JOIN employee_payroll_components pc ON e.EID = pc.EID " +
        "LEFT JOIN employee_payroll_variables pv ON e.EID = pv.EID " +
        "WHERE e.Username = ?";
    
    public final String updateUsersPassword = "UPDATE employee SET password = ? WHERE username = ?";
    
    public final String getUserDesignation = "SELECT d.Designation_Name FROM employee "
            + "e JOIN designations d ON e.Designation_ID = d.Designation_ID WHERE e.EID = ?";

    // GetAll queries
    public final String getAllDesignationNames = "SELECT Designation_Name FROM designations";
    public final String getAllSupervisorNames = "SELECT Supervisor_Name FROM supervisors";
    public final String getEmployeeDetailsByEid = 
        "SELECT " +
        "    e.EID, " +
        "    e.Last_Name, " +
        "    e.First_Name, " +
        "    e.Birthday, " +
        "    e.Username, " +
        "    e.Password, " +
        "    d.Designation_Name, " +
        "    e.Address, " +
        "    e.Phone_Number, " +
        "    g.SSS_Num, " +
        "    g.Philhealth_Num, " +
        "    g.TIN_Num, " +
        "    g.Pagibig_Num, " +
        "    e.Status, " +
        "    s.Supervisor_Name, " +
        "    pc.Basic_Salary, " +
        "    pc.Rice_Subsidy, " +
        "    pc.Phone_Allowance, " +
        "    pc.Clothing_Allowance, " +
        "    pv.Over_Time " +
        "FROM employee e " +
        "LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID " +
        "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
        "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID " +
        "LEFT JOIN employee_payroll_components pc ON e.EID = pc.EID " +
        "LEFT JOIN employee_payroll_variables pv ON e.EID = pv.EID " +
        "WHERE e.EID = ?";
    public final String searchEmployees = "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, "
            + "e.Username, e.Password, d.Designation_Name, e.Address, e.Phone_Number, "
            + "g.SSS_Num, g.Philhealth_Num, g.TIN_Num, g.Pagibig_Num, e.Status, s.Supervisor_Name "
            + "FROM employee e LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID "
            + "LEFT JOIN employee_government_ids g ON e.EID = g.EID "
            + "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID "
            + "WHERE (? IS NULL OR e.EID = ?) AND (? IS NULL OR e.Last_Name LIKE ?) AND (? IS NULL OR d.Designation_Name LIKE ?) AND e.is_active = 1";
    
    public final String searchInactiveEmployees = "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, "
            + "e.Username, e.Password, d.Designation_Name, e.Address, e.Phone_Number, "
            + "g.SSS_Num, g.Philhealth_Num, g.TIN_Num, g.Pagibig_Num, e.Status, s.Supervisor_Name "
            + "FROM employee e LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID "
            + "LEFT JOIN employee_government_ids g ON e.EID = g.EID "
            + "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID "
            + "WHERE (? IS NULL OR e.EID = ?) AND (? IS NULL OR e.Last_Name LIKE ?) AND (? IS NULL OR d.Designation_Name LIKE ?) AND e.is_active = 0";
    
    // Employee management queries
    public final String addEmployee = "INSERT INTO employee (EID, Last_Name, First_Name, Birthday, "
            + "Username, Password, Designation_ID, Address, Phone_Number, Status, Supervisor_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public final String updateEmployee = "UPDATE employee e JOIN employee_payroll_components epc ON e.EID = epc.EID SET e.Last_Name = ?, e.First_Name = ?, e.Birthday = ?, e.Address = ?, e.Phone_Number = ?, e.Status = ?, e.Designation_ID = ?, epc.Basic_Salary = ? WHERE e.EID = ?";
    public final String reactivateEmployee = "UPDATE employee SET is_active = 1 WHERE EID = ?";
    public final String deleteEmployee = "UPDATE employee SET is_active = 0 WHERE EID = ?";
    public final String getEmployeeByEid = 
    "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, e.Username, e.Password, " +
    "d.Designation_Name, e.Address, e.Phone_Number, e.Status, s.Supervisor_Name, " +
    "g.SSS_Num, g.Philhealth_Num, g.TIN_Num, g.Pagibig_Num, pc.Basic_Salary, pc.Rice_Subsidy, pc.Phone_Allowance, pc.Clothing_Allowance " +
    "FROM employee e " +  // <-- Space here
    "LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID " +
    "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID " +
    "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
    "LEFT JOIN employee_payroll_components pc ON e.EID = pc.EID " +
    "WHERE e.EID = ? AND e.is_active = 1";
    
    public final String getAllEmployees = 
    "SELECT " +
    "e.EID, " +
    "e.Last_Name, " +
    "e.First_Name, " +
    "e.Birthday, " +
    "e.Address, " +
    "e.Phone_Number, " +
    "g.SSS_Num, " +
    "g.Philhealth_Num, " +
    "g.TIN_Num, " +
    "g.Pagibig_Num, " +
    "e.Status, " +
    "d.Designation_Name, " +
    "pc.Basic_Salary " +
    "FROM employee e " +
    "JOIN designations d ON e.Designation_ID = d.Designation_ID " +
    "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
    "LEFT JOIN employee_payroll_components pc ON e.EID = pc.EID " +
    "WHERE e.is_active = 1";
    
     public final String insertEmployee = "INSERT INTO employee (EID, Last_Name, First_Name, Birthday, Address, Phone_Number, " +
        "Username, Password, Status, Designation_ID, Supervisor_ID) " + 
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Government IDs queries
    public final String addGovernmentIds = "INSERT INTO employee_government_ids "
            + "(EID, SSS_Num, Philhealth_Num, TIN_Num, Pagibig_Num) VALUES (?, ?, ?, ?, ?)";
    public final String updateGovernmentIds = "UPDATE employee_government_ids SET SSS_Num = ?, "
            + "Philhealth_Num = ?, TIN_Num = ?, Pagibig_Num = ? WHERE EID = ?";
    public final String getGovernmentIdsByEid = "SELECT SSS_Num, "
            + "Philhealth_Num, TIN_Num, Pagibig_Num FROM employee_government_ids WHERE EID = ?";

    // Payroll components queries
    public final String addPayrollComponents = "INSERT INTO employee_payroll_components "
            + "(EID, Basic_Salary, Rice_Subsidy, Phone_Allowance, Clothing_Allowance, Half_Month_Rate, Hourly_Rate) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    public final String updatePayrollComponents = "UPDATE employee_payroll_components SET Basic_Salary = ?, "
            + "Rice_Subsidy = ?, Phone_Allowance = ?, Clothing_Allowance = ?, Half_Month_Rate = ?, Hourly_Rate = ? WHERE EID = ?";
    public final String getPayrollComponentsByEid = "SELECT Basic_Salary, Rice_Subsidy, Phone_Allowance, Clothing_Allowance, "
            + "Half_Month_Rate, Hourly_Rate FROM employee_payroll_components WHERE EID = ?";

    // Payroll variables queries (for incentives like Overtime, Holiday Pay, Performance Bonus)
    public final String addPayrollVariables = "INSERT INTO employee_payroll_variables "
            + "(EID, Over_Time, Holiday_Pay, Performance_Bonus) VALUES (?, ?, ?, ?)";
    public final String updatePayrollVariables = "UPDATE employee_payroll_variables SET Over_Time = ?, "
            + "Holiday_Pay = ?, Performance_Bonus = ? WHERE EID = ?";
    public final String getPayrollVariablesByEid = "SELECT Over_Time, Holiday_Pay, Performance_Bonus "
            + "FROM employee_payroll_variables WHERE EID = ?";
    public final String getEmployeePayrollComponentsByEid = "SELECT * FROM employee_payroll_components WHERE EID = ?";

    // Deduction related queries
    public final String addEmployeeDeduction = "INSERT INTO employee_deductions (EID, Deduction_ID, Amount) VALUES (?, ?, ?)";
    public final String updateEmployeeDeduction = "UPDATE employee_deductions SET Amount = ? WHERE EID = ? AND Deduction_ID = ?";
    public final String getEmployeeDeductionsByEid = "SELECT dt.Deduction_Name, ed.Amount FROM employee_deductions "
            + "ed JOIN deduction_types dt ON ed.Deduction_ID = dt.Deduction_ID WHERE ed.EID = ?";
    public final String getDeductionIdByName = "SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = ?";

    // Timesheet queries
    public final String addTimesheetEntry = "INSERT INTO timesheet (EID, LogDate, LogTime, AttStatus) VALUES (?, ?, ?, ?)";
    public final String getTimesheetByEid = "SELECT LogDate, LogTime, AttStatus FROM timesheet WHERE EID = ?";
    public final String getEmployeeAttendance = "SELECT LogDate, LogTime, AttStatus FROM timesheet WHERE EID = ? ORDER BY LogDate DESC, LogTime DESC";
    public final String getAllTimesheets = "SELECT t.Attendance_ID, t.EID, e.First_Name, e.Last_Name, "
            + "t.LogDate, t.LogTime, t.AttStatus FROM timesheet t JOIN employee e ON t.EID = e.EID ORDER BY t.LogDate DESC, t.LogTime DESC";
    public final String getEmployeeAttendanceByDateRange = "SELECT LogDate, LogTime, AttStatus FROM timesheet WHERE EID = ? "
            + "AND LogDate BETWEEN ? AND ? ORDER BY LogDate ASC";
    public final String deleteTimesheet = "DELETE FROM timesheet WHERE Attendance_ID = ?";
    public final String updateTimesheet = "UPDATE timesheet SET EID = ?, LogDate = ?, LogTime = ?, AttStatus = ? WHERE Attendance_ID = ?";
    
    // Leave management queries
    public final String applyLeave = "INSERT INTO leaves (Leave_ID, EID, Date_Filed, Date_From, Date_To, Reason_For_Leave, Leave_Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public final String updateLeaveStatus = "UPDATE leaves SET Leave_Status = ? WHERE Leave_ID = ?";
    public final String getLeaveByEid = "SELECT Leave_ID, EID, Date_Filed, Date_From, Date_To, "
            + "Reason_For_Leave, Leave_Status FROM leaves WHERE EID = ? ORDER BY Date_Filed DESC";
    public final String getAllLeaves = "SELECT l.Leave_ID, l.EID, e.First_Name, e.Last_Name, "
            + "l.Date_Filed, l.Date_From, l.Date_To, l.Reason_For_Leave, l.Leave_Status FROM leaves l JOIN employee e ON l.EID = e.EID ORDER BY l.Date_Filed DESC";
    public final String deleteLeave = "DELETE FROM leaves WHERE Leave_ID = ?";
    public final String getLeaveByIdQuery = "SELECT Leave_ID, EID, Date_Filed, Date_From, Date_To, Reason_For_Leave, Leave_Status FROM leaves WHERE Leave_ID = ?";
    public final String getMaxLeaveId = "SELECT MAX(Leave_ID) AS maxId FROM leaves";

    // Other utility queries
    public final String getDesignationIdByName = "SELECT Designation_ID FROM designations WHERE Designation_Name = ?";
    public final String getSupervisorIdByName = "SELECT Supervisor_ID FROM supervisors WHERE Supervisor_Name = ?";
    public final String generateNextEid = "SELECT MAX(EID) + 1 AS nextEid FROM employee";
    public final String getEmployeeReportData = 
        "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, e.Username, e.Password, " +
        "d.Designation_Name, e.Address, e.Phone_Number, g.SSS_Num, g.Philhealth_Num, " +
        "g.TIN_Num, g.Pagibig_Num, e.Status, s.Supervisor_Name, " +
        "pc.Basic_Salary, pc.Rice_Subsidy, pc.Phone_Allowance, pc.Clothing_Allowance, pc.Half_Month_Rate, pc.Hourly_Rate, " +
        "pv.Over_Time, pv.Holiday_Pay, pv.Performance_Bonus " +
        "FROM employee e " +
        "LEFT JOIN designations d ON e.Designation_ID = d.Designation_ID " +
        "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
        "LEFT JOIN supervisors s ON e.Supervisor_ID = s.Supervisor_ID " +
        "LEFT JOIN employee_payroll_components pc ON e.EID = pc.EID " +
        "LEFT JOIN employee_payroll_variables pv ON e.EID = pv.EID " +
        "WHERE e.is_active = 1";
    
    // Payslip query - Refactored to use employee_payroll_view and accurate column names
    public final String getPayslipData =
        "SELECT " +
        "    e.EID AS Employee_ID, " +
        "    CONCAT(e.Last_Name, ', ', e.First_Name) AS Employee_Name, " +
        "    d.Designation_Name AS Designation, " +
        "    pc.Basic_Salary AS Monthly_Rate, " +
        "    pc.Half_Month_Rate AS gross_semi_monthly_rate, " +
        "    (pc.Hourly_Rate * 8) AS Daily_Rate, " +
        "    pc.Hourly_Rate AS Hourly_Rate, " +
        "    pv.Over_Time AS Overtime_Hours, " +
    	"    (pc.Hourly_Rate * pv.Over_Time) AS Overtime_Pay, " +
    	"    pv.Holiday_Pay AS Holiday_Pay, " +
    	"    pv.Performance_Bonus AS Performance_Bonus, " +
        "    (pc.Basic_Salary + (pc.Hourly_Rate * pv.Over_Time)) AS Gross_Income, " +
        "    pc.Rice_Subsidy, " + 
        "    pc.Phone_Allowance, " +
        "    pc.Clothing_Allowance, " +
        "    (pc.Rice_Subsidy + pc.Phone_Allowance + pc.Clothing_Allowance) AS Total_Benefits, " +
        "    (SELECT COUNT(DISTINCT LogDate) FROM timesheet WHERE EID = e.EID AND LogDate BETWEEN ? AND ?) AS Days_Worked, " +
        "    COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'SSS')), 0) AS SSS_Contribution, " +
        "    COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'PhilHealth')), 0) AS Philhealth_Contribution, " +
        "    COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'PagIBIG')), 0) AS Pagibig_Contribution, " +
        "    COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'Withholding_Tax')), 0) AS Withholding_Tax, " +
        "    (COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'SSS')), 0) + " +
        "     COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'PhilHealth')), 0) + " +
        "     COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'Pag-IBIG')), 0) + " +
        "     COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'Withholding Tax')), 0)) AS Total_Deductions, " +
        "    ((pc.Basic_Salary + (pc.Hourly_Rate * pv.Over_Time)) - (COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'SSS')), 0) + " +
        "     COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'PhilHealth')), 0) + " +
        "     COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'Pag-IBIG')), 0) + " +
        "     COALESCE((SELECT Amount FROM employee_deductions WHERE EID = e.EID AND Deduction_ID = (SELECT Deduction_ID FROM deduction_types WHERE Deduction_Name = 'Withholding Tax')), 0))) AS Take_Home_Pay " +
        "FROM employee e " +
        "JOIN employee_payroll_components pc ON e.EID = pc.EID " +
        "JOIN employee_payroll_variables pv ON e.EID = pv.EID " +
        "JOIN designations d ON e.Designation_ID = d.Designation_ID " +
        "WHERE e.EID = ?;";
        
    /**
     *
     * @param dateString
     * @return
     * @throws IllegalArgumentException
     */
    public java.sql.Date convertStringToSqlDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.setLenient(false); // Make sure date parsing is strict
            java.util.Date utilDate = sdf.parse(dateString);
            return new java.sql.Date(utilDate.getTime());
        } catch (ParseException |IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format. Expected MM/DD/YYYY.", e);
        }
    }

    public String formatSqlDateToString(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(sqlDate);
    }
    
    // Utility for date formatting from MM/dd/yyyy to YYYY-MM-dd
    public String formatDateForDatabase(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        inputFormat.setLenient(false); // Strict parsing - rejects invvalid dates = "13/49/9999"
        
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date date = inputFormat.parse(dateString);
        return outputFormat.format(date);
    }

    public String createSortQuery(String columnName, boolean ascending) {
        return "SELECT e.EID, e.Last_Name, e.First_Name, e.Birthday, e.Address, e.Phone_Number, " +
               "g.SSS_Num, g.Philhealth_Num, g.TIN_Num, g.Pagibig_Num, " +
               "d.Designation_Name, e.Status, epc.Basic_Salary " +
               "FROM employee e " +
               "JOIN designations d ON e.Designation_ID = d.Designation_ID " +
               "LEFT JOIN employee_government_ids g ON e.EID = g.EID " +
               "JOIN employee_payroll_components epc ON e.EID = epc.EID " +
               "ORDER BY " + columnName + (ascending ? " ASC" : " DESC");
    }
    
    // Report columns enum
    public enum ReportColumns {
        EID("EID"),
        FULL_NAME("Full Name"),
        BIRTHDAY("Birthday"),
        ADDRESS("Address"),
        PHONE_NUMBER("Phone Number"),
        SSS("sss #"),
        PHILHEALTH("PhilHealth #"),
        TIN("TIN #"),
        PAGIBIG("Pag-IBIG #"),
        STATUS("STATUS"),
        DESIGNATION("Designation"),
        GROSS_SALARY("Gross Salary");

        private final String columnName;

        ReportColumns(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }

        public static String[] getAllColumnNames() {
            return Arrays.stream(values()).map(ReportColumns::getColumnName).toArray(String[]::new);
        }
    }

    public String getQuery(String queryName) {
        switch (queryName) {
            case "USER_LOGIN": 
                return userLogin;
            case "USERNAME_EXISTS": 
                return usernameExists;
            case "GET_USER_DETAILS_BY_USERNAME_PASSWORD": 
                return getUserDetailsByUsernamePassword;
            case "GET_USER_DETAILS_BY_USERNAME":
                return getUserDetailsByUsername;
            case "UPDATE_USERS_PASSWORD": // Added new case
                return updateUsersPassword;
            case "GET_USER_DESIGNATION": 
                return getUserDesignation;
            case "ADD_EMPLOYEE": 
                return addEmployee;
            case "INSERT_EMPLOYEE":
                return insertEmployee;
            case "UPDATE_EMPLOYEE": 
                return updateEmployee;
            case "DELETE_EMPLOYEE": 
                return deleteEmployee;
            case "GET_EMPLOYEE_BY_EID": 
                return getEmployeeByEid;
            case "GET_ALL_EMPLOYEES": 
                return getAllEmployees;
            case "ADD_GOVERNMENT_IDS": 
                return addGovernmentIds;
            case "UPDATE_GOVERNMENT_IDS": 
                return updateGovernmentIds;
            case "GET_GOVERNMENT_IDS_BY_EID": 
                return getGovernmentIdsByEid;
            case "ADD_PAYROLL_COMPONENTS": 
                return addPayrollComponents;
            case "UPDATE_PAYROLL_COMPONENTS": 
                return updatePayrollComponents;
            case "GET_PAYROLL_COMPONENTS_BY_EID": 
                return getPayrollComponentsByEid;
            case "ADD_PAYROLL_VARIABLES": 
                return addPayrollVariables;
            case "UPDATE_PAYROLL_VARIABLES": 
                return updatePayrollVariables;
            case "GET_PAYROLL_VARIABLES_BY_EID": 
                return getPayrollVariablesByEid;
            case "ADD_EMPLOYEE_DEDUCTION": 
                return addEmployeeDeduction;
            case "UPDATE_EMPLOYEE_DEDUCTION": 
                return updateEmployeeDeduction;
            case "GET_EMPLOYEE_DEDUCTIONS_BY_EID": 
                return getEmployeeDeductionsByEid;
            case "GET_DEDUCTION_ID_BY_NAME": 
                return getDeductionIdByName;
            case "ADD_TIMESHEET_ENTRY": 
                return addTimesheetEntry;
            case "GET_TIMESHEET_BY_EID": 
                return getTimesheetByEid;
            case "GET_EMPLOYEE_ATTENDANCE": 
                return getEmployeeAttendance;
            case "GET_ALL_TIMESHEETS": 
                return getAllTimesheets;
            case "GET_EMPLOYEE_ATTENDANCE_BY_DATE_RANGE": 
                return getEmployeeAttendanceByDateRange;
            case "DELETE_TIMESHEET":
                return deleteTimesheet;
            case "UPDATE_TIMESHEET":
                return updateTimesheet;
            case "APPLY_LEAVE": 
                return applyLeave;
            case "UPDATE_LEAVE_STATUS": 
                return updateLeaveStatus;
            case "GET_LEAVE_BY_EID": 
                return getLeaveByEid;
            case "GET_ALL_LEAVES": 
                return getAllLeaves;
            case "DELETE_LEAVE": 
                return deleteLeave;
            case "GET_DESIGNATION_ID_BY_NAME": 
                return getDesignationIdByName;
            case "GET_SUPERVISOR_ID_BY_NAME": 
                return getSupervisorIdByName;
            case "GENERATE_NEXT_EID": 
                return generateNextEid;
            case "GET_EMPLOYEE_REPORT_DATA": 
                return getEmployeeReportData;
            case "GET_PAYSLIP_DATA": 
                return getPayslipData;
            case "GET_MAX_LEAVE_ID":
                return getMaxLeaveId;
            default: 
                return null;
        }
    }
}