package chox.web.data;

import java.util.ArrayList;
import java.util.List;

public class ExtraAction {
    
    public static final String EXTRAACTION_updateInsurerClaimNumber = "updateInsurerClaimNumber";
    public static final String EXTRAACTION_updatePaymentReceived = "updatePaymentReceived";
    
    public static List<String> getExtraActions() {
        List<String> action = new ArrayList<String>();
        action.add(EXTRAACTION_updateInsurerClaimNumber);
        action.add(EXTRAACTION_updatePaymentReceived);
        return action;
    }
}
