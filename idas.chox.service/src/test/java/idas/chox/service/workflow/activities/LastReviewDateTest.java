package idas.chox.service.workflow.activities;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.workflow.Activity;
import java.util.List;

public class LastReviewDateTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testLastReviewDateWithInvalidStatus() throws Exception {
        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_CLOSED);
        Activity activity = activityFactory.getActivity("lastReviewDate");
        activity.process(claim);
    }

    @Test(expected = Exception.class)
    public void testLastReviewDateWithNoDate() throws Throwable {
        Claim claim = claimService.getClaim(999);
        LastReviewDate activity = (LastReviewDate) activityFactory.getActivity("lastReviewDate");
        activity.process(claim);
    }

    @Test(expected = Exception.class)
    public void testLastReviewDateWithFutureDate() throws Throwable {
        Claim claim = claimService.getClaim(999);
        LastReviewDate activity = (LastReviewDate) activityFactory.getActivity("lastReviewDate");
        activity.setLastReviewDate(new Date((new Date()).getTime() + (1000 * 60 * 60 * 24)));
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testLastReviewDate() throws Throwable {
        Claim claim = claimService.getClaim(999);
        List<Comment> comments = claim.getComments();
        Assert.assertEquals(0, comments.size());
        LastReviewDate activity = (LastReviewDate) activityFactory.getActivity("lastReviewDate");
        activity.setLastReviewDate(new Date());
        Assert.assertNull(claim.getLastReviewDate());
        activity.process(claim);
        Assert.assertNotNull(claim.getLastReviewDate());
        
        comments = claim.getComments();
        Assert.assertEquals(1, comments.size());
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testLastReviewDateClear() throws Throwable {
        Claim claim = claimService.getClaim(999);
        List<Comment> comments = claim.getComments();
        Assert.assertEquals(0, comments.size());
        claim.setLastReviewDate(new Date());
        LastReviewDate activity = (LastReviewDate) activityFactory.getActivity("lastReviewDate");
        Assert.assertNotNull(claim.getLastReviewDate());
        activity.process(claim);
        Assert.assertNull(claim.getLastReviewDate());
        comments = claim.getComments();
        Assert.assertEquals(1, comments.size());
    }
}
