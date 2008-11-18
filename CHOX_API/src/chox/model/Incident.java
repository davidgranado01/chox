package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Incident implements Serializable
{
	/** 
	 * This attribute maps to the column date in the Incident table.
	 */
	protected Date date;

	/** 
	 * This attribute maps to the column location in the Incident table.
	 */
	protected String location;

	/** 
	 * This attribute maps to the column incidentDescription in the Incident table.
	 */
	protected String incidentDescription;

	/** 
	 * This attribute maps to the column id in the Incident table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column createdBy in the Incident table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the Incident table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the Incident table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the Incident table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column isPoliceInvolved in the Incident table.
	 */
	protected boolean isPoliceInvolved;

	/**
	 * Method 'Incident'
	 * 
	 */
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

	/**
	 * Method 'isIsPoliceInvolved'
	 * 
	 * @return boolean
	 */
	public boolean isIsPoliceInvolved()
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
