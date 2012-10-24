package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;
import java.util.Calendar;

public class RepairBookInDateisFridayCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean isStatus = false;
        if (c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_CONTESTED) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UPDATE_BY_ENG) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REF_TO_ENG)) {
            isStatus = true;
        }

        Boolean isFriday = false;
        if (c.getHireMonitoringDetail() != null) {
            if (c.getHireMonitoringDetail().getOriginalRepairBookInDate() != null) {
                isFriday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.FRIDAY;
            }
        }

        Boolean showing = isStatus & isFriday & c.getCustomer().getIsUsable();
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
