package chox.model;

import chox.Util.DateHelper;
import chox.model.notifications.AnomalousCheck;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.io.Serializable;
import java.util.Date;
import scsbre.model.IBREBandInfo;
import scsbre.model.ICHOrganisationInfo;
import scsbre.model.IClaimInfo;
import scsbre.model.ICustomerVehicleDamageInfo;
import scsbre.model.IEngineerReportInfo;
import scsbre.model.IExtrasInfo;
import scsbre.model.IHireInfo;
import scsbre.model.IVehicleClassInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import scsbre.model.IVehicleClassCeilingInfo;

public class Claim extends AuditableEntity implements Serializable, IClaimInfo {

    // <editor-fold defaultstate="collapsed" desc=" Member Variables ">
    private boolean managingRepair;
    private Date policyHolderContactDate;
    private String choReference;
    private String status;
    private String claimNumber;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private String engineerClaimReviewNotes;
    private boolean isInvoiceReviewRequired;
    private Date creditAgreementDate;
    private Date gtaNoticeDate;
    private boolean isFnolReviewed;
    private Integer reasonOfRejectionId;
    private Date statusModifiedDate;
    private String previousStatus;
    private Date hireMonitoringEcd;
    private WebUser claimOwner;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Composite Objects ">
    private Insurer insurer;
    private Chorganisation chorganisation;
    private Customer customer;
    private Incident incident;
    private Invoice invoice;
    private ThirdParty thirdParty;
    private VehicleHire vehicleHire;
    private EngineerReport engineerReport;
    private HireMonitoringDetail hireMonitoringDetail;
    private Workgroup workgroup;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Composite Collections ">
    private List<HireMonitoringEcd> hireMonitoringEcds;
    private List<Notification> notifications;
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" BRE Values ">
    //BRE values are values required for BRE engine that related with the claim
    // It need to be set explicitly before pass the claim object into BRE engine

    private IBREBandInfo breband;
    private IVehicleClassCeilingInfo vehicleClassCeiling;
    // </editor-fold>   

    public Claim() {
        hireMonitoringEcds = new ArrayList<HireMonitoringEcd>();
        notifications = new ArrayList<Notification>();
    }

    // <editor-fold defaultstate="collapsed" desc="Public Properties">
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

