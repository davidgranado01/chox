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
import idas.chox.service.reports.viewdata.OwnerWorkflowLineItem;
import idas.chox.service.reports.viewdata.OwnerWorkflowReportObject;

/**
 *
 * @author John
 */
public class OwnerWorkflowReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(OwnerWorkflowReport.class);
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
            boolean isWorkgroupEnabled = true;
            Integer insurerId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            String rptInsurerName = "";
            Date serviceCommencingDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
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
                }
            }

            if(((String[]) externalParameter.get("ownerId"))!=null){
                selectedOwnerId = TextHelper.getId(((String[]) externalParameter.get("ownerId"))[0]);
            }
            LOG.debug("selectedWorkgroupId={}, selectedOwnerId={}", selectedWorkgroupId, selectedOwnerId);

            if(((String[]) externalParameter.get("serviceCommencingDate"))!=null){
                serviceCommencingDate = DateHelper.Parse(((String[]) externalParameter.get("serviceCommencingDate"))[0]);
                LOG.debug("serviceCommencingDate={}", serviceCommencingDate.toString());
            }

            // First, update user service stats for Insurer
//            baseDataService.query("select update_user_service(" + insurerId + ")");
            baseDataService.callUpdateUserService(insurerId);

            List<OwnerWorkflowReportObject> workflowReportObjects = new ArrayList<OwnerWorkflowReportObject>();
            if (isWorkgroupEnabled) {
                HashMap queryParameters = new HashMap();
                queryParameters.put("pInsurerId", insurerId);
                StringBuffer sb = new StringBuffer();
                sb.append("select id, name from workgroup where insurer_id = :pInsurerId ");
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
                    queryParameters.put("pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w, web_user_role wur, web_user_user_role wuur where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH' ");
                }
                else if (isWorkgroupEnabled) {
                    queryParameters.put("pWorkgroupId", obj.getId());
                    sb.append("select w.name as workgroup, u.id as id, u.first_name || ' ' || u.last_name as name, u.last_name from web_user u, web_user_workgroup wuw, workgroup w where wuw.workgroup_id = :pWorkgroupId and u.id = wuw.user_id and w.id = wuw.workgroup_id ");
                }
                else {
                    queryParameters.put("pInsurerId", insurerId);
                    sb.append("select u.id as id, u.first_name || ' ' || u.last_name as name, '' as workgroup from web_user u, web_user_role wur, web_user_user_role wuur where u.insurer_id = :pInsurerId and wuur.web_user_id = u.id and wuur.web_user_role_id=wur.id and wur.name='ROLE_INS_CH'");
                }
                if (selectedOwnerId != -1) {
                    queryParameters.put("pOwnerId", selectedOwnerId);
                    sb.append("and u.id = :pOwnerId ");
                }
                sb.append("order by u.last_name");
                List result = baseDataService.externalQuery(sb.toString(), queryParameters);
                boolean first = true;
                for (Object o : result) {
                    Map data = (Map) o;
                    if (!first)
                        data.remove("workgroup");
                    else
                        first = false;
                    OwnerWorkflowLineItem workflowLineItem = OwnerWorkflowLineItem.getObject(data);
                    LOG.debug("Getting stats for user: {}", workflowLineItem.getName());
                    // Now construct query to get claim owner stats
                    sb = new StringBuffer();
                    sb.append("select ");
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and status in " + getOutstandingStatusList() + ") as outstanding,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - claim.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - claim.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(claim.status_modified_date as date), current_date) end as total_day from claim where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and status in " + getOutstandingStatusList() + ") a where total_day <= 5) as outstanding0_5,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - claim.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - claim.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(claim.status_modified_date as date), current_date) end as total_day from claim where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and status in " + getOutstandingStatusList() + ") a where total_day > 5 and total_day <= 15) as outstanding5_15,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - claim.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - claim.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(claim.status_modified_date as date), current_date) end as total_day from claim where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and status in " + getOutstandingStatusList() + ") a where total_day > 15) as outstanding15_,");

                    sb.append("(select case when count(*) is null then 0 else count(*)/65.0 end as no_count from (select case when EXTRACT(DAY FROM (now() - a.update_date)) is null then 0 else EXTRACT(DAY FROM (now() - a.update_date)) end as total_day from claim c, audit_trail a where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and c.id = a.claim_id and a.new_status in " + getOutstandingStatusList() + ") a where total_day < 91) as daysColOS,");

                    sb.append("(select min(modified_date) from (select claim.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - claim.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - claim.status_modified_date)) end as total_day from claim where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and status in " + getOutstandingStatusList() + ") a where total_day = (select max(total_day) from(select claim.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - claim.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - claim.status_modified_date)) end as total_day from claim where claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and status in " + getOutstandingStatusList() + ") b)) as oldestDate,");

                    sb.append("(select cast((select count(*) from user_service where user_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as decimal) / (select count(*) from user_service where user_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and outstanding is not null and week_start >= :pCommencingDate)) as timeInService,");

                    sb.append("(select count(*) from user_service where user_id = :pOwnerId ");
                    if (isWorkgroupEnabled)
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as weeksInService");

                    queryParameters = new HashMap();
                    if (isWorkgroupEnabled)
                        queryParameters.put("pWorkgroupId", obj.getId());
                    queryParameters.put("pOwnerId", workflowLineItem.getId());
                    queryParameters.put("pCommencingDate", serviceCommencingDate);
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
            reportParameters.put("workflowLineItems", workflowReportObjects);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return reportParameters;
    }

    private String getOutstandingStatusList() {
        return"('ClaimUnacknowledgedRouted', 'ClaimRejectionContested', 'ClaimUpdatedByEngineer', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'AwaitingInvoicePayment')";
    }

    @Override
    public String getReportTemplateFileName() {
        user = ((WebUser) externalParameter.get("CurrentUser"));
        if (user.getInsurer().isWorkgroupEnable())
            return "template_WorkgroupOwnerWorkflowReport.xls";
        else
            return "template_OwnerWorkflowReport.xls";
    }

    @Override
    public InputStream build() {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT021";
    }

}
