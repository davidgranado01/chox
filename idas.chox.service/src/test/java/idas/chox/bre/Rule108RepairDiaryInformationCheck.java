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

    private Claim getTestClaim() {
        Claim claim = new Claim();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());

        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setTimeToAuthoriseRepair2(3);
        claim.getBreBand().setTimeToOffHire2(1);

        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setInvoice(testClaim.getTestExtras());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        claim.setClaimType(ClaimType.GTA);
        claim.getBreBand().setTotalLossOwnUnroadworthyCheck(true);
        claim.getVehicleHire().setHireStart(new Date());
        claim.getVehicleHire().setHireEnd(new Date());
        claim.getHireMonitoringDetail().setWhoIsSendingPav("Customers Own Insurer");
        claim.getCustomer().setIsUsable(false);
        claim.setManagingRepair(false);
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new Date());

        return claim;
    }


    @Test
    public void testPassed_Simple() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getVehicleHire().setHireStart(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-04"));
        claim.getHireMonitoringDetail().setRepairAuthorisedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-07"));
        claim.getVehicleHire().setHireEnd(new SimpleDateFormat("yyyy-MM-dd").parse("2019-11-08"));

        RepairDiaryInformationCheck rule = new RepairDiaryInformationCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }
}