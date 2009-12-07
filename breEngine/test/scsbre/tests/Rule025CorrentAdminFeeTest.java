package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.CorrentAdminFee;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule025CorrentAdminFeeTest extends TestCase {

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

        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));
                
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(false);
        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

    public void testPassed_managingRepairIsTrue_equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("60.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsTrue_lessthan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("50.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsTrue_lessthan_2() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }
    
    public void testPassed_managingRepairIsFalse_equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsFalse_lessthan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailled_managingRepairIsTrue() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("61.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Admin Fee billed is incorrect."));

    }

    public void testFailled_managingRepairIsFalse() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("41.00"));

        RuleEvaluation rv = new CorrentAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Admin Fee billed is incorrect."));

    }
}
