package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HasCorrectRepairVatCalculation;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule010HasCorrectRepairVatCalculationTest extends TestCase {

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

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHasCorrectRepairVatCalculation(false);
        RuleEvaluation rv = new HasCorrectRepairVatCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

    public void testPassed() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHasCorrectRepairVatCalculation(true);

        // SET INVOICE
        claim.getInvoice().setRepairNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairVat(new BigDecimal("15.00"));
        // claim.getInvoice().setHireGross(new BigDecimal("115.00"));
        
        RuleEvaluation rv = new HasCorrectRepairVatCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHasCorrectRepairVatCalculation(true);

        // SET INVOICE
        claim.getInvoice().setRepairNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairVat(new BigDecimal("16.00"));
        // claim.getInvoice().setHireGross(new BigDecimal("135.00"));

        RuleEvaluation rv = new HasCorrectRepairVatCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Repair VAT calculation is incorrect."));

    }
}
