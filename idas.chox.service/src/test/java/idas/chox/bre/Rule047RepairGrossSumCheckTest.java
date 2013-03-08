package idas.chox.bre;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.RepairGrossSumCheck;
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
public class Rule047RepairGrossSumCheckTest extends BaseTest {

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
        claim.getBreBand().setHasRepairGrossSumCheck(false);
        RepairGrossSumCheck rule = new RepairGrossSumCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testPassed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasRepairGrossSumCheck(true);
        claim.getInvoice().setRepairNet(new BigDecimal(2634.07));
        claim.getInvoice().setRepairVat(new BigDecimal(460.96));
        claim.getInvoice().setRepairGross(new BigDecimal(3095.03));

        RepairGrossSumCheck rule = new RepairGrossSumCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasRepairGrossSumCheck(true);
        claim.getInvoice().setRepairNet(new BigDecimal(2634.07));
        claim.getInvoice().setRepairVat(new BigDecimal(460.96));
        claim.getInvoice().setRepairGross(new BigDecimal(3090.03));

        RepairGrossSumCheck rule = new RepairGrossSumCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Repair Gross - The sum of the Repair Net and the Repair VAT is incorrect."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }
}
