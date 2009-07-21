package chox.xmlValidation.model;

import chox.model.Claim;
import chox.model.Injury;
import chox.model.Solicitor;
import chox.model.Witness;
import chox.xmlValidation.model.status.ClaimParseStatus;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

public class ClaimResult{
    
    private Claim claim;
    private Element element;
    private ClaimParseStatus claimParseStatus;
    private boolean valid;
    private boolean dataValid;
    private boolean checkDataValid;
    private List<String> message = new ArrayList<String>();
    
    private ArrayList<Witness> witnesses;
    private ArrayList<Injury> injuries;
    private ArrayList<Solicitor> solicitors;

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
    
}