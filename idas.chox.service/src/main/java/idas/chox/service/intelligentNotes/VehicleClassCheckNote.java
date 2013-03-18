package idas.chox.service.intelligentNotes;

import java.util.Date;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.util.DateHelper;

/**
 *
 * @author emmanuel
 */
public class VehicleClassCheckNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {

        Boolean showing = false;

        // EDITED TO EXLUCED PV Vehicle Class
        // 1. Customer Vehicle Class field is P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13

        VehicleClass vclass = c.getCustomer().getVehicleClass();
        if (vclass != null) {
            showing |= (vclass.getName().toLowerCase().startsWith("p") && !vclass.getName().toLowerCase().startsWith("pv"));
            showing |= vclass.getName().toLowerCase().startsWith("sp");
        }

        //2. ECD is less than 5 days from Policy Holder Contact Date
        Date latestEcdDate = c.getLatestHireMonitoringEcd();
        Date policyHolderContactDate = c.getPolicyHolderContactDate();

        if (latestEcdDate != null && policyHolderContactDate != null) {
            long dayBetween = DateHelper.getNumberOf24HourPeriodsBetween(policyHolderContactDate, latestEcdDate);
            showing &= dayBetween < 5;
        } else {
            showing = false;
        }

        return showing;
    }

    @Override
    public String getNote() {
        return "The CHO's client's vehicle is of a Prestige/Sports Performance nature and is entering into a short repair period. Review need for replacement vehicle.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 13;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Prestige Short Repair";
    }
}
