package idas.chox.bre;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.service.bre.rules.ClientVatRegisteredCheck;

/**
 *
 * @author John
 */
public class Rule089ClientVatRegisteredCheckTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    private Claim getTestClaim() {

        Claim claim = new Claim();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setBreBand(testClaim.getTestBreBand());
        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setInvoice(testClaim.getTestExtras());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setClientVatRegisteredCheck(true);
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClientVatRegistered(Boolean.TRUE);
        claim.setHireMonitoringDetail(hmd);
        
        return claim;
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    
    /*
     * Skip when rule switched off
     */
    @Test
    public void testSkipped_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setClientVatRegisteredCheck(false);
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    
    
    /*
     * Rule skipped when no hire monitoring
     */
    @Test
    public void testSkipped_3() throws IOException {
        Claim claim = getTestClaim();
        claim.setHireMonitoringDetail(null);
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    /*
     * Rule skipped when client not vat registered
     */
    @Test
    public void testSkipped_4() throws IOException {
        Claim claim = getTestClaim();
        claim.getHireMonitoringDetail().setClientVatRegistered(Boolean.FALSE);
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }


    /*
     * Client Vat Registered and no VAT charges
     */
    @Test
    public void testPassed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setHireVat(BigDecimal.ZERO);
        claim.getInvoice().setRepairVat(BigDecimal.ZERO);
        claim.getInvoice().setStorageRecoveryVat(BigDecimal.ZERO);
        
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }


    /*
     * Client Vat Registered and charging hire vat
     */
    @Test
    public void testFailed_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setHireVat(BigDecimal.TEN);
        claim.getInvoice().setRepairVat(BigDecimal.ZERO);
        claim.getInvoice().setStorageRecoveryVat(BigDecimal.ZERO);
        
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    /*
     * Client Vat Registered and charging repair vat
     */
    @Test
    public void testFailed_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setHireVat(BigDecimal.ZERO);
        claim.getInvoice().setRepairVat(BigDecimal.TEN);
        claim.getInvoice().setStorageRecoveryVat(BigDecimal.ZERO);
        
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    /*
     * Client Vat Registered and charging stoarge recovery vat
     */
    @Test
    public void testFailed_3() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setHireVat(BigDecimal.ZERO);
        claim.getInvoice().setRepairVat(BigDecimal.ZERO);
        claim.getInvoice().setStorageRecoveryVat(BigDecimal.TEN);
        
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    /*
     * Client Vat Registered and hire and repair vat
     */
    @Test
    public void testFailed_4() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setHireVat(BigDecimal.TEN);
        claim.getInvoice().setRepairVat(BigDecimal.TEN);
        claim.getInvoice().setStorageRecoveryVat(BigDecimal.ZERO);
        
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }

    /*
     * Client Vat Registered and charging hire, repair and stoarge recovery vat
     */
    @Test
    public void testFailed_5() throws IOException {
        Claim claim = getTestClaim();
        claim.getInvoice().setHireVat(BigDecimal.TEN);
        claim.getInvoice().setRepairVat(BigDecimal.TEN);
        claim.getInvoice().setStorageRecoveryVat(BigDecimal.TEN);
        
        RuleEvaluation rv = new ClientVatRegisteredCheck().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());
    }


}
