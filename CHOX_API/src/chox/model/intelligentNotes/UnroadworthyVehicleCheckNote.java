/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model.intelligentNotes;

import chox.data.SecurityInfoProvider;
import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.IntelligentNote;
import java.util.Arrays;
import java.util.List;

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

         //2. claim must in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
        String[] statuses = {ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
            ClaimStatus.CLAIM_PENDING,ClaimStatus.CLAIM_REJECTION_CONTESTED,
            ClaimStatus.CLAIM_UPDATE_BY_ENG,ClaimStatus.CLAIM_REF_TO_ENG};

        List<String> statusList  = Arrays.asList(statuses);

        showing &= statusList.contains(c.getStatus());

        //3. if user is Insurer
        showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The CHO’s client’s vehicle is deemed unroadworthy.";
    }
}
