package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Incident implements Serializable
{

	protected Date date;
	protected String location;
	protected String incidentDescription;
	protected int id;
	protected int createdBy;
	protected boolean createdByNull = true;
	protected Date createdDate;
	protected int lastModifiedBy;
	protected boolean lastModifiedByNull = true;
	protected Date lastModifiedDate;
	protected boolean isPoliceInvolved;
        
	public Incident()
	{
	}

	/**
	 * Method 'getDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getDate()
	{
		return date;
	}

	/**
	 * Method 'setDate'
	 * 
	 * @param date
	 */
	public void setDate(java.util.Date date)
	{
		this.date = date;
	}

	/**
	 * Method 'getLocation'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLocation()
	{
		return location;
	}

	/**
	 * Method 'setLocation'
	 * 
	 * @param location
	 */
	public void setLocation(java.lang.String location)
	{
		this.location = location;
	}

	/**
	 * Method 'getIncidentDescription'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getIncidentDescription()
	{
		return incidentDescription;
	}

	/**
	 * Method 'setIncidentDescription'
	 * 
	 * @param incidentDescription
	 */
	public void setIncidentDescription(java.lang.String incidentDescription)
	{
		this.incidentDescription = incidentDescription;
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
		this.createdByNull = false;
	}

	/** 
	 * Sets the value of createdByNull
	 */
	public void setCreatedByNull(boolean createdByNull)
	{
		this.createdByNull = createdByNull;
	}

	/** 
	 * Gets the value of createdByNull
	 */
	public boolean isCreatedByNull()
	{
		return createdByNull;
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
		this.lastModifiedByNull = false;
	}

	/** 
	 * Sets the value of lastModifiedByNull
	 */
	public void setLastModifiedByNull(boolean lastModifiedByNull)
	{
		this.lastModifiedByNull = lastModifiedByNull;
	}

	/** 
	 * Gets the value of lastModifiedByNull
	 */
	public boolean isLastModifiedByNull()
	{
		return lastModifiedByNull;
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

	/**
	 * Method 'getIsPoliceInvolved'
	 * 
	 * @return short
	 */
	public boolean getIsPoliceInvolved()
	{
		return isPoliceInvolved;
	}

	/**
	 * Method 'setIsPoliceInvolved'
	 * 
	 * @param isPoliceInvolved
	 */
	public void setIsPoliceInvolved(boolean isPoliceInvolved)
	{
		this.isPoliceInvolved = isPoliceInvolved;
	}

}
