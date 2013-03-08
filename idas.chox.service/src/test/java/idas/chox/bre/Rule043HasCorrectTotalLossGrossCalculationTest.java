package idas.chox.bre;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasCorrectTotalLossGrossCalculation;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rajareddydodda
 */
public class Rule043HasCorrectTotalLossGrossCalculationTest extends BaseTest {

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
    public void testSkipped_1() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectTotalLossGrossCalculation(false);
        HasCorrectTotalLossGrossCalculation rule = new HasCorrectTotalLossGrossCalculation();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectTotalLossGrossCalculation(true);

        claim.getInvoice().setTotalLossFeeGross(new BigDecimal(1.20));
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal(1.00));


        HasCorrectTotalLossGrossCalculation rule = new HasCorrectTotalLossGrossCalculation();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectTotalLossGrossCalculation(true);

        claim.getInvoice().setTotalLossFeeGross(new BigDecimal(0.00));
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal(1.00));


        HasCorrectTotalLossGrossCalculation rule = new HasCorrectTotalLossGrossCalculation();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());


        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Total Loss Fee Gross calculation is incorrect."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }
}
