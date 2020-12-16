package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.*;
import idas.chox.service.bre.rules.RepairOnUsableVehicleCheck;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Rule110RepairOnUsableVehicleCheck extends TestCase {

    MockObjects testClaim = new MockObjects();

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private Claim getTestClaim( ClaimType type) {
        Claim claim = new Claim();

        claim.setInsurer(testClaim.getTestInsurer());
        claim.setChorganisation(testClaim.getTestChorganisation());
        claim.setClaimType(type);
        claim.setCustomer(testClaim.getTestCustomerVehicleDamage());
        claim.setInvoice(testClaim.getTestInvoice());

        //Rule conditions (Will Fail)
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);

        //ask for the rule to be executed
        claim.setBreBand(testClaim.getTestBreBand());
        claim.getBreBand().setRepairChargeCheck(true);

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getBreBand().setRepairChargeCheck(false);
        RuleEvaluation rv = new RepairOnUsableVehicleCheck().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.getClaimType()).equals(ClaimStatus.INVOICE_ESCALATED_TO_CH));
        assertFalse(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_GTA() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_InsurerInvoice() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_INVOICE);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_InsurerVsInsurer() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_VS_INSURER);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }


    @Test
    public void testPassed_TPI() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed_GTA() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.GTA);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_InsurerInvoice() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_INVOICE);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testFailed_InsurerVsInsurer() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.INSURER_VS_INSURER);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }


    @Test
    public void testFailed_TPI() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.TPI);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

    @Test
    public void testSkipped_Subscriber() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.SUBSCRIBER);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED== rv.getResult());
    }

    @Test
    public void testSkipped_FixedFee() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.FIXED_FEE);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }

    @Test
    public void testSkipped_CollaborationProtocol() throws IOException, ParseException {

        Claim claim = getTestClaim(ClaimType.COLLABORATION_PROTOCOL);
        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_SKIPPED == rv.getResult());
    }


    @Test
    public void testPassed_UsableNullRepairZero(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(null);
        claim.getInvoice().setRepairNet(BigDecimal.ZERO);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_UsableNullRepairNotZero(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(null);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }


    @Test
    public void testPassed_UsableRepairNull(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setRepairNet(null);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_NotUsableRepairNull(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(null);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_UsableNullRepairNull(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(null);
        claim.getInvoice().setRepairNet(null);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_NotUsableRepairZero(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(BigDecimal.ZERO);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_NotUsableRepairNotZero(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(false);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testPassed_UsableRepairZero(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setRepairNet(BigDecimal.ZERO);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_PASSED == rv.getResult());
    }

    @Test
    public void testFailed_UsableRepairNotZero(){
        Claim claim = getTestClaim(ClaimType.TPI);
        claim.getCustomer().setIsUsable(true);
        claim.getInvoice().setRepairNet(BigDecimal.TEN);

        RepairOnUsableVehicleCheck rule = new RepairOnUsableVehicleCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);

        Assert.assertTrue(RuleEvaluationResult.RULE_FAILED == rv.getResult());
    }

}
