package idas.chox.core.model;

public class AccessibilityEditable {

    protected Integer id;
    protected Accessibility accessibility;
    protected boolean workgroupCheck;
    protected boolean ownershipCheck;

    public Accessibility getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(Accessibility accessibility) {
        this.accessibility = accessibility;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isOwnershipCheck() {
        return ownershipCheck;
    }

    public void setOwnershipCheck(boolean ownershipCheck) {
        this.ownershipCheck = ownershipCheck;
    }

    public boolean isWorkgroupCheck() {
        return workgroupCheck;
    }

    public void setWorkgroupCheck(boolean workgroupCheck) {
        this.workgroupCheck = workgroupCheck;
    }

}