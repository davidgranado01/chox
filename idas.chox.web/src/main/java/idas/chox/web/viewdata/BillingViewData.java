package idas.chox.web.viewdata;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Billing;
import idas.chox.core.model.BillingCho;
import idas.chox.core.model.BillingInsurer;
import idas.chox.core.util.DateHelper;

public class BillingViewData {

    private static final Logger LOG = LoggerFactory.getLogger(BillingViewData.class);
    
    private int billingId;
    private String column1;
    private int column2;
    private String scheduleName;
    private String dateFrom;
    private String dateTo;
    private BigDecimal invoiceAmount;
    private BigDecimal amountReceived;
    private boolean manual;
    private boolean reconciled;
    private boolean manualClaimsOnly;

    public BillingViewData(Billing record) {
        LOG.debug("Billing constructor: {}", record.getClass().getName());
        if (record instanceof BillingInsurer) {
            this.column1 = ((BillingInsurer) record).getInsurer().getName();
            this.column2 = ((BillingInsurer) record).getInsurer().getId();
            LOG.debug("Insurer name: '{}'", ((BillingInsurer) record).getInsurer().getName());
        } else if (record instanceof BillingCho) {
            this.column1 = ((BillingCho) record).getCho().getName();
            this.column2 = ((BillingCho) record).getCho().getId();
        }
        this.billingId = record.getId();
        this.scheduleName = record.getScheduleName();
        this.dateFrom = DateHelper.getLocalDateFormat().format(record.getDateFrom());
        this.dateTo = DateHelper.getLocalDateFormat().format(record.getDateTo());
        this.invoiceAmount = record.getInvoiceAmount();
        this.amountReceived = record.getAmountReceived();
        this.manual = record.isManual();
        this.reconciled = record.isReconciled();
        this.manualClaimsOnly = record.isManualClaimsOnly();
        LOG.debug("From date is: {}, To date is {}", dateFrom, dateTo);
    }

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String header1) {
        this.column1 = header1;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateTo() {
        return dateTo;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("BillingViewData [scheduleName=").append(scheduleName).append("] ").append(this.getClass()).toString();
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

    /**
     * @return the billingId
     */
    public int getBillingId() {
        return billingId;
    }

    /**
     * @param billingId the billingId to set
     */
    public void setBillingId(int billingId) {
        this.billingId = billingId;
    }

    /**
     * @return the column2
     */
    public int getColumn2() {
        return column2;
    }

    /**
     * @param column2 the column2 to set
     */
    public void setColumn2(int column2) {
        this.column2 = column2;
    }


}
