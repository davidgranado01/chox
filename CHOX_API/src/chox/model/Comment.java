/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.io.Serializable;
import java.util.Date;
import chox.model.Claim;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Comment implements Serializable,Auditable{
    
    protected int id;
    protected String comment;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;
    //protected int claimId;
    protected Claim claim;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public WebUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WebUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(WebUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
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
