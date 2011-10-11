package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import idas.chox.service.monitors.ClaimViewingMonitor;
import idas.chox.web.viewdata.ViewingStatus;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ActivityMonitoringAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(ActivityMonitoringAction.class);

    private Integer claimId;
    private List<String> usersViewingThisClaim;
    private UserService userService;
    private String actionResult;
    private String claimIds;
    private ArrayList<ViewingStatus> statuses;
    private String method;

    @Override
    public String execute() {

//        System.out.println(">>>>>>> START ActivityMonitoringAction MONITOR");
        int currentUserID = getUserId();
        LOG.debug("START Monitoring: claimId={}, userId={}", getClaimId(), currentUserID);
        LOG.debug("START Monitoring: Organisation: type={}, id={}", getOrganisationType(), getOrganisationId());
        ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
        List<Integer> userIds = monitor.ping(getClaimId(), getOrganisationType(), getOrganisationId(), currentUserID);
        LOG.debug("monitor.ping returned {} userIds.", userIds.size());
        usersViewingThisClaim = new ArrayList<String>();
        for (Integer id : userIds) {
            if (id != currentUserID) {
                WebUser user = userService.getWebUser(id);
                usersViewingThisClaim.add(user.toString());
                LOG.debug("A user is currently viewing this claim: {}", user.getFullName());
            }
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

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
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
}
