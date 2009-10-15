package scsbre.tests.rules;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.ActualHireDaysDoesNotExceedAllowableHireDays;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.rules.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule006_ActualHireDaysDoesNotExceedAllowableHireDays extends TestCase {

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

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(false);
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_TotalLostIsTrue() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getHireDetail().setIsTotalLoss(true);
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(1);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testSkipped_EstimatedDaysUnderRepairLessThanOne() throws IOException {
        
        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getHireDetail().setIsTotalLoss(false);
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(2);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_Both() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(false);

        claim.getHireDetail().setIsTotalLoss(true);
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(2);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {
        
        // TODO: ActualHireDaysDoesNotExceedAllowableHireDays

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getHireDetail().setIsTotalLoss(false);
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(0);

        
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        boolean success = claim.getHireDetail().getDays() <= cCalc.getAllowedDays();
        System.out.println("getDays: "+claim.getHireDetail().getDays());
        System.out.println("getAllowedDays: "+cCalc.getAllowedDays());
        
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
}
