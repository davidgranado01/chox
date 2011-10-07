package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ClaimRejectionAccept;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-Workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class ClaimRejectionAcceptTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    InsurerService insurerService;
    @Autowired
    ClaimService claimService;

    @Test(expected = InvalidClaimStatusException.class)
    public void testClaimRejectionAcceptWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("acceptRejectedClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testClaimRejectionAccept() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        ClaimRejectionAccept activity = (ClaimRejectionAccept) activityFactory.getActivity("acceptRejectedClaim");


        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_REJECTION_ACCEPTED, claim.getStatus());
    }
}
