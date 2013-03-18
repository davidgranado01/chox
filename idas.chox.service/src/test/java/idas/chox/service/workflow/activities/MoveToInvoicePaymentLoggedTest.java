package idas.chox.service.workflow.activities;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class MoveToInvoicePaymentLoggedTest extends BaseTest {
    
    @Test(expected = AccessDeniedException.class)
    public void testMoveToInvoicePaymentLoggedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("moveToInvoicePaymentLogged");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testMoveToInvoicePaymentLogged() throws Throwable {

        Claim claim = new Claim();
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        claim.setInvoice(invoiceService.getInvoice(999));
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);

        MoveToInvoicePaymentLogged activity = (MoveToInvoicePaymentLogged) activityFactory.getActivity("moveToInvoicePaymentLogged");
        activity.process(claim);

        Assert.assertEquals(ClaimStatus.INVOICE_PAYMENT_RECEIVED, claim.getStatus());
    }
}
