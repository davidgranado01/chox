/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.WebUser;

public class UserViewData {
    
    private int id;
    private String email;
    private String name;
    private boolean status;
    private String statusDesc;
    private String createdBy;
    private String createdDate;
    private String orgName;
    private int orgType;
    
    public UserViewData(WebUser object) {
        
        this.id = object.getId();
        this.email = object.getEmail();
        this.name = object.getDisplayName();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());   
        
        if(object.getStatus()>=1){
            this.status = true;
            this.statusDesc = "Active";
        }else{
            this.status = false;
            this.statusDesc = "Inactive";
        }
    
        if(object.isCHOXAdmin()){
            
            this.orgType = 1;
            this.orgName = "CHOX";
            
        }else{
            
            if(object.getInsurer()!=null){
                this.orgType = 2;
            }else{
                this.orgType = 3;
            }
            
            this.orgName = object.getOrganisationName();
            
        }
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getOrgName() {
        return orgName;
    }

    public boolean isStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }


    
}
