package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class ClaimRejectionContestTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testClaimRejectionContestWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("contestRejectedClaim");
        activity.process(claim);
    }

    @Test
    public void testClaimRejectionContest() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        claim.setPreviousStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        ClaimRejectionContest activity = (ClaimRejectionContest) activityFactory.getActivity("contestRejectedClaim");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTION_CONTESTED, claim.getStatus());
    }
}
