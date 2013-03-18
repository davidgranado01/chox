package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author John
 */
public class HireCommenced48hSinceNotificationCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {
        /* Claim Rule:
         *      This rule is only valid/executed when there is a value in the
         *      'Hire Start' field. If there is a value then compare the Claim
         *      Upload Date with the 'Hire Start' date, if the difference is more
         *      than 48 hours i.e. the hire started more than 48 hours ago
         *      from the claim upload then this note should be displayed
         */
        Boolean showing = false;

        if (c.getVehicleHire() != null && c.getVehicleHire().getHireStart() != null) {
            long diffInMillis = c.getCreatedDate().getTime() - c.getVehicleHire().getHireStart().getTime();
            if (diffInMillis / (1000*60*60.0) > 48.0) {
                showing = true;
            }
        }

        return showing;
    }

    @Override
    public String getNote() {
        return "The notification of this claim is over 48 hours from the date the hire commenced.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 14;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Hire Started Before Notification";
    }
}
