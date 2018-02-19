package idas.chox.bre;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import static org.junit.Assert.*;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.ImpecuniousCheck;
import idas.chox.test.BaseTest;

public class Rule097ImpecuniousCheckTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    private Claim getTestClaim() {
        Claim claim = new Claim();
        try {
            claim.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy").parse("05-07-2016"));
            claim.setClaimType(ClaimType.INSURER_UPLOAD);
            claim.setInsurer(testClaim.getTestInsurer());
            claim.getInsurer().setEnableManualLouDates(true);
            claim.getInsurer().setEnableLouDates(true);
            claim.setChorganisation(testClaim.getTestChorganisation());
            claim.setBreBand(testClaim.getTestBreBand());
            claim.getBreBand().setImpecuniousCheck(true);
            claim.getBreBand().setImpecuniousStartDate(new SimpleDateFormat("dd-MM-yyyy").parse("05-06-2016"));
            claim.setEngineerReport(testClaim.getTestEngineeringReport());
            claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
            claim.setVehicleHire(testClaim.getTestHireDetail());
            claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
            claim.setInsurerHireMonitoringDetail(testClaim.getTestInsurerHireMonitoringDetail());
            claim.setInvoice(testClaim.getTestInvoice());
            claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());
            
        } catch (ParseException ex) {
        }
        return claim;
    }

    
    @Test
    public void testSkipped_ruleNotActivated() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setImpecuniousCheck(false);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testSkipped_louDatesNotActivated() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.GTA);
        claim.getInsurer().setEnableLouDates(false);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testSkipped_ManualLouDatesNotActivated() throws IOException {
        Claim claim = getTestClaim();
        claim.getInsurer().setEnableManualLouDates(false);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_noInsurerHireMonitoring() throws IOException {
        Claim claim = getTestClaim();
        claim.setInsurerHireMonitoringDetail(null);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Impecunious() throws IOException {
        Claim claim = getTestClaim();
        claim.getInsurerHireMonitoringDetail().setClaimantImpecunious(Boolean.TRUE);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    
    @Test
    public void testPassed_notImpecunious() throws IOException {
        Claim claim = getTestClaim();
        claim.getInsurerHireMonitoringDetail().setClaimantImpecunious(Boolean.FALSE);
        claim.getInvoice().setHireGross(BigDecimal.ZERO);
        claim.getInvoice().setRepairGross(BigDecimal.ZERO);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_beforeDate() throws IOException {
        Claim claim = getTestClaim();
        try {
            claim.setCreatedDate(new SimpleDateFormat("dd-MM-yyyy").parse("05-05-2016"));
        } catch (ParseException ex) {
        }
        claim.getInsurerHireMonitoringDetail().setClaimantImpecunious(Boolean.FALSE);
        claim.getInvoice().setHireGross(BigDecimal.TEN);
        claim.getInvoice().setRepairGross(BigDecimal.TEN);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_chargingHire() throws IOException {
        Claim claim = getTestClaim();
        claim.getInsurerHireMonitoringDetail().setClaimantImpecunious(Boolean.FALSE);
        claim.getInvoice().setHireGross(BigDecimal.TEN);
        claim.getInvoice().setRepairGross(BigDecimal.ZERO);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is claiming for Hire and/or Repair and the claimant is flagged as being pecunious."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
    
    
    @Test
    public void testFailed_chargingRepair() throws IOException {
        Claim claim = getTestClaim();
        claim.getInsurerHireMonitoringDetail().setClaimantImpecunious(Boolean.FALSE);
        claim.getInvoice().setHireGross(BigDecimal.ZERO);
        claim.getInvoice().setRepairGross(BigDecimal.TEN);
        ImpecuniousCheck rule = new ImpecuniousCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is claiming for Hire and/or Repair and the claimant is flagged as being pecunious."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }
}
