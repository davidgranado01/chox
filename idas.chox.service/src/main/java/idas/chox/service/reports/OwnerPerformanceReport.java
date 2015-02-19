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
import idas.chox.service.reports.viewdata.OwnerPerformanceLineItem;
import idas.chox.service.reports.viewdata.OwnerPerformanceReportObject;

public class OwnerPerformanceReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerPerformanceReport.class);
    private Map externalParameter;
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
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        try {
            boolean isWorkgroupEnabled = true;
            Integer hasStarred = 0;
            Integer insurerId = -1;
            Integer selectedWorkgroupId = -1;
            Integer selectedOwnerId = -1;
            String rptInsurerName = "";
            Date startDate = null;
            Date endDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
                isWorkgroupEnabled = user.getInsurer().isWorkgroupEnable();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);
            if (isWorkgroupEnabled) {
                if (((String[]) externalParameter.get("workgroupId")) != null) {
                    selectedWorkgroupId = TextHelper.getId(((String[]) externalParameter.get("workgroupId"))[0]);
                    LOG.debug("selectedWorkgroupId={}", selectedWorkgroupId);
                }
            }

            if (((String[]) externalParameter.get("ownerId")) != null) {
                selectedOwnerId = TextHelper.getId(((String[]) externalParameter.get("ownerId"))[0]);
                LOG.debug("selectedOwnerId={}", selectedOwnerId);
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

            List<OwnerPerformanceReportObject> performanceReportObjects = new ArrayList<OwnerPerformanceReportObject>();
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
                    OwnerPerformanceReportObject performanceReportObject = new OwnerPerformanceReportObject();
                    performanceReportObject.setWorkgroup(data.get("name").toString());
                    performanceReportObject.setId((Integer) data.get("id"));
                    performanceReportObjects.add(performanceReportObject);
                    LOG.debug("Workgroup added: {}", performanceReportObject.getWorkgroup());
                }
            } else {
                OwnerPerformanceReportObject performanceReportObject = new OwnerPerformanceReportObject();
                performanceReportObjects.add(performanceReportObject);
                LOG.debug("Empty Workgroup added.");
            }

            for (OwnerPerformanceReportObject obj : performanceReportObjects) {
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
                } else if (isWorkgroupEnabled) {
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
                } else {
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
                    } else {
                        first = false;
                    }
                    OwnerPerformanceLineItem performanceLineItem = OwnerPerformanceLineItem.getObject(data);
                    if (performanceLineItem.getWorkgroup().endsWith("(*)") || performanceLineItem.getName().endsWith("(*)")) {
                            hasStarred++;
                    }
                    LOG.debug("Getting stats for user: {}", performanceLineItem.getName());
                    // Now construct query to get claim owner stats
                    sb = new StringBuffer();
                    sb.append("select ");


                    sb.append("(select count(*) from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList()).append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(") as taskProcessedBetweenGivenPeriod, ");



                    sb.append("(select count(*) from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList()).append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append("and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(" and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) <= 2 ");
                    sb.append(") as taskCompleted0_2days, ");



                    sb.append("(select count(*) from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList()).append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(" and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) <= 5 and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) > 2 ");
                    sb.append(") as taskCompleted2_5days, ");



                    sb.append("(select count(*) from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList()).append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(" and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) <= 15 and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) > 5 ");
                    sb.append(") as taskCompleted5_15days, ");


                    sb.append("(select count(*) from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList()).append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(" and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) > 15 ");
                    sb.append(") as taskCompletedAfter15days, ");



                    sb.append("(select cast(avg(total_day) as numeric(6,2)) from (select (EXTRACT(DAY FROM(a1.update_date - i.created_date))- COUNT_FULL_WEEKEND_DAYS(cast(i.created_date as date), cast(a1.update_date as date))) as total_day  from claim c, audit_trail a1, invoice i where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.invoice_id = i.id ");
                    sb.append("and a1.reverted=false and a1.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') ");
                    sb.append("and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id ) ");
                    sb.append("and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived') and c.id = a3.claim_id and a3.update_date > a1. update_date) ");
                    sb.append(" and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(")  a ) as avgInvoicePaymentDay, ");


                    sb.append("(select cast(avg(avg_day) as numeric(6,2)) from (select (EXTRACT(DAY FROM(a1.update_date - a2.update_date))- COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) as avg_day from claim c, audit_trail a1, audit_trail a2 where c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList()).append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" and a1.update_date between :pStartDate and :pEndDate");
                    sb.append(")  a ) as averageDaysToProcess, ");


                    sb.append("(select avg(original_total_to_pay) from (select io.full_total_to_pay as original_total_to_pay ");
                    sb.append("from claim c, audit_trail a1, invoice_original io, invoice i where i.invoice_original_id=io.id and c.invoice_id = i.id and c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id ");
                    sb.append("and a1.reverted=false and a1.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') ");
                    sb.append("and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id ) ");
                    sb.append("and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived','ClaimClosed') and c.id = a3.claim_id and a3.update_date > a1. update_date) ");
                    sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                    sb.append(")  h ) as originalFullTotalToPay, ");


                    sb.append("(select avg(total_to_pay) from (select i.total_to_pay as total_to_pay ");
                    sb.append("from claim c, audit_trail a1, invoice i  where c.invoice_id=i.id and c.claim_owner_id = :pOwnerId ");
                    if (isWorkgroupEnabled) {
                        sb.append("and workgroup_id = :pWorkgroupId ");
                    }
                    sb.append("and c.id = a1.claim_id ");
                    sb.append("and a1.reverted=false and a1.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') ");
                    sb.append("and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id ) ");
                    sb.append("and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived','ClaimClosed') and c.id = a3.claim_id and a3.update_date > a1. update_date) ");
                    sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                    sb.append(")  h ) as fullTotalToPay ");



                    queryParameters = new HashMap();
                    if (isWorkgroupEnabled) {
                        queryParameters.put("pWorkgroupId", obj.getId());
                    }
                    queryParameters.put("pOwnerId", performanceLineItem.getId());
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                    LOG.debug("Query: {}", sb.toString());
                    List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                    LOG.debug("Query 1: {}", sb.toString());
                    if (detailData.size() > 0) {
                        performanceLineItem.updateObject((Map) detailData.get(0));

                        obj.getOwner().add(performanceLineItem);

                    }
                }
            }

            // Now build report parameters
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("startDate", startDate);
            reportParameters.put("hasStarred", hasStarred);
            reportParameters.put("endDate", endDate);
            reportParameters.put("performanceLineItems", performanceReportObjects);
        } catch (Exception ex) {
            LOG.error("Error thrown generating owner-performance report: {}", ex.getMessage());
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
            return "template_WorkgroupOwnerPerformanceReport.xls";
        } else {
            return "template_OwnerPerformanceReport.xls";
        }
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT056";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }

    @Override
    public boolean isBrandingReportFormat() {
        return externalParameter.get("isBrandingReport")==null ? false : (Boolean)externalParameter.get("isBrandingReport");
    }
}
