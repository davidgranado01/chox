package idas.chox.service.security;

import idas.chox.core.model.AccessibilityEditable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.AccessibilityService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ApplicationAccessibility {

    //Access Right
    public static final Short Declined = 0;
    public static final Short ReadOnly = 1;
    public static final Short Editable = 2;
    // <editor-fold defaultstate="collapsed" desc="TAB">
    // ***************************************
    // TAB
    // ***************************************
    public static final String TAB_CLAIM_DETAIL = "ClaimDetail";
    public static final String TAB_INVOICE_DETAIL = "InvoiceDetail";
    public static final String TAB_HIRE_MONITORING = "HireMonitoring";
    public static final String TAB_PAYMENT_PACK = "PaymentPack";
    public static final String TAB_HISTORY = "History";
    public static final String TAB_NOTES = "Notes";
    public static final String TAB_AUDIT_TRAIL = "AuditTrail";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="NOTIFICATION">
    // ***************************************
    // NOTIFICATION
    // ***************************************
    public static final String NOTE_CLAIM_NUMBER = "ClaimNumberNotification";
    public static final String NOTE_CLAIM_VIEWING = "UserViewingNotification";
    public static final String NOTE_CLAIM_INTELLIGENT_NOTE = "IntelligentNotesNotification";
    public static final String NOTE_CLAIM_NOTES = "NotificationNotesNotification";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FILTER">
    // ***************************************
    // FILTER
    // ***************************************
    public static final String FILTER_REJECTED_CLAIMS = "RejectedClaims";
    public static final String FILTER_INCORRECT_INVOICE_DATA_COLC = "IncorrectInvoiceDataCalculations";
    public static final String FILTER_CONTESTED_INVOICE_REF_CHO = "ContestedInvoicesReferredToCHO";
    public static final String FILTER_CLAIM_AWAITING_HIRE_MONITORING_INFO = "ClaimsAwaitingHireMonitoringInformation";
    public static final String FILTER_CLAIM_AWAITING_ACK = "ClaimsAwaitingAcknowledgement";
    public static final String FILTER_RESUBMIT_CLAIM_AWAITING_ACK = "ReSubmittedClaimsAwaitingAcknowledgement";
    public static final String FILTER_HIRE_UPDATE_ANOMALIES = "HireUpdateAnomalies";
    public static final String FILTER_NEW_CLAIM_TO_BE_ROUTED = "NewClaimsToBerouted";
    public static final String FILTER_CLAIM_AWAITING_CLAIM_HANDLING_PAYMENT = "ClaimsAwaitingClaimsHandlingPayment";
    public static final String FILTER_APPROVED_INVOICE_AWAITING_PAYMENT = "ApprovedInvoicesAwaitingPayment";
    public static final String FILTER_ESCALATED_INVOICE = "EscalatedInvoices";
    public static final String FILTER_ESCALATED_INVOICE_TO_CH = "InvoiceEscalatedToHandler";
    public static final String FILTER_CONTESTED_INVOICE_REF_INS = "ContestedInvoicesReferredToInsurer";
    public static final String FILTER_INVOICE_APPROVED_BY_BRE = "InvoicesApprovedByBRE";
    public static final String FILTER_CLAIM_REF_ENG = "ClaimReferredToEngineer";
    public static final String FILTER_CLAIM_REF_FNOL = "ClaimReferredToFNOL";
    public static final String FILTER_PENALTY_CHARGES_APPLIED = "PenaltyChargesApplied";
    public static final String FILTER_CLAIM_PENDING = "ClaimPending";
    public static final String FILTER_INVOICE_REF_TO_CH = "InvoiceReferredToClaimsHandler";
    public static final String FILTER_INVOICE_PAYMENT_LOGGED = "InvoicePaymentLogged";
    public static final String FILTER_CLAIM_UPDATED_BY_ENGINEER = "ClaimUpdatedByEngineer";
    public static final String FILTER_INVOICE_REF_TO_ENG = "InvoicesReferredToEngineer";
    public static final String FILTER_CLAIM_OWNERSHIP = "ClaimUnacknowledgedUnassigned";
    public static final String FILTER_AWAITING_INVOICE_DATA = "AwaitingInvoiceData";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="PANEL">
    // ***************************************
    // PANEL
    // ***************************************
    public static final String PANEL_FNOL_REVIEWED = "FNOLReviewed";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="MENU">
    // ***************************************
    // MENU
    // ***************************************
    public static final String MENU_DASHBOARD = "Dashboard";
    public static final String MENU_REPORT = "Report";
    public static final String MENU_ADMIN = "Admin";
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="REPORT">
    // ***************************************
    // REPORT
    // ***************************************
    public static final String REPORT_INVOICE_SUMMARY = "InvoiceSummary";
    public static final String REPORT_INS_WEEKLY_SUMMARY = "InsurerWeeklySummary";
    public static final String REPORT_CLAIM_REJECTION = "ClaimRejection";
    public static final String REPORT_INSURER_PAYMENT = "InsurerPayment";
    public static final String REPORT_OVERVIEW_SUMMARY = "OverviewSummary";
    public static final String REPORT_AVERAGE_SETTLEMENT = "AverageSettlementAmountReport";
    public static final String REPORT_INVOICE_SAVING_SUMMARY = "InvoiceSavingSummaryReport";
    public static final String REPORT_INVOICE_REPORT = "InvoiceReport";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ADMIN">
    // ***************************************
    // ADMIN
    // ***************************************
    public static final String ADMIN_INSURER_COMPANIES = "InsurerCompanies";
    public static final String ADMIN_CREDIT_HIRE_ORG = "CreditHireOrg";
    public static final String ADMIN_USER_MANAGEMENT = "UserManagement";
    public static final String ADMIN_INSURER_BRE_MANAGEMENT = "InsurerBreManagement";
    // </editor-fold>
    private HashMap accessibilityMap;
    private AccessibilityService accessibilityService;

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY OBJECT">
    public TabAccessibility getTabAccessibility(WebUser user, Claim claim) {
        return new TabAccessibility(this, user, claim);
    }

    public NotificationAccessibility getNotificationAccessibility(Set roles, String claimStatus) {
        return new NotificationAccessibility(this, roles, claimStatus);
    }

    public PanelAccessibility getPanelAccessibility(Set roles) {
        return new PanelAccessibility(this, roles);
    }   

    public MenuAccessibility getMenuAccessibility(Set roles) {
        return new MenuAccessibility(this, roles);
    }

    public ReportAccessibility getReportAccessibility(Set roles) {
        return new ReportAccessibility(this, roles);
    }

    public AdminAccessibility getAdminAccessibility(Set roles) {
        return new AdminAccessibility(this, roles);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY CHECK">
    public Short checkTabAccessibility(String tabName, Set roles, String claimStatus) {

        String accessibilityKey = getTabAccessibilityKey(tabName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkNotificationAccessibility(String notificationName, Set roles, String claimStatus) {

        String accessibilityKey = getNotificationAccessibilityKey(notificationName, claimStatus);

        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkExtraActionAccessibility(String actionName, Set roles, String claimStatus) {
        String accessibilityKey = getExtraActionAccessibilityKey(actionName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }
        return Declined;
    }

    public Short checkActionAccessibility(String actionName, Set roles, String claimStatus) {

        String accessibilityKey = getActionAccessibilityKey(actionName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkFilterAccessibility(String filterName, Set roles) {

        String accessibilityKey = getFilterAccessibilityKey(filterName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkPanelAccessibility(String filterName, Set roles) {

        String accessibilityKey = getPanelAccessibilityKey(filterName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkMenuAccessibility(String menuName, Set roles) {

        String accessibilityKey = getMenuAccessibilityKey(menuName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkReportAccessibility(String reportName, Set roles) {

        String accessibilityKey = getReportAccessibilityKey(reportName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public Short checkAdminAccessibility(String adminName, Set roles) {

        String accessibilityKey = getAdminAccessibilityKey(adminName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, roles);
        }

        return Declined;
    }

    public AccessibilityEditable checkTabEditableCheck(String tabName, Set roles, String claimStatus) {

        AccessibilityEditable accessibilityEditable = null;

        String accessibilityKey = getTabAccessibilityKey(tabName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            accessibilityEditable = this.accessibilityService.getAccessibilityEditable(accessibilityKey);
        }

        return accessibilityEditable;
    }

    public AccessibilityEditable checkActionEditableCheck(String actionName, Set roles, String claimStatus) {

        AccessibilityEditable accessibilityEditable = null;

        String accessibilityKey = getActionAccessibilityKey(actionName, claimStatus);

        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            accessibilityEditable = this.accessibilityService.getAccessibilityEditable(accessibilityKey);
        }

        return accessibilityEditable;
    }

    public AccessibilityEditable checkExtraActionEditableCheck(String actionName, Set roles, String claimStatus) {
        AccessibilityEditable accessibilityEditable = null;
        String accessibilityKey = getExtraActionAccessibilityKey(actionName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            accessibilityEditable = this.accessibilityService.getAccessibilityEditable(accessibilityKey);
        }
        return accessibilityEditable;
    }

    public AccessibilityEditable checkNotificationEditableCheck(String notificationName, Set roles, String claimStatus) {

        AccessibilityEditable accessibilityEditable = null;
        String accessibilityKey = getNotificationAccessibilityKey(notificationName, claimStatus);

        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            accessibilityEditable = this.accessibilityService.getAccessibilityEditable(accessibilityKey);
        }

        return accessibilityEditable;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY KEY">
    private String getTabAccessibilityKey(String tabName, String claimStatus) {
        return String.format("tab.%1$s.%2$s", tabName, claimStatus);
    }

    private String getNotificationAccessibilityKey(String notificationName, String claimStatus) {
        return String.format("notification.%1$s.%2$s", notificationName, claimStatus);
    }

    private String getActionAccessibilityKey(String actionName, String claimStatus) {
        return String.format("action.%1$s.%2$s", actionName, claimStatus);
    }

    private String getExtraActionAccessibilityKey(String actionName, String claimStatus) {
        return String.format("extraAction.%1$s.%2$s", actionName, claimStatus);
    }

    private String getFilterAccessibilityKey(String filterName) {
        return String.format("filter.%1$s", filterName);
    }

    private String getPanelAccessibilityKey(String filterName) {
        return String.format("panel.%1$s", filterName);
    }

    private String getMenuAccessibilityKey(String menuName) {
        return String.format("menu.%1$s", menuName);
    }

    private String getReportAccessibilityKey(String reportName) {
        return String.format("report.%1$s", reportName);
    }
    // </editor-fold>

    private Short checkAccebility(HashMap roleMap, Set roles) {

        short right = 0;
        boolean isRoleSpecified = false;

        //1. if rolemap did't defined, decline as request
        if (roleMap == null) {
            return Declined;
        }

        //2. return role accessibility if exist

        Iterator itr = roles.iterator();
        while (itr.hasNext()) {
            WebUserRole r = (WebUserRole) itr.next();
            if (roleMap.containsKey(r.getName())) {
                isRoleSpecified = true;
                short curRight = (Short) roleMap.get(r.getName());
                if (curRight > right) {
                    right = curRight;
                }
            }
        }

        //3. return accessibility for all role if specified
        if (!isRoleSpecified && roleMap.containsKey("ALL")) {
            return (Short) roleMap.get("ALL");
        }

        return right;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    private HashMap getAccessibilityMap() {

        if (accessibilityMap == null) {
            accessibilityMap = this.accessibilityService.getAccessibilityMap();
        }
        return accessibilityMap;
    }

    public AccessibilityService getAccessibilityService() {
        return accessibilityService;
    }

    public void setAccessibilityService(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }

    private String getAdminAccessibilityKey(String adminName) {
        return String.format("admin.%1$s", adminName);
    }
    // </editor-fold>
}
