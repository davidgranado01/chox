package chox.model;

import java.util.Date;
import java.io.Serializable;

public class SystemLog implements Serializable,Auditable
{
	protected int id;
	protected String actionId;
        protected String message;
        protected String status;
        protected WebUser createdBy;
        protected Date createdDate;
        protected WebUser lastModifiedBy;
        protected Date lastModifiedDate;
        
	public SystemLog()
	{
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

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
        
}
