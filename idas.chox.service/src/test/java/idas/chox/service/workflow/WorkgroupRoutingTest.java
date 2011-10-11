package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.WorkgroupRouting;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class WorkgroupRoutingTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    InsurerService insurerService;

    @Test(expected = InvalidClaimStatusException.class)
    public void testAutoRoutingWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Activity activity = activityFactory.getActivity("workgroupRouting");
        activity.process(claim);
    }

    @Test
    public void testWorkgroupRouting1() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(false);
        claim.setInsurer(insurer);
        ThirdParty thirdParty = new ThirdParty();
        claim.setThirdParty(thirdParty);
        claim.getThirdParty().setPolicyNumber("0002001029");
        WorkgroupRouting activity = (WorkgroupRouting) activityFactory.getActivity("workgroupRouting");

//        activity.setOasWorkgroupId(101);
//        activity.setClaimOwnerId(999);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }
    
    public void testWorkgroupRouting2() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        ThirdParty thirdParty = new ThirdParty();
        claim.setThirdParty(thirdParty);
        claim.getThirdParty().setPolicyNumber("0002001029");
        WorkgroupRouting activity = (WorkgroupRouting) activityFactory.getActivity("workgroupRouting");

//        activity.setOasWorkgroupId(101);
//        activity.setClaimOwnerId(999);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }
}
