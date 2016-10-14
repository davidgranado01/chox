package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author john
 */
public class InsurerBillingBand extends Entity implements Serializable {
    private Insurer insurer;
    private String bandName;
    private BigDecimal costPerClaim;
    private boolean excludeSupplementary;
    private String triggerStatus;

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public BigDecimal getCostPerClaim() {
        return costPerClaim;
    }

    public void setCostPerClaim(BigDecimal costPerClaim) {
        this.costPerClaim = costPerClaim;
    }

    public boolean isExcludeSupplementary() {
        return excludeSupplementary;
    }

    public void setExcludeSupplementary(boolean excludeSupplementary) {
        this.excludeSupplementary = excludeSupplementary;
    }

    public String getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(String triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

}
