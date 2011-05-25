/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.bre;
import idas.chox.bre.mock.MockObjects;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.rules.InsurancePremiumTaxCheck;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;
/**
 *
 * @author rajareddydodda
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-IntelligentNote-test.xml", "classpath:applicationContext-Filters-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-BRE-test.xml", "classpath:applicationContext-Notification-test.xml", "classpath:applicationContext-Workflow-test.xml"})
public class Rule069InsurancePremiumTaxCheckTest {


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


        Claim claim = getTestClaim();
        claim.getBreBand().setInsurancePremiumTaxCheck(false);
        InsurancePremiumTaxCheck rule = new InsurancePremiumTaxCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setInsurancePremiumTaxCheck(true);
        claim.getInvoice().setNonStandardInsurancePremiumFee(BigDecimal.ZERO);

        InsurancePremiumTaxCheck rule = new InsurancePremiumTaxCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_3() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setInsurancePremiumTaxCheck(true);
        claim.getInvoice().setNonStandardInsurancePremiumQty(0);


        InsurancePremiumTaxCheck rule = new InsurancePremiumTaxCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setInsurancePremiumTaxCheck(true);

        claim.getInvoice().setNonStandardInsurancePremiumFee(new BigDecimal(27.00));
        claim.getInvoice().setNonStandardInsurancePremiumQty(9);


        InsurancePremiumTaxCheck rule = new InsurancePremiumTaxCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setInsurancePremiumTaxCheck(true);


        claim.getInvoice().setNonStandardInsurancePremiumFee(new BigDecimal(36.00));
        claim.getInvoice().setNonStandardInsurancePremiumQty(9);


        InsurancePremiumTaxCheck rule = new InsurancePremiumTaxCheck();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());

        
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging £4.00 per day for the Insurance Premium Tax/Non Standard Risk Insurance Premium Tax, please review."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

}
