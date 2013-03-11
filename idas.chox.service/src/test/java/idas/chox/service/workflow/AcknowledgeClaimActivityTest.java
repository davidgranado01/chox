package idas.chox.service.workflow;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class AcknowledgeClaimActivityTest  extends BaseTest {

    @Test(expected = AccessDeniedException.class)
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
        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
    }
    
    @Test
    public void testAcknowledgeSubscriberClaim() throws Exception {

        Claim claim = new Claim();
        claim.setId(999);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        
        claim.setComments(null);

        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
        Assert.assertEquals("Liability status changed to 'null'", claim.getComments().get(0).getComment());
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

        Activity activity = activityFactory.getActivity("acknowledgeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
        Assert.assertEquals("Liability status changed to 'null'", claim.getComments().get(0).getComment());
    }
    
}
