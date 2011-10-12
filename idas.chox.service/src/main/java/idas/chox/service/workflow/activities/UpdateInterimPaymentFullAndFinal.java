package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateInterimPaymentFullAndFinal extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateInterimPaymentFullAndFinal.class);

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) || (securityInfoProvider.isInRoleOf("ROLE_CHO") && (claim.getChorganisation().getId().compareTo(securityInfoProvider.getCurrentUser().getChorganisation().getId())) != 0)) {
            throw new AccessDeniedException("Not in correct role to update interim Payment full and final.");
        }

    }

    @Override
    protected void doProcess(Claim claim) {

        if (claim.getInvoice().getInterimPayment().compareTo(BigDecimal.ZERO) > 0) {

            claim.getInvoice().setInterimPaymentReceived(true);
            claim.getInvoice().setInterimPaymentReceivedFullAndFinal(true);
            claim.getInvoice().setTotalToPay(claim.getInvoice().getInterimPayment());

            if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
                if (!claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                    claim.setPreviousStatus(claim.getStatus());
                    claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                    logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
                    LOG.debug(" AWAITING_INVOICE_PAYMENT : AuditTrail has been updated");
                }
                claim.setPreviousStatus(claim.getStatus());
                claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
                LOG.debug("INVOICE_PAYMENT_LOGGED : AuditTrail has been updated");
            }

        } else {
            LOG.error(" Trying to update interim payment received full and final when there is no interim payment amount for this claim: {} by {}", claim.getChoReference(), this.getWorkflowContext().getSecurityInfoProvider().getCurrentUser().getDisplayName());
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
    }
}