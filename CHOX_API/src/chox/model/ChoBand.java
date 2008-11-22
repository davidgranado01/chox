package chox.model;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import scsbre.model.ICHOBandInfo;

public class ChoBand implements Serializable, ICHOBandInfo
{
	/** 
	 * This attribute maps to the column insurer_id in the cho_band table.
	 */
	protected int insurerId;

	/** 
	 * This attribute maps to the column created_by in the cho_band table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column created_date in the cho_band table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column last_modified_by in the cho_band table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column last_modified_date in the cho_band table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column is_active in the cho_band table.
	 */
	protected boolean isActive;

	/** 
	 * This attribute maps to the column id in the cho_band table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column take_vehicle_to_garage_days_mobile in the cho_band table.
	 */
	protected int takeVehicleToGarageDaysMobile;

	/** 
	 * This attribute maps to the column take_vehicle_to_garage_days_non_mobile in the cho_band table.
	 */
	protected int takeVehicleToGarageDaysNonMobile;

	/** 
	 * This attribute maps to the column weekend_buffer_days in the cho_band table.
	 */
	protected int weekendBufferDays;

	/** 
	 * This attribute maps to the column take_vehicle_out_days in the cho_band table.
	 */
	protected int takeVehicleOutDays;

	/** 
	 * This attribute maps to the column engineer_inspection_delay_days in the cho_band table.
	 */
	protected int engineerInspectionDelayDays;

	/** 
	 * This attribute maps to the column is_mobile_day_allowance in the cho_band table.
	 */
	protected int isMobileDayAllowance;

	/** 
	 * This attribute maps to the column offer_made_days in the cho_band table.
	 */
	protected int offerMadeDays;

	/** 
	 * This attribute maps to the column receipt_of_final_statement_cheque_days in the cho_band table.
	 */
	protected int receiptOfFinalStatementChequeDays;

	/** 
	 * This attribute maps to the column inspection_delay_days in the cho_band table.
	 */
	protected int inspectionDelayDays;

	/** 
	 * This attribute maps to the column hire_rate_charge_tolerance in the cho_band table.
	 */
	protected BigDecimal hireRateChargeTolerance;

	/** 
	 * This attribute maps to the column hire_net_ceiling in the cho_band table.
	 */
	protected BigDecimal hireNetCeiling;

	/** 
	 * This attribute maps to the column hire_day_ceiling in the cho_band table.
	 */
	protected int hireDayCeiling;

	/** 
	 * This attribute maps to the column max_repair_value in the cho_band table.
	 */
	protected BigDecimal maxRepairValue;

	/** 
	 * This attribute maps to the column is_not_mobile_day_allowance in the cho_band table.
	 */
	protected int isNotMobileDayAllowance;
	/**
	 * Method 'ChoBand'
	 * 
	 */
	public ChoBand()
	{
	}

	/**
	 * Method 'getInsurerId'
	 * 
	 * @return int
	 */
	public int getInsurerId()
	{
		return insurerId;
	}

