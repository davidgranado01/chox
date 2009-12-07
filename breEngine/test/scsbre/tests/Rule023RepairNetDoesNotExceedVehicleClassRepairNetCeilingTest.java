package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.RepairNetDoesNotExceedVehicleClassRepairNetCeiling;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule023RepairNetDoesNotExceedVehicleClassRepairNetCeilingTest extends TestCase {

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
        claim.setBreBand(testClaim.getTestBreBand());
        claim.setClaimEngineeringReport(testClaim.getTestEngineeringReport());
        claim.setCustomerVehicleDamage(testClaim.getTestCustomerVehicleDamage());
        claim.setExtras(testClaim.getTestExtras());
        claim.setHireDetail(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setVClass(testClaim.getTestVehicleClass());


        // SET VEHICLE CLASS CEILING
        VehicleClassCeilingInfo vehicleClassCeiling = new VehicleClassCeilingInfo();
        vehicleClassCeiling.setRepairNetCeiling(new BigDecimal("200.00"));
        
        claim.getBreBand().setRepairNetCeiling(new BigDecimal("400.00"));
        claim.getBreBand().setVehicleClassCeiling(vehicleClassCeiling);
        
        claim.getInvoice().setRepairNet(new BigDecimal("400.00"));
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(false);
        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        
        claim.getInvoice().setRepairNet(new BigDecimal("199.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        
        claim.getInvoice().setRepairNet(new BigDecimal("200.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        ClaimInfo claim = getTestClaim();
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
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £200.50 exceeds the Repair Net ceiling of £200.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

}