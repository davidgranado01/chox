package chox.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Rental implements Serializable
{
        public static final String PENDING = "Pending";
        public static final String IN_PROGRESS = "InProgress";
        public static final String COMPLETE = "Complete";
        public static final String CANCELLED = "Cancelled";
    
	/** 
	 * This attribute maps to the column id in the rental table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column uuid in the rental table.
	 */
	protected String uuid = UUID.randomUUID().toString();

	/** 
	 * This attribute maps to the column supplier_reference in the rental table.
	 */
	protected String supplierReference;

	/** 
	 * This attribute maps to the column rental_status in the rental table.
	 */
	protected String rentalStatus;

	/** 
	 * This attribute maps to the column first_contact in the rental table.
	 */
	protected Date firstContact;

	/** 
	 * This attribute maps to the column created in the rental table.
	 */
	protected Date created;

	/** 
	 * This attribute represents the foreign key relationship to the supplier table.
	 */
	protected Supplier supplier;

	/**
	 * Method 'Rental'
	 * 
	 */
	public Rental()
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
	 * Method 'getUuid'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUuid()
	{
		return uuid;
	}

	/**
	 * Method 'setUuid'
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid)
	{
		this.uuid = uuid;
	}

	/**
	 * Method 'getSupplierReference'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSupplierReference()
	{
		return supplierReference;
	}

	/**
	 * Method 'setSupplierReference'
	 * 
	 * @param supplierReference
	 */
	public void setSupplierReference(java.lang.String supplierReference)
	{
		this.supplierReference = supplierReference;
	}

	/**
	 * Method 'getRentalStatus'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRentalStatus()
	{
		return rentalStatus;
	}

	/**
	 * Method 'setRentalStatus'
	 * 
	 * @param rentalStatus
	 */
	public void setRentalStatus(java.lang.String rentalStatus)
	{
		this.rentalStatus = rentalStatus;
	}

	/**
	 * Method 'getFirstContact'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getFirstContact()
	{
		return firstContact;
	}

	/**
	 * Method 'setFirstContact'
	 * 
	 * @param firstContact
	 */
	public void setFirstContact(java.util.Date firstContact)
	{
		this.firstContact = firstContact;
	}

	/**
	 * Method 'getCreated'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getCreated()
	{
		return created;
	}

	/**
	 * Method 'setCreated'
	 * 
	 * @param created
	 */
	public void setCreated(java.util.Date created)
	{
		this.created = created;
	}

	/**
	 * Method 'getSupplier'
	 * 
	 * @return Supplier
	 */
	public Supplier getSupplier()
	{
		return supplier;
	}

	/**
	 * Method 'setSupplier'
	 * 
	 * @param supplier
	 */
	public void setSupplier(Supplier supplier)
	{
		this.supplier = supplier;
	}

}
