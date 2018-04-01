package idas.chox.service.workflow.activities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.util.DateHelper;

public class LastReviewDate extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(LastReviewDate.class);
    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private Date lastReviewDate;
    private String lastReviewNote;
    // </editor-fold>

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    public String getLastReviewNote() {
        return lastReviewNote;
    }

    public void setLastReviewNote(String lastReviewNote) {
        this.lastReviewNote = lastReviewNote;
    }



    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (lastReviewDate != null && lastReviewDate.after(new Date())) {
            throw new Exception("The Last Review Date cannot be in the future.");
        }
        
        if (claim.getLastReviewDate() == null && lastReviewDate == null) {
            throw new Exception("No Last Review Date provided.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setLastReviewDate(lastReviewDate);
        
        // Add Note

        if (lastReviewDate!=null) {
            claim.addComment(Comment.newComment(1, "Date of Last Review: " + DateHelper.getLocalDateFormat().format(lastReviewDate) + "."));
            if (lastReviewNote != null && !lastReviewNote.isEmpty()) {
                claim.addComment(Comment.newComment(1, "Last Review Note: " + lastReviewNote, true));
            }
        } else {
            claim.addComment(Comment.newComment(1, "The Date of Last Review has been removed."));
            if (lastReviewNote != null && !lastReviewNote.isEmpty()) {
                claim.addComment(Comment.newComment(1, "Last Review Removal Note: " + lastReviewNote, true));
            }
        } 
    }
}