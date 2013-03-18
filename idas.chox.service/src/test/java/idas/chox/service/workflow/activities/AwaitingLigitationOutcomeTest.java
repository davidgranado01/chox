package idas.chox.service.workflow.activities;

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

public class AwaitingLigitationOutcomeTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testInvalidStatus() throws Exception {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        Activity activity = activityFactory.getActivity("awaitingLitigationOutcome");
        activity.process(claim);
    }
    
    @Test
    public void testManualInvoiceRejected() throws Throwable {
    
        Claim claim = new Claim();
        claim.setId(997);
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        Chorganisation cho = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);
        Invoice invoice = invoiceService.getInvoice(999);
        claim.setInvoice(invoice);
        Activity activity = activityFactory.getActivity("awaitingLitigationOutcome");
        
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        
        AwaitingLitigationOutcome alo = (AwaitingLitigationOutcome) activity;
        alo.setSupportingLiabilityNotes("Not liable");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LITIGATION_OUTCOME, claim.getStatus());
        Assert.assertEquals(claim.getComments().get(0).getComment(), "Not liable");
    }
    
}
