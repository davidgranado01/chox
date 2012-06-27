package idas.chox.core.model;

public enum InsurerDiscountType {
    HIRE           (0, "hire"),
    REPAIR         (1, "repair"),
    TOTAL          (2, "total");

    private final String description;
    private final int insurerDiscountTypeValue;

    InsurerDiscountType(int insurerDiscountTypeValue, String description) {
        this.insurerDiscountTypeValue = insurerDiscountTypeValue;
        this.description = description;
    }

    public int getInsurerDiscountTypeValue() {
        return insurerDiscountTypeValue;
    }

    @Override
    public String toString() {
        return description;
    }
}
