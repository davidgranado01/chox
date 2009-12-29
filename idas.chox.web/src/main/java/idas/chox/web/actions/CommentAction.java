/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

public class CommentAction extends BaseModelAction implements ModelDriven<Comment>, Preparable  {

    private Comment model;
    private String comment;
  
    public String createNewComment() {
        try {

            Claim claim = getClaim();
            model.setComment(getComment());
            claim.addComment(model);
            this.claimService.updateClaim(claim);
        } catch (Exception ex) {
            logger.error(ex);
            this.getActionResponse().AddError(ex.getMessage());
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_NOTES;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment getModel() {
        return model;
    }

    public void prepare() throws Exception {
        model = new Comment();
    }
}
