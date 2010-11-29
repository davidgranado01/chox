/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.service.security;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author seenimurugan
 */
public class ButtonAccessibility {

    private boolean switchClaimAccessibility;
    private boolean revertClaimAccessibility;
    private boolean closeClaimAccessibility;
    private boolean reopenClaimAccessibility;

    private static final Logger LOG = LoggerFactory.getLogger(ButtonAccessibility.class);



    public ButtonAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, Claim claim){

        switchClaimAccessibility = applicationAccessibility.checkButtonAccessibility(ApplicationAccessibility.SWITCH_CLAIM, user, claim)>0;
        revertClaimAccessibility = applicationAccessibility.checkButtonAccessibility(ApplicationAccessibility.REVERT_CLAIM, user, claim)>0;
        closeClaimAccessibility = applicationAccessibility.checkButtonAccessibility(ApplicationAccessibility.CLOSE_CLAIM, user, claim)>0;
        reopenClaimAccessibility = applicationAccessibility.checkButtonAccessibility(ApplicationAccessibility.REOPEN_CLAIM, user, claim)>0;

    }

    public boolean getSwitchClaimAccessibility() {
        return switchClaimAccessibility;
    }

    public void setSwitchClaimAccessibility(boolean switchClaimAccessibility) {
        this.switchClaimAccessibility = switchClaimAccessibility;
    }

    /**
     * @return the revertClaimAccessibility
     */
    public boolean getRevertClaimAccessibility() {
        return revertClaimAccessibility;
    }

    /**
     * @param revertClaimAccessibility the revertClaimAccessibility to set
     */
    public void setRevertClaimAccessibility(boolean revertClaimAccessibility) {
        this.revertClaimAccessibility = revertClaimAccessibility;
    }

    public boolean getCloseClaimAccessibility() {
        return closeClaimAccessibility;
    }

    public void setCloseClaimAccessibility(boolean closeClaimAccessibility) {
        this.closeClaimAccessibility = closeClaimAccessibility;
    }

    public boolean getReopenClaimAccessibility() {
        return reopenClaimAccessibility;
    }

    public void setReopenClaimAccessibility(boolean reopenClaimAccessibility) {
        this.reopenClaimAccessibility = reopenClaimAccessibility;
    }

}
