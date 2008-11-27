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

    private short claimDetailTabAccessibility;
    private short invoiceDetailTabAccessibility;
    private short hireMonitoringTabAccessibility;
    private short historyTabAccessibility;
    private short notesTabAccessibility;
    private short paymentPackTabAccessibility;

    public TabAccessibility(GrantedAuthority[] grantedAuthorities,String claimStatus) {
        
        ApplicationAccessibility accessibility = ApplicationAccessibility.getInstance();
        
        claimDetailTabAccessibility = accessibility.checkTabAccessibility(ApplicationAccessibility.TAB_CLAIM_DETAIL,
                grantedAuthorities, claimStatus);
        hireMonitoringTabAccessibility = accessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HIRE_MONITORING,
                grantedAuthorities, claimStatus);
        historyTabAccessibility = accessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HISTORY,
                grantedAuthorities, claimStatus);
        invoiceDetailTabAccessibility = accessibility.checkTabAccessibility(ApplicationAccessibility.TAB_INVOICE_DETAIL,
                grantedAuthorities, claimStatus);
        paymentPackTabAccessibility = accessibility.checkTabAccessibility(ApplicationAccessibility.TAB_PAYMENT_PACK,
                grantedAuthorities, claimStatus);
        notesTabAccessibility = accessibility.checkTabAccessibility(ApplicationAccessibility.TAB_NOTES,
                grantedAuthorities, claimStatus);
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
