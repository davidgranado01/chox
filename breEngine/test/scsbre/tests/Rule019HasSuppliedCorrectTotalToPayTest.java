package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HasSuppliedCorrectTotalToPay;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule019HasSuppliedCorrectTotalToPayTest extends TestCase {

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
        claim.getInvoice().setHireNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairNet(new BigDecimal("0.00"));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("0.00"));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));
        claim.getInvoice().setTotalNet(new BigDecimal("100.00"));
        claim.getInvoice().setTotalVat(new BigDecimal("15.00"));
        claim.getInvoice().setTotalGross(new BigDecimal("115.00"));
        claim.getInvoice().setDiscount(new BigDecimal("-5.00"));
        claim.getInvoice().setPenaltyCharge(new BigDecimal("10.00"));
        claim.getInvoice().setTotalToPay(new BigDecimal("120.00"));
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasSuppliedCorrectTotalToPay(false);
        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testPassed_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasSuppliedCorrectTotalToPay(true);

        claim.getInvoice().setTotalToPay(new BigDecimal("110.00"));

        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasSuppliedCorrectTotalToPay(true);

        claim.getInvoice().setTotalToPay(new BigDecimal("120.00"));

        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println(">>>"+claim.getInvoice().getTotalToPay());
        System.out.println(">>>"+iCalc.getCalculatedTotalToPay());
        System.out.println(">>>"+CalcHelper.LessThanOrEqualTo(claim.getInvoice().getTotalToPay(), iCalc.getCalculatedTotalToPay()));
        */

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasSuppliedCorrectTotalToPay(true);

        claim.getInvoice().setTotalToPay(new BigDecimal("130.00"));

        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println(">>>"+claim.getInvoice().getTotalToPay());
        System.out.println(">>>"+iCalc.getCalculatedTotalToPay());
        System.out.println(">>>"+CalcHelper.LessThanOrEqualTo(claim.getInvoice().getTotalToPay(), iCalc.getCalculatedTotalToPay()));
        */

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Total to Pay calculation is incorrect."));

    }
}
