package idas.chox.service.security;

import idas.chox.core.model.AccessibilityEditable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.AccessibilityHelper;
import java.util.Set;

public class TabAccessibility {

    private short claimDetailTabAccessibility;
    private short invoiceDetailTabAccessibility;
    private short hireMonitoringTabAccessibility;
    private short historyTabAccessibility;
    private short notesTabAccessibility;
    private short paymentPackTabAccessibility;
    private short auditTrailTabAccessibility;
    private Claim claim;
    private Set roles;
    private WebUser user;
    private ApplicationAccessibility applicationAccessibility;

    public TabAccessibility(ApplicationAccessibility applicationAccessibility,WebUser user, Claim claim) {

        this.applicationAccessibility = applicationAccessibility;
        this.user = user;
        this.roles = user.getRoles();
        this.claim = claim;

        claimDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_CLAIM_DETAIL, roles, claim.getStatus());
        hireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HIRE_MONITORING, roles, claim.getStatus());
        historyTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HISTORY, roles, claim.getStatus());
        invoiceDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_INVOICE_DETAIL, roles, claim.getStatus());
        paymentPackTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_PAYMENT_PACK, roles, claim.getStatus());
        notesTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_NOTES, roles, claim.getStatus());
        auditTrailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_AUDIT_TRAIL, roles, claim.getStatus());

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

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    private Short doTabAccessibilityFilter(Short iResult, String tabName) {

        if (iResult >= 2) {

            AccessibilityEditable accEditable = this.applicationAccessibility.checkTabEditableCheck(tabName, this.roles, claim.getStatus());

            if (!AccessibilityHelper.getIsClaimEditable(accEditable, this.claim, user)) {
                iResult = 1;
            }

        }

        return iResult;
    }
}
