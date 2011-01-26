package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

public class AdditionalAction extends BaseAction {

    public static final String EXTRAACTION_updateInsurerClaimNumber = "updateInsurerClaimNumber";
    public static final String EXTRAACTION_updateClaimWorkgroupAndOwner = "updateClaimWorkgroupAndOwner";
    public static final String EXTRAACTION_escalateUnassignedClaim = "escalateUnassignedClaim";
    public static final String EXTRAACTION_updateLiability = "updateLiability";
    public static final String EXTRAACTION_updateClaimSupplierOwner = "updateClaimSupplierOwner";
    public static final String EXTRAACTION_makeInterimPayment = "makeInterimPayment";
    //public static final String EXTRAACTION_updateInterimPayment = "updateInterimPayment";
    public static final String EXTRAACTION_updateInterimPaymentFullAndFinal = "updateInterimPaymentFullAndFinal";
    public static final String EXTRAACTION_paymentReceived = "updatePaymentReceived";

    public static List<String> getExtraActions() {
        List<String> action = new ArrayList<String>();
        action.add(EXTRAACTION_makeInterimPayment);
        action.add(EXTRAACTION_escalateUnassignedClaim);
//        action.add(EXTRAACTION_updateInterimPayment); - moved to action panel as is associated with a queue
        action.add(EXTRAACTION_updateClaimSupplierOwner);
        action.add(EXTRAACTION_updateInsurerClaimNumber);
        action.add(EXTRAACTION_updateLiability);
        action.add(EXTRAACTION_updateClaimWorkgroupAndOwner);
        action.add(EXTRAACTION_updateInterimPaymentFullAndFinal);
        action.add(EXTRAACTION_paymentReceived);

        return action;
    }

    public static String getExtraActionName(String extraAction) {

        String returnStr = "";
        if (extraAction.equalsIgnoreCase(EXTRAACTION_updateInsurerClaimNumber)) {
            returnStr = "Update Insurer Claim Number";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_updateClaimWorkgroupAndOwner)) {
            returnStr = "Update Workgroup/Claim Owner";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_escalateUnassignedClaim)) {
            returnStr = "Re-assign Workgroup";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_updateLiability)) {
            returnStr = "Update Liability";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_updateClaimSupplierOwner)) {
            returnStr = "Update Claim Owner";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_makeInterimPayment)) {
            returnStr = "Make Interim Payment";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_updateInterimPaymentFullAndFinal)) {
            returnStr = "Update To Interim Payment Received Full & Final";
        } else if (extraAction.equalsIgnoreCase(EXTRAACTION_paymentReceived)) {
            returnStr = "Update To Payment Received";
        }
        return returnStr;
    }
}
