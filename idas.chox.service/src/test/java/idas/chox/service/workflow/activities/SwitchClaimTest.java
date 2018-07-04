package idas.chox.service.workflow.activities;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class SwitchClaimTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSwitchClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("switchClaim");
        activity.process(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchClaim() throws Throwable {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Insurer insurer = insurerService.getInsurer(6);
        Insurer relatedInsurer = insurerService.getInsurer(3);
        insurer.setRelatedInsurer(relatedInsurer);
        claim.setInsurer(insurer);
        
        Assert.assertEquals("RBS", claim.getInsurer().getName());
        SwitchClaim activity = (SwitchClaim) activityFactory.getActivity("switchClaim");
        activity.process(claim);
        Assert.assertEquals("RSA", claim.getInsurer().getName());

    }
}
