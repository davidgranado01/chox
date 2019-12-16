package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.rules.TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck;
import idas.chox.test.BaseTest;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Rule104TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck extends BaseTest {

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
        claim.getBreBand().setTotalLossChoUnroadworthyCheck(true);
        claim.getVehicleHire().setHireStart(new Date());
        claim.getVehicleHire().setHireEnd(new Date());
        claim.getHireMonitoringDetail().setWhoIsSendingPav("CHO");
        claim.getCustomer().setIsUsable(false);
        claim.setManagingRepair(true);
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new Date());

        setDefaultDates(claim);

        claim.getBreBand().setTimeToInstructEngineer4(10);
        claim.getBreBand().setTimeToInspect4(10);
        claim.getBreBand().setTimeToAuthoriseRepair4(10);
        claim.getBreBand().setTimeToSubmittEngineersReport4(10);
        claim.getBreBand().setTimeToOffHire4(10);
        claim.getBreBand().setTotalAllowableDays4(10);

        return claim;
    }

    private void setDefaultDates(Claim claim){
        try{
            claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-01"));
            claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-02"));
            claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-03"));
            claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
            claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-05"));
            claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));
            claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
            claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {
        Claim claim = getTestClaim();
        claim.getBreBand().setTotalLossChoUnroadworthyCheck(false);
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
    }

    
    @Test
    public void testSkipped_ClaimTypeSubscriber() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeFixedFee() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.FIXED_FEE);
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ClaimTypeCollaboration() throws IOException {
        Claim claim = getTestClaim();
        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_ManagingRepairFalse() throws IOException {
        Claim claim = getTestClaim();

        claim.setManagingRepair(false);
        
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_IsUsableTrue() throws IOException {
        Claim claim = getTestClaim();

        claim.getCustomer().setIsUsable(true);
        
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_WhoIsSendingPav() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setWhoIsSendingPav("At Fault Insurer");
        
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());

        claim.getHireMonitoringDetail().setWhoIsSendingPav("Customers Own Insurer");
        
        rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoHireEnd() throws IOException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireEnd(null);
        
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoHireStart() throws IOException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(null);
        
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoRepairAuthorised() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setRepairAuthorisedDate(null);

        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoTLOfferCheck() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(null);

        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }
    @Test
    public void testSkipped_NoInspection() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setInspectionDate(null);

        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }
    @Test
    public void testSkipped_NoInspectionBooked() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setInspectionBookedDate(null);

        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }
    @Test
    public void testSkipped_NoPolicyHolderContact() throws IOException {
        Claim claim = getTestClaim();

        claim.setPolicyHolderContactDate(null);

        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_NoEngineerReportSent() throws IOException {
        Claim claim = getTestClaim();

        claim.getHireMonitoringDetail().setEngineersReportSentDate(null);

        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_HireStartBefore() throws IOException, ParseException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-30"));
        
        RuleEvaluation rv = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck().applyToClaim(claim);
        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testPassed_HireStartJulyFirst() throws IOException, ParseException {
        Claim claim = getTestClaim();

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-01"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
    
    @Test
    public void testPassed_OverWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair3(1);
        claim.getBreBand().setTimeToSubmittEngineersReport3(1);
        claim.getBreBand().setTimeToOffHire3(1);
        claim.getBreBand().setTotalAllowableDays3(1);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-01"));                            //Friday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-02"));        //Saturday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-03"));     //Sunday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                              //Monday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);
        
        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_OverWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair3(3);
        claim.getBreBand().setTimeToSubmittEngineersReport3(3);
        claim.getBreBand().setTimeToOffHire3(3);
        claim.getBreBand().setTotalAllowableDays3(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-06"));                            //Wednesday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));        //Thursday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-07-08"));     //Friday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));                              //Sunday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_HireStartToHireEnd() throws IOException, ParseException {
        //3 working days between Hire Start and Hire End. All conditions met

        Claim claim = getTestClaim();
        claim.getBreBand().setTotalAllowableDays4(7);  //Sets Hire Star to Hire End allowed days so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                                        //Monday
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                                          //Friday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_HireStartToHireEnd() throws IOException, ParseException {
        //4 working days between Hire Start and Hire End. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTotalAllowableDays4(3);  //Sets Hire Star to Hire End allowed days so that it exceeds the limit


        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                                        //Monday
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                                          //Friday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_PolicyHolderContactToInspectionBooked() throws IOException, ParseException {
        //3 working days between PolicyHolder Contact Date to Inspection Booked Date. All conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(4); //Set PolicyHolder Contact Date to Inspection Booked Date so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                                           //Monday
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));                    //Thursday
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_PolicyHolderContactToInspectionBooked() throws IOException, ParseException {
        //4 working days between PolicyHolder Contact Date to Inspection Booked Date. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(3); //Set PolicyHolder Contact Date to Inspection Booked Date so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                                           //Monday
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                    //Friday
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_HireStartToDateRepairAuthorised() throws IOException, ParseException {
        //3 working days between Start Date to Date Repair Authorised. All Conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair4(3); //Set Hire Start to Date Repair Authorised so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                                        //Monday
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));                    //Thursday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_HireStartToDateRepairAuthorised() throws IOException, ParseException {
        //4 working days between Start Date to Date Repair Authorised. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToAuthoriseRepair4(3); //Set Hire Start to Date Repair Authorised so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                                        //Monday
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                    //Friday
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_InspectionBookedDateToInspectionDate() throws IOException, ParseException {
        //3 working days between Inspection Booked Date and Inspection Date. All conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInspect4(3); //Set Inspection Booked Date to Inspection Date so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                    //Monday
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));                          //Thursday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_InspectionBookedDateToInspectionDate() throws IOException, ParseException {
        //4 working days between Inspection Booked Date and Inspection Date. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInspect4(3); //Set Inspection Booked Date to Inspection Date so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                    //Monday
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                          //Friday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                            //Friday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_ChequeReceivedToHireEnd() throws IOException, ParseException {
        //3 working days between Total Loss Offer Cheque received and Hire End. All conditions met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToOffHire4(7);  //Sets Total Loss Offer Cheque received to Hire End days so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));         //Monday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                                          //Friday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_ChequeReceivedToHireEnd() throws IOException, ParseException {
        //3 working days between Total Loss Offer Cheque received and Hire End.  Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToOffHire4(3);  //Sets Total Loss Offer Cheque received to Hire End days so that it exceeds the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));         //Monday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                                          //Friday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_InspectionDateToEngineersReportSent() throws IOException, ParseException {
        //3 working days between Inspection Date and Engineers Report Sent Date. All conditions met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToSubmittEngineersReport4(7);  //Sets Inspection Date to Engineers Report Sent Date days so that it is within the limit

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                          //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));                 //Thursday
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_testPassed_InspectionDateToEngineersReportSent() throws IOException, ParseException {
        //4 working days between Inspection Date and Engineers Report Sent Date. Rest of the conditions are met

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToSubmittEngineersReport4(3);  //Sets Inspection Date to Engineers Report Sent Date days so that it exceeds the limit


        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));                          //Monday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                 //Friday
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_OverWeekend1() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(1);
        claim.getBreBand().setTimeToInspect4(1);
        claim.getBreBand().setTimeToAuthoriseRepair4(1);
        claim.getBreBand().setTimeToSubmittEngineersReport4(1);
        claim.getBreBand().setTimeToOffHire4(1);
        claim.getBreBand().setTotalAllowableDays4(1);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                                        //Friday
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));                          //Saturday
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-10"));                 //Sunday
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));         //Monday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-12"));                                          //Tuesday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_OverWeekend2() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(3);
        claim.getBreBand().setTimeToInspect4(3);
        claim.getBreBand().setTimeToAuthoriseRepair4(3);
        claim.getBreBand().setTimeToSubmittEngineersReport4(3);
        claim.getBreBand().setTimeToOffHire4(3);
        claim.getBreBand().setTotalAllowableDays4(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));                                        //Friday
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));                                           //Saturday
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-09"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-10"));                 //Sunday
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-11"));         //Monday
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-14"));                                          //Thursday

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testPassed_bankHoliday1() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(3);
        claim.getBreBand().setTimeToInspect4(3);
        claim.getBreBand().setTimeToAuthoriseRepair4(3);
        claim.getBreBand().setTimeToSubmittEngineersReport4(3);
        claim.getBreBand().setTimeToOffHire4(3);
        claim.getBreBand().setTotalAllowableDays4(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-06"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed_bankHoliday1() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(3);
        claim.getBreBand().setTimeToInspect4(3);
        claim.getBreBand().setTimeToAuthoriseRepair4(3);
        claim.getBreBand().setTimeToSubmittEngineersReport4(3);
        claim.getBreBand().setTimeToOffHire4(3);
        claim.getBreBand().setTotalAllowableDays4(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-30"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-03"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-08"));

        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }

    @Test
    public void testFailed_bankHoliday2() throws IOException, ParseException {
        // NB/ Bank Holiday on 02/12/2019 (from import script)

        Claim claim = getTestClaim();
        claim.getBreBand().setTimeToInstructEngineer4(3);
        claim.getBreBand().setTimeToInspect4(3);
        claim.getBreBand().setTimeToAuthoriseRepair4(3);
        claim.getBreBand().setTimeToSubmittEngineersReport4(3);
        claim.getBreBand().setTimeToOffHire4(3);
        claim.getBreBand().setTotalAllowableDays4(3);

        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.setPolicyHolderContactDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.getHireMonitoringDetail().setInspectionBookedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.getHireMonitoringDetail().setInspectionDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-26"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getHireMonitoringDetail().setEngineersReportSentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getHireMonitoringDetail().setTotalLossOfferCheckReceivedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-29"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-12-05"));


        TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck rule = new TotalLossPeriodWhereCHOSendingAndDealingPAVAndVehicleNotRoadworthyCheck();
        rule.setBankHolidayService(bankHolidayService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        Assert.assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The hire period for a total loss exceeds the number of days allowed where the CHO is sending PAV and the vehicle is not roadworthy."));
        Assert.assertFalse(rv.getIsVisibleToCHO());
    }
}
