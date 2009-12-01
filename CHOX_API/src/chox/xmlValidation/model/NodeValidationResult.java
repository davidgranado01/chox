package chox.xmlValidation.model;

import java.util.ArrayList;
import java.util.List;

public class NodeValidationResult {
    private boolean isValid = true;
    private List<String> message = new ArrayList<String>();

    public boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
    
    
    
}