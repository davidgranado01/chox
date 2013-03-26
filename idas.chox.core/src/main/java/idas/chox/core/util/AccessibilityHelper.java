package idas.chox.core.util;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;

public class AccessibilityHelper {

    public static short isClaimEditable(boolean accessibilityWorkgroupCheck, boolean accessibilityClaimOwnerCheck, Claim claim, WebUser user) {

        boolean bWorkGroupFlag = true;
        boolean bOwnershipFlag = true;

        // ONLY INSURER USER
        // ONLY INSURER WORKGROUPCONTROL IS TRUE
        if (RoleHelper.isInsurerUser(user) && RoleHelper.isWorkgroupValidationEnabledUser(user) && accessibilityWorkgroupCheck && user.getInsurer().isClaimLocked()) {

            // SET TO FALSE IF CLAIM's WORKGROUP IN USER'S WORKGROUP(S)
            if (!AccessibilityHelper.isClaimWorkgroupOwnByUser(user, claim)) {
                bWorkGroupFlag = false;
            }

        }

        // ONLY INSURER USER
        // ONLY INSURER OWNERSHIP IS TRUE
        if (RoleHelper.isInsurerUser(user) && RoleHelper.isOwnershipValidationEnabledUser(user) && accessibilityClaimOwnerCheck && user.getInsurer().isClaimLocked()) {
            if (!AccessibilityHelper.isClaimOwnByUser(user, claim)) {
                bOwnershipFlag = false;
            }
        }

        if (bWorkGroupFlag && bOwnershipFlag) {
            return 2;
        }
        return 1;
    }

    public static boolean getIsClaimWorkgroupEditable(Claim claim, WebUser user) {

        boolean bWorkGroupFlag = true;

        // ONLY INSURER USER
        // ONLY INSURER WORKGROUPCONTROL IS TRUE
        if (RoleHelper.isWorkgroupValidationEnabledUser(user) && user.getInsurer().isClaimLocked()) {

            // SET TO FALSE IF CLAIM's WORKGROUP IN USER'S WORKGROUP(S)
            if (!isClaimWorkgroupOwnByUser(user, claim)) {
                bWorkGroupFlag = false;
            }

        }

        return bWorkGroupFlag;

    }

    public static boolean getIsClaimOwnershipEditable(Claim claim, WebUser user) {

        boolean bOwnershipFlag = true;

        // ONLY INSURER USER
        // ONLY INSURER OWNERSHIP IS TRUE
        if (user.getInsurer() != null && RoleHelper.isOwnershipValidationEnabledUser(user) && user.getInsurer().isClaimLocked()) {

            if (!AccessibilityHelper.isClaimOwnByUser(user, claim)) {
                bOwnershipFlag = false;
            }

        }

        return bOwnershipFlag;

    }

    public static boolean isClaimWorkgroupOwnByUser(WebUser user, Claim claim) {

        boolean bFlag = false;

        if (user.getWorkgroupIds().size() > 0 && claim.getWorkgroup() != null) {

            if (user.getWorkgroupIds().contains(claim.getWorkgroup().getId())) {
                bFlag = true;
            }
        }

        return bFlag;
    }

    public static boolean isClaimOwnByUser(WebUser user, Claim claim) {
        boolean bFlag = false;
        if (claim.getClaimOwner() != null) {
            if (user.getId().equals(claim.getClaimOwner().getId())) {
                bFlag = true;
            }
        }
        return bFlag;
    }
}
