package idas.chox.service.workflow;

import java.math.BigDecimal;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class FullInvoicePaymentReceivedTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testInvalidStatus() throws Exception {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        Activity activity = activityFactory.getActivity("fullInvoicePaymentReceived");
        activity.process(claim);
    }
    
    @Test
    public void testInvoicePaymentReceived() throws Throwable {
    
        Claim claim = new Claim();
        claim.setId(997);
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Chorganisation cho = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);
        Invoice invoice = invoiceService.getInvoice(999);
        claim.setInvoice(invoice);
        Activity activity = activityFactory.getActivity("fullInvoicePaymentReceived");

        activity.process(claim);
        Assert.assertEquals( 0 , claim.getInvoice().getInterimPaymentReceived().compareTo(BigDecimal.ONE));
        Assert.assertEquals(claim.getStatus(), ClaimStatus.INVOICE_PAYMENT_RECEIVED);
    }
    
}
