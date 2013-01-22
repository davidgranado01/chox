package idas.chox.core.model;

import java.io.Serializable;

public class InsurerChorganisation extends Entity implements Serializable {

    private Insurer insurer;
    private Chorganisation chorganisation;
   
    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }
}
