package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasCalculatedCorrectDailyRate;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule002HasCalculatedCorrectDailyRateTest extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
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
        claim. getVehicleHire().setVehicleClass(testClaim.getTestVehicleClass());

        // SET EXTRAS FOR TESTING
        claim.getInvoice().setAdminFee(new BigDecimal(30));
        claim.getInvoice().setAdminQty(0);
        claim.getInvoice().setAutomaticFee(new BigDecimal(10));
        claim.getInvoice().setAutomaticQty(0);
        claim.getInvoice().setBabySeatFee(new BigDecimal(0));
        claim.getInvoice().setBabySeatQty(0);
        claim.getInvoice().setCdwFee(new BigDecimal(0));
        claim.getInvoice().setCdwQty(0);
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
        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_ESCALATED_TO_CH);
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
        claim.getVehicleHire().setVehicleClass(null);

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Vehicle Hire vehicle class is not specified."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_1() throws IOException {

        // getDailyHireRateCharged EQUALS TO allowedDailyRate

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim. getVehicleHire().getVehicleClass().setName("SP1");
//        claim. getVehicleHire().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(330.98));

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        /*
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        BigDecimal allowedDailyRate = claim.getVehicleHire(). getVehicleHire().getVehicleClass()().getPrice().add(claim.getBreBand().getHireRateChargeTolerance());
        System.out.println("allowedDailyRate: "+allowedDailyRate);
        System.out.println("getDailyHireRateCharged:"+cCalc.getDailyHireRateCharged());
        System.out.println("RESULT:"+cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate));
        System.out.println("RESULT:"+(cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate) <= 0));
        System.out.println(rv.getResult());
         */

        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:72.7450
         * getDailyHireRateCharged EQUALS allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_2() throws IOException {

        // getDailyHireRateCharged LESS THAN allowedDailyRate

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
//        claim.getVehicleHire().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(330.90));

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:72.7249
         * getDailyHireRateCharged LESS THAN allowedDailyRate
         * Result:PASSED
         */

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {

        // getDailyHireRateCharged MORE THAN allowedDailyRate

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
        // ToDo: here we need to create a vehiclePrice object and attach it to the vehicleClass (add to MockObjects)
//        claim.getVehicleHire().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(360.90));

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);


        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:80.2249
         * getDailyHireRateCharged MORE THAN allowedDailyRate
         * Result:FAILED
         */

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Daily rate billed for replacement vehicle class exceeds ABI rate."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
