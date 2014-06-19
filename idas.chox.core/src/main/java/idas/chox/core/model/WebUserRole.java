package idas.chox.core.model;

import java.io.Serializable;

public class WebUserRole extends Entity implements Serializable {

    public static final String ROLE_CHOX_ADMIN= "ROLE_CHOX_ADMIN";
    public static final String ROLE_CHOX = "ROLE_CHOX";
    public static final String ROLE_INS = "ROLE_INS";
    public static final String ROLE_INS_CH = "ROLE_INS_CH";
    public static final String ROLE_INS_PC = "ROLE_INS_PC";
    public static final String ROLE_INS_COM = "ROLE_INS_COM";
    public static final String ROLE_INS_UPLOAD = "ROLE_INS_UPLOAD";
    public static final String ROLE_INS_CR = "ROLE_INS_CR";
    public static final String ROLE_INS_FNOL = "ROLE_INS_FNOL";
    public static final String ROLE_INS_MNG = "ROLE_INS_MNG";
    public static final String ROLE_INS_SCR = "ROLE_INS_SCR";
    public static final String ROLE_INS_MI = "ROLE_INS_MI";
    public static final String ROLE_INS_SUP = "ROLE_INS_SUP";
    public static final String ROLE_INS_ADMIN = "ROLE_INS_ADMIN";
    public static final String ROLE_INS_USER_MNG = "ROLE_INS_USER";
    public static final String ROLE_CHO = "ROLE_CHO";
    public static final String ROLE_CHO_MNG = "ROLE_CHO_MNG";
    public static final String ROLE_CHO_OPR = "ROLE_CHO_OPR";
    public static final String ROLE_CHO_MI = "ROLE_CHO_MI";
    public static final String ROLE_CHO_USER_MNG = "ROLE_CHO_USER";

    private String name;
    private String description;
    private Integer typeId;
    private boolean workgroupRelated;
    private boolean ownershipRelated;
    private boolean fnolRelated;
    private boolean engineerRelated;
    private boolean showWorkgroupDisabled;
    private boolean showOwnershipDisabled;
    private boolean showInsurerUploadDisabled;
    private boolean showAdminOnly;
    private boolean canBeAssignedTasks;

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

    public boolean isFnolRelated() {
        return fnolRelated;
    }

    public void setFnolRelated(boolean fnolRelated) {
        this.fnolRelated = fnolRelated;
    }

    public boolean isEngineerRelated() {
        return engineerRelated;
    }

    public void setEngineerRelated(boolean engineerRelated) {
        this.engineerRelated = engineerRelated;
    }

    public boolean isShowOwnershipDisabled() {
        return showOwnershipDisabled;
    }

    public void setShowOwnershipDisabled(boolean showOwnershipDisabled) {
        this.showOwnershipDisabled = showOwnershipDisabled;
    }

    public boolean isShowWorkgroupDisabled() {
        return showWorkgroupDisabled;
    }

    public void setShowWorkgroupDisabled(boolean showWorkgroupDisabled) {
        this.showWorkgroupDisabled = showWorkgroupDisabled;
    }

    public boolean isShowInsurerUploadDisabled() {
        return showInsurerUploadDisabled;
    }

    public void setShowInsurerUploadDisabled(boolean showInsurerUploadDisabled) {
        this.showInsurerUploadDisabled = showInsurerUploadDisabled;
    }

    public boolean isShowAdminOnly() {
        return showAdminOnly;
    }

    public void setShowAdminOnly(boolean showAdminOnly) {
        this.showAdminOnly = showAdminOnly;
    }

    public boolean isCanBeAssignedTasks() {
        return canBeAssignedTasks;
    }

    public void setCanBeAssignedTasks(boolean canBeAssignedTasks) {
        this.canBeAssignedTasks = canBeAssignedTasks;
    }

}
