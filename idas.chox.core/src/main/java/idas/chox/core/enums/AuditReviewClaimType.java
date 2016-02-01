package idas.chox.core.enums;

public enum AuditReviewClaimType {

    HIRE_ONLY(0, "Hire Only"),
    REPAIR_ONLY(1, "Repair Only"),
    HIRE_AND_REPAIR(2, "Hire & Repair"),
    OTHER(3, "Other");

    private final String description;
    private final Integer value;

    AuditReviewClaimType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Integer getValue() {
        return value;
    }
}
