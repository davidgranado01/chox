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
    private BigDecimal hireDay1;
    private BigDecimal hireDay2;
    private BigDecimal hireDay3;
    private boolean useCommercialDay1;
    private boolean useCommercialDay2;
    private boolean useCommercialDay3;
    private int hirePeriodStartDay1;
    private int hirePeriodStartDay2;
    private int hirePeriodStartDay3;
    private BigDecimal repairDay1;
    private BigDecimal repairDay2;
    private BigDecimal repairDay3;
    private int repairPeriodStartDay1;
    private int repairPeriodStartDay2;
    private int repairPeriodStartDay3;

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

    public BigDecimal getHireDay1() {
        return hireDay1;
    }

    public void setHireDay1(BigDecimal hireDay1) {
        this.hireDay1 = hireDay1.setScale(2);
    }

    public BigDecimal getHireDay2() {
        return hireDay2;
    }

    public void setHireDay2(BigDecimal hireDay2) {
        this.hireDay2 = hireDay2.setScale(2);
    }

    public BigDecimal getHireDay3() {
        return hireDay3;
    }

    public void setHireDay3(BigDecimal hireDay3) {
        this.hireDay3 = hireDay3.setScale(2);
    }

    public BigDecimal getRepairDay1() {
        return repairDay1;
    }

    public void setRepairDay1(BigDecimal repairDay1) {
        this.repairDay1 = repairDay1.setScale(2);
    }

    public BigDecimal getRepairDay2() {
        return repairDay2;
    }

    public void setRepairDay2(BigDecimal repairDay2) {
        this.repairDay2 = repairDay2.setScale(2);
    }

    public BigDecimal getRepairDay3() {
        return repairDay3;
    }

    public void setRepairDay3(BigDecimal repairDay3) {
        this.repairDay3 = repairDay3.setScale(2);
    }


    public static List<BrePenaltyBand> getDefaults() {
        List<BrePenaltyBand> result = new ArrayList<>(10);
    
        // Create GTA Defaults
        BrePenaltyBand band = new BrePenaltyBand();
        band.claimType = ClaimType.GTA;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hireDay1 = new BigDecimal("7.5");
        band.hireDay2 = new BigDecimal("15.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.GTA;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hireDay1 = new BigDecimal("12.5");
        band.hireDay2 = new BigDecimal("20.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        // Create Subscriber Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.SUBSCRIBER;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hireDay1 = new BigDecimal("4.0");
        band.hireDay2 = new BigDecimal("8.0");
        band.hireDay3 = new BigDecimal("12.0");
        band.useCommercialDay3 = false;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("0.0");
        band.repairDay2 = new BigDecimal("0.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = 90;
        result.add(band);
        
        // Create Fixed-Fee Results
        band = new BrePenaltyBand();
        band.claimType = ClaimType.FIXED_FEE;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hireDay1 = new BigDecimal("5.0");
        band.hireDay2 = new BigDecimal("10.0");
        band.hireDay3 = new BigDecimal("15.0");
        band.useCommercialDay3 = false;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("5.0");
        band.repairDay2 = new BigDecimal("10.0");
        band.repairDay3 = new BigDecimal("15.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = 90;
        result.add(band);
    
        // Create Collaboration Protocol Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.COLLABORATION_PROTOCOL;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hireDay1 = new BigDecimal("7.5");
        band.hireDay2 = new BigDecimal("15.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.COLLABORATION_PROTOCOL;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hireDay1 = new BigDecimal("12.5");
        band.hireDay2 = new BigDecimal("20.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        // Create Insurer vs Insurer Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.INSURER_VS_INSURER;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hireDay1 = new BigDecimal("7.5");
        band.hireDay2 = new BigDecimal("15.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.INSURER_VS_INSURER;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hireDay1 = new BigDecimal("12.5");
        band.hireDay2 = new BigDecimal("20.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        // Create TPI Defaults
        band = new BrePenaltyBand();
        band.claimType = ClaimType.TPI;
        band.startDate = DateHelper.parse("01/01/1950");
        band.hireDay1 = new BigDecimal("7.5");
        band.hireDay2 = new BigDecimal("15.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        band = new BrePenaltyBand();
        band.claimType = ClaimType.TPI;
        band.startDate = DateHelper.parse("15/06/2012");
        band.hireDay1 = new BigDecimal("12.5");
        band.hireDay2 = new BigDecimal("20.0");
        band.hireDay3 = new BigDecimal("0.0");
        band.useCommercialDay3 = true;
        band.useCommercialDay2 = false;
        band.useCommercialDay1 = false;
        band.hirePeriodStartDay1 = 30;
        band.hirePeriodStartDay2 = 60;
        band.hirePeriodStartDay3 = 90;
        band.repairDay1 = new BigDecimal("2.5");
        band.repairDay2 = new BigDecimal("5.0");
        band.repairDay3 = new BigDecimal("0.0");
        band.repairPeriodStartDay1 = 30;
        band.repairPeriodStartDay2 = 60;
        band.repairPeriodStartDay3 = -1;
        result.add(band);
        
        // Create Manual Defaults
        // - no defaults for manual invoices

        return result;
    }

    public int getHirePeriodStartDay1() {
        return hirePeriodStartDay1;
    }

    public void setHirePeriodStartDay1(int hirePeriodStartDay1) {
        this.hirePeriodStartDay1 = hirePeriodStartDay1;
    }

    public int getHirePeriodStartDay2() {
        return hirePeriodStartDay2;
    }

    public void setHirePeriodStartDay2(int hirePeriodStartDay2) {
        this.hirePeriodStartDay2 = hirePeriodStartDay2;
    }

    public int getHirePeriodStartDay3() {
        return hirePeriodStartDay3;
    }

    public void setHirePeriodStartDay3(int hirePeriodStartDay3) {
        this.hirePeriodStartDay3 = hirePeriodStartDay3;
    }

    public int getRepairPeriodStartDay1() {
        return repairPeriodStartDay1;
    }

    public void setRepairPeriodStartDay1(int repairPeriodStartDay1) {
        this.repairPeriodStartDay1 = repairPeriodStartDay1;
    }

    public int getRepairPeriodStartDay2() {
        return repairPeriodStartDay2;
    }

    public void setRepairPeriodStartDay2(int repairPeriodStartDay2) {
        this.repairPeriodStartDay2 = repairPeriodStartDay2;
    }

    public int getRepairPeriodStartDay3() {
        return repairPeriodStartDay3;
    }

    public void setRepairPeriodStartDay3(int repairPeriodStartDay3) {
        this.repairPeriodStartDay3 = repairPeriodStartDay3;
    }

    public boolean isUseCommercialDay1() {
        return useCommercialDay1;
    }

    public void setUseCommercialDay1(boolean useCommercialDay1) {
        this.useCommercialDay1 = useCommercialDay1;
    }

    public boolean isUseCommercialDay2() {
        return useCommercialDay2;
    }

    public void setUseCommercialDay2(boolean useCommercialDay2) {
        this.useCommercialDay2 = useCommercialDay2;
    }

    public boolean isUseCommercialDay3() {
        return useCommercialDay3;
    }

    public void setUseCommercialDay3(boolean useCommercialDay3) {
        this.useCommercialDay3 = useCommercialDay3;
    }
}
