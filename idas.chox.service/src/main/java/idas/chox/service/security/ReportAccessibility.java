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
    private short ownerPerformanceReportAccessibility;
    private short teamWorkflowReportAccessibility;
    private short teamPerformanceReportAccessibility;
    private short claimStatusWorkflowReportAccessibility;
    private short invoiceStatusReportAccessibility;
    private short handlerPerformanceReportAccessibility;
    private short newIncomingHandlerActionsAccessibility;

    //BRE Invoice Approval Dispute Report
    private short breInvoiceApprovalDisputeReportAccessibility;
    
    private short teamSiteBreReportAccessibility;
    private short workgroupOwnerBreReportAccessibility;

    private String getReportAccessibilityKey(String reportName) {
        return String.format("report.%1$s", reportName);
    }


    public ReportAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user) {

        invoiceSummaryAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INVOICE_SUMMARY), user);
        insurerWeeklySummaryAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INS_WEEKLY_SUMMARY), user);
        claimRejectionAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_CLAIM_REJECTION), user);
        insurerPaymentReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INSURER_PAYMENT), user);
        overviewSummaryAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_OVERVIEW_SUMMARY), user);
        insurerAverageClaimSettlementReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_AVERAGE_SETTLEMENT), user);
        invoiceSavingSummaryReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INVOICE_SAVING_SUMMARY), user);
        invoiceReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INVOICE_REPORT), user);
        billingChoReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_BILLING_CHO_REPORT), user);
        billingInsReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_BILLING_INS_REPORT), user);
        claimFileReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_CLAIM_FILE_REPORT), user);
        ownerWorkflowReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_OWNER_WORKFLOW_REPORT), user);
        ownerPerformanceReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_OWNER_PERFORMANCE_REPORT), user);
        teamWorkflowReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_TEAM_WORKFLOW_REPORT), user);
        teamPerformanceReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_TEAM_PERFORMANCE_REPORT), user);
        claimStatusWorkflowReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INSURER_WORKFLOW_REPORT), user);
        breInvoiceApprovalDisputeReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_BRE_INVOICE_APPROVAL_DISPUTE), user);
        invoiceStatusReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_INVOICE_STATUS_REPORT), user);
        handlerPerformanceReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_HANDLER_PERFORMANCE_REPORT), user);
        teamSiteBreReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_TEAM_SITE_BRE_REPORT), user);
        workgroupOwnerBreReportAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_WORKGROUP_OWNER_BRE_REPORT), user);
        newIncomingHandlerActionsAccessibility = applicationAccessibility.checkAccessibilityForUser(getReportAccessibilityKey(ApplicationAccessibility.REPORT_HANDLER_ACTIONS_REPORT), user);
        
    }

    public boolean canAccess(String reportCode) {
        short accessibility = ApplicationAccessibility.DECLINED;

        LOG.debug("Checking accessibbility for report '{}'", reportCode);
        if (reportCode.equals("RPT008")) {
            accessibility = getInvoiceReportAccessibility();
        }
        else if (reportCode.equals("RPT007")) {
            accessibility = getInvoiceSavingSummaryReportAccessibility();
        }
        else if (reportCode.equals("RPT019")) {
            accessibility = getInvoiceSummaryAccessibility();
        }
        else if (reportCode.equals("RPT003")) {
            accessibility = getClaimRejectionAccessibility();
        }
        else if (reportCode.equals("RPT002")) {
            accessibility = getInsurerPaymentReportAccessibility();
        }
        else if (reportCode.equals("RPT001")) {
            accessibility = getOverviewSummaryAccessibility();
        }
        else if (reportCode.equals("RPT006")) {
            accessibility = getInsurerAverageClaimSettlementReportAccessibility();
        }
        else if (reportCode.equals("RPT058")) {
            accessibility = getNewIncomingHandlerActionsAccessibility();
        }
        else if (reportCode.equals("RPT010")) {
            accessibility = getBillingChoReportAccessibility();
        }
        else if (reportCode.equals("RPT009")) {
            accessibility = getBillingInsReportAccessibility();
        }
        else if (reportCode.equals("RPT100")) {
            accessibility = getClaimFileReportAccessibility();
        }
        else if (reportCode.equals("RPT018")) {
            accessibility = getInsurerWeeklySummaryAccessibility();
        }
        else if (reportCode.equals("RPT021")) {
            accessibility = getOwnerWorkflowReportAccessibility();
        }
        else if (reportCode.equals("RPT056")) {
            accessibility = getOwnerPerformanceReportAccessibility();
        }
        else if (reportCode.equals("RPT057")) {
            accessibility = getHandlerPerformanceReportAccessibility();
        }
        else if (reportCode.equals("RPT022")) {
            accessibility = getTeamWorkflowReportAccessibility();
        }
        else if (reportCode.equals("RPT055")) {
            accessibility = getTeamPerformanceReportAccessibility();
        }
        else if (reportCode.equals("RPT023")) {
            accessibility = getClaimStatusWorkflowReportAccessibility();
        }
        else if (reportCode.equals("RPT030")) {
            accessibility = getBreInvoiceApprovalDisputeReportAccessibility();
        }
        else if (reportCode.equals("RPT025")) {
            accessibility = getInvoiceStatusReportAccessibility();
        }
        else if(reportCode.equals("RPT031")) {
            accessibility = getTeamSiteBreReportAccessibility();
        }
        else if(reportCode.equals("RPT032")) {
            accessibility = getWorkgroupOwnerBreReportAccessibility();
        }
        else {
            LOG.error("Accessibility not defined for report '{}'",reportCode);
        }
        LOG.debug("Accessibbility for report '{}' is {}", reportCode, accessibility);
        if (accessibility == ApplicationAccessibility.DECLINED) {
            return false;
        }

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

    public short getOwnerPerformanceReportAccessibility() {
        return ownerPerformanceReportAccessibility;
    }

    public short getHandlerPerformanceReportAccessibility() {
        return handlerPerformanceReportAccessibility;
    }

    public void setHandlerPerformanceReportAccessibility(short handlerPerformanceReportAccessibility) {
        this.handlerPerformanceReportAccessibility = handlerPerformanceReportAccessibility;
    }
    
    public short getTeamWorkflowReportAccessibility() {
        return teamWorkflowReportAccessibility;
    }

    public short getTeamPerformanceReportAccessibility() {
        return teamPerformanceReportAccessibility;
    }

   
    public short getClaimStatusWorkflowReportAccessibility() {
        return claimStatusWorkflowReportAccessibility;
    }

    public short getBreInvoiceApprovalDisputeReportAccessibility() {
        return breInvoiceApprovalDisputeReportAccessibility;
    }
    public short getInvoiceStatusReportAccessibility() {
        return invoiceStatusReportAccessibility;
    }

   
    /**
     * @return the teamSiteBreReportAccessibility
     */
    public short getTeamSiteBreReportAccessibility() {
        return teamSiteBreReportAccessibility;
    }

    /**
     * @return the workgroupOwnerBreReportAccessibility
     */
    public short getWorkgroupOwnerBreReportAccessibility() {
        return workgroupOwnerBreReportAccessibility;
    }

    public short getNewIncomingHandlerActionsAccessibility() {
        return newIncomingHandlerActionsAccessibility;
    }

}
