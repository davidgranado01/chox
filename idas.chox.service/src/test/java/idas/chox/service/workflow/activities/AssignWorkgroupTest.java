package idas.chox.service.workflow.activities;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class AssignWorkgroupTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testAssignworkGroupInvalidStatus() throws Exception {

        Insurer insurer = insurerService.getInsurer(3);
        Claim claim = new Claim();
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        Activity activity = activityFactory.getActivity("assignWorkgroup");
        activity.process(claim);
    }

    @Test
    public void testAssignworkGroup_ClaimOwnershipEnabled() throws Throwable {

        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(true);
        Claim claim = new Claim();
        claim.setInsurer(insurer);
        
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        AssignWorkgroup activity = (AssignWorkgroup) activityFactory.getActivity("assignWorkgroup");
        activity.setWorkgroupId(101);
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }

    @Test
    public void testAssignworkGroup_ClaimOwnershipDisabled() throws Throwable {

        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(false);
        Claim claim = new Claim();
        claim.setInsurer(insurer);

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        AssignWorkgroup activity = (AssignWorkgroup) activityFactory.getActivity("assignWorkgroup");
        activity.setWorkgroupId(101);
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }

}
