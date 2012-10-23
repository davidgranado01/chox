package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

public class FrontalDamageCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
        Claim Rule: If the ‘Vehicle Damage' field has the text string 'front'
        (ensure the search is not case sensitive and also include if front is joined to another word e.g. frontal)
        then display note below on the action panel for a claim in status ClaimUnacknowledgedRouted, ClaimPending,
        ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer for Insurer roles only.
         */

        //1. If the ‘Vehicle Damage' field has the text string 'front'
        showing |= c.getCustomer().getDamage().toLowerCase().contains("front");

        //2. if user is Insurer
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The damage to the CHO's client's vehicle has been identified as possible frontal damage.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 5;
    }
}
