package idas.chox.bre;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.service.bre.rules.HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling;
import idas.chox.test.BaseTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class Rule079HireNetDoesNotExceedProtocolVehicleClassHireNetCeilingTest extends BaseTest {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim() {

        Claim claim = new Claim();

        claim.setInsurer(insurerService.getInsurer(3));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setBreBand(breBandService.getBreBand(101));
        claim.setVehicleHire(testClaim.getTestHireDetail());
        claim.getVehicleHire().setVehicleClass(vehicleClassService.getVehicleClass(31));
        claim.setInvoice(testClaim.getTestInvoice());
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.getCustomer().setVehicleClass(vehicleClassService.getVehicleClass(31));

        // SET INVOICE
        claim.getInvoice().setHireNet(new BigDecimal(400.00));

        // SET PROTOCOL VEHICLE CLASS CEiLING
        ProtocolVehicleClassCeiling protocolVehicleClassCeiling = new ProtocolVehicleClassCeiling();
        protocolVehicleClassCeiling.setHireNetCeiling(new BigDecimal("300.00"));
        protocolVehicleClassCeiling.setRepairNetCeiling(new BigDecimal("300.00"));
        protocolVehicleClassCeiling.setBreBand(breBandService.getBreBand(101));
        protocolVehicleClassCeiling.setVehicleClass(vehicleClassService.getVehicleClass(31));

//        claim.getBreBand().setHireNetCeiling(new BigDecimal("400.00"));
        claim.getBreBand().setHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling(true);
        claim.getBreBand().addProtocolVehicleClassCeiling(protocolVehicleClassCeiling);

        return claim;
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling(false);

        HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling rule = new HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling();
        rule.setProtocolVehicleClassCeilingService(protocolVehicleClassCeilingService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testPassed_LessThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling(true);

        claim.getInvoice().setHireNet(new BigDecimal("299.00"));
        HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling rule = new HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling();
        rule.setProtocolVehicleClassCeilingService(protocolVehicleClassCeilingService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testPassed_Equals() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling(true);
        claim.getInvoice().setHireNet(new BigDecimal("300.00"));

        HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling rule = new HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling();
        rule.setProtocolVehicleClassCeilingService(protocolVehicleClassCeilingService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFailed_MoreThan() throws IOException {

        Claim claim = getTestClaim();
        claim.getBreBand().setHireNetDoesNotExceedProtocolVehicleClassHireNetCeiling(true);
        claim.getInvoice().setHireNet(new BigDecimal("340.00"));

        HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling rule = new HireNetDoesNotExceedProtocolVehicleClassHireNetCeiling();
        rule.setProtocolVehicleClassCeilingService(protocolVehicleClassCeilingService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Hire Net billed £340.00 exceeds the Protocol Hire Net ceiling of £300.00 for vehicle class S4."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType())));
        assertFalse(rv.getIsVisibleToCHO());

    }
}
