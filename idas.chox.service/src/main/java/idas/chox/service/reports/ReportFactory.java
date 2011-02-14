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
    private static String BILLING_INSURER_REPORT = "BillingInsurerReport-Excel";
    private static String BILLING_CHO_REPORT = "BillingChoReport-Excel";
    private static String CLAIM_FILE_RPT = "ClaimFileReport-Excel";
    private static String OWNER_WORKFLOW_RPT = "OwnerWorkflowReport-Excel";
    private static String OWNER_PERFORMANCE_RPT = "OwnerPerformanceReport-Excel";
    private static String TEAM_WORKFLOW_RPT = "TeamWorkflowReport-Excel";
    private static String TEAM_PERFORMANCE_RPT = "TeamPerformanceReport-Excel";
    private static String INSURER_WORKFLOW_RPT = "InsurerSetupWorkflowReport-Excel";
    private static String BRE_INVOICE_APPROVAL_DISPUTE_RPT = "BreInvoiceApprovalDisputeReport-Excel";
    private static String INVOICE_STATUS_RPT = "InvoiceStatusReport-Excel";

    
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
        } else if ( name.equalsIgnoreCase(BILLING_INSURER_REPORT)){
            report = new BillingInsurerReport();
        } else if ( name.equalsIgnoreCase(BILLING_CHO_REPORT)){
            report = new BillingChoReport();
        } else if (name.equalsIgnoreCase(CLAIM_FILE_RPT)){
            report = new ClaimFileReport();
        } else if (name.equalsIgnoreCase(OWNER_WORKFLOW_RPT)){
            report = new OwnerWorkflowReport();
        }else if (name.equalsIgnoreCase(OWNER_PERFORMANCE_RPT)){
            report = new OwnerPerformanceReport();
        } else if (name.equalsIgnoreCase(TEAM_WORKFLOW_RPT)){
            report = new TeamWorkflowReport();
        } else if (name.equalsIgnoreCase(TEAM_PERFORMANCE_RPT)) {
            report = new TeamPerformanceReport();
        } else if (name.equalsIgnoreCase(INSURER_WORKFLOW_RPT)){
            report = new InsurerSetupWorkflowReport();
        } else if (name.equalsIgnoreCase(BRE_INVOICE_APPROVAL_DISPUTE_RPT)){
            report = new BreInvoiceApprovalDisputeReport();
        }
        else if (name.equalsIgnoreCase(INVOICE_STATUS_RPT)){
            report = new InvoiceStatusReport();
        }

        return report;
    }
}
