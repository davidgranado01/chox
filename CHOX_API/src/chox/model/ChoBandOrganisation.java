package chox.model;

import java.io.Serializable;
import java.util.Date;

public class ChoBandOrganisation implements Serializable, Auditable
{
	protected int id;
        protected ChoBand choBand;
	protected Chorganisation chorganisation;
	protected WebUser createdBy;
	protected Date createdDate;
	protected WebUser lastModifiedBy;
	protected Date lastModifiedDate;

	public ChoBandOrganisation()
	{
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

        public ChoBand getChoBand() {
            return choBand;
        }

        public void setChoBand(ChoBand choBand) {
            this.choBand = choBand;
        }

        public Chorganisation getChorganisation() {
            return chorganisation;
        }

        public void setChorganisation(Chorganisation chorganisation) {
            this.chorganisation = chorganisation;
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
