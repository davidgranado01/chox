/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import java.io.Serializable;

public class Comment extends AuditableEntity implements Serializable {

    protected String comment;
    //protected int claimId;
    protected Claim claim;
    protected boolean isPublic;

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

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
