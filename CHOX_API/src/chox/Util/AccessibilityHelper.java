package chox.Util;

import chox.model.AccessibilityEditable;
import chox.model.Claim;
import chox.model.WebUser;

public class AccessibilityHelper {

    public static boolean getIsClaimEditable(AccessibilityEditable accEditable, Claim claim, WebUser user){

        boolean bWorkGroupFlag = true;
        boolean bOwnershipFlag = true;

        if(accEditable!=null){

            // ONLY INSURER USER
            // ONLY INSURER WORKGROUPCONTROL IS TRUE
            if(RoleHelper.isUserCheckByWorkgroup(user) && accEditable.isWorkgroupCheck()){

                // SET TO FALSE IF CLAIM's WORKGROUP IN USER'S WORKGROUP(S)
                if(!AccessibilityHelper.isClaimWorkgroupOwnByUser(user, claim)){
                    bWorkGroupFlag = false;
                }

            }

            // System.out.println(">>> isUserCheckByOwnership: "+RoleHelper.isUserCheckByOwnership(user));
            // System.out.println(">>> isOwnershipCheck: "+accEditable.isOwnershipCheck());
            // System.out.println(">>> isClaimLocked: "+user.getInsurer().isClaimLocked());
            
            // ONLY INSURER USER
            // ONLY INSURER OWNERSHIP IS TRUE
            if(RoleHelper.isUserCheckByOwnership(user) && accEditable.isOwnershipCheck() && user.getInsurer().isClaimLocked()){

                // System.out.println(">>> IN");

                if(!AccessibilityHelper.isClaimOwnByUser(user, claim)){
                    bOwnershipFlag = false;
                }

            }
        }

        return (bWorkGroupFlag && bOwnershipFlag);
    }

    public static boolean getIsClaimWorkgroupEditable(Claim claim, WebUser user){
        
        boolean bWorkGroupFlag = true;

        // ONLY INSURER USER
        // ONLY INSURER WORKGROUPCONTROL IS TRUE
        if(RoleHelper.isUserCheckByWorkgroup(user)){

            // SET TO FALSE IF CLAIM's WORKGROUP IN USER'S WORKGROUP(S)
            if(!isClaimWorkgroupOwnByUser(user, claim)){
                bWorkGroupFlag = false;
            }

        }

        return bWorkGroupFlag;

    }

    public static boolean getIsClaimOwnershipEditable(Claim claim, WebUser user){

        boolean bOwnershipFlag = true;

        // ONLY INSURER USER
        // ONLY INSURER OWNERSHIP IS TRUE
        if(RoleHelper.isUserCheckByOwnership(user) && user.getInsurer().isClaimLocked()){

            if(!AccessibilityHelper.isClaimOwnByUser(user, claim)){
                bOwnershipFlag = false;
            }

        }

        return bOwnershipFlag;

    }
    
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
