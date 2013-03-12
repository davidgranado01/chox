package idas.chox.service.workflow;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.activities.ClaimRejectionAccept;
import idas.chox.test.BaseTest;

public class ClaimRejectionAcceptTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testClaimRejectionAcceptWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("acceptRejectedClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testClaimRejectionAccept() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        ClaimRejectionAccept activity = (ClaimRejectionAccept) activityFactory.getActivity("acceptRejectedClaim");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTION_ACCEPTED, claim.getStatus());
    }
}
