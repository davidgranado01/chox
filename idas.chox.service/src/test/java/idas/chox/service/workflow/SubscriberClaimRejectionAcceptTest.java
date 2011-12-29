package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.test.BaseTest;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author John
 */
public class SubscriberClaimRejectionAcceptTest extends BaseTest {

    @Test(expected = InvalidClaimStatusException.class)
    public void testSubscriberClaimRejectionAcceptWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("acceptSubscriberChallenge");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberClaimRejectionAccept() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("acceptSubscriberChallenge");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, claim.getStatus());
    }

}
