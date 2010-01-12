package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.viewdata.CommentViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class CommentAction extends BaseModelAction implements ModelDriven<Comment>, Preparable {

    private Comment model;
    private String comment;
    private JSONArray jObject;

    public String createNewComment() {

        try {

            Claim claim = getClaim();
            model.setComment(getComment());
            claim.addComment(model);
            this.claimService.updateClaim(claim);

        } catch (Exception ex) {
            handleException(this, ex);
        }

        return SUCCESS;
    }

    public String getJsonData() {
        return jObject.toString();
    }

    public String getComments() {
        Claim claim = claimService.getClaim(claimId);
        List<Comment> comments = claim.getComments();
        List<CommentViewData> viewDatas = new ArrayList<CommentViewData>();

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

    public Comment getModel() {
        return model;
    }

    public void prepare() throws Exception {
        model = new Comment();
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }
}