	/**
	 * Method 'setInsurerId'
	 * 
	 * @param insurerId
	 */
	public void setInsurerId(int insurerId)
	{
		this.insurerId = insurerId;
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
	 * Method 'isIsActive'
	 * 
	 * @return boolean
	 */
	public boolean isIsActive()
	{
		return isActive;
	}

	/**
	 * Method 'setIsActive'
	 * 
	 * @param isActive
	 */
	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
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
	 * Method 'getTakeVehicleToGarageDaysMobile'
	 * 
	 * @return int
	 */
	public int getTakeVehicleToGarageDaysMobile()
	{
		return takeVehicleToGarageDaysMobile;
	}

	/**
	 * Method 'setTakeVehicleToGarageDaysMobile'
	 * 
	 * @param takeVehicleToGarageDaysMobile
	 */
	public void setTakeVehicleToGarageDaysMobile(int takeVehicleToGarageDaysMobile)
	{
		this.takeVehicleToGarageDaysMobile = takeVehicleToGarageDaysMobile;
	}

	/**
	 * Method 'getTakeVehicleToGarageDaysNonMobile'
	 * 
	 * @return int
	 */
	public int getTakeVehicleToGarageDaysNonMobile()
	{
		return takeVehicleToGarageDaysNonMobile;
	}

	/**
	 * Method 'setTakeVehicleToGarageDaysNonMobile'
	 * 
	 * @param takeVehicleToGarageDaysNonMobile
	 */
	public void setTakeVehicleToGarageDaysNonMobile(int takeVehicleToGarageDaysNonMobile)
	{
		this.takeVehicleToGarageDaysNonMobile = takeVehicleToGarageDaysNonMobile;
	}

	/**
	 * Method 'getWeekendBufferDays'
	 * 
	 * @return int
	 */
	public int getWeekendBufferDays()
	{
		return weekendBufferDays;
	}

	/**
	 * Method 'setWeekendBufferDays'
	 * 
	 * @param weekendBufferDays
	 */
	public void setWeekendBufferDays(int weekendBufferDays)
	{
		this.weekendBufferDays = weekendBufferDays;
	}

	/**
	 * Method 'getTakeVehicleOutDays'
	 * 
	 * @return int
	 */
	public int getTakeVehicleOutDays()
	{
		return takeVehicleOutDays;
	}

	/**
	 * Method 'setTakeVehicleOutDays'
	 * 
	 * @param takeVehicleOutDays
	 */
	public void setTakeVehicleOutDays(int takeVehicleOutDays)
	{
		this.takeVehicleOutDays = takeVehicleOutDays;
	}

	/**
	 * Method 'getEngineerInspectionDelayDays'
	 * 
	 * @return int
	 */
	public int getEngineerInspectionDelayDays()
	{
		return engineerInspectionDelayDays;
	}

	/**
	 * Method 'setEngineerInspectionDelayDays'
	 * 
	 * @param engineerInspectionDelayDays
	 */
	public void setEngineerInspectionDelayDays(int engineerInspectionDelayDays)
	{
		this.engineerInspectionDelayDays = engineerInspectionDelayDays;
	}

	/**
	 * Method 'getIsMobileDayAllowance'
	 * 
	 * @return int
	 */
	public int getIsMobileDayAllowance()
	{
		return isMobileDayAllowance;
	}

	/**
	 * Method 'setIsMobileDayAllowance'
	 * 
	 * @param isMobileDayAllowance
	 */
	public void setIsMobileDayAllowance(int isMobileDayAllowance)
	{
		this.isMobileDayAllowance = isMobileDayAllowance;
	}

	/**
	 * Method 'getOfferMadeDays'
	 * 
	 * @return int
	 */
	public int getOfferMadeDays()
	{
		return offerMadeDays;
	}

	/**
	 * Method 'setOfferMadeDays'
	 * 
	 * @param offerMadeDays
	 */
	public void setOfferMadeDays(int offerMadeDays)
	{
		this.offerMadeDays = offerMadeDays;
	}

	/**
	 * Method 'getReceiptOfFinalStatementChequeDays'
	 * 
	 * @return int
	 */
	public int getReceiptOfFinalStatementChequeDays()
	{
		return receiptOfFinalStatementChequeDays;
	}

	/**
	 * Method 'setReceiptOfFinalStatementChequeDays'
	 * 
	 * @param receiptOfFinalStatementChequeDays
	 */
	public void setReceiptOfFinalStatementChequeDays(int receiptOfFinalStatementChequeDays)
	{
		this.receiptOfFinalStatementChequeDays = receiptOfFinalStatementChequeDays;
	}

	/**
	 * Method 'getInspectionDelayDays'
	 * 
	 * @return int
	 */
	public int getInspectionDelayDays()
	{
		return inspectionDelayDays;
	}

	/**
	 * Method 'setInspectionDelayDays'
	 * 
	 * @param inspectionDelayDays
	 */
	public void setInspectionDelayDays(int inspectionDelayDays)
	{
		this.inspectionDelayDays = inspectionDelayDays;
	}

	/**
	 * Method 'getHireRateChargeTolerance'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getHireRateChargeTolerance()
	{
		return hireRateChargeTolerance;
	}

	/**
	 * Method 'setHireRateChargeTolerance'
	 * 
	 * @param hireRateChargeTolerance
	 */
	public void setHireRateChargeTolerance(java.math.BigDecimal hireRateChargeTolerance)
	{
		this.hireRateChargeTolerance = hireRateChargeTolerance;
	}

	/**
	 * Method 'getHireNetCeiling'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getHireNetCeiling()
	{
		return hireNetCeiling;
	}

	/**
	 * Method 'setHireNetCeiling'
	 * 
	 * @param hireNetCeiling
	 */
	public void setHireNetCeiling(java.math.BigDecimal hireNetCeiling)
	{
		this.hireNetCeiling = hireNetCeiling;
	}

	/**
	 * Method 'getHireDayCeiling'
	 * 
	 * @return int
	 */
	public int getHireDayCeiling()
	{
		return hireDayCeiling;
	}

	/**
	 * Method 'setHireDayCeiling'
	 * 
	 * @param hireDayCeiling
	 */
	public void setHireDayCeiling(int hireDayCeiling)
	{
		this.hireDayCeiling = hireDayCeiling;
	}

	/**
	 * Method 'getMaxRepairValue'
	 * 
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getMaxRepairValue()
	{
		return maxRepairValue;
	}

	/**
	 * Method 'setMaxRepairValue'
	 * 
	 * @param maxRepairValue
	 */
	public void setMaxRepairValue(java.math.BigDecimal maxRepairValue)
	{
		this.maxRepairValue = maxRepairValue;
	}

	/**
	 * Method 'getIsNotMobileDayAllowance'
	 * 
	 * @return int
	 */
	public int getIsNotMobileDayAllowance()
	{
		return isNotMobileDayAllowance;
	}

	/**
	 * Method 'setIsNotMobileDayAllowance'
	 * 
	 * @param isNotMobileDayAllowance
	 */
	public void setIsNotMobileDayAllowance(int isNotMobileDayAllowance)
	{
		this.isNotMobileDayAllowance = isNotMobileDayAllowance;
	}

}
