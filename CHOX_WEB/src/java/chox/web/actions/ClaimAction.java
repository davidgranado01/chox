/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.EngineerReport;
import chox.services.ClaimService;
import chox.services.InvoiceService;
import chox.services.ChoBandService;
import chox.services.LookupService;
import chox.web.data.PanelAction;
import chox.web.security.ApplicationAccessibility;
import chox.web.security.TabAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.acegisecurity.GrantedAuthority;
import scsbre.engine.RulesEngineResponse;
import chox.data.AttachmentCategory;
import chox.model.Chorganisation;
import chox.model.Comment;
import chox.model.Insurer;
import chox.model.LookupItem;
import chox.model.WebUser;
import chox.services.AuditTrailService;
import chox.services.CommentService;
import chox.services.HireMonitoringEcdService;
import chox.services.HistoryService;
import java.util.ArrayList;
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
    
    private Claim claim = new Claim();
    private int id = -1;
    private List lineOfBusinesses;
    private List vehicleClasses;
    private List insurers;
    private List statuses;
    private ClaimService service;
    private LookupService lookupService;
    private InvoiceService invoiceService;
    private ChoBandService choBandService;
    private HistoryService historyService;
    private AuditTrailService auditTrailService;
    private HireMonitoringEcdService hireMonitoringEcdService;
    private CommentService commentService;
    private String actionResult;
    private TabAccessibility tabAccessibility;
    private int vehicleClassId = -1;
    private int lineOfBusinessId = -1;
    private int insurerId = -1;
    private String actionName;
    private List attachmentCategory;
    private String statusMsg="";
    private String reasonForRejection;
    
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

    public String getActionResult() {
        return actionResult;
    }

    public String updateClaimDetail() {
        this.service.updateClaim(claim);
        this.actionResult = "Claim Updated!";
        return SUCCESS;
    }

    public TabAccessibility getTabAccessibility() {

        if (tabAccessibility == null) {
            tabAccessibility = new TabAccessibility(getAuthenticatedUser().getAuthorities(), claim.getStatus());
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
            
            short accessRight = ApplicationAccessibility.getInstance().checkActionAccessibility(action, grantedAuthorities, claim.getStatus());

            if (accessRight > 0) {
                return action;
            }
        }

        return EMPTY;
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
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                try {
                    auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
                    
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
        //chack whether line of busineess if set 

        if (this.actionName.equalsIgnoreCase(ACCEPT)) 
        {
            String validationResult = validateAcknowledgeClaimInfo();
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            } else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }   
        } 
        else if(this.actionName.equalsIgnoreCase(REFER))
        {
            String validationResult = validateAcknowledgeClaimInfo();            
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
            } else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }
        }
        else if(this.actionName.equalsIgnoreCase(REFER_FNOL))
        {
            String validationResult = validateAcknowledgeClaimInfo();            
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
            } else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }
        }
        else 
        {
            claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        }

        try 
        {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        statusMsg = "Your action has been recorded";
        return result;
    }
    
    // FNOL
    public String registerFNOL() {

        String result = SUCCESS;

        if (this.actionName.equalsIgnoreCase(REGISTER_FNOL)) 
        {
            String validationResult = validateAcknowledgeClaimInfo();
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
            } else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }   
        }
        else if(this.actionName.equalsIgnoreCase(REJECT_FNOL))
        {
            String validationResult = validateAcknowledgeClaimInfo();            
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.CLAIM_REJECTED);
                
            } else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }
        }
        
        createNewNote(reasonForRejection);
        
        try 
        {        
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        statusMsg = "Your action has been recorded";
        return result;
    }
    
    private void createNewNote(String sComment){
        
        if(sComment.length()>0){
            Comment comment = new Comment();
            comment.setComment(sComment);
            comment.setClaim(claim);

            try {
                commentService.createNewObject(comment);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }
    }
    
    private String validateAcknowledgeClaimInfo() {
        
        String returnStr = "";
        
        String claimNumber = claim.getClaimNumber().trim();
        
        if(claimNumber.length()>0){
            int claimId = claim.getId();
            boolean isClaimNumberExist = this.service.getClaimCountByClaimNumber(claimNumber, claimId) > 0;
            if (isClaimNumberExist) {
                returnStr = "ERROR : The Claim number you have supplied already exists";
            } else {
                returnStr = "";
            }
        }
        
        return returnStr;
    }
    
    public String reviewByEngineer() {
        String result = SUCCESS;
        //chack whether line of busineess if set 
        claim.setStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }

    public String contestOrAcceptRejectedClaim() {

        String result = SUCCESS;
        
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        }
        
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
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
        
        String validationResult = validateAcknowledgeClaimInfo();
        
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            }else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }
        
        }else if(this.actionName.equalsIgnoreCase(REFER)){
                      
            if (validationResult.isEmpty()) {
                claim.setStatus(ClaimStatus.CLAIM_REF_TO_ENG);
            } else {
                claim.setClaimNumber("");
                result = ERROR;
                this.actionResult = validationResult;
            }
            
        }else {
            claim = service.getClaim(id);
            claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        }
        
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }

    public String submitHireMonitoringDetail() {

        String result = SUCCESS;
        String validationResult = validateHireMonitoringDetail();
        if (validationResult.isEmpty()) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_DATA);
        } else {
            result = ERROR;
            this.actionResult = validationResult;
        }

        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
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
        
        if(this.claim.getCustomer() == null || this.claim.getCustomer().getInitialECD() == null)
        {
            if(this.service.getECDCountByClaimId(this.claim.getId()) == 0)
            {
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
            claim.setStatus(repStatus);
            try {
                auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
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

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        }
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
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
        if (this.actionName.equalsIgnoreCase(ACCEPT)) 
        {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } 
        else if(this.actionName.equalsIgnoreCase(REFER))
        {
            claim.setStatus(ClaimStatus.INVOICE_ESCALATED);
        }
        else 
        {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        }
        try 
        {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } 
        catch (Exception ex)
        {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }
   
    public String approveEscalatedInvoice() {

        String result = SUCCESS;
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        }
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;
    }
    
    public String approveContestedInvoice() {
        String result = SUCCESS;
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        }
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
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

        if (this.actionName.equalsIgnoreCase(REJECT)) {
            claim = constructeClaimForInvoiceValidation(claim);
            RulesEngineResponse reponse = invoiceService.XMLUploaderInvoiceValidation(claim);
            historyService.logInvoiceValidationErrorMsg(reponse, claim);

            claim = service.getClaim(claim.getId());
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);

        } else {
            claim.setStatus(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        }

        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return result;

    }

    public String logInvoicePayment() {
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        try {
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        statusMsg = "Your action has been recorded";
        return SUCCESS;
    }

    private Claim constructeClaimForInvoiceValidation(Claim claim) {

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
        if(service.getCountOfClaimByVRN(BREClaim.getCustomer().getVehicleRegistration(), BREClaim.getId())>0){
            BREClaim.getCustomer().setIsVehicleRegistrationExist(true);
        }
        
        // SET VEHICLE CLASS TO NULL WHEN 
        if (BREClaim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached")) {
            BREClaim.getThirdParty().setVehicleClass(null);
        }

        // SET VEHICLE CLASS TO NULL WHEN 
        if (BREClaim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached")) {
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

    public int getHireMonitoringDetailId() {
        return this.claim.getHireMonitoringDetail() == null ? 0 : this.claim.getHireMonitoringDetail().getId();
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

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService){
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }
    
    public void setCommentService(CommentService commentService){
        this.commentService = commentService;
    }    
            
    public String getStatusMsg() {
        return statusMsg;
    }
    
    public void setReasonForRejection(String s){
        this.reasonForRejection = s;
    }
    
    public String doUpdateAnomalies() {
        String result = SUCCESS;
        try {
            claim = service.getClaim(id);
            claim.setIsAnomalies(false);
            auditTrailService.logAuditLog(claim.getStatus(), claim.getId());
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            result = ERROR;
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return result;
    }
    
}
