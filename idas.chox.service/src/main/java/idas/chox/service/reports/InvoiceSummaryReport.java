package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.InvoiceSummary;
import idas.chox.service.reports.viewdata.InvoiceSummaryReportObject;


public class InvoiceSummaryReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSummaryReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public InvoiceSummaryReport() {
        reportParameterNames = new ArrayList<String>();
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_InvoiceSummaryReport.xls";
    }

    @Override
    public String getReportCode() {
        return "RPT019";
    }

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting Chorganisation for id={}: {}", orgId, e.getMessage());
        }

        return chorg;
    }

    private Insurer getInsurer(int orgId) {
        Insurer ins = new Insurer();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Insurer.class);
            criteria.add(Restrictions.eq("id", orgId));
            ins = (Insurer) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting Insurer for id={}: {}", orgId, e.getMessage());
        }

        return ins;
    }
    
    private Workgroup getWorkgroup(int workgroupId) {
        Workgroup wg = new Workgroup();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(Workgroup.class);
            criteria.add(Restrictions.eq("id", workgroupId));
            wg = (Workgroup) baseDataService.getByCriteria(criteria);

        } catch (Exception e) {
            LOG.error("Error getting workgroup for id={}: {}", workgroupId, e.getMessage());
        }

        return wg;
    }
    
    private WebUser getClaimOwner(int ownerId) {
        WebUser wu = new WebUser();
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUser.class);
            criteria.add(Restrictions.eq("id", ownerId));
            wu = (WebUser) baseDataService.getByCriteria(criteria);
        } catch (Exception e) {
            LOG.error("Error getting workgroup for id={}: {}", ownerId, e.getMessage());
        }
        return wu;
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {

        Map<String, Object> reportParameters = new HashMap<String, Object>();

        try {

            WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
            // PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));

            Date dataStart = null;
            Date dataEnd = null;

            if (((String[]) externalParameter.get("DateStart")) != null) {
                dataStart = DateHelper.parse(((String[]) externalParameter.get("DateStart"))[0]);
            }

            if (((String[]) externalParameter.get("DateStart")) != null) {
                dataEnd = DateHelper.parse(((String[]) externalParameter.get("DateEnd"))[0]);
                dataEnd = DateHelper.setEndOfDay(dataEnd);
            }
            
            if (dataEnd == null || dataStart == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if (dataEnd.before(dataStart)) {
                throw new Exception("End date (" + dataEnd.toString() + ") is before start date (" + dataStart.toString() +  ") ");
            }

            String supplierId;
            String insurerId;
            Integer iSupplierId = -1;
            Integer iInsurerId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            boolean isWorkgroupEnabled = false;

            Integer userOrgId;
            String userOrgLabel;
            String userOrgName;
            Integer selectedOrgId = -1;
            String selectedOrgName = "All";
            String selectedWorkgroupName = null;
            String selectedClaimOwnerName = null;
            String selectedOrgLabel;
            String reportColumnHeader;

            userOrgName = currentUser.getOrganisationName();

            if (currentUser.getInsurer() != null) {

                Insurer ins = currentUser.getInsurer();
                iInsurerId = ins.getId();
                userOrgId = iInsurerId;
                isWorkgroupEnabled = ins.isWorkgroupEnable();

                reportColumnHeader = "Credit Hire Organisation";
                selectedOrgLabel = "Credit Hire Organisation";
                userOrgLabel = "Insurer";

                if ((externalParameter.get("supplierId")) != null) {
                    supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                    if (!supplierId.equalsIgnoreCase("-1") && !supplierId.equalsIgnoreCase("") && !supplierId.equalsIgnoreCase("--- ALL ---")) {
                        iSupplierId = TextHelper.getId(supplierId);
                        selectedOrgId = iSupplierId;
                        selectedOrgName = getChorganisation(iSupplierId).getName();
                    }
                }
                
                if (isWorkgroupEnabled) {
                    if (((String[]) externalParameter.get("workgroupId")) != null) {
                        String workgropId = ((String[]) externalParameter.get("workgroupId"))[0];
                        if (!workgropId.equalsIgnoreCase("") && !workgropId.equalsIgnoreCase("-1") && !workgropId.equalsIgnoreCase("--- ALL ---")) {
                            selectedWorkgroupId = TextHelper.getId(((String[]) externalParameter.get("workgroupId"))[0]);
                            if(getWorkgroup(selectedWorkgroupId).getName()!=null){
                              selectedWorkgroupName = getWorkgroup(selectedWorkgroupId).getName();  
                            }else{
                               LOG.error("workgroup is null for the given id={}, generating report without workgroup", selectedWorkgroupId);
                               selectedWorkgroupId = -1; 
                            }
                             
                            LOG.debug("selectedWorkgroupId={}", selectedWorkgroupId);
                        }
                    }
                }

                if (((String[]) externalParameter.get("ownerId")) != null) {
                    String ownerId = ((String[]) externalParameter.get("ownerId"))[0];
                    if (!ownerId.equalsIgnoreCase("-1") && !ownerId.equalsIgnoreCase("") && !ownerId.equalsIgnoreCase("--- ALL ---")) {
                        selectedOwnerId = TextHelper.getId(((String[]) externalParameter.get("ownerId"))[0]);
                        if(getClaimOwner(selectedOwnerId).getDisplayName()!=null && getClaimOwner(selectedOwnerId).getInsurer().getId().compareTo(currentUser.getInsurer().getId())==0){
                           selectedClaimOwnerName = getClaimOwner(selectedOwnerId).getDisplayName();
                        }else{
                           LOG.error("claimOwner is null or do not belongs to this insurer, selected Claimownerid={}, current logged in insurer {}", selectedOwnerId,currentUser.getInsurer().getName());
                           selectedOwnerId = -1;
                        }
                        LOG.debug("selectedOwnerId={}", selectedOwnerId);
                    }
                }

            } else {

                Chorganisation chorg = currentUser.getChorganisation();
                iSupplierId = chorg.getId();
                userOrgId = iSupplierId;
                

                reportColumnHeader = "Insurer";
                selectedOrgLabel = "Insurer";
                userOrgLabel = "Credit Hire Organisation";

                if ((externalParameter.get("insurerId")) != null) {
                    insurerId = ((String[]) externalParameter.get("insurerId"))[0];
                    if (!insurerId.equalsIgnoreCase("-1") && !insurerId.equalsIgnoreCase("") && !insurerId.equalsIgnoreCase("--- ALL ---")) {
                        iInsurerId = TextHelper.getId(insurerId);
                        selectedOrgId = iInsurerId;
                        selectedOrgName = getInsurer(iInsurerId).getName();
                        
                    }
                }
            }

            StringBuilder sb = new StringBuilder();

            if (currentUser.getInsurer() != null) {
                sb.append("Select chorganisation.id, chorganisation.name, ");
            } else {
                sb.append("Select insurer.id, insurer.name, ");
            }

            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id ");
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append("and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom ") 
              .append("and :pInvUploadDateTo) as noInvoiceSubmitted,");
            
            
            
            sb.append("(select case when sum(original_full_total_to_pay) is null then 0.00 else sum(original_full_total_to_pay) end as no_sum from rpt_claim_invoice ")
              .append("where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append("and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as valueInvoicesSubmitted,"); 
            
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append("and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoicePaymentLogged','PaymentReceived', 'ManualInvoicePaid') ")
              .append("and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoicesPaid,"); 
            
            
            
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice ")
              .append( "where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and status in ('InvoicePaymentLogged','PaymentReceived', 'ManualInvoicePaid') and date_trunc('day', created_date) between :pInvUploadDateFrom ")
              .append( "and :pInvUploadDateTo) as valueOfPaidInvoices,");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id ")
              .append( "and chorganisation_id = insurer_chorganisation.chorganisation_id and status='AwaitingInvoicePayment' "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoiceAwaitingPayment,");
            
            
            
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice ")
              .append( "where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and status='AwaitingInvoicePayment' and date_trunc('day', created_date) between :pInvUploadDateFrom ")
              .append( "and :pInvUploadDateTo) as invoiceAwaitingPaymentValue,");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id ")
              .append( "and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and status in ('InvoiceReferredToEngineer', 'InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceUnassigned', 'InvoiceEscalatedToHandler', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceBREApproved', 'AwaitingLitigationOutcome', 'ManualInvoiceUnassigned') ")
              .append( "and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoicePending,");
            
            
            
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice ")
              .append( "where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and status in ('InvoiceReferredToEngineer','InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer','ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceUnassigned', 'InvoiceEscalatedToHandler', 'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceBREApproved', 'AwaitingLitigationOutcome', 'ManualInvoiceUnassigned') ")
              .append( "and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoicePendingValue,");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id ")
              .append( "and chorganisation_id = insurer_chorganisation.chorganisation_id and status='InvoiceRejectionAccepted' "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as noInvoiceWithdrawn,");
            
            
            
            sb.append("(select case when sum(total_to_pay) is null then 0.00 else sum(total_to_pay) end as no_sum from rpt_claim_invoice ")
              .append( "where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and status='InvoiceRejectionAccepted' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoiceWithdrawnValue,");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id ")
              .append( "and chorganisation_id = insurer_chorganisation.chorganisation_id and status in ('InvoicePaymentLogged', 'PaymentReceived', 'ManualInvoicePaid') "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append("and exists (select * from audit_trail where audit_trail.claim_id=rpt_claim_invoice.claim_id and reverted=false ")
              .append( "and new_status in ('InvoiceEscalated', 'ContestedInvoiceReferredToCHO', 'ContestedInvoiceReferredToInsurer', 'InvoiceReferredToClaimsHandler')) ")
              .append( "and date(created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as invoiceDisputedSettled, ");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count ")
              .append( "from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day ")
              .append( "from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.reverted=false ")
              .append( "and audit.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') and not exists (select claim_id from audit_trail where reverted=false and status='ClaimClosed' ")
              .append( "and claim_id=audit.claim_id) and status in ('InvoicePaymentLogged', 'PaymentReceived', 'ManualInvoicePaid') where date(invoice.created_date) between :pInvUploadDateFrom")
              .append( " and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id ");
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a ")
              .append( "where total_day <= 30) as InvoiceSettledCat0Days, ");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count ")
              .append( "from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day ")
              .append( "from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.reverted=false ")
              .append( "and audit.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') and not exists (select claim_id from audit_trail where reverted=false ")
              .append( "and status='ClaimClosed' and claim_id=audit.claim_id) ")
              .append( "and status in ('InvoicePaymentLogged', 'PaymentReceived', 'ManualInvoicePaid') where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a where total_day > 30 ")
              .append( "and total_day <= 60) as InvoiceSettledCat30Days, ");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count ")
              .append( "from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day ")
              .append( "from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.reverted=false and audit.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and not exists (select claim_id from audit_trail where reverted=false and status='ClaimClosed' and claim_id=audit.claim_id) ")
              .append( "and status in ('InvoicePaymentLogged', 'PaymentReceived', 'ManualInvoicePaid') where date(invoice.created_date) between :pInvUploadDateFrom ")
              .append( "and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a ")
              .append( "where total_day > 60 and total_day <= 90) as InvoiceSettledCat60Days, ");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count ")
              .append( "from (select case when EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) is null then 0 else EXTRACT(DAY FROM (audit.update_date - invoice.created_date)) end as total_day ")
              .append( "from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.reverted=false and audit.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and not exists (select * from audit_trail where reverted=false and status='ClaimClosed' and claim_id=audit.claim_id) ")
              .append( "and status in ('InvoicePaymentLogged', 'PaymentReceived', 'ManualInvoicePaid') where date(invoice.created_date) between :pInvUploadDateFrom ")
              .append( "and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) a ")
              .append( "where total_day > 90) as InvoiceSettledCat90Days, ");
            
            
            
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (audit.update_date - invoice.created_date)))/count(*)) as bigint) end as no_count ")
              .append( "from rpt_claim_invoice invoice inner join audit_trail audit on audit.claim_id=invoice.claim_id and audit.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') and audit.reverted=false "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "where date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo and invoice.insurer_id=insurer_chorganisation.insurer_id ")
              .append( "and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id) as averageNoDaysOfInvoiceSettlement, ");
            
            
            
            sb.append("(select case when count(*) is null or count(*) = 0 then 0 else cast(round(sum(EXTRACT(DAY FROM (current_date - invoice.created_date)))/count(*)) as bigint) end as no_count ")
              .append( "from rpt_claim_invoice invoice where invoice.claim_id not in (select distinct claim_id from audit_trail where reverted=false and new_status='InvoicePaymentLogged') ")
              .append( "and invoice.status not in ('ClaimClosed','InvoiceRejectionAccepted') and date(invoice.created_date) between :pInvUploadDateFrom and :pInvUploadDateTo "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and invoice.insurer_id=insurer_chorganisation.insurer_id and invoice.chorganisation_id=insurer_chorganisation.chorganisation_id ) as averageAgeDaysOfPendingInvoices, ");
            
            
            
            sb.append("(select case when count(*) is null or count(*)<=0 or sum(original_full_total_to_pay)=0 then 0 else sum(original_full_total_to_pay)/count(*) end as no_sum ")
              .append( "from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and status!='ClaimClosed' and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as averageInvoiceValue, ");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice where insurer_id = insurer_chorganisation.insurer_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and chorganisation_id = insurer_chorganisation.chorganisation_id and total_penalty_charge>0.00 and date_trunc('day', created_date) between :pInvUploadDateFrom ")
              .append( "and :pInvUploadDateTo) as noOfInvoicesWithPenalties,");
            
            
            
            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from rpt_claim_invoice invoice where insurer_id = insurer_chorganisation.insurer_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo ")
              .append( "and status = 'ClaimClosed' and not exists (select * from audit_trail a where a.claim_id=invoice.claim_id and a.reverted=false and a.new_status='InvoicePaymentLogged')) as noOfInvoicesClosed,");
            
            
            
            sb.append("(select case when sum(total_to_pay) is null then 0 else sum(total_to_pay) end as no_count from rpt_claim_invoice invoice where insurer_id = insurer_chorganisation.insurer_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and chorganisation_id = insurer_chorganisation.chorganisation_id and date_trunc('day', created_date) between :pInvUploadDateFrom ")
              .append( "and :pInvUploadDateTo and status = 'ClaimClosed' and not exists (select * from audit_trail a where a.claim_id=invoice.claim_id and a.reverted=false and a.new_status='InvoicePaymentLogged')) as valueOfInvoicesClosed,");
            
            
            
            sb.append("(select case when sum(total_penalty_charge) is null then 0.00 else sum(total_penalty_charge) end as no_sum from rpt_claim_invoice invoice ")
              .append( "where insurer_id = insurer_chorganisation.insurer_id and chorganisation_id = insurer_chorganisation.chorganisation_id "); 
            if(isWorkgroupEnabled && selectedWorkgroupId>0 ) {
                sb.append("and workgroup_id = :pWorkgroupId ");
            }
            if(selectedOwnerId>0 ) {
                sb.append("and owner = :pOwnerId ");
            }
            sb.append( "and total_penalty_charge>0.00 and date_trunc('day', created_date) between :pInvUploadDateFrom and :pInvUploadDateTo) as valueOfInvoicesWithPenalties ");
            
            
            
            sb.append("from insurer_chorganisation insurer_chorganisation ");
            sb.append("inner join chorganisation chorganisation on insurer_chorganisation.chorganisation_id = chorganisation.id ");
            sb.append("inner join insurer insurer on insurer_chorganisation.insurer_id = insurer.id ");

            
            
            if (currentUser.getInsurer() != null) {

                sb.append("where insurer_chorganisation.insurer_id = :pUserOrgId ");

                if (iSupplierId > 0) {
                    sb.append("and insurer_chorganisation.chorganisation_id = :selectedOrgId ");
                }

                sb.append("order by chorganisation.id, chorganisation.name asc");

            } else {

                sb.append("where insurer_chorganisation.chorganisation_id = :pUserOrgId ");

                if (iInsurerId > 0) {
                    sb.append("and insurer_chorganisation.insurer_id = :selectedOrgId ");
                }

                sb.append("order by insurer.id, insurer.name asc");

            }

            String query = sb.toString();
            LOG.debug(query);
            Map paramMap = new HashMap();
            paramMap.put("pInvUploadDateFrom", dataStart);
            paramMap.put("pInvUploadDateTo", dataEnd);
            paramMap.put("pUserOrgId", userOrgId);
            if (isWorkgroupEnabled && selectedWorkgroupId>0) {
                paramMap.put("pWorkgroupId", selectedWorkgroupId);
            }
            if(selectedOwnerId>0 ) {
                paramMap.put("pOwnerId", selectedOwnerId);
            }

            if (query.contains("selectedOrgId")) {
                paramMap.put("selectedOrgId", selectedOrgId);
            }

            List result = reportDataService.getReportData(query, paramMap);

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
            reportParameters.put("selectedOrgName", selectedOrgName);
            reportParameters.put("selectedOrgLabel", selectedOrgLabel);
            reportParameters.put("reportColumnHeader", reportColumnHeader);
            if(currentUser.getInsurer() != null && selectedClaimOwnerName != null){
                reportParameters.put("claimOwnerName", selectedClaimOwnerName);
            }
            if(currentUser.getInsurer() != null && currentUser.getInsurer().isWorkgroupEnable() && selectedWorkgroupName != null){
                reportParameters.put("workgroupName", selectedWorkgroupName);
            }
            

        } catch (Exception ex) {
            LOG.error("Exception generating Invoice Summary Report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("    Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }

        return reportParameters;
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
}
