package chox.web.actions;

import chox.model.WebUser;
import chox.services.UserService;
import chox.web.viewdata.UserViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserAction extends BaseAction {

    private List<UserViewData> user;
    private UserService service;
    private int start;
    private int limit;
    private String orgType;
    
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

        List<WebUser> userData = this.service.getUsers();
        
        user = new ArrayList<UserViewData>();
        
        for(WebUser h : userData)
        {
            if(h.getOrganisationType()==(Integer.valueOf(orgType))){
                user.add(new UserViewData(h));
            }
        }
        
        return SUCCESS;
    }

    public int getLimit() {
        return limit;
    }

    public int getStart() {
        return start;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }
    
}
