package idas.chox.service.workflow.activities;

import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;

public class AwaitingLitigationOutcome extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(AwaitingLitigationOutcome.class);
    private String supportingLiabilityNotes;

    @Override
    protected void doProcess(Claim claim) throws Exception {

        LOG.debug("Processing AwaitingLitigationOutcome activity.");

        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.newComment(0, supportingLiabilityNotes, true));
        }

        claim.setCaseWithClientsSolicitor(false);
        claim.setStatus(ClaimStatus.AWAITING_LITIGATION_OUTCOME);

    }
   
    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }
}