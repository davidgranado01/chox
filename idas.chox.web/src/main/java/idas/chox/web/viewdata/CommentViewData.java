package idas.chox.web.viewdata;

import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
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

    public CommentViewData(Comment comment) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        this.id = comment.getId();
        this.createdDate = dateFormat.format(comment.getCreatedDate());
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
        
    }

    int getId() {
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
}
