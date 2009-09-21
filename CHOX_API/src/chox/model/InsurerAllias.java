package chox.model;

import java.io.Serializable;

public class InsurerAllias extends AuditableEntity implements Serializable {

    protected String alliasName;
    protected Insurer insurer;

    public String getAlliasName() {
        return alliasName;
    }

    public void setAlliasName(String alliasName) {
        this.alliasName = alliasName;
    }

    public Insurer getInsurer() {
        return insurer;
    }

    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }
}
