package scsbre.tests;

import java.io.IOException;
import java.math.BigDecimal;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.rules.HireNetDoesNotExceedVehicleClassHireNetCeiling;
import scsbre.model.ClaimStatus;
import scsbre.tests.data.TestClaim;
import scsbre.tests.sample.*;

public class Rule003HireNetDoesNotExceedVehicleClassHireNetCeilingTest extends TestCase {

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
        claim.setChoBand(testClaim.getTestChoBand());
        claim.setClaimEngineeringReport(testClaim.getTestEngineeringReport());
        claim.setCustomerVehicleDamage(testClaim.getTestCustomerVehicleDamage());
        claim.setExtras(testClaim.getTestExtras());
        claim.setHireDetail(testClaim.getTestHireDetail());
        claim.setHireMonitoringDetail(testClaim.getTestHireMonitoringDetail());
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setVClass(testClaim.getTestVehicleClass());

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(400.00));
        
        // SET VEHICLE CLASS CEiLING
        VehicleClassCeilingInfo vehicleClassCeiling = new VehicleClassCeilingInfo();
        vehicleClassCeiling.setHireNetCeiling(new BigDecimal("300.00"));

        // SET CHOBAND
        claim.getChoBand().setHireNetCeiling(new BigDecimal("400.00"));
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getChoBand().setVehicleClassCeiling(vehicleClassCeiling);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(false);
        
        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_LessThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        
        claim.getInvoice().setHireNet(new BigDecimal("299.00"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_Equals() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getInvoice().setHireNet(new BigDecimal("300.00"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed_MoreThan() throws IOException {

        ClaimInfo claim = getTestClaim();
        claim.getChoBand().setHireNetDoesNotExceedVehicleClassHireNetCeiling(true);
        claim.getInvoice().setHireNet(new BigDecimal("340.00"));

        RuleEvaluation rv = new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Hire Net billed £340.00 exceeds the Hire Net ceiling of £300.00 for vehicle class SP1."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure()==ClaimStatus.InvoiceEscalatedToHandler);
        assertFalse(rv.getIsVisibleToCHO());

    }
    
}
