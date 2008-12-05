package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Witness implements Serializable,Auditable
{
	/** 
	 * This attribute maps to the column id in the witness table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column name in the witness table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column address1 in the witness table.
	 */
	protected String address1;

	/** 
	 * This attribute maps to the column address2 in the witness table.
	 */
	protected String address2;

	/** 
	 * This attribute maps to the column address3 in the witness table.
	 */
	protected String address3;

	/** 
	 * This attribute maps to the column address4 in the witness table.
	 */
	protected String address4;

	/** 
	 * This attribute maps to the column address5 in the witness table.
	 */
	protected String address5;

	/** 
	 * This attribute maps to the column postcode in the witness table.
	 */
	protected String postcode;

	/** 
	 * This attribute maps to the column telephone_day in the witness table.
	 */
	protected String telephoneDay;

	/** 
	 * This attribute maps to the column telephone_evening in the witness table.
	 */
	protected String telephoneEvening;

	/** 
	 * This attribute maps to the column email in the witness table.
	 */
	protected String email;

	/** 
	 * This attribute maps to the column created_by in the witness table.
	 */
	protected WebUser createdBy;

	/** 
	 * This attribute maps to the column created_date in the witness table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the witness table.
	 */
	protected WebUser lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the witness table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute represents the foreign key relationship to the incident table.
	 */
	protected Incident incident;

	/**
	 * Method 'Witness'
	 * 
	 */
	public Witness()
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
	 * Method 'getName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getName()
	{
		return name;
	}

	/**
	 * Method 'setName'
	 * 
	 * @param name
	 */
	public void setName(java.lang.String name)
	{
		this.name = name;
	}

	/**
	 * Method 'getAddress1'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress1()
	{
		return address1;
	}

	/**
	 * Method 'setAddress1'
	 * 
	 * @param address1
	 */
	public void setAddress1(java.lang.String address1)
	{
		this.address1 = address1;
	}

	/**
	 * Method 'getAddress2'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress2()
	{
		return address2;
	}

	/**
	 * Method 'setAddress2'
	 * 
	 * @param address2
	 */
	public void setAddress2(java.lang.String address2)
	{
		this.address2 = address2;
	}

	/**
	 * Method 'getAddress3'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress3()
	{
		return address3;
	}

	/**
	 * Method 'setAddress3'
	 * 
	 * @param address3
	 */
	public void setAddress3(java.lang.String address3)
	{
		this.address3 = address3;
	}

	/**
	 * Method 'getAddress4'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress4()
	{
		return address4;
	}

	/**
	 * Method 'setAddress4'
	 * 
	 * @param address4
	 */
	public void setAddress4(java.lang.String address4)
	{
		this.address4 = address4;
	}

	/**
	 * Method 'getAddress5'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAddress5()
	{
		return address5;
	}

	/**
	 * Method 'setAddress5'
	 * 
	 * @param address5
	 */
	public void setAddress5(java.lang.String address5)
	{
		this.address5 = address5;
	}

	/**
	 * Method 'getPostcode'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPostcode()
	{
		return postcode;
	}

	/**
	 * Method 'setPostcode'
	 * 
	 * @param postcode
	 */
	public void setPostcode(java.lang.String postcode)
	{
		this.postcode = postcode;
	}

	/**
	 * Method 'getTelephoneDay'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTelephoneDay()
	{
		return telephoneDay;
	}

	/**
	 * Method 'setTelephoneDay'
	 * 
	 * @param telephoneDay
	 */
	public void setTelephoneDay(java.lang.String telephoneDay)
	{
		this.telephoneDay = telephoneDay;
	}

	/**
	 * Method 'getTelephoneEvening'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTelephoneEvening()
	{
		return telephoneEvening;
	}

	/**
	 * Method 'setTelephoneEvening'
	 * 
	 * @param telephoneEvening
	 */
	public void setTelephoneEvening(java.lang.String telephoneEvening)
	{
		this.telephoneEvening = telephoneEvening;
	}

	/**
	 * Method 'getEmail'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEmail()
	{
		return email;
	}

	/**
	 * Method 'setEmail'
	 * 
	 * @param email
	 */
	public void setEmail(java.lang.String email)
	{
		this.email = email;
	}

	/**
	 * Method 'getCreatedBy'
	 * 
	 * @return int
	 */
	public WebUser getCreatedBy()
	{
		return createdBy;
	}

	/**
	 * Method 'setCreatedBy'
	 * 
	 * @param createdBy
	 */
	public void setCreatedBy(WebUser createdBy)
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
	public WebUser getLastModifiedBy()
	{
		return lastModifiedBy;
	}

	/**
	 * Method 'setLastModifiedBy'
	 * 
	 * @param lastModifiedBy
	 */
	public void setLastModifiedBy(WebUser lastModifiedBy)
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
	 * Method 'getIncident'
	 * 
	 * @return Incident
	 */
	public Incident getIncident()
	{
		return incident;
	}

	/**
	 * Method 'setIncident'
	 * 
	 * @param incident
	 */
	public void setIncident(Incident incident)
	{
		this.incident = incident;
	}

}
