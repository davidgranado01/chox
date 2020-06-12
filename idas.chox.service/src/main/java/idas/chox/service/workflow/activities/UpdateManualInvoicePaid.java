package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

import java.math.BigDecimal;

public class UpdateManualInvoicePaid extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateManualInvoicePaid.class);

    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setAppliedLiability(BigDecimal.valueOf(100));
        InvoicePaymentLogged.updateClaimInvoice(claim, false);

        claim.setStatus(ClaimStatus.MANUAL_INVOICE_PAID);
        selectRandomlyForAuditReview(claim);
        LOG.debug("Claim {} have been moved to manual invoice paid status", claim.getChoReference());
    }

}