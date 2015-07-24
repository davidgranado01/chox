package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author John
 */
public class BrePenaltyBand extends Entity implements Serializable, FullAudit {
    private BreBand breBand;
    private ClaimType claimType;
    private Date startDate;
    private BigDecimal hire30Day;
    private BigDecimal hire60Day;
    private BigDecimal hire90Day;
    private boolean hireApply90DayRate;
    private boolean hireUseCommercial;
    private BigDecimal repair30Day;
    private BigDecimal repair60Day;
    private BigDecimal repair90Day;
    private boolean repairApply90DayRate;
    private boolean repairUseCommercial;

    public BreBand getBreBand() {
        return breBand;
    }

    public void setBreBand(BreBand breBand) {
        this.breBand = breBand;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getHire30Day() {
        return hire30Day;
    }

    public void setHire30Day(BigDecimal hire30Day) {
        this.hire30Day = hire30Day;
    }

    public BigDecimal getHire60Day() {
        return hire60Day;
    }

    public void setHire60Day(BigDecimal hire60Day) {
        this.hire60Day = hire60Day;
    }

    public BigDecimal getHire90Day() {
        return hire90Day;
    }

    public void setHire90Day(BigDecimal hire90Day) {
        this.hire90Day = hire90Day;
    }

    public boolean isHireApply90DayRate() {
        return hireApply90DayRate;
    }

    public void setHireApply90DayRate(boolean hireApply90DayRate) {
        this.hireApply90DayRate = hireApply90DayRate;
    }

    public boolean isHireUseCommercial() {
        return hireUseCommercial;
    }

    public void setHireUseCommercial(boolean hireUseCommercial) {
        this.hireUseCommercial = hireUseCommercial;
    }

    public BigDecimal getRepair30Day() {
        return repair30Day;
    }

    public void setRepair30Day(BigDecimal repair30Day) {
        this.repair30Day = repair30Day;
    }

    public BigDecimal getRepair60Day() {
        return repair60Day;
    }

    public void setRepair60Day(BigDecimal repair60Day) {
        this.repair60Day = repair60Day;
    }

    public BigDecimal getRepair90Day() {
        return repair90Day;
    }

    public void setRepair90Day(BigDecimal repair90Day) {
        this.repair90Day = repair90Day;
    }

    public boolean isRepairApply90DayRate() {
        return repairApply90DayRate;
    }

    public void setRepairApply90DayRate(boolean repairApply90DayRate) {
        this.repairApply90DayRate = repairApply90DayRate;
    }

    public boolean isRepairUseCommercial() {
        return repairUseCommercial;
    }

    public void setRepairUseCommercial(boolean repairUseCommercial) {
        this.repairUseCommercial = repairUseCommercial;
    }

    
}
