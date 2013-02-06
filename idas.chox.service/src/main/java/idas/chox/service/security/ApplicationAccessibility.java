package idas.chox.service.security;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.*;
import idas.chox.core.services.AccessibilityService;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.PenaltyChargeService;
import idas.chox.core.util.AccessibilityHelper;

public class ApplicationAccessibility {

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationAccessibility.class);
    public static final Short DECLINED = 0;
    public static final Short READ_ONLY = 1;
    public static final Short EDITABLE = 2;
    private HashMap accessibilityMap;
    private AccessibilityService accessibilityService;
    private ClaimService claimService;
    private BreBandService breBandService;
    private PenaltyChargeService penaltyChargeService;
    private AuditTrailService auditTrailService;
    // ***************************************
    // TAB
    // ***************************************
    public static final String TAB_CLAIM_DETAIL = "ClaimDetail";
    public static final String TAB_INVOICE_DETAIL = "InvoiceDetail";
    public static final String TAB_HIRE_MONITORING = "HireMonitoring";
    public static final String TAB_PAYMENT_PACK = "PaymentPack";
    public static final String TAB_HISTORY = "History";
    public static final String TAB_NOTES = "Notes";
    public static final String TAB_TASKS = "Tasks";
    public static final String TAB_AUDIT_TRAIL = "AuditTrail";
    public static final String TAB_INVOICE_UNASSIGNED = "InvoiceUnassigned";
    // ***************************************
    // Button
    // ***************************************
    public static final String SWITCH_CLAIM = "SwitchClaim";
    public static final String REVERT_CLAIM = "RevertClaimStatus";
    public static final String CLOSE_CLAIM = "CloseClaim";
    public static final String REOPEN_CLAIM = "ReopenClaim";
    public static final String SWITCH_CLAIM_MULTIPLE_INS = "SwitchClaimToMultipleInsurer";
    public static final String UPDATE_PAYMENT_NOT_RECEIVED = "UpdatePaymentNotReceived";
    // ***************************************
    // NOTIFICATION
    // ***************************************
    public static final String NOTE_CLAIM_NUMBER = "ClaimNumberNotification";
    public static final String NOTE_CLAIM_VIEWING = "UserViewingNotification";
    public static final String NOTE_CLAIM_INTELLIGENT_NOTE = "IntelligentNotesNotification";
    public static final String NOTE_CLAIM_NOTES = "NotificationNotesNotification";
    public static final String NOTE_DUPLICATED_SUPPLEMENTARY_INVOICE = "DuplicatedSupplementaryInvoiceNotification";
    public static final String NOTE_AWAITING_LITIGATION_OUTCOME = "AwaitingLitigationOutcomeNotification";
    // ***************************************
    // FILTER
    // ***************************************
    public static final String FILTER_REJECTED_CLAIMS = "RejectedClaims";
    public static final String FILTER_REJECTED_SUBSCRIBER_CLAIMS = "RejectedSubscriberClaims";
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
    public static final String FILTER_ESCALATED_INVOICE_SUPERVISOR = "InvoiceEscalatedToSupervisor";
    // ***************************************
    // PANEL
    // ***************************************
    public static final String PANEL_FNOL_REVIEWED = "FNOLReviewed";
    // ***************************************
    // MENU
    // ***************************************
    public static final String MENU_DASHBOARD = "Dashboard";
    public static final String MENU_REPORT = "Report";
    public static final String MENU_ADMIN = "Admin";
    public static final String MENU_INBOX = "Inbox";
    public static final String MENU_SEARCH = "Search";
    public static final String MENU_UPLOAD = "Upload";
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
    public static final String REPORT_BILLING_CHO_REPORT = "BillingCHOReport";
    public static final String REPORT_BILLING_INS_REPORT = "BillingInsurerReport";
    public static final String REPORT_CLAIM_FILE_REPORT = "ClaimFileReport";
    public static final String REPORT_OWNER_WORKFLOW_REPORT = "OwnerWorkflowReport";
    public static final String REPORT_OWNER_PERFORMANCE_REPORT = "OwnerPerformanceReport";
    public static final String REPORT_TEAM_WORKFLOW_REPORT = "TeamWorkflowReport";
    public static final String REPORT_TEAM_PERFORMANCE_REPORT = "TeamPerformanceReport";
    public static final String REPORT_INSURER_WORKFLOW_REPORT = "ClaimStatusWorkflowReport";
    public static final String REPORT_INVOICE_STATUS_REPORT = "InvoiceStatusReport";
    public static final String REPORT_HANDLER_PERFORMANCE_REPORT = "HandlerPerformanceReport";
    public static final String REPORT_HANDLER_ACTIONS_REPORT = "IncomingHandlerActionsReport";
    //BRE Invoice Approval Report
    public static final String REPORT_BRE_INVOICE_APPROVAL_DISPUTE = "BreInvoiceApprovalDisputeReport";
    public static final String REPORT_TEAM_SITE_BRE_REPORT = "TeamSiteBreWorkflowReport";
    public static final String REPORT_WORKGROUP_OWNER_BRE_REPORT = "WorkgroupOwnerBreInvoiceReport";
    // ***************************************
    // ADMIN
    // ***************************************
    public static final String ADMIN_INSURER_COMPANIES = "InsurerCompanies";
    public static final String ADMIN_CREDIT_HIRE_ORG = "CreditHireOrg";
    public static final String ADMIN_USER_MANAGEMENT = "UserManagement";
    public static final String ADMIN_INSURER_BRE_MANAGEMENT = "InsurerBreManagement";
    public static final String ADMIN_BILLING = "Billing";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - ACTION PANEL">
    private String getActionAccessibilityKey(String actionName, String claimStatus) {
        return String.format("action.%1$s.%2$s", actionName, claimStatus);
    }

    public Short checkActionAccessibility(String actionName, WebUser user, Claim claim) {
        LOG.debug("Checking action accessibility for action {}, user {}", actionName, user.getFullName());
        String accessibilityKey = getActionAccessibilityKey(actionName, claim.getStatus());
        LOG.debug("Claim='{}', accessibilityKey={}", claim.getChoReference(), accessibilityKey);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            Accessibility accessibility = accessibilityService.getAccessibility(accessibilityKey);
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("Access right is: {} - checking claim editable.....", accessRight);
            if (accessRight >= 2) {
                accessRight = AccessibilityHelper.IsClaimEditable(accessibility.isWorkgroupCheck(), accessibility.isOwnershipCheck(), claim, user);
            }
            LOG.debug("Returning access right: {}", accessRight);
            return accessRight;
        }
        LOG.debug("Access declined (no access rights defined).");
        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - EXTRA ACTION">
    
    private String getExtraActionAccessibilityKey(String actionName, String claimStatus) {
        return String.format("extraAction.%1$s.%2$s", actionName, claimStatus);
    }

    public Short checkExtraActionAccessibility(String actionName, WebUser user, Claim claim) {
        String accessibilityKey = getExtraActionAccessibilityKey(actionName, claim.getStatus());
        LOG.debug("Checking accessibility for key: '{}'", accessibilityKey);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            Accessibility accessibility = accessibilityService.getAccessibility(accessibilityKey);
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("Extra Action Access rights for '{}' is {}", accessibilityKey, accessRight);
            LOG.debug("accessibility.isCheckWorkgroupEnabled(): {}, claim.getInsurer().isWorkgroupEnable(): {}", accessibility.isCheckWorkgroupEnabled(), claim.getInsurer().isWorkgroupEnable());
            if (accessRight > 0 && accessibility.isCheckWorkgroupEnabled() && !claim.getInsurer().isWorkgroupEnable()) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in WORKGROUP CHECK  '{}' is {}", accessibilityKey, accessRight);
            }
            if (accessRight > 0 && accessibility.isCheckClaimOwnershipEnabled() && !claim.getInsurer().isClaimOwnershipEnable()) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in CLAIM OWNERSHIP CHECK for  '{}' is {}", accessibilityKey, accessRight);
            }
            if (accessRight > 0 && accessibility.isCheckFnolEnabled() && !claim.getInsurer().isFnolEnable()) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in FNOL ENABLED CHECK for  '{}' is {}", accessibilityKey, accessRight);
            }
            if (accessRight > 0 && accessibility.isCheckEngineerEnabled() && !claim.getInsurer().isEngineersEnable()) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in ENGINEER ENABLED CHECK for  '{}' is {}", accessibilityKey, accessRight);
            }
            if (accessRight > 0 && accessibility.isCheckSupplierClaimOwnershipEnabled() && !claim.getChorganisation().isClaimOwnershipEnable()) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in SUPLIER CLAIM OWNERSHIP ENABLED for  '{}' is {}", accessibilityKey, accessRight);
            }
            if (accessRight > 0 && actionName.equals(ExtraAction.UPDATE_INSURER_CLAIM_OWNER) && claim.getInsurer().isWorkgroupEnable()) {
                 accessRight = 0;
            }
            
            /*
             * If any of this condition !(insurerWorkgroupEnabled or
             * insurerClaimOwnershipEnabled) or !(manualInvoiceWorkgroupEnabled
             * or manualInvoiceClaimOwnershipEnabled) is true then disable the
             * manual invoice extra action. And this condiont is equal to
             * (insurerWorkgroupDisabled and insurerClaimOwnershipDisabled) or
             * (manualInvoiceWorkgroupDisabled and
             * manualInvoiceClaimOwnershipDisabled).
             *
             */
            if (accessRight > 0 && actionName.equals(ExtraAction.ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER)
                    && (!(claim.getInsurer().isEnableManualInvoiceWorkgroups() || claim.getInsurer().isEnableManualInvoiceOwnership()) 
                    || (!(claim.getInsurer().isWorkgroupEnable() || claim.getInsurer().isClaimOwnershipEnable())))) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in MANUAL INVOICE WORKGROUP/OWNERSHIP CHECK  '{}' is {}", accessibilityKey, accessRight);
            }
            
            if (accessRight > 0 && actionName.equals(ExtraAction.UPDATE_CLAIM_WORKGROUP)
                    && claim.getInsurer().isClaimOwnershipEnable()) {
                accessRight = 0;
                LOG.debug("accessRight made to 0 in WORKGROUP UPDATE CHECK  '{}' is {}", accessibilityKey, accessRight);
            }
            
            if (accessRight >= 2) {
                accessRight = AccessibilityHelper.IsClaimEditable(accessibility.isWorkgroupCheck(), accessibility.isOwnershipCheck(), claim, user);
                LOG.debug("accessRight from after ACCESSIBILITY HELPER is  '{}' is {}", accessibilityKey, accessRight);
            }

            if (accessRight >= 2) {
                if (actionName.equals(ExtraAction.UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL)) {
                    boolean b = true;
                    try {
                        // If there is an outstanding interim payment to be received, this action panel
                        // is already visible so do not display this more action
                        if (claim.getInvoice() == null || claim.getInvoice().getInterimPaymentMade() == null
                                || claim.getInvoice().getInterimPaymentReceived() == null
                                ||  claim.getInvoice().getInterimPaymentMade().compareTo(claim.getInvoice().getInterimPaymentReceived()) != 0) {
                            b = false;
                        }

                    } catch (Exception e) {
                        LOG.debug("thrown exception is {}", e.getMessage());
                        b = false;
                    }
                    if (!b) {
                        LOG.debug("Returning access rights for extraAction.updateInterimPaymentFullAndFinal 0 cos paymentreceived is false");
                        accessRight = 0;
                    }
                } else if (actionName.equals(ExtraAction.UPDATE_PENALTY_CHARGES)) {
                    // Check invoice was uploaded at least 30 days ago
                    Invoice invoice = claim.getInvoice();
                    if (invoice != null) {
                        int days = invoice.getInvoicedDays();
                        /*
                         * For manual invoices always show 'Adjust Penalty
                         * Charges' more action. 
                         *  "7.1.2 Insurer Manual Invoice Process Updates" says - 
                         * the age of the invoice does
                         * not have to be over say 30 days in order to be able
                         * to apply the penalty charges
                         */
                        if (days <= penaltyChargeService.getFirstPenaltyBand(claim) && claim.getClaimType() != ClaimType.INSURER_UPLOAD) {
                            LOG.debug("Returning access rights for extraAction.updatePenaltyCharges 0 as invoice only uploaded {} days ago", days);
                            accessRight = 0;
                        }
                        // Check the 'Adjust Penalty Charges' Panel is not already displayed and not insurer upload claim.
                        else if (invoice.getPenaltyBand() > -1 && claim.getClaimType() != ClaimType.INSURER_UPLOAD) { // Check if not removed from penalty queue
                            if ((!claim.getChorganisation().isAutoPenaltyChargeEnabled() 
                                    || (claim.getChorganisation().isAutoPenaltyChargeEnabled() 
                                        && (!claim.isAutoPenaltyChargeEnabled() 
                                            || penaltyChargeService.calculateCurrentPenaltyBand(claim) >= penaltyChargeService.getLastPenaltyBand(claim)))) 
                                    && days > invoice.getPenaltyBand()) {
                                LOG.debug("Invoice in penalty queue - no access to More Action 'updatePenaltyCharges'");
                                accessRight = 0;
                            }
                        }
                        // Check Penalty Charges disallowed and no current charges
                        if (accessRight != 0) {
                            if (claim.getBreBand() == null) {
                                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                                claim.setBreBand(choBand);
                            }
                            if (!claim.getBreBand().isAllowPenaltyCharges() && (invoice.getTotalPenaltyCharge() == null || invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) == 0) ) {
                                accessRight = 0;
                                LOG.debug("Penalty Charges not allowed by BRE band and no existing penalty charges - no access to More Action 'updatePenaltyCharges'");
                            }
                        }
                        
                    } else {
                        // No invoice!
                        LOG.debug("No invoice - no access to More Action 'updatePenaltyCharges'");
                        accessRight = 0;
                    }
                    LOG.debug("Access right for Update Penalty Charges is {}", accessRight);
                } else if (actionName.equals(ExtraAction.PENALTY_CHARGE_CONFIGURATION)) {
                    Invoice invoice = claim.getInvoice();
                    if (claim.getInvoice() != null) {
                        if (claim.getBreBand() == null) {
                            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                            claim.setBreBand(choBand);
                        }

                        if (!claim.getBreBand().isAllowPenaltyCharges()) {
                            LOG.debug("BRE Band does not allow penalty charges");
                            accessRight = 0;
                        }
                    } else { // No invoice! or wrong claim type
                        LOG.debug("No invoice or wrong claim type - no access to Penalty Charge Config");
                        accessRight = 0;
                    }
                    LOG.debug("Access right for Penalty Charges Configuration is {}", accessRight);
                } else if (actionName.equals(ExtraAction.MARK_SUPPLEMENTARY_INVOICED_CLAIM)) {

                    String customerClaimRef = claim.getCustomer().getClaimReference();

                    if (customerClaimRef != null && !customerClaimRef.isEmpty() && !customerClaimRef.equalsIgnoreCase("N/A") && !customerClaimRef.equalsIgnoreCase("NA")) {
                        List<Claim> claims = claimService.getClaimsByCustomerClaimRef(customerClaimRef, claim.getChorganisation().getId());
                        if (claims.size() > 1) {
                            for (Claim claim1 : claims) {
                                if (ClaimType.isSupplementaryInvoice(claim1.getClaimType())) {
                                    accessRight = 0;
                                }
                            }
                        } else {
                            accessRight = 0;
                        }
                    } else {
                        accessRight = 0;
                    }

                }
            }
            LOG.debug("Returning access rights for extraAction '{}': {}", accessibilityKey, accessRight);
            return accessRight;
        }
        return DECLINED;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - NOTIFICATION">
    private String getNotificationAccessibilityKey(String notificationName, String claimStatus) {
        return String.format("notification.%1$s.%2$s", notificationName, claimStatus);
    }

    public NotificationAccessibility getNotificationAccessibility(WebUser user, String claimStatus) {
        return new NotificationAccessibility(this, user, claimStatus);
    }

    public Short checkNotificationAccessibility(String notificationName, WebUser user, String claimStatus) {
        String accessibilityKey = getNotificationAccessibilityKey(notificationName, claimStatus);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccessibility(roleMap, user);
        }
        return DECLINED;
    }

    public short checkNotificationEditableCheck(String notificationName, WebUser user, Claim claim) {
        String accessibilityKey = getNotificationAccessibilityKey(notificationName, claim.getStatus());
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            Accessibility accessibility = accessibilityService.getAccessibility(accessibilityKey);
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("Access Right for '{}' is {}.", accessibilityKey, accessRight);
            if (accessRight >= 2) {
                accessRight = AccessibilityHelper.IsClaimEditable(accessibility.isWorkgroupCheck(), accessibility.isOwnershipCheck(), claim, user);
                LOG.debug("Access Right for '{}' is now {}.", accessibilityKey, accessRight);
            }
            return accessRight;
        }

        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - TAB">
    private String getTabAccessibilityKey(String tabName, String claimStatus) {
        return String.format("tab.%1$s.%2$s", tabName, claimStatus);
    }

    public TabAccessibility getTabAccessibility(WebUser user, Claim claim) {
        return new TabAccessibility(this, user, claim);
    }

    public Short checkTabAccessibility(String tabName, WebUser user, Claim claim) {

        String accessibilityKey = getTabAccessibilityKey(tabName, claim.getStatus());

        if (getAccessibilityMap().containsKey(accessibilityKey)) {

            Accessibility accessibility = accessibilityService.getAccessibility(accessibilityKey);

            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);
            //log.debug(accessibilityKey + " " + accessRight);
            if (accessRight >= 2) {
                accessRight = AccessibilityHelper.IsClaimEditable(accessibility.isWorkgroupCheck(), accessibility.isOwnershipCheck(), claim, user);
            }

            return accessRight;
        }
        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - FNOL PANE">
    public PanelAccessibility getPanelAccessibility(WebUser user) {
        return new PanelAccessibility(this, user);
    }

    private String getPanelAccessibilityKey(String filterName) {
        return String.format("panel.%1$s", filterName);
    }

    public Short checkPanelAccessibility(String filterName, WebUser user) {

        String accessibilityKey = getPanelAccessibilityKey(filterName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccessibility(roleMap, user);
        }

        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - MENU">
    public MenuAccessibility getMenuAccessibility(WebUser user) {
        return new MenuAccessibility(this, user);
    }

    private String getMenuAccessibilityKey(String menuName) {
        return String.format("menu.%1$s", menuName);
    }

    public Short checkMenuAccessibility(String menuName, WebUser user) {

        String accessibilityKey = getMenuAccessibilityKey(menuName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccessibility(roleMap, user);
        }

        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - REPORT">
    public ReportAccessibility getReportAccessibility(WebUser user) {
        return new ReportAccessibility(this, user);
    }

    private String getReportAccessibilityKey(String reportName) {
        return String.format("report.%1$s", reportName);
    }

    public Short checkReportAccessibility(String reportName, WebUser user) {

        String accessibilityKey = getReportAccessibilityKey(reportName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            Accessibility accessibility = accessibilityService.getAccessibility(accessibilityKey);
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("Access Right for '{}' is {}.", accessibilityKey, accessRight);
            if (accessRight > 0 && accessibility.isCheckWorkgroupEnabled() && !user.getInsurer().isWorkgroupEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckClaimOwnershipEnabled() && !user.getInsurer().isClaimOwnershipEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckFnolEnabled() && !user.getInsurer().isFnolEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckEngineerEnabled() && !user.getInsurer().isEngineersEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckSupplierClaimOwnershipEnabled() && !user.getChorganisation().isClaimOwnershipEnable()) {
                accessRight = 0;
            }
            LOG.debug("Returned access Right for '{}' is {}.", accessibilityKey, accessRight);
            return accessRight;
        }

        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - ADMIN">
    private String getAdminAccessibilityKey(String adminName) {
        return String.format("admin.%1$s", adminName);
    }

    public AdminAccessibility getAdminAccessibility(WebUser user) {
        return new AdminAccessibility(this, user);
    }

    public Short checkAdminAccessibility(String adminName, WebUser user) {

        String accessibilityKey = getAdminAccessibilityKey(adminName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            return checkAccessibility(roleMap, user);
        }

        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - BUTTON">
    private String getButtonAccessibilityKey(String buttonName, String claimStatus) {


        return String.format("button.%1$s.%2$s", buttonName, claimStatus);
    }

    public ButtonAccessibility getButtonAccessibility(WebUser user, Claim claim) {
        return new ButtonAccessibility(this, user, claim);
    }

    public short checkButtonAccessibility(String buttonName, WebUser user, Claim claim) {

        LOG.debug("Checking Button accessibility for action {}, user {}", buttonName, user.getFullName());

        String accessibilityKey = getButtonAccessibilityKey(buttonName, claim.getStatus());

        if (getAccessibilityMap().containsKey(accessibilityKey)) {

            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);

            if (user.isAnInsurer() && buttonName.equalsIgnoreCase(ApplicationAccessibility.REOPEN_CLAIM) && !ClaimType.isInsurerUpload(claim.getClaimType())) {
                LOG.debug("Declined access to Button accessibility (ReOpen claim) as this claim is not insurer uploaded.");
                return DECLINED;
            }
            // this fix is for bug 2208 disable revert function when there is no previous status.
            AuditTrail auditTrail = auditTrailService.getLastChange(claim.getId());
            if (buttonName.equalsIgnoreCase(ApplicationAccessibility.REVERT_CLAIM)
                    && (auditTrail == null || auditTrail.getOriginalStatus() == null
                    || auditTrail.getOriginalStatus().isEmpty())) {
                return DECLINED;
            }
            // </editor-fold>
            LOG.debug("Returning Button accessibility access right: {}", accessRight);
            return accessRight;

        }

        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - FILTER OR QUEUE">
    private String getFilterAccessibilityKey(String filterName) {
        //log.debug("Filter : "+String.format("filter.%1$s", filterName));
        return String.format("filter.%1$s", filterName);
    }

    public Short checkFilterAccessibility(String filterName, WebUser user) {
        String accessibilityKey = getFilterAccessibilityKey(filterName);
        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);

            return checkAccessibility(roleMap, user);
        }
        return DECLINED;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - BATCH UPDATE">
    private String getBatchUpdateAccessibilityKey(String actionName, String claimStatus) {
        return String.format("batch.%1$s.%2$s", actionName, claimStatus);
    }

    public Short checkBatchUpdateEditableAccessibility(String actionName, WebUser user, Claim claim) {

        String accessibilityKey = getBatchUpdateAccessibilityKey(actionName, claim.getStatus());

        if (getAccessibilityMap().containsKey(accessibilityKey)) {

//            System.out.println(">>>>>>>>>>>>>>"+accessibilityKey);

            Accessibility accessibility = accessibilityService.getAccessibility(accessibilityKey);
            HashMap roleMap = (HashMap) getAccessibilityMap().get(accessibilityKey);
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("Batch Update Access rights for '{}' is {}", accessibilityKey, accessRight);
            if (accessRight > 0 && accessibility.isCheckWorkgroupEnabled() && !claim.getInsurer().isWorkgroupEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckClaimOwnershipEnabled() && !claim.getInsurer().isClaimOwnershipEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckFnolEnabled() && !claim.getInsurer().isFnolEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckEngineerEnabled() && !claim.getInsurer().isEngineersEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckSupplierClaimOwnershipEnabled() && !claim.getChorganisation().isClaimOwnershipEnable()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckManualInvoiceWrokgroupEnabled() && !claim.getInsurer().isEnableManualInvoiceWorkgroups()) {
                accessRight = 0;
            }
            if (accessRight > 0 && accessibility.isCheckManualInvoiceClaimOwnershipEnabled() && !claim.getInsurer().isEnableManualInvoiceOwnership()) {
                accessRight = 0;
            }

            if (accessRight >= 2) {
                LOG.debug("Before editable check Batch Update Access rights for '{}' is {}", accessibilityKey, accessRight);
                accessRight = AccessibilityHelper.IsClaimEditable(accessibility.isWorkgroupCheck(), accessibility.isOwnershipCheck(), claim, user);
                LOG.debug("after editable check Batch Update Access rights for '{}' is {}", accessibilityKey, accessRight);
            }
            LOG.debug("Returning access rights for batchupdate '{}': {}", accessibilityKey, accessRight);
            return accessRight;
        }
        return DECLINED;
    }

    public List<String> checkBatchUpdateAccessibility(String actionName, WebUser user) {


        List<String> statuses = new ArrayList<String>();

        // GET LIST OF ACCESSIBILITY BY ACTION NAME
        String accessibilityKey = String.format("batch.%1$s", actionName);
        List<Accessibility> accessibilities = this.accessibilityService.getBatchUpdateAccessibilityMap(accessibilityKey);

        for (Accessibility accessibility : accessibilities) {

            HashMap roleMap = new HashMap();
            for (Object item : accessibility.getAccessibilityItem()) {
                AccessibilityItem aItem = (AccessibilityItem) item;
                Short accessRight = aItem.getAccessRight();
                if (accessRight > 0 && accessibility.isCheckWorkgroupEnabled() && user.isAnInsurer() && !user.getInsurer().isWorkgroupEnable()) {
                    accessRight = 0;
                }
                if (accessRight > 0 && accessibility.isCheckClaimOwnershipEnabled() && user.isAnInsurer() && !user.getInsurer().isClaimOwnershipEnable()) {
                    accessRight = 0;
                }
                if (accessRight > 0 && accessibility.isCheckFnolEnabled() && user.isAnInsurer() && !user.getInsurer().isFnolEnable()) {
                    accessRight = 0;
                }
                if (accessRight > 0 && accessibility.isCheckEngineerEnabled() && user.isAnInsurer() && !user.getInsurer().isEngineersEnable()) {
                    accessRight = 0;
                }
                if (accessRight > 0 && accessibility.isCheckSupplierClaimOwnershipEnabled() && user.isCHO() && !user.getChorganisation().isClaimOwnershipEnable()) {
                    accessRight = 0;
                }
                if (accessRight > 0 && accessibility.isCheckManualInvoiceWrokgroupEnabled() && user.isAnInsurer() && !user.getInsurer().isEnableManualInvoiceWorkgroups()) {
                    accessRight = 0;
                }
                if (accessRight > 0 && accessibility.isCheckManualInvoiceClaimOwnershipEnabled() && user.isAnInsurer() && !user.getInsurer().isEnableManualInvoiceOwnership()) {
                    accessRight = 0;
                }

                roleMap.put(aItem.getRole().trim(), accessRight);
            }

            if (checkAccessibility(roleMap, user) > 0) {
                String status = accessibility.getName().substring((accessibility.getName().lastIndexOf(".") + 1), (accessibility.getName()).length());
                statuses.add(status);
            }
        }
        return statuses;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    private HashMap getAccessibilityMap() {

        if (accessibilityMap == null) {
            accessibilityMap = this.accessibilityService.getAccessibilityMap();
        }
        return accessibilityMap;
    }

    public ClaimService getClaimService() {
        return claimService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setBreBandService(BreBandService BreBandService) {
        this.breBandService = BreBandService;
    }

    public void setPenaltyChargeService(PenaltyChargeService penaltyChargeService) {
        this.penaltyChargeService = penaltyChargeService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public AccessibilityService getAccessibilityService() {
        return accessibilityService;
    }

    public void setAccessibilityService(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }
    // </editor-fold>

    private Short checkAccessibility(HashMap roleMap, WebUser user) {

        short right = 0;
        boolean isRoleSpecified = false;

        //1. if rolemap did't defined, decline as request
        if (roleMap == null) {
            return DECLINED;
        }

        //2. return role accessibility if exist
        Iterator itr = user.getRoles().iterator();
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
            right = (Short) roleMap.get("ALL");
        }

        return right;
    }
}