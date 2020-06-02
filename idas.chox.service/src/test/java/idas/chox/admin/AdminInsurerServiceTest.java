package idas.chox.admin;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.model.AutomaticRoutingStrategy;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.UserService;
import idas.chox.service.ActionResponse;
import idas.chox.test.BaseTest;

public class AdminInsurerServiceTest extends BaseTest {

    @Autowired
    UserService service;


    // <editor-fold defaultstate="collapsed" desc="INSURER">
    @Test
    @Transactional
    public void testInsurer_GetInsurers() {

        // CHECK SELECT ALL INSURERS
        List<Insurer> insurers = adminInsurerService.getInsurers();
        Assert.assertEquals(7, insurers.size());

        // CHECK EVERY SINGLE INSURER
        for (Insurer ins : insurers) {
            Insurer insurer = adminInsurerService.getInsurer(ins.getId());
            Assert.assertEquals(ins.getName(), insurer.getName());
        }

    }

    @Test
    @Transactional
    public void testInsurer_TriggerInsurerStatus() {

        // CHECK SELECT ALL INSURERS
        List<Insurer> insurers = adminInsurerService.getInsurers();
        Assert.assertEquals(7, insurers.size());

        // CHECK EVERY SINGLE INSURER
        Insurer insurer = insurers.get(0);
        boolean originalStatus = insurer.isStatus();

        adminInsurerService.triggerInsurerStatus(insurer.getId());
        boolean newStatus = insurer.isStatus();
        Assert.assertEquals(originalStatus, !newStatus);

    }

    @Test
    @Transactional
    
    public void testInsurer_AddNewInsurer() {
        
       

        Insurer insurer = new Insurer();
//        insurer.setId(34);
//        insurer.setCreatedBy(service.getWebUser(999));
//        insurer.setLastModifiedBy(service.getWebUser(999));
//        insurer.setCreatedDate(new Date());
//        insurer.setLastModifiedDate(new Date());
//        insurer.setVersion(1);
        insurer.setName("TESTINSU");
        insurer.setAddress1("ADDRESS 1");
        insurer.setAddress2("ADDRESS 2");
        insurer.setAddress3("ADDRESS 3");
        insurer.setAddress4("ADDRESS 4");
        insurer.setAddress5("ADDRESS 5");
        insurer.setStatus(false);
        insurer.setVatNo("VATNUMBER");
        insurer.setCompanyNo("COMPANYNUMBER");
        insurer.setAutomaticRoutingStrategy(AutomaticRoutingStrategy.NONE);
        insurer.setClaimLocked(true);
        insurer.setPhone("PHONE");
        insurer.setPostcode("POSTCODE");
        insurer.setWorkgroupEnable(false);
        insurer.setAdminHandlingCharge(BigDecimal.ZERO);
        insurer.setClaimLocked(false);
        insurer.setClaimOwnershipEnable(false);
        insurer.setEngineersEnable(false);
        insurer.setFnolEnable(false);
        insurer.setVehicleClassCeilings(null);
        insurer.setInvoiceWorkgroup(null);
        insurer.setTpiRegexExpression(null);
        insurer.setTpiIdentificationString(null);
        insurer.setInvoiceOwner(null);
        insurer.setThirdPartyInterventionActivated(false);
        insurer.setTaskManagementEnable(false);
        insurer.setRelatedInsurer(null);
        insurer.setSupportProcedure(null);
        insurer.setBlockedMessage("You are blocked.");
        insurer.setBlockTime(0);
        insurer.setDisablePrivateNotes(false);
        insurer.setMaxLoginAttempts(0);
        insurer.setEcdIncreaseTriggerPercentage(0);
        insurer.setClaimAuditReviewEnable(false);
        insurer.setAcceptanceReasonEnable(false);

        ActionResponse response = adminInsurerService.updateInsurer(insurer, true, "test");
        Assert.assertTrue(response.getIsValid());

        int insurerId = insurer.getId();
        Insurer insurerNew = adminInsurerService.getInsurer(insurerId);
        Assert.assertEquals(insurerNew.getVatNo(), "VATNUMBER");
    }

