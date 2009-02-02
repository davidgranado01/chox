/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.services.DataService;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public class InsurerAdminWeeklyOverviewReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public InsurerAdminWeeklyOverviewReport() {
        reportParameterNames = new ArrayList<String>();

    }

    public String getReportTemplateFileName() {
        return "template_InsurerAdminWeeklyOverviewReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();

        String pInvUploadDateFrom = "'2008-11-22'";
        String pInvUploadDateTo = "'2009-11-22'";
        String pInsId = "3";

        String query = "select chorganisation.id,chorganisation.name, insurer_chorganisation.insurer_id,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoiceSubmitted,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as totalInvoiceValue,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoicePaymentLogged' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoicesPaid,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoicePaymentLogged' and date_trunc('day', created_date) between pInvUploadDateFrom and current_date) as valueOfPaidInvoices,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoiceAwaitingPayment,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as invoiceAwaitingPaymentValue,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO') and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoicePending,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO') and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as invoicePendingValue,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoiceWithdrawn,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as invoiceWithdrawnValue from chorganisation chorganisation inner join insurer_chorganisation insurer_chorganisation on insurer_chorganisation.chorganisation_id = chorganisation.id"
        + " where insurer_chorganisation.insurer_id=pInsId";
        
        query = query.replaceAll("pInvUploadDateFrom", pInvUploadDateFrom);
        query = query.replaceAll("pInvUploadDateTo", pInvUploadDateTo);
        query = query.replaceAll("pInsId", pInsId);

        List result = dataService.externalQuery(query);

        //get required parameter passing from external
        //run query, build the report parameter and return
        reportParameters.put("invoiceSummaries", result);
        return reportParameters;
    }

    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}
