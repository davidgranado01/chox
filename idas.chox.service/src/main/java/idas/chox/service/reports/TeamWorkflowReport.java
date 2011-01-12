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
                LOG.debug("endDate={}", endDate.toString());
            }

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

                    /*
                     * # of processed tasks:
                     *      Counts the number of status changes out of an 'outstanding' status
                     *      for claims in the given workgroup (on site/team) in the period in question
                     */
                    sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a2.new_status in ").append(getOutstandingStatusList())
                            .append(" and not exists (select * from audit_trail a3 where a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append("and a1.update_date between :pStartDate and :pEndDate" );
                    sb.append(")  a ) as processed, ");

                    /*
                     * # of outstanding tasks at period start:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' status at the start of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date < :pStartDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") as outstandingStart, ");

                    /*
                     * # of outstanding tasks at period end:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' status at the end of the period in question
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true  and update_date < :pStartDate ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") as outstanding,");

                    /*
                     * # of outstanding 0-5:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for between 0 and 5 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day <= 5) as outstanding0_5,");

                    /*
                     * # of outstanding 5-10:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for between 5 and 10 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day > 5 and total_day <= 10) as outstanding5_10,");

                    /*
                     * # of outstanding 10-15:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for between 10 and 15 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day > 10 and total_day <= 15) as outstanding10_15,");

                    /*
                     * # of outstanding 15-20:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for between 15 and 20 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day > 15 and total_day <= 20) as outstanding15_20,");

                    /*
                     * # of outstanding 20-25:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for between 20 and 25 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day > 20 and total_day <= 25) as outstanding20_25,");

                    /*
                     * # of outstanding 25-30:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for between 25 and 30 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day > 25 and total_day <= 30) as outstanding25_30,");

                    /*
                     * # of outstanding 30+:
                     *      Counts the number of claims attached to the given workgroup (on site/team)
                     *      that were in an 'outstanding' state at the end of the period in question and
                     *      have been in this state for more than 30 days (i.e. weekends are not counted).
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a.claim_id and a.update_date = (select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day > 30) as outstanding30_,");

                    /*
                     * DaysVolOS (Days Volume Outstanding):
                     *      This first counts the total number of outstanding tasks there were attached to the given workgroup (on site/team) in the 13 weeks (91 days) before the period end.
                     *      This is then divided by 65 (5 working days for each of the 13 weeks) to get the average number of outstanding tasks per day of the previous 13 weeks from the period end.
                     *      The 'outstanding' figure is then divided by this to produce the report 'DaysVolOS' figure.
                     */
                    sb.append("(select case when count(*) is null then 0 else count(*)/65.0 end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) end as total_day from claim c, audit_trail a, workgroup w where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date < :pEndDate "); // Not sure if this is needed
                    sb.append("and c.id = a.claim_id and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day < 91) as daysVolOS,");

                    /*
                     * Oldest Date:
                     *      The date the oldest outstanding claim went to Outstanding for the given workgroup (on site/team) for those claims outstanding at the period end date.
                     */
                    sb.append("(select min(a.update_date) from claim c, audit_trail a, workgroup w where c.insurer_id = :pInsurerId and c.id=a.claim_id ")
                        .append("and c.workgroup_id = w.id and w.status = true and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ")
                        .append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ")
                        .append("and a.new_status in ").append(getOutstandingStatusList()).append(") as oldestDate,");

//                    sb.append("(select min(modified_date) from (select c.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
//                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
//                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day = (select max(total_day) from(select c.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
//                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
//                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") b)) as oldestDate,");

                    sb.append("(select cast((select count(*) from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as decimal) / (select case when count(*)=0 then null else count(*) end from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and outstanding is not null and week_start >= :pCommencingDate)) as timeInService,");

                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam and c.id = a.claim_id ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList() ).append(") a ) as averageOutstanding,");

                    sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date)) as total_day from claim c, audit_trail a1, audit_trail a2, workgroup w  where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                    sb.append("and a2.new_status in ").append(getOutstandingStatusList());
                    sb.append(" and a1.update_date < :pEndDate ");
                    sb.append(" and not exists (select * from audit_trail a3 where a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                    sb.append(" union all select EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and c.id=a.claim_id and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status in ").append(getOutstandingStatusList()).append(")  a ) as historicAverage, ");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'ClaimUnacknowledgedRouted' ) as countClaimUnacknowledgedRouted,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'ClaimRejectionContested' ) as countClaimRejectionContested,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'ClaimUpdatedByEngineer' ) as countClaimUpdatedByEngineer,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'InvoiceReferredToClaimsHandler' ) as countInvoiceReferredToClaimsHandler,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'InvoiceEscalatedToHandler' ) as countInvoiceEscalatedToHandler,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'ContestedInvoiceReferredToInsurer' ) as countContestedInvoiceReferredToInsurer,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'InvoiceApprovedByBRE' ) as countInvoiceApprovedByBre,");

                    sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and a.update_date=(select max(update_date) as max_update_id from audit_trail where claim_id = c.id and update_date <= :pEndDate) ");
                    sb.append("and a.new_status = 'AwaitingInvoicePayment' ) as countAwaitingInvoicePayment,");

                    sb.append("(select count(*) from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                    sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as weeksInService");

                    queryParameters = new HashMap();
                    queryParameters.put("pInsurerId", insurerId);
                    queryParameters.put("pSite", obj.getSite());
                    queryParameters.put("pTeam", workflowLineItem.getTeam());
                    queryParameters.put("pStartDate", startDate);
                    queryParameters.put("pEndDate", endDate);
                    queryParameters.put("pCommencingDate", serviceCommencingDate);
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
            reportParameters.put("serviceDate", serviceCommencingDate);
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
