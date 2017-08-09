package idas.chox.service.workflow.activities;


import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.nodes.Document.OutputSettings;

import idas.chox.core.model.Claim;
import idas.chox.core.model.InsurerHireMonitoringEcd;
import idas.chox.core.model.ReasonOfDelay;
import idas.chox.core.services.InsurerHireMonitoringEcdService;
import idas.chox.core.services.ReasonOfDelayService;

public class InsurerEcdUpdate extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(InsurerEcdUpdate.class);
    private InsurerHireMonitoringEcdService insurerHireMonitoringEcdService;
    private ReasonOfDelayService reasonOfDelayService;
    private int reasonOfDelayId = -1;
    private Date ecdDate;
    private String reason;
    private String supportingNote;
    private int sequence;

    public void setReasonOfDelayService(ReasonOfDelayService reasonOfDelayService) {
        this.reasonOfDelayService = reasonOfDelayService;
    }
    
    public void setInsurerHireMonitoringEcdService(InsurerHireMonitoringEcdService insurerHireMonitoringEcdService) {
        this.insurerHireMonitoringEcdService = insurerHireMonitoringEcdService;
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
        this.supportingNote = supportingNote.trim();
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getReasonOfDelayId() {
        return reasonOfDelayId;
    }

    public Date getEcdDate() {
        return ecdDate;
    }

    public String getReason() {
        return reason;
    }

    public String getSupportingNote() {
        return supportingNote;
    }

    public int getSequence() {
        return sequence;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (reasonOfDelayId > 0 && reason == null) {
            ReasonOfDelay reasonOfDelayObject = reasonOfDelayService.getReasonOfDelay(reasonOfDelayId);
            reason = reasonOfDelayObject.getName();
        }
        if (reason == null) {
            LOG.warn("ECD delay reason is null. Can not update ECD.");
            throw new Exception("ECD delay reason is null. Can not update ECD.");
        }
        String supportingNoteClean = StringEscapeUtils.unescapeHtml4(Jsoup.clean(supportingNote.replaceAll("\n", "<br />"), "", Whitelist.basic(), new OutputSettings().prettyPrint(false)));
//        if (!supportingNote.equals(supportingNoteClean)) {
//            LOG.warn("Supporting note contains forbidden content - possible XSS attack: [clean] '{}' != '{}'", supportingNoteClean, supportingNote);
//            throw new Exception("Supporting note contains forbidden content");
//        }
        supportingNote = supportingNoteClean;
        // Check there is no existig ECD with same date and reason (bug#2621)
        List<InsurerHireMonitoringEcd> existingECDs = insurerHireMonitoringEcdService.getInsurerHireMonitoringEcdsByClaimId(claim.getId());
        for (InsurerHireMonitoringEcd existingECD : existingECDs) {
            if (existingECD.getEcdDate().compareTo(ecdDate) == 0 && existingECD.getReason().compareTo(reason) == 0) {
                LOG.warn("ECD Update Already Exists.");
                throw new Exception("ECD Update Already Exists");
            }
        }
    }

    @Override
    protected void doProcess(Claim claim) {

        InsurerHireMonitoringEcd ecd = new InsurerHireMonitoringEcd();
        ecd.setEcdDate(ecdDate);
        ecd.setReason(reason);
        ecd.setSupportingNote(supportingNote);
        ecd.setSequence(sequence);

        insurerHireMonitoringEcdService.addNewInsurerHireMonitoringEcd(claim, ecd);
    }
}