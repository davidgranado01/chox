package idas.chox.web.viewdata;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.util.DateHelper;
import java.util.Iterator;
import java.util.Set;

public class UserViewData {

    private int id;
    private String userName;
    private String email;
    private String name;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String lastLoginDate;
    private String createdDate;
    private String orgName;
    private int orgType;
    private String role;
    private String isExpired;

    public UserViewData(WebUser object) {

        this.id = object.getId();
        this.userName = object.getUserName();
        this.email = object.getEmail();
        this.name = object.getFullName();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        if (object.getLastLoginDate() != null) {
            this.lastLoginDate = DateHelper.getLocalDateTimeFormat().format(object.getLastLoginDate());
        } else {
            this.lastLoginDate = "";
        }
        this.status = object.getStatus() && !object.isBlocked();
        this.isExpired = object.getIsExpired() ? "Yes" : "No";

        if (status) {
            this.statusDesc = "Yes";
        } else {
            this.statusDesc = "No";
        }

        if (object.isCHOXAdmin()) {

            this.orgType = 1;
            this.orgName = "Sherwood";

        } else {

            if (object.getInsurer() != null) {
                this.orgType = 2;
            } else {
                this.orgType = 3;
            }

            this.orgName = object.getOrganisationName();

        }

        this.role = getRoleString(object.getRoles());
    }

    private String getRoleString(Set roles) {

        Integer iRoles = roles.size();
        String sRole = "";



        if (iRoles > 0) {


            Iterator itr = roles.iterator();
            while (itr.hasNext()) {
                WebUserRole webUserrole = (WebUserRole) itr.next();
                if (!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHO) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX) && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {

                    sRole = sRole + webUserrole.getDescription() + ", ";
                }
            }

        }

        if ((sRole.trim()).length() <= 0) {
            sRole = "N/A";
        } else {
            sRole = sRole.substring(0, (sRole.length() - 2));
        }

        return sRole;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getOrgName() {
        return orgName;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the isExpired
     */
    public String getIsExpired() {
        return isExpired;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
