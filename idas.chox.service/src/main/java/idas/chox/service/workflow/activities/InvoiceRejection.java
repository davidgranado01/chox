package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import java.util.List;

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
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
    }

}
