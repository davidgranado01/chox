package idas.chox.service.workflow.activities;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class UpdateManualInvoiceContestedTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testUpdateManualInvoiceContestedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("updateManualInvoiceContested");
        activity.process(claim);
    }
}
