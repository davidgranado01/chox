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

        try {
            PermissionedUser currentUser = ((PermissionedUser) externalParameter.get("CurrentUser"));
            String dataSelected = ((String[]) externalParameter.get("DateSelected"))[0];
            Date selectedDate = DateHelper.LocalDateFormat.parse(dataSelected);

            Calendar c1 = Calendar.getInstance();
            c1.setTime(selectedDate);
            Integer dayOfWeek = c1.get(Calendar.DAY_OF_WEEK);
            c1.add(Calendar.DATE, -dayOfWeek + 1);
            Date sundayOfSelectedDate = c1.getTime();
            c1.add(Calendar.DATE, 6);
            Date saturdayOfSelectedDate = c1.getTime();

            HashMap queryParameters = new HashMap();
            String query = "select :pSelectedDate,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status='ClaimUnacknowledgedUnrouted') as newChoxNotification,"
            + "0 as claimWithdrawn,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from audit_trail where update_date < :pSelectedDate and new_status='ClaimUnacknowledgedUnrouted' ) as existingClaim,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from audit_trail where update_date <= :pSelectedDate+7 and new_status='ClaimUnacknowledgedUnrouted') as cumulativeClaim,"
            + "0 as claimOutOfScope,"
            + "0 as claimInScope,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('ClaimRejected','ClaimRejectionAccepted','ClaimRejectionContested')) as claimNotificationContestedByRsa,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('ClaimRejectionContested','ClaimReferredToFNOL','ClaimReferredToEngineer','ClaimUnacknowledgedUnrouted','ClaimUnacknowledgedRouted')) as claimPendingByRsa,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('AwaitingCarHireInfo')) as claimNotificationAcceptedByRsa,"
            + "0 as inScopeClaimContestedPercentage,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('ClaimReferredToFNOL')) as claimFnolCreatedByRsa,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated')) as claimInvoiced,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect')) as contestedinvoiceByRsa,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceEscalated')) as pendingInvoiceByRsa,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('AwaitingInvoicePayment')) as approvedInvoiceByRsa,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status in ('InvoicePaymentLogged')) as paidInvoiceByRsa,"
            + "0 as paidInvoicePercentage,"
            + "(select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from rpt_week_audit_trail where update_date between :pSelectedDate and :pSelectedDate+7 and new_status='ClaimUnacknowledgedUnrouted' and claim_id not in (select case when count(distinct claim_id) is null then 0 else count(distinct claim_id) end as no_count from audit_trail where new_status in ('InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated') and update_date between :pSelectedDate and :pSelectedDate+7)) as claimTobeInvoiced"; 
            queryParameters.put("pSelectedDate", sundayOfSelectedDate);

            List result = dataService.externalQuery(query,queryParameters);
            List<WeekSummary> weekSummaries = new ArrayList<WeekSummary>();
            for (Object o : result) {
                Map data = (Map) o;
                WeekSummary weekSummary = WeekSummary.getObject(data);
                weekSummaries.add(weekSummary);
            }

            WeekSummaryReportObject reportObject = new WeekSummaryReportObject();

            reportObject.setWeekCycleFrom(DateHelper.LocalDateFormat.format(sundayOfSelectedDate));
            reportObject.setWeekCycleTo(DateHelper.LocalDateFormat.format(saturdayOfSelectedDate));
            reportObject.setCreatedDate(DateHelper.LocalDateFormat.format(new Date()));

            reportParameters.put("weekSummaries", weekSummaries);
            reportParameters.put("reportObj", reportObject);
            Insurer ins = currentUser.getUser().getInsurer();
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
