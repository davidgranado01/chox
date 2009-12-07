package chox.model;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.io.Serializable;
import java.util.Date;

public class Incident extends AuditableEntity implements Serializable {

    protected Date date;
    protected String location;
    protected String incidentDescription;
    protected boolean isPoliceInvolved;
    protected Witness witness;
    protected Injury injury;

    public Incident() {
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getDate() {
        return date;
    }

    /**
     * Method 'setDate'
     * 
     * @param date
     */
    @TypeConversion(converter = "chox.data.DateConverter")
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
