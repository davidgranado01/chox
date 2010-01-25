package idas.chox.core.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class Accessibility implements Serializable {

    protected Integer id;
    protected String name;
    protected Set accessibilityItem = new HashSet();
    protected boolean workgroupCheck;
    protected boolean ownershipCheck;

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

    public Accessibility() {
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public Set getAccessibilityItem() {
        return accessibilityItem;
    }

    public void setAccessibilityItem(Set accessibilityItem) {
        this.accessibilityItem = accessibilityItem;
    }
}
