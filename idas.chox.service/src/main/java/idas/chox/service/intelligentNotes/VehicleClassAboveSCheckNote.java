package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;

public class VehicleClassAboveSCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {

        Boolean showing = false;
        VehicleClass vclass = c.getCustomer().getVehicleClass();
        showing |= !(vclass.getCode().toLowerCase().startsWith("s") && !vclass.getCode().toLowerCase().startsWith("sp"));
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The vehicle class of the CHO’s client’s vehicle is above an S class.";
    }
}
