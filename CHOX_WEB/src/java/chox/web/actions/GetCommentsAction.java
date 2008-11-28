/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.Comment;
import chox.services.CommentService;
import chox.web.security.ApplicationAccessibility;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class GetCommentsAction extends BaseModelAction {

    private int claimId;
    private CommentService service;
    private List<Comment> comments;

    public void setCommentService(CommentService service) {
        this.service = service;
    }
       
    public String getJsonData() {      
        
        JSONArray jObject = JSONArray.fromObject(this.comments);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_NOTES;
    }
    
    @Override
    public String execute()
    {        
        return SUCCESS;
    }
    
    public String getClaims()
    {        
        comments = this.service.getCommentByClaim(claimId);
        return SUCCESS;
    }

    public

    int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }
}