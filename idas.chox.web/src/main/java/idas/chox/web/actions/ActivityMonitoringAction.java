package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import idas.chox.core.model.Claim;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserService;
import idas.chox.service.monitors.ActivityMonitorUserDetail;
import idas.chox.service.monitors.ClaimViewingMonitor;
import idas.chox.web.viewdata.ViewingStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityMonitoringAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityMonitoringAction.class);
    private List<String> usersViewingThisClaim;
    private UserService userService;
    private String actionResult;
    private String claimIds;
    private ArrayList<ViewingStatus> statuses;
    private String method;
    private ClaimService claimService;

    @Override
    public String execute() {

        method = "execute";
        int currentUserID = getUserId();
        Integer claimId = getModelIdFromSession(Claim.class);
        Claim claim;
        
        if (claimId != null) {
            try {
                claim = claimService.getClaim(claimId);
            } catch (Exception ex) {
                // This happens only when a claim has been switched
                LOG.warn("Activity Monitoring: User (with id={}, orgId={}, Organisation type={}) is viewing a claim which does not exist: {}",
                    new Object[]{currentUserID, getOrganisationId(), getOrganisationType(), claimId});
                return SUCCESS;
           }
        } else {
            LOG.warn("Activity Monitoring: User (with id={}, orgId={}, Organisation type={}) is viewing a claim which does not have claimId in session {}.",
                    new Object[]{currentUserID, getOrganisationId(), getOrganisationType(), claimId});
            return SUCCESS;
        }

        if (claim != null) {
            LOG.debug("START Monitoring: claimId={}, userId={}, orgType={}, orgId={}",
                    new Object[]{claimId, currentUserID, getOrganisationType(), getOrganisationId()});
            ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
            ActivityMonitorUserDetail activityMonitorUserDetail = new ActivityMonitorUserDetail(currentUserID, getOrganisationId(), getIsInsurer(), getIsCHO(), getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
            List<ActivityMonitorUserDetail> activityMonitorUserDetails = monitor.ping(claimId, activityMonitorUserDetail, claimService.getActivityMonitorRequestInterval());
            LOG.trace("monitor.ping returned {} userIds.", activityMonitorUserDetails != null ? activityMonitorUserDetails.size() : 0);
            for (ActivityMonitorUserDetail activityMonitorUser : activityMonitorUserDetails) {
                if (activityMonitorUser.getId() != currentUserID) {
                    if ((getAuthenticatedUser().isAnInsurer() && activityMonitorUser.isInsurer()
                            && getAuthenticatedUser().getInsurer().getId().intValue() != activityMonitorUser.getOrgId())
                            || (getAuthenticatedUser().isCHO() && activityMonitorUser.isCho()
                            && getAuthenticatedUser().getChorganisation().getId().intValue() != activityMonitorUser.getOrgId())) {
                        LOG.warn("User {} ('{}') and user {} ('{}') from different org but same org type both viewing claim with id={}",
                                new Object[]{currentUserID, getAuthenticatedUser().toString(), activityMonitorUser.getId(), activityMonitorUser.getUserName(), claimId});
                    } else if (activityMonitorUser.isInsurer() && activityMonitorUser.getOrgId() != claim.getInsurer().getId().intValue()) {
                        LOG.warn("Insurer User {} ('{}') from org '{}' viewing claim with id={} from different org '{}': please check claim has recently been switched",
                                new Object[]{activityMonitorUser.getId(), activityMonitorUser.getUserName(), activityMonitorUser.getOrgName(), claimId, claim.getInsurer().getName()});
                    } else if (activityMonitorUser.isCho() && activityMonitorUser.getOrgId() != claim.getChorganisation().getId().intValue()) {
                        LOG.warn("CHO User {} ('{}') from org '{}' viewing claim with id={} from different org '{}'",
                                new Object[]{activityMonitorUser.getId(), activityMonitorUser.getUserName(), activityMonitorUser.getOrgName(), claimId, claim.getChorganisation().getName()});
                    } else {
                        LOG.debug("A user is currently viewing this claim: {}", activityMonitorUser.getUserName());
                        if (usersViewingThisClaim == null) {
                            usersViewingThisClaim = new ArrayList<String>(5);
                        }
                        usersViewingThisClaim.add(activityMonitorUser.toString());
                    }
                }
            }
        } else {
                LOG.warn("Activity Monitoring: User (with id={}, orgId={}, Organisation type={}) is viewing a claim which does not exist: {}",
                    new Object[]{currentUserID, getOrganisationId(), getOrganisationType(), claimId});
        }

        if (usersViewingThisClaim == null) {
            usersViewingThisClaim = new ArrayList<String>(0);
        }
        return SUCCESS;
    }

    
    public String checkViewingStatus() {
        LOG.trace("Checking view status:");
        statuses = new ArrayList<ViewingStatus>();
        if (claimIds != null) {
            String[] claimIdArray = claimIds.split(",");
            LOG.trace("We have {} claimIds", claimIdArray.length);
            ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();

            for (String s : claimIdArray) {
                LOG.trace("Checking claim {}", s);
                if (s != null && s.matches("^\\d+$")) {
                    Integer cId = Integer.parseInt(s);
                    Boolean status = monitor.isClaimViewingBySomeBody(cId);
                    LOG.debug("Status for claim {} is {}", cId, status);
                    statuses.add(new ViewingStatus(cId, status));
                }
            }
        }

        method = "checkViewingStatus";
        return SUCCESS;
    }

    
    public String getJsonData() {
        if (method.equalsIgnoreCase("checkViewingStatus")) {
            JSONArray jObject = JSONArray.fromObject(this.statuses);
            return "{isValid:true,totalCount:" + this.statuses.size() + ",results:" + jObject.toString() + "}";
        } else {
            JSONArray jObject = JSONArray.fromObject(this.usersViewingThisClaim);
            return "{isValid:true,totalCount:" + this.usersViewingThisClaim.size() + ",results:" + jObject.toString() + "}";
        }
    }

    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    
    @Override
    public String getActionResult() {
        return actionResult;
    }

    
    public int getUserId() {
        return this.getAuthenticatedUser().getId();
    }

    
    public String getOrganisationType() {
        if (this.getIsCHO()) {
            return "C";
        } else if (this.getIsInsurer()) {
            return "I";
        } else {
            return "A";
        }
    }

    
    public int getOrganisationId() {
        if (this.getIsCHO()) {
            return this.getAuthenticatedUser().getChorganisation().getId();
        } else if (this.getIsInsurer()) {
            return this.getAuthenticatedUser().getInsurer().getId();
        } else {
            return 999;
        }
    }

    
    public String getClaimIds() {
        return claimIds;
    }

    
    public void setClaimIds(String claimIds) {
        this.claimIds = claimIds;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
