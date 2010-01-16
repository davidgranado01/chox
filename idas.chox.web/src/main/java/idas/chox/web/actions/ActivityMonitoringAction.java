/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import idas.chox.service.monitors.ClaimViewingMonitor;
import idas.chox.web.viewdata.ViewingStatus;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ActivityMonitoringAction extends BaseAction {

    private Integer claimId;
    private List<String> usersViewingThisClaim;
    private UserService userService;
    private String actionResult;
    private String claimIds;
    private ArrayList<ViewingStatus> statuses;
    private String method;

    @Override
    public String execute() {
        ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
        List<Integer> userIds = monitor.ping(getClaimId(), getOrganisationType(), getOrganisationId(), getUserId());

        usersViewingThisClaim = new ArrayList<String>();
        int currentUserID = getUserId();
        for (Integer id : userIds) {
            if (id != currentUserID) {
                WebUser user = userService.getWebUser(id);
                usersViewingThisClaim.add(user.toString());
            }
        }

        method = "execute";
        return SUCCESS;
    }

    public String checkViewingStatus() {

        statuses = new ArrayList<ViewingStatus>();
        if (claimIds != null) {
            String[] claimIdArray = claimIds.split(",");

            ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();

            for (String s : claimIdArray) {
                if (s != null && s.matches("^\\d+$")) {
                    Integer cId = Integer.parseInt(s);
                    Boolean status = monitor.isClaimViewingBySomeBody(cId, getOrganisationType(), getOrganisationId());
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
