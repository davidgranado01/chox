package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.LabourCostBusinessRule;
import idas.chox.service.bre.util.ClaimCalcHelper;
import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule022LabourCostBusinessRuleTest extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim() {

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
        claim.getBreBand().setLabourCostBusinessRule(false);
        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_NotRequiredToValidateByBRE_HireMonitoringDetailIsNull() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        claim.setHireMonitoringDetail(null);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Insufficient information to perform labour cost rule."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_NotRequiredToValidateByBRE_HireMonitoringDetailNotNull() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        claim.getHireMonitoringDetail().setLabourCost(BigDecimal.ZERO);
        claim.getHireMonitoringDetail().setLabourHour(BigDecimal.ZERO);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Insufficient information to perform labour cost rule."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        // SET CHO BAND
        claim.getBreBand().setAverageLabourRateStandard(4);
        claim.getBreBand().setAverageLabourRatePrestige(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.getCustomer().setInitialECD(DateHelper.parse("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomer().setIsUsable(true);

        // HIRE DAYs
        claim.getVehicleHire().setDays(7);

        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Less() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        // SET CHO BAND
        claim.getBreBand().setAverageLabourRateStandard(4);
        claim.getBreBand().setAverageLabourRatePrestige(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.getCustomer().setInitialECD(DateHelper.parse("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomer().setIsUsable(true);

        // HIRE DAYs
        claim.getVehicleHire().setDays(6);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailled() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setLabourCostBusinessRule(true);

        // SET CHO BAND
        claim.getBreBand().setAverageLabourRateStandard(4);
        claim.getBreBand().setAverageLabourRatePrestige(4);
        claim.getBreBand().setAverageLabourHoursPerHireDay(2);

        // SET HIRE MONITORING
        claim.getHireMonitoringDetail().setLabourCost(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourHour(new BigDecimal("4"));
        claim.getHireMonitoringDetail().setLabourRate(new BigDecimal("10"));

        // SET ECD
        claim.getCustomer().setInitialECD(DateHelper.parse("01/10/2009"));

        // SET CUSTOMER DAMAGA
        claim.getCustomer().setIsUsable(true);

        // HIRE DAYs
        claim.getVehicleHire().setDays(8);

        RuleEvaluation rv = new LabourCostBusinessRule().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of hire days billed by the CHO (8 days) is not relative to the number of expected hire days (7 days) based on the labour information provided."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
}
