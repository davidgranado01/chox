package idas.chox.service.intelligentNotes;

import java.util.Calendar;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;

public class RepairBookInDateisSaturdayCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean isSaturday = false;
        if (c.getHireMonitoringDetail() != null) {
            if (c.getHireMonitoringDetail().getOriginalRepairBookInDate() != null) {
                isSaturday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.SATURDAY;
            }
        }

        Boolean showing = isSaturday && c.getCustomer()!=null && c.getCustomer().getIsUsable();
        return showing;
    }

    @Override
    public String getNote() {
        return "The repair book in date is on a Saturday and the CHO's Customer's vehicle was driveable.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 8;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Saturday Book In";
    }
}
