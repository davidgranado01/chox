package idas.chox.core.model;

/**
 *
 * @author John
 */
public enum ClaimType {
    GTA                                         ("GTA"),
    GTA_ORIGINAL_INVOICE                        ("GTA (Orig. Invoice)"),
    GTA_SUPPLEMENTARY_INVOICE                   ("GTA (Supp. Invoice)"),
    TPI                                         ("TPI"),
    TPI_ORIGINAL_INVOICE                        ("TPI (Orig. Invoice)"),
    TPI_SUPPLEMENTARY_INVOICE                   ("TPI (Supp. Invoice)"),
    INSURER_VS_INSURER                          ("Insurer vs. Insurer"),
    INSURER_VS_INSURER_ORIGINAL_INVOICE         ("Insurer vs. Insurer (Orig. Invoice)"),
    INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE    ("Insurer vs. Insurer (Supp. Invoice)"),
    SUBSCRIBER                                  ("Subscriber"),
    SUBSCRIBER_ORIGINAL_INVOICE                 ("Subscriber (Orig. Invoice)"),
    SUBSCRIBER_SUPPLEMENTARY_INVOICE            ("Subscriber (Supp. Invoice)");

    private String description;

    ClaimType(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return description;
    }

    public static boolean isGTA(ClaimType claimType) {
        if (        claimType == ClaimType.GTA
                ||  claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.GTA_SUPPLEMENTARY_INVOICE)
            return true;
        
        return false;
    }

    public static boolean isTPI(ClaimType claimType) {
        if (        claimType == ClaimType.TPI
                ||  claimType == ClaimType.TPI_ORIGINAL_INVOICE
                ||  claimType == ClaimType.TPI_SUPPLEMENTARY_INVOICE)
            return true;
        
        return false;
    }

    public static boolean isInsurerVsInsurer(ClaimType claimType) {
        if (        claimType == ClaimType.INSURER_VS_INSURER
                ||  claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE)
            return true;
        
        return false;
    }

    public static boolean isSubscriber(ClaimType claimType) {
        if (        claimType == ClaimType.SUBSCRIBER
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE)
            return true;
        
        return false;
    }

    public static boolean isSupplementaryInvoice(ClaimType claimType) {
        if (        claimType == ClaimType.GTA_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.TPI_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.TPI_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE)
            return true;
        
        return false;
    }

    public static boolean isOriginalSupplementaryInvoice(ClaimType claimType) {
        if (        claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.TPI_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE)
            return true;
        
        return false;
    }

}
