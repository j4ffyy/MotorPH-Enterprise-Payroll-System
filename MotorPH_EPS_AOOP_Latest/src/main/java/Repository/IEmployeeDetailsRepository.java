package Repository;

import Model.EmployeeDetails;
import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDetailsRepository {
    void addEmployee(EmployeeDetails employee) throws SQLException;
    void updateEmployee(EmployeeDetails employee) throws SQLException;
    void deleteEmployee(int employeeId) throws SQLException;
    EmployeeDetails getEmployeeById(int employeeId) throws SQLException;

    /**
     *
     * @return
     * @throws SQLException
     */
    List<EmployeeDetails> getAllEmployees() throws SQLException;
}
