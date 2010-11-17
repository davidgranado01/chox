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
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.TeamWorkflowLineItem;
import idas.chox.service.reports.viewdata.TeamWorkflowReportObject;

/**
 *
 * @author John
 */
public class TeamWorkflowReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(TeamWorkflowReport.class);
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

            if(((String[]) externalParameter.get("serviceCommencingDate"))!=null){
                serviceCommencingDate = DateHelper.Parse(((String[]) externalParameter.get("serviceCommencingDate"))[0]);
                LOG.debug("serviceCommencingDate={}", serviceCommencingDate.toString());
            }

            // First, update user service stats for Workgroup
//            baseDataService.query("select update_user_service(" + insurerId + ")");
            baseDataService.callUpdateWorkgroupService(insurerId);

            List<TeamWorkflowReportObject> teamReportObjects = new ArrayList<TeamWorkflowReportObject>();
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
                    TeamWorkflowReportObject teamReportObject = new TeamWorkflowReportObject();
                    teamReportObject.setSite(data.get("site").toString());
                    teamReportObjects.add(teamReportObject);
                    LOG.debug("Site added: {}", teamReportObject.getSite());
            }
            
            for (TeamWorkflowReportObject obj: teamReportObjects) {
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
                    TeamWorkflowLineItem workflowLineItem = TeamWorkflowLineItem.getObject(data);
                    LOG.debug("Getting stats for site='{}', team='{}'", workflowLineItem.getSite(), workflowLineItem.getTeam());
                    // Now construct query to get team stats
                    sb = new StringBuffer();
                    sb.append("select ");

                    sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a2.new_status in ").append(getOutstandingStatusList())
                            .append(" and not exists (select * from audit_trail a3 where a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(")  a ) as processed, ");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") as outstanding,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day <= 5) as outstanding0_5,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day > 5 and total_day <= 10) as outstanding5_10,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day > 10 and total_day <= 15) as outstanding10_15,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day > 15 and total_day <= 20) as outstanding15_20,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day > 20 and total_day <= 25) as outstanding20_25,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day > 25 and total_day <= 30) as outstanding25_30,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day > 30) as outstanding30_,");

                    sb.append("(select case when count(*) is null then 0 else count(*)/65.0 end as no_count from (select case when EXTRACT(DAY FROM (now() - a.update_date)) is null then 0 else EXTRACT(DAY FROM (now() - a.update_date)) end as total_day from claim c, audit_trail a, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day < 91) as daysColOS,");

                    sb.append("(select min(modified_date) from (select c.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day = (select max(total_day) from(select c.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") b)) as oldestDate,");

                    sb.append("(select cast((select count(*) from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as decimal) / (select case when count(*)=0 then null else count(*) end from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and outstanding is not null and week_start >= :pCommencingDate)) as timeInService,");

                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList() ).append(") a ) as averageOutstanding,");


                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date)) as total_day from claim c, audit_trail a1, audit_trail a2, workgroup w  where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a2.new_status in ").append(getOutstandingStatusList())
                            .append(" and not exists (select * from audit_trail a3 where a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" union all select EXTRACT(DAY FROM (now() - c.status_modified_date)) - COUNT_FULL_WEEKEND_DAYS(cast(c.status_modified_date as date), current_date) as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(")  a ) as historicAverage, ");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'ClaimUnacknowledgedRouted' ) as countClaimUnacknowledgedRouted,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'ClaimRejectionContested' ) as countClaimRejectionContested,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'ClaimUpdatedByEngineer' ) as countClaimUpdatedByEngineer,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'InvoiceReferredToClaimsHandler' ) as countInvoiceReferredToClaimsHandler,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'InvoiceEscalatedToHandler' ) as countInvoiceEscalatedToHandler,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'ContestedInvoiceReferredToInsurer' ) as countContestedInvoiceReferredToInsurer,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.status = 'InvoiceApprovedByBRE' ) as countInvoiceApprovedByBre,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and c.status = 'AwaitingInvoicePayment' ) as countAwaitingInvoicePayment,");

                    sb.append("(select count(*) from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as weeksInService");

                    queryParameters = new HashMap();
                    queryParameters.put("pInsurerId", insurerId);
                    queryParameters.put("pSite", obj.getSite());
                    queryParameters.put("pTeam", workflowLineItem.getTeam());
                    queryParameters.put("pCommencingDate", serviceCommencingDate);
//                    LOG.debug("Query: {}", sb.toString());
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
            reportParameters.put("serviceDate", serviceCommencingDate);
            reportParameters.put("workflowLineItems", teamReportObjects);
        } catch (Exception ex) {
            LOG.error("Error generating Team Workflow report: {}", ex.getMessage());
            if (ex.getCause() != null)
                LOG.error("Caused by: {}", ex.getCause().getMessage());
//            ex.printStackTrace();
        }

        return reportParameters;
    }

    private String getOutstandingStatusList() {
        return"('ClaimUnacknowledgedRouted', 'ClaimRejectionContested', 'ClaimUpdatedByEngineer', 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE', 'AwaitingInvoicePayment')";
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_SiteTeamWorkflowReport.xls";
    }

    @Override
    public InputStream build() {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT022";
    }

}
