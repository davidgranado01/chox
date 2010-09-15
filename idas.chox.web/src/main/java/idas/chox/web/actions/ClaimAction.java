package idas.chox.web.actions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.web.ListUtils;
import idas.chox.web.PanelAction;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
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
import idas.chox.core.model.Witness;
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
import idas.chox.service.security.NotificationAccessibility;
import idas.chox.service.security.PanelAccessibility;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.HireMonitoringEcdViewData;
import org.springframework.security.AccessDeniedException;


public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable, SessionAware {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimAction.class);

    private static final Logger logger = LoggerFactory.getLogger(ClaimAction.class);

    private TabAccessibility tabAccessibility;
    private NotificationAccessibility notificationAccessibility;
    private Map session;
    private Integer tab = -1;
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
    private AuditTrailService auditTrailService;
    private WorkgroupService workgroupService;
    private BreBandService breBandService;
    private UserService userService;
    private String hirePenaltyPercentage;
    private String repairPenaltyPercentage;

    private BigDecimal interimPayment;
    private Boolean interimPaymentReceived;

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
        if (invoicePenaltyPercentage==null)
            invoicePenaltyPercentage = "";
        return invoicePenaltyPercentage;
    }

    public void setHirePenaltyPercentage(String hirePenaltyPercentage) {
        this.hirePenaltyPercentage = hirePenaltyPercentage;
    }

    public String getRepairPenaltyPercentage() {
        String invoicePenaltyPercentage = claim.getInvoice().getRepairPenaltyPercentage();
        if (invoicePenaltyPercentage==null)
            invoicePenaltyPercentage = "";
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
            claim = new Claim();
            logger.debug("New claim object created");
        } else {
            claim = service.getClaim(id);
            logger.debug("Claim from db " + claim.getChoReference());
        }
    }

    @Override
    public String execute() throws Exception {

        if (tab > 0) {
            session.put("tabIndex", tab);
        } else {
            session.put("tabIndex", 0);
        }

        if (claim == null) {
            logger.debug("claim is null");
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

    public String submitHireMonitoringDetail() {

        String result = SUCCESS;

        String validationECDResult = validateHireMonitoringECDDetail();
        String validationLabourResult = validateHireMonitoringLabourDetail();
        LOG.debug("validationECDResult: '{}'", validationECDResult);
        LOG.debug("validationLabourResult: '{}'", validationLabourResult);
        if ((validationLabourResult.length() + validationECDResult.length()) <= 0) {

            String newStatus = ClaimStatus.CLAIM_AWAITING_INVOICE_DATA;

            try {

                auditTrailService.logAuditLog(newStatus, claim, null, null);

                this.claim.setStatus(newStatus);
                this.service.updateClaim(claim);

            } catch (Exception ex) {

                setActionResult("ERROR : " + ex.getMessage());

            }

        } else {

            result = ERROR;

            if (validationECDResult.length() > 0) {
                setActionResult(validationECDResult);
            } else {
                setActionResult(validationLabourResult);
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

    public String updateClaimNumber() {

        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            setActionResult("ERROR : " + ex.getMessage());
            return ERROR;
        }

        return SUCCESS;
    }

    public String makeInterimPayment() {

        try {
            claim.getInvoice().setInterimPayment(interimPayment);
            if (interimPayment.compareTo(BigDecimal.ZERO) > 0)
                claim.getInvoice().setInterimPaymentReceived(false);
            else
                claim.getInvoice().setInterimPaymentReceived(null);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            setActionResult("ERROR : " + ex.getMessage());
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateInterimPayment() {

        try {
            claim.getInvoice().setInterimPaymentReceived(true);
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
        if ( getIsBasedOnLiabilityAgreedDate()){
            setInvoiceIntroducedDays(claim.getLiabilityAgreedDays());
        }else {
            setInvoiceIntroducedDays(invoice.getInvoicedDays());
        }
        setTotalAmountToPayBeforeNewPenaltyCharge(invoice.getFullTotalToPay().subtract(invoice.getHirePenaltyCharge()).subtract(invoice.getRepairPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyCharge(invoice.getFullTotalToPay());
        if ( getIsBasedOnLiabilityAgreedDate()){
            setSplitLiabilityToPayBeforePenaltyFormatted(currentcyFormat.format(invoice.getTotalToPay().subtract(invoice.getHirePenaltyCharge().subtract(invoice.getRepairPenaltyCharge()).multiply(claim.getPercentageLiabilityAccepted()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))));
            setSplitLiabilityToPayAfterPenaltyFormatted(currentcyFormat.format(invoice.getTotalToPay()));
        }
        setTotalAmountToPayBeforeNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayBeforeNewPenaltyCharge()));
        setTotalAmountToPayAfterNewPenaltyChargeFormatted(currentcyFormat.format(getTotalAmountToPayAfterNewPenaltyCharge()));
        percentageLiabilityAcceptedForPenalty = claim.getPercentageLiabilityAccepted().toString();
        logger.debug("penalty percent " + percentageLiabilityAcceptedForPenalty);
        setHirePenaltyChargeAmount(invoice.getHirePenaltyCharge());
        setRepairPenaltyChargeAmount(invoice.getRepairPenaltyCharge());
        setTotalPenaltyChargeAmount(invoice.getTotalPenaltyCharge());
        setIsRemovePenaltyAlert((Boolean) false);
        result = "penaltyChargeApplied";


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
            if (getHirePenaltyChargeAmount().compareTo(invoice.getHirePenaltyCharge()) != 0)
                invoice.setHirePenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            if (getRepairPenaltyChargeAmount().compareTo(invoice.getRepairPenaltyCharge()) != 0)
                invoice.setRepairPenaltyChargeAppliedDate(DateHelper.getCurrentDateTime());
            invoice.setFullTotalToPay(newTotalAmountToPay);
            invoice.setHirePenaltyCharge(getHirePenaltyChargeAmount());
            invoice.setHirePenaltyPercentage(hirePenaltyPercentage);
            invoice.setRepairPenaltyCharge(getRepairPenaltyChargeAmount());
            invoice.setRepairPenaltyPercentage(repairPenaltyPercentage);
            totalPenaltyChargeAmount = getHirePenaltyChargeAmount().add(getRepairPenaltyChargeAmount());
            invoice.setTotalPenaltyCharge(totalPenaltyChargeAmount);
            if (isPenaltyAlertNotUsed != null && isPenaltyAlertNotUsed) {
                long dateDiff;
                if ( getIsBasedOnLiabilityAgreedDate()){
                    dateDiff = DateHelper.daysBetween(claim.getLiabilityAgreedDate(), new Date());
                }else{
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
        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();
            if (invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT) && invoice.getPenaltyAlertQty() > -1) {
                if ( getIsBasedOnLiabilityAgreedDate()){
                    return claim.getLiabilityAgreedDays() > (claim.getInvoice().getPenaltyAlertQty() + 1) *30;
                }
                result = invoice.getInvoicedDays() > (invoice.getPenaltyAlertQty() + 1) * 30;
            }
        }
        return result;
    }

    public boolean getIsBasedOnLiabilityAgreedDate(){
        if( claim.getLiabilityStatus() != null && claim.getInvoice() != null &&
            (claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_SPLIT) || claim.getLiabilityStatus().equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE))){            
            return true;
        }
        return false;
    }

    public boolean getIsShowPenaltyChargePanel() {
        boolean result = false;

        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();

            if (invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
                if ( getIsBasedOnLiabilityAgreedDate()){
                    result = claim.getLiabilityAgreedDays() > 30;
                }else{
                    result = invoice.getInvoicedDays() > 30;
                }
            }
        }

        return result;
    }

    public boolean getCanRevertClaimStatus() {
        boolean result = false;

        if ((getIsCHO() || getIsChoxAdmin()) && claim.getStatus().equals("AwaitingInvoiceData"))
            result = true;
        else if ((getIsInsurer() || getIsChoxAdmin()) && (claim.getStatus().equals("ClaimReferredToFNOL")
                                    || claim.getStatus().equals("ClaimReferredToEngineer")
                                    || claim.getStatus().equals("InvoiceReferredToEngineer")
                                    || claim.getStatus().equals("InvoicePaymentLogged")))
            result = true;
        return result;
    }
    
    public boolean getIsClaimClosedStatuses() {
        boolean bFlag = false;

        if ((ClaimStatus.getClosedStatus()).contains(claim.getStatus())) {
            bFlag = true;
        }

        return bFlag;
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

    public String getUpdateLiability(){
        logger.debug("Id " + id  + " " + claim.getChoReference());
        if ( claim != null ){
            fLiabilityAgreedDate = claim.getLiabilityAgreedDate();
            fLiabilityStatus = claim.getLiabilityStatus() == null ? LiabilityStatus.LIABILITY_NULL : claim.getLiabilityStatus();
            fPercentageLiabilityAccepted = claim.getPercentageLiabilityAccepted();
            fPercentageLiabilityCho = claim.getPercentageLiabilityCho();
            logger.debug("fLiabilityAgreedDate : " + fLiabilityAgreedDate);
            logger.debug("fLiabilityStatus : " + fLiabilityStatus.toString());
            logger.debug("fPercentageLiabilityAccepted : " + fPercentageLiabilityAccepted );
            logger.debug("fPercentageLiabilityCho : " + fPercentageLiabilityCho);
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
        if ( claim != null && claim.getInvoice() != null){
            interimPayment = claim.getInvoice().getInterimPayment();
            interimPaymentReceived = claim.getInvoice().getInterimPaymentReceived();
        }
        else {
            interimPayment = null;
            interimPaymentReceived = null;
        }
        return SUCCESS;
    }
    public String getUpdateInterimPayment() {
        if ( claim != null && claim.getInvoice() != null){
            interimPayment = claim.getInvoice().getInterimPayment();
            interimPaymentReceived = claim.getInvoice().getInterimPaymentReceived();
        }
        else {
            interimPayment = null;
            interimPaymentReceived = null;
        }
        return SUCCESS;
    }

    public String updateClaimSupplierOwner() {
        LOG.debug("Updating supplier claim owner to: {}", supplierClaimOwnerId);
        if (this.supplierClaimOwnerId > 0) {
            try {
                WebUser newClaimOwner = userService.getWebUser(supplierClaimOwnerId);

                // SET COMMENT
                if (claim.getSupplierClaimOwner() != null) {
                    String oldOwnerName = claim.getSupplierClaimOwner().getFullName();
                
                    Comment comment = Comment.New(0, "Supplier Claim owner changed from '" + oldOwnerName + "' to '" + newClaimOwner.getFullName() + "'");
                    claim.addComment(comment);
                    if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                        Comment comment2 = Comment.New(0, "Supplier Claims Handler is '" + newClaimOwner.getFullName() + "' (contact number: " + newClaimOwner.getTelephone() +")");
                        claim.addComment(comment2);
                    }
                }
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


    public String updateClaimWorkgroupAndOwner() {

        String oldOwnerName = "N/A";

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
                        Comment comment2 = Comment.New(0, "Insurer Claims Handler is '" + newClaimOwner.getFullName() + "' (contact number: " + newClaimOwner.getTelephone() +")");
                        claim.addComment(comment2);
                    }

                claim.setClaimOwner(newClaimOwner);
                claim.setWorkgroup(workgroupService.getWorkgroup(uosWorkgroupId));
                this.service.updateClaim(claim);

            } catch (Exception ex) {
                LOG.error("Error updating claim workgroup and owner for claim {}: {}", claim.getChoReference(), ex.getMessage());
                handleException(ex);
                return ERROR;
            }
        }

        return SUCCESS;
    }

   public String updateSaveLiabilityStatus() {
        logger.debug("updateSaveLiabilityStatus");
        String note = "Liability status changed from '" + claim.getLiabilityStatus() + "' to '" + fLiabilityStatus ;
        logger.debug("note : " + note);
        try {
            if ( claim.getLiabilityStatus()==null ||! claim.getLiabilityStatus().equals(fLiabilityStatus) ){
                if (fPercentageLiabilityAccepted != null && fPercentageLiabilityCho != null
                        && !fPercentageLiabilityCho.add(fPercentageLiabilityAccepted).equals(new BigDecimal(100.0))) {
                    LOG.error("Liability not 100%: ins={}, cho={}", fPercentageLiabilityAccepted, fPercentageLiabilityCho);
                    throw new AccessDeniedException("Total liability is not 100%");
                }
                
                Comment comment = Comment.New(0, note);
                comment.setClaim(claim);
                claim.getComments().add(comment);
                claim.setPercentageLiabilityAccepted(fPercentageLiabilityAccepted);
                claim.setPercentageLiabilityCho(fPercentageLiabilityCho);
                claim.setLiabilityStatus(fLiabilityStatus);
                claim.setLiabilityAgreedDate(fLiabilityAgreedDate);
                this.service.updateSaveLiabilityStatus(claim);

            }
            
        } catch (Exception ex) {
            logger.error("Error updating liability status for claim {}: {}", claim.getChoReference(), ex.getMessage());
            setActionResult("ERROR : " + ex.getMessage());
            return ERROR;
        }

        return SUCCESS;
    }

    public String escalatedUnassignedClaim() {

        try {

            if (escalateWorkgroupId > 0) {
                claim.setWorkgroup(workgroupService.getWorkgroup(escalateWorkgroupId));
                claim.setClaimOwner(null);
                this.service.updateClaim(claim);
            }

        } catch (Exception ex) {
            logger.error("Error escalating unassigned claim for claim {}: {}", claim.getChoReference(), ex.getMessage());
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
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
        logger.debug("Notification accessibility check: " + notificationAccessibility.getNotificationNotesNotificationAccessibility());
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
    	logger.debug("Total list size " + claim.getNotifications());
    	if (getIsInsurer()){
    		returnList = ListUtils.filter(claim.getNotifications(), new ListUtils.Predicate<Notification>(){
	    		@Override
	    		public boolean apply(Notification object) {
	    			logger.debug("Notification "+ object.getType() 
	    					+ " " + object.getMessage() 
	    					+ " " + object.getClaim().getChoReference()
	    					+ " " + object.getNotificationType() 
	    					+ " " + object.getNotificationType().isInsurerType());
	    			if (object.getNotificationType().isInsurerType()){
	    				return true; 
	    			}
	    			return false;
	    		}
	    	});
    		logger.debug("Notification Return List Size Insurer " + returnList.size());
	    	return returnList; 
    	}else{
    		returnList = ListUtils.filter(claim.getNotifications(), new ListUtils.Predicate<Notification>(){
	    		@Override
	    		public boolean apply(Notification object) {
	    			logger.debug("Notification "+ object.getType() 
	    					+ " " + object.getMessage() 
	    					+ " " + object.getClaim().getChoReference()
	    					+ " " + object.getNotificationType() 
	    					+ " " + object.getNotificationType().isInsurerType());
	    			if (object.getNotificationType().isInsurerType()){
	    				return false; 
	    			}
	    			return true;
	    		}
	    	});
    		logger.debug("Notification Return List Size Cho " + returnList.size());
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
        	if ( getIsInsurer()){
        		claim.removeAllInsurerNotifications();
        	}else{
        		claim.removeAllCHONotifications();
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
        return intelligentNotes;
    }

    public Boolean getIsAnyIntelligentNotes() {
        return getIntelligentNotes().size() > 0;
    }
    
    public Boolean getHasNotifications(){
    	logger.debug("getHasNotifications called " + (getFilteredNotifications().size() > 0));
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
                                                getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled());
        }
        return statuses;
    }

    @Override
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

    public BigDecimal getFormattedInsLiab(){
        return claim.getPercentageLiabilityAccepted() == null || claim.getPercentageLiabilityAccepted().equals(new BigDecimal("0.00"))? BigDecimal.ZERO : claim.getPercentageLiabilityAccepted();
    }

    public BigDecimal getFormattedChoLiab(){
        return claim.getPercentageLiabilityCho() == null|| claim.getPercentageLiabilityCho().equals(new BigDecimal("0.00")) ? BigDecimal.ZERO : claim.getPercentageLiabilityCho();
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

            short accessRight = applicationAccessibility.checkExtraActionAccessibility(action, getAuthenticatedUser(), claim);
            //logger.debug("#########action  " +action + " access right "+accessRight);
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

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    // </editor-fold>
}
