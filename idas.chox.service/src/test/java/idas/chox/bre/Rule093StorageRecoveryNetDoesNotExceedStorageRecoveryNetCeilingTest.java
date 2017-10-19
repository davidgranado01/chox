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
import idas.chox.service.bre.rules.StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeiling;

public class Rule093StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeilingTest extends TestCase {

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

        // Set Storage Recovery Net
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal(100.00));

        // Set Storage Recovery Net Ceiling
        claim.getBreBand().setStorageRecoveryNetCeiling(new BigDecimal("100.00"));
        claim.getBreBand().setStorageRecoveryNetCeilingCheck(true);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setStorageRecoveryNetCeilingCheck(false);

        RuleEvaluation rv = new StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();

        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("99.00"));

        RuleEvaluation rv = new StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();

        RuleEvaluation rv = new StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("101.00"));


        RuleEvaluation rv = new StorageRecoveryNetDoesNotExceedStorageRecoveryNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Storage and Recovery billed £101.00 exceeds the Storage Recovery Net ceiling of £100.00."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
}

