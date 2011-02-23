/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.reports;

import idas.chox.core.model.WebUser;
import idas.chox.core.util.DateHelper;
import idas.chox.data.services.BaseDataService;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.util.RoleHelper;
import idas.chox.service.reports.viewdata.TeamSiteBreInvoiceLineItem;
import idas.chox.service.reports.viewdata.TeamSiteBreInvoiceReportObject;
import java.util.ArrayList;

/**
 *
 * @author rajareddydodda
 */
public class TeamSiteBreInvoiceReport implements Report {


    private static final Logger LOG = LoggerFactory.getLogger(TeamSiteBreInvoiceReport.class);
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
        HashMap reportParameters = new HashMap();
        Map paramMap = new HashMap();
        try {
            Integer insurerId = -1;
            String selectedSite = "";
            String selectedTeam = "";
            String rptInsurerName = "";
            Date startDate = null;
            Date endDate = null;
            Date serviceCommencingDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);
            if(((String[]) externalParameter.get("site"))!=null){
                    selectedSite = ((String[]) externalParameter.get("site"))[0];
                    if (selectedSite.equals("--- ALL ---"))
                        selectedSite = null;
            }

            if(((String[]) externalParameter.get("team"))!=null){
                selectedTeam = ((String[]) externalParameter.get("team"))[0];
                    if (selectedTeam.equals("--- ALL ---"))
                        selectedTeam = null;
            }
            LOG.debug("selectedSite={}, selectedTeam={}", selectedSite, selectedTeam);

