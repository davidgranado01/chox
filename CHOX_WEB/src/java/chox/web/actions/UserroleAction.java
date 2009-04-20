package chox.web.actions;

import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.model.WebUserUserRole;
import chox.services.WebUserUserRoleService;
import chox.web.viewdata.UserroleViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserroleAction extends BaseAction {

    private List<UserroleViewData> userrole;
    private WebUserUserRoleService service;
    private int webUserId;

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.userrole);
        return "{totalCount:" + this.userrole.size() + ",results:" + jObject.toString() + "}";
    }

    public void setWebUserUserRoleService(WebUserUserRoleService service)
    {
        this.service = service;
    }
   
    @Override
    public String execute() {

        List<WebUserUserRole> userroleData = this.service.getUserRoleMapping(webUserId, null);

        userrole = new ArrayList<UserroleViewData>();
        
        for(WebUserUserRole h : userroleData)
        {
            
            if(!h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHO) 
                    && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_INS)
                    && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)){
                
                userrole.add(new UserroleViewData(h));
            }
        }
        
        return SUCCESS;
    } 
    

}
