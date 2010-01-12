/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.workflow.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.DataService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseActivity implements Activity {

    protected WorkflowContext processContext;
    protected Activity chainActivity;
    protected String currentStatus;
    protected List<String> expectingStatuses;

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
    public void processInBatch(Claim claim) throws Exception {

        if (claim == null) {
            throw new Exception("Invalid claim object.");
        }

        if (isRequired(claim)) {

            currentStatus = claim.getStatus();

            validate(claim);
            beforeProcess(claim);
            doProcess(claim);
            afterProcess(claim);
        }
    }

    protected boolean isRequired(Claim claim) {
        return true;
    }

    protected void beforeProcess(Claim claim) throws Exception {
    }

    protected void validate(Claim claim) throws Exception {

        if (!expectingStatuses.contains(claim.getStatus())) {
            throw new InvalidClaimStatusException(claim);
        }
    }

    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim);

        if (chainActivity != null) {
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
        if (!currentStatus.equalsIgnoreCase(nextStatus)) {
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
            auditTrail.setOriginalStatus(claim.getStatus());
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
    // </editor-fold>
}
