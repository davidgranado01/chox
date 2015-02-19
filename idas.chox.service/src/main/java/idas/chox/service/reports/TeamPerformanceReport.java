
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
import idas.chox.service.reports.viewdata.TeamPerformanceLineItem;
import idas.chox.service.reports.viewdata.TeamPerformanceReportObject;


public class TeamPerformanceReport implements Report{


    private static final Logger LOG = LoggerFactory.getLogger(TeamPerformanceReport.class);
    private Map externalParameter;
    private List<String> reportParameterNames;
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
            Integer insurerId = -1;
            String selectedSite = "";
            String selectedTeam = "";
            String rptInsurerName = "";
            Date startDate = null;
            Date endDate = null;
            user = ((WebUser) externalParameter.get("CurrentUser"));
            // GET INSURER INFORMATION
            if (RoleHelper.isInsurerUser(user)) {
                insurerId = user.getInsurer().getId();
                rptInsurerName = user.getInsurer().getName();
            }
            LOG.debug("rptInsurerName={}", rptInsurerName);
            if(((String[]) externalParameter.get("site"))!=null) {
                    selectedSite = ((String[]) externalParameter.get("site"))[0];
                    if (selectedSite.equals("--- ALL ---") || selectedSite.equals("") || selectedSite.equals("-1")) {
                        selectedSite = null;
                    }
            }

            if(((String[]) externalParameter.get("team"))!=null) {
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

            // First, update user service stats for Workgroup
           // baseDataService.query("select update_user_service(" + insurerId + ")");
           // baseDataService.callUpdateWorkgroupService(insurerId);

            List<TeamPerformanceReportObject> teamReportObjects = new ArrayList<TeamPerformanceReportObject>();
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
            List result = reportDataService.getReportData(sb.toString(), queryParameters);
            for (Object o : result) {
                    Map data = (Map) o;
                    TeamPerformanceReportObject teamReportObject = new TeamPerformanceReportObject();
                    teamReportObject.setSite(data.get("site").toString());
                    teamReportObjects.add(teamReportObject);
                    LOG.debug("Site added: {}", teamReportObject.getSite());
            }

            for (TeamPerformanceReportObject obj: teamReportObjects) {
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
                      TeamPerformanceLineItem performanceLineItem = TeamPerformanceLineItem.getObject(data);
                      LOG.debug("Getting stats for site='{}', team='{}'", performanceLineItem.getSite(), performanceLineItem.getTeam());
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
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append(")  a ) as taskProcessedBetweenGivenPeriod, ");



                      sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2, workgroup w where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                              .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append("and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) <= 2 " );
                      sb.append(")  b ) as taskCompleted0_2days, ");


                      sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2, workgroup w where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                              .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append("and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) <= 5 and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) > 2 " );
                      sb.append(")  c ) as taskCompleted2_5days, ");



                      sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2, workgroup w where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                              .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append("and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) <= 15 and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) > 5 " );
                      sb.append(")  d ) as taskCompleted5_15days, ");



                      sb.append("(select count(*) from (select * from claim c, audit_trail a1, audit_trail a2, workgroup w where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                              .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append("and (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) > 15 " );
                      sb.append(")  e ) as taskCompletedAfter15days, ");



                      sb.append("(select cast(avg(total_day) as numeric(6,2)) from (select (EXTRACT(DAY FROM(a1.update_date - i.created_date))- COUNT_FULL_WEEKEND_DAYS(cast(i.created_date as date), cast(a1.update_date as date))) as total_day ");
                      sb.append("from claim c, audit_trail a1, workgroup w, invoice i  where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.invoice_id = i.id ");
                      sb.append("and a1.reverted=false and a1.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') ");
                      sb.append("and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id ) ");
                      sb.append("and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived') and c.id = a3.claim_id and a3.update_date > a1. update_date) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append(")  f ) as avgInvoicePaymentDay, ");


                      sb.append("(select cast(avg(avg_day) as numeric(6,2)) from (select (EXTRACT(DAY FROM(a1.update_date - a2.update_date)) - COUNT_FULL_WEEKEND_DAYS(cast(a2.update_date as date), cast(a1.update_date as date))) as avg_day ");
                      sb.append("from claim c, audit_trail a1, audit_trail a2, workgroup w  where c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id and c.id = a2.claim_id and a2.new_status = a1.original_status and a1.update_date > a2.update_date ");
                      sb.append("and a1.reverted=false and a2.reverted=false and a2.new_status in ").append(getOutstandingStatusList())
                              .append(" and not exists (select * from audit_trail a3 where a3.reverted=false and a3.new_status = a1.original_status and a3.update_date > a2.update_date and a3.update_date < a1.update_date and a3.claim_id=c.id) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append(")  j ) as averageDaysToProcess, ");


                      sb.append("(select avg(original_total_to_pay) from (select io.full_total_to_pay as original_total_to_pay ");
                      sb.append("from claim c, audit_trail a1, workgroup w, invoice_original io, invoice i where i.invoice_original_id=io.id and c.invoice_id = i.id and c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id ");
                      sb.append("and a1.reverted=false and a1.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') ");
                      sb.append("and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id ) ");
                      sb.append("and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived','ClaimClosed') and c.id = a3.claim_id and a3.update_date > a1. update_date) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append(")  h ) as originalFullTotalToPay, ");


                      sb.append("(select avg(total_to_pay) from (select i.total_to_pay as total_to_pay ");
                      sb.append("from claim c, audit_trail a1, workgroup w, invoice i  where c.invoice_id=i.id and c.workgroup_id = w.id and w.status = true ");
                      sb.append("and w.insurer_id = :pInsurerId and w.site=:pSite and w.team=:pTeam ");
                      sb.append("and c.id = a1.claim_id ");
                      sb.append("and a1.reverted=false and a1.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') ");
                      sb.append("and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id ) ");
                      sb.append("and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived','ClaimClosed') and c.id = a3.claim_id and a3.update_date > a1. update_date) ");
                      sb.append("and a1.update_date between :pStartDate and :pEndDate " );
                      sb.append(")  i ) as fullTotalToPay ");



                      queryParameters = new HashMap();
                      queryParameters.put("pInsurerId", insurerId);
                      queryParameters.put("pSite", obj.getSite());
                      queryParameters.put("pTeam", performanceLineItem.getTeam());
                      queryParameters.put("pStartDate", startDate);
                      queryParameters.put("pEndDate", endDate);
                      LOG.debug("Query: {}", sb.toString());
                      List detailData = reportDataService.getReportData(sb.toString(), queryParameters);
                      LOG.debug("Query 1: {}", sb.toString());
                      if (detailData.size() > 0) {
                          LOG.debug("inside creating bean with data");
                          for (int i=0; i<detailData.size(); i++){
                              LOG.debug("result {}",detailData.get(i));
                          }
                          performanceLineItem.updateObject((Map)detailData.get(0));
                          LOG.debug("after setting bean");
                          obj.getTeams().add(performanceLineItem);
                          LOG.debug("after geting teams");
                      }
                    }
                }
            }

            reportParameters.put("insurerName", rptInsurerName);
            reportParameters.put("createdDate", DateHelper.getCurrentDate());
            reportParameters.put("startDate", startDate);
            reportParameters.put("endDate", endDate);
            reportParameters.put("performanceLineItems", teamReportObjects);
        } catch (Exception ex) {
            LOG.error("Error generating Team Performance report: {}", ex.getMessage());
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
        return "template_SiteTeamPerformanceReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = new ExcelReportBuilder();
        return builder.buildReport(this);
    }

    @Override
    public String getReportCode() {
        return "RPT055";
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
