package idas.chox.core.model;

/**
 *
 * @author Emmanuel
 */
public class LookupItem {

    private String text;
    private String value;

    public LookupItem() {
        this.text = "";
        this.value = "";
    }

    public LookupItem(String text, String value) {
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
