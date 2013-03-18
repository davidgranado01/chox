package idas.chox.core.model;

import java.util.Set;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import idas.chox.core.util.RoleHelper;
import idas.chox.core.common.OrganisationType;

public class WebUser extends Entity implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="PARAMETERS">
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String telephone;
    private boolean status;
    private Date lastLoginDate;
    private Date passwordLastModifiedDate;
    private Chorganisation chorganisation;
    private Insurer insurer;
    private Boolean isExpired;
//    private WebUserRole webUserRole;
    private Set roles;
    private Set workgroups;
    private String organisationName;
    private boolean claimHandler = false;
    private Set workgroupRelatedRoles;
    private boolean showSplash;
    private boolean blocked;
    private int failedLoginAttempts;
    private Date blockedDate;
    
    // </editor-fold>

    public WebUser() {
        isExpired = false;
        showSplash = true;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public Date getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(Date blockedDate) {
        this.blockedDate = blockedDate;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Date getPasswordLastModifiedDate() {
        return passwordLastModifiedDate;
    }

    public void setPasswordLastModifiedDate(Date passwordLastModifiedDate) {
        this.passwordLastModifiedDate = passwordLastModifiedDate;
    }

    public boolean isShowSplash() {
        return showSplash;
    }

    public void setShowSplash(boolean showSplash) {
        this.showSplash = showSplash;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public java.lang.String getEmail() {
        return email;
    }

    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    public java.lang.String getFirstName() {
        return firstName;
    }

    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    public java.lang.String getLastName() {
        return lastName;
    }

    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public boolean getStatus() {
        return status;
    }

    public boolean getStatusOrBlocked() {
        return status && !blocked;
    }

    public void setStatus(boolean status) {
        this.status = status;
        if (status && blocked)
            blocked = false;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public void setRoles(Set roles) {
        this.roles = roles;
    }

    public Set getRoles() {
        return this.roles;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Set getWorkgroups() {
        return this.workgroups;
    }

    public void setWorkgroups(Set workgroups) {
        this.workgroups = workgroups;
    }

    // </editor-fold>
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    @Override
    public String toString() {
        String orgName = String.format("(%1$s)", getOrganisationName());
        return String.format("%1$s, %2$s %3$s", this.getLastName(), this.getFirstName(), orgName);
    }

    public Set<Integer> getWorkgroupIds() {
        Set<Integer> ids = new HashSet<Integer>();
        Iterator itr = workgroups.iterator();

        while (itr.hasNext()) {
            Workgroup workgroup = (Workgroup) itr.next();
            ids.add(workgroup.getId());
        }
        return ids;
    }

    public int getMaxFailedLoginAttempts() {
        if (getOrganisationType().equals(OrganisationType.INS)) {
            return insurer.getMaxLoginAttempts();
        }
        else if (getOrganisationType().equals(OrganisationType.CHO)) {
            return chorganisation.getMaxLoginAttempts();
        }
        
        return 0;
    }


    public String getDisplayName() {
        return String.format("%1$s, %2$s", this.getLastName(), this.getFirstName());
    }

    public String getFullName() {
        return String.format("%1$s %2$s", this.getFirstName(), this.getLastName());
    }

    public void setClaimHandler(boolean claimHandler) {
        this.claimHandler = claimHandler;
    }

    public String getOrganisationName() {
        Chorganisation cho = this.getChorganisation();
        Insurer ins = this.getInsurer();

        if (ins != null) {
            organisationName = ins.getName();
        } else if (cho != null) {
            organisationName = cho.getName();
        }

        return organisationName;
    }

    public boolean isCHOXAdmin() {

        boolean bFlag = false;

        if (this.roles != null) {

            if (this.roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)) {
                        bFlag = true;
                        break;
                    }

                }
            }
        }

        return bFlag;
    }

    public boolean isAnInsurer() {

        boolean bFlag = false;

        if (this.roles != null) {

            if (this.roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)) {
                        bFlag = true;
                        break;
                    }

                }
            }
        }

        return bFlag;
    }
    
    public boolean isCHO() {

        boolean bFlag = false;

        if (this.roles != null) {

            if (this.roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHO)) {
                        bFlag = true;
                        break;
                    }

                }
            }
        }

        return bFlag;
    }

    public boolean isClaimHandler() {

        boolean bFlag = false;

        if (this.roles.size() > 0) {
            Iterator itr = roles.iterator();

            while (itr.hasNext()) {

                WebUserRole webUserrole = (WebUserRole) itr.next();

                if (webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH)) {
                    bFlag = true;
                    break;
                }
            }
        }

        return bFlag;

    }
    
    public boolean isInRoleOf(String role) {

        boolean bFlag = false;

        if (this.roles != null) {

            if (this.roles.size() > 0) {

                Iterator itr = roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.getName().equalsIgnoreCase(role)) {
                        bFlag = true;
                        break;
                    }

                }
            }
        }

        return bFlag;
    }
        
    public String getOrganisationType() {

        String orgType = OrganisationType.CHOX;
        if (RoleHelper.isCheckSelectedRoleExist(this.roles, WebUserRole.ROLE_CHOX)) {
            orgType = OrganisationType.CHOX;
        } else if (RoleHelper.isCheckSelectedRoleExist(this.roles, WebUserRole.ROLE_CHO)) {
            orgType = OrganisationType.CHO;
        } else if (RoleHelper.isCheckSelectedRoleExist(this.roles, WebUserRole.ROLE_INS)) {
            orgType = OrganisationType.INS;
        }

        return orgType;
    }

    public Set getWorkgroupRelatedRoles() {

        if (workgroupRelatedRoles == null) {
            setWorkgroupRelatedRoles();
        }

        return workgroupRelatedRoles;
    }

    private void setWorkgroupRelatedRoles() {

        workgroupRelatedRoles = new HashSet();

        if (this.roles != null) {

            if (this.roles.size() > 0) {

                Iterator itr = this.roles.iterator();

                while (itr.hasNext()) {

                    WebUserRole webUserrole = (WebUserRole) itr.next();

                    if (webUserrole.isWorkgroupRelated()) {
                        workgroupRelatedRoles.add(webUserrole);
                    }

                }
            }
        }
    }

    
}
