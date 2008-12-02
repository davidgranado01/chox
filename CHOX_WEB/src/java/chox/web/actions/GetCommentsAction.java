/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.Comment;
import chox.model.History;
import chox.services.CommentService;
import chox.web.security.ApplicationAccessibility;
import chox.web.viewdata.CommentViewData;
import chox.web.viewdata.HistoryViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class GetCommentsAction extends BaseModelAction {

    private int claimId;
    private CommentService service;
    private List<CommentViewData> comments;

    public void setCommentService(CommentService service) {
        this.service = service;
    }
       
   public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.comments);
        
        return "{totalCount:" + this.comments.size() + ",results:" + jObject.toString() + "}";

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
    
    public String getComments()
    {        
        List<Comment> commentsData = this.service.getCommentByClaimId(claimId);
                
        comments = new ArrayList<CommentViewData>();
        for(Comment c : commentsData)
        {
            comments.add(new CommentViewData(c));
        }
        
        return SUCCESS;
    }

    int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }
}