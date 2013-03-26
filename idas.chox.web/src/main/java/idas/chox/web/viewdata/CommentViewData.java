package idas.chox.web.viewdata;

import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.util.DateHelper;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author Emmanuel
 */
public class CommentViewData {

    private int id;
    private String createdBy;
    private String createdDate;
    private String comment;
    private int visibilityType;
    private String delete = "";

    public CommentViewData(Comment comment,WebUser authenticatedUser) {
        this.id = comment.getId();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(comment.getCreatedDate());
        this.comment = comment.getComment();
        this.visibilityType = comment.getVisibilityType();

        String orgName = "";
        WebUser user = comment.getCreatedBy();
        if (user != null && user.isAnInsurer() && user.getInsurer() != null) {
            orgName = String.format("(%1$s)", user.getInsurer().getName());
        } else if (user != null && !user.isAnInsurer() && user.getChorganisation() != null ) {
            orgName = String.format("(%1$s)", user.getChorganisation().getName());
        }
        this.createdBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
        if (authenticatedUser.isCHOXAdmin() 
                        || ((authenticatedUser.getId().compareTo(user.getId())==0 
                             || (authenticatedUser.isInRoleOf(WebUserRole.ROLE_CH_MNG) && user.isCHO())   
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
}
