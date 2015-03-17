package idas.chox.web.actions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.opensymphony.xwork2.Action.SUCCESS;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import static idas.chox.core.model.PenaltyCharge.*;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.Notification;
import idas.chox.core.model.PenaltyCharge;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.Witness;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.PenaltyChargeService;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.DateHelper;
import idas.chox.data.notifications.NotificationType;
import idas.chox.service.claim.ClaimObjectService;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.service.security.ActionPanel;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.ExtraAction;
import idas.chox.service.security.NotificationAccessibility;
import idas.chox.service.security.TabAccessibility;
import idas.chox.service.workflow.activities.ActivityEvent;
import idas.chox.service.workflow.activities.ActivityEventGenerator;
import idas.chox.web.ListUtils;
import idas.chox.web.viewdata.HireMonitoringEcdViewData;

public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable {
    public static final String EMPTY = "empty";

    private static final Logger LOG = LoggerFactory.getLogger(ClaimAction.class);
    private ApplicationAccessibility applicationAccessibility;
    private TabAccessibility tabAccessibility;
    private NotificationAccessibility notificationAccessibility;
    private JSONArray jObject;
    private List vehicleClasses;
    private List<ReasonOfRejection> reasonOfClaimRejections;
    private List<ReasonOfRejection> reasonOfClaimRejectionsRestricted;
    private List<ReasonOfRejection> reasonOfInvoiceRejections;
    private List extraActionList;
    private List insurers;
    private List statuses;
    private List workgroups;
    private List insurerWorkgroups;
    private Claim claim;
    private int id = -1;
    private int claimVersion = -1;
    private int vehicleClassId = -1;
    private int insurerId = -1;
    private BigDecimal totalAmountToPayBeforeNewPenaltyCharge;
    private BigDecimal totalAmountToPayAfterNewPenaltyCharge;
    private String totalAmountToPayBeforeNewPenaltyChargeFormatted;
    private String totalAmountToPayAfterNewPenaltyChargeFormatted;
    private BigDecimal hirePenaltyChargeAmount;
    private BigDecimal repairPenaltyChargeAmount;
    private BigDecimal totalPenaltyChargeAmount;
    private Boolean isRemovePenaltyAlert;
    private long invoiceIntroducedDays;
    private Integer hireMonitoringDetailId;
    private Integer incidentId;
    private Integer thirdPartyId;
    private Integer customerId;
    private Integer invoideId;
    private Integer vehicleHireId;
    private Integer engineerReportId;
    private Integer witnessId;
    private Integer injuryId;
    private Integer notificationId;
    private int workgroupId = -1;
    private List<String> intelligentNotes;
    private List<String> intelligentNotes2;
    private IntelligentNoteDisplayEngine intelligentNoteDisplayEngine;
    private int claimOwnerId = -1;
    private int supplierClaimOwnerId = -1;
    private int escalateWorkgroupId = -1;
    private int oasWorkgroupId = -1;
    private int uosWorkgroupId = -1;
    private Integer reasonOfRejectionId;
    private LiabilityStatus fLiabilityStatus;
    private BigDecimal fPercentageLiabilityAccepted;
    private BigDecimal fPercentageLiabilityCho;
    private Date fLiabilityAgreedDate;
    private String fLiabilityNotes;
    private ClaimObjectService claimObjectService;
    private ClaimService claimService;
    private NotificationService notificationService;
    private LookupService lookupService;
    private WorkgroupService workgroupService;
    private BreBandService breBandService;
    private InsurerDiscountService insurerDiscountService;
    private UserService userService;
    private AuditTrailService auditTrailService;
    private ReasonOfRejectionService reasonOfRejectionService;
    private PenaltyChargeService penaltyChargeService;
    private String hirePenaltyPercentage;
    private String repairPenaltyPercentage;
    private BigDecimal interimPaymentMade;
    private BigDecimal interimPaymentReceived;
    private BigDecimal finalPayment;
    private int actionSelected;
    private String jsonData;
    private List<Insurer> mappedInsurers;
    private Date autoPenaltyStart;
    private Integer claimDays;
    private String statusMsg = null;
    private boolean showMessage = false;
    private boolean showErrorMessage = false;
    private boolean finalReviewRequired;
    private String finalReviewReason;
    private ActivityEventGenerator activityEventGenerator;
    private String customerClaimNumber;

    // <editor-fold defaultstate="collapsed" desc="Service Setters">
    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }
    

    public void setPenaltyChargeService(PenaltyChargeService penaltyChargeService) {
        this.penaltyChargeService = penaltyChargeService;
    }
    
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setReasonOfRejectionService(ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }

    public void setClaimObjectService(ClaimObjectService claimObjectService) {
        this.claimObjectService = claimObjectService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setActivityEventGenerator(ActivityEventGenerator activityEventGenerator) {
        this.activityEventGenerator = activityEventGenerator;
    }
    // </editor-fold>


    @Override
    public boolean isShowMessage() {
        return showMessage;
    }

    public boolean isShowErrorMessage() {
        return showErrorMessage;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        if (statusMsg != null && !statusMsg.isEmpty()) {
            this.statusMsg = statusMsg;
            if (statusMsg.contains("Error")) {
                showErrorMessage = true;
            } else {
                showMessage = true;
            }
        }
    }

    public String getFinalReviewReason() {
        return finalReviewReason;
    }

    public void setFinalReviewReason(String finalReviewReason) {
        this.finalReviewReason = finalReviewReason;
    }

    public Date getAutoPenaltyStart() {
        return autoPenaltyStart;
    }

    public void setAutoPenaltyStart(Date autoPenaltyStart) {
        this.autoPenaltyStart = autoPenaltyStart;
    }

    public boolean isStopAutoPenaltyCharge() {
        return !claim.isAutoPenaltyChargeEnabled();
    }

    public void setStopAutoPenaltyCharge(boolean stopAutoPenaltyCharge) {
        if (claim != null) {
            claim.setAutoPenaltyChargeEnabled(!stopAutoPenaltyCharge);
        } else {
            LOG.debug("Cannot set stopAutoPenaltyCharge to {} as no claim set.", stopAutoPenaltyCharge);
        }
    }

    public int getLiabilityStatusValue() {
        return this.claim.getLiabilityStatus().getLiablityValue();
    }

    public boolean getInvoiceDeleteWarning() {

        AuditTrail auditTrail;
        if ((auditTrail = auditTrailService.getLastChange(claim.getId())) != null && auditTrail.getOriginalStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {

            if (claim.getPreviousStatus() != null && !claim.getPreviousStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                LOG.warn("The 'previous_status' of claim '{}' [{}] does not match the previous status from the auditTrail [{}]",
                        new Object[]{claim.getChoReference(), claim.getPreviousStatus(), auditTrail.getOriginalStatus()});
            }
            return true;
        } else {
            return false;
        }
    }

    public String getPolicyNumber() {
        return StringEscapeUtils.escapeEcmaScript(claim.getThirdParty().getPolicyNumber());
    }

    public int getActivityMonitorRequestInterval() {
        return claimService.getActivityMonitorRequestInterval();
    }

    public boolean isEnableActivityMonitor() {
        return claimService.isEnableActivityMonitor();
    }

    public int getActionSelected() {
        return actionSelected;
    }

    public void setActionSelected(int actionSelected) {
        this.actionSelected = actionSelected;
    }

    public Map getLiabilityStatusDropDownMap() {
        if (claim.getLiabilityStatus() != LiabilityStatus.LIABILITY_NULL) {
            return claimObjectService.getLiabilityStatusMap(false);
        }
        return claimObjectService.getLiabilityStatusMap(true);
    }

    public String getHirePenaltyPercentage() {
        String invoicePenaltyPercentage = claim.getInvoice().getHirePenaltyPercentage();
        if (invoicePenaltyPercentage == null) {
            invoicePenaltyPercentage = "";
        }
        return invoicePenaltyPercentage;
    }

    public void setHirePenaltyPercentage(String hirePenaltyPercentage) {
        this.hirePenaltyPercentage = hirePenaltyPercentage;
    }

    public String getRepairPenaltyPercentage() {
        String invoicePenaltyPercentage = claim.getInvoice().getRepairPenaltyPercentage();
        if (invoicePenaltyPercentage == null) {
            invoicePenaltyPercentage = "";
        }
        return invoicePenaltyPercentage;
    }

    public void setRepairPenaltyPercentage(String repairPenaltyPercentage) {
        this.repairPenaltyPercentage = repairPenaltyPercentage;
    }

    public Date getfLiabilityAgreedDate() {
        return fLiabilityAgreedDate;
    }

    public void setfLiabilityAgreedDate(Date fLiabilityAgreedDate) {
        this.fLiabilityAgreedDate = fLiabilityAgreedDate;
    }

    public String getfLiabilityNotes() {
        return fLiabilityNotes;
    }

    public void setfLiabilityNotes(String fLiabilityNotes) {
        this.fLiabilityNotes = fLiabilityNotes;
    }

    public LiabilityStatus getfLiabilityStatus() {
        return fLiabilityStatus;
    }

    public void setfLiabilityStatus(LiabilityStatus fLiabilityStatus) {
        this.fLiabilityStatus = fLiabilityStatus;
    }

    public BigDecimal getfPercentageLiabilityAccepted() {
        return fPercentageLiabilityAccepted;
    }

    public void setfPercentageLiabilityAccepted(BigDecimal fPercentageLiabilityAccepted) {
        this.fPercentageLiabilityAccepted = fPercentageLiabilityAccepted;
    }

    public BigDecimal getfPercentageLiabilityCho() {
        return fPercentageLiabilityCho;
    }

    public void setfPercentageLiabilityCho(BigDecimal fPercentageLiabilityCho) {
        this.fPercentageLiabilityCho = fPercentageLiabilityCho;
    }

    @Override
    public boolean getInsurerIsWorkgroupEnabled() {
        return claim.getInsurer().isWorkgroupEnable();
    }

    @Override
    public boolean getInsurerIsClaimOwnershipEnabled() {
        return claim.getInsurer().isClaimOwnershipEnable();
    }

    @Override
    public boolean getChoIsClaimOwnershipEnabled() {
        return claim.getChorganisation().isClaimOwnershipEnable();
    }

    @Override
    public boolean getInsurerIsFnolEnabled() {
        return claim.getInsurer().isFnolEnable();
    }

    @Override
    public boolean getInsurerIsEngineersEnabled() {
        return claim.getInsurer().isEngineersEnable();
    }

    public boolean isUpdatedByEng() {
        return ClaimStatus.CLAIM_UPDATE_BY_ENG.equals(claim.getStatus());
    }

    public boolean isInsurerClaim() {
        return ClaimType.isInsurerUpload(claim.getClaimType());
    }

    /*
     * newComment functionality for Phase 7 Sprint 1:
     *   7.1.4 Updates to Subscriber Process Model
     * Check for the following condition being satisfied:
     *      1.  When a Subscriber claim is in the status 'ClaimUnacknowledgedRouted' or 'ClaimUpdatedByEngineer'
     *          and the 'Acknowledge' button is being clicked, then the only valid selection in the
     *          'Liability Status' drop down menu  is 'Full Liability Accepted'.
     */
    public boolean isFullLiability() {
        boolean result = false;
        if (ClaimType.isSubscriber(claim.getClaimType())
                &&  (ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_UPDATE_BY_ENG.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_PENDING.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_REJECTION_CONTESTED.equals(claim.getStatus()))) {
                result = true;
        }
   
        return result;
    }

    public boolean isFullLiabilityOrSplit() {
        boolean result = false;
        if (ClaimType.isFixedFee(claim.getClaimType())
                &&  (ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_UPDATE_BY_ENG.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_PENDING.equals(claim.getStatus())
                        || ClaimStatus.CLAIM_REJECTION_CONTESTED.equals(claim.getStatus()))) {
                result = true;
        }
   
        return result;
    }

    public boolean isInsurerIsDisablePrivateNotes() {
        return claim.getInsurer().isDisablePrivateNotes();
    }

    public boolean isChoIsDisablePrivateNotes() {
        return claim.getChorganisation().isDisablePrivateNotes();
    }

    public BigDecimal getInterimPaymentMade() {
        return claim.getInvoice().getInterimPaymentMade() == null ? BigDecimal.ZERO.setScale(2) : claim.getInvoice().getInterimPaymentMade();
    }

    public void setInterimPaymentMade(BigDecimal interimPaymentMade) {
        this.interimPaymentMade = interimPaymentMade;
    }

    public BigDecimal getInterimPaymentReceived() {
        return interimPaymentReceived == null ? BigDecimal.ZERO.setScale(2) : interimPaymentReceived;
    }

    public void setInterimPaymentReceived(BigDecimal interimPaymentReceived) {
        this.interimPaymentReceived = interimPaymentReceived;
    }

    @Override
    public void prepare() throws Exception {
        if (id < 0) { // No Claim provided so use session
            if (getModelIdFromSession(Claim.class) != null) {
                claim = claimService.getClaim(getModelIdFromSession(Claim.class));
                id = claim.getId();
                LOG.debug("No claim id provided - retrieved from session: {}", id);
            } else if (LOG.isDebugEnabled()) {
                LOG.debug("No claim id provided and no claim in session.");
            }
        } else {
            claim = claimService.getClaim(id);
        }
        if (claim == null) {
            LOG.error("An attempt to retrieve claim by id failed due to invalid id provided: {}", id);
            throw new Exception("An attempt to retrieve claim by id failed due to invalid id provided.");
        }
        LOG.trace("Claim retrieved in prepare() with id={}", id);
        addModelToSession(Arrays.asList(claim));
    }

    @Override
    public String execute() throws Exception {
        updateModelInSession(Arrays.asList(claim));
        // If this action is called by the redirectAction from another action class then perform the below update. 
        if (getSession().containsKey("redirect") && getSession().get("redirect") != null) {
            HashMap<String, String> map = (HashMap) getSession().get("redirect");
            if (map.containsKey("redirectStatusMsg")) {
                setStatusMsg(map.get("redirectStatusMsg"));
            } else if (map.containsKey("redirectErrorMsg")) {
                setActionError(map.get("redirectErrorMsg"));
            }
            // Once the status message value is updated, clear it from the session.
            removeRedirectionParamInSession();
        }
        return SUCCESS;
    }
    
    public String openClaim() {
        return SUCCESS;
    }

    // <editor-fold defaultstate="collapsed" desc="CLAIM PANEL ACTION">
    public String updateClaimDetail() {
        this.claimService.updateClaim(claim);
        setActionResult("Claim Updated!");
        return SUCCESS;
    }

    public String getPaymentReceivedAction() {
        return "updatePaymentReceived";
    }

    public String getUpdatePenaltyCharges() {
        LOG.debug("Setting properties for penalty charge panel....");
        getAlertPanel();
        return SUCCESS;
    }

    public String getUpdatePaymentReceived() {
        return SUCCESS;
    }

    public String validateHireMonitoringECDDetail() {

        if (this.claim.getCustomer() == null || this.claim.getCustomer().getInitialECD() == null) {
            if (this.claimService.getECDCountByClaimId(this.claim.getId()) == 0) {
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

    public String updateClaimNumber() {
        try {
            claim.setClaimNumber(claim.getClaimNumber().trim());
            this.claimService.updateClaim(claim);
            activityEventGenerator.generate(claim, ActivityEvent.CLAIM_NUMBER_ASSIGNED_EVENT);
        } catch (Exception ex) {
            LOG.error("Exception thrown updating the claim number for claim '{}': ", claim.getChoReference(), ex);
            setActionError("An internal error occurred updating the claim number. Please contact CHOX support.");
            updateRedirectionParamInSession();
            return ERROR;
        }

        updateRedirectionParamInSession();
        return SUCCESS;
    }

    public String updateCustomerClaimNumber() {
        try {
            LOG.info("New customer claim number is: {}", customerClaimNumber);
            LOG.info("Old customer claim number is: {}", claim.getCustomer().getClaimReference());
            claim.getCustomer().setClaimReference(customerClaimNumber.trim());
            this.claimService.updateClaim(claim);
            activityEventGenerator.generate(claim, ActivityEvent.CLAIM_CUSTOMER_NUMBER_ASSIGNED_EVENT);
        } catch (Exception ex) {
            LOG.error("Exception thrown updating the customer claim number for claim '{}': ", claim.getChoReference(), ex);
            setActionError("An internal error occurred updating the customer claim number. Please contact CHOX support.");
            updateRedirectionParamInSession();
            return ERROR;
        }

        updateRedirectionParamInSession();
        return SUCCESS;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public String updateInvoiceReviewRequired() {
        try {
            checkVersion(Arrays.asList(claim));
            this.claimService.updateClaim(claim);
        } catch (Exception ex) {
            LOG.error("Exception thrown updating the Invoice Review Required for claim '{}': ", claim.getChoReference(), ex);
            claim = claimService.updateClaimWithInvalidSessionVersion(claim);
            setActionError(ex.getMessage());
            updateRedirectionParamInSession();
            return ERROR;
        }
        updateRedirectionParamInSession();
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

        Invoice invoice = claim.getInvoice();
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);
        setInvoiceIntroducedDays(invoice.getInvoicedDays());
        setTotalAmountToPayBeforeNewPenaltyCharge(invoice.getFullTotalToPay().subtract(invoice.getHirePenaltyCharge()).subtract(invoice.getRepairPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyCharge(invoice.getFullTotalToPay());
        setTotalAmountToPayBeforeNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayBeforeNewPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayAfterNewPenaltyCharge()));
        setHirePenaltyChargeAmount(invoice.getHirePenaltyCharge());
        setRepairPenaltyChargeAmount(invoice.getRepairPenaltyCharge());
        setTotalPenaltyChargeAmount(invoice.getTotalPenaltyCharge());
        setInterimPaymentMade(invoice.getInterimPaymentMade());
        setIsRemovePenaltyAlert((Boolean) false);

        return "penaltyChargeApplied";
    }

    public String doApplyPenaltyCharge() {

        Map resultMap = penaltyChargeService.applyPenaltyCharge(claim, isRemovePenaltyAlert, hirePenaltyChargeAmount,
                hirePenaltyPercentage, repairPenaltyChargeAmount, repairPenaltyPercentage);

        if (resultMap.containsKey("error")) {
            setActionError((String) resultMap.get("error"));
            return ERROR;
        } else {
            return SUCCESS;
        }
    }


    public int getClaimNumberDuplications() {
        int duplications = 0;

        if (!claim.getClaimNumber().isEmpty()) {
            duplications = claimService.getClaimCountByClaimNumber(claim.getClaimNumber(), claim.getId());
        }

        return duplications;
    }

    public boolean getIsCustomerClaimNumberDuplicated() {
        boolean bFlag = false;

        if (!customerClaimNumber.isEmpty()) {
            if (claimService.isCustomerClaimNumberExist(customerClaimNumber, claim.getId(), Boolean.TRUE)) {
                bFlag = true;
            }
        }

        return bFlag;
    }

    public boolean getIsDuplicatedSupplementaryInvoiceExists() {
        boolean bFlag = false;

        if (!claim.getCustomer().getClaimReference().isEmpty() && ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
            if (claimService.getDuplicateSupplementaryInvoiceClaims(claim.getCustomer().getClaimReference(), claim.getId()).size() > 0) {
                bFlag = true;
            }
        }

        return bFlag;
    }

    public boolean getHasOutstandingInterimPayment() {
        boolean bFlag = false;

        if ((getIsCHO() || getIsChoxAdmin()) && claim.getInvoice() != null && claim.getInvoice().isInterimPaymentOutstanding()) {
            bFlag = true;
        }

        return bFlag;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MORE ACTION - DROP DOWN">
    public String getUpdateInsurerClaimNumber() {
        return SUCCESS;
    }

    public String getUpdateCustomerClaimNumber() {
        return SUCCESS;
    }

    public String getInvoiceReviewRequiredPanel() {
        return SUCCESS;
    }

    public String getFinalReviewPanel() {
        return SUCCESS;
    }

    public String getUpdateLiability() {
        LOG.debug("Id " + id + " " + claim.getChoReference());
        if (claim != null) {
            fLiabilityAgreedDate = claim.getLiabilityAgreedDate();
            fLiabilityStatus = claim.getLiabilityStatus();
            fPercentageLiabilityAccepted = claim.getPercentageLiabilityAccepted();
            fPercentageLiabilityCho = claim.getPercentageLiabilityCho();
            LOG.debug("fLiabilityAgreedDate : " + fLiabilityAgreedDate);
            LOG.debug("fLiabilityStatus : " + fLiabilityStatus.toString());
            LOG.debug("fPercentageLiabilityAccepted : " + fPercentageLiabilityAccepted);
            LOG.debug("fPercentageLiabilityCho : " + fPercentageLiabilityCho);
        }
        return SUCCESS;
    }

    public String getUpdateClaimWorkgroupAndOwner() {
        return SUCCESS;
    }

    public String getUpdateClaimWorkgroup() {
        return SUCCESS;
    }

    public String getUpdateManualInvWorkgroupClaimOwner() {
        return SUCCESS;
    }

    public String getEscalateUnassignedClaim() {
        return SUCCESS;
    }

    public String getUpdateClaimSupplierOwner() {
        return SUCCESS;
    }

    public String getUpdateInterimPayment() {
        if (claim != null && claim.getInvoice() != null) {
            interimPaymentMade = claim.getInvoice().getInterimPaymentMade();
            interimPaymentReceived = claim.getInvoice().getInterimPaymentReceived();
        } else {
            interimPaymentMade = null;
            interimPaymentReceived = null;
        }
        return SUCCESS;
    }

//    @Secured({"ROLE_CHOX_ADMIN", "ROLE_CHO"})
    public String updateClaimSupplierOwner() {
        LOG.debug("Updating supplier claim owner to: {}", supplierClaimOwnerId);
        if (this.supplierClaimOwnerId > 0) {
            try {
                WebUser newClaimOwner = userService.getWebUser(supplierClaimOwnerId);
                // Check user belongs to the CHO
                if (newClaimOwner.getChorganisation().getId().intValue() != claim.getChorganisation().getId()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
                }
                // Check user belongs to the CHO
                if (newClaimOwner.getChorganisation().getId() != claim.getChorganisation().getId().intValue()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
                }

                Comment comment;

                // SET COMMENT
                if (claim.getSupplierClaimOwner() != null) {
                    String oldOwnerName = claim.getSupplierClaimOwner().getFullName();

                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        comment = Comment.newComment(0, "Supplier Claim Owner changed from '" + oldOwnerName
                                + "' to '" + newClaimOwner.getFullName()
                                + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                    } else {
                        comment = Comment.newComment(0, "Supplier Claim Owner changed from '" + oldOwnerName
                                + "' to '" + newClaimOwner.getFullName() + "'");
                    }
                } else if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                    comment = Comment.newComment(0, "Supplier Claim Owner is '" + newClaimOwner.getFullName()
                            + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                } else {
                    comment = Comment.newComment(0, "Supplier Claim Owner is '" + newClaimOwner.getFullName() + "'");
                }
                claim.addComment(comment);
                claim.setSupplierClaimOwner(newClaimOwner);
                this.claimService.updateClaim(claim);

            } catch (Exception ex) {
                LOG.error("Error updating supplier claim owner for claim {}: ", claim.getChoReference(), ex);
                handleException(ex);
                updateRedirectionParamInSession();
                return ERROR;
            }
        } else {
            LOG.error("Error: no supplierClaimOwnerId supplied to update claim {}: {}", claim.getChoReference(), supplierClaimOwnerId);
            setActionError("No supplier claim owner selected.");
            getActionResponse().AddError("No supplier claim owner selected.");
            updateRedirectionParamInSession();
            return ERROR;
        }

        updateRedirectionParamInSession();
        return SUCCESS;
    }

    public String updateFinalReview() {
        // Only update if flag value has changed
        if ((getAuthenticatedUser().isCHO() && finalReviewRequired != claim.isFinalReviewCho())
                || (getAuthenticatedUser().isAnInsurer() && finalReviewRequired != claim.isFinalReviewIns())) {
            
        try {
            if (finalReviewRequired && (finalReviewReason == null || finalReviewReason.equals("-1"))) {
                LOG.error("No final review reason given: {}", finalReviewReason);
                throw new Exception("No Final Review Reason Specified");
            }
            if (getAuthenticatedUser().isCHO()) {
                claim.setFinalReviewCho(finalReviewRequired);
                if (finalReviewRequired) {
                    claim.addComment(Comment.newComment(2, "Final Review Reason: " + finalReviewReason));
                    claim.setFinalReviewByCho(getAuthenticatedUser());
                    claim.setFinalReviewDateCho(new Date());
                } else {
                    claim.setFinalReviewByCho(null);
                    claim.setFinalReviewDateCho(null);
                }
            } else if (getAuthenticatedUser().isAnInsurer()) {
                claim.setFinalReviewIns(finalReviewRequired);
                if (finalReviewRequired) {
                    claim.addComment(Comment.newComment(1, "Final Review Reason: " + finalReviewReason));
                    claim.setFinalReviewByIns(getAuthenticatedUser());
                    claim.setFinalReviewDateIns(new Date());
                } else {
                    claim.setFinalReviewByIns(null);
                    claim.setFinalReviewDateIns(null);
                }
            } else {
                LOG.error("Non insurer/cho marking claim {} for final review?", claim.getChoReference());
                return ERROR;
            }
            claimService.updateClaim(claim);
        } catch (Exception ex) {
            LOG.error("Error marking claim {} for final review:", claim.getChoReference(), ex);
            handleException(ex);
            updateRedirectionParamInSession();
            return ERROR;
        }
        }
        updateRedirectionParamInSession();
        return SUCCESS;
    }

//    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS"})
    public String updateClaimWorkgroupAndOwner() {
        String oldOwnerName = "N/A";

        if (claim.getInsurer().isWorkgroupEnable()) {

            if (this.claimOwnerId > 0 && this.uosWorkgroupId > 0) {

                try {

                    WebUser newClaimOwner = userService.getWebUser(claimOwnerId);

                    // SET COMMENT
                    if (claim.getClaimOwner() != null) {
                        oldOwnerName = claim.getClaimOwner().getFullName();
                    }
                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        Comment comment = Comment.newComment(0, "Insurer Claims Handler changed from '" + oldOwnerName + "' to '" + newClaimOwner.getFullName() + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                        claim.addComment(comment);
                    } else {
                        Comment comment = Comment.newComment(0, "Insurer Claims Handler changed from '" + oldOwnerName + "' to '" + newClaimOwner.getFullName() + "'");
                        claim.addComment(comment);
                    }
                    Workgroup workgroup = workgroupService.getWorkgroup(uosWorkgroupId);

                    // Check workgroup belongs to the Insurer
                    if (workgroup.getInsurer().getId() != claim.getInsurer().getId().intValue()) {
                        throw new AccessDeniedException("Workgroup does not belong to Insurer");
                    }
                    // Check user belongs to the Insurer
                    if (newClaimOwner.getInsurer().getId() != claim.getInsurer().getId().intValue()) {
                        throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
                    }

                    claim.setClaimOwner(newClaimOwner);
                    claim.setWorkgroup(workgroup);
                    if (claim.getBreBand() == null) {
                        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                        claim.setBreBand(choBand);
                    }
                    if (claim.getInvoice() != null && claim.getBreBand().isPaymentTeamActive() && !claim.getWorkgroup().isStpExcluded()
                            && ((ClaimType.isGTA(claim.getClaimType()) && claim.getInsurer().isGtaPaymentsTeamEnable())
                            || (ClaimType.isSubscriber(claim.getClaimType()) && claim.getInsurer().isSubscriberPaymentsTeamEnable())
                            || (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isInsurerManualPaymentsTeamEnable())
                            || (ClaimType.isInsurerVsInsurer(claim.getClaimType()) && claim.getInsurer().isInsurerVsInsurerPaymentsTeamEnable())
                            || (ClaimType.isFixedFee(claim.getClaimType()) && claim.getInsurer().isFixedFeePaymentsTeamEnable())
                            || (ClaimType.isCollaborationProtocol(claim.getClaimType()) && claim.getInsurer().isCollaborationPaymentsTeamEnable()))) {
                        claim.getInvoice().setPaymentTeam(true);
                    } else if (claim.getInvoice() != null && claim.getInvoice().isPaymentTeam()) {
                        claim.getInvoice().setPaymentTeam(false);
                    }
                    this.claimService.updateClaim(claim);

                } catch (Exception ex) {
                    LOG.error("Error updating claim workgroup and owner for claim {}: {}", claim.getChoReference(), ex.getMessage());
                    handleException(ex);
                    updateRedirectionParamInSession();
                    return ERROR;
                }
            } else {
                LOG.error("Cannot update workgroup and owner of claim {}: claimOwner={}, workgroup={}", new Object[]{claim.getChoReference(), claimOwnerId, uosWorkgroupId});
                return ERROR;
            }

            updateRedirectionParamInSession();
            return SUCCESS;
        } else if (claim.getInsurer().isClaimOwnershipEnable()) {
            if (this.claimOwnerId > 0) {

                try {

                    WebUser newClaimOwner = userService.getWebUser(claimOwnerId);

                    // SET COMMENT
                    if (claim.getClaimOwner() != null) {
                        oldOwnerName = claim.getClaimOwner().getFullName();
                    }
                    Comment comment = Comment.newComment(0, "Claim owner changed from '" + oldOwnerName + "' to '" + newClaimOwner.getFullName() + "'");
                    claim.addComment(comment);
                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        Comment comment2 = Comment.newComment(0, "Insurer Claims Handler is '" + newClaimOwner.getFullName() + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                        claim.addComment(comment2);
                    }

                    // Check user belongs to the Insurer
                    if (newClaimOwner.getInsurer().getId() != claim.getInsurer().getId().intValue()) {
                        throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
                    }

                    claim.setClaimOwner(newClaimOwner);
                    this.claimService.updateClaim(claim);

                } catch (Exception ex) {
                    LOG.error("Error updating claim workgroup and owner for claim {}: {}", claim.getChoReference(), ex.getMessage());
                    handleException(ex);
                    updateRedirectionParamInSession();
                    return ERROR;
                }

                updateRedirectionParamInSession();
                return SUCCESS;

            } else {
                LOG.error("Cannot update owner of claim {} to claimOwner={}", claim.getChoReference(), claimOwnerId);
                return ERROR;
            }
        } else {
            LOG.debug("Error updating claim workgroup and owner for claim {} as workgroup and claim ownership is not enabled", claim.getChoReference());
            return ERROR;
        }
    }

    public String updateSaveLiabilityStatus() {
        LOG.debug("updateSaveLiabilityStatus");
        String note;
        if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL) {
            note = "Liability status changed to '" + fLiabilityStatus + "'";
        } else {
            note = "Liability status changed from '" + claim.getLiabilityStatus() + "' to '" + fLiabilityStatus + "'";
        }
        LOG.debug("note : " + note);
        try {
            if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL || !claim.getLiabilityStatus().equals(fLiabilityStatus)) {
                if (fPercentageLiabilityAccepted != null && fPercentageLiabilityCho != null
                        && !fPercentageLiabilityCho.add(fPercentageLiabilityAccepted).equals(new BigDecimal(100.0))) {
                    LOG.error("Liability not 100%: ins={}, cho={}", fPercentageLiabilityAccepted, fPercentageLiabilityCho);
                    throw new Exception("Total liability is not 100%");
                }

                Comment comment = Comment.newComment(0, note);
                comment.setClaim(claim);
                claim.addComment(comment);
                claim.setLiabilityPercentages(fPercentageLiabilityAccepted, fPercentageLiabilityCho);
                claim.setLiabilityAgreedDate(fLiabilityAgreedDate);
                claimService.setLiability(claim, fLiabilityStatus);
                claimService.save(claim);

            }

        } catch (Exception ex) {
            LOG.error("Error updating liability status for claim {}: ", claim.getChoReference(), ex);
            handleException(ex);
            updateRedirectionParamInSession();
            return ERROR;
        }

        updateRedirectionParamInSession();
        return SUCCESS;
    }

    public String escalatedUnassignedClaim() {
        try {

            if (escalateWorkgroupId > 0) {
                Workgroup workgroup = workgroupService.getWorkgroup(escalateWorkgroupId);
                // Check workgroup belongs to the Insurer
                if (workgroup.getInsurer().getId() != claim.getInsurer().getId().intValue()) {
                    throw new AccessDeniedException("Workgroup does not belong to Insurer");
                }
                claim.setWorkgroup(workgroup);
                claim.setClaimOwner(null);
                this.claimService.updateClaim(claim);
            }

        } catch (Exception ex) {
            LOG.error("Error escalating unassigned claim for claim {}: {}", claim.getChoReference(), ex.getMessage());
            handleException(ex);
            updateRedirectionParamInSession();
            return ERROR;
        }

        updateRedirectionParamInSession();
        return SUCCESS;
    }

    public String markSupplementaryInvoicedClaim() {
        boolean canMark = true;
        if (!ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
            List<Claim> claims = claimService.getCHOClaimsByCustomerClaimRef(claim.getCustomer().getClaimReference(), claim.getChorganisation().getId());
            if (claims.size() > 1) {
                for (Claim claim1 : claims) {
                    if (ClaimType.isSupplementaryInvoice(claim1.getClaimType())
                            && ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType())) {
                        canMark = false;
                    }
                }
                if (canMark) {
                    if (claim.getClaimType() == ClaimType.GTA || claim.getClaimType() == ClaimType.GTA_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.GTA_ORIGINAL_INVOICE);
                    } else if (claim.getClaimType() == ClaimType.INSURER_VS_INSURER || claim.getClaimType() == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE);
                    } else if (claim.getClaimType() == ClaimType.SUBSCRIBER || claim.getClaimType() == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.SUBSCRIBER_ORIGINAL_INVOICE);
                    } else if (claim.getClaimType() == ClaimType.FIXED_FEE || claim.getClaimType() == ClaimType.FIXED_FEE_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.FIXED_FEE_ORIGINAL_INVOICE);
                    } else if (claim.getClaimType() == ClaimType.INSURER_CLAIM || claim.getClaimType() == ClaimType.INSURER_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.INSURER_ORIGINAL_INVOICE);
                    } else if (claim.getClaimType() == ClaimType.COLLABORATION_PROTOCOL || claim.getClaimType() == ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE);
                    } else {
                        LOG.error("Error determining type for cloned claim '{}': {}", claim.getChoReference(), claim.getClaimType());
                    }

                    this.claimService.updateClaim(claim);
                }
            } else {
                return ERROR;
            }
            return SUCCESS;
        } else {
            return ERROR;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACCESSIBILITY CONTROL">
    public boolean getIsShowPenaltyChargeAlert() {
        return penaltyChargeService.canShowPenaltyChargeAlert(claim, getIsCHO());
    }

    public TabAccessibility getTabAccessibility() {

        if (tabAccessibility == null) {
            tabAccessibility = new TabAccessibility(applicationAccessibility, getAuthenticatedUser(), claim);
        }
        return tabAccessibility;
    }


    public NotificationAccessibility getNotificationAccessibility() {

        if (notificationAccessibility == null) {
            notificationAccessibility = new NotificationAccessibility(applicationAccessibility, getAuthenticatedUser(), claim);
        }
        LOG.debug("Notification accessibility check: " + notificationAccessibility.getNotificationNotesNotificationAccessibility());
        return notificationAccessibility;
    }

    public boolean getCanCloseClaim() {
        return applicationAccessibility.checkActivityAccessibility(ApplicationAccessibility.CLOSE_CLAIM,
                                    getAuthenticatedUser(), claim)>0;
    }

    public boolean getCanReopenClaim() {
        boolean access = applicationAccessibility.checkActivityAccessibility(
                                    ApplicationAccessibility.REOPEN_CLAIM,
                                    getAuthenticatedUser(), claim)>0;
        if (access && getAuthenticatedUser().isAnInsurer() && !ClaimType.isInsurerUpload(claim.getClaimType())) {
            access = false;
        }
        return access;
    }

    public boolean getCanRevertClaimStatus() {
        boolean access = applicationAccessibility.checkActivityAccessibility(
                                    ApplicationAccessibility.REVERT_CLAIM,
                                    getAuthenticatedUser(), claim)>0;
        if (access) {
            // this fix is for bug 2208 disable revert function when there is no previous status.
            AuditTrail auditTrail = auditTrailService.getLastChange(claim.getId());
            if (auditTrail == null || auditTrail.getOriginalStatus() == null
                    || auditTrail.getOriginalStatus().isEmpty()) {
                access = false;
            }
            // disable revert if CHO and status is 'InvoicePaymentLogged'
            //      - CHO has access to this activity as this is needed for when marking
            //        as 'Payment Received But Not Full Amount'
            if (claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && this.getIsCHO()) {
                access = false;
            }
        }
        return access;
    }

    public boolean getShowPayNotReceivedButton() {
        return applicationAccessibility.checkActivityAccessibility(
                                     ApplicationAccessibility.PAYMENT_NOT_RECEIVED,
                                     getAuthenticatedUser(), claim) > 0;
    }

    public boolean getCanShowSwitchClaimButton() {
        boolean canShowButton = false;
        // Only check accessibility if insurer of claim has a 'related' insurer
        if (claim.getInsurer().getRelatedInsurer() != null) {
            canShowButton =  applicationAccessibility.checkActivityAccessibility(ApplicationAccessibility.SWITCH_CLAIM,
                    getAuthenticatedUser(), claim) > 0;
        }
        return canShowButton;
    }


    public boolean getCanShowSwitchClaimToMultipleInsButton() {

        return applicationAccessibility.checkActivityAccessibility(ApplicationAccessibility.SWITCH_CLAIM_MULTIPLE_INS,
                getAuthenticatedUser(), claim) > 0;
    }

    public boolean getCanShowSwitchChoButton() {
        boolean canShowButton = false;

        // Only check accessibility if CHO of claim has a 'linked' CHO
        if (claim.getChorganisation().getLinkedCho() != null) {
            canShowButton = applicationAccessibility.checkActivityAccessibility(ApplicationAccessibility.SWITCH_CLAIM_CHO,
                getAuthenticatedUser(), claim) > 0;
        }
        
        return canShowButton;
   }


    public boolean getIsFnolPanelVisible() {
        boolean visible = false;
        
        if (claim.isIsFnolReviewed()) {
            visible = applicationAccessibility.checkPanelAccessibility(ApplicationAccessibility.PANEL_FNOL_REVIEWED,
                        getAuthenticatedUser(), claim) > 0;
        }
        return visible;
    }


    public String getActionPanel() {

        List<String> actions = ActionPanel.getPanelActions();

        for (String action : actions) {
            short accessRight = applicationAccessibility.checkActivityAccessibilityForActionPanel(action,
                    getAuthenticatedUser(), claim);
            LOG.debug("Access for {} os {}", action, accessRight);
            // If Invoice is with Payments Team then do not show InvoicePaymentLogged for PC
            if (accessRight > 0 && ("InvoicePaymentLogged".equals(action) || "UpdateManualInvoicePaid".equals(action))
                    && claim.getInsurer().isPaymentsTeamEnable() 
                    && (claim.getInvoice() == null || claim.getInvoice().isPaymentTeam())
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_MNG)
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_PC)
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CHOX_ADMIN)) {
                // Do not show if no PC or CHOX Admin role
                accessRight = 0;
            } else if (accessRight > 0 && ("InvoicePaymentLogged".equals(action) || "UpdateManualInvoicePaid".equals(action))
                    && claim.getInsurer().isPaymentsTeamEnable()
                    && (claim.getInvoice() == null || !claim.getInvoice().isPaymentTeam())
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_CH)
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_MNG)
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_MI)
                    && !getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CHOX_ADMIN)) {
                // Do not show if no CH, MNG, MI or CHOX Admin role
                accessRight = 0;
            }
            if (accessRight >= 2) {
                LOG.debug("Returning action: {}", action);
                return action;
            }
        }

        return EMPTY;
    }

    public boolean getIsClaimNotificationEditable() {
        return applicationAccessibility.checkNotificationAccessibilityEditable("NotificationNotesNotification",
                getAuthenticatedUser(), claim) >= 2;
    }

    public List getExtraActionList() {

        List<String> actions = ExtraAction.getExtraActions();
        extraActionList = new ArrayList<>();
        for (String actionName : actions) {
            String extraActionDescription;
            LOG.debug("Checking More Action Accessibility for action '{}' and claim status '{}'", actionName, claim.getStatus());
            short accessRight = applicationAccessibility.checkExtraActionAccessibilityEditable(actionName,
                    getAuthenticatedUser(), claim);
            /*
             * If any of this condition !(insurerWorkgroupEnabled or
             * insurerClaimOwnershipEnabled) or !(manualInvoiceWorkgroupEnabled
             * or manualInvoiceClaimOwnershipEnabled) is true then disable the  1QQ
             * manual invoice extra action. And this condiont is equal to
             * (insurerWorkgroupDisabled and insurerClaimOwnershipDisabled) or
             * (manualInvoiceWorkgroupDisabled and
             * manualInvoiceClaimOwnershipDisabled).
             *
             */
            if (accessRight > 0) {
                if (actionName.equals(ExtraAction.ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER)
                        && (!(claim.getInsurer().isEnableManualInvoiceWorkgroups() || claim.getInsurer().isEnableManualInvoiceOwnership()) 
                        || (!(claim.getInsurer().isWorkgroupEnable() || claim.getInsurer().isClaimOwnershipEnable())))) {
                    accessRight = 0;
                }
            
                // Remove 'Update Claim Owner' and 'Update Workgroup' if both workgroups and Ownership activated
                else if (actionName.equals(ExtraAction.UPDATE_CLAIM_WORKGROUP)
                        && claim.getInsurer().isClaimOwnershipEnable()) {
                    accessRight = 0;
                }
            
                else if (actionName.equals(ExtraAction.UPDATE_INSURER_CLAIM_OWNER)
                        && claim.getInsurer().isWorkgroupEnable()) {
                    accessRight = 0;
                }
            
                // Remove 'Mark Claim For Supplementary Invoice(s)' for Insure (Manual) invoices (bug#2586)
                 // The following should really be done by updating the accessibility tables....
                else if (actionName.equals(ExtraAction.MARK_SUPPLEMENTARY_INVOICED_CLAIM)
                        && claim.getClaimType() == ClaimType.INSURER_INVOICE) {
                    accessRight = 0;
                }
                 // The following should really be done by updating the accessibility tables....
                 else if ((actionName.equals(ExtraAction.MAKE_INTERIM_PAYMENT) || actionName.equals(ExtraAction.FINAL_REVIEW))
                         && ClaimType.isInsurerUpload(claim.getClaimType())) {
                     accessRight = 0;
                 }

                // bug#2719 - disable update of workgrouup/owner if not already routed/assigned
                else if (actionName.equals(ExtraAction.UPDATE_CLAIM_WORKGROUP_AND_OWNER)
                        && (claim.getWorkgroup() == null || claim.getClaimOwner() == null)) {
                    accessRight = 0;
                }
            }
            
            if (accessRight >= 2) {
                switch (actionName) {
                    case ExtraAction.UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL:
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
                            b = false;
                        }
                        if (!b) {
                            accessRight = 0;
                        }
                        break;
                    case ExtraAction.UPDATE_PENALTY_CHARGES:
                        // Check invoice was uploaded at least 30 days ago
                        Invoice invoice = claim.getInvoice();
                        if (invoice != null) {
                            int days = invoice.getInvoicedDays();
                            // Penalty charge has been applied to this claim but penalty charge(for this claim type) switched off in the bre band later.
                            boolean pcExistsBeforeSwithedOffInBreBand = false;
                            
                            // Check Penalty Charges disallowed and no current charges
                            if (accessRight != 0) {
                                if (claim.getBreBand() == null) {
                                    BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                                    claim.setBreBand(choBand);
                                }
                                
                                if (!claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType()) && !(invoice.getTotalPenaltyCharge() == null || invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) == 0)) {
                                    pcExistsBeforeSwithedOffInBreBand = true;
                                }
                                
                                if (!claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType()) && (invoice.getTotalPenaltyCharge() == null || invoice.getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) == 0)) {
                                    accessRight = 0;
                                }
                            }
                            
                            /*
                            * For manual invoices always show 'Adjust Penalty
                            * Charges' more action.
                            *  "7.1.2 Insurer Manual Invoice Process Updates" says -
                            * the age of the invoice does
                            * not have to be over say 30 days in order to be able
                            * to apply the penalty charges
                            */
                            if (accessRight > 0 && !pcExistsBeforeSwithedOffInBreBand && days <= penaltyChargeService.getFirstPenaltyBand(claim) && !ClaimType.isInsurerUpload(claim.getClaimType())) {
                                accessRight = 0;
                            }
                            // Check the 'Adjust Penalty Charges' Panel is not already displayed and not insurer upload claim.
                            else if (accessRight > 0 && !pcExistsBeforeSwithedOffInBreBand && invoice.getPenaltyBand() > -1 && !ClaimType.isInsurerUpload(claim.getClaimType())) { // Check if not removed from penalty queue
                                if ((!claim.getChorganisation().isAutoPenaltyChargeEnabled()
                                        || (claim.getChorganisation().isAutoPenaltyChargeEnabled()
                                        && (!claim.isAutoPenaltyChargeEnabled()
                                        || penaltyChargeService.calculateCurrentPenaltyBand(claim) >= penaltyChargeService.getLastPenaltyBand(claim))))
                                        && days > invoice.getPenaltyBand()) {
                                    accessRight = 0;
                                }
                            }
                            
                            
                        } else {
                            // No invoice!
                            accessRight = 0;
                        }
                        break;
                    case ExtraAction.PENALTY_CHARGE_CONFIGURATION:
                        if (claim.getInvoice() != null) {
                            if (claim.getBreBand() == null) {
                                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                                claim.setBreBand(choBand);
                            }
                            
                            if (!claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType())) {
                                accessRight = 0;
                            }
                        } else { // No invoice! or wrong claim type
                            accessRight = 0;
                        }
                        break;
                    case ExtraAction.MARK_SUPPLEMENTARY_INVOICED_CLAIM:
                        String customerClaimRef = claim.getCustomer().getClaimReference();
                        if (customerClaimRef != null && !customerClaimRef.isEmpty() && !customerClaimRef.equalsIgnoreCase("N/A") && !customerClaimRef.equalsIgnoreCase("NA")) {
                            List<Claim> claims = claimService.getCHOClaimsByCustomerClaimRef(customerClaimRef, claim.getChorganisation().getId());
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
                        break;
                }
            }
            LOG.debug("More Action Accessibility for action '{}': {}", actionName, accessRight);

            if (accessRight >= 2) {

                if (actionName.equals(ExtraAction.ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER)
                        && !claim.getInsurer().isEnableManualInvoiceOwnership() && claim.getInsurer().isEnableManualInvoiceWorkgroups()) {
                    extraActionDescription = ExtraAction.getExtraActionName(ExtraAction.ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP);
                } else if (actionName.equals(ExtraAction.ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER)
                        && claim.getInsurer().isEnableManualInvoiceOwnership() && !claim.getInsurer().isEnableManualInvoiceWorkgroups()) {
                    extraActionDescription = ExtraAction.getExtraActionName(ExtraAction.ASSIGN_OR_UPDATE_MANUAL_INV_CLAIM_OWNER);
                } else {
                    extraActionDescription = ExtraAction.getExtraActionName(actionName);
                }

                extraActionList.add(new LookupItem(actionName, extraActionDescription));
            }
        }

        return extraActionList;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="NOTIFICATION">
    public List<Notification> getFilteredNotifications() {
        List<Notification> returnList;
        List<Notification> notifications = notificationService.getNotifications(claim.getId());
        LOG.debug("Total list size={} ", notifications.size());
        if (getIsInsurer()) {
            returnList = ListUtils.filter(notifications, new ListUtils.Predicate<Notification>() {

                @Override
                public boolean apply(Notification object) {
                    return NotificationType.getNotificationType(object.getType()).isInsurerType();
                }
            });
            LOG.debug("Notification Return List Size Insurer: {}", returnList.size());
            return returnList;
        } else {
            returnList = ListUtils.filter(notifications, new ListUtils.Predicate<Notification>() {

                @Override
                public boolean apply(Notification object) {
                    return !NotificationType.getNotificationType(object.getType()).isInsurerType();
                }
            });
            LOG.debug("Notification Return List Size Cho: {} ", returnList.size());
            return returnList;

        }

    }

    // check whether penalty charge for this claim type enabled in bre band.
    public boolean isPenaltyChargeEnabledInBreBand() {

        if (claim.getInvoice() != null) {
            if (claim.getBreBand() == null) {
                BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
                claim.setBreBand(choBand);
            }

            if (claim.getBreBand().isAllowPenaltyCharges(claim.getClaimType())) {
                return true;
            }
        }
        return false;
    }
    
    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String removeNotification() {

        if (notificationId != null && notificationId > 0) {
            notificationService.removeNotificationById(notificationId);
        } else if (notificationId != null && notificationService != null && claim != null) { // No id given so remove all notifications
            if (getIsInsurer()) {
                notificationService.removeAllInsurerNotifications(claim.getId());
            } else {
                notificationService.removeAllCHONotifications(claim.getId());
            }
        }

        return SUCCESS;
    }

    public String acknowledgeNotification() {

        if (notificationId != null && notificationId > 0) {
                notificationService.acknowledgeNotificationById(notificationId);
        } else if (notificationId != null && notificationService != null && claim != null) {
            LOG.debug("Acknowledge All Notifications");
            if (getIsInsurer()) {
                LOG.debug("Acknowledge All Notifications for Insurer ");
                notificationService.acknowledgeAllInsurerNotifications(claim.getId());
            } else {
                LOG.debug("Acknowledge All Notifications for CHO ");
                notificationService.acknowledgeAllCHONotifications(claim.getId());
            }
        }

        return SUCCESS;
    }

    public String renderNotifications() {
        return SUCCESS;
    }

    public Boolean getHasNotifications() {
        LOG.debug("getHasNotifications called: {} ", (getFilteredNotifications().size() > 0));
        return getFilteredNotifications().size() > 0;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters /  Setters">
    public int getEscalateWorkgroupId() {
        return escalateWorkgroupId;
    }

    public Date getInvoiceCreationDate() {
        if (claim != null && claim.getInvoice() != null) {
            return claim.getInvoice().getCreatedDate();
        }
        return null;
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
        LOG.debug("Returning {} intelligent notes.", intelligentNotes.size());
        return intelligentNotes;
    }

    public List<String> getIntelligentNotes2() {
        if (intelligentNotes2 == null) {
            intelligentNotes2 = intelligentNoteDisplayEngine.getAllIntelligentNotes(claim);
        }
        LOG.debug("Returning {} intelligent notes.", intelligentNotes2.size());
        return intelligentNotes2;
    }

    public Boolean getIsAnyIntelligentNotes() {
        return getIntelligentNotes().size() > 0;
    }

    public Boolean getIsAnyAllIntelligentNotes() {
        return getIntelligentNotes2().size() > 0;
    }


    public boolean getIsInsurerInvoice() {
        return claim.getClaimType() == ClaimType.INSURER_INVOICE;
    }

    public boolean getIsInsurerManual() {
        return claim.getClaimType() == ClaimType.INSURER_INVOICE
                || claim.getClaimType() == ClaimType.INSURER_CLAIM
                || claim.getClaimType() == ClaimType.INSURER_ORIGINAL_INVOICE
                || claim.getClaimType() == ClaimType.INSURER_SUPPLEMENTARY_INVOICE
                || claim.getClaimType() == ClaimType.INSURER_UPLOAD;
    }

    public void setIntelligentNoteDisplayEngine(IntelligentNoteDisplayEngine intelligentNoteDisplayEngine) {
        this.intelligentNoteDisplayEngine = intelligentNoteDisplayEngine;
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses(getInsurerIsWorkgroupEnabled(),
                    getInsurerIsClaimOwnershipEnabled(),
                    getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(),
                    getIsTpiEnabledEnabled(), isInsurerUploadEnabled(),
                    getIsSubscriberEnabled());
        }
        return statuses;
    }
