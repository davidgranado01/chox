package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class VehicleClass implements Serializable
{
	/** 
	 * This attribute maps to the column id in the VehicleClass table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column price in the VehicleClass table.
	 */
	protected BigDecimal price;

	/** 
	 * This attribute maps to the column name in the VehicleClass table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column createdBy in the VehicleClass table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the VehicleClass table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the VehicleClass table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the VehicleClass table.
	 */
	protected Date lastModifiedDate;

	/**
	 * Method 'VehicleClass'
	 * 
	 */
	public VehicleClass()
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
	 * Method 'getPrice'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getPrice()
	{
		return price;
	}

	/**
	 * Method 'setPrice'
	 * 
	 * @param price
	 */
	public void setPrice(java.math.BigDecimal price)
	{
		this.price = price;
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
