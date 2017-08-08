package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.util.DateHelper;

public class Claim extends Entity implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Claim.class);
    private Boolean managingRepairOriginal;
    private boolean managingRepair;
    private boolean manualInvoiceApproved;
    private boolean finalReviewCho;
    private boolean finalReviewIns;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private boolean isFnolReviewed;
    private boolean autoPenaltyChargeEnabled;
    private boolean totalLossChase;
    private boolean paymentDispute;
    private boolean caseWithClientsSolicitor;
    private int slaExtDays;
    private int noAttachments;
    private Integer remainingSlaDaysInt;
    private Date policyHolderContactDate;
    private Date creditAgreementDate;
    private Date gtaNoticeDate;
    private Date statusModifiedDate;
    private Date finalReviewDateCho;
    private Date finalReviewDateIns;
    private Date liabilityAgreedDate;
    private Date liabilityStatusModifiedDate;
    private Date liabilityModifiedDate;
    private Date managingRepairLastModified;
    private Date dateMarkedWithSolicitor;
    private Date lastReviewDate;
    private String choReference;
    private String status;
    private String claimNumber;
    private String tpiClaimStatus;
    private String previousStatus;
    private String remainingSlaDays;
    private String userMarkedWithSolicitor;
    private String indemnityStance;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private BigDecimal percentageLiabilityCho;
    private WebUser claimOwner;
    private WebUser claimOwnerOriginal;
    private WebUser supplierClaimOwner;
    private WebUser finalReviewByCho;
    private WebUser finalReviewByIns;
    private BreBand choband;
    private LiabilityStatus liabilityStatus;
    private ClaimType claimType;
    private Insurer insurer;
    private Chorganisation chorganisation;
    private Customer customer;
    private ClaimAuditReview claimAuditReview;
    private Incident incident;
    private Invoice invoice;
    private ThirdParty thirdParty;
    private VehicleHire vehicleHire;
    private InsurerVehicleHire insurerVehicleHire;
    private EngineerReport engineerReport;
    private HireMonitoringDetail hireMonitoringDetail;
    private InsurerHireMonitoringDetail insurerHireMonitoringDetail;
    private ReasonOfRejection reasonOfRejection;
    private Workgroup workgroup;
    private Workgroup workgroupOriginal;
    private List<HireMonitoringEcd> hireMonitoringEcds;
    private List<InsurerHireMonitoringEcd> insurerHireMonitoringEcds;
    private List<Attachment> attachments;
    private List<History> histories;
    private List<Comment> comments;
    private List<Notification> notifications;
    private int fraudCheckStatus;
    private int matchStatus;
    private boolean sentToKeoghs;
    private boolean fraudResultAcknowledged;
    private KeoghsRequest keoghsRequest;

    public Claim() {
        this.liabilityStatus = LiabilityStatus.LIABILITY_NULL;
        this.claimType = ClaimType.GTA;
        this.autoPenaltyChargeEnabled = true;
        this.noAttachments = 0;
        this.matchStatus = 0;
    }

    public int getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(int matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getIndemnityStance() {
        return indemnityStance;
    }

    public void setIndemnityStance(String indemnityStance) {
        this.indemnityStance = indemnityStance;
    }

    public boolean isSentToKeoghs() {
        return sentToKeoghs;
    }

    public void setSentToKeoghs(boolean sentToKeoghs) {
        this.sentToKeoghs = sentToKeoghs;
    }

    public boolean isFraudResultAcknowledged() {
        return fraudResultAcknowledged;
    }

    public void setFraudResultAcknowledged(boolean fraudResultAcknowledged) {
        this.fraudResultAcknowledged = fraudResultAcknowledged;
    }

    public int getFraudCheckStatus() {
        return fraudCheckStatus;
    }

    public void setFraudCheckStatus(int fraudCheckStatus) {
        this.fraudCheckStatus = fraudCheckStatus;
    }

    public KeoghsRequest getKeoghsRequest() {
        return keoghsRequest;
    }

    public void setKeoghsRequest(KeoghsRequest keoghsRequest) {
        this.keoghsRequest = keoghsRequest;
    }

    public boolean isCaseWithClientsSolicitor() {
        return caseWithClientsSolicitor;
    }

    public void setCaseWithClientsSolicitor(boolean caseWithClientsSolicitor) {
        this.caseWithClientsSolicitor = caseWithClientsSolicitor;
    }

    public String getDateMarkedWithSolicitorAsString() {
        return DateHelper.getLocalDateTimeFormat().format(dateMarkedWithSolicitor);
    }

    public Date getDateMarkedWithSolicitor() {
        return dateMarkedWithSolicitor;
    }

    public void setDateMarkedWithSolicitor(Date dateMarkedWithSolicitor) {
        this.dateMarkedWithSolicitor = dateMarkedWithSolicitor;
    }

    public String getUserMarkedWithSolicitor() {
        return userMarkedWithSolicitor;
    }

    public void setUserMarkedWithSolicitor(String userMarkedWithSolicitor) {
        this.userMarkedWithSolicitor = userMarkedWithSolicitor;
    }

    public Date getLiabilityStatusModifiedDate() {
        return liabilityStatusModifiedDate;
    }

    public Date getLiabilityModifiedDate() {
        return liabilityModifiedDate;
    }

    public void setLiabilityStatusModifiedDate(Date liabilityStatusModifiedDate) {
        this.liabilityStatusModifiedDate = liabilityStatusModifiedDate;
    }

    public void setLiabilityModifiedDate(Date liabilityModifiedDate) {
        this.liabilityModifiedDate = liabilityModifiedDate;
    }

    public int getNoAttachments() {
        return noAttachments;
    }

    public void setNoAttachments(int noAttachments) {
        this.noAttachments = noAttachments;
    }

    public int getSlaExtDays() {
        return slaExtDays;
    }

    public void setSlaExtDays(int slaExtDays) {
        this.slaExtDays = slaExtDays;
    }

    public boolean isAutoPenaltyChargeEnabled() {
        return autoPenaltyChargeEnabled;
    }

    public void setAutoPenaltyChargeEnabled(boolean autoPenaltyChargeEnabled) {
        this.autoPenaltyChargeEnabled = autoPenaltyChargeEnabled;
    }

    public boolean isTotalLossChase() {
        return totalLossChase;
    }

    public void setTotalLossChase(boolean totalLossChase) {
        this.totalLossChase = totalLossChase;
    }

    @Deprecated
    public boolean isTpiClaim() {
        return ClaimType.isTPI(getClaimType());
    }

    public String getTpiClaimStatus() {
        return tpiClaimStatus;
    }

    public void setTpiClaimStatus(String tpiClaimStatus) {
        this.tpiClaimStatus = tpiClaimStatus;
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
        if (customer != null) {
            customer.setClaim(this);
        }
        this.customer = customer;
    }

    public ClaimAuditReview getClaimAuditReview() {
        return claimAuditReview;
    }

    public void setClaimAuditReview(ClaimAuditReview claimAuditReview) {
        this.claimAuditReview = claimAuditReview;
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
        if (hireMonitoringDetail != null) {
            hireMonitoringDetail.setClaim(this);
        }
        this.hireMonitoringDetail = hireMonitoringDetail;
    }

    public InsurerHireMonitoringDetail getInsurerHireMonitoringDetail() {
        return insurerHireMonitoringDetail;
    }

    public void setInsurerHireMonitoringDetail(InsurerHireMonitoringDetail insurerHireMonitoringDetail) {
        if (insurerHireMonitoringDetail != null) {
            insurerHireMonitoringDetail.setClaim(this);
        }
        this.insurerHireMonitoringDetail = insurerHireMonitoringDetail;
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

    public InsurerVehicleHire getInsurerVehicleHire() {
        return insurerVehicleHire;
    }

    public void setInsurerVehicleHire(InsurerVehicleHire insurerVehicleHire) {
        this.insurerVehicleHire = insurerVehicleHire;
    }

    public boolean getManagingRepair() {
        return this.managingRepair;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        if (claimNumber != null) {
            this.claimNumber = claimNumber.trim();
        } else {
            this.claimNumber = claimNumber;
        }
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

    public String getIsManagingRepairDesc() {
        return managingRepair ? "Yes" : "No";
    }

    public String getIsInvoiceReviewRequiredDesc() {
        return isInvoiceReviewRequired ? "Yes" : "No";
    }

    public String getIsQuantumDisputeDesc() {
        return isQuantumDispute ? "Yes" : "No";
    }

    public Long getDaysInStatus() {
        Date now = new Date();
        Date lastStatusModified = this.getStatusModifiedDate();

        return DateHelper.getNumberOf24HourPeriodsBetween(lastStatusModified, now);
    }

    public long getLiabilityAgreedDays() {

        return DateHelper.getNumberOf24HourPeriodsBetween(getLiabilityAgreedDate(), new Date()) + 1;
    }

    public List<HireMonitoringEcd> getHireMonitoringEcds() {
        if (hireMonitoringEcds == null) {
            hireMonitoringEcds = new ArrayList<>();
        }
        return hireMonitoringEcds;
    }

    public List<Notification> getNotifications() {
        if (notifications == null) {
            notifications = new ArrayList<>();
        }
        return notifications;
    }

    public List<Notification> addNotification(Notification notification) {
        notification.setClaim(this);
        getNotifications().add(notification);
        return notifications;
    }

    public List<InsurerHireMonitoringEcd> getInsurerHireMonitoringEcds() {
        if (insurerHireMonitoringEcds == null) {
            insurerHireMonitoringEcds = new ArrayList<>();
        }
        return insurerHireMonitoringEcds;
    }

    public void setHireMonitoringEcds(List<HireMonitoringEcd> hireMonitoringEcds) {
        this.hireMonitoringEcds = hireMonitoringEcds;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addHireMonitoringEcd(HireMonitoringEcd ecd) {
        if (hireMonitoringEcds == null) {
            hireMonitoringEcds = new ArrayList<>();
        }
        ecd.setClaim(this);
        hireMonitoringEcds.add(ecd);
        LOG.debug("Hire Monitoring ECD added: {}", ecd.getEcdDate());
    }

    public void setInsurerHireMonitoringEcds(List<InsurerHireMonitoringEcd> insurerHireMonitoringEcds) {
        this.insurerHireMonitoringEcds = insurerHireMonitoringEcds;
    }

    public void addInsurerHireMonitoringEcd(InsurerHireMonitoringEcd ecd) {
        if (insurerHireMonitoringEcds == null) {
            insurerHireMonitoringEcds = new ArrayList<>();
        }
        ecd.setClaim(this);
        insurerHireMonitoringEcds.add(ecd);
        LOG.debug("Insurer Hire Monitoring ECD added: {}", ecd.getEcdDate());
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public void addAttachment(Attachment attachment) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }

        attachment.setClaim(this);
        attachments.add(attachment);
    }

    public void deleteAttachment(Attachment attachment) {
        attachments.remove(attachment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }

        comment.setClaim(this);
        comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        comments.remove(comment);
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public void addHistory(History history) {

        if (histories == null) {
            histories = new ArrayList<>();
        }

        history.setClaim(this);
        histories.add(history);
    }

    public void addHistories(List<History> histories) {

        if (this.histories == null) {
            this.histories = new ArrayList<>();
        }

        for (History history : histories) {
            history.setClaim(this);
            this.histories.add(history);
        }
    }

    public WebUser getClaimOwner() {
        return claimOwner;
    }

    public void setClaimOwner(WebUser claimOwner) {
        this.claimOwner = claimOwner;
    }

    public String getRemainingSlaDays() {
        return remainingSlaDays;
    }

    public void setRemainingSlaDays(String remainingSlaDays) {
        this.remainingSlaDays = remainingSlaDays;
    }

    public Integer getRemainingSlaDaysInt() {
        return remainingSlaDaysInt;
    }

    public void setRemainingSlaDaysInt(Integer remainingSlaDaysInt) {
        this.remainingSlaDaysInt = remainingSlaDaysInt;
    }

    public boolean isPaymentDispute() {
        return paymentDispute;
    }

    public void setPaymentDispute(boolean paymentDispute) {
        this.paymentDispute = paymentDispute;
    }

    public Date getLastReviewDate() {
        return lastReviewDate;
    }

    public void setLastReviewDate(Date lastReviewDate) {
        this.lastReviewDate = lastReviewDate;
    }

    
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

    public void setLiabilityPercentages(BigDecimal insurerLiability, BigDecimal choLiability) {
        if (((choLiability != null && (this.percentageLiabilityCho == null || this.percentageLiabilityCho.compareTo(choLiability) != 0))
                || (choLiability == null && this.percentageLiabilityCho != null))
                || ((insurerLiability != null && (this.percentageLiabilityAccepted == null || this.percentageLiabilityAccepted.compareTo(insurerLiability) != 0))
                || (insurerLiability == null && this.percentageLiabilityAccepted != null))) {
            this.percentageLiabilityCho = choLiability;
            this.percentageLiabilityAccepted = insurerLiability;
            this.liabilityModifiedDate = new Date();
        }
    }
    

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
    public void setLiability(LiabilityStatus liabilityStatus) {
        if (this.liabilityStatus != liabilityStatus) {
            this.liabilityStatusModifiedDate = new Date();
            this.liabilityStatus = liabilityStatus;
        }
    }
    protected void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public boolean isManualInvoiceApproved() {
        return manualInvoiceApproved;
    }

    public void setManualInvoiceApproved(boolean manualInvoiceApproved) {
        this.manualInvoiceApproved = manualInvoiceApproved;
    }

    public boolean isFinalReviewCho() {
        return finalReviewCho;
    }

    public void setFinalReviewCho(boolean finalReviewCho) {
        this.finalReviewCho = finalReviewCho;
    }

    public boolean isFinalReviewIns() {
        return finalReviewIns;
    }

    public void setFinalReviewIns(boolean finalReviewIns) {
        this.finalReviewIns = finalReviewIns;
    }

    public WebUser getFinalReviewByCho() {
        return finalReviewByCho;
    }

    public void setFinalReviewByCho(WebUser finalReviewByCho) {
        this.finalReviewByCho = finalReviewByCho;
    }

    public WebUser getFinalReviewByIns() {
        return finalReviewByIns;
    }

    public void setFinalReviewByIns(WebUser finalReviewByIns) {
        this.finalReviewByIns = finalReviewByIns;
    }

    public Date getFinalReviewDateCho() {
        return finalReviewDateCho;
    }

    public void setFinalReviewDateCho(Date finalReviewDateCho) {
        this.finalReviewDateCho = finalReviewDateCho;
    }

    public Date getFinalReviewDateIns() {
        return finalReviewDateIns;
    }

    public void setFinalReviewDateIns(Date finalReviewDateIns) {
        this.finalReviewDateIns = finalReviewDateIns;
    }

    public Date getManagingRepairLastModified() {
        return managingRepairLastModified;
    }

    public void setManagingRepairLastModified(Date managingRepairLastModified) {
        this.managingRepairLastModified = managingRepairLastModified;
    }

    public Boolean getManagingRepairOriginal() {
        return managingRepairOriginal;
    }

    public void setManagingRepairOriginal(Boolean managingRepairOriginal) {
        this.managingRepairOriginal = managingRepairOriginal;
    }
    
    public String getManagingRepairOriginalDesc() {
        if (managingRepairOriginal == null) {
            return "";
        }
        else {
            return managingRepairOriginal ? "(Yes)" : "(No)";
        }
    }
    // </editor-fold>
}
