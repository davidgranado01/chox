package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class NotificationAccessibility {

    private short claimNumberNotificationAccessibility;
    private short userViewingNotificationAccessibility;
    private short intelligentNotesNotificationAccessibility;
    private short notificationNotesNotificationAccessibility;
    private short duplicatedSupplementaryInvoiceNotificationAccessibility;

    public NotificationAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, String claimStatus) {
        claimNumberNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_NUMBER, user, claimStatus);
        userViewingNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_VIEWING, user, claimStatus);
        intelligentNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_INTELLIGENT_NOTE, user, claimStatus);
        notificationNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_NOTES, user, claimStatus);
        duplicatedSupplementaryInvoiceNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_DUPLICATED_SUPPLEMENTARY_INVOICE, user, claimStatus);
    }

    public boolean getDuplicatedSupplementaryInvoiceNotificationAccessibility() {
        return duplicatedSupplementaryInvoiceNotificationAccessibility > 0;
    }

    public boolean getClaimNumberNotificationAccessibility() {
        return claimNumberNotificationAccessibility > 0;
    }

    public boolean getIntelligentNotesNotificationAccessibility() {
        return intelligentNotesNotificationAccessibility > 0;
    }

    public boolean getNotificationNotesNotificationAccessibility() {
        return notificationNotesNotificationAccessibility > 0;
    }

    public boolean getUserViewingNotificationAccessibility() {
        return userViewingNotificationAccessibility > 0;
    }
}
