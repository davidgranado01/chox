package idas.chox.service.workflow.activities;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

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
public class SubscriberClaimToGtaTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSubscriberClaimRejectionContestWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");
        activity.process(claim);
    }
    
    @Test(expected = AccessDeniedException.class)
    public void testAttemptToSwichToGtaStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.GTA);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");
        activity.process(claim);
    }

    @Test
    public void testSubscriberClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setId(997);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Subscriber"));
    }
    
    @Test
    public void testFixedFeeClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setId(996);
        claim.setClaimType(ClaimType.FIXED_FEE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Fixed Fee"));
    }
    
    @Test
    public void testFixedFeeOriginalClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setId(996);
        claim.setClaimType(ClaimType.FIXED_FEE_ORIGINAL_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA_ORIGINAL_INVOICE);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Fixed Fee"));
    }
    
    @Test
    public void testFixedFeeSuplementaryClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setId(996);
        claim.setClaimType(ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA_SUPPLEMENTARY_INVOICE);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Fixed Fee"));
    }

}
