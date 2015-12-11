package idas.chox.service.workflow.activities;

import idas.chox.core.model.BreBand;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

/**
 *
 * @author John
 */
public class SubscriberClaimRejectionContestTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSubscriberClaimRejectionContestWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("contestRejectedClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberClaimRejectionContest() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setBreBand(new BreBand());
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claim.setPreviousStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("contestRejectedClaim");

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTION_CONTESTED, claim.getStatus());
    }

}
