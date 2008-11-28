package chox.web.data;

import java.util.ArrayList;
import java.util.List;

public class PanelAction { 
    public static final String ACTION_claimlineofbusiness = "updateclaimlineofbusiness.ClaimUnacknowledgedUnrouted";
    public static final String ACTION_claiminsurerdetail = "updateclaiminsurerdetail.ClaimUnacknowledgedRouted";
    public static final String ACTION_claimcarhiredetail = "updateclaimcarhiredetail.AwaitingCarHireInfo";
    public static final String ACTION_invoicedtailbyinsurer = "updateinvoicedtailbyinsurer.ContestedInvoiceReferredToInsurer";
    public static final String ACTION_invoicedtailbycho = "updateinvoicedtailbycho.ContestedInvoiceReferredToCHO";
    public static final String ACTION_invoicedtailforcalculationincorrect = "updateinvoicedtailforcalculationincorrect.InvoiceDataCalculationIncorrect";
    
    public static List<String> getPanelAction() {
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
