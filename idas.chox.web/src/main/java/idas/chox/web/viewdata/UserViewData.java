package idas.chox.web.viewdata;

import java.util.Iterator;
import java.util.Set;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.util.DateHelper;

public class UserViewData {

    private final int id;
    private String userName;
    private final String email;
    private final String name;
    private final boolean status;
    private final String statusDesc;
    private final String createdBy;
    private final String lastLoginDate;
    private final String createdDate;
    private final String orgName;
    private final int orgType;
    private final String role;
    private final String isExpired;

    public UserViewData(WebUser object) {

        this.id = object.getId();
        this.userName = object.getUserName();
        this.email = object.getEmail().startsWith("~~") ? "GDPR: data removed" : object.getEmail();
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
            this.orgName = "Audatex";

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
