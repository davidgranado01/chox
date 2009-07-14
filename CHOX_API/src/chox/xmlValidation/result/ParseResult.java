package chox.xmlValidation.result;

import chox.model.Bordereau;
import java.util.List;

public class ParseResult {
    
    protected Bordereau bordereau;
    protected boolean status = true;
    protected List<String> message;

    public Bordereau getBordereau() {
        return bordereau;
    }

    public void setBordereau(Bordereau bordereau) {
        this.bordereau = bordereau;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
}