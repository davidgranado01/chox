package scsbre.tests.data;

import java.math.BigDecimal;
import scsbre.engine.util.DateHelper;
import scsbre.tests.sample.*;

public class TestClaim {

    public InsurerInfo getTestInsurer(){

        InsurerInfo insurer = new InsurerInfo();

        insurer.setAddress1("ADDRESS 1");
        insurer.setAddress2("ADDRESS 2");
        insurer.setAddress3("ADDRESS 3");
        insurer.setAddress4("ADDRESS 4");
        insurer.setAddress5("ADDRESS 5");
        insurer.setAdminHandlingCharge(BigDecimal.ZERO);
        insurer.setCompanyNo("COMP NUMBER");
        insurer.setName("NAME");
        insurer.setPhone("PHONE");
        insurer.setPostcode("POSTCODE");
        insurer.setStatus(true);
        insurer.setVatNo("VAT NO");
        insurer.setVehicleClassCellings(null);
        insurer.setWorkgroupEnable(true);
        
        return insurer;
        
    }

    public CHOrganisationInfo getTestChorganisation(){

        CHOrganisationInfo cho = new CHOrganisationInfo();
        
        cho.setAddress1("Address 1");
        cho.setAddress2("Address 2");
        cho.setAddress3("Address 3");
        cho.setAddress4("Address 4");
        cho.setAddress5("Address 5");
        cho.setCompanyNo("Company Number");
        cho.setDelegatedAuthority(true);
        cho.setName("CHORG Name");
        cho.setPhone("PHONE");
        cho.setPostcode("POSTCODE");
        cho.setStatus(true);
        cho.setVatNo("VAT NUMBER");

        return cho;

    }

