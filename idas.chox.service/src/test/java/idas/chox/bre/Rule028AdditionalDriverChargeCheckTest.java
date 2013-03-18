package idas.chox.bre;

import java.io.IOException;
import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.AdditionalDriverChargeCheck;
import java.math.BigDecimal;

public class Rule028AdditionalDriverChargeCheckTest extends TestCase {

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
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setAdditionalDriverChargeCheck(false);
        RuleEvaluation rv = new AdditionalDriverChargeCheck().applyToClaim(claim);
        
        claim.getInvoice().setAdditionalDriverFee(new BigDecimal(100));

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setAdditionalDriverChargeCheck(true);
        
        claim.getInvoice().setAdditionalDriverFee(new BigDecimal(0));

        RuleEvaluation rv = new AdditionalDriverChargeCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testSkippedForSubscriber() throws IOException {

        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.getBreBand().setAdditionalDriverChargeCheck(true);
        
        claim.getInvoice().setAdditionalDriverFee(new BigDecimal(100));

        RuleEvaluation rv = new AdditionalDriverChargeCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testSkippedForFixedFee() throws IOException {

        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.getBreBand().setAdditionalDriverChargeCheck(true);
        
        claim.getInvoice().setAdditionalDriverFee(new BigDecimal(100));

        RuleEvaluation rv = new AdditionalDriverChargeCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testFailled() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setAdditionalDriverChargeCheck(true);

        claim.getInvoice().setAdditionalDriverFee(new BigDecimal(100));

        RuleEvaluation rv = new AdditionalDriverChargeCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging an additional driver fee for the hire, please review need."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
