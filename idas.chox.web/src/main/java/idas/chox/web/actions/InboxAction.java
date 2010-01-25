package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.MenuAccessibility;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class InboxAction extends BaseAction implements SessionAware {

    private Map session;
    private ApplicationAccessibility applicationAccessibility;
    private MenuAccessibility menuAccessibility;
    private AuditTrailService auditTrailService;
    private ClaimService claimService;
    private String batchUpdateAction;
    private List<Integer> selectedClaimIdList;
    private int showHistory = 0;

    public int getShowHistory() {
        return showHistory;
    }

    public void setShowHistory(int showHistory) {
        this.showHistory = showHistory;
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

        int iCount = 0;
        String notAuthorizedClaims = "";
        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);

            if(applicationAccessibility.checkBatchUpdateEditableAccessibility(batchUpdateAction, super.getAuthenticatedUser(), claim)<2){
                notAuthorizedClaims = notAuthorizedClaims + claim.getChoReference() + ", ";
                iCount++;
            }
        }

        if(iCount>0){
            if(notAuthorizedClaims.length()>2){
                notAuthorizedClaims = notAuthorizedClaims.substring(0, (notAuthorizedClaims.length()-1));
            }
            getActionResponse().AssignMessageResult("Please de-select the tick box for following claim(s). <Br/>"+notAuthorizedClaims);
        }else{
            getActionResponse().AssignYesNoResult(Boolean.TRUE);
        }

        return SUCCESS;
    }

    /*********** END - BATCH UPDATE ACCESS RIGHT **************/
    public void setSession(Map arg0) {
        this.session = arg0;
    }

    public Integer getTab() {
        if (session.containsKey("tabIndex")) {
            return (Integer) this.session.get("tabIndex");
        } else {
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
}
