package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;

public class InvoiceRejection extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRejection.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private int reasonOfRejectionId;
    private String supportingRejectionNotes;
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
            claim.addComment(Comment.New(0, "Supporting Rejection Notes: " + getSupportingRejectionNotes()));
        }
        else {
            LOG.error("No 'Reason of Rejection' specified for claim '{}': {}", claim.getChoReference(), reasonOfRejectionId);
        }
        
        claim.getInvoice().setReasonOfRejection(getReasonOfRejection());
       // claim.getInvoice().setSupportingRejectionNotes(getSupportingRejectionNotes());
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
        logTransaction(claim, getCurrentStatus(), null, claim.getInvoice().getReasonOfRejection());

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
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

    /**
     * @return the supportingRejectionNotes
     */
    public String getSupportingRejectionNotes() {
        return supportingRejectionNotes;
    }

    /**
     * @param supportingRejectionNotes the supportingRejectionNotes to set
     */
    public void setSupportingRejectionNotes(String supportingRejectionNotes) {
        this.supportingRejectionNotes = supportingRejectionNotes;
    }

    public int getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    
    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

}
