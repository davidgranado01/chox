package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.SwitchClaim;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class SwitchClaimTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    ClaimService claimService;
    @Autowired
    InsurerService insurerService;
   

    @Test(expected = InvalidClaimStatusException.class)
    public void testSwitchClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("switchClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testSwitchClaim() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        
        Insurer insurer = insurerService.getInsurer(3);
        
        ThirdParty thirdParty = new ThirdParty();
        claim.setInsurer(insurer);
        claim.setThirdParty(thirdParty);
        claimService.save(claim);
        SwitchClaim activity = (SwitchClaim) activityFactory.getActivity("switchClaim");

        
        activity.process(claim);
        Assert.assertEquals("RBS", claim.getInsurer().getName());
    }
}
