package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author emmanuel
 */
public class UnroadworthyVehicleCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {
        Boolean showing = false;

        //Claim Rule: If the ‘Is Usable' field has an N/No then display note below on the action panel for a
        //claim in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer
        //and ClaimReferredToEngineer for Insurer roles only.

        //1. If the ‘Is Usable' field has an N/No
        showing |= !c.getCustomer().getIsUsable();
        //2. if user is Insurer
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The CHO's client's vehicle is deemed unroadworthy.";
    }
}
