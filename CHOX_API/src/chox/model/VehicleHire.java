package chox.model;

import java.io.Serializable;
import java.util.Date;
import scsbre.model.IHireInfo;
import scsbre.model.IVehicleClassInfo;
import chox.services.HireMonitoringDetailService;

public class VehicleHire implements Serializable, IHireInfo
{

        private HireMonitoringDetailService service;
        
        /** 
	 * This attribute maps to the column id in the vehicle_hire table.
	 */
	protected int id;
        protected boolean IsTotalLoss;

    public void setIsTotalLoss(boolean IsTotalLoss) {
        this.IsTotalLoss = IsTotalLoss;
    }
	/** 
	 * This attribute maps to the column vehicle_registration in the vehicle_hire table.
	 */
	protected String vehicleRegistration;

	/** 
	 * This attribute maps to the column vehicle_manufacturer in the vehicle_hire table.
	 */
	protected String vehicleManufacturer;

	/** 
	 * This attribute maps to the column vehicle_model in the vehicle_hire table.
	 */
	protected String vehicleModel;

	/** 
	 * This attribute maps to the column rental_start in the vehicle_hire table.
	 */
	protected Date rentalStart;

	/** 
	 * This attribute maps to the column rental_end in the vehicle_hire table.
	 */
	protected Date rentalEnd;

	/** 
	 * This attribute maps to the column collection_reason in the vehicle_hire table.
	 */
	protected String collectionReason;

	/** 
	 * This attribute maps to the column days in the vehicle_hire table.
	 */
	protected Integer days;

	/** 
	 * This attribute maps to the column cdw_fee in the vehicle_hire table.
	 */
	protected boolean cdwFee;

	/** 
	 * This attribute maps to the column automatic_fee in the vehicle_hire table.
	 */
	protected boolean automaticFee;

	/** 
	 * This attribute maps to the column sat_nav_fee in the vehicle_hire table.
	 */
	protected boolean satNavFee;

	/** 
	 * This attribute maps to the column estate_fee in the vehicle_hire table.
	 */
	protected boolean estateFee;

	/** 
	 * This attribute maps to the column baby_seat_fee in the vehicle_hire table.
	 */
	protected boolean babySeatFee;

	/** 
	 * This attribute maps to the column tow_bars_fee in the vehicle_hire table.
	 */
	protected boolean towBarsFee;

	/** 
	 * This attribute maps to the column non_standard_insurance_premium_fee in the vehicle_hire table.
	 */
	protected boolean nonStandardInsurancePremiumFee;

	/** 
	 * This attribute maps to the column admin_fee in the vehicle_hire table.
	 */
	protected boolean adminFee;

	/** 
	 * This attribute maps to the column roof_rack_fee in the vehicle_hire table.
	 */
	protected boolean roofRackFee;

	/** 
	 * This attribute maps to the column dual_control_fee in the vehicle_hire table.
	 */
	protected boolean dualControlFee;

	/** 
	 * This attribute maps to the column delivery_collection_fee in the vehicle_hire table.
	 */
	protected boolean deliveryCollectionFee;

	/** 
	 * This attribute maps to the column created_by in the vehicle_hire table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column created_date in the vehicle_hire table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the vehicle_hire table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the vehicle_hire table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute represents the foreign key relationship to the vehicle_class table.
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
	 * @return java.math.BigDecimal
	 */
	public Integer getDays()
	{
		return days;
	}

	/**
	 * Method 'setDays'
	 * 
	 * @param days
	 */
	public void setDays(Integer days)
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

        public Date getHireStart() {
            return this.rentalStart;
        }

        public void setHireStart(Date hireStart) {
            this.rentalStart = hireStart;
        }

        public Date getHireEnd() {
            return this.rentalEnd;
        }

        public void setHireEnd(Date hireEnd) {
            this.rentalEnd = hireEnd;
        }

        public int getNumberOfHireDays() {
            return this.days;
        }

        public void setNumberOfHireDays(int numberOfHireDays) {
            this.days = numberOfHireDays;
        }

        public IVehicleClassInfo getVClass() {
            return this.vehicleClass;
        }
        
        // ##### NOT FROM HERE #############
        public boolean getIsTotalLoss() {
            return this.IsTotalLoss;
        }
}
