package idas.chox.core.model;

import java.io.Serializable;
//import org.hibernate.validator.constraints.SafeHtml;

public class InsurerAlias extends Entity implements Serializable {

//    @SafeHtml(whitelistType=org.hibernate.validator.constraints.SafeHtml.WhiteListType.NONE)
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
