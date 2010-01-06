package chox.model;

import java.io.Serializable;

public class ChoBandOrganisation extends AuditableEntity implements Serializable {

    protected ChoBand choBand;
    protected Chorganisation chorganisation;

    public ChoBand getChoBand() {
        return choBand;
    }

    public void setChoBand(ChoBand choBand) {
        this.choBand = choBand;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }
}
