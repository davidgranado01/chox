package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class InvoiceRejectionAcceptTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testInvoiceRejectionAcceptWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("acceptRejectedInvoice");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testInvoiceRejectionAccept() throws Throwable {

        Invoice invoice = invoiceService.getInvoice(999);
        
        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer); 
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        claim.setInvoice(invoice);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        InvoiceRejectionAccept activity = (InvoiceRejectionAccept) activityFactory.getActivity("acceptRejectedInvoice");

        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_REJECTED_ACCEPTED, claim.getStatus());
    }
}
