package chox.web.actions;

import chox.services.WebUserUserRoleService;
import java.util.ArrayList;
import java.util.List;
    
public class WebUserroleDropDownAction extends BaseAction{

    private String orgTypeId;
    private List userroleList = null;
    private WebUserUserRoleService service;
    
    public void setWebUserUserRoleService(WebUserUserRoleService service)
    {
        this.service = service;
    }
    
    @Override
    public String execute() throws Exception {
 
        if (getOrgTypeId() != null && !getOrgTypeId().equals("")) {
            getUserroleList(getOrgTypeId());
            return SUCCESS;
        } else {
            return SUCCESS;
        }
    }
    
    private void getUserroleList(String id) {
        
        this.userroleList = new ArrayList();
        this.userroleList = service.getWebUserrolesLookupItem(Integer.valueOf(id));
        
        System.out.println("WebUserroleDropDownAction > getUserroleList" + userroleList.size());
    }

    public String getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(String orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public List getUserroleList() {
        return userroleList;
    }

    public void setUserroleList(List userroleList) {
        this.userroleList = userroleList;
    }

}

