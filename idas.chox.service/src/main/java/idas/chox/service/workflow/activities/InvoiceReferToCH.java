package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

public class InvoiceReferToCH extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.INVOICE_REF_TO_CH);
    }
}
