package idas.chox.service.intelligentNotes;

import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author emmanuel
 */
public class NeedForSPandPClassCheckWithoutECDNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c) {
        Boolean showing = false;

        /* Claim Rule:
         *      If Customer Vehicle Class field is
         *          P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,
         *          SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13
         *      AND no ECD has been provided AND the ‘Is Usable' field is ‘Y/Yes/T/True'
         *      then display the note
         */

        VehicleClass vclass = c.getCustomer().getVehicleClass();
        // 1. Customer Vehicle Class field is P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13
        if (vclass != null) {
            showing |= (vclass.getName().toLowerCase().startsWith("p") && !vclass.getName().toLowerCase().startsWith("pv"));
            showing |= vclass.getName().toLowerCase().startsWith("sp");
        }
        
        // 2. no ECD has been provided
        showing &= !isEcdExist(c);

        // 3. the ‘Is Usable' field is ‘Y/Yes/T/True'
        showing &= c.getCustomer()!=null && c.getCustomer().getIsUsable();

        // 4. If Managing Repair?' field is 'N' (No) [todo item 6.17.1]
        showing &= !c.isManagingRepair();
                
        // 5. If 'Non-Fault Insurer Managing Repair?' field is 'N' (No)  [todo item 6.17.1]
        if (c.getHireMonitoringDetail() != null) {
            showing &= !c.getHireMonitoringDetail().isIsNFInsurerManagingRepair();
        }
        else {
            showing = false;
        }

        return showing;
    }

    private boolean isEcdExist(Claim c) {

        boolean bFlag = false;

        if (c.getCustomer().getInitialECD() != null || (c.getHireMonitoringEcds().size() > 0)) {
            bFlag = true;
        }

        return bFlag;
    }

    @Override
    public String getNote() {
        return "The CHO's client's vehicle is of a Prestige/Sports Performance nature, no ECD has been provided and the vehicle is roadworthy/usable and is not managed by the CHO or TPI. Manage repair book in date.";
    }

    @Override
    public int getIntelligentNoteId() {
        return 6;
    }

    @Override
    public String getIntelligentNoteName() {
        return "Prestige No ECD Mobile Vehicle";
    }
}
