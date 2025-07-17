package Repository;

import Model.Timesheet;
import ViewModel.DBQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimesheetRepository implements ITimesheetRepository {

    private final DBQueries dbQueries = new DBQueries();

    @Override
    public void addTimesheetEntry(Timesheet timesheet) throws SQLException {
        String query = dbQueries.addTimesheetEntry;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, timesheet.getEid());
            pst.setDate(2, new java.sql.Date(timesheet.getDate().getTime()));
            pst.setString(3, timesheet.getTimeIn());
            pst.setString(4, timesheet.getTimeOut());
            pst.executeUpdate();
        }
    }

    @Override
    public void updateTimesheet(Timesheet timesheet) throws SQLException {
        String query = dbQueries.updateTimesheet;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, timesheet.getEid());
            pst.setDate(2, new java.sql.Date(timesheet.getDate().getTime()));
            pst.setString(3, timesheet.getTimeIn());
            pst.setString(4, timesheet.getTimeOut());
            pst.setInt(5, timesheet.getTimesheetID());
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteTimesheet(int timesheetId) throws SQLException {
        String query = dbQueries.deleteTimesheet;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, timesheetId);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Timesheet> getTimesheetByEid(int employeeId) throws SQLException {
        String query = dbQueries.getTimesheetByEid;
        List<Timesheet> timesheets = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, employeeId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    timesheets.add(new Timesheet(
                        rs.getInt("Attendance_ID"),
                        rs.getInt("EID"),
                        rs.getDate("LogDate"),
                        rs.getString("LogTime"),
                        rs.getString("AttStatus")
                    ));
                }
            }
        }
        return timesheets;
    }

    @Override
    public List<Timesheet> getAllTimesheets() throws SQLException {
        String query = dbQueries.getAllTimesheets;
        List<Timesheet> timesheets = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                timesheets.add(new Timesheet(
                    rs.getInt("Attendance_ID"),
                    rs.getInt("EID"),
                    rs.getDate("LogDate"),
                    rs.getString("LogTime"),
                    rs.getString("AttStatus")
                ));
            }
        }
        return timesheets;
    }
}
