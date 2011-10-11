package idas.chox.core.model;

import java.io.Serializable;

public class Comment extends Entity implements Serializable {

    protected String comment;
    protected Claim claim;
    protected int visibilityType; // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
    private WebUser raisedBy;

    public WebUser getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(WebUser raisedBy) {
        this.raisedBy = raisedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public int getVisibilityType() {
        return visibilityType;
    }

    public void setVisibilityType(int visibilityType) {
        this.visibilityType = visibilityType;
    }

    public static Comment New(int visibilityType, String msg) {
        Comment comment = new Comment();
        comment.setVisibilityType(visibilityType);
        comment.setComment(msg);
        return comment;
    }
}
