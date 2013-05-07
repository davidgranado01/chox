package idas.chox.service.workflow.activities;


import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.ReasonOfDelay;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.core.services.ReasonOfDelayService;

public class EcdUpdate extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(EcdUpdate.class);
    private HireMonitoringEcdService hireMonitoringEcdService;
    private ReasonOfDelayService reasonOfDelayService;
    private int reasonOfDelayId = -1;
    private Date ecdDate;
    private String reason;
    private String supportingNote;
    private boolean updateInsurer;
    private int sequence;

    public void setReasonOfDelayService(ReasonOfDelayService reasonOfDelayService) {
        this.reasonOfDelayService = reasonOfDelayService;
    }
    
    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }

    public void setReasonOfDelayId(int reasonOfDelayId) {
        this.reasonOfDelayId = reasonOfDelayId;
    }

    public void setEcdDate(Date ecdDate) {
        this.ecdDate = ecdDate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSupportingNote(String supportingNote) {
        this.supportingNote = supportingNote;
    }

    public void setUpdateInsurer(boolean updateInsurer) {
        this.updateInsurer = updateInsurer;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (reasonOfDelayId > 0 && reason == null) {
            ReasonOfDelay reasonOfDelayObject = reasonOfDelayService.getReasonOfDelay(reasonOfDelayId);
            reason = reasonOfDelayObject.getName();
        }
        if (reason == null) {
            LOG.error("ECD delay reason is null. Can not update ECD.");
            throw new Exception("ECD delay reason is null. Can not update ECD.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {

        HireMonitoringEcd ecd = new HireMonitoringEcd();
        ecd.setEcdDate(ecdDate);
        ecd.setReason(reason);
        ecd.setSupportingNote(supportingNote);
        ecd.setUpdateInsurer(updateInsurer);
        ecd.setSequence(sequence);

        hireMonitoringEcdService.addNewHireMonitoringEcd(claim, ecd);
    }
}