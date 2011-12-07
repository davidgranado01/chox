package idas.chox.core.model;

import idas.chox.core.util.DateHelper;
import idas.chox.core.notifications.AnomalousCheck;
import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Claim extends Entity implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Claim.class);
    // <editor-fold defaultstate="collapsed" desc=" Member Variables ">
    private boolean managingRepair;
    private Date policyHolderContactDate;
    private String choReference;
    private String status;
    private String claimNumber;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private Date creditAgreementDate;
    private Date gtaNoticeDate;
    private boolean isFnolReviewed;
    private ReasonOfRejection reasonOfRejection;
    private Date statusModifiedDate;
    private String previousStatus;
    private WebUser claimOwner;
    private WebUser claimOwnerOriginal;
    private WebUser supplierClaimOwner;
    private BreBand choband;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    private ClaimType claimType;
//    private boolean tpiClaim;
//    private boolean insurerVsInsurerClaim;
    private boolean specialRoutedTpiClaim;
    private String tpiClaimStatus;
//    private boolean supplementaryInvoicedClaim;
    private boolean insurerUpload;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Composite Objects ">
    private Insurer insurer;
    private Chorganisation chorganisation;
    private Customer customer;
    private Incident incident;
    private Invoice invoice;
    private InvoiceOriginal invoice_original;
    private ThirdParty thirdParty;
    private VehicleHire vehicleHire;
    private EngineerReport engineerReport;
    private HireMonitoringDetail hireMonitoringDetail;
    private Workgroup workgroup;
    private Workgroup workgroupOriginal;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Composite Collections ">
    private List<HireMonitoringEcd> hireMonitoringEcds;
    private List<Notification> notifications;
    private List<Attachment> attachments;
    private List<History> histories;
    private List<Comment> comments;
    // </editor-fold>
    
//    private boolean originalSupplementaryInvoicedClaim;

    public Claim() {
        this.liabilityStatus = LiabilityStatus.LIABILITY_NULL;
        this.claimType = ClaimType.GTA;
    }

    // <editor-fold defaultstate="collapsed" desc="Public Properties">
    public boolean isInsurerUpload() {
        return insurerUpload;
    }

    public void setInsurerUpload(boolean insurerUpload) {
        this.insurerUpload = insurerUpload;
    }

    public boolean isSpecialRoutedTpiClaim() {
        return specialRoutedTpiClaim;
    }

    public void setSpecialRoutedTpiClaim(boolean specialRoutedTpiClaim) {
        this.specialRoutedTpiClaim = specialRoutedTpiClaim;
    }

    @Deprecated
    public boolean isTpiClaim() {
        return ClaimType.isTPI(getClaimType());
    }

