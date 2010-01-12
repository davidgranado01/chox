/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Comment;
import idas.chox.service.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

public class CommentAction extends ClaimModelAction<Comment> {

    private String comment;

    public String createNewComment() {
        model.setComment(getComment());
        claim.addComment(model);
        return super.updateModel();
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    public String getComments() {

        List<CommentViewData> viewDatas = new ArrayList<CommentViewData>();
        
        Claim claim = claimService.getClaim(claimId);
        List<Comment> comments = claim.getComments();
        
        for (Comment c : comments) {
            viewDatas.add(new CommentViewData(c));
        }

        this.jObject = JSONArray.fromObject(viewDatas);
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_NOTES;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public Comment loadModel() {
        return new Comment();
    }

}