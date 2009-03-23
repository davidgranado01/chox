/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.report;

/**
 *
 * @author Emmanuel
 */
public class ReportFactory {

    private static String INS_ADMIN_WEEKLY_OVERVIEW_RPT = "InsurerAdminWeeklyOverviewReport-Excel";
    private static String INVOICE_SUMMARY_RPT = "InvoiceSummaryReport-Excel";
    private static String CLAIM_REJECTED_RPT = "ClaimRejectedReport-Excel";
    private static String INSURER_PAYMENT_RPT = "PaymentReport-Excel";
    
    public static Report getReportByName(String name) {
        Report report = null;
        if (name.equalsIgnoreCase(INS_ADMIN_WEEKLY_OVERVIEW_RPT)) {
            report = new InsurerAdminWeeklyOverviewReport();
        } else if (name.equalsIgnoreCase(INVOICE_SUMMARY_RPT)) {
            report = new InvoiceSummaryReport();
        } else if (name.equalsIgnoreCase(CLAIM_REJECTED_RPT)) {
            report = new ClaimRejectedReport();
        } else if (name.equalsIgnoreCase(INSURER_PAYMENT_RPT)) {
            report = new InsurerPaymentReport();
        }

        return report;
    }
}
