package idas.chox.service.workflow.activities;

import idas.chox.core.model.Chorganisation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;
import org.springframework.security.access.AccessDeniedException;

public class SlaExtensionTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSlaExtensionWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Activity activity = activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSlaExtensionFor14Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(14);
        activity.process(claim);

        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals(comment.getComment(), "14 days extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 14);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSlaExtensionFor10Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(10);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals(comment.getComment(), "10 days extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 10);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testFixedFeeSlaExtensionFor15Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(15);
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberSlaExtensionFor5Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(5);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals(comment.getComment(), "5 days extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 5);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberSlaExtensionFor4Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(4);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals(comment.getComment(), "4 days extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 4);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testSubscriberSlaExtensionFor6Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(6);
        activity.process(claim);
    }
    
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSlaExtensionFor19DaysWithMaxAllowed5Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        Chorganisation cho =  chorganisationService.getChorganisation(1006);
        cho.setMaxAllowedSlaExtForFixedFee(5);
        claim.setChorganisation(cho);
        


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(19);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals(comment.getComment(), "19 days extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 19);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberSlaExtensionFor10DaysWithMaxAllowed5Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        Chorganisation cho =  chorganisationService.getChorganisation(1006);
        cho.setMaxAllowedSlaExtForSubscriber(5);
        claim.setChorganisation(cho);


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(10);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals(comment.getComment(), "10 days extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 10);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberSlaExtensionFor1Day() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        Chorganisation cho =  chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(1);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("day extension granted.")) {
                Assert.assertEquals(comment.getComment(), "1 day extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 1);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSlaExtensionFor1Day() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        Chorganisation cho =  chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);


        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(1);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("day extension granted.")) {
                Assert.assertEquals(comment.getComment(), "1 day extension granted.");
            }
        }
        Assert.assertEquals(claim.getSlaExtDays(), 1);
    }
}
