package idas.chox.web.viewdata;

import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author Emmanuel
 */
public class CommentViewData {

    private final int id;
    private final String createdBy;
    private final String createdDate;
    private final String comment;
    private String reviewRequired;
    private int visibilityType;
    private String delete = "";

    public CommentViewData(Comment comment, WebUser authenticatedUser) {
        this.id = comment.getId();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(comment.getCreatedDate());
        this.comment = comment.getComment();
        this.visibilityType = comment.getVisibilityType();

        String orgName = "";
        WebUser user = comment.getCreatedBy();
        if (user != null) {
            if (user.isAnInsurer() && user.getInsurer() != null) {
                orgName = String.format("(%1$s)", user.getInsurer().getName());
                if (comment.getReviewRequired() != null && comment.getReviewRequired() && authenticatedUser.isCHO()) {
                    this.reviewRequired = "Required";
                } else if (comment.getReviewRequired() != null && comment.getReviewRequired() && !authenticatedUser.isCHO()) {
                    this.reviewRequired = "Pending";
                } else if (comment.getReviewRequired() != null) {
                    this.reviewRequired = "Acknowledged";
                } else {
                    this.reviewRequired = "";
                }
            } else if (!user.isAnInsurer() && user.getChorganisation() != null) {
                orgName = String.format("(%1$s)", user.getChorganisation().getName());
                if (comment.getReviewRequired() != null && comment.getReviewRequired() && authenticatedUser.isAnInsurer()) {
                    this.reviewRequired = "Required";
                } else if (comment.getReviewRequired() != null && comment.getReviewRequired() && !authenticatedUser.isAnInsurer()) {
                    this.reviewRequired = "Pending";
                } else if (comment.getReviewRequired() != null) {
                    this.reviewRequired = "Acknowledged";
                } else {
                    this.reviewRequired = "";
                }
            }
            if ((user.getFirstName() != null && user.getFirstName().startsWith("~~"))
                    || (user.getLastName() != null && user.getLastName().startsWith("~~"))) {
                createdBy = String.format("GDPR: data removed %1$s", orgName);
            } else {
                createdBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
            }
        } else {
            createdBy = "unknown";
        }
        
        if (reviewRequired == null) { // CHOX Admin
                if (comment.getReviewRequired() != null && comment.getReviewRequired()) {
                    this.reviewRequired = "Pending";
                } else if (comment.getReviewRequired() != null) {
                    this.reviewRequired = "Acknowledged";
                } else {
                    this.reviewRequired = "";
                }
        }
        if (authenticatedUser.isCHOXAdmin()
                || (user != null && (authenticatedUser.getId().compareTo(user.getId()) == 0
                || (authenticatedUser.isInRoleOf(WebUserRole.ROLE_CHO_MNG) && user.isCHO())
                || (authenticatedUser.isInRoleOf(WebUserRole.ROLE_INS_MNG) && user.isAnInsurer()))
                && DateHelper.differenceInMinutes(DateHelper.getCurrentDateTime(), comment.getCreatedDate()) <= 5)) {
            this.delete = "Delete";
        }
    }

    public int getId() {
        return id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getComment() {
        return comment;
    }

    public int getVisibilityType() {
        return visibilityType;
    }

    public void setVisibilityType(int visibilityType) {
        this.visibilityType = visibilityType;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getReviewRequired() {
        return reviewRequired;
    }
}
