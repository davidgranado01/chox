package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.Date;

public class BillingDetail extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 2342966159416856075L;
    private Claim claim;
    private Date receivedDate;
    private String comment;
    private String triggerPoint;
    private BigDecimal billAmount;
    private BigDecimal vatOnBillAmount;
    private BigDecimal grossBillAmount;
    private BigDecimal amountReceived;
    private boolean reconciled;
    private Billing billing;

    public BigDecimal getGrossBillAmount() {
        return grossBillAmount;
    }

    public void setGrossBillAmount(BigDecimal grossBillAmount) {
        this.grossBillAmount = grossBillAmount;
    }

    public BigDecimal getVatOnBillAmount() {
        return vatOnBillAmount;
    }

    public void setVatOnBillAmount(BigDecimal vatOnBillAmount) {
        this.vatOnBillAmount = vatOnBillAmount;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(BigDecimal billAmount) {
        this.billAmount = billAmount;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public boolean isReconciled() {
        return reconciled;
    }

    public void setReconciled(boolean reconciled) {
        this.reconciled = reconciled;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public String getTriggerPoint() {
        return triggerPoint;
    }

    public void setTriggerPoint(String triggerPoint) {
        this.triggerPoint = triggerPoint;
    }
}