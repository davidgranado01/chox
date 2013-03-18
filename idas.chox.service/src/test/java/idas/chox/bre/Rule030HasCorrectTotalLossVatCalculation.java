package idas.chox.bre;

import idas.chox.core.util.CalcHelper;
import idas.chox.service.bre.rules.HasCorrectTotalLossVatCalculation;
import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author John
 */
public class Rule030HasCorrectTotalLossVatCalculation extends BaseTest {

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
        claim.setClaimType(ClaimType.GTA);

        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectTotalLossVatCalculation(false);
        HasCorrectTotalLossVatCalculation rule = new HasCorrectTotalLossVatCalculation();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectTotalLossVatCalculation(true);

        claim.getInvoice().setTotalLossFeeVat((new BigDecimal(100.00)).multiply(CalcHelper.VAT_RATE).setScale(2, BigDecimal.ROUND_HALF_UP));
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal(100.00));

        HasCorrectTotalLossVatCalculation rule = new HasCorrectTotalLossVatCalculation();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectTotalLossVatCalculation(true);

        claim.getInvoice().setTotalLossFeeVat(new BigDecimal(50.00));
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal(100.00));


        HasCorrectTotalLossVatCalculation rule = new HasCorrectTotalLossVatCalculation();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());

        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Total Loss Fee VAT calculation is incorrect."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }
}
