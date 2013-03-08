package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.NumberOfHireDaysReconcile;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule024NumberOfHireDaysReconcileTest extends TestCase {

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
        claim.setEngineerReport(testClaim.getTestEngineeringReport());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setInvoice(testClaim.getTestExtras());
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.getCustomer().setVehicleClass(testClaim.getTestVehicleClass());

        claim.getVehicleHire().setRentalStart(DateHelper.Parse("01/10/2009"));
        claim.getVehicleHire().setDays(10);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(false);
        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailled_lessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(true);

        claim.getVehicleHire().setRentalEnd(DateHelper.Parse("11/10/2009"));

        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided"));

    }

    @Test
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(true);

        claim.getVehicleHire().setRentalEnd(DateHelper.Parse("10/10/2009"));

        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testFailled() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setNumberOfHireDaysReconcile(true);

        claim.getVehicleHire().setRentalEnd(DateHelper.Parse("09/10/2009"));

        RuleEvaluation rv = new NumberOfHireDaysReconcile().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided"));

    }
}
