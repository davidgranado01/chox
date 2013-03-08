package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.HasCorrectRepairGrossCalculation;
import idas.chox.core.util.CalcHelper;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule011HasCorrectRepairGrossCalculationTest extends TestCase {

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
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectRepairGrossCalculation(false);
        RuleEvaluation rv = new HasCorrectRepairGrossCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectRepairGrossCalculation(true);

        BigDecimal invNet = new BigDecimal("100.00");
        BigDecimal invVat = invNet.multiply(CalcHelper.VAT_RATE);
        BigDecimal invGross = invNet.add(invVat);

        // SET INVOICE
        claim.getInvoice().setRepairNet(invNet);
        claim.getInvoice().setRepairVat(invVat);
        claim.getInvoice().setRepairGross(invGross);

        RuleEvaluation rv = new HasCorrectRepairGrossCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }
    
    @Test
    public void testFailled() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectRepairGrossCalculation(true);

        // SET INVOICE
        claim.getInvoice().setRepairNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairVat(new BigDecimal("16.00"));
        claim.getInvoice().setRepairGross(new BigDecimal("135.00"));

        RuleEvaluation rv = new HasCorrectRepairGrossCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Repair Gross calculation is incorrect."));

    }
}
