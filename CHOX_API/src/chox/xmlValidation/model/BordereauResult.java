package chox.xmlValidation.model;
import chox.model.Bordereau;
import chox.xmlValidation.model.status.BordereauParseStatus;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class BordereauResult {
    
    protected boolean valid = true;
    protected BordereauParseStatus bordereauStatus;
    protected List<String> message = new ArrayList<String>();
    protected List<ClaimResult> claimResult = new ArrayList<ClaimResult>();
    protected Bordereau bordereau;

    public Bordereau getBordereau() {
        return bordereau;
    }

    public void setBordereau(Bordereau bordereau) {
        this.bordereau = bordereau;
    }
    
    public BordereauParseStatus getBordereauStatus() {
        return bordereauStatus;
    }

    public void setBordereauStatus(BordereauParseStatus bordereauStatus) {
        this.bordereauStatus = bordereauStatus;
    }
    
    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
    
    public void addMessage(String errMsg) {
        this.message.add(errMsg);
    }

    public List<ClaimResult> getClaimResult() {
        return claimResult;
    }

    public void setClaimResult(List<ClaimResult> claimResult) {
        this.claimResult = claimResult;
    }
    
}