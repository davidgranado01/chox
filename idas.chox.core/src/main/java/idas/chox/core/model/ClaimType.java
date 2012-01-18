package idas.chox.core.model;

/**
 *
 * @author John
 */
public enum ClaimType {
    GTA                                         ("GTA"),
    GTA_ORIGINAL_INVOICE                        ("GTA (Orig. Invoice)"),
    GTA_SUPPLEMENTARY_INVOICE                   ("GTA (Supp. Invoice)"),
    TPI                                         ("Third Party Intervention (TPI)"),
    INSURER_VS_INSURER                          ("Insurer vs. Insurer"),
    INSURER_VS_INSURER_ORIGINAL_INVOICE         ("Insurer vs. Insurer (Orig. Invoice)"),
    INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE    ("Insurer vs. Insurer (Supp. Invoice)"),
    SUBSCRIBER                                  ("Subscriber"),
    SUBSCRIBER_ORIGINAL_INVOICE                 ("Subscriber (Orig. Invoice)"),
    SUBSCRIBER_SUPPLEMENTARY_INVOICE            ("Subscriber (Supp. Invoice)"),
    INSURER_UPLOAD                              ("Insurer Manual Invoice");

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
        if (        claimType == ClaimType.TPI)
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

    public static boolean isInsurerUpload(ClaimType claimType) {
        if (        claimType == ClaimType.INSURER_UPLOAD)
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
                ||  claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE)
            return true;
        
        return false;
    }

    public static ClaimType[] getSupplementaryInvoiceTypes() {
        return new ClaimType[] {ClaimType.GTA_SUPPLEMENTARY_INVOICE,
                                ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE,
                                ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE};
    }
    
    public static String getSupplementaryInvoiceTypeOrdinals() {
        return "(" + ClaimType.GTA_SUPPLEMENTARY_INVOICE.ordinal() + ","
                   + ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE.ordinal() + ","
                   + ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE.ordinal() + ")";
    }
    
    public static ClaimType[] getOriginalSupplementaryInvoiceTypes() {
        return new ClaimType[] {ClaimType.GTA_ORIGINAL_INVOICE,
                                ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
                                ClaimType.SUBSCRIBER_ORIGINAL_INVOICE};
    }
    
    public static ClaimType[] getAllSupplementaryInvoiceTypes() {
        return new ClaimType[] {ClaimType.GTA_SUPPLEMENTARY_INVOICE,
                                ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE,
                                ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE,
                                ClaimType.GTA_ORIGINAL_INVOICE,
                                ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
                                ClaimType.SUBSCRIBER_ORIGINAL_INVOICE};
    }
    
    public static boolean isOriginalSupplementaryInvoice(ClaimType claimType) {
        if (        claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE)
            return true;
        
        return false;
    }

}
