package idas.chox.service.workflow;

import idas.chox.admin.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ClaimRejectionConstest;
import org.junit.Assert;
import org.junit.Test;

public class ClaimRejectionContestTest extends BaseTest {

    @Test(expected = InvalidClaimStatusException.class)
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
        ClaimRejectionConstest activity = (ClaimRejectionConstest) activityFactory.getActivity("contestRejectedClaim");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTION_CONTESTED, claim.getStatus());
    }
}
