package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.bre.rules.HasAllowedVehicleClass;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-IntelligentNote-test.xml", "classpath:applicationContext-Filters-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-BRE-test.xml", "classpath:applicationContext-Notification-test.xml", "classpath:applicationContext-Workflow-test.xml"})
public class Rule001HasAllowedVehicleClassTest {

    @Autowired
    VehicleClassPriceService vehicleClassPriceService;
    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
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

        return claim;
    }

    @Test
    public void testSkipped_1() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(false);

        HasAllowedVehicleClass rule = new HasAllowedVehicleClass();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {

        /*
         * CHO Control Flag is ON
         * Claim. V CLass is Null
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);
        claim.getCustomer().setVehicleClass(null);


        HasAllowedVehicleClass rule = new HasAllowedVehicleClass();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle class is not specified."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testPassed() throws IOException {

        // CUSTOMER VEHICLE CLASS V.S HIRE MONITORING DETAIL VEHICLE CLASS (SAME)

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);
        claim.getCustomer().getVehicleClass().setName("SP1");
        claim.getCustomer().getVehicleClass().setId(65);

        claim.getVehicleHire().getVehicleClass().setName("SP1");
        claim.getVehicleHire().getVehicleClass().setId(65);
        HasAllowedVehicleClass rule = new HasAllowedVehicleClass();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    @Transactional
    public void testFailed() throws IOException {

        // CUSTOMER VEHICLE CLASS V.S HIRE MONITORING DETAIL VEHICLE CLASS (NOT SAME)

        Claim claim = getTestClaim();
        claim.getBreBand().setHasAllowedVehicleClass(true);

        claim.getCustomer().getVehicleClass().setId(65);
        claim.getCustomer().getVehicleClass().setName("SP1");
        claim.getInsurer().setId(3);
        claim.getChorganisation().setId(1006);


        claim.getVehicleHire().getVehicleClass().setId(95);
        claim.getVehicleHire().getVehicleClass().setName("SP2");


       
        HasAllowedVehicleClass rule = new HasAllowedVehicleClass();
        rule.setVehicleClassPriceService(vehicleClassPriceService);
        RuleEvaluation rv = rule.applyToClaim(claim);


        Assert.assertNotNull(vehicleClassPriceService);
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The vehicle class allocated for the hire (SP2) is not a like for like match on the customer's vehicle class (SP1)."));
        assertTrue(ClaimStatus.INVOICE_ESCALATED_TO_CH.equals(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())));
        assertFalse(rv.getIsVisibleToCHO());

    }
}

