package Repository;

import Model.PayslipData;
import ViewModel.DBQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the IPayslipRepository interface.
 */
public class PayslipRepository implements IPayslipRepository {

    private final DBQueries dbQueries = new DBQueries();

    @Override
    public List<PayslipData> getEmployeePayslipData(int eid, Date startDate, Date endDate) throws SQLException {
        List<PayslipData> payslipDataList = new ArrayList<>();
        String query = dbQueries.getPayslipData;

        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            // Set parameters for the query
            // The query has two date parameters for Days_Worked and one EID parameter at the end
            pst.setDate(1, new java.sql.Date(startDate.getTime()));
            pst.setDate(2, new java.sql.Date(endDate.getTime()));
            pst.setInt(3, eid);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    PayslipData payslipData = new PayslipData();
                    payslipData.setEmployeeId(rs.getInt("Employee_ID"));
                    payslipData.setEmployeeName(rs.getString("Employee_Name"));
                    payslipData.setPosition(rs.getString("Designation"));
                    payslipData.setDepartment(""); // No direct mapping in query
                    payslipData.setMonthlyRate(rs.getBigDecimal("Monthly_Rate"));
                    payslipData.setDailyRate(rs.getBigDecimal("Daily_Rate"));
                    payslipData.setDaysWorked(rs.getLong("Days_Worked"));
                    payslipData.setOvertimeHours(rs.getInt("Overtime_Hours"));
                    payslipData.setOvertimePay(rs.getBigDecimal("Overtime_Pay"));
                    payslipData.setGrossIncome(rs.getBigDecimal("Gross_Income"));
                    payslipData.setRiceSubsidy(rs.getBigDecimal("Rice_Subsidy"));
                    payslipData.setPhoneAllowance(rs.getBigDecimal("Phone_Allowance"));
                    payslipData.setClothingAllowance(rs.getBigDecimal("Clothing_Allowance"));
                    payslipData.setTotalBenefits(rs.getBigDecimal("Total_Benefits"));
                    payslipData.setSssContribution(rs.getBigDecimal("SSS_Contribution"));
                    payslipData.setPhilhealthContribution(rs.getBigDecimal("Philhealth_Contribution"));
                    payslipData.setPagibigContribution(rs.getBigDecimal("Pagibig_Contribution"));
                    payslipData.setWithholdingTax(rs.getBigDecimal("Withholding_Tax"));
                    payslipData.setTotalDeductions(rs.getBigDecimal("Total_Deductions"));
                    payslipData.setTakeHomePay(rs.getBigDecimal("Take_Home_Pay"));
                    payslipDataList.add(payslipData);
                }
            }
        }
        return payslipDataList;
    }
}