package chox.model;

import chox.Util.DateHelper;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import scsbre.model.IExtrasInfo;
import scsbre.model.IInvoiceInfo;

public class Invoice extends AuditableEntity implements Serializable, IInvoiceInfo, IExtrasInfo {

    /**
     * This attribute maps to the column date_invoiced in the invoice table.
     */
    protected Date dateInvoiced;
    /**
     * This attribute maps to the column hire_net in the invoice table.
     */
    protected BigDecimal hireNet;
    /**
     * This attribute maps to the column hire_vat in the invoice table.
     */
    protected BigDecimal hireVat;
    /**
     * This attribute maps to the column hire_gross in the invoice table.
     */
    protected BigDecimal hireGross;
    /**
     * This attribute maps to the column repair_net in the invoice table.
     */
    protected BigDecimal repairNet;
    /**
     * This attribute maps to the column repair_vat in the invoice table.
     */
    protected BigDecimal repairVat;
    /**
     * This attribute maps to the column repair_gross in the invoice table.
     */
    protected BigDecimal repairGross;
    /**
     * This attribute maps to the column engineer_fee_net in the invoice table.
     */
    protected BigDecimal engineerFeeNet;
    /**
     * This attribute maps to the column engineer_fee_vat in the invoice table.
     */
    protected BigDecimal engineerFeeVat;
    /**
     * This attribute maps to the column engineer_fee_gross in the invoice table.
     */
    protected BigDecimal engineerFeeGross;
    /**
     * This attribute maps to the column storage_recovery_net in the invoice table.
     */
    protected BigDecimal storageRecoveryNet;
    /**
     * This attribute maps to the column storage_recovery_vat in the invoice table.
     */
    protected BigDecimal storageRecoveryVat;
    /**
     * This attribute maps to the column storage_recovery_gross in the invoice table.
     */
    protected BigDecimal storageRecoveryGross;
    /**
     * This attribute maps to the column total_net in the invoice table.
     */
    protected BigDecimal totalNet;
    /**
     * This attribute maps to the column total_vat in the invoice table.
     */
    protected BigDecimal totalVat;
    /**
     * This attribute maps to the column total_gross in the invoice table.
     */
    protected BigDecimal totalGross;
    /**
     * This attribute maps to the column claims_handling_invoice_amount in the invoice table.
     */
    protected BigDecimal claimsHandlingInvoiceAmount;
    /**
     * This attribute maps to the column deduction_for_claims_handling_fee in the invoice table.
     */
    protected BigDecimal deductionForClaimsHandlingFee;
    /**
     * This attribute maps to the column discount in the invoice table.
     */
    protected BigDecimal discount;
    /**
     * This attribute maps to the column total_to_pay in the invoice table.
     */
    protected BigDecimal totalToPay;
    /**
     * This attribute maps to the column handling_invoice_no in the invoice table.
     */
    protected String handlingInvoiceNo;
    /**
     * This attribute maps to the column claim_invoice_no in the invoice table.
     */
    protected String claimInvoiceNo;
    /**
     * This attribute maps to the column cdw_fee in the invoice table.
     */
    protected BigDecimal cdwFee;
    /**
     * This attribute maps to the column cdw_qty in the invoice table.
     */
    protected Integer cdwQty;
    /**
     * This attribute maps to the column automatic_fee in the invoice table.
     */
    protected BigDecimal automaticFee;
    /**
     * This attribute maps to the column automatic_qty in the invoice table.
     */
    protected Integer automaticQty;
    /**
     * This attribute maps to the column sat_nav_fee in the invoice table.
     */
    protected BigDecimal satNavFee;
    /**
     * This attribute maps to the column sat_nav_qty in the invoice table.
     */
    protected Integer satNavQty;
    /**
     * This attribute maps to the column estate_fee in the invoice table.
     */
    protected BigDecimal estateFee;
    /**
     * This attribute maps to the column estate_qty in the invoice table.
     */
    protected Integer estateQty;
    /**
     * This attribute maps to the column baby_seat_fee in the invoice table.
     */
    protected BigDecimal babySeatFee;
    /**
     * This attribute maps to the column baby_seat_qty in the invoice table.
     */
    protected Integer babySeatQty;
    /**
     * This attribute maps to the column tow_bars_fee in the invoice table.
     */
    protected BigDecimal towBarsFee;
    /**
     * This attribute maps to the column tow_bars_qty in the invoice table.
     */
    protected Integer towBarsQty;
    /**
     * This attribute maps to the column non_standard_insurance_premium_fee in the invoice table.
     */
    protected BigDecimal nonStandardInsurancePremiumFee;
    /**
     * This attribute maps to the column non_standard_insurance_premium_qty in the invoice table.
     */
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
    protected ReasonOfRejection reasonOfRejection;
    protected Date penaltyChargeAppliedDate;
    protected BigDecimal originalTotalToPay;

