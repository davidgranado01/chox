package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

public class UpdateManualInvoiceContested extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateManualInvoiceContested.class);

    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void doProcess(Claim claim) {

        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        LOG.debug("Claim {} have been moved to manual invoice contested status", claim.getChoReference());
    }

}