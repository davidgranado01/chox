package idas.chox.web.security;

import org.springframework.security.GrantedAuthority;

public class NotificationAccessibility {

    private short claimNumberNotificationAccessibility;
    private short userViewingNotificationAccessibility;
    private short intelligentNotesNotificationAccessibility;
    private short notificationNotesNotificationAccessibility;

    public NotificationAccessibility(ApplicationAccessibility applicationAccessibility, GrantedAuthority[] grantedAuthorities, String claimStatus) {

        claimNumberNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_NUMBER, grantedAuthorities, claimStatus);
        userViewingNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_VIEWING, grantedAuthorities, claimStatus);
        intelligentNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_INTELLIGENT_NOTE, grantedAuthorities, claimStatus);
        notificationNotesNotificationAccessibility = applicationAccessibility.checkNotificationAccessibility(ApplicationAccessibility.NOTE_CLAIM_NOTES, grantedAuthorities, claimStatus);
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
