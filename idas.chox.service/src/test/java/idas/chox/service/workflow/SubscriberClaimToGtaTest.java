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
public class SubscriberClaimToGtaTest extends BaseTest {

    @Test(expected = InvalidClaimStatusException.class)
    public void testSubscriberClaimRejectionContestWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberClaimRejectionContest() throws Throwable {
        Claim claim = claimService.getClaim(999);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        // TODO: here we should check that the status has been set to its "previous" status
        //        - need to set up audit trail. For now, we'll check that the status has changed...
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Subscriber"));
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setClaimType(ClaimType.FIXED_FEE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        // TODO: here we should check that the status has been set to its "previous" status
        //        - need to set up audit trail. For now, we'll check that the status has changed...
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Fixed Fee"));
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeOriginalClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setClaimType(ClaimType.FIXED_FEE_ORIGINAL_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        // TODO: here we should check that the status has been set to its "previous" status
        //        - need to set up audit trail. For now, we'll check that the status has changed...
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA_ORIGINAL_INVOICE);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Fixed Fee"));
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSuplementaryClaimRejectionContest() throws Throwable {
        Claim claim = new Claim();
        claim.setClaimType(ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("sendClaimGTA");

        activity.process(claim);
        // TODO: here we should check that the status has been set to its "previous" status
        //        - need to set up audit trail. For now, we'll check that the status has changed...
        Assert.assertNotSame(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getStatus());
        Assert.assertEquals(claim.getClaimType(), ClaimType.GTA_SUPPLEMENTARY_INVOICE);
        Assert.assertTrue(claim.getComments().get(0).getComment().contains("Fixed Fee"));
    }

}
