/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.data.ClaimViewingMonitor;
import chox.model.WebUser;
import chox.services.UserService;
import chox.web.viewdata.ViewingStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class ActivityMonitoringAction extends BaseAction{
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
        for(Integer id : userIds)
        {
            if (id != currentUserID) {
                WebUser user = userService.getObject(id);
                usersViewingThisClaim.add(user.toString());
            }
        }
        
        method = "execute";
        return SUCCESS;
    }
    
    public String checkViewingStatus()
    {
        statuses = new ArrayList<ViewingStatus>();
        String[] claimIdArray = claimIds.split(",");
                 
        ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
                       
        for(String s : claimIdArray)
        {
            if (s!= null && s.matches("^\\d+$")) {
                Integer cId = Integer.parseInt(s);
                Boolean status = monitor.isClaimViewingBySomeBody(cId, getOrganisationType(), getOrganisationId());
                statuses.add(new ViewingStatus(cId, status));
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
            return "{totalCount:" + this.statuses.size() + ",results:" + jObject.toString() + "}";
        } else {
            JSONArray jObject = JSONArray.fromObject(this.usersViewingThisClaim);
            return "{totalCount:" + this.usersViewingThisClaim.size() + ",results:" + jObject.toString() + "}";
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getActionResult() {
        return actionResult;
    }
    
    public int getUserId()
    {
        return this.getAuthenticatedUser().getUser().getId();
    }
    
    public String getOrganisationType()
    {
         if(this.getIsCHO())
        {
            return "C";
        }
        else if(this.getIsInsurer())
        {
           return "I";
        }
        else 
        {
            return "A";
        }
    }
    
    public int getOrganisationId()
    {
        if(this.getIsCHO())
        {
            return this.getAuthenticatedUser().getUser().getChorganisation().getId();
        }
        else if(this.getIsInsurer())
        {
           return this.getAuthenticatedUser().getUser().getInsurer().getId();
        }
        else 
        {
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
