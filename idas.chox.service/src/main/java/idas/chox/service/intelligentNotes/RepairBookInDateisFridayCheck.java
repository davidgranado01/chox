package idas.chox.service.intelligentNotes;

import java.util.Calendar;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;

public class RepairBookInDateisFridayCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean isFriday = false;
        if (c.getHireMonitoringDetail() != null) {
            if (c.getHireMonitoringDetail().getOriginalRepairBookInDate() != null) {
                isFriday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.FRIDAY;
            }
        }

        Boolean showing = isFriday && c.getCustomer()!=null && c.getCustomer().getIsUsable();
        return showing;
    }

    @Override
    public String getNote() {
        return "The repair book in date is on a Friday and the CHO's Customer's vehicle was driveable.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 7;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Friday Book In";
    }
}
