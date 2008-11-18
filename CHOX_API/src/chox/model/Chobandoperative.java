package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Chobandoperative implements Serializable
{
	/** 
	 * This attribute maps to the column id in the CHOBandOperative table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column bandId in the CHOBandOperative table.
	 */
	protected int bandId;

	/** 
	 * This attribute maps to the column CHOrganisationId in the CHOBandOperative table.
	 */
	protected int chorganisationid;

	/** 
	 * This attribute maps to the column createdBy in the CHOBandOperative table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the CHOBandOperative table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the CHOBandOperative table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the CHOBandOperative table.
	 */
	protected Date lastModifiedDate;

	/**
	 * Method 'Chobandoperative'
	 * 
	 */
	public Chobandoperative()
	{
	}

	/**
	 * Method 'getId'
	 * 
	 * @return int
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * Method 'setId'
	 * 
	 * @param id
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	/**
	 * Method 'getBandId'
	 * 
	 * @return int
	 */
	public int getBandId()
	{
		return bandId;
	}

	/**
	 * Method 'setBandId'
	 * 
	 * @param bandId
	 */
	public void setBandId(int bandId)
	{
		this.bandId = bandId;
	}

	/**
	 * Method 'getChorganisationid'
	 * 
	 * @return int
	 */
	public int getChorganisationid()
	{
		return chorganisationid;
	}

	/**
	 * Method 'setChorganisationid'
	 * 
	 * @param chorganisationid
	 */
	public void setChorganisationid(int chorganisationid)
	{
		this.chorganisationid = chorganisationid;
	}

	/**
	 * Method 'getCreatedBy'
	 * 
	 * @return int
	 */
	public int getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Method 'setCreatedBy'
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(int createdBy)
	{
		this.createdBy = createdBy;
	}

	/**
	 * Method 'getCreatedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * Method 'setCreatedDate'
	 * 
	 * @param createdDate
	 */
	public void setCreatedDate(java.util.Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * Method 'getLastModifiedBy'
	 * 
	 * @return int
	 */
	public int getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * Method 'setLastModifiedBy'
	 * 
	 * @param lastModifiedBy
	 */
	public void setLastModifiedBy(int lastModifiedBy)
	{
		this.lastModifiedBy = lastModifiedBy;
	}

	/**
	 * Method 'getLastModifiedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getLastModifiedDate()
	{
		return lastModifiedDate;
	}

	/**
	 * Method 'setLastModifiedDate'
	 * 
	 * @param lastModifiedDate
	 */
	public void setLastModifiedDate(java.util.Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}

}
