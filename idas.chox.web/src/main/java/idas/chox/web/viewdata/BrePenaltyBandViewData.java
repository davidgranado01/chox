package idas.chox.web.viewdata;

import java.math.BigDecimal;

import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author John
 */
public class BrePenaltyBandViewData {
    private int id;
    private int claimTypeId;
    private String claimTypeName;
    private String penaltyBandStartDate;
    private BigDecimal hire30DayRate;
    private BigDecimal hire60DayRate;
    private BigDecimal hire90DayRate;
    private boolean hireApply90DayRate;
    private String hireApply90DayRateDesc;
    private boolean hireUseCommercial;
    private String hireUseCommercialDesc;
    private BigDecimal repair30DayRate;
    private BigDecimal repair60DayRate;
    private BigDecimal repair90DayRate;
    private boolean repairApply90DayRate;
    private String repairApply90DayRateDesc;
    private boolean repairUseCommercial;
    private String repairUseCommercialDesc;
    private String createdBy;
    private String createdDate;
    private boolean removed;
    
    public BrePenaltyBandViewData(){}

    public BrePenaltyBandViewData(BrePenaltyBand object) {
        this.id = object.getId();
        this.claimTypeId = object.getClaimType().getClaimTypeValue();
        this.claimTypeName = object.getClaimType().toString();
        this.penaltyBandStartDate = DateHelper.getLocalDateFormat().format(object.getStartDate());
        this.hire30DayRate = object.getHire30Day();
        this.hire60DayRate = object.getHire60Day();
        this.hire90DayRate = object.getHire90Day();
        this.hireApply90DayRate = object.isHireApply90DayRate();
        this.hireApply90DayRateDesc = hireApply90DayRate ? "Yes" : "No";
        this.hireUseCommercial = object.isHireUseCommercial();
        this.hireUseCommercialDesc = hireUseCommercial ? "Yes" : "No";
        this.repair30DayRate = object.getRepair30Day();
        this.repair60DayRate = object.getRepair60Day();
        this.repair90DayRate = object.getRepair90Day();
        this.repairApply90DayRate = object.isRepairApply90DayRate();
        this.repairApply90DayRateDesc = repairApply90DayRate ? "Yes" : "No";
        this.repairUseCommercial = object.isRepairUseCommercial();
        this.repairUseCommercialDesc = repairUseCommercial ? "Yes" : "No";
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClaimTypeId() {
        return claimTypeId;
    }

    public void setClaimTypeId(int claimTypeId) {
        this.claimTypeId = claimTypeId;
    }

    public String getClaimTypeName() {
        return claimTypeName;
    }

    public void setClaimTypeName(String claimTypeName) {
        this.claimTypeName = claimTypeName;
    }

    public String getPenaltyBandStartDate() {
        return penaltyBandStartDate;
    }

    public void setPenaltyBandStartDate(String penaltyBandStartDate) {
        this.penaltyBandStartDate = penaltyBandStartDate;
    }

    public BigDecimal getHire30DayRate() {
        return hire30DayRate;
    }

    public void setHire30DayRate(BigDecimal hire30DayRate) {
        this.hire30DayRate = hire30DayRate;
    }

    public BigDecimal getHire60DayRate() {
        return hire60DayRate;
    }

    public void setHire60DayRate(BigDecimal hire60DayRate) {
        this.hire60DayRate = hire60DayRate;
    }

    public BigDecimal getHire90DayRate() {
        return hire90DayRate;
    }

    public void setHire90DayRate(BigDecimal hire90DayRate) {
        this.hire90DayRate = hire90DayRate;
    }

    public boolean isHireApply90DayRate() {
        return hireApply90DayRate;
    }

    public void setHireApply90DayRate(boolean hireApply90DayRate) {
        this.hireApply90DayRate = hireApply90DayRate;
    }

    public String getHireApply90DayRateDesc() {
        return hireApply90DayRateDesc;
    }

    public void setHireApply90DayRateDesc(String hireApply90DayRateDesc) {
        this.hireApply90DayRateDesc = hireApply90DayRateDesc;
    }

    public boolean isHireUseCommercial() {
        return hireUseCommercial;
    }

    public void setHireUseCommercial(boolean hireUseCommercial) {
        this.hireUseCommercial = hireUseCommercial;
    }

    public String getHireUseCommercialDesc() {
        return hireUseCommercialDesc;
    }

    public void setHireUseCommercialDesc(String hireUseCommercialDesc) {
        this.hireUseCommercialDesc = hireUseCommercialDesc;
    }

    public BigDecimal getRepair30DayRate() {
        return repair30DayRate;
    }

    public void setRepair30DayRate(BigDecimal repair30DayRate) {
        this.repair30DayRate = repair30DayRate;
    }

    public BigDecimal getRepair60DayRate() {
        return repair60DayRate;
    }

    public void setRepair60DayRate(BigDecimal repair60DayRate) {
        this.repair60DayRate = repair60DayRate;
    }

    public BigDecimal getRepair90DayRate() {
        return repair90DayRate;
    }

    public void setRepair90DayRate(BigDecimal repair90DayRate) {
        this.repair90DayRate = repair90DayRate;
    }

    public boolean isRepairApply90DayRate() {
        return repairApply90DayRate;
    }

    public void setRepairApply90DayRate(boolean repairApply90DayRate) {
        this.repairApply90DayRate = repairApply90DayRate;
    }

    public String getRepairApply90DayRateDesc() {
        return repairApply90DayRateDesc;
    }

    public void setRepairApply90DayRateDesc(String repairApply90DayRateDesc) {
        this.repairApply90DayRateDesc = repairApply90DayRateDesc;
    }

    public boolean isRepairUseCommercial() {
        return repairUseCommercial;
    }

    public void setRepairUseCommercial(boolean repairUseCommercial) {
        this.repairUseCommercial = repairUseCommercial;
    }

    public String getRepairUseCommercialDesc() {
        return repairUseCommercialDesc;
    }

    public void setRepairUseCommercialDesc(String repairUseCommercialDesc) {
        this.repairUseCommercialDesc = repairUseCommercialDesc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
