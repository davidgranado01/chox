/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.viewdata;

import chox.model.HireMonitoringEcd;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdViewData {

    private String sequence;
    private String ecdDate;
    private String reason;
    private String supportingNote;

    public HireMonitoringEcdViewData(HireMonitoringEcd h, int seq) {

        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.ecdDate = dateFormat.format(h.getEcdDate());
        this.sequence = String.format("%1d", seq);
        this.reason = h.getReason();
        this.supportingNote = h.getSupportingNote();
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
