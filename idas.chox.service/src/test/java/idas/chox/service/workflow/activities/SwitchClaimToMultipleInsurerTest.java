package idas.chox.service.workflow.activities;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class SwitchClaimToMultipleInsurerTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSwitchClaimToMultipleInsurerWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("switchClaimToMulIns");
        activity.process(claim);
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//    public void testwitchClaimToMultipleInsurer() throws Throwable {
//
//        Claim claim = claimService.getClaim(999);
//        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//
//        Assert.assertEquals("RSA", claim.getInsurer().getName());
//        SwitchClaimToMultipleInsurer activity = (SwitchClaimToMultipleInsurer) activityFactory.getActivity("switchClaimToMulIns");
//        activity.process(claim);
//        Assert.assertEquals("Motability", claim.getInsurer().getName());
//
//    }
}
