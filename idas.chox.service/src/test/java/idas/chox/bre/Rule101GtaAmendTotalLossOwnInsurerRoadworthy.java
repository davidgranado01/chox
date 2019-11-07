package idas.chox.bre;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.GtaAmendTotalLossOwnInsurerRoadworthy;
import idas.chox.test.BaseTest;


public class Rule101GtaAmendTotalLossOwnInsurerRoadworthy extends BaseTest {

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
        
        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setTotalLossOwnRoadworthyCheck(true);
        claim.getVehicleHire().setHireStart(new Date());
        claim.getVehicleHire().setHireEnd(new Date());
        claim.getHireMonitoringDetail().setWhoIsSendingPav("Customers Own Insurer");
        claim.getCustomer().setIsUsable(true);
        claim.setManagingRepair(false);
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new Date());
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setTotalLossOwnRoadworthyCheck(false);
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
    }

    
    @Test
    public void testSkipped_ClaimTypeSubscriber() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeFixedFee() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.FIXED_FEE);
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeCollaboration() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ManagingRepairTrue() throws IOException {
        Claim claim = getTestClaim();

        claim.setManagingRepair(true);
        
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_IsUsableFalse() throws IOException {
        Claim claim = getTestClaim();

        claim.getCustomer().setIsUsable(false);
        
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_WhoIsSendingPav() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setWhoIsSendingPav("At Fault Insurer");
        
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());

        claim.getHireMonitoringDetail().setWhoIsSendingPav("CHO");
        
        rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoHireEnd() throws IOException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireEnd(null);
        
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }


    @Test
    public void testSkipped_NoHireStart() throws IOException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(null);
        
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_HireStartBefore() throws IOException, ParseException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-30"));
        
        RuleEvaluation rv = new GtaAmendTotalLossOwnInsurerRoadworthy().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testPassed_Simple() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testPassed_OverWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-01"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testPassed_OverWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testFailed_HireStartToDateRepairAuthorised() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_DateRepairAuthorisedToHireEnd() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_OverWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-01"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testFailed_OverWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-12"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
    
    // TODO: add tests with hire start, hire-end, and repair authorisation date falling on a weekend
    @Test
    public void testPassed_OnWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-12"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-12"));
        rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testPassed_OnWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_OnWeekend3() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-02")); //Saturday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testPassed_OnWeekend4() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09")); //Saturday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-12"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testFailed_OnWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-02")); //Saturday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
    
    @Test
    public void testFailed_OnWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09")); //Saturday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-13"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
    
    // TODO: add pass/fail tests with bank holidays included (need to populate H2 bank_holidays table first, in import script)
    @Test
    public void testPassed_bankHoliday1() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)
        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-05"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-06"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_bankHoliday2() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)
        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed_bankHoliday1() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)
        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-06"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-07"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_bankHoliday2() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)
        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair1(3);
        claim.getBreBand().setTimeToOffHire1(1);
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-04"));

        GtaAmendTotalLossOwnInsurerRoadworthy rule = new GtaAmendTotalLossOwnInsurerRoadworthy();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the claimants own insurer is dealing and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
}
