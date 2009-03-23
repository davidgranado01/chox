package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import scsbre.model.IVehicleClassInfo;

public class VehicleClass implements Serializable, IVehicleClassInfo, Auditable
{
	/** 
	 * This attribute maps to the column id in the vehicle_class table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column price in the vehicle_class table.
	 */
	protected BigDecimal price;

	/** 
	 * This attribute maps to the column name in the vehicle_class table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column created_by in the vehicle_class table.
	 */
	protected WebUser createdBy;

	/** 
	 * This attribute maps to the column created_date in the vehicle_class table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the vehicle_class table.
	 */
	protected WebUser lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the vehicle_class table.
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

        public String getCode() {
            return this.name;
        }

        public void setCode(String code) {
            this.name = code;
        }

}
