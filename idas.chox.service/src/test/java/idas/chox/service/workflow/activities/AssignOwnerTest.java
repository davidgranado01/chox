package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class AssignOwnerTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testAssignOwnerWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("assignOwner");
        activity.process(claim);
    }

    @Test
    public void testAssignOwner() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        AssignOwner activity = (AssignOwner) activityFactory.getActivity("assignOwner");

        activity.setOasWorkgroupId(101);
        activity.setClaimOwnerId(8);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
        Assert.assertNotNull(claim.getClaimOwner());
    }
}
