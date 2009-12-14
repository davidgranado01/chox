/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.intelligentNotes;

import idas.chox.core.security.SecurityInfoProvider;

import idas.chox.core.model.Claim;

import idas.chox.core.model.IntelligentNote;

/**
 *
 * @author emmanuel
 */
public class CHOManagingRepairCheckNote implements IntelligentNote {

    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider) {
        Boolean showing = false;

        /*
        Claim Rule: If the ‘Managing Repair’ field has an Y/Yes then display note below
        on the action panel for a claim in status ClaimUnacknowledgedRouted, ClaimPending,
        ClaimRejectionContested, ClaimUpdatedByEngineer and ClaimReferredToEngineer for Insurer roles only.
         */

        //1. If the ‘Managing Repair’ field has an Y/Yes
        showing |= c.getManagingRepair();

        //2.  Insurer roles only
        // showing &= securityInfoProvider.getIsINS();

        return showing;
    }

    public String getNote() {
        return "The CHO is managing the repair.";
    }
}
