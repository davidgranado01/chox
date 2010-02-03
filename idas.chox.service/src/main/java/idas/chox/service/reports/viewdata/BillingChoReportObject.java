/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author abrar
 */
public class BillingChoReportObject {
    private Date dateFrom;
    private Date dateTo;
    private Date createdDate;
    private String choName;
    private String reportTitle;
    private int numberOfInvoicesUploaded;
    private BigDecimal chargeRate;

    /**
     * @return the dateFrom
     */
    public Date getDateFrom() {
        return dateFrom;
    }

    /**
     * @param dateFrom the dateFrom to set
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo() {
        return dateTo;
    }

    /**
     * @param dateTo the dateTo to set
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    /**
     * @return the createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate the createdDate to set
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the choName
     */
    public String getChoName() {
        return choName;
    }

    /**
     * @param choName the choName to set
     */
    public void setChoName(String choName) {
        this.choName = choName;
    }

    /**
     * @return the reportTitle
     */
    public String getReportTitle() {
        return reportTitle;
    }

    /**
     * @param reportTitle the reportTitle to set
     */
    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    /**
     * @return the numberOfInvoicesUploaded
     */
    public int getNumberOfInvoicesUploaded() {
        return numberOfInvoicesUploaded;
    }

    /**
     * @param numberOfInvoicesUploaded the numberOfInvoicesUploaded to set
     */
    public void setNumberOfInvoicesUploaded(int numberOfInvoicesUploaded) {
        this.numberOfInvoicesUploaded = numberOfInvoicesUploaded;
    }

    /**
     * @return the chargeRate
     */
    public BigDecimal getChargeRate() {
        return chargeRate;
    }

    /**
     * @param chargeRate the chargeRate to set
     */
    public void setChargeRate(BigDecimal chargeRate) {
        this.chargeRate = chargeRate;
    }
}
