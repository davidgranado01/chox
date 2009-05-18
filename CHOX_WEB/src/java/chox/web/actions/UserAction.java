package chox.web.actions;

import chox.model.WebUser;
import chox.services.UserService;
import chox.web.security.PermissionedUser;
import chox.web.viewdata.UserViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserAction extends BaseAction {

    private List<UserViewData> user;
    private UserService service;
    private PermissionedUser currentUser = getAuthenticatedUser();
    private int orgTypeId = -1;
    private int orgId;
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.user);
        return "{totalCount:" + this.user.size() + ",results:" + jObject.toString() + "}";
    }

    public void setUserService(UserService service)
    {
        this.service = service;
    }

    @Override
    public String execute() {
 
        List<WebUser> userData = this.service.getUsers(orgTypeId, orgId);
        
        user = new ArrayList<UserViewData>();
        
        for(WebUser h : userData)
        {
            user.add(new UserViewData(h));
        }
        
        return SUCCESS;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getOrgTypeId() {        
        return orgTypeId;
    }

    public void setOrgTypeId(int orgTypeId) {
        this.orgTypeId = orgTypeId;
    }
    
    /*
        if(currentUser.getIsCHOXAdmin()){
            orgTypeId = 1;
        }else if(currentUser.getIsINS()){
            orgTypeId = 2;
        }else if(currentUser.getIsCHO()){
            orgTypeId = 3;
        }
     */
}
