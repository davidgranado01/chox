package idas.chox.bre;

import org.springframework.transaction.annotation.Transactional;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.SubscriberCheckRejectedClaims;
import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author John
 */
public class Rule071SubscriberCheckRejectedClaimsTest extends BaseTest {
    MockObjects testClaim = new MockObjects();

    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    
    @Transactional
    private Claim getTestClaim() {
        Claim claim = new Claim();

        claim.setBreBand(testClaim.getTestBreBand());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.getVehicleHire().setVehicleClass(null);
        claim.setPreviousStatus("");
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setClaimNumber("0123456789");
        claimService.save(claim);
//        claimService.flush();

        return claim;
    }

    
    @Test
    public void testSkipped_GTA() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setSubscriberCheckRejectedClaims(true);
        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testSkipped_Insurer() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.INSURER_VS_INSURER);
        claim.getBreBand().setSubscriberCheckRejectedClaims(true);
        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testSkipped_InsurerUpload() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        claim.getBreBand().setSubscriberCheckRejectedClaims(true);
        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testSkipped_ruleoff() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setSubscriberCheckRejectedClaims(false);
        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testSkipped_notRejectedAndAgreed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setSubscriberCheckRejectedClaims(true);

        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        rule.setClaimService(claimService);
        
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
        

    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claim.setPreviousStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claim.getBreBand().setSubscriberCheckRejectedClaims(true);
        // set-up audit trail
        //    Rejected after 2 days 3 hours, rejection accepted after 4 days
        auditTrailService.logAuditLogForce(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, "", claim);
        auditTrailService.logAuditLog(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim, 1000*60*60*24*2 + 1000*60*60*3);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim, 1000*60*60*24*4);

        // set-up vehicle hire of 2 days
        claim.getVehicleHire().setDays(2);
        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        rule.setClaimService(claimService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setSubscriberCheckRejectedClaims(true);

        // set-up audit trail
        //    Rejected after 2 days 3 hours, rejection accepted after 4 days
        auditTrailService.logAuditLogForce(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, "", claim);
        auditTrailService.logAuditLog(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim, 1000*60*60*24*2 + 1000*60*60*3);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim, 1000*60*60*24*4);

        // set-up vehicle hire of 5 days (note that could claim for 3 or 4 days, depending upon time ran < or > 3pm
        claim.getVehicleHire().setDays(5);

        SubscriberCheckRejectedClaims rule = new SubscriberCheckRejectedClaims();
        rule.setClaimService(claimService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The cumulative number of days prior to the claim rejection was"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(ClaimType.isTPI(claim.getClaimType())).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
