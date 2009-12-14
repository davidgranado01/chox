/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import org.springframework.security.GrantedAuthority;

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

    public ReportAccessibility(ApplicationAccessibility applicationAccessibility, GrantedAuthority[] grantedAuthorities) {

        invoiceSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SUMMARY, grantedAuthorities);
        insurerWeeklySummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INS_WEEKLY_SUMMARY, grantedAuthorities);
        claimRejectionAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_CLAIM_REJECTION, grantedAuthorities);
        insurerPaymentReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INSURER_PAYMENT, grantedAuthorities);
        overviewSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_OVERVIEW_SUMMARY, grantedAuthorities);
        insurerAverageClaimSettlementReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_AVERAGE_SETTLEMENT, grantedAuthorities);
        invoiceSavingSummaryReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SAVING_SUMMARY, grantedAuthorities);
        invoiceReportAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_REPORT, grantedAuthorities);
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
