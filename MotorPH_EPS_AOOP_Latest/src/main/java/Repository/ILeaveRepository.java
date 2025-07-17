package Repository;

import Model.Leave;
import java.sql.SQLException;
import java.util.List;

public interface ILeaveRepository {
    void applyLeave(Leave leave) throws SQLException;
    void updateLeaveStatus(int leaveId, String status) throws SQLException;
    void deleteLeave(int leaveId) throws SQLException;
    List<Leave> getLeaveByEid(int employeeId) throws SQLException;
    List<Leave> getAllLeaves() throws SQLException;
}
