package idas.chox.core.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author John
 */
public enum ClaimType {
    /*
     * When the new claim type is added please make sure the correct 'Penalty Type' is defined
     * in the getPenaltyType(ClaimType claimType) method defined below.
     */
    GTA(0, "GTA"),
    GTA_ORIGINAL_INVOICE(1, "GTA (Orig. Invoice)"),
    GTA_SUPPLEMENTARY_INVOICE(2, "GTA (Supp. Invoice)"),
    TPI(3, "Third Party Intervention (TPI)"),
    INSURER_VS_INSURER(4, "Insurer vs. Insurer"),
    INSURER_VS_INSURER_ORIGINAL_INVOICE(5, "Insurer vs. Insurer (Orig. Invoice)"),
    INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE(6, "Insurer vs. Insurer (Supp. Invoice)"),
    SUBSCRIBER(7, "Subscriber"),
    SUBSCRIBER_ORIGINAL_INVOICE(8, "Subscriber (Orig. Invoice)"),
    SUBSCRIBER_SUPPLEMENTARY_INVOICE(9, "Subscriber (Supp. Invoice)"),
    INSURER_INVOICE(10, "Insurer Invoice"),
    FIXED_FEE(11, "Fixed Fee"),
    FIXED_FEE_ORIGINAL_INVOICE(12, "Fixed Fee (Orig. Invoice)"),
    FIXED_FEE_SUPPLEMENTARY_INVOICE(13, "Fixed Fee (Supp. Invoice)"),
    INSURER_CLAIM(14, "Insurer Claim"),
    INSURER_ORIGINAL_INVOICE(15, "Insurer Claim (Orig. Invoice)"),
    INSURER_SUPPLEMENTARY_INVOICE(16, "Insurer Claim  (Supp. Invoice)"),
    INSURER_UPLOAD(17, "Insurer Upload"),
    COLLABORATION_PROTOCOL(18, "Collaboration Protocol"),
    COLLABORATION_PROTOCOL_ORIGINAL_INVOICE(19, "Collaboration Protocol (Orig. Invoice)"),
    COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE(20, "Collaboration Protocol (Supp. Invoice)");

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

    public static List<ClaimType> getMainClaimTypes() {
        return Arrays.asList(new ClaimType[]{GTA, SUBSCRIBER, FIXED_FEE, COLLABORATION_PROTOCOL, TPI, INSURER_VS_INSURER, INSURER_UPLOAD});
    }

    public static ClaimType getResolvedClaimType(ClaimType claimType) {
        switch (claimType) {
            case GTA:
            case GTA_ORIGINAL_INVOICE:
            case GTA_SUPPLEMENTARY_INVOICE:
                return GTA;

            case TPI:
                return TPI;
            case INSURER_VS_INSURER:
            case INSURER_VS_INSURER_ORIGINAL_INVOICE:
            case INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE:
                return INSURER_VS_INSURER;

            case SUBSCRIBER:
            case SUBSCRIBER_ORIGINAL_INVOICE:
            case SUBSCRIBER_SUPPLEMENTARY_INVOICE:
                return SUBSCRIBER;

            case FIXED_FEE:
            case FIXED_FEE_ORIGINAL_INVOICE:
            case FIXED_FEE_SUPPLEMENTARY_INVOICE:
                return FIXED_FEE;

            case INSURER_INVOICE:
            case INSURER_CLAIM:
            case INSURER_ORIGINAL_INVOICE:
            case INSURER_SUPPLEMENTARY_INVOICE:
            case INSURER_UPLOAD:
                return INSURER_UPLOAD;

            case COLLABORATION_PROTOCOL:
            case COLLABORATION_PROTOCOL_ORIGINAL_INVOICE:
            case COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE:
                return COLLABORATION_PROTOCOL;

            default:
                return null;
        }

    }

    public static boolean isGTA(ClaimType claimType) {
        return claimType == ClaimType.GTA
                || claimType == ClaimType.GTA_ORIGINAL_INVOICE
                || claimType == ClaimType.GTA_SUPPLEMENTARY_INVOICE;
    }

    public static boolean isTPI(ClaimType claimType) {
        return claimType == ClaimType.TPI;
    }

    public static boolean isInsurerVsInsurer(ClaimType claimType) {
        return claimType == ClaimType.INSURER_VS_INSURER
                || claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE
                || claimType == ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE;
    }

    public static boolean isInsurerUpload(ClaimType claimType) {
        return claimType == ClaimType.INSURER_INVOICE || claimType == INSURER_CLAIM || claimType == INSURER_UPLOAD
                || claimType == ClaimType.INSURER_ORIGINAL_INVOICE || claimType == ClaimType.INSURER_SUPPLEMENTARY_INVOICE;
    }

    public static boolean isSubscriber(ClaimType claimType) {
        return claimType == ClaimType.SUBSCRIBER
                || claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                || claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE;
    }

    public static boolean isFixedFee(ClaimType claimType) {
        return claimType == ClaimType.FIXED_FEE
                || claimType == ClaimType.FIXED_FEE_ORIGINAL_INVOICE
                || claimType == ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE;
    }

    public static boolean isCollaborationProtocol(ClaimType claimType) {
        return claimType == ClaimType.COLLABORATION_PROTOCOL
                || claimType == ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE
                || claimType == ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE;
    }

