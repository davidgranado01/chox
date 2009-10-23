package chox.model;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import scsbre.model.IHireMonitoringDetail;

public class HireMonitoringDetail extends AuditableEntity implements Serializable, IHireMonitoringDetail {

    /** 
     * This attribute maps to the column name_of_repairer in the hire_monitoring_detail table.
     */
    protected String nameOfRepairer;
    /** 
     * This attribute maps to the column repair_book_in_date in the hire_monitoring_detail table.
     */
    protected Date repairBookInDate;
    protected Date originalRepairBookInDate;

    /** 
     * This attribute maps to the column inspection_booked_date in the hire_monitoring_detail table.
     */
    protected Date inspectionBookedDate;
    /** 
     * This attribute maps to the column inspection_date in the hire_monitoring_detail table.
     */
    protected Date inspectionDate;
    /** 
     * This attribute maps to the column name_Of_ime in the hire_monitoring_detail table.
     */
    protected String nameOfIme;
    /** 
     * This attribute maps to the column repair_completion_date in the hire_monitoring_detail table.
     */
    protected Date repairCompletionDate;
    /** 
     * This attribute maps to the column total_loss_inspection_report in the hire_monitoring_detail table.
     */
    protected String totalLossInspectionReport;
    /** 
     * This attribute maps to the column is_total_lost_check in the hire_monitoring_detail table.
     */
    protected boolean isTotalLostCheck;
    /** 
     * This attribute maps to the column labour_cost in the hire_monitoring_detail table.
     */
    private BigDecimal labourRate;
    private Integer labourHour;
    private BigDecimal labourCost;
    private String nonProvisionReason;
    protected Date nextReviewDate;

    /**
     * Method 'HireMonitoringDetail'
     * 
     */
    public HireMonitoringDetail() {
    }

    /**
     * Method 'getNameOfRepairer'
     * 
     * @return java.lang.String
     */
    public java.lang.String getNameOfRepairer() {
        return nameOfRepairer;
    }

    /**
     * Method 'setNameOfRepairer'
     * 
     * @param nameOfRepairer
     */
    public void setNameOfRepairer(java.lang.String nameOfRepairer) {
        this.nameOfRepairer = nameOfRepairer;
    }

    /**
     * Method 'getRepairBookInDate'
     * 
     * @return java.util.Date
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getRepairBookInDate() {
        return repairBookInDate;
    }

    /**
     * Method 'setRepairBookInDate'
     * 
     * @param repairBookInDate
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public void setRepairBookInDate(java.util.Date repairBookInDate) {
        this.repairBookInDate = repairBookInDate;
    }

    /**
     * Method 'getInspectionBookedDate'
     * 
     * @return java.util.Date
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getInspectionBookedDate() {
        return inspectionBookedDate;
    }

    /**
     * Method 'setInspectionBookedDate'
     * 
     * @param inspectionBookedDate
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public void setInspectionBookedDate(java.util.Date inspectionBookedDate) {
        this.inspectionBookedDate = inspectionBookedDate;
    }

    /**
     * Method 'getInspectionDate'
     * 
     * @return java.util.Date
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * Method 'setInspectionDate'
     * 
     * @param inspectionDate
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public void setInspectionDate(java.util.Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * Method 'getNameOfIme'
     * 
     * @return java.lang.String
     */
    public java.lang.String getNameOfIme() {
        return nameOfIme;
    }

    /**
     * Method 'setNameOfIme'
     * 
     * @param nameOfIme
     */
    public void setNameOfIme(java.lang.String nameOfIme) {
        this.nameOfIme = nameOfIme;
    }

    /**
     * Method 'getRepairCompletionDate'
     * 
     * @return java.util.Date
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getRepairCompletionDate() {
        return repairCompletionDate;
    }

    /**
     * Method 'setRepairCompletionDate'
     * 
     * @param repairCompletionDate
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public void setRepairCompletionDate(java.util.Date repairCompletionDate) {
        this.repairCompletionDate = repairCompletionDate;
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getOriginalRepairBookInDate() {
        return originalRepairBookInDate;
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public void setOriginalRepairBookInDate(java.util.Date originalRepairBookInDate) {
        this.originalRepairBookInDate = originalRepairBookInDate;
    }

    /**
     * Method 'getTotalLossInspectionReport'
     * 
     * @return java.lang.String
     */
    public java.lang.String getTotalLossInspectionReport() {
        return totalLossInspectionReport;
    }

    /**
     * Method 'setTotalLossInspectionReport'
     * 
     * @param totalLossInspectionReport
     */
    public void setTotalLossInspectionReport(java.lang.String totalLossInspectionReport) {
        this.totalLossInspectionReport = totalLossInspectionReport;
    }

    /**
     * Method 'isIsTotalLostCheck'
     * 
     * @return boolean
     */
    public boolean isIsTotalLostCheck() {
        return isTotalLostCheck;
    }

    /**
     * Method 'setIsTotalLostCheck'
     * 
     * @param isTotalLostCheck
     */
    public void setIsTotalLostCheck(boolean isTotalLostCheck) {
        this.isTotalLostCheck = isTotalLostCheck;
    }

    public String getIsTotalLossDesc() {
        return isTotalLostCheck ? "Yes" : "No";
    }

    public BigDecimal getLabourRate() {
        return labourRate;
    }

    public void setLabourRate(BigDecimal labourRate) {
        this.labourRate = labourRate;
    }

    public Integer getLabourHour() {
        return labourHour;
    }

    public void setLabourHour(Integer labourHour) {
        this.labourHour = labourHour;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public String getNonProvisionReason() {
        return nonProvisionReason;
    }

    public void setNonProvisionReason(String nonProvisionReason) {
        this.nonProvisionReason = nonProvisionReason;
    }

@TypeConversion(converter = "chox.data.DateConverter")
    public Date getNextReviewDate() {
        return nextReviewDate;
    }

@TypeConversion(converter = "chox.data.DateConverter")
    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }
}
