package Repository;

import Model.Leave;
import ViewModel.DBQueries;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LeaveRepository implements ILeaveRepository {

    private final DBQueries dbQueries = new DBQueries();

    @Override
    public void applyLeave(Leave leave) throws SQLException {
        String query = dbQueries.applyLeave;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, leave.getEid());
            pst.setDate(2, new java.sql.Date(leave.getDateFiled().getTime()));
            pst.setDate(3, new java.sql.Date(leave.getDateFrom().getTime()));
            pst.setDate(4, new java.sql.Date(leave.getDateTo().getTime()));
            pst.setString(5, leave.getReasonForLeave());
            pst.setString(6, leave.getLeaveStatus());
            pst.executeUpdate();
        }
    }

    @Override
    public void updateLeaveStatus(int leaveId, String status) throws SQLException {
        String query = dbQueries.updateLeaveStatus;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, status);
            pst.setInt(2, leaveId);
            pst.executeUpdate();
        }
    }

    @Override
    public void deleteLeave(int leaveId) throws SQLException {
        String query = dbQueries.deleteLeave;
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, leaveId);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Leave> getLeaveByEid(int employeeId) throws SQLException {
        String query = dbQueries.getLeaveByEid;
        List<Leave> leaves = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, employeeId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    leaves.add(new Leave(
                        rs.getInt("EID"),
                        String.valueOf(rs.getInt("Leave_ID")),
                        rs.getDate("Date_Filed"),
                        rs.getDate("Date_From"),
                        rs.getDate("Date_To"),
                        rs.getString("Reason_For_Leave"),
                        rs.getString("Leave_Status")
                    ));
                }
            }
        }
        return leaves;
    }

    @Override
    public List<Leave> getAllLeaves() throws SQLException {
        String query = dbQueries.getAllLeaves;
        List<Leave> leaves = new ArrayList<>();
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                leaves.add(new Leave(
                    rs.getInt("EID"),
                    rs.getString("Leave_ID"),
                    rs.getDate("Date_Filed"),
                    rs.getDate("Date_From"),
                    rs.getDate("Date_To"),
                    rs.getString("Reason_For_Leave"),
                    rs.getString("Leave_Status")
                ));
            }
        }
        return leaves;
    }
}
