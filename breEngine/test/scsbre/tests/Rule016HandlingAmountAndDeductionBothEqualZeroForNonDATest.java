package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HandlingAmountAndDeductionBothEqualZeroForNonDA;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule016HandlingAmountAndDeductionBothEqualZeroForNonDATest extends TestCase {

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

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairNet(new BigDecimal("0.00"));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("0.00"));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));
        claim.getInvoice().setTotalNet(new BigDecimal("100.00"));
        claim.getInvoice().setTotalVat(new BigDecimal("15.00"));
        claim.getInvoice().setTotalGross(new BigDecimal("115.00"));

        // SET CHO
        claim.getCHOrg().setDelegatedAuthority(false);
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(false);
        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_DelegatedAuthorityIsOn() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET CHO
        claim.getCHOrg().setDelegatedAuthority(true);

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Rule does not apply to CHOs in the DA scheme"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }
    
    public void testPassed() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailled_ClaimsHandlingInvoiceAmountNotZero() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("10.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));
        
        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0."));

    }

    public void testFailled_DeductionForClaimsHandlingFeeNotZero() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("10.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0."));

    }


    public void testFailled_BothNotZero() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("10.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("10.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0."));

    }
}
