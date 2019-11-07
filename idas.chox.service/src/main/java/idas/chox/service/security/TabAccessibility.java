package idas.chox.service.security;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;

public class TabAccessibility {
    public static final String TAB_CLAIM_DETAIL = "ClaimDetail";
    public static final String TAB_INVOICE_DETAIL = "InvoiceDetail";
    public static final String TAB_HIRE_MONITORING = "HireMonitoring";
    public static final String TAB_INSURER_HIRE_MONITORING = "InsurerHireMonitoring";
    public static final String TAB_PAYMENT_PACK = "PaymentPack";
    public static final String TAB_HISTORY = "History";
    public static final String TAB_NOTES = "Notes";
    public static final String TAB_TASKS = "Tasks";
    public static final String TAB_AUDIT_TRAIL = "AuditTrail";
    public static final String TAB_INVOICE_UNASSIGNED = "InvoiceUnassigned";
    public static final String TAB_EVENT_LOG = "Event Log";

    private final short claimDetailTabAccessibility;
    private final short invoiceDetailTabAccessibility;
    private final short hireMonitoringTabAccessibility;
    private final short insurerHireMonitoringTabAccessibility;
    private final short historyTabAccessibility;
    private final short notesTabAccessibility;
    private final short tasksTabAccessibility;
    private final short paymentPackTabAccessibility;
    private final short auditTrailTabAccessibility;

    public TabAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, Claim claim) {
        claimDetailTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_CLAIM_DETAIL,
                        user, claim);
        hireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_HIRE_MONITORING,
                user, claim);
        insurerHireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_INSURER_HIRE_MONITORING,
                user, claim);
        if (claim.getInvoice() == null) {
            historyTabAccessibility = 0;
        } else {
            historyTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_HISTORY,
                user, claim);            
        }
        if (claim.getInvoice() == null) {
            invoiceDetailTabAccessibility = 0;
        } else {
            invoiceDetailTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_INVOICE_DETAIL,
                user, claim);            
        }
        paymentPackTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_PAYMENT_PACK,
                user, claim);
        notesTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_NOTES,
                user, claim);
        tasksTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_TASKS,
                user, claim);
        auditTrailTabAccessibility = applicationAccessibility.checkTabAccessibilityEditable(TAB_AUDIT_TRAIL,
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

    public short getInsurerHireMonitoringTabAccessibility() {
        return insurerHireMonitoringTabAccessibility;
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
