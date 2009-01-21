package chox.model;

import chox.data.UploadStatus;
import java.util.ArrayList;
import org.hibernate.Session;
import chox.Util.TextHelper;
import java.util.List;

public class XMLParseResult {
    
    /*
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "InProgress";
    public static final String COMPLETE = "Complete";
    public static final String CANCELLED = "Cancelled";
    public static final String DUPLICATE = "Duplicated";
    */
    
    private Boolean isSchemaValid = true;
    private Boolean isDataValid = true;
    //private String SchemaValidationRemark = "";
    //private String DataValidationRemark = "";
    private String UploadType;
    private Session currentSession;
    private Boolean isCurrentScheValid = true;
    private Boolean isCurrentDataValid = true;
    
    // PARAM TO CHECK
    private Boolean isClaimExist = false;
    private Boolean isInvoiceExist = false;
    private Boolean isNewInvoiceExit = false;
    private String sExistingClaimStatus = "";
    private String uploadStatus = "";
    
    // SETUP DATA - CLAIM OBJECT
    private Claim claim;
    private ArrayList<Witness> witnesses;
    private ArrayList<Injury> injuries;
    private ArrayList<Solicitor> solicitors;

    private List<String> SchemaValidationRemark = new ArrayList<String>();
    private List<String> DataValidationRemark = new ArrayList<String>();
    
    public String getSExistingClaimStatus() {
        return sExistingClaimStatus;
    }

    public void setSExistingClaimStatus(String sExistingClaimStatus) {
        this.sExistingClaimStatus = sExistingClaimStatus;
    }
    
    public Boolean getIsInvoiceExist() {
        return isInvoiceExist;
    }

    public void setIsInvoiceExist(Boolean isInvoiceExist) {
        this.isInvoiceExist = isInvoiceExist;
    }

    public Boolean getIsClaimExist() {
        return isClaimExist;
    }

    public void setIsClaimExist(Boolean isClaimExist) {
        this.isClaimExist = isClaimExist;
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
    
    /*
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
    */
    
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
    
    private Boolean getSchemaDataValidation(){
        Boolean bFlag = false;
        if(this.isDataValid && this.isSchemaValid){
            bFlag = true;
        }
        return bFlag;
    }

    public Boolean getIsNewInvoiceExit() {
        return isNewInvoiceExit;
    }

    public void setIsNewInvoiceExit(Boolean isNewInvoiceExit) {
        this.isNewInvoiceExit = isNewInvoiceExit;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
    
    
    
    /*
    public String getUploadStatus() {

        String sStatus;
        
        if(!this.isClaimExist){
            if(getSchemaDataValidation()){
                sStatus = UploadStatus.CLAIM_UPLOAD_SUCCESSFUL;
            }else{
                sStatus = UploadStatus.CLAIM_UPLOAD_FAILED;
                this.claim.status="";
            }
            
        }else{
            
            // CLAIM EXIST
            if(this.sExistingClaimStatus.equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)){
                
                sStatus = UploadStatus.CLAIM_EXIST;
                
            }else if(this.sExistingClaimStatus.equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA)){
                
                if(this.isInvoiceExist){
                    
                    sStatus = UploadStatus.INVOICE_EXIST;
                    
                }else{

                    if(getSchemaDataValidation()){
                        sStatus = UploadStatus.INVOICE_UPLOAD_SUCCESSFUL;
                    }else{
                        sStatus = UploadStatus.INVOICE_UPLOAD_FAILED;
                    }
                }
            }else{
                if(this.isInvoiceExist){
                    sStatus = UploadStatus.INVOICE_EXIST;
                }else{
                    sStatus = UploadStatus.INCORRECT_CLAIM_STATUS;
                }
            }
        }
        return sStatus;
    }
    */
    
    public String getUploadStatusCode(){
        return TextHelper.trimWhiteSpace(getUploadStatus()).toUpperCase();
        
    }
    
    
    public List<String> getDataValidationRemark() {
        return DataValidationRemark;
    }

    public void setDataValidationRemark(List<String> DataValidationRemark) {
        this.DataValidationRemark = DataValidationRemark;
    }

    public List<String> getSchemaValidationRemark() {
        return SchemaValidationRemark;
    }

    public void setSchemaValidationRemark(List<String> SchemaValidationRemark) {
        this.SchemaValidationRemark = SchemaValidationRemark;
    }
    
    /*
    public List<String> getDataValidationRemarkInList() {
        return DataValidationRemark.split("\\|");
    }

    public String[] getSchemaValidationRemarkInList() {
        return t
    }
    */
}
