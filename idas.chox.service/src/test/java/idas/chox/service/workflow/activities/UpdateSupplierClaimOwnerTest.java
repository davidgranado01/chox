package idas.chox.service.workflow.activities;

import junit.framework.Assert;

import org.junit.Test;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.test.BaseTest;

public class UpdateSupplierClaimOwnerTest extends BaseTest {

    @Test
    public void testSupplierOwner() throws Throwable {
    
        Claim claim = new Claim();
        claim.setId(997);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Chorganisation cho = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);
        Invoice invoice = invoiceService.getInvoice(999);
        claim.setInvoice(invoice);
        UpdateSupplierClaimOwner  activity = (UpdateSupplierClaimOwner)activityFactory.getActivity("updateSupplierClaimOwner");
        
        activity.setSupplierClaimOwnerId(2);
        
        activity.process(claim);
        Assert.assertEquals("Supplier Claim Owner is 'Operative CHO' (contact number: 009876540987654)", claim.getComments().get(0).getComment());
    }
    
}
