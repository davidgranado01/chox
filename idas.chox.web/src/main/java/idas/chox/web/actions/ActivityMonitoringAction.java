package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.data.services.Config;
import idas.chox.service.monitors.ActivityMonitorUserDetail;
import idas.chox.service.monitors.ClaimViewingMonitor;
import idas.chox.web.viewdata.ViewingStatus;

public class ActivityMonitoringAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityMonitoringAction.class);
    private List<String> usersViewingThisClaim;
    private String actionResult;
    private String claimIds;
    private ArrayList<ViewingStatus> statuses;
    private String method;
    private Integer userId;
    private Integer organisationId;
    private Config config;

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public String execute() {

        method = "execute";
        int currentUserID = getUserId();
        if (currentUserID < 0) {
            LOG.warn("No activity monitoring user with id={}", currentUserID);
            usersViewingThisClaim = new ArrayList<>(0);
            return SUCCESS;
        }
        Integer claimId = getModelIdFromSession(Claim.class);

        if (claimId == null) {
            LOG.warn("Activity Monitoring: User (with id={}, orgId={}, Organisation type={}) is viewing a claim which does not have claimId {} in session.",
                    new Object[]{currentUserID, getOrganisationId(), getOrganisationType(), claimId});
            usersViewingThisClaim = new ArrayList<>(0);
            return SUCCESS;
        }

        LOG.debug("START Monitoring: claimId={}, userId={}, orgType={}, orgId={}",
                new Object[]{claimId, currentUserID, getOrganisationType(), getOrganisationId()});
        ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
        ActivityMonitorUserDetail activityMonitorUserDetail = new ActivityMonitorUserDetail(currentUserID, getOrganisationId(), getIsInsurer(), getIsCHO(), getAuthenticatedUser().getDisplayName(), getAuthenticatedUser().getOrganisationName());
        List<ActivityMonitorUserDetail> activityMonitorUserDetails = monitor.ping(claimId, activityMonitorUserDetail, config.getActivityMonitorRequestInterval());
        LOG.trace("monitor.ping returned {} userIds.", activityMonitorUserDetails != null ? activityMonitorUserDetails.size() : 0);
        if (activityMonitorUserDetails != null) {
            for (ActivityMonitorUserDetail activityMonitorUser : activityMonitorUserDetails) {
                if (activityMonitorUser.getId() != currentUserID) {
                    if ((getAuthenticatedUser().isAnInsurer() && activityMonitorUser.isInsurer()
                            && getAuthenticatedUser().getInsurer().getId() != activityMonitorUser.getOrgId())
                            || (getAuthenticatedUser().isCHO() && activityMonitorUser.isCho()
                            && getAuthenticatedUser().getChorganisation().getId() != activityMonitorUser.getOrgId())) {
                        LOG.warn("User {} ('{}') and user {} ('{}') from different org but same org type both viewing claim with id={}",
                                new Object[]{currentUserID, getAuthenticatedUser().toString(), activityMonitorUser.getId(), activityMonitorUser.getUserName(), claimId});
                    } else {
                        LOG.debug("A user is currently viewing this claim: {}", activityMonitorUser.getUserName());
                        if (usersViewingThisClaim == null) {
                            usersViewingThisClaim = new ArrayList<>(5);
                        }
                        usersViewingThisClaim.add(activityMonitorUser.toString());
                    }
                }
            }
        }

        if (usersViewingThisClaim == null) {
            usersViewingThisClaim = new ArrayList<>(0);
        }
        return SUCCESS;
    }

    public String checkViewingStatus() {
        LOG.trace("Checking view status:");
        statuses = new ArrayList<>();
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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        if (method.equalsIgnoreCase("checkViewingStatus")) {
            try {
                jsonString = mapper.writeValueAsString(statuses);
            } catch (JsonProcessingException ex) {
                LOG.error("Error converting choAliases to json string.");
            }
            return "{isValid:true,totalCount:" + statuses.size() + ",results:" + jsonString + "}";
        } else {
            try {
                jsonString = mapper.writeValueAsString(usersViewingThisClaim);
            } catch (JsonProcessingException ex) {
                LOG.error("Error converting usersViewingThisClaim to json string.");
            }
            return "{isValid:true,totalCount:" + usersViewingThisClaim.size() + ",results:" + jsonString + "}";
        }
    }

    @Override
    public String getActionResult() {
        return actionResult;
    }

    public int getUserId() {
        if (userId == null) {
            try {
                if (getSession().containsKey("user")) {
                    userId = ((WebUser) getSession().get("user")).getId();
                } else {
                    LOG.debug("No user is session {}", getSession());
                }
            } catch (Exception ex) {
                LOG.warn("Exception: {}", ex.getMessage(), ex);
            }
        }
        return userId == null ? -1 : userId;
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
        if (organisationId == null) {
            if (this.getIsCHO()) {
                try {
                    if (getSession().containsKey("choId")) {
                        organisationId = (Integer) getSession().get("choId");
                    } else {
                        LOG.error("No choId is session {}", getSession());
                    }
                } catch (Exception ex) {
                    LOG.error("Exception: {}", ex.getMessage(), ex);
                }
            } else if (this.getIsInsurer()) {
                try {
                    if (getSession().containsKey("insurerId")) {
                        organisationId = (Integer) getSession().get("insurerId");
                    } else {
                        LOG.error("No insurerId is session {}", getSession());
                    }
                } catch (Exception ex) {
                    LOG.error("Exception: {}", ex.getMessage(), ex);
                }
            } else {
                organisationId = 999;
            }
        }

        return organisationId;
    }

    public String getClaimIds() {
        return claimIds;
    }

    public void setClaimIds(String claimIds) {
        this.claimIds = claimIds;
    }

}
