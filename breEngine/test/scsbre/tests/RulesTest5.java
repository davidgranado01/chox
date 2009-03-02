/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests;


import java.math.BigDecimal;
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
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.*;
import scsbre.tests.sample.*;
import static org.junit.Assert.*;

/**
 *
 * @author Derm
 */
public class RulesTest5 {

    public RulesTest5() {
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
        
       // System.out.println(res.getResults().size());
        
        List<RuleEvaluation> results = res.getResults();
        for(int i = 0; i <= results.size()-1; i++){
            
            
            RuleEvaluation res1 = results.get(i);
            System.out.println(res1.toString());
        }
                
        InvoiceCalcHelper ic = InvoiceCalcHelper.getInstance(claim.getInvoice());
        
        System.out.println(claim.getInvoice().getHireNet());
        System.out.println(ic.getCalculatedHireGross());
        System.out.println(ic.getCalculatedHireVat());

        assertTrue(results.get(0).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(1).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(2).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(3).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(4).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(5).getResult() == RuleEvaluationResult.RuleSkipped);
        assertTrue(results.get(6).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(7).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(8).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(9).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(10).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(11).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(12).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(13).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(14).getResult() == RuleEvaluationResult.RuleSkipped);
        assertTrue(results.get(15).getResult() == RuleEvaluationResult.RuleSkipped);
        assertTrue(results.get(16).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(17).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(18).getResult() == RuleEvaluationResult.RulePassed);
        assertTrue(results.get(19).getResult() == RuleEvaluationResult.RuleSkipped);

        
        
        
        assertTrue(res.getStatus() == ClaimStatus.InvoiceApprovedByBRE);
        
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
        
        inv.setHireNet(new BigDecimal(720));
        inv.setHireVat(new BigDecimal(126));
        inv.setHireGross(new BigDecimal(846));
        
        inv.setRepairNet(new BigDecimal(500));
        inv.setRepairVat(new BigDecimal(87.5));
        inv.setRepairGross(new BigDecimal(587.5));

        inv.setEngineerFeeVat(new BigDecimal(0));
        inv.setEngineerFeeNet(new BigDecimal(0));
        inv.setEngineerFeeGross(new BigDecimal(0));

        inv.setStorageRecoveryGross(new BigDecimal(0));
        inv.setStorageRecoveryNet(new BigDecimal(0));
        inv.setStorageRecoveryVat(new BigDecimal(0));

        inv.setTotalNet(new BigDecimal(1120));
        inv.setTotalVat(new BigDecimal(196));
        inv.setTotalGross(new BigDecimal(1316));

        inv.setClaimsHandlingInvoiceAmount(new BigDecimal(100));
        inv.setDeductionForClaimsHandlingFee(new BigDecimal(-100));
        inv.setDiscount(new BigDecimal(0));
        inv.setTotalToPay(new BigDecimal(1316));

        return inv;
    }
    
    private ICHOBandInfo getCHOBand(){
        
        CHOBandInfo band = new CHOBandInfo();

        band.setHireNetCeiling(new BigDecimal(1000));
        band.setHireDayCeiling(30);
        band.setMaxRepairValue(BigDecimal.ZERO);

        band.setTakeVehicleToGarageDaysMobile(1);
        band.setTakeVehicleToGarageDaysNonMobile(3);        
        
        band.setWeekendBufferDays(0);
        band.setTakeVehicleOutDays(0);
        band.setEngineerInspectionDelayDays(0);

        band.setIsMobileDayAllowance(2);
        band.setIsNotMobileDayAllowance(9);

        band.setOfferMadeDays(7);
        band.setReceiptOfFinalStatementChequeDays(10);
        band.setInspectionDelayDays(4);
        band.setHireRateChargeTolerance(new BigDecimal(0.01));

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
        hireDetail.setHireEnd(getDefaultDate());
        hireDetail.setNumberOfHireDays(10);

        hireDetail.setVClass(F1());
        hireDetail.setIsTotalLoss(true);

        return hireDetail;
        
    }
    
    private IClaimInfo getClaim(){
        
        ClaimInfo c = new ClaimInfo();
       
        c.setVClass(F1());
        
        InsurerInfo ins = new InsurerInfo(); 
        ins.setAdminHandlingCharge(new BigDecimal(1));
        c.setInsurer(ins);
        
        CHOrganisationInfo org = new CHOrganisationInfo();
        org.setIsDelegatedAuthority(true);
        c.setCHOrganisation(org);
        
        
        c.setCustomerVehicleDamage(getCVD());
        c.setChoBand(getCHOBand());
        c.setExtras(getExtras());
        c.setHireDetail(getHireInfo());
        c.setInvoice(getInvoice());
        c.setClaimEngineeringReport(getEngineerReport());
        
        return c;
        
    }
    
    private ICustomerVehicleDamageInfo getCVD(){
        
        CustomerVehicleDamageInfo cvd = new CustomerVehicleDamageInfo();
        cvd.setInitialECD(getDefaultDate());
        cvd.setIsUsable(false);
        return cvd;
        
    }
    
    
    
   
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

}