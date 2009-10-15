package scsbre.tests.rules;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HandlingAmountAndDeductionBothEqualZeroForNonDA;
import scsbre.model.ClaimStatus;
import scsbre.tests.rules.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule016_HandlingAmountAndDeductionBothEqualZeroForNonDA extends TestCase {

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

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(false);
        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

// TODO: HandlingAmountAndDeductionBothEqualZeroForNonDA
    public void testPassed() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);


        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

// TODO: HandlingAmountAndDeductionBothEqualZeroForNonDA
    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);


        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }
}
