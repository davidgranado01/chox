package idas.chox.service.intelligentNotes;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author John
 */
public class ClaimUploadDateExceeds48FromContact implements IntelligentNote {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimUploadDateExceeds48FromContact.class);

    @Override
    public Boolean isShowingFor(Claim c) {
        Boolean showing = false;

        /* Claim Rule:
         *      if the claim upload date is greater than 48 hours after the
         *      policy holder contact date then display the message (Saturdays
         *      and Sundays excluded)
         */
        long diffInDays = daysBetween(c.getPolicyHolderContactDate(), c.getCreatedDate());
        if (diffInDays >= 2) {
            LOG.debug("Adding ClaimUploadDateExceeds48FromContact intelligent note (difference is {} days.", diffInDays);
            showing = true;
        }

        return showing;
    }

    
    @Override
    public String getNote() {
        return "The notification of this claim is over 48 hours from the date the non-fault party was contacted.";
    }

    
    private static long daysBetween(Date startDate, Date endDate) {
        LOG.debug("Calculating daysBetween '{}' and '{}'", startDate, endDate);
        Calendar start = Calendar.getInstance(); start.setTime(startDate);
        Calendar end = Calendar.getInstance(); end.setTime(endDate);
        boolean swapped = false;
        if (start.equals(end)) {
            return 0;
        }
        else if (start.after(end)) {  // swap dates so that start is before end
            Calendar swap = start;
            start = end;
            end = swap;
            swapped = true;
        }

        long daysBetween = -1;
        long daysBetweenExcludingWeekends = -1;
        while (start.before(end)) {
            daysBetween++;
            if (start.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                daysBetweenExcludingWeekends++;
            }
            start.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (swapped) {
            daysBetween *= -1;
            daysBetweenExcludingWeekends *= -1;
        }
        LOG.debug("Returning daysBetweenExcludingWeekends={}", daysBetweenExcludingWeekends);
        return daysBetweenExcludingWeekends;
    }

    @Override
    public int getIntelligentNoteId() {
        return 2;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Notification Delay";
    }
}
