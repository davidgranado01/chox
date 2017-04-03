package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.events.BaseActivityEvent;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class InvoiceRejection extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRejection.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private Integer reasonOfRejectionId;
    private String rejectionDescription;
    // </editor-fold>

    @Override
    public boolean needsClaimLockedCheck() {
        return true;
    }

    public String getRejectionDescription() {
        return rejectionDescription;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (getReasonOfRejection() == null) {
            LOG.error("No 'Reason of Rejection' specified for claim '{}': {}", claim.getChoReference(), reasonOfRejectionId);
            throw new Exception("No 'Reason of Rejection' specified");
        }
    }

    @Override
    protected void doProcess(Claim claim) {

        claim.addComment(Comment.newComment(0, "Reason For Rejection: " + getReasonOfRejection().getRorName()));
        if(rejectionDescription != null && !rejectionDescription.equals("")) {
            claim.addComment(Comment.newComment(0, "Supporting Rejection Notes: " + rejectionDescription));
        }
        
        claim.getInvoice().setReasonOfRejection(getReasonOfRejection());
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }

    public ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection reasonOfRejection = null;
        if (reasonOfRejectionId != null && reasonOfRejectionId > 0) {
            reasonOfRejection = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }

        return reasonOfRejection;
    }
    
    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), null, claim.getInvoice().getReasonOfRejection());
//        activityEventGenerator.generate(claim, this);
        for (BaseActivityEvent event : activityEventGenerator.getEvents(claim, this)) {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        }

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }


    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    
    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

	public void setRejectionDescription(String rejectionDescription) {
		this.rejectionDescription = rejectionDescription;
	}

}
