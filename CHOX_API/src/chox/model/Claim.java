package chox.model;

import chox.Util.DateHelper;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
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

public class Claim implements Serializable, Auditable, IClaimInfo{

    private int id;
    private boolean managingRepair;
    private Date policyHolderContactDate;
    private String choReference;
    private String status;
    protected Insurer insurer;
    protected Chorganisation chorganisation;
    protected LineOfBusiness lineOfBusiness;
    protected Customer customer;
    protected Incident incident;
    protected Invoice invoice;
    protected ThirdParty thirdParty;
    protected VehicleHire vehicleHire;
    protected EngineerReport engineerReport;
    protected WebUser createdBy;
    protected Date createdDate;
    protected WebUser lastModifiedBy;
    protected Date lastModifiedDate;
    protected HireMonitoringDetail hireMonitoringDetail;
    private String claimNumber;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private String engineerClaimReviewNotes;
    private boolean isInvoiceReviewRequired;
    private Date creditAgreementDate;
    private Date gtaNoticeDate;
    private boolean isAnomalies;
    private boolean isFnolReviewed;
    private Integer reasonOfRejectionId;
    private Date statusModifiedDate;
    private String previousStatus;
    
    //protected VehicleClass vehicleClass;
    protected Date hireMonitoringEcd;
    protected ChoBand choband;
    
    public Claim() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isManagingRepair() {
        return managingRepair;
    }

    public void setManagingRepair(boolean managingRepair) {
        this.managingRepair = managingRepair;
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getPolicyHolderContactDate() {
        return policyHolderContactDate;
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public void setPolicyHolderContactDate(java.util.Date policyHolderContactDate) {
        this.policyHolderContactDate = policyHolderContactDate;
    }

    public String getChoReference() {
        return choReference;
    }

    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public WebUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(WebUser createdBy) {
        this.createdBy = createdBy;
    }

    public java.util.Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(java.util.Date createdDate) {
        this.createdDate = createdDate;
    }

    public WebUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(WebUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public java.util.Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(java.util.Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public EngineerReport getEngineerReport() {
        return engineerReport;
    }

    public void setEngineerReport(EngineerReport engineerReport) {
        this.engineerReport = engineerReport;
    }

    public HireMonitoringDetail getHireMonitoringDetail() {
        return hireMonitoringDetail;
    }

    public void setHireMonitoringDetail(HireMonitoringDetail hireMonitoringDetail) {
        this.hireMonitoringDetail = hireMonitoringDetail;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public LineOfBusiness getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(LineOfBusiness lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public VehicleHire getVehicleHire() {
        return vehicleHire;
    }

    public void setVehicleHire(VehicleHire vehicleHire) {
        this.vehicleHire = vehicleHire;
    }

    public ICHOrganisationInfo getCHOrg() {
        return this.chorganisation;
    }

    public IHireInfo getHireDetail() {
        return this.getVehicleHire();
    }

    public IEngineerReportInfo getEngineeringReport() {
        return this.getEngineerReport();
    }

    public boolean getManagingRepair() {
        return this.managingRepair;
    }
    
    /*
    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
    */
    
    // NO VEHICLE CLASS DIRECT ASSIGN TO CLAIM, ONLY TO VEHICLE
    public IVehicleClassInfo getVClass() {
        return this.getCustomer().getVehicleClass();
    }
    
    // ICustomerVehicleDamageInfo IS PART OF CUSTOMER DETAIL
    public ICustomerVehicleDamageInfo getCustomerVehicleDamage() {
        return this.getCustomer();
    }
    
    // getExtras IS PART OF INVOICE DETAIL
    public IExtrasInfo getExtras() {
        return this.getInvoice();
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

    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getCreditAgreementDate() {
        return creditAgreementDate;
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public void setCreditAgreementDate(Date creditAgreementDate) {
        this.creditAgreementDate = creditAgreementDate;
    }

    @TypeConversion(converter = "chox.data.DateConverter")
    public java.util.Date getGtaNoticeDate() {
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
    
    public String getIsManagingRepairDesc() {
        return managingRepair ? "Yes" : "No";
    }
        
    public String getIsInvoiceReviewRequiredDesc()
    {
        return isInvoiceReviewRequired ? "Yes" : "No";
    }
    
    public String getIsQuantumDisputeDesc() {
        return isQuantumDispute ? "Yes" : "No";
    }

    public boolean isIsAnomalies() {
        return isAnomalies;
    }

    public void setIsAnomalies(boolean isAnomalies) {
        this.isAnomalies = isAnomalies;
    }
    
    public String getIsAnomaliesDesc() {
        return isAnomalies ? "Yes" : "No";
    }
    
    public void setHireMonitoringEcd(Date d){
        this.hireMonitoringEcd = d;
    }
    
    public Date getHireMonitoringEcd() {
        return this.hireMonitoringEcd;
    }

    public boolean isIsFnolReviewed() {
        return isFnolReviewed;
    }

    public void setIsFnolReviewed(boolean isFnolReviewed) {
        this.isFnolReviewed = isFnolReviewed;
    }

    public Integer getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    public void setReasonOfRejectionId(Integer reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    public Date getStatusModifiedDate() {
        return statusModifiedDate;
    }

    public void setStatusModifiedDate(Date statusModifiedDate) {
        this.statusModifiedDate = statusModifiedDate;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }
    
    public Long getDaysInStatus()
    {
        Date now = new Date();
        Date lastStatusModified = this.getStatusModifiedDate();
        
        return DateHelper.daysBetween(lastStatusModified, now);
    }
    
}
