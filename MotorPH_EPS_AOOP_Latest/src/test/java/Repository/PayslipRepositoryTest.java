package Repository;

import Model.PayslipData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PayslipRepositoryTest {

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void testGetEmployeePayslipData() {
        PayslipRepository repository = new PayslipRepository();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse("2023-01-01");
            Date endDate = sdf.parse("2023-01-31");
            assertNotNull("Payslip data should not be null", repository.getEmployeePayslipData(10001, startDate, endDate));
        } catch (SQLException | ParseException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}