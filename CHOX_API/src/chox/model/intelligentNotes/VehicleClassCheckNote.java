/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.model.IntelligentNote;
import chox.Util.DateHelper;
import chox.data.SecurityInfoProvider;
import chox.model.*;
import java.util.Date;
import scsbre.model.IVehicleClassInfo;

/**
 *
 * @author emmanuel
 */
public class VehicleClassCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {

        Boolean showing = false;

        /*
        IVehicleClassInfo vclass = c.getVClass();
        showing |= vclass.getCode().toLowerCase().startsWith("p");
        */
        
        // EDITED BY CARLSON TO EXLUCED PV Vehicle Class
        // 1. Customer Vehicle Class field is P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13

        IVehicleClassInfo vclass = c.getCustomer().getVehicleClass();
        showing |= (vclass.getCode().toLowerCase().startsWith("p") && !vclass.getCode().toLowerCase().startsWith("pv"));
        showing |= vclass.getCode().toLowerCase().startsWith("sp");
        
        //2. ECD is less than 5 days from Policy Holder Contact Date
        Date latestEcdDate= c.getLatestHireMonitoringEcd();
        Date policyHolderContactDate = c.getPolicyHolderContactDate();

        if(latestEcdDate != null && policyHolderContactDate != null)
        {
            long dayBetween = DateHelper.daysBetween(policyHolderContactDate, latestEcdDate);
            showing &= dayBetween < 5;
        }
        else
        {
            showing = false;
        }

        // AND
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The CHO’s client’s vehicle is of a Prestige/Sports Performance nature and is entering into a short repair period. Review need for replacement vehicle.";
    }

}
