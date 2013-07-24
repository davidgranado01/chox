package idas.chox.core.xmlValidation;

/**
 *
 * @author John
 */
public enum RentalStatus {
    INPROGRESS("inprogress"),
    COMPLETE("complete"),
    OFFHIRED("offhired"),
    HIREMONITORING("hiremonitoring"),
    SUPPLEMENTARYINVOICE("supplementaryinvoice"),
    INSURERVSINSURER("insurervsinsurer"),
    INVOICEONLY("invoiceonly"),
    INSURERUPLOAD("insurerupload"),
    SUBSCRIBER("subscriber"),
    FIXEDFEE("fixedfee"),
    COLLABORATION("collaborationprotocol");

    private String description;
    
    private RentalStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() { return description;}

    public static RentalStatus fromString(String str) {
        if (str != null) {
            for (RentalStatus rentalStatus : RentalStatus.values()) {
                if (str.equalsIgnoreCase(rentalStatus.description)) {
                    return rentalStatus;
                }
            }
        } else {
            throw new IllegalArgumentException("No RentalStatus from (null) description");
        }
        throw new IllegalArgumentException("No RentalStatus with description '" + str + "' found");
    }

    public static boolean isInProgressOrComplete(String rentalStatus) {
        if (RentalStatus.COMPLETE.getDescription().equals(rentalStatus)
                || RentalStatus.INPROGRESS.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isOffHiredRentalStatus(String rentalStatus) {
        if (RentalStatus.OFFHIRED.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isSupplementaryInvoiceRentalStatus(String rentalStatus) {
        if (RentalStatus.SUPPLEMENTARYINVOICE.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isHireMonitoringRentalStatus(String rentalStatus) {
        if (RentalStatus.HIREMONITORING.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isInsurerVsInsurerRentalStatus(String rentalStatus) {
        if (RentalStatus.INSURERVSINSURER.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isInsurerUploadRentalStatus(String rentalStatus) {
        if (RentalStatus.INVOICEONLY.getDescription().equals(rentalStatus)
                || RentalStatus.INSURERUPLOAD.getDescription().equals(rentalStatus)
                || RentalStatus.INPROGRESS.getDescription().equals(rentalStatus)
                || RentalStatus.COMPLETE.getDescription().equals(rentalStatus)
                || RentalStatus.OFFHIRED.getDescription().equals(rentalStatus)
                || RentalStatus.HIREMONITORING.getDescription().equals(rentalStatus)
                || RentalStatus.SUPPLEMENTARYINVOICE.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isSubscriberRentalStatus(String rentalStatus) {
        if (RentalStatus.SUBSCRIBER.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isFixedFeeRentalStatus(String rentalStatus) {
        if (RentalStatus.FIXEDFEE.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isCollaborationProtocolRentalStatus(String rentalStatus) {
        if (RentalStatus.COLLABORATION.getDescription().equals(rentalStatus)) {
            return true;
        }

        return false;
    }

    public static boolean isValid(String rentalStatus) {
        for (RentalStatus status : RentalStatus.values()) {
            if (status.getDescription().equals(rentalStatus)) {
                return true;
            }
        }
        
        return false;         
    }
}
