package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class AuditTrail extends Entity implements Serializable {

    protected Date updateDate;
    protected String originalStatus;
    protected String newStatus;
    protected WebUser user;
    protected Claim claim;
    protected ReasonOfRejection claimReasonOfRejection;
    protected ReasonOfRejection invoiceReasonOfRejection;
    protected boolean reverted = false;

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

    public ReasonOfRejection getClaimReasonOfRejection() {
        return claimReasonOfRejection;
    }

    public void setClaimReasonOfRejection(ReasonOfRejection claimReasonOfRejection) {
        this.claimReasonOfRejection = claimReasonOfRejection;
    }

    public ReasonOfRejection getInvoiceReasonOfRejection() {
        return invoiceReasonOfRejection;
    }

    public void setInvoiceReasonOfRejection(ReasonOfRejection invoiceReasonOfRejection) {
        this.invoiceReasonOfRejection = invoiceReasonOfRejection;
    }

    public boolean getReverted() {
        return reverted;
    }

    public void setReverted(boolean reverted) {
        this.reverted = reverted;
    }
}
