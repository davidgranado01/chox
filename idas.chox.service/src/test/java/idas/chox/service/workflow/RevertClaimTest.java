package idas.chox.service.workflow;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.RevertClaim;

public class RevertClaimTest extends BaseTest{

    @Test(expected = InvalidClaimStatusException.class)
    public void testRevertClaimWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("revertClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testRevertClaim() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(chorganisation);
        claim.setInsurer(insurer);
        claim.setChoReference("testing");
        claim.setManagingRepair(false);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim);
        RevertClaim activity = (RevertClaim) activityFactory.getActivity("revertClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
    }
}
