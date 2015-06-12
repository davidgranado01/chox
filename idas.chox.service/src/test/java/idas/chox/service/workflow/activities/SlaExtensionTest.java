package idas.chox.service.workflow.activities;

import idas.chox.bre.mock.MockObjects;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import org.junit.Assert;
import org.junit.Test;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class SlaExtensionTest extends BaseTest {
    MockObjects testClaim = new MockObjects();

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
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(14);
        activity.process(claim);

        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals("14 days extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(14, claim.getSlaExtDays());
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSlaExtensionFor10Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(10);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals("10 days extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(10, claim.getSlaExtDays());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testFixedFeeSlaExtensionFor15Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

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
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(5);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals("5 days extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(5, claim.getSlaExtDays());
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberSlaExtensionFor4Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(4);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals("4 days extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(4, claim.getSlaExtDays());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testSubscriberSlaExtensionFor6Days() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        claim.setChorganisation(chorganisationService.getChorganisation(1006));
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

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
        claim.setInsurer(insurerService.getInsurer(3));        
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(19);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals("19 days extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(19, claim.getSlaExtDays());
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
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(10);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("days extension granted.")) {
                Assert.assertEquals("10 days extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(10, claim.getSlaExtDays());
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSubscriberSlaExtensionFor1Day() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        Chorganisation cho =  chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(1);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("day extension granted.")) {
                Assert.assertEquals("1 day extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(1, claim.getSlaExtDays());
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testFixedFeeSlaExtensionFor1Day() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.FIXED_FEE);
        Chorganisation cho =  chorganisationService.getChorganisation(1006);
        claim.setChorganisation(cho);
        claim.setInsurer(insurerService.getInsurer(3));
        claim.setBreBand(testClaim.getTestBreBand());

        SlaExtension activity = (SlaExtension) activityFactory.getActivity("slaExtensionDaysUpdate");
        activity.setSlaExtDays(1);
        activity.process(claim);
        for (Comment comment : claim.getComments()) {
            if (comment.getComment().contains("day extension granted.")) {
                Assert.assertEquals("1 day extension granted.", comment.getComment());
            }
        }
        Assert.assertEquals(1, claim.getSlaExtDays());
    }
}
