package chox.model;

import java.io.Serializable;
import java.util.Date;

public class AuditTrail extends AuditableEntity implements Serializable {

    protected Date updateDate;
    protected String originalStatus;
    protected String newStatus;
    protected WebUser user;
    protected Claim claim;
    protected Integer claimReasonOfRejection;
    protected Integer invoiceReasonOfRejection;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
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

    public Integer getClaimReasonOfRejection() {
        return claimReasonOfRejection;
    }

    public void setClaimReasonOfRejection(Integer claimReasonOfRejection) {
        this.claimReasonOfRejection = claimReasonOfRejection;
    }

    public Integer getInvoiceReasonOfRejection() {
        return invoiceReasonOfRejection;
    }

    public void setInvoiceReasonOfRejection(Integer invoiceReasonOfRejection) {
        this.invoiceReasonOfRejection = invoiceReasonOfRejection;
    }
}
