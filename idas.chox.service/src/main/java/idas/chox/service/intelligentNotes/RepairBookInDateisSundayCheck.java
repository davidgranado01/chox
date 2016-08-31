package idas.chox.service.intelligentNotes;

import java.util.Calendar;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;

public class RepairBookInDateisSundayCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean isSunday = false;
        if (c.getHireMonitoringDetail() != null) {
            if (c.getHireMonitoringDetail().getOriginalRepairBookInDate() != null) {
                isSunday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.SUNDAY;
            }
        }

        Boolean showing = isSunday && c.getCustomer()!=null && c.getCustomer().getIsUsable();
        return showing;
    }

    @Override
    public String getNote() {
        return "The repair book in date is on a Sunday and the CHO's Customer's vehicle was driveable.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 9;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Sunday Book in";
    }
}
