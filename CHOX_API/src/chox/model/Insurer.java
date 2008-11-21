package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import scsbre.model.IInsurerInfo;

public class Insurer implements Serializable, IInsurerInfo
{
	/** 
	 * This attribute maps to the column id in the insurer table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column name in the insurer table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column created_by in the insurer table.
	 */
	protected int createdBy;

        /** 
	 * This attribute maps to the column admin_handling_charge in the line_of_business table.
	 */
	protected BigDecimal adminHandlingCharge;

        
	/** 
	 * This attribute maps to the column created_date in the insurer table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the insurer table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the insurer table.
	 */
	protected Date lastModifiedDate;

	/**
	 * Method 'Insurer'
	 * 
	 */
	public Insurer()
	{
	}

        public BigDecimal getAdminHandlingCharge() {
            return adminHandlingCharge;
        }

        public void setAdminHandlingCharge(BigDecimal adminHandlingCharge) {
            this.adminHandlingCharge = adminHandlingCharge;
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
