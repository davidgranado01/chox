package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.apache.commons.text.StringEscapeUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class ClaimAuditReview extends Entity implements Serializable {

    private String claimType;
    private String whoManagedRepair;
    private String hireDurationNotAcceptableReason;
    private String penaltyChargeAvoidableNote;
    private VehicleClass customerVehicleClass;
    private VehicleClass hireVehicleClass;
    private Boolean totalLoss;
    private Boolean hireDurationAcceptable;
    private Boolean repairCostExceedsEngRec;
    private Boolean withinABPGuidelines;
    private Boolean storageClaimed;
    private Boolean recoveryClaimed;
    private Boolean hireLeakage;
    private Boolean penaltyChargeAvoidable;
    private Boolean storageClaimedCorrectly;
    private Boolean recoveryClaimedCorrectly;
    private boolean claimAuditReviewCompleted;
    private Integer hireDuration;
    private BigDecimal totalHireCost;
    private BigDecimal totalRepairCost;
    private BigDecimal penaltyChargesPaid;
    private BigDecimal hireLeakageCost;
    private BigDecimal exceededRepairCost;
    private BigDecimal nonABPGuidelineRepairLabourRate;
    private Date auditCompletedDate;
    private WebUser completedBy;

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getWhoManagedRepair() {
        return whoManagedRepair;
    }

    public void setWhoManagedRepair(String whoManagedRepair) {
        this.whoManagedRepair = whoManagedRepair;
    }

    public String getHireDurationNotAcceptableReason() {
        return hireDurationNotAcceptableReason;
    }

    public void setHireDurationNotAcceptableReason(String hireDurationNotAcceptableReason) {
        this.hireDurationNotAcceptableReason = hireDurationNotAcceptableReason;
    }

    public String getPenaltyChargeAvoidableNote() {
        return penaltyChargeAvoidableNote;
    }

    public void setPenaltyChargeAvoidableNote(String penaltyChargeAvoidableNote) {
        if (penaltyChargeAvoidableNote != null) {
            this.penaltyChargeAvoidableNote = StringEscapeUtils.unescapeHtml4(Jsoup.clean(penaltyChargeAvoidableNote, "", Whitelist.basic(), new Document.OutputSettings().prettyPrint(false)));
        } else {
            this.penaltyChargeAvoidableNote = penaltyChargeAvoidableNote;
        }
    }

    public VehicleClass getCustomerVehicleClass() {
        return customerVehicleClass;
    }

    public void setCustomerVehicleClass(VehicleClass customerVehicleClass) {
        this.customerVehicleClass = customerVehicleClass;
    }

    public VehicleClass getHireVehicleClass() {
        return hireVehicleClass;
    }

    public void setHireVehicleClass(VehicleClass hireVehicleClass) {
        this.hireVehicleClass = hireVehicleClass;
    }

    public Boolean getTotalLoss() {
        return totalLoss;
    }

    public void setTotalLoss(Boolean totalLoss) {
        this.totalLoss = totalLoss;
    }

    public Boolean getHireDurationAcceptable() {
        return hireDurationAcceptable;
    }

    public void setHireDurationAcceptable(Boolean hireDurationAcceptable) {
        this.hireDurationAcceptable = hireDurationAcceptable;
    }

    public Boolean getRepairCostExceedsEngRec() {
        return repairCostExceedsEngRec;
    }

    public void setRepairCostExceedsEngRec(Boolean repairCostExceedsEngRec) {
        this.repairCostExceedsEngRec = repairCostExceedsEngRec;
    }

    public Boolean getWithinABPGuidelines() {
        return withinABPGuidelines;
    }

    public void setWithinABPGuidelines(Boolean withinABPGuidelines) {
        this.withinABPGuidelines = withinABPGuidelines;
    }

    public Boolean getStorageClaimed() {
        return storageClaimed;
    }

    public void setStorageClaimed(Boolean storageClaimed) {
        this.storageClaimed = storageClaimed;
    }

    public Boolean getRecoveryClaimed() {
        return recoveryClaimed;
    }

    public void setRecoveryClaimed(Boolean recoveryClaimed) {
        this.recoveryClaimed = recoveryClaimed;
    }

    public Boolean getHireLeakage() {
        return hireLeakage;
    }

    public void setHireLeakage(Boolean hireLeakage) {
        this.hireLeakage = hireLeakage;
    }

    public Boolean getPenaltyChargeAvoidable() {
        return penaltyChargeAvoidable;
    }

    public void setPenaltyChargeAvoidable(Boolean penaltyChargeAvoidable) {
        this.penaltyChargeAvoidable = penaltyChargeAvoidable;
    }

    public Boolean getStorageClaimedCorrectly() {
        return storageClaimedCorrectly;
    }

    public void setStorageClaimedCorrectly(Boolean storageClaimedCorrectly) {
        this.storageClaimedCorrectly = storageClaimedCorrectly;
    }

    public Boolean getRecoveryClaimedCorrectly() {
        return recoveryClaimedCorrectly;
    }

    public void setRecoveryClaimedCorrectly(Boolean recoveryClaimedCorrectly) {
        this.recoveryClaimedCorrectly = recoveryClaimedCorrectly;
    }

    public boolean isClaimAuditReviewCompleted() {
        return claimAuditReviewCompleted;
    }

    public void setClaimAuditReviewCompleted(boolean claimAuditReviewCompleted) {
        this.claimAuditReviewCompleted = claimAuditReviewCompleted;
    }

    public Integer getHireDuration() {
        return hireDuration;
    }

    public void setHireDuration(Integer hireDuration) {
        this.hireDuration = hireDuration;
    }

    public BigDecimal getTotalHireCost() {
        return totalHireCost;
    }

    public void setTotalHireCost(BigDecimal totalHireCost) {
        this.totalHireCost = totalHireCost;
    }

    public BigDecimal getTotalRepairCost() {
        return totalRepairCost;
    }

    public void setTotalRepairCost(BigDecimal totalRepairCost) {
        this.totalRepairCost = totalRepairCost;
    }

    public BigDecimal getPenaltyChargesPaid() {
        return penaltyChargesPaid;
    }

    public void setPenaltyChargesPaid(BigDecimal penaltyChargesPaid) {
        this.penaltyChargesPaid = penaltyChargesPaid;
    }

    public BigDecimal getHireLeakageCost() {
        return hireLeakageCost;
    }

    public void setHireLeakageCost(BigDecimal hireLeakageCost) {
        this.hireLeakageCost = hireLeakageCost;
    }

    public BigDecimal getExceededRepairCost() {
        return exceededRepairCost;
    }

    public void setExceededRepairCost(BigDecimal exceededRepairCost) {
        this.exceededRepairCost = exceededRepairCost;
    }

    public BigDecimal getNonABPGuidelineRepairLabourRate() {
        return nonABPGuidelineRepairLabourRate;
    }

    public void setNonABPGuidelineRepairLabourRate(BigDecimal nonABPGuidelineRepairLabourRate) {
        this.nonABPGuidelineRepairLabourRate = nonABPGuidelineRepairLabourRate;
    }

    public Date getAuditCompletedDate() {
        return auditCompletedDate;
    }

    public void setAuditCompletedDate(Date auditCompletedDate) {
        this.auditCompletedDate = auditCompletedDate;
    }

    public WebUser getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(WebUser completedBy) {
        this.completedBy = completedBy;
    }
}
