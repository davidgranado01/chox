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
import idas.chox.service.bre.rules.TotalVatLimitCheck;
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
public class Rule055TotalVatLimitCheckTest {

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
        claim.getBreBand().setTotalVatLimitCheck(false);
        TotalVatLimitCheck rule = new TotalVatLimitCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleSkipped == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testPassed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setTotalVatLimitCheck(true);


        claim.getInvoice().setTotalVat(new BigDecimal(712.10).setScale(2, BigDecimal.ROUND_HALF_DOWN));


        claim.getInvoice().setHireNet(new BigDecimal(1690.08));
        claim.getInvoice().setRepairNet(new BigDecimal(1820.43));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal(50.00));
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal(0.00));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal(0.00));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal(0.00));

        TotalVatLimitCheck rule = new TotalVatLimitCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RulePassed == rv.getResult());
        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase(""));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }

    @Test
    public void testFailed() throws IOException {


        Claim claim = getTestClaim();
        claim.getBreBand().setTotalVatLimitCheck(true);

        claim.getInvoice().setTotalVat(new BigDecimal(713.10).setScale(2, BigDecimal.ROUND_HALF_DOWN));


        claim.getInvoice().setHireNet(new BigDecimal(1690.08));
        claim.getInvoice().setRepairNet(new BigDecimal(1820.43));
        claim.getInvoice().setEngineerFeeNet(new BigDecimal(50.00));
        claim.getInvoice().setTotalLossFeeNet(new BigDecimal(0.00));
        claim.getInvoice().setStorageRecoveryNet(new BigDecimal(0.00));
        claim.getInvoice().setDeductionForClaimsHandlingFee(new BigDecimal(0.00));


        TotalVatLimitCheck rule = new TotalVatLimitCheck();
        RuleEvaluation rv = rule.applyToClaim(claim);
        assertTrue(RuleEvaluationResult.RuleFailed == rv.getResult());

        assertTrue(rv.getRelatedRule().getNarrative().equalsIgnoreCase("The CHO is charging more than 20.00% VAT for the Total."));
        assertTrue(rv.getRelatedRule().getStatusAfterFailure(claim.isTpiClaim()) == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        assertTrue(rv.getIsVisibleToCHO());

    }
}
