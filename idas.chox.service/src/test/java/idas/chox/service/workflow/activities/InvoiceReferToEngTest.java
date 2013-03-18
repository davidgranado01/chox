package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class InvoiceReferToEngTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testInvoiceReferToEngWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("invoiceReferToEng");
        activity.process(claim);
    }

    @Test
    public void testInvoiceReferToEng() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        InvoiceReferToEng activity = (InvoiceReferToEng) activityFactory.getActivity("invoiceReferToEng");

        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_REF_TO_ENG, claim.getStatus());
    }
}
