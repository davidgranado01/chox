package idas.chox.core.model;

/**
 *
 * @author abrar
 */
public enum LiabilityStatus {

    LIABILITY_NULL              (0, ""),
    LIABILITY_ACCEPTED          (1, "Full Liability Accepted"),
    LIABILITY_DISPUTED          (2, "Liability In Negotiation"),
    LIABILITY_UNKNOWN           (3, "Liability Unknown"),
    LIABILITY_REPUDIATED        (4, "Liability Repudiated"),
    LIABILITY_SPLIT             (5, "Liability Split"),
    PROCEED_WITHOUT_PREJUDICE   (6, "Proceed Without Prejudice");

    private final String description;
    private final int liablityValue;
    
    LiabilityStatus(int value, String description) {
        this.liablityValue = value;
        this.description = description;
    }

    public static LiabilityStatus getLiabilityStatus(String description) {
        for (LiabilityStatus value : LiabilityStatus.values()) {
            if (value.description.equals(description)) {
                return value;
            }
        }
        throw new IllegalArgumentException("No such Liability Status: " + description);
    }
    
    public int getLiablityValue() {
        return liablityValue;
    }

    @Override
    public String toString() {
        return description;
    }
}
