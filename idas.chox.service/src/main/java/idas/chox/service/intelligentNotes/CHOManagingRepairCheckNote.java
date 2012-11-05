package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;

public class CHOManagingRepairCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {
        Boolean showing = false;

        /*
         * Claim Rule: If the ‘Managing Repair' field has an Y/Yes then display note
         */
        showing |= c.getManagingRepair();

        return showing;
    }

    
    @Override
    public String getNote() {
        return "The CHO is managing the repair.";
    }
}
