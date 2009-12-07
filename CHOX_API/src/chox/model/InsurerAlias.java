package chox.model;

import java.io.Serializable;

public class InsurerAlias extends AuditableEntity implements Serializable {

    protected String aliasName;
    protected Insurer insurer;

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