    @Test
    @Transactional
    public void testInsurer_UpdateInsurer() {

        // CHECK SELECT ALL INSURERS
        List<Insurer> insurers = adminInsurerService.getInsurers();
        Assert.assertEquals(7, insurers.size());

        Insurer insurer = insurers.get(0);
        int insurerId = insurer.getId();

        String oAddress2 = "ASDFGHJKL";
        String oName = "LKJHGFDSA";
        String oCompanyNo = "0987654321";
        String oVatNo = "1234567890";

        insurer.setAddress2(oAddress2);
        insurer.setName(oName);
        insurer.setCompanyNo(oCompanyNo);
        insurer.setVatNo(oVatNo);

        ActionResponse response = adminInsurerService.updateInsurer(insurer, false, "LKJHGFDSA");
        Insurer insurerNew = adminInsurerService.getInsurer(insurerId);
        Assert.assertTrue(response.getIsValid());
        Assert.assertEquals(insurerNew.getAddress2(), oAddress2);
        Assert.assertEquals(insurerNew.getName(), oName);
        Assert.assertEquals(insurerNew.getCompanyNo(), oCompanyNo);
        Assert.assertEquals(insurerNew.getVatNo(), oVatNo);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER ALIAS">
    @Test
    @Transactional
    public void testInsurerAlias_GetAlias() {
        // CHECK SELECT ALL INSURER ALIASES
        Insurer insurer = insurerService.getInsurerByName("RSA");

        List<InsurerAlias> insurerAliases = adminInsurerService.getInsurerAliases(insurer.getId());
        Assert.assertEquals(26, insurerAliases.size());

        // CHECK EVERY SINGLE INSURER ALIASES
        for (InsurerAlias alias : insurerAliases) {
            InsurerAlias insurerAlias = adminInsurerService.getInsurerAlias(alias.getId());
            Assert.assertEquals(alias.getAliasName(), insurerAlias.getAliasName());
        }
    }

    @Test
    @Transactional
    public void testInsurerAlias_AddNewAlias() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        for (int i = 0; i < 100; i++) {
            ActionResponse response = adminInsurerService.addNewInsurerAlias(insurer.getId(), "ABC-ALIAS-" + i);
            Assert.assertTrue(response.getIsValid());
        }
        List<InsurerAlias> newInsurerAliases = adminInsurerService.getInsurerAliases(insurer.getId());
        Assert.assertEquals(126, newInsurerAliases.size());
    }

    @Test
    @Transactional
    public void testInsurerAlias_DeleteAlias() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<InsurerAlias> insurerAliases = adminInsurerService.getInsurerAliases(insurer.getId());
        Assert.assertEquals(26, insurerAliases.size());

        // CHECK EVERY SINGLE INSURER ALIASES
        for (InsurerAlias alias : insurerAliases) {
            ActionResponse response = adminInsurerService.removeInsurerAlias(alias);
            Assert.assertTrue(response.getIsValid());
        }

        List<InsurerAlias> newInsurerAliases = adminInsurerService.getInsurerAliases(insurer.getId());
        Assert.assertEquals(0, newInsurerAliases.size());

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER AUTOMATIC ROUTING">
    @Test
    @Transactional
    public void testInsurerAutoRouting_GetSelectedAutoRouting() {
        Insurer insurer = insurerService.getInsurerByName("RSA");

        List<AutomaticRoutingPolicy> autoRoutings = adminInsurerService.getInsurerAutomaticRoutings(insurer.getId());
        Assert.assertEquals(1, autoRoutings.size());

        // CHECK EVERY SINGLE AUTO ROUTING
        for (AutomaticRoutingPolicy obj : autoRoutings) {
            AutomaticRoutingPolicy existAutoRouting = adminInsurerService.getInsurerAutomaticRouting(obj.getId());
            Assert.assertEquals(existAutoRouting, obj);
        }
    }

