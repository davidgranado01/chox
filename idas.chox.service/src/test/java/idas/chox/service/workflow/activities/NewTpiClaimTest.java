package idas.chox.service.workflow.activities;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class NewTpiClaimTest extends BaseTest {
  
    @Test(expected = AccessDeniedException.class)
    public void testMakeInterimPaymentWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claim.setBreBand(new BreBand());
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("newTpiClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testMakeNewTotalInterimPayment1() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.TPI);
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setStatus(null);
        claim.setTpiClaimStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setThirdParty(thirdPartyService.getThirdParty(999));
        claim.setCustomer(customerService.getCustomer(999));
        claim.setBreBand(new BreBand());

        NewTpiClaim activity = (NewTpiClaim) activityFactory.getActivity("newTpiClaim");
        activity.process(claim);

        Assert.assertEquals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT, claim.getStatus());
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testMakeNewTotalInterimPayment2() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.TPI);
        claim.setBreBand(new BreBand());
        claim.setInsurer(insurerService.getInsurer(3));
        claim.getInsurer().setTpiAutoRoutingEnable(false);
        claim.setStatus(null);
        claim.setTpiClaimStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setThirdParty(thirdPartyService.getThirdParty(999));
        claim.setCustomer(customerService.getCustomer(999));

        NewTpiClaim activity = (NewTpiClaim) activityFactory.getActivity("newTpiClaim");
        activity.process(claim);

        Assert.assertEquals(ClaimStatus.INVOICE_UNASSIGNED, claim.getStatus());
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testMakeNewTotalInterimPayment3() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.TPI);
        claim.setBreBand(new BreBand());
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setStatus(null);
        claim.setTpiClaimStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        claim.setInvoice(invoiceService.getInvoice(999));
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setThirdParty(thirdPartyService.getThirdParty(999));
        claim.setCustomer(customerService.getCustomer(999));

        NewTpiClaim activity = (NewTpiClaim) activityFactory.getActivity("newTpiClaim");
        activity.setAutoRoutedInvoice(true);
        activity.process(claim);

        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }
}
