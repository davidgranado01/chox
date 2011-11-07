package idas.chox.service.workflow;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.service.workflow.activities.ClaimClosed;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ClaimClosedTest extends BaseTest{

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testClaimClosed() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        ClaimClosed activity = (ClaimClosed) activityFactory.getActivity("closeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_CLOSED, claim.getStatus());
    }
}