    @Test
    @Transactional
    public void testInsurerAutoRouting_GetAvailableWorkgroupsForAutoRouting() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        Assert.assertEquals(5, (adminInsurerService.getAvailableWorkgroups(insurer.getId(), true)).size());
    }

    @Test
    @Transactional
    public void testInsurerAutoRouting_AddNewAutoRouting() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        List items;
        items = adminInsurerService.getAvailableWorkgroups(insurer.getId(), true);
        int workgroupId = ((IdLookupItem) items.get(0)).getId();

        ActionResponse response = adminInsurerService.addNewAutomaticRouting(insurer.getId(), workgroupId, "ABCDEFG");
        Assert.assertTrue(response.getIsValid());

        List<AutomaticRoutingPolicy> autoRoutings = adminInsurerService.getInsurerAutomaticRoutings(insurer.getId());
        Assert.assertEquals(2, autoRoutings.size());
    }

    @Test
    @Transactional
    public void testInsurerAutoRouting_DeleteAutoRouting() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<AutomaticRoutingPolicy> autoRoutings = adminInsurerService.getInsurerAutomaticRoutings(insurer.getId());

        // CHECK EVERY SINGLE AUTO ROUTING
        for (AutomaticRoutingPolicy obj : autoRoutings) {
            ActionResponse response = adminInsurerService.deleteAutomaticRouting(obj.getId());
            Assert.assertTrue(response.getIsValid());
        }
        Assert.assertEquals(0, adminInsurerService.getInsurerAutomaticRoutings(insurer.getId()).size());
    }

    @Test
    @Transactional
    public void testInsurerAutoRouting_UpdateAutoRouting() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<AutomaticRoutingPolicy> autoRoutings = adminInsurerService.getInsurerAutomaticRoutings(insurer.getId());

        // CHECK EVERY SINGLE AUTO ROUTING
        for (AutomaticRoutingPolicy obj : autoRoutings) {

            String newRefExp = "ABC-" + obj.getExpression();
            obj.setExpression(newRefExp);
            ActionResponse response = adminInsurerService.updateAutomaticRouting(obj);
            Assert.assertTrue(response.getIsValid());
            Assert.assertEquals(newRefExp, obj.getExpression());
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER BRE BAND">
    @Test
    @Transactional
    public void testInsurerBre_AddNew() {

        Insurer insurer = insurerService.getInsurerByName("Diamond");
        BreBand breBand = new BreBand();
        breBand.setInsurer(insurer);
        breBand.setIsActive(true);
        breBand.setTakeVehicleToGarageDaysMobile(1);
        breBand.setTakeVehicleToGarageDaysNonMobile(1);
        breBand.setWeekendBufferDays(1);
        breBand.setTakeVehicleOutDays(1);
        breBand.setEngineerInspectionDelayDaysMobile(1);
        breBand.setEngineerInspectionDelayDaysNonMobile(1);
        breBand.setIsMobileDayAllowance(1);
        breBand.setOfferMadeDays(1);
        breBand.setReceiptOfFinalStatementChequeDays(1);
        breBand.setInspectionDelayDays(1);
        breBand.setHireRateChargeTolerance(BigDecimal.valueOf(100.00));
        breBand.setHireNetCeiling(BigDecimal.valueOf(100.00));
        breBand.setHireDayCeiling(1);
        breBand.setRepairNetCeiling(BigDecimal.valueOf(100.00));
        breBand.setIsNotMobileDayAllowance(1);
        breBand.setAverageLabourHoursPerHireDay(1);
        breBand.setAverageLabourRateStandard(1);
        breBand.setAverageLabourRatePrestige(1);
        breBand.setName("BRE");
        breBand.setAutomaticChargeCheck(true);
        breBand.setBabySeatChargeCheck(true);
        breBand.setMiscellaneousChargeCheck(true);
        breBand.setDeliveryOrCollectionChargeCheck(true);
        breBand.setDualControlChargeCheck(true);
        breBand.setEstateChargeCheck(true);
        breBand.setNonStandardRiskInsurancePremiumCheck(true);
        breBand.setRoofRackChargeCheck(true);
        breBand.setSatelliteNavigationChargeCheck(true);
        breBand.setTowBarsChargeCheck(true);
        breBand.setActualHireDaysDoesNotExceedAllowableHireDays(true);
        breBand.setRepairChargeCheck(true);
        breBand.setClaimHasZeroDiscountForDA(true);
        breBand.setCorrentAdminFee(true);
        breBand.setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
        breBand.setFlaggedForManualInvoiceReview(true);
        breBand.setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);
        breBand.setHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero(true);
        breBand.setHasAllowedVehicleClass(true);
        breBand.setHasCalculatedCorrectDailyRate(true);
        breBand.setHasCalculatedTotalGrossEqualSuppliedTotalGross(true);
        breBand.setHasCorrectDiscountForNonDA(true);
        breBand.setHasCorrectHireGrossCalculation(true);
        breBand.setHasCorrectHireVatCalculation(true);
        breBand.setHasCorrectRepairGrossCalculation(true);
        breBand.setHasCorrectRepairVatCalculation(true);
        breBand.setHasCorrectTotalNet(true);
        breBand.setHasCorrectTotalVat(true);
        breBand.setHasSuppliedCorrectTotalToPay(true);
        breBand.setHireDayCountDoesNotExceedBandHireDayCeiling(true);
        breBand.setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        breBand.setLabourCostBusinessRule(true);
        breBand.setNumberOfHireDaysReconcile(true);
        breBand.setRepairBookedInDateOnThursday(false);
        breBand.setRepairBookedInDateOnFriday(true);
        breBand.setRepairBookedInDateOnSaturday(true);
        breBand.setRepairBookedInDateOnSunday(true);
        breBand.setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);
        breBand.setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        breBand.setValidateUniqueVehicleRegistrationNumber(true);
        breBand.setHireNetDoesNotExceedBandHireNetCeiling(true);
        breBand.setRepairNetDoesNotExceedBandRepairNetCeiling(true);
        breBand.setEnableClaimAudit(false);
        breBand.setAuditProcessPercentage(BigDecimal.ZERO);
        ActionResponse response = adminInsurerService.updateInsurerBreBand(breBand, insurer.getId(), true);
        Assert.assertTrue(response.getIsValid());
    }

    @Test
    @Transactional
    public void testInsurerBre_Delete() {

        Insurer insurer = insurerService.getInsurerByName("Diamond");
        BreBand breBand = new BreBand();
        breBand.setInsurer(insurer);
        breBand.setIsActive(true);
        breBand.setTakeVehicleToGarageDaysMobile(1);
        breBand.setTakeVehicleToGarageDaysNonMobile(1);
        breBand.setWeekendBufferDays(1);
        breBand.setTakeVehicleOutDays(1);
        breBand.setEngineerInspectionDelayDaysMobile(1);
        breBand.setEngineerInspectionDelayDaysNonMobile(1);
        breBand.setIsMobileDayAllowance(1);
        breBand.setOfferMadeDays(1);
        breBand.setReceiptOfFinalStatementChequeDays(1);
        breBand.setInspectionDelayDays(1);
        breBand.setHireRateChargeTolerance(BigDecimal.valueOf(100.00));
        breBand.setHireNetCeiling(BigDecimal.valueOf(100.00));
        breBand.setHireDayCeiling(1);
        breBand.setRepairNetCeiling(BigDecimal.valueOf(100.00));
        breBand.setIsNotMobileDayAllowance(1);
        breBand.setAverageLabourHoursPerHireDay(1);
        breBand.setAverageLabourRateStandard(1);
        breBand.setAverageLabourRatePrestige(1);
        breBand.setName("BRE");
        breBand.setAutomaticChargeCheck(true);
        breBand.setBabySeatChargeCheck(true);
        breBand.setMiscellaneousChargeCheck(true);
        breBand.setDeliveryOrCollectionChargeCheck(true);
        breBand.setDualControlChargeCheck(true);
        breBand.setEstateChargeCheck(true);
        breBand.setNonStandardRiskInsurancePremiumCheck(true);
        breBand.setRoofRackChargeCheck(true);
        breBand.setSatelliteNavigationChargeCheck(true);
        breBand.setTowBarsChargeCheck(true);
        breBand.setActualHireDaysDoesNotExceedAllowableHireDays(true);
        breBand.setActualHireDaysDoesNotExceedTotalLossInspection(true);
        breBand.setClaimHasZeroDiscountForDA(true);
        breBand.setCorrentAdminFee(true);
        breBand.setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
        breBand.setFlaggedForManualInvoiceReview(true);
        breBand.setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);
        breBand.setHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero(true);
        breBand.setHasAllowedVehicleClass(true);
        breBand.setHasCalculatedCorrectDailyRate(true);
        breBand.setHasCalculatedTotalGrossEqualSuppliedTotalGross(true);
        breBand.setHasCorrectDiscountForNonDA(true);
        breBand.setHasCorrectHireGrossCalculation(true);
        breBand.setHasCorrectHireVatCalculation(true);
        breBand.setHasCorrectRepairGrossCalculation(true);
        breBand.setHasCorrectRepairVatCalculation(true);
        breBand.setHasCorrectTotalNet(true);
        breBand.setHasCorrectTotalVat(true);
        breBand.setHasSuppliedCorrectTotalToPay(true);
        breBand.setHireDayCountDoesNotExceedBandHireDayCeiling(true);
        breBand.setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        breBand.setLabourCostBusinessRule(true);
        breBand.setNumberOfHireDaysReconcile(true);
        breBand.setRepairBookedInDateOnThursday(false);
        breBand.setRepairBookedInDateOnFriday(true);
        breBand.setRepairBookedInDateOnSaturday(true);
        breBand.setRepairBookedInDateOnSunday(true);
        breBand.setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);
        breBand.setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        breBand.setValidateUniqueVehicleRegistrationNumber(true);
        breBand.setHireNetDoesNotExceedBandHireNetCeiling(true);
        breBand.setRepairNetDoesNotExceedBandRepairNetCeiling(true);
        breBand.setEnableClaimAudit(false);
        breBand.setAuditProcessPercentage(BigDecimal.ZERO);
        ActionResponse response = adminInsurerService.updateInsurerBreBand(breBand, insurer.getId(), true);
        Assert.assertTrue(response.getIsValid());

        ActionResponse responseDelete = adminInsurerService.deleteInsurerBreBand(breBand);
        Assert.assertTrue(responseDelete.getIsValid());
    }

    @Test
    @Transactional
    public void testInsurerBre_Update() {
        Insurer insurer = insurerService.getInsurerByName("Diamond");
        BreBand breBand = new BreBand();
        breBand.setInsurer(insurer);
        breBand.setIsActive(true);
        breBand.setTakeVehicleToGarageDaysMobile(1);
        breBand.setTakeVehicleToGarageDaysNonMobile(1);
        breBand.setWeekendBufferDays(1);
        breBand.setTakeVehicleOutDays(1);
        breBand.setEngineerInspectionDelayDaysMobile(1);
        breBand.setEngineerInspectionDelayDaysNonMobile(1);
        breBand.setIsMobileDayAllowance(1);
        breBand.setOfferMadeDays(1);
        breBand.setReceiptOfFinalStatementChequeDays(1);
        breBand.setInspectionDelayDays(1);
        breBand.setHireRateChargeTolerance(BigDecimal.valueOf(100.00));
        breBand.setHireNetCeiling(BigDecimal.valueOf(100.00));
        breBand.setHireDayCeiling(1);
        breBand.setRepairNetCeiling(BigDecimal.valueOf(100.00));
        breBand.setIsNotMobileDayAllowance(1);
        breBand.setAverageLabourHoursPerHireDay(1);
        breBand.setAverageLabourRateStandard(1);
        breBand.setAverageLabourRatePrestige(1);
        breBand.setName("BRE");
        breBand.setAutomaticChargeCheck(true);
        breBand.setBabySeatChargeCheck(true);
        breBand.setMiscellaneousChargeCheck(true);
        breBand.setDeliveryOrCollectionChargeCheck(true);
        breBand.setDualControlChargeCheck(true);
        breBand.setEstateChargeCheck(true);
        breBand.setNonStandardRiskInsurancePremiumCheck(true);
        breBand.setRoofRackChargeCheck(true);
        breBand.setSatelliteNavigationChargeCheck(true);
        breBand.setTowBarsChargeCheck(true);
        breBand.setActualHireDaysDoesNotExceedAllowableHireDays(true);
        breBand.setActualHireDaysDoesNotExceedTotalLossInspection(true);
        breBand.setClaimHasZeroDiscountForDA(true);
        breBand.setCorrentAdminFee(true);
        breBand.setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
        breBand.setFlaggedForManualInvoiceReview(true);
        breBand.setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);
        breBand.setHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero(true);
        breBand.setHasAllowedVehicleClass(true);
        breBand.setHasCalculatedCorrectDailyRate(true);
        breBand.setHasCalculatedTotalGrossEqualSuppliedTotalGross(true);
        breBand.setHasCorrectDiscountForNonDA(true);
        breBand.setHasCorrectHireGrossCalculation(true);
        breBand.setHasCorrectHireVatCalculation(true);
        breBand.setHasCorrectRepairGrossCalculation(true);
        breBand.setHasCorrectRepairVatCalculation(true);
        breBand.setHasCorrectTotalNet(true);
        breBand.setHasCorrectTotalVat(true);
        breBand.setHasSuppliedCorrectTotalToPay(true);
        breBand.setHireDayCountDoesNotExceedBandHireDayCeiling(true);
        breBand.setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        breBand.setLabourCostBusinessRule(true);
        breBand.setNumberOfHireDaysReconcile(true);
        breBand.setRepairBookedInDateOnThursday(false);
        breBand.setRepairBookedInDateOnFriday(true);
        breBand.setRepairBookedInDateOnSaturday(true);
        breBand.setRepairBookedInDateOnSunday(true);
        breBand.setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);
        breBand.setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        breBand.setValidateUniqueVehicleRegistrationNumber(true);
        breBand.setHireNetDoesNotExceedBandHireNetCeiling(true);
        breBand.setRepairNetDoesNotExceedBandRepairNetCeiling(true);
        breBand.setEnableClaimAudit(false);
        breBand.setAuditProcessPercentage(BigDecimal.ZERO);
        ActionResponse response = adminInsurerService.updateInsurerBreBand(breBand, insurer.getId(), true);
        Assert.assertTrue(response.getIsValid());

        BreBand newBreBand = adminInsurerService.getBreBand(breBand.getId());
        newBreBand.setName("NEW BRE BAND NAME");
        ActionResponse response1 = adminInsurerService.updateInsurerBreBand(newBreBand, insurer.getId(), false);
        Assert.assertTrue(response1.getIsValid());
        Assert.assertEquals("NEW BRE BAND NAME", newBreBand.getName());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER VEHICLE CLASS CEILING">    
    @Test
    @Transactional
    public void testInsurerVehicleClassCeiling_AddNew() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<VehicleClass> vehicleClasses = vehicleClassService.getAllVehicleClass();

        for (VehicleClass vc : vehicleClasses) {
            VehicleClassCeiling vehicleClassCeiling = new VehicleClassCeiling();
            vehicleClassCeiling.setVehicleClass(vc);
            vehicleClassCeiling.setInsurer(insurer);
            vehicleClassCeiling.setHireNetCeiling(BigDecimal.valueOf(1000.00));
            vehicleClassCeiling.setRepairNetCeiling(BigDecimal.valueOf(1000.00));
            ActionResponse response = adminInsurerService.addNewVehicleClassCeiling(vehicleClassCeiling, vc.getId(), insurer.getId());
            Assert.assertTrue(response.getIsValid());
        }

        List<VehicleClassCeiling> vehicleClassCeiling = adminInsurerService.getVehicleClassCeilingByInsurer(insurer.getId());
        Assert.assertEquals(vehicleClasses.size(), vehicleClassCeiling.size());
    }

    @Test
    @Transactional
    public void testInsurerVehicleClassCeiling_Update() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<VehicleClass> vehicleClasses = vehicleClassService.getAllVehicleClass();

        for (VehicleClass vc : vehicleClasses) {
            VehicleClassCeiling vehicleClassCeiling = new VehicleClassCeiling();
            vehicleClassCeiling.setVehicleClass(vc);
            vehicleClassCeiling.setInsurer(insurer);
            vehicleClassCeiling.setHireNetCeiling(BigDecimal.valueOf(1000.00));
            vehicleClassCeiling.setRepairNetCeiling(BigDecimal.valueOf(1000.00));
            ActionResponse response = adminInsurerService.addNewVehicleClassCeiling(vehicleClassCeiling, vc.getId(), insurer.getId());
            Assert.assertTrue(response.getIsValid());
        }

        List<VehicleClassCeiling> vehicleClassCeiling = adminInsurerService.getVehicleClassCeilingByInsurer(insurer.getId());

        for (VehicleClassCeiling vcc : vehicleClassCeiling) {
            ActionResponse response = adminInsurerService.updateVehicleClassCeiling(vcc.getId(), 1000, 900);
            Assert.assertTrue(response.getIsValid());
        }
    }

    @Test
    @Transactional
    public void testInsurerVehicleClassCeiling_Delete() {

        Insurer insurer = insurerService.getInsurerByName("RBS");
        List<VehicleClass> vehicleClasses = vehicleClassService.getAllVehicleClass();

        for (VehicleClass vc : vehicleClasses) {
            VehicleClassCeiling vehicleClassCeiling = new VehicleClassCeiling();
            vehicleClassCeiling.setVehicleClass(vc);
            vehicleClassCeiling.setInsurer(insurer);
            vehicleClassCeiling.setHireNetCeiling(BigDecimal.valueOf(1000.00));
            vehicleClassCeiling.setRepairNetCeiling(BigDecimal.valueOf(1000.00));
            ActionResponse response = adminInsurerService.addNewVehicleClassCeiling(vehicleClassCeiling, vc.getId(), insurer.getId());
            Assert.assertTrue(response.getIsValid());
        }

        // CHECK NUMBER OF RECORD ADDED
        List<VehicleClassCeiling> vehicleClassCeiling = adminInsurerService.getVehicleClassCeilingByInsurer(insurer.getId());
        Assert.assertEquals(vehicleClasses.size(), vehicleClassCeiling.size());

        // REMOVE ALL NEWLY ADDED RECORD
        for (VehicleClassCeiling vcc : vehicleClassCeiling) {
            ActionResponse response = adminInsurerService.removeVehicleClassCeiling(vcc.getId());
            Assert.assertTrue(response.getIsValid());
        }

        List<VehicleClassCeiling> vehicleClassCeiling1 = adminInsurerService.getVehicleClassCeilingByInsurer(insurer.getId());
        Assert.assertEquals(0, vehicleClassCeiling1.size());

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER CH ORGANISATION">
    @Test
    @Transactional
    public void testInsurerCHO_Selected() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<InsurerChorganisation> insSelChorganisations = adminInsurerService.getInsurerChorganisations(insurer.getId());
        Assert.assertEquals(2, insSelChorganisations.size());
    }

    @Test
    @Transactional
    public void testInsurerCHO_Available() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<Chorganisation> insAvlChorganisations = adminInsurerService.getAvailableChorganisationsByInsurer(insurer.getId());
        Assert.assertEquals(0, insAvlChorganisations.size());
    }

    @Test
    @Transactional
    public void testInsurerCHO_AddNew() {
        Insurer insurer = insurerService.getInsurerByName("RBS");
        List<Chorganisation> insAvlChorganisations = adminInsurerService.getAvailableChorganisationsByInsurer(insurer.getId());
        List<InsurerChorganisation> insSelChorganisations = adminInsurerService.getInsurerChorganisations(insurer.getId());

        int iCount = insSelChorganisations.size();
        for (Chorganisation cho : insAvlChorganisations) {
            ActionResponse response = adminInsurerService.addNewInsurerChorganisation(insurer.getId(), cho.getId());
            Assert.assertTrue(response.getIsValid());
            iCount++;
        }

        insSelChorganisations = adminInsurerService.getInsurerChorganisations(insurer.getId());
        Assert.assertEquals(iCount, insSelChorganisations.size());
    }

    @Test
    @Transactional
    public void testInsurerCHO_Delete_Failed() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<InsurerChorganisation> insSelChorganisations = adminInsurerService.getInsurerChorganisations(insurer.getId());
        Assert.assertEquals(2, insSelChorganisations.size());

        for (InsurerChorganisation insCho : insSelChorganisations) {
            ActionResponse response = adminInsurerService.removeInsurerChorganisation(insCho.getId());
            Assert.assertTrue(response.getIsValid());
            validateError("", response, "Not allowed to delete this Credit Hire Org from this Insurer. Please remove the Bre Band assigned to this Credit Hire Organisation first.");
        }

        List<InsurerChorganisation> insSelChorganisationsAfter = adminInsurerService.getInsurerChorganisations(insurer.getId());
        Assert.assertEquals(2, insSelChorganisationsAfter.size());

    }

    @Test
    @Transactional
    public void testInsurerCHO_Delete_Passed() {
        // ADD NEW MAPPING
//        Insurer insurer = insurerService.getInsurerByName("RBS");
//        List<Chorganisation> insAvlChorganisations = adminInsurerService.getAvailableChorganisationsByInsurer(insurer.getId());
//
//        for (Chorganisation cho : insAvlChorganisations) {
//            ActionResponse response = adminInsurerService.addNewInsurerChorganisation(insurer.getId(), cho.getId());
//            Assert.assertTrue(response.getIsValid());
//        }
        // GET NEWLY ADDED MAPPING
//        List<InsurerChorganisation> insSelChorganisations = adminInsurerService.getInsurerChorganisations(insurer.getId());
//        Assert.assertEquals(2, insSelChorganisations.size());
//
//        for (InsurerChorganisation insCho : insSelChorganisations) {
//            ActionResponse response = adminInsurerService.removeInsurerChorganisation(insCho.getId());
//            Assert.assertTrue(response.getIsValid());
//        }
//
//        List<InsurerChorganisation> insSelChorganisationsAfter = adminInsurerService.getInsurerChorganisations(insurer.getId());
//        Assert.assertEquals(0, insSelChorganisationsAfter.size());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER WORKGROUP">
    @Test
    @Transactional
    public void testInsurerWorkgroup_Selected() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<Workgroup> workgroups = workgroupService.getActiveWorkgroupsByInsurer(insurer.getId());
        for (Workgroup workgroup : workgroups) {
            Workgroup eachWorkGroup = adminInsurerService.getWorkgroup(workgroup.getId());
            Assert.assertEquals(workgroup, eachWorkGroup);
        }
    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_SelecteAll() {
        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<Workgroup> workgroup1 = workgroupService.getActiveWorkgroupsByInsurer(insurer.getId());
        List<Workgroup> workgroup2 = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(workgroup1.size(), workgroup2.size());
    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_AddNewWorkgroup_Passed() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        Workgroup newWorkgroup = new Workgroup();
        newWorkgroup.setInsurer(insurer);
        newWorkgroup.setStatus(true);
        newWorkgroup.setName("WORKGROUP-DUMMY-TEMP");
        newWorkgroup.setSite("testing site");
        newWorkgroup.setTeam("tesing team");

        ActionResponse response = adminInsurerService.addNewInsurerWorkgroup(newWorkgroup, insurer.getId());
        Assert.assertTrue(response.getIsValid());
        validateError("", response, "Workgroup 'WORKGROUP-DUMMY-TEMP' has been created");
    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_AddNewWorkgroup_Failed() {

        Insurer insurer = insurerService.getInsurerByName("RSA");
        List<Workgroup> workgroups = workgroupService.getActiveWorkgroupsByInsurer(insurer.getId());

        Workgroup newWorkgroup = new Workgroup();
        newWorkgroup.setInsurer(insurer);
        newWorkgroup.setStatus(true);
        newWorkgroup.setName(workgroups.get(0).getName());
        newWorkgroup.setSite("testing site");
        newWorkgroup.setTeam("tesing team");

        ActionResponse response = adminInsurerService.addNewInsurerWorkgroup(newWorkgroup, insurer.getId());
        Assert.assertFalse(response.getIsValid());
        validateError("Error", response, "Workgroup '" + workgroups.get(0).getName() + "' already exists");
    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_UpdateStatus_FalseToTrue() {
        Insurer insurer = insurerService.getInsurerByName("RBS");
        updateInsurer(insurer);

        addWorkgroup(insurer, "RBS-WORKGROUP-1", false);

        List<Workgroup> workgroups = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(1, workgroups.size());
        ActionResponse response = adminInsurerService.triggerInsurerWorkgroupStatus(workgroups.get(0), insurer.getId());
        Assert.assertTrue(response.getIsValid());
    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_UpdateStatus_TrueToFalse_multiple_workgroups() {
        Insurer insurer = insurerService.getInsurerByName("RBS");
        updateInsurer(insurer);

        addWorkgroup(insurer, "RBS-WORKGROUP-1", true);
        addWorkgroup(insurer, "RBS-WORKGROUP-2", true);
        addWorkgroup(insurer, "RBS-WORKGROUP-3", true);
        addWorkgroup(insurer, "RBS-WORKGROUP-4", true);

        List<Workgroup> workgroups = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(4, workgroups.size());

        for (Workgroup workgroup : workgroups) {
            ActionResponse response = adminInsurerService.triggerInsurerWorkgroupStatus(workgroup, insurer.getId());
            Assert.assertTrue(response.getIsValid());
            return;
        }

        List<Workgroup> workgroups1 = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(3, workgroups1.size());

    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_UpdateStatus_TrueToFalse_single_workgroups() {
        Insurer insurer = insurerService.getInsurerByName("RBS");
        updateInsurer(insurer);

        addWorkgroup(insurer, "RBS-WORKGROUP-1", true);

        List<Workgroup> workgroups = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(1, workgroups.size());

        for (Workgroup workgroup : workgroups) {
            ActionResponse response = adminInsurerService.triggerInsurerWorkgroupStatus(workgroup, insurer.getId());
            Assert.assertFalse(response.getIsValid());
            validateError("Error", response, "Unable to de-activate this workgroup. Must maintain at least one active workgroup for this insurer.");
            return;
        }

        List<Workgroup> workgroups1 = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(1, workgroups1.size());

    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_RemoveMapping_single_workgroup() {

        Insurer insurer = insurerService.getInsurerByName("RBS");
        updateInsurer(insurer);

        addWorkgroup(insurer, "RBS-WORKGROUP-1", true);

        List<Workgroup> workgroups = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(1, workgroups.size());

        for (Workgroup workgroup : workgroups) {
            ActionResponse response = adminInsurerService.removeInsurerWorkgroup(workgroup.getId(), insurer.getId());
            Assert.assertFalse(response.getIsValid());
            validateError("Error", response, "Unable to remove this workgroup. Must maintain at least one active workgroup for this insurer.");
            return;
        }

        List<Workgroup> workgroups1 = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(1, workgroups1.size());

    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_RemoveMapping_multiple_workgroups_Passed() {

        Insurer insurer = insurerService.getInsurerByName("RBS");
        updateInsurer(insurer);

        addWorkgroup(insurer, "RBS-WORKGROUP-1", true);
        addWorkgroup(insurer, "RBS-WORKGROUP-2", true);

        List<Workgroup> workgroups = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(2, workgroups.size());

        for (Workgroup workgroup : workgroups) {
            ActionResponse response = adminInsurerService.removeInsurerWorkgroup(workgroup.getId(), insurer.getId());
            Assert.assertTrue(response.getIsValid());
            validateError("", response, "Workgroup '" + workgroup.getName() + "' has been removed");
            return;
        }

        List<Workgroup> workgroups1 = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(1, workgroups1.size());

    }

    @Test
    @Transactional
    public void testInsurerWorkgroup_RemoveMapping_multiple_workgroups_Failed() {

        Insurer insurer = insurerService.getInsurerByName("RBS");
        updateInsurer(insurer);

        addWorkgroup(insurer, "RBS-WORKGROUP-1", true);
        addWorkgroup(insurer, "RBS-WORKGROUP-2", true);
        addWorkgroup(insurer, "RBS-WORKGROUP-2", true);

        List<Workgroup> workgroups = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(3, workgroups.size());

        for (Workgroup workgroup : workgroups) {

            // ADD AUTOROUTING WHO USING SELECTED WORKGROUP
            adminInsurerService.addNewAutomaticRouting(insurer.getId(), workgroup.getId(), "ABCDEFG");

            ActionResponse response = adminInsurerService.removeInsurerWorkgroup(workgroup.getId(), insurer.getId());
            Assert.assertFalse(response.getIsValid());
            validateError("Error", response, "Workgroup '" + workgroup.getName() + "' cannot be removed");
            return;
        }

        List<Workgroup> workgroups1 = adminInsurerService.getInsurerWorkgroups(insurer.getId());
        Assert.assertEquals(3, workgroups1.size());

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER BRE BAND MAPPING">
    @Test
    @Transactional
    public void testInsurerBreMapping_Selected() {


        List<BreBandOrganisation> breBandOrganisations = adminInsurerService.getBreBandChorganisationsByBreBandId(101);
        Assert.assertEquals(2, breBandOrganisations.size());

        List<Chorganisation> chos = adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(3);
        Assert.assertEquals(0, chos.size());
    }

    @Test
    @Transactional
    public void testInsurerBreMapping_Delete() {

        int testBreBandId = 101;
        int testInsurerId = 3;

        List<BreBandOrganisation> breBandOrganisations = adminInsurerService.getBreBandChorganisationsByBreBandId(testBreBandId);

        Assert.assertEquals(2, breBandOrganisations.size());
        Assert.assertEquals(0, (adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(testInsurerId)).size());

        for (BreBandOrganisation breBandOrganisation : breBandOrganisations) {
            ActionResponse response = adminInsurerService.deleteBreBandChorganisation(breBandOrganisation.getId());
            breBandOrganisationService.getBreBandChorganisationsByBreBandId(testBreBandId);
            Assert.assertTrue(response.getIsValid());
            return;
        }

        Assert.assertEquals(1, (adminInsurerService.getBreBandChorganisationsByBreBandId(testBreBandId)).size());
        Assert.assertEquals(1, (adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(testInsurerId)).size());

    }

    @Test
    @Transactional
    public void testInsurerBreMapping_AddNew() {

        int testBreBandId = 21;
        int testInsurerId = 5;

        List<BreBandOrganisation> breBandOrganisations = adminInsurerService.getBreBandChorganisationsByBreBandId(testBreBandId);
        Assert.assertEquals(0, breBandOrganisations.size());
        Assert.assertEquals(0, (adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(testInsurerId)).size());

        // ADD INSURER V.S CHO MAPPING
        Chorganisation cho = chorganisationService.getActiveChorganisation().get(0);
        adminInsurerService.addNewInsurerChorganisation(testInsurerId, cho.getId());

        List<Chorganisation> chorganisation = adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(testInsurerId);
        Assert.assertEquals(1, chorganisation.size());

        //TODO:REVIEW
        for (Chorganisation chorg : chorganisation) {
            // ActionResponse response = adminInsurerService.addBreBandChorganisation(testBreBandId, chorg.getId());
            // breBandOrganisationService.getBreBandChorganisationsByBreBandId(testBreBandId);
            // Assert.assertTrue(response.getIsValid());
            return;
        }

        //Assert.assertEquals(1, (adminInsurerService.getBreBandChorganisationsByBreBandId(8)).size());
        // Assert.assertEquals(0, (adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(testInsurerId)).size());


    }
    // </editor-fold>

    private void updateInsurer(Insurer insurer) {
        insurer.setWorkgroupEnable(true);
        insurer.setClaimLocked(true);
        insurer.setClaimOwnershipEnable(true);
        insurer.setAutomaticRoutingStrategy(AutomaticRoutingStrategy.POLICY);
        insurerService.saveInsurer(insurer);
    }

    private void addWorkgroup(Insurer insurer, String workgroupName, boolean bStatus) {
        Workgroup workgroup = new Workgroup();
        workgroup.setInsurer(insurer);
        workgroup.setStatus(bStatus);
        workgroup.setName(workgroupName);
        workgroup.setSite("testing site");
        workgroup.setTeam("tesing team");
        workgroupService.saveWorkgroup(workgroup);
    }

    private void validateError(String resultType, ActionResponse response, String expectedMessage) {
        if (resultType.equalsIgnoreCase("Error")) {
            Assert.assertEquals(response.getErrors().get(0), expectedMessage);
        } else {
            Assert.assertEquals(response.getResult(), expectedMessage);
        }
    }
}
