package idas.chox.service.workflow;

import java.math.BigDecimal;

import junit.framework.Assert;

import org.junit.Test;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.FullPaymentNotReceived;
import idas.chox.test.BaseTest;

public class FullPaymentNotReceivedTest extends BaseTest {

    @Test(expected = InvalidClaimStatusException.class)
    public void testInvalidStatus() throws Exception {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        Activity activity = activityFactory.getActivity("fullPaymentNotReceived");
        activity.process(claim);
    }
    
    @Test
    public void testAddInterimPaymentContest() throws Throwable {
        Claim claim = new Claim();
        claim.setId(997);
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(chorganisation);
        Invoice invoice = invoiceService.getInvoice(999);
        claim.setInvoice(invoice);
        
        Activity activity = activityFactory.getActivity("fullPaymentNotReceived");
        
        FullPaymentNotReceived fullPaymentNotReceived = (FullPaymentNotReceived) activity;
        fullPaymentNotReceived.setInterimPaymentReceived(BigDecimal.ONE);

        activity.process(claim);
        Assert.assertEquals(0, claim.getInvoice().getInterimPaymentMade().compareTo(BigDecimal.valueOf(2.00)));
        Assert.assertEquals(0, claim.getInvoice().getInterimPaymentReceived().compareTo(BigDecimal.valueOf(2.00)));
    }
        
    @Test
    public void testSetInterimPaymentContest() throws Throwable {
        Claim claim = new Claim();
        claim.setId(997);
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(chorganisation);
        Invoice invoice = invoiceService.getInvoice(999);
        claim.setInvoice(invoice);
        claim.getInvoice().setInterimPaymentMade(null);
        claim.getInvoice().setInterimPaymentReceived(null);
        Activity activity = activityFactory.getActivity("fullPaymentNotReceived");
        
        FullPaymentNotReceived fullPaymentNotReceived = (FullPaymentNotReceived) activity;
        fullPaymentNotReceived.setInterimPaymentReceived(BigDecimal.ONE);

        activity.process(claim);
        Assert.assertEquals(0,claim.getInvoice().getInterimPaymentMade().compareTo(BigDecimal.valueOf(1.00)));
        Assert.assertEquals(0,claim.getInvoice().getInterimPaymentReceived().compareTo(BigDecimal.valueOf(1.00)));
    }
    
}
