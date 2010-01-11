package idas.chox.service.reports.viewdata;

import java.util.Date;

public class OverviewSummaryReportObject {
    private Date uploadDateFrom;
    private Date uploadDateTo;
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUploadDateFrom() {
        return uploadDateFrom;
    }

    public void setUploadDateFrom(Date uploadDateFrom) {
        this.uploadDateFrom = uploadDateFrom;
    }

    public Date getUploadDateTo() {
        return uploadDateTo;
    }

    public void setUploadDateTo(Date uploadDateTo) {
        this.uploadDateTo = uploadDateTo;
    }
    
    
}
