/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.UserWorkgroup;

/**
 *
 * @author Carlson
 */
public class UserWorkgroupViewData {

    private int id;
    private String name;
    private String createdBy;
    private String createdDate;
    
    public UserWorkgroupViewData(UserWorkgroup object) {
        
        this.id = object.getId();
        this.name = object.getWorkgroup().getName();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
