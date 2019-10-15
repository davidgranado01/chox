package idas.chox.bre.mock;

import java.math.BigDecimal;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerHireMonitoringDetail;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.util.DateHelper;
import java.util.Date;

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
        band.setHireRateChargeTolerance(BigDecimal.ZERO);
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
    
    public InsurerHireMonitoringDetail getTestInsurerHireMonitoringDetail(){
        
        InsurerHireMonitoringDetail hireMonitoringDetail = new InsurerHireMonitoringDetail();

        hireMonitoringDetail.setInspectionBookedDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setInspectionDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setLabourCost(BigDecimal.ZERO);
        hireMonitoringDetail.setLabourHour(BigDecimal.ZERO);
        hireMonitoringDetail.setLabourRate(BigDecimal.ZERO);
        hireMonitoringDetail.setRepairBookInDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setRepairCompletionDate(DateHelper.getCurrentDate());
        hireMonitoringDetail.setClaimantImpecunious(Boolean.TRUE);

        return hireMonitoringDetail;
    }
    
    public HireMonitoringEcd getTestHireMonitoringEcd(){
        
        HireMonitoringEcd hireMonitoringEcd = new HireMonitoringEcd();

        hireMonitoringEcd.setEcdDate(DateHelper.parse("27/02/2011"));
        hireMonitoringEcd.setSequence(0);
        hireMonitoringEcd.setReason("Mock ECD reason");
        hireMonitoringEcd.setSupportingNote("Mock supporting note for ECD");
        hireMonitoringEcd.setUpdateInsurer(false);
    
        return hireMonitoringEcd;
    }
    
    public Invoice getTestInvoice(){
        
        Invoice invoice = new Invoice();

        // EXTRA
        invoice.setAdminFee(new BigDecimal(30));
        invoice.setAdminQty(0);
        invoice.setAutomaticFee(BigDecimal.TEN);
        invoice.setAutomaticQty(0);
        invoice.setBabySeatFee(BigDecimal.ZERO);
        invoice.setBabySeatQty(0);
        invoice.setMiscellaneousFee(BigDecimal.ZERO);
        invoice.setMiscellaneousQty(0);
        invoice.setEstateFee(BigDecimal.ZERO);
        invoice.setEstateQty(0);
        invoice.setDeliveryCollectionFee(BigDecimal.ZERO);
        invoice.setDeliveryCollectionQty(0);
        invoice.setDualControlFee(BigDecimal.ZERO);
        invoice.setDualControlQty(0);
        invoice.setRoofRackFee(BigDecimal.ZERO);
        invoice.setRoofRackQty(0);
        invoice.setSatNavFee(BigDecimal.ZERO);
        invoice.setSatNavQty(0);
        invoice.setTowBarsFee(BigDecimal.ZERO);
        invoice.setTowBarsQty(0);
        invoice.setNonStandardInsurancePremiumFee(BigDecimal.ZERO);
        invoice.setNonStandardInsurancePremiumQty(0);

        // INVOICE
        invoice.setClaimInvoiceNo(null);
        invoice.setDateInvoiced(new Date());
        invoice.setHandlingInvoiceNo(null);
        invoice.setEngineerInvoiceReviewNotes(null);
        invoice.setHireRateChargedPerDay(BigDecimal.ZERO);
        invoice.setIsEngineerDecisionApproved(true);
        invoice.setIsPaymentMode(true);
        invoice.setHirePenaltyChargeAppliedDate(null);
        invoice.setReasonOfRejection(getTestReasonOfRejection());
        
        invoice.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        invoice.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        invoice.setDiscount(BigDecimal.ZERO);
        invoice.setInsurerDiscount(BigDecimal.ZERO);
        invoice.setTotalGrossInsurerDiscount(BigDecimal.ZERO);
        invoice.setRepairGrossInsurerDiscount(BigDecimal.ZERO);
        invoice.setHireGrossInsurerDiscount(BigDecimal.ZERO);
        invoice.setExcessAmountCollected(BigDecimal.ZERO);
//        invoice.setOriginalFullTotalToPay(new BigDecimal(0));
//        invoice.setPenaltyAlertQty(0);
        invoice.setHirePenaltyCharge(BigDecimal.ZERO);
        invoice.setVatAmountCollected(BigDecimal.ZERO);
        
        // ADDITIONAL CHARGES
        invoice.setEngineerFeeNet(BigDecimal.ZERO);
        invoice.setEngineerFeeVat(BigDecimal.ZERO);
        invoice.setEngineerFeeGross(BigDecimal.ZERO);

        invoice.setHireGross(BigDecimal.ZERO);
        invoice.setHireNet(BigDecimal.ZERO);
        invoice.setHireVat(BigDecimal.ZERO);

        invoice.setStorageRecoveryGross(BigDecimal.ZERO);
        invoice.setStorageRecoveryNet(BigDecimal.ZERO);
        invoice.setStorageRecoveryVat(BigDecimal.ZERO);

        invoice.setRepairGross(BigDecimal.ZERO);
        invoice.setRepairNet(BigDecimal.ZERO);
        invoice.setRepairVat(BigDecimal.ZERO);

        // TOTAL
        invoice.setTotalGross(BigDecimal.ZERO);
        invoice.setTotalNet(BigDecimal.ZERO);
        invoice.setTotalVat(BigDecimal.ZERO);
        invoice.setFullTotalToPay(BigDecimal.ZERO);


        invoice.setTotalLossFeeNet(BigDecimal.ZERO);
        invoice.setTotalLossFeeVat(BigDecimal.ZERO);
        invoice.setTotalLossFeeGross(BigDecimal.ZERO);

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
        extra.setAutomaticFee(BigDecimal.TEN);
        extra.setAutomaticQty(0);
        extra.setBabySeatFee(BigDecimal.ZERO);
        extra.setBabySeatQty(0);
        extra.setMiscellaneousFee(BigDecimal.ZERO);
        extra.setMiscellaneousQty(0);
        extra.setEstateFee(BigDecimal.ZERO);
        extra.setEstateQty(0);
        extra.setDeliveryCollectionFee(BigDecimal.ZERO);
        extra.setDeliveryCollectionQty(0);
        extra.setDualControlFee(BigDecimal.ZERO);
        extra.setDualControlQty(0);
        extra.setRoofRackFee(BigDecimal.ZERO);
        extra.setRoofRackQty(0);
        extra.setSatNavFee(BigDecimal.ZERO);
        extra.setSatNavQty(0);
        extra.setTowBarsFee(BigDecimal.ZERO);
        extra.setTowBarsQty(0);
        extra.setNonStandardInsurancePremiumFee(BigDecimal.ZERO);
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
