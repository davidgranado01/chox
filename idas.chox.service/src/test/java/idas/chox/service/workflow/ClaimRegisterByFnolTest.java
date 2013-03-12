package idas.chox.service.workflow;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.activities.ClaimRegisterByFnol;
import idas.chox.test.BaseTest;

public class ClaimRegisterByFnolTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testClaimRegisterByFnolWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("registerFNOL");
        activity.process(claim);
    }

    @Test
    public void testClaimRegisterByFnol() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setPreviousStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        ClaimRegisterByFnol activity = (ClaimRegisterByFnol) activityFactory.getActivity("registerFNOL");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
    }
}
