package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class UpdateManualInvoicePaidTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testUpdateManualInvoicePaidWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("updateManualInvoicePaid");
        activity.process(claim);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testUpdateManualInvoiceContested() throws Throwable {

        Claim claim = new Claim();
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setClaimType(ClaimType.INSURER_UPLOAD);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setThirdParty(thirdPartyService.getThirdParty(999));
        claim.setCustomer(customerService.getCustomer(999));
        claim.getInvoice().setInterimPaymentReceived(BigDecimal.ZERO);
        
        Activity activity = activityFactory.getActivity("updateManualInvoicePaid");
        activity.process(claim);

        Assert.assertEquals(claim.getStatus(), ClaimStatus.MANUAL_INVOICE_PAID);
    }
}
