/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.AssignWorkgroup;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class AssignWorkgroupTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    InsurerService insurerService;

    @Test(expected = InvalidClaimStatusException.class)
    public void testAssignworkGroupInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("assignWorkgroup");
        activity.process(claim);
    }

    @Test
    public void testAssignworkGroup_ClaimOwnershipEnabled() throws Throwable {

        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(true);
        Claim claim = new Claim();
        claim.setInsurer(insurer);
        
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        AssignWorkgroup activity = (AssignWorkgroup) activityFactory.getActivity("assignWorkgroup");
        activity.setWorkgroupId(1);
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }

    @Test
    public void testAssignworkGroup_ClaimOwnershipDisabled() throws Throwable {

        Insurer insurer = insurerService.getInsurer(3);
        insurer.setClaimOwnershipEnable(false);
        Claim claim = new Claim();
        claim.setInsurer(insurer);

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        AssignWorkgroup activity = (AssignWorkgroup) activityFactory.getActivity("assignWorkgroup");
        activity.setWorkgroupId(1);
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
    }

}
