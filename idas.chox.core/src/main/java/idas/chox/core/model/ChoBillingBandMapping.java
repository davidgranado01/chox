package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author john
 */
public class ChoBillingBandMapping extends Entity implements Serializable {
    private ChoBillingBand choBillingBand;
    private Insurer insurer;
    private ClaimType claimType;

    public ChoBillingBand getChoBillingBand() {
        return choBillingBand;
    }

    public void setChoBillingBand(ChoBillingBand choBillingBand) {
        this.choBillingBand = choBillingBand;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

}
