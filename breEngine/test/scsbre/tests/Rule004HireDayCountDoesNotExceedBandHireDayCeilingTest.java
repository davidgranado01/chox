package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HireDayCountDoesNotExceedBandHireDayCeiling;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule004HireDayCountDoesNotExceedBandHireDayCeilingTest extends TestCase {

    TestClaim testClaim = new TestClaim();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private ClaimInfo getTestClaim(){

        ClaimInfo claim = new ClaimInfo();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setCHOrganisation(testClaim.getTestChorganisation());
        claim.setChoBand(testClaim.getTestChoBand());
        claim.setClaimEngineeringReport(testClaim.getTestEngineeringReport());
        claim.setCustomerVehicleDamage(testClaim.getTestCustomerVehicleDamage());
        claim.setExtras(testClaim.getTestExtras());
        claim.setHireDetail(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setVClass(testClaim.getTestVehicleClass());

         claim.getHireDetail().setDays(123);
         claim.getChoBand().setHireDayCeiling(123);
        
        return claim;
    }

    @Test
    public void testSkipped() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireDayCountDoesNotExceedBandHireDayCeiling(false);
        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed_1() throws IOException {

        /*
         * A : HIRE DETAIL > HIRE DAY
         * B : BRE BAND > HIRE DAY CELLING
         * A EQUALS TO B > PASSED
         */
        
        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireDayCountDoesNotExceedBandHireDayCeiling(true);
        
         claim.getHireDetail().setDays(123);
         claim.getChoBand().setHireDayCeiling(123);

        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);

         
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed_2() throws IOException {

        /*
         * A : HIRE DETAIL > HIRE DAY
         * B : BRE BAND > HIRE DAY CELLING
         * A LESS TO B > PASSED
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireDayCountDoesNotExceedBandHireDayCeiling(true);

         claim.getHireDetail().setDays(122);
         claim.getChoBand().setHireDayCeiling(123);

        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testFailed() throws IOException {

        /*
         * A : HIRE DETAIL > HIRE DAY
         * B : BRE BAND > HIRE DAY CELLING
         * A MORE THAN B > PASSED
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireDayCountDoesNotExceedBandHireDayCeiling(true);

         claim.getHireDetail().setDays(124);
         claim.getChoBand().setHireDayCeiling(123);

        RuleEvaluation rv = new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Number of hire days billed by the CHO exceeds the CHO's hire days ceiling."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
