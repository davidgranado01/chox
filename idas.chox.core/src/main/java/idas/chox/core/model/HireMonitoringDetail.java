package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



public class HireMonitoringDetail extends Entity implements Serializable {

    /** 
     * This attribute maps to the column name_of_repairer in the hire_monitoring_detail table.
     */
    private String nameOfRepairer;
    /** 
     * This attribute maps to the column repair_book_in_date in the hire_monitoring_detail table.
     */
    private Date repairBookInDate;
    private Date repairAuthorisedDate;
    private Date repairCommencedDate;
    private Date originalRepairBookInDate;

    /** 
     * This attribute maps to the column inspection_booked_date in the hire_monitoring_detail table.
     */
    private Date inspectionBookedDate;
    /** 
     * This attribute maps to the column inspection_date in the hire_monitoring_detail table.
     */
    private Date inspectionDate;
    /** 
     * This attribute maps to the column name_Of_ime in the hire_monitoring_detail table.
     */
    private String nameOfIme;
    /** 
     * This attribute maps to the column repair_completion_date in the hire_monitoring_detail table.
     */
    private Date repairCompletionDate;
    /** 
     * This attribute maps to the column total_loss_inspection_report in the hire_monitoring_detail table.
     */
    private String totalLossInspectionReport;
    /** 
     * This attribute maps to the column is_total_lost_check in the hire_monitoring_detail table.
     */
    private boolean isTotalLostCheck;
    private boolean isRepairOnlyCheck;
    private boolean isNFInsurerManagingRepair;
    private Boolean clientVatRegistered;

    private Date totalLossOfferMadeDate;
    private Date totalLossOfferAcceptedDate;
    private Date totalLossOfferCheckIssuedDate;
    private Date totalLossOfferCheckReceivedDate;
    private Date engineersReportSentDate;

    /** 
     * This attribute maps to the column labour_cost in the hire_monitoring_detail table.
     */
//    @DecimalMax(value = "100000.00", message = "Labour Rate can not exceed maximum allowed limit.")
    private BigDecimal labourRate;
//    @DecimalMax(value = "100000.00", message = "Labour Hour can not exceed maximum allowed limit.")
    private BigDecimal labourHour;
//    @DecimalMax(value = "100000.00", message = "Labour Cost can not exceed maximum allowed limit.")
    private BigDecimal labourCost;
    
    private String nonProvisionReason;
    private Date nextReviewDate;

    private Date inspectionBookedDateLastModified;
    private Date inspectionDateLastModified;
    private Date repairAuthorisedDateLastModified;
    private Date repairBookInDateLastModified;
    private Date repairCommencedDateLastModified;
    private Date isTotalLostCheckLastModified;
    private Date totalLossOfferMadeLastModified;
    private Date totalLossOfferAcceptedLastModified;
    private Date totalLossCheckIssuedLastModified;
    private Date totalLossCheckReceivedLastModified;
    private Date engineersReportSentLastModified;
    private Date repairCompletionDateLastModified;
    private Date isRepairOnlyCheckLastModified;
    private Date isNFInsurerManagingRepairLastModified;
    private Date clientVatRegisteredLastModified;
    private boolean updateInsurer = false;
    private Claim claim;
    private String whoIsSendingPav;

    
    public Claim getClaim() {
        return claim;
    }

    public String getWhoIsSendingPav() {
        return whoIsSendingPav;
    }

    public void setWhoIsSendingPav(String whoIsSendingPav) {
        this.whoIsSendingPav = whoIsSendingPav;
    }

    
    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public boolean isUpdateInsurer() {
        return updateInsurer;
    }

    public void setUpdateInsurer(boolean updateInsurer) {
        this.updateInsurer = updateInsurer;
    }

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
    public java.util.Date getRepairBookInDate() {
        return repairBookInDate;
    }

    /**
     * Method 'setRepairBookInDate'
     * 
     * @param repairBookInDate
     */
    public void setRepairBookInDate(java.util.Date repairBookInDate) {
        this.repairBookInDate = repairBookInDate;
    }

    /**
     * Method 'getInspectionBookedDate'
     * 
     * @return java.util.Date
     */
    public java.util.Date getInspectionBookedDate() {
        return inspectionBookedDate;
    }

    /**
     * Method 'setInspectionBookedDate'
     * 
     * @param inspectionBookedDate
     */
    public void setInspectionBookedDate(java.util.Date inspectionBookedDate) {
        this.inspectionBookedDate = inspectionBookedDate;
       
    }

    /**
     * Method 'getInspectionDate'
     * 
     * @return java.util.Date
     */
    public java.util.Date getInspectionDate() {
        return inspectionDate;
    }

    /**
     * Method 'setInspectionDate'
     * 
     * @param inspectionDate
     */
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
    public java.util.Date getRepairCompletionDate() {
        return repairCompletionDate;
    }

    /**
     * Method 'setRepairCompletionDate'
     * 
     * @param repairCompletionDate
     */
    public void setRepairCompletionDate(java.util.Date repairCompletionDate) {
        this.repairCompletionDate = repairCompletionDate;
    }

