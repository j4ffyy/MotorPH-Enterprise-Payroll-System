package Model;

/**
 *
 * @author dashcodes
 */

public class Incentives {
    private int eid;
    private float overTime; // number of overtime hours worked
    private float overTimePay;
    private float performanceBonus;
    private float holidayPay;
    private float totalIncentives;
    
    
    public Incentives(){
    
    }
    
    public Incentives(int eid){
        this.eid = eid;
    }
    
    public Incentives(
            int eid, 
            float overTimePay, 
            float overTime,
            float performanceBonus,
            float holidayPay,
            float totalIncentives
    )
    {
        this.eid = eid;
        this.overTimePay = overTimePay;
        this.overTime = overTime;
        this.performanceBonus = performanceBonus;
        this.holidayPay = holidayPay;
        this.totalIncentives = totalIncentives;
    }
    
    public Incentives(
            int eid, 
            float overTime, 
            float performanceBonus
            )
    {
        this.eid = eid;
        this.overTime = overTime;
        this.performanceBonus = performanceBonus;
    }
    
    public Incentives(
            int eid, 
            float overTime, 
            float performanceBonus, 
            float holidayPay)
    {
        this.eid = eid;
        this.overTime = overTime;
        this.performanceBonus = performanceBonus;
        this.holidayPay = holidayPay;
        calculateTotalIncentives();
    }
    
     /**
     * Calculates the total incentives from individual incentives
     */
    private void calculateTotalIncentives(){
        this.totalIncentives = this.overTimePay + this.performanceBonus + this.holidayPay;
    }
    
    // Getter methods

    public int getEid() {
        return eid;
    }

    public float getOverTimePay() {
        return overTimePay;
    }
    
    public float getOverTime() {
        return overTime;
    }

    public float getPerformanceBonus() {
        return performanceBonus;
    }

    public float getHolidayPay() {
        return holidayPay;
    }
    
    public float getTotalIncentives() {
        return totalIncentives;
    }
    
    // Setter methods 

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setOverTimePay(float overTimePay) {
        this.overTimePay = overTimePay;
    }
    
    public void setOverTime(float overTime) {
        this.overTime = overTime;
    }

    public void setPerformanceBonus(float performanceBonus) {
        this.performanceBonus = performanceBonus;
    }

    public void setHolidayPay(float holidayPay) {
        this.holidayPay = holidayPay;
    }
    
    public void setTotalIncentives(float totalIncentives) {
        this.totalIncentives = totalIncentives;
    }
   
}