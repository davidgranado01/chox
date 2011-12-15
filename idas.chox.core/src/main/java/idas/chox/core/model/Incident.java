package idas.chox.core.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;
import java.util.Date;

public class Incident extends Entity implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Incident.class);

    /** 
     * This attribute maps to the column date in the incident table.
     */
    private Date date;
    private String time;
    /** 
     * This attribute maps to the column location in the incident table.
     */
    private String location;
    /** 
     * This attribute maps to the column incident_description in the incident table.
     */
    private String incidentDescription;
    /** 
     * This attribute maps to the column is_police_involved in the incident table.
     */
    private Boolean isPoliceInvolved;
    private Witness witness;
    private Injury injury;

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

    public String getTime() {
        return (time==null ? "": time);
    }

    public void setTime(String time) {
        this.time = time;
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
    public Boolean getIsPoliceInvolved() {
        return isPoliceInvolved;
    }

    /**
     * Method 'setIsPoliceInvolved'
     * 
     * @param isPoliceInvolved
     */
    public void setIsPoliceInvolved(Boolean isPoliceInvolved) {
        this.isPoliceInvolved = isPoliceInvolved;
    }

    public String getIsPoliceInvolvedDesc() {
        if (isPoliceInvolved == null)
            return "";

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
