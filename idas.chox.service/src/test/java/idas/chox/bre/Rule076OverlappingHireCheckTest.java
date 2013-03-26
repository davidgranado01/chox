package idas.chox.bre;

import java.io.IOException;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.OverlappingHireCheck;
import idas.chox.test.BaseTest;


public class Rule076OverlappingHireCheckTest extends BaseTest {
    MockObjects testClaim = new MockObjects();
    

    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }
    

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    
    @Transactional
    private Claim getTestClaim() {
        Claim claim = new Claim();

        claim.setChoReference("testClaim");
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        
        claim.setInsurer(insurerService.getInsurer(3));
        
        claim.setBreBand(testClaim.getTestBreBand());
        
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.getVehicleHire().setVehicleClass(null);
        claim.getVehicleHire().setHireStart(DateHelper.parse("01/03/2013"));
        claim.getVehicleHire().setHireEnd(DateHelper.parse("14/03/2013"));
        claim.getVehicleHire().setVehicleRegistration("LC59YOY");
        
        claim.setCustomer(customerService.getCustomer(999));
        claim.getCustomer().setClaimReference("123456");
        
        claim.setClaimNumber("0123456789");
        
        claimService.updateClaim(claim);
        claimService.flush();

        return claim;
    }
    
    @Test
    public void testSkipped() throws IOException {
        Claim claim = getTestClaim();
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setOverlappingHireCheck(false);
        
        RuleEvaluation rv = new OverlappingHireCheck().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
   
    @Test
    public void testPassed() throws IOException {
        getTestClaim();
        
        Claim claim = new Claim();
        claim.setChoReference("testClaim1");
        
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setOverlappingHireCheck(true);
             
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.getCustomer().setClaimReference("345");
        
        claim.setInsurer(testClaim.getTestInsurer());
        claim.getInsurer().setId(4);
        
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.getVehicleHire().setHireStart(DateHelper.parse("01/03/2013"));
        claim.getVehicleHire().setHireEnd(DateHelper.parse("14/03/2013"));
        claim.getVehicleHire().setVehicleRegistration("LC59YOY");
        
        claim.setClaimNumber("01234567891");
        
        OverlappingHireCheck overlappingHireCheck = new OverlappingHireCheck();
        overlappingHireCheck.setClaimService(claimService);
        RuleEvaluation rv = overlappingHireCheck.applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().contains(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testFailed() throws IOException {
        getTestClaim();
        
        Claim claim = new Claim();
        
        claim.setChoReference("testClaim1");
        
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setOverlappingHireCheck(true);
                
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.getCustomer().setClaimReference("345");
        
        claim.setInsurer(testClaim.getTestInsurer());
        claim.getInsurer().setId(3);
        
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.getVehicleHire().setHireStart(DateHelper.parse("01/03/2013"));
        claim.getVehicleHire().setHireEnd(DateHelper.parse("14/03/2013"));
        claim.getVehicleHire().setVehicleRegistration("LC59YOY");
        
        claim.setClaimNumber("01234567891");
        
        OverlappingHireCheck overlappingHireCheck = new OverlappingHireCheck();
        overlappingHireCheck.setClaimService(claimService);
        RuleEvaluation rv = overlappingHireCheck.applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().contains("Overlapping Hire - replacement hire vehicle on hire during overlapping periods as indicated on claim "));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
