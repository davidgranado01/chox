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
        claim.setChoBand(testClaim.getTestChoBand());
        claim.setClaimEngineeringReport(testClaim.getTestEngineeringReport());
        claim.setCustomerVehicleDamage(testClaim.getTestCustomerVehicleDamage());
        claim.setExtras(testClaim.getTestExtras());
        claim.setHireDetail(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setVClass(testClaim.getTestVehicleClass());


        // SET VEHICLE CLASS CELLING
        VehicleClassCellingInfo vehicleClassCelling = new VehicleClassCellingInfo();
        vehicleClassCelling.setRepairNetCelling(new BigDecimal("300.00"));
        
        // SET CHOBAND
        claim.getChoBand().setRepairNetCeiling(new BigDecimal("400.00"));
        claim.getChoBand().setVehicleClassCelling(vehicleClassCelling);
        
        claim.getInvoice().setRepairNet(new BigDecimal("400.00"));
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(false);
        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingDisabled_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        claim.getChoBand().setVehicleClassCellingEnable(false);
        claim.getInvoice().setRepairNet(new BigDecimal("399.99"));

        // CEILLING > 300
        // BRE BAND > CEILING > 400

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingDisabled_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        claim.getChoBand().setVehicleClassCellingEnable(false);
        claim.getInvoice().setRepairNet(new BigDecimal("400.00"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_VehicleClassCellingDisabled_MoreThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        claim.getChoBand().setVehicleClassCellingEnable(false);
        claim.getInvoice().setRepairNet(new BigDecimal("400.50"));

        // CEILLING > 300
        // BRE BAND > CEILING > 400

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £400.50 exceeds the Repair Net ceiling of £400.00"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingEnabled_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();

        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        claim.getChoBand().setVehicleClassCellingEnable(true);
        
        claim.getInvoice().setRepairNet(new BigDecimal("299.00"));

        // CEILLING > 300
        // BRE BAND > CEILING > 400

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingEnabled_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();

        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        claim.getChoBand().setVehicleClassCellingEnable(true);

        claim.getInvoice().setRepairNet(new BigDecimal("300.00"));

        // CEILLING > 300
        // BRE BAND > CEILING > 400

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_VehicleClassCellingEnabled_MoreThan() throws IOException {

        ClaimInfo claim = getTestClaim();

        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(true);
        claim.getChoBand().setVehicleClassCellingEnable(true);

        claim.getInvoice().setRepairNet(new BigDecimal("350.00"));

        // CEILLING > 300
        // BRE BAND > CEILING > 400

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);
        /*
        boolean success = (claim.getInvoice().getRepairNet()).compareTo(claim.getChoBand().getMaxRepairNetCelling()) <= 0;
        System.out.println("REPAIR NET: "+claim.getInvoice().getRepairNet());
        System.out.println("REPAIR NET CELLING: "+claim.getChoBand().getRepairNetCeiling());
        System.out.println("REPAIR MAX NET CELLING: "+claim.getChoBand().getMaxRepairNetCelling());
        System.out.println(success+"RESULT: "+rv.getResult());
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        System.out.println("RESULT: "+rv.getRelatedRule().getStatusAfterFailure());
        */
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £350.00 exceeds the Repair Net ceiling of £300.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

}