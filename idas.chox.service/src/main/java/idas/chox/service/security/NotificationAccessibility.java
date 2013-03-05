package idas.chox.service.security;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;

public class NotificationAccessibility {
    private static final String NOTE_CLAIM_NUMBER = "ClaimNumberNotification";
    private static final String NOTE_CLAIM_VIEWING = "UserViewingNotification";
    private static final String NOTE_CLAIM_INTELLIGENT_NOTE = "IntelligentNotesNotification";
    private static final String NOTE_CLAIM_NOTES = "NotificationNotesNotification";
    private static final String NOTE_DUPLICATED_SUPPLEMENTARY_INVOICE = "DuplicatedSupplementaryInvoiceNotification";
    private static final String NOTE_AWAITING_LITIGATION_OUTCOME = "AwaitingLitigationOutcomeNotification";

    private short claimNumberNotificationAccessibility;
    private short userViewingNotificationAccessibility;
    private short intelligentNotesNotificationAccessibility;
    private short notificationNotesNotificationAccessibility;
    private short duplicatedSupplementaryInvoiceNotificationAccessibility;
    private short awaitingLitigationOutcomeNotificationAccessibility;

    public NotificationAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, Claim claim) {
        claimNumberNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(
                                                    NOTE_CLAIM_NUMBER, user, claim);
        userViewingNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(
                                                    NOTE_CLAIM_VIEWING, user, claim);
        intelligentNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(
                                                    NOTE_CLAIM_INTELLIGENT_NOTE, user, claim);
        notificationNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(
                                                    NOTE_CLAIM_NOTES, user, claim);
        duplicatedSupplementaryInvoiceNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(
                                                    NOTE_DUPLICATED_SUPPLEMENTARY_INVOICE, user, claim);
        awaitingLitigationOutcomeNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(
                                                    NOTE_AWAITING_LITIGATION_OUTCOME, user, claim);
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

    public boolean getAwaitingLitigationOutcomeNotificationAccessibility() {
        return awaitingLitigationOutcomeNotificationAccessibility > 0;
    }
}
