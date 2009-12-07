package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.ActualHireDaysDoesNotExceedTotalLossInspection;
import scsbre.engine.util.BreBandCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule007ActualHireDaysDoesNotExceedTotalLossInspectionTest extends TestCase {

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

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(false);
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testSkipped_TotalLossIsFalse() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Rule only applies when the clam is a total loss"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);
        claim.getBreBand().setOfferMadeDays(5);
        claim.getBreBand().setReceiptOfFinalStatementChequeDays(3);
        claim.getBreBand().setInspectionDelayDays(1);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(true);
        claim.getHireDetail().setDays(8);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);
        claim.getBreBand().setOfferMadeDays(5);
        claim.getBreBand().setReceiptOfFinalStatementChequeDays(3);
        claim.getBreBand().setInspectionDelayDays(1);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(true);
        claim.getHireDetail().setDays(9);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        // BreBandCalcHelper cBand = BreBandCalcHelper.getInstance(claim.getBreBand());
        // System.out.println("getTotalLossInspectionDays:"+cBand.getTotalLossInspectionDays());

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailed() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedTotalLossInspection(true);
        claim.getBreBand().setOfferMadeDays(5);
        claim.getBreBand().setReceiptOfFinalStatementChequeDays(3);
        claim.getBreBand().setInspectionDelayDays(1);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(true);
        claim.getHireDetail().setDays(10);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim);

        // BreBandCalcHelper cBand = BreBandCalcHelper.getInstance(claim.getBreBand());
        // System.out.println("getTotalLossInspectionDays:"+cBand.getTotalLossInspectionDays());
        
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Number of hire days billed by the CHO exceeds the allowable days threshold for total loss hires."));

    }
}
