/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report.viewdata;

/**
 *
 * @author Emmanuel
 */
public class InvoiceSummaryReportObject {
    private String invoiceUploadDateFrom;
    private String invoiceUploadDateTo;
    private String createdDate;

    public String getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    public void setInvoiceUploadDateFrom(String invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    public String getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    public void setInvoiceUploadDateTo(String invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
