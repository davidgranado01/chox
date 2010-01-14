package idas.chox.core.util;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RoleHelper {

    /*****************************************************
     * WORKGROUP
     *****************************************************/
    // TRUE: FILTER BY HIS WORKGROUPS ONLY
    
    public static boolean isGlobalFilterByWorkgroup(WebUser user) {

        boolean bFlag = false;
        if ((isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH) || isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_COM)) && !hasNoneWorkgroupEnableRole(user.getRoles()) && user.getInsurer().isWorkgroupEnable()) {
            bFlag = true;
        }

        return bFlag;
    }

    private static boolean isUserWithWorkgroupRole(WebUser user) {

        boolean bFlag = false;

        if ((isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH) || isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_COM) || isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_FNOL)) && user.getInsurer().isWorkgroupEnable()) {
            bFlag = true;
        }

        return bFlag;
    }

    public static boolean isUserCheckByWorkgroup(WebUser user) {
        // IS INS USER
        // IS COM AND CH ROLE USER
        // IS WORKGROUP ENABLE
        return (isInsurerUser(user) && user.getWorkgroupRelatedRoles().size()>=0);
    }

    /*****************************************************
     * OWNERSHIP
     *****************************************************/
    // TRUE: FILTER BY OWNERSHIP ID ONLY
    public static boolean isGlobalFilterByOwnership(WebUser user) {

        boolean bFlag = false;

        if ((user.getRoles().size() <= 2) && isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH) && user.getInsurer().isClaimOwnershipEnable()) {
            bFlag = true;
        }

        return bFlag;
    }

    public static boolean isEditableByOwnership(WebUser user) {

        boolean bFlag = false;

        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CH) && user.getInsurer().isClaimOwnershipEnable()) {
            bFlag = true;
        }

        return bFlag;
    }

    public static boolean isUserCheckByOwnership(WebUser user) {
        // IS INS USER
        // IS CH ROLE USER
        // IS OWNERSHIP ENABLE
        //return (isInsurerWorkgroupUser(user) && isUserWithWorkgroupRole(user));
        return (isInsurerUser(user) && isEditableByOwnership(user));
    }

    /*****************************************************
     * OTHER
     *****************************************************/
    public static boolean isChoxAdmin(WebUser user) {

        boolean bFlag = false;

        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHOX)) {
            bFlag = true;
        }

        return bFlag;
    }

    public static boolean isInsurerUser(WebUser user) {

        boolean bFlag = false;

        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_INS)) {
            bFlag = true;
        }

        return bFlag;
    }

    public static boolean isCreditHireUser(WebUser user) {

        boolean bFlag = false;

        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHO)) {
            bFlag = true;
        }

        return bFlag;
    }

    public static boolean hasClaimHandlerRoleOnly(Set roles) {

        boolean bFlag = true;

        if (roles != null) {

            if (roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                        bFlag = false;
                        break;
                    }
                }
            }
        }
        
        return bFlag;
    }

    public static boolean hasComRoleOnly(Set roles) {

        boolean bFlag = true;

        if (roles != null) {

            if (roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_COM) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                        bFlag = false;
                        break;
                    }
                }
            }
        }
        return bFlag;
    }

    public static boolean hasFnolRoleOnly(Set roles) {

        boolean bFlag = true;

        if (roles != null) {

            if (roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_FNOL) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                        bFlag = false;
                        break;
                    }
                }
            }
        }
        return bFlag;
    }

    public static boolean hasNoneWorkgroupEnableRole(Set roles) {

        boolean bFlag = false;

        if (roles != null) {

            if (roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();
                    if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_COM) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                        bFlag = true;
                        break;
                    }
                }
            }
        }
        return bFlag;
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
