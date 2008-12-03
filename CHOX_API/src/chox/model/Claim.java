package chox.model;

import java.io.Serializable;
import java.util.Date;
import scsbre.model.ICHOBandInfo;
import scsbre.model.ICHOrganisationInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IEngineerReportInfo;
import scsbre.model.IExtrasInfo;
import scsbre.model.IHireInfo;
import scsbre.model.IVehicleClassInfo;
import java.math.BigDecimal;

public class Claim implements Serializable, IClaimInfo {

    protected ChoBand choband;
    /** 
     * This attribute maps to the column id in the claim table.
     */
    protected int id;
    /** 
     * This attribute maps to the column managing_repair in the claim table.
     */
    protected boolean managingRepair;
    /** 
     * This attribute maps to the column policy_holder_contact_date in the claim table.
     */
    protected Date policyHolderContactDate;
    /** 
     * This attribute maps to the column cho_reference in the claim table.
     */
    private String choReference;
    /** 
     * This attribute maps to the column status in the claim table.
     */
    protected String status;
    /** 
     * This attribute maps to the column created_by in the claim table.
     */
    protected WebUser createdBy;
    /** 
     * This attribute maps to the column created_date in the claim table.
     */
    protected Date createdDate;
    /** 
     * This attribute maps to the column last_modified_by in the claim table.
     */
    protected int lastModifiedBy;
    /** 
     * This attribute maps to the column last_modified_date in the claim table.
     */
    protected Date lastModifiedDate;
    /** 
     * This attribute represents the foreign key relationship to the chorganisation table.
     */
    protected Chorganisation chorganisation;
    /** 
     * This attribute represents the foreign key relationship to the customer table.
     */
    protected Customer customer;
    /** 
     * This attribute represents the foreign key relationship to the engineer_report table.
     */
    protected EngineerReport engineerReport;
    /** 
     * This attribute represents the foreign key relationship to the hire_monitoring_detail table.
     */
    protected HireMonitoringDetail hireMonitoringDetail;
    /** 
     * This attribute represents the foreign key relationship to the incident table.
     */
    protected Incident incident;
    /** 
     * This attribute represents the foreign key relationship to the insurer table.
     */
    protected Insurer insurer;
    /** 
     * This attribute represents the foreign key relationship to the invoice table.
     */
    protected Invoice invoice;
    /** 
     * This attribute represents the foreign key relationship to the line_of_business table.
     */
    protected LineOfBusiness lineOfBusiness;
    /** 
     * This attribute represents the foreign key relationship to the third_party table.
     */
    protected ThirdParty thirdParty;
    /** 
     * This attribute represents the foreign key relationship to the vehicle_hire table.
     */
    protected VehicleHire vehicleHire;
    /** 
     * This attribute maps to the column claim_number in the claim table.
     */
    protected String claimNumber;
    protected Date creditAgreementDate;
    protected Date gtaNoticeDate;
    protected BigDecimal indemnityAmount;//indemnity
    protected BigDecimal percentageLiabilityAccepted;
    protected boolean isQuantumDispute;
    protected String engineerClaimReviewNotes;
    protected boolean isInvoiceReviewRequired;

    /**
     * Method 'Claim'
     * 
     */
    public Claim() {
    }

    /**
     * Method 'getId'
     * 
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Method 'setId'
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method 'isManagingRepair'
     * 
     * @return boolean
     */
    public boolean isManagingRepair() {
        return managingRepair;
    }

    /**
     * Method 'setManagingRepair'
     * 
     * @param managingRepair
     */
    public void setManagingRepair(boolean managingRepair) {
        this.managingRepair = managingRepair;
    }

    /**
     * Method 'getPolicyHolderContactDate'
     * 
     * @return java.util.Date
     */
    public java.util.Date getPolicyHolderContactDate() {
        return policyHolderContactDate;
    }

    /**
     * Method 'setPolicyHolderContactDate'
     * 
     * @param policyHolderContactDate
     */
    public void setPolicyHolderContactDate(java.util.Date policyHolderContactDate) {
        this.policyHolderContactDate = policyHolderContactDate;
    }

    /**
     * Method 'getChoReference'
     * 
     * @return java.lang.String
     */
    public String getChoReference() {
        return choReference;
    }

    /**
     * Method 'setChoReference'
     * 
     * @param choReference
     */
    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    /**
     * Method 'getStatus'
     * 
     * @return java.lang.String
     */
    public java.lang.String getStatus() {
        return status;
    }

    /**
     * Method 'setStatus'
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    /**
     * Method 'getCreatedBy'
     * 
     * @return int
     */
    public WebUser getCreatedBy() {
        return createdBy;
    }

