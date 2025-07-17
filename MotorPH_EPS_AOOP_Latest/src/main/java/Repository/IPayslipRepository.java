package Repository;

import Model.PayslipData;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Interface for accessing payslip data.
 */
public interface IPayslipRepository {
    List<PayslipData> getEmployeePayslipData(int eid, Date startDate, Date endDate) throws SQLException;
}
