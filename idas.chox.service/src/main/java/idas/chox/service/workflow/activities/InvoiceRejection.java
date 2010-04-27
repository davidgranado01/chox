package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class InvoiceRejection extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private int reasonOfRejectionId;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }
    // </editor-fold>

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_CH") && !securityInfoProvider.isInRoleOf("ROLE_INS_SCR")
                    && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to reject invoice.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {

        if (getReasonOfRejection() != null) {
            claim.addComment(Comment.New(0, "Reason For Rejection: " + getReasonOfRejection().getName()));
        }
        
        claim.getInvoice().setReasonOfRejection(getReasonOfRejection());
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }

    protected ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection reasonOfRejection = null;
        if (reasonOfRejectionId > 0) {
            reasonOfRejection = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }
        return reasonOfRejection;
    }
    
    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), null, getReasonOfRejection());

        if (chainActivity != null) {
            chainActivity.setWorkflowContext(processContext);
            chainActivity.processInBatch(claim);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
    }

}
