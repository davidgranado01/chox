package idas.chox.core.model;

import idas.chox.core.util.RoleHelper;
import idas.chox.core.common.OrganisationType;
import java.util.Set;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

public class WebUser implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="PARAMETERS">
    
    protected int id;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected String password;
    protected boolean status;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;
    protected Chorganisation chorganisation;
    protected Insurer insurer;
    private Boolean isExpired;
    protected WebUserRole webUserRole;
    protected Set roles;
    private Set workgroups;
    protected String organisationName;
    protected boolean claimHandler = false;

    // </editor-fold>
    
    public WebUser() {
        isExpired = false;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public void setStatus(boolean status) {
        this.status = status;
    }

    public WebUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public WebUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(WebUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(java.util.Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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
        return workgroups;
    }

    public void setWorkgroups(Set workgroups) {
        this.workgroups = workgroups;
    }
    
    // </editor-fold>

    @Override
    public String toString() {
        String orgName = orgName = String.format("(%1$s)", getOrganisationName());
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

    public String getDisplayName() {
        return String.format("%1$s %2$s", this.getLastName(), this.getFirstName());
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

        if(this.roles!=null){
            
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

    public boolean isClaimHandler() {

        boolean bFlag = false;

        if (this.roles.size() > 0) {
            Iterator itr = roles.iterator();

            while (itr.hasNext()) {

                WebUserRole webUserrole = (WebUserRole) itr.next();

                if (webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CH)
                        || webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_COM)
                        || webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_FNOL)) {
                    bFlag = true;
                    break;
                }
            }
        }

        return bFlag;

    }
    
    public String getOrganisationType() {

        String orgType = OrganisationType.CHO;
        
        if (RoleHelper.isCheckSelectedRoleExist(this.roles, WebUserRole.ROLE_CHOX)) {
            orgType = OrganisationType.CHOX;
        } else if (RoleHelper.isCheckSelectedRoleExist(this.roles, WebUserRole.ROLE_CHO)) {
            orgType = OrganisationType.CHO;
        } else if (RoleHelper.isCheckSelectedRoleExist(this.roles, WebUserRole.ROLE_INS)) {
            orgType = OrganisationType.INS;
        }
        
        return orgType;
    }
    
}