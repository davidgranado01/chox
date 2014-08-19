package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class MakeInterimPaymentTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testMakeInterimPaymentWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("makeInterimPayment");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testMakeNewTotalInterimPayment() throws Throwable {

        Claim claim = new Claim();
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.getInvoice().setTotalToPay(BigDecimal.TEN);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);

        MakeInterimPayment activity = (MakeInterimPayment) activityFactory.getActivity("makeInterimPayment");
        activity.setNewTotalInterimPayment(BigDecimal.TEN);
        activity.setAdditionalInterimPayment(BigDecimal.ZERO);
        activity.process(claim);

        Assert.assertEquals(claim.getInvoice().getInterimPaymentMade(), BigDecimal.TEN);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testMakeAdditionalInterimPayment() throws Throwable {

        Invoice invoice = invoiceService.getInvoice(999);
        invoice.setInterimPaymentMade(BigDecimal.TEN);

        Claim claim = new Claim();
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        invoice.setTotalToPay(BigDecimal.valueOf(20.00));
        claim.setInvoice(invoice);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);

        MakeInterimPayment activity = (MakeInterimPayment) activityFactory.getActivity("makeInterimPayment");
        activity.setNewTotalInterimPayment(null);
        activity.setAdditionalInterimPayment(BigDecimal.TEN);
        activity.process(claim);

        Assert.assertEquals(claim.getInvoice().getInterimPaymentMade(), new BigDecimal(20));
    }
}
