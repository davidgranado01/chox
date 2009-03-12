/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.EngineerReport;
import chox.services.ClaimService;
import chox.services.InvoiceService;
import chox.services.ChoBandService;
import chox.services.LookupService;
import chox.web.data.PanelAction;
import chox.web.data.ExtraAction;
import chox.web.security.ApplicationAccessibility;
import chox.web.security.TabAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.math.BigDecimal;
import java.util.List;
import org.acegisecurity.GrantedAuthority;
import scsbre.engine.RulesEngineResponse;
import chox.data.AttachmentCategory;
import chox.model.Chorganisation;
import chox.model.Comment;
import chox.model.Customer;
import chox.model.HireMonitoringDetail;
import chox.model.Incident;
import chox.model.Injury;
import chox.model.Insurer;
import chox.model.Invoice;
import chox.model.LookupItem;
import chox.model.ReasonOfRejection;
import chox.model.Solicitor;
import chox.model.ThirdParty;
import chox.model.VehicleHire;
import chox.model.WebUser;
import chox.model.Witness;
import chox.services.AuditTrailService;
import chox.services.CommentService;
import chox.services.HireMonitoringEcdService;
import chox.services.HistoryService;
import chox.services.ReasonOfRejectionService;
import chox.web.security.PanelAccessibility;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *  
 * @author Emmanuel
 */
