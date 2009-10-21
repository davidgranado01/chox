package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule020EstimatedRepairDaysPlusBandDaysDoNotExceedHireDaysTest extends TestCase {

    TestClaim testClaim = new TestClaim();

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

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
        claim.getInvoice().setHireNet(new BigDecimal("100.00"));
        claim.getInvoice().setRepairNet(new BigDecimal("0.00"));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal("0.00"));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal("0.00"));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal("0.00"));
        claim.getInvoice().setTotalNet(new BigDecimal("100.00"));
        claim.getInvoice().setTotalVat(new BigDecimal("15.00"));
        claim.getInvoice().setTotalGross(new BigDecimal("115.00"));
        claim.getInvoice().setDiscount(new BigDecimal("-5.00"));
        claim.getInvoice().setPenaltyCharge(new BigDecimal("10.00"));
        claim.getInvoice().setTotalToPay(new BigDecimal("120.00"));

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);

        // SET ENGINNERING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(10);
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(false);
        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testSkipped() throws IOException {
        
        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);

        claim.getHireDetail().setIsTotalLoss(true);
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(0);

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Claim is a Total Loss or Estimated Days Under Repair is less than 1"));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    public void testPassed_NonMobile() throws IOException {

        ClaimInfo claim = getTestClaim();
        
        // SET BRE BAND
        claim.getChoBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        /*
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        // getLabourCostAverageRateDay = 10
        // getDayBufferForEngineeringProcess : 6
        // getWeekendBuffer: 4
        // getTakeVehicleToGarageDaysNonMobile + getEstimatedDaysUnderRepair + getWeekendBuffer + getTakeVehicleOutDays + getEngineerInspectionDelayDays
        // 3 + 10 + 4 + 1 + 2: 20

int hireDays = claim.getHireDetail().getDays();

                int takeVehicleToGarageDays = claim.getCustomerVehicleDamage().getIsUsable()
                        ? claim.getChoBand().getTakeVehicleToGarageDaysMobile()
                        : claim.getChoBand().getTakeVehicleToGarageDaysNonMobile();

                int maxDays = claim.getEngineeringReport().getEstimatedDaysUnderRepair();

                maxDays += takeVehicleToGarageDays;

                maxDays += cCalc.getWeekendBuffer();
                maxDays += claim.getChoBand().getTakeVehicleOutDays();
                maxDays += claim.getChoBand().getEngineerInspectionDelayDays();

                boolean success = hireDays <= maxDays;
                
            System.out.println(">>>>"+hireDays);
            System.out.println(">>>>"+maxDays);
            System.out.println(">>>>"+success);
            */
        
        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_Mobile_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getCustomerVehicleDamage().setIsUsable(true);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        claim.getHireDetail().setDays(18);

        // SET ENGINERTING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testPassed_Mobile_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getCustomerVehicleDamage().setIsUsable(true);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        claim.getHireDetail().setDays(17);

        // SET ENGINERTING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }
    
    public void testPassed_Mobile_Failed() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays(true);
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
        claim.getCustomerVehicleDamage().setIsUsable(true);

        // SET HIRE DETAIL
        claim.getHireDetail().setIsTotalLoss(false);
        claim.getHireDetail().setDays(21);

        // SET ENGINERTING REPORT
        claim.getEngineeringReport().setEstimatedDaysUnderRepair(10);

        // SET HIRE MONIROTING DETAIL
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        RuleEvaluation rv = new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Number of hire days billed exceeds the allowable threshold (non total loss) with the inclusion of the Engineer's Esimtated Days Under Repair."));

    }
}
