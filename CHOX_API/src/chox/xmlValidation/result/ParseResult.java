package chox.xmlValidation.result;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class ParseResult {
    
    protected boolean status = true;
    protected List<String> message = new ArrayList<String>();

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
    
    public void addMessage(String errMsg) {
        /*
        if(this.message==null || this.message.size()<=0){
            this.message = new ArrayList<String>();
        }
         */ 
        this.message.add(errMsg);
    }
    
}