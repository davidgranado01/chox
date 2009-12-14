package idas.chox.core.model;

import java.io.Serializable;

public class InsurerChorganisation extends AuditableEntity implements Serializable {

    protected Insurer insurer;
    protected Chorganisation chorganisation;
    protected boolean status;

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
