package idas.chox.service.workflow.activities;

import idas.chox.core.enums.AuditReviewClaimType;
import idas.chox.core.enums.YesNoMapping;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimAuditReview;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveOrSubmitClaimAuditReview extends BaseActivity {

    static final Logger LOG = LoggerFactory.getLogger(SaveOrSubmitClaimAuditReview.class);

    private ClaimAuditReview claimAuditReview;
    @Autowired
    private VehicleClassService vehicleClassService;

    private String nameOfActivity;

    private Integer customerVehicleClassId;
    private Integer hireVehicleClassId;
    private Integer claimTypeId;
    private Integer totalLossId;
    private Integer hireDurationAcceptableId;
    private Integer repairCostExceedsEngRecId;
    private Integer withinABPGuidelinesId;
    private Integer storageClaimedId;
    private Integer recoveryClaimedId;
    private Integer hireLeakageId;
    private Integer penaltyChargeAvoidableId;
    private Integer storageClaimedCorrectlyId;
    private Integer recoveryClaimedCorrectlyId;

    private String whoManagedRepair;
    private String hireDurationNotAcceptableReason;
    private String penaltyChargeAvoidableNote;
    private Integer hireDuration;

    // custom AuditReviewBigDecimalConverter is implemented to 
    // avoid global bigdecimal converter changing null value to zero.
    private BigDecimal totalHireCost;
    private BigDecimal totalRepairCost;
    private BigDecimal penaltyChargesPaid;
    private BigDecimal hireLeakageCost;
    private BigDecimal exceededRepairCost;
    private BigDecimal nonABPGuidelineRepairLabourRate;

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setClaimAuditReview(ClaimAuditReview claimAuditReview) {
        this.claimAuditReview = claimAuditReview;
    }

    public String getNameOfActivity() {
        return nameOfActivity;
    }

    public void setNameOfActivity(String nameOfActivity) {
        this.nameOfActivity = nameOfActivity;
    }

    public Integer getCustomerVehicleClassId() {
        return customerVehicleClassId;
    }

    public void setCustomerVehicleClassId(Integer customerVehicleClassId) {
        if (claimAuditReview != null) {
            if (customerVehicleClassId != null) {
                claimAuditReview.setCustomerVehicleClass(this.vehicleClassService.getVehicleClass(customerVehicleClassId));
            } else if (claimAuditReview.getCustomerVehicleClass() != null) {
                claimAuditReview.setCustomerVehicleClass(null);
            }
        }
    }

    public Integer getHireVehicleClassId() {
        return hireVehicleClassId;
    }

    public void setHireVehicleClassId(Integer hireVehicleClassId) {
        if (claimAuditReview != null) {
            if (hireVehicleClassId != null) {
                claimAuditReview.setHireVehicleClass(this.vehicleClassService.getVehicleClass(hireVehicleClassId));
            } else if (claimAuditReview.getHireVehicleClass() != null) {
                claimAuditReview.setHireVehicleClass(null);
            }
        }
    }

    public Integer getClaimTypeId() {
        return claimTypeId;
    }

    public void setClaimTypeId(Integer claimTypeId) {
        if (claimAuditReview != null) {
            if (claimTypeId != null) {
                for (AuditReviewClaimType auditReviewClaimType : AuditReviewClaimType.values()) {
                    if (auditReviewClaimType.getValue().equals(claimTypeId)) {
                        claimAuditReview.setClaimType(auditReviewClaimType.getDescription());
                        break;
                    }
                }
            } else if (claimAuditReview.getClaimType() != null) {
                claimAuditReview.setClaimType(null);
            }
        }
    }

    public Integer getTotalLossId() {
        return totalLossId;
    }

    public void setTotalLossId(Integer totalLossId) {
        if (claimAuditReview != null) {
            if (totalLossId != null) {
                if (totalLossId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setTotalLoss(Boolean.TRUE);
                } else if (totalLossId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setTotalLoss(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setTotalLoss(null);
            }
        }
    }

    public Integer getHireDurationAcceptableId() {
        return hireDurationAcceptableId;
    }

    public void setHireDurationAcceptableId(Integer hireDurationAcceptableId) {
        if (claimAuditReview != null) {
            if (hireDurationAcceptableId != null) {
                if (hireDurationAcceptableId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setHireDurationAcceptable(Boolean.TRUE);
                } else if (hireDurationAcceptableId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setHireDurationAcceptable(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setHireDurationAcceptable(null);
            }
        }
    }

    public Integer getRepairCostExceedsEngRecId() {
        return repairCostExceedsEngRecId;
    }

    public void setRepairCostExceedsEngRecId(Integer repairCostExceedsEngRecId) {
        if (claimAuditReview != null) {
            if (repairCostExceedsEngRecId != null) {
                if (repairCostExceedsEngRecId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setRepairCostExceedsEngRec(Boolean.TRUE);
                } else if (repairCostExceedsEngRecId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setRepairCostExceedsEngRec(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setRepairCostExceedsEngRec(null);
            }
        }
    }

    public Integer getWithinABPGuidelinesId() {
        return withinABPGuidelinesId;
    }

    public void setWithinABPGuidelinesId(Integer withinABPGuidelinesId) {
        if (claimAuditReview != null) {
            if (withinABPGuidelinesId != null) {
                if (withinABPGuidelinesId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setWithinABPGuidelines(Boolean.TRUE);
                } else if (withinABPGuidelinesId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setWithinABPGuidelines(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setWithinABPGuidelines(null);
            }
        }
    }

    public Integer getStorageClaimedId() {
        return storageClaimedId;
    }

    public void setStorageClaimedId(Integer storageClaimedId) {
        if (claimAuditReview != null) {
            if (storageClaimedId != null) {
                if (storageClaimedId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setStorageClaimed(Boolean.TRUE);
                } else if (storageClaimedId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setStorageClaimed(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setStorageClaimed(null);
            }
        }
    }

    public Integer getRecoveryClaimedId() {
        return recoveryClaimedId;
    }

    public void setRecoveryClaimedId(Integer recoveryClaimedId) {
        if (claimAuditReview != null) {
            if (recoveryClaimedId != null) {
                if (recoveryClaimedId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setRecoveryClaimed(Boolean.TRUE);
                } else if (recoveryClaimedId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setRecoveryClaimed(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setRecoveryClaimed(null);
            }
        }
    }

    public Integer getHireLeakageId() {
        return hireLeakageId;
    }

    public void setHireLeakageId(Integer hireLeakageId) {
        if (claimAuditReview != null) {
            if (hireLeakageId != null) {
                if (hireLeakageId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setHireLeakage(Boolean.TRUE);
                } else if (hireLeakageId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setHireLeakage(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setHireLeakage(null);
            }
        }
    }

    public Integer getPenaltyChargeAvoidableId() {
        return penaltyChargeAvoidableId;
    }

    public void setPenaltyChargeAvoidableId(Integer penaltyChargeAvoidableId) {
        if (claimAuditReview != null) {
            if (penaltyChargeAvoidableId != null) {
                if (penaltyChargeAvoidableId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setPenaltyChargeAvoidable(Boolean.TRUE);
                } else if (penaltyChargeAvoidableId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setPenaltyChargeAvoidable(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setPenaltyChargeAvoidable(null);
            }
        }
    }

    public Integer getStorageClaimedCorrectlyId() {
        return storageClaimedCorrectlyId;
    }

    public void setStorageClaimedCorrectlyId(Integer storageClaimedCorrectlyId) {
        if (claimAuditReview != null) {
            if (storageClaimedCorrectlyId != null) {
                if (storageClaimedCorrectlyId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setStorageClaimedCorrectly(Boolean.TRUE);
                } else if (storageClaimedCorrectlyId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setStorageClaimedCorrectly(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setStorageClaimedCorrectly(null);
            }
        }
    }

    public Integer getRecoveryClaimedCorrectlyId() {
        return recoveryClaimedCorrectlyId;
    }

    public void setRecoveryClaimedCorrectlyId(Integer recoveryClaimedCorrectlyId) {
        if (claimAuditReview != null) {
            if (recoveryClaimedCorrectlyId != null) {
                if (recoveryClaimedCorrectlyId.equals(YesNoMapping.YES.getValue())) {
                    claimAuditReview.setRecoveryClaimedCorrectly(Boolean.TRUE);
                } else if (recoveryClaimedCorrectlyId.equals(YesNoMapping.NO.getValue())) {
                    claimAuditReview.setRecoveryClaimedCorrectly(Boolean.FALSE);
                }
            } else {
                claimAuditReview.setRecoveryClaimedCorrectly(null);
            }
        }
    }

    public String getWhoManagedRepair() {
        return whoManagedRepair;
    }

    public void setWhoManagedRepair(String whoManagedRepair) {
        if (claimAuditReview != null) {
            claimAuditReview.setWhoManagedRepair(whoManagedRepair);
        }
    }

    public String getHireDurationNotAcceptableReason() {
        return hireDurationNotAcceptableReason;
    }

    public void setHireDurationNotAcceptableReason(String hireDurationNotAcceptableReason) {
        if (claimAuditReview != null) {
            claimAuditReview.setHireDurationNotAcceptableReason(hireDurationNotAcceptableReason);
        }
    }

    public String getPenaltyChargeAvoidableNote() {
        return penaltyChargeAvoidableNote;
    }

    public void setPenaltyChargeAvoidableNote(String penaltyChargeAvoidableNote) {
        if (claimAuditReview != null) {
            claimAuditReview.setPenaltyChargeAvoidableNote(penaltyChargeAvoidableNote);
        }
    }

    public Integer getHireDuration() {
        return hireDuration;
    }

    public void setHireDuration(Integer hireDuration) {
        if (claimAuditReview != null) {
            claimAuditReview.setHireDuration(hireDuration);
        }
    }

    public BigDecimal getTotalHireCost() {
        return totalHireCost;
    }

    public void setTotalHireCost(BigDecimal totalHireCost) {
        if (claimAuditReview != null) {
            claimAuditReview.setTotalHireCost(totalHireCost);
        }
    }

    public BigDecimal getTotalRepairCost() {
        return totalRepairCost;
    }

    public void setTotalRepairCost(BigDecimal totalRepairCost) {
        if (claimAuditReview != null) {
            claimAuditReview.setTotalRepairCost(totalRepairCost);
        }
    }

    public BigDecimal getPenaltyChargesPaid() {
        return penaltyChargesPaid;
    }

    public void setPenaltyChargesPaid(BigDecimal penaltyChargesPaid) {
        if (claimAuditReview != null) {
            claimAuditReview.setPenaltyChargesPaid(penaltyChargesPaid);
        }
    }

    public BigDecimal getHireLeakageCost() {
        return hireLeakageCost;
    }

    public void setHireLeakageCost(BigDecimal hireLeakageCost) {
        if (claimAuditReview != null) {
            claimAuditReview.setHireLeakageCost(hireLeakageCost);
        }
    }

    public BigDecimal getExceededRepairCost() {
        return exceededRepairCost;
    }

    public void setExceededRepairCost(BigDecimal exceededRepairCost) {
        if (claimAuditReview != null) {
            claimAuditReview.setExceededRepairCost(exceededRepairCost);
        }
    }

    public BigDecimal getNonABPGuidelineRepairLabourRate() {
        return nonABPGuidelineRepairLabourRate;
    }

    public void setNonABPGuidelineRepairLabourRate(BigDecimal nonABPGuidelineRepairLabourRate) {
        if (claimAuditReview != null) {
            claimAuditReview.setNonABPGuidelineRepairLabourRate(nonABPGuidelineRepairLabourRate);
        }
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (claimAuditReview == null) {
            LOG.warn("Claim Audit Review Object is null. Can not update Audit Review.");
            throw new Exception("Claim Audit Review Object is null. Can not update Audit Review.");
        }
        switch (nameOfActivity) {
            case "submitClaimAuditReview":
                if (claimAuditReview.getClaimType() == null) {
                    LOG.warn("'Claim Type' is null. Can not update Audit Review.");
                    throw new Exception("'Claim Type' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getWhoManagedRepair() == null) {
                    LOG.warn("Who Managed Repair is null. Can not update Audit Review.");
                    throw new Exception("'Who Managed Repair?' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getTotalLoss() == null) {
                    LOG.warn("'Total Loss' is null. Can not update Audit Review.");
                    throw new Exception("'Total Loss' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getCustomerVehicleClass() == null) {
                    LOG.warn("'Customers Vehicle Class' is null. Can not update Audit Review.");
                    throw new Exception("'Customers Vehicle Class' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getHireVehicleClass() == null) {
                    LOG.warn("'Hire Vehicle Class' is null. Can not update Audit Review.");
                    throw new Exception("'Hire Vehicle Class' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getHireDuration() == null) {
                    LOG.warn("'Hire Duration' is null. Can not update Audit Review.");
                    throw new Exception("'Hire Duration' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getHireDurationAcceptable() == null) {
                    LOG.warn("'Hire Duration Acceptable' is null. Can not update Audit Review.");
                    throw new Exception("'Hire Duration Acceptable' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getHireDurationAcceptable() != null && !claimAuditReview.getHireDurationAcceptable()
                        && claimAuditReview.getHireDurationNotAcceptableReason() == null) {
                    LOG.warn("'Reason for Hire Duration Not Acceptable' is null. Can not update Audit Review.");
                    throw new Exception("'Reason for Hire Duration Not Acceptable' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getTotalHireCost() == null) {
                    LOG.warn("'Total Hire Costs' is null. Can not update Audit Review.");
                    throw new Exception("'Total Hire Costs' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getHireLeakage() == null) {
                    LOG.warn("'Hire Leakage' is null. Can not update Audit Review.");
                    throw new Exception("'Hire Leakage' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getHireLeakage() != null && claimAuditReview.getHireLeakage()
                        && (claimAuditReview.getHireLeakageCost() == null || claimAuditReview.getHireLeakageCost().compareTo(BigDecimal.ZERO) <= 0)) {
                    if (claimAuditReview.getHireLeakageCost() == null) {
                        LOG.warn("'If Yes, by how much' for 'Hire Leakage' is null. Can not update Audit Review.");
                        throw new Exception("'If Yes, by how much' for 'Hire Leakage' is null. Can not update Audit Review.");
                    } else {
                        LOG.warn("'If Yes, by how much' for 'Hire Leakage' Must Be Larger Than 0. Can not update Audit Review.");
                        throw new Exception("'If Yes, by how much' for 'Hire Leakage' Must Be Larger Than 0. Can not update Audit Review.");
                    }
                }
                if (claimAuditReview.getTotalRepairCost() == null) {
                    LOG.warn("'Total Repair Costs' is null. Can not update Audit Review.");
                    throw new Exception("'Total Repair Costs' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getRepairCostExceedsEngRec() != null && claimAuditReview.getRepairCostExceedsEngRec()
                        && (claimAuditReview.getExceededRepairCost() == null || claimAuditReview.getExceededRepairCost().compareTo(BigDecimal.ZERO) <= 0)) {
                    if (claimAuditReview.getExceededRepairCost() == null) {
                        LOG.warn("'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations is null. Can not update Audit Review.");
                        throw new Exception("'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations is null. Can not update Audit Review.");
                    } else {
                        LOG.warn("'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations Must Be Larger Than 0. Can not update Audit Review.");
                        throw new Exception("'If Yes, by how much?' for Repair Cost Exceeds Engineers Recommendations Must Be Larger Than 0. Can not update Audit Review.");
                    }
                }
                if (claimAuditReview.getPenaltyChargesPaid() == null) {
                    LOG.warn("'Penalty Charges paid' is null. Can not update Audit Review.");
                    throw new Exception("'Penalty Charges paid' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getPenaltyChargeAvoidable() != null && claimAuditReview.getPenaltyChargeAvoidable()
                        && claimAuditReview.getPenaltyChargeAvoidableNote() == null) {
                    LOG.warn("'How were the penalty charges avoidable' is null. Can not update Audit Review.");
                    throw new Exception("'How were the penalty charges avoidable' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getWithinABPGuidelines() != null && !claimAuditReview.getWithinABPGuidelines()
                        && (claimAuditReview.getNonABPGuidelineRepairLabourRate() == null || claimAuditReview.getNonABPGuidelineRepairLabourRate().compareTo(BigDecimal.ZERO) <= 0)) {
                    if (claimAuditReview.getNonABPGuidelineRepairLabourRate() == null) {
                        LOG.warn("'If No how much was charged (hourly rate)' for 'Repair labour rate' is null. Can not update Audit Review.");
                        throw new Exception("'If No how much was charged (hourly rate)' for 'Repair labour rate' is null. Can not update Audit Review.");
                    } else {
                        LOG.warn("'If No how much was charged (hourly rate)' for 'Repair labour rate' Must Be Larger Than 0. Can not update Audit Review.");
                        throw new Exception("'If No how much was charged (hourly rate)' for 'Repair labour rate' Must Be Larger Than 0. Can not update Audit Review.");
                    }
                }
                if (claimAuditReview.getStorageClaimed() == null) {
                    LOG.warn("'Storage Claimed' is null. Can not update Audit Review.");
                    throw new Exception("'Storage Claimed' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getStorageClaimed() != null && claimAuditReview.getStorageClaimed()
                        && claimAuditReview.getStorageClaimedCorrectly() == null) {
                    LOG.warn("'If Yes, correctly so' for 'Storage Claimed' is null. Can not update Audit Review.");
                    throw new Exception("'If Yes, correctly so' for 'Storage Claimed' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getRecoveryClaimed() == null) {
                    LOG.warn("'Recovery Claimed' is null. Can not update Audit Review.");
                    throw new Exception("'Recovery Claimed' is null. Can not update Audit Review.");
                }
                if (claimAuditReview.getRecoveryClaimed() != null && claimAuditReview.getRecoveryClaimed()
                        && claimAuditReview.getRecoveryClaimedCorrectly() == null) {
                    LOG.warn("'If Yes, correctly so' for 'Recovery Claimed' is null. Can not update Audit Review.");
                    throw new Exception("'If Yes, correctly so' for 'Recovery Claimed' is null. Can not update Audit Review.");
                }
                break;
            case "saveClaimAuditReview":
                LOG.debug("Save Claim Audit Review. No validation required!!!");
                break;
            default:
                LOG.error("Activity name does not match for claimAuditReview activity. Can not proceed to save or submit claimAuditReview.");
                throw new Exception("Activity name does not match. Can not update Audit Review.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        switch (nameOfActivity) {
            case "submitClaimAuditReview":
                claimAuditReview.setClaimAuditReviewCompleted(true);
                claimAuditReview.setAuditCompletedDate(new Date());
                claimAuditReview.setCompletedBy(getCurrentUser());
                LOG.debug("Submitted Claim Audit Review.");
                break;
            case "saveClaimAuditReview":
                LOG.debug("Saved Claim Audit Review.");
                break;
        }
    }

    @Override
    protected void afterProcess(Claim claim) {
        if (nameOfActivity.equals("submitClaimAuditReview")) {
//            activityEventGenerator.generate(claim, this);
            activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
                ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getMBassador().post(event).now();
            });
        }
    }
}
