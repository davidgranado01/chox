package idas.chox.bre;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.service.bre.rules.MaximumLabourRatePrestige;

/**
 *
 * @author John
 */
public class Rule088MaximumLabourRatePrestigeTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    private Claim getTestClaim() {

        Claim claim = new Claim();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setBreBand(testClaim.getTestBreBand());
        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setInvoice(testClaim.getTestExtras());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setMaximumLabourRatePrestigeCheck(true);
        claim.getBreBand().setMaxAllowedLabourPrestigeRate(BigDecimal.ZERO);
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setLabourRate(BigDecimal.ZERO);
        claim.setHireMonitoringDetail(hmd);
        claim.getCustomer().getVehicleClass().setName("SP5");
      
        return claim;
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    
    /*
     * Skip when rule switched off
     */
    @Test
    public void testSkipped_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setMaximumLabourRateCheck(false);
        RuleEvaluation rv = new MaximumLabourRatePrestige().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    /*
     * Rule skipped when no hire monitoring
     */
    @Test
    public void testSkipped_2() throws IOException {
        Claim claim = getTestClaim();
        claim.setHireMonitoringDetail(null);
        RuleEvaluation rv = new MaximumLabourRatePrestige().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    /*
     * Rule skipped when no hire monitoring labour rate
     */
    @Test
    public void testSkipped_3() throws IOException {
        Claim claim = getTestClaim();
        claim.getHireMonitoringDetail().setLabourRate(null);
        RuleEvaluation rv = new MaximumLabourRatePrestige().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }


    /*
     * Labour rate is less than the maximum
     */
    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getHireMonitoringDetail().setLabourRate(BigDecimal.TEN);
        claim.getBreBand().setMaxAllowedLabourPrestigeRate(BigDecimal.TEN.add(new BigDecimal("0.01")));
        
        RuleEvaluation rv = new MaximumLabourRatePrestige().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    /*
     * Labour rate is equal to the maximum
     */
    @Test
    public void testPassed_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getHireMonitoringDetail().setLabourRate(BigDecimal.TEN);
        claim.getBreBand().setMaxAllowedLabourPrestigeRate(claim.getHireMonitoringDetail().getLabourRate());
        
        RuleEvaluation rv = new MaximumLabourRatePrestige().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    /*
     * Labour rate is more than the maximum
     */
    @Test
    public void testFailed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getHireMonitoringDetail().setLabourRate(BigDecimal.TEN);
        claim.getBreBand().setMaxAllowedLabourPrestigeRate(BigDecimal.TEN.subtract(new BigDecimal("0.01")));
        
        RuleEvaluation rv = new MaximumLabourRatePrestige().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

}
