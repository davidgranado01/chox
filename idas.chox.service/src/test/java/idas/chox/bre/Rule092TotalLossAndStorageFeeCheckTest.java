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
import idas.chox.service.bre.rules.TotalLossAndStorageFeeCheck;

/**
 *
 * @author John
 */
public class Rule092TotalLossAndStorageFeeCheckTest extends BaseTest {

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
        claim.getBreBand().setTotalLossAndStorageFeeCheck(true);
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
        claim.getBreBand().setTotalLossAndStorageFeeCheck(false);
        RuleEvaluation rv = new TotalLossAndStorageFeeCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    
    /*
     * Rule passsed: switched-on and no storage recovery fee when total loss fee present
     */
    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal("40.00"));
        claim.getInvoice().setStorageRecoveryNet(BigDecimal.ZERO);
        
        RuleEvaluation rv = new TotalLossAndStorageFeeCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }


    /*
     * Rule passsed: switched-on and storage recovery fee present but no total loss net
     */
    @Test
    public void testPassed_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setTotalLossFeeNet(BigDecimal.ZERO);
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("40.00"));
        
        RuleEvaluation rv = new TotalLossAndStorageFeeCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }


    /*
     * Rule failed: switched-on and both total loss fee net and storage and recovery fee charged
     */
    @Test
    public void testFailed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal("20.00"));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("40.00"));
        
        RuleEvaluation rv = new TotalLossAndStorageFeeCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }


}
