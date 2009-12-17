package idas.chox.service.reports;

public class ReportFactory {

    private static String ADMIN_WEEKLY_OVERVIEW_RPT = "AdminWeeklyOverviewReport-Excel";
    private static String INVOICE_SUMMARY_RPT = "InvoiceSummaryReport-Excel";
    private static String CLAIM_REJECTED_RPT = "ClaimRejectedReport-Excel";
    private static String INSURER_PAYMENT_RPT = "PaymentReport-Excel";
    private static String OVERVIEW_SUMMARY_REPORT = "OverviewSummary-Excel";
    private static String AVERAGE_SETTLEMENT_REPORT = "AverageSettlementAmount-Excel";
    private static String INVOICE_SAVING_SUMMARY_REPORT = "InvoiceSavingSummaryReport-Excel";
    private static String INVOICE_RPT = "InvoiceReport-Excel";
    
    public static Report getReportByName(String name) {
        Report report = null;
        if (name.equalsIgnoreCase(ADMIN_WEEKLY_OVERVIEW_RPT)) {
            report = new AdminWeeklyOverviewReport();
        } else if (name.equalsIgnoreCase(INVOICE_SUMMARY_RPT)) {
            report = new InvoiceSummaryReport();
        } else if (name.equalsIgnoreCase(CLAIM_REJECTED_RPT)) {
            report = new ClaimRejectedReport();
        } else if (name.equalsIgnoreCase(INSURER_PAYMENT_RPT)) {
            report = new InsurerPaymentReport();
        } else if (name.equalsIgnoreCase(OVERVIEW_SUMMARY_REPORT)){
            report = new OverviewSummaryReport();
        } else if (name.equalsIgnoreCase(AVERAGE_SETTLEMENT_REPORT)){
            report = new AverageSettlementAmountReport();            
        } else if (name.equalsIgnoreCase(INVOICE_SAVING_SUMMARY_REPORT)){
            report = new InvoiceSavingSummaryReport();
        } else if (name.equalsIgnoreCase(INVOICE_RPT)){
            report = new InvoiceReport();
        }
        return report;
    }
}
