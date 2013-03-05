package idas.chox.core.model;

import static idas.chox.core.model.PenaltyCharge.PenaltyType;

/**
 *
 * @author John
 */
public enum ClaimType {
    /*
     * When the new claim type is added please make sure the correct 'Penalty Type' is defined
     * in the getPenaltyType(ClaimType claimType) method defined below.
     */
    GTA                                         (0, "GTA"),
    GTA_ORIGINAL_INVOICE                        (1, "GTA (Orig. Invoice)"),
    GTA_SUPPLEMENTARY_INVOICE                   (2, "GTA (Supp. Invoice)"),
    TPI                                         (3, "Third Party Intervention (TPI)"),
    INSURER_VS_INSURER                          (4, "Insurer vs. Insurer"),
    INSURER_VS_INSURER_ORIGINAL_INVOICE         (5, "Insurer vs. Insurer (Orig. Invoice)"),
    INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE    (6, "Insurer vs. Insurer (Supp. Invoice)"),
    SUBSCRIBER                                  (7, "Subscriber"),
    SUBSCRIBER_ORIGINAL_INVOICE                 (8, "Subscriber (Orig. Invoice)"),
    SUBSCRIBER_SUPPLEMENTARY_INVOICE            (9, "Subscriber (Supp. Invoice)"),
    INSURER_INVOICE                             (10, "Insurer Manual Invoice"),
    FIXED_FEE                                   (11, "Fixed Fee"),
    FIXED_FEE_ORIGINAL_INVOICE                  (12, "Fixed Fee (Orig. Invoice)"),
    FIXED_FEE_SUPPLEMENTARY_INVOICE             (13, "Fixed Fee (Supp. Invoice)");

    private final String description;
    private final int claimTypeValue;

    ClaimType(int claimTypeValue, String description) {
        this.claimTypeValue = claimTypeValue;
        this.description = description;
    }

    public int getClaimTypeValue() {
        return claimTypeValue;
    }
    
    @Override
    public String toString() {
        return description;
    }

    public static boolean isGTA(ClaimType claimType) {
        if (claimType == ClaimType.GTA
                ||  claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.GTA_SUPPLEMENTARY_INVOICE) {
            return true;
        }
        
        return false;
    }

    public static boolean isTPI(ClaimType claimType) {
        if (        claimType == ClaimType.TPI) {
            return true;
        }
        
        return false;
    }

    public static boolean allowAutomaticPenaltyCharges(ClaimType claimType) {
        if (isGTA(claimType) || isSubscriber(claimType) || isFixedFee(claimType)) {
            return true;
        }

        return false;
    }

    public static boolean isInsurerVsInsurer(ClaimType claimType) {
        if (claimType == ClaimType.INSURER_VS_INSURER
                ||  claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE) {
            return true;
        }
        
        return false;
    }

    public static boolean isInsurerUpload(ClaimType claimType) {
        if (claimType == ClaimType.INSURER_INVOICE) {
            return true;
        }
        
        return false;
    }

    public static boolean isSubscriber(ClaimType claimType) {
        if (claimType == ClaimType.SUBSCRIBER
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE) {
            return true;
        }
        
        return false;
    }

    public static boolean isFixedFee(ClaimType claimType) {
        if (claimType == ClaimType.FIXED_FEE
                ||  claimType == ClaimType.FIXED_FEE_ORIGINAL_INVOICE
                ||  claimType == ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE) {
            return true;
        }
        
        return false;
    }

    public static boolean isSupplementaryInvoice(ClaimType claimType) {
        if (claimType == ClaimType.GTA_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE
                ||  claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.FIXED_FEE_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE) {
            return true;
        }
        
        return false;
    }

    public static ClaimType[] getSupplementaryInvoiceTypes() {
        return new ClaimType[] {ClaimType.GTA_SUPPLEMENTARY_INVOICE,
                                ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE,
                                ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE,
                                ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE};
    }
    
    public static String getSupplementaryInvoiceTypeOrdinals() {
        return "(" + ClaimType.GTA_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                   + ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                   + ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                   + ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ")";
    }
    
    public static ClaimType[] getOriginalSupplementaryInvoiceTypes() {
        return new ClaimType[] {ClaimType.GTA_ORIGINAL_INVOICE,
                                ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
                                ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,
                                ClaimType.FIXED_FEE_ORIGINAL_INVOICE};
    }
    
    public static ClaimType[] getAllSupplementaryInvoiceTypes() {
        return new ClaimType[] {ClaimType.GTA_SUPPLEMENTARY_INVOICE,
                                ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE,
                                ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE,
                                ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE,
                                ClaimType.GTA_ORIGINAL_INVOICE,
                                ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
                                ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,
                                ClaimType.FIXED_FEE_ORIGINAL_INVOICE};
    }
    
    public static boolean isOriginalSupplementaryInvoice(ClaimType claimType) {
        if (claimType == ClaimType.GTA_ORIGINAL_INVOICE
                ||  claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                ||  claimType == ClaimType.FIXED_FEE_ORIGINAL_INVOICE
                ||  claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE) {
            return true;
        }
        
        return false;
    }
    
    public static PenaltyType getPenaltyType(ClaimType claimType) {
        return isSubscriber(claimType) ? PenaltyType.SUBSCRIBER : 
                isFixedFee(claimType) ? PenaltyType.FIXEDFEE : PenaltyType.DEFAULT;
    }

}
