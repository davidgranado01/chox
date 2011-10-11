package idas.chox.service.reports.viewdata;

import java.util.Date;

public class ClaimRejectedReportObject {
    private Date DateFrom;
    private Date DateTo;
    private Date createdDate;

    public Date getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(Date DateFrom) {
        this.DateFrom = DateFrom;
    }

    public Date getDateTo() {
        return DateTo;
    }

    public void setDateTo(Date DateTo) {
        this.DateTo = DateTo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    
}
