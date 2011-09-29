/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.InvoiceRejection;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class InvoiceRejectionTest {

    @Autowired
    ActivityFactory activityFactory;
   

    @Test(expected = InvalidClaimStatusException.class)
    public void testInvoiceRejectionWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("rejectInvoice");
        activity.process(claim);
    }

    @Test
    public void testInvoiceRejection() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Invoice invoice = new Invoice();
        invoice.setDateInvoiced(new Date());
        invoice.setHireNet(BigDecimal.ONE);
        invoice.setHireVat(BigDecimal.ZERO);
        invoice.setHireGross(BigDecimal.ZERO);
        invoice.setRepairNet(BigDecimal.ONE);
        invoice.setRepairVat(BigDecimal.ZERO);
        invoice.setRepairGross(BigDecimal.ZERO);
        invoice.setEngineerFeeNet(BigDecimal.ONE);
        invoice.setEngineerFeeVat(BigDecimal.ZERO);
        invoice.setEngineerFeeGross(BigDecimal.ZERO);
        invoice.setStorageRecoveryNet(BigDecimal.ZERO);
        invoice.setStorageRecoveryVat(BigDecimal.ZERO);
        invoice.setStorageRecoveryGross(BigDecimal.ZERO);
        invoice.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        invoice.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        invoice.setTotalNet(BigDecimal.ONE);
        invoice.setTotalVat(BigDecimal.ZERO);
        invoice.setTotalGross(BigDecimal.ZERO);
        invoice.setDiscount(BigDecimal.ZERO);
        invoice.setTotalToPay(BigDecimal.TEN);
        invoice.setFullTotalToPay(BigDecimal.TEN);
        invoice.setOriginalFullTotalToPay(BigDecimal.TEN);
        invoice.setOriginalTotalToPay(BigDecimal.TEN);
        invoice.setInsurerDiscount(BigDecimal.ZERO);
        claim.setInvoice(invoice);
        InvoiceRejection activity = (InvoiceRejection) activityFactory.getActivity("rejectInvoice");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO, claim.getStatus());
    }
}
