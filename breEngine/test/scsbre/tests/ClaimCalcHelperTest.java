package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.engine.util.DateHelper;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class ClaimCalcHelperTest extends TestCase {

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
    public void testNewLabourCost() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.setHireMonitoringDetail(null);
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        assertTrue(cCalc.getNewLabourCost().equals(new BigDecimal("0")));

    }

    @Test
    public void testNewLabourCost_LabourCostNotZero() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setAverageLabourRate(4);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("5.00"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // START VALIDATION
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        /*
        System.out.println(""+(claim.getHireMonitoringDetail().getLabourCost().compareTo(new BigDecimal(0.00))<1));
        System.out.println(""+(claim.getHireMonitoringDetail().getLabourHour()>0));
        System.out.println(new BigDecimal(claim.getChoBand().getAverageLabourRate()*claim.getHireMonitoringDetail().getLabourHour()));
        System.out.println("getDays: "+cCalc.getNewLabourCost());
        */

        // COMPARE RESULT
        assertTrue(cCalc.getNewLabourCost().equals(new BigDecimal("5.00")));

    }

    @Test
    public void testNewLabourCost_LabourCostIsZero_LabourRateNotZero() throws IOException {

        // claim.getHireMonitoringDetail.LabourRate X claim.getHireMonitoringDetail.LabourHour
        
        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setAverageLabourRate(4);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("0"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // START VALIDATION
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        
        /*
        System.out.println(""+(claim.getHireMonitoringDetail().getLabourCost().compareTo(new BigDecimal(0.00))<1));
        System.out.println(""+(claim.getHireMonitoringDetail().getLabourHour()>0));
        System.out.println("getDays: "+cCalc.getNewLabourCost());
        */

        // COMPARE RESULT
        assertTrue(cCalc.getNewLabourCost().equals(new BigDecimal("40")));

    }

    @Test
    public void testNewLabourCost_LabourCostIsZero_LabourRateIsZero() throws IOException {

        // claim.getChoBand.AverageLabourRate X claim.getHireMonitoringDetail.LabourHour
        
        ClaimInfo claim = getTestClaim();

        // SET BRE BAND
        claim.getChoBand().setAverageLabourRate(4);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("0"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("0"));

        // START VALIDATION
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        /*
        System.out.println(""+(claim.getHireMonitoringDetail().getLabourCost().compareTo(new BigDecimal(0.00))<1));
        System.out.println(""+(claim.getHireMonitoringDetail().getLabourHour()>0));
        System.out.println("getDays: "+cCalc.getNewLabourCost());
        */
        
        // COMPARE RESULT
        assertTrue(cCalc.getNewLabourCost().equals(new BigDecimal("16")));

    }

    @Test
    public void testLabourCostAverageRateDay_LabourCostIsZero() throws IOException {

        /*
         * USE getNewLabourCost : 40
         * LabourCost / AverageLabourRate / AverageLabourHoursPerHireDay
         */
        
        ClaimInfo claim = getTestClaim();

        // SET CHO BAND
        claim.getChoBand().setAverageLabourRate(4);
        claim.getChoBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("0"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        // getAverageLabourHoursPerHireDay
        // getAverageLabourRate
        // System.out.println(cCalc.getLabourCostAverageRateDay());
        
        assertTrue(cCalc.getLabourCostAverageRateDay()==5);
    }
    
    @Test
    public void testLabourCostAverageRateDay_LabourCostNotZero() throws IOException {

        /*
         * LabourCost / AverageLabourRate / AverageLabourHoursPerHireDay
         */
        
        ClaimInfo claim = getTestClaim();

        // SET CHO BAND
        claim.getChoBand().setAverageLabourRate(4);
        claim.getChoBand().setAverageLabourHoursPerHireDay(5);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("200"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        // getAverageLabourHoursPerHireDay
        // getAverageLabourRate
        // System.out.println(cCalc.getLabourCostAverageRateDay());
        
        assertTrue(cCalc.getLabourCostAverageRateDay()==10);
    }
    
     @Test
    public void testDayBufferForEngineeringProcess_withECD_usable() throws IOException {

        // TakeVehicleToGarageDaysMobile : 1
        // TakeVehicleToGarageDaysNonMobile : 3
        // TakeVehicleOutDays : 1
        // EngineerInspectionDelayDays : 2

        // TakeVehicleOutDays + EngineerInspectionDelayDays + TakeVehicleToGarageDaysMobile
        // 1 + 2 + 1: 4
        
        ClaimInfo claim = getTestClaim();

        // SET ECD
        claim.setHireMonitoringEcd(DateHelper.getDateFromString("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(true);

        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        assertTrue(cCalc.getDayBufferForEngineeringProcess()==4);
    }

    @Test
    public void testDayBufferForEngineeringProcess_withECD_notUsable() throws IOException {

        // TakeVehicleToGarageDaysMobile : 1
        // TakeVehicleToGarageDaysNonMobile : 3
        // TakeVehicleOutDays : 1
        // EngineerInspectionDelayDays : 2

        // TakeVehicleOutDays + EngineerInspectionDelayDays + TakeVehicleToGarageDaysNonMobile
        // 1 + 2 + 3 : 6
        
        ClaimInfo claim = getTestClaim();

        // SET ECD
        claim.setHireMonitoringEcd(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(false);
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        assertTrue(cCalc.getDayBufferForEngineeringProcess()==6);

    }

    @Test
    public void testDayBufferForEngineeringProcess_withoutECD_usable() throws IOException {

        // TakeVehicleToGarageDaysMobile : 1
        // TakeVehicleToGarageDaysNonMobile : 3
        // TakeVehicleOutDays : 1
        // EngineerInspectionDelayDays : 2

        // TakeVehicleOutDays + EngineerInspectionDelayDays + TakeVehicleToGarageDaysMobile
        // 1 + 2 + 1 : 4
        
        ClaimInfo claim = getTestClaim();

        // SET ECD
        claim.setHireMonitoringEcd(DateHelper.getDateFromString("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(true);
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        assertTrue(cCalc.getDayBufferForEngineeringProcess()==4);

    }

    @Test
    public void testDayBufferForEngineeringProcess_withoutECD_notUsable() throws IOException {

        // TakeVehicleToGarageDaysMobile : 1
        // TakeVehicleToGarageDaysNonMobile : 3
        // TakeVehicleOutDays : 1
        // EngineerInspectionDelayDays : 2

        // TakeVehicleOutDays + EngineerInspectionDelayDays + TakeVehicleToGarageDaysNonMobile
        // 1 + 2 + 3 : 6
        
        ClaimInfo claim = getTestClaim();

        // SET ECD
        claim.setHireMonitoringEcd(null);

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(false);
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        assertTrue(cCalc.getDayBufferForEngineeringProcess()==6);

    }

    @Test
    public void testNumberDayOfLabourCostWorthy() throws IOException {

        ClaimInfo claim = getTestClaim();

        // SET CHO BAND
        claim.getChoBand().setAverageLabourRate(4);
        claim.getChoBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("0"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.setHireMonitoringEcd(DateHelper.getDateFromString("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(true);

        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        assertTrue(cCalc.getLabourCostAverageRateDay()==5);
        assertTrue(cCalc.getDayBufferForEngineeringProcess()==4);
        assertTrue(cCalc.getWeekendBuffer()==2);
        assertTrue(cCalc.getNumberDayOfLabourCostWorthy()==11);
    }

}
