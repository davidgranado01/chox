/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.services.DataService;
import chox.services.SystemLogService;
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

public class InvoiceSummaryReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;
    private SystemLogService systemLogService;
    
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
        boolean bAction = true;
        String sActionMsg = "";
        
        try {
            
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            Date dataStart = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateStart"))[0]);
            Date dataEnd = DateHelper.LocalDateFormat.parse(((String[]) externalParameter.get("DateEnd"))[0]);
            
            String supplierId = "";
            String insurerId = "";
            Integer iSupplierId = -1;
            Integer iInsurerId = -1;
            
            Integer userOrgId = -1;
            String userOrgLabel = "";
            String userOrgName = "";
            Integer selectedOrgId = -1;
            String selectedOrgName = "All";
            String selectedOrgLabel = "";
            String reportColumnHeader = "";
            
            userOrgName = currentUser.getUser().getOrganisationName();
            
            if(currentUser.getIsINS()){
                
                Insurer ins = currentUser.getUser().getInsurer();
                iInsurerId = ins.getId();
                userOrgId = iInsurerId;
                
                reportColumnHeader = "Credit Hire Organisation";
                selectedOrgLabel = "Credit Hire Organisation"; 
                userOrgLabel = "Insurer";
                
                supplierId = (((String[]) externalParameter.get("supplierId"))[0]).trim();
                if(!supplierId.equalsIgnoreCase("")){
                    iSupplierId = Integer.parseInt(supplierId);
                    selectedOrgId = iSupplierId;
                    selectedOrgName = getChorganisation(iSupplierId).getName();
                }
                
            }else{
                
                Chorganisation chorg = currentUser.getUser().getChorganisation();
                iSupplierId = chorg.getId();
                userOrgId = iSupplierId;
                
                reportColumnHeader = "Insurer";
                selectedOrgLabel = "Insurer"; 
                userOrgLabel = "Credit Hire Organisation";
                
                insurerId = (((String[]) externalParameter.get("insurerId"))[0]).trim();
                if(!insurerId.equalsIgnoreCase("")){
                    iInsurerId = Integer.parseInt(insurerId);
                    selectedOrgId = iInsurerId;
                    selectedOrgName = getInsurer(iInsurerId).getName();
                }

            }
            
            StringBuffer sb = new StringBuffer();
            
            if(currentUser.getIsINS()){
                sb.append("Select chorganisation.id, chorganisation.name, ");
            }else{
                sb.append("Select insurer.id, insurer.name, ");                
            }
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoiceSubmitted,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as totalInvoiceValue,");
            sb.append("(select case when count(distinct claim.claim_id) is null then 0 else count(distinct claim.claim_id) end as no_count from rpt_claim_invoice claim, (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged') audit where claim.claim_id=audit.claim_id and insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoicesPaid,");
            sb.append("(select case when sum(claim.total_to_pay) is null then 0 else sum(claim.total_to_pay) end as no_count from rpt_claim_invoice claim, (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged') audit where claim.claim_id=audit.claim_id and insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as valueOfPaidInvoices,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoiceAwaitingPayment,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoiceAwaitingPaymentValue,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler') and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoicePending,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler') and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoicePendingValue,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoiceWithdrawn,");
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoiceWithdrawnValue," );
            // sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between ::pInvUploadDateFrom and :pInvUploadDateTo) as faceValueOfPaidInvoiceValue, ");
            sb.append("(select case when sum(claim.total_gross) is null then 0 else sum(claim.total_gross) end as no_count from rpt_claim_invoice claim, (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged') audit where claim.claim_id=audit.claim_id and insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as faceValueOfPaidInvoiceValue,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status ='InvoicePaymentLogged' and claim_id in (select distinct claim_id from audit_trail where new_status in ('InvoiceEscalated', 'ContestedInvoiceReferredToCHO', 'ContestedInvoiceReferredToInsurer', 'InvoiceReferredToClaimsHandler')) and date(created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoiceDisputedSettled, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='InvoicePaymentLogged' where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a where total_day <= 30) as InvoiceSettledCat0Days, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='InvoicePaymentLogged' where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a where total_day > 30 and total_day <= 60) as InvoiceSettledCat30Days, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='InvoicePaymentLogged' where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a where total_day > 60 and total_day <= 90) as InvoiceSettledCat60Days, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='InvoicePaymentLogged' where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a where total_day > 90) as InvoiceSettledCat90Days, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (audit.update_date - invoice.created_date)))/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status='InvoicePaymentLogged' where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) as averageNoDaysOfInvoiceSettlement, ");
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (current_date - invoice.created_date)))/count(*)) as bigint) end as no_count from rpt_claim_invoice invoice where invoice.claim_id not in (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged') and invoice.status not in ('ClaimClosed','InvoiceRejectionAccepted') and date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id ) as averageAgeDaysOfPendingInvoices, ");
            sb.append("(select case when count(*) is null or count(*)<=0 or sum(total_to_pay)=0 then 0 else sum(total_to_pay)/count(*) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and status!='ClaimClosed' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as averageInvoiceValue, ");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and panalty_charge>0.00 and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noOfInvoicesWithPenalties,");
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and status = 'ClaimClosed' and claim_id not in (select distinct claim_id from audit_trail where new_status='InvoicePaymentLogged')) as noOfInvoicesClosed,");
            sb.append("(select case when sum(panalty_charge) is null then 0.00 else sum(panalty_charge) end as no_sum from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id and panalty_charge>0.00 and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as valueOfInvoicesWithPenalties ");
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on insurer_chorganisation.chorganisation_id = chorganisation.id ");
            sb.append("inner join insurer insurer on insurer_chorganisation.insurer_id = insurer.id ");
            
            if(currentUser.getIsINS()){
                
                sb.append("where insurer_chorganisation.insurer_id = :pUserOrgId ");
                
                if(iSupplierId>0){
                    sb.append("and insurer_chorganisation.chorganisation_id = :selectedOrgId ");
                }
                
                sb.append("order by chorganisation.id, chorganisation.name asc");
                
            }else{
                
                sb.append("where insurer_chorganisation.chorganisation_id = :pUserOrgId ");
                
                if(iInsurerId>0){
                    sb.append("and insurer_chorganisation.insurer_id = :selectedOrgId ");
                }
                
                sb.append("order by insurer.id, insurer.name asc");
                
            }
            
            String query = sb.toString();
            /*query = query.replaceAll("@pInvUploadDateFrom", "'" + DateHelper.DBDateFormat.format(dataStart) + "'");
            query = query.replaceAll("@pInvUploadDateTo", "'" + DateHelper.DBDateFormat.format(dataEnd) + "'");
            query = query.replaceAll("@pUserOrgId", userOrgId);
            query = query.replaceAll("@selectedOrgId", selectedOrgId);
            
            List result = dataService.externalQuery(query);*/

            //Emmanuel
            //27-07-2009
            //prevent SQL Injection
            Map paramMap = new HashMap();
            paramMap.put("pInvUploadDateFrom", dataStart);
            paramMap.put("pInvUploadDateTo", dataEnd);
            paramMap.put("pUserOrgId", userOrgId);

            if(query.contains("selectedOrgId"))
            {
                paramMap.put("selectedOrgId", selectedOrgId);
            }

            List result = dataService.externalQuery(query,paramMap);

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
            ex.printStackTrace();
            bAction = false;
            sActionMsg = ex.getLocalizedMessage();
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
    
    public void setSystemLogService(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    public String getReportCode() {
        return "RPT005";
    }
}
