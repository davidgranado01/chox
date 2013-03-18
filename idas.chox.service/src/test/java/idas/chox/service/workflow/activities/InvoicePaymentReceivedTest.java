package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class InvoicePaymentReceivedTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testInvoicePaymentReceivedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("invoicePaymentReceived");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testInvoicePaymentReceived() throws Throwable {
    	Invoice invoice = invoiceService.getInvoice(999);
        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(chorganisation);
        claim.setInvoice(invoice);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        InvoicePaymentReceived activity = (InvoicePaymentReceived) activityFactory.getActivity("invoicePaymentReceived");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_PAYMENT_RECEIVED, claim.getStatus());
    }
}
