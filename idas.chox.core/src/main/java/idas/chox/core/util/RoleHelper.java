package idas.chox.core.util;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import java.util.Iterator;
import java.util.Set;

public class RoleHelper {

    /*****************************************************
     * WORKGROUP
     *****************************************************/
    public static boolean isWorkgroupRelatedUserOnly(WebUser user) {
        if(isWorkgroupValidationEnabledUser(user) && !hasNoneWorkgroupEnableRole(user.getRoles())){
            return true;
        }
        return false;
    }

    public static boolean isWorkgroupValidationEnabledUser(WebUser user) {
        if (isInsurerUser(user)) {

            if (user.getInsurer().isClaimOwnershipEnable()) {

                if (user.getRoles() != null) {

                    if (user.getRoles().size() > 0) {
                        Iterator itr = user.getRoles().iterator();
                        while (itr.hasNext()) {
                            WebUserRole webUserrole = (WebUserRole) itr.next();
                            if (webUserrole.isWorkgroupRelated()) {
                                return true;
                            }
                        }
                    }

                }

            }

        }

        return false;
    }

    /*****************************************************
     * OWNERSHIP
     *****************************************************/
    public static boolean isOwnershipValidationEnabledUser(WebUser user) {

        if (isInsurerUser(user)) {

            if (user.getInsurer().isClaimOwnershipEnable()) {

                if (user.getRoles() != null) {

                    if (user.getRoles().size() > 0) {
                        Iterator itr = user.getRoles().iterator();
                        while (itr.hasNext()) {
                            WebUserRole webUserrole = (WebUserRole) itr.next();
                            if (webUserrole.isOwnershipRelated()) {
                                return true;
                            }
                        }
                    }

                }

            }

        }

        return false;
    }

    /*****************************************************
     * OTHER
     *****************************************************/
    public static boolean isChoxAdmin(WebUser user) {
        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHOX)) {
            return true;
        }
        return false;
    }

    public static boolean isInsurerUser(WebUser user) {
        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_INS)) {
            return true;
        }
        return false;
    }

    public static boolean isCreditHireUser(WebUser user) {
        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHO)) {
            return true;
        }
        return false;
    }

    public static boolean hasNoneWorkgroupEnableRole(Set roles) {

        if (roles != null) {

            if (roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();
                    //if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_COM) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                    if(!webUserrole.isWorkgroupRelated() && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)){
                        return true;
                    }
                }
            }
        }
        
        return false;
    }

    public static boolean isCheckSelectedRoleExist(Set roles, String roleName) {

        boolean bFlag = false;

        if (roles != null) {

            if (roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.getName().equalsIgnoreCase(roleName)) {
                        bFlag = true;
                        break;
                    }

                }
            }
        }

        return bFlag;

    }
}
