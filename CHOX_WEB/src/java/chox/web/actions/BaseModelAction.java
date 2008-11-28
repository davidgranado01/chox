/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

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
    protected int objectId = -1;
    protected String claimStatus;
    protected String actionResult;

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

        if (accessRight == 0) {
            return DECLINE;
        }
        String result = accessRight > 1 ? EDITABLE : READ_ONLY;

        return result;
    }
}
