package idas.chox.core.model;

import java.io.Serializable;

public class ChorganisationAlias extends Entity implements Serializable {

    private String aliasName;
    private Chorganisation chorganisation;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Chorganisation getChorganisation() {
        return chorganisation;
    }

    public void setChorganisation(Chorganisation chorganisation) {
        this.chorganisation = chorganisation;
    }
}
