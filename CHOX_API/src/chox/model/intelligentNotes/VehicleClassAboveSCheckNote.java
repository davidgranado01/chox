/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.data.SecurityInfoProvider;
import chox.model.*;
import scsbre.model.IVehicleClassInfo;

/**
 *
 * @author emmanuel
 */
public class VehicleClassAboveSCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        IVehicleClassInfo vclass = c.getVClass();
        //1. Customer Vehicle Class field is > S
        showing |= vclass.getCode().toLowerCase().startsWith("s");
        showing &= !vclass.getCode().toLowerCase().startsWith("sp");

         //2. if user is Insurer
        showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The vehicle class of the CHO’s client’s vehicle is above an S class.";
    }

}
