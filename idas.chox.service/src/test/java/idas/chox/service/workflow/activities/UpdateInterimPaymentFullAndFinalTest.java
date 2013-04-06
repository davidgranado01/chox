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

public class UpdateInterimPaymentFullAndFinalTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testUpdateInterimPaymentFullAndFinalWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("updateInterimPaymentFullAndFinal");
        activity.process(claim);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testUpdateInterimPaymentFullAndFinal() throws Throwable {

        Claim claim = new Claim();
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setThirdParty(thirdPartyService.getThirdParty(999));
        claim.setCustomer(customerService.getCustomer(999));

        UpdateInterimPaymentFullAndFinal activity = (UpdateInterimPaymentFullAndFinal) activityFactory.getActivity("updateInterimPaymentFullAndFinal");
        activity.process(claim);

        Assert.assertEquals(ClaimStatus.INVOICE_PAYMENT_RECEIVED, claim.getStatus());
    }
}
