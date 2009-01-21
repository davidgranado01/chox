/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

/**
 *
 * @author Emmanuel
 */
public class LookupItem {
    private String text;
    private String value;
    
    public LookupItem(String text,String value)
    {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
