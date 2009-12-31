/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import java.util.Set;

/**
 *
 * @author Emmanuel
 */
public class ReportAccessibility {

    private short invoiceSummaryAccessibility;
    private short insurerWeeklySummaryAccessibility;
    private short claimRejectionAccessibility;
    private short insurerPaymentReportAccessibility;
    private short overviewSummaryAccessibility;
    private short insurerAverageClaimSettlementReportAccessibility;
    private short invoiceSavingSummaryReportAccessibility;
    private short invoiceReportAccessibility;

    public ReportAccessibility(ApplicationAccessibility applicationAccessibility, Set roles) {

        invoiceSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SUMMARY, roles);
        insurerWeeklySummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INS_WEEKLY_SUMMARY, roles);
        claimRejectionAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_CLAIM_REJECTION, roles);
        insurerPaymentReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INSURER_PAYMENT, roles);
        overviewSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_OVERVIEW_SUMMARY, roles);
        insurerAverageClaimSettlementReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_AVERAGE_SETTLEMENT, roles);
        invoiceSavingSummaryReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SAVING_SUMMARY, roles);
        invoiceReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_REPORT, roles);
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
}
