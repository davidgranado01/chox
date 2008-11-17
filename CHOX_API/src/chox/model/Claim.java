package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Claim implements Serializable
{

        public static final String NEW_CLAIM = "1st Notification";
        
	protected int id;
	protected boolean managingRepair;
	protected Date policyHolderContactDate;

	protected String choReference;
	protected String status;
	protected int incidentId;
	protected int createdBy;
	protected boolean createdByNull = true;
	protected Date createdDate;
	protected int lastModifiedBy;
	protected boolean lastModifiedByNull = true;
	protected Date lastModifiedDate;
	protected Chorganisation chorganisation;
	protected Customer customer;
	protected EngineerReport engineerReport;
	protected HireMonitoringDetail hireMonitoringDetail;
	protected Incident incident;
	protected Insurer insurer;
	protected Invoice invoice;
	protected LineOfBusiness lineOfBusiness;
	protected ThirdParty thirdParty;
	protected VehicleHire vehicleHire;


	public Claim()
	{
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
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
	 * Method 'getIncidentId'
	 * 
	 * @return int
	 */
	public int getIncidentId()
	{
		return incidentId;
	}

	/**
	 * Method 'setIncidentId'
	 * 
	 * @param incidentId
	 */
	public void setIncidentId(int incidentId)
	{
		this.incidentId = incidentId;
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

        public boolean isManagingRepair() {
            return managingRepair;
        }

        public void setManagingRepair(boolean managingRepair) {
            this.managingRepair = managingRepair;
        }
}