            if(((String[]) externalParameter.get("startDate"))!=null){
                startDate = DateHelper.Parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if(((String[]) externalParameter.get("endDate"))!=null){
                endDate = DateHelper.Parse(((String[]) externalParameter.get("endDate"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
                LOG.debug("endDate={}", endDate.toString());
            }

           
            // First, update user service stats for Workgroup
//            baseDataService.query("select update_user_service(" + insurerId + ")");
           // baseDataService.callUpdateWorkgroupService(insurerId);

            List<TeamSiteBreInvoiceReportObject> teamReportObjects = new ArrayList<TeamSiteBreInvoiceReportObject>();
            HashMap queryParameters = new HashMap();
            queryParameters.put("pInsurerId", insurerId);
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct site from workgroup where insurer_id = :pInsurerId and status = true ");
            if (selectedSite != null && selectedSite.length() > 0) {
                sb.append("and site = :pSite ");
                queryParameters.put("pSite", selectedSite);
            }
            if (selectedTeam != null && selectedTeam.length() > 0) {
                    queryParameters.put("pTeam", selectedTeam);
                    sb.append("and team = :pTeam ");
            }
            sb.append("order by site");
            List result = baseDataService.externalQuery(sb.toString(), queryParameters);
            for (Object o : result) {
                    Map data = (Map) o;
                    TeamSiteBreInvoiceReportObject teamReportObject = new TeamSiteBreInvoiceReportObject();
                    teamReportObject.setSite(data.get("site").toString());
                    teamReportObjects.add(teamReportObject);
                    LOG.debug("Site added: {}", teamReportObject.getSite());
            }

            for (TeamSiteBreInvoiceReportObject obj: teamReportObjects) {
                LOG.debug("Getting teams of site: {}", obj.getSite());
                queryParameters = new HashMap();
                sb = new StringBuffer();
                queryParameters.put("pSite", obj.getSite());
                queryParameters.put("pInsurerId", insurerId);
                sb.append("select distinct site, team from workgroup where site = :pSite and insurer_id = :pInsurerId and status = true ");
                if (selectedTeam != null && selectedTeam.length() > 0) {
                    queryParameters.put("pTeam", selectedTeam);
                    sb.append("and team = :pTeam ");
                }
                sb.append("order by team");
                result = baseDataService.externalQuery(sb.toString(), queryParameters);
                boolean first = true;
                if (result.isEmpty())
                    teamReportObjects.remove(obj);
                else
                  for (Object o : result) {
                    Map data = (Map) o;
                    if (!first)
                        data.remove("site");
                    else
                        first = false;
                    TeamSiteBreInvoiceLineItem workflowLineItem = TeamSiteBreInvoiceLineItem.getObject(data);
                    LOG.debug("Getting stats for site='{}', team='{}'", workflowLineItem.getSite(), workflowLineItem.getTeam());
                    // Now construct query to get team stats
                    sb = new StringBuffer();
                    sb.append("select ");


                    /*
                     * No of Invoices uploaded
                     */

                    sb.append("(select count(*) from claim c, invoice i, workgroup w "
                            + "where c.invoice_id=i.id "
                            + "and c.workgroup_id=w.id "
                            + "and w.status=true "
                            + "and w.site = :pSite "
                            + "and w.team = :pTeam "
                            + "and w.insurer_id = :pInsurerId "
                            + "and i.created_date between :pStartDate and  :pEndDate ) as no_invoices_uploaded, ");


                    
                    /*
                     * No of invoices Approved by BRE
                     *
                     */


                     sb.append("(select count(*) from claim c, invoice i, workgroup w, audit_trail a "
                             + "where c.invoice_id=i.id "
                             + "and c.workgroup_id=w.id "
                             + "and a.claim_id=c.id "
                             + "and w.status=true "
                             + "and w.site = :pSite "
                             + "and w.team = :pTeam "
                             + "and w.insurer_id = :pInsurerId "
                             + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                             + "and a.new_status='InvoiceApprovedByBRE' "
                             + "and i.created_date between :pStartDate and  :pEndDate ) as no_invoices_approved_bre, ");



                    

                    
                    /*
                     * No of Invoices approved by BRE and Then Disputed
                     *
                     */



                     sb.append("(select count(*) from (select c.id from claim c, invoice i, workgroup w, audit_trail a "
                             + "where c.invoice_id=i.id "
                             + "and c.workgroup_id=w.id "
                             + "and a.claim_id=c.id "
                             + "and w.status=true "
                             + "and w.site = :pSite "
                             + "and w.team = :pTeam "
                             + "and w.insurer_id = :pInsurerId "
                             + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                             + "and a.new_status='InvoiceApprovedByBRE' "
                             + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a "
                             + "where b.id =a.claim_id and a.new_status = 'ContestedInvoiceReferredToCHO' ) as no_invoices_approved_bre_disputed, ");


                   


                    /*
                     *
                     * % of Invoices Approves By Business Rules Engine And  Not Disputed  And Paid Within 15 Days
                     *
                     */


                     sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, workgroup w, audit_trail a "
                             + "where c.invoice_id=i.id "
                             + "and c.workgroup_id=w.id "
                             + "and a.claim_id=c.id "
                             + "and w.status=true "
                             + "and w.site = :pSite "
                             + "and w.team = :pTeam "
                             + "and w.insurer_id = :pInsurerId "
                             + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                             + "and a.new_status='InvoiceApprovedByBRE' "
                             + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a "
                             + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                             + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                             + "and a.update_date between b.created_date  and  b.created_date + interval '15 days' )as no_invoices_approved_bre_not_disputed_paid_15days, ");


                    


                    /**
                     *
                     * % of Invoices Approved By Business Rules Engine And Not Disputed And Paid Within 30 Days
                     *
                     */



                     sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, workgroup w, audit_trail a "
                             + "where c.invoice_id=i.id "
                             + "and c.workgroup_id=w.id "
                             + "and a.claim_id=c.id "
                             + "and w.status=true "
                             + "and w.site = :pSite "
                             + "and w.team = :pTeam "
                             + "and w.insurer_id = :pInsurerId "
                             + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                             + "and a.new_status='InvoiceApprovedByBRE' "
                             + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a "
                             + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                             + "and not exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                             + "and a.update_date between b.created_date  and  b.created_date + interval '30 days' )as no_invoices_approved_bre_not_disputed_paid_30days, ");

                    



                    /*
                     * % of Invoices Approved  By Business Rules Engine And  Disputed And Paid  within 15 Days
                     *
                     */


                     sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, workgroup w, audit_trail a "
                             + "where c.invoice_id=i.id "
                             + "and c.workgroup_id=w.id "
                             + "and a.claim_id=c.id "
                             + "and w.status=true "
                             + "and w.site = :pSite "
                             + "and w.team = :pTeam "
                             + "and w.insurer_id = :pInsurerId "
                             + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                             + "and a.new_status='InvoiceApprovedByBRE' "
                             + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a "
                             + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                             + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                             + "and a.update_date between b.created_date  and  b.created_date + interval '15 days' )as no_invoices_approved_bre_disputed_paid_15days, ");

                    


                    /**
                     * % of Invoices Approved  By Business Rules Engine and Disputed And Paid Within30 Days
                     *
                     */


                     sb.append("(select count(*) from (select c.id, c.created_date from claim c, invoice i, workgroup w, audit_trail a "
                             + "where c.invoice_id=i.id "
                             + "and c.workgroup_id=w.id "
                             + "and a.claim_id=c.id "
                             + "and w.status=true "
                             + "and w.site = :pSite "
                             + "and w.team = :pTeam "
                             + "and w.insurer_id = :pInsurerId "
                             + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                             + "and a.new_status='InvoiceApprovedByBRE' "
                             + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a "
                             + "where b.id =a.claim_id and a.new_status = 'PaymentReceived' "
                             + "and exists ( select * from audit_trail a1 where a1.claim_id =b.id and a1.new_status='ContestedInvoiceReferredToCHO') "
                             + "and a.update_date between b.created_date  and  b.created_date + interval '30 days' )as no_invoices_approved_bre_disputed_paid_30days, ");






                    /**
                     *
                     * % of Invoices Disputed Due To Hire Charge
                     *
                     */


                        sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Hire Charge' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_hire_charge, ");


                   

                    /*
                     * % of Invoices Disputed Due To Hire Duration
                     *
                     */


                        sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Hire Duration' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_hire_duration, ");






                    /**
                     * % of Invoices Disputed Due To LIability Dispute
                     *
                     *   no_invoices_disputed_due_to_liability_dispute
                     *   Liability Dispute
                     */


                        sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Liability Dispute' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_liability_dispute, ");

                    



                    /**
                     * % of Invoices Disputed Due To Like for Like
                     * no_invoices_disputed_due_to_like_for_like
                     * Like for Like
                     *
                     */


                        sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Like for Like' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_like_for_like, ");





                   

                    /**
                     * % of Invoices Disputed Due To Quantum
                     * no_invoices_disputed_due_to_quantam
                     * Quantum
                     *
                     */


                        sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Quantum' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_quantam, ");




                   


                    /**
                     *  % of Invoices Disputed Due To Repair Cost
                     *
                     *
                     */


                         sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Repair Cost' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_repair_cost, ");



                    /**
                     * % of Invoices Disputed Due To Invoice Already Paid
                     *
                     *
                     */



                          sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Invoice Already Paid' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_invoice_already_paid, ");




                    /**
                     * % of Invoices Disputed Due To Undisclosed
                     *
                     */



                            sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Undisclosed' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_undisclosed, ");




                    /**
                     * % of Invoices Disputed Due To  Other
                     *
                     */




                     sb.append("(select count(*) from (select c.id, c.invoice_id from claim c, invoice i, workgroup w, audit_trail a "
                                + "where c.invoice_id=i.id "
                                + "and c.workgroup_id=w.id "
                                + "and a.claim_id=c.id "
                                + "and w.status=true "
                                + "and w.site = :pSite "
                                + "and w.team = :pTeam "
                                + "and w.insurer_id = :pInsurerId "
                                + "and a.original_status in ('AwaitingInvoiceData', 'InvoiceDataCalculationIncorrect') "
                                + "and a.new_status='InvoiceApprovedByBRE' "
                                + "and i.created_date between :pStartDate and  :pEndDate ) b, audit_trail a, invoice i, reason_of_rejection r "
                                + "where b.id =a.claim_id "
                                + "and a.new_status = 'ContestedInvoiceReferredToCHO' "
                                + "and b.invoice_id = i.id "
                                + "and a.invoice_reason_of_rejection = r.id "
                                + "and r.name = 'Other' "
                                + "and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status='ContestedInvoiceReferredToCHO'  and a2.update_date < a.update_date))as no_invoices_disputed_due_to_other ");








                    queryParameters = new HashMap();
                    queryParameters.put("pInsurerId", insurerId);
                    queryParameters.put("pSite", obj.getSite());
                    queryParameters.put("pTeam", workflowLineItem.getTeam());
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                    LOG.debug("Query: {}", sb.toString());
//                    LOG.debug("pWorkgroupId = {}, pOwnerId = {}", obj.getId(), workflowLineItem.getId());
                    List detailData = baseDataService.externalQuery(sb.toString(), queryParameters);
                    // parse query results and add to workflowLineItem
                    if (detailData.size() > 0) {
                        workflowLineItem.updateObject((Map)detailData.get(0));
                        obj.getTeams().add(workflowLineItem);
                    }
                  }
            }

            // Now build report parameters
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("workflowLineItems", teamReportObjects);
        } catch (Exception ex) {
            LOG.error("Error generating Team Workflow report: {}", ex.getMessage());
            if (ex.getCause() != null)
                LOG.error("Caused by: {}", ex.getCause().getMessage());
//            ex.printStackTrace();
        }

        return reportParameters;
    }

    
    @Override
    public String getReportTemplateFileName() {
        return "template_SiteTeamBreInvoiceReport.xls";
    }

    @Override
    public InputStream build() {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT031";
    }

}
