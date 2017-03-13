package idas.chox.service.workflow.activities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;


public class NewSupplementaryInvoice extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewSupplementaryInvoice.class);
    private boolean isNewClaim = false;
    protected RulesEngineResponse breResponse = null;

    public boolean isIsNewClaim() {
        return isNewClaim;
    }

    public RulesEngineResponse getBreResponse() {
        return breResponse;
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        if (claim.getStatus() == null) {
            Claim originalSupplementaryInvoicedClaim = getWorkflowContext().getClaimService().getOriginalSupplementaryInvoicedClaim(claim.getCustomer().getClaimReference());
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
//            claim.setSupplementaryInvoicedClaim(true); -- this is now set when original claim is cloned
            claim.setStatusModifiedDate(new Date());
            if (claim.getBreBand() == null) {
                BreBand choBand = getWorkflowContext().getBreBandService().getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
            }
            if (originalSupplementaryInvoicedClaim!=null) {
                Comment comment = Comment.newComment(0, "This is a supplementary Invoice. The original claim's supplier reference is "+originalSupplementaryInvoicedClaim.getChoReference()+".");
                claim.addComment(comment);
            } else {
                Comment comment = Comment.newComment(0, "This is a supplementary Invoice.");
                claim.addComment(comment);
            }
            if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {

                Comment comment = Comment.newComment(0, "CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
            }
            isNewClaim = true;
        }
        LOG.debug("Finished NewSupplementaryInvoice activity for claim '{}': invoice is {}", claim.getChoReference(), claim.getInvoice());
    }
    
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
        getDataService().save(claim);
        LOG.trace("Claim saved - logging transaction...");
        logTransaction(claim);
        LOG.trace("Claim saved & transaction logged.");

//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });

        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
            if (ClaimType.isInsurerUpload(claim.getClaimType())) {
                breResponse = ((InsurerUpload)getChainActivity()).getBreResponse();
            } else {
                breResponse = ((NewInvoice)getChainActivity()).getBreResponse();
            }
        }
    }


    @Override
    protected String getCurrentStatus() {
        return "";
    }
}
