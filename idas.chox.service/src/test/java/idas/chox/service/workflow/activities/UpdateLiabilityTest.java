package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class UpdateLiabilityTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testUpdateLiabilityWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("updateLiability");
        activity.process(claim);
    }

    @Test
    public void testUpdateLiability() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_DISPUTED);
     
        UpdateLiability activity = (UpdateLiability) activityFactory.getActivity("updateLiability");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }
}
