package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import static com.opensymphony.xwork2.Action.SUCCESS;

import net.sf.json.JSONArray;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.LookupService;
import idas.chox.core.util.RoleHelper;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.MenuAccessibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboxAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(InboxAction.class);
    private ApplicationAccessibility applicationAccessibility;
    private MenuAccessibility menuAccessibility;
    private AuditTrailService auditTrailService;
    private LookupService lookupService;
    private ClaimService claimService;
    private String batchUpdateAction;
    private List<Integer> selectedClaimIdList = new ArrayList<Integer>();;
    private int showHistory;
    private List<Insurer> insurers;
    private List<Chorganisation> suppliers;
    private boolean showSplash;
    private String jsonData;

    public int getActivityMonitorRequestInterval() {
        return claimService.getActivityMonitorRequestInterval();
    }

    public boolean isEnableActivityMonitor() {
        return claimService.isEnableActivityMonitor();
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
    
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public int getShowHistory() {
        return showHistory;
    }

    public boolean isShowSplash() {
        if (!getAuthenticatedUser().isShowSplash()) {
            return false;
        }
        return showSplash;
    }

    public void setShowSplash(boolean showSplash) {
        this.showSplash = showSplash;
    }

    public void setShowHistory(int showHistory) {
        LOG.debug("setShowHistory is called with the value of '{}'", showHistory);
        if (showHistory == 10) {
            if(getSession()!= null) {
                getSession().put("tabIndex", 0);
            }
            this.showHistory = 0;
        } else {
            this.showHistory = showHistory;
        }
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String loadInbox() throws Exception {
        return SUCCESS;
    }
    
    public String getInboxTabPanel() {
        return SUCCESS;
    }
    
    public MenuAccessibility getMenuAccessibility() {
        if (menuAccessibility == null) {
            menuAccessibility = new MenuAccessibility(applicationAccessibility, getAuthenticatedUser());
        }
        return menuAccessibility;
    }

    /*********** START - BATCH UPDATE ACCESS RIGHT **************/
    public String checkBatchUpdateStatus() {

        getActionResponse().AssignYesNoResult(Boolean.FALSE);
        List<String> statusAllow = applicationAccessibility.getAllowedStatusesForBatchUpdate(batchUpdateAction, super.getAuthenticatedUser());
        List<String> insurerName = new ArrayList<String>();

        for (Integer id : selectedClaimIdList) {

            Claim claim = claimService.getClaim(id);

            if (getAuthenticatedUser().isAnInsurer()
                    && ClaimStatus.isManualStatus(claim.getStatus())
                    && statusAllow.contains(claim.getStatus())) {
                // in case we have manual invoice ownership batch update enabled 
                if (batchUpdateAction.equals("claimOwnership")
                        && !getAuthenticatedUser().getInsurer().isEnableManualInvoiceOwnership()) {
                    return SUCCESS;
                }
                // in case we have manual invoice workgroup and ownership batch update enabled
                if (batchUpdateAction.equals("updateClaimWorkgroupAndOwner") 
                        && !getAuthenticatedUser().getInsurer().isEnableManualInvoiceWorkgroups()) {
                    return SUCCESS;
                }
            }

            if (batchUpdateAction.equalsIgnoreCase("routeClaims") && claim.getInsurer().isClaimOwnershipEnable()
                    && !claim.getStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                getActionResponse().AssignYesNoResult(Boolean.FALSE);
                return SUCCESS;
            }
            
            if (batchUpdateAction.equalsIgnoreCase("routeClaims") 
                    && (!claim.getInsurer().isWorkgroupEnable() 
                        || (ClaimStatus.isManualStatus(claim.getStatus()) 
                            && ((!claim.getInsurer().isEnableManualInvoiceWorkgroups()) 
                                || (claim.getInsurer().isClaimOwnershipEnable() || claim.getInsurer().isEnableManualInvoiceOwnership()))))) {
                getActionResponse().AssignYesNoResult(Boolean.FALSE);
                return SUCCESS;
            }
            
            // Remove 'Update Claim(s) To Invoice Payment Logged' option for manual claims
            //   - his is lazy and should really be achieved by fine tuning the accessibilities entries
            if (batchUpdateAction.equalsIgnoreCase("logInvoicePayment") && ClaimType.isInsurerUpload(claim.getClaimType())) {
                getActionResponse().AssignYesNoResult(Boolean.FALSE);
                return SUCCESS;
            }

            if (!statusAllow.contains(claim.getStatus()) || canShowRouteClaimsInBatchUpdate(insurerName, claim)) {
                return SUCCESS;
            }
        }

        getActionResponse().AssignYesNoResult(Boolean.TRUE);

        return SUCCESS;
    }

    // This below method ensure that chox admin can not 'Route Claims' via batch update when multiple insurers are selected.
    private boolean canShowRouteClaimsInBatchUpdate(List<String> insurerName, Claim claim) {
        if (getAuthenticatedUser().isCHOXAdmin()
                && batchUpdateAction.equalsIgnoreCase("routeClaims")
                && !insurerName.isEmpty()
                && (!insurerName.contains(claim.getInsurer().getName())
                    || !claim.getInsurer().isWorkgroupEnable())) {
            return true;
        } else {
            if (insurerName.isEmpty()) {
                insurerName.add(claim.getInsurer().getName());
            }
            return false;
        }
    }
    
    // Below functionality implemented for bug#1546 Bulk action 'Assign Claim Owner' should default to correct workgroup
    public String getUniqueWorkgroupId() {
        List<Integer> workgrouId = new ArrayList<Integer>();
        for (Iterator<Integer> it = selectedClaimIdList.iterator(); it.hasNext();) {
            Integer id = it.next();
            Claim claim = claimService.getClaim(id);
            if (claim.getWorkgroup() == null || (!workgrouId.isEmpty() && !workgrouId.contains(claim.getWorkgroup().getId()))) {
                setJsonData("{workgroupId:-1}");
                return SUCCESS;
            } else if (workgrouId.isEmpty()) {
                workgrouId.add(claim.getWorkgroup().getId());
            }
        }
        setJsonData("{workgroupId:" + workgrouId.get(0) + "}");
        return SUCCESS;
    }

    public String checkClaimsBatchUpdate() {
        LOG.debug("Inside checkClaimsBatchUpdate method ");
        int iCount = 0;
        String notAuthorizedClaims = "";
        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);

            if (applicationAccessibility.checkBatchUpdateAccessibilityEditable(batchUpdateAction,
                    super.getAuthenticatedUser(), claim) < 2) {
                LOG.debug("checking batchupdate editable accessibility failed for {} this action", batchUpdateAction);
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

    public List<Insurer> getInsurers() {
        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public List<Chorganisation> getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getSuppliers(false); // inlucde manual CHO.
        }
        return suppliers;
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getName(), supplier.getId().toString()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getName(), insurer.getId().toString()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }
    
    public String getClaimTypesJsonString() {
        List<LookupItem> claimTypesList = lookupService.getClaimTypes(getAuthenticatedUser());
        String claimTypesJson = JSONArray.fromObject(claimTypesList).toString();
        return "{totalCount:" + claimTypesList.size() + ", results:" + claimTypesJson + "}";
    }
    
    public boolean getEnableManualInvoiceWorkgroups() {
        if(getAuthenticatedUser().isCHOXAdmin() || getAuthenticatedUser().isCHO()){
           return false;
        } 
        return getAuthenticatedUser().getInsurer().isEnableManualInvoiceWorkgroups();
    }
    
    public boolean getEnableManualInvoiceOwnership() {
        if(getAuthenticatedUser().isCHOXAdmin() || getAuthenticatedUser().isCHO()){
            return false;
         } 
        return getAuthenticatedUser().getInsurer().isEnableManualInvoiceOwnership();
    }

    public int getFilterOrgId() {
        if (getSession().containsKey("filterOrgId")) {
            return (Integer) getSession().get("filterOrgId");
        } else {
            return -1;
        }
    }

    public int getFilterClaimTypeId() {
        if (getSession().containsKey("filterClaimTypeId")) {
            return (Integer) getSession().get("filterClaimTypeId");
        } else {
            return -1;
        }
    }
    
    public String getGridTitle() {
        if (getSession().containsKey("gridTitle")) {
            return (String) getSession().get("gridTitle");
        } else {
            return "";
        }
    }
    
    public String getFilterKey() {
        if (getSession().containsKey("filterKey")) {
            return (String) getSession().get("filterKey");
        } else {
            return "";
        }
    }
}