    /**
     * Method 'setCreatedBy'
     * 
     * @param createdBy
     */
    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Method 'getCreatedDate'
     * 
     * @return java.util.Date
     */
    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Method 'setCreatedDate'
     * 
     * @param createdDate
     */
    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Method 'getLastModifiedBy'
     * 
     * @return int
     */
    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * Method 'setLastModifiedBy'
     * 
     * @param lastModifiedBy
     */
    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * Method 'getLastModifiedDate'
     * 
     * @return java.util.Date
     */
    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * Method 'setLastModifiedDate'
     * 
     * @param lastModifiedDate
     */
    public void setLastModifiedDate(java.util.Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Method 'getChorganisation'
     * 
     * @return Chorganisation
     */
    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    /**
     * Method 'setChorganisation'
     * 
     * @param chorganisation
     */
    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    /**
     * Method 'getCustomer'
     * 
     * @return Customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Method 'setCustomer'
     * 
     * @param customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Method 'getEngineerReport'
     * 
     * @return EngineerReport
     */
    public EngineerReport getEngineerReport() {
        return engineerReport;
    }

    /**
     * Method 'setEngineerReport'
     * 
     * @param engineerReport
     */
    public void setEngineerReport(EngineerReport engineerReport) {
        this.engineerReport = engineerReport;
    }

    /**
     * Method 'getHireMonitoringDetail'
     * 
     * @return HireMonitoringDetail
     */
    public HireMonitoringDetail getHireMonitoringDetail() {
        return hireMonitoringDetail;
    }

    /**
     * Method 'setHireMonitoringDetail'
     * 
     * @param hireMonitoringDetail
     */
    public void setHireMonitoringDetail(HireMonitoringDetail hireMonitoringDetail) {
        this.hireMonitoringDetail = hireMonitoringDetail;
    }

    /**
     * Method 'getIncident'
     * 
     * @return Incident
     */
    public Incident getIncident() {
        return incident;
    }

    /**
     * Method 'setIncident'
     * 
     * @param incident
     */
    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    /**
     * Method 'getInsurer'
     * 
     * @return Insurer
     */
    public Insurer getInsurer() {
        return insurer;
    }

    /**
     * Method 'setInsurer'
     * 
     * @param insurer
     */
    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    /**
     * Method 'getInvoice'
     * 
     * @return Invoice
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Method 'setInvoice'
     * 
     * @param invoice
     */
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    /**
     * Method 'getLineOfBusiness'
     * 
     * @return LineOfBusiness
     */
    public LineOfBusiness getLineOfBusiness() {
        return lineOfBusiness;
    }

    /**
     * Method 'setLineOfBusiness'
     * 
     * @param lineOfBusiness
     */
    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    /**
     * Method 'getThirdParty'
     * 
     * @return ThirdParty
     */
    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    /**
     * Method 'setThirdParty'
     * 
     * @param thirdParty
     */
    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    /**
     * Method 'getVehicleHire'
     * 
     * @return VehicleHire
     */
    public VehicleHire getVehicleHire() {
        return vehicleHire;
    }

    /**
     * Method 'setVehicleHire'
     * 
     * @param vehicleHire
     */
    public void setVehicleHire(VehicleHire vehicleHire) {
        this.vehicleHire = vehicleHire;
    }

    public ICHOrganisationInfo getCHOrg() {
        return this.chorganisation;
    }

    public IHireInfo getHireDetail() {
        return this.vehicleHire;
    }

    public IEngineerReportInfo getEngineeringReport() {
        return this.engineerReport;
    }

    public boolean getManagingRepair() {
        return this.managingRepair;
    }
    // NO VEHICLE CLASS DIRECT ASSIGN TO CLAIM, ONLY TO VEHICLE
    public IVehicleClassInfo getVClass() {
        return this.customer.vehicleClass;
    }
    // ICustomerVehicleDamageInfo IS PART OF CUSTOMER DETAIL
    public ICustomerVehicleDamageInfo getCustomerVehicleDamage() {
        return this.customer;
    }
    // getExtras IS PART OF INVOICE DETAIL
    public IExtrasInfo getExtras() {
        return this.invoice;
    }

    public void setChoband(ChoBand choband) {
        this.choband = choband;
    }

    public ICHOBandInfo getChoBand() {
        return choband;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Date getCreditAgreementDate() {
        return creditAgreementDate;
    }

    public void setCreditAgreementDate(Date creditAgreementDate) {
        this.creditAgreementDate = creditAgreementDate;
    }

    public Date getGtaNoticeDate() {
        return gtaNoticeDate;
    }

    public void setGtaNoticeDate(Date gtaNoticeDate) {
        this.gtaNoticeDate = gtaNoticeDate;
    }

    public String getEngineerClaimReviewNotes() {
        return engineerClaimReviewNotes;
    }

    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    public BigDecimal getIndemnityAmount() {
        return indemnityAmount;
    }

    public void setIndemnityAmount(BigDecimal indemintyAmount) {
        this.indemnityAmount = indemintyAmount;
    }

    public boolean getIsInvoiceReviewRequired() {
        return isInvoiceReviewRequired;
    }

    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired = isInvoiceReviewRequired;
    }

    public boolean getIsQuantumDispute() {
        return isQuantumDispute;
    }

    public void setIsQuantumDispute(boolean isQuantumDispute) {
        this.isQuantumDispute = isQuantumDispute;
    }

    public BigDecimal getPercentageLiabilityAccepted() {
        return percentageLiabilityAccepted;
    }

    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }
    
    
}
