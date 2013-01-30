package idas.chox.service.workflow;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.SwitchClaim;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class SwitchClaimTest extends BaseTest {

    @Test(expected = InvalidClaimStatusException.class)
    public void testSwitchClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("switchClaim");
        activity.process(claim);
    }

    //TODO remove expected and read the claim from import file.
    @Test(expected = InvalidClaimStatusException.class)
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
