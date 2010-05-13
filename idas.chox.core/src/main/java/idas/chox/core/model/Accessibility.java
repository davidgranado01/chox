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
    protected boolean checkWorkgroupEnabled;
    protected boolean checkClaimOwnershipEnabled;
    protected boolean checkFnolEnabled;
    protected boolean checkEngineerEnabled;

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

    public boolean isCheckClaimOwnershipEnabled() {
        return checkClaimOwnershipEnabled;
    }

    public void setCheckClaimOwnershipEnabled(boolean checkClaimOwnershipEnabled) {
        this.checkClaimOwnershipEnabled = checkClaimOwnershipEnabled;
    }

    public boolean isCheckEngineerEnabled() {
        return checkEngineerEnabled;
    }

    public void setCheckEngineerEnabled(boolean checkEngineerEnabled) {
        this.checkEngineerEnabled = checkEngineerEnabled;
    }

    public boolean isCheckFnolEnabled() {
        return checkFnolEnabled;
    }

    public void setCheckFnolEnabled(boolean checkFnolEnabled) {
        this.checkFnolEnabled = checkFnolEnabled;
    }

    public boolean isCheckWorkgroupEnabled() {
        return checkWorkgroupEnabled;
    }

    public void setCheckWorkgroupEnabled(boolean checkWorkgroupEnabled) {
        this.checkWorkgroupEnabled = checkWorkgroupEnabled;
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
