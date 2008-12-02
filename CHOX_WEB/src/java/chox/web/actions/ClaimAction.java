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
import chox.services.InvoiceServiceImpl;
import chox.services.ChoBandService;
import chox.services.ChoBandServiceImpl;
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

/**
 *  
 * @author Emmanuel
 */
public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable {

    public static final String REJECT = "reject";
    public static final String ACCEPT = "accept";
    public static final String EMPTY = "empty";
    private Claim claim = new Claim();
    private int id = -1;
    private List lineOfBusinesses;
    private List statuses;
    private ClaimService service;
    private LookupService lookupService;
    private String actionResult;
    private TabAccessibility tabAccessibility;
    private int lineOfBusinessId = -1;
    private String actionName;

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
        if (id == -1) {
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

    public String getActionResult() {
        return actionResult;
    }

    public String updateClaimDetail() {
        this.service.updateClaim(claim);
        this.actionResult = "Claim Updated!";
        return SUCCESS;
    }

    public String updateIncident() {
        this.service.updateIncident(claim.getIncident());
        this.actionResult = "Incident Updated!";
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
                    this.service.updateClaim(claim);
                } catch (Exception ex) {
                    this.actionResult = "ERROR : " + ex.getMessage();
                }
            }
        }

        return SUCCESS;
    }

    //Acknowledge
    public String acknowledge() {
        //chack whether line of busineess if set 
        if (validateAcknowledgeClaimInfo()) {

            if (this.actionName.equalsIgnoreCase(ACCEPT)) {
                claim.setStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            } else {
                claim.setStatus(ClaimStatus.CLAIM_REJECTED);
            }
            try {
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }

        } else {
            this.actionResult = "ERROR : You need to provide correct detail to acknowledge this claim.";
        }
        return SUCCESS;
    }

    public Map getAcknowledgeClaimActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Request invoice data");
        names.put(REJECT, "Reject this claim");
        return names;
    }

    private boolean validateAcknowledgeClaimInfo() {

        String claimNumber = claim.getClaimNumber();
        BigDecimal indemintyAmount = claim.getIndemintyAmount();
        BigDecimal percentageLiabilityAccepted = claim.getPercentageLiabilityAccepted();
        String engineerClaimReviewNotes = claim.getEngineerClaimReviewNotes();

        boolean result = true;
        result = result && (claimNumber != null && !claimNumber.isEmpty());
        result = result && (percentageLiabilityAccepted != null);
        result = result && (engineerClaimReviewNotes != null);

        return result;
    }
    
     public String contestOrAcceptRejectedClaim() {
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return SUCCESS;
    }

    public Map getContestOrAcceptRejectedClaimActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Accept rejection decision");
        names.put(REJECT, "Contest this claim");
        return names;
    }

     public String approveContestedClaim() {
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }

        return SUCCESS;
    }
    
    public Map getApproveContestedClaimActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Request invoice data");
        names.put(REJECT, "Reject this claim");
        return names;
    }
        
    public String submitHireMonitoringDetail() {
        if (validateHireMonitoringDetail()) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_DATA);
            try {
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        } else {
            this.actionResult = "ERROR : You need to provide correct hire monitoring detail detail to submit this claim.";
        }

        return SUCCESS;
    }   
    
    public String reSubmitRejectedClaim() {
        claim = constructeClaimForInvoiceValidation(claim);
        InvoiceService invoiceService = new InvoiceServiceImpl();
        RulesEngineResponse rep = invoiceService.XMLUploaderInvoiceValidation(claim);
        String repStatus = rep.getStatus().name();

        if (!repStatus.equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            claim.setStatus(repStatus);
            try {
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
                return ERROR;
            }
            return SUCCESS;
        } else {
            this.actionResult = "ERROR : Invoice data calculation incorrect";
            return ERROR;
        }


    }

    public String contestOrAcceptRejectedInvoice() {
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return SUCCESS;
    }

    public String approveBREPassedClaim() {

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }


        return SUCCESS;
    }    
    
    public Map getApproveBREPassedClaimActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Clear for payment");
        names.put(REJECT, "Reject Invoice");
        return names;
    }

    public String approveEscalatedInvoice() {

        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return SUCCESS;
    }
    
     public Map getApproveEscalatedInvoiceActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Clear for payment");
        names.put(REJECT, "Reject Invoice");
        return names;
    }

    public String approveContestedInvoice() {
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        } else {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return SUCCESS;
    }

    public Map getApproveContestedInvoiceActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Accept and proceed to payment");
        names.put(REJECT, "Reject the claim");
        return names;
    }

    public String resubmitOrAcceptContestedInvoice() {
        if (this.actionName.equalsIgnoreCase(ACCEPT)) {
            claim.setStatus(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        } else {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        }
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return SUCCESS;
    }

    public Map getResubmitOrAcceptContestedInvoiceActions() {
        Map names = new HashMap();
        names.put(ACCEPT, "Accept rejection decision");
        names.put(REJECT, "Reject rejection decision and resubmit claim");
        return names;
    }
    
    public String logInvoicePayment()
    {
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        try {
            this.service.updateClaim(claim);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }
        return SUCCESS;
    }

    private Claim constructeClaimForInvoiceValidation(Claim claim) {

        Claim BREClaim = claim;

        // GET HARDCODDED CHOBAND
        ChoBandService chobandservice = new ChoBandServiceImpl();
        claim.setChoband(chobandservice.getDummyChoBand());

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

        // SET VEHICLE CLASS TO NULL WHEN 
        if (BREClaim.getThirdParty().getVehicleClass().getName().equalsIgnoreCase("Unattached")) {
            BREClaim.getThirdParty().setVehicleClass(null);
        }

        // SET VEHICLE CLASS TO NULL WHEN 
        if (BREClaim.getCustomer().getVehicleClass().getName().equalsIgnoreCase("Unattached")) {
            BREClaim.getCustomer().setVehicleClass(null);
        }

        return BREClaim;
    }

    public boolean validateHireMonitoringDetail() {
        //TODO : implement validateHireMonitoringDetail
        return true;
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

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
}
