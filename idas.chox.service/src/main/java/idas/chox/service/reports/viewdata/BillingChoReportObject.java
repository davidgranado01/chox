package idas.chox.service.reports.viewdata;

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
    private int numberOfClaimsBilled;
    private String scheduleName;

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }
    
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
     * @return the numberOfClaimsBilled
     */
    public int getNumberOfClaimsBilled() {
        return numberOfClaimsBilled;
    }

    /**
     * @param numberOfClaimsBilled the numberOfInvoicesUploaded to set
     */
    public void setNumberOfClaimsBilled(int numberOfClaimsBilled) {
        this.numberOfClaimsBilled = numberOfClaimsBilled;
    }
}