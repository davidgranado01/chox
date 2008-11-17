package chox.model.hibernate;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class LineOfBusiness implements Serializable
{
	/** 
	 * This attribute maps to the column id in the LineOfBusiness table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column name in the LineOfBusiness table.
	 */
	protected String name;

	/** 
	 * This attribute maps to the column adminHandlingCharge in the LineOfBusiness table.
	 */
	protected long adminHandlingCharge;

	/** 
	 * This attribute represents whether the primitive attribute adminHandlingCharge is null.
	 */
	protected boolean adminHandlingChargeNull = true;

	/** 
	 * This attribute maps to the column address1 in the LineOfBusiness table.
	 */
	protected String address1;

	/** 
	 * This attribute maps to the column address2 in the LineOfBusiness table.
	 */
	protected String address2;

	/** 
	 * This attribute maps to the column address3 in the LineOfBusiness table.
	 */
	protected String address3;

	/** 
	 * This attribute maps to the column address4 in the LineOfBusiness table.
	 */
	protected String address4;

	/** 
	 * This attribute maps to the column address5 in the LineOfBusiness table.
	 */
	protected String address5;

	/** 
	 * This attribute maps to the column postcode in the LineOfBusiness table.
	 */
	protected String postcode;

	/** 
	 * This attribute maps to the column createdBy in the LineOfBusiness table.
	 */
	protected int createdBy;

	/** 
	 * This attribute represents whether the primitive attribute createdBy is null.
	 */
	protected boolean createdByNull = true;

	/** 
	 * This attribute maps to the column createdDate in the LineOfBusiness table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the LineOfBusiness table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute represents whether the primitive attribute lastModifiedBy is null.
	 */
	protected boolean lastModifiedByNull = true;

	/** 
	 * This attribute maps to the column lastModifiedDate in the LineOfBusiness table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute represents the foreign key relationship to the Insurer table.
	 */
	protected Insurer insurer;

	/**
	 * Method 'LineOfBusiness'
	 * 
	 */
	public LineOfBusiness()
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
	 * Method 'getAdminHandlingCharge'
	 * 
	 * @return long
	 */
	public long getAdminHandlingCharge()
	{
		return adminHandlingCharge;
	}

	/**
	 * Method 'setAdminHandlingCharge'
	 * 
	 * @param adminHandlingCharge
	 */
	public void setAdminHandlingCharge(long adminHandlingCharge)
	{
		this.adminHandlingCharge = adminHandlingCharge;
		this.adminHandlingChargeNull = false;
	}

	/** 
	 * Sets the value of adminHandlingChargeNull
	 */
	public void setAdminHandlingChargeNull(boolean adminHandlingChargeNull)
	{
		this.adminHandlingChargeNull = adminHandlingChargeNull;
	}

	/** 
	 * Gets the value of adminHandlingChargeNull
	 */
	public boolean isAdminHandlingChargeNull()
	{
		return adminHandlingChargeNull;
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
	 * Method 'getInsurer'
	 * 
	 * @return Insurer
	 */
	public Insurer getInsurer()
	{
		return insurer;
	}

	/**
	 * Method 'setInsurer'
	 * 
	 * @param insurer
	 */
	public void setInsurer(Insurer insurer)
	{
		this.insurer = insurer;
	}

}
