package chox.model;

import java.io.Serializable;
import java.util.Date;

public class WebUserUserRole implements Serializable, Auditable{
    
    protected Integer id;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;
    protected boolean active;
    protected WebUser webUser;
    protected WebUserRole webUserRole;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public WebUserRole getWebUserRole() {
        return webUserRole;
    }

    public void setWebUserRole(WebUserRole webUserRole) {
        this.webUserRole = webUserRole;
    }

    

    

    
    
    public WebUser getCreatedBy()
    {
            return createdBy;
    }

    public void setCreatedBy(WebUser createdBy)
    {
            this.createdBy = createdBy;
    }

    public java.util.Date getCreatedDate()
    {
            return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate)
    {
            this.createdDate = createdDate;
    }

    public WebUser getLastModifiedBy()
    {
            return lastModifiedBy;
    }

    public void setLastModifiedBy(WebUser lastModifiedBy)
    {
            this.lastModifiedBy = lastModifiedBy;
    }

    public java.util.Date getLastModifiedDate()
    {
            return lastModifiedDate;
    }

    public void setLastModifiedDate(java.util.Date lastModifiedDate)
    {
            this.lastModifiedDate = lastModifiedDate;
    }
    
}
