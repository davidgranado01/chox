package idas.chox.service.workflow;

import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.test.BaseTest;

public class AcknowledgeClaimActivityTest  extends BaseTest {

    @Test(expected = InvalidClaimStatusException.class)
    public void testAcknowledgeClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
    }

    @Test
    public void testAcknowledgeClaim() throws Exception {

        Claim claim = new Claim();
        
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
//        List<Comment> comments = new ArrayList<Comment>();
//        comments.add(Comment.New(0, "tesing comment"));
//        claim.setComments(comments);
        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
    }
    
    @Test
    public void testAcknowledgeSubscriberClaim() throws Exception {

//        Claim claim = claimService.getClaim(999);
        
        Claim claim = new Claim();
        claim.setId(999);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        
        claim.setComments(null);
        claim.setNotifications(null);

        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
        Assert.assertEquals("Liability status changed to 'null'", claim.getComments().get(0).getComment());
        Assert.assertEquals("RSA failed to respond to the Subscriber notification within the 5 day SLA, claim taken down Subscriber route.", claim.getComments().get(1).getComment());
    }
    
    @Test
    public void testAcknowledgeFixedFeeClaim() throws Exception {

        Claim claim = new Claim();
        claim.setId(998);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.FIXED_FEE);
        
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        
        claim.setComments(null);
        claim.setNotifications(null);

        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
        Assert.assertEquals("Liability status changed to 'null'", claim.getComments().get(0).getComment());
        Assert.assertEquals("RSA failed to respond to the Fixed Fee notification within the 10 day SLA, claim taken down Fixed Fee route.", claim.getComments().get(1).getComment());
    }
    
}
