package chox.model.hibernate;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
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
	protected long price;

	/** 
	 * This attribute represents whether the primitive attribute price is null.
	 */
	protected boolean priceNull = true;

	/** 
	 * This attribute maps to the column name in the VehicleClass table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column createdBy in the VehicleClass table.
	 */
	protected int createdBy;

	/** 
	 * This attribute represents whether the primitive attribute createdBy is null.
	 */
	protected boolean createdByNull = true;

	/** 
	 * This attribute maps to the column createdDate in the VehicleClass table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the VehicleClass table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute represents whether the primitive attribute lastModifiedBy is null.
	 */
	protected boolean lastModifiedByNull = true;

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
	 * @return long
	 */
	public long getPrice()
	{
		return price;
	}

	/**
	 * Method 'setPrice'
	 * 
	 * @param price
	 */
	public void setPrice(long price)
	{
		this.price = price;
		this.priceNull = false;
	}

	/** 
	 * Sets the value of priceNull
	 */
	public void setPriceNull(boolean priceNull)
	{
		this.priceNull = priceNull;
	}

	/** 
	 * Gets the value of priceNull
	 */
	public boolean isPriceNull()
	{
		return priceNull;
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

}
