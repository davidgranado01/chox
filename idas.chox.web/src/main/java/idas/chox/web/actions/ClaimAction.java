package idas.chox.web.actions;

import idas.chox.web.PanelAction;
import java.util.*;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.AccessibilityEditable;
import idas.chox.core.model.AttachmentType;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.Notification;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Witness;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.BusinessRulesEngService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.CommentService;
import idas.chox.core.services.HistoryService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.AccessibilityHelper;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.FileHelper;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.web.security.ApplicationAccessibility;
import idas.chox.web.security.NotificationAccessibility;
import idas.chox.web.security.PanelAccessibility;
import idas.chox.web.security.TabAccessibility;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.security.GrantedAuthority;

public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable, SessionAware {

    private static final String strPrefix = "Claim Review Note: ";
    private static final String statusMsg = "Your action has been recorded";
    private TabAccessibility tabAccessibility;
    private NotificationAccessibility notificationAccessibility;
    private Map session;
    private Integer tab = -1;
    private String actionResult;
    private String actionResult2;
    // <editor-fold defaultstate="collapsed" desc="DECLARE ACTION NAME">
    public static final String REFER_FNOL = "referFNOL";
    public static final String REJECT = "reject";
    public static final String ACCEPT = "accept";
    public static final String ASSIGNED = "assigned";
    public static final String ASSIGNED_PROCESS = "assigned_routed";
    public static final String REFER = "refer";
    public static final String EMPTY = "empty";
    public static final String REGISTER_FNOL = "registerFNOL";
    public static final String REJECT_FNOL = "rejectFNOL";
    public static final String PENDING = "pending";
    public static final String REFER_CH = "referCH";
    public static final String INV_REFER_ENG = "InvReferEng";
    public static final String UPDATED_BY_ENG = "updatedByEng";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DECLARE DROP DOWN LIST OBJECTS">
    private List vehicleClasses;
    private List reasonOfClaimRejections;
    private List reasonOfInvoiceRejections;
    private List extraActionList;
    private List insurers;
    private List statuses;
    private List workgroups;
    private List insurerWorkgroups;
    private List otherWorkgroups;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DECLARE SERVICES OBJECTS">
    private ClaimService service;
    private LookupService lookupService;
    private InvoiceService invoiceService;
    private AuditTrailService auditTrailService;
    private HistoryService historyService;
    private ReasonOfRejectionService reasonOfRejectionService;
    private WorkgroupService workgroupService;
    private UserService userService;
    private AttachmentTypeService attachmentTypeService;
    private BusinessRulesEngService businessRulesEngService;
    private CommentService commentService;
    private BreBandService breBandService;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DECLARE CLAIM OBJECT PARAMETERS">
    private Claim claim = new Claim();
    private int id = -1;
    private int vehicleClassId = -1;
    private int insurerId = -1;
    private String actionName;
    private List attachmentCategory;
    private String reasonForRejection;
    private BigDecimal totalAmountToPayBeforeNewPenaltyCharge;
    private BigDecimal totalAmountToPayAfterNewPenaltyCharge;
    private String totalAmountToPayBeforeNewPenaltyChargeFormatted;
    private String totalAmountToPayAfterNewPenaltyChargeFormatted;
    private BigDecimal penaltyChargeAmount;
    private Boolean isRemovePenaltyAlert;
    private long invoiceIntroducedDays;
    private ApplicationAccessibility applicationAccessibility;
    private PanelAccessibility panelAccessibility;
    private String extraActionName;
    private Integer hireMonitoringDetailId;
    private Integer incidentId;
    private Integer thirdPartyId;
    private Integer customerId;
    private Integer invoideId;
    private Integer vehicleHireId;
    private Integer engineerReportId;
    private Integer witnessId;
    private Integer injuryId;
    private Integer injurySolicitorId;
    private Integer notificationId;
    private int workgroupId = -1;
    private List<String> intelligentNotes;
    private IntelligentNoteDisplayEngine intelligentNoteDisplayEngine;
    private int claimOwnerId = -1;
    private int escalateWorkgroupId = -1;
    private int oasWorkgroupId = -1; // OWNERSHIP ASSIGNMENT - WORKGROUP ID
    private int uosWorkgroupId = -1; // UPDATE CLAIM OWNERSHIP - WORKGROUP ID
    private Integer reasonOfRejectionId;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MODEL DRIVEN OBJECT">
    public void prepare() throws Exception {
        if (id <= 0) {
            claim = new Claim();
        } else {
            claim = service.getClaim(id);
        }
    }

    @Override
    public String execute() throws Exception {

        if (tab > 0) {
            session.put("tabIndex", tab);
        }
        if (claim == null) {
            return "ClaimNotFound";
        } else {
            return SUCCESS;
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="CLAIM PANEL ACTION">
    public String updateClaimDetail() {
        this.service.updateClaim(claim);
        this.actionResult = "Claim Updated!";
        return SUCCESS;
    }

    public String getPaymentReceivedAction() {
        return "updatePaymentReceived";
    }

    public String route() {

        String result = SUCCESS;

        if (this.getWorkgroups() == null) {

            this.actionResult = "ERROR : You need to provide workgroup to route this claim.";

        } else {

            if (!claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {

                this.actionResult = "ERROR : Invalid operation!";

            } else {

                try {

                    String newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
                    auditTrailService.logAuditLog(newStatus, claim, null, null);
                    claim.setStatus(newStatus);
                    this.service.updateClaim(claim);

                    if (claim.getInsurer().isClaimOwnershipEnable()) {

                        newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;
                        auditTrailService.logAuditLog(newStatus, claim, null, null, 1);
                        claim.setStatus(newStatus);
                        this.service.updateClaim(claim);

                    }

                } catch (Exception ex) {
                    result = ERROR;
                    this.actionResult = "ERROR : " + ex.getMessage();
                }
            }
        }

        return result;
    }

    public String acknowledge() {

        String result = SUCCESS;
        String newStatus = "";

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_CAR_HIRE_INFO;
        } else if (this.actionName.equalsIgnoreCase(REFER)) {
            newStatus = ClaimStatus.CLAIM_REF_TO_ENG;
        } else if (this.actionName.equalsIgnoreCase(REFER_FNOL)) {
            newStatus = ClaimStatus.CLAIM_REFERRED_TO_FNOL;
        } else if (this.actionName.equalsIgnoreCase(PENDING)) {
            newStatus = ClaimStatus.CLAIM_PENDING;
        } else {
            newStatus = ClaimStatus.CLAIM_REJECTED;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY

            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }

        }

        if (!result.equalsIgnoreCase(ERROR)) {

            try {

                auditTrailService.logAuditLog(newStatus, claim, null, null);
                // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
                // createNewNote(claim.getEngineerClaimReviewNotes(), false, strPrefix);
                createNewNote(claim.getEngineerClaimReviewNotes(), 1, strPrefix);

                claim.setEngineerClaimReviewNotes("");
                claim.setIsFnolReviewed(false);
                claim.setStatus(newStatus);
                this.service.updateClaim(claim);

            } catch (Exception ex) {

                result = ERROR;
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }

        return result;
    }

    public String ownershipAssignment() {

        String result = SUCCESS;
        String newStatus = "";

        if (this.actionName.equalsIgnoreCase(ASSIGNED_PROCESS)) {

            if (this.claimOwnerId > 0) {

                newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;

                // GET NEW CLAIM OWNER
                WebUser newClaimOwner = userService.getObject(claimOwnerId);
                claim.setClaimOwner(newClaimOwner);
                claim.setIsFnolReviewed(false);
                if (oasWorkgroupId > 0) {
                    claim.setWorkgroup(workgroupService.getObject(oasWorkgroupId));
                }

            } else {
                result = ERROR;
            }

        } else if (this.actionName.equalsIgnoreCase(REFER_FNOL)) {

            newStatus = ClaimStatus.CLAIM_REFERRED_TO_FNOL;

        } else {
            result = ERROR;
        }

        if (!result.equalsIgnoreCase(ERROR)) {

            try {
                claim.setStatus(newStatus);
                this.service.updateClaim(claim);

                // LOG AUDIT TRAIL
                auditTrailService.logAuditLog(newStatus, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim);

            } catch (Exception ex) {
                result = ERROR;
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }

        return result;
    }

    public String ownershipUpdating() {

        String result = SUCCESS;
        String oldOwnerName = "N/A";
        String noteMsg = "";

        if (this.claimOwnerId > 0) {

            // GET NEW CLAIM OWNER
            WebUser newClaimOwner = userService.getObject(claimOwnerId);

            // GET OLD CLAIM OWNER
            if (claim.getClaimOwner() != null) {
                oldOwnerName = claim.getClaimOwner().getDisplayName();
            }

            noteMsg = "Claim owner changed from '" + oldOwnerName + "' to '" + newClaimOwner.getDisplayName() + "'";

            if (!result.equalsIgnoreCase(ERROR)) {

                try {

                    if (workgroupId > 0) {
                        claim.setWorkgroup(workgroupService.getObject(uosWorkgroupId));
                    }

                    claim.setClaimOwner(newClaimOwner);
                    this.service.updateClaim(claim);

                    // SAVE NEW NOTE
                    int noteVisibilityType = 0;
                    //createNewNote(noteMsg, true, "");
                    createNewNote(noteMsg, noteVisibilityType, "");

                } catch (Exception ex) {
                    result = ERROR;
                    this.actionResult = "ERROR : " + ex.getMessage();
                }
            }
        }

        return result;
    }

    /*
     * There are 2 roles are allow to send the claim to FNOL to register into 3rd party system
     * 1. COM - During Claim Ownership Assignment
     * 2. CH - DUring Claim Acknowledge Assignment
     *
     * After FNOL register the claim to 3rd party system,
     * If the claim is COME from COM, then need to back to CLAIM_UNACKNOWLEDGED_UNASSIGNED
     * Else alway go to CLAIM_UNACKNOWLEDGED_ROUTED
     */
    public String registerFNOL() {

        String result = SUCCESS;
        String newStatus = "";

        if (this.actionName.equalsIgnoreCase(REGISTER_FNOL)) {

            if (claim.getPreviousStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
                newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;
            } else {
                newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
            }

            claim.setIsFnolReviewed(true);

        } else if (this.actionName.equalsIgnoreCase(REJECT_FNOL)) {
            newStatus = ClaimStatus.CLAIM_REJECTED;
        }

        createNewNote(reasonForRejection, 1, strPrefix);

        if (!result.equalsIgnoreCase(ERROR)) {

            try {

                auditTrailService.logAuditLog(newStatus, claim, null, null);
                claim.setStatus(newStatus);
                this.service.updateClaim(claim);

            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }

        }

        return result;
    }

    private void createNewNote(String sComment, int noteVisibilityType, String strPrefix) {

        if (sComment.length() > 0) {
            Comment comment = new Comment();
            comment.setVisibilityType(noteVisibilityType);
            comment.setComment(strPrefix + sComment);
            comment.setClaim(claim);

            try {
                commentService.createNewObject(comment);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }
    }

    public String reviewByEngineer() {

        String result = SUCCESS;
        String newStatus = "";

        if (this.actionName.equalsIgnoreCase(UPDATED_BY_ENG)) {
            newStatus = ClaimStatus.CLAIM_UPDATE_BY_ENG;
        } else {
            newStatus = ClaimStatus.AWAITING_CAR_HIRE_INFO;
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);
            // INSURER ONLY
            // createNewNote(claim.getEngineerClaimReviewNotes(), false, strPrefix);
            createNewNote(claim.getEngineerClaimReviewNotes(), 1, strPrefix);

            claim.setEngineerClaimReviewNotes("");
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;
    }

    public String contestOrAcceptRejectedClaim() {

        String result = SUCCESS;
        String newStatus;

        ReasonOfRejection claimRejectionReason = null;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {

            newStatus = ClaimStatus.CLAIM_REJECTION_ACCEPTED;
            claimRejectionReason = claim.getReasonOfRejection();

        } else {

            newStatus = ClaimStatus.CLAIM_REJECTION_CONTESTED;

        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, claimRejectionReason, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return result;
    }

    public String approveContestedClaim() {

        String result = SUCCESS;
        String newStatus = "";

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_CAR_HIRE_INFO;
        } else if (this.actionName.equalsIgnoreCase(REFER)) {
            newStatus = ClaimStatus.CLAIM_REF_TO_ENG;
        } else if (this.actionName.equalsIgnoreCase(REFER_FNOL)) {
            newStatus = ClaimStatus.CLAIM_REFERRED_TO_FNOL;
        } else if (this.actionName.equalsIgnoreCase(PENDING)) {
            newStatus = ClaimStatus.CLAIM_PENDING;
        } else {

            newStatus = ClaimStatus.CLAIM_REJECTED;

            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }
        }

        if (!result.equalsIgnoreCase(ERROR)) {

            try {

                // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
                // createNewNote(claim.getEngineerClaimReviewNotes(), false, strPrefix);
                createNewNote(claim.getEngineerClaimReviewNotes(), 1, strPrefix);
                auditTrailService.logAuditLog(newStatus, claim, null, null);

                claim.setEngineerClaimReviewNotes("");
                this.claim.setStatus(newStatus);
                this.service.updateClaim(claim);

            } catch (Exception ex) {

                result = ERROR;
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }

        return result;

    }

    public String submitHireMonitoringDetail() {

        String result = SUCCESS;

        String validationECDResult = validateHireMonitoringECDDetail();
        String validationLabourResult = validateHireMonitoringLabourDetail();

        if ((validationLabourResult.length() + validationECDResult.length()) <= 0) {

            String newStatus = ClaimStatus.AWAITING_INVOICE_DATA;

            try {

                auditTrailService.logAuditLog(newStatus, claim, null, null);

                this.claim.setStatus(newStatus);
                this.service.updateClaim(claim);

            } catch (Exception ex) {

                this.actionResult = "ERROR : " + ex.getMessage();

            }

        } else {

            result = ERROR;

            if (validationECDResult.length() > 0) {
                this.actionResult = validationECDResult;
                this.actionResult2 = validationLabourResult;
            } else {
                this.actionResult = validationLabourResult;
            }
        }

        return result;
    }

    public String validateHireMonitoringECDDetail() {

        if (this.claim.getCustomer() == null || this.claim.getCustomer().getInitialECD() == null) {
            if (this.service.getECDCountByClaimId(this.claim.getId()) == 0) {
                return "Error : You need to provide an Estimated Completion Date (ECD) to submit this claim. ";
            }
        }

        return "";
    }

    public String validateHireMonitoringLabourDetail() {

        String sNonProvisionReasonDetailErrorMsg = "Error : In order to progress the claim, entries in either 'Labour Hours' or 'Total Labour Cost' fields are required, if this information cannot be provided please select the reason why using the 'Labour Information Non-Provision Reason' drop down box.";

        if (claim.getHireMonitoringDetail() == null) {
            return sNonProvisionReasonDetailErrorMsg;

        } else {

            String sNonProvisionReason = "";
            if (claim.getHireMonitoringDetail().getNonProvisionReason() != null) {
                sNonProvisionReason = claim.getHireMonitoringDetail().getNonProvisionReason().trim();
            }

            if (claim.getHireMonitoringDetail().getLabourCost() == null && claim.getHireMonitoringDetail().getLabourHour() == null && sNonProvisionReason.length() == 0 && !claim.getHireMonitoringDetail().isIsTotalLostCheck()) {
                return sNonProvisionReasonDetailErrorMsg;
            }

        }

        return "";
    }

    public String reSubmitRejectedClaim() {

        boolean bActionFlag = true;
        String result = SUCCESS;
        String sActionMsg = "";

        RulesEngineResponse reponse = businessRulesEngService.processResubmitInvoice(claim);
        historyService.logInvoiceValidationErrorMsg(reponse, claim);
        String repStatus = reponse.getStatus();

        if (!repStatus.equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {

            claim = service.getClaim(claim.getId());

            try {

                auditTrailService.logAuditLog(repStatus, claim, null, null);
                sActionMsg = "ClaimId:" + claim.getId() + "| Status:" + repStatus;

                claim.setStatus(repStatus);
                this.service.updateClaim(claim);

            } catch (Exception ex) {

                this.actionResult = "ERROR : " + ex.getMessage();
                bActionFlag = false;
                result = ERROR;
                sActionMsg = this.actionResult;

            }

            result = SUCCESS;

        } else {

            this.actionResult = "ERROR : Invoice data calculation incorrect";
            result = ERROR;

        }

        return result;

    }

    public String contestOrAcceptRejectedInvoice() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.CLAIM_REJECTION_ACCEPTED;
        } else {
            newStatus = ClaimStatus.CLAIM_REJECTION_CONTESTED;
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return result;
    }

    public String approveBREPassedClaim() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(INV_REFER_ENG)) {
            newStatus = ClaimStatus.INVOICE_REF_TO_ENG;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY

            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.getInvoice().setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }
            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejection(), 0);
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;
    }

    public String approveBREPassedByClaimHandler() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(INV_REFER_ENG)) {
            newStatus = ClaimStatus.INVOICE_REF_TO_ENG;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejectionId(), true);

            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.getInvoice().setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }

            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejection(), 0);
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return result;
    }

    // 20090422
    // CHECK THE INVOICE    
    public String approveInvoiceRefferedByEngineer() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(INV_REFER_ENG)) {
            newStatus = ClaimStatus.INVOICE_REF_TO_ENG;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.getInvoice().setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }
            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejection(), 0);
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return result;
    }

    public String updateInsurerClaimNumber() {

        String result = SUCCESS;

        try {

            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;
    }

    public String UpdateClaimWorkgroupAssignment() {

        String result = SUCCESS;

        try {

            if (escalateWorkgroupId > 0) {

                claim.setWorkgroup(workgroupService.getObject(escalateWorkgroupId));
                claim.setClaimOwner(null);
                this.service.updateClaim(claim);
            }

        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return result;
    }

    public String updateClaimOwnership() {

        String result = SUCCESS;

        return result;
    }

    public String updatePaymentReceived() {

        String result = SUCCESS;
        String newStatus = "";


        try {

            newStatus = ClaimStatus.INVOICE_PAYMENT_RECEIVED;

            auditTrailService.logAuditLog(newStatus, claim, null, null);


            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;
    }

    // 20090422
    // CHECK THE INVOICE    
    public String approveEscalatedInvoice() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(REFER_CH)) {
            newStatus = ClaimStatus.INVOICE_REF_TO_CH;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY

            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.getInvoice().setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }
            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejection(), 0);
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }
        return result;
    }

    // 20090422
    // CHECK THE INVOICE    
    public String approveContestedInvoice() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(INV_REFER_ENG)) {
            newStatus = ClaimStatus.INVOICE_REF_TO_ENG;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY
            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.getInvoice().setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }
            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejection(), 0);
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;
    }

    public String contestedInvoiceByCH() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(REFER_CH)) {
            newStatus = ClaimStatus.INVOICE_REF_TO_CH;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            // 0 - ALL, 1 - INSURER ONLY, 2 - CREDIT HIRE ONLY

            if (reasonOfRejectionId > 0) {
                ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(reasonOfRejectionId);
                claim.getInvoice().setReasonOfRejection(reasonOfRejection);
                logNewCommentForRejection(reasonOfRejection, 0);
            }

            // logNewCommentForRejection(claim.getInvoice().getReasonOfRejection(), 0);
        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return result;
    }

    // 20090422
    // CHECK THE INVOICE 
    public String resubmitOrAcceptContestedInvoice() {

        String result = SUCCESS;
        String newStatus;
        ReasonOfRejection invoiceRejectionReason = null;

        if (this.actionName.equalsIgnoreCase(REJECT)) {

            RulesEngineResponse reponse = businessRulesEngService.processResubmitInvoice(claim);
            historyService.logInvoiceValidationErrorMsg(reponse, claim);
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_INS;

        } else {

            newStatus = ClaimStatus.INVOICE_REJECTED_ACCEPTED;
            invoiceRejectionReason = claim.getInvoice().getReasonOfRejection();

        }

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, invoiceRejectionReason);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;

    }

    public String logInvoicePayment() {

        String newStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;

        try {

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            this.actionResult = "ERROR : " + ex.getMessage();

        }
        return SUCCESS;
    }

    public String getCreatedByDesc() {

        String desc = "";
        String orgName = "";
        WebUser user = claim.getCreatedBy();

        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }
            desc = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
        }
        return desc;
    }

    public String getAlertPanel() {
        String result = EMPTY;

        Invoice invoice = claim.getInvoice();
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);
        setInvoiceIntroducedDays(invoice.getInvoicedDays());
        setTotalAmountToPayBeforeNewPenaltyCharge(invoice.getTotalToPay().subtract(invoice.getPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyCharge(invoice.getTotalToPay());
        setTotalAmountToPayBeforeNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayBeforeNewPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayAfterNewPenaltyCharge()));
        setPenaltyChargeAmount(invoice.getPenaltyCharge());
        setIsRemovePenaltyAlert((Boolean) false);
        result = "penaltyChargeApplied";


        return result;
    }

    public String doApplyPenaltyCharge() {

        String result = SUCCESS;

        try {

            Invoice invoice = claim.getInvoice();
            BigDecimal newTotalAmountToPay = (invoice.getTotalToPay().subtract(invoice.getPenaltyCharge())).add(getPenaltyChargeAmount());
            invoice.setTotalToPay(newTotalAmountToPay);
            invoice.setPenaltyCharge(getPenaltyChargeAmount());
            Boolean isPenaltyAlertNotUsed = getIsRemovePenaltyAlert();

            if (isPenaltyAlertNotUsed != null && isPenaltyAlertNotUsed) {
                long dateDiff = DateHelper.daysBetween(invoice.getCreatedDate(), new Date());
                int newpenaltyAlertQty = (int) (dateDiff / 30);
                newpenaltyAlertQty = newpenaltyAlertQty >= 3 ? -1 : newpenaltyAlertQty;
                invoice.setPenaltyAlertQty(newpenaltyAlertQty);
            }

            invoice.setPenaltyChargeAppliedDate(DateHelper.getCurrentTimeStamp());
            this.invoiceService.updateObject(invoice);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }

        return result;
    }

    public String doUpdateClaimStatus() {
        String result = SUCCESS;

        try {

            claim = service.getClaim(id);
            String newStatus = ClaimStatus.CLAIM_CLOSED;

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }
        return result;
    }

    public String doReopenClaimStatus() {

        String result = SUCCESS;

        try {

            claim = service.getClaim(id);
            String newStatus = claim.getPreviousStatus();

            auditTrailService.logAuditLog(newStatus, claim, null, null);

            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();

        }
        return result;
    }

    public boolean getIsShowPenaltyChargeAlert() {
        boolean result = false;

        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();
            if (invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT) && invoice.getPenaltyAlertQty() > -1) {
                result = invoice.getInvoicedDays() > (invoice.getPenaltyAlertQty() + 1) * 30;
            }
        }

        return result;
    }

    public boolean getIsShowPenaltyChargePanel() {
        boolean result = false;

        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();

            if (invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
                result = invoice.getInvoicedDays() > 30;
            }
        }

        return result;
    }

    public boolean getIsClaimClosed() {
        boolean bFlag = false;

        if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)) {
            bFlag = true;
        }

        return bFlag;
    }

    public boolean getIsClaimNumberDuplicated() {
        boolean bFlag = false;

        if (!claim.getClaimNumber().isEmpty()) {
            if (service.getClaimCountByClaimNumber(claim.getClaimNumber(), claim.getId()) > 0) {
                bFlag = true;
            }
        }

        return bFlag;
    }

    public void logNewCommentForRejection(ReasonOfRejection reasonOfRejection, int noteVisibilityType) {

        if (reasonOfRejection != null) {
            createNewNote(reasonOfRejection.getName(), noteVisibilityType, "Reason For Rejection: ");
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="STRUCT RENDER PAGE">
    public String getUpdateInsurerClaimNumber() {
        return SUCCESS;
    }

    public String getUpdateClaimOwnership() {
        return SUCCESS;
    }

    public String getEscalateUnassignedClaim() {
        return SUCCESS;
    }

    public String getExtraActionName() {
        return extraActionName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY CONTROL">
    public TabAccessibility getTabAccessibility() {

        if (tabAccessibility == null) {
            tabAccessibility = applicationAccessibility.getTabAccessibility(getAuthenticatedUser().getAuthorities(), claim);
        }
        return tabAccessibility;
    }

    public NotificationAccessibility getNotificationAccessibility() {

        if (notificationAccessibility == null) {
            notificationAccessibility = applicationAccessibility.getNotificationAccessibility(getAuthenticatedUser().getAuthorities(), claim.getStatus());
        }
        return notificationAccessibility;
    }

    public PanelAccessibility getPanelAccessibility() {
        if (panelAccessibility == null) {
            panelAccessibility = applicationAccessibility.getPanelAccessibility(getAuthenticatedUser().getAuthorities());
        }
        return panelAccessibility;
    }

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="NOTIFICATION">
    public List<Notification> getNotifications() {
        return claim.getNotifications();
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String removeNotification() {

        if (notificationId > 0) {

            Notification notification = claim.GetNotificationById(notificationId);
            if (notification != null) {
                claim.RemoveNotifications(notification);
                service.updateClaim(claim);
            }

        } else {

            claim.RemoveAllNotifications();
            service.updateClaim(claim);

        }

        return SUCCESS;
    }

    public String renderNotifications() {
        return SUCCESS;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getEscalateWorkgroupId() {
        return escalateWorkgroupId;
    }

    public void setEscalateWorkgroupId(int escalateWorkgroupId) {
        this.escalateWorkgroupId = escalateWorkgroupId;
    }

    public int getOasWorkgroupId() {
        return oasWorkgroupId;
    }

    public void setOasWorkgroupId(int oasWorkgroupId) {
        this.oasWorkgroupId = oasWorkgroupId;
    }

    public int getUosWorkgroupId() {
        return uosWorkgroupId;
    }

    public void setUosWorkgroupId(int uosWorkgroupId) {
        this.uosWorkgroupId = uosWorkgroupId;
    }

    public List<String> getIntelligentNotes() {
        if (intelligentNotes == null) {
            intelligentNotes = intelligentNoteDisplayEngine.getIntelligentNotes(claim);
        }
        return intelligentNotes;
    }

    public Boolean getIsAnyIntelligentNotes() {
        return getIntelligentNotes().size() > 0;
    }

    public Boolean getIsClaimAnomalous() {
        return claim.getIsIsAnomalies();
    }

    public void setIntelligentNoteDisplayEngine(IntelligentNoteDisplayEngine intelligentNoteDisplayEngine) {
        this.intelligentNoteDisplayEngine = intelligentNoteDisplayEngine;
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses();
        }
        return statuses;
    }

    public String getAllowFileType() {

        String sAllowFileType = "";

        for (AttachmentType a : attachmentTypeService.getAllAttachmentType()) {
            sAllowFileType += "." + a.getCode() + ", ";
        }

        if (sAllowFileType.length() > 2) {
            sAllowFileType = sAllowFileType.substring(0, sAllowFileType.length() - 2);
        }

        return sAllowFileType;
    }

    public List<AttachmentType> getAllowFileTypes() {
        return attachmentTypeService.getAllAttachmentType();
    }

    public int getMaxFileSize() {
        return FileHelper.MAX_FILE_SIZE_ALLOW;
    }

    public List getAttachmentCategory() {
        List items = new ArrayList<LookupItem>();
        for (String s : AttachmentCategory.getAttachmentCategory()) {
            items.add(new LookupItem(s, s));
        }
        attachmentCategory = items;
        return attachmentCategory;
    }

    public void setSession(Map arg0) {
        this.session = arg0;
    }

    public void setTab(Integer tab) {
        this.tab = tab;
    }

    public BigDecimal getTotalAmountToPayBeforeNewPenaltyCharge() {
        return totalAmountToPayBeforeNewPenaltyCharge;
    }

    public void setTotalAmountToPayBeforeNewPenaltyCharge(BigDecimal totalAmountToPayBeforeNewPenaltyCharge) {
        this.totalAmountToPayBeforeNewPenaltyCharge = totalAmountToPayBeforeNewPenaltyCharge;
    }

    public BigDecimal getTotalAmountToPayAfterNewPenaltyCharge() {
        return totalAmountToPayAfterNewPenaltyCharge;
    }

    public void setTotalAmountToPayAfterNewPenaltyCharge(BigDecimal totalAmountToPayAfterNewPenaltyCharge) {
        this.totalAmountToPayAfterNewPenaltyCharge = totalAmountToPayAfterNewPenaltyCharge;
    }

    public BigDecimal getPenaltyChargeAmount() {
        return penaltyChargeAmount;
    }

    public void setPenaltyChargeAmount(BigDecimal penaltyChargeAmount) {
        this.penaltyChargeAmount = penaltyChargeAmount;
    }

    public Boolean getIsRemovePenaltyAlert() {
        return isRemovePenaltyAlert;
    }

    public void setIsRemovePenaltyAlert(Boolean isRemovePenaltyAlert) {
        this.isRemovePenaltyAlert = isRemovePenaltyAlert;
    }

    public String getTotalAmountToPayBeforeNewPenaltyChargeFormatted() {
        return totalAmountToPayBeforeNewPenaltyChargeFormatted;
    }

    public void setTotalAmountToPayBeforeNewPenaltyChargeFormatted(String totalAmountToPayBeforeNewPenaltyChargeFormatted) {
        this.totalAmountToPayBeforeNewPenaltyChargeFormatted = totalAmountToPayBeforeNewPenaltyChargeFormatted;
    }

    public String getTotalAmountToPayAfterNewPenaltyChargeFormatted() {
        return totalAmountToPayAfterNewPenaltyChargeFormatted;
    }

    public void setTotalAmountToPayAfterNewPenaltyChargeFormatted(String totalAmountToPayAfterNewPenaltyChargeFormatted) {
        this.totalAmountToPayAfterNewPenaltyChargeFormatted = totalAmountToPayAfterNewPenaltyChargeFormatted;
    }

    public long getInvoiceIntroducedDays() {
        return invoiceIntroducedDays;
    }

    public void setInvoiceIntroducedDays(long invoiceIntroducedDays) {
        this.invoiceIntroducedDays = invoiceIntroducedDays;
    }

    public boolean getIsFnolPanelVisible() {
        return getPanelAccessibility().getFnolReviewedPanelAccessible();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Claim getModel() {
        return claim;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getActionResult() {
        return actionResult;
    }

    public String getActionResult2() {
        return actionResult2;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setReasonForRejection(String s) {
        this.reasonForRejection = s;
    }

    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET SUB OBJECT ID">
    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }

    public int getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public int getInsurerClassId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getHireMonitoringDetailId() {
        if (hireMonitoringDetailId == null) {
            HireMonitoringDetail h = claim.getHireMonitoringDetail();
            hireMonitoringDetailId = h == null ? -1 : h.getId();
        }

        return hireMonitoringDetailId;
    }

    public int getIncidentId() {
        if (incidentId == null) {
            Incident i = claim.getIncident();
            incidentId = i == null ? -1 : i.getId();
        }
        return incidentId;
    }

    public int getThirdPartyId() {
        if (thirdPartyId == null) {
            ThirdParty t = claim.getThirdParty();
            thirdPartyId = t == null ? -1 : t.getId();
        }
        return thirdPartyId;
    }

    public int getCustomerId() {
        if (customerId == null) {
            Customer c = claim.getCustomer();
            customerId = c == null ? -1 : c.getId();
        }

        return customerId;
    }

    public int getInvoiceId() {
        if (invoideId == null) {
            Invoice i = claim.getInvoice();
            invoideId = i == null ? -1 : i.getId();
        }

        return invoideId;
    }

    public int getVehicleHireId() {
        if (vehicleHireId == null) {
            VehicleHire v = claim.getVehicleHire();
            vehicleHireId = v == null ? -1 : v.getId();
        }

        return vehicleHireId;
    }

    public int getEngineerReportId() {
        if (engineerReportId == null) {
            EngineerReport e = claim.getEngineerReport();
            engineerReportId = e == null ? -1 : e.getId();
        }

        return engineerReportId;
    }

    public int getWitnessId() {
        if (witnessId == null) {
            witnessId = -1;
            Incident incident = claim.getIncident();

            if (incident != null) {
                Witness witness = incident.getWitness();
                witnessId = witness == null ? -1 : witness.getId();
            }
        }
        return witnessId;
    }

    public int getInjuryId() {

        if (injuryId == null) {
            injuryId = -1;
            Incident incident = claim.getIncident();

            if (incident != null) {
                Injury injury = incident.getInjury();
                injuryId = injury == null ? -1 : injury.getId();
            }
        }
        return injuryId;
    }

    public int getInjurySolicitorId() {

        if (injurySolicitorId == null) {
            injurySolicitorId = -1;
            Incident incident = claim.getIncident();

            if (incident != null) {
                Injury injury = incident.getInjury();
                if (injury != null) {
                    Solicitor solicitor = injury.getSolicitor();
                    injurySolicitorId = solicitor == null ? -1 : solicitor.getId();
                }
            }
        }
        return injurySolicitorId;
    }

    public BreBandService getBreBandService() {
        return breBandService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public void setReasonOfRejectionService(ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }

    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }

    public void setBusinessRulesEngService(BusinessRulesEngService businessRulesEngService) {
        this.businessRulesEngService = businessRulesEngService;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GET DROP DOWN LIST">
    /*
    public List getOtherWorkgroups() {

    if (otherWorkgroups == null) {
    otherWorkgroups = lookupService.getWorkgroupsByInsurerId(this.getAuthenticatedUser().getUser(), true);
    }

    return otherWorkgroups;
    }
     */
    public List getWorkgroups() {

        if (workgroups == null) {
            workgroups = lookupService.getWorkgroups(this.getAuthenticatedUser().getUser(), true);
        }

        return workgroups;

    }

    public List getInsurerWorkgroups() {

        if (insurerWorkgroups == null) {
            if (this.getAuthenticatedUser().getUser().getInsurer() != null) {
                insurerWorkgroups = lookupService.getWorkgroupsByInsurerId(this.getAuthenticatedUser().getUser().getInsurer().getId(), true);
            } else {
                insurerWorkgroups = lookupService.getWorkgroupsByInsurerId(-1, true);
            }
        }

        return insurerWorkgroups;

    }

    public List getVehicleClasses() {
        if (vehicleClasses == null) {
            vehicleClasses = lookupService.getVehicleClasses();
        }
        return vehicleClasses;
    }

    public List getInsurers() {

        if (insurers == null) {

            if (this.getAuthenticatedUser().getUser().getChorganisation() != null) {
                Chorganisation currentCho = this.getAuthenticatedUser().getUser().getChorganisation();
                insurers = this.lookupService.getInsurers(currentCho.getId());
            } else {
                insurers = this.lookupService.getInsurers();
            }

        }

        return insurers;
    }

    public List getReasonOfClaimRejections() {
        if (reasonOfClaimRejections == null) {
            reasonOfClaimRejections = lookupService.getClaimRejectionReason();
        }
        return reasonOfClaimRejections;
    }

    public List getReasonOfInvoiceRejections() {
        if (reasonOfInvoiceRejections == null) {
            reasonOfInvoiceRejections = lookupService.getInvoiceRejectionReason();
        }
        return reasonOfInvoiceRejections;
    }

    // </editor-fold>
    public String getActionPanel() {

        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        List<String> actions = PanelAction.getPanelActions();

        for (String action : actions) {

            short accessRight = applicationAccessibility.checkActionAccessibility(action, grantedAuthorities, claim.getStatus());

            if (accessRight > 0) {

                AccessibilityEditable accEditable = applicationAccessibility.checkActionEditableCheck(action, grantedAuthorities, claim.getStatus());

                if (!AccessibilityHelper.getIsClaimEditable(accEditable, this.claim, getAuthenticatedUser().getUser())) {
                    action = EMPTY;
                }

                return action;

            }

        }

        return EMPTY;
    }

    public boolean getIsClaimNotificationEditable() {
        AccessibilityEditable accEditable = applicationAccessibility.checkNotificationEditableCheck("NotificationNotesNotification", getAuthenticatedUser().getAuthorities(), claim.getStatus());
        return AccessibilityHelper.getIsClaimEditable(accEditable, this.claim, getAuthenticatedUser().getUser());
    }

    public List getExtraActionList() {

        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        List<String> actions = AdditionalAction.getExtraActions();

        extraActionList = new ArrayList<LookupItem>();

        for (String action : actions) {

            short accessRight = applicationAccessibility.checkExtraActionAccessibility(action, grantedAuthorities, claim.getStatus());

            if (accessRight >= 1) {

                AccessibilityEditable accEditable = applicationAccessibility.checkExtraActionEditableCheck(action, grantedAuthorities, claim.getStatus());

                if (AccessibilityHelper.getIsClaimEditable(accEditable, this.claim, getAuthenticatedUser().getUser())) {
                    String extraActionDescription = AdditionalAction.getExtraActionName(action);
                    extraActionList.add(new LookupItem(action, extraActionDescription));
                }
            }
        }

        return extraActionList;
    }
}
