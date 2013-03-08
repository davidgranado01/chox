package idas.chox.bre;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.HireTerminatedAfterRepairCompletionCheck;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author John
 */
public class Rule056HireTerminatedAfterRepairCompletionCheckTest extends BaseTest {

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

        return claim;
    }

    
    @Test
    public void testSkipped_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setHireTerminatedAfterRepairCompletionCheck(false);

        HireTerminatedAfterRepairCompletionCheck rule = new HireTerminatedAfterRepairCompletionCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    
    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setHireTerminatedAfterRepairCompletionCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.Parse("09/10/2011"));
        claim.getVehicleHire().setHireEnd(DateHelper.Parse("09/10/2011"));

        HireTerminatedAfterRepairCompletionCheck rule = new HireTerminatedAfterRepairCompletionCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setHireTerminatedAfterRepairCompletionCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.Parse("08/10/2011"));
        claim.getVehicleHire().setHireEnd(DateHelper.Parse("09/10/2011"));
        HireTerminatedAfterRepairCompletionCheck rule = new HireTerminatedAfterRepairCompletionCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_3() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setHireTerminatedAfterRepairCompletionCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.Parse("08/10/2011"));
        claim.getVehicleHire().setHireEnd(DateHelper.Parse("05/10/2011"));

        HireTerminatedAfterRepairCompletionCheck rule = new HireTerminatedAfterRepairCompletionCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    
    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setHireTerminatedAfterRepairCompletionCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.Parse("08/10/2011"));
        claim.getVehicleHire().setHireEnd(DateHelper.Parse("10/10/2011"));


        HireTerminatedAfterRepairCompletionCheck rule = new HireTerminatedAfterRepairCompletionCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());

        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire was terminated 2 days after the repairs were completed (allowed 1 day), please review."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
