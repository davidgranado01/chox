package idas.chox.service.workflow;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.activities.ClaimReviewByEng;
import idas.chox.test.BaseTest;

public class ClaimReviewByEngTest extends BaseTest{


    @Test(expected = AccessDeniedException.class)
    public void testClaimReviewByEngWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("updatedByEng");
        activity.process(claim);
    }

    @Test
    public void testClaimReviewByEng() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setChoReference("testing");
        claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
        ClaimReviewByEng activity = (ClaimReviewByEng) activityFactory.getActivity("updatedByEng");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UPDATE_BY_ENG, claim.getStatus());
    }
}