    public Invoice() {
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getDateInvoiced() {
        return dateInvoiced;
    }

    /**
     * Method 'setDateInvoiced'
     *
     * @param dateInvoiced
     */
    @TypeConversion(converter = "chox.data.DateConverter")
    public void setDateInvoiced(java.util.Date dateInvoiced) {
        this.dateInvoiced = dateInvoiced;
    }

    /**
     * Method 'getHireNet'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getHireNet() {
        return hireNet;
    }

    /**
     * Method 'setHireNet'
     *
     * @param hireNet
     */
    public void setHireNet(java.math.BigDecimal hireNet) {
        this.hireNet = hireNet;
    }

    /**
     * Method 'getHireVat'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getHireVat() {
        return hireVat;
    }

    /**
     * Method 'setHireVat'
     *
     * @param hireVat
     */
    public void setHireVat(java.math.BigDecimal hireVat) {
        this.hireVat = hireVat;
    }

    /**
     * Method 'getHireGross'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getHireGross() {
        return hireGross;
    }

    /**
     * Method 'setHireGross'
     *
     * @param hireGross
     */
    public void setHireGross(java.math.BigDecimal hireGross) {
        this.hireGross = hireGross;
    }

    /**
     * Method 'getRepairNet'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getRepairNet() {
        return repairNet;
    }

    /**
     * Method 'setRepairNet'
     *
     * @param repairNet
     */
    public void setRepairNet(java.math.BigDecimal repairNet) {
        this.repairNet = repairNet;
    }

    /**
     * Method 'getRepairVat'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getRepairVat() {
        return repairVat;
    }

    /**
     * Method 'setRepairVat'
     *
     * @param repairVat
     */
    public void setRepairVat(java.math.BigDecimal repairVat) {
        this.repairVat = repairVat;
    }

    /**
     * Method 'getRepairGross'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getRepairGross() {
        return repairGross;
    }

    /**
     * Method 'setRepairGross'
     *
     * @param repairGross
     */
    public void setRepairGross(java.math.BigDecimal repairGross) {
        this.repairGross = repairGross;
    }

    /**
     * Method 'getEngineerFeeNet'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getEngineerFeeNet() {
        return engineerFeeNet;
    }

    /**
     * Method 'setEngineerFeeNet'
     *
     * @param engineerFeeNet
     */
    public void setEngineerFeeNet(java.math.BigDecimal engineerFeeNet) {
        this.engineerFeeNet = engineerFeeNet;
    }

    /**
     * Method 'getEngineerFeeVat'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getEngineerFeeVat() {
        return engineerFeeVat;
    }

    /**
     * Method 'setEngineerFeeVat'
     *
     * @param engineerFeeVat
     */
    public void setEngineerFeeVat(java.math.BigDecimal engineerFeeVat) {
        this.engineerFeeVat = engineerFeeVat;
    }

    /**
     * Method 'getEngineerFeeGross'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getEngineerFeeGross() {
        return engineerFeeGross;
    }

    /**
     * Method 'setEngineerFeeGross'
     *
     * @param engineerFeeGross
     */
    public void setEngineerFeeGross(java.math.BigDecimal engineerFeeGross) {
        this.engineerFeeGross = engineerFeeGross;
    }

    /**
     * Method 'getStorageRecoveryNet'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getStorageRecoveryNet() {
        return storageRecoveryNet;
    }

    /**
     * Method 'setStorageRecoveryNet'
     *
     * @param storageRecoveryNet
     */
    public void setStorageRecoveryNet(java.math.BigDecimal storageRecoveryNet) {
        this.storageRecoveryNet = storageRecoveryNet;
    }

    /**
     * Method 'getStorageRecoveryVat'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getStorageRecoveryVat() {
        return storageRecoveryVat;
    }

    /**
     * Method 'setStorageRecoveryVat'
     *
     * @param storageRecoveryVat
     */
    public void setStorageRecoveryVat(java.math.BigDecimal storageRecoveryVat) {
        this.storageRecoveryVat = storageRecoveryVat;
    }

    /**
     * Method 'getStorageRecoveryGross'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getStorageRecoveryGross() {
        return storageRecoveryGross;
    }

    /**
     * Method 'setStorageRecoveryGross'
     *
     * @param storageRecoveryGross
     */
    public void setStorageRecoveryGross(java.math.BigDecimal storageRecoveryGross) {
        this.storageRecoveryGross = storageRecoveryGross;
    }

    /**
     * Method 'getTotalNet'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotalNet() {
        return totalNet;
    }

    /**
     * Method 'setTotalNet'
     *
     * @param totalNet
     */
    public void setTotalNet(java.math.BigDecimal totalNet) {
        this.totalNet = totalNet;
    }

    /**
     * Method 'getTotalVat'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotalVat() {
        return totalVat;
    }

    /**
     * Method 'setTotalVat'
     *
     * @param totalVat
     */
    public void setTotalVat(java.math.BigDecimal totalVat) {
        this.totalVat = totalVat;
    }

    /**
     * Method 'getTotalGross'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotalGross() {
        return totalGross;
    }

    /**
     * Method 'setTotalGross'
     *
     * @param totalGross
     */
    public void setTotalGross(java.math.BigDecimal totalGross) {
        this.totalGross = totalGross;
    }

    /**
     * Method 'getClaimsHandlingInvoiceAmount'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getClaimsHandlingInvoiceAmount() {
        return claimsHandlingInvoiceAmount;
    }

    /**
     * Method 'setClaimsHandlingInvoiceAmount'
     *
     * @param claimsHandlingInvoiceAmount
     */
    public void setClaimsHandlingInvoiceAmount(java.math.BigDecimal claimsHandlingInvoiceAmount) {
        this.claimsHandlingInvoiceAmount = claimsHandlingInvoiceAmount;
    }

    /**
     * Method 'getDeductionForClaimsHandlingFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getDeductionForClaimsHandlingFee() {
        return deductionForClaimsHandlingFee;
    }

    /**
     * Method 'setDeductionForClaimsHandlingFee'
     *
     * @param deductionForClaimsHandlingFee
     */
    public void setDeductionForClaimsHandlingFee(java.math.BigDecimal deductionForClaimsHandlingFee) {
        this.deductionForClaimsHandlingFee = deductionForClaimsHandlingFee;
    }

    /**
     * Method 'getDiscount'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getDiscount() {
        return discount;
    }

    /**
     * Method 'setDiscount'
     *
     * @param discount
     */
    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * Method 'getTotalToPay'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotalToPay() {
        return totalToPay;
    }

    /**
     * Method 'setTotalToPay'
     *
     * @param totalToPay
     */
    public void setTotalToPay(java.math.BigDecimal totalToPay) {
        this.totalToPay = totalToPay;
    }

    /**
     * Method 'getHandlingInvoiceNo'
     *
     * @return java.lang.String
     */
    public java.lang.String getHandlingInvoiceNo() {
        return handlingInvoiceNo;
    }

    /**
     * Method 'setHandlingInvoiceNo'
     *
     * @param handlingInvoiceNo
     */
    public void setHandlingInvoiceNo(java.lang.String handlingInvoiceNo) {
        this.handlingInvoiceNo = handlingInvoiceNo;
    }

    /**
     * Method 'getClaimInvoiceNo'
     *
     * @return java.lang.String
     */
    public java.lang.String getClaimInvoiceNo() {
        return claimInvoiceNo;
    }

    /**
     * Method 'setClaimInvoiceNo'
     *
     * @param claimInvoiceNo
     */
    public void setClaimInvoiceNo(java.lang.String claimInvoiceNo) {
        this.claimInvoiceNo = claimInvoiceNo;
    }

    /**
     * Method 'getCdwFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getCdwFee() {
        return cdwFee;
    }

    /**
     * Method 'setCdwFee'
     *
     * @param cdwFee
     */
    public void setCdwFee(java.math.BigDecimal cdwFee) {
        this.cdwFee = cdwFee;
    }

    /**
     * Method 'getCdwQty'
     *
     * @return java.lang.Integer
     */
    public int getCdwQty() {
        return cdwQty;
    }

    /**
     * Method 'setCdwQty'
     *
     * @param cdwQty
     */
    public void setCdwQty(java.lang.Integer cdwQty) {
        this.cdwQty = cdwQty;
    }

    /**
     * Method 'getAutomaticFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getAutomaticFee() {
        return automaticFee;
    }

    /**
     * Method 'setAutomaticFee'
     *
     * @param automaticFee
     */
    public void setAutomaticFee(java.math.BigDecimal automaticFee) {
        this.automaticFee = automaticFee;
    }

    /**
     * Method 'getAutomaticQty'
     *
     * @return java.lang.Integer
     */
    public int getAutomaticQty() {
        return automaticQty;
    }

    /**
     * Method 'setAutomaticQty'
     *
     * @param automaticQty
     */
    public void setAutomaticQty(java.lang.Integer automaticQty) {
        this.automaticQty = automaticQty;
    }

    /**
     * Method 'getSatNavFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getSatNavFee() {
        return satNavFee;
    }

    /**
     * Method 'setSatNavFee'
     *
     * @param satNavFee
     */
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

    /**
     * Method 'getAdminFee'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getAdminFee() {
        return adminFee;
    }

    /**
     * Method 'setAdminFee'
     *
     * @param adminFee
     */
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

    public long getInvoicedDays() {
        // long dateDiff = DateHelper.daysBetween(getDateInvoiced(), new Date()) + 1;
        long dateDiff = DateHelper.daysBetween(getCreatedDate(), new Date()) + 1;
        return dateDiff;
    }

    public ReasonOfRejection getReasonOfRejection() {
        return reasonOfRejection;
    }

    public void setReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        this.reasonOfRejection = reasonOfRejection;
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
