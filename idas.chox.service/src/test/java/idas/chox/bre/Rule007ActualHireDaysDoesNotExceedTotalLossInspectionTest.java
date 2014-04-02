package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.ActualHireDaysDoesNotExceedTotalLossInspection;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class Rule007ActualHireDaysDoesNotExceedTotalLossInspectionTest extends TestCase {

    MockObjects testClaim = new MockObjects();

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

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(false);
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testSkipped_TotalLossIsFalse() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(false);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Rule only applies when the claim is a total loss"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);
        claim.getBreBand().setOfferMadeDays(5);
        claim.getBreBand().setReceiptOfFinalStatementChequeDays(3);
        claim.getBreBand().setInspectionDelayDays(1);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setDays(8);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);
        claim.getBreBand().setOfferMadeDays(5);
        claim.getBreBand().setReceiptOfFinalStatementChequeDays(3);
        claim.getBreBand().setInspectionDelayDays(1);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setDays(9);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        // BreBandCalcHelper cBand = BreBandCalcHelper.getInstance(claim.getBreBand());
        // System.out.println("getTotalLossInspectionDays:"+cBand.getTotalLossInspectionDays());

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testFailed() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);
        claim.getBreBand().setOfferMadeDays(5);
        claim.getBreBand().setReceiptOfFinalStatementChequeDays(3);
        claim.getBreBand().setInspectionDelayDays(1);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setDays(10);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        // BreBandCalcHelper cBand = BreBandCalcHelper.getInstance(claim.getBreBand());
        // System.out.println("getTotalLossInspectionDays:"+cBand.getTotalLossInspectionDays());
        
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of hire days billed by the CHO (10 days) exceeds the allowable days threshold (9 days) for total loss hires."));

    }
}
