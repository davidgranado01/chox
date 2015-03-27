package idas.chox.service.workflow.activities;

import idas.chox.core.model.Chorganisation;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class SwitchChoTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSwitchClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Activity activity = activityFactory.getActivity("switchCho");
        activity.process(claim);
    }

    @Test(expected = AccessDeniedException.class)
    public void testSwitchClaimWithInvalidStatus2() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_CLOSED);
        Activity activity = activityFactory.getActivity("switchCho");
        activity.process(claim);
    }

    @Test(expected = AccessDeniedException.class)
    public void testSwitchClaimWithInvalidStatus3() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        Activity activity = activityFactory.getActivity("switchCho");
        activity.process(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());

        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess2() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess3() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess4() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess5() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess6() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_PENDING);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess7() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess8() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess9() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess10() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess11() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test
    public void testSwitchChoSuccess12() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        claim.setClaimType(ClaimType.GTA);
        claim.getChorganisation().setLinkedCho(linkedCho);
        linkedCho.setLinkedCho(claim.getChorganisation());
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
        Assert.assertEquals("Enterprise", claim.getChorganisation().getName());
    }
    
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testSwitchChoInvalidClaimTypeSubscriber() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setClaimType(ClaimType.SUBSCRIBER);
        linkedCho.setEnableSubscriberClaims(false);
        claim.getChorganisation().setLinkedCho(linkedCho);
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testSwitchChoInvalidClaimTypeFixedFee() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setClaimType(ClaimType.FIXED_FEE);
        linkedCho.setEnableFixedFeeClaims(false);
        claim.getChorganisation().setLinkedCho(linkedCho);
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Test(expected = Exception.class)
    public void testSwitchChoInvalidClaimTypeCollaboration() throws Throwable {

        Claim claim = claimService.getClaim(999);
        Chorganisation linkedCho = chorganisationService.getChorganisation(1007);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
        linkedCho.setEnableCollaborationProtocolClaims(false);
        claim.getChorganisation().setLinkedCho(linkedCho);
        
        Assert.assertEquals("Drive Assist", claim.getChorganisation().getName());
        SwitchCho activity = (SwitchCho) activityFactory.getActivity("switchCho");
        activity.process(claim);
    }
}
