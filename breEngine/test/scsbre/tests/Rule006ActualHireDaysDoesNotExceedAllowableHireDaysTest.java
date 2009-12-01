package scsbre.tests;

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
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule006ActualHireDaysDoesNotExceedAllowableHireDaysTest extends TestCase {

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
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getHireDetail().setIsTotalLoss(true);
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(2);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);
        claim.getChoBand().setAverageLabourRate(4);
        claim.getChoBand().setAverageLabourHoursPerHireDay(5);
        claim.getChoBand().setTakeVehicleToGarageDaysMobile(1);
        claim.getChoBand().setTakeVehicleToGarageDaysNonMobile(3);
        claim.getChoBand().setTakeVehicleOutDays(1);
        claim.getChoBand().setEngineerInspectionDelayDays(2);
        claim.getChoBand().setIsMobileDayAllowance(2);
        claim.getChoBand().setIsNotMobileDayAllowance(9);

        // SET HIRE MONITORING ECD
        claim.setHireMonitoringEcd(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(false);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        claim.getHireDetail().setDays(19);
                
        // SET ENGINERTING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(0);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays + getTakeVehicleToGarageDaysNonMobile + getIsNotMobileDayAllowance
        // 4 + 1 + 2 + 3 + 9 : 19
        // System.out.println("getDays: "+cCalc.getAllowedDays());

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);
        claim.getChoBand().setAverageLabourRate(4);
        claim.getChoBand().setAverageLabourHoursPerHireDay(5);
        claim.getChoBand().setTakeVehicleToGarageDaysMobile(1);
        claim.getChoBand().setTakeVehicleToGarageDaysNonMobile(3);
        claim.getChoBand().setTakeVehicleOutDays(1);
        claim.getChoBand().setEngineerInspectionDelayDays(2);
        claim.getChoBand().setIsMobileDayAllowance(2);
        claim.getChoBand().setIsNotMobileDayAllowance(9);

        // SET HIRE MONITORING ECD
        claim.setHireMonitoringEcd(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(false);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        claim.getHireDetail().setDays(18);

        // SET ENGINERTING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(0);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays + getTakeVehicleToGarageDaysNonMobile + getIsNotMobileDayAllowance
        // 4 + 1 + 2 + 3 + 9 : 19
        // System.out.println("getDays: "+cCalc.getAllowedDays());

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);
        claim.getChoBand().setAverageLabourRate(4);
        claim.getChoBand().setAverageLabourHoursPerHireDay(5);
        claim.getChoBand().setTakeVehicleToGarageDaysMobile(1);
        claim.getChoBand().setTakeVehicleToGarageDaysNonMobile(3);
        claim.getChoBand().setTakeVehicleOutDays(1);
        claim.getChoBand().setEngineerInspectionDelayDays(2);
        claim.getChoBand().setIsMobileDayAllowance(2);
        claim.getChoBand().setIsNotMobileDayAllowance(9);

        // SET HIRE MONITORING ECD
        claim.setHireMonitoringEcd(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(false);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        claim.getHireDetail().setDays(20);

        // SET ENGINERTING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(0);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays + getTakeVehicleToGarageDaysNonMobile + getIsNotMobileDayAllowance
        // 4 + 1 + 2 + 3 + 9 : 19
        // System.out.println("getDays: "+cCalc.getAllowedDays());

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Number of hire days billed by the CHO exceeds the allowable days threshold (non total loss), taking into account the ECD(s) provided by the CHO and the additional days allowed through delay variables."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
