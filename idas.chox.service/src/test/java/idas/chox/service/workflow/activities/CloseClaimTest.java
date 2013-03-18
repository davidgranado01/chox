package idas.chox.service.workflow.activities;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;

public class CloseClaimTest extends BaseTest{

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testCloseClaim() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        CloseClaim activity = (CloseClaim) activityFactory.getActivity("closeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_CLOSED, claim.getStatus());
    }
}
