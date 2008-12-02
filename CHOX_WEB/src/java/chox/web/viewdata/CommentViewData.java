/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.viewdata;

import chox.model.Chorganisation;
import chox.model.Comment;
import chox.model.Insurer;
import chox.model.WebUser;
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

    public CommentViewData(Comment comment) {
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        this.id = comment.getId();
        this.createdDate = dateFormat.format(comment.getCreatedDate());
        this.comment = comment.getComment();
        String orgName = "";
        WebUser user = comment.getCreatedBy();
        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }
            this.createdBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
        }
        
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
}
