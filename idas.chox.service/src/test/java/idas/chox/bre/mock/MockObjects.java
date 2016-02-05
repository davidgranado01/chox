package idas.chox.bre.mock;

import java.math.BigDecimal;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.util.DateHelper;

public class MockObjects {

    public Insurer getTestInsurer(){

        Insurer insurer = new Insurer();

        //insurer.setId(3);
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
        insurer.setVehicleClassCeilings(null);
        insurer.setWorkgroupEnable(true);
        insurer.setClaimAuditReviewEnable(false);
        insurer.setAcceptanceReasonEnable(false);
        
        return insurer;
        
    }

    public Chorganisation getTestChorganisation(){

        Chorganisation cho = new Chorganisation();
        
        //cho.setId(null);
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

    public BreBand getTestBreBand(){

        BreBand band = new BreBand();

        band.setName("BAND");
        band.setIsActive(true);
        band.setInsurer(getTestInsurer());
        band.setIsMobileDayAllowance(2);
        band.setIsNotMobileDayAllowance(9);
        band.setTakeVehicleToGarageDaysMobile(1);
        band.setTakeVehicleToGarageDaysNonMobile(3);
        band.setTakeVehicleOutDays(1);
        band.setEngineerInspectionDelayDaysMobile(2);
        band.setEngineerInspectionDelayDaysNonMobile(2);
        band.setOfferMadeDays(7);
        band.setReceiptOfFinalStatementChequeDays(10);
        band.setAverageLabourRateStandard(40);
        band.setAverageLabourRatePrestige(40);
        band.setAverageLabourHoursPerHireDay(4);
        band.setInspectionDelayDays(4);
        band.setHireDayCeiling(22);
        band.setHireRateChargeTolerance(new BigDecimal("0.00"));
        band.setTotalVatTolerance(null);
        band.setHireDaysPriorToDateRepairCommenced(5);
        band.setHireDaysPriorToDateRepairBookInDateNonMobileVehicles(3);
        band.setHireDaysPriorToDateRepairBookInDateMobileVehicles(1);
        band.setAuditProcessPercentage(BigDecimal.ZERO);
        band.setEnableClaimAudit(false);

        // band.setHireNetCeiling(new BigDecimal("1500"));
        // band.setMaxRepairValue(BigDecimal.ZERO);
        
        band.setVehicleClassCeiling(null);
        
        // ON / OFF FLAG
        band.setHireDayCountDoesNotExceedBandHireDayCeiling(true);
        band.setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        band.setEstateChargeCheck(true);
        band.setBabySeatChargeCheck(true);
        band.setMiscellaneousChargeCheck(true);
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
        band.setRepairBookedInDateOnThursday(true);
        band.setRepairBookedInDateOnFriday(true);
        band.setRepairBookedInDateOnSaturday(true);
        band.setRepairBookedInDateOnSunday(true);
        band.setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);
        band.setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        band.setSatelliteNavigationChargeCheck(true);
        band.setTowBarsChargeCheck(true);
        band.setValidateUniqueVehicleRegistrationNumber(true);
        band.setNonStandardRiskInsurancePremiumCheck(true);
        band.setNumberOfHireDaysReconcile(true);
        band.setRepairNetDoesNotExceedBandRepairNetCeiling(true);
        band.setHireNetDoesNotExceedBandHireNetCeiling(true);

        
        
        return band;

    }

    public EngineerReport getTestEngineeringReport(){
        
        EngineerReport engRpt = new EngineerReport();

        engRpt.setDays(3);
        engRpt.setLabourAmount(new BigDecimal("122.00"));
        engRpt.setTotalAmount(new BigDecimal("123.00"));

        return engRpt;

    }
    
    public Customer getTestCustomerVehicleDamage(){
        
        Customer customer = new Customer();

        customer.setInitialECD(DateHelper.getCurrentDate());
        customer.setIsUsable(true);
        
        return customer;

    }
    
