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
import java.util.Date;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author emmanuel
 */
public abstract class BaseActivity implements Activity {

    private WorkflowContext processContext;

    @Override
    public void setProcessContext(WorkflowContext processContext) {
        this.processContext = processContext;
    }

    public WorkflowContext getProcessContext() {
        return processContext;
    }

    @Override
    public void process(Claim claim) throws Exception {
        prepare(claim);
        validate(claim);
        doProcess(claim);
        onProcessCompleted(claim);
    }

    @Override
    public void processInBatch(Claim claim) throws Exception {
        prepare(claim);
        validate(claim);
        doProcess(claim);
        onProcessCompleted(claim);
    }

    protected void prepare(Claim claim) throws Exception {
    }

    protected void validate(Claim claim) throws Exception {
        if (!getCurrentStatus().isEmpty()) {
            if (!claim.getStatus().equalsIgnoreCase(getCurrentStatus())) {
                throw new InvalidClaimStatusException(claim);
            }
        }
        else
        {
            throw new InvalidClaimStatusException(claim);
        }
    }

    protected void onProcessCompleted(Claim claim) {
        getDataService().save(claim);
        logTransaction(claim);
    }

    protected abstract void doProcess(Claim claim) throws Exception;

    protected abstract String getCurrentStatus();

    protected abstract String getNextStatus();

    // <editor-fold defaultstate="collapsed" desc="Member functions">
    protected WebUser getCurrentUser() {
        return getProcessContext().getSecurityInfoProvider().getCurrentUser();
    }

    protected DataService getDataService() {
        return getProcessContext().getDataService();
    }

    protected void logTransaction(Claim claim) {
        logTransaction(claim, getCurrentStatus(), getNextStatus());
    }

    protected void logTransaction(Claim claim, String currentStatus, String nextStatus) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setClaim(claim);
        auditTrail.setNewStatus(nextStatus);
        auditTrail.setOriginalStatus(currentStatus);
        auditTrail.setUpdateDate(DateHelper.getCurrentDateTime());
        auditTrail.setUser(getCurrentUser());
        getDataService().save(auditTrail);
    }

    protected void logTransaction(Claim claim, String nextStatus, String currentStatus, Integer secInteval) {

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setClaim(claim);
        auditTrail.setNewStatus(nextStatus);
        auditTrail.setOriginalStatus(currentStatus);

        Date currentDate = DateHelper.getCurrentDateTime();
        currentDate.setTime(currentDate.getTime() + secInteval);

        auditTrail.setUpdateDate(currentDate);
        auditTrail.setUser(getCurrentUser());
        getDataService().save(auditTrail);
    }

    protected void logTransaction(Claim claim, String newStatus, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection) {


        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setClaim(claim);
        auditTrail.setNewStatus(newStatus);
        auditTrail.setOriginalStatus(claim.getStatus());
        auditTrail.setUpdateDate(DateHelper.getCurrentDateTime());
        auditTrail.setUser(getCurrentUser());

        if (claimReasonOfRejection != null) {
            auditTrail.setClaimReasonOfRejection(claimReasonOfRejection);
        }

        if (invoiceReasonOfRejection != null) {
            auditTrail.setInvoiceReasonOfRejection(invoiceReasonOfRejection);
        }

        getDataService().save(auditTrail);

    }

    protected void logTransaction(Claim claim, String newStatus, ReasonOfRejection claimReasonOfRejection, ReasonOfRejection invoiceReasonOfRejection, Integer secInteval) {

        Date currentDate = DateHelper.getCurrentDateTime();
        currentDate.setTime(currentDate.getTime() + secInteval);

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setClaim(claim);
        auditTrail.setNewStatus(newStatus);
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
    // </editor-fold>
}
