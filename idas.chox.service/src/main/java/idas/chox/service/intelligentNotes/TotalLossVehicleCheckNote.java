package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author emmanuel
 */
public class TotalLossVehicleCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
        Claim Rule: If the 'Total Loss' field has an Y/Yes then display note below
        on the action panel for a claim in status ClaimUnacknowledgedRouted, ClaimPending,
        laimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer for Insurer roles only.
         */

        //1. If the 'Total Loss' field has an Y/Yes
        showing |= c.getCustomer().getIsTotalLoss();

        //2.  Insurer roles only
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The CHO's client's vehicle is deemed a total loss.";
    }

    @Override
    public int getIntelligentNoteId() {
        // TODO Auto-generated method stub
        return 10;
    }
}
