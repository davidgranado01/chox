package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;

public class SwitchFromPaymentsTeam extends BaseActivity {


    @Override
    protected void doProcess(Claim claim) {
        claim.getInvoice().setPaymentTeam(false);
        claim.addComment(Comment.newComment(0, "Claim Switched From Payments Team to Claims Handler."));
    }
}