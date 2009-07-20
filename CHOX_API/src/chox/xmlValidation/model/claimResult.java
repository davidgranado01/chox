/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.model;

import chox.model.Claim;
import chox.xmlValidation.model.status.ClaimParseStatus;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.List;

public class ClaimResult {
    
    private Claim claim;
    private Element element;
    private ClaimParseStatus claimParseStatus;
    private boolean valid;
    private boolean dataValid;
    private boolean checkDataValid;
    private List<String> message = new ArrayList<String>();

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