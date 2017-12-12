package idas.chox.service.workflow.activities;

import java.util.Date;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;


public class UpdateCaseWithSolicitor extends BaseActivity {

//    private static final Logger LOG = LoggerFactory.getLogger(UpdateCaseWithSolicitor.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private boolean caseWithSolicitor;
    // </editor-fold>

    public boolean isCaseWithSolicitor() {
        return caseWithSolicitor;
    }

    public void setCaseWithSolicitor(boolean caseWithSolicitor) {
        this.caseWithSolicitor = caseWithSolicitor;
    }


    @Override
    protected void doProcess(Claim claim) {
        claim.setCaseWithClientsSolicitor(caseWithSolicitor);
        claim.setDateMarkedWithSolicitor(new Date());
        claim.setUserMarkedWithSolicitor(this.getCurrentUser().getFullName());
        
        if (caseWithSolicitor) {
            // Add Note
            claim.addComment(Comment.newComment(2, "Case Marked As With Clients Solicitor."));
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
//        getDataService().save(claim);
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });
    }

}