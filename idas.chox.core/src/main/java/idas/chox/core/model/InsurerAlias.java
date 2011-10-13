package idas.chox.core.model;

import java.io.Serializable;

public class InsurerAlias extends Entity implements Serializable {

    private String aliasName;
    private Insurer insurer;

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }
}
