/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
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
    Map externalParameter;
    List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public HashMap getReportParameters() {


        LOG.error("getReportParameters '{}' ");
        HashMap reportParameters = new HashMap();
        Map paramMap = new HashMap();
        try {
            boolean isWorkgroupEnabled = true;
            Integer insurerId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            String rptInsurerName = "";
            Date serviceCommencingDate = null;
            Date startDate = null;
            Date endDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));

            LOG.debug("user={}"+user);
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
                isWorkgroupEnabled = user.getInsurer().isWorkgroupEnable();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);
            if (isWorkgroupEnabled) {
                if(((String[]) externalParameter.get("workgroupId"))!=null){
                    selectedWorkgroupId = TextHelper.getId(((String[]) externalParameter.get("workgroupId"))[0]);
                    LOG.debug("selectedWorkgroupId={}", selectedWorkgroupId);
                }
            }

            if(((String[]) externalParameter.get("ownerId"))!=null){
                selectedOwnerId = TextHelper.getId(((String[]) externalParameter.get("ownerId"))[0]);
                LOG.debug("selectedOwnerId={}", selectedOwnerId);
            }

           
            if(((String[]) externalParameter.get("startDate"))!=null){
                startDate = DateHelper.Parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if(((String[]) externalParameter.get("endDate"))!=null){
                endDate = DateHelper.Parse(((String[]) externalParameter.get("endDate"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
                LOG.debug("endDate={}", endDate.toString());
            }

            // First, update user service stats for Insurer
//            baseDataService.query("select update_user_service(" + insurerId + ")");
           // baseDataService.callUpdateUserService(insurerId);

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
                List result = baseDataService.externalQuery(sb.toString(), queryParameters);
                for (Object o : result) {
                    Map data = (Map) o;
                    WorkgroupOwnerBreReportObject workflowReportObject = new WorkgroupOwnerBreReportObject();
                    workflowReportObject.setWorkgroup(data.get("name").toString());
                    workflowReportObject.setId((Integer)data.get("id"));
                    workflowReportObjects.add(workflowReportObject);
                    LOG.debug("Workgroup added: {}", workflowReportObject.getWorkgroup());
                }
            }
            else {
                WorkgroupOwnerBreReportObject workflowReportObject = new WorkgroupOwnerBreReportObject();
                workflowReportObjects.add(workflowReportObject);
                    LOG.debug("Empty Workgroup added.");
            }

            for (WorkgroupOwnerBreReportObject obj: workflowReportObjects) {
                LOG.debug("Getting members of workgroup: {}", obj.getWorkgroup());
                HashMap queryParameters = new HashMap();
                StringBuffer sb = new StringBuffer();
                if (isWorkgroupEnabled && selectedOwnerId == -1) {
                    queryParameters.put("pWorkgroupId", obj.getId());
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w, web_user_role wur, web_user_user_role wuur where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH' and u.status = true ");
                }
                else if (isWorkgroupEnabled) {
                    queryParameters.put("pWorkgroupId", obj.getId());
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id ");
                }
                else {
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
                List result = baseDataService.externalQuery(sb.toString(), queryParameters);
                LOG.debug("Got {} results", result.size());
                boolean first = true;
                for (Object o : result) {
                    Map data = (Map) o;
                    if (!first)
                        data.remove("workgroup");
                    else
                        first = false;
                    WorkgroupOwnerBreLineItem workflowLineItem = WorkgroupOwnerBreLineItem.getObject(data);
                    LOG.debug("Getting stats for user: {}", workflowLineItem.getName());
                    // Now construct query to get claim owner stats
                    sb = new StringBuffer();
                    sb.append("select ");

                    /*
                     * No of Invoices uploaded
                     */
                    sb.append("(select count(*) from claim c, invoice i where c.invoice_id=i.id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId and i.created_date between :pStartDate and :pEndDate ) as no_invoices_uploaded, ");

                    /*
                     * No of invoices Approved by BRE
                     * 
                     */

                    sb.append("(select count(*) from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) as no_invoices_approved_bre, ");

                    /*
                     * No of Invoices approved by BRE and Then Disputed
                     * 
                     */
 

                    sb.append("(select count(*) from (select c.id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' ) as no_invoices_approved_bre_disputed, ");



                    /*
                     *
                     * % of Invoices Approves By Business Rules Engine And  Not Disputed  And Paid Within 15 Days
                     * 
                     */


                    sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a "
                            + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                            + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                            + "and a.update_date between b.created_date  and  b.created_date + interval '15 days' ) as no_invoices_approved_bre_not_disputed_paid_15days, ");



                    /**
                     *
                     * % of Invoices Approved By Business Rules Engine And Not Disputed And Paid Within 30 Days
                     * 
                     */



                    sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a "
                            + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                            + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                            + "and a.update_date between b.created_date  and  b.created_date + interval '30 days' ) as no_invoices_approved_bre_not_disputed_paid_30days, ");




                    /*
                     * % of Invoices Approved  By Business Rules Engine And  Disputed And Paid  within 15 Days
                     * 
                     */


                    sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a "
                            + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                            + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                            + "and a.update_date between b.created_date  and  b.created_date + interval '15 days' ) as no_invoices_approved_bre_disputed_paid_15days, ");



                    /**
                     * % of Invoices Approved  By Business Rules Engine and Disputed And Paid Within30 Days 
                     * 
                     */

                    sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id= :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a "
                            + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                            + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                            + "and a.update_date between b.created_date  and  b.created_date + interval '30 days' ) as no_invoices_approved_bre_disputed_paid_30days, ");



                    /**
                     *
                     * % of Invoices Disputed Due To Hire Charge
                     * 
                     */



                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Hire Charge' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_hire_charge, ");
                            

                    /*
                     * % of Invoices Disputed Due To Hire Duration
                     * 
                     */

                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Hire Duration' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_hire_duration, ");
                   



                    /**
                     * % of Invoices Disputed Due To LIability Dispute
                     *
                     *   no_invoices_disputed_due_to_liability_dispute
                     *   Liability Dispute
                     */

                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Liability Dispute' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_liability_dispute, ");
                   



                    /**
                     * % of Invoices Disputed Due To Like for Like
                     * no_invoices_disputed_due_to_like_for_like
                     * Like for Like
                     *
                     */


                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Like for Like' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_like_for_like, ");
                   

                    /**
                     * % of Invoices Disputed Due To Quantum
                     * no_invoices_disputed_due_to_quantam
                     * Quantum
                     * 
                     */


                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Quantum' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_quantam, ");
                   



                    /**
                     *  % of Invoices Disputed Due To Repair Cost
                     *
                     * 
                     */

                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Repair Cost' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_repair_cost, ");
                   

                    /**
                     * % of Invoices Disputed Due To Invoice Already Paid
                     *
                     * 
                     */


                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Invoice Already Paid' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_invoice_already_paid, ");
                   


                    /**
                     * % of Invoices Disputed Due To Undisclosed
                     * 
                     */


                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Undisclosed' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_undisclosed, ");
                   

                    /**
                     * % of Invoices Disputed Due To  Other
                     * 
                     */

                    sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, audit_trail a where c.invoice_id=i.id and c.id=a.claim_id ");
                    if(isWorkgroupEnabled)
                    sb.append("and c.workgroup_id = :pWorkgroupId ");
                    sb.append("and c.claim_owner_id = :pOwnerId ");
                    sb.append("and c.insurer_id = :pInsurerId "
                            + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                            + "and a.new_status='InvoiceApprovedByBRE' "
                            + "and i.created_date between :pStartDate and :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                            + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' "
                            + "and b.invoice_id = i.id and a.invoice_reason_of_rejection = r.id and r.name = 'Other' "
                            + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_other ");
                   
  
                    queryParameters = new HashMap();
                    if (isWorkgroupEnabled)
                        queryParameters.put("pWorkgroupId", obj.getId());
                    queryParameters.put("pOwnerId", workflowLineItem.getId());
                    queryParameters.put("pInsurerId", insurerId);
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
//                    LOG.debug("Query: {}", sb.toString());
//                    LOG.debug("pWorkgroupId = {}, pOwnerId = {}", obj.getId(), workflowLineItem.getId());
                    List detailData = baseDataService.externalQuery(sb.toString(), queryParameters);
                    // parse query results and add to workflowLineItem
                    if (detailData.size() > 0) {
                        workflowLineItem.updateObject((Map)detailData.get(0));
                        obj.getOwner().add(workflowLineItem);
                    }
                }
            }

            // Now build report parameters
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("serviceDate", serviceCommencingDate);
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("workflowLineItems", workflowReportObjects);
        } catch (Exception ex) {
            LOG.error("Error thrown generating owner-workflow report: {}", ex.getMessage());
//            ex.printStackTrace();
        }

        return reportParameters;
    }

  
    @Override
    public String getReportTemplateFileName() {
        user = ((WebUser) externalParameter.get("CurrentUser"));

         LOG.error("user '{}' ", user);
        if (user.getInsurer().isWorkgroupEnable())
            return "template_WorkgroupOwnerBreInvoiceReport.xls";
        else
            return "template_OwnerBreInvoiceReport.xls";
    }

    @Override
    public InputStream build() {

        LOG.error("build '{}' ");
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT032";
    }


}
