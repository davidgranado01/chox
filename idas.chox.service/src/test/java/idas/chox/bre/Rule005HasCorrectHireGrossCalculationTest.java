package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasCorrectHireGrossCalculation;
import idas.chox.core.util.CalcHelper;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule005HasCorrectHireGrossCalculationTest extends TestCase {

    MockObjects testClaim = new MockObjects();
    String invoiceAmt = "200";

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

        //CalcHelper
        BigDecimal invNet = new BigDecimal(invoiceAmt);
        BigDecimal invVat = invNet.multiply(CalcHelper.VAT_RATE);
        BigDecimal invGross = invNet.add(invVat);
        // SET INVOICE
        claim.getInvoice().setHireNet(invNet);
        claim.getInvoice().setHireVat(invVat);
        claim.getInvoice().setHireGross(invGross);

        return claim;
    }

    @Test
    public void testSkipped() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectHireGrossCalculation(false);
        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_equal() throws IOException {

        BigDecimal invNet = new BigDecimal(invoiceAmt);
        BigDecimal invVat = invNet.multiply(CalcHelper.VAT_RATE);
        BigDecimal invGross = invNet.add(invVat);

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectHireGrossCalculation(true);

        claim.getInvoice().setHireGross(invGross);

        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println("HIRE GROSS: "+claim.getInvoice().getHireGross());
        System.out.println("ECPEXTED GROSS: "+iCalc.getCalculatedHireGross());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        System.out.println("success: "+success);
        */

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertTrue(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_lessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectHireGrossCalculation(true);

        BigDecimal invNet = new BigDecimal(invoiceAmt);
        BigDecimal invVat = invNet.multiply(CalcHelper.VAT_RATE);
        BigDecimal invGross = invNet.add(invVat);

        claim.getInvoice().setHireGross(invGross.add(new BigDecimal("-0.06")));
        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println("HIRE GROSS: "+claim.getInvoice().getHireGross());
        System.out.println("ECPEXTED GROSS: "+iCalc.getCalculatedHireGross());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        System.out.println("success: "+success);
        */

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertTrue(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHasCorrectHireGrossCalculation(true);

        claim.getInvoice().setHireGross(new BigDecimal("241.50"));

        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println("HIRE GROSS: "+claim.getInvoice().getHireGross());
        System.out.println("ECPEXTED GROSS: "+iCalc.getCalculatedHireGross());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        System.out.println("success: "+success);
        */

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Hire Gross calculation is incorrect."));
        assertTrue(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertTrue(rv.getIsVisibleToCHO());

    }
}
