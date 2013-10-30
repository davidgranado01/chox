package idas.chox.bre;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.bre.rules.CompoundAutomaticEstateChargeCheckWithHpiLookup;
import idas.chox.test.BaseTest;

/**
 *
 * @author John
 */
public class Rule083CompoundAutomaticEstateChargeCheckWithHpiLookupTest extends BaseTest {
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
        testClaim.getTestVehicleClass().setName("");
        claim.getVehicleHire().setVehicleClass(testClaim.getTestVehicleClass());

        // SET EXTRAS FOR TESTING
        claim.getInvoice().setAdminFee(BigDecimal.ZERO);
        claim.getInvoice().setAdminQty(0);
        claim.getInvoice().setAutomaticFee(BigDecimal.ZERO);
        claim.getInvoice().setAutomaticQty(0);
        claim.getInvoice().setBabySeatFee(BigDecimal.ZERO);
        claim.getInvoice().setBabySeatQty(0);
        claim.getInvoice().setMiscellaneousFee(BigDecimal.ZERO);
        claim.getInvoice().setMiscellaneousQty(0);
        claim.getInvoice().setEstateFee(BigDecimal.ZERO);
        claim.getInvoice().setEstateQty(0);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.ZERO);
        claim.getInvoice().setDeliveryCollectionQty(0);
        claim.getInvoice().setDualControlFee(BigDecimal.ZERO);
        claim.getInvoice().setDualControlQty(0);
        claim.getInvoice().setRoofRackFee(BigDecimal.ZERO);
        claim.getInvoice().setRoofRackQty(0);
        claim.getInvoice().setSatNavFee(BigDecimal.ZERO);
        claim.getInvoice().setSatNavQty(0);
        claim.getInvoice().setTowBarsFee(BigDecimal.ZERO);
        claim.getInvoice().setTowBarsQty(0);
        claim.getInvoice().setNonStandardInsurancePremiumFee(BigDecimal.ZERO);
        claim.getInvoice().setNonStandardInsurancePremiumQty(0);

        // SET HIRE DETAIL
        claim.getVehicleHire().setDays(1);



        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(false);
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
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
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.setVehicleHire(null);
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_3() throws IOException {

        /*
         * CHO Control Flag is ON
         * Claim. V CLass does not end in 'A'
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("SP1");
        claim.getVehicleHire().getVehicleClass().setId(65);
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testSkipped_4() throws IOException {

        // car identified as automatic

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("CV1ESTA");
        claim.getVehicleHire().getVehicleClass().setId(145);
        claim.getVehicleHire().setDays(1);
        claim.getVehicleHire().setHpiVehicleTransmission("Automatic");
        claim.getVehicleHire().setHpiVehicleDoorplan("Estate");

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
        claim.getVehicleHire().getVehicleClass().setId(65);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(80.00));

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
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

        // car not identified as automatic, charge rate less than expected

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("CV1ESTA");
        claim.getVehicleHire().getVehicleClass().setId(145);
        claim.getVehicleHire().setDays(1);
        claim.getVehicleHire().setHpiVehicleTransmission("Manual");
        claim.getVehicleHire().setHpiVehicleDoorplan("Estate");

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(84.00));

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 80.0
         * getDailyHireRateCharged:80
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

        // car not identified as an estate, charge rate less than expected

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("CV1ESTA");
        claim.getVehicleHire().getVehicleClass().setId(145);
        claim.getVehicleHire().setDays(1);
        claim.getVehicleHire().setHpiVehicleTransmission("Automatic");
        claim.getVehicleHire().setHpiVehicleDoorplan("Saloon");

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(84.00));

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 80.0
         * getDailyHireRateCharged:80
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
    public void testFailed_1() throws IOException {

        // car not identified as automatic, charge rate equal to expected

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("CV1ESTA");
        claim.getVehicleHire().getVehicleClass().setId(145);
        claim.getVehicleHire().setDays(1);
        claim.getVehicleHire().setHpiVehicleTransmission("Manual");
        claim.getVehicleHire().setHpiVehicleDoorplan("Estate");

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(85.00));

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 80.0
         * getDailyHireRateCharged:80
         * getDailyHireRateCharged EQUALS allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging for an automatic and an estate fee for the hire and the HPI lookup did not identify the hire vehicle to be an automatic and/or an estate, please review need."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testFailed_2() throws IOException {

        // car not identified as estate, charge rate equal to expected

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("CV1ESTA");
        claim.getVehicleHire().getVehicleClass().setId(145);
        claim.getVehicleHire().setDays(1);
        claim.getVehicleHire().setHpiVehicleTransmission("Automatic");
        claim.getVehicleHire().setHpiVehicleDoorplan("Saloon");

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(85.00));

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 80.0
         * getDailyHireRateCharged:80
         * getDailyHireRateCharged EQUALS allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging for an automatic and an estate fee for the hire and the HPI lookup did not identify the hire vehicle to be an automatic and/or an estate, please review need."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testFailed_3() throws IOException {

        // car not identified as estate or automatic, charge rate equal to expected

        Claim claim = getTestClaim();
        claim.getBreBand().setCompoundAutomaticEstateChargeCheckHpiLookup(true);
        claim.getVehicleHire().getVehicleClass().setName("CV1ESTA");
        claim.getVehicleHire().getVehicleClass().setId(145);
        claim.getVehicleHire().setDays(1);
        claim.getVehicleHire().setHpiVehicleTransmission("Manual");
        claim.getVehicleHire().setHpiVehicleDoorplan("Saloon");

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(85.00));

        CompoundAutomaticEstateChargeCheckWithHpiLookup rule = new CompoundAutomaticEstateChargeCheckWithHpiLookup();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        /*
         * allowedDailyRate: 80.0
         * getDailyHireRateCharged:80
         * getDailyHireRateCharged EQUALS allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging for an automatic and an estate fee for the hire and the HPI lookup did not identify the hire vehicle to be an automatic and/or an estate, please review need."));
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
