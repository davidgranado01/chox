package idas.chox.service.intelligentNotes;

import java.util.Calendar;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;

public class RepairBookInDateisThursdayCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean isThursday = false;
        if (c.getHireMonitoringDetail() != null) {
            if (c.getHireMonitoringDetail().getOriginalRepairBookInDate() != null) {
                isThursday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.THURSDAY;
            }
        }

        Boolean showing = isThursday && c.getCustomer()!=null && c.getCustomer().getIsUsable();
        return showing;
    }

    @Override
    public String getNote() {
        return "The repair book in date is on a Thursday and the CHO's Customer's vehicle was driveable.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 15;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Thursday Book In";
    }
}