public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable {

    public static final String REFER_FNOL = "referFNOL";
    public static final String REJECT = "reject";
    public static final String ACCEPT = "accept";
    public static final String REFER = "refer";
    public static final String EMPTY = "empty";
    public static final String REGISTER_FNOL = "registerFNOL";
    public static final String REJECT_FNOL = "rejectFNOL";
    public static final String PENDING = "pending";
    public static final String REFER_CH = "referCH";
    private Claim claim = new Claim();
    private int id = -1;
    private List lineOfBusinesses;
    private List vehicleClasses;
    private List reasonOfClaimRejections;
    private List reasonOfInvoiceRejections;
    private List extraActionList;
    private List insurers;
    private List statuses;
    private ClaimService service;
    private LookupService lookupService;
    private InvoiceService invoiceService;
    private ChoBandService choBandService;
    private HistoryService historyService;
    private AuditTrailService auditTrailService;
    private ReasonOfRejectionService reasonOfRejectionService;
    private HireMonitoringEcdService hireMonitoringEcdService;
    private CommentService commentService;
    private String actionResult;
    private TabAccessibility tabAccessibility;
    private int vehicleClassId = -1;
    private int lineOfBusinessId = -1;
    private int insurerId = -1;
    private String actionName;
    private List attachmentCategory;
    private String statusMsg = "";
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
    Integer hireMonitoringDetailId;
    Integer incidentId;
    Integer thirdPartyId;
    Integer customerId;
    Integer invoideId;
    Integer vehicleHireId;
    Integer engineerReportId;
    Integer witnessId;
    Integer injuryId;
    Integer injurySolicitorId;

    public List getAttachmentCategory() {
        List items = new ArrayList<LookupItem>();
        for (String s : AttachmentCategory.getAttachmentCategory()) {
            items.add(new LookupItem(s, s));
        }

        attachmentCategory = items;

        return attachmentCategory;

    }
    // ADDED BY CARLSON @ 2008-12-02 - END
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public Claim getModel() {
        return claim;
    }

    public void prepare() throws Exception {
        if (id <= 0) {
            claim = new Claim();
        } else {
            claim = service.getClaim(id);
        }
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses();
        }
        return statuses;
    }

    public List getLineOfBusinesses() {
        if (lineOfBusinesses == null) {
            lineOfBusinesses = lookupService.getLineOfBusinesses();
        }
        return lineOfBusinesses;
    }

    // ADDED BY CARLSON @ 2008-12-03
    public List getVehicleClasses() {
        if (vehicleClasses == null) {
            vehicleClasses = lookupService.getVehicleClasses();
        }
        return vehicleClasses;
    }

    // ADDED BY CARLSON @ 2008-12-03
    public List getInsurers() {
        if (insurers == null) {
            insurers = lookupService.getInsurers();
        }
        return insurers;
    }

    // ADDED BY CARLSON @ 2009-02-03
    public List getReasonOfClaimRejections() {
        if (reasonOfClaimRejections == null) {
            reasonOfClaimRejections = lookupService.getClaimRejectionReason();
        }
        return reasonOfClaimRejections;
    }
    // ADDED BY CARLSON @ 2009-02-03
    public List getReasonOfInvoiceRejections() {
        if (reasonOfInvoiceRejections == null) {
            reasonOfInvoiceRejections = lookupService.getInvoiceRejectionReason();
        }
        return reasonOfInvoiceRejections;
    }
    // ADDED BY CARLSON @ 2009-02-03
    public List getExtraActionList() {

        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        List<String> actions = ExtraAction.getExtraActions();

        List items = new ArrayList<LookupItem>();

        for (String action : actions) {
            short accessRight = applicationAccessibility.checkExtraActionAccessibility(action, grantedAuthorities, claim.getStatus());

            if (accessRight > 1) {
                String extraActionDescription = getExtraActionName(action);
                items.add(new LookupItem(action, extraActionDescription));
            }
        }

        extraActionList = items;
        return extraActionList;
    }

    public String getExtraActionName(String extraAction) {
        String returnStr = "";
        if (extraAction.equalsIgnoreCase("updateInsurerClaimNumber")) {
            returnStr = "Update Insurer Claim Number";
        }else if(extraAction.equalsIgnoreCase("updatePaymentReceived")){
            returnStr = "Update Payment Logged";
        } 
                
        return returnStr;
    }

    public String getActionResult() {
        return actionResult;
    }

    public String getExtraActionName() {
        return extraActionName;
    }

    public String updateClaimDetail() {
        this.service.updateClaim(claim);
        this.actionResult = "Claim Updated!";
        return SUCCESS;
    }

    public TabAccessibility getTabAccessibility() {

        if (tabAccessibility == null) {
            tabAccessibility = applicationAccessibility.getTabAccessibility(getAuthenticatedUser().getAuthorities(), claim.getStatus());
        }
        return tabAccessibility;
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public String getActionPanel() {
        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        List<String> actions = PanelAction.getPanelActions();

        for (String action : actions) {

            short accessRight = applicationAccessibility.checkActionAccessibility(action, grantedAuthorities, claim.getStatus());

            if (accessRight > 0) {
                return action;
            }
        }

        return EMPTY;
    }

    public String getUpdateInsurerClaimNumberAction() {
        return "updateInsurerClaimNumber";
    }
    
    public String getPaymentReceivedAction(){
        System.out.println("CLAIM ACTION > getPaymentReceivedAction");
        return "updatePaymentReceived";
    }
    //Claim Actions
    public String route() {
        String result = SUCCESS;
        //chack whether line of busineess if set 
        if (this.getLineOfBusinesses() == null) {
            this.actionResult = "ERROR : You need to provide line of business to route this claim.";
        } else {
            if (!claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                this.actionResult = "ERROR : Invalid operation!";
            } else {
                //LineOfBusiness lob = new LineOfBusiness();
                //lob.setId(this.lineOfBusinessId);
                //claim.setLineOfBusiness(lob);

                try {
                    String newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
                    auditTrailService.logAuditLog(newStatus, claim, null, null);
                    claim.setStatus(newStatus);
                    this.service.updateClaim(claim);

                } catch (Exception ex) {
                    result = ERROR;
                    this.actionResult = "ERROR : " + ex.getMessage();
                }
            }
        }
        statusMsg = "Your action has been recorded";
        return result;
    }

    //Acknowledge
    public String acknowledge() {

        String result = SUCCESS;
        String newStatus = "";
        //chack whether line of busineess if set 

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
            logNewCommentForRejection(claim.getReasonOfRejectionId(), true);
        }

        if (!result.equalsIgnoreCase(ERROR)) {
            try {

                boolean isPublic = false;
                String strPrefix = "Claim Review Note: ";
                createNewNote(claim.getEngineerClaimReviewNotes(), isPublic, strPrefix);

                claim.setEngineerClaimReviewNotes("");
                claim.setIsFnolReviewed(false);

                auditTrailService.logAuditLog(newStatus, claim, null, null);

                claim.setStatus(newStatus);
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                result = ERROR;
                this.actionResult = "ERROR : " + ex.getMessage();
            }

            statusMsg = "Your action has been recorded";
        }
        return result;
    }
    // FNOL
    public String registerFNOL() {

        String result = SUCCESS;
        String newStatus = "";
        if (this.actionName.equalsIgnoreCase(REGISTER_FNOL)) {
            newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
            claim.setIsFnolReviewed(true);
        } else if (this.actionName.equalsIgnoreCase(REJECT_FNOL)) {
            newStatus = ClaimStatus.CLAIM_REJECTED;
        }

        boolean isPublic = false;
        String strPrefix = "FNOL Review Note: ";
        createNewNote(reasonForRejection, isPublic, strPrefix);

        if (!result.equalsIgnoreCase(ERROR)) {
            try {
                auditTrailService.logAuditLog(newStatus, claim, null, null);
                claim.setStatus(newStatus);
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }

        statusMsg = "Your action has been recorded";
        return result;
    }

    private void createNewNote(String sComment, boolean isPublic, String strPrefix) {

        if (sComment.length() > 0) {
            Comment comment = new Comment();
            comment.setIsPublic(isPublic);
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
        String newStatus = ClaimStatus.AWAITING_CAR_HIRE_INFO;

        try {

            boolean isPublic = false;
            String strPrefix = "Claim Review Note: ";
            createNewNote(claim.getEngineerClaimReviewNotes(), isPublic, strPrefix);

            claim.setEngineerClaimReviewNotes("");

            auditTrailService.logAuditLog(newStatus, claim, null, null);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }

    public String contestOrAcceptRejectedClaim() {

        String result = SUCCESS;
        String newStatus;
        
        Integer iClaimRejectionReasonId = null;
        
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            
            newStatus = ClaimStatus.CLAIM_REJECTION_ACCEPTED;
            iClaimRejectionReasonId = claim.getReasonOfRejectionId();
            
        } else {
            newStatus = ClaimStatus.CLAIM_REJECTION_CONTESTED;
        }
        
        try {
            auditTrailService.logAuditLog(newStatus, claim, iClaimRejectionReasonId, null);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        statusMsg = "Your action has been recorded";
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
            //claim = service.getClaim(id);
            newStatus = ClaimStatus.CLAIM_REJECTED;
            logNewCommentForRejection(claim.getReasonOfRejectionId(), true);
        }

        if (!result.equalsIgnoreCase(ERROR)) {
            try {

                boolean isPublic = false;
                String strPrefix = "Claim Review Note: ";
                createNewNote(claim.getEngineerClaimReviewNotes(), isPublic, strPrefix);
                claim.setEngineerClaimReviewNotes("");

                auditTrailService.logAuditLog(newStatus, claim, null, null);
                this.claim.setStatus(newStatus);
                this.service.updateClaim(claim);
                statusMsg = "Your action has been recorded";

            } catch (Exception ex) {
                result = ERROR;
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }

        return result;
    }

    public String submitHireMonitoringDetail() {

        String result = SUCCESS;

        String validationResult = validateHireMonitoringDetail();
        if (validationResult.isEmpty()) {
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
            this.actionResult = validationResult;
        }


        statusMsg = "Your action has been recorded";
        return result;
    }

    public String validateHireMonitoringDetail() {

        String result = "";

        /*0000314
        if (this.claim.getHireMonitoringDetail() == null) {
        return  "Error : You need to provide correct hire monitoring detail to submit this claim.";
        } 
         */

        if (this.claim.getCustomer() == null || this.claim.getCustomer().getInitialECD() == null) {
            if (this.service.getECDCountByClaimId(this.claim.getId()) == 0) {
                return "Error : You need to provide an Estimated Completion Date (ECD) to submit this claim.";
            }
        }

        statusMsg = "Your action has been recorded";
        return result;
    }

    public String reSubmitRejectedClaim() {

        claim = constructeClaimForInvoiceValidation(claim);
        RulesEngineResponse reponse = invoiceService.XMLUploaderInvoiceValidation(claim);
        historyService.logInvoiceValidationErrorMsg(reponse, claim);

        String repStatus = reponse.getStatus().name();

        if (!repStatus.equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            claim = service.getClaim(claim.getId());

            try {
                auditTrailService.logAuditLog(repStatus, claim, null, null);
                claim.setStatus(repStatus);
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
                return ERROR;
            }
            statusMsg = "Your action has been recorded";
            return SUCCESS;
        } else {
            this.actionResult = "ERROR : Invoice data calculation incorrect";
            return ERROR;
        }


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
        statusMsg = "Your action has been recorded";
        return result;
    }

    public String approveBREPassedClaim() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(REFER)) {
            newStatus = ClaimStatus.INVOICE_ESCALATED;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            logNewCommentForRejection(claim.getReasonOfRejectionId(), true);
        }
        try {
            auditTrailService.logAuditLog(newStatus, claim, null, null);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }

    public String approveInvoiceRefferedByEngineer() {

        String result = SUCCESS;
        String newStatus;

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        } else if (this.actionName.equalsIgnoreCase(REFER)) {
            newStatus = ClaimStatus.INVOICE_ESCALATED;
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            logNewCommentForRejection(claim.getReasonOfRejectionId(), true);
        }
        try {
            auditTrailService.logAuditLog(newStatus, claim, null, null);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
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

        statusMsg = "Your action has been recorded";
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

        statusMsg = "Your action has been recorded";
        return result;
    }
    
    /*
     * Edited By: Carlson Hoo
     * Edited Dt: 25 Feb 2009
     * Description: Add new function to Refer to Claim Handler
     */
    
    public String approveEscalatedInvoice() {

        String result = SUCCESS;
        String newStatus;
        
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        }else if(this.actionName.equalsIgnoreCase(REFER_CH)){
            newStatus = ClaimStatus.INVOICE_REF_TO_CH;            
        } else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            logNewCommentForRejection(claim.getReasonOfRejectionId(), true);
        }
        try {
            auditTrailService.logAuditLog(newStatus, claim, null, null);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);

        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }
    
    /*
     * Edited By: Carlson Hoo
     * Edited Dt: 25 Feb 2009
     * Description: Add new function to Refer to Claim Handler
     */
    
    public String approveContestedInvoice() {
        String result = SUCCESS;
        String newStatus;
        
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        }else if(this.actionName.equalsIgnoreCase(REFER_CH)){
            newStatus = ClaimStatus.INVOICE_REF_TO_CH;
        }else {
            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO;
            logNewCommentForRejection(claim.getReasonOfRejectionId(), true);
        }
        
        try {
            auditTrailService.logAuditLog(newStatus, claim, null, null);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }

    public String resubmitOrAcceptContestedInvoice() {

        String result = SUCCESS;
        String newStatus;
        Integer invoiceReasonOfRejectionId = null;

        if (this.actionName.equalsIgnoreCase(REJECT)) {

            claim = constructeClaimForInvoiceValidation(claim);
            RulesEngineResponse reponse = invoiceService.XMLUploaderInvoiceValidation(claim);
            historyService.logInvoiceValidationErrorMsg(reponse, claim);

            newStatus = ClaimStatus.CONTESTED_INVOICE_REF_TO_INS;

        } else {
            newStatus = ClaimStatus.INVOICE_REJECTED_ACCEPTED;
            invoiceReasonOfRejectionId = claim.getInvoice().getReasonOfRejectionId();
        }

        try {
            auditTrailService.logAuditLog(newStatus, claim, null, invoiceReasonOfRejectionId);
            this.claim.setStatus(newStatus);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
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
        statusMsg = "Your action has been recorded";
        return SUCCESS;
    }

    private Claim constructeClaimForInvoiceValidation(final Claim claim) {

        Claim BREClaim = claim;

        // GET HARDCODDED CHOBAND        
        claim.setChoband(choBandService.getDummyChoBand());

        // INTERFACE MAPPING WITH BRE - WHERE HIRE MONITORING NOT EXIST
        Boolean isIsTotalLostCheck = false;
        if (BREClaim.getHireMonitoringDetail() != null) {
            isIsTotalLostCheck = BREClaim.getHireMonitoringDetail().isIsTotalLostCheck();
        }

        BREClaim.getVehicleHire().setIsTotalLoss(isIsTotalLostCheck);

        // CONSTRUCTE DUMMY ENGINEERING REPORT WITH ALL VALUE IS ZERO WHEN ER NOT EXIST
        if (BREClaim.getEngineerReport() == null) {
            EngineerReport engineerreport = new EngineerReport();
            engineerreport.setDays(0);
            engineerreport.setLabourAmount(new BigDecimal("0.00"));
            engineerreport.setTotalAmount(new BigDecimal("0.00"));
            BREClaim.setEngineerReport(engineerreport);
        }

        // ADDED CLAIM SERVICES TO CHECK VEHICLE REGISTRATION NUMBER
        if (service.getCountOfClaimByVRN(BREClaim.getCustomer().getVehicleRegistration(), BREClaim.getId()) > 0) {
            BREClaim.getCustomer().setIsVehicleRegistrationExist(true);
        }

        // SET VEHICLE CLASS TO NULL WHEN 
        if (BREClaim.getThirdParty().getVehicleClass() != null) {
            if (BREClaim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached") 
                    || BREClaim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")) {
                BREClaim.getThirdParty().setVehicleClass(null);
            }
        }

        // SET VEHICLE CLASS TO NULL WHEN 
        if (BREClaim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached") 
                || BREClaim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("UNATTACHED")) {
            BREClaim.getCustomer().setVehicleClass(null);
        }

        // Mantis: 0000331
        BREClaim.setHireMonitoringEcd(hireMonitoringEcdService.getLatestHireMonitoringECDDate(BREClaim));


        return BREClaim;
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

    public int getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(int lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }

    public String getActionName() {
        return actionName;
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

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setChoBandService(ChoBandService choBandService) {
        this.choBandService = choBandService;
    }

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    public void setReasonOfRejectionService(ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setReasonForRejection(String s) {
        this.reasonForRejection = s;
    }

    public String doUpdateAnomalies() {
        String result = SUCCESS;
        try {
            claim = service.getClaim(id);
            claim.setIsAnomalies(false);
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return result;
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
            Boolean isRemovePenaltyAlert = getIsRemovePenaltyAlert();
            
            if (isRemovePenaltyAlert != null && isRemovePenaltyAlert) {
                long dateDiff = DateHelper.daysBetween(invoice.getDateInvoiced(), new Date());
                int newpenaltyAlertQty = (int) (dateDiff / 30);
                //if penaltyAlertQty > 3 mean it already reach the limit and alert not showing anymore, set it to -1
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
            if (invoice != null 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED) 
                    && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_RECEIVED)
                    && invoice.getPenaltyAlertQty() > -1) {
                result = invoice.getInvoicedDays() > (invoice.getPenaltyAlertQty() + 1) * 30;
            }
        }

        return result;
    }

    public boolean getIsShowPenaltyChargePanel() {
        boolean result = false;

        if (getIsCHO()) {
            Invoice invoice = claim.getInvoice();

            //if penaltyAlertQty = -1 mean it already reach the limit and alert not showing anymore 
            if (invoice != null && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED) && !claim.getStatus().equalsIgnoreCase(ClaimStatus.INVOICE_REJECTED_ACCEPTED)) {
                result = invoice.getInvoicedDays() > 30;
            }
        }

        return result;
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

    public boolean getIsClaimClosed() {
        boolean bFlag = false;

        if (claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_CLOSED)) {
            bFlag = true;
        }

        return bFlag;
    }
    
    public boolean getIsClaimNumberDuplicated()
    {
        boolean bFlag = false;
        
        if(!claim.getClaimNumber().isEmpty())
        {
            if (service.getClaimCountByClaimNumber(claim.getClaimNumber(), claim.getId()) > 0) {
                bFlag = true;
            }
        }

        return bFlag;
    }

    public void logNewCommentForRejection(Integer reasonOfRejectionId, boolean isPublic) {

        if (reasonOfRejectionId != null) {
            ReasonOfRejection reasonOfRejection = reasonOfRejectionService.getObject(claim.getReasonOfRejectionId());
            createNewNote(reasonOfRejection.getName(), isPublic, "Reason For Rejection: ");
        }
    }
}
