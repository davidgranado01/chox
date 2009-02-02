/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.services.DataService;
import chox.web.report.viewdata.InvoiceSummary;
import chox.web.report.viewdata.InvoiceSummaryReportObject;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public class InvoiceSummaryReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public InvoiceSummaryReport() {
        reportParameterNames = new ArrayList<String>();
    }

    public String getReportTemplateFileName() {
        return "template_InvoiceSummaryReport2.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();

        String dataStart = ((String[])externalParameter.get("DateStart"))[0];
        String dataEnd = ((String[])externalParameter.get("DateEnd"))[0];
        Integer orgId = Integer.parseInt(((String[])externalParameter.get("OrgId"))[0]);
        PermissionedUser currentUser = ((PermissionedUser)externalParameter.get("CurrentUser"));

        String query = "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoiceSubmitted,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as totalInvoiceValue,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoicePaymentLogged' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoicesPaid,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoicePaymentLogged' and date_trunc('day', created_date) between pInvUploadDateFrom and current_date) as valueOfPaidInvoices,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoiceAwaitingPayment,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as invoiceAwaitingPaymentValue,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO') and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoicePending,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO') and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as invoicePendingValue,"
        + "(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as noInvoiceWithdrawn,"
        + "(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between pInvUploadDateFrom and pInvUploadDateTo) as invoiceWithdrawnValue" 
        + " from chorganisation chorganisation inner join insurer_chorganisation insurer_chorganisation on insurer_chorganisation.chorganisation_id = chorganisation.id inner join insurer insurer on insurer_chorganisation.insurer_id = insurer.id";
        
        if (orgId > 0) {
            if (currentUser.getIsCHO()) {
                query = "select chorganisation.id,insurer.id,insurer.name," + query;
                query += " where insurer_chorganisation.insurer_id=pOrgId";
                query += " and insurer_chorganisation.chorganisation_id=" + currentUser.getUser().getChorganisation().getId();
            } else {
                query = "select chorganisation.id,insurer.id,chorganisation.name," + query;
                query += " where insurer_chorganisation.chorganisation_id=pOrgId";
                query += " and insurer_chorganisation.insurer_id=" + currentUser.getUser().getInsurer().getId();
            }
        }
        else
        {
             if (currentUser.getIsCHO()) {
                query = "select chorganisation.id,insurer.id,insurer.name," + query;
                query += " where insurer_chorganisation.chorganisation_id=" + currentUser.getUser().getChorganisation().getId();;
            } else {
                query = "select chorganisation.id,insurer.id,chorganisation.name," + query;
                query += " where insurer_chorganisation.insurer_id=" + currentUser.getUser().getInsurer().getId();
            }
        }
        
        query = query.replaceAll("pInvUploadDateFrom", "'" + dataStart + "'");
        query = query.replaceAll("pInvUploadDateTo",  "'" + dataEnd +  "'");
        query = query.replaceAll("pOrgId", orgId.toString());

        List result = dataService.externalQuery(query);
        List<InvoiceSummary> invoiceSummaries = new ArrayList<InvoiceSummary>();
        for(Object o : result)
        {
            Map data = (Map)o;
            InvoiceSummary invoiceSummary = InvoiceSummary.getObject(data);
            invoiceSummaries.add(invoiceSummary);            
        }
        
        InvoiceSummaryReportObject reportObject = new InvoiceSummaryReportObject();
        
        reportObject.setInvoiceUploadDateFrom(dataStart);
        reportObject.setInvoiceUploadDateTo(dataEnd);
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        reportObject.setCreatedDate(sdf.format(new Date()));
        reportObject.setName(currentUser.getIsCHO() ? currentUser.getUser().getChorganisation().getName() : currentUser.getUser().getInsurer().getName());
        
        reportParameters.put("invoiceSummaries", invoiceSummaries);
        reportParameters.put("reportObj", reportObject);
        reportParameters.put("insurerObj", reportObject);
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
