package idas.chox.bre;

import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.rules.HasCorrectAdminFee;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Rule025CorrentAdminFeeTest extends TestCase {

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

        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        return claim;
    }

    @Test
    public void testSkipped_OnOffFlag() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(false);
        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_noHire() throws IOException {

        /*
         * CHO Control Flag is OFF
         */

        Claim claim = getTestClaim();
        claim.setVehicleHire(null);
        claim.getBreBand().setCorrentAdminFee(true);
        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT.equals(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim())));
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed_managingRepairCoverNoteRequired_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("60.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_managingRepairCoverNoteNotRequired_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("50.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteRequired_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteNotRequired_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_managingRepairCoverNoteRequired_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("55.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_managingRepairCoverNoteNotRequired_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteRequired_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteNotRequired_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("28.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    
    @Test
    public void testPassed_managingRepairCoverNoteRequired_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("61.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

    @Test
    public void testPassed_managingRepairCoverNoteNotRequired_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("51.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteRequired_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("41.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteNotRequired_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/05/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("31.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

   
    
    
    @Test
    public void testPassed_managingRepairCoverNoteRequired_post201107_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("61.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_managingRepairCoverNoteNotRequired_post201107_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("51.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteRequired_post201107_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("41.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteNotRequired_post201107_equals() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("31.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_managingRepairCoverNoteRequired_post201107_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("55.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_managingRepairCoverNoteNotRequired_post201107_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("40.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteRequired_post201107_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("30.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteNotRequired_post201107_less() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("28.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));

    }

    
    @Test
    public void testPassed_managingRepairCoverNoteRequired_post201107_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("62.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

    @Test
    public void testPassed_managingRepairCoverNoteNotRequired_post201107_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(true);
        claim.getInvoice().setAdminFee(new BigDecimal("52.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteRequired_post201107_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.TRUE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("42.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

    @Test
    public void testPassed_notManagingRepairCoverNoteNotRequired_post201107_more() throws IOException, ParseException {

        Claim claim = getTestClaim();
        claim.getBreBand().setCorrentAdminFee(true);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        Date hireStart = formatter.parse("2011/07/21");
        claim.getVehicleHire().setHireStart(hireStart);
        claim.getInvoice().setCoverNoteRequired(Boolean.FALSE);
        claim.setManagingRepair(false);
        claim.getInvoice().setAdminFee(new BigDecimal("32.00"));

        RuleEvaluation rv = new HasCorrectAdminFee().applyToClaim(claim);

        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().startsWith("The Admin Fee billed is incorrect. The allowed Admin Fee is £"));

    }

}
