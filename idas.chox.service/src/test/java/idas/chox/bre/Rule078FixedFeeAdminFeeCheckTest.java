package idas.chox.bre;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Invoice;
import idas.chox.core.model.ClaimType;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.FixedFeeAdminFeeCheck;
import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;

/**
 *
 * @author John
 */
public class Rule078FixedFeeAdminFeeCheckTest extends BaseTest {
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

        claim.setBreBand(testClaim.getTestBreBand());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.getVehicleHire().setVehicleClass(null);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.setClaimNumber("0123456789");
        claim.setManagingRepair(true);
        claimService.updateClaim(claim);
        claimService.flush();

        return claim;
    }

    @Test
    public void testSkipped_ruleOff() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setFixedFeeAdminFeeCheck(false);
        FixedFeeAdminFeeCheck rule = new FixedFeeAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }
    
    @Test
    public void testSkipped_wrongType() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setFixedFeeAdminFeeCheck(true);
        FixedFeeAdminFeeCheck rule = new FixedFeeAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }
    
    
    @Test
    public void testPassed_managingRepair() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setFixedFeeAdminFeeCheck(true);
        claim.getBreBand().setAdminFeeCeilingFixedFeeManagingRepair(new BigDecimal("60.00"));

        Invoice invoice = new Invoice();
        invoice.setAdminFee(new BigDecimal("60.00"));
        claim.setInvoice(invoice);
        

        FixedFeeAdminFeeCheck rule = new FixedFeeAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_notManagingRepair() throws IOException {
        Claim claim = getTestClaim();
        claim.setManagingRepair(false);
        claim.getBreBand().setFixedFeeAdminFeeCheck(true);
        claim.getBreBand().setAdminFeeCeilingFixedFee(new BigDecimal("50.00"));

        Invoice invoice = new Invoice();
        invoice.setAdminFee(new BigDecimal("50.00"));
        claim.setInvoice(invoice);
        

        FixedFeeAdminFeeCheck rule = new FixedFeeAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testFailed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setFixedFeeAdminFeeCheck(true);
        claim.getBreBand().setAdminFeeCeilingFixedFeeManagingRepair(new BigDecimal("55.00"));

        Invoice invoice = new Invoice();
        invoice.setAdminFee(new BigDecimal("60.00"));
        claim.setInvoice(invoice);
        

        FixedFeeAdminFeeCheck rule = new FixedFeeAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Admin Fee billed is incorrect. The allowed Admin Fee for Fixed-Fee claims managing the repair is £55.00."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_2() throws IOException {
        Claim claim = getTestClaim();
        claim.setManagingRepair(false);
        claim.getBreBand().setFixedFeeAdminFeeCheck(true);
        claim.getBreBand().setAdminFeeCeilingFixedFee(new BigDecimal("50.00"));

        Invoice invoice = new Invoice();
        invoice.setAdminFee(new BigDecimal("60.00"));
        claim.setInvoice(invoice);
        

        FixedFeeAdminFeeCheck rule = new FixedFeeAdminFeeCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Admin Fee billed is incorrect. The allowed Admin Fee for Fixed-Fee claims not managing the repair is £50.00."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    
    

}
