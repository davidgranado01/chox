package idas.chox.core.xmlValidation;

import idas.chox.core.model.Claim;
import idas.chox.core.model.History;
import idas.chox.core.model.Injury;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.Solicitor;
import idas.chox.core.model.Witness;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

public class ClaimResult{
    
    private Claim claim;
    private Invoice invoice;
    private Element element;
    private ClaimParseStatus claimParseStatus;
    private boolean valid;
    private boolean dataValid;
    private boolean checkDataValid;
    private List<String> message = new ArrayList<String>();
    
    private ArrayList<Witness> witnesses;
    private ArrayList<Injury> injuries;
    private ArrayList<Solicitor> solicitors;
    private List<History> history;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
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
    
    public boolean isCheckDataValid() {
        return checkDataValid;
    }

    public void setCheckDataValid(boolean checkDataValid) {
        this.checkDataValid = checkDataValid;
    }

    public boolean isDataValid() {
        return dataValid;
    }

    public void setDataValid(boolean dataValid) {
        this.dataValid = dataValid;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public ClaimParseStatus getClaimParseStatus() {
        return claimParseStatus;
    }

    public void setClaimParseStatus(ClaimParseStatus claimParseStatus) {
        this.claimParseStatus = claimParseStatus;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public String getProcessStatus(){
        
        String processStatus = "Failed";
        
        if(this.dataValid && this.valid){
            
            processStatus = "Uploaded";
            
            if(this.claimParseStatus.equals(ClaimParseStatus.existClaim)){
                processStatus = "Updated";
            }
            
        }
        
        return processStatus;
    }
    
    public String getClaimStatus(){
        
        String sReturn = "";
        
        if(this.claim!=null){
            sReturn = this.claim.getStatus();
        }
        
        if(this.claimParseStatus.equals(ClaimParseStatus.newClaim)
            && (!this.dataValid || !this.valid)
        ){
            sReturn = "N/A";
        }
        
        if(this.claimParseStatus.equals(ClaimParseStatus.invalidSchema)){
            sReturn = "N/A";
        }
        
        // System.out.println(">>>>>> ClaimStatus : " + sReturn);
        
        return sReturn;
    }
    
    public String getUploadedStatus(){
        
        String sReturn = "";

        if(this.claimParseStatus.equals(ClaimParseStatus.newInvoice)){
            sReturn ="New Invoice";
        }else if(this.claimParseStatus.equals(ClaimParseStatus.newClaim)){
            sReturn ="New Claim";
        }else if(this.claimParseStatus.equals(ClaimParseStatus.ClaimNotEditable)){
            sReturn ="Claim Closed or Pending";
        }else if(this.claimParseStatus.equals(ClaimParseStatus.existClaim)){
            sReturn ="Claim Already Exists";
        }else if(this.claimParseStatus.equals(ClaimParseStatus.existInvoice)){
            sReturn ="Invoice Already Exists";
        }else if(this.claimParseStatus.equals(ClaimParseStatus.invalidSchema)){
            sReturn ="Incorrect XML Structure";
        }else if(this.claimParseStatus.equals(ClaimParseStatus.tpiNotRecognized)){
            sReturn ="Incorrect Value Provided for Hire State";
        }else{
            sReturn ="Error";
        }
        
        return sReturn;
    }
    
}