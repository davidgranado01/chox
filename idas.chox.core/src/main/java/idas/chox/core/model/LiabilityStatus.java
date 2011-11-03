package idas.chox.core.model;

/**
 *
 * @author abrar
 */
public enum LiabilityStatus {

    LIABILITY_NULL              (null),
    LIABILITY_ACCEPTED          ("Full Liability Accepted"),
    LIABILITY_DISPUTED          ("Liability In Negotiation"),
    LIABILITY_UNKNOWN           ("Liability Unknown"),
    LIABILITY_REPUDIATED        ("Liability Repudiated"),
    LIABILITY_SPLIT             ("Liability Split"),
    PROCEED_WITHOUT_PREJUDICE   ("Proceed Without Prejudice");

    String description;

    LiabilityStatus(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }
}
