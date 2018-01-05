package idas.chox.service.workflow.activities;


import idas.chox.core.model.Chorganisation;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.InsurerHireMonitoringEcd;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class InsurerEcdUpdateTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(3);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(true);
    }

    @After
    public void tearDownClass() throws Exception {
        fakeSecurityInfoProvider.setIsINS(false);
         
        WebUserRole webUserRole = new WebUserRole();
        webUserRole.setName(WebUserRole.ROLE_CHO_OPR);
        Set roles = new HashSet();
        roles.add(webUserRole);
        
        WebUser currentUser = new WebUser();
        currentUser.setId(999);
        currentUser.setFirstName("UnitTest");
        currentUser.setLastName("User");
        currentUser.setVersion(1);
        currentUser.setRoles(roles);
        

        // SET CHORGANISATION
        Chorganisation chorganisation = new Chorganisation();
        chorganisation.setId(1006);
        chorganisation.setVersion(1);
        currentUser.setChorganisation(chorganisation);
        fakeSecurityInfoProvider.setCurrentUser(currentUser);
    }

    @Test(expected = AccessDeniedException.class)
    public void testInsurerEcdUpdateWithInvalidStatus1() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        Activity activity = activityFactory.getActivity("insurerEcdUpdate");
        activity.process(claim);
    }

    @Test(expected = AccessDeniedException.class)
    public void testInsurerEcdUpdateWithInvalidStatus2() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        Activity activity = activityFactory.getActivity("insurerEcdUpdate");
        activity.process(claim);
    }

    @Test(expected = AccessDeniedException.class)
    public void testInsurerEcdUpdateWithInvalidStatus3() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        Activity activity = activityFactory.getActivity("insurerEcdUpdate");
        activity.process(claim);
    }

    @Test(expected = AccessDeniedException.class)
    public void testInsurerEcdUpdateWithInvalidStatus4() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_CLOSED);
        Activity activity = activityFactory.getActivity("insurerEcdUpdate");
        activity.process(claim);
    }

    @Test(expected = Exception.class)
    public void testDuplicateInsurerEcdUpdate() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        
        InsurerEcdUpdate activity = (InsurerEcdUpdate) activityFactory.getActivity("insurerEcdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("Tesing Insurer ECD Update with valid entry");
        activity.setSequence(1);
        activity.process(claim);
        
        List<InsurerHireMonitoringEcd> hireMonitoringEcds = insurerHireMonitoringEcdService.getInsurerHireMonitoringEcdsByClaimId(claim.getId());
        Assert.assertEquals(1, hireMonitoringEcds.size());
        
        // Adding same insurer ECD should now cause an exception
        activity.process(claim);
    }

    @Test(expected = Exception.class)
    public void testInsurerEcdUpdateWithInvalidReasonOfDelay() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        
        InsurerEcdUpdate activity = (InsurerEcdUpdate) activityFactory.getActivity("insurerEcdUpdate");
        activity.setReasonOfDelayId(0);
        activity.process(claim);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testInsurerEcdUpdateWithValidReasonOfDelay() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        
        InsurerEcdUpdate activity = (InsurerEcdUpdate) activityFactory.getActivity("insurerEcdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("Tesing Insurer ECD Update with valid entry");
        activity.setSequence(1);
        activity.process(claim);
        
        List<InsurerHireMonitoringEcd> hireMonitoringEcds = insurerHireMonitoringEcdService.getInsurerHireMonitoringEcdsByClaimId(claim.getId());
        Assert.assertEquals(1, hireMonitoringEcds.size());
    }
    
}
