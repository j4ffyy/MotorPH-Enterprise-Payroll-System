/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModel.Reports;


/**
 *
 * @author dashcodes
 */

import Model.EmployeeReports;
import Repository.DataSource;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class PayrollReportsGenerator {
    private final EmployeeReports employeeReport;
    
    public PayrollReportsGenerator(EmployeeReports employeeReport) {
        this.employeeReport = employeeReport;
    }
    
    public void generatePayslipPDF() {
        try (Connection connection = DataSource.getInstance().getConnection()) {
            // Load the compiled report (.jasper file)
            InputStream reportStream = getClass().getResourceAsStream(
                "/Resources/JasperReports/reports_payslip.jasper");
            
            if (reportStream == null) {
                throw new Exception("JasperReport template not found in resources");
            }
            
            // Load the compiled report
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            
            // Prepare parameters 
            Map<String, Object> parameters = new HashMap<>();
            
            // Employee Basic Information
            parameters.put("employeeId", employeeReport.getEid()); 
            parameters.put("lastName", employeeReport.getLastName());
            parameters.put("firstName", employeeReport.getFirstName());
            
            
            parameters.put("position", employeeReport.getDesignation());
            parameters.put("department", employeeReport.getDesignation());
            
            // Government Numbers
            parameters.put("sssNumber", employeeReport.getSssNumber());
            parameters.put("philhealthNumber", employeeReport.getPhilhealthNumber());
            parameters.put("tinNumber", employeeReport.getTinNumber());
            parameters.put("pagibigNumber", employeeReport.getPagibigNumber());
            
            // Salary Information
            parameters.put("basicSalary", employeeReport.getBasicSalary());
            parameters.put("hourlyRate", employeeReport.getHourlyRate());
            
            // Calculate grossPay and netPay if not available in EmployeeReports
            BigDecimal grossPay = employeeReport.getBasicSalary()
                .add(employeeReport.getRiceSubsidy() != null ? employeeReport.getRiceSubsidy() : BigDecimal.ZERO)
                .add(employeeReport.getPhoneAllowance() != null ? employeeReport.getPhoneAllowance() : BigDecimal.ZERO)
                .add(employeeReport.getClothingAllowance() != null ? employeeReport.getClothingAllowance() : BigDecimal.ZERO);
            
            parameters.put("grossPay", grossPay);
            parameters.put("netPay", grossPay); // Adjust this if you have deductions
            
            // Use current date if dateFrom/dateTo not available
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String currentDate = dateFormat.format(new java.util.Date());
            parameters.put("dateFrom", currentDate);
            parameters.put("dateTo", currentDate);
            
            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, connection);
            
            // Export to PDF
            String outputFile = System.getProperty("user.home") + File.separator + 
                "Documents" + File.separator + "MotorPH_Payslip_" + 
                employeeReport.getEid() + "_" + 
                System.currentTimeMillis() + ".pdf";
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
            
            // Open the generated PDF
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(outputFile));
            }
            
        } catch (Exception e) {
            }
    }

    public void generatePayrollSummaryPDF() {
        try (Connection connection = DataSource.getInstance().getConnection()) {
            InputStream reportStream = getClass().getResourceAsStream(
                "/Resources/JasperReports/MotorphReports/reports_payrollsummary.jasper");
            
            if (reportStream == null) {
                throw new Exception("JasperReport template not found in resources: reports_payrollsummary.jasper");
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            // Add parameters here -- For example:
            // parameters.put("startDate", someStartDateObject);
            // parameters.put("endDate", someEndDateObject);
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, connection);
            
            String outputFile = System.getProperty("user.home") + File.separator + 
                "Documents" + File.separator + "MotorPH_PayrollSummary_" +
                System.currentTimeMillis() + ".pdf";
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(outputFile));
            }
            
        } catch (Exception e) {
        }
    }
}
