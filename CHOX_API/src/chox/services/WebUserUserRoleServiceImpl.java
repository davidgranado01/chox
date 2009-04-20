package chox.services;

import chox.model.WebUser;
import chox.model.WebUserRole;
import chox.model.WebUserUserRole;
import chox.services.UserService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class WebUserUserRoleServiceImpl extends SecureDataService implements WebUserUserRoleService {

    public WebUserUserRoleServiceImpl() {
    }
    
    protected UserService userService;
    protected WebUserUserRoleService webUserUserRoleService;
    
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
    
    public void setWebUserRoleService(WebUserUserRoleService webUserUserRoleService)
    {
        this.webUserUserRoleService = webUserUserRoleService;
    }
    
    public WebUserUserRole getObject(int id) {        
        return (WebUserUserRole) get(WebUserUserRole.class, id);
    }
    
    public List<WebUserUserRole> getUserRoleMapping(Integer webUserId, Integer webUserRoleId){
        
        List<WebUserUserRole> webUserUserRoles = new ArrayList<WebUserUserRole>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUserUserRole.class);  
            
            if(webUserId!=null && webUserId>0){
                criteria.add(Restrictions.eq("webUser.id", webUserId));
            }
            
            if(webUserRoleId!=null && webUserRoleId>0){
                criteria.add(Restrictions.eq("webUserRole.id", webUserRoleId));
            }            
            
            webUserUserRoles = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return webUserUserRoles;
    }
    
    public void updateObject(WebUserUserRole object) {
        try {
            save(object);
        } catch (Throwable e) {
           e.printStackTrace();
        }
    }
    
    public boolean addNewUserRole(int webUserId, int webUserRoleId){
        
        boolean bFlag = false;
        
        try {
            
            WebUserUserRole webUserUserRole = new WebUserUserRole();
            webUserUserRole.setActive(true);
            webUserUserRole.setWebUser(userService.getObject(webUserId));
            webUserUserRole.setWebUserRole(getWebUserRole(webUserRoleId));
            updateObject(webUserUserRole);
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();  
        }
        
        return bFlag;

    }
    
    public boolean addNewUserRole(int webUserId, String roleName){
        
        boolean bFlag = false;
        
        try {
            
            WebUserUserRole webUserUserRole = new WebUserUserRole();
            webUserUserRole.setActive(true);
            webUserUserRole.setWebUser(userService.getObject(webUserId));
            webUserUserRole.setWebUserRole(getWebUserRole(roleName));

            updateObject(webUserUserRole);
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
           
        }         
        
        return bFlag;

    }
    
    public boolean DeleteObject(WebUserUserRole object){
        
        boolean bFlag = false;
        
        try {
            
            
            delete(object);
            
            bFlag = true;
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        return bFlag;
    }
    
    private WebUserRole getWebUserRole(String roleName){
        
        WebUserRole webUserRole = new WebUserRole();
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);  
        criteria.add(Restrictions.eq("name", roleName));
        webUserRole = (WebUserRole) getByCriteria(criteria);
        
        return webUserRole;
    }
    
    private WebUserRole getWebUserRole(int webUserRoleId){
        
        WebUserRole webUserRole = new WebUserRole();
        
        DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class);  
        criteria.add(Restrictions.eq("id", webUserRoleId));
        webUserRole = (WebUserRole) getByCriteria(criteria);
        
        return webUserRole;
    } 
    
    public List<WebUserRole> getWebUserroles(int orgTypeId){
    
        List<WebUserRole> webUserRoles = new ArrayList<WebUserRole>();
        
        try {
            
            DetachedCriteria criteria = DetachedCriteria.forClass(WebUserRole.class); 
            
            if(orgTypeId==1){
                criteria.add(Restrictions.like("name", "ROLE_CHOX_%"));
            }else if(orgTypeId==2){
                criteria.add(Restrictions.like("name", WebUserRole.ROLE_INS+"_%"));
            }else if(orgTypeId==3){
                criteria.add(Restrictions.like("name", WebUserRole.ROLE_CHO+"_%"));
            }
            
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHO));
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_INS));
            criteria.add(Restrictions.ne("name", WebUserRole.ROLE_CHOX));
            webUserRoles = findByCriteria(criteria);
        
        } catch (Throwable e) {
           e.printStackTrace();
        }    
        
        return webUserRoles;
        
    }
}
