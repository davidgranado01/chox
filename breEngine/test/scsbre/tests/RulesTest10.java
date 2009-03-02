/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.RulesEngine;
import scsbre.engine.RulesEngineResponse;
import scsbre.engine.util.mathHelper;
import scsbre.model.*;
import scsbre.tests.sample.*;
import static org.junit.Assert.*;

/**
 *  numberOfHireDay = 15 
 * 
 *  LabourCost = 0
 *  LabourHour = 4
 *  LabourRate = 220
 * 
 *  AverageLabourHoursPerHireDay = 4
 *  AverageLabourrate = 40
 * 
 *  DayIntoGarage = 3
 *  DayOutFromGarage = 1
 *  EngineerInspectionDelay = 2
 *  Total = 6
 * 
 *  >> LabourCose <= 0
 *  Applied Rule 2 to get new Labour Cost the Rule 1
 *  
 *  RULE 2
 *  NewLabourCost = LabourRate * LabourHour
 *  NewLabourCost = 220 * 4
 *  NewLabourCost = 880
 * 
 *  RULE 1
 *  Y = (LabourCost / AverageLabourRate / AverageLabourHour) 
 *  Y = 880 / 40 / 4
 *  Y = 5.5
 *  Y = 6
 * 
 *  Weekend Buffer
 *  X = 4 (Becuase Y + DayIntoGarage + DayOutFromGarage + EngineerInspectionDelay is 12)
 * 
 *  Z = Y + X + DayIntoGarage + DayOutFromGarage + EngineerInspectionDelay
 *  Z = 6 + 4 + 3 + 1 + 2
 *  Z = 16
 * 
 *  RULE is PASSED because 
 *  numberOfHireDay < Z = PASSES
 */

public class RulesTest10 {

    public RulesTest10() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test
    public void TestDefaultCase(){
        
        
        IClaimInfo claim = getClaim();
        RulesEngine engine = RulesEngine.getInstance(claim);
        RulesEngineResponse res =  engine.ResolveStatus();
        List<RuleEvaluation> results = res.getResults();
        
        
        for(int i = 0; i < results.size(); i++){
            
            RuleEvaluation res1 = results.get(i);
            
            System.out.println(">>>"+res1.getResult().toString() + "|" + res1.getRelatedRule().getRuleId());
            
        }
        
        assertTrue(results.get(0).getResult() == RuleEvaluationResult.RulePassed);
        
        assertTrue(results.get(0).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(1).getResult() == RuleEvaluationResult.RulePassed);
        //assertTrue(results.get(2).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(3).getResult() == RuleEvaluationResult.RuleFailed);
        //assertTrue(results.get(4).getResult() == RuleEvaluationResult.RulePassed);
        //assertTrue(results.get(5).getResult() == RuleEvaluationResult.RuleFailed);
        assertTrue(results.get(6).getResult() == RuleEvaluationResult.RuleSkipped);
        assertTrue(results.get(7).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(8).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(9).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(10).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(11).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(12).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(13).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(14).getResult() == RuleEvaluationResult.RuleFailed);
        assertTrue(results.get(15).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(16).getResult() == RuleEvaluationResult.RuleSkipped);
        assertTrue(results.get(17).getResult() == RuleEvaluationResult.RulePassed);
        //assertTrue(results.get(18).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(19).getResult() == RuleEvaluationResult.RuleSkipped);
        assertTrue(results.get(20).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(21).getResult() == RuleEvaluationResult.RulePassed);
        
    }
    
    
    private static Date getDateFromString(String inp){
        
          SimpleDateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
          try{
            return df.parse(inp);
          }
          catch(Exception e){
              
              return null;
          }
        
    }
    
    private static Date getDefaultDate(){
        return getDateFromString("01/01/2001");
    }
    
    private static Date getDefaultDatePlusSeven(){
        return getDateFromString("08/01/2001");
    }
    
    private static IVehicleClassInfo F1(){
        
        VehicleClassInfo vc = new  VehicleClassInfo();
        vc.setCode("F1");
        vc.setPrice(new BigDecimal(86.94));
        return vc;
    }

    
    private IEngineerReportInfo getEngineerReport(){
        
        EngineerReportInfo eReport = new EngineerReportInfo();
        eReport.setEstimatedDaysUnderRepair(0);
        eReport.setEstimatedLabourAmount(BigDecimal.ZERO);
        eReport.setEstimatedTotalRepairAmount(BigDecimal.ZERO);
        
        return eReport;
    }
    
