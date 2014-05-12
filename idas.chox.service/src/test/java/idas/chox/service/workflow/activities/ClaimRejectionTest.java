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

public class ClaimRejectionTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testClaimRejectionWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimNumber("0001");
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("rejectClaim");
        activity.process(claim);
    }

    @Test
    public void testClaimRejection() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimNumber("0001");
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        ClaimRejection activity = (ClaimRejection) activityFactory.getActivity("rejectClaim");
        activity.setReasonOfRejectionId(new Integer(1));


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTED, claim.getStatus());
    }
}
