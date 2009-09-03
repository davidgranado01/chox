/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.UserWorkgroup;
import chox.services.UserWorkgroupService;
import chox.web.viewdata.UserWorkgroupViewData;
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

        List<UserWorkgroup> userworkgroupData = this.service.getObjects(webUserId);

        userworkgroups = new ArrayList<UserWorkgroupViewData>();
        
        for(UserWorkgroup h : userworkgroupData)
        {
            userworkgroups.add(new UserWorkgroupViewData(h));
        }
        
        return SUCCESS;
    }     
}
