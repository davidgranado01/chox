package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class Incident extends AuditableEntity implements Serializable {

    /** 
     * This attribute maps to the column date in the incident table.
     */
    protected Date date;
    /** 
     * This attribute maps to the column location in the incident table.
     */
    protected String location;
    /** 
     * This attribute maps to the column incident_description in the incident table.
     */
    protected String incidentDescription;
    /** 
     * This attribute maps to the column is_police_involved in the incident table.
     */
    protected boolean isPoliceInvolved;
    protected Witness witness;
    protected Injury injury;

    /**
     * Method 'Incident'
     * 
     */
    public Incident() {
    }

    /**
     * Method 'getDate'
     * 
     * @return java.util.Date
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     * Method 'setDate'
     * 
     * @param date
     */
    public void setDate(java.util.Date date) {
        this.date = date;
    }

    /**
     * Method 'getLocation'
     * 
     * @return java.lang.String
     */
    public java.lang.String getLocation() {
        return location;
    }

    /**
     * Method 'setLocation'
     * 
     * @param location
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    /**
     * Method 'getIncidentDescription'
     * 
     * @return java.lang.String
     */
    public java.lang.String getIncidentDescription() {
        return incidentDescription;
    }

    /**
     * Method 'setIncidentDescription'
     * 
     * @param incidentDescription
     */
    public void setIncidentDescription(java.lang.String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    /**
     * Method 'isIsPoliceInvolved'
     * 
     * @return boolean
     */
    public boolean isIsPoliceInvolved() {
        return isPoliceInvolved;
    }

    /**
     * Method 'setIsPoliceInvolved'
     * 
     * @param isPoliceInvolved
     */
    public void setIsPoliceInvolved(boolean isPoliceInvolved) {
        this.isPoliceInvolved = isPoliceInvolved;
    }

    public String getIsPoliceInvolvedDesc() {

        return isPoliceInvolved ? "Yes" : "No";
    }

    public Witness getWitness() {
        return witness;
    }

    public void setWitness(Witness witness) {
        this.witness = witness;
    }

    public Injury getInjury() {
        return injury;
    }

    public void setInjury(Injury injury) {
        this.injury = injury;
    }
}
