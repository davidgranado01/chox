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

    
    public ReportAccessibility(ApplicationAccessibility applicationAccessibility,GrantedAuthority[] grantedAuthorities) {       
               
        invoiceSummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INVOICE_SUMMARY,grantedAuthorities);
        insurerWeeklySummaryAccessibility = applicationAccessibility.checkReportAccessibility(ApplicationAccessibility.REPORT_INS_WEEKLY_SUMMARY,grantedAuthorities);     
    }

    public short getInvoiceSummaryAccessibility() {
        return invoiceSummaryAccessibility;
    }

    public short getInsurerWeeklySummaryAccessibility() {
        return insurerWeeklySummaryAccessibility;
    }
   



}
