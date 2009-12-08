package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;

public class Accessibility implements Serializable
{

    protected Integer id;
    protected String name;
    protected Set accessibilityItem = new HashSet();
    protected Set AccessibilityEditable = new HashSet();

    public Accessibility(){
    }

    public java.lang.Integer getId(){
        return id;
    }

    public void setId(java.lang.Integer id){
        this.id = id;
    }

    public java.lang.String getName(){
        return name;
    }

    public void setName(java.lang.String name){
        this.name = name;
    }

    public Set getAccessibilityItem(){
        return accessibilityItem;
    }

    public void setAccessibilityItem(Set accessibilityItem)
    {
        this.accessibilityItem = accessibilityItem;
    }

    public Set getAccessibilityEditable() {
        return AccessibilityEditable;
    }

    public void setAccessibilityEditable(Set AccessibilityEditable) {
        this.AccessibilityEditable = AccessibilityEditable;
    }



}
