package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.DeliveryCollectionChargeCheck;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

public class Rule109DeliveryCollectionChargeCheckTest extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private Claim getTestClaim() {
        return getTestClaim(null);
    }

    private Claim getTestClaim(ClaimType claimType) {

        Claim claim = new Claim();
        if (null != claimType) {
            claim.setClaimType(claimType);
        }

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

        claim.getBreBand().setDeliveryCollectionChargeCheck(true);
        return claim;
    }

    @Test
    public void testSkippedWithOffFlagShouldSuccess() {
        Claim claim = getTestClaim();
        claim.getBreBand().setDeliveryCollectionChargeCheck(false);
        RuleEvaluation rv = new DeliveryCollectionChargeCheck().applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_SKIPPED, rv.getResult());
        assertEquals("", rv.getRelatedRule().getNarrative());
        assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testRulePassedForClaimTypeGTA() {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);

        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
    }

    @Test
    public void testRulePassedForClaimTypeInsurerInvoice() {

        Claim claim = getTestClaim(ClaimType.INSURER_INVOICE);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
    }

    @Test
    public void testRulePassedForClaimTypeInsurerVsInsurer() {

        Claim claim = getTestClaim(ClaimType.INSURER_VS_INSURER);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
    }


    @Test
    public void testRulePassedForClaimTypeTPI() {

        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
    }

    @Test
    public void testRuleFailedForClaimTypeGTA() {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_FAILED, rv.getResult());
    }

    @Test
    public void testRuleFailedForClaimTypeInsurerInvoice() {

        Claim claim = getTestClaim(ClaimType.INSURER_INVOICE);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_FAILED, rv.getResult());
    }

    @Test
    public void testRuleFailedForClaimTypeInsurerVsInsurer() {

        Claim claim = getTestClaim(ClaimType.INSURER_VS_INSURER);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_FAILED, rv.getResult());
    }


    @Test
    public void testRuleFailedForClaimTypeTPI() {

        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(BigDecimal.TEN);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_FAILED, rv.getResult());
    }

    @Test
    public void testRuleSkippedForClaimTypeSubscriber() {

        Claim claim = getTestClaim(ClaimType.SUBSCRIBER);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_SKIPPED, rv.getResult());
    }

    @Test
    public void testRuleSkippedForClaimTypeFixedFee() {

        Claim claim = getTestClaim(ClaimType.FIXED_FEE);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_SKIPPED, rv.getResult());
    }

    @Test
    public void testRuleSkippedForClaimTypeCollaborationProtocol() {

        Claim claim = getTestClaim(ClaimType.COLLABORATION_PROTOCOL);
        DeliveryCollectionChargeCheck rule = new DeliveryCollectionChargeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_SKIPPED, rv.getResult());
    }

    @Test
    public void testVehicleUsableAndDeliveryCollectionFeeFeeZeroShouldPassRule() {

        Claim claim = getTestClaim();
        claim.getBreBand().setDeliveryCollectionChargeCheck(true);

        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(new BigDecimal("0.00"));
        RuleEvaluation rv = new DeliveryCollectionChargeCheck().applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
        assertEquals("", rv.getRelatedRule().getNarrative());
        assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testVehicleUsableAndDeliveryCollectionFeeNegativeShouldPassRule() {

        Claim claim = getTestClaim();
        claim.getBreBand().setDeliveryCollectionChargeCheck(true);

        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(new BigDecimal("-1.00"));
        RuleEvaluation rv = new DeliveryCollectionChargeCheck().applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
        assertEquals("", rv.getRelatedRule().getNarrative());
        assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testVehicleNotUsableAndDeliveryCollectionFeePositiveShouldPassRule() {

        Claim claim = getTestClaim();
        claim.getBreBand().setDeliveryCollectionChargeCheck(true);

        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setDeliveryCollectionFee(new BigDecimal("1.00"));
        RuleEvaluation rv = new DeliveryCollectionChargeCheck().applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
        assertEquals("", rv.getRelatedRule().getNarrative());
        assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testVehicleUsableNullAndDeliveryCollectionFeePositiveShouldPassRule() {

        Claim claim = getTestClaim();
        claim.getBreBand().setDeliveryCollectionChargeCheck(true);

        claim.getCustomer().setIsUsable(null);
        claim.getInvoice().setDeliveryCollectionFee(new BigDecimal("1.00"));
        RuleEvaluation rv = new DeliveryCollectionChargeCheck().applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_PASSED, rv.getResult());
        assertEquals("", rv.getRelatedRule().getNarrative());
        assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()));
        assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testVehicleUsableAndDeliveryCollectionFeePositiveShouldFailRule() {

        Claim claim = getTestClaim();
        claim.getBreBand().setDeliveryCollectionChargeCheck(true);

        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setDeliveryCollectionFee(new BigDecimal("1.00"));
        RuleEvaluation rv = new DeliveryCollectionChargeCheck().applyToClaim(claim);

        assertEquals(RuleEvaluationResult.RULE_FAILED, rv.getResult());
        assertEquals(DeliveryCollectionChargeCheck.FAILURE_MESSAGE, rv.getRelatedRule().getNarrative());
        assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
