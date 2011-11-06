package idas.chox.service.workflow;

import idas.chox.admin.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ClaimRejection;
import org.junit.Assert;
import org.junit.Test;

public class ClaimRejectionTest extends BaseTest{

    @Test(expected = InvalidClaimStatusException.class)
    public void testClaimRejectionWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("rejectClaim");
        activity.process(claim);
    }

    @Test
    public void testClaimRejection() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        ClaimRejection activity = (ClaimRejection) activityFactory.getActivity("rejectClaim");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTED, claim.getStatus());
    }
}
