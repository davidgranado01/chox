package chox.model;

import java.io.Serializable;
import java.util.Date;

public class UserWorkgroup implements Serializable, Auditable
{	
    protected int id;	
    protected Workgroup workgroup;
    protected WebUser user;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WebUser getUser() {
        return user;
    }

    public void setUser(WebUser user) {
        this.user = user;
    }

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
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
