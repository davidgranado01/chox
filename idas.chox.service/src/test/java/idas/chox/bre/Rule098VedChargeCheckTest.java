package idas.chox.bre;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import static org.junit.Assert.*;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.VedChargeCheck;
import idas.chox.test.BaseTest;

public class Rule098VedChargeCheckTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    private Claim getTestClaim() {
        Claim claim = new Claim();

        claim.setClaimType(ClaimType.INSURER_UPLOAD);
        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setVedChargeCheck(true);
        claim.getBreBand().setVedChargeCeiling(new BigDecimal("20.00"));
        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getInvoice().setVedFee(BigDecimal.TEN);
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        return claim;
    }

    
    @Test
    public void testSkipped_ruleNotActivated() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setVedChargeCheck(false);
        VedChargeCheck rule = new VedChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testPassed() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setVedFee(new BigDecimal("30.00"));
        claim.getInvoice().setVedQty(2);
        VedChargeCheck rule = new VedChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_zeroVedQuantity() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setVedFee(new BigDecimal("20.00"));
        claim.getInvoice().setVedQty(0);
        VedChargeCheck rule = new VedChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_nullVedQuantity() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setVedFee(new BigDecimal("20.00"));
        claim.getInvoice().setVedQty(null);
        VedChargeCheck rule = new VedChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }


    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setVedFee(new BigDecimal("41.00"));
        claim.getInvoice().setVedQty(2);
        VedChargeCheck rule = new VedChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The daily VED Charge of £20.50 is greater than the VED Charge Ceiling of £20.00."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_noQuantity() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setVedFee(new BigDecimal("21.00"));
        claim.getInvoice().setVedQty(0);
        VedChargeCheck rule = new VedChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The daily VED Charge of £21.00 is greater than the VED Charge Ceiling of £20.00."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

}
