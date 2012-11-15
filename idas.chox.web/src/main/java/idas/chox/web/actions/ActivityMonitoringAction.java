package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserService;
import idas.chox.service.monitors.ClaimViewingMonitor;
import idas.chox.web.viewdata.ViewingStatus;

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

        usersViewingThisClaim = new ArrayList<String>();
        int currentUserID = getUserId();
        Integer claimId = getModelIdFromSession(Claim.class);
        if (claimId != null) {
            Claim claim = claimService.getClaim(claimId);
            LOG.debug("START Monitoring: claimId={}, userId={}", claimId, currentUserID);
            LOG.debug("START Monitoring: Organisation: type={}, id={}", getOrganisationType(), getOrganisationId());
            ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
            List<Integer> userIds = monitor.ping(claimId, getOrganisationType(), getOrganisationId(), currentUserID, claimService.getActivityMonitorRequestInterval());
            LOG.debug("monitor.ping returned {} userIds.", userIds.size());
            for (Integer id : userIds) {
                if (id != currentUserID) {
                    WebUser user = userService.getWebUser(id);
                    LOG.debug("A user is currently viewing this claim: {}", user.getFullName());
                    if ((getAuthenticatedUser().isAnInsurer() && user.isAnInsurer()
                            && getAuthenticatedUser().getInsurer().getId().intValue() != user.getInsurer().getId().intValue())
                            || (getAuthenticatedUser().isCHO() && user.isCHO()
                            && getAuthenticatedUser().getChorganisation().getId().intValue() != user.getChorganisation().getId().intValue())) {
                        LOG.error("User {} ('{}') and user {} ('{}') from different org but same org type both viewing claim with id={}",
                                new Object[]{currentUserID, getAuthenticatedUser().toString(), user.getId(), user.toString(), claimId});
                    } else if (user.isAnInsurer() && user.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                        LOG.error("Insurer User {} ('{}') from org '{}' viewing claim with id={} from different org '{}'",
                                new Object[]{user.getId(), user.toString(), user.getInsurer().getName(), claimId, claim.getInsurer().getName()});
                    } else if (user.isCHO() && user.getChorganisation().getId().intValue() != claim.getChorganisation().getId().intValue()) {
                        LOG.error("CHO User {} ('{}') from org '{}' viewing claim with id={} from different org '{}'",
                                new Object[]{user.getId(), user.toString(), user.getChorganisation().getName(), claimId, claim.getChorganisation().getName()});
                    } else {
                        LOG.debug("A user is currently viewing this claim: {}", user.getFullName());
                        usersViewingThisClaim.add(user.toString());
                    }
                }
            }
        } else {
            LOG.warn("Activity Monitoring: User (with id={}, orgId={}, Organisation type={}) is viewing a claim which does not have claimId in session {}.",
                    new Object[]{currentUserID, getOrganisationId(), getOrganisationType(), claimId});
        }

        method = "execute";
        return SUCCESS;
    }

    public String checkViewingStatus() {
        LOG.debug("Checking view status:");
        statuses = new ArrayList<ViewingStatus>();
        if (claimIds != null) {
            String[] claimIdArray = claimIds.split(",");
            LOG.debug("We have {} claimIds", claimIdArray.length);
            ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();

            for (String s : claimIdArray) {
                LOG.debug("Checking claim {}", s);
                if (s != null && s.matches("^\\d+$")) {
                    Integer cId = Integer.parseInt(s);
                    Boolean status = monitor.isClaimViewingBySomeBody(cId, getOrganisationType(), getOrganisationId());
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

//        System.out.println(">>>>>>> START ActivityMonitoringAction :"+this.getAuthenticatedUser().getEmail());
//        LOG.info(">>>>>>> START ActivityMonitoringAction :"+this.getAuthenticatedUser().getEmail());

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
