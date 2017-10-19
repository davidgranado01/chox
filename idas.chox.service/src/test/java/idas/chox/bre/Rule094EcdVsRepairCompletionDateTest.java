package idas.chox.bre;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

import idas.chox.test.BaseTest;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.EcdVsRepairCompletionDate;


public class Rule094EcdVsRepairCompletionDateTest extends BaseTest {
    MockObjects testClaim = new MockObjects();

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }

    private Claim getTestClaim() {
        Claim claim = new Claim();
        claim.setId(-1);
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
    public void testSkipped_1() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setEcdVsRepairCompletionDateCheck(false);
        
        EcdVsRepairCompletionDate rule = new EcdVsRepairCompletionDate();
        rule.setHireMonitoringEcdService(hireMonitoringEcdService);
        
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testSkipped_2() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setEcdVsRepairCompletionDateCheck(true);
        claim.setHireMonitoringDetail(null);

        EcdVsRepairCompletionDate rule = new EcdVsRepairCompletionDate();
        rule.setHireMonitoringEcdService(hireMonitoringEcdService);
        
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testSkipped_3() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setEcdVsRepairCompletionDateCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(null);

        EcdVsRepairCompletionDate rule = new EcdVsRepairCompletionDate();
        rule.setHireMonitoringEcdService(hireMonitoringEcdService);
        
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testSkipped_4() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setEcdVsRepairCompletionDateCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.parse("27/02/2011"));
        claim.getCustomer().setInitialECD(null);
        claim.setHireMonitoringEcds(null);
        
        EcdVsRepairCompletionDate rule = new EcdVsRepairCompletionDate();
        rule.setHireMonitoringEcdService(hireMonitoringEcdService);
        
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testPassed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setEcdVsRepairCompletionDateCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.parse("27/02/2011"));
        claim.getCustomer().setInitialECD(DateHelper.parse("29/02/2011"));
        claim.setHireMonitoringEcds(null);

        EcdVsRepairCompletionDate rule = new EcdVsRepairCompletionDate();
        rule.setHireMonitoringEcdService(hireMonitoringEcdService);
        
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }

    
    @Test
    public void testFailed() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setEcdVsRepairCompletionDateCheck(true);
        claim.getHireMonitoringDetail().setRepairCompletionDate(DateHelper.parse("28/02/2011"));
        claim.getCustomer().setInitialECD(DateHelper.parse("20/02/2011"));
        claim.setHireMonitoringEcds(null);

        EcdVsRepairCompletionDate rule = new EcdVsRepairCompletionDate();
        rule.setHireMonitoringEcdService(hireMonitoringEcdService);

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());

        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Repair Completion Date is after the latest ECD as updated by the CHO, review discrepancy."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());
    }
}
