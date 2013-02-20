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
        claimDetailTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_CLAIM_DETAIL,
                        user, claim);
        hireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_HIRE_MONITORING,
                user, claim);
        historyTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_HISTORY,
                user, claim);
        if (historyTabAccessibility > 0 && claim.getInvoice() == null ) {
            historyTabAccessibility = 0;
        }
        invoiceDetailTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_INVOICE_DETAIL,
                user, claim);
        if (invoiceDetailTabAccessibility > 0 && claim.getInvoice() == null ) {
            invoiceDetailTabAccessibility = 0;
        }
        paymentPackTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_PAYMENT_PACK,
                user, claim);
        notesTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_NOTES,
                user, claim);
        tasksTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_TASKS,
                user, claim);
        auditTrailTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(ApplicationAccessibility.TAB_AUDIT_TRAIL,
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
