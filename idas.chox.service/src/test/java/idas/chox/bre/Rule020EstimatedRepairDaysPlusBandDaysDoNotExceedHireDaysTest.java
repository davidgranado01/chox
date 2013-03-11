package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;



public class Rule020EstimatedRepairDaysPlusBandDaysDoNotExceedHireDaysTest extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

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

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairNet(new BigDecimal("0.00"));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("0.00"));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));
        claim.getInvoice().setTotalNet(new BigDecimal("100.00"));
        claim.getInvoice().setTotalVat(new BigDecimal("15.00"));
        claim.getInvoice().setTotalGross(new BigDecimal("115.00"));
        claim.getInvoice().setDiscount(new BigDecimal("-5.00"));
        claim.getInvoice().setHirePenaltyCharge(new BigDecimal("10.00"));
        claim.getInvoice().setFullTotalToPay(new BigDecimal("120.00"));

        // SET HIRE DETAIL
        claim.getVehicleHire().setIsTotalLoss(false);

        // SET ENGINNERING REPORT
        claim.getEngineerReport().setDays(10);
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(false);
        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped() throws IOException {
        
        Claim claim = getTestClaim();
        claim.getBreBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);

        claim.getVehicleHire().setIsTotalLoss(true);
        claim.getEngineerReport().setDays(0);

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is a Total Loss or Estimated Days Under Repair is less than 1 or is not present"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testPassed_NonMobile() throws IOException {

        Claim claim = getTestClaim();
        
        // SET BRE BAND
        claim.getBreBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getVehicleHire().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(20);

        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        /*
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getTakeVehicleToGarageDaysNonMobile + getEstimatedDaysUnderRepair + getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays
        // 3 + 10 + 4 + 1 + 2: 20

int hireDays = claim.getVehicleHire().getDays();

                int takeVehicleToGarageDays = claim.getCustomer().getIsUsable()
                        ? claim.getBreBand().getTakeVehicleToGarageDaysMobile()
                        : claim.getBreBand().getTakeVehicleToGarageDaysNonMobile();

                int maxDays = claim.getEngineerReport().getEstimatedDaysUnderRepair();

                maxDays += takeVehicleToGarageDays;

                maxDays += cCalc.getWeekendBuffer();
                maxDays += claim.getBreBand().getTakeVehicleOutDays();
                maxDays += claim.getBreBand().getEngineerInspectionDelayDays();

                boolean success = hireDays <= maxDays;
                
            System.out.println(">>>>"+hireDays);
            System.out.println(">>>>"+maxDays);
            System.out.println(">>>>"+success);
            */
        
        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_Mobile_Equals() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getCustomer().setIsUsable(true);

        // SET HIRE DETAIL
        claim.getVehicleHire().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(18);

        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_Mobile_LessThan() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getCustomer().setIsUsable(true);

        // SET HIRE DETAIL
        claim.getVehicleHire().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(17);

        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }
    
    @Test
    public void testPassed_Mobile_Failed() throws IOException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getCustomer().setIsUsable(true);

        // SET HIRE DETAIL
        claim.getVehicleHire().setIsTotalLoss(false);
        claim.getVehicleHire().setDays(21);

        // SET ENGINERTING REPORT
        claim.getEngineerReport().setDays(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of hire days billed by the CHO (21 days) exceeds the allowable threshold for non total loss hires (18 days) with the inclusion of the Engineer's estimated days under repair."));

    }
}
