package chox.model.intelligentNotes;

import chox.model.IntelligentNote;
import chox.Util.DateHelper;
import chox.data.SecurityInfoProvider;
import chox.model.*;
import java.util.Calendar;

public class RepairBookInDateisSundayCheck implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {

        Boolean showing = false;

        Boolean isStatus = false;
        if(c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED)
                || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_PENDING)
                || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REJECTION_CONTESTED)
                || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UPDATE_BY_ENG)
                || c.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_REF_TO_ENG)){
            isStatus = true;
        }

        Boolean isSunday = false;
        if(c.getHireMonitoringDetail()!=null){
            if(c.getHireMonitoringDetail().getOriginalRepairBookInDate()!=null){
                isSunday = DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.SUNDAY;
            }
        }

        showing = isStatus;
        showing &= isSunday;
        return showing;
    }

    public String getNote() {
        return "The repair book in date is on a Sunday";
    }

}
