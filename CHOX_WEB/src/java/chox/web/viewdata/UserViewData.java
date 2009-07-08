/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.WebUser;
import chox.model.WebUserRole;
import java.util.Iterator;
import java.util.Set;

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
    private String role;
    
    public UserViewData(WebUser object) {
        
        this.id = object.getId();
        this.email = object.getEmail();
        this.name = object.getDisplayName();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());   
        this.status = object.getStatus();
        
        if(object.getStatus()){
            this.statusDesc = "Yes";
        }else{
            this.statusDesc = "No";
        }
    
        if(object.isCHOXAdmin()){
            
            this.orgType = 1;
            this.orgName = "Sherwood";
            
        }else{
            
            if(object.getInsurer()!=null){
                this.orgType = 2;
            }else{
                this.orgType = 3;
            }
            
            this.orgName = object.getOrganisationName();
            
        }
        
        this.role = getRoleString(object.getRoles());
    }
    
    private String getRoleString(Set roles){
        
        Integer iRoles = roles.size();
        String sRole = "";
        
        if(iRoles>0){
            
            Iterator it = roles.iterator();
            
            while (it.hasNext()) {
                
                WebUserRole webUserrole = (WebUserRole) it.next();
                
                if(!webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHO)
                        && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)
                        && !webUserrole.getName().equalsIgnoreCase(WebUserRole.ROLE_INS)){
                    
                    sRole = sRole + webUserrole.getDescription() + ", ";
                }
            }
        }
        
        if((sRole.trim()).length()<=0){
            sRole = "N/A";
        }else{
            sRole = sRole.substring(0, (sRole.length()-2));
        }
        
        return sRole;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    
}
