/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.Claim;
import chox.model.Comment;
import chox.services.CommentService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class CommentAction extends BaseModelAction implements ModelDriven<Comment>, Preparable {

    private CommentService service;
    private Comment model;
   
    public void setCommentService(CommentService service) {
        this.service = service;
    }

    public Comment getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId == -1) {
            model = new Comment();
        } else {
            model = service.getObject(objectId);
        }
    }   

    public String createNewComment() {
        try {
            this.service.createNewObject(model);
            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
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
    
}
