package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



public class InsurerHireMonitoringDetail extends Entity implements Serializable {

    private Date inspectionBookedDate;
    private Date inspectionDate;
    private Date repairAuthorisedDate;
    private Date repairBookInDate;
    private Date repairCommencedDate;
    private Date repairCompletionDate;
    private Date totalLossOfferMadeDate;
    private Date totalLossOfferAcceptedDate;
    private Date totalLossOfferCheckIssuedDate;
    private Date totalLossOfferCheckReceivedDate;
    private Date engineersReportSentDate;

    private BigDecimal labourRate;
    private BigDecimal labourHour;
    private BigDecimal labourCost;
    private Boolean claimantImpecunious;
    private String whoManagedRepair;

    private Claim claim;

    
    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Date getEngineersReportSentDate() {
        return engineersReportSentDate;
    }

    public void setEngineersReportSentDate(Date engineersReportSentDate) {
        this.engineersReportSentDate = engineersReportSentDate;
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

    public Date getTotalLossOfferMadeDate() {
        return totalLossOfferMadeDate;
    }

    public void setTotalLossOfferMadeDate(Date totalLossOfferMadeDate) {
        this.totalLossOfferMadeDate = totalLossOfferMadeDate;
    }

    public Boolean getClaimantImpecunious() {
        return claimantImpecunious;
    }

    public String getClaimantImpecuniousDesc() {
        return claimantImpecunious == null ?  "" : (claimantImpecunious ? "Yes" : "No");
    }

    public void setClaimantImpecunious(Boolean claimantImpecunious) {
        this.claimantImpecunious = claimantImpecunious;
    }

    public String getWhoManagedRepair() {
        return whoManagedRepair;
    }

    public void setWhoManagedRepair(String whoManagedRepair) {
        this.whoManagedRepair = whoManagedRepair;
    }
}
