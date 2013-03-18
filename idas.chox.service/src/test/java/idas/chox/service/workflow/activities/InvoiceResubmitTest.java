package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class InvoiceResubmitTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testInvoiceResubmitWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("resubmitInvoice");
        activity.process(claim);
    }

//    @Test
//    need to add xml file to run this test.
    public void testInvoiceResubmit() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
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
//        invoice.setOriginalFullTotalToPay(BigDecimal.TEN);
//        invoice.setOriginalTotalToPay(BigDecimal.TEN);
        claim.setInvoice(invoice);
        InvoiceResubmit activity = (InvoiceResubmit) activityFactory.getActivity("resubmitInvoice");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT, claim.getStatus());
    }
}