    public java.util.Date getOriginalRepairBookInDate() {
        return originalRepairBookInDate;
    }

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

    public boolean isIsRepairOnlyCheck() {
        return isRepairOnlyCheck;
    }

    public void setIsRepairOnlyCheck(boolean isRepairOnlyCheck) {
        this.isRepairOnlyCheck = isRepairOnlyCheck;
    }

    public BigDecimal getLabourRate() {
        return labourRate;
    }

    public void setLabourRate(BigDecimal labourRate) {
        this.labourRate = labourRate;
    }

    public BigDecimal getLabourHour() {
        return labourHour;
    }

    public void setLabourHour(BigDecimal labourHour) {
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

    public Date getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(Date nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public Date getRepairAuthorisedDate() {
        return repairAuthorisedDate;
    }

    public void setRepairAuthorisedDate(Date repairAuthorisedDate) {
        this.repairAuthorisedDate = repairAuthorisedDate;
    }

    public Date getRepairCommencedDate() {
        return repairCommencedDate;
    }

    public void setRepairCommencedDate(Date repairCommencedDate) {
        this.repairCommencedDate = repairCommencedDate;
    }

    public Date getTotalLossOfferAcceptedDate() {
        return totalLossOfferAcceptedDate;
    }

    public void setTotalLossOfferAcceptedDate(Date totalLossOfferAcceptedDate) {
        this.totalLossOfferAcceptedDate = totalLossOfferAcceptedDate;
    }

    public Date getTotalLossOfferCheckIssuedDate() {
        return totalLossOfferCheckIssuedDate;
    }

    public void setTotalLossOfferCheckIssuedDate(Date totalLossOfferCheckIssuedDate) {
        this.totalLossOfferCheckIssuedDate = totalLossOfferCheckIssuedDate;
    }

    public Date getTotalLossOfferCheckReceivedDate() {
        return totalLossOfferCheckReceivedDate;
    }

    public void setTotalLossOfferCheckReceivedDate(Date totalLossOfferCheckReceivedDate) {
        this.totalLossOfferCheckReceivedDate = totalLossOfferCheckReceivedDate;
    }
        
    public Date getEngineersReportSentDate() {
        return engineersReportSentDate;
    }

    public void setEngineersReportSentDate(Date engineersReportSentDate) {
        this.engineersReportSentDate = engineersReportSentDate;
    }
    public Date getTotalLossOfferMadeDate() {
        return totalLossOfferMadeDate;
    }

    public void setTotalLossOfferMadeDate(Date totalLossOfferMadeDate) {
        this.totalLossOfferMadeDate = totalLossOfferMadeDate;
    }

    /**
     * @return the inspectionBookedDateLastModified
     */
    public Date getInspectionBookedDateLastModified() {
        return inspectionBookedDateLastModified;
    }

    /*
    public String getInspectionBookedDateLastModifiedStr() {
        return inspectionBookedDateLastModified;
    }

     *
     */
    /**
     * @param inspectionBookedDateLastModified the inspectionBookedDateLastModified to set
     */
    public void setInspectionBookedDateLastModified(Date inspectionBookedDateLastModified) {
        this.inspectionBookedDateLastModified = inspectionBookedDateLastModified;
    }

    /**
     * @return the inspectionDateLastModified
     */
    public Date getInspectionDateLastModified() {
        return inspectionDateLastModified;
    }

    /**
     * @param inspectionDateLastModified the inspectionDateLastModified to set
     */
    public void setInspectionDateLastModified(Date inspectionDateLastModified) {
        this.inspectionDateLastModified = inspectionDateLastModified;
    }

    /**
     * @return the repairAuthorisedDateLastModified
     */
    public Date getRepairAuthorisedDateLastModified() {
        return repairAuthorisedDateLastModified;
    }

    /**
     * @param repairAuthorisedDateLastModified the repairAuthorisedDateLastModified to set
     */
    public void setRepairAuthorisedDateLastModified(Date repairAuthorisedDateLastModified) {
        this.repairAuthorisedDateLastModified = repairAuthorisedDateLastModified;
    }

    /**
     * @return the repairBookInDateLastModified
     */
    public Date getRepairBookInDateLastModified() {
        return repairBookInDateLastModified;
    }

    /**
     * @param repairBookInDateLastModified the repairBookInDateLastModified to set
     */
    public void setRepairBookInDateLastModified(Date repairBookInDateLastModified) {
        this.repairBookInDateLastModified = repairBookInDateLastModified;
    }

    /**
     * @return the repairCommencedDateLastModified
     */
    public Date getRepairCommencedDateLastModified() {
        return repairCommencedDateLastModified;
    }

    /**
     * @param repairCommencedDateLastModified the repairCommencedDateLastModified to set
     */
    public void setRepairCommencedDateLastModified(Date repairCommencedDateLastModified) {
        this.repairCommencedDateLastModified = repairCommencedDateLastModified;
    }

    /**
     * @return the isTotalLostCheckLastModified
     */
    public Date getIsTotalLostCheckLastModified() {
        return isTotalLostCheckLastModified;
    }

    /**
     * @param isTotalLostCheckLastModified the isTotalLostCheckLastModified to set
     */
    public void setIsTotalLostCheckLastModified(Date isTotalLostCheckLastModified) {
        this.isTotalLostCheckLastModified = isTotalLostCheckLastModified;
    }

    /**
     * @return the totalLossOfferMadeLastModified
     */
    public Date getTotalLossOfferMadeLastModified() {
        return totalLossOfferMadeLastModified;
    }

    /**
     * @param totalLossOfferMadeLastModified the totalLossOfferMadeLastModified to set
     */
    public void setTotalLossOfferMadeLastModified(Date totalLossOfferMadeLastModified) {
        this.totalLossOfferMadeLastModified = totalLossOfferMadeLastModified;
    }

    /**
     * @return the totalLossOfferAcceptedLastModified
     */
    public Date getTotalLossOfferAcceptedLastModified() {
        return totalLossOfferAcceptedLastModified;
    }

    /**
     * @param totalLossOfferAcceptedLastModified the totalLossOfferAcceptedLastModified to set
     */
    public void setTotalLossOfferAcceptedLastModified(Date totalLossOfferAcceptedLastModified) {
        this.totalLossOfferAcceptedLastModified = totalLossOfferAcceptedLastModified;
    }

    /**
     * @return the totalLossCheckIssuedLastModified
     */
    public Date getTotalLossCheckIssuedLastModified() {
        return totalLossCheckIssuedLastModified;
    }

    /**
     * @param totalLossCheckIssuedLastModified the totalLossCheckIssuedLastModified to set
     */
    public void setTotalLossCheckIssuedLastModified(Date totalLossCheckIssuedLastModified) {
        this.totalLossCheckIssuedLastModified = totalLossCheckIssuedLastModified;
    }

    /**
     * @return the totalLossCheckReceivedLastModified
     */
    public Date getTotalLossCheckReceivedLastModified() {
        return totalLossCheckReceivedLastModified;
    }

    /**
     * @param totalLossCheckReceivedLastModified the totalLossCheckReceivedLastModified to set
     */
    public void setTotalLossCheckReceivedLastModified(Date totalLossCheckReceivedLastModified) {
        this.totalLossCheckReceivedLastModified = totalLossCheckReceivedLastModified;
    }


    public Date getEngineersReportSentLastModified() {
        return engineersReportSentLastModified;
    }

    public void setEngineersReportSentLastModified(Date engineersReportSentLastModified) {
        this.engineersReportSentLastModified = engineersReportSentLastModified;
    }
    
    /**
     * @return the repairCompletionDateLastModified
     */
    public Date getRepairCompletionDateLastModified() {
        return repairCompletionDateLastModified;
    }

    /**
     * @param repairCompletionDateLastModified the repairCompletionDateLastModified to set
     */
    public void setRepairCompletionDateLastModified(Date repairCompletionDateLastModified) {
        this.repairCompletionDateLastModified = repairCompletionDateLastModified;
    }

     public boolean isIsNFInsurerManagingRepair() {
        return isNFInsurerManagingRepair;
    }

    public void setIsNFInsurerManagingRepair(boolean isNFInsurerManagingRepair) {
        this.isNFInsurerManagingRepair = isNFInsurerManagingRepair;
    }

    /**
     * @return the isRepairOnlyCheckLastModified
     */
    public Date getIsRepairOnlyCheckLastModified() {
        return isRepairOnlyCheckLastModified;
    }

    /**
     * @param isRepairOnlyCheckLastModified the isRepairOnlyCheckLastModified to set
     */
    public void setIsRepairOnlyCheckLastModified(Date isRepairOnlyCheckLastModified) {
        this.isRepairOnlyCheckLastModified = isRepairOnlyCheckLastModified;
    }

    /**
     * @return the isNFInsurerManagingRepairLastModified
     */
    public Date getIsNFInsurerManagingRepairLastModified() {
        return isNFInsurerManagingRepairLastModified;
    }

    /**
     * @param isNFInsurerManagingRepairLastModified the isNFInsurerManagingRepairLastModified to set
     */
    public void setIsNFInsurerManagingRepairLastModified(Date isNFInsurerManagingRepairLastModified) {
        this.isNFInsurerManagingRepairLastModified = isNFInsurerManagingRepairLastModified;
    }

    public Boolean getClientVatRegistered() {
        return clientVatRegistered;
    }

    public String getClientVatRegisteredDesc() {
        if (clientVatRegistered == null) {
            return "";
        } else {
            return clientVatRegistered ? "Yes" : "No";
        }
    }

    public void setClientVatRegistered(Boolean clientVatRegistered) {
        this.clientVatRegistered = clientVatRegistered;
    }

    public Date getClientVatRegisteredLastModified() {
        return clientVatRegisteredLastModified;
    }

    public void setClientVatRegisteredLastModified(Date clientVatRegisteredLastModified) {
        this.clientVatRegisteredLastModified = clientVatRegisteredLastModified;
    }



  
}
