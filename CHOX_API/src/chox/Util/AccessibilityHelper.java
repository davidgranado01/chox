package chox.Util;

import chox.model.Claim;
import chox.model.WebUser;

public class AccessibilityHelper {

    public static boolean isClaimWorkgroupOwnByUser(WebUser user, Claim claim){

        boolean bFlag = false;

        if(user.getWorkgroupIds().size()>0 && claim.getWorkgroup()!=null){

            if(user.getWorkgroupIds().contains(claim.getWorkgroup().getId())){
                bFlag = true;
            }
        }

        return bFlag;
    }

    public static boolean isClaimOwnByUser(WebUser user, Claim claim){

        boolean bFlag = false;

        if(claim.getClaimOwner()!=null){

            if(user.getId()==claim.getClaimOwner().getId()){
                bFlag = true;
            }
        }

        return bFlag;
    }
    
}
