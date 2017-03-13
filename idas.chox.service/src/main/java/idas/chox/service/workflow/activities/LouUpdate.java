package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.NotificationService;
import idas.chox.data.notifications.HireUpdatedNotification;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;


public class LouUpdate extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(LouUpdate.class);
    private NotificationService notificationService;
    private String repairerName;
    private Date inspectionBookedDate;
    private Date inspectionDate;
    private Date repairAuthorisedDate;
    private Date repairBookedInDate;
    private Date repairCommencedDate;
    private Date repairCompletionDate;
    private Boolean totalLoss;
    private Date totalLossMadeDate;
    private Date totalLossAcceptedDate;
    private Date totalLossIssuedDate;
    private Date totalLossReceivedDate;
    private String imeName;
    private BigDecimal labourRate;
    private BigDecimal labourHours;
    private BigDecimal labourCost;
    private String nonProvisionReason;
    private Boolean repairOnly;
    private Boolean choManagingRepair;
    private Boolean insurerManagingRepair;
    private Boolean vatRegistered;
    private boolean updateInsurer;
    
    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public String getRepairerName() {
        return repairerName;
    }

    public void setRepairerName(String repairerName) {
        this.repairerName = repairerName;
    }

    public Date getInspectionBookedDate() {
        return inspectionBookedDate;
    }

    public void setInspectionBookedDate(Date inspectionBookedDate) {
        this.inspectionBookedDate = inspectionBookedDate;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public Date getRepairAuthorisedDate() {
        return repairAuthorisedDate;
    }

    public void setRepairAuthorisedDate(Date repairAuthorisedDate) {
        this.repairAuthorisedDate = repairAuthorisedDate;
    }

    public Date getRepairBookedInDate() {
        return repairBookedInDate;
    }

    public void setRepairBookedInDate(Date repairBookedInDate) {
        this.repairBookedInDate = repairBookedInDate;
    }

    public Date getRepairCommencedDate() {
        return repairCommencedDate;
    }

    public void setRepairCommencedDate(Date repairCommencedDate) {
        this.repairCommencedDate = repairCommencedDate;
    }

    public Date getRepairCompletionDate() {
        return repairCompletionDate;
    }

    public void setRepairCompletionDate(Date repairCompletionDate) {
        this.repairCompletionDate = repairCompletionDate;
    }

    public Boolean getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(Boolean totalLoss) {
        this.totalLoss = totalLoss;
    }

    public Date getTotalLossMadeDate() {
        return totalLossMadeDate;
    }

    public void setTotalLossMadeDate(Date totalLossMadeDate) {
        this.totalLossMadeDate = totalLossMadeDate;
    }

    public Date getTotalLossAcceptedDate() {
        return totalLossAcceptedDate;
    }

    public void setTotalLossAcceptedDate(Date totalLossAcceptedDate) {
        this.totalLossAcceptedDate = totalLossAcceptedDate;
    }

    public Date getTotalLossIssuedDate() {
        return totalLossIssuedDate;
    }

    public void setTotalLossIssuedDate(Date totalLossIssuedDate) {
        this.totalLossIssuedDate = totalLossIssuedDate;
    }

    public Date getTotalLossReceivedDate() {
        return totalLossReceivedDate;
    }

    public void setTotalLossReceivedDate(Date totalLossReceivedDate) {
        this.totalLossReceivedDate = totalLossReceivedDate;
    }

    public String getImeName() {
        return imeName;
    }

    public void setImeName(String imeName) {
        this.imeName = imeName;
    }

    public BigDecimal getLabourRate() {
        return labourRate;
    }

    public void setLabourRate(BigDecimal labourRate) {
        this.labourRate = labourRate;
    }

    public BigDecimal getLabourHours() {
        return labourHours;
    }

    public void setLabourHours(BigDecimal labourHours) {
        this.labourHours = labourHours;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public String getNonProvisionReason() {
        return nonProvisionReason;
    }

    public void setNonProvisionReason(String nonProvisionReason) {
        this.nonProvisionReason = nonProvisionReason;
    }

    public Boolean getRepairOnly() {
        return repairOnly;
    }

    public void setRepairOnly(Boolean repairOnly) {
        this.repairOnly = repairOnly;
    }

    public Boolean getChoManagingRepair() {
        return choManagingRepair;
    }

    public void setChoManagingRepair(Boolean choManagingRepair) {
        this.choManagingRepair = choManagingRepair;
    }

    public Boolean getInsurerManagingRepair() {
        return insurerManagingRepair;
    }

    public void setInsurerManagingRepair(Boolean insurerManagingRepair) {
        this.insurerManagingRepair = insurerManagingRepair;
    }

    public Boolean getVatRegistered() {
        return vatRegistered;
    }

    public void setVatRegistered(Boolean vatRegistered) {
        this.vatRegistered = vatRegistered;
    }



    public boolean isUpdateInsurer() {
        return updateInsurer;
    }

    public void setUpdateInsurer(boolean updateInsurer) {
        this.updateInsurer = updateInsurer;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (repairerName != null) {
            String repairerNameClean = Jsoup.clean(repairerName, Whitelist.basic());
            if (!repairerNameClean.equals(repairerName)) {
                LOG.warn("Repairer name contains forbidden content - possible XSS attack: '{}'!='{}'", repairerName, repairerNameClean);
                repairerName = repairerNameClean;
//                throw new Exception("Repairer name contains forbidden content");
            }
        }
        if (imeName != null) {
            String imeNameClean = Jsoup.clean(imeName, Whitelist.basic());
            if (!imeNameClean.equals(imeName)) {
                LOG.warn("IME Name contains forbidden content - possible XSS attack: '{}'!='{}'", imeName, imeNameClean);
                imeName = imeNameClean;
//                throw new Exception("Name of IME contains forbidden content");
            }
        }
        if (nonProvisionReason != null) {
            String nonProvisionReasonClean = Jsoup.clean(nonProvisionReason, Whitelist.basic());
            if (!nonProvisionReasonClean.equals(nonProvisionReason)) {
                LOG.warn("Non-provision reason contains forbidden content - possible XSS attack: '{}'!='{}'", nonProvisionReason, nonProvisionReasonClean);
                nonProvisionReason = nonProvisionReasonClean;
//                throw new Exception("Non-Provision Reason contains forbidden content");
            }
        }

    }

    @Override
    protected void doProcess(Claim claim) {

        HireMonitoringDetail hmd = claim.getHireMonitoringDetail();
        if (hmd == null) {
            hmd = new HireMonitoringDetail();
            hmd.setClaim(claim);
            claim.setHireMonitoringDetail(hmd);
        }

        if (repairerName != null) {
            hmd.setNameOfRepairer(repairerName);
        }
        if (inspectionBookedDate != null && (hmd.getInspectionBookedDate() == null
                        || inspectionBookedDate.compareTo(hmd.getInspectionBookedDate()) != 0)) {
            hmd.setInspectionBookedDate(inspectionBookedDate);
            hmd.setInspectionBookedDateLastModified(new Date());
        }
        if (inspectionDate != null && (hmd.getInspectionDate() == null
                        || inspectionDate.compareTo(hmd.getInspectionDate()) != 0)) {
            hmd.setInspectionDate(inspectionDate);
            hmd.setInspectionDateLastModified(new Date());
        }
        if (repairAuthorisedDate != null && (hmd.getRepairAuthorisedDate() == null
                        || repairAuthorisedDate.compareTo(hmd.getRepairAuthorisedDate()) != 0)) {
            hmd.setRepairAuthorisedDate(repairAuthorisedDate);
            hmd.setRepairAuthorisedDateLastModified(new Date());
        }
        if (repairBookedInDate != null && (hmd.getRepairBookInDate() == null
                        || repairBookedInDate.compareTo(hmd.getRepairBookInDate()) != 0)) {
            if (hmd.getOriginalRepairBookInDate() == null && hmd.getRepairBookInDate() != null) {
                hmd.setOriginalRepairBookInDate(hmd.getRepairBookInDate());
            }
            hmd.setRepairBookInDate(repairBookedInDate);
            hmd.setRepairBookInDateLastModified(new Date());
            claimService.checkRepairBookedInDateAnomaly(claim);
        }
        if (repairCommencedDate != null && (hmd.getRepairCommencedDate() == null
                        || repairCommencedDate.compareTo(hmd.getRepairCommencedDate()) != 0)) {
            hmd.setRepairCommencedDate(repairCommencedDate);
            hmd.setRepairCommencedDateLastModified(new Date());
        }
        if (repairCompletionDate != null && (hmd.getRepairCompletionDate() == null
                        || repairCompletionDate.compareTo(hmd.getRepairCompletionDate()) != 0)) {
            hmd.setRepairCompletionDate(repairCompletionDate);
            hmd.setRepairCompletionDateLastModified(new Date());
        }
        if (totalLoss != null && hmd.isIsTotalLostCheck() != totalLoss) {
            hmd.setIsTotalLostCheck(totalLoss);
            hmd.setIsTotalLostCheckLastModified(new Date());
        }
        if (totalLossMadeDate != null && (hmd.getTotalLossOfferMadeDate() == null
                        || totalLossMadeDate.compareTo(hmd.getTotalLossOfferMadeDate()) != 0)) {
            hmd.setTotalLossOfferMadeDate(totalLossMadeDate);
            hmd.setTotalLossOfferMadeLastModified(new Date());
        }
        if (totalLossAcceptedDate != null && (hmd.getTotalLossOfferAcceptedDate() == null
                        || totalLossAcceptedDate.compareTo(hmd.getTotalLossOfferAcceptedDate()) != 0)) {
            hmd.setTotalLossOfferAcceptedDate(totalLossAcceptedDate);
            hmd.setTotalLossOfferAcceptedLastModified(new Date());
        }
        if (totalLossIssuedDate != null && (hmd.getTotalLossOfferCheckIssuedDate() == null
                        || totalLossIssuedDate.compareTo(hmd.getTotalLossOfferCheckIssuedDate()) != 0)) {
            hmd.setTotalLossOfferCheckIssuedDate(totalLossIssuedDate);
            hmd.setTotalLossCheckIssuedLastModified(new Date());
        }
        if (totalLossReceivedDate != null && (hmd.getTotalLossOfferCheckReceivedDate() == null
                        || totalLossReceivedDate.compareTo(hmd.getTotalLossOfferCheckReceivedDate()) != 0)) {
            hmd.setTotalLossOfferCheckReceivedDate(totalLossReceivedDate);
            hmd.setTotalLossCheckReceivedLastModified(new Date());
        }
        if (imeName != null) {
            hmd.setNameOfIme(imeName);
        }
        if (labourRate != null) {
            hmd.setLabourRate(labourRate);
        }
        if (labourHours != null) {
            hmd.setLabourHour(labourHours);
        }
        if (labourCost != null) {
            hmd.setLabourCost(labourCost);
        }
        if (nonProvisionReason != null) {
            hmd.setNonProvisionReason(nonProvisionReason);
        }
        if (repairOnly != null) {
            hmd.setIsRepairOnlyCheck(repairOnly);
        }
        if (choManagingRepair != null) {
            if (claim.getManagingRepair() != choManagingRepair) {
                if (claim.getManagingRepairOriginal() == null) {
                    claim.setManagingRepairOriginal(claim.getManagingRepair());
                }
                claim.setManagingRepair(choManagingRepair);
                claim.setManagingRepairLastModified(new Date());
            }
        }
        if (insurerManagingRepair != null && insurerManagingRepair != hmd.isIsNFInsurerManagingRepair()) {
            hmd.setIsNFInsurerManagingRepair(insurerManagingRepair);
            hmd.setIsNFInsurerManagingRepairLastModified(new Date());
        }
        if (vatRegistered != null && !Objects.equals(vatRegistered, hmd.getClientVatRegistered())) {
            hmd.setClientVatRegistered(vatRegistered);
            hmd.setClientVatRegisteredLastModified(new Date());
        }
       
    }

    @Override
    protected void afterProcess(Claim claim) {
        getDataService().save(claim);
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });
        if (updateInsurer || claim.getInsurer().isAllowDefaultHMUpdates()) {
                notificationService.addNotification(claim, new HireUpdatedNotification());
        }
    }
}