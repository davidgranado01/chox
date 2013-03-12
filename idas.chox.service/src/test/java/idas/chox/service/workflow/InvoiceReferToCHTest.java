package idas.chox.service.workflow;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.activities.InvoiceReferToCH;
import idas.chox.test.BaseTest;

public class InvoiceReferToCHTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testInvoiceReferToCHWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("invoiceReferToCH");
        activity.process(claim);
    }

    @Test
    public void testInvoiceReferToCH() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.INVOICE_ESCALATED);
        InvoiceReferToCH activity = (InvoiceReferToCH) activityFactory.getActivity("invoiceReferToCH");

        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_REF_TO_CH, claim.getStatus());
    }
}
