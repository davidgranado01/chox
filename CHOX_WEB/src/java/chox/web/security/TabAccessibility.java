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
public class TabAccessibility {

    private ApplicationAccessibility accessibility = new ApplicationAccessibility();
    private short claimDetailTabAccessibility =1 ;
    private short invoiceDetailTabAccessibility = 1;
    private short hireMonitoringTabAccessibility = 1;
    private short historyTabAccessibility = 1;
    private short notesTabAccessibility = 1;
    private short paymentPackTabAccessibility = 1;

    public void TabAccessibility(GrantedAuthority[] grantedAuthorities,String claimStatus) {
        
        claimDetailTabAccessibility = getAccessibility().checkTabAccessibility(ApplicationAccessibility.TAB_CLAIM_DETAIL,
                grantedAuthorities, claimStatus);
        hireMonitoringTabAccessibility = getAccessibility().checkTabAccessibility(ApplicationAccessibility.TAB_HIRE_MONITORING,
                grantedAuthorities, claimStatus);
        historyTabAccessibility = getAccessibility().checkTabAccessibility(ApplicationAccessibility.TAB_HISTORY,
                grantedAuthorities, claimStatus);
        invoiceDetailTabAccessibility = getAccessibility().checkTabAccessibility(ApplicationAccessibility.TAB_INVOICE_DETAIL,
                grantedAuthorities, claimStatus);
        paymentPackTabAccessibility = getAccessibility().checkTabAccessibility(ApplicationAccessibility.TAB_PAYMENT_PACK,
                grantedAuthorities, claimStatus);
        notesTabAccessibility = getAccessibility().checkTabAccessibility(ApplicationAccessibility.TAB_NOTES,
                grantedAuthorities, claimStatus);
    }

    public ApplicationAccessibility getAccessibility() {
        return accessibility;
    }

    public short getClaimDetailTabAccessibility() {
        return claimDetailTabAccessibility;
    }

    public short getInvoiceDetailTabAccessibility() {
        return invoiceDetailTabAccessibility;
    }

    public short getHireMonitoringTabAccessibility() {
        return hireMonitoringTabAccessibility;
    }

    public short getHistoryTabAccessibility() {
        return historyTabAccessibility;
    }

    public short getNotesTabAccessibility() {
        return notesTabAccessibility;
    }

    public short getPaymentPackTabAccessibility() {
        return paymentPackTabAccessibility;
    }
}
