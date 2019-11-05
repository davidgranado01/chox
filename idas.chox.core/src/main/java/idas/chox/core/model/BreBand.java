package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BreBand extends Entity implements Serializable, FullAudit {
    private Insurer insurer;
    private boolean isActive;
    private int takeVehicleToGarageDaysMobile;
    private int takeVehicleToGarageDaysNonMobile;
    private int weekendBufferDays;
    private int takeVehicleOutDays;
    private int engineerInspectionDelayDaysMobile;
    private int engineerInspectionDelayDaysNonMobile;
    private int isMobileDayAllowance;
    private int offerMadeDays;
    private int receiptOfFinalStatementChequeDays;
    private int inspectionDelayDays;
    private BigDecimal hireRateChargeTolerance;
    private BigDecimal hireNetCeiling;
    private int hireDayCeiling;
    private BigDecimal repairNetCeiling;
    private int isNotMobileDayAllowance;
    private int averageLabourRateStandard;
    private int averageLabourRatePrestige;
    private int averageLabourHoursPerHireDay;
    private String name;
    private String claimUploadNote;
    private VehicleClassCeiling vehicleClassCeiling;
    private boolean automaticChargeCheck;
    private boolean automaticChargeCheckHpiLookup;
    private boolean additionalDriverChargeCheck;
    private boolean estateChargeCheck;
    private boolean estateChargeCheckHpi;
    private boolean nonStandardRiskInsurancePremiumCheck;
    private boolean miscellaneousChargeCheck;
    private boolean satelliteNavigationChargeCheck;
    private boolean babySeatChargeCheck;
    private boolean towBarsChargeCheck;
    private boolean roofRackChargeCheck;
    private boolean deliveryOrCollectionChargeCheck;
    private boolean dualControlChargeCheck;
    private boolean hasAllowedVehicleClass;
    private boolean hasCalculatedCorrectDailyRate;
    private boolean hireNetDoesNotExceedVehicleClassHireNetCeiling;
    private boolean hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling;
    private boolean hireDayCountDoesNotExceedBandHireDayCeiling;
    private boolean actualHireDaysDoesNotExceedAllowableHireDays;
    private boolean actualHireDaysDoesNotExceedTotalLossInspection;
    private boolean repairGrossIsLessThanEstimatedTotalRepairAmount;
    private boolean hasCorrectTotalLossGrossCalculation;
    private boolean hasCorrectTotalLossVatCalculation;
    private boolean hasCorrectHireGrossCalculation;
    private boolean hasCorrectHireVatCalculation;
    private boolean hasCorrectRepairVatCalculation;
    private boolean hasCorrectRepairGrossCalculation;
    private boolean hasCorrectTotalNet;
    private boolean hasCorrectTotalVat;
    private boolean hasCalculatedTotalGrossEqualSuppliedTotalGross;
    private boolean hasCorrectDiscountForNonDA;
    private boolean handlingAmountAndDeductionBothEqualZeroForNonDA;
    private boolean claimHasZeroDiscountForDA;
    private boolean handlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero;
    private boolean hasSuppliedCorrectTotalToPay;
    private boolean estimatedRepairDaysPlusBandDaysDoNotExceedHireDays;
    private boolean validateUniqueVehicleRegistrationNumber;
    private boolean labourCostBusinessRule;
    private boolean repairNetDoesNotExceedVehicleClassRepairNetCeiling;
    private boolean repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling;
    private boolean numberOfHireDaysReconcile;
    private boolean correntAdminFee;
    private boolean repairBookedInDateOnThursday;
    private boolean repairBookedInDateOnFriday;
    private boolean repairBookedInDateOnSaturday;
    private boolean repairBookedInDateOnSunday;
    private boolean flaggedForManualInvoiceReview;
    private boolean hireNetDoesNotExceedBandHireNetCeiling;
    private boolean repairNetDoesNotExceedBandRepairNetCeiling;
    private boolean hasHireGrossSumCheck;
    private boolean hasRepairGrossSumCheck;
    private boolean hasTotalLossFeeGrossSumCheck;
    private boolean hasTotalGrossSumCheck;
    private boolean vehicleClassHireProvisionLikeForLike6To8;
    private boolean vehicleClassHireProvisionLikeForLike8To9;
    private boolean vehicleClassHireProvisionLikeForLikeOver9;
    private boolean vehicleClassHireProvisionLikeForLike6To8SP;
    private boolean vehicleClassHireProvisionLikeForLike8To9SP;
    private boolean vehicleClassHireProvisionLikeForLikeOver9SP;
    private boolean hireVatLimitCheck;
    private boolean repairVatLimitCheck;
    private boolean totalLossFeeVatLimitCheck;
    private boolean totalVatLimitCheck;
    private boolean storageRecoveryVatLimitCheck;
    private boolean engineerFeeVatLimitCheck;
    private boolean hireVatHireEndCheck;
    private boolean hireVatInvoicedDateCheck;
    private boolean repairVatCompletionDateCheck;
    private boolean useSupplierRates;
    private boolean totalLabourCostBusinessRule;
    private boolean dateRepairCommencedChkForNonMobileVehicle;
    private boolean dateRepairBookInDateChkForMobileVehicle;
    private boolean dateRepairBookInDateChkForNonMobileVehicle;
    private boolean supplierAdminstrationFee;
    private boolean autoRestoreOneDayRepairCheck;
    private boolean insurancePremiumTaxCheck;
    private boolean mobileVehicleTotalLossCheck;
    private boolean allowGTAPenaltyCharges = true;
    private boolean allowGTAAutoPenaltyCharges = true;
    private boolean allowSubscriberPenaltyCharges = true;
    private boolean allowSubscriberAutoPenaltyCharges = true;
    private boolean allowFixedFeePenaltyCharges = true;
    private boolean allowFixedFeeAutoPenaltyCharges = true;
    private boolean allowCollaborationProtocolPenaltyCharges = true;
    private boolean allowCollaborationProtocolAutoPenaltyCharges = true;
    private boolean allowTPIPenaltyCharges = true;
    private boolean allowTPIAutoPenaltyCharges = false;
    private boolean allowInsurervsInsurerPenaltyCharges = true;
    private boolean allowInsurervsInsurerAutoPenaltyCharges = false;
    private boolean allowManualInvoicePenaltyCharges = true;
    private boolean allowManualInvoiceAutoPenaltyCharges = false;
    private int hireDaysPriorToDateRepairCommenced;
    private int hireDaysPriorToDateRepairBookInDateNonMobileVehicles;
    private int hireDaysPriorToDateRepairBookInDateMobileVehicles;
    private int numberOfDays = 4;
    private int subscriberSlaDays = 5;
    private int subscriberResubmissionAllowed = 2;
    private String subscriberTimeCutOff = "15:00";
    private int fixedFeeSlaDays = 14;
    private int fixedFeeResubmissionAllowed = 2;
    private String fixedFeeTimeCutOff = "15:00";
    private BigDecimal adminFeeCeilingSubscriberManagingRepair = new BigDecimal("50.00");
    private BigDecimal adminFeeCeilingSubscriber = new BigDecimal("50.00");
    private BigDecimal adminFeeCeiling = new BigDecimal("27.50");
    private BigDecimal adminFeeCeilingManagingRepair = new BigDecimal("27.50");
    private BigDecimal standardInsurancePremium = new BigDecimal("3.00");
    private BigDecimal nonStandardInsurancePremium = new BigDecimal("5.75");
    private BigDecimal hireVatTolerance;
    private BigDecimal repairVatTolerance;
    private BigDecimal totalVatTolerance;
    private BigDecimal nonStandardInsurancePremiumCeilingTolerance;
    private BigDecimal fullTotalRequestedCeilingTolerance;
    private String nameOfRepairer = "Autorestore ltd";
    private boolean subscriberCheckRejectedClaims;
    private boolean subscriberAcquisitionFeeCheck;
    private boolean subscriberAdminFeeCheck;
    private boolean hireTerminatedAfterRepairCompletionCheck;
    private boolean allowManagingRepairAutomatedTasks;
    private boolean allowNotManagingRepairAutomatedTasks;
    private boolean allowMissingECDAutomatedTasks;
    private boolean allowOnHireAutomatedTasks;
    private boolean overlappingHireCheck;
    private boolean maximumLabourRateCheck;
    private boolean maximumLabourRateStandardCheck;
    private boolean maximumLabourRatePrestigeCheck;
    private BigDecimal maxAllowedLabourRate = BigDecimal.ZERO;
    private BigDecimal maxAllowedLabourStandardRate = BigDecimal.ZERO;
    private BigDecimal maxAllowedLabourPrestigeRate = BigDecimal.ZERO;
    private BigDecimal maxAllowedEngineerNetFee = new BigDecimal("50.00");
    private BigDecimal maxAllowedTotalLossNetFee = BigDecimal.ZERO;
    private boolean fixedFeeAdminFeeCheck;
    private BigDecimal adminFeeCeilingFixedFeeManagingRepair = new BigDecimal("60.00");
    private BigDecimal adminFeeCeilingFixedFee = new BigDecimal("40.00");
    private List<ProtocolVehicleClassCeiling> protocolVehicleClassCeilings;
    private List<BrePenaltyBand> brePenaltyBands;
    private List<ClaimMatchingBand> claimMatchingBands;
    private List<BreAppliedLiability> appliedLiabilities;
    private boolean compoundAutomaticChargeCheckHpiLookup;
    private boolean compoundEstateChargeCheckHpiLookup;
    private boolean compoundAutomaticEstateChargeCheckHpiLookup;
    private boolean clientVatRegisteredCheck;
    private boolean engineerNetFeeCheck;
    private boolean totalLossFeeNetCeilingCheck;
    private boolean totalLossAndStorageFeeCheck;
    private boolean paymentTeamActive;
    private boolean enableClaimAudit;
    private BigDecimal auditProcessPercentage = new BigDecimal(BigInteger.ZERO);
    private boolean breInvoiceSavingActive;
    private boolean pauseSubscriberSlaClock;
    private boolean pauseFixedFeeSlaClock;
    private boolean storageRecoveryNetCeilingCheck;
    private boolean ecdVsRepairCompletionDateCheck;
    private boolean fullTotalRequestedCeilingCheck;
    private BigDecimal storageRecoveryNetCeiling = BigDecimal.ZERO;
    private boolean enableGtaDiscount;
    private boolean fraudCheckEnable;
    private boolean claimMatchingEnable;
    private Workgroup claimMatchingWorkgroup;
    private WebUser claimMatchingOwner;
    private boolean copleyOfferMadeCheck;
    private boolean impecuniousCheck;
    private Date impecuniousStartDate;
    private boolean vedChargeCheck;
    private BigDecimal vedChargeCeiling = BigDecimal.ZERO;
    private boolean appliedLiabilityEnabled;
    private boolean blankLabourRateCheck;
    private boolean totalLossOwnRoadworthyCheck;
    private boolean totalLossOwnUnroadworthyCheck;
    private boolean totalLossChoRoadworthyCheck;
    private boolean totalLossChoUnroadworthyCheck;
    private boolean totalLossChoAtFaultRoadworthyCheck;
    private boolean totalLossChoAtFaultUnroadworthyCheck;
    private boolean totalLossDiaryInfoCheck;
    private boolean repairDiaryInfoCheck;
    private boolean upload414Check;
    private BigDecimal timeToAuthoriseRepair1;
    private BigDecimal timeToOffHire1;
    private BigDecimal timeToAuthoriseRepair2;
    private BigDecimal timeToOffHire2;
    private BigDecimal timeToAuthoriseRepair3;
    private BigDecimal timeToSubmittEngineersReport3;
    private BigDecimal timeToOffHire3;
    private BigDecimal totalAllowableDays3;
    private BigDecimal timeToInstructEngineer4;
    private BigDecimal timeToInspect4;
    private BigDecimal timeToAuthoriseRepair4;
    private BigDecimal timeToSubmittEngineersReport4;
    private BigDecimal timeToOffHire4;
    private BigDecimal totalAllowableDays4;
    private BigDecimal timeToAuthoriseRepair5;
    private BigDecimal timeToSubmittEngineersReport5;
    private BigDecimal timeToOffHire5;
    private BigDecimal totalAllowableDays5;
    private BigDecimal timeToInstructEngineer6;
    private BigDecimal timeToInspect6;
    private BigDecimal timeToAuthoriseRepair6;
    private BigDecimal timeToSubmittEngineersReport6;
    private BigDecimal timeToOffHire6;
    private BigDecimal totalAllowableDays6;
      
    public BreBand() {
    }

    public boolean isUpload414Check() {
        return upload414Check;
    }

    public void setUpload414Check(boolean upload414Check) {
        this.upload414Check = upload414Check;
    }

    public boolean isTotalLossDiaryInfoCheck() {
        return totalLossDiaryInfoCheck;
    }

    public void setTotalLossDiaryInfoCheck(boolean totalLossDiaryInfoCheck) {
        this.totalLossDiaryInfoCheck = totalLossDiaryInfoCheck;
    }

    public boolean isRepairDiaryInfoCheck() {
        return repairDiaryInfoCheck;
    }

    public void setRepairDiaryInfoCheck(boolean repairDiaryInfoCheck) {
        this.repairDiaryInfoCheck = repairDiaryInfoCheck;
    }

    public boolean isTotalLossOwnRoadworthyCheck() {
        return totalLossOwnRoadworthyCheck;
    }

    public void setTotalLossOwnRoadworthyCheck(boolean totalLossOwnRoadworthyCheck) {
        this.totalLossOwnRoadworthyCheck = totalLossOwnRoadworthyCheck;
    }

    public boolean isTotalLossOwnUnroadworthyCheck() {
        return totalLossOwnUnroadworthyCheck;
    }

    public void setTotalLossOwnUnroadworthyCheck(boolean totalLossOwnUnroadworthyCheck) {
        this.totalLossOwnUnroadworthyCheck = totalLossOwnUnroadworthyCheck;
    }

    public boolean isTotalLossChoRoadworthyCheck() {
        return totalLossChoRoadworthyCheck;
    }

    public void setTotalLossChoRoadworthyCheck(boolean totalLossChoRoadworthyCheck) {
        this.totalLossChoRoadworthyCheck = totalLossChoRoadworthyCheck;
    }

    public boolean isTotalLossChoUnroadworthyCheck() {
        return totalLossChoUnroadworthyCheck;
    }

    public void setTotalLossChoUnroadworthyCheck(boolean totalLossChoUnroadworthyCheck) {
        this.totalLossChoUnroadworthyCheck = totalLossChoUnroadworthyCheck;
    }

    public boolean isTotalLossChoAtFaultRoadworthyCheck() {
        return totalLossChoAtFaultRoadworthyCheck;
    }

    public void setTotalLossChoAtFaultRoadworthyCheck(boolean totalLossChoAtFaultRoadworthyCheck) {
        this.totalLossChoAtFaultRoadworthyCheck = totalLossChoAtFaultRoadworthyCheck;
    }

    public boolean isTotalLossChoAtFaultUnroadworthyCheck() {
        return totalLossChoAtFaultUnroadworthyCheck;
    }

    public void setTotalLossChoAtFaultUnroadworthyCheck(boolean totalLossChoAtFaultUnroadworthyCheck) {
        this.totalLossChoAtFaultUnroadworthyCheck = totalLossChoAtFaultUnroadworthyCheck;
    }

    public BigDecimal getTimeToAuthoriseRepair1() {
        return timeToAuthoriseRepair1;
    }

    public void setTimeToAuthoriseRepair1(BigDecimal timeToAuthoriseRepair1) {
        this.timeToAuthoriseRepair1 = timeToAuthoriseRepair1;
    }

    public BigDecimal getTimeToOffHire1() {
        return timeToOffHire1;
    }

    public void setTimeToOffHire1(BigDecimal timeToOffHire1) {
        this.timeToOffHire1 = timeToOffHire1;
    }

    public BigDecimal getTimeToAuthoriseRepair2() {
        return timeToAuthoriseRepair2;
    }

    public void setTimeToAuthoriseRepair2(BigDecimal timeToAuthoriseRepair2) {
        this.timeToAuthoriseRepair2 = timeToAuthoriseRepair2;
    }

    public BigDecimal getTimeToOffHire2() {
        return timeToOffHire2;
    }

    public void setTimeToOffHire2(BigDecimal timeToOffHire2) {
        this.timeToOffHire2 = timeToOffHire2;
    }

    public BigDecimal getTimeToAuthoriseRepair3() {
        return timeToAuthoriseRepair3;
    }

    public void setTimeToAuthoriseRepair3(BigDecimal timeToAuthoriseRepair3) {
        this.timeToAuthoriseRepair3 = timeToAuthoriseRepair3;
    }

    public BigDecimal getTimeToSubmittEngineersReport3() {
        return timeToSubmittEngineersReport3;
    }

    public void setTimeToSubmittEngineersReport3(BigDecimal timeToSubmittEngineersReport3) {
        this.timeToSubmittEngineersReport3 = timeToSubmittEngineersReport3;
    }

    public BigDecimal getTimeToOffHire3() {
        return timeToOffHire3;
    }

    public void setTimeToOffHire3(BigDecimal timeToOffHire3) {
        this.timeToOffHire3 = timeToOffHire3;
    }

    public BigDecimal getTotalAllowableDays3() {
        return totalAllowableDays3;
    }

    public void setTotalAllowableDays3(BigDecimal totalAllowableDays3) {
        this.totalAllowableDays3 = totalAllowableDays3;
    }

    public BigDecimal getTimeToInstructEngineer4() {
        return timeToInstructEngineer4;
    }

    public void setTimeToInstructEngineer4(BigDecimal timeToInstructEngineer4) {
        this.timeToInstructEngineer4 = timeToInstructEngineer4;
    }

    public BigDecimal getTimeToInspect4() {
        return timeToInspect4;
    }

    public void setTimeToInspect4(BigDecimal timeToInspect4) {
        this.timeToInspect4 = timeToInspect4;
    }

    public BigDecimal getTimeToAuthoriseRepair4() {
        return timeToAuthoriseRepair4;
    }

    public void setTimeToAuthoriseRepair4(BigDecimal timeToAuthoriseRepair4) {
        this.timeToAuthoriseRepair4 = timeToAuthoriseRepair4;
    }

    public BigDecimal getTimeToSubmittEngineersReport4() {
        return timeToSubmittEngineersReport4;
    }

    public void setTimeToSubmittEngineersReport4(BigDecimal timeToSubmittEngineersReport4) {
        this.timeToSubmittEngineersReport4 = timeToSubmittEngineersReport4;
    }

    public BigDecimal getTimeToOffHire4() {
        return timeToOffHire4;
    }

    public void setTimeToOffHire4(BigDecimal timeToOffHire4) {
        this.timeToOffHire4 = timeToOffHire4;
    }

    public BigDecimal getTotalAllowableDays4() {
        return totalAllowableDays4;
    }

    public void setTotalAllowableDays4(BigDecimal totalAllowableDays4) {
        this.totalAllowableDays4 = totalAllowableDays4;
    }

    public BigDecimal getTimeToAuthoriseRepair5() {
        return timeToAuthoriseRepair5;
    }

    public void setTimeToAuthoriseRepair5(BigDecimal timeToAuthoriseRepair5) {
        this.timeToAuthoriseRepair5 = timeToAuthoriseRepair5;
    }

    public BigDecimal getTimeToSubmittEngineersReport5() {
        return timeToSubmittEngineersReport5;
    }

    public void setTimeToSubmittEngineersReport5(BigDecimal timeToSubmittEngineersReport5) {
        this.timeToSubmittEngineersReport5 = timeToSubmittEngineersReport5;
    }

    public BigDecimal getTimeToOffHire5() {
        return timeToOffHire5;
    }

    public void setTimeToOffHire5(BigDecimal timeToOffHire5) {
        this.timeToOffHire5 = timeToOffHire5;
    }

    public BigDecimal getTotalAllowableDays5() {
        return totalAllowableDays5;
    }

    public void setTotalAllowableDays5(BigDecimal totalAllowableDays5) {
        this.totalAllowableDays5 = totalAllowableDays5;
    }

    public BigDecimal getTimeToInstructEngineer6() {
        return timeToInstructEngineer6;
    }

    public void setTimeToInstructEngineer6(BigDecimal timeToInstructEngineer6) {
        this.timeToInstructEngineer6 = timeToInstructEngineer6;
    }

    public BigDecimal getTimeToInspect6() {
        return timeToInspect6;
    }

    public void setTimeToInspect6(BigDecimal timeToInspect6) {
        this.timeToInspect6 = timeToInspect6;
    }

    public BigDecimal getTimeToAuthoriseRepair6() {
        return timeToAuthoriseRepair6;
    }

    public void setTimeToAuthoriseRepair6(BigDecimal timeToAuthoriseRepair6) {
        this.timeToAuthoriseRepair6 = timeToAuthoriseRepair6;
    }

    public BigDecimal getTimeToSubmittEngineersReport6() {
        return timeToSubmittEngineersReport6;
    }

    public void setTimeToSubmittEngineersReport6(BigDecimal timeToSubmittEngineersReport6) {
        this.timeToSubmittEngineersReport6 = timeToSubmittEngineersReport6;
    }

    public BigDecimal getTimeToOffHire6() {
        return timeToOffHire6;
    }

    public void setTimeToOffHire6(BigDecimal timeToOffHire6) {
        this.timeToOffHire6 = timeToOffHire6;
    }

    public BigDecimal getTotalAllowableDays6() {
        return totalAllowableDays6;
    }

    public void setTotalAllowableDays6(BigDecimal totalAllowableDays6) {
        this.totalAllowableDays6 = totalAllowableDays6;
    }

    public boolean isBlankLabourRateCheck() {
        return blankLabourRateCheck;
    }

    public void setBlankLabourRateCheck(boolean blankLabourRateCheck) {
        this.blankLabourRateCheck = blankLabourRateCheck;
    }

    public boolean isAppliedLiabilityEnabled() {
        return appliedLiabilityEnabled;
    }

    public void setAppliedLiabilityEnabled(boolean appliedLiabilityEnabled) {
        this.appliedLiabilityEnabled = appliedLiabilityEnabled;
    }

    public boolean isVedChargeCheck() {
        return vedChargeCheck;
    }

    public void setVedChargeCheck(boolean vedChargeCheck) {
        this.vedChargeCheck = vedChargeCheck;
    }

    public BigDecimal getVedChargeCeiling() {
        return vedChargeCeiling;
    }

    public void setVedChargeCeiling(BigDecimal vedChargeCeiling) {
        this.vedChargeCeiling = vedChargeCeiling;
    }

    public boolean isImpecuniousCheck() {
        return impecuniousCheck;
    }

    public void setImpecuniousCheck(boolean impecuniousCheck) {
        this.impecuniousCheck = impecuniousCheck;
    }

    public Date getImpecuniousStartDate() {
        return impecuniousStartDate;
    }

    public void setImpecuniousStartDate(Date impecuniousStartDate) {
        this.impecuniousStartDate = impecuniousStartDate;
    }

    public boolean isClaimMatchingEnable() {
        return claimMatchingEnable;
    }

    public void setClaimMatchingEnable(boolean claimMatchingEnable) {
        this.claimMatchingEnable = claimMatchingEnable;
    }

    public Workgroup getClaimMatchingWorkgroup() {
        return claimMatchingWorkgroup;
    }

    public void setClaimMatchingWorkgroup(Workgroup claimMatchingWorkgroup) {
        this.claimMatchingWorkgroup = claimMatchingWorkgroup;
    }

    public WebUser getClaimMatchingOwner() {
        return claimMatchingOwner;
    }

    public void setClaimMatchingOwner(WebUser claimMatchingOwner) {
        this.claimMatchingOwner = claimMatchingOwner;
    }

    public boolean isEnableGtaDiscount() {
        return enableGtaDiscount;
    }

    public void setEnableGtaDiscount(boolean enableGtaDiscount) {
        this.enableGtaDiscount = enableGtaDiscount;
    }

    public boolean isStorageRecoveryNetCeilingCheck() {
        return storageRecoveryNetCeilingCheck;
    }

    public void setStorageRecoveryNetCeilingCheck(boolean storageRecoveryNetCeilingCheck) {
        this.storageRecoveryNetCeilingCheck = storageRecoveryNetCeilingCheck;
    }

    public boolean isEcdVsRepairCompletionDateCheck() {
        return ecdVsRepairCompletionDateCheck;
    }

    public void setEcdVsRepairCompletionDateCheck(boolean ecdVsRepairCompletionDateCheck) {
        this.ecdVsRepairCompletionDateCheck = ecdVsRepairCompletionDateCheck;
    }

    public boolean isFullTotalRequestedCeilingCheck() {
        return fullTotalRequestedCeilingCheck;
    }

    public void setFullTotalRequestedCeilingCheck(boolean fullTotalRequestedCeilingCheck) {
        this.fullTotalRequestedCeilingCheck = fullTotalRequestedCeilingCheck;
    }

    public BigDecimal getStorageRecoveryNetCeiling() {
        return storageRecoveryNetCeiling;
    }

    public void setStorageRecoveryNetCeiling(BigDecimal storageRecoveryNetCeiling) {
        this.storageRecoveryNetCeiling = storageRecoveryNetCeiling;
    }

    public int getSubscriberSlaDays() {
        return subscriberSlaDays;
    }

    public void setSubscriberSlaDays(int subscriberSlaDays) {
        this.subscriberSlaDays = subscriberSlaDays;
    }

    public int getSubscriberResubmissionAllowed() {
        return subscriberResubmissionAllowed;
    }

    public void setSubscriberResubmissionAllowed(int subscriberResubmissionAllowed) {
        this.subscriberResubmissionAllowed = subscriberResubmissionAllowed;
    }

    public String getSubscriberTimeCutOff() {
        return subscriberTimeCutOff;
    }

    public void setSubscriberTimeCutOff(String subscriberTimeCutOff) {
        this.subscriberTimeCutOff = subscriberTimeCutOff;
    }

    public int getFixedFeeSlaDays() {
        return fixedFeeSlaDays;
    }

    public void setFixedFeeSlaDays(int fixedFeeSlaDays) {
        this.fixedFeeSlaDays = fixedFeeSlaDays;
    }

    public int getFixedFeeResubmissionAllowed() {
        return fixedFeeResubmissionAllowed;
    }

    public void setFixedFeeResubmissionAllowed(int fixedFeeResubmissionAllowed) {
        this.fixedFeeResubmissionAllowed = fixedFeeResubmissionAllowed;
    }

    public String getFixedFeeTimeCutOff() {
        return fixedFeeTimeCutOff;
    }

    public void setFixedFeeTimeCutOff(String fixedFeeTimeCutOff) {
        this.fixedFeeTimeCutOff = fixedFeeTimeCutOff;
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

    public boolean isAllowPenaltyCharges(ClaimType claimType) {
        boolean allowPenalty = false;
        if ((ClaimType.isGTA(claimType) && isAllowGTAPenaltyCharges())
                || (ClaimType.isSubscriber(claimType) && isAllowSubscriberPenaltyCharges())
                || (ClaimType.isFixedFee(claimType) && isAllowFixedFeePenaltyCharges())
                || (ClaimType.isCollaborationProtocol(claimType) && isAllowCollaborationProtocolPenaltyCharges())
                || (ClaimType.isInsurerVsInsurer(claimType) && isAllowInsurervsInsurerPenaltyCharges())
                || (ClaimType.isInsurerUpload(claimType) && isAllowManualInvoicePenaltyCharges())
                || (ClaimType.isTPI(claimType) && isAllowTPIPenaltyCharges())) {
            allowPenalty = true;
        }
        return allowPenalty;
    }

    public boolean isAllowGTAPenaltyCharges() {
        return allowGTAPenaltyCharges;
    }

    public void setAllowGTAPenaltyCharges(boolean allowGTAPenaltyCharges) {
        this.allowGTAPenaltyCharges = allowGTAPenaltyCharges;
    }

    public boolean isAllowSubscriberPenaltyCharges() {
        return allowSubscriberPenaltyCharges;
    }

    public void setAllowSubscriberPenaltyCharges(boolean allowSubscriberPenaltyCharges) {
        this.allowSubscriberPenaltyCharges = allowSubscriberPenaltyCharges;
    }

    public boolean isAllowFixedFeePenaltyCharges() {
        return allowFixedFeePenaltyCharges;
    }

    public void setAllowFixedFeePenaltyCharges(boolean allowFixedFeePenaltyCharges) {
        this.allowFixedFeePenaltyCharges = allowFixedFeePenaltyCharges;
    }

    public boolean isAllowCollaborationProtocolPenaltyCharges() {
        return allowCollaborationProtocolPenaltyCharges;
    }

    public void setAllowCollaborationProtocolPenaltyCharges(boolean allowCollaborationProtocolPenaltyCharges) {
        this.allowCollaborationProtocolPenaltyCharges = allowCollaborationProtocolPenaltyCharges;
    }

    public boolean isAllowTPIPenaltyCharges() {
        return allowTPIPenaltyCharges;
    }

    public void setAllowTPIPenaltyCharges(boolean allowTPIPenaltyCharges) {
        this.allowTPIPenaltyCharges = allowTPIPenaltyCharges;
    }

    public boolean isAllowInsurervsInsurerPenaltyCharges() {
        return allowInsurervsInsurerPenaltyCharges;
    }

    public void setAllowInsurervsInsurerPenaltyCharges(boolean allowInsurervsInsurerPenaltyCharges) {
        this.allowInsurervsInsurerPenaltyCharges = allowInsurervsInsurerPenaltyCharges;
    }

    public boolean isAllowManualInvoicePenaltyCharges() {
        return allowManualInvoicePenaltyCharges;
    }

    public void setAllowManualInvoicePenaltyCharges(boolean allowManualInvoicePenaltyCharges) {
        this.allowManualInvoicePenaltyCharges = allowManualInvoicePenaltyCharges;
    }

    public boolean isAllowManualInvoiceAutoPenaltyCharges() {
        return allowManualInvoiceAutoPenaltyCharges;
    }

    public void setAllowManualInvoiceAutoPenaltyCharges(boolean allowManualInvoiceAutoPenaltyCharges) {
        this.allowManualInvoiceAutoPenaltyCharges = allowManualInvoiceAutoPenaltyCharges;
    }

    public boolean isHasTotalGrossSumCheck() {
        return hasTotalGrossSumCheck;
    }

    public void setHasTotalGrossSumCheck(boolean hasTotalGrossSumCheck) {
        this.hasTotalGrossSumCheck = hasTotalGrossSumCheck;
    }

    public boolean isHasHireGrossSumCheck() {
        return hasHireGrossSumCheck;
    }

    public void setHasHireGrossSumCheck(boolean hasHireGrossSumCheck) {
        this.hasHireGrossSumCheck = hasHireGrossSumCheck;
    }

    public boolean isHasRepairGrossSumCheck() {
        return hasRepairGrossSumCheck;
    }

    public void setHasRepairGrossSumCheck(boolean hasRepairGrossSumCheck) {
        this.hasRepairGrossSumCheck = hasRepairGrossSumCheck;
    }

    public boolean isHasTotalLossFeeGrossSumCheck() {
        return hasTotalLossFeeGrossSumCheck;
    }

    public void setHasTotalLossFeeGrossSumCheck(boolean hasTotalLossFeeGrossSumCheck) {
        this.hasTotalLossFeeGrossSumCheck = hasTotalLossFeeGrossSumCheck;
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

    public int getEngineerInspectionDelayDaysMobile() {
        return engineerInspectionDelayDaysMobile;
    }

    public void setEngineerInspectionDelayDaysMobile(int engineerInspectionDelayDaysMobile) {
        this.engineerInspectionDelayDaysMobile = engineerInspectionDelayDaysMobile;
    }

    public int getEngineerInspectionDelayDaysNonMobile() {
        return engineerInspectionDelayDaysNonMobile;
    }

    public void setEngineerInspectionDelayDaysNonMobile(int engineerInspectionDelayDaysNonMobile) {
        this.engineerInspectionDelayDaysNonMobile = engineerInspectionDelayDaysNonMobile;
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

    public int getAverageLabourRate(String vehicleClass) {
        if (VehicleClass.isPrestige(vehicleClass)) {
            return getAverageLabourRatePrestige();
        }
        
        return getAverageLabourRateStandard();
    }

    public int getAverageLabourRateStandard() {
        return averageLabourRateStandard;
    }

    public void setAverageLabourRateStandard(int averageLabourRateStandard) {
        this.averageLabourRateStandard = averageLabourRateStandard;
    }

    public int getAverageLabourRatePrestige() {
        return averageLabourRatePrestige;
    }

    public void setAverageLabourRatePrestige(int averageLabourRatePrestige) {
        this.averageLabourRatePrestige = averageLabourRatePrestige;
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

        if (vehicleClassCeiling != null) {
            maxRepairNetCeiling = vehicleClassCeiling.getRepairNetCeiling();
        }

        return maxRepairNetCeiling;
    }

    public java.math.BigDecimal getMaxHireNetCeiling() {

        BigDecimal maxHireNetCeiling = new BigDecimal(100000);

        if (vehicleClassCeiling != null) {
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

    public boolean isMiscellaneousChargeCheck() {
        return miscellaneousChargeCheck;
    }

    public void setMiscellaneousChargeCheck(boolean miscellaneousChargeCheck) {
        this.miscellaneousChargeCheck = miscellaneousChargeCheck;
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

    public boolean isEstateChargeCheckHpi() {
        return estateChargeCheckHpi;
    }

    public void setEstateChargeCheckHpi(boolean estateChargeCheckHpi) {
        this.estateChargeCheckHpi = estateChargeCheckHpi;
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

     public void setRepairBookedInDateOnThursday(boolean repairBookedInDateOnThursday) {
        this.repairBookedInDateOnThursday = repairBookedInDateOnThursday;
    }

    public boolean isRepairBookedInDateOnThursday() {
        return repairBookedInDateOnThursday;
    }

    public void setRepairBookedInDateOnFriday(boolean repairBookedInDateOnFriday) {
        this.repairBookedInDateOnFriday = repairBookedInDateOnFriday;
    }

    public boolean isRepairBookedInDateOnFriday() {
        return repairBookedInDateOnFriday;
    }

    public void setRepairBookedInDateOnSaturday(boolean repairBookedInDateOnSaturday) {
        this.repairBookedInDateOnSaturday = repairBookedInDateOnSaturday;
    }

    public boolean isRepairBookedInDateOnSaturday() {
        return repairBookedInDateOnSaturday;
    }

    public void setRepairBookedInDateOnSunday(boolean repairBookedInDateOnSunday) {
        this.repairBookedInDateOnSunday = repairBookedInDateOnSunday;
    }

   public boolean isRepairBookedInDateOnSunday() {
        return repairBookedInDateOnSunday;
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

    public boolean isAdditionalDriverChargeCheck() {
        return additionalDriverChargeCheck;
    }

    public void setAdditionalDriverChargeCheck(boolean additionalDriverChargeCheck) {
        this.additionalDriverChargeCheck = additionalDriverChargeCheck;
    }

    public boolean isHasCorrectTotalLossGrossCalculation() {
        return hasCorrectTotalLossGrossCalculation;
    }

    public void setHasCorrectTotalLossGrossCalculation(boolean hasCorrectTotalLossGrossCalculation) {
        this.hasCorrectTotalLossGrossCalculation = hasCorrectTotalLossGrossCalculation;
    }

    public boolean isHasCorrectTotalLossVatCalculation() {
        return hasCorrectTotalLossVatCalculation;
    }

    public void setHasCorrectTotalLossVatCalculation(boolean hasCorrectTotalLossVatCalculation) {
        this.hasCorrectTotalLossVatCalculation = hasCorrectTotalLossVatCalculation;
    }

    public boolean isVehicleClassHireProvisionLikeForLike6To8() {
        return vehicleClassHireProvisionLikeForLike6To8;
    }

    public void setVehicleClassHireProvisionLikeForLike6To8(boolean vehicleClassHireProvisionLikeForLike6To8) {
        this.vehicleClassHireProvisionLikeForLike6To8 = vehicleClassHireProvisionLikeForLike6To8;
    }

    public boolean isVehicleClassHireProvisionLikeForLike8To9() {
        return vehicleClassHireProvisionLikeForLike8To9;
    }

    public void setVehicleClassHireProvisionLikeForLike8To9(boolean vehicleClassHireProvisionLikeForLike8To9) {
        this.vehicleClassHireProvisionLikeForLike8To9 = vehicleClassHireProvisionLikeForLike8To9;
    }

    public boolean isVehicleClassHireProvisionLikeForLikeOver9() {
        return vehicleClassHireProvisionLikeForLikeOver9;
    }

    public void setVehicleClassHireProvisionLikeForLikeOver9(boolean vehicleClassHireProvisionLikeForLikeOver9) {
        this.vehicleClassHireProvisionLikeForLikeOver9 = vehicleClassHireProvisionLikeForLikeOver9;
    }

    public boolean isVehicleClassHireProvisionLikeForLike6To8SP() {
        return vehicleClassHireProvisionLikeForLike6To8SP;
    }

    public void setVehicleClassHireProvisionLikeForLike6To8SP(boolean vehicleClassHireProvisionLikeForLike6To8SP) {
        this.vehicleClassHireProvisionLikeForLike6To8SP = vehicleClassHireProvisionLikeForLike6To8SP;
    }

    public boolean isVehicleClassHireProvisionLikeForLike8To9SP() {
        return vehicleClassHireProvisionLikeForLike8To9SP;
    }

    public void setVehicleClassHireProvisionLikeForLike8To9SP(boolean vehicleClassHireProvisionLikeForLike8To9SP) {
        this.vehicleClassHireProvisionLikeForLike8To9SP = vehicleClassHireProvisionLikeForLike8To9SP;
    }

    public boolean isVehicleClassHireProvisionLikeForLikeOver9SP() {
        return vehicleClassHireProvisionLikeForLikeOver9SP;
    }

    public void setVehicleClassHireProvisionLikeForLikeOver9SP(boolean vehicleClassHireProvisionLikeForLikeOver9SP) {
        this.vehicleClassHireProvisionLikeForLikeOver9SP = vehicleClassHireProvisionLikeForLikeOver9SP;
    }

    public boolean isHireVatLimitCheck() {
        return hireVatLimitCheck;
    }

    public void setHireVatLimitCheck(boolean hireVatLimitCheck) {
        this.hireVatLimitCheck = hireVatLimitCheck;
    }

    public boolean isRepairVatLimitCheck() {
        return repairVatLimitCheck;
    }

    public void setRepairVatLimitCheck(boolean repairVatLimitCheck) {
        this.repairVatLimitCheck = repairVatLimitCheck;
    }

    public boolean isTotalLossFeeVatLimitCheck() {
        return totalLossFeeVatLimitCheck;
    }

    public void setTotalLossFeeVatLimitCheck(boolean totalLossFeeVatLimitCheck) {
        this.totalLossFeeVatLimitCheck = totalLossFeeVatLimitCheck;
    }

    public boolean isTotalVatLimitCheck() {
        return totalVatLimitCheck;
    }

    public void setTotalVatLimitCheck(boolean totalVatLimitCheck) {
        this.totalVatLimitCheck = totalVatLimitCheck;
    }

    public boolean isEngineerFeeVatLimitCheck() {
        return engineerFeeVatLimitCheck;
    }

    public void setEngineerFeeVatLimitCheck(boolean engineerFeeVatLimitCheck) {
        this.engineerFeeVatLimitCheck = engineerFeeVatLimitCheck;
    }

    public boolean isStorageRecoveryVatLimitCheck() {
        return storageRecoveryVatLimitCheck;
    }

    public void setStorageRecoveryVatLimitCheck(boolean storageRecoveryVatLimitCheck) {
        this.storageRecoveryVatLimitCheck = storageRecoveryVatLimitCheck;
    }

    public boolean isHireVatHireEndCheck() {
        return hireVatHireEndCheck;
    }

    public void setHireVatHireEndCheck(boolean hireVatHireEndCheck) {
        this.hireVatHireEndCheck = hireVatHireEndCheck;
    }

    public boolean isHireVatInvoicedDateCheck() {
        return hireVatInvoicedDateCheck;
    }

    public void setHireVatInvoicedDateCheck(boolean hireVatInvoicedDateCheck) {
        this.hireVatInvoicedDateCheck = hireVatInvoicedDateCheck;
    }

    public boolean isRepairVatCompletionDateCheck() {
        return repairVatCompletionDateCheck;
    }

    public void setRepairVatCompletionDateCheck(boolean repairVatCompletionDateCheck) {
        this.repairVatCompletionDateCheck = repairVatCompletionDateCheck;
    }

    public boolean isUseSupplierRates() {
        return useSupplierRates;
    }

    public void setUseSupplierRates(boolean useSupplierRates) {
        this.useSupplierRates = useSupplierRates;
    }

    /**
     * @return the totalLabourCostBusinessRule
     */
    public boolean isTotalLabourCostBusinessRule() {
        return totalLabourCostBusinessRule;
    }

    /**
     * @param totalLabourCostBusinessRule the totalLabourCostBusinessRule to set
     */
    public void setTotalLabourCostBusinessRule(boolean totalLabourCostBusinessRule) {
        this.totalLabourCostBusinessRule = totalLabourCostBusinessRule;
    }

    /**
     * @return the automaticChargeCheckHpiLookup
     */
    public boolean isAutomaticChargeCheckHpiLookup() {
        return automaticChargeCheckHpiLookup;
    }

    /**
     * @param automaticChargeCheckHpiLookup the automaticChargeCheckHpiLookup to set
     */
    public void setAutomaticChargeCheckHpiLookup(boolean automaticChargeCheckHpiLookup) {
        this.automaticChargeCheckHpiLookup = automaticChargeCheckHpiLookup;
    }

    /**
     * @return the dateRepairCommencedChkForNonMobileVehicle
     */
    public boolean isDateRepairCommencedChkForNonMobileVehicle() {
        return dateRepairCommencedChkForNonMobileVehicle;
    }

    /**
     * @param dateRepairCommencedChkForNonMobileVehicle the dateRepairCommencedChkForNonMobileVehicle to set
     */
    public void setDateRepairCommencedChkForNonMobileVehicle(boolean dateRepairCommencedChkForNonMobileVehicle) {
        this.dateRepairCommencedChkForNonMobileVehicle = dateRepairCommencedChkForNonMobileVehicle;
    }

    /**
     * @return the hireDaysPriorToDateRepairCommenced
     */
    public int getHireDaysPriorToDateRepairCommenced() {
        return hireDaysPriorToDateRepairCommenced;
    }

    /**
     * @param hireDaysPriorToDateRepairCommenced the hireDaysPriorToDateRepairCommenced to set
     */
    public void setHireDaysPriorToDateRepairCommenced(int hireDaysPriorToDateRepairCommenced) {
        this.hireDaysPriorToDateRepairCommenced = hireDaysPriorToDateRepairCommenced;
    }

    /**
     * @return the dateRepairBookInDateChkForMobileVehicle
     */
    public boolean isDateRepairBookInDateChkForMobileVehicle() {
        return dateRepairBookInDateChkForMobileVehicle;
    }

    /**
     * @param dateRepairBookInDateChkForMobileVehicle the dateRepairBookInDateChkForMobileVehicle to set
     */
    public void setDateRepairBookInDateChkForMobileVehicle(boolean dateRepairBookInDateChkForMobileVehicle) {
        this.dateRepairBookInDateChkForMobileVehicle = dateRepairBookInDateChkForMobileVehicle;
    }

    /**
     * @return the dateRepairBookInDateChkForNonMobileVehicle
     */
    public boolean isDateRepairBookInDateChkForNonMobileVehicle() {
        return dateRepairBookInDateChkForNonMobileVehicle;
    }

    /**
     * @param dateRepairBookInDateChkForNonMobileVehicle the dateRepairBookInDateChkForNonMobileVehicle to set
     */
    public void setDateRepairBookInDateChkForNonMobileVehicle(boolean dateRepairBookInDateChkForNonMobileVehicle) {
        this.dateRepairBookInDateChkForNonMobileVehicle = dateRepairBookInDateChkForNonMobileVehicle;
    }

    /**
     * @return the hireDaysPriorToDateRepairBookInDateNonMobileVehicles
     */
    public int getHireDaysPriorToDateRepairBookInDateNonMobileVehicles() {
        return hireDaysPriorToDateRepairBookInDateNonMobileVehicles;
    }

    /**
     * @param hireDaysPriorToDateRepairBookInDateNonMobileVehicles the hireDaysPriorToDateRepairBookInDateNonMobileVehicles to set
     */
    public void setHireDaysPriorToDateRepairBookInDateNonMobileVehicles(int hireDaysPriorToDateRepairBookInDateNonMobileVehicles) {
        this.hireDaysPriorToDateRepairBookInDateNonMobileVehicles = hireDaysPriorToDateRepairBookInDateNonMobileVehicles;
    }

    /**
     * @return the hireDaysPriorToDateRepairBookInDateMobileVehicles
     */
    public int getHireDaysPriorToDateRepairBookInDateMobileVehicles() {
        return hireDaysPriorToDateRepairBookInDateMobileVehicles;
    }

    /**
     * @param hireDaysPriorToDateRepairBookInDateMobileVehicles the hireDaysPriorToDateRepairBookInDateMobileVehicles to set
     */
    public void setHireDaysPriorToDateRepairBookInDateMobileVehicles(int hireDaysPriorToDateRepairBookInDateMobileVehicles) {
        this.hireDaysPriorToDateRepairBookInDateMobileVehicles = hireDaysPriorToDateRepairBookInDateMobileVehicles;
    }

    /**
     * @return the supplierAdminstrationFee
     */
    public boolean isSupplierAdminstrationFee() {
        return supplierAdminstrationFee;
    }

    /**
     * @param supplierAdminstrationFee the supplierAdminstrationFee to set
     */
    public void setSupplierAdminstrationFee(boolean supplierAdminstrationFee) {
        this.supplierAdminstrationFee = supplierAdminstrationFee;
    }

    /**
     * @return the adminFeeCeiling
     */
    public BigDecimal getAdminFeeCeiling() {
        return adminFeeCeiling;
    }

    /**
     * @param adminFeeCeiling the adminFeeCeiling to set
     */
    public void setAdminFeeCeiling(BigDecimal adminFeeCeiling) {
        this.adminFeeCeiling = adminFeeCeiling;
    }

    public BigDecimal getAdminFeeCeilingSubscriber() {
        return adminFeeCeilingSubscriber;
    }

    public void setAdminFeeCeilingSubscriber(BigDecimal adminFeeCeilingSubscriber) {
        this.adminFeeCeilingSubscriber = adminFeeCeilingSubscriber;
    }

    public BigDecimal getAdminFeeCeilingSubscriberManagingRepair() {
        return adminFeeCeilingSubscriberManagingRepair;
    }

    public void setAdminFeeCeilingSubscriberManagingRepair(BigDecimal adminFeeCeilingSubscriberManagingRepair) {
        this.adminFeeCeilingSubscriberManagingRepair = adminFeeCeilingSubscriberManagingRepair;
    }

    public BigDecimal getAdminFeeCeilingFixedFee() {
        return adminFeeCeilingFixedFee;
    }

    public void setAdminFeeCeilingFixedFee(BigDecimal adminFeeCeilingFixedFee) {
        this.adminFeeCeilingFixedFee = adminFeeCeilingFixedFee;
    }

    public BigDecimal getAdminFeeCeilingFixedFeeManagingRepair() {
        return adminFeeCeilingFixedFeeManagingRepair;
    }

    public void setAdminFeeCeilingFixedFeeManagingRepair(BigDecimal adminFeeCeilingFixedFeeManagingRepair) {
        this.adminFeeCeilingFixedFeeManagingRepair = adminFeeCeilingFixedFeeManagingRepair;
    }

    public BigDecimal getAdminFeeCeilingManagingRepair() {
        return adminFeeCeilingManagingRepair;
    }

    public void setAdminFeeCeilingManagingRepair(BigDecimal adminFeeCeilingManagingRepair) {
        this.adminFeeCeilingManagingRepair = adminFeeCeilingManagingRepair;
    }

    /**
     * @return the autoRestoreOneDayRepairCheck
     */
    public boolean isAutoRestoreOneDayRepairCheck() {
        return autoRestoreOneDayRepairCheck;
    }

    /**
     * @param autoRestoreOneDayRepairCheck the autoRestoreOneDayRepairCheck to set
     */
    public void setAutoRestoreOneDayRepairCheck(boolean autoRestoreOneDayRepairCheck) {
        this.autoRestoreOneDayRepairCheck = autoRestoreOneDayRepairCheck;
    }

    /**
     * @return the insurancePremiumTaxCheck
     */
    public boolean isInsurancePremiumTaxCheck() {
        return insurancePremiumTaxCheck;
    }

    /**
     * @param insurancePremiumTaxCheck the insurancePremiumTaxCheck to set
     */
    public void setInsurancePremiumTaxCheck(boolean insurancePremiumTaxCheck) {
        this.insurancePremiumTaxCheck = insurancePremiumTaxCheck;
    }

    /**
     * @return the standardInsurancePremium
     */
    public BigDecimal getStandardInsurancePremium() {
        return standardInsurancePremium;
    }

    /**
     * @param standardInsurancePremium the standardInsurancePremium to set
     */
    public void setStandardInsurancePremium(BigDecimal standardInsurancePremium) {
        this.standardInsurancePremium = standardInsurancePremium;
    }

    /**
     * @return the nonStandardInsurancePremium
     */
    public BigDecimal getNonStandardInsurancePremium() {
        return nonStandardInsurancePremium;
    }

    /**
     * @param nonStandardInsurancePremium the nonStandardInsurancePremium to set
     */
    public void setNonStandardInsurancePremium(BigDecimal nonStandardInsurancePremium) {
        this.nonStandardInsurancePremium = nonStandardInsurancePremium;
    }

    /**
     * @return the nameOfRepairer
     */
    public String getNameOfRepairer() {
        return nameOfRepairer;
    }

    /**
     * @param nameOfRepairer the nameOfRepairer to set
     */
    public void setNameOfRepairer(String nameOfRepairer) {
        this.nameOfRepairer = nameOfRepairer;
    }

    /**
     * @return the numberOfDays
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    /**
     * @param numberOfDays the numberOfDays to set
     */
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public BigDecimal getHireVatTolerance() {
        return hireVatTolerance;
    }

    public void setHireVatTolerance(BigDecimal hireVatTolerance) {
        this.hireVatTolerance = hireVatTolerance;
    }

    public BigDecimal getRepairVatTolerance() {
        return repairVatTolerance;
    }

    public void setRepairVatTolerance(BigDecimal repairVatTolerance) {
        this.repairVatTolerance = repairVatTolerance;
    }

    public BigDecimal getTotalVatTolerance() {
        return totalVatTolerance;
    }

    public void setTotalVatTolerance(BigDecimal totalVatTolerance) {
        this.totalVatTolerance = totalVatTolerance;
    }

    public BigDecimal getNonStandardInsurancePremiumCeilingTolerance() {
        return nonStandardInsurancePremiumCeilingTolerance;
    }

    public void setNonStandardInsurancePremiumCeilingTolerance(BigDecimal nonStandardInsurancePremiumCeilingTolerance) {
        this.nonStandardInsurancePremiumCeilingTolerance = nonStandardInsurancePremiumCeilingTolerance;
    }

    public BigDecimal getFullTotalRequestedCeilingTolerance() {
        return fullTotalRequestedCeilingTolerance;
    }

    public void setFullTotalRequestedCeilingTolerance(BigDecimal fullTotalRequestedCeilingTolerance) {
        this.fullTotalRequestedCeilingTolerance = fullTotalRequestedCeilingTolerance;
    }

    public boolean isMobileVehicleTotalLossCheck() {
        return mobileVehicleTotalLossCheck;
    }

    public void setMobileVehicleTotalLossCheck(boolean mobileVehicleTotalLossCheck) {
        this.mobileVehicleTotalLossCheck = mobileVehicleTotalLossCheck;
    }

    public String getClaimUploadNote() {
        return claimUploadNote;
    }

    public void setClaimUploadNote(String claimUploadNote) {
        this.claimUploadNote = claimUploadNote;
    }

    public boolean isSubscriberCheckRejectedClaims() {
        return subscriberCheckRejectedClaims;
    }

    public void setSubscriberCheckRejectedClaims(boolean subscriberCheckRejectedClaims) {
        this.subscriberCheckRejectedClaims = subscriberCheckRejectedClaims;
    }

    public boolean isSubscriberAcquisitionFeeCheck() {
        return subscriberAcquisitionFeeCheck;
    }

    public void setSubscriberAcquisitionFeeCheck(boolean subscriberAcquisitionFeeCheck) {
        this.subscriberAcquisitionFeeCheck = subscriberAcquisitionFeeCheck;
    }

    public boolean isSubscriberAdminFeeCheck() {
        return subscriberAdminFeeCheck;
    }

    public void setSubscriberAdminFeeCheck(boolean subscriberAdminFeeCheck) {
        this.subscriberAdminFeeCheck = subscriberAdminFeeCheck;
    }

    public boolean isFixedFeeAdminFeeCheck() {
        return fixedFeeAdminFeeCheck;
    }

    public void setFixedFeeAdminFeeCheck(boolean fixedFeeAdminFeeCheck) {
        this.fixedFeeAdminFeeCheck = fixedFeeAdminFeeCheck;
    }

    public boolean isHireTerminatedAfterRepairCompletionCheck() {
        return hireTerminatedAfterRepairCompletionCheck;
    }

    public void setHireTerminatedAfterRepairCompletionCheck(boolean hireTerminatedAfterRepairCompletionCheck) {
        this.hireTerminatedAfterRepairCompletionCheck = hireTerminatedAfterRepairCompletionCheck;
    }

    public boolean isAllowManagingRepairAutomatedTasks() {
        return allowManagingRepairAutomatedTasks;
    }

    public void setAllowManagingRepairAutomatedTasks(boolean allowManagingRepairAutomatedTasks) {
        this.allowManagingRepairAutomatedTasks = allowManagingRepairAutomatedTasks;
    }

    public boolean isAllowNotManagingRepairAutomatedTasks() {
        return allowNotManagingRepairAutomatedTasks;
    }

    public void setAllowNotManagingRepairAutomatedTasks(boolean allowNotManagingRepairAutomatedTasks) {
        this.allowNotManagingRepairAutomatedTasks = allowNotManagingRepairAutomatedTasks;
    }

    public boolean isOverlappingHireCheck() {
        return overlappingHireCheck;
    }

    public void setOverlappingHireCheck(boolean overlappingHireCheck) {
        this.overlappingHireCheck = overlappingHireCheck;
    }

    public boolean isAllowMissingECDAutomatedTasks() {
        return allowMissingECDAutomatedTasks;
    }

    public void setAllowMissingECDAutomatedTasks(boolean allowMissingECDAutomatedTasks) {
        this.allowMissingECDAutomatedTasks = allowMissingECDAutomatedTasks;
    }

    public boolean isAllowOnHireAutomatedTasks() {
        return allowOnHireAutomatedTasks;
    }

    public void setAllowOnHireAutomatedTasks(boolean allowOnHireAutomatedTasks) {
        this.allowOnHireAutomatedTasks = allowOnHireAutomatedTasks;
    }

    public BigDecimal getMaxAllowedLabourRate() {
        return maxAllowedLabourRate;
    }

    public void setMaxAllowedLabourRate(BigDecimal maxAllowedLabourRate) {
        this.maxAllowedLabourRate = maxAllowedLabourRate;
    }

    public BigDecimal getMaxAllowedLabourStandardRate() {
        return maxAllowedLabourStandardRate;
    }

    public void setMaxAllowedLabourStandardRate(BigDecimal maxAllowedLabourStandardRate) {
        this.maxAllowedLabourStandardRate = maxAllowedLabourStandardRate;
    }

    public BigDecimal getMaxAllowedLabourPrestigeRate() {
        return maxAllowedLabourPrestigeRate;
    }

    public void setMaxAllowedLabourPrestigeRate(BigDecimal maxAllowedLabourPrestigeRate) {
        this.maxAllowedLabourPrestigeRate = maxAllowedLabourPrestigeRate;
    }

    public BigDecimal getMaxAllowedEngineerNetFee() {
        return maxAllowedEngineerNetFee;
    }

    public void setMaxAllowedEngineerNetFee(BigDecimal maxAllowedEngineerNetFee) {
        this.maxAllowedEngineerNetFee = maxAllowedEngineerNetFee;
    }

    public boolean isMaximumLabourRateCheck() {
        return maximumLabourRateCheck;
    }

    public void setMaximumLabourRateCheck(boolean maximumLabourRateCheck) {
        this.maximumLabourRateCheck = maximumLabourRateCheck;
    }

    public boolean isMaximumLabourRateStandardCheck() {
        return maximumLabourRateStandardCheck;
    }

    public void setMaximumLabourRateStandardCheck(boolean maximumLabourRateStandardCheck) {
        this.maximumLabourRateStandardCheck = maximumLabourRateStandardCheck;
    }

    public boolean isMaximumLabourRatePrestigeCheck() {
        return maximumLabourRatePrestigeCheck;
    }

    public void setMaximumLabourRatePrestigeCheck(boolean maximumLabourRatePrestigeCheck) {
        this.maximumLabourRatePrestigeCheck = maximumLabourRatePrestigeCheck;
    }

    public List<ProtocolVehicleClassCeiling> getProtocolVehicleClassCeilings() {
        return protocolVehicleClassCeilings;
    }

    public void setProtocolVehicleClassCeilings(List<ProtocolVehicleClassCeiling> protocolVehicleClassCeilings) {
        this.protocolVehicleClassCeilings = protocolVehicleClassCeilings;
    }
    
    public void addProtocolVehicleClassCeiling(ProtocolVehicleClassCeiling protocolVehicleClassCeiling) {
        if (protocolVehicleClassCeiling == null) {
            return;
        }
        
        if (protocolVehicleClassCeilings == null) {
            protocolVehicleClassCeilings = new ArrayList<>();
        } 
        
        protocolVehicleClassCeiling.setBreBand(this);
        protocolVehicleClassCeilings.add(protocolVehicleClassCeiling);
    }

    public List<BrePenaltyBand> getBrePenaltyBands() {
        return brePenaltyBands;
    }

    public void setBrePenaltyBands(List<BrePenaltyBand> brePenaltyBands) {
        this.brePenaltyBands = brePenaltyBands;
    }

    public void addBrePenaltyBand(BrePenaltyBand brePenaltyBand) {
        if (brePenaltyBand == null) {
            return;
        }
        
        if (brePenaltyBands == null) {
            brePenaltyBands = new ArrayList<>();
        } 
        brePenaltyBand.setBreBand(this);

        brePenaltyBands.add(brePenaltyBand);
    }

    public List<ClaimMatchingBand> getClaimMatchingBands() {
        return claimMatchingBands;
    }

    public void setClaimMatchingBands(List<ClaimMatchingBand> claimMatchingBands) {
        this.claimMatchingBands = claimMatchingBands;
    }

    public List<BreAppliedLiability> getAppliedLiabilities() {
        return appliedLiabilities;
    }

    public void setAppliedLiabilities(List<BreAppliedLiability> appliedLiabilities) {
        this.appliedLiabilities = appliedLiabilities;
    }

    public void addAppliedLiability(BreAppliedLiability appliedLiability) {
        if (appliedLiability == null) {
            return;
        }
        
        if (appliedLiabilities == null) {
            appliedLiabilities = new ArrayList<>();
        } 
        appliedLiability.setBreBand(this);

        appliedLiabilities.add(appliedLiability);
    }

    public void addClaimMatchingBand(ClaimMatchingBand claimMatchingBand) {
        if (claimMatchingBand == null) {
            return;
        }
        
        if (claimMatchingBands == null) {
            claimMatchingBands = new ArrayList<>();
        } 
        claimMatchingBand.setBreBand(this);

        claimMatchingBands.add(claimMatchingBand);
    }

    public boolean isHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling() {
        return hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling;
    }

    public void setHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling(boolean hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling) {
        this.hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling = hireNetDoesNotExceedProtocolVehicleClassHireNetCeiling;
    }

    public boolean isRepairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling() {
        return repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling;
    }

    public void setRepairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling(boolean repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling) {
        this.repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling = repairNetDoesNotExceedProtocolVehicleClassRepairNetCeiling;
    }

    public boolean isCompoundAutomaticChargeCheckHpiLookup() {
        return compoundAutomaticChargeCheckHpiLookup;
    }

    public void setCompoundAutomaticChargeCheckHpiLookup(boolean compoundAutomaticChargeCheckHpiLookup) {
        this.compoundAutomaticChargeCheckHpiLookup = compoundAutomaticChargeCheckHpiLookup;
    }

    public boolean isCompoundEstateChargeCheckHpiLookup() {
        return compoundEstateChargeCheckHpiLookup;
    }

    public void setCompoundEstateChargeCheckHpiLookup(boolean compoundEstateChargeCheckHpiLookup) {
        this.compoundEstateChargeCheckHpiLookup = compoundEstateChargeCheckHpiLookup;
    }

    public boolean isCompoundAutomaticEstateChargeCheckHpiLookup() {
        return compoundAutomaticEstateChargeCheckHpiLookup;
    }

    public void setCompoundAutomaticEstateChargeCheckHpiLookup(boolean compoundAutomaticEstateChargeCheckHpiLookup) {
        this.compoundAutomaticEstateChargeCheckHpiLookup = compoundAutomaticEstateChargeCheckHpiLookup;
    }

    public boolean isClientVatRegisteredCheck() {
        return clientVatRegisteredCheck;
    }

    public void setClientVatRegisteredCheck(boolean clientVatRegisteredCheck) {
        this.clientVatRegisteredCheck = clientVatRegisteredCheck;
    }

    public boolean isEngineerNetFeeCheck() {
        return engineerNetFeeCheck;
    }

    public void setEngineerNetFeeCheck(boolean engineerNetFeeCheck) {
        this.engineerNetFeeCheck = engineerNetFeeCheck;
    }

    public BigDecimal getMaxAllowedTotalLossNetFee() {
        return maxAllowedTotalLossNetFee;
    }

    public void setMaxAllowedTotalLossNetFee(BigDecimal maxAllowedTotalLossNetFee) {
        this.maxAllowedTotalLossNetFee = maxAllowedTotalLossNetFee;
    }

    public boolean isTotalLossFeeNetCeilingCheck() {
        return totalLossFeeNetCeilingCheck;
    }

    public void setTotalLossFeeNetCeilingCheck(boolean totalLossFeeNetCeilingCheck) {
        this.totalLossFeeNetCeilingCheck = totalLossFeeNetCeilingCheck;
    }

    public boolean isTotalLossAndStorageFeeCheck() {
        return totalLossAndStorageFeeCheck;
    }

    public void setTotalLossAndStorageFeeCheck(boolean totalLossAndStorageFeeCheck) {
        this.totalLossAndStorageFeeCheck = totalLossAndStorageFeeCheck;
    }

    public boolean isPaymentTeamActive() {
        return paymentTeamActive;
    }

    public void setPaymentTeamActive(boolean paymentTeamActive) {
        this.paymentTeamActive = paymentTeamActive;
    }

    public boolean isEnableClaimAudit() {
        return enableClaimAudit;
    }

    public void setEnableClaimAudit(boolean enableClaimAudit) {
        this.enableClaimAudit = enableClaimAudit;
    }

    public BigDecimal getAuditProcessPercentage() {
        return auditProcessPercentage;
    }

    public void setAuditProcessPercentage(BigDecimal auditProcessPercentage) {
        this.auditProcessPercentage = auditProcessPercentage;
    }

    public boolean isBreInvoiceSavingActive() {
        return breInvoiceSavingActive;
    }

    public void setBreInvoiceSavingActive(boolean breInvoiceSavingActive) {
        this.breInvoiceSavingActive = breInvoiceSavingActive;
    }

    public boolean isAllowGTAAutoPenaltyCharges() {
        return allowGTAAutoPenaltyCharges;
    }

    public void setAllowGTAAutoPenaltyCharges(boolean allowGTAAutoPenaltyCharges) {
        this.allowGTAAutoPenaltyCharges = allowGTAAutoPenaltyCharges;
    }

    public boolean isAllowSubscriberAutoPenaltyCharges() {
        return allowSubscriberAutoPenaltyCharges;
    }

    public void setAllowSubscriberAutoPenaltyCharges(boolean allowSubscriberAutoPenaltyCharges) {
        this.allowSubscriberAutoPenaltyCharges = allowSubscriberAutoPenaltyCharges;
    }

    public boolean isAllowFixedFeeAutoPenaltyCharges() {
        return allowFixedFeeAutoPenaltyCharges;
    }

    public void setAllowFixedFeeAutoPenaltyCharges(boolean allowFixedFeeAutoPenaltyCharges) {
        this.allowFixedFeeAutoPenaltyCharges = allowFixedFeeAutoPenaltyCharges;
    }

    public boolean isAllowCollaborationProtocolAutoPenaltyCharges() {
        return allowCollaborationProtocolAutoPenaltyCharges;
    }

    public void setAllowCollaborationProtocolAutoPenaltyCharges(boolean allowCollaborationProtocolAutoPenaltyCharges) {
        this.allowCollaborationProtocolAutoPenaltyCharges = allowCollaborationProtocolAutoPenaltyCharges;
    }

    public boolean isAllowTPIAutoPenaltyCharges() {
        return allowTPIAutoPenaltyCharges;
    }

    public void setAllowTPIAutoPenaltyCharges(boolean allowTPIAutoPenaltyCharges) {
        this.allowTPIAutoPenaltyCharges = allowTPIAutoPenaltyCharges;
    }

    public boolean isAllowInsurervsInsurerAutoPenaltyCharges() {
        return allowInsurervsInsurerAutoPenaltyCharges;
    }

    public void setAllowInsurervsInsurerAutoPenaltyCharges(boolean allowInsurervsInsurerAutoPenaltyCharges) {
        this.allowInsurervsInsurerAutoPenaltyCharges = allowInsurervsInsurerAutoPenaltyCharges;
    }

    public boolean isPauseSubscriberSlaClock() {
        return pauseSubscriberSlaClock;
    }

    public void setPauseSubscriberSlaClock(boolean pauseSubscriberSlaClock) {
        this.pauseSubscriberSlaClock = pauseSubscriberSlaClock;
    }

    public boolean isPauseFixedFeeSlaClock() {
        return pauseFixedFeeSlaClock;
    }

    public void setPauseFixedFeeSlaClock(boolean pauseFixedFeeSlaClock) {
        this.pauseFixedFeeSlaClock = pauseFixedFeeSlaClock;
    }

    public boolean isFraudCheckEnable() {
        return fraudCheckEnable;
    }

    public void setFraudCheckEnable(boolean fraudCheckEnable) {
        this.fraudCheckEnable = fraudCheckEnable;
    }

    public boolean isCopleyOfferMadeCheck() {
        return copleyOfferMadeCheck;
    }

    public void setCopleyOfferMadeCheck(boolean copleyOfferMadeCheck) {
        this.copleyOfferMadeCheck = copleyOfferMadeCheck;
    }
}
