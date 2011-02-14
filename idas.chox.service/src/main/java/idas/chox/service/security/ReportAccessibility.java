/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;

/**
 *
 * @author Emmanuel
 */
public class ReportAccessibility {
    private static final Logger LOG = LoggerFactory.getLogger(ReportAccessibility.class);

//    private short weeklyOverviewAccessibility;
    private short invoiceSummaryAccessibility;
    private short insurerWeeklySummaryAccessibility;
    private short claimRejectionAccessibility;
    private short insurerPaymentReportAccessibility;
    private short overviewSummaryAccessibility;
    private short insurerAverageClaimSettlementReportAccessibility;
    private short invoiceSavingSummaryReportAccessibility;
    private short invoiceReportAccessibility;
    private short billingChoReportAccessibility;
    private short billingInsReportAccessibility;
    private short claimFileReportAccessibility;
    private short ownerWorkflowReportAccessibility;
    private short teamWorkflowReportAccessibility;
    private short insurerSetupWorkflowReportAccessibility;

    //BRE Invoice Approval Dispute Report

    private short breInvoiceApprovalDisputeReportAccessibility;
    

    public ReportAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user) {

//        weeklyOverviewAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_WEEKLY_OVERVIEW, user);
        invoiceSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SUMMARY, user);
        insurerWeeklySummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INS_WEEKLY_SUMMARY, user);
        claimRejectionAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_CLAIM_REJECTION, user);
        insurerPaymentReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INSURER_PAYMENT, user);
        overviewSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_OVERVIEW_SUMMARY, user);
        insurerAverageClaimSettlementReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_AVERAGE_SETTLEMENT, user);
        invoiceSavingSummaryReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SAVING_SUMMARY, user);
        invoiceReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_REPORT, user);
        billingChoReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_BILLING_CHO_REPORT, user);
        billingInsReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_BILLING_INS_REPORT, user);
        claimFileReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_CLAIM_FILE_REPORT, user);
        ownerWorkflowReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_OWNER_WORKFLOW_REPORT, user);
        teamWorkflowReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_TEAM_WORKFLOW_REPORT, user);
        insurerSetupWorkflowReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INSURER_WORKFLOW_REPORT, user);
        breInvoiceApprovalDisputeReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_BRE_INVOICE_APPROVAL_DISPUTE, user);
    }

    public boolean canAccess(String reportCode) {
        short accessibility = ApplicationAccessibility.Declined;

        LOG.debug("Checking accessibbility for report '{}'", reportCode);
        if (reportCode.equals("RPT008"))
            accessibility = getInvoiceReportAccessibility();
        else if (reportCode.equals("RPT007"))
            accessibility = getInvoiceSavingSummaryReportAccessibility();
        else if (reportCode.equals("RPT019"))
            accessibility = getInvoiceSummaryAccessibility();
        else if (reportCode.equals("RPT003"))
            accessibility = getClaimRejectionAccessibility();
        else if (reportCode.equals("RPT002"))
            accessibility = getInsurerPaymentReportAccessibility();
        else if (reportCode.equals("RPT001"))
            accessibility = getOverviewSummaryAccessibility();
        else if (reportCode.equals("RPT006"))
            accessibility = getInsurerAverageClaimSettlementReportAccessibility();
//        else if (reportName.equals("RPT018"))
//            accessibility = getWeeklyOverviewAccessibility();
        else if (reportCode.equals("RPT010"))
            accessibility = getBillingChoReportAccessibility();
        else if (reportCode.equals("RPT009"))
            accessibility = getBillingInsReportAccessibility();
        else if (reportCode.equals("RPT100"))
            accessibility = getClaimFileReportAccessibility();
        else if (reportCode.equals("RPT018"))
            accessibility = getInsurerWeeklySummaryAccessibility();
        else if (reportCode.equals("RPT021"))
            accessibility = getOwnerWorkflowReportAccessibility();
        else if (reportCode.equals("RPT022"))
            accessibility = getTeamWorkflowReportAccessibility();
        else if (reportCode.equals("RPT023"))
            accessibility = getInsurerSetupWorkflowReportAccessibility();
        else if (reportCode.equals("RPT030"))
            accessibility = getBreInvoiceApprovalDisputeReportAccessibility();
        else {
            LOG.error("Accessibility not defined for report '{}'",reportCode);
        }
        LOG.debug("Accessibbility for report '{}' is {}", reportCode, accessibility);
        if (accessibility == ApplicationAccessibility.Declined)
            return false;

        return true;
    }

    public short getClaimFileReportAccessibility() {
        return claimFileReportAccessibility;
    }

    public short getBillingChoReportAccessibility() {
        return billingChoReportAccessibility;
    }

    public short getBillingInsReportAccessibility() {
        return billingInsReportAccessibility;
    }

    public short getInvoiceReportAccessibility() {
        return invoiceReportAccessibility;
    }

    public short getInvoiceSavingSummaryReportAccessibility() {
        return invoiceSavingSummaryReportAccessibility;
    }

    public short getInvoiceSummaryAccessibility() {
        return invoiceSummaryAccessibility;
    }

//    public short getWeeklyOverviewAccessibility() {
//        return weeklyOverviewAccessibility;
//    }

    public short getInsurerWeeklySummaryAccessibility() {
        return insurerWeeklySummaryAccessibility;
    }

    public short getClaimRejectionAccessibility() {
        return claimRejectionAccessibility;
    }

    public short getInsurerPaymentReportAccessibility() {
        return insurerPaymentReportAccessibility;
    }

    public short getOverviewSummaryAccessibility() {
        return overviewSummaryAccessibility;
    }

    public short getInsurerAverageClaimSettlementReportAccessibility() {
        return insurerAverageClaimSettlementReportAccessibility;
    }

    public short getOwnerWorkflowReportAccessibility() {
        return ownerWorkflowReportAccessibility;
    }

    public short getTeamWorkflowReportAccessibility() {
        return teamWorkflowReportAccessibility;
    }

    public short getInsurerSetupWorkflowReportAccessibility() {
        return insurerSetupWorkflowReportAccessibility;
    }

    public short getBreInvoiceApprovalDisputeReportAccessibility() {
        return breInvoiceApprovalDisputeReportAccessibility;
    }
}