    public CHOBandInfo getTestChoBand(){

        CHOBandInfo band = new CHOBandInfo();

        band.setName("BAND");
        band.setIsActive(true);
        band.setInsurer(getTestInsurer());
        band.setIsMobileDayAllowance(2);
        band.setIsNotMobileDayAllowance(9);
        band.setTakeVehicleToGarageDaysMobile(1);
        band.setTakeVehicleToGarageDaysNonMobile(3);
        band.setTakeVehicleOutDays(1);
        band.setEngineerInspectionDelayDays(2);
        band.setOfferMadeDays(7);
        band.setReceiptOfFinalStatementChequeDays(10);
        band.setAverageLabourRate(40);
        band.setAverageLabourHoursPerHireDay(4);
        band.setInspectionDelayDays(4);
        band.setHireDayCeiling(22);
        band.setHireRateChargeTolerance(new BigDecimal("3.00"));

        // band.setHireNetCeiling(new BigDecimal("1500"));
        // band.setMaxRepairValue(BigDecimal.ZERO);
        
        band.setVehicleClassCelling(null);
        band.setVehicleClassCellingEnable(true);
        
        // ON / OFF FLAG
        band.setHireDayCountDoesNotExceedBandHireDayCeiling(true);
        band.setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        band.setEstateChargeCheck(true);
        band.setBabySeatChargeCheck(true);
        band.setCdwChargeCheck(true);
        band.setClaimHasZeroDiscountForDA(true);
        band.setCorrentAdminFee(true);
        band.setDeliveryOrCollectionChargeCheck(true);
        band.setDualControlChargeCheck(true);
        band.setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
        band.setFlaggedForManualInvoiceReview(true);
        band.setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);
        band.setHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero(true);
        band.setHasAllowedVehicleClass(true);
        band.setHasCalculatedCorrectDailyRate(true);
        band.setHasCalculatedTotalGrossEqualSuppliedTotalGross(true);
        band.setHasCorrectDiscountForNonDA(true);
        band.setHasCorrectHireVatCalculation(true);
        band.setHasCorrectRepairGrossCalculation(true);
        band.setHasCorrectRepairVatCalculation(true);
        band.setHasCorrectTotalNet(true);
        band.setHasCorrectTotalVat(true);
        band.setHasSuppliedCorrectTotalToPay(true);
        band.setLabourCostBusinessRule(true);
        band.setActualHireDaysDoesNotExceedAllowableHireDays(true);
        band.setActualHireDaysDoesNotExceedTotalLossInspection(true);
        band.setAutomaticChargeCheck(true);
        band.setRoofRackChargeCheck(true);
        band.setRepairBookedInDate(true);
        band.setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);
        band.setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        band.setSatelliteNavigationChargeCheck(true);
        band.setTowBarsChargeCheck(true);
        band.setValidateUniqueVehicleRegistrationNumber(true);
        band.setNonStandardRiskInsurancePremiumCheck(true);
        band.setNumberOfHireDaysReconcile(true);
        
        return band;

    }

    public EngineerReportInfo getTestEngineeringReport(){
        
        EngineerReportInfo engRpt = new EngineerReportInfo();

        engRpt.setEstimatedDaysUnderRepair(3);
        engRpt.setEstimatedLabourAmount(new BigDecimal("122.00"));
        engRpt.setEstimatedTotalRepairAmount(new BigDecimal("123.00"));

        return engRpt;

    }
    
    public CustomerVehicleDamageInfo getTestCustomerVehicleDamage(){
        
        CustomerVehicleDamageInfo customerDamage = new CustomerVehicleDamageInfo();

        customerDamage.setInitialECD(DateHelper.getCurrentDate());
        customerDamage.setIsUsable(true);
        
        return customerDamage;

    }
    
    public HireInfo getTestHireDetail(){

        HireInfo hireInfo = new HireInfo();

        hireInfo.setCollectionReason("REASON");
        hireInfo.setDays(3);
        hireInfo.setIsTotalLoss(true);
        hireInfo.setRentalEnd(DateHelper.getCurrentDate());
        hireInfo.setRentalStart(DateHelper.getCurrentDate());

        VehicleClassInfo vehicleClass = new VehicleClassInfo();
        vehicleClass.setCode("SP1");
        vehicleClass.setPrice(new BigDecimal("69.74"));
        
        hireInfo.setVehicleClass(vehicleClass);
        hireInfo.setVehicleManufacturer("ABC - 01");
        hireInfo.setVehicleModel("ABC - 02");
        hireInfo.setVehicleRegistration("ABC - 03");
        
        return hireInfo;

    }
    
    public HireMonitoringDetailInfo getTestHireMonitoringDetail(){
        
        HireMonitoringDetailInfo hireMonitoringDetail = new HireMonitoringDetailInfo();

        hireMonitoringDetail.setInspectionBookedDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setInspectionDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setIsTotalLostCheck(true);
        hireMonitoringDetail.setLabourCost(BigDecimal.ZERO);
        hireMonitoringDetail.setLabourHour(Integer.MIN_VALUE);
        hireMonitoringDetail.setLabourRate(BigDecimal.ZERO);
        hireMonitoringDetail.setNameOfIme("NAME IME");
        hireMonitoringDetail.setNameOfRepairer("ABC");
        hireMonitoringDetail.setNonProvisionReason("ABC");
        hireMonitoringDetail.setRepairBookInDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setRepairCompletionDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setTotalLossInspectionReport("ABC");

        return hireMonitoringDetail;
    }
    
    public InvoiceInfo getTestInvoice(){
        
        InvoiceInfo invoice = new InvoiceInfo();

        // EXTRA
        invoice.setAdminFee(new BigDecimal(30));
        invoice.setAdminQty(0);
        invoice.setAutomaticFee(new BigDecimal(10));
        invoice.setAutomaticQty(0);
        invoice.setBabySeatFee(new BigDecimal(0));
        invoice.setBabySeatQty(0);
        invoice.setCdwFee(new BigDecimal(0));
        invoice.setCdwQty(0);
        invoice.setEstateFee(new BigDecimal(0));
        invoice.setEstateQty(0);
        invoice.setDeliveryCollectionFee(new BigDecimal(0));
        invoice.setDeliveryCollectionQty(0);
        invoice.setDualControlFee(new BigDecimal(0));
        invoice.setDualControlQty(0);
        invoice.setRoofRackFee(new BigDecimal(0));
        invoice.setRoofRackQty(0);
        invoice.setSatNavFee(new BigDecimal(0));
        invoice.setSatNavQty(0);
        invoice.setTowBarsFee(new BigDecimal(0));
        invoice.setTowBarsQty(0);
        invoice.setNonStandardInsurancePremiumFee(new BigDecimal(0));
        invoice.setNonStandardInsurancePremiumQty(0);

        // INVOICE
        invoice.setClaimInvoiceNo(null);
        invoice.setDateInvoiced(null);
        invoice.setHandlingInvoiceNo(null);
        invoice.setEngineerInvoiceReviewNotes(null);
        invoice.setHireRateChargedPerDay(new BigDecimal(0));
        invoice.setIsEngineerDecisionApproved(true);
        invoice.setIsPaymentMode(true);
        invoice.setPenaltyChargeAppliedDate(null);
        invoice.setReasonOfRejectionId(2);
        invoice.setRejectionReason(null);
        
        invoice.setClaimsHandlingInvoiceAmount(new BigDecimal(0));
        invoice.setDeductionForClaimsHandlingFee(new BigDecimal(0));
        invoice.setDiscount(new BigDecimal(0));
        invoice.setExcessAmountCollected(new BigDecimal(0));
        invoice.setOriginalTotalToPay(new BigDecimal(0));
        invoice.setPenaltyAlertQty(0);
        invoice.setPenaltyCharge(new BigDecimal(0));
        invoice.setVatAmountCollected(new BigDecimal(0));
        
        // ADDITIONAL CHARGES
        invoice.setEngineerFeeNet(new BigDecimal(0));
        invoice.setEngineerFeeVat(new BigDecimal(0));
        invoice.setEngineerFeeGross(new BigDecimal(0));

        invoice.setHireGross(new BigDecimal(0));
        invoice.setHireNet(new BigDecimal(0));
        invoice.setHireVat(new BigDecimal(0));

        invoice.setStorageRecoveryGross(new BigDecimal(0));
        invoice.setStorageRecoveryNet(new BigDecimal(0));
        invoice.setStorageRecoveryVat(new BigDecimal(0));

        invoice.setRepairGross(new BigDecimal(0));
        invoice.setRepairNet(new BigDecimal(0));
        invoice.setRepairVat(new BigDecimal(0));

        // TOTAL
        invoice.setTotalGross(new BigDecimal(0));
        invoice.setTotalNet(new BigDecimal(0));
        invoice.setTotalVat(new BigDecimal(0));
        invoice.setTotalToPay(new BigDecimal(0));

        return invoice;
    }
    
    public VehicleClassInfo getTestVehicleClass(){

        VehicleClassInfo vehicleClass = new VehicleClassInfo();

        vehicleClass.setCode("SP1");
        vehicleClass.setPrice(new BigDecimal("69.74"));

        return vehicleClass;

    }

    public ExtrasInfo getTestExtras(){

        ExtrasInfo extra = new ExtrasInfo();
        
        extra.setAdminFee(new BigDecimal(30));
        extra.setAdminQty(0);
        extra.setAutomaticFee(new BigDecimal(10));
        extra.setAutomaticQty(0);
        extra.setBabySeatFee(new BigDecimal(0));
        extra.setBabySeatQty(0);
        extra.setCdwFee(new BigDecimal(0));
        extra.setCdwQty(0);
        extra.setEstateFee(new BigDecimal(0));
        extra.setEstateQty(0);
        extra.setDeliveryCollectionFee(new BigDecimal(0));
        extra.setDeliveryCollectionQty(0);
        extra.setDualControlFee(new BigDecimal(0));
        extra.setDualControlQty(0);
        extra.setRoofRackFee(new BigDecimal(0));
        extra.setRoofRackQty(0);
        extra.setSatNavFee(new BigDecimal(0));
        extra.setSatNavQty(0);
        extra.setTowBarsFee(new BigDecimal(0));
        extra.setTowBarsQty(0);
        extra.setNonStandardInsurancePremiumFee(new BigDecimal(0));
        extra.setNonStandardInsurancePremiumQty(0);

        return extra;
    }
    
    public ClaimInfo getTestClaim(){
        
        ClaimInfo claim = new ClaimInfo();

        claim.setIsInvoiceReviewRequired(true);
        claim.setManagingRepair(true);
        claim.setPolicyHolderContactDate(null);

        return claim;
    }

}
