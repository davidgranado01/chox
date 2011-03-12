package idas.chox.core.model;

import java.io.Serializable;

public class InsurerChorganisation extends Entity implements Serializable {

    protected Insurer insurer;
    protected Chorganisation chorganisation;
    protected boolean status;
    private boolean specialPriceActivated;
    private boolean thirdPartyInterventionActivated;
    private String tpiIdentificationString;
    private boolean tpiClaimOnly;
    private Workgroup tpiWorkgroup;
    private WebUser tpiClaimOwner;
    private String tpiRegexExpression;

    public String getTpiRegexExpression() {
        return tpiRegexExpression;
    }

    public void setTpiRegexExpression(String tpiRegexExpression) {
        this.tpiRegexExpression = tpiRegexExpression;
    }


     public Workgroup getTpiWorkgroup() {
         return tpiWorkgroup;
    }

    public WebUser getTpiClaimOwner() {
        return tpiClaimOwner;
    }

    public void setTpiWorkgroup(Workgroup workgroup) {
        this.tpiWorkgroup=workgroup;
    }

    public void setTpiClaimOwner(WebUser webUser) {
        this.tpiClaimOwner=webUser;
    }

    public boolean isTpiClaimOnly() {
        return tpiClaimOnly;
    }

    public void setTpiClaimOnly(boolean tpiClaimOnly) {
        this.tpiClaimOnly = tpiClaimOnly;
    }

    public String getTpiIdentificationString() {
        return tpiIdentificationString;
    }

    public void setTpiIdentificationString(String tpiIdentificationString) {
        this.tpiIdentificationString = tpiIdentificationString;
    }

    public boolean isThirdPartyInterventionActivated() {
        return thirdPartyInterventionActivated;
    }

    public void setThirdPartyInterventionActivated(boolean thirdPartyIntervention) {
        this.thirdPartyInterventionActivated = thirdPartyIntervention;
    }

    public boolean isSpecialPriceActivated() {
        return specialPriceActivated;
    }

    public void setSpecialPriceActivated(boolean specialPriceActivated) {
        this.specialPriceActivated = specialPriceActivated;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }
}
