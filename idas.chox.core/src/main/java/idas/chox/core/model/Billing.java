package idas.chox.core.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Billing extends Entity {

    /**
     *
     */
    private static final long serialVersionUID = 2856353151755686662L;
    private String scheduleName;
    private Date dateFrom;
    private Date dateTo;
    private BigDecimal invoiceAmount;
    private BigDecimal amountReceived;
    private boolean manual;
    private boolean reconciled;
    private boolean manualClaimsOnly;
    private Set<BillingDetail> billingDetails = new HashSet<BillingDetail>(0);

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isManualClaimsOnly() {
        return manualClaimsOnly;
    }

    public void setManualClaimsOnly(boolean manualClaimsOnly) {
        this.manualClaimsOnly = manualClaimsOnly;
    }

    public boolean isReconciled() {
        return reconciled;
    }

    public void setReconciled(boolean reconciled) {
        this.reconciled = reconciled;
    }

    public Set<BillingDetail> getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(Set<BillingDetail> billingDetails) {
        this.billingDetails = billingDetails;
    }
}