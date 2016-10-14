package idas.chox.core.model;

import java.io.Serializable;

/**
 *
 * @author john
 */
public class InsurerBillingBandMapping extends Entity implements Serializable {
    private InsurerBillingBand insurerBillingBand;
    private Chorganisation chorganisation;
    private ClaimType claimType;

    public InsurerBillingBand getInsurerBillingBand() {
        return insurerBillingBand;
    }

    public void setInsurerBillingBand(InsurerBillingBand insurerBillingBand) {
        this.insurerBillingBand = insurerBillingBand;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }

}
