package idas.chox.bre;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.GtaAmendUploadCheck;


public class Rule100GtaAmendUploadCheckTest extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

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

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setUpload414Check(false);
        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
    }

    
    @Test
    public void testSkipped_ClaimTypeSubscriber() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setUpload414Check(true);
        claim.getCustomer().setIsTotalLoss(true);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeFixedFee() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setUpload414Check(true);
        claim.getCustomer().setIsTotalLoss(true);
        claim.setClaimType(ClaimType.FIXED_FEE);
        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeCollaboration() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setUpload414Check(true);
        claim.getCustomer().setIsTotalLoss(true);
        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_TotalLossIsFalse() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setUpload414Check(true);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(false);
        
        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testPassed_HireStartBefore() throws IOException, ParseException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setUpload414Check(true);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-30"));
        claim.getHireMonitoringDetail().setWhoIsSendingPav("CHO");
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new Date());

        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());

    }

    @Test
    public void testPassed_HireStartAfter() throws IOException, ParseException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setUpload414Check(true);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setWhoIsSendingPav("CHO");
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new Date());

        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());

    }

    @Test
    public void testFailed_PAVNull() throws IOException, ParseException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setUpload414Check(true);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new Date());

        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Correct Managing Repair information in support of GTA 4.14 not provided."));
        assertFalse(rv.getIsVisibleToCHO());

    }


    @Test
    public void testFailed_EngineersReportSentDateNull() throws IOException, ParseException {

        Claim claim = getTestClaim();

        // SET BRE BAND
        claim.getBreBand().setUpload414Check(true);

        // SET HIRE DETAIL
        claim.getCustomer().setIsTotalLoss(true);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setWhoIsSendingPav("CHO");

        RuleEvaluation rv = new GtaAmendUploadCheck().applyToClaim(claim);
        
        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Correct Managing Repair information in support of GTA 4.14 not provided."));
        assertFalse(rv.getIsVisibleToCHO());

    }

}
