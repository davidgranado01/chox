/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

import chox.Util.DateHelper;
import chox.model.Insurer;
import chox.services.DataService;
import chox.web.report.viewdata.WeekSummary;
import chox.web.report.viewdata.WeekSummaryReportObject;
import chox.web.security.PermissionedUser;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Emmanuel
 */
public class InsurerAdminWeeklyOverviewReport implements Report {

    Map externalParameter;
    List<String> reportParameterNames;
    private DataService dataService;

    public InsurerAdminWeeklyOverviewReport() {
        reportParameterNames = new ArrayList<String>();

    }

    public String getReportTemplateFileName() {
        return "template_InsurerAdminWeeklyOverviewReport.xls";
    }

    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    public HashMap getReportParameters() {
        HashMap reportParameters = new HashMap();
        //monday as start of week
        //sunday as end of week
        try {
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            Insurer ins = currentUser.getUser().getInsurer();
            String dataStartRaw = ((String[]) externalParameter.get("DateStart"))[0];
            String dateEndRaw = ((String[]) externalParameter.get("DateEnd"))[0];
            Date startDate = DateHelper.LocalDateFormat.parse(dataStartRaw);//user selected start date of report
            Date endDate = DateHelper.LocalDateFormat.parse(dateEndRaw);//user selected end date of report
            
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);
            Integer dayOfWeek1 = c1.get(Calendar.DAY_OF_WEEK);
            c1.add(Calendar.DATE, -dayOfWeek1 + 2);
            Date dateFirstMonday = c1.getTime();//actual start date of report
            
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);
            Integer dayOfWeek2 = c2.get(Calendar.DAY_OF_WEEK);
            c2.add(Calendar.DATE, -dayOfWeek2 + 2);
            Date dateLastMonday = c2.getTime();
            c2.add(Calendar.DATE, 6);
            Date dateLastSunday = c2.getTime();//actual end date of report
            
            Date currentMonday = dateFirstMonday;
            List<WeekSummary> weekSummaries = new ArrayList<WeekSummary>();
            do {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonday);
                Date startOfTheWeek = c.getTime();
                c.add(Calendar.DATE, 6);
                Date endOfTheWeek = c.getTime();

                HashMap queryParameters = new HashMap();

                String query = "select 1, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where insurer_id = insurer.id and update_date between :pSelectedStartDate and :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted') as newChoxNotification, "
                + "0 as claimWithdrawn, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where insurer_id = insurer.id and update_date < :pSelectedStartDate and new_status='ClaimUnacknowledgedUnrouted') as existingClaim, "
                + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_claim_audit_trail where insurer_id = insurer.id and update_date <= :pSelectedEndDate and new_status='ClaimUnacknowledgedUnrouted') as cumulativeClaim, "
                + "0 as claimOutOfScope, "
                + "0 as claimInScope, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimRejected','ClaimRejectionAccepted','ClaimRejectionContested')) as claimNotificationContestedByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimRejectionContested','ClaimReferredToFNOL','ClaimReferredToEngineer','ClaimUnacknowledgedUnrouted','ClaimUnacknowledgedRouted', 'ClaimPending')) as claimPendingByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingCarHireInfo')) as claimNotificationAcceptedByRsa, "
                + "0 as inScopeClaimContestedPercentage, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ClaimReferredToFNOL')) as claimFnolCreatedByRsa, "
                //+ "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated')) as claimInvoiced, "
                + "((select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_invoice where insurer_id = insurer.id and created_date between :pSelectedStartDate and :pSelectedEndDate)) as claimInvoiced, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect','InvoiceRejectionAccepted')) as contestedinvoiceByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceEscalated')) as pendingInvoiceByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingInvoicePayment')) as approvedInvoiceByRsa, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('InvoicePaymentLogged')) as paidInvoiceByRsa, "
                + "0 as paidInvoicePercentage, "
                + "(select case when count(distinct id) is null then 0 else count(distinct id) end as no_count from rpt_claim_audit_trail a, (select claim_id, max(update_date) as max_update_date from rpt_claim_audit_trail where update_date between :pSelectedStartDate and :pSelectedEndDate group by claim_id) b where a.insurer_id=insurer.id AND a.update_date between :pSelectedStartDate and :pSelectedEndDate AND a.claim_id=b.claim_id AND a.update_date=b.max_update_date AND a.new_status in ('AwaitingCarHireInfo','AwaitingInvoiceData')) as claimTobeInvoiced "
                + "from insurer insurer where insurer.id = :pInsId";
                
                queryParameters.put("pSelectedStartDate", startOfTheWeek);
                queryParameters.put("pSelectedEndDate", endOfTheWeek);
                queryParameters.put("pInsId", ins.getId());

                List result = dataService.externalQuery(query, queryParameters);
                
                for (Object o : result) {
                    Map data = (Map) o;
                    data.put("weekCycleDate", DateHelper.LocalDateFormat.format(startOfTheWeek));
                    WeekSummary weekSummary = WeekSummary.getObject(data);
                    weekSummaries.add(weekSummary);
                }
                
                c.add(Calendar.DATE, 1);
                currentMonday = c.getTime();

            }while(currentMonday.before(dateLastSunday));

            WeekSummaryReportObject reportObject = new WeekSummaryReportObject();

            reportObject.setWeekCycleFrom(dateFirstMonday);
            reportObject.setWeekCycleTo(dateLastSunday);
            reportObject.setCreatedDate(new Date());

            reportParameters.put("weekSummaries", weekSummaries);
            reportParameters.put("reportObj", reportObject);            
            reportParameters.put("insurerObj", ins);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return reportParameters;
    }

    public InputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}
