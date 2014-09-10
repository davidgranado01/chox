package idas.chox.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Set;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;

public class RoleHelper {
    private static final Logger LOG = LoggerFactory.getLogger(RoleHelper.class);

    public static final int ORGANISATION_CHOX = 1;
    public static final int ORGANISATION_INS = 2;
    public static final int ORGANISATION_CHO = 3;

    /**
     * ***************************************************
     * WORKGROUP
     ****************************************************
     */
    public static boolean isWorkgroupRelatedUserOnly(WebUser user) {
        if (isWorkgroupValidationEnabledUser(user) && !hasNoneWorkgroupEnableRole(user.getRoles())) {
            return true;
        }
        return false;
    }

    public static boolean isWorkgroupValidationEnabledUser(WebUser user) {
        if (isInsurerUser(user)) {

            if (user.getInsurer().isWorkgroupEnable()) {

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

    public static boolean isManualWorkgroupValidationEnabledUser(WebUser user) {
        if (isInsurerUser(user)) {

            if (user.getInsurer().isEnableManualInvoiceWorkgroups()) {

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

    /**
     * ***************************************************
     * OWNERSHIP
     ****************************************************
     */
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
        } else if (isCreditHireUser(user)) {
            if (user.getChorganisation().isClaimOwnershipEnable()) {
                if (user.getRoles() != null) {
                    if (user.getRoles().size() > 0) {
                        Iterator itr = user.getRoles().iterator();
                        boolean isEnabled = false;
                        boolean isManager = false;
                        while (itr.hasNext()) {
                            WebUserRole webUserrole = (WebUserRole) itr.next();
                            if (webUserrole.isOwnershipRelated()) {
                                isEnabled = true;
                            }
                            if (webUserrole.getName().equals(WebUserRole.ROLE_CHO_MNG)) {
                                isManager = true;
                            }
                        }
                        return isEnabled && !isManager;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isManualOwnershipValidationEnabledUser(WebUser user) {

        if (isInsurerUser(user)) {
            if (user.getInsurer().isEnableManualInvoiceOwnership()) {
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

    /**
     * ***************************************************
     * OTHER
     ****************************************************
     */
    public static boolean isChoxAdmin(WebUser user) {
        if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_CHOX)) {
            return true;
        }
        return false;
    }

    public static boolean isInsurerUser(WebUser user) {
        if (user == null) {
            LOG.warn("Cannot determine if insurer from null user");
        }
        else if (isCheckSelectedRoleExist(user.getRoles(), WebUserRole.ROLE_INS)) {
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

        if (roles != null && !roles.isEmpty()) {

            Iterator itr = roles.iterator();

            while (itr.hasNext()) {

                WebUserRole webUserrole = (WebUserRole) itr.next();
                if (!webUserrole.isWorkgroupRelated() && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                    return true;
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
