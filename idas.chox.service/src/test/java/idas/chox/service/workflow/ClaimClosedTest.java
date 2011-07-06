package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ClaimService;
import idas.chox.service.workflow.activities.ClaimClosed;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-Workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class ClaimClosedTest {

    @Autowired
    ActivityFactory activityFactory;

    @Autowired
    ClaimService claimService;

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testClaimClosed() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        ClaimClosed activity = (ClaimClosed) activityFactory.getActivity("closeClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_CLOSED, claim.getStatus());
    }
}
