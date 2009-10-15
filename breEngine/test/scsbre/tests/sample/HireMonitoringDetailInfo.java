package scsbre.tests.sample;

import java.math.BigDecimal;
import java.util.Date;
import scsbre.model.IHireMonitoringDetail;

public class HireMonitoringDetailInfo implements IHireMonitoringDetail{

    protected String nameOfRepairer;
    protected Date repairBookInDate;
    protected Date inspectionBookedDate;
    protected Date inspectionDate;
    protected String nameOfIme;
    protected Date repairCompletionDate;
    protected String totalLossInspectionReport;
    protected boolean isTotalLostCheck;
    private BigDecimal labourRate;
    private Integer labourHour;
    private BigDecimal labourCost;
    private String nonProvisionReason;

    public HireMonitoringDetailInfo() {
    }

    public java.lang.String getNameOfRepairer() {
        return nameOfRepairer;
    }

    public void setNameOfRepairer(java.lang.String nameOfRepairer) {
        this.nameOfRepairer = nameOfRepairer;
    }

    public java.util.Date getRepairBookInDate() {
        return repairBookInDate;
    }

    public void setRepairBookInDate(java.util.Date repairBookInDate) {
        this.repairBookInDate = repairBookInDate;
    }

    public java.util.Date getInspectionBookedDate() {
        return inspectionBookedDate;
    }

    public void setInspectionBookedDate(java.util.Date inspectionBookedDate) {
        this.inspectionBookedDate = inspectionBookedDate;
    }

    public java.util.Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(java.util.Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public java.lang.String getNameOfIme() {
        return nameOfIme;
    }

    public void setNameOfIme(java.lang.String nameOfIme) {
        this.nameOfIme = nameOfIme;
    }

    public java.util.Date getRepairCompletionDate() {
        return repairCompletionDate;
    }

    public void setRepairCompletionDate(java.util.Date repairCompletionDate) {
        this.repairCompletionDate = repairCompletionDate;
    }

    public java.lang.String getTotalLossInspectionReport() {
        return totalLossInspectionReport;
    }

    public void setTotalLossInspectionReport(java.lang.String totalLossInspectionReport) {
        this.totalLossInspectionReport = totalLossInspectionReport;
    }

    public boolean isIsTotalLostCheck() {
        return isTotalLostCheck;
    }

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


}
