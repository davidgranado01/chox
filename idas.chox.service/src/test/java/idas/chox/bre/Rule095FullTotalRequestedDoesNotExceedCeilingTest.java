package idas.chox.bre;

import java.io.IOException;
import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.FullTotalRequestedDoesNotExceedCeiling;

public class Rule095FullTotalRequestedDoesNotExceedCeilingTest extends TestCase {

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
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        // SET INVOICE
        claim.getInvoice().setFullTotalToPay(new BigDecimal(400.00));


        claim.getBreBand().setFullTotalRequestedCeilingTolerance(new BigDecimal("350.00"));

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setFullTotalRequestedCeilingCheck(false);

        RuleEvaluation rv = new FullTotalRequestedDoesNotExceedCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setFullTotalRequestedCeilingCheck(true);
        claim.getInvoice().setFullTotalToPay(new BigDecimal(400.00));
        claim.getBreBand().setFullTotalRequestedCeilingTolerance(new BigDecimal("401.00"));

        RuleEvaluation rv = new FullTotalRequestedDoesNotExceedCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setFullTotalRequestedCeilingCheck(true);
        claim.getInvoice().setFullTotalToPay(new BigDecimal(400.00));
        claim.getBreBand().setFullTotalRequestedCeilingTolerance(new BigDecimal("400.00"));

        RuleEvaluation rv = new FullTotalRequestedDoesNotExceedCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setFullTotalRequestedCeilingCheck(true);
        claim.getInvoice().setFullTotalToPay(new BigDecimal(401.00));
        claim.getBreBand().setFullTotalRequestedCeilingTolerance(new BigDecimal("400.00"));

        RuleEvaluation rv = new FullTotalRequestedDoesNotExceedCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Full Total Requested billed £401.00 exceeds the Full Total Requested ceiling of £400.00"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
}

