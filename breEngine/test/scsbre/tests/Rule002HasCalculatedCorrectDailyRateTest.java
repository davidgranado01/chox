package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HasCalculatedCorrectDailyRate;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule002HasCalculatedCorrectDailyRateTest extends TestCase {

TestClaim testClaim = new TestClaim();

    @BeforeClass
    public static void setUpClass() throws Exception {}

    @AfterClass
    public static void tearDownClass() throws Exception {}

    @Test
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

        // SET EXTRAS FOR TESTING
        claim.getExtras().setAdminFee(new BigDecimal(30));
        claim.getExtras().setAdminQty(0);
        claim.getExtras().setAutomaticFee(new BigDecimal(10));
        claim.getExtras().setAutomaticQty(0);
        claim.getExtras().setBabySeatFee(new BigDecimal(0));
        claim.getExtras().setBabySeatQty(0);
        claim.getExtras().setCdwFee(new BigDecimal(0));
        claim.getExtras().setCdwQty(0);
        claim.getExtras().setEstateFee(new BigDecimal(0));
        claim.getExtras().setEstateQty(0);
        claim.getExtras().setDeliveryCollectionFee(new BigDecimal(0));
        claim.getExtras().setDeliveryCollectionQty(0);
        claim.getExtras().setDualControlFee(new BigDecimal(0));
        claim.getExtras().setDualControlQty(0);
        claim.getExtras().setRoofRackFee(new BigDecimal(0));
        claim.getExtras().setRoofRackQty(0);
        claim.getExtras().setSatNavFee(new BigDecimal(0));
        claim.getExtras().setSatNavQty(0);
        claim.getExtras().setTowBarsFee(new BigDecimal(0));
        claim.getExtras().setTowBarsQty(0);
        claim.getExtras().setNonStandardInsurancePremiumFee(new BigDecimal(0));
        claim.getExtras().setNonStandardInsurancePremiumQty(0);

        // SET HIRE DETAIL
        claim.getHireDetail().setDays(4);


        
        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasCalculatedCorrectDailyRate(false);
        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {

        /*
         * CHO Control Flag is ON
         * Claim. V CLass is Null
         */

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);
        claim.setVClass(null);

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Vehicle Hire vehicle class is not specified."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_1() throws IOException {

        // getDailyHireRateCharged EQUALS TO allowedDailyRate

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);

        claim.getHireDetail().getVehicleClass().setCode("SP1");
        claim.getHireDetail().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(330.98));
        
        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        /*
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        BigDecimal allowedDailyRate = claim.getHireDetail().getVClass().getPrice().add(claim.getChoBand().getHireRateChargeTolerance());
        System.out.println("allowedDailyRate: "+allowedDailyRate);
        System.out.println("getDailyHireRateCharged:"+cCalc.getDailyHireRateCharged());
        System.out.println("RESULT:"+cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate));
        System.out.println("RESULT:"+(cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate) <= 0));
        System.out.println(rv.getResult());
        */
         
        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:72.7450
         * getDailyHireRateCharged EQUALS allowedDailyRate
         * Result:PASSED
         */
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_2() throws IOException {

        // getDailyHireRateCharged LESS THAN allowedDailyRate

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);

        claim.getHireDetail().getVehicleClass().setCode("SP1");
        claim.getHireDetail().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(330.90));

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);

        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:72.7249
         * getDailyHireRateCharged LESS THAN allowedDailyRate
         * Result:PASSED
         */
        
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    @Test
    public void testFailed() throws IOException {

        // getDailyHireRateCharged MORE THAN allowedDailyRate

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHasAllowedVehicleClass(true);

        claim.getHireDetail().getVehicleClass().setCode("SP1");
        claim.getHireDetail().getVehicleClass().setPrice(new BigDecimal("69.7450"));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(360.90));

        RuleEvaluation rv = new HasCalculatedCorrectDailyRate().applyToClaim(claim);
        

        /*
         * allowedDailyRate: 72.7450
         * getDailyHireRateCharged:80.2249
         * getDailyHireRateCharged MORE THAN allowedDailyRate
         * Result:FAILED
         */
        
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Daily rate billed for replacement vehicle class exceeds ABI rate."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
}