/**
    public boolean isOriginalSupplementaryInvoicedClaim() {
        return originalSupplementaryInvoicedClaim;
    }

    public void setOriginalSupplementaryInvoicedClaim(boolean originalSupplementaryInvoicedClaim) {
        this.originalSupplementaryInvoicedClaim = originalSupplementaryInvoicedClaim;
    }
    
    public boolean isSupplementaryInvoicedClaim() {
        return supplementaryInvoicedClaim;
    }

    public void setSupplementaryInvoicedClaim(boolean supplementaryInvoicedClaim) {
        this.supplementaryInvoicedClaim = supplementaryInvoicedClaim;
    }


    public void setTpiClaim(boolean TpiClaim) {
        this.tpiClaim = TpiClaim;
    }

    public boolean isInsurerVsInsurerClaim() {
        return insurerVsInsurerClaim;
    }

    public void setInsurerVsInsurerClaim(boolean insurerVsInsurerClaim) {
        this.insurerVsInsurerClaim = insurerVsInsurerClaim;
    }
**/
    public String getTpiClaimStatus() {
        return tpiClaimStatus;
    }

    public void setTpiClaimStatus(String TpiClaimStatus) {
        this.tpiClaimStatus = TpiClaimStatus;
    }

    public InvoiceOriginal getInvoice_original() {
        return invoice_original;
    }

    public void setInvoice_original(InvoiceOriginal invoice_original) {
        this.invoice_original = invoice_original;
    }

    public boolean isManagingRepair() {
        return managingRepair;
    }

    public void setManagingRepair(boolean managingRepair) {
        this.managingRepair = managingRepair;
    }

    public java.util.Date getPolicyHolderContactDate() {
        return policyHolderContactDate;
    }

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

    public java.util.Date getCreditAgreementDate() {
        return creditAgreementDate;
    }

    public void setCreditAgreementDate(Date creditAgreementDate) {
        this.creditAgreementDate = creditAgreementDate;
    }

    public java.util.Date getGtaNoticeDate() {
        return gtaNoticeDate;
    }

    public void setGtaNoticeDate(Date gtaNoticeDate) {
        this.gtaNoticeDate = gtaNoticeDate;
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
        // This null check added to fix Bug#957 & Bug934.
        if (percentageLiabilityAccepted == null) {
            this.percentageLiabilityAccepted = BigDecimal.ZERO;
        }
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }

    public boolean isIsFnolReviewed() {
        return isFnolReviewed;
    }

    public void setIsFnolReviewed(boolean isFnolReviewed) {
        this.isFnolReviewed = isFnolReviewed;
    }

    public ReasonOfRejection getReasonOfRejection() {
        return reasonOfRejection;
    }

    public void setReasonOfRejection(ReasonOfRejection reasonOfRejection) {
        this.reasonOfRejection = reasonOfRejection;
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

    public WebUser getSupplierClaimOwner() {
        return supplierClaimOwner;
    }

    public void setSupplierClaimOwner(WebUser supplierClaimOwner) {
        this.supplierClaimOwner = supplierClaimOwner;
    }

    public WebUser getClaimOwnerOriginal() {
        return claimOwnerOriginal;
    }

    public void setClaimOwnerOriginal(WebUser claimOwnerOriginal) {
        this.claimOwnerOriginal = claimOwnerOriginal;
    }

    public Workgroup getWorkgroupOriginal() {
        return workgroupOriginal;
    }

    public void setWorkgroupOriginal(Workgroup workgroupOriginal) {
        this.workgroupOriginal = workgroupOriginal;
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

    public void updateLiabilityPayment() {

        LiabilityStatus l = getLiabilityStatus();
        if (getInvoice() != null) {
            if (!ClaimType.isInsurerVsInsurer(claimType) && l != null && (l.equals(LiabilityStatus.LIABILITY_SPLIT) || (l.equals(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE)))) {
                BigDecimal ttp = getInvoice().getFullTotalToPay();
                BigDecimal insper = getPercentageLiabilityAccepted();
                getInvoice().setTotalToPay(ttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                BigDecimal ofttp = getInvoice().getOriginalFullTotalToPay();
                getInvoice().setOriginalTotalToPay(ofttp.multiply(insper).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
                LOG.debug("liability updated " + getInvoice().getTotalToPay());
            } else if (!ClaimType.isInsurerVsInsurer(claimType) && l != null && l.equals(LiabilityStatus.LIABILITY_REPUDIATED)) {
                getInvoice().setTotalToPay(BigDecimal.ZERO);
                getInvoice().setOriginalTotalToPay(BigDecimal.ZERO);
            } else {
                getInvoice().setTotalToPay(getInvoice().getFullTotalToPay());
                LOG.debug("liablity not updated");
            }
        }
    }

    public long getLiabilityAgreedDays() {

        long dateDiff = DateHelper.daysBetween(getLiabilityAgreedDate(), new Date()) + 1;
        return dateDiff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" HireMonitoringEcd ">
    public List<HireMonitoringEcd> getHireMonitoringEcds() {
        if (hireMonitoringEcds == null) {
            hireMonitoringEcds = new ArrayList<HireMonitoringEcd>();
        }
        return hireMonitoringEcds;
    }

    public void setHireMonitoringEcds(List<HireMonitoringEcd> hireMonitoringEcds) {
        this.hireMonitoringEcds = hireMonitoringEcds;
    }

    public void addHireMonitoringEcd(HireMonitoringEcd ecd) {
        if (hireMonitoringEcds == null) {
            hireMonitoringEcds = new ArrayList<HireMonitoringEcd>();
        }
        ecd.setClaim(this);
        hireMonitoringEcds.add(ecd);
        LOG.debug("Hire Monitoring ECD added: {}", ecd.getEcdDate());
    }

    public Date getLatestHireMonitoringEcd() {

        if (hireMonitoringEcds != null && !hireMonitoringEcds.isEmpty()) {


            // Emmanuel 08-09-2009
            // the HireMonitoringEcd is sorted by "createdDate" when retrieving from daabase, see claim.hbm.xml
            // so the last item must be the latest updated Ecd

            HireMonitoringEcd latestEcd = hireMonitoringEcds.get(hireMonitoringEcds.size() - 1);
            LOG.debug("Latest hire monitoring ECD: {}", latestEcd.getEcdDate());
            return latestEcd.getEcdDate();

        } else if (customer != null) {

            //return the initial ecd if have no hireMonitoringEcd been added
            LOG.debug("Latest hire monitoring ECD is customer initial ECD: {}", customer.getInitialECD());
            return customer.getInitialECD();

        }

        LOG.debug("Latest hire monitoring ECD is null");
        return null;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Attachment ">
    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        if (attachments == null) {
            attachments = new ArrayList<Attachment>();
        }

        attachment.setClaim(this);
        attachments.add(attachment);
    }

    public void deleteAttachment(Attachment attachment) {
        attachments.remove(attachment);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Comments ">
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<Comment>();
        }

        comment.setClaim(this);
        comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="History">
    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public void addHistory(History history) {

        if (histories == null) {
            histories = new ArrayList<History>();
        }

        history.setClaim(this);
        histories.add(history);
    }

    public void addHistories(List<History> histories) {

        if (histories == null) {
            histories = new ArrayList<History>();
        }

        for (History history : histories) {
            history.setClaim(this);
            histories.add(history);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Notification">
    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
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

    private void RemoveNotification(List<AnomalousCheck> anomalousChecks) {

        if (this.notifications.size() > 0) {

            for (AnomalousCheck anc : anomalousChecks) {

                if (anc.isRefreshRequired()) {

                    boolean isDeletable = true;

                    // DO NOT DELETE DAY CHECK WHEN THE RepairBookInDate Doesn't Changed
                    if (anc.BuildNotification().getType().equalsIgnoreCase("EcdAnomalousNotification") || (anc.BuildNotification().getType().equalsIgnoreCase("RepairBookedInOnFridayNotification") || anc.BuildNotification().getType().equalsIgnoreCase("RepairBookedInOnSaturdayNotification") || anc.BuildNotification().getType().equalsIgnoreCase("RepairBookedInOnSundayNotification")) && DateHelper.DateCompare(this.getHireMonitoringDetail().getRepairBookInDate(), this.getHireMonitoringDetail().getNotificationRepairBookInDate())) {

                        isDeletable = false;

                        // System.out.println("BuildNotification TYPE:"+anc.BuildNotification().getType());
                        // System.out.println("RepairBookInDate:"+this.getHireMonitoringDetail().getRepairBookInDate());
                        // System.out.println("NotificationRepairBookInDate:"+this.getHireMonitoringDetail().getNotificationRepairBookInDate());
                        // System.out.println("");
                    }

                    if (isDeletable) {

                        Notification notificationToBeRemoved = GetNotificationByType(anc.BuildNotification().getType());

                        if (notificationToBeRemoved != null) {
                            RemoveNotifications(notificationToBeRemoved);
                        }

                    }
                }
            }
        }
    }

    /*
    public void AddNotification(Notification notification) {
    
    if (notification != null && !isSameTypeOfNotificationExist(notification)) {
    
    if (this.notifications == null) {
    this.notifications = new ArrayList<Notification>();
    }
    
    notification.setClaim(this);
    notifications.add(notification);
    } else if (notification != null && isSameTypeOfNotificationExist(notification)) {
    Notification n = getSameTypeOfNotificationExist(notification);
    n.setMessage(notification.getMessage());
    }
    }
    
     */
    public void AddNotification(Notification notification) {

        if (notification != null) {

            if (this.notifications == null) {
                this.notifications = new ArrayList<Notification>();
            }

            notification.setClaim(this);
            notifications.add(notification);


        }
    }

    public void AcknowledgeNotifications(Notification notification) {


        if (notification != null) {
            //Notification n = getSameTypeOfNotificationExist(notification);
            notification.setIsacknowledged(true);
        }

        //notifications.add(id, notification);
        // notifications.remove(notification);
    }

    public void AcknowledgeAllNotifications() {
        List<Notification> toAcknowledgeList = new ArrayList<Notification>();
        for (Notification notification : notifications) {
            if (notification.getNotificationType().isInsurerType()) {
                toAcknowledgeList.add(notification);
            }
        }

        for (Notification obj : toAcknowledgeList) {
            //notifications.remove(obj);

            if (obj != null) {
                //Notification n = getSameTypeOfNotificationExist(obj);
                obj.setIsacknowledged(true);
            }
        }
    }

    private Notification getSameTypeOfNotificationExist(Notification notification) {

        if (this.notifications != null) {
            for (Notification n : notifications) {
                if (n.getType().equals(notification.getType())) {
                    return n;
                }
            }
        }

        return null;
    }

    private boolean isSameTypeOfNotificationExist(Notification notification) {

        if (this.notifications != null) {
            for (Notification n : notifications) {
                if (n.getType().equals(notification.getType())) {
                    return true;
                }
            }
        }

        return false;
    }

    public void removeAllInsurerNotifications() {
        List<Notification> toRemoveList = new ArrayList<Notification>();
        for (Notification notification : notifications) {
            if (notification.getNotificationType().isInsurerType()) {
                toRemoveList.add(notification);
            }
        }

        for (Notification obj : toRemoveList) {
            notifications.remove(obj);
        }
    }

    public void removeAllCHONotifications() {
        List<Notification> toRemoveList = new ArrayList<Notification>();
        for (Notification notification : notifications) {
            if (!notification.getNotificationType().isInsurerType()) {
                toRemoveList.add(notification);
            }
        }

        for (Notification obj : toRemoveList) {
            notifications.remove(obj);
        }
    }

    public void RemoveNotifications(Notification notification) {
        notifications.remove(notification);
    }

    private Notification GetNotificationByType(String type) {

        for (Notification notification : notifications) {

            if (notification.getType().equalsIgnoreCase(type)) {
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

    // <editor-fold defaultstate="collapsed" desc="BRE Properties ">
    public void setBreBand(BreBand choband) {
        this.choband = choband;
    }

    public BreBand getBreBand() {
        return choband;
    }

    /**
     * @return the percentageLiabilityCho
     */
    public BigDecimal getPercentageLiabilityCho() {
        return percentageLiabilityCho;
    }

    /**
     * @param percentageLiabilityCho the percentageLiabilityCho to set
     */
    public void setPercentageLiabilityCho(BigDecimal percentageLiabilityCho) {
        this.percentageLiabilityCho = percentageLiabilityCho;
    }

    /**
     * @return the liabilityAgreedDate
     */
    public Date getLiabilityAgreedDate() {
        return liabilityAgreedDate;
    }

    /**
     * @param liabilityAgreedDate the liabilityAgreedDate to set
     */
    public void setLiabilityAgreedDate(Date liabilityAgreedDate) {
        this.liabilityAgreedDate = liabilityAgreedDate;
    }

    /**
     * @return the liabilityStatus
     */
    public LiabilityStatus getLiabilityStatus() {
        return liabilityStatus;
    }

    /**
     * @param liabilityStatus the liabilityStatus to set
     */
    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }
    // </editor-fold>

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }
}
