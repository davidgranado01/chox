package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

/*
 * This activity is used to move any claim to invoice payment logged from any status by CHO
 * This is mainly used when claim is at any status and CHO click payment received from more action.
 * This activity is called before claim move to paymentReceived status.
 */
public class MoveToInvoicePaymentLogged extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(MoveToInvoicePaymentLogged.class);

    @Override
    protected void doProcess(Claim claim) {

        if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
            if (!claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                claim.setPreviousStatus(claim.getStatus());
                claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
                LOG.debug(" AWAITING_INVOICE_PAYMENT : AuditTrail has been updated");
            }
            setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(claim.getStatus());
            claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
            LOG.debug("INVOICE_PAYMENT_LOGGED : AuditTrail has been updated");
        }
    }

}