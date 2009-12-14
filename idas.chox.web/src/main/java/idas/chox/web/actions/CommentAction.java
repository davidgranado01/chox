/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.services.CommentService;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

public class CommentAction extends BaseModelAction implements ModelDriven<Comment>, Preparable {

    private CommentService service;
    private Comment model;
    // private int claimId;

    public void setCommentService(CommentService service) {
        this.service = service;
    }

    public Comment getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Comment();
            Claim c = new Claim();
            c.setId(claimId);
            model.setClaim(c);

        } else {
            model = service.getObject(objectId);
        }
    }

    public String createNewComment() {
        try {
            this.service.createNewObject(model);
        } catch (Exception ex) {
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
    /*
    public int getClaimId() {
    return claimId;
    }

    public void setClaimId(int claimId) {
    this.claimId = claimId;
    }
     */
}
