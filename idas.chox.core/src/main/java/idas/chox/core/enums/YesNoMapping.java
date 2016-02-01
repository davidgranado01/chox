package idas.chox.core.enums;

public enum YesNoMapping {

    YES(1, true),
    NO(2, false);

    private final boolean description;
    private final Integer value;

    YesNoMapping(Integer value, boolean description) {
        this.value = value;
        this.description = description;
    }

    public boolean isDescription() {
        return description;
    }

    public Integer getValue() {
        return value;
    }
}
