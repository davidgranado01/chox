package idas.chox.service.workflow.activities;

import idas.chox.core.enums.AuditReviewClaimType;
import idas.chox.core.enums.YesNoMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimAuditReview;
import idas.chox.core.services.VehicleClassService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveClaimAuditReview extends BaseActivity {

    static final Logger LOG = LoggerFactory.getLogger(SaveClaimAuditReview.class);

    private ClaimAuditReview claimAuditReview;
    @Autowired
    private VehicleClassService vehicleClassService;

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
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Saved Claim Audit Review.");
    }

    @Override
    protected void afterProcess(Claim claim) {
//        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
//        getDataService().save(claim);
    }
}
