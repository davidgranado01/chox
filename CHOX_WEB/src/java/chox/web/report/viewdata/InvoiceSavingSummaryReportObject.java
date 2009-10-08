package chox.web.report.viewdata;

import java.util.Date;

public class InvoiceSavingSummaryReportObject {

    private Date invoiceUploadDateFrom;
    private Date invoiceUploadDateTo;
    private Date createdDate;
    private String insurerName;
    private String creditHireName;

    public Date getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    public void setInvoiceUploadDateFrom(Date invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    public Date getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    public void setInvoiceUploadDateTo(Date invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreditHireName() {
        return creditHireName;
    }

    public void setCreditHireName(String creditHireName) {
        this.creditHireName = creditHireName;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }


}
