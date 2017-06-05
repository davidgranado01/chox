package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.CommentService;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.CommentViewData;
import idas.chox.core.util.DateHelper;

public class CommentAction extends ClaimModelAction<Comment> {

    private static final Logger LOG = LoggerFactory.getLogger(CommentAction.class);
    private String jObject;
    private int jObjectSize;
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

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + jObjectSize + ",results:" + jObject + "}";
        }
        return "";
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getComments() {

        List<CommentViewData> viewDatas = new ArrayList<>();

        //Claim claim = claimService.getClaim(claimId);
        List<Comment> comments = claim.getComments();

        for (Comment c : comments) {
            if (c.isReverted() || ((c.getVisibilityType() == 1 && this.getIsCHO()) || (c.getVisibilityType() == 2 && this.getIsInsurer()))) {
                continue;
            }
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
            viewDatas.add(new CommentViewData(c, getAuthenticatedUser()));
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            jObject = mapper.writeValueAsString(viewDatas);
            jObjectSize = viewDatas.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting comments to json string.");
            jObjectSize = 0;
            jObject = null;
        }
        return SUCCESS;
    }

    public String getCommentsForReview() {

        List<CommentViewData> viewDatas = new ArrayList<>();

        if (claim == null) {
            claim = claimService.getClaim(claimId);
        }

        List<Comment> comments = claim.getComments();

        for (Comment c : comments) {
            if (c.isReverted() || ((c.getVisibilityType() == 1 && this.getIsCHO()) || (c.getVisibilityType() == 2 && this.getIsInsurer()))
                    || c.getReviewRequired() == null || !c.getReviewRequired()) {
                continue;
            }
            CommentViewData cvd = new CommentViewData(c, getAuthenticatedUser());
            if (cvd.getReviewRequired().equals("Required")) {
                viewDatas.add(cvd);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            jObject = mapper.writeValueAsString(viewDatas);
            jObjectSize = viewDatas.size();
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting comments to json string.");
            jObjectSize = 0;
            jObject = null;
        }
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_NOTES;
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
                        || ((getAuthenticatedUser().getId().compareTo(user.getId()) == 0
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

    public String acknowledgeComment() {
        LOG.debug("Acknowledging comment...");
        try {
            if (model.getId() != null) {

                commentService.acknowledgeCommentById(model.getId());
                LOG.debug("Comment acknowledged.");
                this.getActionResponse().AssignMessageResult("Note has been acknowledged.");
            } else {
                LOG.warn("User trying to acknowledge Comment without Comment id. user is {}, {}", getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getId());
                this.getActionResponse().AssignMessageResult("No comment found");
                return ERROR;
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown acknowledgeding comment: {}", ex.getMessage());
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
        } else {
            LOG.debug(" CommentAction validation is not done as claim is null");
        }
    }

    public boolean isInsurerIsDisablePrivateNotes() {
        return claim.getInsurer().isDisablePrivateNotes();
    }

    public boolean isChoIsDisablePrivateNotes() {
        return claim.getChorganisation().isDisablePrivateNotes();
    }

    public boolean isInsurerTaskManagementEnabled() {
        return claim.getInsurer().isTaskManagementEnable();
    }

    public boolean isChoTaskManagementEnabled() {
        return claim.getChorganisation().isTaskManagementEnable();
    }

}
