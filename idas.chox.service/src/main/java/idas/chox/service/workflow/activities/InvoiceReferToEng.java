package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

public class InvoiceReferToEng extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.INVOICE_REF_TO_ENG);
    }    
}
