/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.services.ClaimService;
import idas.chox.data.services.BaseDataService;
import idas.chox.web.security.ApplicationAccessibility;
import java.util.Set;

/**
 *
 * @author Emmanuel
 */
public abstract class BaseModelAction extends BaseAction {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    public static final String READ_ONLY = "r";
    public static final String EDITABLE = "w";
    public static final String DECLINE = "decline";
    protected int claimId = 0;
    protected String claimStatus;
    protected String actionResult;
    protected ClaimService claimService;
    protected BaseDataService baseDataService;
    protected ApplicationAccessibility applicationAccessibility;
    private Claim claim;
    // </editor-fold>

    abstract String getTabName();

    public String getCaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Claim getClaim() {
        if (claim == null) {
            claim = this.claimService.getClaim(claimId);
        }
        return claim;
    }

    public int getClaimId() {
        return claimId;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }

    @Override
    public String execute() {
        Set roles = getAuthenticatedUser().getRoles();
        String tabName = getTabName();
        claimStatus = getCaimStatus();
        short accessRight = applicationAccessibility.checkTabAccessibility(tabName, roles, claimStatus);

        String result = accessRight > 1 ? EDITABLE : READ_ONLY;

        return result;
    }

    // <editor-fold defaultstate="collapsed" desc="Services">
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }
    // </editor-fold>
}
