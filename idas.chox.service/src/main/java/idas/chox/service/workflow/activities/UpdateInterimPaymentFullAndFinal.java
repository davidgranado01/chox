package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;

public class UpdateInterimPaymentFullAndFinal extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateInterimPaymentFullAndFinal.class);
    private ActivityFactory activityFactory;
    
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

        if (claim.getInvoice().getInterimPaymentMade().compareTo(BigDecimal.ZERO) > 0) {

            claim.getInvoice().setInterimPaymentReceivedFullAndFinal(true);
            claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentMade());
            claim.getInvoice().setTotalToPay(claim.getInvoice().getInterimPaymentMade());
            
            if (claim.getStatus().equals(ClaimStatus.CLAIM_CLOSED)) {
                try {
                    Activity activity = activityFactory.getActivity("reopenClaim");
                    activity.process(claim);
                } catch (Exception ex) {
                    LOG.debug(" Exception thrown re-opening claim in InterimPaymentFullAndFinal activity: ", ex);
                }
            }
            
            if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)) {
                if (!claim.getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
                    logTransaction(claim, claim.getStatus(), ClaimStatus.AWAITING_INVOICE_PAYMENT, 0);
                    LOG.debug(" AWAITING_INVOICE_PAYMENT : AuditTrail has been updated");
                }
                setCurrentStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
                claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
                LOG.debug("INVOICE_PAYMENT_LOGGED : AuditTrail has been updated");
            } else if (claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_RECEIVED)) {
                // if the claim status is payment received then do not change the claim status via paymentreceived chain activity.
                super.setChainActivity(null);
            } 

        } else {
            LOG.error("Trying to update interim payment received full and final when there is no interim payment amount for this claim: {} by {}", claim.getChoReference(), this.getWorkflowContext().getSecurityInfoProvider().getCurrentUser().getDisplayName());
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
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
        expectingStatuses.add(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
    }

    public ActivityFactory getActivityFactory() {
        return activityFactory;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}