package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HireDayCountDoesNotExceedBandHireDayCeiling;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule004HireDayCountDoesNotExceedBandHireDayCeilingTest extends TestCase {

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
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        claim.getVehicleHire().setDays(123);
        claim.getBreBand().setHireDayCeiling(123);

        return claim;
    }

    @Test
    public void testSkipped() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireDayCountDoesNotExceedBandHireDayCeiling(false);
        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed_1() throws IOException {

        /*
         * A : HIRE DETAIL > HIRE DAY
         * B : BRE BAND > HIRE DAY CEILING
         * A EQUALS TO B > PASSED
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHireDayCountDoesNotExceedBandHireDayCeiling(true);

        claim.getVehicleHire().setDays(123);
        claim.getBreBand().setHireDayCeiling(123);

        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed_2() throws IOException {

        /*
         * A : HIRE DETAIL > HIRE DAY
         * B : BRE BAND > HIRE DAY CEILING
         * A LESS TO B > PASSED
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHireDayCountDoesNotExceedBandHireDayCeiling(true);

        claim.getVehicleHire().setDays(122);
        claim.getBreBand().setHireDayCeiling(123);

        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testFailed() throws IOException {

        /*
         * A : HIRE DETAIL > HIRE DAY
         * B : BRE BAND > HIRE DAY CEILING
         * A MORE THAN B > PASSED
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHireDayCountDoesNotExceedBandHireDayCeiling(true);

        claim.getVehicleHire().setDays(124);
        claim.getBreBand().setHireDayCeiling(123);

        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Number of hire days billed by the CHO exceeds the CHO's hire days ceiling."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
