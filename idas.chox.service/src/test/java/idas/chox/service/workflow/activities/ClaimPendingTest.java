package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class ClaimPendingTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testClaimPendingWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("pending");
        activity.process(claim);
    }

    @Test
    public void testClaimPending() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        ClaimPending activity = (ClaimPending) activityFactory.getActivity("pending");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_PENDING, claim.getStatus());
    }
}
