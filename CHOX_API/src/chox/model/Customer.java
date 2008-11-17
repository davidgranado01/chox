package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable
{
	/** 
	 * This attribute maps to the column id in the Customer table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column title in the Customer table.
	 */
	protected String title;

	/** 
	 * This attribute maps to the column firstnames in the Customer table.
	 */
	protected String firstnames;

	/** 
	 * This attribute maps to the column lastname in the Customer table.
	 */
	protected String lastname;

	/** 
	 * This attribute maps to the column address1 in the Customer table.
	 */
	protected String address1;

	/** 
	 * This attribute maps to the column address2 in the Customer table.
	 */
	protected String address2;

	/** 
	 * This attribute maps to the column address3 in the Customer table.
	 */
	protected String address3;

	/** 
	 * This attribute maps to the column address4 in the Customer table.
	 */
	protected String address4;

	/** 
	 * This attribute maps to the column address5 in the Customer table.
	 */
	protected String address5;

	/** 
	 * This attribute maps to the column postcode in the Customer table.
	 */
	protected String postcode;

	/** 
	 * This attribute maps to the column telephoneDay in the Customer table.
	 */
	protected String telephoneDay;

	/** 
	 * This attribute maps to the column telephoneEvening in the Customer table.
	 */
	protected String telephoneEvening;

	/** 
	 * This attribute maps to the column email in the Customer table.
	 */
	protected String email;

	/** 
	 * This attribute maps to the column vehicleRegistration in the Customer table.
	 */
	protected String vehicleRegistration;

	/** 
	 * This attribute maps to the column vehicleManufacturer in the Customer table.
	 */
	protected String vehicleManufacturer;

	/** 
	 * This attribute maps to the column vehicleModel in the Customer table.
	 */
	protected String vehicleModel;

	/** 
	 * This attribute maps to the column location in the Customer table.
	 */
	protected String location;

	/** 
	 * This attribute maps to the column damage in the Customer table.
	 */
	protected String damage;

	/** 
	 * This attribute maps to the column initialEcd in the Customer table.
	 */
	protected Date initialEcd;

	/** 
	 * This attribute maps to the column policyNumber in the Customer table.
	 */
	protected String policyNumber;

	/** 
	 * This attribute maps to the column claimReference in the Customer table.
	 */
	protected String claimReference;

	/** 
	 * This attribute maps to the column comprehensive in the Customer table.
	 */
	protected boolean comprehensive;

	/** 
	 * This attribute maps to the column createdBy in the Customer table.
	 */
	protected int createdBy;

	/** 
	 * This attribute represents whether the primitive attribute createdBy is null.
	 */
	protected boolean createdByNull = true;

	/** 
	 * This attribute maps to the column createdDate in the Customer table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the Customer table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute represents whether the primitive attribute lastModifiedBy is null.
	 */
	protected boolean lastModifiedByNull = true;

	/** 
	 * This attribute maps to the column lastModifiedDate in the Customer table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column isPrimaryDriver in the Customer table.
	 */
	protected boolean isPrimaryDriver;

	/** 
	 * This attribute maps to the column isUsable in the Customer table.
	 */
	protected boolean isUsable;

	/** 
	 * This attribute maps to the column isActive in the Customer table.
	 */
	protected boolean isActive;

	/** 
	 * This attribute maps to the column insurerName in the Customer table.
	 */
	protected String insurerName;

	/** 
	 * This attribute represents the foreign key relationship to the VehicleClass table.
	 */
	protected VehicleClass vehicleClass;

	/**
	 * Method 'Customer'
	 * 
	 */
	public Customer()
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
	 * Method 'getTitle'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle()
	{
		return title;
	}

	/**
	 * Method 'setTitle'
	 * 
	 * @param title
	 */
	public void setTitle(java.lang.String title)
	{
		this.title = title;
	}

	/**
	 * Method 'getFirstnames'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFirstnames()
	{
		return firstnames;
	}

	/**
	 * Method 'setFirstnames'
	 * 
	 * @param firstnames
	 */
	public void setFirstnames(java.lang.String firstnames)
	{
		this.firstnames = firstnames;
	}

	/**
	 * Method 'getLastname'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLastname()
	{
		return lastname;
	}

	/**
	 * Method 'setLastname'
	 * 
	 * @param lastname
	 */
	public void setLastname(java.lang.String lastname)
	{
		this.lastname = lastname;
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
	 * Method 'getVehicleRegistration'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVehicleRegistration()
	{
		return vehicleRegistration;
	}

	/**
	 * Method 'setVehicleRegistration'
	 * 
	 * @param vehicleRegistration
	 */
	public void setVehicleRegistration(java.lang.String vehicleRegistration)
	{
		this.vehicleRegistration = vehicleRegistration;
	}

	/**
	 * Method 'getVehicleManufacturer'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVehicleManufacturer()
	{
		return vehicleManufacturer;
	}

	/**
	 * Method 'setVehicleManufacturer'
	 * 
	 * @param vehicleManufacturer
	 */
	public void setVehicleManufacturer(java.lang.String vehicleManufacturer)
	{
		this.vehicleManufacturer = vehicleManufacturer;
	}

	/**
	 * Method 'getVehicleModel'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVehicleModel()
	{
		return vehicleModel;
	}

	/**
	 * Method 'setVehicleModel'
	 * 
	 * @param vehicleModel
	 */
	public void setVehicleModel(java.lang.String vehicleModel)
	{
		this.vehicleModel = vehicleModel;
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
	 * Method 'getDamage'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDamage()
	{
		return damage;
	}

	/**
	 * Method 'setDamage'
	 * 
	 * @param damage
	 */
	public void setDamage(java.lang.String damage)
	{
		this.damage = damage;
	}

	/**
	 * Method 'getInitialEcd'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getInitialEcd()
	{
		return initialEcd;
	}

	/**
	 * Method 'setInitialEcd'
	 * 
	 * @param initialEcd
	 */
	public void setInitialEcd(java.util.Date initialEcd)
	{
		this.initialEcd = initialEcd;
	}

	/**
	 * Method 'getPolicyNumber'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPolicyNumber()
	{
		return policyNumber;
	}

	/**
	 * Method 'setPolicyNumber'
	 * 
	 * @param policyNumber
	 */
	public void setPolicyNumber(java.lang.String policyNumber)
	{
		this.policyNumber = policyNumber;
	}

	/**
	 * Method 'getClaimReference'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getClaimReference()
	{
		return claimReference;
	}

	/**
	 * Method 'setClaimReference'
	 * 
	 * @param claimReference
	 */
	public void setClaimReference(java.lang.String claimReference)
	{
		this.claimReference = claimReference;
	}

    public boolean isComprehensive() {
        return comprehensive;
    }

    public void setComprehensive(boolean comprehensive) {
        this.comprehensive = comprehensive;
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
	 * Method 'getInsurerName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getInsurerName()
	{
		return insurerName;
	}

	/**
	 * Method 'setInsurerName'
	 * 
	 * @param insurerName
	 */
	public void setInsurerName(java.lang.String insurerName)
	{
		this.insurerName = insurerName;
	}

	/**
	 * Method 'getVehicleClass'
	 * 
	 * @return VehicleClass
	 */
	public VehicleClass getVehicleClass()
	{
		return vehicleClass;
	}

	/**
	 * Method 'setVehicleClass'
	 * 
	 * @param vehicleClass
	 */
	public void setVehicleClass(VehicleClass vehicleClass)
	{
		this.vehicleClass = vehicleClass;
	}

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsPrimaryDriver() {
        return isPrimaryDriver;
    }

    public void setIsPrimaryDriver(boolean isPrimaryDriver) {
        this.isPrimaryDriver = isPrimaryDriver;
    }

    public boolean isIsUsable() {
        return isUsable;
    }

    public void setIsUsable(boolean isUsable) {
        this.isUsable = isUsable;
    }

}
