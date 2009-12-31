package idas.chox.web.security;

import java.util.Set;

public class NotificationAccessibility {

    private short claimNumberNotificationAccessibility;
    private short userViewingNotificationAccessibility;
    private short intelligentNotesNotificationAccessibility;
    private short notificationNotesNotificationAccessibility;

    public NotificationAccessibility(ApplicationAccessibility applicationAccessibility, Set roles, String claimStatus) {

        claimNumberNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_NUMBER, roles, claimStatus);
        userViewingNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_VIEWING, roles, claimStatus);
        intelligentNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_INTELLIGENT_NOTE, roles, claimStatus);
        notificationNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_NOTES, roles, claimStatus);
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
