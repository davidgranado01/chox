package idas.chox.bre;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.bre.rules.HasCalculatedCorrectDailyRate;

public class Rule002HasCalculatedCorrectDailyRateTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim() {

        Claim claim = new Claim();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setBreBand(testClaim.getTestBreBand());
        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setInvoice(testClaim.getTestExtras());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getVehicleHire().setVehicleClass(testClaim.getTestVehicleClass());

        // SET EXTRAS FOR TESTING
        claim.getInvoice().setAdminFee(new BigDecimal(30));
        claim.getInvoice().setAdminQty(0);
        claim.getInvoice().setAutomaticFee(new BigDecimal(10));
        claim.getInvoice().setAutomaticQty(0);
        claim.getInvoice().setBabySeatFee(new BigDecimal(0));
        claim.getInvoice().setBabySeatQty(0);
        claim.getInvoice().setMiscellaneousFee(new BigDecimal(0));
        claim.getInvoice().setMiscellaneousQty(0);
        claim.getInvoice().setEstateFee(new BigDecimal(0));
        claim.getInvoice().setEstateQty(0);
        claim.getInvoice().setDeliveryCollectionFee(new BigDecimal(0));
        claim.getInvoice().setDeliveryCollectionQty(0);
        claim.getInvoice().setDualControlFee(new BigDecimal(0));
        claim.getInvoice().setDualControlQty(0);
        claim.getInvoice().setRoofRackFee(new BigDecimal(0));
        claim.getInvoice().setRoofRackQty(0);
        claim.getInvoice().setSatNavFee(new BigDecimal(0));
        claim.getInvoice().setSatNavQty(0);
        claim.getInvoice().setTowBarsFee(new BigDecimal(0));
        claim.getInvoice().setTowBarsQty(0);
        claim.getInvoice().setNonStandardInsurancePremiumFee(new BigDecimal(0));
        claim.getInvoice().setNonStandardInsurancePremiumQty(0);

        // SET HIRE DETAIL
        claim.getVehicleHire().setDays(4);



        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCalculatedCorrectDailyRate(false);
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        HasCalculatedCorrectDailyRate rule = new HasCalculatedCorrectDailyRate();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {

        /*
         * CHO Control Flag is ON
         * Claim. V CLass is Null
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);
        claim.setVehicleHire(null);
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        HasCalculatedCorrectDailyRate rule = new HasCalculatedCorrectDailyRate();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testPassed_1() throws IOException {

        // getDailyHireRateCharged EQUALS TO allowedDailyRate

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
        claim.getVehicleHire().getVehicleClass().setId(65);
//        claim. getVehicleHire().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(334.08));

        HasCalculatedCorrectDailyRate rule = new HasCalculatedCorrectDailyRate();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:72.7450
         * getDailyHireRateCharged EQUALS allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testPassed_2() throws IOException {

        // getDailyHireRateCharged LESS THAN allowedDailyRate

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
        claim.getVehicleHire().getVehicleClass().setId(65);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(330.90));

        HasCalculatedCorrectDailyRate rule = new HasCalculatedCorrectDailyRate();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);


        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:72.7249
         * getDailyHireRateCharged LESS THAN allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testFailed() throws IOException {

        // getDailyHireRateCharged MORE THAN allowedDailyRate

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
        claim.getVehicleHire().getVehicleClass().setId(65);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(360.90));

        HasCalculatedCorrectDailyRate rule = new HasCalculatedCorrectDailyRate();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:80.2249
         * getDailyHireRateCharged MORE THAN allowedDailyRate
         * Result:FAILED
         */
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The daily rate billed of £80.23 for the replacement vehicle class SP1 exceeds the allowed ABI rate of £73.52."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    /**
     * @param vehicleClassPriceService the vehicleClassPriceService to set
     */
    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    /**
     * @param insurerChorganisationService the insurerChorganisationService to set
     */
    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }
}
