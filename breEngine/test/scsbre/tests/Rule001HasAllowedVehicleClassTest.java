package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HasAllowedVehicleClass;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;
import static org.junit.Assert.*;

public class Rule001HasAllowedVehicleClassTest extends TestCase {

    public Rule001HasAllowedVehicleClassTest() {
    }
    
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
        
        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {

        /*
         * CHO Control Flag is OFF
         */
        
        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(false);
        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {

        /*
         * CHO Control Flag is ON
         * Claim. V CLass is Null
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);
        claim.setVClass(null);

        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle class is not specified."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testPassed() throws IOException {

        // CUSTOMER VEHICLE CLASS V.S HIRE MONITORING DETAIL VEHICLE CLASS (SAME)

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);

        claim.getVClass().setCode("SP1");
        claim.getVClass().setPrice(new BigDecimal("69.74"));

        claim.getHireDetail().getVehicleClass().setCode("SP1");
        claim.getHireDetail().getVehicleClass().setPrice(new BigDecimal("69.74"));
        
        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());
        
    }

    @Test
    public void testFailed() throws IOException {
        
        // CUSTOMER VEHICLE CLASS V.S HIRE MONITORING DETAIL VEHICLE CLASS (NOT SAME)

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);

        claim.getVClass().setCode("SP2");
        claim.getVClass().setPrice(new BigDecimal("62.74"));

        claim.getHireDetail().getVehicleClass().setCode("SP1");
        claim.getHireDetail().getVehicleClass().setPrice(new BigDecimal("69.74"));

        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Vehicle class allocated for hire is not a like for like match on the customer's vehicle class."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    
}