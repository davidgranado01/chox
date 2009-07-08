package chox.web.actions;

import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.services.UserService;
import java.util.Set;
import chox.web.viewdata.UserViewData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;

public class UserAction extends BaseAction {

    private List<UserViewData> user;
    private UserService service;
    private int orgTypeId = -1;
    private int orgId;
    private int userRoleId = -1;
    
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
            if(userRoleId>0){
                 if(isSelectedRoleExist(h.getRoles(), userRoleId)){
                    user.add(new UserViewData(h));
                 }
            }else{
                user.add(new UserViewData(h));
            }
        }
        
        return SUCCESS;
    }
    
    private boolean isSelectedRoleExist(Set roles, int selectedRole){
        boolean isExist = false;
        
        Iterator it = roles.iterator();
        
        while (it.hasNext()) {
            WebUserRole webUserrole = (WebUserRole) it.next();
            if(webUserrole.getId()==selectedRole){
                isExist = true;
                break;
            }
        }
        
        return isExist;
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

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
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
