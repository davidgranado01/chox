/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Comment;
import idas.chox.core.services.CommentService;
import idas.chox.web.security.ApplicationAccessibility;
import idas.chox.web.viewdata.CommentViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class GetCommentsAction extends BaseModelAction {

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
    public String execute() {
        return SUCCESS;
    }

    public String getComments() {

        List<Comment> commentsData = this.service.getCommentByClaimIdFilterByOrg(claimId, getAuthenticatedUser().getUser().getOrganisationType());

        comments = new ArrayList<CommentViewData>();
        for (Comment c : commentsData) {
            comments.add(new CommentViewData(c));
        }

        return SUCCESS;
    }
}
