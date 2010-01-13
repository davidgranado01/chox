/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.AssignOwner;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class AssignOwnerTest {

    @Autowired
    ActivityFactory activityFactory;

    @Test(expected = InvalidClaimStatusException.class)
    public void testAssignOwnerWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("assignOwner");
        activity.process(claim);
    }

    @Test
    public void testAssignOwner() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        AssignOwner activity = (AssignOwner) activityFactory.getActivity("assignOwner");

        activity.setOasWorkgroupId(1);
        activity.setClaimOwnerId(999);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
        Assert.assertNotNull(claim.getWorkgroup());
        Assert.assertNotNull(claim.getClaimOwner());
    }
}
