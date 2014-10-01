package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

public class AddNote extends BaseActivity {

    static final Logger LOG = LoggerFactory.getLogger(AddNote.class);
    private String comment;
    private Comment note;
    private int visibilityType; // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY

    public Comment getNote() {
        return note;
    }

    public void setNote(Comment note) {
        this.note = note;
    }

    public String getComment() {
        return comment;
    }

    public int getVisibilityType() {
        return visibilityType;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setVisibilityType(int visibilityType) {
        this.visibilityType = visibilityType;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        boolean disablePrivateNotes = getCurrentUser().isAnInsurer()
                ? getCurrentUser().getInsurer().isDisablePrivateNotes()
                : getCurrentUser().isCHO()
                ? getCurrentUser().getChorganisation().isDisablePrivateNotes()
                : claim.getInsurer().isDisablePrivateNotes();

        if (disablePrivateNotes && visibilityType != 0) {
            LOG.warn("User without priviliges is trying to add private note. user is {}, {}", getCurrentUser().getDisplayName(), getCurrentUser().getId());
            throw new Exception("Note can't be added. Insufficient priviliges!");
        }
    }

    @Override
    @Secured ({"ROLE_INS", "ROLE_CHO"})
    protected void doProcess(Claim claim) {
        note = Comment.newComment(visibilityType, comment.replaceAll("\n", "<br />"));
        claim.addComment(note);
        // saving and forcing the transaction to commit is to avoid nullPointerException while accessing the createdBy info at the ActivityEvent class.
        getDataService().save(note);
        getDataService().flush();
    }
}
