/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.model.AuditTrail;
import idas.chox.core.workflow.*;
import idas.chox.core.model.Claim;
import idas.chox.core.util.DateHelper;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author emmanuel
 */
public abstract class BaseActivity implements Activity {

    protected WorkflowContext processContext;

    @Override
    public void setProcessContext(WorkflowContext processContext) {
        this.processContext = processContext;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void process(Claim claim) throws Exception {
        validate(claim);
        doProcess(claim);
        onProcessCompleted(claim);
    }

    protected void validate(Claim claim) throws Exception {
        if (claim.getStatus().equalsIgnoreCase(getCurrentStatus())) {
            throw new InvalidClaimStatusException(claim);
        }
    }

    protected void onProcessCompleted(Claim claim) {
        logTransaction(claim);
    }

    protected void logTransaction(Claim claim) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setClaim(claim);
        auditTrail.setNewStatus(getNextStatus());
        auditTrail.setOriginalStatus(getCurrentStatus());
        auditTrail.setUpdateDate(DateHelper.getCurrentTimeStamp());
        //auditTrail.setUser(getCurrentUser());
        processContext.getDataService().save(auditTrail);
    }

    protected abstract void doProcess(Claim claim);

    protected abstract String getCurrentStatus();

    protected abstract String getNextStatus();
}
