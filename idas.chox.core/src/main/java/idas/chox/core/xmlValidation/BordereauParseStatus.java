package idas.chox.core.xmlValidation;

public enum BordereauParseStatus {

    PARTIAL_UPLOAD      (0, "Partially Uploaded"),
    ALL_REJECTED        (1, "All Rejected"),
    ALL_UPLOADED        (2, "All Uploaded"),
    ERROR               (3, "Error");
    
    private final String description;
    private final int bordereauParseStatusValue;

    BordereauParseStatus(int bordereauParseStatusValue, String description) {
        this.bordereauParseStatusValue = bordereauParseStatusValue;
        this.description = description;
    }

    public int getBordereauParseStatusValue() {
        return bordereauParseStatusValue;
    }

    public String getDescription() {
        return description;
    }
}