    public VehicleHire getTestHireDetail(){

        VehicleHire hire = new VehicleHire();

        hire.setCollectionReason("REASON");
        hire.setDays(3);
        hire.setRentalEnd(DateHelper.getCurrentDate());
        hire.setRentalStart(DateHelper.getCurrentDate());
        hire.setHireEnd(DateHelper.getCurrentDate());
        hire.setHireStart(DateHelper.getCurrentDate());

        VehicleClass vehicleClass = new VehicleClass();
        vehicleClass.setName("SP1");

        //ToDo: Create a vehiclePrice object for the vehicleClass
//        vehicleClass.setPrice(new BigDecimal("69.74"));
        hire.setVehicleClass(vehicleClass);
        hire.setVehicleManufacturer("ABC - 01");
        hire.setVehicleModel("ABC - 02");
        hire.setVehicleRegistration("ABC - 03");
        
        return hire;

    }
    
    public HireMonitoringDetail getTestHireMonitoringDetail(){
        
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();

        hireMonitoringDetail.setInspectionBookedDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setInspectionDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setIsTotalLostCheck(true);
        hireMonitoringDetail.setLabourCost(BigDecimal.ZERO);
        hireMonitoringDetail.setLabourHour(BigDecimal.ZERO);
        hireMonitoringDetail.setLabourRate(BigDecimal.ZERO);
        hireMonitoringDetail.setNameOfIme("NAME IME");
        hireMonitoringDetail.setNameOfRepairer("ABC");
        hireMonitoringDetail.setNonProvisionReason("ABC");
        hireMonitoringDetail.setRepairBookInDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setRepairCompletionDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setTotalLossInspectionReport("ABC");

        return hireMonitoringDetail;
    }
    
    public Invoice getTestInvoice(){
        
        Invoice invoice = new Invoice();

        // EXTRA
        invoice.setAdminFee(new BigDecimal(30));
        invoice.setAdminQty(0);
        invoice.setAutomaticFee(new BigDecimal(10));
        invoice.setAutomaticQty(0);
        invoice.setBabySeatFee(new BigDecimal(0));
        invoice.setBabySeatQty(0);
        invoice.setMiscellaneousFee(new BigDecimal(0));
        invoice.setMiscellaneousQty(0);
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
        invoice.setHirePenaltyChargeAppliedDate(null);
        invoice.setReasonOfRejection(getTestReasonOfRejection());
        
        invoice.setClaimsHandlingInvoiceAmount(new BigDecimal(0));
        invoice.setDeductionForClaimsHandlingFee(new BigDecimal(0));
        invoice.setDiscount(new BigDecimal(0));
        invoice.setExcessAmountCollected(new BigDecimal(0));
//        invoice.setOriginalFullTotalToPay(new BigDecimal(0));
//        invoice.setPenaltyAlertQty(0);
        invoice.setHirePenaltyCharge(new BigDecimal(0));
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
        invoice.setFullTotalToPay(new BigDecimal(0));


        invoice.setTotalLossFeeNet(new BigDecimal(0));
        invoice.setTotalLossFeeVat(new BigDecimal(0));
        invoice.setTotalLossFeeGross(new BigDecimal(0));

        invoice.setCollaborationFee(BigDecimal.ZERO);

        invoice.setAdditionalDriverFee(BigDecimal.ZERO);


        
     
        return invoice;
    }
    
    public VehicleClass getTestVehicleClass(){

        VehicleClass vehicleClass = new VehicleClass();

        


        vehicleClass.setName("SP1");
        //ToDo: Create a vehiclePrice object for the vehicleClass
//        vehicleClass.setPrice(new BigDecimal("69.74"));


        return vehicleClass;

    }

    public ReasonOfRejection getTestReasonOfRejection()
    {
        ReasonOfRejection reasonOfRejection = new ReasonOfRejection();
        reasonOfRejection.setId(2);

        return reasonOfRejection;
    }

    public Invoice getTestExtras(){

        Invoice extra = new Invoice();

        extra.setAdminFee(new BigDecimal(30));
        extra.setAdminQty(0);
        extra.setAutomaticFee(new BigDecimal(10));
        extra.setAutomaticQty(0);
        extra.setBabySeatFee(new BigDecimal(0));
        extra.setBabySeatQty(0);
        extra.setMiscellaneousFee(new BigDecimal(0));
        extra.setMiscellaneousQty(0);
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
        extra.setCollaborationFee(BigDecimal.ZERO);
        extra.setCollaborationQty(0);

        return extra;
    }
    
    public Claim getTestClaim(){
        
        Claim claim = new Claim();

        claim.setIsInvoiceReviewRequired(true);
        claim.setManagingRepair(true);
        claim.setPolicyHolderContactDate(null);

        return claim;
    }

}
