package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import net.sf.json.JSONArray;

import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.CommentService;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.CommentViewData;
import idas.chox.core.util.DateHelper;
import idas.chox.service.workflow.activities.ActivityEvent;

public class CommentAction extends ClaimModelAction<Comment> {
    private static final Logger LOG = LoggerFactory.getLogger(CommentAction.class);
    private String comment;
    private JSONArray jObject;
    private int commentId;
    private CommentService commentService;

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }
    
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String createNewComment() {
        try {
            boolean disablePrivateNotes = getAuthenticatedUser().isAnInsurer()
                    ? getAuthenticatedUser().getInsurer().isDisablePrivateNotes()
                    : getAuthenticatedUser().isCHO()
                    ? getAuthenticatedUser().getChorganisation().isDisablePrivateNotes()
                    : claim.getInsurer().isDisablePrivateNotes();

            if (disablePrivateNotes && model.getVisibilityType() != 0) {
                LOG.warn("User without priviliges is trying to add private note. user is {}, {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getId());
                this.getActionResponse().AddError("Note can't be added. Insufficient priviliges!");
                return ERROR;
            }
            model.setComment(getComment());
            claim.addComment(model);
            
            String result = super.updateModel();
            
            // Generate NoteAdded Event
            LOG.debug("Generating NoteAdded Event...");
            activityEventGenerator.generate(claim, model, ActivityEvent.NOTE_ADDED_EVENT);
            
            return result;
        } catch (Exception ex) {
            LOG.warn("Error creating comment/note for claim {}", claim.getChoReference(), ex);
            handleException(ex);
            return ERROR;
        }
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getComments() {

        List<CommentViewData> viewDatas = new ArrayList<CommentViewData>();

        //Claim claim = claimService.getClaim(claimId);
        List<Comment> comments = claim.getComments();

        for (Comment c : comments) {
            if (c.isReverted() || ((c.getVisibilityType() == 1 && this.getIsCHO()) || (c.getVisibilityType() == 2 && this.getIsInsurer()))) {
                continue;
            }
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
            viewDatas.add(new CommentViewData(c,getAuthenticatedUser()));
        }

        this.jObject = JSONArray.fromObject(viewDatas);
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_NOTES;
    }

    public String getComment() {
        
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Comment loadModel() {
        if (commentId > 0) {
            return (Comment) baseDataService.get(Comment.class, commentId);
        } else {
            return new Comment();
        }
    }
    
    public String deleteComment() {
        LOG.debug("Deleting comment...");
        try {
            if (model.getId() != null) {
                WebUser user = model.getCreatedBy();
                if (getAuthenticatedUser().isCHOXAdmin() 
                        || ((getAuthenticatedUser().getId().compareTo(user.getId())==0 
                             || (getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CHO_MNG) && user.isCHO())   
                             || (getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_MNG) && user.isAnInsurer()))
                            && DateHelper.differenceInMinutes(DateHelper.getCurrentDateTime(), model.getCreatedDate()) <= 5)) {

                    commentService.deleteCommentById(model.getId());
                    LOG.debug("Comment deleted.");
                    this.getActionResponse().AssignMessageResult("Note has been deleted.");
                    // Generate NoteDeleted Event - removed as not needed
//                    activityEventGenerator.generate(claim, model, ActivityEvent.NOTE_DELETED_EVENT);
                    
                } else {
                    
                    if (getAuthenticatedUser().getId().compareTo(user.getId()) == 0
                            && DateHelper.differenceInMinutes(DateHelper.getCurrentDateTime(), model.getCreatedDate()) >= 5) {
                        LOG.warn("User trying to delete Comment which they created more than 5 mins ago.");
                        this.getActionResponse().AssignMessageResult("Sorry, 5 minutes have elapsed since the creation of this note and therefore the note cannot be deleted.");
                        return ERROR;
                    }
                    LOG.warn("User trying to delete Comment which they do not own. user display name: {}, user id : {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getId());
                    this.getActionResponse().AssignMessageResult("Sorry, You do not have permission to delete this note");
                    return ERROR;
                }

            } else {
                LOG.warn("User trying to delete Comment without Comment id. user is {}, {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getId());
                this.getActionResponse().AssignMessageResult("No comment id found");
                return ERROR;
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown deleting comment: {}", ex.getMessage());
            this.getActionResponse().AssignMessageResult(ex.getMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("CommentAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("CommentAction validate success");
        }
        else {
            LOG.debug(" CommentAction validation is not done as claim is null");
        }
    }

    public boolean isInsurerIsDisablePrivateNotes() {
        return claim.getInsurer().isDisablePrivateNotes();
    }

    public boolean isChoIsDisablePrivateNotes() {
        return claim.getChorganisation().isDisablePrivateNotes();
    }


}