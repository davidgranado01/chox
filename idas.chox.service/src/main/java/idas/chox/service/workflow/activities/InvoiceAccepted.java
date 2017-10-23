package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LiabilityStatus;

public class InvoiceAccepted extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        if ( !ClaimType.isInsurerVsInsurer(claim.getClaimType()) &&
            ( claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
             || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
             || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
             || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED)) {
            claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        } else {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        }
    }

}
