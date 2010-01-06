package scsbre.tests.sample;

import java.math.BigDecimal;
import java.util.Date;

import scsbre.model.IExtrasInfo;
import scsbre.model.IInvoiceInfo;

public class InvoiceInfo implements IInvoiceInfo, IExtrasInfo {


    protected Date dateInvoiced;
    protected BigDecimal hireNet;
    protected BigDecimal hireVat;
    protected BigDecimal hireGross;
    protected BigDecimal repairNet;
    protected BigDecimal repairVat;
    protected BigDecimal repairGross;
    protected BigDecimal engineerFeeNet;
    protected BigDecimal engineerFeeVat;
    protected BigDecimal engineerFeeGross;
    protected BigDecimal storageRecoveryNet;
    protected BigDecimal storageRecoveryVat;
    protected BigDecimal storageRecoveryGross;
    protected BigDecimal totalNet;
    protected BigDecimal totalVat;
    protected BigDecimal totalGross;
    protected BigDecimal claimsHandlingInvoiceAmount;
    protected BigDecimal deductionForClaimsHandlingFee;
    protected BigDecimal discount;
    protected BigDecimal totalToPay;
    protected String handlingInvoiceNo;
    protected String claimInvoiceNo;
    protected BigDecimal cdwFee;
    protected Integer cdwQty;
    protected BigDecimal automaticFee;
    protected Integer automaticQty;
    protected BigDecimal satNavFee;
    protected Integer satNavQty;
    protected BigDecimal estateFee;
    protected Integer estateQty;
    protected BigDecimal babySeatFee;
    protected Integer babySeatQty;
    protected BigDecimal towBarsFee;
    protected Integer towBarsQty;
    protected BigDecimal nonStandardInsurancePremiumFee;
    protected Integer nonStandardInsurancePremiumQty;
    protected BigDecimal adminFee;
    protected Integer adminQty;
    protected BigDecimal roofRackFee;
    protected Integer roofRackQty;
    protected BigDecimal dualControlFee;
    protected Integer dualControlQty;
    protected BigDecimal deliveryCollectionFee;
    protected Integer deliveryCollectionQty;
    protected boolean isPaymentMode;
    protected boolean isEngineerDecisionApproved;
    protected String engineerInvoiceReviewNotes;
    protected String rejectionReason;
    protected BigDecimal hireRateChargedPerDay;
    protected BigDecimal excessAmountCollected;
    protected BigDecimal vatAmountCollected;
    protected BigDecimal penaltyCharge;
    protected Integer penaltyAlertQty;
    protected Integer reasonOfRejectionId;
    protected Date penaltyChargeAppliedDate;
    protected BigDecimal originalTotalToPay;


    public java.util.Date getDateInvoiced() {
        return dateInvoiced;
    }

    public void setDateInvoiced(java.util.Date dateInvoiced) {
        this.dateInvoiced = dateInvoiced;
    }

    public java.math.BigDecimal getHireNet() {
        return hireNet;
    }

    public void setHireNet(java.math.BigDecimal hireNet) {
        this.hireNet = hireNet;
    }

    public java.math.BigDecimal getHireVat() {
        return hireVat;
    }

    public void setHireVat(java.math.BigDecimal hireVat) {
        this.hireVat = hireVat;
    }

    public java.math.BigDecimal getHireGross() {
        return hireGross;
    }

    public void setHireGross(java.math.BigDecimal hireGross) {
        this.hireGross = hireGross;
    }

    public java.math.BigDecimal getRepairNet() {
        return repairNet;
    }

    public void setRepairNet(java.math.BigDecimal repairNet) {
        this.repairNet = repairNet;
    }

    public java.math.BigDecimal getRepairVat() {
        return repairVat;
    }

    public void setRepairVat(java.math.BigDecimal repairVat) {
        this.repairVat = repairVat;
    }

    public java.math.BigDecimal getRepairGross() {
        return repairGross;
    }

    public void setRepairGross(java.math.BigDecimal repairGross) {
        this.repairGross = repairGross;
    }

    public java.math.BigDecimal getEngineerFeeNet() {
        return engineerFeeNet;
    }

    public void setEngineerFeeNet(java.math.BigDecimal engineerFeeNet) {
        this.engineerFeeNet = engineerFeeNet;
    }

