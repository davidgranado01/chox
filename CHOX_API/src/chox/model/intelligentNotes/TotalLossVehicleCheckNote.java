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
public class TotalLossVehicleCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
            Claim Rule: If the ‘Total Loss’ field has an Y/Yes then display note below
            on the action panel for a claim in status ClaimUnacknowledgedRouted, ClaimPending,
            laimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer for Insurer roles only.
         */

        //1. If the ‘Total Loss’ field has an Y/Yes
        showing |= c.getCustomer().getIsTotalLoss();

        //2.  Insurer roles only
        showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The CHO’s client’s vehicle is deemed a total loss.";
    }

}
