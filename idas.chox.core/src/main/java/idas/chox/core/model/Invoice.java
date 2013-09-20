package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DateHelper;

public class Invoice extends Entity implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Invoice.class);
    /**
     * This attribute maps to the column date_invoiced in the invoice table.
     */
    private Date dateInvoiced;
    /**
     * This attribute maps to the column hire_net in the invoice table.
     */
    private BigDecimal hireNet;
    /**
     * This attribute maps to the column hire_vat in the invoice table.
     */
    private BigDecimal hireVat;
    /**
     * This attribute maps to the column hire_gross in the invoice table.
     */
    private BigDecimal hireGross;
    /**
     * This attribute maps to the column repair_net in the invoice table.
     */
    private BigDecimal repairNet;
    /**
     * This attribute maps to the column repair_vat in the invoice table.
     */
    private BigDecimal repairVat;
    /**
     * This attribute maps to the column repair_gross in the invoice table.
     */
    private BigDecimal repairGross;
    /**
     * This attribute maps to the column engineer_fee_net in the invoice table.
     */
    private BigDecimal engineerFeeNet;
    /**
     * This attribute maps to the column engineer_fee_vat in the invoice table.
     */
    private BigDecimal engineerFeeVat;
    /**
     * This attribute maps to the column engineer_fee_gross in the invoice
     * table.
     */
    private BigDecimal engineerFeeGross;
    /**
     * This attribute maps to the column storage_recovery_net in the invoice
     * table.
     */
    private BigDecimal storageRecoveryNet;
    /**
     * This attribute maps to the column storage_recovery_vat in the invoice
     * table.
     */
    private BigDecimal storageRecoveryVat;
    /**
     * This attribute maps to the column storage_recovery_gross in the invoice
     * table.
     */
    private BigDecimal storageRecoveryGross;
    /**
     * This attribute maps to the column total_net in the invoice table.
     */
    private BigDecimal totalNet;
    /**
     * This attribute maps to the column total_vat in the invoice table.
     */
    private BigDecimal totalVat;
    /**
     * This attribute maps to the column total_gross in the invoice table.
     */
    private BigDecimal totalGross;
    /**
     * This attribute maps to the column claims_handling_invoice_amount in the
     * invoice table.
     */
    private BigDecimal claimsHandlingInvoiceAmount;
    /**
     * This attribute maps to the column deduction_for_claims_handling_fee in
     * the invoice table.
     */
    private BigDecimal deductionForClaimsHandlingFee;
    /**
     * This attribute maps to the column discount in the invoice table.
     */
    private BigDecimal discount;
    private BigDecimal insurerDiscount;
    
    private BigDecimal totalGrossInsurerDiscount;
    
    private BigDecimal repairGrossInsurerDiscount;
    
    private BigDecimal hireGrossInsurerDiscount;
    /**
     * This attribute maps to the column total_to_pay in the invoice table.
     */
    private BigDecimal fullTotalToPay;
    /**
     * This attribute maps to the column handling_invoice_no in the invoice
     * table.
     */
    private String handlingInvoiceNo;
    /**
     * This attribute maps to the column claim_invoice_no in the invoice table.
     */
    private String claimInvoiceNo;
    /**
     * This attribute maps to the column cdw_fee in the invoice table.
     */
    private BigDecimal miscellaneousFee;
    /**
     * This attribute maps to the column cdw_qty in the invoice table.
     */
    private Integer miscellaneousQty;
    /**
     * This attribute maps to the column automatic_fee in the invoice table.
     */
    private BigDecimal automaticFee;
    /**
     * This attribute maps to the column automatic_qty in the invoice table.
     */
    private Integer automaticQty;
    /**
     * This attribute maps to the column additional_driver_fee in the invoice
     * table.
     */
    private BigDecimal additionalDriverFee;
    /**
     * This attribute maps to the column additional_driver_qty in the invoice
     * table.
     */
    private Integer additionalDriverQty;
    /**
     * This attribute maps to the column sat_nav_fee in the invoice table.
     */
    private BigDecimal satNavFee;
    /**
     * This attribute maps to the column sat_nav_qty in the invoice table.
     */
    private Integer satNavQty;
    /**
     * This attribute maps to the column estate_fee in the invoice table.
     */
    private BigDecimal estateFee;
    /**
     * This attribute maps to the column estate_qty in the invoice table.
     */
    private Integer estateQty;
    /**
     * This attribute maps to the column baby_seat_fee in the invoice table.
     */
    private BigDecimal babySeatFee;
    /**
     * This attribute maps to the column baby_seat_qty in the invoice table.
     */
    private Integer babySeatQty;
    /**
     * This attribute maps to the column tow_bars_fee in the invoice table.
     */
    private BigDecimal towBarsFee;
    /**
     * This attribute maps to the column tow_bars_qty in the invoice table.
     */
    private Integer towBarsQty;
    /**
     * This attribute maps to the column non_standard_insurance_premium_fee in
     * the invoice table.
     */
    private BigDecimal nonStandardInsurancePremiumFee;
    /**
     * This attribute maps to the column non_standard_insurance_premium_qty in
     * the invoice table.
     */
    private Integer nonStandardInsurancePremiumQty;
    private Boolean coverNoteRequired;
    private BigDecimal adminFee;
    private Integer adminQty;
    private BigDecimal roofRackFee;
    private Integer roofRackQty;
    private BigDecimal dualControlFee;
    private Integer dualControlQty;
    private BigDecimal deliveryCollectionFee;
    private Integer deliveryCollectionQty;
    private boolean isPaymentMode;
    private boolean isEngineerDecisionApproved;
    private String engineerInvoiceReviewNotes;
    private BigDecimal hireRateChargedPerDay;
    private BigDecimal excessAmountCollected;
    private BigDecimal vatAmountCollected;
    private BigDecimal hirePenaltyCharge;
    private String hirePenaltyPercentage;
    private Date hirePenaltyChargeAppliedDate;
    private BigDecimal repairPenaltyCharge;
    private String repairPenaltyPercentage;
    private Date repairPenaltyChargeAppliedDate;
    private BigDecimal totalPenaltyCharge;
    private ReasonOfRejection reasonOfRejection;
    private BigDecimal totalToPay;
    private BigDecimal totalLossFeeNet;
    private BigDecimal totalLossFeeVat;
    private BigDecimal totalLossFeeGross;
    private BigDecimal interimPaymentMade;
    private BigDecimal interimPaymentReceived = BigDecimal.ZERO;
    private Boolean interimPaymentReceivedFullAndFinal;
    private BigDecimal hireGrossPaid;
    private BigDecimal repairGrossPaid;
    private BigDecimal engineerFeeGrossPaid;
    private BigDecimal totalLossFeeGrossPaid;
    private BigDecimal storageRecoveryGrossPaid;
    private BigDecimal hirePenaltyChargePaid;
    private BigDecimal repairPenaltyChargePaid;
    private BigDecimal claimHandlerChargePaid;
    private BigDecimal deductionClaimHandlerFeePaid;
    private BigDecimal choDiscountFeePaid;
    private BigDecimal insurerDiscountFeePaid;
    private BigDecimal finalPayment;
    private boolean penaltyChargesPaid;
    private Date autoPenaltyStart;
    private InvoiceOriginal invoiceOriginal;
    private BigDecimal hireInsurerDiscountCalculated = BigDecimal.ZERO;
    private BigDecimal repairInsurerDiscountCalculated = BigDecimal.ZERO;
    private BigDecimal totalInsurerDiscountCalculated = BigDecimal.ZERO;
    private int penaltyBand;
    private BigDecimal repairAdminFee;
    private BigDecimal repairAcquisitionFee;
    private BigDecimal acquisitionFee;
    private Integer acquisitionQty;
    private BigDecimal overheadFee;
    private Integer overheadQty;

    public BigDecimal getRepairAdminFee() {
        return repairAdminFee;
    }

    public void setRepairAdminFee(BigDecimal repairAdminFee) {
        this.repairAdminFee = repairAdminFee;
    }

    public BigDecimal getRepairAcquisitionFee() {
        return repairAcquisitionFee;
    }

    public void setRepairAcquisitionFee(BigDecimal repairAcquisitionFee) {
        this.repairAcquisitionFee = repairAcquisitionFee;
    }

    public int getPenaltyBand() {
        return penaltyBand;
    }

    public void setPenaltyBand(int penaltyBand) {
        this.penaltyBand = penaltyBand;
    }

    public BigDecimal getHireInsurerDiscountCalculated() {
        return hireInsurerDiscountCalculated;
    }

    public void setHireInsurerDiscountCalculated(BigDecimal hireInsurerDiscountCalculated) {
        this.hireInsurerDiscountCalculated = hireInsurerDiscountCalculated;
    }

    public BigDecimal getRepairInsurerDiscountCalculated() {
        return repairInsurerDiscountCalculated;
    }

    public void setRepairInsurerDiscountCalculated(BigDecimal repairInsurerDiscountCalculated) {
        this.repairInsurerDiscountCalculated = repairInsurerDiscountCalculated;
    }

    public BigDecimal getTotalInsurerDiscountCalculated() {
        return totalInsurerDiscountCalculated;
    }

    public void setTotalInsurerDiscountCalculated(BigDecimal totalInsurerDiscountCalculated) {
        this.totalInsurerDiscountCalculated = totalInsurerDiscountCalculated;
    }

    public BigDecimal getTotalGrossInsurerDiscount() {
        return totalGrossInsurerDiscount;
    }

    public void setTotalGrossInsurerDiscount(BigDecimal totalGrossInsurerDiscount) {
        this.totalGrossInsurerDiscount = totalGrossInsurerDiscount;
    }

    public InvoiceOriginal getInvoiceOriginal() {
        return invoiceOriginal;
    }

    public void setInvoiceOriginal(InvoiceOriginal invoiceOriginal) {
        this.invoiceOriginal = invoiceOriginal;
    }

    public Date getAutoPenaltyStart() {
        return autoPenaltyStart;
    }

    public void setAutoPenaltyStart(Date autoPenaltyStart) {
        this.autoPenaltyStart = autoPenaltyStart;
    }

    public boolean isPenaltyChargesPaid() {
        return penaltyChargesPaid;
    }

    public void setPenaltyChargesPaid(boolean penaltyChargesPaid) {
        this.penaltyChargesPaid = penaltyChargesPaid;
    }

    public BigDecimal getEngineerFeeGrossPaid() {
        return engineerFeeGrossPaid;
    }

    public void setEngineerFeeGrossPaid(BigDecimal engineerFeeGrossPaid) {
        this.engineerFeeGrossPaid = engineerFeeGrossPaid;
    }

    public BigDecimal getHireGrossPaid() {
        return hireGrossPaid;
    }

    public void setHireGrossPaid(BigDecimal hireGrossPaid) {
        this.hireGrossPaid = hireGrossPaid;
    }

    public BigDecimal getHirePenaltyChargePaid() {
        return hirePenaltyChargePaid;
    }

    public void setHirePenaltyChargePaid(BigDecimal hirePenaltyChargePaid) {
        this.hirePenaltyChargePaid = hirePenaltyChargePaid;
    }

    public BigDecimal getRepairGrossPaid() {
        return repairGrossPaid;
    }

    public void setRepairGrossPaid(BigDecimal repairGrossPaid) {
        this.repairGrossPaid = repairGrossPaid;
    }

    public BigDecimal getRepairPenaltyChargePaid() {
        return repairPenaltyChargePaid;
    }

    public void setRepairPenaltyChargePaid(BigDecimal repairPenaltyChargePaid) {
        this.repairPenaltyChargePaid = repairPenaltyChargePaid;
    }

    public BigDecimal getStorageRecoveryGrossPaid() {
        return storageRecoveryGrossPaid;
    }

    public void setStorageRecoveryGrossPaid(BigDecimal storageRecoveryGrossPaid) {
        this.storageRecoveryGrossPaid = storageRecoveryGrossPaid;
    }

    public BigDecimal getTotalLossFeeGrossPaid() {
        return totalLossFeeGrossPaid;
    }

    public void setTotalLossFeeGrossPaid(BigDecimal totalLossFeeGrossPaid) {
        this.totalLossFeeGrossPaid = totalLossFeeGrossPaid;
    }

    public Boolean isInterimPaymentReceivedFullAndFinal() {
        return interimPaymentReceivedFullAndFinal == null ? Boolean.FALSE : interimPaymentReceivedFullAndFinal;
    }

    public void setInterimPaymentReceivedFullAndFinal(Boolean interimPaymentReceivedFullAndFinal) {
        this.interimPaymentReceivedFullAndFinal = interimPaymentReceivedFullAndFinal;
    }

    public BigDecimal getChoDiscountFeePaid() {
        return choDiscountFeePaid;
    }

    public void setChoDiscountFeePaid(BigDecimal choDiscountFeePaid) {
        this.choDiscountFeePaid = choDiscountFeePaid;
    }

    public BigDecimal getClaimHandlerChargePaid() {
        return claimHandlerChargePaid;
    }

    public void setClaimHandlerChargePaid(BigDecimal claimHandlerChargePaid) {
        this.claimHandlerChargePaid = claimHandlerChargePaid;
    }

    public BigDecimal getDeductionClaimHandlerFeePaid() {
        return deductionClaimHandlerFeePaid;
    }

    public void setDeductionClaimHandlerFeePaid(BigDecimal deductionClaimHandlerFeePaid) {
        this.deductionClaimHandlerFeePaid = deductionClaimHandlerFeePaid;
    }

    public BigDecimal getInsurerDiscountFeePaid() {
        return insurerDiscountFeePaid;
    }

    public void setInsurerDiscountFeePaid(BigDecimal insurerDiscountFeePaid) {
        this.insurerDiscountFeePaid = insurerDiscountFeePaid;
    }

    public Invoice() {
    }

    public java.util.Date getDateInvoiced() {

        return dateInvoiced;
    }

    /**
     * Method 'setDateInvoiced'
     *
     * @param dateInvoiced
     */
    public void setDateInvoiced(java.util.Date dateInvoiced) {

        this.dateInvoiced = dateInvoiced;
    }

    /**
     * Method 'getHireNet'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getHireNet() {
        LOG.debug("getHireNet is being called");
        return hireNet;
    }

    /**
     * Method 'setHireNet'
     *
     * @param hireNet
     */
    public void setHireNet(java.math.BigDecimal hireNet) {
        LOG.debug("setHireNet is being called");
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

    public BigDecimal getInsurerDiscount() {
        return insurerDiscount;
    }

    public void setInsurerDiscount(BigDecimal insurerDiscount) {
        this.insurerDiscount = insurerDiscount;
    }

    public BigDecimal getHireGrossInsurerDiscount() {
        return hireGrossInsurerDiscount;
    }

    public void setHireGrossInsurerDiscount(BigDecimal hireGrossInsurerDiscount) {
        this.hireGrossInsurerDiscount = hireGrossInsurerDiscount;
    }

    public BigDecimal getRepairGrossInsurerDiscount() {
        return repairGrossInsurerDiscount;
    }

    public void setRepairGrossInsurerDiscount(BigDecimal repairGrossInsurerDiscount) {
        this.repairGrossInsurerDiscount = repairGrossInsurerDiscount;
    }

    /**
     * Method 'getFullTotalToPay'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getFullTotalToPay() {
        return fullTotalToPay;
    }

    /**
     * Method 'setFullTotalToPay'
     *
     * @param fullTotalToPay
     */
    public void setFullTotalToPay(java.math.BigDecimal totalToPay) {
        this.fullTotalToPay = totalToPay;
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
    public java.math.BigDecimal getMiscellaneousFee() {
        return miscellaneousFee;
    }

    /**
     * Method 'setCdwFee'
     *
     * @param cdwFee
     */
    public void setMiscellaneousFee(java.math.BigDecimal miscellaneousFee) {
        this.miscellaneousFee = miscellaneousFee;
    }

    /**
     * Method 'getCdwQty'
     *
     * @return java.lang.Integer
     */
    public int getMiscellaneousQty() {
        if (miscellaneousQty == null) {
            return 0;
        }
        return miscellaneousQty;
    }

    /**
     * Method 'setCdwQty'
     *
     * @param cdwQty
     */
    public void setMiscellaneousQty(java.lang.Integer miscellaneousQty) {
        this.miscellaneousQty = miscellaneousQty;
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
        if (automaticQty == null) {
            return 0;
        }
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
        if (satNavQty == null) {
            return 0;
        }
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
        if (estateQty == null) {
            return 0;
        }
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
        if (babySeatQty == null) {
            return 0;
        }
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
        if (towBarsQty == null) {
            return 0;
        }
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
        if (nonStandardInsurancePremiumQty == null) {
            return 0;
        }
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
        if (adminQty == null) {
            return 0;
        }
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
        if (roofRackQty == null) {
            return 0;
        }
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
        if (dualControlQty == null) {
            return 0;
        }
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
        if (deliveryCollectionQty == null) {
            return 0;
        }
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

    public BigDecimal getHirePenaltyCharge() {
        return hirePenaltyCharge == null ? BigDecimal.ZERO : hirePenaltyCharge;
    }

    public void setHirePenaltyCharge(BigDecimal hirePenaltyCharge) {
        this.hirePenaltyCharge = hirePenaltyCharge == null ? BigDecimal.ZERO : hirePenaltyCharge;
    }

    public BigDecimal getRepairPenaltyCharge() {
        return repairPenaltyCharge == null ? BigDecimal.ZERO : repairPenaltyCharge;
    }

    public void setRepairPenaltyCharge(BigDecimal repairPenaltyCharge) {
        this.repairPenaltyCharge = repairPenaltyCharge == null ? BigDecimal.ZERO : repairPenaltyCharge;
    }

    public int getInvoicedDays() {
        // long dateDiff = DateHelper.getNumberOf24HourPeriodsBetween(getDateInvoiced(), new Date()) + 1;
        int dateDiff = DateHelper.getNumberOfDaysBetween(getAutoPenaltyStart(), new Date()) + 1;
        return dateDiff;
    }

    public ReasonOfRejection getReasonOfRejection() {
        return reasonOfRejection;
    }

    public void setReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        this.reasonOfRejection = reasonOfRejection;
    }

    public Date getHirePenaltyChargeAppliedDate() {
        return hirePenaltyChargeAppliedDate;
    }

    public void setHirePenaltyChargeAppliedDate(Date hirePenaltyChargeAppliedDate) {
        this.hirePenaltyChargeAppliedDate = hirePenaltyChargeAppliedDate;
    }

    public Date getRepairPenaltyChargeAppliedDate() {
        return repairPenaltyChargeAppliedDate;
    }

    public void setRepairPenaltyChargeAppliedDate(Date repairPenaltyChargeAppliedDate) {
        this.repairPenaltyChargeAppliedDate = repairPenaltyChargeAppliedDate;
    }

//    public BigDecimal getOriginalFullTotalToPay() {
//        return originalFullTotalToPay;
//    }
//
//    public void setOriginalFullTotalToPay(BigDecimal originalTotalToPay) {
//        this.originalFullTotalToPay = originalTotalToPay;
//    }

    public BigDecimal getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(BigDecimal totalToPaySplitLiability) {
        LOG.debug("setTotalToPay() is called with the value of {}", totalToPaySplitLiability);
        this.totalToPay = totalToPaySplitLiability;
    }

//    public BigDecimal getOriginalTotalToPay() {
//        return originalTotalToPay;
//    }
//
//    public void setOriginalTotalToPay(BigDecimal originalTotalToPay) {
//        this.originalTotalToPay = originalTotalToPay;
//    }

    public BigDecimal getAdditionalDriverFee() {
//        if (additionalDriverFee == null)
//            return BigDecimal.ZERO;
//        else
        return additionalDriverFee;
    }

    public void setAdditionalDriverFee(BigDecimal additionalDriverFee) {
        this.additionalDriverFee = additionalDriverFee;
    }

    public Integer getAdditionalDriverQty() {
//        if (additionalDriverQty == null)
//            return 0;
//        else
        return additionalDriverQty;
    }

    public void setAdditionalDriverQty(Integer additionalDriverQty) {
        this.additionalDriverQty = additionalDriverQty;
    }

    public Boolean getCoverNoteRequired() {
        return coverNoteRequired;
    }

    public void setCoverNoteRequired(Boolean coverNoteRequired) {
        this.coverNoteRequired = coverNoteRequired;
    }

    public String getCoverNoteRequiredDesc() {
        if (coverNoteRequired == null) {
            return "";
        } else {
            return coverNoteRequired ? "Yes" : "No";
        }
    }

    public BigDecimal getTotalLossFeeGross() {
        return totalLossFeeGross;
    }

    public void setTotalLossFeeGross(BigDecimal totalLossFeeGross) {
        this.totalLossFeeGross = totalLossFeeGross;
    }

    public BigDecimal getTotalLossFeeNet() {
        return totalLossFeeNet;
    }

    public void setTotalLossFeeNet(BigDecimal totalLossFeeNet) {
        this.totalLossFeeNet = totalLossFeeNet;
    }

    public BigDecimal getTotalLossFeeVat() {
        return totalLossFeeVat;
    }

    public void setTotalLossFeeVat(BigDecimal totalLossFeeVat) {
        this.totalLossFeeVat = totalLossFeeVat;
    }

    public String getHirePenaltyPercentage() {
        return hirePenaltyPercentage;
    }

    public void setHirePenaltyPercentage(String hirePenaltyPercentage) {
        this.hirePenaltyPercentage = hirePenaltyPercentage;
    }

    public String getRepairPenaltyPercentage() {
        return repairPenaltyPercentage;
    }

    public void setRepairPenaltyPercentage(String repairPenaltyPercentage) {
        this.repairPenaltyPercentage = repairPenaltyPercentage;
    }

    public BigDecimal getInterimPaymentMade() {
        return interimPaymentMade;
    }

    public void setInterimPaymentMade(BigDecimal interimPaymentMade) {
        this.interimPaymentMade = interimPaymentMade;
    }

    public Boolean isInterimPaymentOutstanding() {
        if (interimPaymentReceived == null) {
            interimPaymentReceived = BigDecimal.ZERO;
        }
        if (interimPaymentMade == null) {
            interimPaymentReceived = BigDecimal.ZERO;
        }
        return interimPaymentMade.compareTo(interimPaymentReceived) > 0;
    }

    public BigDecimal getTotalPenaltyCharge() {
        return totalPenaltyCharge;
    }

    public void setTotalPenaltyCharge(BigDecimal totalPenaltyCharge) {
        this.totalPenaltyCharge = totalPenaltyCharge;
    }

    public BigDecimal getInterimPaymentReceived() {
        return interimPaymentReceived;
    }

    public void setInterimPaymentReceived(BigDecimal interimPaymentReceived) {
        this.interimPaymentReceived = interimPaymentReceived;
    }

    public BigDecimal getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(BigDecimal finalPayment) {
        this.finalPayment = finalPayment;
    }

    public BigDecimal getAcquisitionFee() {
        return acquisitionFee;
    }

    public void setAcquisitionFee(BigDecimal acquisitionFee) {
        this.acquisitionFee = acquisitionFee;
    }

    public Integer getAcquisitionQty() {
        return acquisitionQty;
    }

    public void setAcquisitionQty(Integer acquisitionQty) {
        this.acquisitionQty = acquisitionQty;
    }

    public BigDecimal getOverheadFee() {
        return overheadFee;
    }

    public void setOverheadFee(BigDecimal overheadFee) {
        this.overheadFee = overheadFee;
    }

    public Integer getOverheadQty() {
        return overheadQty;
    }

    public void setOverheadQty(Integer overheadQty) {
        this.overheadQty = overheadQty;
    }

    
    public String getRepairPenaltyPercentageApplied() {
        BigDecimal appliedRepairPenaltyPercentageValue = getRepairPenaltyPercentageAppliedValue();
        if (appliedRepairPenaltyPercentageValue != null) {
            return appliedRepairPenaltyPercentageValue.toString().concat("%");
        }
        return null;
    }

    public String getHirePenaltyPercentageApplied() {
        BigDecimal appliedHirePenaltyPercentageValue = getHirePenaltyPercentageAppliedValue();
        if (appliedHirePenaltyPercentageValue != null) {
            return appliedHirePenaltyPercentageValue.toString().concat("%");
        }
        return null;
    }

    public BigDecimal getHirePenaltyPercentageAppliedValue() {
        if (getHireGross() != null && getHireGross().compareTo(BigDecimal.ZERO) >= 1
                && getHirePenaltyCharge() != null && getHirePenaltyCharge().compareTo(BigDecimal.ZERO) >= 1) {
            /*
             * Calculate the appliedHirePenaltyPercentage using the Hire Gross
             */
            BigDecimal appliedHirePenaltyPercentageValue = getHirePenaltyCharge().multiply(BigDecimal.valueOf(100)).divide((getHireGross()), 2, RoundingMode.HALF_UP);
            LOG.debug("actualHirePenaltyPercentage : {}, appliedHirePenaltyPercentage : {}", getHirePenaltyPercentage(), appliedHirePenaltyPercentageValue);
            return appliedHirePenaltyPercentageValue;
        }
        return null;
    }

    public BigDecimal getRepairPenaltyPercentageAppliedValue() {
        if (getRepairGross() != null && getRepairGross().compareTo(BigDecimal.ZERO) >= 1
                && getRepairPenaltyCharge() != null && getRepairPenaltyCharge().compareTo(BigDecimal.ZERO) >= 1) {
            /*
             * Calculate the appliedRepairPenaltyPercentage using the Hire Gross
             */
            BigDecimal appliedRepairPenaltyPercentageValue = getRepairPenaltyCharge().multiply(BigDecimal.valueOf(100)).divide((getRepairGross()), 2, RoundingMode.HALF_UP);
            LOG.debug("actualRepairPenaltyPercentage : {}, appliedRepairPenaltyPercentage : {}", getRepairPenaltyPercentage(), appliedRepairPenaltyPercentageValue);
            return appliedRepairPenaltyPercentageValue;
        }
        return null;
    }

    public boolean isAppliedHirePenaltyPercentageDifferent() {
        try {
            if (getHirePenaltyPercentageAppliedValue() != null && getHirePenaltyPercentage() != null && getHirePenaltyPercentage().endsWith("%")) {
                BigDecimal selectedHirePenaltyPercentageValue = new BigDecimal(getHirePenaltyPercentage().trim().replace("%", ""));
                return getHirePenaltyPercentageAppliedValue().compareTo(selectedHirePenaltyPercentageValue) != 0 ? true : false;
            } else if (getHirePenaltyPercentage() != null && getHirePenaltyPercentage().equalsIgnoreCase("commercial")) {
                return true;
            }
        } catch (Exception ex) {
            LOG.error("Exception while converting HirePenaltyPercentage string '{}' to BigDecimal", getHirePenaltyPercentage(), ex);
        }
        return false;
    }

    public boolean isAppliedRepairPenaltyPercentageDifferent() {

        try {
            if (getRepairPenaltyPercentageAppliedValue() != null && getRepairPenaltyPercentage() != null && getRepairPenaltyPercentage().endsWith("%")) {
                BigDecimal selectedRepairPenaltyPercentageValue = new BigDecimal(getRepairPenaltyPercentage().trim().replace("%", ""));
                return getRepairPenaltyPercentageAppliedValue().compareTo(selectedRepairPenaltyPercentageValue) != 0 ? true : false;
            }
        } catch (Exception ex) {
            LOG.error("Exception while converting RepairPenaltyPercentage string '{}' to BigDecimal", getRepairPenaltyPercentage(), ex);
        }
        return false;
    }
    
    public enum DisplayName {

        HANDLING_INVOICE_NO                 ("handlingInvoiceNo", "Supplier Claims Handling #"),
        CLAIM_INVOICE_NO                    ("claimInvoiceNo", "Supplier Claim Invoice #"),
        HIRE_RATE_CHARGED_PER_DAY           ("hireRateChargedPerDay", "Hire Rate Charged Per Day"),
        HIRE_NET                            ("hireNet", "Hire Net"),
        HIRE_VAT                            ("hireVat", "Hire VAT"),
        HIRE_GROSS                          ("hireGross", "Hire Gross"),
        REPAIR_NET                          ("repairNet", "Repair Net"),
        REPAIR_VAT                          ("repairVat", "Repair VAT"),
        REPAIR_GROSS                        ("repairGross", "Repair Gross"),
        ENGINEER_FEE_NET                    ("engineerFeeNet", "Engineer Fee Net"),
        ENGINEER_FEE_VAT                    ("engineerFeeVat", "Engineer Fee VAT"),
        ENGINEER_FEE_GROSS                  ("engineerFeeGross", "Engineer Fee Gross"),
        TOTAL_LOSS_FEE_NET                  ("totalLossFeeNet", "Total Loss Fee Net"),
        TOTAL_LOSS_FEE_VAT                  ("totalLossFeeVat", "Total Loss Fee VAT"),
        TOTAL_LOSS_FEE_GROSS                ("totalLossFeeGross", "Total Loss Fee Gross"),
        STORAGE_RECOVERY_NET                ("storageRecoveryNet", "Storage Recovery Net"),
        STORAGE_RECOVERY_VAT                ("storageRecoveryVat", "Storage Recovery VAT"),
        STORAGE_RECOVERY_GROSS              ("storageRecoveryGross", "Storage Recovery Gross"),
        TOTAL_NET                           ("totalNet", "Total Net"),
        TOTAL_VAT                           ("totalVat", "Total Vat"),
        TOTAL_GROSS                         ("totalGross", "Total Gross"),
        CLAIMS_HANDLING_INVOICE_AMOUNT      ("claimsHandlingInvoiceAmount", "Claims Handling Invoice Amount"),
        DEDUCTION_CLAIMS_HANDLING_FEE       ("deductionForClaimsHandlingFee", "Deduction For Claims Handling Fee"),
        CHO_DISCOUNT                        ("discount", "CHO Discount"),
        FULL_TOTAL_REQUESTED                ("fullTotalToPay", "Full Total Requested"),
        TOTAL_TO_PAY                        ("totalToPay", "Total To Pay"),
        EXCESS_AMOUNT_COLLECTED             ("excessAmountCollected", "Excess Collected From Policyholder"),
        VAT_AMOUNT_COLLECTED                ("vatAmountCollected", "VAT Collected From Policyholder"),
        DATE_INVOICED                       ("dateInvoiced", "Date Invoiced"),
        ACQUISITION_FEE                     ("acquisitionFee", "Acquisition Fee"),
        AUTOMATIC_FEE                       ("automaticFee", "Automatic Fee"),
        AUTOMATIC_QTY                       ("automaticQty", "Automatic Quantity"),
        MISCELLANEOUS_FEE                   ("miscellaneousFee", "Miscellaneous Costs"),
        ADDITIONAL_DRIVER_FEE               ("additionalDriverFee", "Additional Driver Fee"),
        ADDITIONAL_DRIVER_QTY               ("additionalDriverQty", "Additional Driver Quantity"),
        SAT_NAV_FEE                         ("satNavFee", "Sat Nav Fee"),
        SAT_NAV_QTY                         ("satNavQty", "Sat Nav Quantity"),
        ESTATE_FEE                          ("estateFee", "Estate Fee"),
        ESTATE_QTY                          ("estateQty", "Estate Quantity"),
        BABY_SEAT_FEE                       ("babySeatFee", "Baby Seat Fee"),
        BABY_SEAT_QTY                       ("babySeatQty", "Baby Seat Quantity"),
        TOW_BARS_FEE                        ("towBarsFee", "Tow Bars Fee"),
        TOW_BARS_QTY                        ("towBarsQty", "Tow Bars Quantity"),
        NON_STANDARD_INS_PREMIUM_FEE        ("nonStandardInsurancePremiumFee", "Non-standard Risk Ins. Premium Fee"),
        NON_STANDARD_INS_PREMIUM_QTY        ("nonStandardInsurancePremiumQty", "Non-standard Risk Ins. Premium Qty"),
        COVER_NOTE_REQUIRED                 ("coverNoteRequired", "Cover Note Required For Customers Own Insurance Policy?"),
        ADMIN_FEE                           ("adminFee", "Admin Fee"),
        ADMIN_QTY                           ("adminQty", "Admin Quantity"),
        ROOF_RACK_FEE                       ("roofRackFee", "Roof Rack Fee"),
        ROOF_RACK_QTY                       ("roofRackQty", "Roof Rack Quantity"),
        DUAL_CONTROL_FEE                    ("dualControlFee", "Dual Control Fee"),
        DUAL_CONTROL_QTY                    ("dualControlQty", "Dual Control Quantity"),
        DELIVERY_COLLECTION_FEE             ("deliveryCollectionFee", "Delivery Collection Fee"),
        DELIVERY_COLLECTION_QTY             ("deliveryCollectionQty", "Delivery Collection Fee Quantity"),
        OVERHEAD_FEE                        ("overheadFee", "Overhead and Margin Fee"),
        REPAIR_ADMIN_FEE                    ("repairAdminFee", "Repair Admin Fee"),
        REPAIR_ACQUISITION_FEE              ("repairAcquisitionFee", "Repair Acquisition Fee");
        
        private final String ParameterName;
        private final String displayName;

        DisplayName(String name, String displayName) {
            this.ParameterName = name;
            this.displayName = displayName;
        }

        public String getParameterName() {
            return ParameterName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
