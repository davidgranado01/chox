package idas.chox.service.security;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.WebUser;

public class NotificationAccessibility {

    private short claimNumberNotificationAccessibility;
    private short userViewingNotificationAccessibility;
    private short intelligentNotesNotificationAccessibility;
    private short notificationNotesNotificationAccessibility;
    private short duplicatedSupplementaryInvoiceNotificationAccessibility;
    private short awaitingLitigationOutcomeNotificationAccessibility;

    public NotificationAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, Claim claim) {
        claimNumberNotificationAccessibility = applicationAccessibility.checkAccessibilityForClaimType(
                    ApplicationAccessibility.getNotificationAccessibilityKey(ApplicationAccessibility.NOTE_CLAIM_NUMBER,
                                                claim.getStatus(), claim.getClaimType()), user, null);
        userViewingNotificationAccessibility = applicationAccessibility.checkAccessibilityForClaimType(
                    ApplicationAccessibility.getNotificationAccessibilityKey(ApplicationAccessibility.NOTE_CLAIM_VIEWING,
                                                claim.getStatus(), claim.getClaimType()), user, null);
        intelligentNotesNotificationAccessibility = applicationAccessibility.checkAccessibilityForClaimType(
                    ApplicationAccessibility.getNotificationAccessibilityKey(ApplicationAccessibility.NOTE_CLAIM_INTELLIGENT_NOTE,
                                                claim.getStatus(), claim.getClaimType()), user, null);
        notificationNotesNotificationAccessibility = applicationAccessibility.checkAccessibilityForClaimType(
                    ApplicationAccessibility.getNotificationAccessibilityKey(ApplicationAccessibility.NOTE_CLAIM_NOTES,
                                                claim.getStatus(), claim.getClaimType()), user, null);
        duplicatedSupplementaryInvoiceNotificationAccessibility = applicationAccessibility.checkAccessibilityForClaimType(
                    ApplicationAccessibility.getNotificationAccessibilityKey(ApplicationAccessibility.NOTE_DUPLICATED_SUPPLEMENTARY_INVOICE,
                                                claim.getStatus(), claim.getClaimType()), user, null);
        awaitingLitigationOutcomeNotificationAccessibility = applicationAccessibility.checkAccessibilityForClaimType(
                    ApplicationAccessibility.getNotificationAccessibilityKey(ApplicationAccessibility.NOTE_AWAITING_LITIGATION_OUTCOME,
                                                claim.getStatus(), claim.getClaimType()), user, null);
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
