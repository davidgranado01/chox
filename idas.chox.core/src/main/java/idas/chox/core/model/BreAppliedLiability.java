package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 *
 * @author John
 */
public class BreAppliedLiability extends Entity implements Serializable, FullAudit {
    private BreBand breBand;
    private ClaimType claimType;
    private BigDecimal appliedLiability;
    private boolean appliesToRepudiated;

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

    public BigDecimal getAppliedLiability() {
        return appliedLiability;
    }

    public void setAppliedLiability(BigDecimal appliedLiability) {
        this.appliedLiability = appliedLiability;
    }

    public boolean isAppliesToRepudiated() {
        return appliesToRepudiated;
    }

    public void setAppliesToRepudiated(boolean appliesToRepudiated) {
        this.appliesToRepudiated = appliesToRepudiated;
    }


}
