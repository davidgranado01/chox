/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.security;

import org.acegisecurity.GrantedAuthority;

/**
 *
 * @author Emmanuel
 */
public class ReportAccessibility {
    
     private short invoiceSummaryAccessibility;
     private short insurerWeeklySummaryAccessibility;
     private short claimRejectionAccessibility;
//
    
    public ReportAccessibility(ApplicationAccessibility applicationAccessibility,GrantedAuthority[] grantedAuthorities) {       
               
        invoiceSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SUMMARY,grantedAuthorities);
        insurerWeeklySummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INS_WEEKLY_SUMMARY,grantedAuthorities);     
        claimRejectionAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_CLAIM_REJECTION,grantedAuthorities);     
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



}
