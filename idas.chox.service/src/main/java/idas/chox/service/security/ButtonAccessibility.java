package idas.chox.service.security;

import idas.chox.core.model.AuditTrail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.AuditTrailService;

/**
 *
 * @author seenimurugan
 */
public class ButtonAccessibility {
    private static final Logger LOG = LoggerFactory.getLogger(ButtonAccessibility.class);

    private boolean switchClaimAccessibility;
    private boolean revertClaimAccessibility;
    private boolean closeClaimAccessibility;
    private boolean reopenClaimAccessibility;
    private boolean switchClaimToMultipleInsurerAccessibility;
    private boolean updatePaymentNotReceived;
    private AuditTrailService auditTrailService;

    private String getButtonAccessibilityKey(String buttonName, String claimStatus, ClaimType claimType) {
        return String.format("button.%1$s.%2$s.%3$s", buttonName, claimStatus, claimType.name());
    }


    public ButtonAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user, Claim claim){

        switchClaimAccessibility = applicationAccessibility.checkAccessibilityForClaimType(getButtonAccessibilityKey(ApplicationAccessibility.SWITCH_CLAIM, claim.getStatus(), claim.getClaimType()), user, claim)>0;
        revertClaimAccessibility = applicationAccessibility.checkAccessibilityForClaimType(getButtonAccessibilityKey(ApplicationAccessibility.REVERT_CLAIM, claim.getStatus(), claim.getClaimType()), user, claim)>0;
        closeClaimAccessibility = applicationAccessibility.checkAccessibilityForClaimType(getButtonAccessibilityKey(ApplicationAccessibility.CLOSE_CLAIM, claim.getStatus(), claim.getClaimType()), user, claim)>0;
        reopenClaimAccessibility = applicationAccessibility.checkAccessibilityForClaimType(getButtonAccessibilityKey(ApplicationAccessibility.REOPEN_CLAIM, claim.getStatus(), claim.getClaimType()), user, claim)>0;
        switchClaimToMultipleInsurerAccessibility = applicationAccessibility.checkAccessibilityForClaimType(getButtonAccessibilityKey(ApplicationAccessibility.SWITCH_CLAIM_MULTIPLE_INS, claim.getStatus(), claim.getClaimType()), user, claim)>0;
        updatePaymentNotReceived = applicationAccessibility.checkAccessibilityForClaimType(getButtonAccessibilityKey(ApplicationAccessibility.UPDATE_PAYMENT_NOT_RECEIVED, claim.getStatus(), claim.getClaimType()), user, claim)>0;

        if (reopenClaimAccessibility && user.isAnInsurer() && !ClaimType.isInsurerUpload(claim.getClaimType())) {
            reopenClaimAccessibility = false;
        }
        
        if (revertClaimAccessibility) {
            // this fix is for bug 2208 disable revert function when there is no previous status.
            AuditTrail auditTrail = auditTrailService.getLastChange(claim.getId());
            if (auditTrail == null || auditTrail.getOriginalStatus() == null
                    || auditTrail.getOriginalStatus().isEmpty()) {
                revertClaimAccessibility = false;
            }
        }

    }

    public boolean getSwitchClaimToMultipleInsurerAccessibility() {
        return switchClaimToMultipleInsurerAccessibility;
    }

    public void setSwitchClaimToMultipleInsurerAccessibility(boolean switchClaimToMultipleInsurerAccessibility) {
        this.switchClaimToMultipleInsurerAccessibility = switchClaimToMultipleInsurerAccessibility;
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

    public boolean getUpdatePaymentNotReceived() {
        return updatePaymentNotReceived;
    }

    public void setUpdatePaymentNotReceived(boolean updatePaymentNotReceived) {
        this.updatePaymentNotReceived = updatePaymentNotReceived;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }


}
