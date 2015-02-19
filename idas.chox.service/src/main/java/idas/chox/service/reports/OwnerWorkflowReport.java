package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.RoleHelper;
import idas.chox.core.util.TextHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.OwnerWorkflowLineItem;
import idas.chox.service.reports.viewdata.OwnerWorkflowReportObject;

/**
 *
 * @author John
 */
public class OwnerWorkflowReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(OwnerWorkflowReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();
    private ReportDataService reportDataService;
    boolean isInsurerInvoiceUploadEnabled;

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
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        try {
            boolean isWorkgroupEnabled = true;
            Integer hasStarred = 0;
            Integer insurerId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            String rptInsurerName = "";
            Date serviceCommencingDate = null;
            Date startDate = null;
            Date endDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                if (user.getInsurer().isInvoiceUploadEnabled() || user.getInsurer().isClaimUploadEnabled()) {
                    isInsurerInvoiceUploadEnabled = true;
                }
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

            if(((String[]) externalParameter.get("serviceCommencingDate"))!=null){
                serviceCommencingDate = DateHelper.parse(((String[]) externalParameter.get("serviceCommencingDate"))[0]);
                LOG.debug("serviceCommencingDate={}", serviceCommencingDate.toString());
            }

            if(((String[]) externalParameter.get("startDate"))!=null){
                startDate = DateHelper.parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if(((String[]) externalParameter.get("endDate"))!=null){
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

            List<OwnerWorkflowReportObject> workflowReportObjects = new ArrayList<OwnerWorkflowReportObject>();
            if (isWorkgroupEnabled) {
                HashMap queryParameters = new HashMap();
                queryParameters.put("pInsurerId", insurerId);
                StringBuilder sb = new StringBuilder();
                sb.append("select id, name from workgroup where insurer_id = :pInsurerId ");
                if (selectedWorkgroupId != -1) {
                    sb.append("and id = :pWorkgroupId ");
                    queryParameters.put("pWorkgroupId", selectedWorkgroupId);
                }
                if (selectedOwnerId != -1) {
                    sb.append("and exists (select * from web_user_workgroup where workgroup_id = workgroup.id and user_id = :pOwnerId) ");
                    queryParameters.put("pOwnerId", selectedOwnerId);
                }
                if (selectedWorkgroupId == -1 && selectedOwnerId != -1) {
                    // Add workgroups user no longer a member of but worked in during period
                    sb.append("union select distinct w.id, name || ' (*)' as name from workgroup w, claim c ")
                      .append("where c.workgroup_id = w.id ")
                      .append("and c.created_date between :pStartDate and :pEndDate ")
                      .append("and c.claim_owner_id = :pOwnerId and not exists ")
                      .append("(select * from web_user_workgroup where workgroup_id = w.id and user_id = :pOwnerId) ");
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                }
                sb.append("order by name");
                List result = reportDataService.getReportData(sb.toString(), queryParameters);
                for (Object o : result) {
                    Map data = (Map) o;
                    OwnerWorkflowReportObject workflowReportObject = new OwnerWorkflowReportObject();
                    workflowReportObject.setWorkgroup(data.get("name").toString());
                    workflowReportObject.setId((Integer)data.get("id"));
                    workflowReportObjects.add(workflowReportObject);
                    LOG.debug("Workgroup added: {}", workflowReportObject.getWorkgroup());
                }
            }
            else {
                OwnerWorkflowReportObject workflowReportObject = new OwnerWorkflowReportObject();
                workflowReportObjects.add(workflowReportObject);
                    LOG.debug("Empty Workgroup added.");
            }
            
            for (OwnerWorkflowReportObject obj: workflowReportObjects) {
                LOG.debug("Getting members of workgroup: {}", obj.getWorkgroup());
                HashMap queryParameters = new HashMap();
                StringBuffer sb = new StringBuffer();
                if (isWorkgroupEnabled && selectedOwnerId == -1) {
                    // Workgroup enabled, no Claim Owner selected
                    queryParameters.put("pWorkgroupId", obj.getId());
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, ")
                      .append("u.last_name from web_user u, web_user_workgroup wuw, workgroup w, web_user_role wur, ")
                      .append("web_user_user_role wuur where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id ")
                      .append("and w.id = wuw.workgroup_id and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id ")
                      .append("and wur.name='ROLE_INS_CH' and u.status = true ");
                        // Add any ex-members of workgroup that had claims assigned to them during the period
                    sb.append("union ")
                      .append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name || ' (*)' as name, ")
                      .append("u.last_name from web_user u, workgroup w, claim c ")
                      .append("where w.id = :pWorkgroupId and c.workgroup_id = :pWorkgroupId ")
                      .append("and c.created_date between :pStartDate and :pEndDate ")
                      .append("and c.claim_owner_id = u.id and not exists ")
                      .append("(select * from web_user_workgroup where workgroup_id = :pWorkgroupId and user_id = c.claim_owner_id) ");
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                }
                else if (isWorkgroupEnabled) {
                    // Workgroup and Claim Owner selected
                    queryParameters.put("pWorkgroupId", obj.getId());
                    queryParameters.put("pOwnerId", selectedOwnerId);
                    LOG.debug("Added to parameter map: {}={}", "pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id ");
                    sb.append("and u.id = :pOwnerId ");
                    if (selectedWorkgroupId == -1) {
                        // Add workgroups no longer a member of but worked on during period
                        sb.append("union ")
                          .append("select w.name || ' (*)' as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, ")
                          .append("u.last_name from web_user u, workgroup w, claim c ")
                          .append("where w.id = :pWorkgroupId and c.workgroup_id = :pWorkgroupId ")
                          .append("and c.created_date between :pStartDate and :pEndDate ")
                          .append("and c.claim_owner_id = u.id and u.id = :pOwnerId and not exists ")
                          .append("(select * from web_user_workgroup where workgroup_id = :pWorkgroupId and user_id = c.claim_owner_id) ");
                        queryParameters.put("pStartDate", startDate);
                        queryParameters.put("pEndDate", endDate);
                    }
                }
                else { // No workgroups
                    queryParameters.put("pInsurerId", insurerId);
                    LOG.debug("Added to parameter map: {}={}", "pInsurerId", insurerId);
                    sb.append("select u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_role wur, web_user_user_role wuur where u.insurer_id = :pInsurerId and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH'");
                    if (selectedOwnerId != -1) {
                        queryParameters.put("pOwnerId", selectedOwnerId);
                        LOG.debug("Added to parameter map: {}={}", "pOwnerId", selectedOwnerId);
                        sb.append("and u.id = :pOwnerId ");
                    }
                }
                sb.append("order by last_name");
                LOG.debug("Querying for users with: {}", sb.toString());
                List result = reportDataService.getReportData(sb.toString(), queryParameters);
                LOG.debug("Got {} results", result.size());
                boolean first = true;
                for (Object o : result) {
                    Map data = (Map) o;
                    if (!first) {
                        data.remove("workgroup");
                    }
                    else {
                        first = false;
                    }
                    OwnerWorkflowLineItem workflowLineItem = OwnerWorkflowLineItem.getObject(data);
                    LOG.debug("Getting stats for user: {}", workflowLineItem.getName());
                    // Now construct query to get claim owner stats
                    sb = new StringBuffer();
                    sb.append("select ");

                    /*
                     * # of processed tasks:
                     *      Counts the number of status changes out of an 'outstanding' status for claims owned by the user in the
                     *      period under question
                     */
                    sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                            .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append("and a1.update_date between :pStartDate and :pEndDate" );
                    sb.append(")  a ) as processed, ");


                    /*
                     * # of outstanding tasks at period start:
                     *      Counts the number of claims owned by the user that were in an 'outstanding' status at the start
                     *      of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date < :pStartDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") as outstandingStart,");

                    /*
                     * # of outstanding tasks at period end:
                     *      Counts the number of claims owned by the user that were in an 'outstanding' status at the end
                     *      of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") as outstanding,");

                    /*
                     * # of outstanding 0-5:
                     *      Counts the number of claims owned by the user that were in an outstanding state for between 0 and 5 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day < 5) as outstanding0_5,");

                    /*
                     * # of outstanding 5-10:
                     *      Counts the number of claims owned by the user that were in an outstanding state for between 5 and 10 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 5 and total_day < 10) as outstanding5_10,");

                    /*
                     * # of outstanding 10-15:
                     *      Counts the number of claims owned by the user that were in an outstanding state for between 10 and 15 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 10 and total_day < 15) as outstanding10_15,");

                    /*
                     * # of outstanding 15-20:
                     *      Counts the number of claims owned by the user that were in an outstanding state for between 15 and 20 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 15 and total_day < 20) as outstanding15_20,");

                    /*
                     * # of outstanding 20-25:
                     *      Counts the number of claims owned by the user that were in an outstanding state for between 20 and 25 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 20 and total_day < 25) as outstanding20_25,");

                    /*
                     * # of outstanding 25-30:
                     *      Counts the number of claims owned by the user that were in an outstanding state for between 25 and 30 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 25 and total_day < 30) as outstanding25_30,");

                    /*
                     * # of outstanding 30+:
                     *      Counts the number of claims owned by the user that were in an outstanding state for more than 30 days
                     *      at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 30) as outstanding30_,");

                    /*
                     * Average Outstanding:
                     *      the average number of days (not counting weekends) that claims owned by the user were outstanding at the end of the period in question
                     */
                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList() ).append(") a ) as averageOutstanding,");

                    /*
                     * Historic Average:
                     *      the average number of days (excluding weekends) that claims owned by the user have spent in an oustanding state up to
                     *      the period end date
                     */
                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date)) as total_day from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList());
                    sb.append(" and a1.update_date < :pEndDate ");
                    sb.append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" union all select EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) as total_day from claim c, audit_trail a where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(")  a ) as historicAverage, ");

                    /*
                     * DaysVolOS (Days Volume Outstanding):
                     *      This first counts the total number of outstanding tasks there were for the user in the 13 weeks (91 days) before the period end.
                     *      This is then divided by 65 (5 working days for each of the 13 weeks) to get the average number of outstanding tasks per day of the previous 13 weeks from the period end.
                     *      The 'outstanding' figure is then divided by this to produce the report 'DaysVolOS' figure.
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*)/65.0 end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) end as total_day from claim c, audit_trail a where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.reverted=false and a.update_date < :pEndDate ");
                    sb.append("and c.id = a.claim_id and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day < 91) as daysVolOS,");

                    /*
                     * Oldest Date:
                     *      The date the oldest outstanding claim went to Outstanding for the given user for those claims outstanding at the period end date.
                     */
                    sb.append("(select min(a.update_date) from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") as oldestDate,");

                    /*
                     * Oldest Days:
                     *      The number of working days the oldest outstanding claim at the period end date has been outstanding for the given user
                     */
                    sb.append("(select cast(max(total_day) as integer) from (select a.update_date as modified_date, case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a ) as oldestDays, ");

                    /*
                     * Time in Service:
                     *      number of weeks acheived90 / total number of weeks
                     *      where outstanding is not null for the week and week is after the service commencing date
                     */
                    sb.append("(select cast((select count(*) from user_service where user_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as decimal) / (select case when count(*)=0 then null else count(*) end from user_service where user_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and outstanding is not null and week_start >= :pCommencingDate)) as timeInService,");

                    /*
                     * count ClaimUnacknowledgedRouted:
                     *      Counts the number of claims in status 'ClaimUnacknowledgedRouted' at the period end date
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'ClaimUnacknowledgedRouted' ) as countClaimUnacknowledgedRouted,");

                    /*
                     * count ClaimRejectionContested:
                     *     Counts the number of claims in status 'ClaimRejectionContested' at the period end date
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'ClaimRejectionContested' ) as countClaimRejectionContested,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'ClaimUpdatedByEngineer' ) as countClaimUpdatedByEngineer,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'InvoiceReferredToClaimsHandler' ) as countInvoiceReferredToClaimsHandler,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'InvoiceEscalatedToHandler' ) as countInvoiceEscalatedToHandler,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToInsurer' ) as countContestedInvoiceReferredToInsurer,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'InvoiceApprovedByBRE' ) as countInvoiceApprovedByBre,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                    sb.append("and a.reverted=false and a.new_status = 'AwaitingInvoicePayment' ) as countAwaitingInvoicePayment,");

                    if (isInsurerInvoiceUploadEnabled) {
                        
                        /*
                         * count ManualInvoiceBREApproved:
                         *     Counts the number of claims in status 'ManualInvoiceBREApproved' at the period end date
                         */
                        
                        sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                        if (isWorkgroupEnabled) {
                            sb.append("and workgroup_id = :pWorkgroupId ");
                        }
                        sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                        sb.append("and a.reverted=false and a.new_status = 'ManualInvoiceBREApproved' ) as countManualInvoiceBREApproved,");

                        /*
                         * count ManualInvoiceBRERejected:
                         *     Counts the number of claims in status 'ManualInvoiceBRERejected' at the period end date
                         */
                        
                        sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                        if (isWorkgroupEnabled) {
                            sb.append("and workgroup_id = :pWorkgroupId ");
                        }
                        sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                        sb.append("and a.reverted=false and a.new_status = 'ManualInvoiceBRERejected' ) as countManualInvoiceBRERejected,");

                        /*
                         * count ManualInvoiceContested:
                         *     Counts the number of claims in status 'ManualInvoiceContested' at the period end date
                         */
                        
                        sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, audit_trail a where claim_owner_id = :pOwnerId and c.id=a.claim_id ");
                        if (isWorkgroupEnabled) {
                            sb.append("and workgroup_id = :pWorkgroupId ");
                        }
                        sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                        sb.append("and a.reverted=false and a.new_status = 'ManualInvoiceContested' ) as countManualInvoiceContested,");

                    }
                                            
                    /*
                     * Weeks In Service:
                     *      Counts the number of weeks the user 'achieved90' since the service commencing date
                     */
                    sb.append("(select count(*) from user_service where user_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as weeksInService, ");

                    /*
                     * Last Login Date:
                     *      The date the user last logged into CHOX
                     */
                    sb.append("(select last_login_date from web_user where id = :pOwnerId) as lastLoginDate");

                    queryParameters = new HashMap();
                    if (isWorkgroupEnabled) {
                        queryParameters.put("pWorkgroupId", obj.getId());
                    }
                    queryParameters.put("pOwnerId", workflowLineItem.getId());
                    queryParameters.put("pCommencingDate", serviceCommencingDate);
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                    List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                    // parse query results and add to workflowLineItem
                    if (detailData.size() > 0) {
                        workflowLineItem.updateObject((Map)detailData.get(0), isInsurerInvoiceUploadEnabled);
                        obj.getOwner().add(workflowLineItem);
                        if (workflowLineItem.getWorkgroup().endsWith("(*)") || workflowLineItem.getName().endsWith("(*)")) {
                            hasStarred++;
                        }
                    }
                }
            }

            // Now build report parameters
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("serviceDate", serviceCommencingDate);
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("hasStarred", hasStarred);
            reportParameters.put("workflowLineItems", workflowReportObjects);
        } catch (Exception ex) {
            LOG.error("Error thrown generating owner-workflow report: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }

        return reportParameters;
    }

    private String getOutstandingStatusList() {
        return "(" + ClaimStatus.getHandlerOutstandingStatusListAsString() + ")";
    }

    @Override
    public String getReportTemplateFileName() {
        user = ((WebUser) externalParameter.get("CurrentUser"));
        if (user.getInsurer().isWorkgroupEnable()) {
            return "template_WorkgroupOwnerWorkflowReport.xls";
        }
        else {
            return "template_OwnerWorkflowReport.xls";
        }
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT021";
    }
    
    @Override
    public short[] getColumnsToHide() {
        short[] columnsToHide = null;
        if (!isInsurerInvoiceUploadEnabled) {
            if (user.getInsurer().isWorkgroupEnable()) {
                columnsToHide = new short[]{(short) 36, (short) 37, (short) 38};
            } else {
                columnsToHide = new short[]{(short) 35, (short) 36, (short) 37};
            }
        }
        return columnsToHide;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }
}
