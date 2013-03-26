package idas.chox.bre;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.DateRepairsCommencedFollowingHireStartCheck;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author rajareddydodda
 */
public class Rule064DateRepairsCommencedFollowingHireStartCheckTest extends BaseTest {
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
        claim.getBreBand().setDateRepairCommencedChkForNonMobileVehicle(false);
        DateRepairsCommencedFollowingHireStartCheck rule = new DateRepairsCommencedFollowingHireStartCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testSkipped_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setDateRepairCommencedChkForNonMobileVehicle(true);
        claim.getCustomer().setIsUsable(true);

        DateRepairsCommencedFollowingHireStartCheck rule = new DateRepairsCommencedFollowingHireStartCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testSkipped_3() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setDateRepairCommencedChkForNonMobileVehicle(true);
        claim.getCustomer().setIsUsable(true);
        claim.setHireMonitoringDetail(null);

        DateRepairsCommencedFollowingHireStartCheck rule = new DateRepairsCommencedFollowingHireStartCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testSkipped_4() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setDateRepairCommencedChkForNonMobileVehicle(true);
        claim.getCustomer().setIsUsable(true);
        claim.setVehicleHire(null);

        DateRepairsCommencedFollowingHireStartCheck rule = new DateRepairsCommencedFollowingHireStartCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testPassed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setDateRepairCommencedChkForNonMobileVehicle(true);
        claim.getCustomer().setIsUsable(false);
        claim.getHireMonitoringDetail().setRepairCommencedDate(DateHelper.parse("27/02/2011"));
        claim.getVehicleHire().setHireStart(DateHelper.parse("22/02/2011"));

        DateRepairsCommencedFollowingHireStartCheck rule = new DateRepairsCommencedFollowingHireStartCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setDateRepairCommencedChkForNonMobileVehicle(true);
        claim.getCustomer().setIsUsable(false);
        claim.getHireMonitoringDetail().setRepairCommencedDate(DateHelper.parse("28/02/2011"));
        claim.getVehicleHire().setHireStart(DateHelper.parse("22/02/2011"));

        DateRepairsCommencedFollowingHireStartCheck rule = new DateRepairsCommencedFollowingHireStartCheck();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());

        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire commenced 6 days prior to the date repairs commenced, the allowable number of days is 5 day(s) for un-driveable vehicles."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
