package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class SupportMessage implements Serializable,Auditable
{

	protected int id;
	protected int claimId;
	protected String subject;
	protected String message;
	protected WebUser createdBy;
	protected Date createdDate;
	protected WebUser lastModifiedBy;
	protected Date lastModifiedDate;

	public SupportMessage()
	{
	}

        public int getClaimId() {
            return claimId;
        }

        public void setClaimId(int claimId) {
            this.claimId = claimId;
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

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
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
