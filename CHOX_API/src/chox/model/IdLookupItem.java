/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

/**
 *
 * @author Emmanuel
 */
public class IdLookupItem {

    private String text;
    private Integer value;

    public IdLookupItem() {
        this.text = "";
        this.value = -1;
    }

    public IdLookupItem(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
