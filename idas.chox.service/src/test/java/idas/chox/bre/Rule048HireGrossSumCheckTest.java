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
import idas.chox.service.bre.rules.HireGrossSumCheck;
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
public class Rule048HireGrossSumCheckTest {

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
        claim.getBreBand().setHasHireGrossSumCheck(false);
        HireGrossSumCheck rule = new HireGrossSumCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

     @Test
    public void testPassed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHasHireGrossSumCheck(true);
        claim.getInvoice().setHireNet(new BigDecimal(1157.63));
        claim.getInvoice().setHireVat(new BigDecimal(231.53));
        claim.getInvoice().setHireGross(new BigDecimal(1389.16));

        HireGrossSumCheck rule = new HireGrossSumCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setHasHireGrossSumCheck(true);
        claim.getInvoice().setHireNet(new BigDecimal(1157.63));
        claim.getInvoice().setHireVat(new BigDecimal(231.53));
        claim.getInvoice().setHireGross(new BigDecimal(1380.16));

        HireGrossSumCheck rule = new HireGrossSumCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("Hire Gross - The sum of the Hire Net and the Hire VAT is incorrect."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }





}
