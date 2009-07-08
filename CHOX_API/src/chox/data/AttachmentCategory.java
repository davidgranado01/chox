package chox.data;

import java.util.ArrayList;
import java.util.List;

public class AttachmentCategory {
    
    public static final String ATTCAT_PAYMENTPACK = "Payment Pack";
    public static final String ATTCAT_TOTALLOSS = "Total Loss Inspection Check";
    
    public static List<String> getAttachmentCategory() {
        List<String> status = new ArrayList<String>();
        status.add(ATTCAT_PAYMENTPACK);
        status.add(ATTCAT_TOTALLOSS);
        return status;
    }
}