    public Workgroup getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(Workgroup workgroup) {
        this.workgroup = workgroup;
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

    public boolean getManagingRepair() {
        return this.managingRepair;
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

    public void setHireMonitoringEcd(Date d) {
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BRE Properties ">
    public IHireInfo getHireDetail() {
        return this.getVehicleHire();
    }

    public IEngineerReportInfo getEngineeringReport() {
        return this.getEngineerReport();
    }

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

    public void setBreBand(IBREBandInfo breband) {
        this.breband = breband;
    }

    public IBREBandInfo getBreBand() {
        return this.breband;
    }
        
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Logic Methods ">

    public String getIsManagingRepairDesc() {
        return managingRepair ? "Yes" : "No";
    }

    public String getIsInvoiceReviewRequiredDesc() {
        return isInvoiceReviewRequired ? "Yes" : "No";
    }

    public String getIsQuantumDisputeDesc() {
        return isQuantumDispute ? "Yes" : "No";
    }

    public boolean getIsIsAnomalies() {
        return notifications != null ? notifications.size() > 0 : false;
    }

    public String getIsAnomaliesDesc() {
        return getIsIsAnomalies() ? "Yes" : "No";
    }

    public Long getDaysInStatus() {
        Date now = new Date();
        Date lastStatusModified = this.getStatusModifiedDate();

        return DateHelper.daysBetween(lastStatusModified, now);
    }

    public ICHOrganisationInfo getCHOrg() {
        return this.chorganisation;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" HireMonitoringEcd ">
    public List<HireMonitoringEcd> getHireMonitoringEcds() {
        return hireMonitoringEcds;
    }

    public void setHireMonitoringEcds(List<HireMonitoringEcd> hireMonitoringEcds) {
        this.hireMonitoringEcds = hireMonitoringEcds;
    }

    public void addHireMonitoringEcd(HireMonitoringEcd ecd) {
        ecd.claim = this;
        hireMonitoringEcds.add(ecd);
    }

	public Date getLatestHireMonitoringEcd() {
    
        if (hireMonitoringEcds != null && !hireMonitoringEcds.isEmpty()) {


            // Emmanuel 08-09-2009
            // the HireMonitoringEcd is sorted by "createdDate" when retrieving from daabase, see claim.hbm.xml
            // so the last item must be the latest updated Ecd

            HireMonitoringEcd latestEcd = hireMonitoringEcds.get(hireMonitoringEcds.size() - 1);
            return latestEcd.getEcdDate();

        } else if (customer != null) {
            
			//return the initial ecd if have no hireMonitoringEcd been added
            return customer.getInitialECD();

        }

        return null;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Notification ">
    public List<Notification> getNotifications() {
        return notifications;
    }

    private void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    //Add a list of  notification to claim
    //the isAnomalies will automatic mark as true
    public void AddNotifications(List<AnomalousCheck> anomalousChecks, List<Notification> notifications) {

        RemoveNotification(anomalousChecks);

        if (notifications != null) {
            for (Notification notification : notifications) {
                AddNotification(notification);
            }
        }
        
    }

    private void RemoveNotification(List<AnomalousCheck> anomalousChecks){

        if(this.notifications.size()>0){

            for(AnomalousCheck anc : anomalousChecks){
                
                if(anc.isRefreshRequired()){
                    
                    boolean isDeletable = true;

                    // DO NOT DELETE DAY CHECK WHEN THE RepairBookInDate Doesn't Changed
                    if((anc.BuildNotification().getType().equalsIgnoreCase("RepairBookedInOnFridayNotification")
                            || anc.BuildNotification().getType().equalsIgnoreCase("RepairBookedInOnSaturdayNotification")
                            || anc.BuildNotification().getType().equalsIgnoreCase("RepairBookedInOnSundayNotification"))
                            && DateHelper.DateCompare(this.getHireMonitoringDetail().getRepairBookInDate(), this.getHireMonitoringDetail().getNotificationRepairBookInDate())
                            ){

                            isDeletable = false;

                            // System.out.println("BuildNotification TYPE:"+anc.BuildNotification().getType());
                            // System.out.println("RepairBookInDate:"+this.getHireMonitoringDetail().getRepairBookInDate());
                            // System.out.println("NotificationRepairBookInDate:"+this.getHireMonitoringDetail().getNotificationRepairBookInDate());
                            // System.out.println("");
                    }
                    
                    if(isDeletable){
                        
                        Notification notificationToBeRemoved = GetNotificationByType(anc.BuildNotification().getType());

                        if(notificationToBeRemoved!=null){
                            RemoveNotifications(notificationToBeRemoved);
                        }
                        
                    }
                }
            }
        }
    }

    public void AddNotification(Notification notification) {
        
        if (notification != null && !isSameTypeOfNotificationExist(notification)) {
            
            if (this.notifications == null) {
                this.notifications = new ArrayList<Notification>();
            }

            notification.setClaim(this);
            notifications.add(notification);
        }
    }

    public boolean isSameTypeOfNotificationExist(Notification notification) {
        
        if (this.notifications != null) {
            for (Notification n : notifications) {
                if (n.getType().equals(notification.getType())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void RemoveAllNotifications(){
        notifications.clear();
    }
    
    public void RemoveNotifications(Notification notification) {
        notifications.remove(notification);
    }

    private Notification GetNotificationByType(String type) {
        
        for (Notification notification : notifications) {
       
            if (notification.getType().equalsIgnoreCase(type)){
                return notification;
            }

        }
        
        return null;
    }

    public Notification GetNotificationById(int id) {
        for (Notification notification : notifications) {
            if (notification.getId() == id) {
                return notification;
            }
        }
        return null;
    }

    public WebUser getClaimOwner() {
        return claimOwner;
    }

    public void setClaimOwner(WebUser claimOwner) {
        this.claimOwner = claimOwner;
    }

    // </editor-fold>  

    
}
