package idas.chox.web.actions;

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

    private List<UserViewData> users = new ArrayList<UserViewData>();
    private UserService userService;
    private int organisationTypeId = -1;
    private int organisationId = -1;
    private int userRoleId = -1;

    // <editor-fold defaultstate="collapsed" desc="GRID VIEW HOLDER">
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.users);
        return "{totalCount:" + this.users.size() + ",results:" + jObject.toString() + "}";
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute() {

        List<WebUser> userData = this.userService.getUsers(organisationId, organisationTypeId, userRoleId);

        for (WebUser h : userData) {
            if (userRoleId > 0) {
                if (isSelectedRoleExist(h.getRoles(), userRoleId)) {
                    users.add(new UserViewData(h));
                }
            } else {
                users.add(new UserViewData(h));
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

    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public int getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        this.organisationId = organisationId;
    }

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    // </editor-fold>

}