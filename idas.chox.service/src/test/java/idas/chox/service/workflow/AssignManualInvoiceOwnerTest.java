package idas.chox.service.workflow;

import junit.framework.Assert;

import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.activities.AssignManualInvoiceOwner;
import idas.chox.test.BaseTest;
import org.springframework.security.access.AccessDeniedException;

public class AssignManualInvoiceOwnerTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testInvalidStatus() throws Exception {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Activity activity = activityFactory.getActivity("assignManualInvoiceOwner");
        activity.process(claim);
    }
        
    @Test
    public void testManualInvoiceRejected() throws Throwable {
        Claim claim = new Claim();
        claim.setId(997);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(true);
        insurer.setEnableManualInvoiceOwnership(true);
        claim.setInsurer(insurer);
        Invoice invoice = invoiceService.getInvoice(999);
        claim.setInvoice(invoice);
        Activity activity = activityFactory.getActivity("assignManualInvoiceOwner");
        
        AssignManualInvoiceOwner assignManualInvoiceOwner = (AssignManualInvoiceOwner) activity;
        assignManualInvoiceOwner.setClaimOwnerId(8);
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.MANUAL_INVOICE_REJECTED, claim.getStatus());
    }
    
}
