/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import idas.chox.core.model.AuditTrail;
import idas.chox.core.workflow.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.DataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;



public abstract class BaseActivity implements Activity {
    private static final Logger LOG = LoggerFactory.getLogger(BaseActivity.class);

    protected WorkflowContext processContext;
    protected Activity chainActivity;
    protected String currentStatus;
    protected List<String> expectingStatuses;
    
    /*
     * xmlActivityProcessing used to identify the caller (UI or XML), if called from XML upload and differnt check needed for different caller this can be set to true, default false.
     * 
     */
    private boolean xmlActivityProcessing;

    public boolean isXmlActivityProcessing() {
        return xmlActivityProcessing;
    }

    @Override
    public void setXmlActivityProcessing(boolean xmlActivityProcessing) {
        this.xmlActivityProcessing = xmlActivityProcessing;
    }

    public BaseActivity() {
        expectingStatuses = new ArrayList<String>();
        setupExpectingStatuses(expectingStatuses);
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
        return processContext;
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
            LOG.debug("current Status: {}", currentStatus);
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
        
        if (!expectingStatuses.contains(claim.getStatus())) {
            LOG.warn("Invalid status found: {}", claim.getStatus());
            LOG.warn("Expecting one of: ({})", expectingStatuses);
            throw new InvalidClaimStatusException(claim);
        }
    }

    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
        getDataService().save(claim);
        logTransaction(claim);

        if (chainActivity != null) {
            LOG.debug("Processing next chain activity.");
            chainActivity.setWorkflowContext(processContext);
            chainActivity.processInBatch(claim);
        }
    }

    protected abstract void doProcess(Claim claim) throws Exception;

    protected abstract void setupExpectingStatuses(List<String> expectingStatuses);

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
        LOG.debug("Logging transaction/audit trail for claim '{}", claim.getChoReference());
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

            getDataService().save(auditTrail);
        }
    }
}
