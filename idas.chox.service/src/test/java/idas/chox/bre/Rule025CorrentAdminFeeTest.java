package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasCorrectAdminFee;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule025CorrentAdminFeeTest extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim() {

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

        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(false);
        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    public void testPassed_managingRepairIsTrue_equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("60.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsTrue_lessthan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("50.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsTrue_lessthan_2() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsFalse_equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_managingRepairIsFalse_lessthan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailled_managingRepairIsTrue() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("61.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Admin Fee billed is incorrect."));

    }

    public void testFailled_managingRepairIsFalse() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);

        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("41.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Admin Fee billed is incorrect."));

    }
}
