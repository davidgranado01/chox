package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.LabourCostBusinessRule;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.engine.util.DateHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule022LabourCostBusinessRuleTest extends TestCase {

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
        claim.getBreBand().setLabourCostBusinessRule(false);
        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_NotRequiredToValidateByBRE_HireMonitoringDetailIsNull() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        claim.setHireMonitoringDetail(null);
                
        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Insufficient information to perform labour cost rule."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_NotRequiredToValidateByBRE_HireMonitoringDetailNotNull() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        claim.getHireMonitoringDetail().setLabourCost(BigDecimal.ZERO);
        claim.getHireMonitoringDetail().setLabourHour(0);
        
        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Insufficient information to perform labour cost rule."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        // SET CHO BAND
        claim.getBreBand().setAverageLabourRate(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.setHireMonitoringEcd(DateHelper.getDateFromString("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(true);

        // HIRE DAYs
        claim.getHireDetail().setDays(7);

        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        
        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed_Less() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        // SET CHO BAND
        claim.getBreBand().setAverageLabourRate(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.setHireMonitoringEcd(DateHelper.getDateFromString("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(true);

        // HIRE DAYs
        claim.getHireDetail().setDays(6);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        // SET CHO BAND
        claim.getBreBand().setAverageLabourRate(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourHour(4);
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.setHireMonitoringEcd(DateHelper.getDateFromString("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomerVehicleDamage().setIsUsable(true);

        // HIRE DAYs
        claim.getHireDetail().setDays(8);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of hire days billed by the CHO is not relative to the number of expected hire days based on the labour information provided."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalated);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
