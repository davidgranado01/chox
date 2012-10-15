package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.util.DateHelper;
import java.util.Calendar;

public class RepairBookInDateisThursdayCheck implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {

        Boolean isStatus = false;
        if (c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_CONTESTED) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UPDATE_BY_ENG) || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REF_TO_ENG)) {
            isStatus = true;
        }

        Boolean isThursday = false;
        if (c.getHireMonitoringDetail() != null) {
            if (c.getHireMonitoringDetail().getOriginalRepairBookInDate() != null) {
                isThursday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.THURSDAY;
            }
        }

        Boolean showing = isStatus & isThursday & c.getCustomer().getIsUsable();
        return showing;
    }

    @Override
    public String getNote() {
        return "The repair book in date is on a Thursday and the CHO's Customer's vehicle was driveable.";
    }
}
