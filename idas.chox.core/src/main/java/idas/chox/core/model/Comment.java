package idas.chox.core.model;

import java.io.Serializable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class Comment extends Entity implements Serializable {

    private String comment;
    private Claim claim;
    private int visibilityType; // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
    private WebUser raisedBy;
    private boolean reverted;

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
        this.comment = Jsoup.clean(comment, "", Whitelist.basic(), new Document.OutputSettings().prettyPrint(false));
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

    public boolean isReverted() {
        return reverted;
    }

    public void setReverted(boolean reverted) {
        this.reverted = reverted;
    }

    public static Comment newComment(int visibilityType, String msg) {
        Comment comment = new Comment();
        comment.setVisibilityType(visibilityType);
        comment.setComment(msg);
        return comment;
    }
}
