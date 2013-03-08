package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.service.bre.rules.RepairNetDoesNotExceedVehicleClassRepairNetCeiling;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule023RepairNetDoesNotExceedVehicleClassRepairNetCeilingTest extends TestCase {

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


        // SET VEHICLE CLASS CEILING
        VehicleClassCeiling vehicleClassCeiling = new VehicleClassCeiling();
        vehicleClassCeiling.setRepairNetCeiling(new BigDecimal("200.00"));

        claim.getBreBand().setRepairNetCeiling(new BigDecimal("400.00"));
        claim.getBreBand().setVehicleClassCeiling(vehicleClassCeiling);

        claim.getInvoice().setRepairNet(new BigDecimal("400.00"));

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(false);
        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);

        claim.getInvoice().setRepairNet(new BigDecimal("199.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);

        claim.getInvoice().setRepairNet(new BigDecimal("200.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);

        claim.getInvoice().setRepairNet(new BigDecimal("200.50"));


        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);
        /*
        boolean success = (claim.getInvoice().getRepairNet()).compareTo(claim.getBreBand().getMaxRepairNetCeiling()) <= 0;
        System.out.println("REPAIR NET: "+claim.getInvoice().getRepairNet());
        System.out.println("REPAIR NET CELIING: "+claim.getBreBand().getRepairNetCeiling());
        System.out.println("REPAIR MAX NET CEILING: "+claim.getBreBand().getMaxRepairNetCeiling());
        System.out.println(success+"RESULT: "+rv.getResult());
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        System.out.println("RESULT: "+rv.getRelatedRule().getStatusAfterFailure());
         */
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £200.50 exceeds the Repair Net ceiling of £200.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
}
