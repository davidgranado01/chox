package chox.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Claim implements Serializable
{
	/** 
	 * This attribute maps to the column id in the claim table.
	 */
	protected int id;

	/** 
	 * This attribute maps to the column uuid in the claim table.
	 */
	protected String uuid = UUID.randomUUID().toString();

	/** 
	 * This attribute maps to the column rental_id in the claim table.
	 */
	protected int rentalId;

	/** 
	 * This attribute maps to the column insurer_country_id in the claim table.
	 */
	protected int insurerCountryId;

	/** 
	 * This attribute represents whether the primitive attribute insurerCountryId is null.
	 */
	protected boolean insurerCountryIdNull = true;

	/** 
	 * This attribute maps to the column policy_number in the claim table.
	 */
	protected String policyNumber;

	/** 
	 * This attribute maps to the column claim_reference in the claim table.
	 */
	protected String claimReference;

	/** 
	 * This attribute maps to the column comprehensive in the claim table.
	 */
	protected String comprehensive;

	/** 
	 * This attribute maps to the column policy_holder_name in the claim table.
	 */
	protected String policyHolderName;

	/** 
	 * This attribute maps to the column vehicle_registration in the claim table.
	 */
	protected String vehicleRegistration;

	/** 
	 * This attribute maps to the column vehicle_manufacturer in the claim table.
	 */
	protected String vehicleManufacturer;

	/** 
	 * This attribute maps to the column vehicle_model in the claim table.
	 */
	protected String vehicleModel;

	/** 
	 * This attribute maps to the column vehicle_class_id in the claim table.
	 */
	protected int vehicleClassId;

	/** 
	 * This attribute maps to the column usable in the claim table.
	 */
	protected String usable;

	/** 
	 * This attribute maps to the column damage_description in the claim table.
	 */
	protected String damageDescription;

	/** 
	 * This attribute maps to the column tp_insurer_country_id in the claim table.
	 */
	protected int tpInsurerCountryId;

	/** 
	 * This attribute maps to the column tp_policy_number in the claim table.
	 */
	protected String tpPolicyNumber;

	/** 
	 * This attribute maps to the column tp_claim_reference in the claim table.
	 */
	protected String tpClaimReference;

	/** 
	 * This attribute maps to the column tp_vehicle_registration in the claim table.
	 */
	protected String tpVehicleRegistration;

	/** 
	 * This attribute maps to the column tp_vehicle_manufacturer in the claim table.
	 */
	protected String tpVehicleManufacturer;

	/** 
	 * This attribute maps to the column tp_vehicle_model in the claim table.
	 */
	protected String tpVehicleModel;

	/** 
	 * This attribute maps to the column tp_vehicle_class_id in the claim table.
	 */
	protected int tpVehicleClassId;

	/** 
	 * This attribute represents whether the primitive attribute tpVehicleClassId is null.
	 */
	protected boolean tpVehicleClassIdNull = true;

	/** 
	 * This attribute maps to the column tp_name in the claim table.
	 */
	protected String tpName;

	/** 
	 * This attribute maps to the column tp_address1 in the claim table.
	 */
	protected String tpAddress1;

	/** 
	 * This attribute maps to the column tp_address2 in the claim table.
	 */
	protected String tpAddress2;

	/** 
	 * This attribute maps to the column tp_address3 in the claim table.
	 */
	protected String tpAddress3;

	/** 
	 * This attribute maps to the column tp_address4 in the claim table.
	 */
	protected String tpAddress4;

	/** 
	 * This attribute maps to the column tp_address5 in the claim table.
	 */
	protected String tpAddress5;

	/** 
	 * This attribute maps to the column tp_postcode in the claim table.
	 */
	protected String tpPostcode;

	/** 
	 * This attribute maps to the column tp_telephone_day in the claim table.
	 */
	protected String tpTelephoneDay;

	/** 
	 * This attribute maps to the column tp_telephone_evening in the claim table.
	 */
	protected String tpTelephoneEvening;

	/** 
	 * This attribute maps to the column tp_email in the claim table.
	 */
	protected String tpEmail;

	/** 
	 * This attribute maps to the column incident_date in the claim table.
	 */
	protected Date incidentDate;

	/** 
	 * This attribute maps to the column location in the claim table.
	 */
	protected String location;

	/** 
	 * This attribute maps to the column police_involved in the claim table.
	 */
	protected String policeInvolved;

	/** 
	 * This attribute maps to the column incident_description in the claim table.
	 */
	protected String incidentDescription;

	/** 
	 * This attribute maps to the column vehicle_location in the claim table.
	 */
	protected String vehicleLocation;

	/** 
	 * This attribute maps to the column proposed_rental_class_id in the claim table.
	 */
	protected int proposedRentalClassId;

	/** 
	 * This attribute represents whether the primitive attribute proposedRentalClassId is null.
	 */
	protected boolean proposedRentalClassIdNull = true;

	/** 
	 * This attribute maps to the column claim_status in the claim table.
	 */
	protected String claimStatus;

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
	 * Method 'getUuid'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUuid()
	{
		return uuid;
	}

	/**
	 * Method 'setUuid'
	 * 
	 * @param uuid
	 */
	public void setUuid(java.lang.String uuid)
	{
		this.uuid = uuid;
	}

	/**
	 * Method 'getRentalId'
	 * 
	 * @return int
	 */
	public int getRentalId()
	{
		return rentalId;
	}

	/**
	 * Method 'setRentalId'
	 * 
	 * @param rentalId
	 */
	public void setRentalId(int rentalId)
	{
		this.rentalId = rentalId;
	}

	/**
	 * Method 'getInsurerCountryId'
	 * 
	 * @return int
	 */
	public int getInsurerCountryId()
	{
		return insurerCountryId;
	}

	/**
	 * Method 'setInsurerCountryId'
	 * 
	 * @param insurerCountryId
	 */
	public void setInsurerCountryId(int insurerCountryId)
	{
		this.insurerCountryId = insurerCountryId;
		this.insurerCountryIdNull = false;
	}

	/** 
	 * Sets the value of insurerCountryIdNull
	 */
	public void setInsurerCountryIdNull(boolean insurerCountryIdNull)
	{
		this.insurerCountryIdNull = insurerCountryIdNull;
	}

	/** 
	 * Gets the value of insurerCountryIdNull
	 */
	public boolean isInsurerCountryIdNull()
	{
		return insurerCountryIdNull;
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
	 * Method 'getComprehensive'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getComprehensive()
	{
		return comprehensive;
	}

	/**
	 * Method 'setComprehensive'
	 * 
	 * @param comprehensive
	 */
	public void setComprehensive(java.lang.String comprehensive)
	{
		this.comprehensive = comprehensive;
	}

	/**
	 * Method 'getPolicyHolderName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPolicyHolderName()
	{
		return policyHolderName;
	}

	/**
	 * Method 'setPolicyHolderName'
	 * 
	 * @param policyHolderName
	 */
	public void setPolicyHolderName(java.lang.String policyHolderName)
	{
		this.policyHolderName = policyHolderName;
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
	 * Method 'getVehicleClassId'
	 * 
	 * @return int
	 */
	public int getVehicleClassId()
	{
		return vehicleClassId;
	}

	/**
	 * Method 'setVehicleClassId'
	 * 
	 * @param vehicleClassId
	 */
	public void setVehicleClassId(int vehicleClassId)
	{
		this.vehicleClassId = vehicleClassId;
	}

	/**
	 * Method 'getUsable'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUsable()
	{
		return usable;
	}

	/**
	 * Method 'setUsable'
	 * 
	 * @param usable
	 */
	public void setUsable(java.lang.String usable)
	{
		this.usable = usable;
	}

	/**
	 * Method 'getDamageDescription'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDamageDescription()
	{
		return damageDescription;
	}

	/**
	 * Method 'setDamageDescription'
	 * 
	 * @param damageDescription
	 */
	public void setDamageDescription(java.lang.String damageDescription)
	{
		this.damageDescription = damageDescription;
	}

	/**
	 * Method 'getTpInsurerCountryId'
	 * 
	 * @return int
	 */
	public int getTpInsurerCountryId()
	{
		return tpInsurerCountryId;
	}

	/**
	 * Method 'setTpInsurerCountryId'
	 * 
	 * @param tpInsurerCountryId
	 */
	public void setTpInsurerCountryId(int tpInsurerCountryId)
	{
		this.tpInsurerCountryId = tpInsurerCountryId;
	}

	/**
	 * Method 'getTpPolicyNumber'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpPolicyNumber()
	{
		return tpPolicyNumber;
	}

	/**
	 * Method 'setTpPolicyNumber'
	 * 
	 * @param tpPolicyNumber
	 */
	public void setTpPolicyNumber(java.lang.String tpPolicyNumber)
	{
		this.tpPolicyNumber = tpPolicyNumber;
	}

	/**
	 * Method 'getTpClaimReference'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpClaimReference()
	{
		return tpClaimReference;
	}

	/**
	 * Method 'setTpClaimReference'
	 * 
	 * @param tpClaimReference
	 */
	public void setTpClaimReference(java.lang.String tpClaimReference)
	{
		this.tpClaimReference = tpClaimReference;
	}

	/**
	 * Method 'getTpVehicleRegistration'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpVehicleRegistration()
	{
		return tpVehicleRegistration;
	}

	/**
	 * Method 'setTpVehicleRegistration'
	 * 
	 * @param tpVehicleRegistration
	 */
	public void setTpVehicleRegistration(java.lang.String tpVehicleRegistration)
	{
		this.tpVehicleRegistration = tpVehicleRegistration;
	}

	/**
	 * Method 'getTpVehicleManufacturer'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpVehicleManufacturer()
	{
		return tpVehicleManufacturer;
	}

	/**
	 * Method 'setTpVehicleManufacturer'
	 * 
	 * @param tpVehicleManufacturer
	 */
	public void setTpVehicleManufacturer(java.lang.String tpVehicleManufacturer)
	{
		this.tpVehicleManufacturer = tpVehicleManufacturer;
	}

	/**
	 * Method 'getTpVehicleModel'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpVehicleModel()
	{
		return tpVehicleModel;
	}

	/**
	 * Method 'setTpVehicleModel'
	 * 
	 * @param tpVehicleModel
	 */
	public void setTpVehicleModel(java.lang.String tpVehicleModel)
	{
		this.tpVehicleModel = tpVehicleModel;
	}

	/**
	 * Method 'getTpVehicleClassId'
	 * 
	 * @return int
	 */
	public int getTpVehicleClassId()
	{
		return tpVehicleClassId;
	}

	/**
	 * Method 'setTpVehicleClassId'
	 * 
	 * @param tpVehicleClassId
	 */
	public void setTpVehicleClassId(int tpVehicleClassId)
	{
		this.tpVehicleClassId = tpVehicleClassId;
		this.tpVehicleClassIdNull = false;
	}

	/** 
	 * Sets the value of tpVehicleClassIdNull
	 */
	public void setTpVehicleClassIdNull(boolean tpVehicleClassIdNull)
	{
		this.tpVehicleClassIdNull = tpVehicleClassIdNull;
	}

	/** 
	 * Gets the value of tpVehicleClassIdNull
	 */
	public boolean isTpVehicleClassIdNull()
	{
		return tpVehicleClassIdNull;
	}

	/**
	 * Method 'getTpName'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpName()
	{
		return tpName;
	}

	/**
	 * Method 'setTpName'
	 * 
	 * @param tpName
	 */
	public void setTpName(java.lang.String tpName)
	{
		this.tpName = tpName;
	}

	/**
	 * Method 'getTpAddress1'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpAddress1()
	{
		return tpAddress1;
	}

	/**
	 * Method 'setTpAddress1'
	 * 
	 * @param tpAddress1
	 */
	public void setTpAddress1(java.lang.String tpAddress1)
	{
		this.tpAddress1 = tpAddress1;
	}

	/**
	 * Method 'getTpAddress2'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpAddress2()
	{
		return tpAddress2;
	}

	/**
	 * Method 'setTpAddress2'
	 * 
	 * @param tpAddress2
	 */
	public void setTpAddress2(java.lang.String tpAddress2)
	{
		this.tpAddress2 = tpAddress2;
	}

	/**
	 * Method 'getTpAddress3'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpAddress3()
	{
		return tpAddress3;
	}

	/**
	 * Method 'setTpAddress3'
	 * 
	 * @param tpAddress3
	 */
	public void setTpAddress3(java.lang.String tpAddress3)
	{
		this.tpAddress3 = tpAddress3;
	}

	/**
	 * Method 'getTpAddress4'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpAddress4()
	{
		return tpAddress4;
	}

	/**
	 * Method 'setTpAddress4'
	 * 
	 * @param tpAddress4
	 */
	public void setTpAddress4(java.lang.String tpAddress4)
	{
		this.tpAddress4 = tpAddress4;
	}

	/**
	 * Method 'getTpAddress5'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpAddress5()
	{
		return tpAddress5;
	}

	/**
	 * Method 'setTpAddress5'
	 * 
	 * @param tpAddress5
	 */
	public void setTpAddress5(java.lang.String tpAddress5)
	{
		this.tpAddress5 = tpAddress5;
	}

	/**
	 * Method 'getTpPostcode'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpPostcode()
	{
		return tpPostcode;
	}

	/**
	 * Method 'setTpPostcode'
	 * 
	 * @param tpPostcode
	 */
	public void setTpPostcode(java.lang.String tpPostcode)
	{
		this.tpPostcode = tpPostcode;
	}

	/**
	 * Method 'getTpTelephoneDay'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpTelephoneDay()
	{
		return tpTelephoneDay;
	}

	/**
	 * Method 'setTpTelephoneDay'
	 * 
	 * @param tpTelephoneDay
	 */
	public void setTpTelephoneDay(java.lang.String tpTelephoneDay)
	{
		this.tpTelephoneDay = tpTelephoneDay;
	}

	/**
	 * Method 'getTpTelephoneEvening'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpTelephoneEvening()
	{
		return tpTelephoneEvening;
	}

	/**
	 * Method 'setTpTelephoneEvening'
	 * 
	 * @param tpTelephoneEvening
	 */
	public void setTpTelephoneEvening(java.lang.String tpTelephoneEvening)
	{
		this.tpTelephoneEvening = tpTelephoneEvening;
	}

	/**
	 * Method 'getTpEmail'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTpEmail()
	{
		return tpEmail;
	}

	/**
	 * Method 'setTpEmail'
	 * 
	 * @param tpEmail
	 */
	public void setTpEmail(java.lang.String tpEmail)
	{
		this.tpEmail = tpEmail;
	}

	/**
	 * Method 'getIncidentDate'
	 * 
	 * @return java.util.Date
	 */
	public java.util.Date getIncidentDate()
	{
		return incidentDate;
	}

	/**
	 * Method 'setIncidentDate'
	 * 
	 * @param incidentDate
	 */
	public void setIncidentDate(java.util.Date incidentDate)
	{
		this.incidentDate = incidentDate;
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
	 * Method 'getPoliceInvolved'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPoliceInvolved()
	{
		return policeInvolved;
	}

	/**
	 * Method 'setPoliceInvolved'
	 * 
	 * @param policeInvolved
	 */
	public void setPoliceInvolved(java.lang.String policeInvolved)
	{
		this.policeInvolved = policeInvolved;
	}

	/**
	 * Method 'getIncidentDescription'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getIncidentDescription()
	{
		return incidentDescription;
	}

	/**
	 * Method 'setIncidentDescription'
	 * 
	 * @param incidentDescription
	 */
	public void setIncidentDescription(java.lang.String incidentDescription)
	{
		this.incidentDescription = incidentDescription;
	}

	/**
	 * Method 'getVehicleLocation'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVehicleLocation()
	{
		return vehicleLocation;
	}

	/**
	 * Method 'setVehicleLocation'
	 * 
	 * @param vehicleLocation
	 */
	public void setVehicleLocation(java.lang.String vehicleLocation)
	{
		this.vehicleLocation = vehicleLocation;
	}

	/**
	 * Method 'getProposedRentalClassId'
	 * 
	 * @return int
	 */
	public int getProposedRentalClassId()
	{
		return proposedRentalClassId;
	}

	/**
	 * Method 'setProposedRentalClassId'
	 * 
	 * @param proposedRentalClassId
	 */
	public void setProposedRentalClassId(int proposedRentalClassId)
	{
		this.proposedRentalClassId = proposedRentalClassId;
		this.proposedRentalClassIdNull = false;
	}

	/** 
	 * Sets the value of proposedRentalClassIdNull
	 */
	public void setProposedRentalClassIdNull(boolean proposedRentalClassIdNull)
	{
		this.proposedRentalClassIdNull = proposedRentalClassIdNull;
	}

	/** 
	 * Gets the value of proposedRentalClassIdNull
	 */
	public boolean isProposedRentalClassIdNull()
	{
		return proposedRentalClassIdNull;
	}

	/**
	 * Method 'getClaimStatus'
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getClaimStatus()
	{
		return claimStatus;
	}

	/**
	 * Method 'setClaimStatus'
	 * 
	 * @param claimStatus
	 */
	public void setClaimStatus(java.lang.String claimStatus)
	{
		this.claimStatus = claimStatus;
	}

}
