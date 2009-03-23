/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.report.viewdata.InvoiceSummary;
import chox.web.report.viewdata.InvoiceSummaryReportObject;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

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
        return "template_InvoiceSummaryReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    private Chorganisation getChorganisation(int orgId){
        
        Chorganisation chorg = new Chorganisation();
                
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation)dataService.getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        } 
        
        return chorg;
    }
    
    private Insurer getInsurer(int orgId){
        Insurer ins = new Insurer();
     
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            ins = (Insurer)dataService.getByCriteria(criteria);
            
        } catch (Throwable e) {
           e.printStackTrace();
        } 
        
        return ins;
    }
    
    public HashMap getReportParameters() {
        
        HashMap reportParameters = new HashMap();

        try {
            
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            
            String supplierId = "";
            String insurerId = "";
            Integer iSupplierId = -1;
            Integer iInsurerId = -1;
            
            String userOrgId = "";
            String userOrgLabel = "";
            String userOrgName = "";
            String selectedOrgId = "";
            String selectedOrgName = "All";
            String selectedOrgLabel = "";
            String reportColumnHeader = "";
            if(currentUser.getIsINS()){
                
                Insurer ins = currentUser.getUser().getInsurer();
                iInsurerId = ins.getId();
                userOrgId = iInsurerId.toString();
                
                reportColumnHeader = "Credit Hire Organisation";
                selectedOrgLabel = "Credit Hire Organisation"; 
                userOrgLabel = "Insurer";
                userOrgName = ins.getName(); 
                
                supplierId = (((String[]) externalParameter.get("supplierId"))[0]).trim();
                if(!supplierId.equalsIgnoreCase("")){
                    iSupplierId = Integer.parseInt(supplierId);
                    selectedOrgId = iSupplierId.toString().trim();
                    selectedOrgName = getChorganisation(iSupplierId).getName();
                }
                
            }else{
                
                Chorganisation chorg = currentUser.getUser().getChorganisation();
                iSupplierId = chorg.getId();
                userOrgId = iSupplierId.toString();
                
                reportColumnHeader = "Insurer";
                selectedOrgLabel = "Insurer"; 
                userOrgLabel = "Credit Hire Organisation";
                userOrgName = chorg.getName(); 
                
                insurerId = (((String[]) externalParameter.get("insurerId"))[0]).trim();
                if(!insurerId.equalsIgnoreCase("")){
                    iInsurerId = Integer.parseInt(insurerId);
                    selectedOrgId = iInsurerId.toString().trim();
                    selectedOrgName = getInsurer(iInsurerId).getName();
                }
                
            }
            
            StringBuffer sb = new StringBuffer();
            
            if(currentUser.getIsINS()){
                sb.append("Select chorganisation.id, chorganisation.name, ");
            }else{
                sb.append("Select insurer.id, insurer.name, ");                
            }
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as noInvoiceSubmitted,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as totalInvoiceValue,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoicePaymentLogged' and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as noInvoicesPaid,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoicePaymentLogged' and date_trunc('day', created_date) between @pInvUploadDateFrom and current_date) as valueOfPaidInvoices,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as noInvoiceAwaitingPayment,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as invoiceAwaitingPaymentValue,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler') and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as noInvoicePending,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler') and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as invoicePendingValue,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as noInvoiceWithdrawn,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as invoiceWithdrawnValue," );
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and panalty_charge>0.00 and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as noOfInvoicesWithPenalties,");
            sb.append("(select case when sum(panalty_charge) is null then 0.00 else sum(panalty_charge) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and panalty_charge>0.00 and date_trunc('day', created_date) between @pInvUploadDateFrom and @pInvUploadDateTo) as valueOfInvoicesWithPenalties ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on insurer_chorganisation.chorganisation_id = chorganisation.id ");
            sb.append("inner join insurer insurer on insurer_chorganisation.insurer_id = insurer.id ");
            
            if(currentUser.getIsINS()){
                
                sb.append(" where insurer_chorganisation.insurer_id=@pUserOrgId ");
                if(iSupplierId>0){
                    sb.append("and insurer_chorganisation.chorganisation_id = @selectedOrgId");
                }
                sb.append("order by chorganisation.id, chorganisation.name asc");
                
            }else{
                
                sb.append(" where insurer_chorganisation.chorganisation_id=@pUserOrgId ");
                if(iInsurerId>0){
                    sb.append("and insurer_chorganisation.insurer_id = @selectedOrgId");
                }
                sb.append("order by insurer.id, insurer.name asc"); 
                
            }
            
            String query = sb.toString();
            query = query.replaceAll("@pInvUploadDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
            query = query.replaceAll("@pInvUploadDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");
            query = query.replaceAll("@pUserOrgId", userOrgId);
            query = query.replaceAll("@selectedOrgId", selectedOrgId);
            
            List result = dataService.externalQuery(query);
            List<InvoiceSummary> invoiceSummaries = new ArrayList<InvoiceSummary>();
            
            for (Object o : result) {
                Map data = (Map) o;
                InvoiceSummary invoiceSummary = InvoiceSummary.getObject(data);
                invoiceSummaries.add(invoiceSummary);
            }

            InvoiceSummaryReportObject reportObject = new InvoiceSummaryReportObject();

            reportObject.setInvoiceUploadDateFrom(dataStart);
            reportObject.setInvoiceUploadDateTo(dataEnd);
            reportObject.setCreatedDate(new Date());

            reportParameters.put("invoiceSummaries", invoiceSummaries);
            reportParameters.put("reportObj", reportObject);
            reportParameters.put("userOrgLabel", userOrgLabel);
            reportParameters.put("userOrgName", userOrgName);
            reportParameters.put("selectedOrgName",selectedOrgName);
            reportParameters.put("selectedOrgLabel",selectedOrgLabel);
            reportParameters.put("reportColumnHeader", reportColumnHeader);
        } catch (Exception ex) {
        }
        
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
