package idas.chox.service.workflow.activities;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class WorkgroupRoutingTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testAutoRoutingWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Activity activity = activityFactory.getActivity("workgroupRouting");
        activity.process(claim);
    }

    @Test
    public void testWorkgroupRouting1() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(false);
        claim.setInsurer(insurer);
        ThirdParty thirdParty = new ThirdParty();
        claim.setThirdParty(thirdParty);
        claim.getThirdParty().setPolicyNumber("0002001029");
        WorkgroupRouting activity = (WorkgroupRouting) activityFactory.getActivity("workgroupRouting");

//        activity.setOasWorkgroupId(101);
//        activity.setClaimOwnerId(999);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }
    
    public void testWorkgroupRouting2() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        ThirdParty thirdParty = new ThirdParty();
        claim.setThirdParty(thirdParty);
        claim.getThirdParty().setPolicyNumber("0002001029");
        WorkgroupRouting activity = (WorkgroupRouting) activityFactory.getActivity("workgroupRouting");

//        activity.setOasWorkgroupId(101);
//        activity.setClaimOwnerId(999);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }
}
