package idas.chox.bre;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.test.BaseTest;
import idas.chox.service.bre.rules.RepairDiaryInformationCheck;


public class Rule108RepairDiaryInformationCheck extends BaseTest {

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
        claim.getHireMonitoringDetail().setRepairBookInDate(new Date());
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new Date());
        claim.getHireMonitoringDetail().setRepairCommencedDate(new Date());
        claim.getHireMonitoringDetail().setRepairCompletionDate(new Date());
        claim.getHireMonitoringDetail().setIsTotalLostCheck(false);

        //ask for the rule to e executed
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setRepairDiaryInfoCheck(true);

        return claim;
    }


    @Test
    public void testPassed_CHO_GTA() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setRepairCompletionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_Insurer_INSURER_INVOICE() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_INVOICE);
        claim.getHireMonitoringDetail().setRepairCompletionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_CHO_TPI() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getHireMonitoringDetail().setRepairCompletionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }



    @Test
    public void testFailed_CHO_InspectionBookedDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setInspectionBookedDate(null);

        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_Insurer_InspectionBookedDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setInspectionBookedDate(null);

        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_RepairBookInDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setRepairBookInDate(null);

        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_RepairRepairAuthorisedDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(null);

        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_RepairCommencedDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setRepairCommencedDate(null);


        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_RepairCompletionDate_Missing() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setRepairCompletionDate(null);

        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testSkipped_BRERepairDiaryInfoCheck_Disabled() throws IOException {
        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getBreBand().setRepairDiaryInfoCheck(false);
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_claimType_FixedFee_notGTA() throws IOException {
        Claim claim = getTestClaim(ClaimType.FIXED_FEE);
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_claimType_Subscriber_notGTA() throws IOException {
        Claim claim = getTestClaim(ClaimType.SUBSCRIBER);
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_claimType_Collaboration_notGTA() throws IOException {
        Claim claim = getTestClaim(ClaimType.COLLABORATION_PROTOCOL);
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_cho_isTotalLossYes() throws IOException {
        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getHireMonitoringDetail().setIsTotalLostCheck(true);
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }
    @Test
    public void testSkipped_insurer_isTotalLossYes() throws IOException {
        Claim claim = getTestClaim(ClaimType.INSURER_UPLOAD);
        claim.getHireMonitoringDetail().setIsTotalLostCheck(true);
        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }



}
