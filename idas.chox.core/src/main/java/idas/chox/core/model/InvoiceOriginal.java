package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceOriginal extends Entity implements Serializable {

    private Date dateInvoicedOriginal;
    private BigDecimal hireVatOriginal;
    private BigDecimal hireGrossOriginal;
    private BigDecimal repairNetOriginal;
    private BigDecimal repairVatOriginal;
    private BigDecimal repairGrossOriginal;
    private BigDecimal engineerFeeNetOriginal;
    private BigDecimal engineerFeeVatOriginal;
    private BigDecimal engineerFeeGrossOriginal;
    private BigDecimal storageRecoveryNetOriginal;
    private BigDecimal storageRecoveryVatOriginal;
    private BigDecimal storageRecoveryGrossOriginal;
    private BigDecimal totalNetOriginal;
    private BigDecimal totalVatOriginal;
    private BigDecimal totalGrossOriginal;
    private BigDecimal claimsHandlingInvoiceAmountOriginal;
    private BigDecimal deductionForClaimsHandlingFeeOriginal;
    private BigDecimal discountOriginal;
    private BigDecimal insurerDiscountOriginal;
    private BigDecimal fullTotalToPayOriginal;
    private BigDecimal collaborationFeeOriginal;
    private Integer collaborationQtyOriginal;
    private BigDecimal miscellaneousFeeOriginal;
    private Integer miscellaneousQtyOriginal;
    private BigDecimal automaticFeeOriginal;
    private Integer automaticQtyOriginal;
    private BigDecimal additionalDriverFeeOriginal;
    private Integer additionalDriverQtyOriginal;
    private BigDecimal satNavFeeOriginal;
    private Integer satNavQtyOriginal;
    private BigDecimal estateFeeOriginal;
    private Integer estateQtyOriginal;
    private BigDecimal babySeatFeeOriginal;
    private Integer babySeatQtyOriginal;
    private BigDecimal towBarsFeeOriginal;
    private Integer towBarsQtyOriginal;
    private BigDecimal nonStandardInsurancePremiumFeeOriginal;
    private Integer nonStandardInsurancePremiumQtyOriginal;
    private BigDecimal adminFeeOriginal;
    private Integer adminQtyOriginal;
    private BigDecimal roofRackFeeOriginal;
    private Integer roofRackQtyOriginal;
    private BigDecimal dualControlFeeOriginal;
    private Integer dualControlQtyOriginal;
    private BigDecimal deliveryCollectionFeeOriginal;
    private Integer deliveryCollectionQtyOriginal;
    private BigDecimal hireRateChargedPerDayOriginal;
    private BigDecimal excessAmountCollectedOriginal;
    private BigDecimal vatAmountCollectedOriginal;
    private BigDecimal hirePenaltyChargeOriginal;
    private String hirePenaltyPercentageOriginal;
    private Date hirePenaltyChargeAppliedDateOriginal;
    private BigDecimal repairPenaltyChargeOriginal;
    private String repairPenaltyPercentageOriginal;
    private Date repairPenaltyChargeAppliedDateOriginal;
    private BigDecimal totalPenaltyChargeOriginal;
    private BigDecimal totalToPayOriginal;
    private BigDecimal totalLossFeeNetOriginal;
    private BigDecimal totalLossFeeVatOriginal;
    private BigDecimal totalLossFeeGrossOriginal;
    private BigDecimal interimPaymentOriginal;
    private BigDecimal repairAdminFeeOriginal;
    private BigDecimal repairAcquisitionFeeOriginal;

    public BigDecimal getRepairAdminFeeOriginal() {
        return repairAdminFeeOriginal;
    }

    public void setRepairAdminFeeOriginal(BigDecimal repairAdminFeeOriginal) {
        this.repairAdminFeeOriginal = repairAdminFeeOriginal;
    }

    public BigDecimal getRepairAcquisitionFeeOriginal() {
        return repairAcquisitionFeeOriginal;
    }

    public void setRepairAcquisitionFeeOriginal(BigDecimal repairAcquisitionFeeOriginal) {
        this.repairAcquisitionFeeOriginal = repairAcquisitionFeeOriginal;
    }

    public Date getDateInvoicedOriginal() {
        return dateInvoicedOriginal;
    }

    public void setDateInvoicedOriginal(Date dateInvoiced) {
        this.dateInvoicedOriginal = dateInvoiced;
    }
    private BigDecimal hireNetOriginal;
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceOriginal.class);

    public BigDecimal getAdditionalDriverFeeOriginal() {
        return additionalDriverFeeOriginal;
    }

    public void setAdditionalDriverFeeOriginal(BigDecimal additionalDriverFeeOriginal) {
            this.additionalDriverFeeOriginal = additionalDriverFeeOriginal;
    }

    public Integer getAdditionalDriverQtyOriginal() {
        return additionalDriverQtyOriginal;
    }

    public void setAdditionalDriverQtyOriginal(Integer additionalDriverQtyOriginal) {
            this.additionalDriverQtyOriginal = additionalDriverQtyOriginal;
    }

    public BigDecimal getAdminFeeOriginal() {
        return adminFeeOriginal;
    }

    public void setAdminFeeOriginal(BigDecimal adminFeeOriginal) {
            this.adminFeeOriginal = adminFeeOriginal;
    }

    public Integer getAdminQtyOriginal() {
        return adminQtyOriginal;
    }

    public void setAdminQtyOriginal(Integer adminQtyOriginal) {
            this.adminQtyOriginal = adminQtyOriginal;
    }

    public BigDecimal getAutomaticFeeOriginal() {
        return automaticFeeOriginal;
    }

    public void setAutomaticFeeOriginal(BigDecimal automaticFeeOriginal) {
            this.automaticFeeOriginal = automaticFeeOriginal;
    }

    public Integer getAutomaticQtyOriginal() {
        return automaticQtyOriginal;
    }

    public void setAutomaticQtyOriginal(Integer automaticQtyOriginal) {
            this.automaticQtyOriginal = automaticQtyOriginal;
    }

    public BigDecimal getBabySeatFeeOriginal() {
        return babySeatFeeOriginal;
    }

    public void setBabySeatFeeOriginal(BigDecimal babySeatFeeOriginal) {
            this.babySeatFeeOriginal = babySeatFeeOriginal;
    }

    public Integer getBabySeatQtyOriginal() {
        return babySeatQtyOriginal;
    }

    public void setBabySeatQtyOriginal(Integer babySeatQtyOriginal) {
            this.babySeatQtyOriginal = babySeatQtyOriginal;
    }

    public BigDecimal getCollaborationFeeOriginal() {
        return collaborationFeeOriginal;
    }

    public void setCollaborationFeeOriginal(BigDecimal collaborationFeeOriginal) {
        this.collaborationFeeOriginal = collaborationFeeOriginal;
    }

    public Integer getCollaborationQtyOriginal() {
        return collaborationQtyOriginal;
    }

    public void setCollaborationQtyOriginal(Integer collaborationQtyOriginal) {
        this.collaborationQtyOriginal = collaborationQtyOriginal;
    }

    public BigDecimal getMiscellaneousFeeOriginal() {
        return miscellaneousFeeOriginal;
    }

    public void setMiscellaneousFeeOriginal(BigDecimal miscellaneousFeeOriginal) {
            this.miscellaneousFeeOriginal = miscellaneousFeeOriginal;
    }

    public Integer getMiscellaneousQtyOriginal() {
        return miscellaneousQtyOriginal;
    }

    public void setMiscellaneousQtyOriginal(Integer miscellaneousQtyOriginal) {
            this.miscellaneousQtyOriginal = miscellaneousQtyOriginal;
    }

    public BigDecimal getClaimsHandlingInvoiceAmountOriginal() {
        return claimsHandlingInvoiceAmountOriginal;
    }

    public void setClaimsHandlingInvoiceAmountOriginal(BigDecimal claimsHandlingInvoiceAmountOriginal) {
            this.claimsHandlingInvoiceAmountOriginal = claimsHandlingInvoiceAmountOriginal;
    }

    public BigDecimal getDeductionForClaimsHandlingFeeOriginal() {
        return deductionForClaimsHandlingFeeOriginal;
    }

    public void setDeductionForClaimsHandlingFeeOriginal(BigDecimal deductionForClaimsHandlingFeeOriginal) {
            this.deductionForClaimsHandlingFeeOriginal = deductionForClaimsHandlingFeeOriginal;
    }

    public BigDecimal getDeliveryCollectionFeeOriginal() {
        return deliveryCollectionFeeOriginal;
    }

    public void setDeliveryCollectionFeeOriginal(BigDecimal deliveryCollectionFeeOriginal) {
            this.deliveryCollectionFeeOriginal = deliveryCollectionFeeOriginal;
    }

    public Integer getDeliveryCollectionQtyOriginal() {
        return deliveryCollectionQtyOriginal;
    }

    public void setDeliveryCollectionQtyOriginal(Integer deliveryCollectionQtyOriginal) {
            this.deliveryCollectionQtyOriginal = deliveryCollectionQtyOriginal;
    }

    public BigDecimal getInsurerDiscountOriginal() {
        return insurerDiscountOriginal;
    }

    public void setInsurerDiscountOriginal(BigDecimal insurerDiscountOriginal) {
        this.insurerDiscountOriginal = insurerDiscountOriginal;
    }

    public BigDecimal getDiscountOriginal() {
        return discountOriginal;
    }

    public void setDiscountOriginal(BigDecimal discountOriginal) {
            this.discountOriginal = discountOriginal;
    }

    public BigDecimal getDualControlFeeOriginal() {
        return dualControlFeeOriginal;
    }

    public void setDualControlFeeOriginal(BigDecimal dualControlFeeOriginal) {
            this.dualControlFeeOriginal = dualControlFeeOriginal;
    }

    public Integer getDualControlQtyOriginal() {
        return dualControlQtyOriginal;
    }

    public void setDualControlQtyOriginal(Integer dualControlQtyOriginal) {
            this.dualControlQtyOriginal = dualControlQtyOriginal;
    }

    public BigDecimal getEngineerFeeGrossOriginal() {
        return engineerFeeGrossOriginal;
    }

    public void setEngineerFeeGrossOriginal(BigDecimal engineerFeeGrossOriginal) {
            this.engineerFeeGrossOriginal = engineerFeeGrossOriginal;
    }

    public BigDecimal getEngineerFeeNetOriginal() {
        return engineerFeeNetOriginal;
    }

    public void setEngineerFeeNetOriginal(BigDecimal engineerFeeNetOriginal) {
            this.engineerFeeNetOriginal = engineerFeeNetOriginal;
    }

    public BigDecimal getEngineerFeeVatOriginal() {
        return engineerFeeVatOriginal;
    }

    public void setEngineerFeeVatOriginal(BigDecimal engineerFeeVatOriginal) {
            this.engineerFeeVatOriginal = engineerFeeVatOriginal;
    }

    public BigDecimal getEstateFeeOriginal() {
        return estateFeeOriginal;
    }

    public void setEstateFeeOriginal(BigDecimal estateFeeOriginal) {
            this.estateFeeOriginal = estateFeeOriginal;
    }

    public Integer getEstateQtyOriginal() {
        return estateQtyOriginal;
    }

    public void setEstateQtyOriginal(Integer estateQtyOriginal) {
            this.estateQtyOriginal = estateQtyOriginal;
    }

    public BigDecimal getExcessAmountCollectedOriginal() {
        return excessAmountCollectedOriginal;
    }

    public void setExcessAmountCollectedOriginal(BigDecimal excessAmountCollectedOriginal) {
            this.excessAmountCollectedOriginal = excessAmountCollectedOriginal;
    }

    public BigDecimal getFullTotalToPayOriginal() {
        return fullTotalToPayOriginal;
    }

    public void setFullTotalToPayOriginal(BigDecimal fullTotalToPayOriginal) {
            this.fullTotalToPayOriginal = fullTotalToPayOriginal;
    }

    public BigDecimal getHireGrossOriginal() {
        return hireGrossOriginal;
    }

    public void setHireGrossOriginal(BigDecimal hireGrossOriginal) {
            this.hireGrossOriginal = hireGrossOriginal;
    }

    public BigDecimal getHireNetOriginal() {
        return hireNetOriginal;
    }

    public void setHireNetOriginal(BigDecimal hireNetOriginal) {
            this.hireNetOriginal = hireNetOriginal;
    }

    public Date getHirePenaltyChargeAppliedDateOriginal() {
        return hirePenaltyChargeAppliedDateOriginal;
    }
    
    public BigDecimal getHirePenaltyChargeOriginal() {
        return hirePenaltyChargeOriginal;
    }

    public void setHirePenaltyChargeOriginal(BigDecimal hirePenaltyChargeOriginal) {
            this.hirePenaltyChargeOriginal = hirePenaltyChargeOriginal;
    }

    public String getHirePenaltyPercentageOriginal() {
        return hirePenaltyPercentageOriginal;
    }

    public void setHirePenaltyPercentageOriginal(String hirePenaltyPercentageOriginal) {
            this.hirePenaltyPercentageOriginal = hirePenaltyPercentageOriginal;
    }

    public BigDecimal getHireRateChargedPerDayOriginal() {
        return hireRateChargedPerDayOriginal;
    }

    public void setHireRateChargedPerDayOriginal(BigDecimal hireRateChargedPerDayOriginal) {
            this.hireRateChargedPerDayOriginal = hireRateChargedPerDayOriginal;
    }

    public BigDecimal getHireVatOriginal() {
        return hireVatOriginal;
    }

    public void setHireVatOriginal(BigDecimal hireVatOriginal) {
            this.hireVatOriginal = hireVatOriginal;
    }

    public BigDecimal getInterimPaymentOriginal() {
        return interimPaymentOriginal;
    }

    public void setInterimPaymentOriginal(BigDecimal interimPaymentOriginal) {
            this.interimPaymentOriginal = interimPaymentOriginal;
    }

    public BigDecimal getNonStandardInsurancePremiumFeeOriginal() {
        return nonStandardInsurancePremiumFeeOriginal;
    }

    public void setNonStandardInsurancePremiumFeeOriginal(BigDecimal nonStandardInsurancePremiumFeeOriginal) {
            this.nonStandardInsurancePremiumFeeOriginal = nonStandardInsurancePremiumFeeOriginal;
    }

    public Integer getNonStandardInsurancePremiumQtyOriginal() {
        return nonStandardInsurancePremiumQtyOriginal;
    }

    public void setNonStandardInsurancePremiumQtyOriginal(Integer nonStandardInsurancePremiumQtyOriginal) {
            this.nonStandardInsurancePremiumQtyOriginal = nonStandardInsurancePremiumQtyOriginal;
    }

    public BigDecimal getRepairGrossOriginal() {
        return repairGrossOriginal;
    }

    public void setRepairGrossOriginal(BigDecimal repairGrossOriginal) {
            this.repairGrossOriginal = repairGrossOriginal;
    }

    public BigDecimal getRepairNetOriginal() {
        return repairNetOriginal;
    }

    public void setRepairNetOriginal(BigDecimal repairNetOriginal) {
            this.repairNetOriginal = repairNetOriginal;
    }

    public Date getRepairPenaltyChargeAppliedDateOriginal() {
        return repairPenaltyChargeAppliedDateOriginal;
    }

    public BigDecimal getRepairPenaltyChargeOriginal() {
        return repairPenaltyChargeOriginal;
    }

    public void setRepairPenaltyChargeOriginal(BigDecimal repairPenaltyChargeOriginal) {
            this.repairPenaltyChargeOriginal = repairPenaltyChargeOriginal;
    }

    public String getRepairPenaltyPercentageOriginal() {
        return repairPenaltyPercentageOriginal;
    }

    public void setRepairPenaltyPercentageOriginal(String repairPenaltyPercentageOriginal) {
            this.repairPenaltyPercentageOriginal = repairPenaltyPercentageOriginal;
    }

    public BigDecimal getRepairVatOriginal() {
        return repairVatOriginal;
    }

    public void setRepairVatOriginal(BigDecimal repairVatOriginal) {
            this.repairVatOriginal = repairVatOriginal;
    }

    public BigDecimal getRoofRackFeeOriginal() {
        return roofRackFeeOriginal;
    }

    public void setRoofRackFeeOriginal(BigDecimal roofRackFeeOriginal) {
            this.roofRackFeeOriginal = roofRackFeeOriginal;
    }

    public Integer getRoofRackQtyOriginal() {
        return roofRackQtyOriginal;
    }

    public void setRoofRackQtyOriginal(Integer roofRackQtyOriginal) {
            this.roofRackQtyOriginal = roofRackQtyOriginal;
    }

    public BigDecimal getSatNavFeeOriginal() {
        return satNavFeeOriginal;
    }

    public void setSatNavFeeOriginal(BigDecimal satNavFeeOriginal) {
            this.satNavFeeOriginal = satNavFeeOriginal;
    }

    public Integer getSatNavQtyOriginal() {
        return satNavQtyOriginal;
    }

    public void setSatNavQtyOriginal(Integer satNavQtyOriginal) {
            this.satNavQtyOriginal = satNavQtyOriginal;
    }

    public BigDecimal getStorageRecoveryGrossOriginal() {
        return storageRecoveryGrossOriginal;
    }

    public void setStorageRecoveryGrossOriginal(BigDecimal storageRecoveryGrossOriginal) {
            this.storageRecoveryGrossOriginal = storageRecoveryGrossOriginal;
    }

    public BigDecimal getStorageRecoveryNetOriginal() {
        return storageRecoveryNetOriginal;
    }

    public void setStorageRecoveryNetOriginal(BigDecimal storageRecoveryNetOriginal) {
            this.storageRecoveryNetOriginal = storageRecoveryNetOriginal;
    }

    public BigDecimal getStorageRecoveryVatOriginal() {
        return storageRecoveryVatOriginal;
    }

    public void setStorageRecoveryVatOriginal(BigDecimal storageRecoveryVatOriginal) {
            this.storageRecoveryVatOriginal = storageRecoveryVatOriginal;
    }

    public BigDecimal getTotalGrossOriginal() {
        return totalGrossOriginal;
    }

    public void setTotalGrossOriginal(BigDecimal totalGrossOriginal) {
            this.totalGrossOriginal = totalGrossOriginal;
    }

    public BigDecimal getTotalLossFeeGrossOriginal() {
        return totalLossFeeGrossOriginal;
    }

    public void setTotalLossFeeGrossOriginal(BigDecimal totalLossFeeGrossOriginal) {
            this.totalLossFeeGrossOriginal = totalLossFeeGrossOriginal;
    }

    public BigDecimal getTotalLossFeeNetOriginal() {
        return totalLossFeeNetOriginal;
    }

    public void setTotalLossFeeNetOriginal(BigDecimal totalLossFeeNetOriginal) {
            this.totalLossFeeNetOriginal = totalLossFeeNetOriginal;
    }

    public BigDecimal getTotalLossFeeVatOriginal() {
        return totalLossFeeVatOriginal;
    }

    public void setTotalLossFeeVatOriginal(BigDecimal totalLossFeeVatOriginal) {
            this.totalLossFeeVatOriginal = totalLossFeeVatOriginal;
    }

    public BigDecimal getTotalNetOriginal() {
        return totalNetOriginal;
    }

    public void setTotalNetOriginal(BigDecimal totalNetOriginal) {
            this.totalNetOriginal = totalNetOriginal;
    }

    public BigDecimal getTotalPenaltyChargeOriginal() {
        return totalPenaltyChargeOriginal;
    }

    public void setTotalPenaltyChargeOriginal(BigDecimal totalPenaltyChargeOriginal) {
            this.totalPenaltyChargeOriginal = totalPenaltyChargeOriginal;
    }

    public BigDecimal getTotalToPayOriginal() {
        return totalToPayOriginal;
    }

    public void setTotalToPayOriginal(BigDecimal totalToPayOriginal) {
            this.totalToPayOriginal = totalToPayOriginal;
    }

    public BigDecimal getTotalVatOriginal() {
        return totalVatOriginal;
    }

    public void setTotalVatOriginal(BigDecimal totalVatOriginal) {
            this.totalVatOriginal = totalVatOriginal;
    }

    public BigDecimal getTowBarsFeeOriginal() {
        return towBarsFeeOriginal;
    }

    public void setTowBarsFeeOriginal(BigDecimal towBarsFeeOriginal) {
            this.towBarsFeeOriginal = towBarsFeeOriginal;
    }

    public Integer getTowBarsQtyOriginal() {
        return towBarsQtyOriginal;
    }

    public void setTowBarsQtyOriginal(Integer towBarsQtyOriginal) {
            this.towBarsQtyOriginal = towBarsQtyOriginal;
    }

    public BigDecimal getVatAmountCollectedOriginal() {
        return vatAmountCollectedOriginal;
    }

    public void setVatAmountCollectedOriginal(BigDecimal vatAmountCollectedOriginal) {
            this.vatAmountCollectedOriginal = vatAmountCollectedOriginal;
    }
}
