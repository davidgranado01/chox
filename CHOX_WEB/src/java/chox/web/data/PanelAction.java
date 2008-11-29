package chox.web.data;

import java.util.ArrayList;
import java.util.List;

public class PanelAction { 
    public static final String ACTION_claimlineofbusiness = "updateclaimlineofbusiness";
    public static final String ACTION_claiminsurerdetail = "updateclaiminsurerdetail";
    public static final String ACTION_claimcarhiredetail = "updateclaimcarhiredetail";
    public static final String ACTION_invoicedtailbyinsurer = "updateinvoicedtailbyinsurer";
    public static final String ACTION_invoicedtailbycho = "updateinvoicedtailbycho";
    public static final String ACTION_invoicedtailforcalculationincorrect = "updateinvoicedtailforcalculationincorrect";
    
    public static List<String> getPanelActions() {
        List<String> action = new ArrayList<String>();
        action.add(ACTION_claimlineofbusiness);
        action.add(ACTION_claiminsurerdetail);
        action.add(ACTION_claimcarhiredetail);
        action.add(ACTION_invoicedtailbyinsurer);
        action.add(ACTION_invoicedtailbycho);
        action.add(ACTION_invoicedtailforcalculationincorrect);
        return action;
    }
}
