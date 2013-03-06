package idas.chox.service.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Accessibility;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.AccessibilityService;
import idas.chox.core.util.AccessibilityHelper;

public class ApplicationAccessibility {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationAccessibility.class);
    public static final Short DECLINED = 0;
    public static final Short READ_ONLY = 1;
    public static final Short EDITABLE = 2;
    private Map<String, Accessibility> accessibilityMap;
    private Map<String, Accessibility> accessibilityByClaimTypeMap;
    private Map<String, List<Accessibility>> batchUpdateAccessibilityMap;
    private AccessibilityService accessibilityService;

    // ***************************************
    // Activities via buttons
    // ***************************************
    public static final String SWITCH_CLAIM = "SwitchClaim";
    public static final String REVERT_CLAIM = "RevertClaim";
    public static final String CLOSE_CLAIM = "CloseClaim";
    public static final String REOPEN_CLAIM = "ReopenClaim";
    public static final String SWITCH_CLAIM_MULTIPLE_INS = "SwitchClaimToMultipleInsurer";
    public static final String PAYMENT_NOT_RECEIVED = "PaymentNotReceived";
    public static final String CLAIM_REJECTION = "ClaimRejection";
    public static final String SLA_EXTENSION = "SlaExtension";
    
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
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Public  Functions">
    public Short checkActivityAccessibilityForActionPanel(String buttonName, WebUser user, Claim claim) {
        
        return checkAccessibilityEditableForClaimType(String.format("activity.%1$s.%2$s.%3$s", buttonName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }


    public Short checkActivityAccessibility(String buttonName, WebUser user, Claim claim) {
        
        return checkAccessibilityForClaimType(String.format("activity.%1$s.%2$s.%3$s", buttonName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }

    public Short checkBatchUpdateAccessibilityEditable(String actionName, WebUser user, Claim claim) {
        return checkAccessibilityEditableForClaimType(String.format("batch.%1$s.%2$s.%3$s", actionName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }
    
    public Short checkTabAccessibilityEditable(String tabName, WebUser user, Claim claim) {
        return checkAccessibilityEditableForClaimType(String.format("tab.%1$s.%2$s.%3$s", tabName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }

    public Short checkAdminAccessibility(String adminName, WebUser user) {
        return checkAccessibilityForUser(String.format("admin.%1$s", adminName), user);
    }

    public Short checkMenuAccessibility(String menuName, WebUser user) {
        return checkAccessibilityForUser(String.format("menu.%1$s", menuName), user);
    }

    public Short checkReportAccessibility(String reportName, WebUser user) {
        return checkAccessibilityForUser(String.format("report.%1$s", reportName), user);
    }

    public Short checkFilterAccessibility(String filterName, WebUser user) {
        return checkAccessibilityForUser(String.format("filter.%1$s", filterName), user);
    }


    public Short checkExtraActionAccessibilityEditable(String actionName, WebUser user, Claim claim) {
        return checkAccessibilityEditableForClaimType(String.format("extraAction.%1$s.%2$s.%3$s", actionName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }

    public Short checkNotificationAccessibilityEditable(String notificationName, WebUser user, Claim claim) {
        return checkAccessibilityEditableForClaimType(String.format("notification.%1$s.%2$s.%3$s", notificationName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }

    public Short checkNotificationAccessibility(String notificationName, WebUser user, Claim claim) {
        return checkAccessibilityForClaimType(String.format("notification.%1$s.%2$s.%3$s", notificationName, claim.getStatus(), getClaimTypeKey(claim.getClaimType())),
                user, claim);
    }

    public Short checkPanelAccessibility(String actionName, WebUser user, Claim claim) {
        return checkAccessibilityForUser(String.format("panel.%1$s.%2$s", actionName, claim.getStatus()), user);
    }

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - Statuses Allowed For Batch Update">
    public List<String> getAllowedStatusesForBatchUpdate(String actionName, WebUser user) {

        List<String> statuses = new ArrayList<String>();

        // GET LIST OF ACCESSIBILITY BY ACTION NAME
        List<Accessibility> accessibilities = getBatchUpdateAccessibilityMap().get(
                String.format("batch.%1$s", actionName));

        for (Accessibility accessibility : accessibilities) {

            Map<String, Short> roleMap = accessibility.getAccessibilityRoleMap();
            roleMap = restrictAccess(roleMap, accessibility, user);
            if (checkAccessibility(roleMap, user) > 0) {
                String status = accessibility.getName().substring((accessibility.getName().lastIndexOf(".") + 1),
                        (accessibility.getName()).length());
//                LOG.debug("Status allowed for batch update '{}': {}", actionName, status);
                statuses.add(status);
            }

        }
        return statuses;
    }
    // </editor-fold>


    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Private Functions">
    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - Editable By Claim">
    private Short checkAccessibilityEditableForClaimType(String accessibilityKey, WebUser user, Claim claim) {
        LOG.debug("Claim='{}', accessibilityKey={}", claim.getChoReference(), accessibilityKey);
        Short accessRight = checkAccessibilityForClaimType(accessibilityKey, user, claim);
        LOG.debug("Access right is: {} - checking claim editable.....", accessRight);
        if (accessRight >= 2) {
            Accessibility accessibility = (Accessibility)getAccessibilityByClaimTypeMap().get(accessibilityKey);
            accessRight = AccessibilityHelper.IsClaimEditable(accessibility.isWorkgroupCheck(), accessibility.isOwnershipCheck(), claim, user);
        }
        LOG.debug("AccessibilityEditable access for '{}'={}", accessibilityKey, accessRight);
        return accessRight;
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - By Claim Type">
    private Short checkAccessibilityForClaimType(String accessibilityKey, WebUser user, Claim claim) {
        LOG.debug("Checking accessibility for key '{}' in status '{}'", accessibilityKey, claim.getStatus());
        if (getAccessibilityByClaimTypeMap().containsKey(accessibilityKey)) {
            Accessibility accessibility = (Accessibility)getAccessibilityByClaimTypeMap().get(accessibilityKey);
            Map<String, Short> roleMap = accessibility.getAccessibilityRoleMap();
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("    Access is {}", accessRight);
            if (accessRight > 0 && claim != null && !canAccess(accessibility, claim)) {
                accessRight = 0;
            }
            LOG.debug("    Returning Access of {}", accessRight);
      
            return accessRight;
        }
        LOG.debug("Key '{}' not found",  accessibilityKey);
        return DECLINED;
    }
        

    private static String getClaimTypeKey(ClaimType claimType) {
        String claimTypeString;
        if (ClaimType.isGTA(claimType)) {
            claimTypeString = ClaimType.GTA.name();
        }
        else if (ClaimType.isFixedFee(claimType)) {
            claimTypeString = ClaimType.FIXED_FEE.name();
        }
        else if (ClaimType.isSubscriber(claimType)) {
            claimTypeString = ClaimType.SUBSCRIBER.name();
        }
        else if (ClaimType.isInsurerVsInsurer(claimType)) {
            claimTypeString = ClaimType.INSURER_VS_INSURER.name();
        }
        else if (ClaimType.isTPI(claimType)) {
            claimTypeString = ClaimType.TPI.name();
        }
        else if (ClaimType.isInsurerUpload(claimType)) {
            claimTypeString = ClaimType.INSURER_UPLOAD.name();
        }
        else {
            claimTypeString = "";
        }
        return claimTypeString;
    }

    private Map<String, Accessibility> getAccessibilityMap() {

        if (accessibilityMap == null) {
// We shouldn't need to synchronize this map as it will be read only
//            accessibilityMap = Collections.synchronizedMap(accessibilityService.getAccessibilityMap());
            accessibilityMap = accessibilityService.getAccessibilityMap();
        }
        return accessibilityMap;
    }

    private Map<String, Accessibility> getAccessibilityByClaimTypeMap() {

        if (accessibilityByClaimTypeMap == null) {
// We shouldn't need to synchronize this map as it will be read only
//            accessibilityMap = Collections.synchronizedMap(accessibilityService.getAccessibilityMap());
            accessibilityByClaimTypeMap = accessibilityService.getAccessibilityByClaimTypeMap();
        }
        return accessibilityByClaimTypeMap;
    }

    private Map<String, List<Accessibility>> getBatchUpdateAccessibilityMap() {

        if (batchUpdateAccessibilityMap == null) {
// We shouldn't need to synchronize this map as it will be read only
//            batchUpdateAccessibilityMap = Collections.synchronizedMap(accessibilityService.getBatchUpdateAccessibilityMap());
            batchUpdateAccessibilityMap = accessibilityService.getBatchUpdateAccessibilityMap();
        }
        return batchUpdateAccessibilityMap;
    }

    private Map<String, Short> restrictAccess(Map<String, Short> map, Accessibility accessibility, WebUser user) {
        Map<String, Short> results = new HashMap<String, Short>(map.size());
        
        for (String key : map.keySet()) {
            if (map.get(key) > 0 && canAccess(accessibility, user)) {
                results.put(key, map.get(key));
            } else {
                results.put(key, (short)0);
            }
        }
        return results;
    }

    private boolean canAccess(Accessibility accessibility, Claim claim) {
            if (accessibility.isCheckWorkgroupEnabled() && !claim.getInsurer().isWorkgroupEnable()) {
                return false;
            }
            if (accessibility.isCheckClaimOwnershipEnabled() && !claim.getInsurer().isClaimOwnershipEnable()) {
                return false;
            }
            if (accessibility.isCheckFnolEnabled() && !claim.getInsurer().isFnolEnable()) {
                return false;
            }
            if (accessibility.isCheckEngineerEnabled() && !claim.getInsurer().isEngineersEnable()) {
                return false;
            }
            if (accessibility.isCheckSupplierClaimOwnershipEnabled() && !claim.getChorganisation().isClaimOwnershipEnable()) {
                return false;
            }
            if (accessibility.isCheckManualInvoiceWrokgroupEnabled() && !claim.getInsurer().isEnableManualInvoiceWorkgroups()) {
                return false;
            }
            if (accessibility.isCheckManualInvoiceClaimOwnershipEnabled() && !claim.getInsurer().isEnableManualInvoiceOwnership()) {
                return false;
            }

            return true;
    }
    
    private boolean canAccess(Accessibility accessibility, WebUser user) {
            if (accessibility.isCheckWorkgroupEnabled() &&
                    (user.getChorganisation() != null || (user.getInsurer() != null && !user.getInsurer().isWorkgroupEnable()))) {
                return false;
            }
            if (accessibility.isCheckClaimOwnershipEnabled() && 
                    (user.getChorganisation() != null || (user.getInsurer() != null && !user.getInsurer().isClaimOwnershipEnable()))) {
                return false;
            }
            if (accessibility.isCheckFnolEnabled() && 
                    (user.getChorganisation() != null || (user.getInsurer() != null && !user.getInsurer().isFnolEnable()))) {
                return false;
            }
            if (accessibility.isCheckEngineerEnabled() &&
                    (user.getChorganisation() != null || (user.getInsurer() != null && !user.getInsurer().isEngineersEnable()))) {
                return false;
            }
            if (accessibility.isCheckSupplierClaimOwnershipEnabled() &&
                    (user.getInsurer()!= null || (user.getChorganisation() != null && !user.getChorganisation().isClaimOwnershipEnable()))) {
                return false;
            }

            return true;
    }

    private Short checkAccessibility(Map roleMap, WebUser user) {
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


    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY - By User">
    private Short checkAccessibilityForUser(String accessibilityKey, WebUser user) {

        if (getAccessibilityMap().containsKey(accessibilityKey)) {
            Accessibility accessibility = (Accessibility)getAccessibilityMap().get(accessibilityKey);
            Map<String, Short> roleMap = accessibility.getAccessibilityRoleMap();
            Short accessRight = checkAccessibility(roleMap, user);
            LOG.debug("Access right for before access for key '{}': {}", accessibilityKey, accessRight);
            if (accessRight > 0 && !canAccess(accessibility, user)) {
                accessRight = 0;
            }
            LOG.debug("Returning access right for key '{}': {}", accessibilityKey, accessRight);
            return accessRight;
        }

        LOG.debug("Access declined for key '{}' (no access rights defined).", accessibilityKey);
        return DECLINED;
    }
    // </editor-fold>
    
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Getters / Setters">
    public void setAccessibilityService(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }
    // </editor-fold>

}