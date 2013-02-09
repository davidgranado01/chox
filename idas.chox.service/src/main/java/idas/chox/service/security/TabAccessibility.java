package idas.chox.service.security;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;

public class TabAccessibility {

    private short claimDetailTabAccessibility;
    private short invoiceDetailTabAccessibility;
    private short hireMonitoringTabAccessibility;
    private short historyTabAccessibility;
    private short notesTabAccessibility;
    private short tasksTabAccessibility;
    private short paymentPackTabAccessibility;
    private short auditTrailTabAccessibility;

    public TabAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, Claim claim) {
        claimDetailTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                        ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_CLAIM_DETAIL, claim.getStatus(), claim.getClaimType()),
                        user, claim);
        hireMonitoringTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_HIRE_MONITORING, claim.getStatus(), claim.getClaimType()),
                user, claim);
        historyTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_HISTORY, claim.getStatus(), claim.getClaimType()),
                user, claim);
        if (historyTabAccessibility > 0 && claim.getInvoice() == null ) {
            historyTabAccessibility = 0;
        }
        invoiceDetailTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_INVOICE_DETAIL, claim.getStatus(), claim.getClaimType()),
                user, claim);
        if (invoiceDetailTabAccessibility > 0 && claim.getInvoice() == null ) {
            invoiceDetailTabAccessibility = 0;
        }
        paymentPackTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_PAYMENT_PACK, claim.getStatus(), claim.getClaimType()),
                user, claim);
        notesTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_NOTES, claim.getStatus(), claim.getClaimType()),
                user, claim);
        tasksTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_TASKS, claim.getStatus(), claim.getClaimType()),
                user, claim);
        auditTrailTabAccessibility = applicationAccessibility.checkAccessibilityEditableForClaim(
                ApplicationAccessibility.getTabAccessibilityKey(ApplicationAccessibility.TAB_AUDIT_TRAIL, claim.getStatus(), claim.getClaimType()),
                user, claim);

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

    public short getTasksTabAccessibility() {
        return tasksTabAccessibility;
    }

}
