package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

public class InvoicePaymentLogged extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
    }
}