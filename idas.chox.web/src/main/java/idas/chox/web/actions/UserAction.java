package idas.chox.web.actions;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.UserService;
import idas.chox.web.viewdata.UserViewData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONArray;

public class UserAction extends BaseAction {

    private List<UserViewData> user;
    private UserService userService;
    private int orgTypeId = -1;
    private int orgId;
    private int userRoleId = -1;

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.user);
        return "{totalCount:" + this.user.size() + ",results:" + jObject.toString() + "}";
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute() {

        String orgType = "";
        if (orgTypeId == 1) {
            orgType = OrganisationType.CHOX;
        } else if (orgTypeId == 2) {
            orgType = OrganisationType.INS;
        } else if (orgTypeId == 3) {
            orgType = OrganisationType.CHO;
        }

        List<WebUser> userData = this.userService.getUsers(orgId, orgType);

        user = new ArrayList<UserViewData>();

        for (WebUser h : userData) {

            if (userRoleId > 0) {
                if (isSelectedRoleExist(h.getRoles(), userRoleId)) {
                    user.add(new UserViewData(h));
                }
            } else {
                user.add(new UserViewData(h));
            }
        }

        return SUCCESS;
    }

    private boolean isSelectedRoleExist(Set roles, int selectedRole) {
        boolean isExist = false;

        Iterator it = roles.iterator();

        while (it.hasNext()) {
            WebUserRole webUserrole = (WebUserRole) it.next();
            if (webUserrole.getId() == selectedRole) {
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
}