//    public void setTab(Integer tab) {
//        if (tab >= 0) {
//            getSession().put("tabIndex", tab);
//        }
//    }

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

    public BigDecimal getHirePenaltyChargeAmount() {
        return hirePenaltyChargeAmount;
    }

    public void setHirePenaltyChargeAmount(BigDecimal hirePenaltyChargeAmount) {
        this.hirePenaltyChargeAmount = hirePenaltyChargeAmount;
    }

    public BigDecimal getRepairPenaltyChargeAmount() {
        return repairPenaltyChargeAmount;
    }

    public void setRepairPenaltyChargeAmount(BigDecimal repairPenaltyChargeAmount) {
        this.repairPenaltyChargeAmount = repairPenaltyChargeAmount;
    }

    public BigDecimal getTotalPenaltyChargeAmount() {
        return totalPenaltyChargeAmount;
    }

    public void setTotalPenaltyChargeAmount(BigDecimal totalPenaltyChargeAmount) {
        this.totalPenaltyChargeAmount = totalPenaltyChargeAmount;
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

    public boolean getIsInsurerVsInsurerClaim() {
        return ClaimType.isInsurerVsInsurer(claim.getClaimType());
    }
    
    public boolean getIsSubscriberClaim() {
        return ClaimType.isSubscriber(claim.getClaimType());
    }
    
    public boolean getIsFixedFeeClaim() {
        return ClaimType.isFixedFee(claim.getClaimType());
    }
    
    public boolean getIsCollaborationProtocolClaim() {
        return ClaimType.isCollaborationProtocol(claim.getClaimType());
    }
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Subscriber Process Utility Functions">
    public boolean isRejectButtonEnabled() {
        boolean rejectEnabled = applicationAccessibility.checkActivityAccessibility(
                ApplicationAccessibility.CLAIM_REJECTION,
                getAuthenticatedUser(), claim) > 0;

        // If we have a subscriber or fixed-fee claim, we need to check the age of the claim
        if (rejectEnabled && ClaimType.isSubscriber(claim.getClaimType()) || ClaimType.isFixedFee(claim.getClaimType())) {
            int maxDays = 0;

            if (ClaimType.isSubscriber(claim.getClaimType())) {
                maxDays = DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays();
                if (claimDays == null) {
                    getSubscriberClaimDays();
                }
            } else if (ClaimType.isFixedFee(claim.getClaimType())) {
                if (claimDays == null) {
                    getFixedFeeClaimDays();
                }
                maxDays = DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays();
            }

            rejectEnabled = (claimDays < maxDays || (claimDays == maxDays && DateHelper.isBefore3pm()));
        }

        return rejectEnabled;
    }
    
    public boolean getCanShowSlaExtensionButton() {

        boolean slaExtensionEnabled = applicationAccessibility.checkActivityAccessibility(
                ApplicationAccessibility.SLA_EXTENSION,
                getAuthenticatedUser(), claim) > 0;

        int maxDays = 0;
        int maxAllowedSlaExtDays = 0;

        if (slaExtensionEnabled && !isSubscriberClaimRejectedMoreThanOnce()) {
            if (ClaimType.isSubscriber(claim.getClaimType())) {
                maxAllowedSlaExtDays = claim.getChorganisation().getMaxAllowedSlaExtForSubscriber();
                maxDays = DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays();
                if (claimDays == null) {
                    getSubscriberClaimDays();
                }
            } else if (ClaimType.isFixedFee(claim.getClaimType())) {
                maxAllowedSlaExtDays = claim.getChorganisation().getMaxAllowedSlaExtForFixedFee();
                maxDays = DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays();
                if (claimDays == null) {
                    getFixedFeeClaimDays();
                }
            }
        }
        return ((claimDays < maxDays && (claim.getSlaExtDays() < maxAllowedSlaExtDays)) || (claimDays == maxDays && DateHelper.isBefore3pm() && (claim.getSlaExtDays() < maxAllowedSlaExtDays)));
    }
    
    public int getAvailableSlaExtensionDays() {
        int availableDays = 0;
        if (getIsCHO() || getIsChoxAdmin()) {
            if (ClaimType.isSubscriber(claim.getClaimType())) {
                int maxAllowedSlaExtForSubscriber = claim.getChorganisation().getMaxAllowedSlaExtForSubscriber();
                if (maxAllowedSlaExtForSubscriber > 0 && maxAllowedSlaExtForSubscriber > claim.getSlaExtDays()) {
                    availableDays = maxAllowedSlaExtForSubscriber - claim.getSlaExtDays();
                }
            } else if (ClaimType.isFixedFee(claim.getClaimType())) {
                int maxAllowedSlaExtForFixedFee = claim.getChorganisation().getMaxAllowedSlaExtForFixedFee();
                if (maxAllowedSlaExtForFixedFee > 0 && maxAllowedSlaExtForFixedFee > claim.getSlaExtDays()) {
                    availableDays = maxAllowedSlaExtForFixedFee - claim.getSlaExtDays();
                }
            }
        }
        return availableDays;
    }

    public boolean isSubscriberClaimRejectedMoreThanOnce() {
        if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())) {
            return false;
        }
        
        int noTimesRejected = ClaimType.isSubscriber(claim.getClaimType()) ? claimService.getSubscriberClaimRejects(claim.getId())
                : claimService.getClaimRejects(claim.getId());
        
        return noTimesRejected > 1;
    }
 
    /*
     * Returns true if subscriber claim rejected reason is one of
     *     'Subscriber - Indemnity Issues' or 'Subscriber - Fraud Issues' 
     */
    public boolean isSubscriberClaimRejected() {

        return ClaimType.isSubscriber(claim.getClaimType()) && ClaimStatus.SUBSCRIBER_CLAIM_REJECTED.equals(claim.getStatus())
                && reasonOfRejectionService.isSubscriberClaimRejected(claim.getReasonOfRejection());
    }
 
    public boolean isSubscriberClaimUnder5Days() {
        if (!ClaimType.isSubscriber(claim.getClaimType())) {
            return false;
        }

        if (!claim.getStatus().equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REF_TO_ENG)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_PENDING)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_CONTESTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UPDATE_BY_ENG)) {
            return false;
        }

        if (claimDays == null) {
            getSubscriberClaimDays();
        }

        return claimDays < (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays());
    }
    
    public boolean isSubscriberClaimAt5Days() {
        if (!ClaimType.isSubscriber(claim.getClaimType())) {
            return false;
        }

        if (!claim.getStatus().equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REF_TO_ENG)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_PENDING)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_CONTESTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UPDATE_BY_ENG)) {
            return false;
        }

        if (claimDays == null) {
            getSubscriberClaimDays();
        }
        if (claimDays == (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            if (cal.get(Calendar.HOUR_OF_DAY) < 15) {
                return true;
            }
        }

        return false;
    }


    /* This function not only gets SubscriberClaimDays but also sometimes add new notes
     to the claim so need to update the claim version in the session.*/
    private void getSubscriberClaimDays() {
        claimDays = claimService.getSubscriberClaimDays(claim.getId());
        updateModelInSession(Arrays.asList(claim));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="Fixed Fee Process Utility Functions">
    public boolean isFixedFeeClaimUnder14Days() {
        if (!ClaimType.isFixedFee(claim.getClaimType())) {
            return false;
        }

        if (!claim.getStatus().equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REF_TO_ENG)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_PENDING)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_CONTESTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UPDATE_BY_ENG)) {
            return false;
        }

        if (claimDays == null) {
            getFixedFeeClaimDays();
        }

        return claimDays < (DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays());
    }

    public boolean isFixedFeeClaimAt14Days() {
        if (!ClaimType.isFixedFee(claim.getClaimType())) {
            return false;
        }

        if (!claim.getStatus().equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REF_TO_ENG)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_PENDING)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_CONTESTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_UPDATE_BY_ENG)) {
            return false;
        }

        if (claimDays == null) {
            getFixedFeeClaimDays();
        }

        if (claimDays == (DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            if (cal.get(Calendar.HOUR_OF_DAY) < 15) {
                return true;
            }
        }

        return false;
    }

    public String getSubscriberTimeLeft() {
        if (!ClaimType.isSubscriber(claim.getClaimType())) {
            return null;
        }

        if (claimDays == null) {
            getSubscriberClaimDays();
        }

        if (claimDays == ((DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays()) - 1)) {
            return "1 day remains";
        }
        return "" + ((DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays()) - claimDays) + " days remain";
    }

    public String getFixedFeeTimeLeft() {
        if (!ClaimType.isFixedFee(claim.getClaimType())) {
            return null;
        }

        if (claimDays == null) {
            getFixedFeeClaimDays();
        }

        if (claimDays == ((DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays()) - 1)) {
            return "1 day remains";
        }
        return "" + ((DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays()) - claimDays) + " days remain";
    }
    
    /* This function not only gets FixedFeeClaimDays but also sometimes add new notes
     to the claim so need to update the claim version in the session.*/
    private void getFixedFeeClaimDays() {
        claimDays = claimService.getFixedFeeClaimDays(claim.getId());
        updateModelInSession(Arrays.asList(claim));
    }
    // </editor-fold>

    public BigDecimal getFormattedInsLiab() {
        return claim.getPercentageLiabilityAccepted() == null ? BigDecimal.ZERO.setScale(2) : claim.getPercentageLiabilityAccepted();
    }

    public BigDecimal getFormattedChoLiab() {
        return claim.getPercentageLiabilityCho() == null ? BigDecimal.ZERO.setScale(2) : claim.getPercentageLiabilityCho();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Claim getModel() {
        return claim;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }

    public int getSupplierClaimOwnerId() {
        return supplierClaimOwnerId;
    }

    public void setSupplierClaimOwnerId(int supplierClaimOwnerId) {
        this.supplierClaimOwnerId = supplierClaimOwnerId;
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

    public List getWorkgroups() {
        if (workgroups == null) {
            workgroups = lookupService.getWorkgroups(this.getAuthenticatedUser(), true);
        }
        return workgroups;
    }

    public List getInsurerWorkgroups() {

        if (insurerWorkgroups == null) {
            if (this.getAuthenticatedUser().getInsurer() != null) {
                insurerWorkgroups = lookupService.getWorkgroupsByInsurerId(this.getAuthenticatedUser().getInsurer().getId(), true);
                addCurrentClaimInactiveWorkgroup();
            } else {
                insurerWorkgroups = lookupService.getWorkgroupsByInsurerId(-1, true);
                addCurrentClaimInactiveWorkgroup();
            }
        }

        return insurerWorkgroups;

    }
    
    // If the claim is blongs to in-active workgroup then add the in-active workgroup.
    private void addCurrentClaimInactiveWorkgroup() {
        if (claim != null && claim.getWorkgroup() != null && !claim.getWorkgroup().isStatus()) {
            insurerWorkgroups.add(claim.getWorkgroup());
        }
    }

    public List getVehicleClasses() {
        if (vehicleClasses == null) {
            vehicleClasses = lookupService.getVehicleClasses();
        }
        return vehicleClasses;
    }

    public List getInsurers() {

        if (insurers == null) {

            if (this.getAuthenticatedUser().getChorganisation() != null) {
                Chorganisation currentCho = this.getAuthenticatedUser().getChorganisation();
                insurers = this.lookupService.getInsurers(currentCho.getId());
            } else {
                insurers = this.lookupService.getInsurers();
            }

        }

        return insurers;
    }

    public List<ReasonOfRejection> getReasonOfClaimRejections() {
        if (reasonOfClaimRejections == null) {
            reasonOfClaimRejections = lookupService.getClaimRejectionReason(getInsurerIdForReasonOfRejection(), claim.getClaimType());
        }
        return reasonOfClaimRejections;
    }

    public List<ReasonOfRejection> getReasonOfClaimRejectionsRestricted() {
        if (reasonOfClaimRejectionsRestricted == null) {
            reasonOfClaimRejectionsRestricted = lookupService.getClaimRejectionRestrictedReason(getInsurerIdForReasonOfRejection(), claim.getClaimType());
        }
        return reasonOfClaimRejectionsRestricted;
    }

    public void setFinalReviewRequired(boolean finalReviewRequired) {
        this.finalReviewRequired = finalReviewRequired;
    }

    public List<LookupItem> getFinalReviewReasons() {
        List<LookupItem> reasons = new ArrayList<>(4);

        reasons.add(new LookupItem("Final Liability Stance", "Final Liability Stance"));
        reasons.add(new LookupItem("Indemnity Issues", "Indemnity Issues"));
        reasons.add(new LookupItem("Other", "Other"));
        return reasons;
    }

    public JSONArray getJsonReasonOfClaimRejectionDesc() {
        if (reasonOfClaimRejections == null) {
            reasonOfClaimRejections = lookupService.getClaimRejectionReason(getInsurerIdForReasonOfRejection(), claim.getClaimType());
        }
        List<LookupItem> rorItems = new ArrayList<>();
        for (ReasonOfRejection ror : reasonOfClaimRejections) {
            rorItems.add(new LookupItem(ror.getId().toString(), ror.getDescription()));
        }
        return JSONArray.fromObject(rorItems);
    }

    public List<ReasonOfRejection> getReasonOfInvoiceRejections() {
        if (reasonOfInvoiceRejections == null) {
            reasonOfInvoiceRejections = lookupService.getInvoiceRejectionReason(getInsurerIdForReasonOfRejection(), claim.getClaimType());
        }
        return reasonOfInvoiceRejections;
    }

    public JSONArray getJsonReasonOfInvoiceRejectionDesc() {
        if (reasonOfInvoiceRejections == null) {
            reasonOfInvoiceRejections = lookupService.getInvoiceRejectionReason(getInsurerIdForReasonOfRejection(), claim.getClaimType());
        }
        List<LookupItem> rorItems = new ArrayList<>();
        for (ReasonOfRejection ror : reasonOfInvoiceRejections) {
            rorItems.add(new LookupItem(ror.getId().toString(), ror.getDescription()));
        }
        return JSONArray.fromObject(rorItems);
    }

    public String getHireMonitoringEcds() {

        List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();

        List<HireMonitoringEcdViewData> viewDatas = new ArrayList<>();
        int seq = 1;
        for (HireMonitoringEcd h : hireMonitoringEcds) {
            viewDatas.add(new HireMonitoringEcdViewData(h, seq));
            seq++;
        }

        jObject = JSONArray.fromObject(viewDatas);

        return SUCCESS;
    }

    public String getJsonArrayData() {

        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";

    }
    // </editor-fold>

    public BigDecimal getPaymentDetailsCHODiscount() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getDiscount();
            } else {
                return claim.getInvoice().getDiscount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPaymentDetailsClaimHandInvAmt() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getClaimsHandlingInvoiceAmount();
            } else {
                return claim.getInvoice().getClaimsHandlingInvoiceAmount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPaymentDetailsDeductionClaimHandFee() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getDeductionForClaimsHandlingFee();
            } else {
                return claim.getInvoice().getDeductionForClaimsHandlingFee().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getPaymentDetailsInsurerDiscount() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getInsurerDiscount();
            } else {
                return claim.getInvoice().getInsurerDiscount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public boolean isPenaltyChargeApplied() {
        return claim.getInvoice() != null && claim.getInvoice().getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean getPenaltyChargeApplied() {
        return claim.getInvoice() != null && claim.getInvoice().getTotalPenaltyCharge().compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getEngineerFeeGrossPaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getEngineerFeeGross();
            } else {
                return claim.getInvoice().getEngineerFeeGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getHireGrossPaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getHireGross();
            } else {
                return claim.getInvoice().getHireGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getHirePenaltyChargePaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getHirePenaltyCharge();
            } else {
                return claim.getInvoice().getHirePenaltyCharge().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getRepairGrossPaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getRepairGross();
            } else {
                return claim.getInvoice().getRepairGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getRepairPenaltyChargePaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getRepairPenaltyCharge();
            } else {
                return claim.getInvoice().getRepairPenaltyCharge().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getStorageRecoveryGrossPaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getStorageRecoveryGross();
            } else {
                return claim.getInvoice().getStorageRecoveryGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalLossFeeGrossPaid() {
        if (claim.getInvoice() != null) {
            if (ClaimType.isInsurerVsInsurer(claim.getClaimType()) || ClaimType.isSubscriber(claim.getClaimType())
                    || ClaimType.isFixedFee(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
                return claim.getInvoice().getTotalLossFeeGross();
            } else {
                return claim.getInvoice().getTotalLossFeeGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal getProjectedFinalPayment() {
        BigDecimal projectedFinalPayment = null;
        if (claim.getInvoice() != null) {
            projectedFinalPayment = claim.getInvoice().getTotalToPay().subtract(getInterimPaymentMade());
        }

        return projectedFinalPayment;
    }

    public BigDecimal getFinalPayment() {
        if (claim.getInvoice() != null && finalPayment == null) {
            finalPayment = claim.getInvoice().getFinalPayment();
        }

        return finalPayment;
    }

    public BigDecimal getFinalPaymentOrTotal() {
        BigDecimal finalPaymentOrTotal = finalPayment;
        if (claim.getInvoice() != null && finalPaymentOrTotal == null) {
            finalPaymentOrTotal = claim.getInvoice().getFinalPayment();
            if (finalPaymentOrTotal == null) {
                finalPaymentOrTotal = claim.getInvoice().getTotalToPay();
                if (claim.getInvoice().getInterimPaymentMade() != null) {
                    finalPaymentOrTotal = finalPaymentOrTotal.subtract(claim.getInvoice().getInterimPaymentMade());
                }
            }
        }

        return finalPaymentOrTotal;
    }

    public BigDecimal getTotalToPay() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getTotalToPay();
        } else {
            return BigDecimal.ZERO;
        }
    }


    public String getChoRef() {

        return claim.getChoReference();

    }

    public String getInsurerName() {

        return claim.getInsurer().getName();

    }

    public String getRelatedInsurerName() {

        return claim.getInsurer().getRelatedInsurer().getName();

    }

     public String getLinkedChoName() {

        return claim.getChorganisation().getLinkedCho().getName();

    }

    public Boolean getIsAllNotationStatus() {
        String[] notationStatuses = {ClaimStatus.CLAIM_AWAITING_INVOICE_DATA,
            ClaimStatus.INVOICE_APPROVED_BY_BRE,
            ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT,
            ClaimStatus.INVOICE_ESCALATED,
            ClaimStatus.INVOICE_ESCALATED_TO_CH,
            ClaimStatus.INVOICE_PAYMENT_LOGGED,
            ClaimStatus.INVOICE_PAYMENT_RECEIVED,
            ClaimStatus.INVOICE_REF_TO_CH,
            ClaimStatus.INVOICE_REF_TO_ENG,
            ClaimStatus.INVOICE_REJECTED_ACCEPTED,
            ClaimStatus.AWAITING_INVOICE_PAYMENT,
            ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO,
            ClaimStatus.CONTESTED_INVOICE_REF_TO_INS,
            ClaimStatus.CLAIM_CLOSED,
            ClaimStatus.AWAITING_LITIGATION_OUTCOME,
            ClaimStatus.AWAITING_LIABILITY_RESOLUTION};

        List<String> statusList = Arrays.asList(notationStatuses);

        LOG.debug("claim status {}" + claim.getStatus());
        return statusList.contains(claim.getStatus());
    }

     public boolean isAtInvoicePaymentLogged() {
        return ClaimStatus.INVOICE_PAYMENT_LOGGED.equals(claim.getStatus());
    }

    public boolean isPaymentLoggedOverDays() {
        Date loggedDate = claim.getStatusModifiedDate();

        long days = DateHelper.getNumberOf24HourPeriodsBetween(loggedDate, new Date());
        LOG.debug("Invoice Payment Logged {} days ago", days);

        return days > 9;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        return jsonData;
    }

    /*
     * This method will exclude the current claim's insurer. This is used in
     * Switch claim to multiple insurer functionality.
     */
    public List getMappedInsurers() {

        if (mappedInsurers == null) {

            if (this.getAuthenticatedUser().getChorganisation() != null) {
                Chorganisation currentCho = this.getAuthenticatedUser().getChorganisation();
                mappedInsurers = this.lookupService.getInsurers(currentCho.getId());
                mappedInsurers.remove(claim.getInsurer());
            } else if (this.getAuthenticatedUser().isCHOXAdmin() && claim != null && claim.getChorganisation() != null) {
                mappedInsurers = this.lookupService.getInsurers(claim.getChorganisation().getId());
                mappedInsurers.remove(claim.getInsurer());
            }

        }

        return mappedInsurers;
    }

    /*
     * This method will exclude the current claim's insurer. This is used in
     * Switch claim to multiple insurer functionality.
     */
    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getMappedInsurers().size());
        for (Insurer insurer : mappedInsurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }

    public String getRepairPenaltyPercentageJsonString() {
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        List<PenaltyCharge> repairPenaltyCharges = penaltyChargeService.getPenaltyCharges(hireStart, ClaimType.getPenaltyType(claim.getClaimType()), PenaltyName.REPAIR);
        List<LookupItem> luItems = new ArrayList<>(repairPenaltyCharges.size());
        for (PenaltyCharge repairPenaltyPercentageEnum : repairPenaltyCharges) {
            // Append Age to Repair Penalty Percentage Desc eg. (30 days - 7.5%) 
            String perdec = new StringBuilder()
                    .append(repairPenaltyPercentageEnum.getPenaltyStartAge())
                    .append(" days - ")
                    .append(repairPenaltyPercentageEnum.getRepairPenaltyPercentageDsc())
                    .toString();
            luItems.add(new LookupItem(perdec, repairPenaltyPercentageEnum.getRepairPenaltyPercentageDsc()));
        }
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public String getHirePenaltyPercentageJsonString() {
        Date hireStart = (claim.getVehicleHire() != null && claim.getVehicleHire().getHireStart() != null) ? claim.getVehicleHire().getHireStart()
                    : (claim.getInvoice() != null && claim.getInvoice().getDateInvoiced() != null) ? claim.getInvoice().getDateInvoiced() : new Date();
        List<PenaltyCharge> hirePenaltyCharges = penaltyChargeService.getPenaltyCharges(hireStart, ClaimType.getPenaltyType(claim.getClaimType()), PenaltyName.HIRE);
        List<LookupItem> luItems = new ArrayList<>(hirePenaltyCharges.size());
        for (PenaltyCharge hirePenaltyPercentageEnum : hirePenaltyCharges) {
            // Append Age to Hire Penalty Percentage Desc eg. (30 days - 7.5%) 
            String perdec = new StringBuilder()
                    .append(hirePenaltyPercentageEnum.getPenaltyStartAge())
                    .append(" days - ")
                    .append(hirePenaltyPercentageEnum.getHirePenaltyPercentageDsc())
                    .toString();
            luItems.add(new LookupItem(perdec, hirePenaltyPercentageEnum.getHirePenaltyPercentageDsc()));
        }
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public String getCalculatedHirePenaltyPercentage() {
        if (isInsurerClaim()) {
            String percentage = claim.getInvoice().getHirePenaltyPercentage();
            return (percentage != null && !percentage.isEmpty()) ? percentage : "0%";
        }
        return penaltyChargeService.getPenaltyPercentageDsc(claim, PenaltyName.HIRE);
    }

    public String getCalculatedRepairPenaltyPercentage() {
        if (isInsurerClaim()) {
            String percentage = claim.getInvoice().getRepairPenaltyPercentage();
            return (percentage != null && !percentage.isEmpty()) ? percentage : "0%";
        }
        return penaltyChargeService.getPenaltyPercentageDsc(claim, PenaltyName.REPAIR);
    }

    public BigDecimal getCalculatedHirePenaltyChargeAmount() {
        if (isInsurerClaim()) {
            return penaltyChargeService.calculatePenaltyChargeVal(claim, getCalculatedHirePenaltyPercentage(), PenaltyName.HIRE);
        }
        return penaltyChargeService.calculatePenaltyChargeVal(claim, PenaltyName.HIRE);
    }

    public BigDecimal getCalculatedRepairPenaltyChargeAmount() {
        if (isInsurerClaim()) {
            return penaltyChargeService.calculatePenaltyChargeVal(claim, getCalculatedRepairPenaltyPercentage(), PenaltyName.REPAIR);
        }
        return penaltyChargeService.calculatePenaltyChargeVal(claim, PenaltyName.REPAIR);
    }

    public String getRepairPenaltyAmount() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", Boolean.TRUE);
        jsonObject.put("repairPenaltyAmount", penaltyChargeService.calculatePenaltyChargeVal(claim, repairPenaltyPercentage, PenaltyName.REPAIR));
        setJsonData(jsonObject.toString());
        return SUCCESS;
    }

    public String getHirePenaltyAmount() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", Boolean.TRUE);
        jsonObject.put("hirePenaltyAmount", penaltyChargeService.calculatePenaltyChargeVal(claim, hirePenaltyPercentage, PenaltyName.HIRE));
        setJsonData(jsonObject.toString());
        return SUCCESS;
    }

    public boolean getShowAutoPenaltyCheckbox() {

        return (claim.getChorganisation().isAutoPenaltyChargeEnabled() 
                && penaltyChargeService.calculateCurrentPenaltyBand(claim) < penaltyChargeService.getLastPenaltyBand(claim)
                && ClaimType.allowAutomaticPenaltyCharges(claim.getClaimType()));
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_CHO"})
    public String adjustAutoPenaltyCharge() {

        Map resultMap = penaltyChargeService.adjustAutoPenaltyCharge(claim, autoPenaltyStart, getIsCHO());

        if (resultMap.containsKey("error")) {
            this.setActionError((String) resultMap.get("error"));
            return ERROR;
        } else {
            if (resultMap.containsKey("claim")) {
                // Invoice details may have changed  so we need to reload the claim
                claim = (Claim) resultMap.get("claim");
            }
            return SUCCESS;
        }
    }

    public Date getAutoPenaltyStartDate() {
        return claim.getInvoice().getAutoPenaltyStart();
    }

    public String getPenaltyChargeConfiguration() {
        return SUCCESS;
    }

    public BigDecimal getInvHireGross() {
        return claim.getInvoice().getHireGross();
    }

    public BigDecimal getRepairGross() {
        return claim.getInvoice().getRepairGross();
    }

    public Integer getVersion() {
        LOG.debug("getVersion returning claimVersion={}", claimVersion);
        return claimVersion;
    }

    public boolean isAddPenaltyChargeConfigValidation() {
        return getIsCHO() && claim.getInvoice().getAutoPenaltyStart().compareTo(DateHelper.removeTime(claim.getInvoice().getCreatedDate())) >= 0;
    }

    @Override
    public void validate() {

        if (claim != null && (claim.getChorganisation() != null || claim.getInsurer() != null)) {
            if ((getIsInsurer() && claim.getInsurer().getId() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("ClaimAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("ClaimAction validated");
        } else {
            LOG.info(" ClaimAction validation not done as claim or cho or insurer is null");
        }
    }

    public BigDecimal getOutstandingInterimPayment() {
        interimPaymentReceived = claim.getInvoice().getInterimPaymentReceived();
        if (interimPaymentReceived == null) {
            interimPaymentReceived = BigDecimal.ZERO;
        }
        interimPaymentMade = claim.getInvoice().getInterimPaymentMade();
        if (interimPaymentMade == null) {
            interimPaymentMade = BigDecimal.ZERO;
        }
        return interimPaymentMade.subtract(interimPaymentReceived).setScale(2);
    }

    /**
     * Returns Insurer's id (in case of chox admin we get Insurer's id from
     * claim) return int id
     */
    private int getInsurerIdForReasonOfRejection() {
        int insurerIdt = -1;
        if (getAuthenticatedUser().getInsurer() != null) {
            insurerIdt = getAuthenticatedUser().getInsurer().getId();
        }
        if (claim.getInsurer() != null) {
            insurerIdt = claim.getInsurer().getId();
        }
        return insurerIdt;
    }

    public boolean getIsEscalatedToSupervisor() {
        if (getAuthenticatedUser().isCHO()) {
            return false;
        } else if (getAuthenticatedUser().isAnInsurer() && getAuthenticatedUser().getInsurer().isSupervisorEnable()
                && isInsurerAllowedForSupervisorQueue()
                && isEscalatedToSupervisor(getAuthenticatedUser().getInsurer().getDaysBeforeEscalated(), getAuthenticatedUser().getInsurer().getTimesInStatusContested())) {
            LOG.debug("Claim escalated to supervisor and visible to insurer.");
            return true;
        } else if (getAuthenticatedUser().isCHOXAdmin() && claim.getInsurer() != null && claim.getInsurer().isSupervisorEnable()
                && isEscalatedToSupervisor(claim.getInsurer().getDaysBeforeEscalated(), claim.getInsurer().getTimesInStatusContested())) {
            LOG.debug("Claim escalated to supervisor and visible to CHOX Admin.");
            return true;
        }
        LOG.debug("Claim not escalated to supervisor.");
        return false;
    }

    public boolean isInvoiceWithPaymentsTeam() {
        if (claim.getInvoice() == null)
            return false;

        return claim.getInvoice().isPaymentTeam();
    }

    public boolean isFinalReviewRequired() {
        if (getAuthenticatedUser().isCHO()) {
            return claim.isFinalReviewCho();
        } else if (getAuthenticatedUser().isAnInsurer()) {
            return claim.isFinalReviewIns();
        } else if (getAuthenticatedUser().isCHOXAdmin()) {
            return claim.isFinalReviewIns() || claim.isFinalReviewCho();
        }

        return false;
    }


    public String getFinalReviewMessage() {
        if (getAuthenticatedUser().isCHO()) {
            return "This invoice had a final review on "
                    + DateHelper.getLocalDateTimeFormat().format(claim.getFinalReviewDateCho())
                    + " by " + claim.getFinalReviewByCho().getFullName() + ".";
        } else if (getAuthenticatedUser().isAnInsurer()) {
            return "This invoice had a final review on "
                    + DateHelper.getLocalDateTimeFormat().format(claim.getFinalReviewDateIns())
                    + " by " + claim.getFinalReviewByIns().getFullName() + ".";
        } else if (getAuthenticatedUser().isCHOXAdmin() && claim.isFinalReviewCho() && claim.isFinalReviewIns()) {
            return "This invoice had a CHO final review on "
                    + DateHelper.getLocalDateTimeFormat().format(claim.getFinalReviewDateCho())
                    + " by " + claim.getFinalReviewByCho().getFullName()
                    + " and an Insurer final review on "
                    + DateHelper.getLocalDateTimeFormat().format(claim.getFinalReviewDateIns())
                    + " by " + claim.getFinalReviewByIns().getFullName() + ".";
        } else if (getAuthenticatedUser().isCHOXAdmin() && claim.isFinalReviewCho() && !claim.isFinalReviewIns()) {
            return "This invoice had a CHO final review on "
                    + DateHelper.getLocalDateTimeFormat().format(claim.getFinalReviewDateCho())
                    + " by " + claim.getFinalReviewByCho().getFullName() + ".";
        } else if (getAuthenticatedUser().isCHOXAdmin() && !claim.isFinalReviewCho() && claim.isFinalReviewIns()) {
            return "This invoice had an Insurer final review on "
                    + DateHelper.getLocalDateTimeFormat().format(claim.getFinalReviewDateIns())
                    + " by " + claim.getFinalReviewByIns().getFullName() + ".";
        }
        return "No final Review";
    }

    private boolean isInsurerAllowedForSupervisorQueue() {
        for (Object userRole : getAuthenticatedUser().getRoles()) {
            WebUserRole role = (WebUserRole) userRole;
            if (role.getName().contains(WebUserRole.ROLE_INS_MNG)
                    || role.getName().contains(WebUserRole.ROLE_INS_SUP)
                    || role.getName().contains(WebUserRole.ROLE_INS_MI)) {
                LOG.debug("User role allows for supervisor");
                return true;
            }
        }
        LOG.debug("User role does not allow for supervisor");
        return false;
    }

    private boolean isEscalatedToSupervisor(Integer daysBeforeEscaltedRestriction, Integer timesInStatusContestedRestionction) {
        if ((daysBeforeEscaltedRestriction != null && claimService.getDaysSinceInvoiceUploadToEscalate(claim.getId()) >= daysBeforeEscaltedRestriction)
                || (timesInStatusContestedRestionction != null && claimService.getNumberOfTimesContestedWithCHOtoEscalate(claim.getId()) >= timesInStatusContestedRestionction)) {
            LOG.debug("Claim has been escalated to supervisor");
            return true;
        }
        LOG.debug("Claim has not been escalated to supervisor");
        return false;
    }

    public String getCustomerClaimNumber() {
        return claim.getCustomer().getClaimReference();
    }

    public void setCustomerClaimNumber(String customerClaimNumber) {
        LOG.debug("Setting customer claim number to '{}'", customerClaimNumber);
        this.customerClaimNumber = customerClaimNumber;
    }
    
    public boolean isBrandingClaim() {
        return isBrandingTypeClaim(claim);
    } 
}
