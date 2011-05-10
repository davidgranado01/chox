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
import idas.chox.service.bre.rules.HireVatInvoicedDateCheck;
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
public class Rule059HireVatInvoicedDateCheckTest {

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
        claim.getBreBand().setHireVatInvoicedDateCheck(false);
        HireVatInvoicedDateCheck rule = new HireVatInvoicedDateCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testSkipped_2() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHireVatInvoicedDateCheck(true);
        claim.setVehicleHire(null);

        HireVatInvoicedDateCheck rule = new HireVatInvoicedDateCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHireVatInvoicedDateCheck(true);


        claim.getInvoice().setHireNet(new BigDecimal(1064.06));
        claim.getInvoice().setHireVat(new BigDecimal(212.81).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        claim.getInvoice().setDateInvoiced(DateHelper.Parse("14/03/2011"));

        //claim.getInvoice().setAutomaticFee(new BigDecimal(0.00));


        HireVatInvoicedDateCheck rule = new HireVatInvoicedDateCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHireVatInvoicedDateCheck(true);

        claim.getInvoice().setHireNet(new BigDecimal(1064.06));
        claim.getInvoice().setHireVat(new BigDecimal(213.81).setScale(2, BigDecimal.ROUND_HALF_DOWN));
        claim.getInvoice().setDateInvoiced(DateHelper.Parse("14/03/2011"));

        HireVatInvoicedDateCheck rule = new HireVatInvoicedDateCheck();

        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());


        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The Hire VAT charged by this CHO is dependent on the Invoiced Date, with this in consideration the CHO is charging more than the allowed VAT rate of 20.00% for the Hire."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }
}
