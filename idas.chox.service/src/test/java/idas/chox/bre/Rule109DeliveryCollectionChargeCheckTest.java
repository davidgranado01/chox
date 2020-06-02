package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
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
