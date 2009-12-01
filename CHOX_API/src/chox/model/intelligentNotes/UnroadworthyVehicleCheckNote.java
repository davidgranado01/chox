/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model.intelligentNotes;

import chox.data.SecurityInfoProvider;
import chox.model.Claim;
import chox.model.IntelligentNote;

/**
 *
 * @author emmanuel
 */
public class UnroadworthyVehicleCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        //Claim Rule: If the ‘Is Usable’ field has an N/No then display note below on the action panel for a
        //claim in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer
        //and ClaimReferredToEngineer for Insurer roles only.

        //1. If the ‘Is Usable’ field has an N/No
        showing |= !c.getCustomerVehicleDamage().getIsUsable();
        //2. if user is Insurer
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The CHO’s client’s vehicle is deemed unroadworthy.";
    }
}
