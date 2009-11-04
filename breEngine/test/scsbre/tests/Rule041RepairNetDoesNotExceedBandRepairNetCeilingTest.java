package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.RepairNetDoesNotExceedRepairNetCeiling;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule041RepairNetDoesNotExceedBandRepairNetCeilingTest extends TestCase {

    TestClaim testClaim = new TestClaim();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private ClaimInfo getTestClaim(){

        ClaimInfo claim = new ClaimInfo();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setCHOrganisation(testClaim.getTestChorganisation());
        claim.setChoBand(testClaim.getTestChoBand());
        claim.setClaimEngineeringReport(testClaim.getTestEngineeringReport());
        claim.setCustomerVehicleDamage(testClaim.getTestCustomerVehicleDamage());
        claim.setExtras(testClaim.getTestExtras());
        claim.setHireDetail(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setVClass(testClaim.getTestVehicleClass());


        // SET VEHICLE CLASS CEILING
        VehicleClassCeilingInfo vehicleClassCeiling = new VehicleClassCeilingInfo();
        vehicleClassCeiling.setRepairNetCeiling(new BigDecimal("10000.00"));

        // SET CHOBAND
        claim.getChoBand().setRepairNetCeiling(new BigDecimal("100.00"));
        claim.getChoBand().setVehicleClassCeiling(vehicleClassCeiling);

        claim.getInvoice().setRepairNet(new BigDecimal("400.00"));

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedBandRepairNetCeiling(false);
        RuleEvaluation rv = new RepairNetDoesNotExceedRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedBandRepairNetCeiling(true);

        claim.getInvoice().setRepairNet(new BigDecimal("99.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedBandRepairNetCeiling(true);

        claim.getInvoice().setRepairNet(new BigDecimal("100.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedBandRepairNetCeiling(true);

        claim.getInvoice().setRepairNet(new BigDecimal("100.50"));

        RuleEvaluation rv = new RepairNetDoesNotExceedRepairNetCeiling().applyToClaim(claim);
        /*
        boolean success = (claim.getInvoice().getRepairNet()).compareTo(claim.getChoBand().getMaxRepairNetCeiling()) <= 0;
        System.out.println("REPAIR NET: "+claim.getInvoice().getRepairNet());
        System.out.println("REPAIR NET CELIING: "+claim.getChoBand().getRepairNetCeiling());
        System.out.println("REPAIR MAX NET CEILING: "+claim.getChoBand().getMaxRepairNetCeiling());
        System.out.println(success+"RESULT: "+rv.getResult());
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        System.out.println("RESULT: "+rv.getRelatedRule().getStatusAfterFailure());
        */
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £100.50 exceeds the Repair Net ceiling of £100.00."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

}