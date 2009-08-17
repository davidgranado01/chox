package chox.model;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import scsbre.model.ICHOBandInfo;

public class ChoBand implements Auditable, Serializable, ICHOBandInfo
{

	protected Insurer insurer;
	protected WebUser createdBy;
	protected Date createdDate;
	protected WebUser lastModifiedBy;
	protected Date lastModifiedDate;
	protected boolean isActive;
	protected int id;
	protected int takeVehicleToGarageDaysMobile;
	protected int takeVehicleToGarageDaysNonMobile;
	protected int weekendBufferDays;
	protected int takeVehicleOutDays;
	protected int engineerInspectionDelayDays;
	protected int isMobileDayAllowance;
	protected int offerMadeDays;
	protected int receiptOfFinalStatementChequeDays;
	protected int inspectionDelayDays;
	protected BigDecimal hireRateChargeTolerance;
	protected BigDecimal hireNetCeiling;
	protected int hireDayCeiling;
	protected BigDecimal maxRepairValue;
        protected int isNotMobileDayAllowance;
        protected int averageLabourRate;
        protected int averageLabourHoursPerHireDay;
        protected String name;

	public ChoBand()
	{
	}

        public Insurer getInsurer() {
            return insurer;
        }

        public void setInsurer(Insurer insurer) {
            this.insurer = insurer;
        }

	public WebUser getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(WebUser createdBy)
	{
		this.createdBy = createdBy;
	}

	public java.util.Date getCreatedDate()
	{
		return createdDate;
	}

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

	public int getTakeVehicleToGarageDaysMobile()
	{
		return takeVehicleToGarageDaysMobile;
	}

	public void setTakeVehicleToGarageDaysMobile(int takeVehicleToGarageDaysMobile)
	{
		this.takeVehicleToGarageDaysMobile = takeVehicleToGarageDaysMobile;
	}

	public int getTakeVehicleToGarageDaysNonMobile()
	{
		return takeVehicleToGarageDaysNonMobile;
	}

	public void setTakeVehicleToGarageDaysNonMobile(int takeVehicleToGarageDaysNonMobile)
	{
		this.takeVehicleToGarageDaysNonMobile = takeVehicleToGarageDaysNonMobile;
	}

	public int getWeekendBufferDays()
	{
		return weekendBufferDays;
	}

	public void setWeekendBufferDays(int weekendBufferDays)
	{
		this.weekendBufferDays = weekendBufferDays;
	}

	public int getTakeVehicleOutDays()
	{
		return takeVehicleOutDays;
	}

	public void setTakeVehicleOutDays(int takeVehicleOutDays)
	{
		this.takeVehicleOutDays = takeVehicleOutDays;
	}

	public int getEngineerInspectionDelayDays()
	{
		return engineerInspectionDelayDays;
	}

	public void setEngineerInspectionDelayDays(int engineerInspectionDelayDays)
	{
		this.engineerInspectionDelayDays = engineerInspectionDelayDays;
	}

	public int getIsMobileDayAllowance()
	{
		return isMobileDayAllowance;
	}

	public void setIsMobileDayAllowance(int isMobileDayAllowance)
	{
		this.isMobileDayAllowance = isMobileDayAllowance;
	}

	public int getOfferMadeDays()
	{
		return offerMadeDays;
	}

	public void setOfferMadeDays(int offerMadeDays)
	{
		this.offerMadeDays = offerMadeDays;
	}

	public int getReceiptOfFinalStatementChequeDays()
	{
		return receiptOfFinalStatementChequeDays;
	}

	public void setReceiptOfFinalStatementChequeDays(int receiptOfFinalStatementChequeDays)
	{
		this.receiptOfFinalStatementChequeDays = receiptOfFinalStatementChequeDays;
	}

	public int getInspectionDelayDays()
	{
		return inspectionDelayDays;
	}

	public void setInspectionDelayDays(int inspectionDelayDays)
	{
		this.inspectionDelayDays = inspectionDelayDays;
	}

	public java.math.BigDecimal getHireRateChargeTolerance()
	{
            if(hireRateChargeTolerance==null){
                hireRateChargeTolerance = new BigDecimal(0.00);
            }
            return hireRateChargeTolerance;
	}

	public void setHireRateChargeTolerance(java.math.BigDecimal hireRateChargeTolerance)
	{
		this.hireRateChargeTolerance = hireRateChargeTolerance;
	}

	public java.math.BigDecimal getHireNetCeiling()
	{
            if(hireNetCeiling==null){
                hireNetCeiling = new BigDecimal(0.00);
            }

            return hireNetCeiling;
	}

	public void setHireNetCeiling(java.math.BigDecimal hireNetCeiling)
	{
		this.hireNetCeiling = hireNetCeiling;
	}

	public int getHireDayCeiling()
	{
		return hireDayCeiling;
	}

	public void setHireDayCeiling(int hireDayCeiling)
	{
		this.hireDayCeiling = hireDayCeiling;
	}

	public java.math.BigDecimal getMaxRepairValue()
	{
            if(maxRepairValue==null){
                maxRepairValue = new BigDecimal(0.00);
            }
            return maxRepairValue;
	}

	public void setMaxRepairValue(java.math.BigDecimal maxRepairValue)
	{
		this.maxRepairValue = maxRepairValue;
	}

	public int getIsNotMobileDayAllowance()
	{
		return isNotMobileDayAllowance;
	}

	public void setIsNotMobileDayAllowance(int isNotMobileDayAllowance)
	{
		this.isNotMobileDayAllowance = isNotMobileDayAllowance;
	}

        public int getAverageLabourHoursPerHireDay() {
            return averageLabourHoursPerHireDay;
        }

        public void setAverageLabourHoursPerHireDay(int averageLabourHoursPerHireDay) {
            this.averageLabourHoursPerHireDay = averageLabourHoursPerHireDay;
        }

        public int getAverageLabourRate() {
            return averageLabourRate;
        }

        public void setAverageLabourRate(int averageLabourRate) {
            this.averageLabourRate = averageLabourRate;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
        


}
