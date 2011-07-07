package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.RoleHelper;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.MenuAccessibility;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboxAction extends BaseAction  {

    private static final Logger LOG = LoggerFactory.getLogger(InboxAction.class);
    private ApplicationAccessibility applicationAccessibility;
    private MenuAccessibility menuAccessibility;
    private AuditTrailService auditTrailService;
    private ClaimService claimService;
    private String batchUpdateAction;
    private List<Integer> selectedClaimIdList;
    private int showHistory;

    public int getShowHistory() {
        LOG.debug("getShowHistory is called and returning value is '{}'", showHistory);
        return showHistory;
    }

    public void setShowHistory(int showHistory) {
        LOG.debug("setShowHistory is called with the value of '{}'", showHistory);
        if (showHistory == 10) {
            getSession().put("tabIndex", 0);
            this.showHistory = 0;
        } else {
            this.showHistory = showHistory;
        }
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public MenuAccessibility getMenuAccessibility() {
        if (menuAccessibility == null) {
            menuAccessibility = applicationAccessibility.getMenuAccessibility(super.getAuthenticatedUser());
        }
        return menuAccessibility;
    }

    /*********** START - BATCH UPDATE ACCESS RIGHT **************/
    public String checkBatchUpdateStatus() {

        getActionResponse().AssignYesNoResult(Boolean.FALSE);
        List<String> statusAllow = applicationAccessibility.checkBatchUpdateAccessibility(batchUpdateAction, super.getAuthenticatedUser());

        for (Integer id : selectedClaimIdList) {

            Claim claim = claimService.getClaim(id);

            if (!statusAllow.contains(claim.getStatus())) {
                getActionResponse().AssignYesNoResult(Boolean.FALSE);
                return SUCCESS;
            }
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        }

        return SUCCESS;
    }

    public String checkClaimsBatchUpdate() {
        LOG.debug("Inside checkClaimsBatchUpdate method ");
        int iCount = 0;
        String notAuthorizedClaims = "";
        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);

            if (applicationAccessibility.checkBatchUpdateEditableAccessibility(batchUpdateAction, super.getAuthenticatedUser(), claim) < 2) {
                LOG.debug("checking batchupdate editable accessibility failed for {} this action",batchUpdateAction);
                notAuthorizedClaims = notAuthorizedClaims + claim.getChoReference() + ", ";
                iCount++;
            }
        }

        if (iCount > 0) {
            if (notAuthorizedClaims.length() > 2) {
                notAuthorizedClaims = notAuthorizedClaims.substring(0, (notAuthorizedClaims.length() - 1));
            }
            getActionResponse().AssignMessageResult("Please de-select the tick box for following claim(s). " + notAuthorizedClaims);
        } else {
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        }

        return SUCCESS;
    }

    /*********** END - BATCH UPDATE ACCESS RIGHT **************/
  

    public Integer getTab() {
        if (getSession().containsKey("tabIndex")) {
            LOG.debug("getTab is called and the returning value is '{}'", getSession().get("tabIndex"));
            return (Integer) getSession().get("tabIndex");
        } else {
            LOG.debug("getTab is called and the returning value is 0");
            return 0;
        }
    }

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    public AuditTrailService getAuditTrailService() {
        return auditTrailService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public String getBatchUpdateAction() {
        return batchUpdateAction;
    }

    public void setBatchUpdateAction(String batchUpdateAction) {
        this.batchUpdateAction = batchUpdateAction;
    }

    public void setSelectedClaimIds(String ids) {
        String[] list = ids.split(",");

        selectedClaimIdList = new ArrayList<Integer>();

        for (String s : list) {
            Integer id = Integer.parseInt(s.trim());
            selectedClaimIdList.add(id);
        }
    }

    public boolean getIsComUser() {
        return RoleHelper.isCheckSelectedRoleExist(super.getAuthenticatedUser().getRoles(), WebUserRole.ROLE_COM);
    }

    public boolean getIsScrUser() {
        return RoleHelper.isCheckSelectedRoleExist(super.getAuthenticatedUser().getRoles(), WebUserRole.ROLE_INS_SCR);
    }
}
