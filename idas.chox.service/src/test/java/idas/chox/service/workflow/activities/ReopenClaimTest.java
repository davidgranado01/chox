package idas.chox.service.workflow.activities;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class ReopenClaimTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testReopenClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("reopenClaim");
        activity.process(claim);
    }

    @Test
//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testReopenClaim() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setBreBand(breBandService.getBreBand(101));
        claim.setStatus(ClaimStatus.CLAIM_CLOSED);
        claim.setPreviousStatus(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        auditTrailService.logAuditLog(claim.getStatus(), claim.getPreviousStatus(), claim);
        ReopenClaim activity = (ReopenClaim) activityFactory.getActivity("reopenClaim");
        activity.process(claim);
        claim = claimService.getClaim(claim.getId());
        Assert.assertEquals(ClaimStatus.CLAIM_UPDATE_BY_ENG, claim.getStatus());
    }
}
