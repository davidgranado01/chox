package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class UpdateLiabilityTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testUpdateLiabilityWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("updateLiability");
        activity.process(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testUpdateLiability() throws Throwable {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        UpdateLiability activity = (UpdateLiability) activityFactory.getActivity("updateLiability");
        activity.setLiabilityStatus(LiabilityStatus.LIABILITY_DISPUTED);
        activity.setPercentageLiabilityAccepted(BigDecimal.ZERO);
        activity.setPercentageLiabilityCho(BigDecimal.ZERO);
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }
}
