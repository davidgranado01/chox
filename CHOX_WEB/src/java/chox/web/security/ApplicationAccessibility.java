/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.security;

import chox.services.AccessibilityService;
import java.util.HashMap;
import org.acegisecurity.GrantedAuthority;

/**
 *
 * @author Emmanuel
 */
public class ApplicationAccessibility {

    //Access Right
    public static final Short Declined = 0;
    public static final Short ReadOnly = 1;
    public static final Short Editable = 2;
    
    //Tab Name
    public static final String TAB_CLAIM_DETAIL = "ClaimDetail";
    public static final String TAB_INVOICE_DETAIL = "InvoiceDetail";
    public static final String TAB_HIRE_MONITORING = "HireMonitoring";
    public static final String TAB_PAYMENT_PACK = "PaymentPack";
    public static final String TAB_HISTORY = "History";
    public static final String TAB_NOTES = "Notes"; 
    public static final String TAB_AUDIT_TRAIL = "AuditTrail"; 
    
    //Filter Name
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
    public static final String FILTER_CONTESTED_INVOICE_REF_INS = "ContestedInvoicesReferredToInsurer";
    public static final String FILTER_INVOICE_APPROVED_BY_BRE = "InvoicesApprovedByBRE";
    public static final String FILTER_CLAIM_REF_ENG = "ClaimReferredToEngineer";
    public static final String FILTER_CLAIM_REF_FNOL = "ClaimReferredToFNOL";
    public static final String FILTER_PENALTY_CHARGES_APPLIED = "PenaltyChargesApplied";
    public static final String FILTER_CLAIM_PENDING = "ClaimPending";
    public static final String FILTER_INVOICE_REF_TO_CH = "InvoiceReferredToClaimsHandler";
    public static final String FILTER_INVOICE_PAYMENT_LOGGED = "InvoicePaymentLogged";
    public static final String PANEL_FNOL_REVIEWED  = "FNOLReviewed";
    
    public static final String MENU_DASHBOARD  = "Dashboard";
    public static final String MENU_REPORT  = "Report";
    
    public static final String REPORT_INVOICE_SUMMARY = "InvoiceSummary";
    public static final String REPORT_INS_WEEKLY_SUMMARY = "InsurerWeeklySummary";
    public static final String REPORT_CLAIM_REJECTION = "ClaimRejection";
    public static final String REPORT_INSURER_PAYMENT = "InsurerPayment";
    public static final String REPORT_OVERVIEW_SUMMARY = "OverviewSummary";
    
    private HashMap accessibilityMap;
    private AccessibilityService accessibilityService;
    
    public ApplicationAccessibility() {
        
    }
    
    public TabAccessibility getTabAccessibility(GrantedAuthority[] grantedAuthorities,String claimStatus)
    {
        return new TabAccessibility(this,grantedAuthorities,claimStatus);
    }        
    
    public PanelAccessibility getPanelAccessibility(GrantedAuthority[] grantedAuthorities)
    {
        return new PanelAccessibility(this,grantedAuthorities);
    }
    
    public FilterAccessibility getFilterAccessibility(GrantedAuthority[] grantedAuthorities)
    {
        return new FilterAccessibility(this,grantedAuthorities);
    }
    
    public MenuAccessibility getMenuAccessibility(GrantedAuthority[] grantedAuthorities)
    {
        return new MenuAccessibility(this,grantedAuthorities);
    }
    
    public ReportAccessibility getReportAccessibility(GrantedAuthority[] grantedAuthorities)
    {
        return new ReportAccessibility(this,grantedAuthorities);
    }

    public Short checkTabAccessibility(String tabName, GrantedAuthority[] grantedAuthorities, String claimStatus) {

        //accessibilityMap = getAccessibilityMap();
        String accessibilityKey = getTabAccessibilityKey(tabName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }

        return Declined;
    }

    public Short checkFilterAccessibility(String filterName, GrantedAuthority[] grantedAuthorities) {

        //accessibilityMap = getAccessibilityMap();
        String accessibilityKey = getFilterAccessibilityKey(filterName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }

        return Declined;
    }
    
    public Short checkPanelAccessibility(String filterName, GrantedAuthority[] grantedAuthorities) {

        //accessibilityMap = getAccessibilityMap();
        String accessibilityKey = getPanelAccessibilityKey(filterName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }

        return Declined;
    }

    public Short checkExtraActionAccessibility(String actionName, GrantedAuthority[] grantedAuthorities, String claimStatus) {
        String accessibilityKey = getExtraActionAccessibilityKey(actionName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }
        return Declined;
    }
    
    public Short checkActionAccessibility(String actionName, GrantedAuthority[] grantedAuthorities, String claimStatus) {
        String accessibilityKey = getActionAccessibilityKey(actionName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }

        return Declined;
    }
    
    public Short checkMenuAccessibility(String menuName, GrantedAuthority[] grantedAuthorities) {

        //accessibilityMap = getAccessibilityMap();
        String accessibilityKey = getMenuAccessibilityKey(menuName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }

        return Declined;
    }
    
    public Short checkReportAccessibility(String reportName, GrantedAuthority[] grantedAuthorities) {

        String accessibilityKey = getReportAccessibilityKey(reportName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccebility(roleMap, grantedAuthorities);
        }

        return Declined;
    }

    private String getTabAccessibilityKey(String tabName, String claimStatus) {
        return String.format("tab.%1$s.%2$s", tabName, claimStatus);
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

    private Short checkAccebility(HashMap roleMap, GrantedAuthority[] grantedAuthorities) {

        short right = 0;
        boolean isRoleSpecified = false;
        //1. if rolemap did't defined, decline as request
        if (roleMap == null) {
            return Declined;
        }
        
        //2. return role accessibility if exist
        for (GrantedAuthority g : grantedAuthorities) {
            if (roleMap.containsKey(g.getAuthority())) {
                isRoleSpecified = true;
                short curRight = (Short) roleMap.get(g.getAuthority());
                if(curRight > right)
                {
                    right = curRight;
                }
            }
        }

        //3. return accessibility for all role if specified
        if (!isRoleSpecified && roleMap.containsKey("ALL")) {
            return (Short) roleMap.get("ALL");
        }

        //4 decline to all non-specified accessibility
        return right;
    }

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
}









