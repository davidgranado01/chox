package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class ResolveLiabilityTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testResolveLiabilityWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("resolveLiability");
        activity.process(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testResolveLiability() throws Throwable {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
     
        ResolveLiability activity = (ResolveLiability) activityFactory.getActivity("resolveLiability");
        activity.setLiabilityStatus(LiabilityStatus.LIABILITY_ACCEPTED);
        activity.setPercentageLiabilityCho(BigDecimal.ZERO);
        activity.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }
}
