package idas.chox.service.workflow.activities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Task;
import idas.chox.core.services.TaskService;

public class AddNote extends BaseActivity {

    static final Logger LOG = LoggerFactory.getLogger(AddNote.class);
    private String comment;
    private Comment note;
    private int visibilityType; // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
    private boolean reviewRequired;
    private TaskService taskService;
    private boolean canCreateTask;

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
    
    public boolean isReviewRequired() {
        return reviewRequired;
    }

    public void setReviewRequired(boolean reviewRequired) {
        this.reviewRequired = reviewRequired;
    }

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
        
        // Determine if an external task can be created on the claim
        canCreateTask = false;
        if (getCurrentUser().isAnInsurer() && claim.getChorganisation().isTaskManagementEnable()) {
            canCreateTask = true;
        } else if (getCurrentUser().isCHO() && claim.getInsurer().isTaskManagementEnable()) {
            canCreateTask = true;
        }
    }

    @Override
    @Secured ({"ROLE_INS", "ROLE_CHO"})
    protected void doProcess(Claim claim) {
        note = Comment.newComment(visibilityType, reviewRequired, comment.replaceAll("\n", "<br />"), true);
        if (reviewRequired && visibilityType == 0 && canCreateTask) {
            // Create task for comment review required
            Task task = new Task();
            note.setTask(task);
            task.setClaim(claim);
            String commentDescription;
            if (getCurrentUser().isAnInsurer()) {
                commentDescription = "The Insurer has added a note that requires review. Please go to the notes section of this claim to review.";
                task.setInsurer(Boolean.TRUE);
            } else {
                commentDescription = "The CHO has added a note that requires review. Please go to the notes section of this claim to review.";
                task.setInsurer(Boolean.FALSE);
            }
            task.setDescription(commentDescription);
            task.setDueDate(new Date());
            task.setRaisedBy(getCurrentUser());
            task.setType("Note Review Required");
            task.setVisibility(3); // External
            try {
                taskService.createNewTask(task);
            } catch (Exception ex) {
                LOG.warn("Error creating 'Note Review Required' task on claim with id={}: {}", claim.getId(), ex.getMessage());
            }
        }
        claim.addComment(note);
        // saving and forcing the transaction to commit is to avoid nullPointerException while accessing the createdBy info at the ActivityEvent class.
        getDataService().save(note);
        getDataService().flush();
    }
}
