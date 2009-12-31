package idas.chox.web.actions;

import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.web.viewdata.UserroleViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserroleAction extends BaseAction {

    private List<UserroleViewData> userroles;
    private WebUserUserRoleService webUserUserRoleService;
    private int webUserId;

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.userroles);
        return "{totalCount:" + this.userroles.size() + ",results:" + jObject.toString() + "}";
    }

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    @Override
    public String execute() {

        try {

            userroles = new ArrayList<UserroleViewData>();

            List<WebUserUserRole> userroleData = this.webUserUserRoleService.getMappedUserRole(webUserId);

            for (WebUserUserRole h : userroleData) {
                if (!h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHO) && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_INS) && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)) {
                    userroles.add(new UserroleViewData(h));
                }
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }
}
