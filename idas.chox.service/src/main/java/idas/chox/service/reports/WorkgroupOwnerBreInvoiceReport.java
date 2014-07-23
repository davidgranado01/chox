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
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.MathHelper;
import idas.chox.core.util.RoleHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.WorkgroupOwnerBreLineItem;
import idas.chox.service.reports.viewdata.WorkgroupOwnerBreReportObject;

/**
 *
 * @author rajareddydodda
 */
public class WorkgroupOwnerBreInvoiceReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(WorkgroupOwnerBreInvoiceReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public Map<String, Object> getReportParameters() throws Exception {

        LOG.debug("getReportParameters '{}' ");
        Map<String, Object> reportParameters = new HashMap<String, Object>();

        try {
            String supplierId;
            Integer selectedCHOId = -1;
            String selectedCHOName = "All";
            boolean isWorkgroupEnabled = true;
            Integer insurerId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            String rptInsurerName = "";
            Date serviceCommencingDate = null;
            Date startDate = null;
            Date endDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
            
            List<ReasonOfRejection> reasonsOfRejection = getReasonsOfRejection(user, false);

            LOG.debug("user={}", user);
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
                isWorkgroupEnabled = user.getInsurer().isWorkgroupEnable();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);

            if ((externalParameter.get("supplierId")) != null) {
                supplierId = ((String[]) externalParameter.get("supplierId"))[0];
                LOG.debug("SUPPLIER ID IS ={}", supplierId);
                if (!supplierId.equalsIgnoreCase("-1") && !supplierId.equalsIgnoreCase("") && !supplierId.equalsIgnoreCase("--- ALL ---")) {
                    selectedCHOId = TextHelper.getId(supplierId);
                    LOG.debug("selectedChoId ={}", selectedCHOId);
                    selectedCHOName = getChorganisation(selectedCHOId).getName();
                }
            }

            if (isWorkgroupEnabled) {
                if (((String[]) externalParameter.get("workgroupId")) != null) {
                    String workgropId = ((String[]) externalParameter.get("workgroupId"))[0];
                    if (!workgropId.equalsIgnoreCase("-1") && !workgropId.equalsIgnoreCase("") && !workgropId.equalsIgnoreCase("--- ALL ---")) {
                        selectedWorkgroupId = TextHelper.getId(((String[]) externalParameter.get("workgroupId"))[0]);
                        LOG.debug("selectedWorkgroupId={}", selectedWorkgroupId);
                    }
                }
            }

            if (((String[]) externalParameter.get("ownerId")) != null) {
                String ownerId = ((String[]) externalParameter.get("ownerId"))[0];
                if (!ownerId.equalsIgnoreCase("-1") && !ownerId.equalsIgnoreCase("") && !ownerId.equalsIgnoreCase("--- ALL ---")) {
                    selectedOwnerId = TextHelper.getId(((String[]) externalParameter.get("ownerId"))[0]);
                    LOG.debug("selectedOwnerId={}", selectedOwnerId);
                }
            }


            if (((String[]) externalParameter.get("startDate")) != null) {
                startDate = DateHelper.parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if (((String[]) externalParameter.get("endDate")) != null) {
                endDate = DateHelper.parse(((String[]) externalParameter.get("endDate"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
                LOG.debug("endDate={}", endDate.toString());
            }
            
            if (endDate == null || startDate == null) {
                throw new Exception("Start and End dates cannot be empty.");
            }

            if (endDate.before(startDate)) {
                throw new Exception("End date (" + endDate.toString() + ") is before start date (" + startDate.toString() +  ") ");
            }

            List<WorkgroupOwnerBreReportObject> workflowReportObjects = new ArrayList<WorkgroupOwnerBreReportObject>();
            if (isWorkgroupEnabled) {
                HashMap queryParameters = new HashMap();
                queryParameters.put("pInsurerId", insurerId);
                StringBuilder sb = new StringBuilder();
                sb.append("select id, name from workgroup where insurer_id = :pInsurerId and status = true ");
                if (selectedWorkgroupId != -1) {
                    sb.append("and id = :pWorkgroupId ");
                    queryParameters.put("pWorkgroupId", selectedWorkgroupId);
                }
                if (selectedOwnerId != -1) {
                    sb.append("and exists (select * from web_user_workgroup where workgroup_id = workgroup.id and user_id = :pOwnerId)");
                    queryParameters.put("pOwnerId", selectedOwnerId);
                }
                sb.append("order by name");
                List result = reportDataService.getReportData(sb.toString(), queryParameters);
                for (Object o : result) {
                    Map data = (Map) o;
                    WorkgroupOwnerBreReportObject workflowReportObject = new WorkgroupOwnerBreReportObject();
                    workflowReportObject.setWorkgroup(data.get("name").toString());
                    workflowReportObject.setId((Integer) data.get("id"));
                    workflowReportObjects.add(workflowReportObject);
                    LOG.debug("Workgroup added: {}", workflowReportObject.getWorkgroup());
                }
            } else {
                WorkgroupOwnerBreReportObject workflowReportObject = new WorkgroupOwnerBreReportObject();
                workflowReportObjects.add(workflowReportObject);
                LOG.debug("Empty Workgroup added.");
            }

            for (WorkgroupOwnerBreReportObject obj : workflowReportObjects) {
                LOG.debug("Getting members of workgroup: {}", obj.getWorkgroup());
                HashMap queryParameters = new HashMap();
                StringBuffer sb = new StringBuffer();
                if (isWorkgroupEnabled && selectedOwnerId == -1) {
                    queryParameters.put("pWorkgroupId", obj.getId());
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w, web_user_role wur, web_user_user_role wuur where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH' and u.status = true ");
                } else if (isWorkgroupEnabled) {
                    queryParameters.put("pWorkgroupId", obj.getId());
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id ");
                } else {
                    queryParameters.put("pInsurerId", insurerId);
                    LOG.debug("Added to parameter map: {}={}", "pInsurerId", insurerId);
                    sb.append("select u.id as id, u.first_name || ' ' || u.last_name as name from web_user u, web_user_role wur, web_user_user_role wuur where u.insurer_id = :pInsurerId and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH'");
                }
                if (selectedOwnerId != -1) {
                    queryParameters.put("pOwnerId", selectedOwnerId);
                    LOG.debug("Added to parameter map: {}={}", "pOwnerId", selectedOwnerId);
                    sb.append("and u.id = :pOwnerId ");
                }
                sb.append("order by u.last_name");
                LOG.debug("Querying for users with: {}", sb.toString());
                List result = reportDataService.getReportData(sb.toString(), queryParameters);
                LOG.debug("Got {} results", result.size());
                boolean first = true;
                for (Object o : result) {
                    Map data = (Map) o;
                    if (!first) {
                        data.remove("workgroup");
                    } else {
                        first = false;
                    }
                    WorkgroupOwnerBreLineItem workflowLineItem = WorkgroupOwnerBreLineItem.getObject(data);
                    LOG.debug("Getting stats for user: {}", workflowLineItem.getName());
                    // Now construct query to get claim owner stats
                    sb = new StringBuffer();
                    sb.append("select ");

                    /*
                     * No of Invoices uploaded
                     */
                    sb.append("(select count(*) from claim c, invoice i where c.invoice_id=i.id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId and c.claim_type not in ")
                        .append(ClaimType.getInsurerUploadTypeOrdinals())
                        .append("and c.insurer_id = :pInsurerId and i.created_date between :pStartDate and :pEndDate ) as no_invoices_uploaded, ");

                    /*
                     * No of invoices Approved by BRE
                     * 
                     */
                    sb.append("(select count(*) from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId ")
                      .append("and c.insurer_id = :pInsurerId ")
                      .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                      .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                      .append("and i.created_date between :pStartDate and :pEndDate ) as no_invoices_approved_bre, ");

                    /*
                     * No of Invoices approved by BRE and Then Disputed
                     * 
                     */
                    sb.append("(select count(distinct b.id) from (select c.id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId ")
                      .append("and c.insurer_id = :pInsurerId ")
                      .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                      .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                      .append("and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a ")
                      .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' ) as no_invoices_approved_bre_disputed, ");


                    /*
                     *
                     * % of Invoices Approves By Business Rules Engine And  Not Disputed  And Paid Within 15 Days
                     * 
                     */
                    sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId ")
                      .append("and c.insurer_id = :pInsurerId ")
                      .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                      .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                      .append("and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a ")
                      .append("where b.id=a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                      .append("and not exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                      .append("and a.update_date between b.created_date  and  b.created_date + interval '15 days' ) as no_invoices_approved_bre_not_disputed_paid_15days, ");


                    /**
                     *
                     * % of Invoices Approved By Business Rules Engine And Not Disputed And Paid Within 30 Days
                     * 
                     */
                    sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId ")
                      .append("and c.insurer_id = :pInsurerId ")
                      .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                      .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                      .append("and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a ")
                      .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                      .append("and not exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                      .append("and a.update_date between b.created_date  and  b.created_date + interval '30 days' ) as no_invoices_approved_bre_not_disputed_paid_30days, ");

                    /*
                     * % of Invoices Approved  By Business Rules Engine And  Disputed And Paid  within 15 Days
                     * 
                     */
                    sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId ")
                      .append("and c.insurer_id = :pInsurerId ")
                      .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                      .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                      .append("and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a ")
                      .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                      .append("and exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                      .append("and a.update_date between b.created_date  and  b.created_date + interval '15 days' ) as no_invoices_approved_bre_disputed_paid_15days, ");

                    /**
                     * % of Invoices Approved  By Business Rules Engine and Disputed And Paid Within30 Days 
                     * 
                     */
                    sb.append("(select count(*) from (select c.id, i.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    if(selectedCHOId>0) {
                        sb.append("and c.chorganisation_id = :pChoId ");
                    }
                    sb.append("and c.claim_owner_id = :pOwnerId ")
                      .append("and c.insurer_id= :pInsurerId ")
                      .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                      .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                      .append("and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a ")
                      .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'InvoicePaymentLogged' ")
                      .append("and exists ( select * from audit_trail a1 where a1.claim_id=b.id and a1.reverted=false and a1.new_status='ContestedInvoiceReferredToCHO') ")
                      .append("and a.update_date between b.created_date  and  b.created_date + interval '30 days' ) as no_invoices_approved_bre_disputed_paid_30days, ");

                    for(ReasonOfRejection ror : reasonsOfRejection){
                        sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                        if(isWorkgroupEnabled) {
                            sb.append("and c.workgroup_id = :pWorkgroupId ");
                        }
                        if(selectedCHOId>0) {
                            sb.append("and c.chorganisation_id = :pChoId ");
                        }
                        sb.append("and c.claim_owner_id = :pOwnerId ")
                            .append("and c.insurer_id = :pInsurerId ")
                            .append("and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect', 'InvoiceUnassigned') ")
                            .append("and a.reverted=false and a.new_status='InvoiceApprovedByBRE' ")
                            .append("and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i ")
                            .append("where b.id =a.claim_id and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToCHO' ")
                            .append("and b.invoice_id = i.id and a.invoice_reason_of_rejection = ")
                            .append(ror.getId())
                            .append(" and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.reverted=false and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_")
                            .append(ror.getRorName());
                            if(reasonsOfRejection.indexOf(ror) != reasonsOfRejection.size() -1) {
                            sb.append(", ");
                        }
                    }
                    
                    queryParameters = new HashMap();
                    if (isWorkgroupEnabled) {
                        queryParameters.put("pWorkgroupId", obj.getId());
                    }
                    if(selectedCHOId>0){
                      queryParameters.put("pChoId", selectedCHOId);  
                    }
                    queryParameters.put("pOwnerId", workflowLineItem.getId());
                    queryParameters.put("pInsurerId", insurerId);
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                    List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                    // parse query results and add to workflowLineItem
                    if (detailData.size() > 0) {
                        workflowLineItem.updateObject((Map) detailData.get(0), reasonsOfRejection);
                        obj.getOwner().add(workflowLineItem);
                    }
                }
            }

            // Now build report parameters
            reportParameters.put("reasonsOfRejectionHeader", getReasonsOfRejection(user, true));
            reportParameters.put("reasonsOfRejection", reasonsOfRejection);
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("choName", selectedCHOName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("serviceDate", serviceCommencingDate);
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("workflowLineItems", workflowReportObjects);
        } catch (Exception ex) {
            LOG.debug("Error thrown generating owner-workflow report: {}", ex.getMessage());
            throw ex;
        }

        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        user = ((WebUser) externalParameter.get("CurrentUser"));

        LOG.debug("user '{}' ", user);
        if (user.getInsurer().isWorkgroupEnable()) {
            return "template_WorkgroupOwnerBreInvoiceReport.xls";
        } else {
            return "template_OwnerBreInvoiceReport.xls";
        }
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {

        LOG.debug("build '{}' ");
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    private Chorganisation getChorganisation(int orgId) {

        Chorganisation chorg = new Chorganisation();

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Chorganisation.class);
            criteria.add(Restrictions.eq("id", orgId));
            chorg = (Chorganisation) baseDataService.getByCriteria(criteria);

        } catch (Exception ex) {
            LOG.error("Error getting Chorganisation for id={}:\n", orgId, ex);
        }

        return chorg;
    }

    @Override
    public String getReportCode() {
        return "RPT032";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }
    
    private List<ReasonOfRejection> getReasonsOfRejection(WebUser currentUser, boolean displayForHeader) {
        List<ReasonOfRejection> reportRows = new ArrayList<ReasonOfRejection>();
        List result;
        if(currentUser.getInsurer() != null){
            String query = "select ror.id, ror.name from reason_of_rejection ror join invoice iv on ror.id = iv.reason_of_rejection_id " +
                    "where ror.type='Invoice' " +
                    "and ror.insurer_id = :insurerId group by ror.id " +
                    "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Invoice' and insurer_id = :insurerId order by name asc ";
            Map paramMap = new HashMap();
            paramMap.put("insurerId", currentUser.getInsurer().getId());
            result = reportDataService.getReportData(query, paramMap);
        } else {
            String query = "select ror.id, ror.name from reason_of_rejection ror join invoice iv on ror.id = iv.reason_of_rejection_id " +
                    "where ror.type='Invoice' " +
                    "and ror.insurer_id in (select insurer_id from insurer_chorganisation where chorganisation_id = :choId)" +
                    " group by ror.id " +
                    "union select id, name from reason_of_rejection where (gta_active = true or insurer_vs_insurer_active=true or subscriber_active=true or fixed_fee_active=true or insurer_upload_active = true or tpi_active= true) and type='Invoice' and insurer_id in" +
                    " (select insurer_id from insurer_chorganisation where chorganisation_id = :choId) order by name asc ";
            Map paramMap = new HashMap();
            paramMap.put("choId", currentUser.getChorganisation().getId());
            result = reportDataService.getReportData(query, paramMap);
        }
        
        for (Object o : result) {
            Map data = (Map) o;
            ReasonOfRejection reportRow = new ReasonOfRejection();
            reportRow.setId(MathHelper.getIntegerValue(data.get("id".toLowerCase())));
            if(!displayForHeader){
                reportRow.setRorName(data.get("name").toString().replaceAll("\\s+", "_").replaceAll("[^A-Za-z0-9_]", ""));
            } else {
                reportRow.setRorName(data.get("name").toString());
            }
            reportRows.add(reportRow);
        }

        return reportRows;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return (Boolean) externalParameter.get("isBrandingReport");
    }
    
}
