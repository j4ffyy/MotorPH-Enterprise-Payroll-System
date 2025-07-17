package ViewModel.Reports;

/*
*@author dashcodes
*/

import Model.EmployeeReports;
import Repository.DataSource;
import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class EmployeeReportGenerator {
    private final EmployeeReports employeeReport;
    
    public EmployeeReportGenerator(EmployeeReports employeeReport) {
        this.employeeReport = employeeReport;
    }
    
    public void generateEmployeeReportPDF() {
        try (Connection connection = DataSource.getInstance().getConnection()) {
            InputStream reportStream = getClass().getResourceAsStream(
                "/Resources/JasperReports/MotorphReports/reports_employee.jasper");
            
            if (reportStream == null) {
                throw new Exception("JasperReport template not found in resources: reports_employee.jasper");
            }
            
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);
            
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("employeeId", employeeReport.getEid());
            parameters.put("lastName", employeeReport.getLastName());
            parameters.put("firstName", employeeReport.getFirstName());
            // Add more parameters later for better view of the employee reports...
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport, parameters, connection);
            
            String outputFile = System.getProperty("user.home") + File.separator + 
                "Documents" + File.separator + "MotorPH_EmployeeReport_" + 
                employeeReport.getEid() + "_" +
                System.currentTimeMillis() + ".pdf";
            
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
            
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(outputFile));
            }
            
        } catch (Exception e) {
            // Handle error appropriately
            
        }
    }
}