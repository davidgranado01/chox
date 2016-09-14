package idas.chox.web.viewdata;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.InsurerHireMonitoringEcd;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdViewData {
    private static final Logger LOG = LoggerFactory.getLogger(HireMonitoringEcdViewData.class);

    private String sequence;
    private String ecdDate;
    private String createdDate;
    private String reason;
    private String supportingNote;

    public HireMonitoringEcdViewData(HireMonitoringEcd h, int seq) {
        if (h != null) {
            Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (h.getEcdDate() != null) {
                this.ecdDate = dateFormat.format(h.getEcdDate());
            } else {
                LOG.error("ECD date is null");
            }
            if (h.getCreatedDate() != null) {
                this.createdDate = dateFormat.format(h.getCreatedDate());
            } else {
                LOG.error("Created date is null");
            }
            this.sequence = String.format("%1d", seq);
            this.reason = h.getReason();
            this.supportingNote = h.getSupportingNote();
        } else {
            LOG.error("No Hire Monitoring ECD to format.");
        }
    }
    
    public HireMonitoringEcdViewData(InsurerHireMonitoringEcd h, int seq) {
        if (h != null) {
            Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (h.getEcdDate() != null) {
                this.ecdDate = dateFormat.format(h.getEcdDate());
            } else {
                LOG.error("ECD date is null");
            }
            if (h.getCreatedDate() != null) {
                this.createdDate = dateFormat.format(h.getCreatedDate());
            } else {
                LOG.error("Created date is null");
            }
            this.sequence = String.format("%1d", seq);
            this.reason = h.getReason();
            this.supportingNote = h.getSupportingNote();
        } else {
            LOG.error("No Insurer Hire Monitoring ECD to format.");
        }
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getEcdDate() {
        return ecdDate;
    }

    public void setEcdDate(String ecdDate) {
        this.ecdDate = ecdDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSupportingNote() {
        return supportingNote;
    }

    public void setSupportingNote(String supportingNote) {
        this.supportingNote = supportingNote;
    }
}
