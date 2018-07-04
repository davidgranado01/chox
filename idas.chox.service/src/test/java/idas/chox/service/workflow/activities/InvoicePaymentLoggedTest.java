package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class InvoicePaymentLoggedTest extends BaseTest{
	

    @Test(expected = AccessDeniedException.class)
    public void testInvoicePaymentLoggedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("invoicePaymentLogged");
        activity.process(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testInvoicePaymentLogged() throws Throwable {
    	Invoice invoice = invoiceService.getInvoice(999);
        Claim claim = claimService.getClaim(999);
        claim.setInvoice(invoice);
        claim.setPercentageLiabilityAccepted(new BigDecimal(2.00));
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        InvoicePaymentLogged activity = (InvoicePaymentLogged) activityFactory.getActivity("invoicePaymentLogged");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_PAYMENT_LOGGED, claim.getStatus());
    }
}
