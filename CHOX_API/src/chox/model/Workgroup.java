package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Workgroup implements Serializable, Auditable
{	
	protected int id;
	protected String name;
	protected Insurer insurer;
        protected boolean status;
	protected WebUser createdBy;
	protected Date createdDate;
	protected WebUser lastModifiedBy;
	protected Date lastModifiedDate;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
