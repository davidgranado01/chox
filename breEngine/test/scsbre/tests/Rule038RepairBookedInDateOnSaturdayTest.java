package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.RepairBookedInDateOnSaturday;
import scsbre.engine.util.DateHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;


public class Rule038RepairBookedInDateOnSaturdayTest extends TestCase {

    TestClaim testClaim = new TestClaim();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private ClaimInfo getTestClaim(){

        ClaimInfo claim = new ClaimInfo();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setCHOrganisation(testClaim.getTestChorganisation());
        claim.setBreBand(testClaim.getTestBreBand());
        claim.setClaimEngineeringReport(testClaim.getTestEngineeringReport());
        claim.setCustomerVehicleDamage(testClaim.getTestCustomerVehicleDamage());
        claim.setExtras(testClaim.getTestExtras());
        claim.setHireDetail(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setVClass(testClaim.getTestVehicleClass());

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setRepairBookedInDate(false);
        RuleEvaluation rv = new RepairBookedInDateOnSaturday().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testPassed() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setRepairBookedInDate(true);

        // SET TO NOT SATURDAY
        claim.getHireMonitoringDetail().setRepairBookInDate(DateHelper.getDateFromString("09/10/2009"));

        RuleEvaluation rv = new RepairBookedInDateOnSaturday().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }

    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setRepairBookedInDate(true);

        // SET TO SATURDAY
        claim.getHireMonitoringDetail().setRepairBookInDate(DateHelper.getDateFromString("10/10/2009"));

        RuleEvaluation rv = new RepairBookedInDateOnSaturday().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Repair was booked in on a Saturday."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
}
