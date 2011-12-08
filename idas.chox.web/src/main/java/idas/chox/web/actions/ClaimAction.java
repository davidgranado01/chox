package idas.chox.web.actions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.BreBand;
import idas.chox.web.ListUtils;
import idas.chox.web.PanelAction;
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
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.VehicleHire;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.Witness;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.claim.ClaimObjectService;
import idas.chox.service.intelligentNotes.IntelligentNoteDisplayEngine;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.ButtonAccessibility;
import idas.chox.service.security.NotificationAccessibility;
import idas.chox.service.security.PanelAccessibility;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.HireMonitoringEcdViewData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.json.JSONObject;

public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimAction.class);
    private TabAccessibility tabAccessibility;
    private NotificationAccessibility notificationAccessibility;
    private JSONArray jObject;
    public static final String EMPTY = "empty";
    private List vehicleClasses;
    private List reasonOfClaimRejections;
    private List reasonOfClaimRejectionsRestricted;
    private List reasonOfInvoiceRejections;
    private List extraActionList;
    private List insurers;
    private List statuses;
    private List workgroups;
    private List insurerWorkgroups;
    private Claim claim = new Claim();
    private int id = -1;
    private int vehicleClassId = -1;
    private int insurerId = -1;
    private BigDecimal totalAmountToPayBeforeNewPenaltyCharge;
    private BigDecimal totalAmountToPayAfterNewPenaltyCharge;
    private String totalAmountToPayBeforeNewPenaltyChargeFormatted;
    private String totalAmountToPayAfterNewPenaltyChargeFormatted;
    private String splitLiabilityToPayBeforePenaltyFormatted;
    private String percentageLiabilityAcceptedForPenalty;
    private String splitLiabilityToPayAfterPenaltyFormatted;
    private BigDecimal hirePenaltyChargeAmount;
    private BigDecimal repairPenaltyChargeAmount;
    private BigDecimal totalPenaltyChargeAmount;
    private Boolean isRemovePenaltyAlert;
    private long invoiceIntroducedDays;
    private ApplicationAccessibility applicationAccessibility;
    private PanelAccessibility panelAccessibility;
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
    private ClaimService service;
    private LookupService lookupService;
    private WorkgroupService workgroupService;
    private BreBandService breBandService;
    private UserService userService;
    private String hirePenaltyPercentage;
    private String repairPenaltyPercentage;
    private BigDecimal interimPayment;
    private Boolean interimPaymentReceived;
    private ButtonAccessibility buttonAccessibility;
    private int actionSelected;
    private String nonce;
    private Boolean paymentLogged = false;
    private BigDecimal hireGrossPaid;
    private BigDecimal repairGrossPaid;
    private BigDecimal engineerFeeGrossPaid;
    private BigDecimal totalLossFeeGrossPaid;
    private BigDecimal storageRecoveryGrossPaid;
    private BigDecimal hirePenaltyChargePaid;
    private BigDecimal repairPenaltyChargePaid;
    private BigDecimal totalPaid;
    private boolean penaltyChargesPaid;
    private String jsonData;
    private List<Insurer> mappedInsurers;
    private AuditTrailService auditTrailService;

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public boolean isPenaltyChargeApplied() {
        if (claim.getInvoice() != null && (claim.getInvoice().getHirePenaltyCharge().compareTo(BigDecimal.ZERO) == 1 || claim.getInvoice().getRepairPenaltyCharge().compareTo(BigDecimal.ZERO) == 1)) {
            return true;
        }
        return false;
    }

    public int getLiabilityStatusValue() {
        return this.claim.getLiabilityStatus().ordinal();
    }

    public boolean getInvoiceDeleteWarning() {

        AuditTrail auditTrail;
        if ((auditTrail = auditTrailService.getLastChange(claim.getId())) != null && auditTrail.getOriginalStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {

            if (claim.getPreviousStatus() != null && !claim.getPreviousStatus().equalsIgnoreCase(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)) {
                LOG.warn("The 'previous_status' of claim '{}' [{}] does not match the previous status from the auditTrail [{}]",
                        new Object[] {claim.getChoReference(), claim.getPreviousStatus(), auditTrail.getOriginalStatus()});
            }
            return true;
        } else {
            return false;
        }
    }

    public String getPolicyNumber() {
        return claim.getThirdParty().getPolicyNumber();
    }

    public Boolean getPaymentLogged() {
        return paymentLogged;
    }

    public void setPaymentLogged(Boolean paymentLogged) {
        this.paymentLogged = paymentLogged;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public int getActionSelected() {
        return actionSelected;
    }

    public void setActionSelected(int actionSelected) {
        this.actionSelected = actionSelected;
    }

    public ClaimObjectService getClaimObjectService() {
        return claimObjectService;
    }

    public void setClaimObjectService(ClaimObjectService claimObjectService) {
        this.claimObjectService = claimObjectService;
    }

    public Map getLiabilityStatusDropDownMap() {
        return claimObjectService.getLiabilityStatusMap();
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

    public BigDecimal getInterimPayment() {
        return interimPayment;
    }

    public void setInterimPayment(BigDecimal interimPayment) {
        this.interimPayment = interimPayment;
    }

    public Boolean getInterimPaymentReceived() {
        return interimPaymentReceived;
    }

    public void setInterimPaymentReceived(Boolean interimPaymentReceived) {
        this.interimPaymentReceived = interimPaymentReceived;
    }

    @Override
    public void prepare() throws Exception {
        if (id <= 0) {
            if (getSession().containsKey("claimDetailPageClaimId") && getSession().get("claimDetailPageClaimId") != null) {
                LOG.info("claim is null and got id from session id is {}", (Integer) getSession().get("claimDetailPageClaimId"));
                claim = service.getClaim((Integer) getSession().get("claimDetailPageClaimId"));
            }
            // because creating new claim if id<=0 then the execute method will never return ClaimNotFound so it's useless having claim_not_found.jsp.
//            claim = new Claim();
//            LOG.debug("New claim object created");
        } else {
            claim = service.getClaim(id);
            getSession().put("claimDetailPageClaimId", id);
            LOG.debug("Claim from db " + claim.getChoReference());
        }
    }

    @Override
    public String execute() throws Exception {

        if (claim == null) {
            LOG.debug("claim is null");
            // this is never returned.
            return "ClaimNotFound";
        } else {
            return SUCCESS;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="CLAIM PANEL ACTION">
    public String updateClaimDetail() {
        this.service.updateClaim(claim);
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

        if (!claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED)) {
            LOG.debug("Claim status not INVOICE_PAYMENT_LOGGED: {}", claim.getStatus());
            paymentLogged = true;
            return SUCCESS;
        } else {
            return SUCCESS;
        }

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

    public String updateClaimNumber() {
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            setActionResult("ERROR : " + ex.getMessage());
            return ERROR;
        }

        return SUCCESS;
    }

//    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS"})
    public String makeInterimPayment() {
        try {
            claim.getInvoice().setInterimPayment(interimPayment);
            if (interimPayment.compareTo(BigDecimal.ZERO) > 0) {
                claim.getInvoice().setInterimPaymentReceived(false);

            } else {
                claim.getInvoice().setInterimPaymentReceived(null);

            }
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            setActionResult("ERROR : " + ex.getMessage());
            return ERROR;
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
        if (getIsBasedOnLiabilityAgreedDate()) {
            setInvoiceIntroducedDays(claim.getLiabilityAgreedDays());
        } else {
            setInvoiceIntroducedDays(invoice.getInvoicedDays());
        }
        setTotalAmountToPayBeforeNewPenaltyCharge(invoice.getFullTotalToPay().subtract(invoice.getHirePenaltyCharge()).subtract(invoice.getRepairPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyCharge(invoice.getFullTotalToPay());
        if (getIsBasedOnLiabilityAgreedDate()) {
            setSplitLiabilityToPayBeforePenaltyFormatted(currentcyFormat.format(invoice.getTotalToPay().subtract(invoice.getHirePenaltyCharge().subtract(invoice.getRepairPenaltyCharge()).multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))));
            setSplitLiabilityToPayAfterPenaltyFormatted(currentcyFormat.format(invoice.getTotalToPay()));
        }
        setTotalAmountToPayBeforeNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayBeforeNewPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayAfterNewPenaltyCharge()));
        percentageLiabilityAcceptedForPenalty = claim.getPercentageLiabilityAccepted().toString();
        LOG.debug("penalty percent " + percentageLiabilityAcceptedForPenalty);
        setHirePenaltyChargeAmount(invoice.getHirePenaltyCharge());
        setRepairPenaltyChargeAmount(invoice.getRepairPenaltyCharge());
        setTotalPenaltyChargeAmount(invoice.getTotalPenaltyCharge());
        setInterimPayment(invoice.getInterimPayment());
        setInterimPaymentReceived(invoice.getInterimPaymentReceived());
        setIsRemovePenaltyAlert((Boolean) false);
        result = "penaltyChargeApplied";

        LOG.debug("Returning: {}", result);
        return result;
    }

    public String doApplyPenaltyCharge() {
        String result = SUCCESS;

        try {

            Invoice invoice = claim.getInvoice();
            BigDecimal newTotalAmountToPay = invoice.getFullTotalToPay().subtract(invoice.getHirePenaltyCharge()).subtract(invoice.getRepairPenaltyCharge()).add(getHirePenaltyChargeAmount()).add(getRepairPenaltyChargeAmount());
            Boolean isPenaltyAlertNotUsed = getIsRemovePenaltyAlert();
            if (getHirePenaltyChargeAmount().compareTo(BigDecimal.ZERO) > 0 && (hirePenaltyPercentage == null || hirePenaltyPercentage.length() == 0)) {
                setActionResult("You must supply a value for 'Hire Penalty Percentage'");
                return ERROR;
            }
            if (getRepairPenaltyChargeAmount().compareTo(BigDecimal.ZERO) > 0 && (repairPenaltyPercentage == null || repairPenaltyPercentage.length() == 0)) {
                setActionResult("You must supply a value for 'Repair Penalty Percentage'");
                return ERROR;
            }
            if (getHirePenaltyChargeAmount().compareTo(invoice.getHirePenaltyCharge()) != 0) {
                invoice.setHirePenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            }
            if (getRepairPenaltyChargeAmount().compareTo(invoice.getRepairPenaltyCharge()) != 0) {
                invoice.setRepairPenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            }
            invoice.setFullTotalToPay(newTotalAmountToPay);
            invoice.setHirePenaltyCharge(getHirePenaltyChargeAmount());
            invoice.setHirePenaltyPercentage(hirePenaltyPercentage);
            invoice.setRepairPenaltyCharge(getRepairPenaltyChargeAmount());
            invoice.setRepairPenaltyPercentage(repairPenaltyPercentage);
            totalPenaltyChargeAmount = getHirePenaltyChargeAmount().add(getRepairPenaltyChargeAmount());
            invoice.setTotalPenaltyCharge(totalPenaltyChargeAmount);
            if (isPenaltyAlertNotUsed != null && isPenaltyAlertNotUsed) {
                long dateDiff;
                if (getIsBasedOnLiabilityAgreedDate()) {
                    dateDiff = DateHelper.daysBetween(claim.getLiabilityAgreedDate(), new Date());
                } else {
                    dateDiff = DateHelper.daysBetween(invoice.getCreatedDate(), new Date());
                }

                int newpenaltyAlertQty = (int) (dateDiff / 30);
                newpenaltyAlertQty = newpenaltyAlertQty >= 3 ? -1 : newpenaltyAlertQty;
                invoice.setPenaltyAlertQty(newpenaltyAlertQty);
            }

            service.updateClaim(claim);

        } catch (Exception ex) {

            result = ERROR;
            setActionResult("ERROR : " + ex.getMessage());

        }

        return result;
    }

    public boolean getIsShowPenaltyChargeAlert() {
        boolean result = false;
        boolean allowPenaltyCharges = true;
        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();
            // Set Claim BRE band
            BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
            if (claim.getBreBand() == null) {
                LOG.error("No BRE Band for claim '{}'", claim.getChoReference());
            }
            else if (!claim.getBreBand().isAllowPenaltyCharges()) {
                allowPenaltyCharges = false;
            }
            if (allowPenaltyCharges && invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT) && invoice.getPenaltyAlertQty() > -1) {
                
                if (getIsBasedOnLiabilityAgreedDate()) {
                    return claim.getLiabilityAgreedDays() > (claim.getInvoice().getPenaltyAlertQty() + 1) * 30;
                }
                result = invoice.getInvoicedDays() > (invoice.getPenaltyAlertQty() + 1) * 30;
            }
        }
        return result;
    }

    public boolean getIsBasedOnLiabilityAgreedDate() {
        Invoice invoice = claim.getInvoice();
        if (claim.getLiabilityStatus() != LiabilityStatus.LIABILITY_NULL && claim.getInvoice() != null
                && (claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_SPLIT) || claim.getLiabilityStatus().equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE))
                && claim.getLiabilityAgreedDate().after(invoice.getCreatedDate())) {
            return true;
        }
        return false;
    }

    public boolean getIsShowPenaltyChargePanel() {
        boolean result = false;

        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();

            if (invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
                if (getIsBasedOnLiabilityAgreedDate()) {
                    result = claim.getLiabilityAgreedDays() > 30;
                } else {
                    result = invoice.getInvoicedDays() > 30;
                }
            }
        }

        return result;
    }

    public boolean getCanCloseClaim() {
        LOG.debug("canCloseClaim: {}", getButtonAccessibility().getCloseClaimAccessibility());
        return getButtonAccessibility().getCloseClaimAccessibility();
    }

    public boolean getCanReopenClaim() {
        LOG.debug("canReopenClaim: {}", getButtonAccessibility().getReopenClaimAccessibility());
        return getButtonAccessibility().getReopenClaimAccessibility();
    }

    public boolean getCanRevertClaimStatus() {
        LOG.debug("canRevertClaim: {}", getButtonAccessibility().getRevertClaimAccessibility());
        return getButtonAccessibility().getRevertClaimAccessibility();
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

    public boolean getIsDuplicatedSupplementaryInvoiceExists() {
        boolean bFlag = false;

        if (!claim.getCustomer().getClaimReference().isEmpty() && ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
            if (service.getDuplicateSupplementaryInvoiceClaims(claim.getCustomer().getClaimReference(), claim.getId()).size() > 0) {
                bFlag = true;
            }
        }

        return bFlag;
    }

    public boolean getIsInterimPaymentMade() {
        boolean bFlag = false;

        if ((getIsCHO() || getIsChoxAdmin()) && claim.getInvoice() != null && !claim.getInvoice().getInterimPaymentReceived()) {
            bFlag = true;
        }

        return bFlag;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MORE ACTION - DROP DOWN">
    public String getUpdateInsurerClaimNumber() {
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

    public String getEscalateUnassignedClaim() {
        return SUCCESS;
    }

    public String getUpdateClaimSupplierOwner() {
        return SUCCESS;
    }

    public String getMakeInterimPayment() {
        if (claim != null && claim.getInvoice() != null) {
            interimPayment = claim.getInvoice().getInterimPayment();
            interimPaymentReceived = claim.getInvoice().getInterimPaymentReceived();
        } else {
            interimPayment = null;
            interimPaymentReceived = null;
        }
        return SUCCESS;
    }

    public String getUpdateInterimPayment() {
        if (claim != null && claim.getInvoice() != null) {
            interimPayment = claim.getInvoice().getInterimPayment();
            interimPaymentReceived = claim.getInvoice().getInterimPaymentReceived();
        } else {
            interimPayment = null;
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
                if (newClaimOwner.getChorganisation().getId().intValue() != claim.getChorganisation().getId().intValue()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
                }
                // Check user belongs to the CHO
                if (newClaimOwner.getChorganisation().getId().intValue() != claim.getChorganisation().getId().intValue()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
                }

                Comment comment;

                // SET COMMENT
                if (claim.getSupplierClaimOwner() != null) {
                    String oldOwnerName = claim.getSupplierClaimOwner().getFullName();


                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        comment = Comment.New(0, "Supplier Claim Owner changed from '" + oldOwnerName
                                + "' to '" + newClaimOwner.getFullName()
                                + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                    } else {
                        comment = Comment.New(0, "Supplier Claim Owner changed from '" + oldOwnerName
                                + "' to '" + newClaimOwner.getFullName() + "'");
                    }
                } else if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                    comment = Comment.New(0, "Supplier Claim Owner is '" + newClaimOwner.getFullName()
                            + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                } else {
                    comment = Comment.New(0, "Supplier Claim Owner is '" + newClaimOwner.getFullName() + "'");
                }
                claim.addComment(comment);
                claim.setSupplierClaimOwner(newClaimOwner);
                this.service.updateClaim(claim);

            } catch (Exception ex) {
                LOG.error("Error updating supplier claim owner for claim {}: {}", claim.getChoReference(), ex.getMessage());
                handleException(ex);
                return ERROR;
            }
        } else {
            LOG.error("Error: no supplierClaimOwnerId supplied to update claim {}: {}", claim.getChoReference(), supplierClaimOwnerId);
            setActionError("No supplier claim owner selected.");
            getActionResponse().AddError("No supplier claim owner selected.");
            return ERROR;
        }

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
                    Comment comment = Comment.New(0, "Claim owner changed from '" + oldOwnerName + "' to '" + newClaimOwner.getFullName() + "'");
                    claim.addComment(comment);
                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        Comment comment2 = Comment.New(0, "Insurer Claims Handler is '" + newClaimOwner.getFullName() + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                        claim.addComment(comment2);
                    }
                    Workgroup workgroup = workgroupService.getWorkgroup(uosWorkgroupId);

                    // Check workgroup belongs to the Insurer
                    if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                        throw new AccessDeniedException("Workgroup does not belong to Insurer");
                    }
                    // Check user belongs to the Insurer
                    if (newClaimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                        throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
                    }

                    claim.setClaimOwner(newClaimOwner);
                    claim.setWorkgroup(workgroup);
                    this.service.updateClaim(claim);

                } catch (Exception ex) {
                    LOG.error("Error updating claim workgroup and owner for claim {}: {}", claim.getChoReference(), ex.getMessage());
                    handleException(ex);
                    return ERROR;
                }
            } else {
                LOG.error("UN EXPECTED ERROR OCCURED SAVING claim {} ", claim.getChoReference());
                return ERROR;
            }


            return SUCCESS;
        } else if (claim.getInsurer().isClaimOwnershipEnable()) {
            if (this.claimOwnerId > 0) {

                try {

                    WebUser newClaimOwner = userService.getWebUser(claimOwnerId);

                    // SET COMMENT
                    if (claim.getClaimOwner() != null) {
                        oldOwnerName = claim.getClaimOwner().getFullName();
                    }
                    Comment comment = Comment.New(0, "Claim owner changed from '" + oldOwnerName + "' to '" + newClaimOwner.getFullName() + "'");
                    claim.addComment(comment);
                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        Comment comment2 = Comment.New(0, "Insurer Claims Handler is '" + newClaimOwner.getFullName() + "' (contact number: " + newClaimOwner.getTelephone() + ")");
                        claim.addComment(comment2);
                    }

                    // Check user belongs to the Insurer
                    if (newClaimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                        throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
                    }

                    claim.setClaimOwner(newClaimOwner);
                    this.service.updateClaim(claim);

                } catch (Exception ex) {
                    LOG.error("Error updating claim workgroup and owner for claim {}: {}", claim.getChoReference(), ex.getMessage());
                    handleException(ex);
                    return ERROR;
                }


                return SUCCESS;

            } else {
                LOG.error("UN EXPECTED ERROR OCCURED SAVING claim {} ", claim.getChoReference());
                return ERROR;
            }
        } else {
            LOG.debug("Error updating claim workgroup and owner for claim {} as workgroup and claim ownership is not enabled", claim.getChoReference());
            return ERROR;
        }
    }

    public String updateSaveLiabilityStatus() {
        LOG.debug("updateSaveLiabilityStatus");
//        String note = "Liability status changed from '" + claim.getLiabilityStatus() + "' to '" + fLiabilityStatus;
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
                    throw new AccessDeniedException("Total liability is not 100%");
                }

                Comment comment = Comment.New(0, note);
                comment.setClaim(claim);
                claim.addComment(comment);
                claim.setPercentageLiabilityAccepted(fPercentageLiabilityAccepted);
                claim.setPercentageLiabilityCho(fPercentageLiabilityCho);
                claim.setLiabilityStatus(fLiabilityStatus);
                claim.setLiabilityAgreedDate(fLiabilityAgreedDate);
                this.service.updateSaveLiabilityStatus(claim);

            }

        } catch (Exception ex) {
            LOG.error("Error updating liability status for claim {}: {}", claim.getChoReference(), ex.getMessage());
            setActionResult("ERROR : " + ex.getMessage());
            return ERROR;
        }

        return SUCCESS;
    }

    public String escalatedUnassignedClaim() {
        try {

            if (escalateWorkgroupId > 0) {
                Workgroup workgroup = workgroupService.getWorkgroup(escalateWorkgroupId);
                // Check workgroup belongs to the Insurer
                if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                    throw new AccessDeniedException("Workgroup does not belong to Insurer");
                }
                claim.setWorkgroup(workgroup);
                claim.setClaimOwner(null);
                this.service.updateClaim(claim);
            }

        } catch (Exception ex) {
            LOG.error("Error escalating unassigned claim for claim {}: {}", claim.getChoReference(), ex.getMessage());
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String markSupplementaryInvoicedClaim() {
        boolean canMark = true;
        if (!ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
            List<Claim> claims = service.getClaimsByCustomerClaimRef(claim.getCustomer().getClaimReference(), claim.getChorganisation().getId());
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
                    }
                    else if (claim.getClaimType() == ClaimType.INSURER_VS_INSURER || claim.getClaimType() == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE);
                    }
                    else if (claim.getClaimType() == ClaimType.SUBSCRIBER || claim.getClaimType() == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.SUBSCRIBER_ORIGINAL_INVOICE);
                    }
                    else if (claim.getClaimType() == ClaimType.TPI || claim.getClaimType() == ClaimType.TPI_ORIGINAL_INVOICE) {
                        claim.setClaimType(ClaimType.TPI_ORIGINAL_INVOICE);
                    } else {
                        LOG.error("Error determining type for cloned claim '{}': {}", claim.getChoReference(), claim.getClaimType());
                    }

                    
  //                  claim.setSupplementaryInvoicedClaim(true);
  //                  claim.setOriginalSupplementaryInvoicedClaim(true);
                    this.service.updateClaim(claim);
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
    public TabAccessibility getTabAccessibility() {

        if (tabAccessibility == null) {
            tabAccessibility = applicationAccessibility.getTabAccessibility(getAuthenticatedUser(), claim);
        }
        return tabAccessibility;
    }

    public NotificationAccessibility getNotificationAccessibility() {

        if (notificationAccessibility == null) {
            notificationAccessibility = applicationAccessibility.getNotificationAccessibility(getAuthenticatedUser(), claim.getStatus());
        }
        LOG.debug("Notification accessibility check: " + notificationAccessibility.getNotificationNotesNotificationAccessibility());
        return notificationAccessibility;
    }

    public PanelAccessibility getPanelAccessibility() {
        if (panelAccessibility == null) {
            panelAccessibility = applicationAccessibility.getPanelAccessibility(getAuthenticatedUser());
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
    public List<Notification> getFilteredNotifications() {
        List<Notification> returnList;
        LOG.debug("Total list size " + claim.getNotifications());
        if (getIsInsurer()) {
            returnList = ListUtils.filter(claim.getNotifications(), new ListUtils.Predicate<Notification>() {

                @Override
                public boolean apply(Notification object) {
                    LOG.debug("Notification " + object.getType()
                            + " " + object.getMessage()
                            + " " + object.getClaim().getChoReference()
                            + " " + object.getNotificationType()
                            + " " + object.getNotificationType().isInsurerType());
                    if (object.getNotificationType().isInsurerType()) {
                        return true;
                    }
                    return false;
                }
            });
            LOG.debug("Notification Return List Size Insurer " + returnList.size());
            return returnList;
        } else {
            returnList = ListUtils.filter(claim.getNotifications(), new ListUtils.Predicate<Notification>() {

                @Override
                public boolean apply(Notification object) {
                    LOG.debug("Notification " + object.getType()
                            + " " + object.getMessage()
                            + " " + object.getClaim().getChoReference()
                            + " " + object.getNotificationType()
                            + " " + object.getNotificationType().isInsurerType());
                    if (object.getNotificationType().isInsurerType()) {
                        return false;
                    }
                    return true;
                }
            });
            LOG.debug("Notification Return List Size Cho " + returnList.size());
            return returnList;

        }

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
            if (getIsInsurer()) {
                claim.removeAllInsurerNotifications();
            } else {
                claim.removeAllCHONotifications();
            }
            service.updateClaim(claim);

        }

        return SUCCESS;
    }

    public String acknowledgeNotification() {

        if (notificationId > 0) {

            Notification notification = claim.GetNotificationById(notificationId);
            if (notification != null) {
                claim.AcknowledgeNotifications(notification);
                service.updateClaim(claim);
            }

        } else {


            LOG.debug("Acknowledge All Notifications");
            if (getIsInsurer()) {

                LOG.debug("Acknowledge All Notifications for Insurer ");

                claim.AcknowledgeAllNotifications();

            }

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

    public Boolean getHasNotifications() {
        LOG.debug("getHasNotifications called " + (getFilteredNotifications().size() > 0));
        return getFilteredNotifications().size() > 0;
    }

    public Boolean getIsClaimAnomalous() {
        return claim.getIsIsAnomalies();
    }

    public void setIntelligentNoteDisplayEngine(IntelligentNoteDisplayEngine intelligentNoteDisplayEngine) {
        this.intelligentNoteDisplayEngine = intelligentNoteDisplayEngine;
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses(getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
                    getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), getIsTpiEnabledEnabled(), getInsurerIsUploadEnabled());
        }
        return statuses;
    }

    public void setTab(Integer tab) {
        if (tab > 0) {
            getSession().put("tabIndex", tab);
        } else if (!getSession().containsKey("tabIndex")) {
            getSession().put("tabIndex", 0);
        }
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

    public String getSplitLiabilityToPayAfterPenaltyFormatted() {
        return splitLiabilityToPayAfterPenaltyFormatted;
    }

    public void setSplitLiabilityToPayAfterPenaltyFormatted(String splitLiabilityToPayAfterPenaltyFormatted) {
        this.splitLiabilityToPayAfterPenaltyFormatted = splitLiabilityToPayAfterPenaltyFormatted;
    }

    public String getSplitLiabilityToPayBeforePenaltyFormatted() {
        return splitLiabilityToPayBeforePenaltyFormatted;
    }

    public void setSplitLiabilityToPayBeforePenaltyFormatted(String splitLiabilityToPayBeforePenaltyFormatted) {
        this.splitLiabilityToPayBeforePenaltyFormatted = splitLiabilityToPayBeforePenaltyFormatted;
    }

    public String getPercentageLiabilityAcceptedForPenalty() {
        return percentageLiabilityAcceptedForPenalty;
    }

    public void setPercentageLiabilityAcceptedForPenalty(String percentageLiabilityAcceptedForPenalty) {
        this.percentageLiabilityAcceptedForPenalty = percentageLiabilityAcceptedForPenalty;
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

    public boolean getIsInsurerVsInsurerClaim() {
        return ClaimType.isInsurerVsInsurer(claim.getClaimType());
    }

    public BigDecimal getFormattedInsLiab() {
        return claim.getPercentageLiabilityAccepted() == null || claim.getPercentageLiabilityAccepted().equals(new BigDecimal("0.00")) ? BigDecimal.ZERO : claim.getPercentageLiabilityAccepted();
    }

    public BigDecimal getFormattedChoLiab() {
        return claim.getPercentageLiabilityCho() == null || claim.getPercentageLiabilityCho().equals(new BigDecimal("0.00")) ? BigDecimal.ZERO : claim.getPercentageLiabilityCho();
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

            if (this.getAuthenticatedUser().getChorganisation() != null) {
                Chorganisation currentCho = this.getAuthenticatedUser().getChorganisation();
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

    public List getReasonOfClaimRejectionsRestricted() {
        if (reasonOfClaimRejectionsRestricted == null) {
            reasonOfClaimRejectionsRestricted = lookupService.getClaimRejectionRestrictedReason();
        }
        return reasonOfClaimRejectionsRestricted;
    }

    public List getReasonOfInvoiceRejections() {
        if (reasonOfInvoiceRejections == null) {
            reasonOfInvoiceRejections = lookupService.getInvoiceRejectionReason();
        }
        return reasonOfInvoiceRejections;
    }

    public String getActionPanel() {

        List<String> actions = PanelAction.getPanelActions();

        for (String action : actions) {
            short accessRight = applicationAccessibility.checkActionAccessibility(action, getAuthenticatedUser(), claim);
            if (accessRight >= 2) {
                LOG.debug("Returning action: {}", action);
                if (action.equals("updatePenaltyCharges")) {
                    LOG.debug("Setting properties for penalty charge panel....");
                    getAlertPanel();
                }
                return action;
            }
        }

        return EMPTY;
    }

    public boolean getIsClaimNotificationEditable() {
        if (applicationAccessibility.checkNotificationEditableCheck("NotificationNotesNotification", getAuthenticatedUser(), claim) < 2) {
            return false;
        }
        return true;
    }

    public List getExtraActionList() {

        List<String> actions = AdditionalAction.getExtraActions();
        extraActionList = new ArrayList<LookupItem>();
        for (String action : actions) {

            LOG.debug("Checking More Action Accessibility for action '{}' and claim status '{}'", action, claim.getStatus());
            short accessRight = applicationAccessibility.checkExtraActionAccessibility(action, getAuthenticatedUser(), claim);
            LOG.debug("More Action Accessibility for action '{}': {}", action, accessRight);
            if (accessRight >= 2) {
                String extraActionDescription = AdditionalAction.getExtraActionName(action);
                extraActionList.add(new LookupItem(action, extraActionDescription));
            }
        }

        return extraActionList;
    }

    public String getHireMonitoringEcds() {

        List<HireMonitoringEcd> hireMonitoringEcds = claim.getHireMonitoringEcds();

        List<HireMonitoringEcdViewData> viewDatas = new ArrayList<HireMonitoringEcdViewData>();
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

    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public BreBandService getBreBandService() {
        return breBandService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Payment Details Panel">
    public BigDecimal getInterimPaymentAmount() {
        return claim.getInvoice().getInterimPayment() != null ? claim.getInvoice().getInterimPayment() : BigDecimal.ZERO;
    }

    public boolean getInterimPaymentAmountReceived() {
        return claim.getInvoice().getInterimPaymentReceived() != null ? claim.getInvoice().getInterimPaymentReceived() : false;
    }

    public BigDecimal getPaymentDetailsCHODiscount() {
        return claim.getInvoice().getDiscount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPaymentDetailsClaimHandInvAmt() {
        return claim.getInvoice().getClaimsHandlingInvoiceAmount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPaymentDetailsDeductionClaimHandFee() {
        return claim.getInvoice().getDeductionForClaimsHandlingFee().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getPaymentDetailsInsurerDiscount() {
        return claim.getInvoice().getInsurerDiscount().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getEngineerFeeGrossPaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getEngineerFeeGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setEngineerFeeGrossPaid(BigDecimal engineerFeeGrossPaid) {
        this.engineerFeeGrossPaid = engineerFeeGrossPaid;
    }

    public BigDecimal getHireGrossPaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getHireGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public boolean isPenaltyChargesPaid() {
        return penaltyChargesPaid;
    }

    public void setPenaltyChargesPaid(boolean penaltyChargesPaid) {
        this.penaltyChargesPaid = penaltyChargesPaid;
    }

    public void setHireGrossPaid(BigDecimal hireGrossPaid) {
        this.hireGrossPaid = hireGrossPaid;
    }

    public BigDecimal getHirePenaltyChargePaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getHirePenaltyCharge().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setHirePenaltyChargePaid(BigDecimal hirePenaltyChargePaid) {
        this.hirePenaltyChargePaid = hirePenaltyChargePaid;
    }

    public BigDecimal getRepairGrossPaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getRepairGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setRepairGrossPaid(BigDecimal repairGrossPaid) {
        this.repairGrossPaid = repairGrossPaid;
    }

    public BigDecimal getRepairPenaltyChargePaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getRepairPenaltyCharge().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setRepairPenaltyChargePaid(BigDecimal repairPenaltyChargePaid) {
        this.repairPenaltyChargePaid = repairPenaltyChargePaid;
    }

    public BigDecimal getStorageRecoveryGrossPaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getStorageRecoveryGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setStorageRecoveryGrossPaid(BigDecimal storageRecoveryGrossPaid) {
        this.storageRecoveryGrossPaid = storageRecoveryGrossPaid;
    }

    public BigDecimal getTotalLossFeeGrossPaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getTotalLossFeeGross().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setTotalLossFeeGrossPaid(BigDecimal totalLossFeeGrossPaid) {
        this.totalLossFeeGrossPaid = totalLossFeeGrossPaid;
    }

    public BigDecimal getTotalPaid() {
        if (claim.getInvoice() != null) {
            return claim.getInvoice().getFullTotalToPay().multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String updatePaymentDetails() {
        JSONObject jsonObject = new JSONObject();
        if (claim.getInvoice() != null) {
            try {
                Invoice inv = claim.getInvoice();
                inv.setHireGrossPaid(hireGrossPaid);
                inv.setRepairGrossPaid(repairGrossPaid);
                inv.setEngineerFeeGrossPaid(engineerFeeGrossPaid);
                inv.setTotalLossFeeGrossPaid(totalLossFeeGrossPaid);
                inv.setStorageRecoveryGrossPaid(storageRecoveryGrossPaid);
                inv.setHirePenaltyChargePaid(hirePenaltyChargePaid);
                inv.setRepairPenaltyChargePaid(repairPenaltyChargePaid);
                inv.setTotalPaid(totalPaid);
                inv.setPenaltyChargesPaid(penaltyChargesPaid);
                service.updateClaim(claim);
                jsonObject.put("success", Boolean.TRUE);
                jsonObject.put("message", "Payment details updated successfully.");
                setJsonData(jsonObject.toString());
                return SUCCESS;
            } catch (Exception ex) {
                LOG.error("Exception thrown while updating payment details, error message : {}", ex.getMessage());
                jsonObject.put("success", Boolean.FALSE);
                jsonObject.put("errors", "An unexpected error occured while updating payment details. Please report to CHOX support.");
                setJsonData(jsonObject.toString());
                return ERROR;
            }
        } else {
            jsonObject.put("success", Boolean.FALSE);
            jsonObject.put("errors", "Sorry - This claim do not have invoice.");
            setJsonData(jsonObject.toString());
            return ERROR;
        }
    }

    // </editor-fold>
    public ButtonAccessibility getButtonAccessibility() {

        if (buttonAccessibility == null) {
            setButtonAccessibility(applicationAccessibility.getButtonAccessibility(getAuthenticatedUser(), claim));
        }
        return buttonAccessibility;
    }

    public String getChoRef() {

        return claim.getChoReference();

    }

    public boolean getpaymentDetailsConfirmationEnabled() {

        return claim.getInsurer().isPaymentDetailsConfirmationEnabled();

    }

    public String getInsurerName() {

        return claim.getInsurer().getName();

    }

    public String getRelatedInsurerName() {

        return claim.getInsurer().getRelatedInsurer().getName();

    }

    public boolean getCanShowSwitchClaimButton() {

        if ((getButtonAccessibility().getSwitchClaimAccessibility()) && (claim.getInsurer().getRelatedInsurer() != null) && claim.getInvoice() == null) {
            return true;
        }

        return false;
    }

    public boolean getCanShowSwitchClaimToMultipleInsButton() {

        if ((getButtonAccessibility().getSwitchClaimToMultipleInsurerAccessibility()) && (claim.getInvoice() == null)) {
            return true;
        }

        return false;
    }

    public boolean getIsAdminChox() {

        boolean roleExist = false;
        Iterator itr = getAuthenticatedUser().getRoles().iterator();
        while (itr.hasNext()) {
            WebUserRole r = (WebUserRole) itr.next();
            LOG.debug("CHECKING USER ROLE TO DIVERT THE PAGE: '{}'", r.getName());
            if ((r.getName().equals(WebUserRole.ROLE_CHOX_ADMIN))) {

                LOG.debug("ROLE EXIST : '{}'", r.getName());

                roleExist = true;
            }

        }
        return roleExist;
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
            ClaimStatus.AWAITING_LIABILITY_RESOLUTION};

        List<String> statusList = Arrays.asList(notationStatuses);

        LOG.debug("claim status {}" + claim.getStatus());
        return statusList.contains(claim.getStatus());
    }

    /**
     * @param buttonAccessibility the buttonAccessibility to set
     */
    public void setButtonAccessibility(ButtonAccessibility buttonAccessibility) {
        this.buttonAccessibility = buttonAccessibility;
    }

    public boolean isPaymentLoggedOverDays() {
        Date loggedDate = claim.getStatusModifiedDate();

        long days = DateHelper.daysBetween(loggedDate, new Date());
        LOG.debug("Invoice Payment Logged {} days ago", days);

        if (days > 9) {
            return true;
        }

        return false;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        return jsonData;
    }

    /*
     *  This method will exclude the current claim's insurer. This is used in Switch claim to multiple insurer functionality. 
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
     *  This method will exclude the current claim's insurer. This is used in Switch claim to multiple insurer functionality. 
     */
    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getMappedInsurers().size());
        for (Insurer insurer : mappedInsurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    @Override
    public void validate() {

        if (claim != null && (claim.getChorganisation() != null || claim.getInsurer() != null)) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("ClaimAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("ClaimAction validated");
        } else {
            LOG.debug(" ClaimAction validation not done as claim is null");
        }
    }
}
