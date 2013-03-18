package idas.chox.service.workflow.activities;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class NewSupplementaryInvoiceTest extends BaseTest {
    
    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
    }

    @After
    public void tearDownClass() throws Exception {
         fakeSecurityInfoProvider.setIsCHO(false);
    }
    
    @Test(expected = AccessDeniedException.class)
    public void testNewSupplementaryInvoiceWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("supplementaryInvoice");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testNewSupplementaryInvoice() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(null);
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setCustomer(customerService.getCustomer(999));
        claim.setInvoice(invoiceService.getInvoice(999));
        
        NewSupplementaryInvoice activity = (NewSupplementaryInvoice) activityFactory.getActivity("supplementaryInvoice");
        activity.setChainActivity(null);
        activity.process(claim);

        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, claim.getStatus());
    }
}
