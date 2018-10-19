package idas.chox.core.enums;

public enum InvoiceReviewReason {

    INTERVENTION(0, "Intervention"),
    INVESTIGATION(1, "Claims Investigation"),
    INDEMNITY(2, "Indemnity"),
    QUANTUM(3, "Quantum/causation issue"),
    CONCERNS(3, "Handler concerns");

    private final String description;
    private final Integer value;

    InvoiceReviewReason(Integer value, String description) {
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
