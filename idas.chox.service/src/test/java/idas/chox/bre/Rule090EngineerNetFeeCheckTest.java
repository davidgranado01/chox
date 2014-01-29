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
import idas.chox.service.bre.rules.EngineerNetFeeCheck;

/**
 *
 * @author John
 */
public class Rule090EngineerNetFeeCheckTest extends BaseTest {

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
        claim.getBreBand().setEngineerNetFeeCheck(true);
        claim.getBreBand().setMaxAllowedEngineerNetFee(new BigDecimal("50.00"));
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
        claim.getBreBand().setEngineerNetFeeCheck(false);
        RuleEvaluation rv = new EngineerNetFeeCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    
    /*
     * Rule passsed: switched-on and engineer net fee less than max allowed
     */
    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("48.00"));
        
        RuleEvaluation rv = new EngineerNetFeeCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }


    /*
     * Rule passsed: switched-on and engineer net fee equal to max allowed
     */
    @Test
    public void testPassed_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("50.00"));
        
        RuleEvaluation rv = new EngineerNetFeeCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }


    /*
     * Rule failed: switched-on and engineer net fee more than max allowed
     */
    @Test
    public void testFailed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("51.00"));
        
        RuleEvaluation rv = new EngineerNetFeeCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }


}
