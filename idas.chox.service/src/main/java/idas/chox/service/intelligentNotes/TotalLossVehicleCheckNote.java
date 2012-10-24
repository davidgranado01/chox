package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author emmanuel
 */
public class TotalLossVehicleCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {
        Boolean showing = false;
        
        /*
        Claim Rule: If the 'Total Loss' field has an Y/Yes then display note below
        on the action panel for a claim in status ClaimUnacknowledgedRouted, ClaimPending,
        laimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer for Insurer roles only.
         */

        //1. If the 'Total Loss' field has an Y/Yes
        //2. If Managing Repair?' field is 'N' (No) [todo item 6.17.1]
        //3. If 'Non-Fault Insurer Managing Repair?' field is 'N' (No)  [todo item 6.17.1]
        if (c.getHireMonitoringDetail() != null)
            showing = c.getCustomer().getIsTotalLoss() & !c.isManagingRepair()
                        & !c.getHireMonitoringDetail().isIsNFInsurerManagingRepair();

        //2.  Insurer roles only
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The CHO's client's vehicle is deemed a total loss and is not being managed by the CHO or the TPI. Please arrange inspection.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 10;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Total Loss Not Managing Repair";
    }
}
