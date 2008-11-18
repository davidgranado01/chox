package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class HireMonitoringDetail implements Serializable
{
	/** 
	 * This attribute maps to the column createdBy in the HireMonitoringDetail table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the HireMonitoringDetail table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the HireMonitoringDetail table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the HireMonitoringDetail table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute maps to the column nameOfRepairer in the HireMonitoringDetail table.
	 */
	protected String nameOfRepairer;

	/** 
	 * This attribute maps to the column repairBookInDate in the HireMonitoringDetail table.
	 */
	protected Date repairBookInDate;

	/** 
	 * This attribute maps to the column inspectionBookedDate in the HireMonitoringDetail table.
	 */
	protected Date inspectionBookedDate;

	/** 
	 * This attribute maps to the column inspectionDate in the HireMonitoringDetail table.
	 */
	protected Date inspectionDate;

	/** 
	 * This attribute maps to the column nameOfIME in the HireMonitoringDetail table.
	 */
	protected String nameOfIME;

	/** 
	 * This attribute maps to the column repairCompletionDate in the HireMonitoringDetail table.
	 */
	protected Date repairCompletionDate;

	/** 
	 * This attribute maps to the column totalLossInspectionReport in the HireMonitoringDetail table.
	 */
	protected String totalLossInspectionReport;

	/** 
	 * This attribute maps to the column isTotalLostCheck in the HireMonitoringDetail table.
	 */
	protected boolean isTotalLostCheck;

	/** 
	 * This attribute maps to the column id in the HireMonitoringDetail table.
	 */
	protected int id;

	/**
	 * Method 'HireMonitoringDetail'
	 * 
	 */
	public HireMonitoringDetail()
	{
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
	 * Method 'getNameOfRepairer'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNameOfRepairer()
	{
		return nameOfRepairer;
	}

	/**
	 * Method 'setNameOfRepairer'
	 * 
	 * @param nameOfRepairer
	 */
	public void setNameOfRepairer(java.lang.String nameOfRepairer)
	{
		this.nameOfRepairer = nameOfRepairer;
	}

	/**
	 * Method 'getRepairBookInDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getRepairBookInDate()
	{
		return repairBookInDate;
	}

	/**
	 * Method 'setRepairBookInDate'
	 * 
	 * @param repairBookInDate
	 */
	public void setRepairBookInDate(java.util.Date repairBookInDate)
	{
		this.repairBookInDate = repairBookInDate;
	}

	/**
	 * Method 'getInspectionBookedDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getInspectionBookedDate()
	{
		return inspectionBookedDate;
	}

	/**
	 * Method 'setInspectionBookedDate'
	 * 
	 * @param inspectionBookedDate
	 */
	public void setInspectionBookedDate(java.util.Date inspectionBookedDate)
	{
		this.inspectionBookedDate = inspectionBookedDate;
	}

	/**
	 * Method 'getInspectionDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getInspectionDate()
	{
		return inspectionDate;
	}

	/**
	 * Method 'setInspectionDate'
	 * 
	 * @param inspectionDate
	 */
	public void setInspectionDate(java.util.Date inspectionDate)
	{
		this.inspectionDate = inspectionDate;
	}

	/**
	 * Method 'getNameOfIME'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNameOfIME()
	{
		return nameOfIME;
	}

	/**
	 * Method 'setNameOfIME'
	 * 
	 * @param nameOfIME
	 */
	public void setNameOfIME(java.lang.String nameOfIME)
	{
		this.nameOfIME = nameOfIME;
	}

	/**
	 * Method 'getRepairCompletionDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getRepairCompletionDate()
	{
		return repairCompletionDate;
	}

	/**
	 * Method 'setRepairCompletionDate'
	 * 
	 * @param repairCompletionDate
	 */
	public void setRepairCompletionDate(java.util.Date repairCompletionDate)
	{
		this.repairCompletionDate = repairCompletionDate;
	}

	/**
	 * Method 'getTotalLossInspectionReport'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTotalLossInspectionReport()
	{
		return totalLossInspectionReport;
	}

	/**
	 * Method 'setTotalLossInspectionReport'
	 * 
	 * @param totalLossInspectionReport
	 */
	public void setTotalLossInspectionReport(java.lang.String totalLossInspectionReport)
	{
		this.totalLossInspectionReport = totalLossInspectionReport;
	}

	/**
	 * Method 'isIsTotalLostCheck'
	 * 
	 * @return boolean
	 */
	public boolean isIsTotalLostCheck()
	{
		return isTotalLostCheck;
	}

	/**
	 * Method 'setIsTotalLostCheck'
	 * 
	 * @param isTotalLostCheck
	 */
	public void setIsTotalLostCheck(boolean isTotalLostCheck)
	{
		this.isTotalLostCheck = isTotalLostCheck;
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

}
