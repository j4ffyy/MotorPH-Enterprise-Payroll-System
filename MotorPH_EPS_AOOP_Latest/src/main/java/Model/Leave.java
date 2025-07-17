/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Model;

/**
 *
 * @author dashcodes
 */

import java.util.Date;

public class Leave {
    private int eid;
    private String leaveId;
    private Date dateFiled;
    private Date dateFrom;
    private Date dateTo;
    private String reasonForLeave;
    private String leaveStatus;
    private String notes;
    
    // Constructor
    public Leave
            (
            int eid,
            String leaveId,
            Date dateFiled,
            Date dateFrom,
            Date dateTo, 
            String reasonForLeave,
            String leaveStatus
            )
        {
        this.eid = eid;
        this.leaveId = leaveId;
        this.dateFiled = dateFiled;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.reasonForLeave = reasonForLeave;
        this.leaveStatus = leaveStatus;
        }
    
    // Default Constructor
    public Leave(){
    }

    // Getter methods

    public int getEid() {
        return eid;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public Date getDateFiled() {
        return dateFiled;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public String getReasonForLeave() {
        return reasonForLeave;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public String getNotes() {
        return notes;
    }
    
    
    
    // Setter methods

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setLeaveId(String leaveId) {
        this.leaveId = leaveId;
    }

    public void setDateFiled(Date dateFiled) {
        this.dateFiled = dateFiled;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setReasonForLeave(String reasonForLeave) {
        this.reasonForLeave = reasonForLeave;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }    
}
