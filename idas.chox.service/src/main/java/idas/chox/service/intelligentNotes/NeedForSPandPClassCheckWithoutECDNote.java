/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.Claim;
import idas.chox.core.model.IntelligentNote;
import idas.chox.core.model.VehicleClass;

/**
 *
 * @author emmanuel
 */
public class NeedForSPandPClassCheckWithoutECDNote implements IntelligentNote {

    @Override
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
        Claim Rule: If Customer Vehicle Class field is
        P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13
        AND no ECD has been provided
        AND the ‘Is Usable' field is ‘Y/Yes/T/True' then display note below on the action panel
        for a claim in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer.
         */

        VehicleClass vclass = c.getCustomer().getVehicleClass();
        // 1. Customer Vehicle Class field is P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13
        // showing |= vclass.getCode().toLowerCase().startsWith("p");
        showing |= (vclass.getCode().toLowerCase().startsWith("p") && !vclass.getCode().toLowerCase().startsWith("pv"));
        showing |= vclass.getCode().toLowerCase().startsWith("sp");

        // 2. no ECD has been provided
        // showing &= c.getCustomer().getHireMonitoringEcds == null || c.getHireMonitoringEcds().isEmpty();
        showing &= !isEcdExist(c);

        //3. the ‘Is Usable' field is ‘Y/Yes/T/True'
        showing &= c.getCustomer().getIsUsable();

        //4. if user is Insurer
        // showing &= securityInfoProvider.getIsINS();

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
        return "The CHO's client's vehicle is of a Prestige/Sports Performance nature, no ECD has been provided and the vehicle is roadworthy/usable. Manage repair book in date.";
    }
}
