package idas.chox.service.workflow;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ClaimReferToEng;
import org.junit.Assert;
import org.junit.Test;

public class ClaimReferToEngTest extends BaseTest{

    @Test(expected = InvalidClaimStatusException.class)
    public void testClaimReferToEngWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("referEng");
        activity.process(claim);
    }

    @Test
    public void testClaimReferToEng() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        ClaimReferToEng activity = (ClaimReferToEng) activityFactory.getActivity("referEng");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REF_TO_ENG, claim.getStatus());
    }
}
