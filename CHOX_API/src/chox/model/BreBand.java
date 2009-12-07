package chox.model;

import java.io.Serializable;
import java.math.BigDecimal;
import scsbre.model.IBREBandInfo;

public class BreBand extends AuditableEntity implements Serializable, IBREBandInfo {

    protected Insurer insurer;
    protected boolean isActive;
    protected int takeVehicleToGarageDaysMobile;
    protected int takeVehicleToGarageDaysNonMobile;
    protected int weekendBufferDays;
    protected int takeVehicleOutDays;
    protected int engineerInspectionDelayDays;
    protected int isMobileDayAllowance;
    protected int offerMadeDays;
    protected int receiptOfFinalStatementChequeDays;
    protected int inspectionDelayDays;
    protected BigDecimal hireRateChargeTolerance;
    protected BigDecimal hireNetCeiling;
    protected int hireDayCeiling;
    protected BigDecimal repairNetCeiling;
    protected int isNotMobileDayAllowance;
    protected int averageLabourRate;
    protected int averageLabourHoursPerHireDay;
    protected String name;
    protected VehicleClassCeiling vehicleClassCeiling;
    protected boolean automaticChargeCheck;
    protected boolean estateChargeCheck;
    protected boolean nonStandardRiskInsurancePremiumCheck;
    protected boolean cdwChargeCheck;
    protected boolean satelliteNavigationChargeCheck;
    protected boolean babySeatChargeCheck;
    protected boolean towBarsChargeCheck;
    protected boolean roofRackChargeCheck;
    protected boolean deliveryOrCollectionChargeCheck;
    protected boolean dualControlChargeCheck;
    protected boolean hasAllowedVehicleClass;
    protected boolean hasCalculatedCorrectDailyRate;
    protected boolean hireNetDoesNotExceedVehicleClassHireNetCeiling;
    protected boolean hireDayCountDoesNotExceedBandHireDayCeiling;
    protected boolean hasCorrectHireGrossCalculation;
    protected boolean actualHireDaysDoesNotExceedAllowableHireDays;
    protected boolean actualHireDaysDoesNotExceedTotalLossInspection;
    protected boolean repairGrossIsLessThanEstimatedTotalRepairAmount;
    protected boolean hasCorrectHireVatCalculation;
    protected boolean hasCorrectRepairVatCalculation;
    protected boolean hasCorrectRepairGrossCalculation;
    protected boolean hasCorrectTotalNet;
    protected boolean hasCorrectTotalVat;
    protected boolean hasCalculatedTotalGrossEqualSuppliedTotalGross;
    protected boolean hasCorrectDiscountForNonDA;
    protected boolean handlingAmountAndDeductionBothEqualZeroForNonDA;
    protected boolean claimHasZeroDiscountForDA;
    protected boolean handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero;
    protected boolean hasSuppliedCorrectTotalToPay;
    protected boolean estimatedRepairDaysPlusBandDaysDoNotExceedHireDays;
    protected boolean validateUniqueVehicleRegistrationNumber;
    protected boolean labourCostBusinessRule;
    protected boolean repairNetDoesNotExceedVehicleClassRepairNetCeiling;
    protected boolean numberOfHireDaysReconcile;
    protected boolean correntAdminFee;
    protected boolean repairBookedInDate;
    protected boolean flaggedForManualInvoiceReview;
    protected boolean hireNetDoesNotExceedBandHireNetCeiling;
    protected boolean repairNetDoesNotExceedBandRepairNetCeiling;

