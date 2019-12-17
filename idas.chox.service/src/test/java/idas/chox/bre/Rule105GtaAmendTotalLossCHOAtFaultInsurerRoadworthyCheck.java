package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck;
import idas.chox.test.BaseTest;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Rule105GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck extends BaseTest {

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
        claim.getBreBand().setTotalLossChoAtFaultRoadworthyCheck(true);
        claim.getVehicleHire().setHireStart(new Date());
        claim.getVehicleHire().setHireEnd(new Date());
        claim.getHireMonitoringDetail().setWhoIsSendingPav("At Fault Insurer");
        claim.getCustomer().setIsUsable(true);
        claim.setManagingRepair(true);
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new Date());

        setDefaultDates(claim);

        claim.getBreBand().setTimeToAuthoriseRepair5(10);
        claim.getBreBand().setTimeToSubmittEngineersReport5(10);
        claim.getBreBand().setTimeToOffHire5(10);
        claim.getBreBand().setTotalAllowableDays5(10);

        return claim;
    }

    private void setDefaultDates(Claim claim){
        try{
            claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-01"));
            claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
            claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
            claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setTotalLossChoAtFaultRoadworthyCheck(false);
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
    }

    
    @Test
    public void testSkipped_ClaimTypeSubscriber() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeFixedFee() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.FIXED_FEE);
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeCollaboration() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ManagingRepairFalse() throws IOException {
        Claim claim = getTestClaim();

        claim.setManagingRepair(false);
        
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_IsUsableFalse() throws IOException {
        Claim claim = getTestClaim();

        claim.getCustomer().setIsUsable(false);
        
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_WhoIsSendingPav() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setWhoIsSendingPav("CHO");
        
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());

        claim.getHireMonitoringDetail().setWhoIsSendingPav("Customers Own Insurer");
        
        rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoHireEnd() throws IOException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireEnd(null);
        
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoHireStart() throws IOException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(null);
        
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoRepairAuthorised() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setRepairAuthorisedDate(null);

        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoEngineerReportSent() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setEngineersReportSentDate(null);

        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_HireStartBefore() throws IOException, ParseException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-30"));
        
        RuleEvaluation rv = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testPassed_HireStartJulyFirst() throws IOException, ParseException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-03"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-03"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-06"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testPassed_OverWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(1);
        claim.getBreBand().setTimeToSubmittEngineersReport5(1);
        claim.getBreBand().setTimeToOffHire5(1);
        claim.getBreBand().setTotalAllowableDays5(1);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-01"));                            //Friday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-02"));        //Saturday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-03"));     //Sunday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                              //Monday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_OverWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3);
        claim.getBreBand().setTimeToSubmittEngineersReport5(3);
        claim.getBreBand().setTimeToOffHire5(3);
        claim.getBreBand().setTotalAllowableDays5(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));                            //Wednesday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));        //Thursday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-08"));     //Friday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));                              //Sunday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_HireStartToHireEnd() throws IOException, ParseException {
        //3 working days between Hire Start and Hire End. All conditions met

        Claim claim = getTestClaim();
        claim.getBreBand().setTotalAllowableDays5(3);  //Sets Hire Star to Hire End allowed days so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                            //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));                              //Thursday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_HireStartToHireEnd() throws IOException, ParseException {
        //4 working days between Hire Start and Hire End. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTotalAllowableDays5(3);  //Sets Hire Star to Hire End allowed days so that it exceeds the limit


        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                            //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                              //Friday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_DateRepairAuthorisedToEngineersReportSent() throws IOException, ParseException {
        //3 working days between Date Repair Authorised to Date Engineers Report sent. All conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToSubmittEngineersReport5(3); //Set Date Repair Authorised to Date Engineers Report sent so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                            //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));     //Tuesday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_DateRepairAuthorisedToEngineersReportSent() throws IOException, ParseException {
        //4 working days between Date Repair Authorised to Date Engineers Report sent. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToSubmittEngineersReport5(3); //Set Date Repair Authorised to Date Engineers Report sent so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                            //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));     //Friday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_HireStartToDateRepairAuthorised() throws IOException, ParseException {
        //3 working days between Start Date to Date Repair Authorised. All Conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3); //Set Hire Start to Date Repair Authorised so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                            //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-05"));        //Tuesday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_HireStartToDateRepairAuthorised() throws IOException, ParseException {
        //4 working days between Start Date to Date Repair Authorised. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3); //Set Hire Start to Date Repair Authorised so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                            //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));        //Friday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_DateRepairAuthorisedToHireEnd() throws IOException, ParseException {
        //3 working days between Date Repair Authorised to Hire End. All conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToOffHire5(3); //Set Date Repair Authorised to Hire End so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));        //Monday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));                              //Thursday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_DateRepairAuthorisedToHireEnd() throws IOException, ParseException {
        //4 working days between Date Repair Authorised to Hire End. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToOffHire5(3); //Set Date Repair Authorised to Hire End so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));        //Monday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                              //Friday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_OverWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(1);
        claim.getBreBand().setTimeToSubmittEngineersReport5(1);
        claim.getBreBand().setTimeToOffHire5(1);
        claim.getBreBand().setTotalAllowableDays5(1);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                            //Friday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));        //Saturday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-10"));     //Sunday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-12"));                              //Tuesday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_OverWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3);
        claim.getBreBand().setTimeToSubmittEngineersReport5(3);
        claim.getBreBand().setTimeToOffHire5(3);
        claim.getBreBand().setTotalAllowableDays5(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                            //Friday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));        //Saturday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-10"));     //Sunday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-14"));                              //Thursday

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_bankHoliday1() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3);
        claim.getBreBand().setTimeToSubmittEngineersReport5(3);
        claim.getBreBand().setTimeToOffHire5(3);
        claim.getBreBand().setTotalAllowableDays5(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-06"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_bankHoliday2() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3);
        claim.getBreBand().setTimeToSubmittEngineersReport5(3);
        claim.getBreBand().setTimeToOffHire5(3);
        claim.getBreBand().setTotalAllowableDays5(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed_bankHoliday1() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3);
        claim.getBreBand().setTimeToSubmittEngineersReport5(3);
        claim.getBreBand().setTimeToOffHire5(3);
        claim.getBreBand().setTotalAllowableDays5(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-06"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-08"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_bankHoliday2() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair5(3);
        claim.getBreBand().setTimeToSubmittEngineersReport5(3);
        claim.getBreBand().setTimeToOffHire5(3);
        claim.getBreBand().setTotalAllowableDays5(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-05"));

        GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck rule = new GtaAmendTotalLossCHOAtFaultInsurerRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the at-fault insurer is sending PAV and the vehicle is roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
}
