package idas.chox.core.model;

import java.io.Serializable;

public class AccessibilityItem implements Serializable {

    private Integer id;
    private Accessibility accessibility;
    private String role;
    private Short accessRight;

    public AccessibilityItem() {
    }

    public java.lang.String getRole() {
        return role;
    }

    public void setRole(java.lang.String role) {
        this.role = role;
    }

    public java.lang.Short getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(java.lang.Short accessRight) {
        this.accessRight = accessRight;
    }

    public Accessibility getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(Accessibility accessibility) {
        this.accessibility = accessibility;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }
}
