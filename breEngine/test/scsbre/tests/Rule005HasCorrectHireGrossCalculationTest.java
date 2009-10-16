package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HasCorrectHireGrossCalculation;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule005HasCorrectHireGrossCalculationTest extends TestCase {

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
        claim.getInvoice().setHireNet(new BigDecimal("200.00"));
        claim.getInvoice().setHireVat(new BigDecimal("30.00"));
        claim.getInvoice().setHireGross(new BigDecimal("230.00"));
        
        return claim;
    }

    @Test
    public void testSkipped() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasCorrectHireGrossCalculation(false);
        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_equal() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasCorrectHireGrossCalculation(true);

        claim.getInvoice().setHireGross(new BigDecimal("230.00"));
        
        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println("HIRE GROSS: "+claim.getInvoice().getHireGross());
        System.out.println("ECPEXTED GROSS: "+iCalc.getCalculatedHireGross());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        System.out.println("success: "+success);
        */
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_lessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasCorrectHireGrossCalculation(true);

        claim.getInvoice().setHireGross(new BigDecimal("229.89"));

        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println("HIRE GROSS: "+claim.getInvoice().getHireGross());
        System.out.println("ECPEXTED GROSS: "+iCalc.getCalculatedHireGross());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        System.out.println("success: "+success);
        */

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }


    @Test
    public void testFailed() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasCorrectHireGrossCalculation(true);

        claim.getInvoice().setHireGross(new BigDecimal("240.50"));

        RuleEvaluation rv = new HasCorrectHireGrossCalculation().applyToClaim(claim);

        /*
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        System.out.println("HIRE GROSS: "+claim.getInvoice().getHireGross());
        System.out.println("ECPEXTED GROSS: "+iCalc.getCalculatedHireGross());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        System.out.println("success: "+success);
        */

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Hire Gross calculation is incorrect."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }
}
