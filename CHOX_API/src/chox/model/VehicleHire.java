package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class VehicleHire implements Serializable
{
	/** 
	 * This attribute maps to the column id in the VehicleHire table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column vehicleRegistration in the VehicleHire table.
	 */
	protected String vehicleRegistration;

	/** 
	 * This attribute maps to the column vehicleManufacturer in the VehicleHire table.
	 */
	protected String vehicleManufacturer;

	/** 
	 * This attribute maps to the column vehicleModel in the VehicleHire table.
	 */
	protected String vehicleModel;

	/** 
	 * This attribute maps to the column rentalStart in the VehicleHire table.
	 */
	protected Date rentalStart;

	/** 
	 * This attribute maps to the column rentalEnd in the VehicleHire table.
	 */
	protected Date rentalEnd;

	/** 
	 * This attribute maps to the column collectionReason in the VehicleHire table.
	 */
	protected String collectionReason;

	/** 
	 * This attribute maps to the column days in the VehicleHire table.
	 */
	protected Integer days;

	/** 
	 * This attribute maps to the column cdwFee in the VehicleHire table.
	 */
	protected boolean cdwFee;

	/** 
	 * This attribute maps to the column automaticFee in the VehicleHire table.
	 */
	protected boolean automaticFee;

	/** 
	 * This attribute maps to the column satNavFee in the VehicleHire table.
	 */
	protected boolean satNavFee;

	/** 
	 * This attribute maps to the column estateFee in the VehicleHire table.
	 */
	protected boolean estateFee;

	/** 
	 * This attribute maps to the column babySeatFee in the VehicleHire table.
	 */
	protected boolean babySeatFee;

	/** 
	 * This attribute maps to the column towBarsFee in the VehicleHire table.
	 */
	protected boolean towBarsFee;

	/** 
	 * This attribute maps to the column nonStandardInsurancePremiumFee in the VehicleHire table.
	 */
	protected boolean nonStandardInsurancePremiumFee;

	/** 
	 * This attribute maps to the column adminFee in the VehicleHire table.
	 */
	protected boolean adminFee;

	/** 
	 * This attribute maps to the column roofRackFee in the VehicleHire table.
	 */
	protected boolean roofRackFee;

	/** 
	 * This attribute maps to the column dualControlFee in the VehicleHire table.
	 */
	protected boolean dualControlFee;

	/** 
	 * This attribute maps to the column deliveryCollectionFee in the VehicleHire table.
	 */
	protected boolean deliveryCollectionFee;

	/** 
	 * This attribute maps to the column createdBy in the VehicleHire table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the VehicleHire table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the VehicleHire table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the VehicleHire table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute represents the foreign key relationship to the VehicleClass table.
	 */
	protected VehicleClass vehicleClass;

	/**
	 * Method 'VehicleHire'
	 * 
	 */
	public VehicleHire()
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
	 * Method 'getRentalStart'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getRentalStart()
	{
		return rentalStart;
	}

	/**
	 * Method 'setRentalStart'
	 * 
	 * @param rentalStart
	 */
	public void setRentalStart(java.util.Date rentalStart)
	{
		this.rentalStart = rentalStart;
	}

	/**
	 * Method 'getRentalEnd'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getRentalEnd()
	{
		return rentalEnd;
	}

	/**
	 * Method 'setRentalEnd'
	 * 
	 * @param rentalEnd
	 */
	public void setRentalEnd(java.util.Date rentalEnd)
	{
		this.rentalEnd = rentalEnd;
	}

	/**
	 * Method 'getCollectionReason'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCollectionReason()
	{
		return collectionReason;
	}

	/**
	 * Method 'setCollectionReason'
	 * 
	 * @param collectionReason
	 */
	public void setCollectionReason(java.lang.String collectionReason)
	{
		this.collectionReason = collectionReason;
	}

	/**
	 * Method 'getDays'
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDays()
	{
		return days;
	}

	/**
	 * Method 'setDays'
	 * 
	 * @param days
	 */
	public void setDays(java.lang.Integer days)
	{
		this.days = days;
	}

	/**
	 * Method 'isCdwFee'
	 * 
	 * @return boolean
	 */
	public boolean isCdwFee()
	{
		return cdwFee;
	}

	/**
	 * Method 'setCdwFee'
	 * 
	 * @param cdwFee
	 */
	public void setCdwFee(boolean cdwFee)
	{
		this.cdwFee = cdwFee;
	}

	/**
	 * Method 'isAutomaticFee'
	 * 
	 * @return boolean
	 */
	public boolean isAutomaticFee()
	{
		return automaticFee;
	}

	/**
	 * Method 'setAutomaticFee'
	 * 
	 * @param automaticFee
	 */
	public void setAutomaticFee(boolean automaticFee)
	{
		this.automaticFee = automaticFee;
	}

	/**
	 * Method 'isSatNavFee'
	 * 
	 * @return boolean
	 */
	public boolean isSatNavFee()
	{
		return satNavFee;
	}

	/**
	 * Method 'setSatNavFee'
	 * 
	 * @param satNavFee
	 */
	public void setSatNavFee(boolean satNavFee)
	{
		this.satNavFee = satNavFee;
	}

	/**
	 * Method 'isEstateFee'
	 * 
	 * @return boolean
	 */
	public boolean isEstateFee()
	{
		return estateFee;
	}

	/**
	 * Method 'setEstateFee'
	 * 
	 * @param estateFee
	 */
	public void setEstateFee(boolean estateFee)
	{
		this.estateFee = estateFee;
	}

	/**
	 * Method 'isBabySeatFee'
	 * 
	 * @return boolean
	 */
	public boolean isBabySeatFee()
	{
		return babySeatFee;
	}

	/**
	 * Method 'setBabySeatFee'
	 * 
	 * @param babySeatFee
	 */
	public void setBabySeatFee(boolean babySeatFee)
	{
		this.babySeatFee = babySeatFee;
	}

	/**
	 * Method 'isTowBarsFee'
	 * 
	 * @return boolean
	 */
	public boolean isTowBarsFee()
	{
		return towBarsFee;
	}

	/**
	 * Method 'setTowBarsFee'
	 * 
	 * @param towBarsFee
	 */
	public void setTowBarsFee(boolean towBarsFee)
	{
		this.towBarsFee = towBarsFee;
	}

	/**
	 * Method 'isNonStandardInsurancePremiumFee'
	 * 
	 * @return boolean
	 */
	public boolean isNonStandardInsurancePremiumFee()
	{
		return nonStandardInsurancePremiumFee;
	}

	/**
	 * Method 'setNonStandardInsurancePremiumFee'
	 * 
	 * @param nonStandardInsurancePremiumFee
	 */
	public void setNonStandardInsurancePremiumFee(boolean nonStandardInsurancePremiumFee)
	{
		this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
	}

	/**
	 * Method 'isAdminFee'
	 * 
	 * @return boolean
	 */
	public boolean isAdminFee()
	{
		return adminFee;
	}

	/**
	 * Method 'setAdminFee'
	 * 
	 * @param adminFee
	 */
	public void setAdminFee(boolean adminFee)
	{
		this.adminFee = adminFee;
	}

	/**
	 * Method 'isRoofRackFee'
	 * 
	 * @return boolean
	 */
	public boolean isRoofRackFee()
	{
		return roofRackFee;
	}

	/**
	 * Method 'setRoofRackFee'
	 * 
	 * @param roofRackFee
	 */
	public void setRoofRackFee(boolean roofRackFee)
	{
		this.roofRackFee = roofRackFee;
	}

	/**
	 * Method 'isDualControlFee'
	 * 
	 * @return boolean
	 */
	public boolean isDualControlFee()
	{
		return dualControlFee;
	}

	/**
	 * Method 'setDualControlFee'
	 * 
	 * @param dualControlFee
	 */
	public void setDualControlFee(boolean dualControlFee)
	{
		this.dualControlFee = dualControlFee;
	}

	/**
	 * Method 'isDeliveryCollectionFee'
	 * 
	 * @return boolean
	 */
	public boolean isDeliveryCollectionFee()
	{
		return deliveryCollectionFee;
	}

	/**
	 * Method 'setDeliveryCollectionFee'
	 * 
	 * @param deliveryCollectionFee
	 */
	public void setDeliveryCollectionFee(boolean deliveryCollectionFee)
	{
		this.deliveryCollectionFee = deliveryCollectionFee;
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

}
