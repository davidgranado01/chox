package chox.model.intelligentNotes;

import chox.model.IntelligentNote;
import chox.data.SecurityInfoProvider;
import chox.model.*;
import scsbre.model.IVehicleClassInfo;

public class VehicleClassAboveSCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        
        Boolean showing = false;
        IVehicleClassInfo vclass = c.getCustomer().getVehicleClass();
        showing |= !(vclass.getCode().toLowerCase().startsWith("s") && !vclass.getCode().toLowerCase().startsWith("sp"));
        // showing &= securityInfoProvider.getIsINS();
        
        return showing;
    }

    public String getNote() {
        return "The vehicle class of the CHO’s client’s vehicle is above an S class.";
    }

}
