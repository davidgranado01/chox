package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;

public class VehicleClassAboveSCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {

        Boolean showing = false;
        VehicleClass vclass = c.getCustomer().getVehicleClass();
        showing |= !(vclass.getName().toLowerCase().startsWith("s") && !vclass.getName().toLowerCase().startsWith("sp"));
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    @Override
    public String getNote() {
        return "The vehicle class of the CHO's client's vehicle is above an S class.";
    }
}
