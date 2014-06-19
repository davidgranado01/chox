package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.DataService;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.WorkflowContext;
import idas.chox.service.security.ApplicationAccessibility;



public abstract class BaseActivity implements Activity {
    private static final Logger LOG = LoggerFactory.getLogger(BaseActivity.class);
    private WorkflowContext processContext;
    private Activity chainActivity;
    private String currentStatus;
    private String message;
    @Autowired
    private UserWorkgroupService userWorkgroupService;
    @Autowired
    private ApplicationAccessibility applicationAccessibility;
    @Autowired
    protected ActivityEventGenerator activityEventGenerator;
    @Autowired
    protected ClaimService claimService;

    public void setActivityEventGenerator(ActivityEventGenerator activityEventGenerator) {
        this.activityEventGenerator = activityEventGenerator;
    }

    /*
     * xmlActivityProcessing used to identify the caller (UI or XML), if called from XML upload and differnt check needed for different caller this can be set to true, default false.
     * 
     */
    private boolean xmlActivityProcessing;

    public boolean isXmlActivityProcessing() {
        return xmlActivityProcessing;
    }
    
    public boolean needsOwnershipCheck() {
        return true;
    }

    public boolean needsClaimLockedCheck() {
        return false;
    }

    @Override
    public void setXmlActivityProcessing(boolean xmlActivityProcessing) {
        this.xmlActivityProcessing = xmlActivityProcessing;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserWorkgroupService(UserWorkgroupService userWorkgroupService) {
        this.userWorkgroupService = userWorkgroupService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void setChainActivity(Activity nextActivity) {
        this.chainActivity = nextActivity;
    }

    @Override
    public void setWorkflowContext(WorkflowContext processContext) {
        this.processContext = processContext;
    }

    public WorkflowContext getWorkflowContext() {
        return getProcessContext();
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void process(Claim claim) throws Exception {
            processInBatch(claim);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void processInBatch(Claim claim) throws Exception {

        if (claim == null) {
            throw new Exception("Invalid claim object.");
        }

        if (isRequired(claim)) {

            currentStatus = claim.getStatus();
            LOG.debug("current Status: {}", getCurrentStatus());
            validate(claim);
            LOG.debug("Claim validated.");
            beforeProcess(claim);
            LOG.debug("Claim beforeProcessed.");
            doProcess(claim);
            LOG.debug("Claim doProcessed.");
            afterProcess(claim);
            LOG.debug("Claim afterProcessed.");
        }
    }

    protected boolean isRequired(Claim claim) {
        return true;
    }

    protected void beforeProcess(Claim claim) throws Exception {
    }

    protected void validate(Claim claim) throws Exception {
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        
        if (applicationAccessibility.checkActivityAccessibility(getClass().getSimpleName(),
                securityInfoProvider.getCurrentUser(), claim) < 1) {
            LOG.error("No access to activity '{}' for claim '{}' of type {} in status '{}'",
                    new Object[]{getClass().getSimpleName(), claim.getChoReference(),
                                 claim.getClaimType().name(), claim.getStatus()});
            throw new AccessDeniedException("No access to activity " + getClass().getSimpleName());
        }
        
        // Check that, if we are an insurer or CHO, then the claim belongs to us
        if (needsOwnershipCheck() && ((securityInfoProvider.getIsINS() &&
                claim.getInsurer().getId().intValue() != securityInfoProvider.getCurrentUser().getInsurer().getId().intValue())
                || (securityInfoProvider.getIsCHO() &&
                claim.getChorganisation().getId().intValue() != securityInfoProvider.getCurrentUser().getChorganisation().getId().intValue()))) {
                LOG.error("User with id={} has attempted to action claim '{}' from a different organisation", getCurrentUser().getId(), claim.getChoReference());
                throw new AccessDeniedException("Attempt to action a claim that you do not own");
        }
        // If Insurer is locked and claim ownership is enabled, and if the user is a CH, then the user must own the claim
        if (needsClaimLockedCheck() && claim.getInsurer().isClaimLocked() && claim.getInsurer().isClaimOwnershipEnable() && securityInfoProvider.getIsINS()
                && (securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_CH))// || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_COM) || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_FNOL))
                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)) {
            if (claim.getClaimOwner() == null || claim.getClaimOwner().getId().intValue() != getCurrentUser().getId().intValue()) {
                LOG.error("User {} has attempted to action claim '{}' which he does not own.", getCurrentUser().getId(), claim.getChoReference());
                throw new AccessDeniedException("Attempt to action a claim that you do not own");
            }
        }
        
        // If Insurer is locked and workgroups are enabled, and if the user is a COM or FNOL, then the user must be in the same workgroup
        if (needsClaimLockedCheck() && claim.getInsurer().isClaimLocked() && claim.getInsurer().isWorkgroupEnable() && securityInfoProvider.getIsINS()
                && (securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_COM) || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_FNOL))
                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)) {
            if (claim.getWorkgroup() == null || !userWorkgroupService.isUserWorkgroupExist(claim.getWorkgroup().getId(), getCurrentUser().getId())) {
                LOG.error("User {} has attempted to action claim '{}' which is not in a workgroup to which they belong.", getCurrentUser().getId(), claim.getChoReference());
                throw new AccessDeniedException("Attempt to action a claim to which you do not have access");
            }
            
        }
    }

    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
        getDataService().save(claim);
        LOG.debug("Claim saved - logging transaction...");
        logTransaction(claim);
        LOG.debug("Claim saved & transaction logged.");

        activityEventGenerator.generate(claim, this);
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

    protected abstract void doProcess(Claim claim) throws Exception;


    // <editor-fold defaultstate="collapsed" desc="Member functions">
    protected WebUser getCurrentUser() {
        return getWorkflowContext().getSecurityInfoProvider().getCurrentUser();
    }

    protected DataService getDataService() {
        return getWorkflowContext().getDataService();
    }

    protected String getCurrentStatus() {
        return currentStatus;
    }

    protected void logTransaction(Claim claim) {
        logTransaction(claim, getCurrentStatus(), claim.getStatus(), 0);
    }

    protected void logTransaction(Claim claim, Integer timeInterval) {
        logTransaction(claim, getCurrentStatus(), claim.getStatus(), timeInterval);
    }

    protected void logTransaction(Claim claim, String currentStatus, String nextStatus) {
        logTransaction(claim, currentStatus, nextStatus, 0);
    }

    protected void logTransaction(Claim claim, String currentStatus, String nextStatus, Integer timeInterval) {
        LOG.debug("Logging transaction/audit trail for claim '{}'", claim.getChoReference());
        LOG.debug("Current status='{}', next status='{}'", currentStatus, nextStatus);
        if (!currentStatus.equalsIgnoreCase(nextStatus)) {
            LOG.debug("Creating new audit trail record for currentStatus='{}', nextStatus='{}'", currentStatus, nextStatus);
            AuditTrail auditTrail = new AuditTrail();
            auditTrail.setClaim(claim);
            auditTrail.setNewStatus(nextStatus);
            auditTrail.setOriginalStatus(currentStatus);

            Date currentDate = DateHelper.getCurrentDateTime();
            currentDate.setTime(currentDate.getTime() + (timeInterval * 1000));

            auditTrail.setUpdateDate(currentDate);
            auditTrail.setUser(getCurrentUser());
            setPreviousTotalToPay(claim, auditTrail);
            getDataService().save(auditTrail);
        }
    }

    protected void logTransaction(Claim claim, String currentStatus, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection) {
        logTransaction(claim, currentStatus, claimReasonOfRejection, invoiceReasonOfRejection, 0);
    }

    protected void logTransaction(Claim claim, String currentStatus, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection, Integer timeInterval) {

        if (!currentStatus.equalsIgnoreCase(claim.getStatus())) {
            Date currentDate = DateHelper.getCurrentDateTime();
            currentDate.setTime(currentDate.getTime() + (timeInterval * 1000));

            AuditTrail auditTrail = new AuditTrail();
            auditTrail.setClaim(claim);
            auditTrail.setNewStatus(claim.getStatus());
            auditTrail.setOriginalStatus(currentStatus);
            auditTrail.setUpdateDate(currentDate);
            auditTrail.setUser(getCurrentUser());

            if (claimReasonOfRejection != null) {
                auditTrail.setClaimReasonOfRejection(claimReasonOfRejection);
            }

            if (invoiceReasonOfRejection != null) {
                auditTrail.setInvoiceReasonOfRejection(invoiceReasonOfRejection);
            }
            setPreviousTotalToPay(claim, auditTrail);
            getDataService().save(auditTrail);
        }
    }

    /**
     * @return the processContext
     */
    public WorkflowContext getProcessContext() {
        return processContext;
    }

    /**
     * @return the chainActivity
     */
    public Activity getChainActivity() {
        return chainActivity;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
    
    private void setPreviousTotalToPay(Claim claim, AuditTrail auditTrail) {

        /*
         * To-do Item - 7.2.2- If a claim moves into the status 'InvoiceRejectionAccepted' 
         * or 'ClaimClosed' then the 'Total To Pay' should be set to £0.00.
         */
        if (claim.getInvoice() != null
                && (claim.getStatus().equals(ClaimStatus.CLAIM_CLOSED)
                || claim.getStatus().equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED))) {
            auditTrail.setPreviousTotalToPay(claim.getInvoice().getTotalToPay());
            claim.getInvoice().setTotalToPay(BigDecimal.ZERO.setScale(2));
        }
    }


}
