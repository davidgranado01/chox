package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public class AuditTrail extends Entity implements Serializable {
    public static final Comparator<AuditTrail> UPDATECOMPARATOR =
            new Comparator<AuditTrail>() {
                @Override
                public int compare(AuditTrail a1, AuditTrail a2) {
                    return a1.getUpdateDate().compareTo(a2.getUpdateDate());
                }
    };
    private Date updateDate;
    private String originalStatus;
    private String newStatus;
    private WebUser user;
    private Claim claim;
    private ReasonOfRejection claimReasonOfRejection;
    private ReasonOfRejection invoiceReasonOfRejection;
    private boolean reverted = false;
    private BigDecimal previousTotalToPay; 

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
    
    public BigDecimal getPreviousTotalToPay() {
        return previousTotalToPay;
    }

    public void setPreviousTotalToPay(BigDecimal previousTotalToPay) {
        this.previousTotalToPay = previousTotalToPay;
    }
}
