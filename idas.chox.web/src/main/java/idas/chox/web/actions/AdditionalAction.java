package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

public class AdditionalAction extends BaseAction{
    
    public static final String EXTRAACTION_updateInsurerClaimNumber = "updateInsurerClaimNumber";
    public static final String EXTRAACTION_updateInsurerClaimOwner = "updateClaimOwner";
    public static final String EXTRAACTION_escalateUnassignedClaim = "escalateUnassignedClaim";



    
    public static List<String> getExtraActions() {
        List<String> action = new ArrayList<String>();
        action.add(EXTRAACTION_updateInsurerClaimNumber);
        action.add(EXTRAACTION_updateInsurerClaimOwner);
        action.add(EXTRAACTION_escalateUnassignedClaim);
        return action;
    }

    public static String getExtraActionName(String extraAction) {
        
        String returnStr = "";
        
        if (extraAction.equalsIgnoreCase("updateInsurerClaimNumber")) {
            returnStr = "Update Insurer Claim Number";
        }else if (extraAction.equalsIgnoreCase("updateClaimOwner")) {
            returnStr = "Update Claim Ownership";
        }else if (extraAction.equalsIgnoreCase("escalateUnassignedClaim")) {
            returnStr = "Re-assign Workgroup";
        }

        return returnStr;
    }




}
