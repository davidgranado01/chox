/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.services.ClaimService;
import chox.web.security.ApplicationAccessibility;
import org.acegisecurity.GrantedAuthority;

/**
 *
 * @author Emmanuel
 */
public abstract class BaseModelAction extends BaseAction {

    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";
    protected int objectId = 0;
    protected int claimId = 0;
    protected String claimStatus;
    protected String actionResult;
    protected ClaimService claimService;

    abstract String getTabName();

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int id) {
        this.objectId = id;
    }

    public String getCaimStatus() {
        return claimStatus;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    @Override
    public String execute() {
        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        String tabName = getTabName();
        String claimStatus = getCaimStatus();
        short accessRight = ApplicationAccessibility.getInstance().checkTabAccessibility(tabName, grantedAuthorities, claimStatus);
    
        String result = accessRight > 1 ? EDITABLE : READ_ONLY;

        return result;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