    private IInvoiceInfo getInvoice(){
        
        InvoiceInfo inv = new InvoiceInfo();
        
        inv.setHireNet(new BigDecimal(301));
        inv.setHireVat(new BigDecimal(45.15));
        inv.setHireGross(new BigDecimal(345.4));

        inv.setRepairNet(BigDecimal.ZERO);
        inv.setRepairVat(BigDecimal.ZERO);
        inv.setRepairGross(BigDecimal.ZERO);

        inv.setEngineerFeeVat(BigDecimal.ZERO);
        inv.setEngineerFeeNet(BigDecimal.ZERO);
        inv.setEngineerFeeGross(BigDecimal.ZERO);

        inv.setStorageRecoveryNet(new BigDecimal(100));
        inv.setStorageRecoveryVat(new BigDecimal(15));
        inv.setStorageRecoveryGross(new BigDecimal(115));

        inv.setTotalNet(new BigDecimal(401));
        inv.setTotalVat(new BigDecimal(60.15));
        inv.setTotalGross(new BigDecimal(461.15));

        inv.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        inv.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        inv.setDiscount(BigDecimal.ZERO);
        inv.setTotalToPay(BigDecimal.ZERO);
        inv.setPenaltyCharge(BigDecimal.ZERO);

        return inv;
    }
    
    private ICHOBandInfo getCHOBand(){
        
        CHOBandInfo band = new CHOBandInfo();

        band.setHireNetCeiling(BigDecimal.ZERO);
        band.setHireDayCeiling(0);
        band.setMaxRepairValue(BigDecimal.ZERO);

        band.setTakeVehicleToGarageDaysMobile(1);
        band.setTakeVehicleToGarageDaysNonMobile(3);        
        
        band.setWeekendBufferDays(0);
        band.setTakeVehicleOutDays(1);
        band.setEngineerInspectionDelayDays(2);

        band.setIsMobileDayAllowance(2);
        band.setIsNotMobileDayAllowance(9);

        band.setOfferMadeDays(7);
        band.setReceiptOfFinalStatementChequeDays(10);
        band.setInspectionDelayDays(4);
        band.setHireRateChargeTolerance(new BigDecimal(0.01));

        band.setAverageLabourHoursPerHireDay(4);
        band.setAverageLabourRate(40);
        return band;
        
    }
    
    private IExtrasInfo getExtras(){
        
        ExtrasInfo xtra = new ExtrasInfo();

        xtra.setAdminFee (BigDecimal.ZERO);
        xtra.setAutomaticFee (BigDecimal.ZERO);
        xtra.setBabySeatFee (BigDecimal.ZERO);
        xtra.setCdwFee (BigDecimal.ZERO);
        xtra.setDeliveryCollectionFee (BigDecimal.ZERO);
        xtra.setDualControlFee (BigDecimal.ZERO);
        xtra.setEstateFee (BigDecimal.ZERO);
        xtra.setNonStandardInsurancePremiumFee (BigDecimal.ZERO);
        xtra.setRoofRackFee(BigDecimal.ZERO);
        xtra.setSatNavFee (BigDecimal.ZERO);
        xtra.setTowBarsFee(BigDecimal.ZERO);

        return xtra;
        
    }
    
    private IHireInfo getHireInfo(){
        
        HireInfo hireDetail = new HireInfo();
        hireDetail.setHireStart(getDefaultDate());        
        hireDetail.setHireEnd(getDefaultDatePlusSeven());
        hireDetail.setNumberOfHireDays(15);
        hireDetail.setVClass(F1());
        hireDetail.setIsTotalLoss(false);
        return hireDetail;
        
    }
    
    private IHireMonitoringDetail getHireMonitoringDetail(){
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        hireMonitoringDetail.setLabourCost(new BigDecimal(0));
        hireMonitoringDetail.setLabourHour(4);
        hireMonitoringDetail.setLabourRate(new BigDecimal(220));
        return hireMonitoringDetail;
    }

    private IClaimInfo getClaim(){
        
        ClaimInfo c = new ClaimInfo();
       
        c.setVClass(F1());
        
        InsurerInfo ins = new InsurerInfo(); 
        ins.setAdminHandlingCharge(new BigDecimal(100));
        c.setInsurer(ins);
        
        CHOrganisationInfo org = new CHOrganisationInfo();
        org.setIsDelegatedAuthority(false);
        c.setCHOrganisation(org);
        
        c.setCustomerVehicleDamage(getCVD());
        c.setChoBand(getCHOBand());
        c.setExtras(getExtras());
        c.setHireDetail(getHireInfo());
        c.setInvoice(getInvoice());
        c.setClaimEngineeringReport(getEngineerReport());
        c.setHireMonitoringDetail(getHireMonitoringDetail());
        return c;
        
    }
    
    private ICustomerVehicleDamageInfo getCVD(){
        
        CustomerVehicleDamageInfo cvd = new CustomerVehicleDamageInfo();
        cvd.setInitialECD(getDefaultDate());
        cvd.setIsUsable(false);
        return cvd;
        
    }
   
}