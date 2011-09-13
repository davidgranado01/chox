package idas.chox.web.actions;

// import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.viewdata.CommentViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class CommentAction extends ClaimModelAction<Comment> {

    private String comment;
    private JSONArray jObject;

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

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getComments() {

        List<CommentViewData> viewDatas = new ArrayList<CommentViewData>();

        //Claim claim = claimService.getClaim(claimId);
        List<Comment> comments = claim.getComments();

        for (Comment c : comments) {
            if ((c.getVisibilityType() == 1 && this.getIsCHO()) || (c.getVisibilityType() == 2 && this.getIsInsurer())) {
                continue;
            }
            if (c.getRaisedBy() != null) {
                c.setCreatedBy(c.getRaisedBy());
            }
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