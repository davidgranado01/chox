package idas.chox.bre;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.CopleyOfferMadeCheck;
import idas.chox.test.BaseTest;

public class Rule096CopleyOfferMadeCheckTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    private Claim getTestClaim() {
        Claim claim = new Claim();

        claim.setClaimType(ClaimType.INSURER_UPLOAD);
        claim.setInsurer(testClaim.getTestInsurer());
        claim.getInsurer().setCopleyQuestion(true);
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setCopleyOfferMadeCheck(true);
        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        return claim;
    }

    
    @Test
    public void testSkipped_ruleNotActivated() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setCopleyOfferMadeCheck(false);
        CopleyOfferMadeCheck rule = new CopleyOfferMadeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testSkipped_copleyNotActivated() throws IOException {
        Claim claim = getTestClaim();
        claim.getInsurer().setCopleyQuestion(false);
        CopleyOfferMadeCheck rule = new CopleyOfferMadeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

        
    @Test
    public void testPassed() throws IOException {
        Claim claim = getTestClaim();
        claim.setCopleyOfferMade(Boolean.FALSE);
        CopleyOfferMadeCheck rule = new CopleyOfferMadeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.setCopleyOfferMade(Boolean.TRUE);
        CopleyOfferMadeCheck rule = new CopleyOfferMadeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is claiming when a Copley Offer has been made on this claim."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
 }
