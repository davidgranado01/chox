package chox.model;

import java.util.ArrayList;
import org.hibernate.Session;

public class XMLParseResult {

    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String DUPLICATE = "Duplicated";
        
    private Boolean isSchemaValid = true;
    private Boolean isDataValid = true;
    private String SchemaValidationRemark = "";
    private String DataValidationRemark = "";
    private String UploadType;
    private Session currentSession;
    
    // USE WHEN RUNING THE VALIDATION
    private Boolean isCurrentScheValid = true;
    private Boolean isCurrentDataValid = true;
    
    // SETUP DATA - CLAIM OBJECT
    private Claim claim;
    private ArrayList<Witness> witnesses;
    private ArrayList<Injury> injuries;
    private ArrayList<Solicitor> solicitors;
    private ArrayList<EngineerReport> engineerReports;
    private  ArrayList<VehicleHire> vehiclehires;

    public ArrayList<VehicleHire> getVehiclehires() {
        return vehiclehires;
    }

    public void setVehiclehires(ArrayList<VehicleHire> vehiclehires) {
        this.vehiclehires = vehiclehires;
    }
    
    public ArrayList<EngineerReport> getEngineerReports() {
        return engineerReports;
    }

    public void setEngineerReports(ArrayList<EngineerReport> engineerReports) {
        this.engineerReports = engineerReports;
    }
    
    public ArrayList<Injury> getInjuries() {
        return injuries;
    }

    public void setInjuries(ArrayList<Injury> injuries) {
        this.injuries = injuries;
    }

    public ArrayList<Solicitor> getSolicitors() {
        return solicitors;
    }

    public void setSolicitors(ArrayList<Solicitor> solicitors) {
        this.solicitors = solicitors;
    }

    public ArrayList<Witness> getWitnesses() {
        return witnesses;
    }

    public void setWitnesses(ArrayList<Witness> witnesses) {
        this.witnesses = witnesses;
    }

    
    public String getDataValidationRemark() {
        return DataValidationRemark;
    }

    public void setDataValidationRemark(String DataValidationRemark) {
        this.DataValidationRemark = DataValidationRemark;
    }

    public String getSchemaValidationRemark() {
        return SchemaValidationRemark;
    }

    public void setSchemaValidationRemark(String SchemaValidationRemark) {
        this.SchemaValidationRemark = SchemaValidationRemark;
    }

    public String getUploadType() {
        return UploadType;
    }

    public void setUploadType(String UploadType) {
        this.UploadType = UploadType;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Boolean getIsCurrentDataValid() {
        return isCurrentDataValid;
    }

    public void setIsCurrentDataValid(Boolean isCurrentDataValid) {
        this.isCurrentDataValid = isCurrentDataValid;
    }

    public Boolean getIsCurrentScheValid() {
        return isCurrentScheValid;
    }

    public void setIsCurrentScheValid(Boolean isCurrentScheValid) {
        this.isCurrentScheValid = isCurrentScheValid;
    }

    public Boolean getIsDataValid() {
        return isDataValid;
    }

    public void setIsDataValid(Boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    public Boolean getIsSchemaValid() {
        return isSchemaValid;
    }

    public void setIsSchemaValid(Boolean isSchemaValid) {
        this.isSchemaValid = isSchemaValid;
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
    
    
    
    
    
    
    
}
