package chox.model.hibernate;

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
	protected long days;

	/** 
	 * This attribute represents whether the primitive attribute days is null.
	 */
	protected boolean daysNull = true;

	/** 
	 * This attribute maps to the column cdwFee in the VehicleHire table.
	 */
	protected short cdwFee;

	/** 
	 * This attribute represents whether the primitive attribute cdwFee is null.
	 */
	protected boolean cdwFeeNull = true;

	/** 
	 * This attribute maps to the column automaticFee in the VehicleHire table.
	 */
	protected short automaticFee;

	/** 
	 * This attribute represents whether the primitive attribute automaticFee is null.
	 */
	protected boolean automaticFeeNull = true;

	/** 
	 * This attribute maps to the column satNavFee in the VehicleHire table.
	 */
	protected short satNavFee;

	/** 
	 * This attribute represents whether the primitive attribute satNavFee is null.
	 */
	protected boolean satNavFeeNull = true;

	/** 
	 * This attribute maps to the column estateFee in the VehicleHire table.
	 */
	protected short estateFee;

	/** 
	 * This attribute represents whether the primitive attribute estateFee is null.
	 */
	protected boolean estateFeeNull = true;

	/** 
	 * This attribute maps to the column babySeatFee in the VehicleHire table.
	 */
	protected short babySeatFee;

	/** 
	 * This attribute represents whether the primitive attribute babySeatFee is null.
	 */
	protected boolean babySeatFeeNull = true;

	/** 
	 * This attribute maps to the column towBarsFee in the VehicleHire table.
	 */
	protected short towBarsFee;

	/** 
	 * This attribute represents whether the primitive attribute towBarsFee is null.
	 */
	protected boolean towBarsFeeNull = true;

	/** 
	 * This attribute maps to the column nonStandardInsurancePremiumFee in the VehicleHire table.
	 */
	protected short nonStandardInsurancePremiumFee;

	/** 
	 * This attribute represents whether the primitive attribute nonStandardInsurancePremiumFee is null.
	 */
	protected boolean nonStandardInsurancePremiumFeeNull = true;

	/** 
	 * This attribute maps to the column adminFee in the VehicleHire table.
	 */
	protected short adminFee;

	/** 
	 * This attribute represents whether the primitive attribute adminFee is null.
	 */
	protected boolean adminFeeNull = true;

	/** 
	 * This attribute maps to the column roofRackFee in the VehicleHire table.
	 */
	protected short roofRackFee;

	/** 
	 * This attribute represents whether the primitive attribute roofRackFee is null.
	 */
	protected boolean roofRackFeeNull = true;

	/** 
	 * This attribute maps to the column dualControlFee in the VehicleHire table.
	 */
	protected short dualControlFee;

	/** 
	 * This attribute represents whether the primitive attribute dualControlFee is null.
	 */
	protected boolean dualControlFeeNull = true;

	/** 
	 * This attribute maps to the column deliveryCollectionFee in the VehicleHire table.
	 */
	protected short deliveryCollectionFee;

	/** 
	 * This attribute represents whether the primitive attribute deliveryCollectionFee is null.
	 */
	protected boolean deliveryCollectionFeeNull = true;

	/** 
	 * This attribute maps to the column createdBy in the VehicleHire table.
	 */
	protected int createdBy;

	/** 
	 * This attribute represents whether the primitive attribute createdBy is null.
	 */
	protected boolean createdByNull = true;

	/** 
	 * This attribute maps to the column createdDate in the VehicleHire table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the VehicleHire table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute represents whether the primitive attribute lastModifiedBy is null.
	 */
	protected boolean lastModifiedByNull = true;

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
	 * @return long
	 */
	public long getDays()
	{
		return days;
	}

	/**
	 * Method 'setDays'
	 * 
	 * @param days
	 */
	public void setDays(long days)
	{
		this.days = days;
		this.daysNull = false;
	}

	/** 
	 * Sets the value of daysNull
	 */
	public void setDaysNull(boolean daysNull)
	{
		this.daysNull = daysNull;
	}

	/** 
	 * Gets the value of daysNull
	 */
	public boolean isDaysNull()
	{
		return daysNull;
	}

	/**
	 * Method 'getCdwFee'
	 * 
	 * @return short
	 */
	public short getCdwFee()
	{
		return cdwFee;
	}

	/**
	 * Method 'setCdwFee'
	 * 
	 * @param cdwFee
	 */
	public void setCdwFee(short cdwFee)
	{
		this.cdwFee = cdwFee;
		this.cdwFeeNull = false;
	}

	/** 
	 * Sets the value of cdwFeeNull
	 */
	public void setCdwFeeNull(boolean cdwFeeNull)
	{
		this.cdwFeeNull = cdwFeeNull;
	}

	/** 
	 * Gets the value of cdwFeeNull
	 */
	public boolean isCdwFeeNull()
	{
		return cdwFeeNull;
	}

	/**
	 * Method 'getAutomaticFee'
	 * 
	 * @return short
	 */
	public short getAutomaticFee()
	{
		return automaticFee;
	}

	/**
	 * Method 'setAutomaticFee'
	 * 
	 * @param automaticFee
	 */
	public void setAutomaticFee(short automaticFee)
	{
		this.automaticFee = automaticFee;
		this.automaticFeeNull = false;
	}

	/** 
	 * Sets the value of automaticFeeNull
	 */
	public void setAutomaticFeeNull(boolean automaticFeeNull)
	{
		this.automaticFeeNull = automaticFeeNull;
	}

	/** 
	 * Gets the value of automaticFeeNull
	 */
	public boolean isAutomaticFeeNull()
	{
		return automaticFeeNull;
	}

	/**
	 * Method 'getSatNavFee'
	 * 
	 * @return short
	 */
	public short getSatNavFee()
	{
		return satNavFee;
	}

	/**
	 * Method 'setSatNavFee'
	 * 
	 * @param satNavFee
	 */
	public void setSatNavFee(short satNavFee)
	{
		this.satNavFee = satNavFee;
		this.satNavFeeNull = false;
	}

	/** 
	 * Sets the value of satNavFeeNull
	 */
	public void setSatNavFeeNull(boolean satNavFeeNull)
	{
		this.satNavFeeNull = satNavFeeNull;
	}

	/** 
	 * Gets the value of satNavFeeNull
	 */
	public boolean isSatNavFeeNull()
	{
		return satNavFeeNull;
	}

	/**
	 * Method 'getEstateFee'
	 * 
	 * @return short
	 */
	public short getEstateFee()
	{
		return estateFee;
	}

	/**
	 * Method 'setEstateFee'
	 * 
	 * @param estateFee
	 */
	public void setEstateFee(short estateFee)
	{
		this.estateFee = estateFee;
		this.estateFeeNull = false;
	}

	/** 
	 * Sets the value of estateFeeNull
	 */
	public void setEstateFeeNull(boolean estateFeeNull)
	{
		this.estateFeeNull = estateFeeNull;
	}

	/** 
	 * Gets the value of estateFeeNull
	 */
	public boolean isEstateFeeNull()
	{
		return estateFeeNull;
	}

	/**
	 * Method 'getBabySeatFee'
	 * 
	 * @return short
	 */
	public short getBabySeatFee()
	{
		return babySeatFee;
	}

	/**
	 * Method 'setBabySeatFee'
	 * 
	 * @param babySeatFee
	 */
	public void setBabySeatFee(short babySeatFee)
	{
		this.babySeatFee = babySeatFee;
		this.babySeatFeeNull = false;
	}

	/** 
	 * Sets the value of babySeatFeeNull
	 */
	public void setBabySeatFeeNull(boolean babySeatFeeNull)
	{
		this.babySeatFeeNull = babySeatFeeNull;
	}

	/** 
	 * Gets the value of babySeatFeeNull
	 */
	public boolean isBabySeatFeeNull()
	{
		return babySeatFeeNull;
	}

	/**
	 * Method 'getTowBarsFee'
	 * 
	 * @return short
	 */
	public short getTowBarsFee()
	{
		return towBarsFee;
	}

	/**
	 * Method 'setTowBarsFee'
	 * 
	 * @param towBarsFee
	 */
	public void setTowBarsFee(short towBarsFee)
	{
		this.towBarsFee = towBarsFee;
		this.towBarsFeeNull = false;
	}

	/** 
	 * Sets the value of towBarsFeeNull
	 */
	public void setTowBarsFeeNull(boolean towBarsFeeNull)
	{
		this.towBarsFeeNull = towBarsFeeNull;
	}

	/** 
	 * Gets the value of towBarsFeeNull
	 */
	public boolean isTowBarsFeeNull()
	{
		return towBarsFeeNull;
	}

	/**
	 * Method 'getNonStandardInsurancePremiumFee'
	 * 
	 * @return short
	 */
	public short getNonStandardInsurancePremiumFee()
	{
		return nonStandardInsurancePremiumFee;
	}

	/**
	 * Method 'setNonStandardInsurancePremiumFee'
	 * 
	 * @param nonStandardInsurancePremiumFee
	 */
	public void setNonStandardInsurancePremiumFee(short nonStandardInsurancePremiumFee)
	{
		this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
		this.nonStandardInsurancePremiumFeeNull = false;
	}

	/** 
	 * Sets the value of nonStandardInsurancePremiumFeeNull
	 */
	public void setNonStandardInsurancePremiumFeeNull(boolean nonStandardInsurancePremiumFeeNull)
	{
		this.nonStandardInsurancePremiumFeeNull = nonStandardInsurancePremiumFeeNull;
	}

	/** 
	 * Gets the value of nonStandardInsurancePremiumFeeNull
	 */
	public boolean isNonStandardInsurancePremiumFeeNull()
	{
		return nonStandardInsurancePremiumFeeNull;
	}

	/**
	 * Method 'getAdminFee'
	 * 
	 * @return short
	 */
	public short getAdminFee()
	{
		return adminFee;
	}

	/**
	 * Method 'setAdminFee'
	 * 
	 * @param adminFee
	 */
	public void setAdminFee(short adminFee)
	{
		this.adminFee = adminFee;
		this.adminFeeNull = false;
	}

	/** 
	 * Sets the value of adminFeeNull
	 */
	public void setAdminFeeNull(boolean adminFeeNull)
	{
		this.adminFeeNull = adminFeeNull;
	}

	/** 
	 * Gets the value of adminFeeNull
	 */
	public boolean isAdminFeeNull()
	{
		return adminFeeNull;
	}

	/**
	 * Method 'getRoofRackFee'
	 * 
	 * @return short
	 */
	public short getRoofRackFee()
	{
		return roofRackFee;
	}

	/**
	 * Method 'setRoofRackFee'
	 * 
	 * @param roofRackFee
	 */
	public void setRoofRackFee(short roofRackFee)
	{
		this.roofRackFee = roofRackFee;
		this.roofRackFeeNull = false;
	}

	/** 
	 * Sets the value of roofRackFeeNull
	 */
	public void setRoofRackFeeNull(boolean roofRackFeeNull)
	{
		this.roofRackFeeNull = roofRackFeeNull;
	}

	/** 
	 * Gets the value of roofRackFeeNull
	 */
	public boolean isRoofRackFeeNull()
	{
		return roofRackFeeNull;
	}

	/**
	 * Method 'getDualControlFee'
	 * 
	 * @return short
	 */
	public short getDualControlFee()
	{
		return dualControlFee;
	}

	/**
	 * Method 'setDualControlFee'
	 * 
	 * @param dualControlFee
	 */
	public void setDualControlFee(short dualControlFee)
	{
		this.dualControlFee = dualControlFee;
		this.dualControlFeeNull = false;
	}

	/** 
	 * Sets the value of dualControlFeeNull
	 */
	public void setDualControlFeeNull(boolean dualControlFeeNull)
	{
		this.dualControlFeeNull = dualControlFeeNull;
	}

	/** 
	 * Gets the value of dualControlFeeNull
	 */
	public boolean isDualControlFeeNull()
	{
		return dualControlFeeNull;
	}

	/**
	 * Method 'getDeliveryCollectionFee'
	 * 
	 * @return short
	 */
	public short getDeliveryCollectionFee()
	{
		return deliveryCollectionFee;
	}

	/**
	 * Method 'setDeliveryCollectionFee'
	 * 
	 * @param deliveryCollectionFee
	 */
	public void setDeliveryCollectionFee(short deliveryCollectionFee)
	{
		this.deliveryCollectionFee = deliveryCollectionFee;
		this.deliveryCollectionFeeNull = false;
	}

	/** 
	 * Sets the value of deliveryCollectionFeeNull
	 */
	public void setDeliveryCollectionFeeNull(boolean deliveryCollectionFeeNull)
	{
		this.deliveryCollectionFeeNull = deliveryCollectionFeeNull;
	}

	/** 
	 * Gets the value of deliveryCollectionFeeNull
	 */
	public boolean isDeliveryCollectionFeeNull()
	{
		return deliveryCollectionFeeNull;
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
