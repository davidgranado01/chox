package idas.chox.service.workflow;

import idas.chox.admin.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ClaimOwnerAssigning;
import junit.framework.Assert;
import org.junit.Test;

public class ClaimOwnerAssigningTest extends BaseTest{

    @Test(expected = InvalidClaimStatusException.class)
    public void testAutoRoutingWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Activity activity = activityFactory.getActivity("claimOwnerAssigning");
        activity.process(claim);
    }

    @Test
    public void testAutoRouting() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        ClaimOwnerAssigning activity = (ClaimOwnerAssigning) activityFactory.getActivity("claimOwnerAssigning");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
    }
}
