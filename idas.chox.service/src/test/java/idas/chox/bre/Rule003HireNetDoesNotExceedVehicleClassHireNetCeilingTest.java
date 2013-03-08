package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.service.bre.rules.HireNetDoesNotExceedVehicleClassHireNetCeiling;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule003HireNetDoesNotExceedVehicleClassHireNetCeilingTest extends TestCase {

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

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(400.00));

        // SET VEHICLE CLASS CEiLING
        VehicleClassCeiling vehicleClassCeiling = new VehicleClassCeiling();
        vehicleClassCeiling.setHireNetCeiling(new BigDecimal("300.00"));

        claim.getBreBand().setHireNetCeiling(new BigDecimal("400.00"));
        claim.getBreBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getBreBand().setVehicleClassCeiling(vehicleClassCeiling);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(false);

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);

        claim.getInvoice().setHireNet(new BigDecimal("299.00"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getInvoice().setHireNet(new BigDecimal("300.00"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getInvoice().setHireNet(new BigDecimal("340.00"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Hire Net billed £340.00 exceeds the Hire Net ceiling of £300.00 for vehicle class SP1."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }
}