    public BreBand() {
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getTakeVehicleToGarageDaysMobile() {
        return takeVehicleToGarageDaysMobile;
    }

    public void setTakeVehicleToGarageDaysMobile(int takeVehicleToGarageDaysMobile) {
        this.takeVehicleToGarageDaysMobile = takeVehicleToGarageDaysMobile;
    }

    public int getTakeVehicleToGarageDaysNonMobile() {
        return takeVehicleToGarageDaysNonMobile;
    }

    public void setTakeVehicleToGarageDaysNonMobile(int takeVehicleToGarageDaysNonMobile) {
        this.takeVehicleToGarageDaysNonMobile = takeVehicleToGarageDaysNonMobile;
    }

    public int getWeekendBufferDays() {
        return weekendBufferDays;
    }

    public void setWeekendBufferDays(int weekendBufferDays) {
        this.weekendBufferDays = weekendBufferDays;
    }

    public int getTakeVehicleOutDays() {
        return takeVehicleOutDays;
    }

    public void setTakeVehicleOutDays(int takeVehicleOutDays) {
        this.takeVehicleOutDays = takeVehicleOutDays;
    }

    public int getEngineerInspectionDelayDays() {
        return engineerInspectionDelayDays;
    }

    public void setEngineerInspectionDelayDays(int engineerInspectionDelayDays) {
        this.engineerInspectionDelayDays = engineerInspectionDelayDays;
    }

    public int getIsMobileDayAllowance() {
        return isMobileDayAllowance;
    }

    public void setIsMobileDayAllowance(int isMobileDayAllowance) {
        this.isMobileDayAllowance = isMobileDayAllowance;
    }

    public int getOfferMadeDays() {
        return offerMadeDays;
    }

    public void setOfferMadeDays(int offerMadeDays) {
        this.offerMadeDays = offerMadeDays;
    }

    public int getReceiptOfFinalStatementChequeDays() {
        return receiptOfFinalStatementChequeDays;
    }

    public void setReceiptOfFinalStatementChequeDays(int receiptOfFinalStatementChequeDays) {
        this.receiptOfFinalStatementChequeDays = receiptOfFinalStatementChequeDays;
    }

    public int getInspectionDelayDays() {
        return inspectionDelayDays;
    }

    public void setInspectionDelayDays(int inspectionDelayDays) {
        this.inspectionDelayDays = inspectionDelayDays;
    }

    public java.math.BigDecimal getHireRateChargeTolerance() {
        if (hireRateChargeTolerance == null) {
            hireRateChargeTolerance = new BigDecimal(0.00);
        }
        return hireRateChargeTolerance;
    }

    public void setHireRateChargeTolerance(java.math.BigDecimal hireRateChargeTolerance) {
        this.hireRateChargeTolerance = hireRateChargeTolerance;
    }

    public void setHireNetCeiling(java.math.BigDecimal hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public int getHireDayCeiling() {
        return hireDayCeiling;
    }

    public void setHireDayCeiling(int hireDayCeiling) {
        this.hireDayCeiling = hireDayCeiling;
    }


    public void setRepairNetCeiling(java.math.BigDecimal repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
    }
    
    public int getIsNotMobileDayAllowance() {
        return isNotMobileDayAllowance;
    }

    public void setIsNotMobileDayAllowance(int isNotMobileDayAllowance) {
        this.isNotMobileDayAllowance = isNotMobileDayAllowance;
    }

    public int getAverageLabourHoursPerHireDay() {
        return averageLabourHoursPerHireDay;
    }

    public void setAverageLabourHoursPerHireDay(int averageLabourHoursPerHireDay) {
        this.averageLabourHoursPerHireDay = averageLabourHoursPerHireDay;
    }

    public int getAverageLabourRate() {
        return averageLabourRate;
    }

    public void setAverageLabourRate(int averageLabourRate) {
        this.averageLabourRate = averageLabourRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling) {
        this.vehicleClassCeiling = vehicleClassCeiling;
    }

    public java.math.BigDecimal getRepairNetCeiling() {

        if (repairNetCeiling == null) {
            repairNetCeiling = new BigDecimal(0.00);
        }
        
        return repairNetCeiling;
    }

    public java.math.BigDecimal getHireNetCeiling() {
        if (hireNetCeiling == null) {
            hireNetCeiling = new BigDecimal(0.00);
        }
        return hireNetCeiling;
    }


    public java.math.BigDecimal getMaxRepairNetCeiling() {

        BigDecimal maxRepairNetCeiling = new BigDecimal(100000);

        if(vehicleClassCeiling!=null){
            maxRepairNetCeiling = vehicleClassCeiling.getRepairNetCeiling();
        }

        return maxRepairNetCeiling;
    }
    
    public java.math.BigDecimal getMaxHireNetCeiling() {
        
        BigDecimal maxHireNetCeiling = new BigDecimal(100000);
        
        if(vehicleClassCeiling!=null){
            maxHireNetCeiling = vehicleClassCeiling.getHireNetCeiling();
        }

        return maxHireNetCeiling;
    }

    public boolean isAutomaticChargeCheck() {
        return automaticChargeCheck;
    }

    public void setAutomaticChargeCheck(boolean automaticChargeCheck) {
        this.automaticChargeCheck = automaticChargeCheck;
    }

    public boolean isBabySeatChargeCheck() {
        return babySeatChargeCheck;
    }

    public void setBabySeatChargeCheck(boolean babySeatChargeCheck) {
        this.babySeatChargeCheck = babySeatChargeCheck;
    }

    public boolean isCdwChargeCheck() {
        return cdwChargeCheck;
    }

    public void setCdwChargeCheck(boolean cdwChargeCheck) {
        this.cdwChargeCheck = cdwChargeCheck;
    }

    public boolean isDeliveryOrCollectionChargeCheck() {
        return deliveryOrCollectionChargeCheck;
    }

    public void setDeliveryOrCollectionChargeCheck(boolean deliveryOrCollectionChargeCheck) {
        this.deliveryOrCollectionChargeCheck = deliveryOrCollectionChargeCheck;
    }

    public boolean isDualControlChargeCheck() {
        return dualControlChargeCheck;
    }

    public void setDualControlChargeCheck(boolean dualControlChargeCheck) {
        this.dualControlChargeCheck = dualControlChargeCheck;
    }

    public boolean isEstateChargeCheck() {
        return estateChargeCheck;
    }

    public void setEstateChargeCheck(boolean estateChargeCheck) {
        this.estateChargeCheck = estateChargeCheck;
    }

    public boolean isNonStandardRiskInsurancePremiumCheck() {
        return nonStandardRiskInsurancePremiumCheck;
    }

    public void setNonStandardRiskInsurancePremiumCheck(boolean nonStandardRiskInsurancePremiumCheck) {
        this.nonStandardRiskInsurancePremiumCheck = nonStandardRiskInsurancePremiumCheck;
    }

    public boolean isRoofRackChargeCheck() {
        return roofRackChargeCheck;
    }

    public void setRoofRackChargeCheck(boolean roofRackChargeCheck) {
        this.roofRackChargeCheck = roofRackChargeCheck;
    }

    public boolean isSatelliteNavigationChargeCheck() {
        return satelliteNavigationChargeCheck;
    }

    public void setSatelliteNavigationChargeCheck(boolean satelliteNavigationChargeCheck) {
        this.satelliteNavigationChargeCheck = satelliteNavigationChargeCheck;
    }

    public boolean isTowBarsChargeCheck() {
        return towBarsChargeCheck;
    }

    public void setTowBarsChargeCheck(boolean towBarsChargeCheck) {
        this.towBarsChargeCheck = towBarsChargeCheck;
    }

    public boolean isActualHireDaysDoesNotExceedAllowableHireDays() {
        return actualHireDaysDoesNotExceedAllowableHireDays;
    }

    public void setActualHireDaysDoesNotExceedAllowableHireDays(boolean actualHireDaysDoesNotExceedAllowableHireDays) {
        this.actualHireDaysDoesNotExceedAllowableHireDays = actualHireDaysDoesNotExceedAllowableHireDays;
    }

    public boolean isActualHireDaysDoesNotExceedTotalLossInspection() {
        return actualHireDaysDoesNotExceedTotalLossInspection;
    }

    public void setActualHireDaysDoesNotExceedTotalLossInspection(boolean actualHireDaysDoesNotExceedTotalLossInspection) {
        this.actualHireDaysDoesNotExceedTotalLossInspection = actualHireDaysDoesNotExceedTotalLossInspection;
    }

    public boolean isClaimHasZeroDiscountForDA() {
        return claimHasZeroDiscountForDA;
    }

    public void setClaimHasZeroDiscountForDA(boolean claimHasZeroDiscountForDA) {
        this.claimHasZeroDiscountForDA = claimHasZeroDiscountForDA;
    }

    public boolean isCorrentAdminFee() {
        return correntAdminFee;
    }

    public void setCorrentAdminFee(boolean correntAdminFee) {
        this.correntAdminFee = correntAdminFee;
    }

    public boolean isEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays() {
        return estimatedRepairDaysPlusBandDaysDoNotExceedHireDays;
    }

    public void setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(boolean estimatedRepairDaysPlusBandDaysDoNotExceedHireDays) {
        this.estimatedRepairDaysPlusBandDaysDoNotExceedHireDays = estimatedRepairDaysPlusBandDaysDoNotExceedHireDays;
    }

    public boolean isFlaggedForManualInvoiceReview() {
        return flaggedForManualInvoiceReview;
    }

    public void setFlaggedForManualInvoiceReview(boolean flaggedForManualInvoiceReview) {
        this.flaggedForManualInvoiceReview = flaggedForManualInvoiceReview;
    }

    public boolean isHandlingAmountAndDeductionBothEqualZeroForNonDA() {
        return handlingAmountAndDeductionBothEqualZeroForNonDA;
    }

    public void setHandlingAmountAndDeductionBothEqualZeroForNonDA(boolean handlingAmountAndDeductionBothEqualZeroForNonDA) {
        this.handlingAmountAndDeductionBothEqualZeroForNonDA = handlingAmountAndDeductionBothEqualZeroForNonDA;
    }

    public boolean isHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero() {
        return handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero;
    }

    public void setHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero(boolean handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero) {
        this.handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero = handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero;
    }

    public boolean isHasAllowedVehicleClass() {
        return hasAllowedVehicleClass;
    }

    public void setHasAllowedVehicleClass(boolean hasAllowedVehicleClass) {
        this.hasAllowedVehicleClass = hasAllowedVehicleClass;
    }

    public boolean isHasCalculatedCorrectDailyRate() {
        return hasCalculatedCorrectDailyRate;
    }

    public void setHasCalculatedCorrectDailyRate(boolean hasCalculatedCorrectDailyRate) {
        this.hasCalculatedCorrectDailyRate = hasCalculatedCorrectDailyRate;
    }

    public boolean isHasCalculatedTotalGrossEqualSuppliedTotalGross() {
        return hasCalculatedTotalGrossEqualSuppliedTotalGross;
    }

    public void setHasCalculatedTotalGrossEqualSuppliedTotalGross(boolean hasCalculatedTotalGrossEqualSuppliedTotalGross) {
        this.hasCalculatedTotalGrossEqualSuppliedTotalGross = hasCalculatedTotalGrossEqualSuppliedTotalGross;
    }

    public boolean isHasCorrectDiscountForNonDA() {
        return hasCorrectDiscountForNonDA;
    }

    public void setHasCorrectDiscountForNonDA(boolean hasCorrectDiscountForNonDA) {
        this.hasCorrectDiscountForNonDA = hasCorrectDiscountForNonDA;
    }

    public boolean isHasCorrectHireGrossCalculation() {
        return hasCorrectHireGrossCalculation;
    }

    public void setHasCorrectHireGrossCalculation(boolean hasCorrectHireGrossCalculation) {
        this.hasCorrectHireGrossCalculation = hasCorrectHireGrossCalculation;
    }

    public boolean isHasCorrectHireVatCalculation() {
        return hasCorrectHireVatCalculation;
    }

    public void setHasCorrectHireVatCalculation(boolean hasCorrectHireVatCalculation) {
        this.hasCorrectHireVatCalculation = hasCorrectHireVatCalculation;
    }

    public boolean isHasCorrectRepairGrossCalculation() {
        return hasCorrectRepairGrossCalculation;
    }

    public void setHasCorrectRepairGrossCalculation(boolean hasCorrectRepairGrossCalculation) {
        this.hasCorrectRepairGrossCalculation = hasCorrectRepairGrossCalculation;
    }

    public boolean isHasCorrectRepairVatCalculation() {
        return hasCorrectRepairVatCalculation;
    }

    public void setHasCorrectRepairVatCalculation(boolean hasCorrectRepairVatCalculation) {
        this.hasCorrectRepairVatCalculation = hasCorrectRepairVatCalculation;
    }

    public boolean isHasCorrectTotalNet() {
        return hasCorrectTotalNet;
    }

    public void setHasCorrectTotalNet(boolean hasCorrectTotalNet) {
        this.hasCorrectTotalNet = hasCorrectTotalNet;
    }

    public boolean isHasCorrectTotalVat() {
        return hasCorrectTotalVat;
    }

    public void setHasCorrectTotalVat(boolean hasCorrectTotalVat) {
        this.hasCorrectTotalVat = hasCorrectTotalVat;
    }

    public boolean isHasSuppliedCorrectTotalToPay() {
        return hasSuppliedCorrectTotalToPay;
    }

    public void setHasSuppliedCorrectTotalToPay(boolean hasSuppliedCorrectTotalToPay) {
        this.hasSuppliedCorrectTotalToPay = hasSuppliedCorrectTotalToPay;
    }

    public boolean isHireDayCountDoesNotExceedBandHireDayCeiling() {
        return hireDayCountDoesNotExceedBandHireDayCeiling;
    }

    public void setHireDayCountDoesNotExceedBandHireDayCeiling(boolean hireDayCountDoesNotExceedBandHireDayCeiling) {
        this.hireDayCountDoesNotExceedBandHireDayCeiling = hireDayCountDoesNotExceedBandHireDayCeiling;
    }

    public boolean isHireNetDoesNotExceedVehicleClassHireNetCeiling() {
        return hireNetDoesNotExceedVehicleClassHireNetCeiling;
    }

    public void setHireNetDoesNotExceedVehicleClassHireNetCeiling(boolean hireNetDoesNotExceedVehicleClassHireNetCeiling) {
        this.hireNetDoesNotExceedVehicleClassHireNetCeiling = hireNetDoesNotExceedVehicleClassHireNetCeiling;
    }

    public boolean isLabourCostBusinessRule() {
        return labourCostBusinessRule;
    }

    public void setLabourCostBusinessRule(boolean labourCostBusinessRule) {
        this.labourCostBusinessRule = labourCostBusinessRule;
    }

    public boolean isNumberOfHireDaysReconcile() {
        return numberOfHireDaysReconcile;
    }

    public void setNumberOfHireDaysReconcile(boolean numberOfHireDaysReconcile) {
        this.numberOfHireDaysReconcile = numberOfHireDaysReconcile;
    }

    public boolean isRepairBookedInDate() {
        return repairBookedInDate;
    }

    public void setRepairBookedInDate(boolean repairBookedInDate) {
        this.repairBookedInDate = repairBookedInDate;
    }

    public boolean isRepairGrossIsLessThanEstimatedTotalRepairAmount() {
        return repairGrossIsLessThanEstimatedTotalRepairAmount;
    }

    public void setRepairGrossIsLessThanEstimatedTotalRepairAmount(boolean repairGrossIsLessThanEstimatedTotalRepairAmount) {
        this.repairGrossIsLessThanEstimatedTotalRepairAmount = repairGrossIsLessThanEstimatedTotalRepairAmount;
    }

    public boolean isRepairNetDoesNotExceedVehicleClassRepairNetCeiling() {
        return repairNetDoesNotExceedVehicleClassRepairNetCeiling;
    }

    public void setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(boolean repairNetDoesNotExceedVehicleClassRepairNetCeiling) {
        this.repairNetDoesNotExceedVehicleClassRepairNetCeiling = repairNetDoesNotExceedVehicleClassRepairNetCeiling;
    }

    public boolean isValidateUniqueVehicleRegistrationNumber() {
        return validateUniqueVehicleRegistrationNumber;
    }

    public void setValidateUniqueVehicleRegistrationNumber(boolean validateUniqueVehicleRegistrationNumber) {
        this.validateUniqueVehicleRegistrationNumber = validateUniqueVehicleRegistrationNumber;
    }

    public boolean isHireNetDoesNotExceedBandHireNetCeiling() {
        return hireNetDoesNotExceedBandHireNetCeiling;
    }

    public void setHireNetDoesNotExceedBandHireNetCeiling(boolean hireNetDoesNotExceedBandHireNetCeiling) {
        this.hireNetDoesNotExceedBandHireNetCeiling = hireNetDoesNotExceedBandHireNetCeiling;
    }

    public boolean isRepairNetDoesNotExceedBandRepairNetCeiling() {
        return repairNetDoesNotExceedBandRepairNetCeiling;
    }

    public void setRepairNetDoesNotExceedBandRepairNetCeiling(boolean repairNetDoesNotExceedBandRepairNetCeiling) {
        this.repairNetDoesNotExceedBandRepairNetCeiling = repairNetDoesNotExceedBandRepairNetCeiling;
    }

}