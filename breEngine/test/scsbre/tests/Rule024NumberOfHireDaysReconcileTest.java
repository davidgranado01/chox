package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.NumberOfHireDaysReconcile;
import scsbre.engine.util.DateHelper;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule024NumberOfHireDaysReconcileTest extends TestCase {

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

        claim.getHireDetail().setRentalStart(DateHelper.getDateFromString("01/10/2009"));
        claim.getHireDetail().setDays(10);
        
        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(false);
        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceDataCalculationIncorrect);
        assertTrue(rv.getIsVisibleToCHO());

    }

    public void testFailled_lessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(true);

        claim.getHireDetail().setRentalEnd(DateHelper.getDateFromString("11/10/2009"));

        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided"));

    }

    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(true);

        claim.getHireDetail().setRentalEnd(DateHelper.getDateFromString("10/10/2009"));

        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    public void testFailled() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(true);

        claim.getHireDetail().setRentalEnd(DateHelper.getDateFromString("09/10/2009"));

        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided"));

    }
}
