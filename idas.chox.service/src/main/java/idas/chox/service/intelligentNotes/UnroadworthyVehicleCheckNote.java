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

        /* Claim Rule:
         *      If the ‘Is Usable' field has an N/No then display the note
         */

        //1. If the ‘Is Usable' field has an N/No
        showing |= !c.getCustomer().getIsUsable();

        return showing;
    }

    @Override
    public String getNote() {
        return "The CHO's client's vehicle is deemed unroadworthy.";
    }
}
