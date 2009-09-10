/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model.intelligentNotes;

import chox.Util.DateHelper;
import chox.data.SecurityInfoProvider;
import chox.model.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import scsbre.model.IVehicleClassInfo;

/**
 *
 * @author emmanuel
 */
public class VehicleClassCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        IVehicleClassInfo vclass = c.getVClass();
        //1. Customer Vehicle Class field is P1,P2,P3,P4,P5,P6,P7,P8,P9,P10,P11,P12,SP1,SP2,SP3,SP4,SP5,SP6,SP7,SP8,SP9,SP10,SP11,SP12,SP13
        showing |= vclass.getCode().toLowerCase().startsWith("p");
        showing |= vclass.getCode().toLowerCase().startsWith("sp");

        //AND
        
        //2. ECD is less than 5 days from Policy Holder Contact Date
        Date latestEcdDate= c.getLatestHireMonitoringEcd();
        Date policyHolderContactDate = c.getPolicyHolderContactDate();

        long dayBetween = DateHelper.daysBetween(policyHolderContactDate, latestEcdDate);
        showing &= dayBetween < 5;

        //3. claim must in status ClaimUnacknowledgedRouted, ClaimPending, ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer
        String[] statuses = {ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED,
            ClaimStatus.CLAIM_PENDING,ClaimStatus.CLAIM_REJECTION_CONTESTED,
            ClaimStatus.CLAIM_UPDATE_BY_ENG,ClaimStatus.CLAIM_REF_TO_ENG};

        List<String> statusList  = Arrays.asList(statuses);

        showing &= statusList.contains(c.getStatus());

        return showing;
    }

    public String getNote() {
        return "The CHO’s client’s vehicle is of a Prestige/Sports Performance nature and is entering into a short repair period. Review need for replacement vehicle.";
    }

}
