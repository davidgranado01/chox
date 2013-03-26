package idas.chox.service.workflow.activities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.services.BreBandService;


public class NewSupplementaryInvoice extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewSupplementaryInvoice.class);

    @Override
    protected void doProcess(Claim claim) throws Exception {

        BreBandService breBandService = getWorkflowContext().getBreBandService();
        if (claim.getStatus() == null) {
            Claim originalSupplementaryInvoicedClaim = getWorkflowContext().getClaimService().getOriginalSupplementaryInvoicedClaim(claim.getCustomer().getClaimReference());
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
//            claim.setSupplementaryInvoicedClaim(true); -- this is now set when original claim is cloned
            claim.setStatusModifiedDate(new Date());
            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
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
        }
        LOG.debug("Finished NewSupplementaryInvoice activity for claim '{}': invoice is {}", claim.getChoReference(), claim.getInvoice());
    }

    @Override
    protected String getCurrentStatus() {
        return "";
    }
}
