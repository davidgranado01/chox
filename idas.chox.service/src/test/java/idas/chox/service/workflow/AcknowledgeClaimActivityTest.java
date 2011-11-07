package idas.chox.service.workflow;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import org.junit.Assert;
import org.junit.Test;

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
}
