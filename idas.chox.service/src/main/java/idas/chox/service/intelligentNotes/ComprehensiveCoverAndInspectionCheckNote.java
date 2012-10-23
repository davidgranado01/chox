package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author John
 */
public class ComprehensiveCoverAndInspectionCheckNote implements IntelligentNote {
    @Override
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
        Claim Rule: If 'Comprehensive' field is N/No/False/F in the Customer Details section of CHOX on the Claim Details tab
        then display the note below on the action panel
        for a claim in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer.
         */

        //1. If ‘Comprehensive' field is N/No/False/F
        showing = (!c.getCustomer().isComprehensive()) && !c.isManagingRepair();

        // AND
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The CHO's client does not have comprehensive insurance cover for their vehicle and the CHO is not managing the repair. Please arrange the vehicle inspection.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 3;
    }

}
