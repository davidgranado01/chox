package idas.chox.core.model;

import java.io.Serializable;

public class WebUserRole extends Entity implements Serializable {

    public static final String ROLE_CHOX = "ROLE_CHOX";
    public static final String ROLE_INS = "ROLE_INS";
    public static final String ROLE_CHO = "ROLE_CHO";
    public static final String ROLE_CH = "ROLE_INS_CH";
    public static final String ROLE_COM = "ROLE_INS_COM";
    public static final String ROLE_FNOL = "ROLE_INS_FNOL";
    public static final String ROLE_CH_MNG = "ROLE_CHO_MNG";
    public static final String ROLE_INS_MNG = "ROLE_INS_MNG";
    public static final String ROLE_INS_SCR = "ROLE_INS_SCR";

    protected String name;
    protected String description;
    protected Integer typeId;
    protected boolean workgroupRelated;
    protected boolean ownershipRelated;

    public WebUserRole() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public boolean isOwnershipRelated() {
        return ownershipRelated;
    }

    public void setOwnershipRelated(boolean ownershipRelated) {
        this.ownershipRelated = ownershipRelated;
    }

    public boolean isWorkgroupRelated() {
        return workgroupRelated;
    }

    public void setWorkgroupRelated(boolean workgroupRelated) {
        this.workgroupRelated = workgroupRelated;
    }

    
}
