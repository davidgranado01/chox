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
import idas.chox.service.reports.viewdata.TeamWorkflowLineItem;
import idas.chox.service.reports.viewdata.TeamWorkflowReportObject;

/**
 *
 * @author John
 */
public class TeamWorkflowReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(TeamWorkflowReport.class);
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
                if (user.getInsurer().isInvoiceUploadEnabled() || user.getInsurer().isClaimUploadEnabled()) {
                    isInsurerInvoiceUploadEnabled = true;
                }
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);
            if(((String[]) externalParameter.get("site"))!=null){
                    selectedSite = ((String[]) externalParameter.get("site"))[0];
                    if (selectedSite.equals("--- ALL ---") || selectedSite.equals("") || selectedSite.equals("-1")) {
                        selectedSite = null;
                }
            }

            if(((String[]) externalParameter.get("team"))!=null){
                selectedTeam = ((String[]) externalParameter.get("team"))[0];
                    if (selectedTeam.equals("--- ALL ---") || selectedTeam.equals("") || selectedTeam.equals("-1")) {
                        selectedTeam = null;
                }
            }
            LOG.debug("selectedSite={}, selectedTeam={}", selectedSite, selectedTeam);

            if (((String[]) externalParameter.get("startDate"))!=null) {
                startDate = DateHelper.parse(((String[]) externalParameter.get("startDate"))[0]);
                LOG.debug("startDate={}", startDate.toString());
            }

            if (((String[]) externalParameter.get("endDate"))!=null) {
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

            if (((String[]) externalParameter.get("serviceCommencingDate"))!=null) {
                serviceCommencingDate = DateHelper.parse(((String[]) externalParameter.get("serviceCommencingDate"))[0]);
                LOG.debug("serviceCommencingDate={}", serviceCommencingDate.toString());
            }

            List<TeamWorkflowReportObject> teamReportObjects = new ArrayList<TeamWorkflowReportObject>();
            HashMap queryParameters = new HashMap();
            queryParameters.put("pInsurerId", insurerId);
            StringBuffer sb = new StringBuffer();
            sb.append("select distinct site from workgroup where insurer_id = :pInsurerId and status = true ");
            if (selectedSite != null) {
                sb.append("and site = :pSite ");
                queryParameters.put("pSite", selectedSite);
            }
            if (selectedTeam != null) {
                    queryParameters.put("pTeam", selectedTeam);
                    sb.append("and team = :pTeam ");
            }
            sb.append("order by site");
            List result = reportDataService.getReportData(sb.toString(), queryParameters);
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
                result = reportDataService.getReportData(sb.toString(), queryParameters);
                boolean first = true;
                if (result.isEmpty()) {
                    teamReportObjects.remove(obj);
                }
                else {
                    for (Object o : result) {
                      Map data = (Map) o;
                      if (!first) {
                            data.remove("site");
                        }
                      else {
                            first = false;
                        }
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
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                              .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate" );
                      sb.append(")  a ) as processed, ");

                      /*
                       * # of outstanding tasks at period start:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' status at the start of the period in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date < :pStartDate) order by id desc limit 1) ");
                          
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") as outstandingStart, ");

                      /*
                       * # of outstanding tasks at period end:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' status at the end of the period in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") as outstanding,");

                      /*
                       * # of outstanding 0-5:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for between 0 and 5 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day < 5) as outstanding0_5,");

                      /*
                       * # of outstanding 5-10:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for between 5 and 10 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 5 and total_day < 10) as outstanding5_10,");

                      /*
                       * # of outstanding 10-15:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for between 10 and 15 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 10 and total_day < 15) as outstanding10_15,");

                      /*
                       * # of outstanding 15-20:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for between 15 and 20 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 15 and total_day < 20) as outstanding15_20,");

                      /*
                       * # of outstanding 20-25:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for between 20 and 25 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 20 and total_day < 25) as outstanding20_25,");

                      /*
                       * # of outstanding 25-30:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for between 25 and 30 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 25 and total_day < 30) as outstanding25_30,");

                      /*
                       * # of outstanding 30+:
                       *      Counts the number of claims attached to the given workgroup (on site/team)
                       *      that were in an 'outstanding' state at the end of the period in question and
                       *      have been in this state for more than 30 days (i.e. weekends are not counted).
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) end as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day >= 30) as outstanding30_,");

                      /*
                       * DaysVolOS (Days Volume Outstanding):
                       *      This first counts the total number of outstanding tasks there were attached to the given workgroup (on site/team) in the 13 weeks (91 days) before the period end.
                       *      This is then divided by 65 (5 working days for each of the 13 weeks) to get the average number of outstanding tasks per day of the previous 13 weeks from the period end.
                       *      The 'outstanding' figure is then divided by this to produce the report 'DaysVolOS' figure.
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*)/65.0 end as no_count from (select case when EXTRACT(DAY FROM (:pEndDate - a.update_date)) is null then 0 else EXTRACT(DAY FROM (:pEndDate - a.update_date)) end as total_day from claim c, audit_trail a, workgroup w where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.update_date < :pEndDate "); // Not sure if this is needed
                      sb.append("and a.reverted=false and c.id = a.claim_id and a.new_status in ").append(getOutstandingStatusList()).append(") a where total_day < 91) as daysVolOS,");

                      /*
                       * Oldest Date:
                       *      The date the oldest outstanding claim went to Outstanding for the given workgroup (on site/team) for those claims outstanding at the period end date.
                       */
                      sb.append("(select min(a.update_date) from claim c, audit_trail a, workgroup w where c.insurer_id = :pInsurerId and c.id=a.claim_id ")
                          .append("and c.workgroup_id = w.id and w.status = true and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ")
                          .append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ")
                          .append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(") as oldestDate,");

  //                    sb.append("(select min(modified_date) from (select c.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
  //                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
  //                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") a where total_day = (select max(total_day) from(select c.status_modified_date as modified_date, case when EXTRACT(DAY FROM (now() - c.status_modified_date)) is null then 0 else EXTRACT(DAY FROM (now() - c.status_modified_date)) end as total_day from claim c, workgroup w where c.workgroup_id = w.id and w.status = true ");
  //                    sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
  //                    sb.append("and c.status in ").append(getOutstandingStatusList()).append(") b)) as oldestDate,");

                      /*
                       * Time in Service:
                       *      number of weeks acheived90 / total number of weeks
                       *      where outstanding is not null for the week and week is after the service commencing date
                       */
                      sb.append("(select cast((select count(*) from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and achieved90 = true and outstanding is not null and week_start >= :pCommencingDate) as decimal) / (select case when count(*)=0 then null else count(*) end from workgroup_service u, workgroup w where u.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and outstanding is not null and week_start >= :pCommencingDate)) as timeInService,");

                      /*
                       * Average Outstanding:
                       *      the average number of days (not counting weekends) that claims in workgroups of the current site/team were outstanding at the end of the period in question
                       */
                      sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam and c.id = a.claim_id ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList() ).append(") a ) as averageOutstanding,");

                      /*
                       * Historic Average:
                       *      the average number of days (excluding weekends) that claims in workgroups of the current site/team have spent in an oustanding state up to
                       *      the period end date
                       */
                      sb.append("(select cast(avg(total_day) as integer) from (select EXTRACT(DAY FROM (a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date)) as total_day from claim c, audit_trail a1, audit_trail a2, workgroup w  where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList());
                      sb.append(" and a1.update_date < :pEndDate ");
                      sb.append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append(" union all select EXTRACT(DAY FROM (:pEndDate - a.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a.update_date as date), :pEndDate) as total_day from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id=a.claim_id and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status in ").append(getOutstandingStatusList()).append(")  a ) as historicAverage, ");

                      /*
                       * count ClaimUnacknowledgedRouted:
                       *      Counts the number of claims in status 'ClaimUnacknowledgedRouted' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'ClaimUnacknowledgedRouted' ) as countClaimUnacknowledgedRouted,");

                      /*
                       * count ClaimRejectionContested:
                       *      Counts the number of claims in status 'ClaimRejectionContested' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'ClaimRejectionContested' ) as countClaimRejectionContested,");

                      /*
                       * count ClaimUpdatedByEngineer:
                       *      Counts the number of claims in status 'ClaimUpdatedByEngineer' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'ClaimUpdatedByEngineer' ) as countClaimUpdatedByEngineer,");

                      /*
                       * count InvoiceReferredToClaimsHandler:
                       *      Counts the number of claims in status 'InvoiceReferredToClaimsHandler' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'InvoiceReferredToClaimsHandler' ) as countInvoiceReferredToClaimsHandler,");

                      /*
                       * count InvoiceEscalatedToHandler:
                       *      Counts the number of claims in status 'InvoiceEscalatedToHandler' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'InvoiceEscalatedToHandler' ) as countInvoiceEscalatedToHandler,");

                      /*
                       * count ContestedInvoiceReferredToInsurer:
                       *      Counts the number of claims in status 'ContestedInvoiceReferredToInsurer' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'ContestedInvoiceReferredToInsurer' ) as countContestedInvoiceReferredToInsurer,");

                      /*
                       * count InvoiceApprovedByBRE:
                       *      Counts the number of claims in status 'InvoiceApprovedByBRE' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'InvoiceApprovedByBRE' ) as countInvoiceApprovedByBre,");

                      /*
                       * count AwaitingInvoicePayment:
                       *      Counts the number of claims in status 'AwaitingInvoicePayment' at
                       *      the period end date for the workgroup of the site/team in question
                       */
                      sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                      sb.append("and a.reverted=false and a.new_status = 'AwaitingInvoicePayment' ) as countAwaitingInvoicePayment,");
                      
                        if (isInsurerInvoiceUploadEnabled) {
                            /*
                             * count ManualInvoiceBREApproved:
                             *      Counts the number of claims in status 'ManualInvoiceBREApproved' at
                             *      the period end date for the workgroup of the site/team in question
                             */
                            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                            sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                            sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                            sb.append("and a.reverted=false and a.new_status = 'ManualInvoiceBREApproved' ) as countManualInvoiceBREApproved,");

                            /*
                             * count ManualInvoiceBRERejected:
                             *      Counts the number of claims in status 'ManualInvoiceBRERejected' at
                             *      the period end date for the workgroup of the site/team in question
                             */
                            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                            sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                            sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                            sb.append("and a.reverted=false and a.new_status = 'ManualInvoiceBRERejected' ) as countManualInvoiceBRERejected,");

                            /*
                             * count ManualInvoiceContested:
                             *      Counts the number of claims in status 'ManualInvoiceContested' at
                             *      the period end date for the workgroup of the site/team in question
                             */
                            sb.append("(select case when count(*) is null then 0 else count(*) end as no_count from claim c, workgroup w, audit_trail a where c.workgroup_id = w.id and w.status = true and c.id = a.claim_id ");
                            sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                            sb.append("and a.id = (select id from audit_trail at where at.claim_id=c.id and at.reverted=false and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and a3.reverted=false and a3.created_date <= :pEndDate) order by id desc limit 1) ");
                            sb.append("and a.reverted=false and a.new_status = 'ManualInvoiceContested' ) as countManualInvoiceContested,");

                        }
                      
                      /*
                       * Weeks In Service:
                       *      Counts the number of weeks the site/team 'achieved90' since the service commencing date
                       */
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
                      List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                      // parse query results and add to workflowLineItem
                      if (detailData.size() > 0) {
                          workflowLineItem.updateObject((Map)detailData.get(0), isInsurerInvoiceUploadEnabled);
                          obj.getTeams().add(workflowLineItem);
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
            reportParameters.put("workflowLineItems", teamReportObjects);
        } catch (Exception ex) {
            LOG.error("Error generating Team Workflow report: {}", ex.getMessage());
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
        return "template_SiteTeamWorkflowReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT022";
    }
    
    @Override
    public short[] getColumnsToHide() {
        short[] columnsToHide = null;
        if (!isInsurerInvoiceUploadEnabled) {
            columnsToHide = new short[]{(short) 34, (short) 35, (short) 36};
        }
        return columnsToHide;
    }

}
