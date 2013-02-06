package idas.chox.core.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class Accessibility implements Serializable {

    private Integer id;
    private String name;
    private ClaimType claimType;
    private Set accessibilityItem = new HashSet();
    private boolean workgroupCheck;
    private boolean ownershipCheck;
    private boolean checkWorkgroupEnabled;
    private boolean checkClaimOwnershipEnabled;
    private boolean checkSupplierClaimOwnershipEnabled;
    private boolean checkFnolEnabled;
    private boolean checkEngineerEnabled;
    private boolean checkManualInvoiceWrokgroupEnabled;
    private boolean checkManualInvoiceClaimOwnershipEnabled;

    public boolean isCheckManualInvoiceClaimOwnershipEnabled() {
        return checkManualInvoiceClaimOwnershipEnabled;
    }

    public void setCheckManualInvoiceClaimOwnershipEnabled(boolean checkManualInvoiceClaimOwnershipEnabled) {
        this.checkManualInvoiceClaimOwnershipEnabled = checkManualInvoiceClaimOwnershipEnabled;
    }

    public boolean isCheckManualInvoiceWrokgroupEnabled() {
        return checkManualInvoiceWrokgroupEnabled;
    }

    public void setCheckManualInvoiceWrokgroupEnabled(boolean checkManualInvoiceWrokgroupEnabled) {
        this.checkManualInvoiceWrokgroupEnabled = checkManualInvoiceWrokgroupEnabled;
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

    public boolean isCheckSupplierClaimOwnershipEnabled() {
        return checkSupplierClaimOwnershipEnabled;
    }

    public void setCheckSupplierClaimOwnershipEnabled(boolean checkSupplierClaimOwnershipEnabled) {
        this.checkSupplierClaimOwnershipEnabled = checkSupplierClaimOwnershipEnabled;
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

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }
}
