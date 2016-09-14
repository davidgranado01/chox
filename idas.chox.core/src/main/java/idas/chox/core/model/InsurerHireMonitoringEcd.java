package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class InsurerHireMonitoringEcd extends Entity implements Serializable {

    /**
     * This attribute maps to the column ecd_date in the hire_monitoring_ecd table.
     */
    private Date ecdDate;
    /**
     * This attribute maps to the column sequence in the hire_monitoring_ecd table.
     */
    private int sequence;
    /**
     * This attribute maps to the column reason_id in the hire_monitoring_ecd table.
     */
    private String reason;
    /**
     * This attribute represents the foreign key relationship to the claim table.
     */
    private Claim claim;
    private String supportingNote;

    /**
     * Method 'getEcdDate'
     *
     * @return java.util.Date
     */
    public java.util.Date getEcdDate() {
        return ecdDate;
    }

    /**
     * Method 'setEcdDate'
     *
     * @param ecdDate
     */
    public void setEcdDate(java.util.Date ecdDate) {
        this.ecdDate = ecdDate;
    }

    /**
     * Method 'getSequence'
     *
     * @return int
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Method 'setSequence'
     *
     * @param sequence
     */
    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    /**
     * Method 'getReasonId'
     *
     * @return int
     */
    public String getReason() {
        return reason;
    }

    /**
     * Method 'setReasonId'
     *
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Method 'getClaim'
     *
     * @return Claim
     */
    public Claim getClaim() {
        return claim;
    }

    /**
     * Method 'setClaim'
     *
     * @param claim
     */
    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getSupportingNote() {
        return supportingNote;
    }

    public void setSupportingNote(String supportingNote) {
        this.supportingNote = supportingNote;
    }
}
