package Repository;

import Model.Timesheet;
import java.sql.SQLException;
import java.util.List;

public interface ITimesheetRepository {
    void addTimesheetEntry(Timesheet timesheet) throws SQLException;
    void updateTimesheet(Timesheet timesheet) throws SQLException;
    void deleteTimesheet(int timesheetId) throws SQLException;
    List<Timesheet> getTimesheetByEid(int employeeId) throws SQLException;
    List<Timesheet> getAllTimesheets() throws SQLException;
}
