/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.UpdateLiability;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class UpdateLiabilityTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    InsurerService insurerService;
  

    @Test(expected = InvalidClaimStatusException.class)
    public void testUpdateLiabilityWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("updateLiability");
        activity.process(claim);
    }

    @Test
    public void testUpdateLiability() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_DISPUTED);
     
        UpdateLiability activity = (UpdateLiability) activityFactory.getActivity("updateLiability");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }
}
