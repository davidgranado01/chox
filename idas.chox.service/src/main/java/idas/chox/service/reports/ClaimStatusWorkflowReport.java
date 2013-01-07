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
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.ClaimStatusWorkflowLineItem;
import idas.chox.service.reports.viewdata.ClaimStatusWorkflowReportObject;

/**
 *
 * @author John
 */
public class ClaimStatusWorkflowReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimStatusWorkflowReport.class);
    private Map externalParameter;
    private BaseDataService baseDataService;
    private WebUser user = new WebUser();
    private ReportDataService reportDataService;
    private boolean workgroupBreakdown;
    
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
    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();
        Map paramMap = new HashMap();
        boolean isWorkgroupEnabled = true;
        
        try {
            boolean isEngineersEnabled = true;
            Integer insurerId = -1;
            String rptInsurerName = "";
            Date startDate = null;
            Date endDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
            LOG.debug("Current user is: {}", user);
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
                isEngineersEnabled = user.getInsurer().isEngineersEnable();
                isWorkgroupEnabled = user.getInsurer().isWorkgroupEnable();
                LOG.debug("insurerId={}", insurerId);
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);


            if (((String[]) externalParameter.get("startDate")) != null) {
                startDate = DateHelper.Parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if (((String[]) externalParameter.get("endDate")) != null) {
                endDate = DateHelper.Parse(((String[]) externalParameter.get("endDate"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
                LOG.debug("endDate={}", endDate.toString());
            }
            
            workgroupBreakdown = isWorkgroupBreakdown();

            if(endDate != null && startDate != null && endDate.before(startDate)){
                throw new Exception("End date (" + endDate.toString() + ") is before start date (" + startDate.toString() +  ") ");
            }
            
            List<String> statuses = ClaimStatus.getInsurerOutstandingStatusList(isEngineersEnabled, user.getInsurer().isWorkgroupEnable(),
                                        user.getInsurer().isClaimOwnershipEnable(), user.getInsurer().isFnolEnable(), user.getInsurer().isThirdPartyInterventionActivated(), user.getInsurer().isUploadEnabled());
            
            List<ClaimStatusWorkflowReportObject> workflowReportObjects = new ArrayList<ClaimStatusWorkflowReportObject>();

            if (isWorkgroupEnabled && workgroupBreakdown) {

                HashMap queryParameters = new HashMap();
                queryParameters.put("pInsurerId", insurerId);
                StringBuilder sb = new StringBuilder();
                sb.append("select id, name from workgroup where insurer_id = :pInsurerId and status = true ");
                sb.append("order by name");

                List result = reportDataService.getReportData(sb.toString(), queryParameters);

                for (String status : statuses) {
                    ClaimStatusWorkflowReportObject object = new ClaimStatusWorkflowReportObject(status);
                    boolean isFirst = true;
                    for (Object o : result) {
                        Map data = (Map) o;
                        ClaimStatusWorkflowLineItem claimStatusWorkflowLineItem = new ClaimStatusWorkflowLineItem();
                        claimStatusWorkflowLineItem.setWorkgroup(data.get("name").toString());
                        claimStatusWorkflowLineItem.setWorkgroupId((Integer) data.get("id"));
                        if (isFirst) {
                            isFirst = false;
                            claimStatusWorkflowLineItem.setStatus(status);
                        } else {
                            claimStatusWorkflowLineItem.setStatus("");
                        }
                        object.getLineItems().add(claimStatusWorkflowLineItem);
                    }
                    workflowReportObjects.add(object);
                }

            } else {
                for (String status : statuses) {
                    ClaimStatusWorkflowReportObject object = new ClaimStatusWorkflowReportObject(status);
                    ClaimStatusWorkflowLineItem claimStatusWorkflowLineItem = new ClaimStatusWorkflowLineItem();
                    claimStatusWorkflowLineItem.setStatus(status);
                    object.getLineItems().add(claimStatusWorkflowLineItem);
                    workflowReportObjects.add(object);
                }
            }

           
            for (ClaimStatusWorkflowReportObject workflowReportObject : workflowReportObjects) {


                for (ClaimStatusWorkflowLineItem claimStatusWorkflowLineItem : workflowReportObject.getLineItems()) {
                    
                    LOG.debug("Getting stats for status: {}", workflowReportObject.getStatus());
                    StringBuilder sb = new StringBuilder();
                    sb.append("select ");

                    sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2 where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a1.reverted=false and a2.reverted=false and a2.new_status = a1.original_status and a1.created_date > a2.created_date ")
                      .append("and a2.new_status = '").append(workflowReportObject.getStatus()).append("' and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.created_date > a2.created_date and a3.created_date < a1.created_date and a3.claim_id=c.id) ")
                      .append("and a1.created_date between :pstartDate and :pendDate")
                      .append(")  a ) as processed, ");


                    sb.append("(select count(*) from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') as outstandingStart, ");


                    sb.append("(select count(*) from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') as outstanding, ");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day < 5) as outstanding0_5,");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day >= 5 and total_day < 10) as outstanding5_10,");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day >= 10 and total_day < 15) as outstanding10_15,");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day >= 15 and total_day < 20) as outstanding15_20,");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.reverted=false and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day >= 20 and total_day < 25) as outstanding20_25,");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day >= 25 and total_day < 30) as outstanding25_30,");


                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b where total_day >= 30) as outstanding30_,");


                    sb.append("(select cast(avg(total_day) as integer) from (select case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') b) as averageOutstanding,");


                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (a1.created_date - a2.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.created_date as date), cast(a1.created_date as date)) as total_day from claim c, audit_trail a1, audit_trail a2 where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and a1.reverted=false and a2.reverted=false and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.created_date > a2.created_date ")
                      .append("and a2.new_status = '").append(workflowReportObject.getStatus()).append("' and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.created_date > a2.created_date and a3.created_date < a1.created_date and a3.claim_id=c.id and a1.created_date <= :pendDate) ")
                      .append(" union all select EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.reverted=false and c.id=a.claim_id and a.id=(select max(id) as max_create_id from audit_trail where claim_id = c.id and reverted=false and created_date <= :pendDate) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("')  a ) as historicAverage, ");


                    sb.append("(select min(a.created_date) from claim c, audit_trail a where c.insurer_id = :pInsurerId and c.id=a.claim_id ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') as oldestDate,");


                    sb.append("(select cast(max(total_day) as integer) from (select a.created_date as modified_date, case when EXTRACT(DAY FROM (:pendDate - a.created_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.created_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.created_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ");
                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        sb.append("and c.workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id=a.claim_id and a.reverted=false and a.id = (select id from audit_trail at where at.claim_id = c.id and at.reverted = false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted = false and a3.created_date <= :pstartDate) order by id desc limit 1) ")
                      .append("and a.new_status = '").append(workflowReportObject.getStatus()).append("') a ) as oldestDays");


                    Map queryParameters = new HashMap();

                    if (isWorkgroupEnabled && workgroupBreakdown) {
                        queryParameters.put("pWorkgroupId", claimStatusWorkflowLineItem.getWorkgroupId());
                    }
                    queryParameters.put("pInsurerId", insurerId);
                    queryParameters.put("pstartDate", startDate);
                    queryParameters.put("pendDate", endDate);
                    List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                    // parse query results and add to workflowLineItem
                    if (detailData.size() > 0) {
                        claimStatusWorkflowLineItem.updateObject((Map) detailData.get(0));
                    }
                }
            }
            // Now build report parameters
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("workflowLineItems", workflowReportObjects);
        } catch (Exception ex) {
            LOG.error("Error thrown generating insurer-setup-workflow report: ", ex);
        }

        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        user = ((WebUser) externalParameter.get("CurrentUser"));

        return (user.getInsurer().isWorkgroupEnable() && isWorkgroupBreakdown()) ? "template_ClaimStatusWorkflowReportByWorkgroupbreakdown.xls"
                : "template_ClaimStatusWorkflowReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT023";
    }

    @Override
    public short[] getColumnsToHide() {
        return null;
    }
    
    private boolean isWorkgroupBreakdown() {
        boolean returnValue = false;
        if (((String[]) externalParameter.get("workgroupBreakdownCheckbox")) != null) {
            returnValue = ((String[]) externalParameter.get("workgroupBreakdownCheckbox"))[0].equalsIgnoreCase("on") ? true : false;
            LOG.info("workgroupBreakdownCheckbox={}", returnValue);
        }
        return returnValue;
    }
}
