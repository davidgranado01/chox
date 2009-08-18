package chox.web.data;

import chox.model.Claim;
import chox.model.Injury;
import chox.model.Solicitor;
import chox.model.Witness;

public class ExcelClaim {
    protected Claim claim;
    protected Injury injury;
    protected Solicitor solicitor;
    protected Witness witness;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Injury getInjury() {
        return injury;
    }

    public void setInjury(Injury injury) {
        this.injury = injury;
    }

    public Solicitor getSolicitor() {
        return solicitor;
    }

    public void setSolicitor(Solicitor solicitor) {
        this.solicitor = solicitor;
    }

    public Witness getWitness() {
        return witness;
    }

    public void setWitness(Witness witness) {
        this.witness = witness;
    }
    
}
