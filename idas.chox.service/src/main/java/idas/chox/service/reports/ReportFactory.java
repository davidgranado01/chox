package idas.chox.service.reports;

public class ReportFactory {

    public static final String ADMIN_WEEKLY_OVERVIEW_RPT = "AdminWeeklyOverviewReport-Excel";
    public static final String INVOICE_SUMMARY_RPT = "InvoiceSummaryReport-Excel";
    public static final String CLAIM_REJECTED_RPT = "ClaimRejectedReport-Excel";
    public static final String INSURER_PAYMENT_RPT = "PaymentReport-Excel";
    public static final String OVERVIEW_SUMMARY_REPORT = "OverviewSummary-Excel";
    public static final String AVERAGE_SETTLEMENT_REPORT = "AverageSettlementAmount-Excel";
    public static final String INVOICE_SAVING_SUMMARY_REPORT = "InvoiceSavingSummaryReport-Excel";
    public static final String INVOICE_RPT = "InvoiceReport-Excel";
    public static final String BILLING_INSURER_REPORT = "BillingInsurerReport-Excel";
    public static final String BILLING_CHO_REPORT = "BillingChoReport-Excel";
    public static final String CLAIM_FILE_RPT = "ClaimFileReport-Excel";
    public static final String OWNER_WORKFLOW_RPT = "OwnerWorkflowReport-Excel";
    public static final String OWNER_PERFORMANCE_RPT = "OwnerPerformanceReport-Excel";
    public static final String TEAM_WORKFLOW_RPT = "TeamWorkflowReport-Excel";
    public static final String TEAM_PERFORMANCE_RPT = "TeamPerformanceReport-Excel";
    public static final String INSURER_WORKFLOW_RPT = "ClaimStatusWorkflowReport-Excel";
    public static final String BRE_INVOICE_APPROVAL_DISPUTE_RPT = "BreInvoiceApprovalDisputeReport-Excel";
    public static final String INVOICE_STATUS_RPT = "InvoiceStatusReport-Excel";
    public static final String WORKGROUP_OWNER_BRE_RPT = "WorkgroupOwnerBreInvoiceReport-Excel";
    public static final String TEAM_SITE_BRE_INVOICE_RPT = "TeamSiteBreInvoiceReport-Excel";
    public static final String HANDLER_PERFORMANCE_RPT = "HandlerPerformanceReport-Excel";
    public static final String NEW_INCOMING_HANDLER_ACTIONS_RPT = "NewIncomingHandlerActionsReport-Excel";
    
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
        }else if (name.equalsIgnoreCase(HANDLER_PERFORMANCE_RPT)){
            report = new HandlerPerformanceReport();
        } else if (name.equalsIgnoreCase(TEAM_WORKFLOW_RPT)){
            report = new TeamWorkflowReport();
        } else if (name.equalsIgnoreCase(TEAM_PERFORMANCE_RPT)) {
            report = new TeamPerformanceReport();
        } else if (name.equalsIgnoreCase(INSURER_WORKFLOW_RPT)){
            report = new ClaimStatusWorkflowReport();
        } else if (name.equalsIgnoreCase(BRE_INVOICE_APPROVAL_DISPUTE_RPT)){
            report = new BreInvoiceApprovalDisputeReport();
        } else if (name.equalsIgnoreCase(INVOICE_STATUS_RPT)){
            report = new InvoiceStatusReport();
        } else if (name.equalsIgnoreCase(WORKGROUP_OWNER_BRE_RPT)){
            report = new WorkgroupOwnerBreInvoiceReport();
        } else if (name.equalsIgnoreCase(TEAM_SITE_BRE_INVOICE_RPT)){
            report = new TeamSiteBreInvoiceReport();
        } else if (name.equalsIgnoreCase(NEW_INCOMING_HANDLER_ACTIONS_RPT)){
            report = new NewIncomingHandlerActionsReport();
        }

        return report;
    }
}
