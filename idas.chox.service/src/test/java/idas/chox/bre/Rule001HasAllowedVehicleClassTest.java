package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasAllowedVehicleClass;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class Rule001HasAllowedVehicleClassTest extends TestCase {

    public Rule001HasAllowedVehicleClassTest() {
    }
    
    MockObjects testClaim = new MockObjects();
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim(){
        
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

        /*
         * CHO Control Flag is OFF
         */
        
        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(false);
        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())==ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {

        /*
         * CHO Control Flag is ON
         * Claim. V CLass is Null
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);
        claim.getCustomer().setVehicleClass(null);

        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle class is not specified."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())==ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testPassed() throws IOException {

        // CUSTOMER VEHICLE CLASS V.S HIRE MONITORING DETAIL VEHICLE CLASS (SAME)

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getCustomer().getVehicleClass().setName("SP1");
//        claim.getCustomer().getVehicleClass().setPrice(new BigDecimal("69.74"));

        claim.getVehicleHire().getVehicleClass().setName("SP1");
//        claim.getVehicleHire().getVehicleClass().setPrice(new BigDecimal("69.74"));
        
        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())==ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());
        
    }

    @Test
    public void testFailed() throws IOException {
        
        // CUSTOMER VEHICLE CLASS V.S HIRE MONITORING DETAIL VEHICLE CLASS (NOT SAME)

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getCustomer().getVehicleClass().setName("SP2");
//        claim.getCustomer().getVehicleClass().setPrice(new BigDecimal("62.74"));

        claim.getVehicleHire().getVehicleClass().setName("SP1");
//        claim.getVehicleHire().getVehicleClass().setPrice(new BigDecimal("69.74"));

        RuleEvaluation rv = new HasAllowedVehicleClass().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Vehicle class allocated for hire is not a like for like match on the customer's vehicle class."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())==ClaimStatus.INVOICE_ESCALATED_TO_CH);
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    
}