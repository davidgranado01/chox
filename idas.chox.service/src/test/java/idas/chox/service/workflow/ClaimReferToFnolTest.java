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
import idas.chox.service.workflow.activities.ClaimReferToFnol;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class ClaimReferToFnolTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    InsurerService insurerService;

    @Test(expected = InvalidClaimStatusException.class)
    public void testClaimReferToFnolWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("referFNOL");
        activity.process(claim);
    }

    @Test
    public void testClaimReferToFnol() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        ClaimReferToFnol activity = (ClaimReferToFnol) activityFactory.getActivity("referFNOL");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REFERRED_TO_FNOL, claim.getStatus());
    }
}
