package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Claim implements Serializable
{
	/** 
	 * This attribute maps to the column id in the Claim table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column managingRepair in the Claim table.
	 */
	protected boolean managingRepair;

	/** 
	 * This attribute maps to the column policyHolderContactDate in the Claim table.
	 */
	protected Date policyHolderContactDate;

	/** 
	 * This attribute maps to the column choReference in the Claim table.
	 */
	protected String choReference;

	/** 
	 * This attribute maps to the column status in the Claim table.
	 */
	protected String status;

	/** 
	 * This attribute maps to the column createdBy in the Claim table.
	 */
	protected int createdBy;

	/** 
	 * This attribute maps to the column createdDate in the Claim table.
	 */
	protected Date createdDate;

	/** 
	 * This attribute maps to the column lastModifiedBy in the Claim table.
	 */
	protected int lastModifiedBy;

	/** 
	 * This attribute maps to the column lastModifiedDate in the Claim table.
	 */
	protected Date lastModifiedDate;

	/** 
	 * This attribute represents the foreign key relationship to the CHOrganisation table.
	 */
	protected Chorganisation chorganisation;

	/** 
	 * This attribute represents the foreign key relationship to the Customer table.
	 */
	protected Customer customer;

	/** 
	 * This attribute represents the foreign key relationship to the EngineerReport table.
	 */
	protected EngineerReport engineerReport;

	/** 
	 * This attribute represents the foreign key relationship to the HireMonitoringDetail table.
	 */
	protected HireMonitoringDetail hireMonitoringDetail;

	/** 
	 * This attribute represents the foreign key relationship to the Incident table.
	 */
	protected Incident incident;

	/** 
	 * This attribute represents the foreign key relationship to the Insurer table.
	 */
	protected Insurer insurer;

	/** 
	 * This attribute represents the foreign key relationship to the Invoice table.
	 */
	protected Invoice invoice;

	/** 
	 * This attribute represents the foreign key relationship to the LineOfBusiness table.
	 */
	protected LineOfBusiness lineOfBusiness;

	/** 
	 * This attribute represents the foreign key relationship to the ThirdParty table.
	 */
	protected ThirdParty thirdParty;

	/** 
	 * This attribute represents the foreign key relationship to the VehicleHire table.
	 */
	protected VehicleHire vehicleHire;

	/**
	 * Method 'Claim'
	 * 
	 */
	public Claim()
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
	 * Method 'isManagingRepair'
	 * 
	 * @return boolean
	 */
	public boolean isManagingRepair()
	{
		return managingRepair;
	}

	/**
	 * Method 'setManagingRepair'
	 * 
	 * @param managingRepair
	 */
	public void setManagingRepair(boolean managingRepair)
	{
		this.managingRepair = managingRepair;
	}

	/**
	 * Method 'getPolicyHolderContactDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getPolicyHolderContactDate()
	{
		return policyHolderContactDate;
	}

	/**
	 * Method 'setPolicyHolderContactDate'
	 * 
	 * @param policyHolderContactDate
	 */
	public void setPolicyHolderContactDate(java.util.Date policyHolderContactDate)
	{
		this.policyHolderContactDate = policyHolderContactDate;
	}

	/**
	 * Method 'getChoReference'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getChoReference()
	{
		return choReference;
	}

	/**
	 * Method 'setChoReference'
	 * 
	 * @param choReference
	 */
	public void setChoReference(java.lang.String choReference)
	{
		this.choReference = choReference;
	}

	/**
	 * Method 'getStatus'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getStatus()
	{
		return status;
	}

	/**
	 * Method 'setStatus'
	 * 
	 * @param status
	 */
	public void setStatus(java.lang.String status)
	{
		this.status = status;
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
	 * Method 'getChorganisation'
	 * 
	 * @return Chorganisation
	 */
	public Chorganisation getChorganisation()
	{
		return chorganisation;
	}

	/**
	 * Method 'setChorganisation'
	 * 
	 * @param chorganisation
	 */
	public void setChorganisation(Chorganisation chorganisation)
	{
		this.chorganisation = chorganisation;
	}

	/**
	 * Method 'getCustomer'
	 * 
	 * @return Customer
	 */
	public Customer getCustomer()
	{
		return customer;
	}

	/**
	 * Method 'setCustomer'
	 * 
	 * @param customer
	 */
	public void setCustomer(Customer customer)
	{
		this.customer = customer;
	}

	/**
	 * Method 'getEngineerReport'
	 * 
	 * @return EngineerReport
	 */
	public EngineerReport getEngineerReport()
	{
		return engineerReport;
	}

	/**
	 * Method 'setEngineerReport'
	 * 
	 * @param engineerReport
	 */
	public void setEngineerReport(EngineerReport engineerReport)
	{
		this.engineerReport = engineerReport;
	}

	/**
	 * Method 'getHireMonitoringDetail'
	 * 
	 * @return HireMonitoringDetail
	 */
	public HireMonitoringDetail getHireMonitoringDetail()
	{
		return hireMonitoringDetail;
	}

	/**
	 * Method 'setHireMonitoringDetail'
	 * 
	 * @param hireMonitoringDetail
	 */
	public void setHireMonitoringDetail(HireMonitoringDetail hireMonitoringDetail)
	{
		this.hireMonitoringDetail = hireMonitoringDetail;
	}

	/**
	 * Method 'getIncident'
	 * 
	 * @return Incident
	 */
	public Incident getIncident()
	{
		return incident;
	}

	/**
	 * Method 'setIncident'
	 * 
	 * @param incident
	 */
	public void setIncident(Incident incident)
	{
		this.incident = incident;
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

	/**
	 * Method 'getInvoice'
	 * 
	 * @return Invoice
	 */
	public Invoice getInvoice()
	{
		return invoice;
	}

	/**
	 * Method 'setInvoice'
	 * 
	 * @param invoice
	 */
	public void setInvoice(Invoice invoice)
	{
		this.invoice = invoice;
	}

	/**
	 * Method 'getLineOfBusiness'
	 * 
	 * @return LineOfBusiness
	 */
	public LineOfBusiness getLineOfBusiness()
	{
		return lineOfBusiness;
	}

	/**
	 * Method 'setLineOfBusiness'
	 * 
	 * @param lineOfBusiness
	 */
	public void setLineOfBusiness(LineOfBusiness lineOfBusiness)
	{
		this.lineOfBusiness = lineOfBusiness;
	}

	/**
	 * Method 'getThirdParty'
	 * 
	 * @return ThirdParty
	 */
	public ThirdParty getThirdParty()
	{
		return thirdParty;
	}

	/**
	 * Method 'setThirdParty'
	 * 
	 * @param thirdParty
	 */
	public void setThirdParty(ThirdParty thirdParty)
	{
		this.thirdParty = thirdParty;
	}

	/**
	 * Method 'getVehicleHire'
	 * 
	 * @return VehicleHire
	 */
	public VehicleHire getVehicleHire()
	{
		return vehicleHire;
	}

	/**
	 * Method 'setVehicleHire'
	 * 
	 * @param vehicleHire
	 */
	public void setVehicleHire(VehicleHire vehicleHire)
	{
		this.vehicleHire = vehicleHire;
	}

}
