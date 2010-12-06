package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceOriginal extends Entity implements Serializable {

    protected BigDecimal hireNet_original;
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceOriginal.class);

    public BigDecimal getAdditionalDriverFee_original() {
        return additionalDriverFee_original;
    }

    public void setAdditionalDriverFee_original(BigDecimal additionalDriverFee_original) {
        
            this.additionalDriverFee_original = additionalDriverFee_original;
        
    }

    public Integer getAdditionalDriverQty_original() {
        return additionalDriverQty_original;
    }

    public void setAdditionalDriverQty_original(Integer additionalDriverQty_original) {
        
            this.additionalDriverQty_original = additionalDriverQty_original;
        

    }

    public BigDecimal getAdminFee_original() {
        return adminFee_original;
    }

    public void setAdminFee_original(BigDecimal adminFee_original) {
        
            this.adminFee_original = adminFee_original;
       

    }

    public Integer getAdminQty_original() {
        return adminQty_original;
    }

    public void setAdminQty_original(Integer adminQty_original) {
       
            this.adminQty_original = adminQty_original;
       

    }

    public BigDecimal getAutomaticFee_original() {
        return automaticFee_original;
    }

    public void setAutomaticFee_original(BigDecimal automaticFee_original) {
        
            this.automaticFee_original = automaticFee_original;
       

    }

    public Integer getAutomaticQty_original() {
        return automaticQty_original;
    }

    public void setAutomaticQty_original(Integer automaticQty_original) {
       
            this.automaticQty_original = automaticQty_original;
        

    }

    public BigDecimal getBabySeatFee_original() {
        return babySeatFee_original;
    }

    public void setBabySeatFee_original(BigDecimal babySeatFee_original) {
       
            this.babySeatFee_original = babySeatFee_original;
       

    }

    public Integer getBabySeatQty_original() {
        return babySeatQty_original;
    }

    public void setBabySeatQty_original(Integer babySeatQty_original) {
       
            this.babySeatQty_original = babySeatQty_original;
       

    }

    public BigDecimal getCdwFee_original() {
        return cdwFee_original;
    }

    public void setCdwFee_original(BigDecimal cdwFee_original) {
            this.cdwFee_original = cdwFee_original;

    }

    public Integer getCdwQty_original() {
        return cdwQty_original;
    }

    public void setCdwQty_original(Integer cdwQty_original) {
            this.cdwQty_original = cdwQty_original;

    }

    public BigDecimal getClaimsHandlingInvoiceAmount_original() {
        return claimsHandlingInvoiceAmount_original;
    }

    public void setClaimsHandlingInvoiceAmount_original(BigDecimal claimsHandlingInvoiceAmount_original) {
            this.claimsHandlingInvoiceAmount_original = claimsHandlingInvoiceAmount_original;

    }

    public BigDecimal getDeductionForClaimsHandlingFee_original() {
        return deductionForClaimsHandlingFee_original;
    }

    public void setDeductionForClaimsHandlingFee_original(BigDecimal deductionForClaimsHandlingFee_original) {
            this.deductionForClaimsHandlingFee_original = deductionForClaimsHandlingFee_original;

    }

    public BigDecimal getDeliveryCollectionFee_original() {
        return deliveryCollectionFee_original;
    }

    public void setDeliveryCollectionFee_original(BigDecimal deliveryCollectionFee_original) {
            this.deliveryCollectionFee_original = deliveryCollectionFee_original;

    }

    public Integer getDeliveryCollectionQty_original() {
        return deliveryCollectionQty_original;
    }

    public void setDeliveryCollectionQty_original(Integer deliveryCollectionQty_original) {
            this.deliveryCollectionQty_original = deliveryCollectionQty_original;

    }

    public BigDecimal getDiscount_original() {
        return discount_original;
    }

    public void setDiscount_original(BigDecimal discount_original) {
            this.discount_original = discount_original;

    }

    public BigDecimal getDualControlFee_original() {
        return dualControlFee_original;
    }

    public void setDualControlFee_original(BigDecimal dualControlFee_original) {
            this.dualControlFee_original = dualControlFee_original;

    }

    public Integer getDualControlQty_original() {
        return dualControlQty_original;
    }

    public void setDualControlQty_original(Integer dualControlQty_original) {
            this.dualControlQty_original = dualControlQty_original;

    }

    public BigDecimal getEngineerFeeGross_original() {
        return engineerFeeGross_original;
    }

    public void setEngineerFeeGross_original(BigDecimal engineerFeeGross_original) {
            this.engineerFeeGross_original = engineerFeeGross_original;

    }

    public BigDecimal getEngineerFeeNet_original() {
        return engineerFeeNet_original;
    }

    public void setEngineerFeeNet_original(BigDecimal engineerFeeNet_original) {
            this.engineerFeeNet_original = engineerFeeNet_original;

    }

    public BigDecimal getEngineerFeeVat_original() {
        return engineerFeeVat_original;
    }

    public void setEngineerFeeVat_original(BigDecimal engineerFeeVat_original) {
            this.engineerFeeVat_original = engineerFeeVat_original;

    }

    public BigDecimal getEstateFee_original() {
        return estateFee_original;
    }

    public void setEstateFee_original(BigDecimal estateFee_original) {
            this.estateFee_original = estateFee_original;

    }

    public Integer getEstateQty_original() {
        return estateQty_original;
    }

    public void setEstateQty_original(Integer estateQty_original) {
            this.estateQty_original = estateQty_original;

    }

    public BigDecimal getExcessAmountCollected_original() {
        return excessAmountCollected_original;
    }

    public void setExcessAmountCollected_original(BigDecimal excessAmountCollected_original) {
            this.excessAmountCollected_original = excessAmountCollected_original;

    }

    public BigDecimal getFullTotalToPay_original() {
        return fullTotalToPay_original;
    }

    public void setFullTotalToPay_original(BigDecimal fullTotalToPay_original) {
            this.fullTotalToPay_original = fullTotalToPay_original;

    }

    public BigDecimal getHireGross_original() {
        return hireGross_original;
    }

    public void setHireGross_original(BigDecimal hireGross_original) {
            this.hireGross_original = hireGross_original;

    }

    public BigDecimal getHireNet_original() {
        LOG.debug("getHireNet_original is being called");
        return hireNet_original;
    }

    public void setHireNet_original(BigDecimal hireNet_original) {
        LOG.debug("setHireNet_original is being called");
            this.hireNet_original = hireNet_original;
        

    }

    public Date getHirePenaltyChargeAppliedDate_original() {
        return hirePenaltyChargeAppliedDate_original;
    }

    

    public BigDecimal getHirePenaltyCharge_original() {
        return hirePenaltyCharge_original;
    }

    public void setHirePenaltyCharge_original(BigDecimal hirePenaltyCharge_original) {
            this.hirePenaltyCharge_original = hirePenaltyCharge_original;

    }

    public String getHirePenaltyPercentage_original() {
        return hirePenaltyPercentage_original;
    }

    public void setHirePenaltyPercentage_original(String hirePenaltyPercentage_original) {
            this.hirePenaltyPercentage_original = hirePenaltyPercentage_original;

    }

    public BigDecimal getHireRateChargedPerDay_original() {
        return hireRateChargedPerDay_original;
    }

    public void setHireRateChargedPerDay_original(BigDecimal hireRateChargedPerDay_original) {
            this.hireRateChargedPerDay_original = hireRateChargedPerDay_original;

    }

    public BigDecimal getHireVat_original() {

        return hireVat_original;
    }

    public void setHireVat_original(BigDecimal hireVat_original) {
            this.hireVat_original = hireVat_original;


    }

    public BigDecimal getInterimPayment_original() {
        return interimPayment_original;
    }

    public void setInterimPayment_original(BigDecimal interimPayment_original) {
            this.interimPayment_original = interimPayment_original;

    }

    public BigDecimal getNonStandardInsurancePremiumFee_original() {
        return nonStandardInsurancePremiumFee_original;
    }

    public void setNonStandardInsurancePremiumFee_original(BigDecimal nonStandardInsurancePremiumFee_original) {
            this.nonStandardInsurancePremiumFee_original = nonStandardInsurancePremiumFee_original;

    }

    public Integer getNonStandardInsurancePremiumQty_original() {
        return nonStandardInsurancePremiumQty_original;
    }

    public void setNonStandardInsurancePremiumQty_original(Integer nonStandardInsurancePremiumQty_original) {
            this.nonStandardInsurancePremiumQty_original = nonStandardInsurancePremiumQty_original;

    }

    public BigDecimal getOriginalFullTotalToPay_original() {
        return originalFullTotalToPay_original;
    }

    public void setOriginalFullTotalToPay_original(BigDecimal originalFullTotalToPay_original) {
            this.originalFullTotalToPay_original = originalFullTotalToPay_original;

    }

    public BigDecimal getOriginalTotalToPay_original() {
        return originalTotalToPay_original;
    }

    public void setOriginalTotalToPay_original(BigDecimal originalTotalToPay_original) {
            this.originalTotalToPay_original = originalTotalToPay_original;

    }

    public Integer getPenaltyAlertQty_original() {
        return penaltyAlertQty_original;
    }

    public void setPenaltyAlertQty_original(Integer penaltyAlertQty_original) {
            this.penaltyAlertQty_original = penaltyAlertQty_original;

    }

    public BigDecimal getRepairGross_original() {
        return repairGross_original;
    }

    public void setRepairGross_original(BigDecimal repairGross_original) {
            this.repairGross_original = repairGross_original;

    }

    public BigDecimal getRepairNet_original() {

        return repairNet_original;
    }

    public void setRepairNet_original(BigDecimal repairNet_original) {
            this.repairNet_original = repairNet_original;

    }

    public Date getRepairPenaltyChargeAppliedDate_original() {
        return repairPenaltyChargeAppliedDate_original;
    }

    

    public BigDecimal getRepairPenaltyCharge_original() {
        return repairPenaltyCharge_original;
    }

    public void setRepairPenaltyCharge_original(BigDecimal repairPenaltyCharge_original) {
            this.repairPenaltyCharge_original = repairPenaltyCharge_original;

    }

    public String getRepairPenaltyPercentage_original() {
        return repairPenaltyPercentage_original;
    }

    public void setRepairPenaltyPercentage_original(String repairPenaltyPercentage_original) {
            this.repairPenaltyPercentage_original = repairPenaltyPercentage_original;

    }

    public BigDecimal getRepairVat_original() {
        return repairVat_original;
    }

    public void setRepairVat_original(BigDecimal repairVat_original) {
            this.repairVat_original = repairVat_original;

    }

    public BigDecimal getRoofRackFee_original() {
        return roofRackFee_original;
    }

    public void setRoofRackFee_original(BigDecimal roofRackFee_original) {
            this.roofRackFee_original = roofRackFee_original;

    }

    public Integer getRoofRackQty_original() {
        return roofRackQty_original;
    }

    public void setRoofRackQty_original(Integer roofRackQty_original) {
            this.roofRackQty_original = roofRackQty_original;

    }

    public BigDecimal getSatNavFee_original() {
        return satNavFee_original;
    }

    public void setSatNavFee_original(BigDecimal satNavFee_original) {
            this.satNavFee_original = satNavFee_original;

    }

    public Integer getSatNavQty_original() {
        return satNavQty_original;
    }

    public void setSatNavQty_original(Integer satNavQty_original) {
            this.satNavQty_original = satNavQty_original;

    }

    public BigDecimal getStorageRecoveryGross_original() {
        return storageRecoveryGross_original;
    }

    public void setStorageRecoveryGross_original(BigDecimal storageRecoveryGross_original) {
            this.storageRecoveryGross_original = storageRecoveryGross_original;

    }

    public BigDecimal getStorageRecoveryNet_original() {
        return storageRecoveryNet_original;
    }

    public void setStorageRecoveryNet_original(BigDecimal storageRecoveryNet_original) {
            this.storageRecoveryNet_original = storageRecoveryNet_original;

    }

    public BigDecimal getStorageRecoveryVat_original() {
        return storageRecoveryVat_original;
    }

    public void setStorageRecoveryVat_original(BigDecimal storageRecoveryVat_original) {
            this.storageRecoveryVat_original = storageRecoveryVat_original;

    }

    public BigDecimal getTotalGross_original() {
        return totalGross_original;
    }

    public void setTotalGross_original(BigDecimal totalGross_original) {
            this.totalGross_original = totalGross_original;

    }

    public BigDecimal getTotalLossFeeGross_original() {
        return totalLossFeeGross_original;
    }

    public void setTotalLossFeeGross_original(BigDecimal totalLossFeeGross_original) {
            this.totalLossFeeGross_original = totalLossFeeGross_original;

    }

    public BigDecimal getTotalLossFeeNet_original() {
        return totalLossFeeNet_original;
    }

    public void setTotalLossFeeNet_original(BigDecimal totalLossFeeNet_original) {
            this.totalLossFeeNet_original = totalLossFeeNet_original;

    }

    public BigDecimal getTotalLossFeeVat_original() {
        return totalLossFeeVat_original;
    }

    public void setTotalLossFeeVat_original(BigDecimal totalLossFeeVat_original) {
            this.totalLossFeeVat_original = totalLossFeeVat_original;

    }

    public BigDecimal getTotalNet_original() {
        return totalNet_original;
    }

    public void setTotalNet_original(BigDecimal totalNet_original) {
            this.totalNet_original = totalNet_original;

    }

    public BigDecimal getTotalPenaltyCharge_original() {
        return totalPenaltyCharge_original;
    }

    public void setTotalPenaltyCharge_original(BigDecimal totalPenaltyCharge_original) {
            this.totalPenaltyCharge_original = totalPenaltyCharge_original;

    }

    public BigDecimal getTotalToPay_original() {
        return totalToPay_original;
    }

    public void setTotalToPay_original(BigDecimal totalToPay_original) {
            this.totalToPay_original = totalToPay_original;

    }

    public BigDecimal getTotalVat_original() {
        return totalVat_original;
    }

    public void setTotalVat_original(BigDecimal totalVat_original) {
            this.totalVat_original = totalVat_original;

    }

    public BigDecimal getTowBarsFee_original() {
        return towBarsFee_original;
    }

    public void setTowBarsFee_original(BigDecimal towBarsFee_original) {
            this.towBarsFee_original = towBarsFee_original;

    }

    public Integer getTowBarsQty_original() {
        return towBarsQty_original;
    }

    public void setTowBarsQty_original(Integer towBarsQty_original) {
            this.towBarsQty_original = towBarsQty_original;

    }

    public BigDecimal getVatAmountCollected_original() {
        return vatAmountCollected_original;
    }

    public void setVatAmountCollected_original(BigDecimal vatAmountCollected_original) {
            this.vatAmountCollected_original = vatAmountCollected_original;

    }
    /**
     * This attribute maps to the column hire_vat in the invoice table.
     */
    protected BigDecimal hireVat_original;
    /**
     * This attribute maps to the column hire_gross in the invoice table.
     */
    protected BigDecimal hireGross_original;
    /**
     * This attribute maps to the column repair_net in the invoice table.
     */
    protected BigDecimal repairNet_original;
    /**
     * This attribute maps to the column repair_vat in the invoice table.
     */
    protected BigDecimal repairVat_original;
    /**
     * This attribute maps to the column repair_gross in the invoice table.
     */
    protected BigDecimal repairGross_original;
    /**
     * This attribute maps to the column engineer_fee_net in the invoice table.
     */
    protected BigDecimal engineerFeeNet_original;
    /**
     * This attribute maps to the column engineer_fee_vat in the invoice table.
     */
    protected BigDecimal engineerFeeVat_original;
    /**
     * This attribute maps to the column engineer_fee_gross in the invoice table.
     */
    protected BigDecimal engineerFeeGross_original;
    /**
     * This attribute maps to the column storage_recovery_net in the invoice table.
     */
    protected BigDecimal storageRecoveryNet_original;
    /**
     * This attribute maps to the column storage_recovery_vat in the invoice table.
     */
    protected BigDecimal storageRecoveryVat_original;
    /**
     * This attribute maps to the column storage_recovery_gross in the invoice table.
     */
    protected BigDecimal storageRecoveryGross_original;
    /**
     * This attribute maps to the column total_net in the invoice table.
     */
    protected BigDecimal totalNet_original;
    /**
     * This attribute maps to the column total_vat in the invoice table.
     */
    protected BigDecimal totalVat_original;
    /**
     * This attribute maps to the column total_gross in the invoice table.
     */
    protected BigDecimal totalGross_original;
    /**
     * This attribute maps to the column claims_handling_invoice_amount in the invoice table.
     */
    protected BigDecimal claimsHandlingInvoiceAmount_original;
    /**
     * This attribute maps to the column deduction_for_claims_handling_fee in the invoice table.
     */
    protected BigDecimal deductionForClaimsHandlingFee_original;
    /**
     * This attribute maps to the column discount in the invoice table.
     */
    protected BigDecimal discount_original;
    /**
     * This attribute maps to the column total_to_pay in the invoice table.
     */
    protected BigDecimal fullTotalToPay_original;
    /**
     * This attribute maps to the column handling_invoice_no in the invoice table.
     */
    protected BigDecimal cdwFee_original;
    /**
     * This attribute maps to the column cdw_qty in the invoice table.
     */
    protected Integer cdwQty_original;
    /**
     * This attribute maps to the column automatic_fee in the invoice table.
     */
    protected BigDecimal automaticFee_original;
    /**
     * This attribute maps to the column automatic_qty in the invoice table.
     */
    protected Integer automaticQty_original;
    /**
     * This attribute maps to the column additional_driver_fee in the invoice table.
     */
    protected BigDecimal additionalDriverFee_original;
    /**
     * This attribute maps to the column additional_driver_qty in the invoice table.
     */
    protected Integer additionalDriverQty_original;
    /**
     * This attribute maps to the column sat_nav_fee in the invoice table.
     */
    protected BigDecimal satNavFee_original;
    /**
     * This attribute maps to the column sat_nav_qty in the invoice table.
     */
    protected Integer satNavQty_original;
    /**
     * This attribute maps to the column estate_fee in the invoice table.
     */
    protected BigDecimal estateFee_original;
    /**
     * This attribute maps to the column estate_qty in the invoice table.
     */
    protected Integer estateQty_original;
    /**
     * This attribute maps to the column baby_seat_fee in the invoice table.
     */
    protected BigDecimal babySeatFee_original;
    /**
     * This attribute maps to the column baby_seat_qty in the invoice table.
     */
    protected Integer babySeatQty_original;
    /**
     * This attribute maps to the column tow_bars_fee in the invoice table.
     */
    protected BigDecimal towBarsFee_original;
    /**
     * This attribute maps to the column tow_bars_qty in the invoice table.
     */
    protected Integer towBarsQty_original;
    /**
     * This attribute maps to the column non_standard_insurance_premium_fee in the invoice table.
     */
    protected BigDecimal nonStandardInsurancePremiumFee_original;
    /**
     * This attribute maps to the column non_standard_insurance_premium_qty in the invoice table.
     */
    protected Integer nonStandardInsurancePremiumQty_original;
    protected BigDecimal adminFee_original;
    protected Integer adminQty_original;
    protected BigDecimal roofRackFee_original;
    protected Integer roofRackQty_original;
    protected BigDecimal dualControlFee_original;
    protected Integer dualControlQty_original;
    protected BigDecimal deliveryCollectionFee_original;
    protected Integer deliveryCollectionQty_original;
    protected BigDecimal hireRateChargedPerDay_original;
    protected BigDecimal excessAmountCollected_original;
    protected BigDecimal vatAmountCollected_original;
    protected BigDecimal hirePenaltyCharge_original;
    protected String hirePenaltyPercentage_original;
    protected Date hirePenaltyChargeAppliedDate_original;
    protected BigDecimal repairPenaltyCharge_original;
    protected String repairPenaltyPercentage_original;
    protected Date repairPenaltyChargeAppliedDate_original;
    protected Integer penaltyAlertQty_original;
    protected BigDecimal totalPenaltyCharge_original;
    protected BigDecimal originalFullTotalToPay_original;
    protected BigDecimal totalToPay_original;
    protected BigDecimal originalTotalToPay_original;
    protected BigDecimal totalLossFeeNet_original;
    protected BigDecimal totalLossFeeVat_original;
    protected BigDecimal totalLossFeeGross_original;
    protected BigDecimal interimPayment_original;

    public InvoiceOriginal() {
    }
}
