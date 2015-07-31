package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import idas.chox.core.util.DateHelper;

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
        this.hire30Day = hire30Day.setScale(1);
    }

    public BigDecimal getHire60Day() {
        return hire60Day;
    }

    public void setHire60Day(BigDecimal hire60Day) {
        this.hire60Day = hire60Day.setScale(1);
    }

    public BigDecimal getHire90Day() {
        return hire90Day;
    }

    public void setHire90Day(BigDecimal hire90Day) {
        this.hire90Day = hire90Day.setScale(1);
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
        this.repair30Day = repair30Day.setScale(1);
    }

    public BigDecimal getRepair60Day() {
        return repair60Day;
    }

    public void setRepair60Day(BigDecimal repair60Day) {
        this.repair60Day = repair60Day.setScale(1);
    }

    public BigDecimal getRepair90Day() {
        return repair90Day;
    }

    public void setRepair90Day(BigDecimal repair90Day) {
        this.repair90Day = repair90Day.setScale(1);
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

    public static List<BrePenaltyBand> getDefaults() {
        List<BrePenaltyBand> result = new ArrayList<>(10);
    
        // Create GTA Defaults
        BrePenaltyBand band = new BrePenaltyBand();
        band.claimType = ClaimType.GTA;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hire30Day = new BigDecimal("7.5");
        band.hire60Day = new BigDecimal("15.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.GTA;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hire30Day = new BigDecimal("12.5");
        band.hire60Day = new BigDecimal("20.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        // Create Subscriber Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.SUBSCRIBER;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hire30Day = new BigDecimal("4.0");
        band.hire60Day = new BigDecimal("8.0");
        band.hire90Day = new BigDecimal("12.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = false;
        band.repair30Day = new BigDecimal("0.0");
        band.repair60Day = new BigDecimal("0.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = true;
        band.repairUseCommercial = false;
        result.add(band);
        
        // Create Fixed-Fee Results
        band = new BrePenaltyBand();
        band.claimType = ClaimType.FIXED_FEE;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hire30Day = new BigDecimal("5.0");
        band.hire60Day = new BigDecimal("10.0");
        band.hire90Day = new BigDecimal("15.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = false;
        band.repair30Day = new BigDecimal("5.0");
        band.repair60Day = new BigDecimal("10.0");
        band.repair90Day = new BigDecimal("15.0");
        band.repairApply90DayRate = true;
        band.repairUseCommercial = false;
        result.add(band);
    
        // Create Collaboration Protocol Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.COLLABORATION_PROTOCOL;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hire30Day = new BigDecimal("7.5");
        band.hire60Day = new BigDecimal("15.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.COLLABORATION_PROTOCOL;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hire30Day = new BigDecimal("12.5");
        band.hire60Day = new BigDecimal("20.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        // Create Insurer vs Insurer Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.INSURER_VS_INSURER;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hire30Day = new BigDecimal("7.5");
        band.hire60Day = new BigDecimal("15.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.INSURER_VS_INSURER;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hire30Day = new BigDecimal("12.5");
        band.hire60Day = new BigDecimal("20.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        // Create TPI Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.TPI;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hire30Day = new BigDecimal("7.5");
        band.hire60Day = new BigDecimal("15.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.0");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.TPI;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hire30Day = new BigDecimal("12.5");
        band.hire60Day = new BigDecimal("20.0");
        band.hire90Day = new BigDecimal("0.0");
        band.hireApply90DayRate = true;
        band.hireUseCommercial = true;
        band.repair30Day = new BigDecimal("2.5");
        band.repair60Day = new BigDecimal("5.0");
        band.repair90Day = new BigDecimal("0.0");
        band.repairApply90DayRate = false;
        band.repairUseCommercial = false;
        result.add(band);
        
        // Create Manual Defaults
        // - no defaults for manual invoices

        return result;
    }
}
