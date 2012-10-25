package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;

public class VehicleClassAboveSCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean showing = false;
        VehicleClass vclass = c.getCustomer().getVehicleClass();
        if (vclass != null)
            showing |= !(vclass.getName().toLowerCase().startsWith("s") && !vclass.getName().toLowerCase().startsWith("sp"));
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The vehicle class of the CHO's client's vehicle is above an S class.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 12;
    }

    @Override
    public String getIntelligentNoteName() {
        return "S Class";
    }
}