    public static boolean isSupplementaryInvoice(ClaimType claimType) {
        return claimType == ClaimType.GTA_SUPPLEMENTARY_INVOICE
                || claimType == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE
                || claimType == ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE
                || claimType == ClaimType.GTA_ORIGINAL_INVOICE
                || claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                || claimType == ClaimType.FIXED_FEE_ORIGINAL_INVOICE
                || claimType == ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE
                || claimType == ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE
                || claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE
                || claimType == ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE
                || claimType == ClaimType.INSURER_ORIGINAL_INVOICE
                || claimType == ClaimType.INSURER_SUPPLEMENTARY_INVOICE;
    }

    public static ClaimType[] getSupplementaryInvoiceTypes() {
        return new ClaimType[]{ClaimType.GTA_SUPPLEMENTARY_INVOICE,
            ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE,
            ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE,
            ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE,
            ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE,
            ClaimType.INSURER_SUPPLEMENTARY_INVOICE};
    }

    public static ClaimType[] getClaimTypeList(ClaimType type, boolean excludeSupplementary) {
        if (excludeSupplementary) {
            switch (type) {
                case GTA:
                    return new ClaimType[]{ClaimType.GTA,
                        ClaimType.GTA_ORIGINAL_INVOICE};
                case TPI:
                    return new ClaimType[]{ClaimType.TPI};
                case SUBSCRIBER:
                    return new ClaimType[]{ClaimType.SUBSCRIBER,
                        ClaimType.SUBSCRIBER_ORIGINAL_INVOICE};
                case FIXED_FEE:
                    return new ClaimType[]{ClaimType.FIXED_FEE,
                        ClaimType.FIXED_FEE_ORIGINAL_INVOICE};
                case INSURER_VS_INSURER:
                    return new ClaimType[]{ClaimType.INSURER_VS_INSURER,
                        ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE};

                case INSURER_INVOICE:
                case INSURER_CLAIM:
                case INSURER_UPLOAD:
                    return new ClaimType[]{ClaimType.INSURER_UPLOAD,
                        ClaimType.INSURER_ORIGINAL_INVOICE};
                default:
                    return new ClaimType[]{};

            }
        } else {
            switch (type) {
                case GTA:
                    return new ClaimType[]{ClaimType.GTA,
                        ClaimType.GTA_ORIGINAL_INVOICE,
                        ClaimType.GTA_SUPPLEMENTARY_INVOICE};
                case TPI:
                    return new ClaimType[]{ClaimType.TPI};
                case SUBSCRIBER:
                    return new ClaimType[]{ClaimType.SUBSCRIBER,
                        ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,
                        ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE};
                case FIXED_FEE:
                    return new ClaimType[]{ClaimType.FIXED_FEE,
                        ClaimType.FIXED_FEE_ORIGINAL_INVOICE,
                        ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE};
                case INSURER_VS_INSURER:
                    return new ClaimType[]{ClaimType.INSURER_VS_INSURER,
                        ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
                        ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE};

                case INSURER_INVOICE:
                case INSURER_CLAIM:
                case INSURER_UPLOAD:
                    return new ClaimType[]{ClaimType.INSURER_UPLOAD,
                        ClaimType.INSURER_ORIGINAL_INVOICE,
                        ClaimType.INSURER_SUPPLEMENTARY_INVOICE};
                default:
                    return new ClaimType[]{};

            }
        }

    }

    public static String getSupplementaryInvoiceTypeOrdinals() {
        return "(" + ClaimType.GTA_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                + ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                + ClaimType.INSURER_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                + ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                + ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                + ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ")";
    }

    public static String getInsurerUploadTypeOrdinals() {
        return "(" + ClaimType.INSURER_CLAIM.getClaimTypeValue() + ","
                + ClaimType.INSURER_INVOICE.getClaimTypeValue() + ","
                + ClaimType.INSURER_ORIGINAL_INVOICE.getClaimTypeValue() + ","
                + ClaimType.INSURER_SUPPLEMENTARY_INVOICE.getClaimTypeValue() + ","
                + ClaimType.INSURER_UPLOAD.getClaimTypeValue() + ")";
    }

    public static ClaimType[] getOriginalSupplementaryInvoiceTypes() {
        return new ClaimType[]{ClaimType.GTA_ORIGINAL_INVOICE,
            ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
            ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,
            ClaimType.INSURER_ORIGINAL_INVOICE,
            ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE,
            ClaimType.FIXED_FEE_ORIGINAL_INVOICE};
    }

    public static ClaimType[] getAllSupplementaryInvoiceTypes() {
        return new ClaimType[]{ClaimType.GTA_SUPPLEMENTARY_INVOICE,
            ClaimType.INSURER_VS_INSURER_SUPPLEMENTARY_INVOICE,
            ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE,
            ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE,
            ClaimType.COLLABORATION_PROTOCOL_SUPPLEMENTARY_INVOICE,
            ClaimType.GTA_ORIGINAL_INVOICE,
            ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE,
            ClaimType.SUBSCRIBER_ORIGINAL_INVOICE,
            ClaimType.FIXED_FEE_ORIGINAL_INVOICE,
            ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE,
            ClaimType.INSURER_ORIGINAL_INVOICE,
            ClaimType.INSURER_SUPPLEMENTARY_INVOICE};
    }

    public static boolean isOriginalSupplementaryInvoice(ClaimType claimType) {
        return claimType == ClaimType.GTA_ORIGINAL_INVOICE
                || claimType == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                || claimType == ClaimType.FIXED_FEE_ORIGINAL_INVOICE
                || claimType == ClaimType.COLLABORATION_PROTOCOL_ORIGINAL_INVOICE
                || claimType == ClaimType.INSURER_ORIGINAL_INVOICE
                || claimType == ClaimType.INSURER_VS_INSURER_ORIGINAL_INVOICE;
    }
}
