package idas.chox.bre;

import java.math.BigDecimal;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.ClaimType;
import org.springframework.transaction.annotation.Transactional;
import idas.chox.service.bre.rules.SubscriberAdminFeeCheck;
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
public class Rule073SubscriberAdminFeeCheckTest extends BaseTest {
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
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setClaimNumber("0123456789");
        claimService.updateClaim(claim);
        claimService.flush();

        return claim;
    }

     @Test
    public void testSkipped_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setSubscriberAdminFeeCheck(false);
        SubscriberAdminFeeCheck rule = new SubscriberAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testSkipped_2() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setSubscriberAdminFeeCheck(true);
        SubscriberAdminFeeCheck rule = new SubscriberAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }
    
    
    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setSubscriberAdminFeeCheck(true);

        Invoice invoice = new Invoice();
        invoice.setAdminFee(BigDecimal.ZERO);
        claim.setInvoice(invoice);
        
        // set-up audit trail
        //    Rejected after 2 days 3 hours, rejection accepted after 4 days
        auditTrailService.logAuditLogForce(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, null, claim);
        auditTrailService.logAuditLog(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim, 1000*60*60*24*2 + 1000*60*60*3);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim, 1000*60*60*24*4);


        SubscriberAdminFeeCheck rule = new SubscriberAdminFeeCheck();
        rule.setClaimService(claimService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setSubscriberAdminFeeCheck(true);

        Invoice invoice = new Invoice();
        invoice.setAdminFee(new BigDecimal(10.0));
        claim.setInvoice(invoice);
        
        // set-up audit trail
        //    Rejected after 2 days 3 hours, rejection accepted after 4 days
        auditTrailService.logAuditLogForce(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, null, claim);
        auditTrailService.logAuditLog(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim, 1000*60*60*24*2 + 1000*60*60*3);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim, 1000*60*60*24*4);


        SubscriberAdminFeeCheck rule = new SubscriberAdminFeeCheck();
        rule.setClaimService(claimService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging an Admin Fee however the Subscriber rejection was accepted and therefore this charge should not be made."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    
    

}
