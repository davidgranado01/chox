package chox.model;

import java.io.Serializable;
import java.util.Date;

public class ReasonOfRejection implements Serializable, Auditable
{
	protected int id;
	protected String name;
        protected String type;
        protected boolean status;
        protected WebUser createdBy;
	protected Date createdDate;
	protected WebUser lastModifiedBy;
	protected Date lastModifiedDate;
        
	public ReasonOfRejection()
	{
	}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
        
        
}
