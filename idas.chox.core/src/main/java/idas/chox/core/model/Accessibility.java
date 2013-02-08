package idas.chox.core.model;

import java.io.Serializable;
import java.util.Map;

public class Accessibility implements Serializable {

    private Integer id;
    private String name;
    private ClaimType claimType;
    private Map<String, Short> accessibilityRoleMap;
    private boolean workgroupCheck;
    private boolean ownershipCheck;
    private boolean checkWorkgroupEnabled;
    private boolean checkClaimOwnershipEnabled;
    private boolean checkSupplierClaimOwnershipEnabled;
    private boolean checkFnolEnabled;
    private boolean checkEngineerEnabled;
    private boolean checkManualInvoiceWrokgroupEnabled;
    private boolean checkManualInvoiceClaimOwnershipEnabled;

    public Accessibility() {
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Short> getAccessibilityRoleMap() {
        return accessibilityRoleMap;
    }

    public void setAccessibilityRoleMap(Map<String, Short> accessibilityRoleMap) {
        this.accessibilityRoleMap = accessibilityRoleMap;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(ClaimType claimType) {
        this.claimType = claimType;
    }
}
