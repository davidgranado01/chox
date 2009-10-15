package scsbre.tests.rules;

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
import scsbre.tests.rules.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule023_RepairNetDoesNotExceedVehicleClassRepairNetCeiling extends TestCase {

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
        vehicleClassCelling.setRepairNetCelling(new BigDecimal("500.00"));
        
        // SET CHOBAND
        claim.getChoBand().setMaxRepairValue(new BigDecimal("400.00"));
        claim.getChoBand().setVehicleClassCelling(vehicleClassCelling);
        
        claim.getInvoice().setRepairNet(new BigDecimal("400.00"));
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairNetDoesNotExceedVehicleClassRepairNetCeiling(false);
        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingEnable() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setVehicleClassCellingEnable(true);
        claim.getInvoice().setRepairNet(new BigDecimal("499.99"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        /*
        boolean success = (claim.getInvoice().getHireNet()).compareTo(claim.getChoBand().getMaxHireNetCeiling()) <= 0;
        System.out.println("HIRE NET: "+claim.getInvoice().getHireNet());
        System.out.println("HIRE NET CELLING: "+claim.getChoBand().getMaxHireNetCeiling());
        System.out.println("RESULT: "+success);
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        */

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_VehicleClassCellingEnable() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setVehicleClassCellingEnable(true);
        claim.getInvoice().setRepairNet(new BigDecimal("500.01"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        /*
        boolean success = (claim.getInvoice().getHireNet()).compareTo(claim.getChoBand().getMaxHireNetCeiling()) <= 0;
        System.out.println("HIRE NET: "+claim.getInvoice().getHireNet());
        System.out.println("HIRE NET CELLING: "+claim.getChoBand().getMaxHireNetCeiling());
        System.out.println("RESULT: "+success);
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        */

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £500.01 exceeds the Repair Net ceiling of £500.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingDisable() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setVehicleClassCellingEnable(false);
        claim.getInvoice().setRepairNet(new BigDecimal("400"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        /*
        boolean success = (claim.getInvoice().getHireNet()).compareTo(claim.getChoBand().getMaxHireNetCeiling()) <= 0;
        System.out.println("HIRE NET: "+claim.getInvoice().getHireNet());
        System.out.println("HIRE NET CELLING: "+claim.getChoBand().getMaxHireNetCeiling());
        System.out.println("RESULT: "+success);
        */

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_VehicleClassCellingDisable() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setVehicleClassCellingEnable(false);
        claim.getInvoice().setRepairNet(new BigDecimal("400.01"));

        RuleEvaluation rv = new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim);

        /*
        boolean success = (claim.getInvoice().getHireNet()).compareTo(claim.getChoBand().getMaxHireNetCeiling()) <= 0;
        System.out.println("HIRE NET: "+claim.getInvoice().getHireNet());
        System.out.println("HIRE NET CELLING: "+claim.getChoBand().getMaxHireNetCeiling());
        System.out.println("RESULT: "+success);
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        */

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Net billed £400.01 exceeds the Repair Net ceiling of £400.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
