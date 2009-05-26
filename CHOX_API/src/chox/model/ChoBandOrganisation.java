package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class ChoBandOrganisation implements Serializable
{
	protected int id;
	// protected int bandId;
        // protected int chorganisationId;
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
        

        
        /*
         * 
	public int getBandId()
	{
		return bandId;
	}

	public void setBandId(int bandId)
	{
		this.bandId = bandId;
	}
         *          
	public int getChorganisationId()
	{
		return chorganisationId;
	}

	public void setChorganisationId(int chorganisationId)
	{
		this.chorganisationId = chorganisationId;
	}
        */
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
