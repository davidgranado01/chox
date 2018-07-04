package idas.chox.service.workflow.activities;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;
import java.math.BigDecimal;

public class PaymentNotReceivedTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testPaymentNotReceivedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("paymentNotReceived");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testPaymentNotReceived() throws Throwable {

        Claim claim = new Claim();
        claim.setStatusModifiedDate(DateHelper.getCurrentDate());
        Insurer insurer = insurerService.getInsurer(3);
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(chorganisation);
        claim.setInsurer(insurer);
        claim.setChoReference("testing");
        claim.setManagingRepair(false);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
        claim.setAppliedLiability(new BigDecimal("100.00"));
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim);
        auditTrailService.logAuditLog(ClaimStatus.INVOICE_PAYMENT_LOGGED, ClaimStatus.AWAITING_INVOICE_PAYMENT, claim);

        PaymentNotReceived activity = (PaymentNotReceived) activityFactory.getActivity("paymentNotReceived");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }
}
