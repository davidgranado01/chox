package idas.chox.core.model;


public enum Branding {

    NO_BRANDING          (0, "Off"),
    PARTIAL_BRANDING     (1, "Partial"),
    FULL_BRANDING        (2, "Full");

    private final String description;
    private final Integer brandingValue;
    
    Branding(Integer value, String description) {
        this.brandingValue = value;
        this.description = description;
    }

    public Integer getbrandingValue() {
        return brandingValue;
    }

    public String getDescription() {
        return description;
    }
}
