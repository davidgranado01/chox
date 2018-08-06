package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasSuppliedCorrectTotalToPay;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule019HasSuppliedCorrectTotalToPayTest extends TestCase {

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

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairNet(BigDecimal.ZERO);
        claim.getInvoice().setEngineerFeeNet(BigDecimal.ZERO);
        claim.getInvoice().setStorageRecoveryNet(BigDecimal.ZERO);
        claim.getInvoice().setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        claim.getInvoice().setTotalNet(new BigDecimal("100.00"));
        claim.getInvoice().setTotalVat(new BigDecimal("15.00"));
        claim.getInvoice().setTotalGross(new BigDecimal("115.00"));
        claim.getInvoice().setDiscount(new BigDecimal("-5.00"));
        claim.getInvoice().setHirePenaltyCharge(new BigDecimal("10.00"));
        claim.getInvoice().setFullTotalToPay(new BigDecimal("120.00"));
        claim.getInvoice().setInsurerDiscount(BigDecimal.ZERO);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasSuppliedCorrectTotalToPay(false);
        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasSuppliedCorrectTotalToPay(true);

        claim.getInvoice().setFullTotalToPay(new BigDecimal("110.00"));

        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasSuppliedCorrectTotalToPay(true);

        claim.getInvoice().setFullTotalToPay(new BigDecimal("120.00"));

        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println(">>>"+claim.getInvoice().getFullTotalToPay());
        System.out.println(">>>"+iCalc.getCalculatedTotalToPay());
        System.out.println(">>>"+CalcHelper.LessThanOrEqualTo(claim.getInvoice().getFullTotalToPay(), iCalc.getCalculatedTotalToPay()));
         */

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testFailled() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasSuppliedCorrectTotalToPay(true);

        claim.getInvoice().setFullTotalToPay(new BigDecimal("130.00"));

        RuleEvaluation rv = new HasSuppliedCorrectTotalToPay().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println(">>>"+claim.getInvoice().getFullTotalToPay());
        System.out.println(">>>"+iCalc.getCalculatedTotalToPay());
        System.out.println(">>>"+CalcHelper.LessThanOrEqualTo(claim.getInvoice().getFullTotalToPay(), iCalc.getCalculatedTotalToPay()));
         */

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Total to Pay calculation is incorrect."));

    }
}
