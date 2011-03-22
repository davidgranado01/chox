package idas.chox.service.reports;

import idas.chox.core.model.ClaimStatus;
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
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.InsurerSetupWorkflowReportObject;

/**
 *
 * @author John
 */
public class InsurerSetupWorkflowReport implements Report {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerSetupWorkflowReport.class);
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
                LOG.debug("insurerId={}", insurerId);
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);


            if (((String[]) externalParameter.get("startDate")) != null) {
                startDate = DateHelper.Parse(((String[]) externalParameter.get("startDate"))[0]);
//                startDate.setHours(0);
//                startDate.setMinutes(0);
//                startDate.setSeconds(0);
                LOG.debug("startDate={}", startDate.toString());
            }

            if (((String[]) externalParameter.get("endDate")) != null) {
                endDate = DateHelper.Parse(((String[]) externalParameter.get("endDate"))[0]);
                endDate = DateHelper.setEndOfDay(endDate);
                LOG.debug("endDate={}", endDate.toString());
            }

            List<InsurerSetupWorkflowReportObject> workflowReportObjects = new ArrayList<InsurerSetupWorkflowReportObject>();

            List<String> statuses = getStatusList(isEngineersEnabled, user.getInsurer().isWorkgroupEnable(),
                                        user.getInsurer().isClaimOwnershipEnable(), user.getInsurer().isFnolEnable(), user.getInsurer().isThirdPartyInterventionActivated());


            for (String status : statuses) {
                InsurerSetupWorkflowReportObject object = new InsurerSetupWorkflowReportObject(status);

                LOG.debug("Getting stats for status: {}", status);
                StringBuilder sb = new StringBuilder();
                sb.append("select ");

                sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2 where c.insurer_id = :pInsurerId ")
                    .append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ")
                    .append("and a2.new_status = '").append(status).append("' and not exists (select * from audit_trail a3 where a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ")
                    .append("and a1.update_date between :pstartDate and :pendDate")
                    .append(")  a ) as processed, ");

                sb.append("(select count(*) from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date < :pstartDate) ")
                    .append("and a.new_status = '").append(status).append("') as outstandingStart, ");

                sb.append("(select count(*) from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate) ")
                    .append("and a.new_status = '").append(status).append("') as outstanding, ");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day < 5) as outstanding0_5,");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day >= 5 and total_day < 10) as outstanding5_10,");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day >= 10 and total_day < 15) as outstanding10_15,");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day >= 15 and total_day < 20) as outstanding15_20,");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day >= 20 and total_day < 25) as outstanding20_25,");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day >= 25 and total_day < 30) as outstanding25_30,");

                sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_date from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b where total_day >= 30) as outstanding30_,");

                sb.append("(select cast(avg(total_day) as integer) from (select case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId ")
                    .append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate)")
                    .append("and a.new_status = '").append(status).append("') b) as averageOutstanding,");

                sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date)) as total_day from claim c, audit_trail a1, audit_trail a2 where c.insurer_id = :pInsurerId ")
                    .append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ")
                    .append("and a2.new_status = '").append(status).append("' and not exists (select * from audit_trail a3 where a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id and a1.update_date <= :pendDate) ")
                    .append(" union all select EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId and c.id=a.claim_id and a.id=(select max(id) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate) ")
                    .append("and a.new_status = '").append(status).append("')  a ) as historicAverage, ");

                sb.append("(select min(a.update_date) from claim c, audit_trail a where c.insurer_id = :pInsurerId and c.id=a.claim_id ")
                    .append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate) ")
                    .append("and a.new_status = '").append(status).append("') as oldestDate,");

                sb.append("(select cast(max(total_day) as integer) from (select a.update_date as modified_date, case when EXTRACT(DAY FROM (:pendDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pendDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pendDate) end as total_day from claim c, audit_trail a where c.insurer_id = :pInsurerId and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pendDate) ")
                    .append("and a.new_status = '").append(status).append("') a ) as oldestDays");


                Map queryParameters = new HashMap();
                queryParameters.put("pInsurerId", insurerId);
                queryParameters.put("pstartDate", startDate);
                queryParameters.put("pendDate", endDate);
                List detailData = baseDataService.externalQuery(sb.toString(), queryParameters);
                // parse query results and add to workflowLineItem
                if (detailData.size() > 0) {
                    object.updateObject((Map) detailData.get(0));
                    workflowReportObjects.add(object);
                }

            }

            // Now build report parameters
            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("workflowLineItems", workflowReportObjects);
        } catch (Exception ex) {
            LOG.error("Error thrown generating insurer-setup-workflow report: {}", ex.getMessage());
//            ex.printStackTrace();
        }

        return reportParameters;
    }

    private List<String> getStatusList(boolean usesEngineers, boolean usesWorkgroups, boolean usesClaimOwnership, boolean usesFnol, boolean usesTPI) {
        List<String> results = new ArrayList<String>();

        if (usesWorkgroups)
            results.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);

        if (usesClaimOwnership)
            results.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);

        results.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);

        if (usesFnol)
            results.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);

        results.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        results.add(ClaimStatus.CLAIM_PENDING);
        if (usesTPI) {
            results.add(ClaimStatus.INVOICE_UNASSIGNED);
        }
        results.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        results.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        results.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        results.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        results.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);

        if (usesEngineers) {
            results.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
            results.add(ClaimStatus.INVOICE_REF_TO_CH);
            results.add(ClaimStatus.INVOICE_ESCALATED);
            results.add(ClaimStatus.CLAIM_REF_TO_ENG);
        }
        return results;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_InsurerSetupWorkflowReport.xls";
    }

    @Override
    public InputStream build() {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT023";
    }
}
