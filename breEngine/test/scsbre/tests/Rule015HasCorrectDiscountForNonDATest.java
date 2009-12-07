package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HasCorrectDiscountForNonDA;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule015HasCorrectDiscountForNonDATest extends TestCase {

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

        // SET INSURER
        claim.getInsurer().setAdminHandlingCharge(new BigDecimal("100"));
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHasCorrectDiscountForNonDA(false);
        RuleEvaluation rv = new HasCorrectDiscountForNonDA().applyToClaim(claim);

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
        claim.getBreBand().setHasCorrectDiscountForNonDA(true);

        // SET CHO
        claim.getCHOrg().setDelegatedAuthority(true);

        RuleEvaluation rv = new HasCorrectDiscountForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Rule does not apply to CHOs in the DA scheme"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }
    
    public void testPassed() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHasCorrectDiscountForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setDiscount(new BigDecimal("-15.00"));
        
        RuleEvaluation rv = new HasCorrectDiscountForNonDA().applyToClaim(claim);
        // System.out.println((claim.getInsurer().getAdminHandlingCharge()).multiply(CalcHelper.VAT_RATE).negate());
        // System.out.println((CalcHelper.EqualTo(claim.getInvoice().getDiscount(), (claim.getInsurer().getAdminHandlingCharge()).multiply(CalcHelper.VAT_RATE).negate())));
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setHasCorrectDiscountForNonDA(true);

        claim.getInvoice().setDiscount(new BigDecimal("-16.00"));

        RuleEvaluation rv = new HasCorrectDiscountForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Discount calculation is incorrect"));

    }
}
