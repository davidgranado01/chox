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
    private short auditTrailTabAccessibility;

    public TabAccessibility(ApplicationAccessibility applicationAccessibility,GrantedAuthority[] grantedAuthorities,String claimStatus) {       
               
        claimDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_CLAIM_DETAIL,
                grantedAuthorities, claimStatus);
        hireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HIRE_MONITORING,
                grantedAuthorities, claimStatus);
        historyTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HISTORY,
                grantedAuthorities, claimStatus);
        invoiceDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_INVOICE_DETAIL,
                grantedAuthorities, claimStatus);
        paymentPackTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_PAYMENT_PACK,
                grantedAuthorities, claimStatus);
        notesTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_NOTES,
                grantedAuthorities, claimStatus);
        auditTrailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_AUDIT_TRAIL,
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

    public short getAuditTrailTabAccessibility() {
        return auditTrailTabAccessibility;
    }    
}
