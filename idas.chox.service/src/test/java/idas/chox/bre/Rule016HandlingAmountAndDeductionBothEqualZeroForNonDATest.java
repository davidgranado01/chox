package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.HandlingAmountAndDeductionBothEqualZeroForNonDA;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule016HandlingAmountAndDeductionBothEqualZeroForNonDATest extends TestCase {

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
        claim.getInvoice().setRepairNet(new BigDecimal("0.00"));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("0.00"));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));
        claim.getInvoice().setTotalNet(new BigDecimal("100.00"));
        claim.getInvoice().setTotalVat(new BigDecimal("15.00"));
        claim.getInvoice().setTotalGross(new BigDecimal("115.00"));

        // SET CHO
        claim.getChorganisation().setDelegatedAuthority(false);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(false);
        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_DelegatedAuthorityIsOn() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET CHO
        claim.getChorganisation().setDelegatedAuthority(true);

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Rule does not apply to CHOs in the DA scheme"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testFailled_ClaimsHandlingInvoiceAmountNotZero() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("10.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0."));

    }

    @Test
    public void testFailled_DeductionForClaimsHandlingFeeNotZero() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("10.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0."));

    }

    @Test
    public void testFailled_BothNotZero() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHandlingAmountAndDeductionBothEqualZeroForNonDA(true);

        // SET INVOICE
        claim.getInvoice().setClaimsHandlingInvoiceAmount(new BigDecimal("10.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("10.00"));

        RuleEvaluation rv = new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0."));

    }
}
