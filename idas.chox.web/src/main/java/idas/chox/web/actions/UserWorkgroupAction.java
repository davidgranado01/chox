/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.WebUserWorkgroup;
import idas.chox.core.services.UserWorkgroupService;
import idas.chox.web.viewdata.UserWorkgroupViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserWorkgroupAction extends BaseAction {

    private List<UserWorkgroupViewData> userworkgroups;
    private UserWorkgroupService service;
    private int webUserId;

    public void setUserWorkgroupService(UserWorkgroupService service) {
        this.service = service;
    }

    public List<UserWorkgroupViewData> getUserworkgroups() {
        return userworkgroups;
    }

    public void setUserworkgroups(List<UserWorkgroupViewData> userworkgroups) {
        this.userworkgroups = userworkgroups;
    }

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.userworkgroups);
        return "{totalCount:" + this.userworkgroups.size() + ",results:" + jObject.toString() + "}";
    }

    @Override
    public String execute() {

        List<WebUserWorkgroup> userworkgroupData = this.service.getUserWorkgroupsByUser(webUserId);

        userworkgroups = new ArrayList<UserWorkgroupViewData>();

        for (WebUserWorkgroup h : userworkgroupData) {
            userworkgroups.add(new UserWorkgroupViewData(h));
        }

        return SUCCESS;
    }
}
