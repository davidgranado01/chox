/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.data.SecurityInfoProvider;
import chox.model.*;

/**
 *
 * @author emmanuel
 */
public class ComprehensiveCoverCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
             Claim Rule: If ‘Comprehensive’ field is N/No/False/F in the Customer Details section of CHOX on the Claim Details tab
             then display the note below on the action panel
             for a claim in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer.
         */

        //1. If ‘Comprehensive’ field is N/No/False/F
        showing = !c.getCustomer().isComprehensive();

        // AND
        // showing &= securityInfoProvider.getIsINS();
        
        return showing;
    }

    public String getNote() {
        return "The CHO’s client does not have comprehensive insurance cover for their vehicle.";
    }

}
