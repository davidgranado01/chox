/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.Util.DateHelper;
import chox.data.SecurityInfoProvider;
import chox.model.*;
import java.util.Calendar;

public class RepairBookInDateCheck implements IntelligentNote {

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

        showing = isStatus;

        if(c.getHireMonitoringDetail()!=null){
            if(c.getHireMonitoringDetail().getOriginalRepairBookInDate()!=null){
                showing &= DateHelper.getDay(c.getHireMonitoringDetail().getOriginalRepairBookInDate()) == Calendar.FRIDAY;
            }
        }
        
        return showing;
    }

    public String getNote() {
        return "The repair book in date is on a Friday";
    }

}
