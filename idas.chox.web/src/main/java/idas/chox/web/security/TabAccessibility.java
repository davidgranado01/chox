package idas.chox.web.security;

import idas.chox.core.model.AccessibilityEditable;
import idas.chox.core.model.Claim;
import idas.chox.core.util.AccessibilityHelper;
import idas.chox.web.actions.BaseAction;
import org.springframework.security.GrantedAuthority;

public class TabAccessibility extends BaseAction {

    private short claimDetailTabAccessibility;
    private short invoiceDetailTabAccessibility;
    private short hireMonitoringTabAccessibility;
    private short historyTabAccessibility;
    private short notesTabAccessibility;
    private short paymentPackTabAccessibility;
    private short auditTrailTabAccessibility;
    private Claim claim;
    private GrantedAuthority[] grantedAuthorities;
    private ApplicationAccessibility applicationAccessibility;

    public TabAccessibility(ApplicationAccessibility applicationAccessibility, GrantedAuthority[] grantedAuthorities, Claim claim) {

        setApplicationAccessibility(applicationAccessibility);
        setGrantedAuthorities(grantedAuthorities);
        setClaim(claim);

        claimDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_CLAIM_DETAIL, grantedAuthorities, claim.getStatus());
        hireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HIRE_MONITORING, grantedAuthorities, claim.getStatus());
        historyTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HISTORY, grantedAuthorities, claim.getStatus());
        invoiceDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_INVOICE_DETAIL, grantedAuthorities, claim.getStatus());
        paymentPackTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_PAYMENT_PACK, grantedAuthorities, claim.getStatus());
        notesTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_NOTES, grantedAuthorities, claim.getStatus());
        auditTrailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_AUDIT_TRAIL, grantedAuthorities, claim.getStatus());

    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public short getClaimDetailTabAccessibility() {
        return doTabAccessibilityFilter(claimDetailTabAccessibility, ApplicationAccessibility.TAB_CLAIM_DETAIL);
    }

    public short getInvoiceDetailTabAccessibility() {
        return doTabAccessibilityFilter(invoiceDetailTabAccessibility, ApplicationAccessibility.TAB_INVOICE_DETAIL);
    }

    public short getHireMonitoringTabAccessibility() {
        return doTabAccessibilityFilter(hireMonitoringTabAccessibility, ApplicationAccessibility.TAB_HIRE_MONITORING);
    }

    public short getHistoryTabAccessibility() {
        return doTabAccessibilityFilter(historyTabAccessibility, ApplicationAccessibility.TAB_HISTORY);
    }

    public short getNotesTabAccessibility() {
        return doTabAccessibilityFilter(notesTabAccessibility, ApplicationAccessibility.TAB_NOTES);
    }

    public short getPaymentPackTabAccessibility() {
        return doTabAccessibilityFilter(paymentPackTabAccessibility, ApplicationAccessibility.TAB_PAYMENT_PACK);
    }

    public short getAuditTrailTabAccessibility() {
        return doTabAccessibilityFilter(auditTrailTabAccessibility, ApplicationAccessibility.TAB_AUDIT_TRAIL);
    }

    public GrantedAuthority[] getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(GrantedAuthority[] grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    private Short doTabAccessibilityFilter(Short iResult, String tabName) {

        if (iResult >= 2) {

            AccessibilityEditable accEditable = this.applicationAccessibility.checkTabEditableCheck(tabName, this.grantedAuthorities, claim.getStatus());

            if (!AccessibilityHelper.getIsClaimEditable(accEditable, this.claim, getAuthenticatedUser().getUser())) {
                iResult = 1;
            }

        }

        return iResult;
    }
}
