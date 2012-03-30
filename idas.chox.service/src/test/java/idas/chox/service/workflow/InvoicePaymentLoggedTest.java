package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.InvoicePaymentLogged;
import idas.chox.test.BaseTest;

import org.junit.Assert;
import org.junit.Test;

public class InvoicePaymentLoggedTest extends BaseTest{
	

    @Test(expected = InvalidClaimStatusException.class)
    public void testInvoicePaymentLoggedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("invoicePaymentLogged");
        activity.process(claim);
    }

    @Test
    public void testInvoicePaymentLogged() throws Throwable {
    	Invoice invoice = invoiceService.getInvoice(999);
        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInvoice(invoice);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        InvoicePaymentLogged activity = (InvoicePaymentLogged) activityFactory.getActivity("invoicePaymentLogged");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_PAYMENT_LOGGED, claim.getStatus());
    }
}
