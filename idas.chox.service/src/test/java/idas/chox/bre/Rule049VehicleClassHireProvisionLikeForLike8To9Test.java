package idas.chox.bre;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.bre.mock.MockObjects;
import idas.chox.service.bre.rules.VehicleClassHireProvisionLikeForLike8To9;
import java.io.IOException;
import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;
import static org.junit.Assert.*;

/**
 *
 * @author rajareddydodda
 */
public class Rule049VehicleClassHireProvisionLikeForLike8To9Test extends BaseTest {

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

        return claim;
    }


    @Test
    public void testSkipped_1() throws IOException {

        /*
         *   Vehicle Class Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(false);
        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {

        /*
         * Customer vehicle class is not specified.
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
         claim.getCustomer().setVehicleClass(null);
        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle class is not specified."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_3() throws IOException {

        /*
         * Customer hire start date not available.
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
        //claim.getCustomer().setVehicleClass(null);
        claim.getVehicleHire().setHireStart(null);
        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer hire start date not available."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_4() throws IOException {

       /*
        * Customer vehicle not prestige.
        */

        Claim claim = getTestClaim();
        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
        //claim.getCustomer().setVehicleClass(null);
        //claim.getVehicleHire().setHireStart(null);
        claim.getCustomer().getVehicleClass().setName("P10");

        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle not prestige."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_5() throws IOException {

        /*
         *   Customer vehicle registration date not available.
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
        //claim.getCustomer().setVehicleClass(null);
        //claim.getVehicleHire().setHireStart(null);
        claim.getCustomer().getVehicleClass().setName("P2");
        claim.getVehicleHire().getVehicleClass().setName("P1");
        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();
        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle registration date not available."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }


    @Test
    //@Transactional
    public void testSkipped_6() throws IOException {

        /*
         * Customer vehicle registration date not available.
         */
        Claim claim = getTestClaim();

        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
        claim.getCustomer().getVehicleClass().setName("P2");
        claim.getVehicleHire().getVehicleClass().setName("P1");

        // claim.getCustomer().setInitialECD(DateHelper.Parse("01/10/2009"));
        claim.getCustomer().setHpiFirstRegistration(DateHelper.Parse("01/10/2005"));
        claim.getVehicleHire().setHireStart(DateHelper.Parse("01/10/2011"));

        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();

        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Customer vehicle registration date not available."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }



    @Test
    //@Transactional
    public void testPassed() throws IOException {


        Claim claim = getTestClaim();

        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
        claim.getCustomer().getVehicleClass().setName("P4");
        claim.getVehicleHire().getVehicleClass().setName("P1");

        // claim.getCustomer().setInitialECD(DateHelper.Parse("01/10/2009"));
        claim.getCustomer().setHpiFirstRegistration(DateHelper.Parse("01/10/2002"));
        claim.getVehicleHire().setHireStart(DateHelper.Parse("01/10/2011"));

        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();

        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    //@Transactional
    public void testFailed() throws IOException {


        Claim claim = getTestClaim();

        claim.getBreBand().setVehicleClassHireProvisionLikeForLike8To9(true);
        claim.getCustomer().getVehicleClass().setName("P1");
        claim.getVehicleHire().getVehicleClass().setName("P1");

        // claim.getCustomer().setInitialECD(DateHelper.Parse("01/10/2009"));
        claim.getCustomer().setHpiFirstRegistration(DateHelper.Parse("01/10/2002"));
        claim.getVehicleHire().setHireStart(DateHelper.Parse("01/10/2011"));

        VehicleClassHireProvisionLikeForLike8To9 rule = new VehicleClassHireProvisionLikeForLike8To9();

        RuleEvaluation rv = rule.applyToClaim(claim);


        assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO's customer's vehicle is 8 years old and vehicle class P1, the replacement vehicle class of P1 is not acceptable as the replacement vehicle class should be two classes less than the CHO's customer's vehicle based on the age of the vehicle and the agreement in place."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }







}
