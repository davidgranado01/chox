package scsbre.tests.rules;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HireNetDoesNotExceedVehicleClassHireNetCeiling;
import scsbre.model.ClaimStatus;
import scsbre.tests.rules.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule003_HireNetDoesNotExceedVehicleClassHireNetCeiling extends TestCase {

    /*
     * COMPARE
     * INVOICE HIRE NET
     * VEHICLE CLASS CELLING VALUE
     * PASSED RULE IF "INVOICE HIRE NET" LESS THAN OR EQUAL TO "HIRE NET CELLING VALUE"
     */
    
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

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(400.00));
        
        // SET VEHICLE CLASS CELLING
        VehicleClassCellingInfo vehicleClassCelling = new VehicleClassCellingInfo();
        vehicleClassCelling.setHireNetCelling(new BigDecimal("500.00"));

        // SET CHOBAND
        claim.getChoBand().setHireNetCeiling(new BigDecimal(400.00));
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getChoBand().setVehicleClassCelling(vehicleClassCelling);

        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {


        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(false);
        
        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingEnable() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setVehicleClassCellingEnable(true);
        claim.getInvoice().setHireNet(new BigDecimal("499.99"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

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
        claim.getInvoice().setHireNet(new BigDecimal("500.01"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        /*
        boolean success = (claim.getInvoice().getHireNet()).compareTo(claim.getChoBand().getMaxHireNetCeiling()) <= 0;
        System.out.println("HIRE NET: "+claim.getInvoice().getHireNet());
        System.out.println("HIRE NET CELLING: "+claim.getChoBand().getMaxHireNetCeiling());
        System.out.println("RESULT: "+success);
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        */
        
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Hire Net billed £500.01 exceeds the Hire Net ceiling of £500.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_VehicleClassCellingDisable() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setVehicleClassCellingEnable(false);
        claim.getInvoice().setHireNet(new BigDecimal("400"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

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
        claim.getInvoice().setHireNet(new BigDecimal("400.01"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        /*
        boolean success = (claim.getInvoice().getHireNet()).compareTo(claim.getChoBand().getMaxHireNetCeiling()) <= 0;
        System.out.println("HIRE NET: "+claim.getInvoice().getHireNet());
        System.out.println("HIRE NET CELLING: "+claim.getChoBand().getMaxHireNetCeiling());
        System.out.println("RESULT: "+success);
        System.out.println("RESULT: "+rv.getRelatedRule().getNarrative());
        */
        
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Hire Net billed £400.01 exceeds the Hire Net ceiling of £400.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
}
