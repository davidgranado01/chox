package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.RepairGrossIsLessThanEstimatedTotalRepairAmount;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule008RepairGrossIsLessThanEstimatedTotalRepairAmountTest extends TestCase {

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
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setRepairGrossIsLessThanEstimatedTotalRepairAmount(false);
        RuleEvaluation rv = new RepairGrossIsLessThanEstimatedTotalRepairAmount().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_EstimatedTotalRepairAmountIsZero() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);

        // SET ENGINEERING REPORT
        claim.getEngineeringReport().setEstimatedTotalRepairAmount(new BigDecimal("0.00"));

        // SET INVOICE
        claim.getInvoice().setRepairGross(new BigDecimal("0.00"));

        RuleEvaluation rv = new RepairGrossIsLessThanEstimatedTotalRepairAmount().applyToClaim(claim);

        // System.out.println(">>>" + (claim.getEngineeringReport().getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0));

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_EstimatedTotalRepairAmountNotZero() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);

        // SET ENGINEERING REPORT
        claim.getEngineeringReport().setEstimatedTotalRepairAmount(new BigDecimal("100.00"));

        // SET INVOICE
        claim.getInvoice().setRepairGross(new BigDecimal("100.00"));
        
        RuleEvaluation rv = new RepairGrossIsLessThanEstimatedTotalRepairAmount().applyToClaim(claim);

        // System.out.println(">>>" + (claim.getEngineeringReport().getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0));
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        
    }
    
    @Test
    public void testFailled_EstimatedTotalRepairAmountIsZero() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);

        // SET ENGINEERING REPORT
        claim.getEngineeringReport().setEstimatedTotalRepairAmount(new BigDecimal("0"));

        // SET INVOICE
        claim.getInvoice().setRepairGross(new BigDecimal("10.00"));
        
        RuleEvaluation rv = new RepairGrossIsLessThanEstimatedTotalRepairAmount().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Repair Gross is higher than the Estimated Total Repair Amount."));

    }

    @Test
    public void testFailled_EstimatedTotalRepairAmountNoZero() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setRepairGrossIsLessThanEstimatedTotalRepairAmount(true);

        // SET ENGINEERING REPORT
        claim.getEngineeringReport().setEstimatedTotalRepairAmount(new BigDecimal("100.00"));

        // SET INVOICE
        claim.getInvoice().setRepairGross(new BigDecimal("101.00"));
        
        RuleEvaluation rv = new RepairGrossIsLessThanEstimatedTotalRepairAmount().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Repair Gross is higher than the Estimated Total Repair Amount."));
        
    }
}
