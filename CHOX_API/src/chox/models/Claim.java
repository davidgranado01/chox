package chox.models;

import java.sql.Timestamp;

public class Claim {

    private long ID = -1;
    private long rentalID = -1;
    private long insurerCountryID = -1;
    private String policyNumber;
    private String claimReference;
    private String comprehensive = "y";
    private String policyHolderName;
    private String vehicleRegistration;
    private String vehicleManufacturer;
    private String vehicleModel;
    private long vehicleClassID = -1;
    private String usable;
    private String damageDescription;
    private long tpInsurerCountryID = -1;
    private String tpPolicyNumber;
    private String tpClaimReference;
    private String tpVehicleRegistration;
    private String tpVehicleManufacturer;
    private String tpVehicleModel;
    private long tpVehicleClassID = -1;
    private String tpName;
    private String tpAddress1;
    private String tpAddress2;
    private String tpAddress3;
    private String tpAddress4;
    private String tpAddress5;
    private String tpPostcode;
    private String tpTelephoneDay;
    private String tpTelephoneEvening;
    private String tpEmail;
    private Timestamp incidentDate;
    private String location;
    private String policeInvolved = "n";
    private String incidentDescription;
    private String vehicleLocation;
    private long proposedRentalClassID=-1;
    private String claimStatus = "Awaiting Authorization";

    public long getID() {
        return ID;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public long getInsurerCountryID() {
        return insurerCountryID;
    }

    public void setInsurerCountryID(long insurerCountryID) {
        this.insurerCountryID = insurerCountryID;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getClaimReference() {
        return claimReference;
    }

    public void setClaimReference(String claimReference) {
        this.claimReference = claimReference;
    }

    public String getComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(String comprehensive) {
        this.comprehensive = comprehensive;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public long getVehicleClassID() {
        return vehicleClassID;
    }

    public void setVehicleClassID(long vehicleClassID) {
        this.vehicleClassID = vehicleClassID;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }

    public String getDamageDescription() {
        return damageDescription;
    }

    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }

    public long getTpInsurerCountryID() {
        return tpInsurerCountryID;
    }

    public void setTpInsurerCountryID(long tpInsurerCountryID) {
        this.tpInsurerCountryID = tpInsurerCountryID;
    }

    public String getTpPolicyNumber() {
        return tpPolicyNumber;
    }

    public void setTpPolicyNumber(String tpPolicyNumber) {
        this.tpPolicyNumber = tpPolicyNumber;
    }

    public String getTpClaimReference() {
        return tpClaimReference;
    }

    public void setTpClaimReference(String tpClaimReference) {
        this.tpClaimReference = tpClaimReference;
    }

    public String getTpVehicleRegistration() {
        return tpVehicleRegistration;
    }

    public void setTpVehicleRegistration(String tpVehicleRegistration) {
        this.tpVehicleRegistration = tpVehicleRegistration;
    }

    public String getTpVehicleManufacturer() {
        return tpVehicleManufacturer;
    }

    public void setTpVehicleManufacturer(String tpVehicleManufacturer) {
        this.tpVehicleManufacturer = tpVehicleManufacturer;
    }

    public String getTpVehicleModel() {
        return tpVehicleModel;
    }

    public void setTpVehicleModel(String tpVehicleModel) {
        this.tpVehicleModel = tpVehicleModel;
    }

    public long getTpVehicleClassID() {
        return tpVehicleClassID;
    }

    public void setTpVehicleClassID(long tpVehicleClassID) {
        this.tpVehicleClassID = tpVehicleClassID;
    }

    public String getTpName() {
        return tpName;
    }

    public void setTpName(String tpName) {
        this.tpName = tpName;
    }

    public String getTpAddress1() {
        return tpAddress1;
    }

    public void setTpAddress1(String tpAddress1) {
        this.tpAddress1 = tpAddress1;
    }

    public String getTpAddress2() {
        return tpAddress2;
    }

    public void setTpAddress2(String tpAddress2) {
        this.tpAddress2 = tpAddress2;
    }

    public String getTpAddress3() {
        return tpAddress3;
    }

    public void setTpAddress3(String tpAddress3) {
        this.tpAddress3 = tpAddress3;
    }

    public String getTpAddress4() {
        return tpAddress4;
    }

    public void setTpAddress4(String tpAddress4) {
        this.tpAddress4 = tpAddress4;
    }

    public String getTpAddress5() {
        return tpAddress5;
    }

    public void setTpAddress5(String tpAddress5) {
        this.tpAddress5 = tpAddress5;
    }

    public String getTpPostcode() {
        return tpPostcode;
    }

    public void setTpPostcode(String tpPostcode) {
        this.tpPostcode = tpPostcode;
    }

    public String getTpTelephoneDay() {
        return tpTelephoneDay;
    }

    public void setTpTelephoneDay(String tpTelephoneDay) {
        this.tpTelephoneDay = tpTelephoneDay;
    }

    public String getTpTelephoneEvening() {
        return tpTelephoneEvening;
    }

    public void setTpTelephoneEvening(String tpTelephoneEvening) {
        this.tpTelephoneEvening = tpTelephoneEvening;
    }

    public String getTpEmail() {
        return tpEmail;
    }

    public void setTpEmail(String tpEmail) {
        this.tpEmail = tpEmail;
    }

    public Timestamp getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(Timestamp incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoliceInvolved() {
        return policeInvolved;
    }

    public void setPoliceInvolved(String policeInvolved) {
        this.policeInvolved = policeInvolved;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getVehicleLocation() {
        return vehicleLocation;
    }

    public void setVehicleLocation(String vehicleLocation) {
        this.vehicleLocation = vehicleLocation;
    }

    public long getProposedRentalClassID() {
        return proposedRentalClassID;
    }

    public void setProposedRentalClassID(long proposedRentalClassID) {
        this.proposedRentalClassID = proposedRentalClassID;
    }
}
