package idas.chox.service.workflow.activities;


import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.WebUser;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class EcdUpdateTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(2);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(false);
    }
    
    @Test(expected = AccessDeniedException.class)
    public void testEcdUpdateWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Activity activity = activityFactory.getActivity("ecdUpdate");
        activity.process(claim);
    }

    @Test(expected = Exception.class)
    public void testEcdUpdateWithInvalidReasonOfDelay() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        
        EcdUpdate activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
        activity.setReasonOfDelayId(0);
        activity.process(claim);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testEcdUpdateWithValidReasonOfDelay() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        
        EcdUpdate activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("Tesing ECD Update with valid entry");
        activity.setUpdateInsurer(true);
        activity.setSequence(1);
        activity.process(claim);
        
        List<HireMonitoringEcd> hireMonitoringEcds = hireMonitoringEcdService.getHireMonitoringEcdsByClaimId(claim.getId());
        Assert.assertEquals(hireMonitoringEcds.size(), 1);
    }
    
}
