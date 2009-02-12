/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Carlson
 */
public class AuditTrail implements Serializable,Auditable{

    
    protected Integer id;
    protected Date updateDate;
    protected String originalStatus;
    protected String newStatus;
    protected WebUser user;
    protected Claim claim;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;
    protected int claimReasonOfRejection;
    protected int invoiceReasonOfRejection;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public WebUser getUser() {
        return user;
    }

    public void setUser(WebUser user) {
        this.user = user;
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

    public int getClaimReasonOfRejection() {
        return claimReasonOfRejection;
    }

    public void setClaimReasonOfRejection(int claimReasonOfRejection) {
        this.claimReasonOfRejection = claimReasonOfRejection;
    }

    public int getInvoiceReasonOfRejection() {
        return invoiceReasonOfRejection;
    }

    public void setInvoiceReasonOfRejection(int invoiceReasonOfRejection) {
        this.invoiceReasonOfRejection = invoiceReasonOfRejection;
    }

    
}
