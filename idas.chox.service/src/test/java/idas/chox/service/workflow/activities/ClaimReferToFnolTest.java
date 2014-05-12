package idas.chox.service.workflow.activities;


import idas.chox.core.model.Chorganisation;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class ClaimReferToFnolTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testClaimReferToFnolWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimNumber("0001");
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("referFNOL");
        activity.process(claim);
    }

    @Test
    public void testClaimReferToFnol() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimNumber("0001");
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        ClaimReferToFnol activity = (ClaimReferToFnol) activityFactory.getActivity("referFNOL");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REFERRED_TO_FNOL, claim.getStatus());
    }
}
