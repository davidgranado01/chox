package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.LiabilityStatus;

public class UpdateManualInvoiceAgreeQuantum extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateManualInvoiceAgreeQuantum.class);

    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void doProcess(Claim claim) {

        if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_ACCEPTED
                || claim.getLiabilityStatus() == LiabilityStatus.PROCEED_WITHOUT_PREJUDICE
                || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_SPLIT) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        }
    }

}