    public java.math.BigDecimal getEngineerFeeVat() {
        return engineerFeeVat;
    }

    public void setEngineerFeeVat(java.math.BigDecimal engineerFeeVat) {
        this.engineerFeeVat = engineerFeeVat;
    }

    public java.math.BigDecimal getEngineerFeeGross() {
        return engineerFeeGross;
    }

    public void setEngineerFeeGross(java.math.BigDecimal engineerFeeGross) {
        this.engineerFeeGross = engineerFeeGross;
    }

    public java.math.BigDecimal getStorageRecoveryNet() {
        return storageRecoveryNet;
    }

    public void setStorageRecoveryNet(java.math.BigDecimal storageRecoveryNet) {
        this.storageRecoveryNet = storageRecoveryNet;
    }

    public java.math.BigDecimal getStorageRecoveryVat() {
        return storageRecoveryVat;
    }

    public void setStorageRecoveryVat(java.math.BigDecimal storageRecoveryVat) {
        this.storageRecoveryVat = storageRecoveryVat;
    }

    public java.math.BigDecimal getStorageRecoveryGross() {
        return storageRecoveryGross;
    }

    public void setStorageRecoveryGross(java.math.BigDecimal storageRecoveryGross) {
        this.storageRecoveryGross = storageRecoveryGross;
    }

    public java.math.BigDecimal getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(java.math.BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    public java.math.BigDecimal getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(java.math.BigDecimal totalVat) {
        this.totalVat = totalVat;
    }

    public java.math.BigDecimal getTotalGross() {
        return totalGross;
    }

    public void setTotalGross(java.math.BigDecimal totalGross) {
        this.totalGross = totalGross;
    }

    public java.math.BigDecimal getClaimsHandlingInvoiceAmount() {
        return claimsHandlingInvoiceAmount;
    }

    public void setClaimsHandlingInvoiceAmount(java.math.BigDecimal claimsHandlingInvoiceAmount) {
        this.claimsHandlingInvoiceAmount = claimsHandlingInvoiceAmount;
    }

    public java.math.BigDecimal getDeductionForClaimsHandlingFee() {
        return deductionForClaimsHandlingFee;
    }

    public void setDeductionForClaimsHandlingFee(java.math.BigDecimal deductionForClaimsHandlingFee) {
        this.deductionForClaimsHandlingFee = deductionForClaimsHandlingFee;
    }

    public java.math.BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }

