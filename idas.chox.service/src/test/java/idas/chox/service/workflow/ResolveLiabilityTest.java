package idas.chox.service.workflow;

import idas.chox.admin.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ResolveLiability;
import org.junit.Assert;
import org.junit.Test;

public class ResolveLiabilityTest extends BaseTest{

    @Test(expected = InvalidClaimStatusException.class)
    public void testResolveLiabilityWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("resolveLiability");
        activity.process(claim);
    }

    @Test
    public void testResolveLiability() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
     
        ResolveLiability activity = (ResolveLiability) activityFactory.getActivity("resolveLiability");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }
}
