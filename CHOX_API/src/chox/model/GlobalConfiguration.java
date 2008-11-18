package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class GlobalConfiguration implements Serializable
{
	/** 
	 * This attribute maps to the column parameter in the global_configuration table.
	 */
	protected String parameter;

	/** 
	 * This attribute maps to the column value in the global_configuration table.
	 */
	protected String value;

	/** 
	 * This attribute maps to the column id in the global_configuration table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column created_by in the global_configuration table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column created_date in the global_configuration table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the global_configuration table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the global_configuration table.
	 */
	protected Date lastModifiedDate;

	/**
	 * Method 'GlobalConfiguration'
	 * 
	 */
	public GlobalConfiguration()
	{
	}

	/**
	 * Method 'getParameter'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParameter()
	{
		return parameter;
	}

	/**
	 * Method 'setParameter'
	 * 
	 * @param parameter
	 */
	public void setParameter(java.lang.String parameter)
	{
		this.parameter = parameter;
	}

	/**
	 * Method 'getValue'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getValue()
	{
		return value;
	}

	/**
	 * Method 'setValue'
	 * 
	 * @param value
	 */
	public void setValue(java.lang.String value)
	{
		this.value = value;
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

}
