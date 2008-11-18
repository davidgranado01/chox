package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class ThirdParty implements Serializable
{
	/** 
	 * This attribute maps to the column id in the ThirdParty table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column policyNumber in the ThirdParty table.
	 */
	protected String policyNumber;

	/** 
	 * This attribute maps to the column claimReference in the ThirdParty table.
	 */
	protected String claimReference;

	/** 
	 * This attribute maps to the column vehicleRegistration in the ThirdParty table.
	 */
	protected String vehicleRegistration;

	/** 
	 * This attribute maps to the column vehicleManufacturer in the ThirdParty table.
	 */
	protected String vehicleManufacturer;

	/** 
	 * This attribute maps to the column vehicleModel in the ThirdParty table.
	 */
	protected String vehicleModel;

	/** 
	 * This attribute maps to the column vehicleClassId in the ThirdParty table.
	 */
	protected VehicleClass vehicleClass;

	/** 
	 * This attribute represents whether the primitive attribute vehicleClassId is null.
	 */
	protected boolean vehicleClassIdNull = true;

	/** 
	 * This attribute maps to the column address1 in the ThirdParty table.
	 */
	protected String address1;

	/** 
	 * This attribute maps to the column address2 in the ThirdParty table.
	 */
	protected String address2;

	/** 
	 * This attribute maps to the column address3 in the ThirdParty table.
	 */
	protected String address3;

	/** 
	 * This attribute maps to the column address4 in the ThirdParty table.
	 */
	protected String address4;

	/** 
	 * This attribute maps to the column address5 in the ThirdParty table.
	 */
	protected String address5;

	/** 
	 * This attribute maps to the column postcode in the ThirdParty table.
	 */
	protected String postcode;

	/** 
	 * This attribute maps to the column telephoneDay in the ThirdParty table.
	 */
	protected String telephoneDay;

	/** 
	 * This attribute maps to the column telephoneEvening in the ThirdParty table.
	 */
	protected String telephoneEvening;

	/** 
	 * This attribute maps to the column email in the ThirdParty table.
	 */
	protected String email;

	/** 
	 * This attribute maps to the column createdBy in the ThirdParty table.
	 */
	protected int createdBy;

	/** 
	 * This attribute represents whether the primitive attribute createdBy is null.
	 */
	protected boolean createdByNull = true;

	/** 
	 * This attribute maps to the column createdDate in the ThirdParty table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the ThirdParty table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute represents whether the primitive attribute lastModifiedBy is null.
	 */
	protected boolean lastModifiedByNull = true;

	/** 
	 * This attribute maps to the column lastModifiedDate in the ThirdParty table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column firstNames in the ThirdParty table.
	 */
	protected String firstNames;

	/** 
	 * This attribute maps to the column lastName in the ThirdParty table.
	 */
	protected String lastName;

	/** 
	 * This attribute maps to the column title in the ThirdParty table.
	 */
	protected String title;

	/** 
	 * This attribute represents the foreign key relationship to the Insurer table.
	 */
	protected Insurer insurer;

	/**
	 * Method 'ThirdParty'
	 * 
	 */
	public ThirdParty()
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

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }



	/** 
	 * Sets the value of vehicleClassIdNull
	 */
	public void setVehicleClassIdNull(boolean vehicleClassIdNull)
	{
		this.vehicleClassIdNull = vehicleClassIdNull;
	}

	/** 
	 * Gets the value of vehicleClassIdNull
	 */
	public boolean isVehicleClassIdNull()
	{
		return vehicleClassIdNull;
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
	 * Method 'getFirstNames'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getFirstNames()
	{
		return firstNames;
	}

	/**
	 * Method 'setFirstNames'
	 * 
	 * @param firstNames
	 */
	public void setFirstNames(java.lang.String firstNames)
	{
		this.firstNames = firstNames;
	}

	/**
	 * Method 'getLastName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLastName()
	{
		return lastName;
	}

	/**
	 * Method 'setLastName'
	 * 
	 * @param lastName
	 */
	public void setLastName(java.lang.String lastName)
	{
		this.lastName = lastName;
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
