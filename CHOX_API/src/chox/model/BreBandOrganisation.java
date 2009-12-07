package chox.model;

import java.io.Serializable;

public class BreBandOrganisation extends AuditableEntity implements Serializable {

    protected BreBand breBand;
    protected Chorganisation chorganisation;

    public BreBand getBreBand() {
        return breBand;
    }

    public void setBreBand(BreBand breBand) {
        this.breBand = breBand;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }
}