    public java.math.BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(java.math.BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    public java.lang.String getHandlingInvoiceNo() {
        return handlingInvoiceNo;
    }

    public void setHandlingInvoiceNo(java.lang.String handlingInvoiceNo) {
        this.handlingInvoiceNo = handlingInvoiceNo;
    }

    public java.lang.String getClaimInvoiceNo() {
        return claimInvoiceNo;
    }

    public void setClaimInvoiceNo(java.lang.String claimInvoiceNo) {
        this.claimInvoiceNo = claimInvoiceNo;
    }

    public java.math.BigDecimal getCdwFee() {
        return cdwFee;
    }

    public void setCdwFee(java.math.BigDecimal cdwFee) {
        this.cdwFee = cdwFee;
    }

    public int getCdwQty() {
        return cdwQty;
    }

    public void setCdwQty(java.lang.Integer cdwQty) {
        this.cdwQty = cdwQty;
    }

    public java.math.BigDecimal getAutomaticFee() {
        return automaticFee;
    }

    public void setAutomaticFee(java.math.BigDecimal automaticFee) {
        this.automaticFee = automaticFee;
    }

    public int getAutomaticQty() {
        return automaticQty;
    }

    public void setAutomaticQty(java.lang.Integer automaticQty) {
        this.automaticQty = automaticQty;
    }

    public java.math.BigDecimal getSatNavFee() {
        return satNavFee;
    }

    public void setSatNavFee(java.math.BigDecimal satNavFee) {
        this.satNavFee = satNavFee;
    }

    /**
     * Method 'getSatNavQty'
     *
     * @return java.lang.Integer
     */
    public int getSatNavQty() {
        return satNavQty;
    }

    /**
     * Method 'setSatNavQty'
     *
     * @param satNavQty
     */
    public void setSatNavQty(java.lang.Integer satNavQty) {
        this.satNavQty = satNavQty;
    }

    /**
     * Method 'getEstateFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getEstateFee() {
        return estateFee;
    }

    /**
     * Method 'setEstateFee'
     *
     * @param estateFee
     */
    public void setEstateFee(java.math.BigDecimal estateFee) {
        this.estateFee = estateFee;
    }

    /**
     * Method 'getEstateQty'
     *
     * @return java.lang.Integer
     */
    public int getEstateQty() {
        return estateQty;
    }

    /**
     * Method 'setEstateQty'
     *
     * @param estateQty
     */
    public void setEstateQty(java.lang.Integer estateQty) {
        this.estateQty = estateQty;
    }

    /**
     * Method 'getBabySeatFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getBabySeatFee() {
        return babySeatFee;
    }

    /**
     * Method 'setBabySeatFee'
     *
     * @param babySeatFee
     */
    public void setBabySeatFee(java.math.BigDecimal babySeatFee) {
        this.babySeatFee = babySeatFee;
    }

    /**
     * Method 'getBabySeatQty'
     *
     * @return java.lang.Integer
     */
    public int getBabySeatQty() {
        return babySeatQty;
    }

    /**
     * Method 'setBabySeatQty'
     *
     * @param babySeatQty
     */
    public void setBabySeatQty(java.lang.Integer babySeatQty) {
        this.babySeatQty = babySeatQty;
    }

    /**
     * Method 'getTowBarsFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTowBarsFee() {
        return towBarsFee;
    }

    /**
     * Method 'setTowBarsFee'
     *
     * @param towBarsFee
     */
    public void setTowBarsFee(java.math.BigDecimal towBarsFee) {
        this.towBarsFee = towBarsFee;
    }

    /**
     * Method 'getTowBarsQty'
     *
     * @return java.lang.Integer
     */
    public int getTowBarsQty() {
        return towBarsQty;
    }

    /**
     * Method 'setTowBarsQty'
     *
     * @param towBarsQty
     */
    public void setTowBarsQty(java.lang.Integer towBarsQty) {
        this.towBarsQty = towBarsQty;
    }

    /**
     * Method 'getNonStandardInsurancePremiumFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }

    /**
     * Method 'setNonStandardInsurancePremiumFee'
     *
     * @param nonStandardInsurancePremiumFee
     */
    public void setNonStandardInsurancePremiumFee(java.math.BigDecimal nonStandardInsurancePremiumFee) {
        this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
    }

    /**
     * Method 'getNonStandardInsurancePremiumQty'
     *
     * @return java.lang.Integer
     */
    public int getNonStandardInsurancePremiumQty() {
        return nonStandardInsurancePremiumQty;
    }

    /**
     * Method 'setNonStandardInsurancePremiumQty'
     *
     * @param nonStandardInsurancePremiumQty
     */
    public void setNonStandardInsurancePremiumQty(java.lang.Integer nonStandardInsurancePremiumQty) {
        this.nonStandardInsurancePremiumQty = nonStandardInsurancePremiumQty;
    }

    public java.math.BigDecimal getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(java.math.BigDecimal adminFee) {
        this.adminFee = adminFee;
    }

    /**
     * Method 'getAdminQty'
     *
     * @return java.lang.Integer
     */
    public int getAdminQty() {
        return adminQty;
    }

    /**
     * Method 'setAdminQty'
     *
     * @param adminQty
     */
    public void setAdminQty(java.lang.Integer adminQty) {
        this.adminQty = adminQty;
    }

    /**
     * Method 'getRoofRackFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getRoofRackFee() {
        return roofRackFee;
    }

    /**
     * Method 'setRoofRackFee'
     *
     * @param roofRackFee
     */
    public void setRoofRackFee(java.math.BigDecimal roofRackFee) {
        this.roofRackFee = roofRackFee;
    }

    /**
     * Method 'getRoofRackQty'
     *
     * @return java.lang.Integer
     */
    public int getRoofRackQty() {
        return roofRackQty;
    }

    /**
     * Method 'setRoofRackQty'
     *
     * @param roofRackQty
     */
    public void setRoofRackQty(java.lang.Integer roofRackQty) {
        this.roofRackQty = roofRackQty;
    }

    /**
     * Method 'getDualControlFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getDualControlFee() {
        return dualControlFee;
    }

    /**
     * Method 'setDualControlFee'
     *
     * @param dualControlFee
     */
    public void setDualControlFee(java.math.BigDecimal dualControlFee) {
        this.dualControlFee = dualControlFee;
    }

    /**
     * Method 'getDualControlQty'
     *
     * @return java.lang.Integer
     */
    public int getDualControlQty() {
        return dualControlQty;
    }

    /**
     * Method 'setDualControlQty'
     *
     * @param dualControlQty
     */
    public void setDualControlQty(java.lang.Integer dualControlQty) {
        this.dualControlQty = dualControlQty;
    }

    /**
     * Method 'getDeliveryCollectionFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getDeliveryCollectionFee() {
        return deliveryCollectionFee;
    }

    /**
     * Method 'setDeliveryCollectionFee'
     *
     * @param deliveryCollectionFee
     */
    public void setDeliveryCollectionFee(java.math.BigDecimal deliveryCollectionFee) {
        this.deliveryCollectionFee = deliveryCollectionFee;
    }

    /**
     * Method 'getDeliveryCollectionQty'
     *
     * @return java.lang.Integer
     */
    public int getDeliveryCollectionQty() {
        return deliveryCollectionQty;
    }

    /**
     * Method 'setDeliveryCollectionQty'
     *
     * @param deliveryCollectionQty
     */
    public void setDeliveryCollectionQty(java.lang.Integer deliveryCollectionQty) {
        this.deliveryCollectionQty = deliveryCollectionQty;
    }

    public String getEngineerInvoiceReviewNotes() {
        return engineerInvoiceReviewNotes;
    }

    public void setEngineerInvoiceReviewNotes(String engineerInvoiceReviewNotes) {
        this.engineerInvoiceReviewNotes = engineerInvoiceReviewNotes;
    }

    public boolean isIsEngineerDecisionApproved() {
        return isEngineerDecisionApproved;
    }

    public void setIsEngineerDecisionApproved(boolean isEngineerDecisionApproved) {
        this.isEngineerDecisionApproved = isEngineerDecisionApproved;
    }

    public boolean isIsPaymentMode() {
        return isPaymentMode;
    }

    public void setIsPaymentMode(boolean isPaymentMode) {
        this.isPaymentMode = isPaymentMode;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public BigDecimal getHireRateChargedPerDay() {
        return hireRateChargedPerDay;
    }

    public void setHireRateChargedPerDay(BigDecimal hireRateChargedPerDay) {
        this.hireRateChargedPerDay = hireRateChargedPerDay;
    }

    public BigDecimal getExcessAmountCollected() {
        return excessAmountCollected;
    }

    public void setExcessAmountCollected(BigDecimal excessAmountCollected) {
        this.excessAmountCollected = excessAmountCollected;
    }

    public BigDecimal getVatAmountCollected() {
        return vatAmountCollected;
    }

    public void setVatAmountCollected(BigDecimal vatAmountCollected) {
        this.vatAmountCollected = vatAmountCollected;
    }

    public BigDecimal getPenaltyCharge() {
        return penaltyCharge == null ? BigDecimal.ZERO : penaltyCharge;
    }

    public void setPenaltyCharge(BigDecimal penaltyCharge) {
        this.penaltyCharge = penaltyCharge == null ? BigDecimal.ZERO : penaltyCharge;
    }

    public Integer getPenaltyAlertQty() {
        return penaltyAlertQty == null ? 0 : penaltyAlertQty;
    }

    public void setPenaltyAlertQty(Integer penaltyAlertQty) {
        this.penaltyAlertQty = penaltyAlertQty == null ? 0 : penaltyAlertQty;
    }

    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    public Date getPenaltyChargeAppliedDate() {
        return penaltyChargeAppliedDate;
    }

    public void setPenaltyChargeAppliedDate(Date penaltyChargeAppliedDate) {
        this.penaltyChargeAppliedDate = penaltyChargeAppliedDate;
    }

    public BigDecimal getOriginalTotalToPay() {
        return originalTotalToPay;
    }

    public void setOriginalTotalToPay(BigDecimal originalTotalToPay) {
        this.originalTotalToPay = originalTotalToPay;
    }

}
