package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.ActualHireDaysDoesNotExceedAllowableHireDays;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class Rule006ActualHireDaysDoesNotExceedAllowableHireDaysTest extends TestCase {

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
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(false);
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_TotalLostIsTrue() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getCustomer().setIsTotalLoss(true);
        claim.getEngineerReport().setDays(1);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair"));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testSkipped_EstimatedDaysUnderRepairLessThanOne() throws IOException {
        
        Claim claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getCustomer().setIsTotalLoss(false);
        claim.getEngineerReport().setDays(2);

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair"));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_Both() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);

        claim.getCustomer().setIsTotalLoss(true);
        claim.getEngineerReport().setDays(2);
        
        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair"));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);
        claim.getBreBand().setAverageLabourRateStandard(4);
        claim.getBreBand().setAverageLabourRatePrestige(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(5);
        claim.getBreBand().setTakeVehicleToGarageDaysMobile(1);
        claim.getBreBand().setTakeVehicleToGarageDaysNonMobile(3);
        claim.getBreBand().setTakeVehicleOutDays(1);
        claim.getBreBand().setEngineerInspectionDelayDaysMobile(2);
        claim.getBreBand().setEngineerInspectionDelayDaysNonMobile(2);
        claim.getBreBand().setIsMobileDayAllowance(2);
        claim.getBreBand().setIsNotMobileDayAllowance(9);

        // SET HIRE MONITORING ECD
        claim.getCustomer().setInitialECD(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomer().setIsUsable(false);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(19);
                
        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(0);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays + getTakeVehicleToGarageDaysNonMobile + getIsNotMobileDayAllowance
        // 4 + 1 + 2 + 3 + 9 : 19
        // System.out.println("getDays: "+cCalc.getAllowedDays());

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);
        claim.getBreBand().setAverageLabourRateStandard(4);
        claim.getBreBand().setAverageLabourRatePrestige(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(5);
        claim.getBreBand().setTakeVehicleToGarageDaysMobile(1);
        claim.getBreBand().setTakeVehicleToGarageDaysNonMobile(3);
        claim.getBreBand().setTakeVehicleOutDays(1);
        claim.getBreBand().setEngineerInspectionDelayDaysMobile(2);
        claim.getBreBand().setEngineerInspectionDelayDaysNonMobile(2);
        claim.getBreBand().setIsMobileDayAllowance(2);
        claim.getBreBand().setIsNotMobileDayAllowance(9);

        // SET HIRE MONITORING ECD
        claim.getCustomer().setInitialECD(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomer().setIsUsable(false);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(18);

        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(0);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays + getTakeVehicleToGarageDaysNonMobile + getIsNotMobileDayAllowance
        // 4 + 1 + 2 + 3 + 9 : 19
        // System.out.println("getDays: "+cCalc.getAllowedDays());

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setActualHireDaysDoesNotExceedAllowableHireDays(true);
        claim.getBreBand().setAverageLabourRateStandard(4);
        claim.getBreBand().setAverageLabourRatePrestige(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(5);
        claim.getBreBand().setTakeVehicleToGarageDaysMobile(1);
        claim.getBreBand().setTakeVehicleToGarageDaysNonMobile(3);
        claim.getBreBand().setTakeVehicleOutDays(1);
        claim.getBreBand().setEngineerInspectionDelayDaysMobile(2);
        claim.getBreBand().setEngineerInspectionDelayDaysNonMobile(2);
        claim.getBreBand().setIsMobileDayAllowance(2);
        claim.getBreBand().setIsNotMobileDayAllowance(9);

        // SET HIRE MONITORING ECD
        claim.getCustomer().setInitialECD(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomer().setIsUsable(false);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(20);

        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(0);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays + getTakeVehicleToGarageDaysNonMobile + getIsNotMobileDayAllowance
        // 4 + 1 + 2 + 3 + 9 : 19
        // System.out.println("getDays: "+cCalc.getAllowedDays());

        RuleEvaluation rv = new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Number of hire days billed by the CHO exceeds the allowable days threshold (non total loss), taking into account the ECD(s) provided by the CHO and the additional days allowed through delay variables."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
}
