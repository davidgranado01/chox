package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.RepairDiaryInformationCheck;
import idas.chox.service.bre.rules.TotalLossDiaryInformationCheck;
import idas.chox.test.BaseTest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Rule107TotalLossDiaryInformationCheck extends BaseTest {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim( ClaimType type) {
        Claim claim = new Claim();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setClaimType(type);

        //All required HireMonitoring required fields set up
        claim.getHireMonitoringDetail().setInspectionBookedDate(new Date());
        claim.getHireMonitoringDetail().setInspectionDate(new Date());
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new Date());
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new Date());
        claim.getHireMonitoringDetail().setTotalLossOfferMadeDate(new Date());
        claim.getHireMonitoringDetail().setTotalLossOfferAcceptedDate(new Date());
        claim.getHireMonitoringDetail().setTotalLossOfferCheckIssuedDate(new Date());
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new Date());


        claim.getHireMonitoringDetail().setIsTotalLostCheck(true);

        //ask for the rule to e executed
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setTotalLossDiaryInfoCheck(true);

        return claim;
    }


    @Test
    public void testPassed_CHO_Simple() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
       TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        org.junit.Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_Insurer_Simple() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_INVOICE);
        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed_CHO_InspectionBookedDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setInspectionBookedDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_InspectionDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setInspectionDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_DateRepairAuthorised_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_DateEngineersReportSent_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setEngineersReportSentDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_DateTotalLossOfferMade_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setTotalLossOfferMadeDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_DateTotalLossOfferAccepted_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setTotalLossOfferAcceptedDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_DateTotalLossChequeIssued_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setTotalLossOfferCheckIssuedDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_DateTotalLossChequeReceived_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_CHO_DateTotalLossChequeReceived_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(null);

        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testSkipped_BRETotalLossDiaryInfoCheck_Disabled() throws IOException {
        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getBreBand().setTotalLossDiaryInfoCheck(false);
        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_claimType_not_GTA() throws IOException {
        Claim claim = getTestClaim(ClaimType.FIXED_FEE);
        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_CHO_isTotalLossNo() throws IOException {
        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setIsTotalLostCheck(false);
        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_Insurer_isTotalLossNo() throws IOException {
        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setIsTotalLostCheck(false);
        TotalLossDiaryInformationCheck rule = new TotalLossDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

}