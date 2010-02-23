package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

public class AdditionalAction extends BaseAction{
    
    public static final String EXTRAACTION_updateInsurerClaimNumber = "updateInsurerClaimNumber";
    public static final String EXTRAACTION_updateClaimWorkgroupAndOwner = "updateClaimWorkgroupAndOwner";
    public static final String EXTRAACTION_escalateUnassignedClaim = "escalateUnassignedClaim";
    public static final String EXTRAACTION_updateLiability = "updateLiability";

    public static List<String> getExtraActions() {
        List<String> action = new ArrayList<String>();
        action.add(EXTRAACTION_updateInsurerClaimNumber);
        action.add(EXTRAACTION_updateClaimWorkgroupAndOwner);
        action.add(EXTRAACTION_escalateUnassignedClaim);
        action.add(EXTRAACTION_updateLiability);
        return action;
    }

    public static String getExtraActionName(String extraAction) {
        
        String returnStr = "";        
        if (extraAction.equalsIgnoreCase("updateInsurerClaimNumber")) {
            returnStr = "Update Insurer Claim Number";
        }else if (extraAction.equalsIgnoreCase("updateClaimWorkgroupAndOwner")) {
            returnStr = "Update Workgroup/Claim Owner";
        }else if (extraAction.equalsIgnoreCase("escalateUnassignedClaim")) {
            returnStr = "Re-assign Workgroup";
        }else if (extraAction.equalsIgnoreCase(EXTRAACTION_updateLiability)) {
            returnStr = "Update Liability";
        }
        return returnStr;
    }




}
